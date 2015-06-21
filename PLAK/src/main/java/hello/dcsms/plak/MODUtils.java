package hello.dcsms.plak;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Set;

import de.robv.android.xposed.XSharedPreferences;

public class MODUtils {
    public static Resources getPlakResource(Context mContext) {
        Context PlakContext = null;
        Resources res = null;
        try {
            if (mContext == null)
                return null;
            PlakContext = mContext.createPackageContext(
                    C.PAKETPLAK, Context.CONTEXT_IGNORE_SECURITY);
            return PlakContext.getResources();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return res;
    }

    public static void AntiGravity(View v, int g) {

        if (v instanceof RelativeLayout)
            ((RelativeLayout) v).setGravity(g);
        else if (v instanceof LinearLayout)
            ((LinearLayout) v).setGravity(g);
        else if (v instanceof TextView)
            ((TextView) v).setGravity(g);

    }

    public static Object getXPrefs(String key, Object obj_value) {
        XSharedPreferences pref = new XSharedPreferences(C.PAKETPLAK);
        pref.makeWorldReadable();
        if (obj_value instanceof String)
            return pref.getString(key, (String) obj_value);
        else if (obj_value instanceof Integer)
            return pref.getInt(key, (int) obj_value);
        else if (obj_value instanceof Float)
            return pref.getFloat(key, (float) obj_value);
        else if (obj_value instanceof Boolean)
            return pref.getBoolean(key, (boolean) obj_value);
        else if (obj_value instanceof Set<?>)
            return pref.getStringSet(key, (Set<String>) obj_value);
        else if (obj_value instanceof Long)
            return pref.getLong(key, (long) obj_value);
        else
            return null;
    }
}
