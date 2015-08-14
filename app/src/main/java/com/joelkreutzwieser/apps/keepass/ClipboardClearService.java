package com.joelkreutzwieser.apps.keepass;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ClipboardClearService extends Service {

    private Handler handler;
    private Runnable runnable;

    @Override
    public void onCreate() {
        handler = new Handler();
        Log.v("KEEPASS", "CREATE");

        runnable = new Runnable() {
            @Override
            public void run() {
                Log.v("KEEPASS", "RUNNER");
                ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", "");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Clearing Clipboard Service Done", Toast.LENGTH_SHORT).show();
            }
        };

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("KEEPASS", "COMMAND");
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 30);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
