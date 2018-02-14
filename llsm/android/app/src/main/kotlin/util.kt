package org.sceext.llsm

import android.widget.Toast

import com.beust.klaxon.Parser
import com.beust.klaxon.JsonObject


// show one toast on Android
fun toast(text: String) {
    Toast.makeText(get_app_context(), text, Toast.LENGTH_SHORT).show()
}


fun parse_json(text: String): JsonObject {
    val b = StringBuilder(text)
    val parser = Parser()
    return parser.parse(b) as JsonObject
}
