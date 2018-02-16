package org.sceext.llsm_server

import java.net.Socket
import java.net.ServerSocket
import java.net.InetAddress
import java.io.InputStream

import com.beust.klaxon.Parser
import com.beust.klaxon.JsonObject


fun parse_json(text: String): JsonObject {
    val b = StringBuilder(text)
    val parser = Parser()
    return parser.parse(b) as JsonObject
}


class SocketThread(val s: Socket) : Runnable {

    var running: Boolean = false
    var codec: Codec? = null

    fun _debug_str(): String {
        return "${s.inetAddress.toString()}:${s.port}"
    }

    override fun run() {
        // DEBUG
        println("DEBUG: accept connection from ${_debug_str()}")

        try {
            running = true
            recv_loop()
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            println("DEBUG: connection ${_debug_str()} closed")
            running = false
            // free codec
            codec?.free()
        }
    }

    fun recv_loop() {
        s.setTcpNoDelay(true)

        // recv reader, use the simple data block protocol
        val reader = BlockReader(s.inputStream)
        // first block is config data, print out for DEBUG
        val b = reader.read()
        if (b == null) {
            return
        }
        println("DEBUG: ${_debug_str()} config: ${String(b)}")
        // create codec
        val config = parse_json(String(b))
        val screen_size_x = config.int("screen_size_x")!!
        val screen_size_y = config.int("screen_size_y")!!
        println("DEBUG: create codec with size ${screen_size_x} x ${screen_size_y}")
        codec = Codec(screen_size_x, screen_size_y)

        // FIXME try to feed first 2 block to codec
        val bx = reader.read()
        val by = reader.read()
        if ((bx == null) or (by == null)) {
            return
        }
        val bb = bx!! + by!!
        println("DEBUG: feed bb (${bb.size}) = bx (${bx.size}) + by (${by.size})")
        codec!!.feed(bb)

        while (running) {
            val b = reader.read()
            if (b == null) {
                break  // EoS
            }
            // print out number of bytes recved
            println("DEBUG: ${_debug_str()}  got ${b.size} Bytes data")
            // feed data to codec
            codec!!.feed(b)
        }
    }
}


// command line: server.jar IP port
fun main(args: Array<String>) {
    val ip = args[0].trim()
    val port = args[1].toInt()

    // init ffmpeg
    codec_init()

    // listen: init server
    val addr = InetAddress.getByName(ip)
    val ss = ServerSocket(port, 16, addr)
    println("DEBUG: listen at ${ss.inetAddress.toString()}:${ss.localPort}")

    // accept incomming connections
    while (true) {
        val s = ss.accept()
        // create new thread
        val t = Thread(SocketThread(s))
        t.start()
    }
}
