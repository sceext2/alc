package org.sceext.llsm

import com.beust.klaxon.JsonObject
import com.beust.klaxon.JsonArray


data class Sconfig(
    val server_ip: String,
    val server_port: Int,
    val screen_size_x: Int,
    val screen_size_y: Int,
    val fps: Int
    )

fun parse_config(raw: JsonObject): Sconfig {
    val server_ip = raw.string("server_ip")!!
    val server_port = raw.int("server_port")!!
    val screen_size: JsonArray<Int> = raw.array("screen_size")!!
    val x = screen_size[0]
    val y = screen_size[1]
    val fps = raw.int("fps")!!

    return Sconfig(server_ip, server_port, x, y, fps)
}
