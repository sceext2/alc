package org.sceext.llsm

import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager

import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.JavaScriptModule
import com.facebook.react.uimanager.ViewManager


const val MODULE_NAME: String = "sm_native"

class SmNative : ReactContextBaseJavaModule {
    // TODO

    constructor(context: ReactApplicationContext): super(context) {
        // TODO
    }

    override fun getName(): String {
        return MODULE_NAME
    }

    override fun getConstants(): Map<String, Any> {
        // TODO
        return mapOf()
    }

    @ReactMethod
    fun start_sm(config_json: String, promise: Promise) {
        var c: Sconfig
        // parse config
        try {
            val j = parse_json(config_json)
            c = parse_config(j)
        } catch (e: Exception) {
            // DEBUG
            e.printStackTrace()

            toast("ERROR: bad config_json: " + config_json)

            promise.reject("bad config_json", Exception("bad config_json"))
            return
        }
        // save config
        get_app_context().sconfig = c

        // SM step 1: Ask user by MediaProjectionManager
        val m: MediaProjectionManager = get_app_context().getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val i = m.createScreenCaptureIntent()
        get_app_context().main_activity!!.startActivityForResult(i, MEDIA_PROJECTION_REQUEST_CODE)

        // OK callback
        promise.resolve(null)
    }

    @ReactMethod
    fun stop_sm(promise: Promise) {
        // TODO
    }

    fun getContext(): Context {
        return reactApplicationContext
    }
}


class SmPackage : ReactPackage {

    // TODO
    //override fun createJSModules(): List<Class<?: JavaScriptModule>>> {
    //    return emptyList()
    //}

    override fun createViewManagers(context: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }

    override fun createNativeModules(context: ReactApplicationContext): List<NativeModule> {
        return listOf(SmNative(context))
    }
}
