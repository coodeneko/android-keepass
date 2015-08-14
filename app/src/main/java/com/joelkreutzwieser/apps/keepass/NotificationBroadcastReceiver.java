package com.joelkreutzwieser.apps.keepass;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.joelkreutzwieser.apps.keepass.keepass.domain.Property;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent closingIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closingIntent);

        String action = intent.getAction();
        String data = intent.getStringExtra("DATA");

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(action, data);
        clipboard.setPrimaryClip(clip);

        if (action.equals("com.joelkreutzwieser.apps.keepass.COPY_ACTION")) {
            Toast.makeText(context, "User Copied to Clipboard", Toast.LENGTH_LONG).show();
        } else if (action.equals("com.joelkreutzwieser.apps.keepass.PASS_ACTION")) {
            Toast.makeText(context, "Password Copied to Clipboard", Toast.LENGTH_LONG).show();
        }

        Intent clearClipboardIntent = new Intent(context, ClipboardClearService.class);
        context.startService(clearClipboardIntent);
    }
}
