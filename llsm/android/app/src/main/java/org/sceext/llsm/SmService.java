package org.sceext.llsm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class SmService extends Service {
    // TODO

    @Override
    public void onCreate() {
        // TODO
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;  // not allow bind
    }

    @Override
    public void onDestroy() {
        // TODO
    }
}
