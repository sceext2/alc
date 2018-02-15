package org.sceext.llsm_server

import java.io.InputStream
import java.nio.ByteBuffer


// the very simple protocol with llsm over TCP
class BlockReader(val s: InputStream) {

    // read one block, return null if End of Stream
    fun read(): ByteArray? {
        // read first 4 Bytes: length of the data block
        val b = ByteBuffer.allocate(4)
        for (i in 0 until 4) {
            val x = s.read()
            if (x < 0) {
                return null  // -1, EoS
            }
            b.put(x.toByte())
        }
        val len = b.getInt(0)
        // read data block
        val bb = ByteArray(len)
        var rest = len
        while (rest > 0) {
            val i = s.read(bb, len - rest, rest)
            if (i < 0) {
                return null  // EoS
            }
            rest -= i
        }
        return bb
    }
}
