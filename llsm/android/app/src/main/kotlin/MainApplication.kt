package org.sceext.llsm


import java.util.Arrays

import android.app.Application
import android.content.Intent
import android.media.projection.MediaProjection

import com.facebook.react.ReactApplication
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.shell.MainReactPackage
import com.facebook.soloader.SoLoader


// save MainApplication instance
var app_context: MainApplication? = null

fun get_app_context(): MainApplication = app_context!!


class MainApplication : Application(), ReactApplication {

    var main_activity: MainActivity? = null

    lateinit var media_projection: MediaProjection
    lateinit var sconfig: Sconfig


    private val mReactNativeHost = object: ReactNativeHost(this) {
        override fun getUseDeveloperSupport(): Boolean {
            return BuildConfig.DEBUG
        }

        override fun getPackages(): List<ReactPackage> {
            return Arrays.asList(
                MainReactPackage(),
                SmPackage()
            )
        }

        override fun getJSMainModuleName(): String {
            return "index"
        }
    }

    override fun getReactNativeHost(): ReactNativeHost {
        return mReactNativeHost
    }

    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, /* native exopackage */ false)

        app_context = this
    }

    fun init_sm() {
        _start_service()
    }

    fun _start_service() {
        startService(Intent(this, SmService::class.java))
    }
}
