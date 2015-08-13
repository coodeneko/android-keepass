package com.joelkreutzwieser.apps.keepass;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class ClipboardClearService extends Service {

    private Handler handler;
    private Runnable runnable;

    @Override
    public void onCreate() {
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", "");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Clearing Clipboard Service Done (NOT ACTUALLY CLEARED)", Toast.LENGTH_SHORT).show();
            }
        };

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 30);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
