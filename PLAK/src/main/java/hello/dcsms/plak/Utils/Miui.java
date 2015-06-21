package hello.dcsms.plak.Utils;

import android.os.Build;

import miui.os.SystemProperties;

public class Miui {
    private static int MIUIV6 = 4;
    private static int MIUIV5 = 3;

    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static int getMIUIVersion() {
        try {
         String res =  SystemProperties.get("ro.miui.ui.version.code");
        return Integer.parseInt(res);
        } catch (Exception ex) {
            return 0;
        }

    }

}
