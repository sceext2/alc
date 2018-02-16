package org.sceext.llsm_server

import org.bytedeco.javacpp.avformat
import org.bytedeco.javacpp.avformat.AVFormatContext
import org.bytedeco.javacpp.avformat.avformat_open_input
import org.bytedeco.javacpp.avformat.avformat_find_stream_info
import org.bytedeco.javacpp.avformat.avformat_close_input
import org.bytedeco.javacpp.avformat.av_read_frame
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
import org.bytedeco.javacpp.avutil
import org.bytedeco.javacpp.avutil.AV_PIX_FMT_RGB24
import org.bytedeco.javacpp.avutil.AVFrame
import org.bytedeco.javacpp.avutil.AVDictionary
import org.bytedeco.javacpp.avutil.av_frame_alloc
import org.bytedeco.javacpp.avutil.av_free
import org.bytedeco.javacpp.avutil.av_malloc
import org.bytedeco.javacpp.avutil.av_image_get_buffer_size
import org.bytedeco.javacpp.avutil.av_image_fill_arrays
import org.bytedeco.javacpp.swscale
import org.bytedeco.javacpp.swscale.SWS_BILINEAR
import org.bytedeco.javacpp.swscale.SwsContext
import org.bytedeco.javacpp.swscale.sws_getContext
import org.bytedeco.javacpp.swscale.sws_scale
import org.bytedeco.javacpp.PointerPointer
import org.bytedeco.javacpp.Pointer
import org.bytedeco.javacpp.BytePointer
import org.bytedeco.javacpp.DoublePointer


fun codec_init() {
    avformat.av_register_all()
}


interface CodecCallback {
    // callback one frame with RGB24 format
    fun on_one_frame(size_x: Int, size_y: Int, data: ByteArray)
}

// codec with ffmpeg
class Codec(val url: String, val callback: CodecCallback) {
    var video_stream = 0  // use first stream

    var p_format_ctx: AVFormatContext
    var p_codec: AVCodec
    var p_codec_ctx: AVCodecContext
    var p_frame: AVFrame
    var p_frame_rgb: AVFrame
    var packet: AVPacket
    var buffer: BytePointer
    var sws_ctx: SwsContext
    var buffer_size: Int

    var frame_count: Int = 0

    init {
        // open input video source
        p_format_ctx = AVFormatContext(null)
        avformat_open_input(p_format_ctx, url, null, null)
        avformat_find_stream_info(p_format_ctx, null as PointerPointer<Pointer>?)

        // create codec
        p_codec_ctx = p_format_ctx.streams(video_stream).codec()  // FIXME
        p_codec = avcodec_find_decoder(p_codec_ctx.codec_id())
        // open codec
        val i = avcodec_open2(p_codec_ctx, p_codec, null as AVDictionary?)
        if (i < 0) {
            throw Exception("avcodec_open2() return ${i}")
        }
        p_frame = av_frame_alloc()
        p_frame_rgb = av_frame_alloc()
        packet = AVPacket()
        // alloc RGB24 data buffer
        buffer_size = av_image_get_buffer_size(AV_PIX_FMT_RGB24, p_codec_ctx.width(), p_codec_ctx.height(), 1)
        println("DEBUG: ${p_codec_ctx.width()} x ${p_codec_ctx.height()}  buffer size ${buffer_size}")
        buffer = BytePointer(av_malloc(buffer_size.toLong()))

        sws_ctx = sws_getContext(p_codec_ctx.width(), p_codec_ctx.height(),
            p_codec_ctx.pix_fmt(), p_codec_ctx.width(), p_codec_ctx.height(),
            AV_PIX_FMT_RGB24, SWS_BILINEAR, null, null, null as DoublePointer?)
        // FIXME
        av_image_fill_arrays(p_frame_rgb.data(), p_frame_rgb.linesize(), buffer, AV_PIX_FMT_RGB24, p_codec_ctx.width(), p_codec_ctx.height(), 1)
    }

    // decode one frame
    fun one(): Boolean {
        val i = av_read_frame(p_format_ctx, packet)
        if (i < 0) {
            return false
        }
        if (packet.stream_index() != video_stream) {
            return true  // just ignore it
        }
        // do decode
        var j = avcodec_send_packet(p_codec_ctx, packet)
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
            // convert image to RGB24
            sws_scale(sws_ctx, p_frame.data(), p_frame.linesize(),
                0, p_codec_ctx.height(), p_frame_rgb.data(), p_frame_rgb.linesize())
            _got_one_frame()
        }
        // tested, no memory leaks
        return true
    }

    fun _got_one_frame() {
        frame_count += 1
        // TODO
        println("DEBUG: Codec: got frame ${frame_count}")

        // get bytes from p_frame_rgb
        val data = p_frame_rgb.data(0)
        val b = ByteArray(buffer_size)
        data.position(0).get(b)

        callback.on_one_frame(p_codec_ctx.width(), p_codec_ctx.height(), b)
    }

    fun free() {
        av_free(buffer)
        av_free(p_frame_rgb)
        av_free(p_frame)

        avcodec_close(p_codec_ctx)
        avformat_close_input(p_format_ctx)
    }
}
