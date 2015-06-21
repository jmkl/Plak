package hello.dcsms.plak.widget;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by jmkl on 4/27/2015.
 */
public class MToast {


    public static void show(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
