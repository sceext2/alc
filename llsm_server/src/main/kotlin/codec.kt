package org.sceext.llsm_server

import org.bytedeco.javacpp.avformat
import org.bytedeco.javacpp.avutil
import org.bytedeco.javacpp.swscale
import org.bytedeco.javacpp.avcodec
import org.bytedeco.javacpp.avcodec.AV_CODEC_ID_H264
import org.bytedeco.javacpp.avcodec.AV_INPUT_BUFFER_PADDING_SIZE
import org.bytedeco.javacpp.avcodec.AVCodec
import org.bytedeco.javacpp.avcodec.AVCodecContext
import org.bytedeco.javacpp.avcodec.AVPacket
import org.bytedeco.javacpp.avcodec.avcodec_find_decoder
import org.bytedeco.javacpp.avcodec.avcodec_alloc_context3
import org.bytedeco.javacpp.avcodec.avcodec_open2
import org.bytedeco.javacpp.avcodec.avcodec_close
import org.bytedeco.javacpp.avcodec.avcodec_free_context
import org.bytedeco.javacpp.avcodec.avcodec_send_packet
import org.bytedeco.javacpp.avcodec.avcodec_receive_frame
import org.bytedeco.javacpp.avcodec.av_packet_alloc
import org.bytedeco.javacpp.avcodec.av_packet_free
import org.bytedeco.javacpp.avcodec.av_packet_from_data
import org.bytedeco.javacpp.avcodec.av_packet_unref
import org.bytedeco.javacpp.avutil.AVFrame
import org.bytedeco.javacpp.avutil.av_frame_alloc
import org.bytedeco.javacpp.avutil.av_free
import org.bytedeco.javacpp.PointerPointer
import org.bytedeco.javacpp.Pointer


fun codec_init() {
    avformat.av_register_all()
}


// use ffmpeg to decode raw h264
class Codec(val size_x: Int, size_y: Int) {
    var p_codec: AVCodec
    var p_codec_ctx: AVCodecContext
    var p_frame: AVFrame

    init {
        p_codec = avcodec_find_decoder(AV_CODEC_ID_H264)
        p_codec_ctx = avcodec_alloc_context3(p_codec)
        // open codec
        val i = avcodec_open2(p_codec_ctx, p_codec, PointerPointer<Pointer>())
        if (i < 0) {
            throw Exception("avcodec_open2() return ${i}")
        }
        p_frame = av_frame_alloc()
    }

    // feed raw h264 data
    fun feed(data: ByteArray) {
        // add padding bytes
        val buffer = data.copyOf(data.size + AV_INPUT_BUFFER_PADDING_SIZE)
        // create a packet and put data in it
        val packet = av_packet_alloc()
        av_packet_from_data(packet, buffer, data.size)

        // FIMXE no output now
        // do decode
        val i = avcodec_send_packet(p_codec_ctx, packet)
        // TODO check return code
        while (true) {
            val r = avcodec_receive_frame(p_codec_ctx, p_frame)
            // TODO check return code
            if (r != 0) {
                // DEBUG
                if (r != -11) {
                    println("DEBUG: avcodec_receive_frame() return ${r}")
                }
                break
            }
            _got_one_frame()
        }
        // tested, no memory leaks
    }

    fun _got_one_frame() {
        // TODO
        println("DEBUG: Codec: got one frame !")
    }

    fun free() {
        av_free(p_frame)

        avcodec_close(p_codec_ctx)
        avcodec_free_context(p_codec_ctx)  // for avcodec_alloc_context3()
    }
}
