package com.joelkreutzwieser.apps.keepass.keepass;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.joelkreutzwieser.apps.keepass.ApplicationBase;
import com.joelkreutzwieser.apps.keepass.GroupViewActivity;
import com.joelkreutzwieser.apps.keepass.R;
import com.joelkreutzwieser.apps.keepass.keepass.domain.KeePassFile;

import java.io.InputStream;

public class KeePassAsync extends AsyncTask<Void, Void, KeePassFile> {

    private InputStream inputStream;
    private String password;
    private ProgressDialog progressDialog;
    private GroupViewActivity groupViewActivity;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Testing...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }

    public KeePassAsync(InputStream inputStream, String password, GroupViewActivity groupViewActivity) {
        this.inputStream = inputStream;
        this.password = password;
        this.groupViewActivity = groupViewActivity;
        progressDialog = new ProgressDialog(groupViewActivity);
    }

    @Override
    protected KeePassFile doInBackground(Void... params) {
        return KeePassDatabase.getInstance(inputStream).openDatabase(password, progressDialog);
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
    }
}
