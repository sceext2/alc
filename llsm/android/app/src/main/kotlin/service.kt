package org.sceext.llsm

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

import android.support.v4.app.NotificationCompat


const val SM_SERVICE_ID: Int = 1001

class SmService : Service() {
    var _running: Boolean = false

    lateinit var _core_thread: VideoThread
    lateinit var _t: Thread


    override fun onCreate() {
        // create thread
        _core_thread = VideoThread(this)
        _t = Thread(_core_thread)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (_running) {
            // WARNING
            toast("ERROR: service already running")
            return START_NOT_STICKY
        }

        _show_notification()
        // start core thread
        _t.start()

        _running = true
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null  // not allow bind
    }

    override fun onDestroy() {
        _remove_notification()

        _running = false
    }

    fun _show_notification() {
        val sc = get_app_context().sconfig
        val server = "${sc.server_ip}:${sc.server_port}"

        // create notification
        val intent = Intent(this, MainActivity::class.java)
        val p = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val b = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.llsm_logo_grey)
            .setContentTitle("LLSM")
            .setContentText("Server ${server}")
            .setContentIntent(p)
        val notification = b.build()

        startForeground(SM_SERVICE_ID, notification)
    }

    fun _remove_notification() {
        stopForeground(true)
    }
}
