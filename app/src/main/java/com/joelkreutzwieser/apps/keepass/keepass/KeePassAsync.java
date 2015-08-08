package com.joelkreutzwieser.apps.keepass.keepass;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.joelkreutzwieser.apps.keepass.ApplicationBase;
import com.joelkreutzwieser.apps.keepass.groupActivity.GroupViewActivity;
import com.joelkreutzwieser.apps.keepass.R;
import com.joelkreutzwieser.apps.keepass.keepass.domain.KeePassFile;

import java.io.InputStream;

public class KeePassAsync extends AsyncTask<Void, Void, KeePassFile> {

    private InputStream inputStream;
    private String password;
    private String keyfile;
    private ProgressDialog progressDialog;
    private GroupViewActivity groupViewActivity;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        groupViewActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        progressDialog.setMessage("Decrypting and parsing database...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressNumberFormat("");
        progressDialog.show();
        groupViewActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public KeePassAsync(InputStream inputStream, String password, String keyfile, GroupViewActivity groupViewActivity) {
        this.inputStream = inputStream;
        this.password = password;
        this.keyfile = keyfile;
        this.groupViewActivity = groupViewActivity;
        progressDialog = new ProgressDialog(groupViewActivity);
    }

    @Override
    protected KeePassFile doInBackground(Void... params) {
        try {
            return KeePassDatabase.getInstance(inputStream).openDatabase(password, keyfile, progressDialog);
        } catch (Exception e) {
            Log.e("KEEPASS", e.getMessage());
            cancel(true);
            return null;
        }
    }

    @Override
    protected void onPostExecute(KeePassFile keePassFile) {
        super.onPostExecute(keePassFile);
        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        ((ApplicationBase) groupViewActivity.getApplication()).setDatabase(keePassFile);
        Toast.makeText(groupViewActivity, R.string.openedDatabase, Toast.LENGTH_SHORT).show();
        groupViewActivity.drawerFragment.drawerLayout.closeDrawers();
        groupViewActivity.drawerFragment.sendToActivity.onNavigationItemSelected(((ApplicationBase) groupViewActivity.getApplication()).getDatabaseRoot());
        groupViewActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onCancelled(KeePassFile keePassFile) {
        super.onCancelled(keePassFile);
        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        ((ApplicationBase) groupViewActivity.getApplication()).setDatabase(keePassFile);
        Toast.makeText(groupViewActivity, R.string.failedToOpenDatabase, Toast.LENGTH_LONG).show();
        groupViewActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        groupViewActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }
}
