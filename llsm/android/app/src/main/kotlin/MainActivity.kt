package org.sceext.llsm

import android.os.Bundle
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.media.projection.MediaProjection

import com.facebook.react.ReactActivity


const val MEDIA_PROJECTION_REQUEST_CODE: Int = 1000

class MainActivity : ReactActivity() {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    override fun getMainComponentName(): String? {
        return "llsm_main"
    }

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)

        // save single instance ref
        get_app_context().main_activity = this
    }

    override fun onDestroy() {
        get_app_context().main_activity = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            MEDIA_PROJECTION_REQUEST_CODE -> {
                if (resultCode != RESULT_OK) {
                    toast("ERROR: user canceled (resultCode = ${resultCode})")
                } else {
                    // SM step 2: get MediaProjection now
                    val m: MediaProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

                    val p = m.getMediaProjection(resultCode, data)
                    if (p == null) {
                        toast("ERROR: got null MediaProjection")
                    } else {
                        get_app_context().media_projection = p
                        get_app_context().init_sm()
                    }
                }
            }
        }
    }
}
