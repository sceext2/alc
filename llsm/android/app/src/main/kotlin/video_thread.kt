package org.sceext.llsm

import java.net.Socket
import java.net.InetSocketAddress

import android.content.Intent

// TODO


class VideoThread(val service: SmService) : Runnable {

    lateinit var s: Socket


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

        // TODO
        toast("DEBUG: VideoThread will exit after 10s for test")

        Thread.sleep(10000)
    }
}
