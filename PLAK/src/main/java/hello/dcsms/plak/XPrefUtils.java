package hello.dcsms.plak;

import android.widget.Toast;

import de.robv.android.xposed.XSharedPreferences;

/**
 * Created by jmkl on 5/11/2015.
 */
public class XPrefUtils {
    private static XSharedPreferences XPrefs() {
        XSharedPreferences pref = new XSharedPreferences(C.PAKETPLAK);
        pref.makeWorldReadable();
        return pref;

    }
    public static String getString(String key, String val) {
        return XPrefs().getString(key,val);
    }
    public static Integer getInteger(String key, int val) {
        return XPrefs().getInt(key, val);
    }
    public static Boolean getBoolean(String key, boolean val) {
        return XPrefs().getBoolean(key, val);
    }
    public static Float getFloat(String key, float val) {
        return XPrefs().getFloat(key, val);
    }
}
