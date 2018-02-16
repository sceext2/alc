package org.sceext.llsm

import java.net.Socket
import java.net.InetSocketAddress
import java.io.OutputStream
import java.nio.ByteBuffer

import android.content.Intent
import android.media.projection.MediaProjection
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.view.Surface
import android.hardware.display.VirtualDisplay
import android.hardware.display.DisplayManager


const val VIRTUAL_DISPLAY_NAME: String = "llsm-display"
const val VIRTUAL_DISPLAY_DPI: Int = 96

class VideoThread(val service: SmService) : Runnable {

    var running: Boolean = false  // exit flag

    lateinit var s: Socket
    lateinit var o: OutputStream

    lateinit var codec: MediaCodec
    lateinit var surface: Surface
    lateinit var display: VirtualDisplay


    override fun run() {
        try {
            _run()
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            _stop_service()
        }
    }

    fun _stop_service() {
        val c = get_app_context()
        c.stopService(Intent(c, SmService::class.java))

        // DEBUG
        toast("DEBUG: VideoThread stopped")
    }

    fun _run() {
        val sconfig = get_app_context().sconfig
        // connect to server and start service
        val socket = Socket()
        try {
            val addr = InetSocketAddress(sconfig.server_ip, sconfig.server_port)
            socket.connect(addr)
        } catch (e: Exception) {
            // DEBUG
            e.printStackTrace()

            toast("ERROR: can not connect to server (${e})")
            return
        }
        s = socket  // save socket
        // turn-off TCP delay
        s.setTcpNoDelay(true)
        // get OutputStream
        o = s.outputStream
        try {
            _init_sm()
            // TODO send json data to TCP
            o.write(_int_to_bytes(0))
            o.flush()

            running = true
            _sm_loop()
            // TODO clean-up
        } finally {
            s.close()  // close connection to server
        }
    }

    fun _init_sm() {
        val sconfig = get_app_context().sconfig
        val mp = get_app_context().media_projection

        // use h264 codec
        val format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC,
            sconfig.screen_size_x, sconfig.screen_size_y)
        format.setInteger(MediaFormat.KEY_BITRATE_MODE,
            MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CBR)  // Constant bitrate mode
        format.setInteger(MediaFormat.KEY_BIT_RATE, 20_000_000)  // 20 Mbps
        format.setInteger(MediaFormat.KEY_CAPTURE_RATE, sconfig.fps)
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
            MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
        format.setInteger(MediaFormat.KEY_FRAME_RATE, sconfig.fps)
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1)  // 1s per I-frame
        format.setInteger(MediaFormat.KEY_PRIORITY, 0)  // realtime
        // FIXME not support profile
        //format.setInteger(MediaFormat.KEY_PROFILE,
        //    MediaCodecInfo.CodecProfileLevel.AVCProfileBaseline)  // use baseline profile

        // init MediaCodec
        codec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
        try {
            codec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        } catch (e: MediaCodec.CodecException) {
            // DEBUG
            System.err.println("ERROR: diagnosticInfo [${e.diagnosticInfo}], errorCode: ${e.errorCode}, isRecoverable: ${e.isRecoverable()}, isTransient: ${e.isTransient()}")

            throw Exception("configure codec error", e)
        }

        surface = codec.createInputSurface()
        codec.start()
        // create VirtualDisplay
        display = mp.createVirtualDisplay(VIRTUAL_DISPLAY_NAME,
            sconfig.screen_size_x, sconfig.screen_size_y, VIRTUAL_DISPLAY_DPI,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, surface, null, null)
    }

    fun _int_to_bytes(i: Int): ByteArray {
        return ByteBuffer.allocate(4).putInt(i).array()
    }

    fun _sm_loop() {
        val info = MediaCodec.BufferInfo()
        while (running) {
            val i = codec.dequeueOutputBuffer(info, -1)  // wait infinite
            if (i < 0) {
                continue  // TODO just ignore here
            }
            val buffer = codec.getOutputBuffer(i)
            try {
                _send_data(buffer)
            } finally {
                codec.releaseOutputBuffer(i, System.nanoTime())
            }
        }
    }

    fun _send_data(data: ByteBuffer) {
        val len = data.remaining()
        // send length
        o.write(_int_to_bytes(len))

        val bytes = ByteArray(len)
        data.get(bytes)
        o.write(bytes)
        o.flush()
    }
}
