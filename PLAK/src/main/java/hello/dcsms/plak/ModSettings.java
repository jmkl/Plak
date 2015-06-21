package hello.dcsms.plak;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.ListView;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import miui.preference.ValuePreference;

/**
 * Created by jmkl on 5/15/2015.
 */
public class ModSettings {

    public static void handleInitPackageResources(XC_LoadPackage.LoadPackageParam par) {

        try {
            Class<?> deviceInfo  = XposedHelpers.findClass("com.android.settings.MiuiDeviceInfoSettings",par.classLoader);
            XposedHelpers.findAndHookMethod(deviceInfo, "onCreate", Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                   final PreferenceFragment fragment = (PreferenceFragment) param.thisObject;
                    ValuePreference plak_pref=new ValuePreference(fragment.getActivity());
                    plak_pref.setTitle("Plak");
                    plak_pref.setValue("Current version : " + MODUtils.getPlakResource(fragment.getActivity()).getString(R.string.readable_versi));
                    plak_pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            Intent intent = new Intent();
                            intent.setClassName(C.PAKETPLAK, C.PAKETPLAK_MAINCONTENT);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            fragment.getActivity().startActivity(intent);
                            return true;
                        }
                    });

                    fragment.getPreferenceScreen().addPreference(plak_pref);
                    plak_pref.setOrder(2);



                }
            });
        } catch (Throwable e) {
        }
    }
}
