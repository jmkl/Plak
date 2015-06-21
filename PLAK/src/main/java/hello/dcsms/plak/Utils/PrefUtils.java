package hello.dcsms.plak.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;
import java.util.Set;

import hello.dcsms.plak.C;
import hello.dcsms.plak.Hello;

/**
 * Created by jmkl on 5/1/2015.
 */
public class PrefUtils {
    private SharedPreferences pref;
    private Context context;

    public PrefUtils(Context context) {
        this.context = context;
        pref = getPref();
    }

    public void refreshPref() {
        pref = getPref();
    }

    public SharedPreferences getPref() {
        SharedPreferences pref = context.getSharedPreferences("hello.dcsms.plak_preferences", Context.MODE_WORLD_READABLE);
        return pref;
    }

    public void edit(String key, Object val) {
        SharedPreferences.Editor edit = pref.edit();
        if (val instanceof String)
            edit.putString(key, (String) val);
        else if (val instanceof Integer)
            edit.putInt(key, (int) val);
        else if (val instanceof Float)
            edit.putFloat(key, (float) val);
        else if (val instanceof Boolean)
            edit.putBoolean(key, (boolean) val);
        else if (val instanceof Long)
            edit.putLong(key, (Long) val);

        edit.commit();
        PrefUtils.fix();
    }

    public int getLatestVersion() {
        return pref.getInt(C.PLAK_VERSION, 0);
    }

    public static void fix() {
        File shit = new File(
                Environment.getDataDirectory()
                        + "/data/hello.dcsms.plak/shared_prefs/hello.dcsms.plak_preferences.xml");
        synchronized (shit) {
            if (shit.exists()) {
                shit.setReadable(true, false);
            }
        }
    }
}
