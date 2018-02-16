package org.sceext.llsm_server


class Decoder(val port: Int, val socket_thread: SocketThread) : Runnable {
    var codec: Codec? = null

    override fun run() {
        // gen URL
        val url = "tcp://127.0.0.1:${port}?tcp_nodelay=1&recv_buffer_size=4096"
        try {
            println("DEBUG: create codec on ${url}")
            codec = Codec(url, object: CodecCallback {
                override fun on_one_frame(size_x: Int, size_y: Int, data: ByteArray) {
                    if (main_window != null) {
                        main_window!!.update_frame(size_x, size_y, data)
                    }
                }
            })
            while (codec!!.one()) {
            }
        } finally {
            codec?.free()
        }
    }
}
