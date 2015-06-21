package hello.dcsms.plak.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import hello.dcsms.plak.Utils.Debugger;

/**
 * Created by jmkl on 5/18/2015.
 */
public class BatchScreenshotReceiver extends BroadcastReceiver {
    Debugger d = new Debugger();
    @Override
    public void onReceive(Context context, Intent intent) {
       byte[] bitmap = intent.getByteArrayExtra("bitmap");
        if(bitmap==null)
            return;

        d.log("SS DIKIRIM",bitmap.length);


    }
}
