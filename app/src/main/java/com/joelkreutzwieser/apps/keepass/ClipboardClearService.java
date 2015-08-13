package com.joelkreutzwieser.apps.keepass;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class ClipboardClearService extends Service{

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Clearing Clipboard Service Done (NOT ACTUALLY CLEARED)", Toast.LENGTH_SHORT).show();
    }
}
