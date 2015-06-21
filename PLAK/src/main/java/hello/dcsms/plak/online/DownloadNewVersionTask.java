package hello.dcsms.plak.online;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.Contacts;
import android.webkit.URLUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import hello.dcsms.plak.C;
import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.Utils;

/**
 * Created by jmkl on 5/19/2015.
 */
public class DownloadNewVersionTask extends AsyncTask<Void,Integer,File> {
    String urls;
    Notification.Builder notif;
    NotificationManager mNotificationManager;
    int NOTIFID= 0x10123;
    Debugger d;
    Context mContext;
    public DownloadNewVersionTask (Context context,String link){
        this.urls = link;
        d = new Debugger();
        this.mContext = context;
        notif = new Notification.Builder(context)
                .setContentTitle("Plak").setContentText("Downloading")
                .setSmallIcon(R.drawable.plak_carrier).setProgress(0, 0, true);
        notif.setOngoing(true);
        mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFID, notif.build());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(File result) {
        super.onPostExecute(result);
        if (result != null && result.exists()) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");

            PendingIntent pi = PendingIntent.getActivity(mContext, 0, i, 0);
            notif.setContentIntent(pi);
            notif.setContentText("Downloading is done")
                    .setProgress(0, 0, false);
            notif.setTicker("Install Now!!!");
            notif.setOngoing(false);
            mNotificationManager.notify(NOTIFID, notif.build());
        }else{
            notif.setContentText("Failed").setTicker("Please cek your internet connection");
            notif.setOngoing(false)
            .setProgress(0,0,false);
            mNotificationManager.notify(NOTIFID, notif.build());
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        notif.setContentText("Downloaded : " + formatBytes((long) values[0]))
                .setProgress(100, values[0], false);
        mNotificationManager.notify(NOTIFID, notif.build());
    }

    @Override
    protected File doInBackground(Void... voids) {
        File f = null;
        try {
            URL url = new URL(urls);
            String droot = C.PLAK_SD_ROOT+"/Downloads/";
            Utils.cekDir(droot);

            String filename = URLUtil.guessFileName(urls,null,null);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.connect();
            int lenght = conn.getContentLength();
            d.log(lenght);
            FileOutputStream out = new FileOutputStream(new File(droot,filename));
            InputStream input = conn.getInputStream();

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress((int) total * 100 / lenght);

                out.write(data, 0, count);
            }
            out.flush();
            out.close();
            input.close();
            f = new File(droot,filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }


    String mB = "B";
    String mKB = "KB";
    String mMB = "MB";
    String mS = "s";
    NumberFormat mDecimalFormat = new DecimalFormat("##0.0");
    NumberFormat mIntegerFormat = NumberFormat.getIntegerInstance();
    private String formatBytes(long bytes) {
        if (bytes > 10485760) { // 1024 * 1024 * 10
            return (mIntegerFormat.format(bytes / 1048576) + mMB);
        } else if (bytes > 1048576) { // 1024 * 1024
            return (mDecimalFormat.format(((float) bytes) / 1048576f) + mMB);
        } else if (bytes > 10240) { // 1024 * 10
            return (mIntegerFormat.format(bytes / 1024) + mKB);
        } else if (bytes > 1024) { // 1024
            return (mDecimalFormat.format(((float) bytes) / 1024f) + mKB);
        } else {
            return (mIntegerFormat.format(bytes) + mB);
        }
    }
}
