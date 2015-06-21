package hello.dcsms.plak.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

import org.apache.http.protocol.HTTP;

import java.io.File;

import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.PrefUtils;
import hello.dcsms.plak.task.SavingImageTask;

/**
 * Created by jmkl on 5/12/2015.
 */
public class SSHelper extends Service implements SavingImageTask.onSavingImageListener {
    NotificationManager mNotifmanager;
    Notification.Builder notif;
    Notification mNotif;
    Debugger debug;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    int NOTIF_ID=103;
    @Override
    public void onCreate() {
        super.onCreate();

        mNotifmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notif = new Notification.Builder(this)
                .setContentInfo("Preparing...")
                .setContentTitle("Hello")
                .setSmallIcon(R.drawable.ic_notif_image)
                .setAutoCancel(false)
                .setWhen(System.currentTimeMillis());
        debug = new Debugger();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            debug.log("On Start Command");
            String data = intent.getStringExtra("path");
            if (data != null && new File(data).exists()) {
                notif.setContentInfo("Preparing")
                        .setWhen(System.currentTimeMillis());
                SavingImageTask savingImageTask = new SavingImageTask(this, data);
                savingImageTask.setListener(this);
                savingImageTask.execute();
            } else {
                notif.setAutoCancel(true)
                        .setContentInfo("Error")
                        .setContentText("Error occured, Please try again")
                        .setWhen(System.currentTimeMillis());
                mNotifmanager.notify(NOTIF_ID, notif.build());

                stopSelf();

            }




        return START_NOT_STICKY;

    }

    @Override
    public void onUpdateStatus(String status) {
        notif.setContentText(status)
                .setWhen(System.currentTimeMillis());

    }

    @Override
    public void onDestroy() {
        System.gc();
        debug.log("On Destroy");
        super.onDestroy();
    }


    @Override
    public void onComplete(String result, int STATUSCODE) {
        PrefUtils pref = new PrefUtils(getApplicationContext());
        String mode = pref.getPref().getString("pref_intent", "0");
        PendingIntent pifinish = PendingIntent.getActivity(this, 0, new Intent(), 0);
        if(STATUSCODE==SavingImageTask.SUCCESS) {

            //share
            Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            i.setType("image/*");
            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(result)));

            //open
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            File file = new File(result);
            intent.setDataAndType(Uri.fromFile(file), "image/*");

            PendingIntent pi_share = PendingIntent.getActivity(this,0,i,0);
            PendingIntent pi_open= PendingIntent.getActivity(this,0,intent,0);
            notif.setContentText("Saving complete")
                    .setContentInfo("Complete")
                    .setContentIntent(mode.equals("0")?pi_share:pi_open)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis());

            notif.addAction(R.drawable.ic_notif_share, "Share", pi_share);
            notif.addAction(R.drawable.ic_notif_image,"Open", pi_open);
        }else{
            notif.setContentText("Error during saving mockup, please try again")
                    .setAutoCancel(true)
                    .setContentInfo("Failed")
                    .setContentIntent(pifinish)
                    .setWhen(System.currentTimeMillis());
        }
        mNotifmanager.notify(NOTIF_ID, notif.build());


        stopSelf();

    }
}
