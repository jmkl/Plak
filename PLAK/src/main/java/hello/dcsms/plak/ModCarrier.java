package hello.dcsms.plak;

import android.widget.TextView;

import com.android.systemui.statusbar.SignalClusterView;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by jmkl on 5/11/2015.
 */
public class ModCarrier {
    static String[] mCustomCarrier;
    static Class<?> netWork;
    SignalClusterView mSignalClusterView;
    SignalClusterView mSignalClusterView2;
    static TextView[] mCarrierTextView;
    static String NETWORK_CLASS = "com.android.systemui.statusbar.policy.BaseNetworkController";
    static String PHONESTATUSBAR = "com.android.systemui.statusbar.phone.SimpleStatusBar";
    public static void passStatusbar(Object thisObject) {
    }


    public static void hook(XC_LoadPackage.LoadPackageParam lpparam, ClassLoader mClassLoader) {
        mCustomCarrier = new String[]{C.PREF_CARRIERTEXT1, C.PREF_CARRIERTEXT2};
        netWork = XposedHelpers.findClass(NETWORK_CLASS, mClassLoader);

        try {
            XposedHelpers.findAndHookMethod(netWork, "refreshViews", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    boolean customs = XPrefUtils.getBoolean(C.ENABLE_CUSTOM_CARRIER, false);
                    if (!customs) return;
                    if (mCarrierTextView == null || mCustomCarrier == null) return;


                }
            });
        } catch (Throwable t) {//eat
        }


    }
}