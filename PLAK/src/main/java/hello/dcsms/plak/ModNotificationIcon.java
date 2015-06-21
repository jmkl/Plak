package hello.dcsms.plak;

import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Field;


import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by jmkl on 5/5/2015.
 */
public class ModNotificationIcon {
    private static Class<?> classIconMerger;
    static int countx = 0,county = 0;
    enum PREFTYPE{
        BOOLEAN,STRING,INT,STRINT
    }

    private static Object getPreferenceValue(String key,Object def,PREFTYPE type){
        XSharedPreferences pref = new XSharedPreferences(C.PAKETPLAK);
        pref.makeWorldReadable();
        Object result = null;
        switch (type){
            case BOOLEAN:
                result = pref.getBoolean(key, (Boolean) def);
                break;
            case STRING:
                result =pref.getString(key, (String) def);
                break;
            case INT:
                result =pref.getInt(key, (int) def);
                break;
            case STRINT:
                result =Integer.parseInt(pref.getString(key, (String) def));
                break;

        }
        return result;
    }

    public static void hook(XC_LoadPackage.LoadPackageParam lpparam, ClassLoader mClassLoader) {
        classIconMerger = XposedHelpers.findClass("com.android.systemui.statusbar.phone.IconMerger", mClassLoader);


        XposedHelpers.findAndHookMethod(classIconMerger, "onMeasure",
                int.class, int.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                      //
                        int value = (int)getPreferenceValue(C.MAX_NOTIF_NUM,"2",PREFTYPE.STRINT);
                        LinearLayout  mIconMergerView = (LinearLayout) param.thisObject;

                        int mIconWidth = XposedHelpers.getIntField(param.thisObject, "mIconWidth");
                        int newWidth;
                        int max = mIconMergerView.getChildCount();
                        if (mIconMergerView.getChildCount() > value) {
                            max = value;
                            newWidth = mIconWidth * max;
                        }else
                            newWidth=mIconWidth*max;

                        Field fMeasuredWidth = View.class.getDeclaredField("mMeasuredWidth");
                        fMeasuredWidth.setAccessible(true);
                        Field fMeasuredHeight = View.class.getDeclaredField("mMeasuredHeight");
                        fMeasuredHeight.setAccessible(true);
                        Field fPrivateFlags = View.class.getDeclaredField("mPrivateFlags");
                        fPrivateFlags.setAccessible(true);
                        fMeasuredWidth.setInt(param.thisObject, newWidth);
                        fMeasuredHeight.setInt(param.thisObject, ((View) param.thisObject).getMeasuredHeight());
                        int privateFlags = fPrivateFlags.getInt(param.thisObject);
                        privateFlags |= 0x00000800;
                        fPrivateFlags.setInt(param.thisObject, privateFlags);
                    }
                });


        XposedHelpers.findAndHookMethod(classIconMerger, "checkOverflow",
                new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(final MethodHookParam param) throws Throwable {
                      //
                        int value = (int)getPreferenceValue(C.MAX_NOTIF_NUM,"2",PREFTYPE.STRINT);
                        try {
                            final View moreView = (View) XposedHelpers.getObjectField(param.thisObject, "mMoreView");
                            if (moreView == null) return null;


                            LinearLayout layout = (LinearLayout) param.thisObject;
                            final int N = layout.getChildCount();
                            int visibleChildren = 0;
                            for (int i = 0; i < N; i++) {
                                if (layout.getChildAt(i).getVisibility() != View.GONE)
                                    visibleChildren++;
                            }

                            final boolean overflowShown = (moreView.getVisibility() == View.VISIBLE);
                            final boolean moreRequired = visibleChildren > value;
                            if (moreRequired != overflowShown) {
                                layout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        moreView.setVisibility(moreRequired ? View.VISIBLE : View.GONE);

                                        try {

                                           // XposedHelpers.callMethod(param.thisObject, "setForceShowingMore", moreRequired);
                                        }catch (Throwable t){

                                        }

                                        }
                                });
                            }
                            return null;
                        } catch (Throwable t) {

                            return XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args);
                        }
                    }
                });


    }
}
