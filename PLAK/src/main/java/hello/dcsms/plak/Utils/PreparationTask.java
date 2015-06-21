package hello.dcsms.plak.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

import hello.dcsms.plak.C;

/**
 * Created by jmkl on 4/27/2015.
 */
public class PreparationTask extends AsyncTask<Void, String, Void> {
    private ProgressDialog dialog;
    private Context mContext;

    public PreparationTask(Context context) {
        mContext = context;
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setTitle("Doing stuff");
        dialog.setIndeterminate(true);
        dialog.setMessage("Preparation...\nplease wait");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        dialog.setMessage(values[0]);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        File f = new File(C.PLAK_SD_ROOT);
        if (!f.exists()) {
            publishProgress("Creating Plak dir on sdcard");
            f.mkdirs();
        }
        publishProgress("Copying needed files");
        boolean copy = AssetUtils.copy(mContext,f.getAbsolutePath()+"/");
        publishProgress(copy?"Copying complete":"Something went wrong while copying [ERROR : 9]");
        publishProgress("Copying some more needed files");
        boolean copyt = AssetUtils.copy(mContext,"frame_template",C.PLAK_SS_MOCKUP_ROOT+"Redmi 1S/");
        publishProgress(copyt ? "Copying complete" : "Something went wrong while copying [ERROR : 10]");

        return null;
    }


}
