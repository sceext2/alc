package org.sceext.llsm_server

import org.bytedeco.javacpp.avcodec
import org.bytedeco.javacpp.avformat
import org.bytedeco.javacpp.avutil
import org.bytedeco.javacpp.swscale


// use ffmpeg to decode raw h264

fun codec_init() {
    avformat.av_register_all()
}


class Codec {
    // TODO
}
