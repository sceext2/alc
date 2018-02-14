package org.sceext.llsm

import android.app.Service
import android.content.Intent
import android.os.IBinder


class SmService : Service() {
    // TODO

    override fun onCreate() {
        // TODO
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // TODO
        return START_NOT_STICKY;
    }

    override fun onBind(intent: Intent): IBinder? {
        return null;  // not allow bind
    }

    override fun onDestroy() {
        // TODO
    }
}
