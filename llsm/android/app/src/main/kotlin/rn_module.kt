package org.sceext.llsm

import android.content.Context

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
        // TODO
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
