package org.sceext.llsm_server


class Decoder(val port: Int) : Runnable {
    var codec: Codec? = null

    override fun run() {
        // gen URL
        val url = "tcp://127.0.0.1:${port}?tcp_nodelay=1&recv_buffer_size=4096"
        try {
            println("DEBUG: create codec on ${url}")
            codec = Codec(url)
            while (codec!!.one()) {
            }
        } finally {
            codec?.free()
        }
    }
}
