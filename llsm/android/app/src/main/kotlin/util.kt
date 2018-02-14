package org.sceext.llsm

import android.widget.Toast


// show one toast on Android
fun toast(text: String) {
    Toast.makeText(get_app_context(), text, Toast.LENGTH_SHORT).show()
}
