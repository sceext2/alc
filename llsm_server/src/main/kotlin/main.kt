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

    fun _debug_str(): String {
        return "${s.inetAddress.toString()}:${s.port}"
    }

    override fun run() {
        // DEBUG
        println("DEBUG: accept connection from ${_debug_str()}")

        try {
            recv_loop()
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            println("DEBUG: connection ${_debug_str()} closed")
        }
    }

    fun recv_loop() {
        s.setTcpNoDelay(true)

        val i = s.inputStream
        // recv buffer, size 1 MB
        val b = ByteArray(1024 * 1024)
        while (true) {
            val r = i.read(b)
            if (r == -1) {
                println("DEBUG: ${_debug_str()}  ${r}")
                break
            }
            // just print out number of bytes recved
            println("DEBUG: ${_debug_str()}  got ${r} Bytes data")
        }
    }
}


// command line: server.jar IP port
fun main(args: Array<String>) {
    val ip = args[0].trim()
    val port = args[1].toInt()

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
