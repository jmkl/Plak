package hello.dcsms.plak;

import android.os.Build;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import hello.dcsms.plak.Utils.Debugger;

import static de.robv.android.xposed.XposedBridge.hookMethod;
import static de.robv.android.xposed.XposedHelpers.findMethodExact;
import static de.robv.android.xposed.XposedHelpers.getObjectField;
import static de.robv.android.xposed.XposedHelpers.setIntField;


/**
 * Created by jmkl on 4/25/2015.
 */
public class Resident {
    public static void KeepMihomeOnMemory(){
        try {
            // Hook one of the several variations of ActivityStack.realStartActivityLocked from different ROMs
            Method mthRealStartActivityLocked;
            if (Build.VERSION.SDK_INT <= 18) {
                try {
                    mthRealStartActivityLocked = findMethodExact("com.android.server.am.ActivityStack", null, "realStartActivityLocked",
                            "com.android.server.am.ActivityRecord", "com.android.server.am.ProcessRecord",
                            boolean.class, boolean.class, boolean.class);
                } catch (NoSuchMethodError t) {
                    mthRealStartActivityLocked = findMethodExact("com.android.server.am.ActivityStack", null, "realStartActivityLocked",
                            "com.android.server.am.ActivityRecord", "com.android.server.am.ProcessRecord",
                            boolean.class, boolean.class);
                }
            } else {
                mthRealStartActivityLocked = findMethodExact("com.android.server.am.ActivityStackSupervisor", null, "realStartActivityLocked",
                        "com.android.server.am.ActivityRecord", "com.android.server.am.ProcessRecord",
                        boolean.class, boolean.class);
            }
            hookMethod(mthRealStartActivityLocked, new XC_MethodHook() {

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    String pkgName = (String) getObjectField(param.args[0], "packageName");
                    if (pkgName.equals("com.miui.home")) {
                        int adj = -12;
                        Object proc = getObjectField(param.args[0], "app");

                        // Override the *Adj values if meant to be resident in memory
                        if (proc != null) {
                            setIntField(proc, "maxAdj", adj);
                            if (Build.VERSION.SDK_INT <= 18)
                                setIntField(proc, "hiddenAdj", adj);
                            setIntField(proc, "curRawAdj", adj);
                            setIntField(proc, "setRawAdj", adj);
                            setIntField(proc, "curAdj", adj);
                            setIntField(proc, "setAdj", adj);
                        }}

                }
            });
        } catch (Throwable e) {

        }
    }

}
