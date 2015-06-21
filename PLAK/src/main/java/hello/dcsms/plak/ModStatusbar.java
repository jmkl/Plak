package hello.dcsms.plak;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.MiuiConfiguration;
import android.content.res.Resources;
import android.content.res.XModuleResources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import hello.dcsms.plak.Utils.BitmapUtils;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.MemUtils;
import hello.dcsms.plak.Utils.MemUtils.MemUtilsListener;
import hello.dcsms.plak.Utils.RLParam;
import hello.dcsms.plak.Utils.StringUtils;
import hello.dcsms.plak.data.SIData;
import hello.dcsms.plak.data.SuperScriptTagData;
import hello.dcsms.plak.manual.ManualDataJsonHelper;
import hello.dcsms.plak.manual.ManualItemData;
import hello.dcsms.plak.widget.ModExpanded;
import miui.date.DateUtils;

/**
 * @author jmkl
 */
public class ModStatusbar {

    static RelativeLayout RL_ROOT = null;
    static LinearLayout SS_JAM;
    static LinearLayout ICONMERGER;
    static ImageView ICON_MORE;
    static LinearLayout SS_NOTIF_ICON;
    static LinearLayout SS_STATUSBARICONS;
    static TextView SS_TRAFIK;
    static LinearLayout SS_SINYAL;
    static LinearLayout SS_BATRAI;
    static Object clusterViewClass;
    static Context mContext;
    static Class<?> MiuiConf, MiuiUtils, globalSS, SaveSSTask, IconMerger, SimpleStatusbar;
    static String format = getPrefString(C.FORMATJAM, "HH:mma");
    static LinearLayout CLUSTER_LAYOUT = null, CLUSTER_LAYOUT2 = null;
    static LinearLayout MOBILE_COMBO, MOBILE_COMBO2;
    static ViewGroup WIFI_PARENT, WIFI_PARENT2;
    static ViewGroup MOB_PARENT, MOB_PARENT2;
    static View WIFI_APP,WIFI_APP2, ROAMING, ROAMING2, MOB_INOUT, MOB_INOUT2, MOB_SIG, MOB_SIG2,
            MOB_CARRIER = null, MOB_CARRIER2 = null, MOB_TYPE, MOB_TYPE2, CARRIERTXT = null, CARRIERTXT2 = null;
    static private String JAM = "12:00PM", BAT = "B", TRF = "666K/s",
            SIG = "D", ICO = "EF", STAT_IC = "G";
    static private int BATTID = 1;
    static private int JAMID = 2;
    private static MemUtils memUtils;
    private static TextView tvjam = null;
    private static ImageView IV_CARRIER = null, IV_CARRIER2 = null;


    static Bitmap mScreenBitmap;
    static Context ssContext;
    static Display mDisplay;
    static List<View> kananView = null;
    static List<View> KiriView = null;
    static List<View> tengahView = null;
    static DisplayMetrics mDisplayMetrics;
    static NotificationManager mNotificationManager;
    private static MemUtils.MemUtilsListener listener = new MemUtilsListener() {

        @Override
        public void getMem(String mem) {
            String result = new SimpleDateFormat(format, Locale.getDefault())
                    .format(System.currentTimeMillis());
            if (tvjam != null) {
                String tempres = result.replace("mem", MemUtils.getMEM());

                List<SuperScriptTagData> match = StringUtils.RegexSuperScriptTag(tempres);

                if (match.size() > 0)
                    tvjam.setText((StringUtils.SuperscriptIt(tempres, match)));
                else
                    tvjam.setText(tempres);


            }
        }
    };
    private static boolean tempshowmore = false;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context ct, Intent it) {
            if (it.getAction().equals(C.MODSTATUSBAR)) {
                Debugger.logcat("Onreceive BroadCast", it.getAction());
                FormatUlang(it.getStringExtra(C.PREF_SB_KANAN),
                        it.getStringExtra(C.PREF_SB_KIRI),
                        it.getStringExtra(C.PREF_SB_TENGAH));
                MenganukanAnuStatusbarRuntime();
            } else if (it.getAction().equals(
                    "com.miui.app.ExtraStatusBarManager.REQUEST_RESTART")) {
                Debugger.logcat("Onreceive BroadCast", it.getAction());
                RL_ROOT = null;
                tvjam = null;
                IV_CARRIER = null;
                IV_CARRIER2 = null;
                if (memUtils != null) {
                    memUtils.stop();
                    memUtils = null;
                }
            } else if (it.getAction().equals(C.MODSTATUSBARRUNTIME)) {
                MenganukanAnuStatusbarRuntime();

            } else if (it.getAction().equals("hello.dcsms.plak.SS")) {
                try {

                    Bitmap bmpss = (Bitmap) XposedHelpers.callStaticMethod(MiuiUtils, "getScreenshot", mContext);
                    if (bmpss != null) {

                    }
                } catch (Throwable x) {
                }


            } else if (it.getAction().equals(C.ORDERSIM)) {
                try {
                    //Config changes=80000000 {0.86 510mcc89mnc en_US ldltr sw360dp w360dp h615dp 320dpi nrml long port uimode=12 finger -keyb/v/h -nav/h s.14 themeChanged=8 themeChangedFlags=268466313}
                    // Config changes=80000000 {0.86 510mcc89mnc en_US ldltr sw360dp w360dp h615dp 320dpi nrml long port uimode=12 finger -keyb/v/h -nav/h s.12 themeChanged=6 themeChangedFlags=8192}
                    XposedHelpers.callStaticMethod(MiuiConf, "sendThemeConfigurationChangeMsg", MiuiConfiguration.THEME_FLAG_STATUSBAR);
                } catch (Throwable x) {
                    Debugger.logcat("sendThemeConfigurationChangeMsg", x.getStackTrace().toString());
                }
// if(mContext!=null){
//
//                    mContext.sendBroadcast(new Intent("com.miui.app.ExtraStatusBarManager.REQUEST_RESTART"));
//                }
                //  Process.sendSignal(Process.myPid(),Process.SIGNAL_KILL);
            } else if (it.getAction().equals(C.MODSTATUSBARUPDATECARRIERONLY)) {
                updateCarrierStuff();
            }else if(it.getAction().equals(C.SSASMUCHASUCAN)){
                if(mContext==null || MiuiUtils==null)
                    return;
                try {
                    Bitmap bmpss = (Bitmap) XposedHelpers.callStaticMethod(MiuiUtils, "getScreenshot", mContext);

                    if(bmpss!=null){
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bmpss.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        Intent i = new Intent();
                        i.setAction(C.SS_DIKIRIM);
                        i.putExtra("bitmap",stream.toByteArray());
                        mContext.sendBroadcast(i);
                        bmpss.recycle();
                        stream.flush();
                        stream.close();
                    }
                }catch (Throwable t){

                }
            }

        }

    };
    private XModuleResources modres;

    public ModStatusbar(InitPackageResourcesParam resparam,
                        XModuleResources mXModuleResources) {
        this.modres = mXModuleResources;
        initPackageResource(resparam);
        JabatTangandenganLayout(resparam);
    }

    static int visibleChildren = 0;

    static Object simpleStatusbarObject;
    static Class<?> MiuiStatusBarIconView;
    static boolean isDarkMode;

    public static void onLoadPackage(LoadPackageParam lpparam) {
        Debugger.logcat("PLAK", "ModStatusbar onLoadPackage");
        final ClassLoader mClassLoader = lpparam.classLoader;
        MiuiConf = XposedHelpers.findClass("android.content.res.MiuiConfiguration", mClassLoader);
        MiuiUtils = XposedHelpers.findClass("miui.util.ScreenshotUtils", mClassLoader);
        IconMerger = XposedHelpers.findClass("com.android.systemui.statusbar.phone.IconMerger", mClassLoader);
        SimpleStatusbar = XposedHelpers.findClass("com.android.systemui.statusbar.phone.SimpleStatusBar", mClassLoader);
        globalSS = XposedHelpers.findClass("com.android.systemui.screenshot.GlobalScreenshot", mClassLoader);
        SaveSSTask = XposedHelpers.findClass("com.android.systemui.screenshot.SaveImageInBackgroundTask", mClassLoader);
        MiuiStatusBarIconView = XposedHelpers.findClass("com.android.systemui.statusbar.StatusBarIconView", mClassLoader);

        XposedHelpers.findAndHookMethod(MiuiStatusBarIconView, "updateDarkMode", boolean.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                    isDarkMode =(boolean) param.args[0];
                    updateCarrierStuff();

            }
        });
        XposedHelpers.findAndHookMethod(SaveSSTask, "onPostExecute", "com.android.systemui.screenshot.SaveImageInBackgroundData", new XC_MethodHook() {
            Context context;

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Object data = param.args[0];
                if (data != null) {
                    Uri url = (Uri) XposedHelpers.getObjectField(data, "imageUri");
                    context = (Context) XposedHelpers.getObjectField(data, "context");
                    BitmapUtils.SSUriToCrot(context, url);
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                boolean overide = getPrefBool(C.OVERRIDE_ORI_SS_NOTIF, true);
                if (overide) {
                    try {
                        NotificationManager mNMan = (NotificationManager) XposedHelpers.getObjectField(param.thisObject, "mNotificationManager");
                        int notifId = (int) XposedHelpers.getObjectField(param.thisObject, "mNotificationId");

                        Class<?> miuistatusbarManager = XposedHelpers.findClass("android.app.MiuiStatusBarManager", mClassLoader);
                        boolean isenable = (boolean) XposedHelpers.callStaticMethod(miuistatusbarManager, "isScreenshotNotificationEnabled", context);
                        if (isenable) {
                            mNMan.cancel(notifId);
                        }
                    } catch (Throwable t) {
                    }
                }
            }
        });

        ModCarrier.hook(lpparam, mClassLoader);
        ModNotificationIcon.hook(lpparam, mClassLoader);

        XposedBridge.hookAllConstructors(globalSS, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object globalScreenshot = param.thisObject;
                mScreenBitmap = (Bitmap) XposedHelpers.getObjectField(globalScreenshot, "mScreenBitmap");
                ssContext = (Context) XposedHelpers.getObjectField(globalScreenshot, "mContext");
                mNotificationManager = (NotificationManager) XposedHelpers.getObjectField(globalScreenshot, "mNotificationManager");
                mDisplay = (Display) XposedHelpers.getObjectField(globalScreenshot, "mDisplay");
                mDisplayMetrics = (DisplayMetrics) XposedHelpers.getObjectField(globalScreenshot, "mDisplayMetrics");

            }
        });

        XposedHelpers.findAndHookMethod(MiuiConf, "sendThemeConfigurationChangeMsg", long.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Long tes = (Long) param.args[0];

            }
        });
        Class<?> clockClass = XposedHelpers.findClass(
                "com.android.systemui.statusbar.policy.Clock", mClassLoader);
        XposedHelpers.findAndHookMethod(clockClass, "updateClock",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        super.beforeHookedMethod(param);
                        Debugger.logcat("PLAK", "Clock view is fucked");

                        initJamStatusbar((TextView) param.thisObject);
                        updateJamStatusBar();
                        param.setResult(null);

                    }

                });
        Class<?> clusterClass = XposedHelpers.findClass(
                "com.android.systemui.statusbar.SignalClusterView",
                mClassLoader);
        XposedBridge.hookAllConstructors(clusterClass, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param)
                    throws Throwable {
                clusterViewClass = param.thisObject;

            }
        });
        final Class<?> classSimpleStatusbar = XposedHelpers.findClass(
                "com.android.systemui.statusbar.phone.PhoneStatusBar",
                mClassLoader);
        XposedHelpers.findAndHookMethod(classSimpleStatusbar,
                "makeStatusBarView", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        ModCarrier.passStatusbar(param.thisObject);
                        simpleStatusbarObject = param.thisObject;
                        mContext = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");
                    }
                });


        //shouldDisableNavbarGestures
        Class<?> phoneStatusbarClass = XposedHelpers.findClass(
                "com.android.systemui.statusbar.phone.PhoneStatusBar",
                mClassLoader);


        XposedHelpers.findAndHookMethod(phoneStatusbarClass,
                "updateNotificationIcons", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param)
                            throws Throwable {
                        super.afterHookedMethod(param);
                        MenganukanAnuStatusbarRuntime();
                        ModExpanded.isTimeforUpdate();
                    }
                });
        XposedHelpers.findAndHookMethod(phoneStatusbarClass,
                "updateViewsInStatusBar", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param)
                            throws Throwable {
                        super.afterHookedMethod(param);
                        MenganukanAnuStatusbarRuntime();
                        ModExpanded.isTimeforUpdate();
                    }
                });


    }

    private static void doUpdateDefault() {
        Debugger.logcat("PLAK", "doUpdateDefault");
        if (tvjam != null)
            tvjam.setText(DateUtils.formatDateTime(
                    Calendar.getInstance(TimeZone.getDefault())
                            .getTimeInMillis(), 12));
    }

    protected static void updateJamStatusBar() {

        boolean isCustom = getPrefBool(C.MODJAM, false);
        if (isCustom) {
            if (memUtils != null) {
                memUtils.start();
            } else {
                memUtils = new MemUtils();
                memUtils.setListener(listener);
                memUtils.start();
            }
        } else {
            if (memUtils != null)
                memUtils.stop();
            doUpdateDefault();
        }
        Debugger.logcat("updateJamStatusBar", "updateJamStatusBar");

    }

    private static void initJamStatusbar(TextView tv) {

        if (tvjam == null) {
            tvjam = tv;
            Debugger.logcat("initJamStatusbar", "TVJAM is NULL");
        } else {
            Debugger.logcat("initJamStatusbar", "TVJAM is not NULL");
        }

    }

    /**
     * WIFIMODEKIRI = 0; WIFIMODKANAN = 1;
     */
    protected static void doModifyMobileBitches() {
        int mode = Integer.parseInt(getPrefString(C.PREF_LAYOUT_WIFI_ICON, "0"));
        switch (mode) {
            case 0:
                CLUSTER_LAYOUT.removeAllViews();
                CLUSTER_LAYOUT.addView(WIFI_PARENT);
                CLUSTER_LAYOUT.addView(WIFI_APP);
                CLUSTER_LAYOUT.addView(MOB_PARENT);
                CLUSTER_LAYOUT.addView(ROAMING);

                CLUSTER_LAYOUT2.removeAllViews();
                CLUSTER_LAYOUT2.addView(WIFI_PARENT2);
                CLUSTER_LAYOUT.addView(WIFI_APP2);
                CLUSTER_LAYOUT2.addView(MOB_PARENT2);
                CLUSTER_LAYOUT2.addView(ROAMING2);
                break;

            case 1:
                CLUSTER_LAYOUT.removeAllViews();
                CLUSTER_LAYOUT.addView(MOB_PARENT);
                CLUSTER_LAYOUT.addView(WIFI_PARENT);
                CLUSTER_LAYOUT.addView(WIFI_APP);
                CLUSTER_LAYOUT.addView(ROAMING);

                CLUSTER_LAYOUT2.removeAllViews();
                CLUSTER_LAYOUT2.addView(MOB_PARENT2);
                CLUSTER_LAYOUT2.addView(WIFI_PARENT2);
                CLUSTER_LAYOUT.addView(WIFI_APP2);
                CLUSTER_LAYOUT2.addView(ROAMING2);
                break;
        }
        CLUSTER_LAYOUT.requestLayout();
        CLUSTER_LAYOUT2.requestLayout();
        Debugger.logcat("doModifyWifiBitches", mode);

    }


    //TODO CARRIER ALLABOUTOCHANGE HERE DOSOMETHING WTF
    protected static void doModifyWifiBitches() {
        List<SIData> items = SIData.getListFromString(getPrefString(
                C.PREF_LAYOUT_MOBILE_ICON, SIData.DEFAULT));
        MOBILE_COMBO.removeAllViews();
        MOBILE_COMBO2.removeAllViews();
        for (SIData object : items) {
            String obj = object.getKey();
            if (obj.equals(SIData.INOUT)) {
                MOBILE_COMBO.addView(MOB_INOUT);
                MOBILE_COMBO2.addView(MOB_INOUT2);
            } else if (obj.equals(SIData.SIGNAL)) {
                MOBILE_COMBO.addView(MOB_SIG);
                MOBILE_COMBO2.addView(MOB_SIG2);
                //TODO CHILD NOT NULL
            } else if (obj.equals(SIData.CARRIER)) {

                MOBILE_COMBO.addView(MOB_CARRIER);
                MOBILE_COMBO2.addView(MOB_CARRIER2);

            } else if (obj.equals(SIData.SIGNAL_TYPE)) {
                MOBILE_COMBO.addView(MOB_TYPE);
                MOBILE_COMBO2.addView(MOB_TYPE2);
            }
        }

        MOBILE_COMBO.requestLayout();
        MOBILE_COMBO2.requestLayout();


    }

    private static void MenganukanAnuStatusbarRuntime() {
        if(SS_BATRAI==null)
            return;
        boolean switch_baterai = getPrefBool(C.PREF_SB_BATTERYORDER, false);

         View   ch1 = SS_BATRAI.getChildAt(0);
         View   ch2 = SS_BATRAI.getChildAt(1);
         View   ch3 = SS_BATRAI.getChildAt(2);

        if (switch_baterai) {
            SS_BATRAI.removeAllViews();

            if (ch1 instanceof ImageView && ch2 instanceof ImageView && ch3 instanceof TextView) {
                SS_BATRAI.addView(ch3);
                SS_BATRAI.addView(ch1, 1);
                SS_BATRAI.addView(ch2, 2);
            } else {
                SS_BATRAI.addView(ch1);
                SS_BATRAI.addView(ch2, 1);
                SS_BATRAI.addView(ch3, 2);
            }

        } else {
            SS_BATRAI.removeAllViews();
            if (ch1 instanceof ImageView && ch2 instanceof ImageView && ch3 instanceof TextView) {
                SS_BATRAI.addView(ch1);
                SS_BATRAI.addView(ch2, 1);
                SS_BATRAI.addView(ch3, 2);
            } else {
                SS_BATRAI.addView(ch3);
                SS_BATRAI.addView(ch1, 1);
                SS_BATRAI.addView(ch2, 2);
            }
        }

        SS_BATRAI.requestLayout();
        boolean showjam = getPrefBool(C.PREF_SH_JAM, true);
        boolean showbat = getPrefBool(C.PREF_SH_BATT, true);
        boolean showsinyal = getPrefBool(C.PREF_SH_SINYAL, true);
        boolean shownotif = getPrefBool(C.PREF_SH_NOTIF, true);
        boolean showicon = getPrefBool(C.PREF_SH_ICON, true);

        showHideView(SS_JAM, showjam);
        showHideView(SS_BATRAI, showbat);
        showHideView(SS_SINYAL, showsinyal);
        showHideView(SS_NOTIF_ICON, shownotif);
        showHideView(SS_STATUSBARICONS, showicon);
        format = getPrefString(C.FORMATJAM, "HH:mma");
        updateJamStatusBar();

        doModifyMobileBitches();
        doModifyWifiBitches();
        updateCarrierStuff();


    }

    private static void showHideView(final View v, final boolean show) {
        v.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private static boolean getPrefBool(String key, boolean val) {
        XSharedPreferences pref = new XSharedPreferences(C.PAKETPLAK);
        pref.makeWorldReadable();
        return pref.getBoolean(key, val);
    }

    private static String getPrefString(String key, String val) {
        XSharedPreferences pref = new XSharedPreferences(C.PAKETPLAK);
        pref.makeWorldReadable();
        return pref.getString(key, val);
    }


    private void initPackageResource(InitPackageResourcesParam resparam) {

        resparam.res.hookLayout(C.NAMAPAKET_SYSTEMUI, "layout",
                "status_bar_simple", new XC_LayoutInflated() {

                    @Override
                    public void handleLayoutInflated(LayoutInflatedParam liparam)
                            throws Throwable {
                        Context c = liparam.view.getContext();
                        IntentFilter filter = new IntentFilter();
                        filter.addAction(C.MODSTATUSBAR);
                        filter.addAction(C.MODSTATUSBARRUNTIME);
                        filter.addAction(C.ORDERSIM);
                        filter.addAction("hello.dcsms.plak.SS");
                        filter.addAction("com.miui.app.ExtraStatusBarManager.REQUEST_RESTART");
                        filter.addAction(C.MODSTATUSBARUPDATECARRIERONLY);
                        filter.addAction(C.SSASMUCHASUCAN);
                        c.registerReceiver(receiver, filter);

                    }
                });

        boolean isManualModEnable = getPrefBool(C.ENABLEMANUAL_CONFIG, false);

        if (isManualModEnable) {

            List<ManualItemData> data = ManualDataJsonHelper
                    .ReadJson(C.PLAKMANUALSETTINGSFILE);

            if (data.size() > 0) {
                for (ManualItemData mi : data) {
                    if (mi.getNamaPaket().equals(C.NAMAPAKET_SYSTEMUI)) {
                        if (mi.getTipe().equals(ManualItemData.TSTRING))
                            setReplaceMentShit(resparam, "string",
                                    mi.getNamaField(), mi.getNilai());
                        else if (mi.getTipe().equals(ManualItemData.TBOOL))
                            setReplaceMentShit(resparam, "bool",
                                    mi.getNamaField(), str2bool(mi.getNilai()));
                        else if (mi.getTipe().equals(ManualItemData.TCOL))
                            setReplaceMentShit(resparam, "color",
                                    mi.getNamaField(),
                                    Color.parseColor(mi.getNilai()));
                        else if (mi.getTipe().equals(ManualItemData.TINT))
                            setReplaceMentShit(resparam, "integer",
                                    mi.getNamaField(), str2int(mi.getNilai()));
                        else if (mi.getTipe().equals(ManualItemData.TDIMEN))
                            setReplaceMentShit(
                                    resparam,
                                    "dimen",
                                    mi.getNamaField(),
                                    modres.fwd(C.DIMENS[str2int(mi.getNilai())]));
                    }
                }
            }
        }

    }

    private int str2int(String integer) {
        return Integer.parseInt(integer);
    }

    private boolean str2bool(String bool) {
        return Integer.parseInt(bool) == 1;
    }

    private void setReplaceMentShit(InitPackageResourcesParam resparam,
                                    String tipe, String namafield, Object nilai) {
        try {
            resparam.res.setReplacement(C.NAMAPAKET_SYSTEMUI, tipe, namafield,
                    nilai);
        } catch (Exception e) {
            Debugger.logcat("PLAK", e.getMessage());
        }
    }


    ViewGroup.LayoutParams ViewGroupWRAPKONTEN() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    static  boolean cekDarkMode() {
        return isDarkMode;
    }

    public static void updateCarrierStuff() {
        boolean isDark = cekDarkMode();
        if (IV_CARRIER != null && mContext != null) {
            IV_CARRIER.setVisibility(CARRIERTXT.getVisibility());
            Bitmap bmp = BitmapUtils.getBitmapCarrier(isDark?C.PLAK_CARRIERIMAGE_SIM1_DARK:C.PLAK_CARRIERIMAGE_SIM1);
            if (bmp != null)
                IV_CARRIER.setImageBitmap(bmp);
        }
        if (IV_CARRIER2 != null && mContext != null) {
            IV_CARRIER2.setVisibility(CARRIERTXT2.getVisibility());
            Bitmap bmp = BitmapUtils.getBitmapCarrier(isDark?C.PLAK_CARRIERIMAGE_SIM2_DARK:C.PLAK_CARRIERIMAGE_SIM2);
            if (bmp != null)
                IV_CARRIER2.setImageBitmap(bmp);
        }

    }

    private Drawable getDrawableResources() {

        Context PlakContext = null;
        Drawable d = null;
        Resources res = MODUtils.getPlakResource(mContext);
        if (res != null)
            d = res.getDrawable(R.drawable.plak_carrier);

        return d;

    }

    Drawable default_carrier;

    private synchronized void initSignalCluster(LinearLayout v) {
        CLUSTER_LAYOUT = v;
        WIFI_APP = CLUSTER_LAYOUT.getChildAt(0);
        WIFI_PARENT = (ViewGroup) CLUSTER_LAYOUT.getChildAt(1);
        ROAMING = CLUSTER_LAYOUT.getChildAt(2);
        MOB_PARENT = (ViewGroup) CLUSTER_LAYOUT.getChildAt(3);
        // Mobile part
        MOBILE_COMBO = (LinearLayout) MOB_PARENT.getChildAt(0);
        MOB_INOUT = MOBILE_COMBO.getChildAt(0);
        MOB_SIG = MOBILE_COMBO.getChildAt(1);
        if (cekCustCarrier())
            CARRIERTXT = MOBILE_COMBO.getChildAt(2);
        else
            MOB_CARRIER = MOBILE_COMBO.getChildAt(2);

        MOB_TYPE = MOBILE_COMBO.getChildAt(3);


        if (cekCustCarrier() && IV_CARRIER == null) {
            MOB_CARRIER = new FrameLayout(MOBILE_COMBO.getContext());
            MOB_CARRIER.setLayoutParams(ViewGroupWRAPKONTEN());
            IV_CARRIER = new ImageView(MOBILE_COMBO.getContext());
            IV_CARRIER.setLayoutParams(ViewGroupWRAPKONTEN());
            if (default_carrier == null)
                default_carrier = getDrawableResources();

            if (default_carrier != null)
                IV_CARRIER.setImageDrawable(default_carrier);

            MOBILE_COMBO.removeView(CARRIERTXT);

            ((FrameLayout) MOB_CARRIER).addView(CARRIERTXT);
            ((FrameLayout) MOB_CARRIER).addView(IV_CARRIER);
            MOBILE_COMBO.addView(MOB_CARRIER, 2);
            updateCarrierStuff();
        }

    }

    private static boolean cekCustCarrier() {
        return getPrefBool(C.ENABLE_CUSTOM_CARRIER, false);
    }

    private synchronized void initSignalCluster2(LinearLayout v) {
        CLUSTER_LAYOUT2 = v;
        WIFI_APP2 = CLUSTER_LAYOUT2.getChildAt(0);
        WIFI_PARENT2 = (ViewGroup) CLUSTER_LAYOUT2.getChildAt(1);
        ROAMING2 = CLUSTER_LAYOUT2.getChildAt(2);
        MOB_PARENT2 = (ViewGroup) CLUSTER_LAYOUT2.getChildAt(3);
        // Mobile part
        MOBILE_COMBO2 = (LinearLayout) MOB_PARENT2.getChildAt(0);
        MOB_INOUT2 = MOBILE_COMBO2.getChildAt(0);
        MOB_SIG2 = MOBILE_COMBO2.getChildAt(1);
        if (cekCustCarrier())
            CARRIERTXT2 = MOBILE_COMBO2.getChildAt(2);
        else
            MOB_CARRIER2 = MOBILE_COMBO2.getChildAt(2);

        MOB_TYPE2 = MOBILE_COMBO2.getChildAt(3);

        if (cekCustCarrier() && IV_CARRIER2 == null) {
            MOB_CARRIER2 = new FrameLayout(MOBILE_COMBO2.getContext());
            MOB_CARRIER2.setLayoutParams(ViewGroupWRAPKONTEN());
            IV_CARRIER2 = new ImageView(MOBILE_COMBO2.getContext());
            IV_CARRIER2.setLayoutParams(ViewGroupWRAPKONTEN());
            if (default_carrier != null)
                IV_CARRIER2.setImageDrawable(default_carrier);

            MOBILE_COMBO2.removeView(CARRIERTXT2);

            ((FrameLayout) MOB_CARRIER2).addView(CARRIERTXT2);
            ((FrameLayout) MOB_CARRIER2).addView(IV_CARRIER2);
            MOBILE_COMBO2.addView(MOB_CARRIER2, 2);
            updateCarrierStuff();
        }
    }

    private void JabatTangandenganLayout(InitPackageResourcesParam resparam) {
        resparam.res.hookLayout(C.NAMAPAKET_SYSTEMUI, "layout",
                "signal_cluster_view", new XC_LayoutInflated() {

                    @Override
                    public void handleLayoutInflated(LayoutInflatedParam liparam)
                            throws Throwable {

                    }
                });
        resparam.res.hookLayout(C.NAMAPAKET_SYSTEMUI, "layout",
                "status_bar_simple", new XC_LayoutInflated() {

                    @Override
                    public void handleLayoutInflated(LayoutInflatedParam liparam)
                            throws Throwable {
                        Debugger.logcat("JABATTANGAN", "RL_ROOT INIT");
                        if (RL_ROOT == null) {
                            Debugger.logcat("JABATTANGAN", "RL_ROOT is NULL");
                            RL_ROOT = (RelativeLayout) liparam.view
                                    .findViewById(liparam.res
                                            .getIdentifier("icons", "id",
                                                    C.NAMAPAKET_SYSTEMUI));

                            Debugger.logcat("JABATTANGAN ROOT CHILD COUNTING",
                                    Integer.toString(RL_ROOT.getChildCount()));

                            // 0

                            TextView SS_JAM_ORI = (TextView) RL_ROOT.getChildAt(0);
                            // 1
                            // 1
                            SS_NOTIF_ICON = (LinearLayout) RL_ROOT
                                    .getChildAt(1);
                            ICONMERGER = (LinearLayout) SS_NOTIF_ICON.getChildAt(0);
                            ICON_MORE = (ImageView) SS_NOTIF_ICON.getChildAt(1);
                            // 2
                            SS_STATUSBARICONS = (LinearLayout) RL_ROOT
                                    .getChildAt(2);

                            // 2.0
                            ImageView SS_MOREICON = (ImageView) SS_STATUSBARICONS
                                    .getChildAt(0);
                            // 2.1
                            SS_TRAFIK = (TextView) SS_STATUSBARICONS
                                    .getChildAt(1);

                            // 2.2
                            LinearLayout SS_STATUSICON = (LinearLayout) SS_STATUSBARICONS
                                    .getChildAt(2);

                            // 2.3
                            SS_SINYAL = (LinearLayout) SS_STATUSBARICONS
                                    .getChildAt(3);
                            //TODO COMINGSOON

                            // 2.3.0

                            ViewGroup SS_SINYALCLUSTER1 = (ViewGroup) liparam.view
                                    .findViewById(liparam.res
                                            .getIdentifier("signal_cluster", "id",
                                                    C.NAMAPAKET_SYSTEMUI));
                            // 2.3.1
                            ViewGroup SS_SINYALCLUSTER2 = (ViewGroup) liparam.view
                                    .findViewById(liparam.res
                                            .getIdentifier("signal_cluster2", "id",
                                                    C.NAMAPAKET_SYSTEMUI));
                            initSignalCluster((LinearLayout) SS_SINYALCLUSTER1);
                            initSignalCluster2((LinearLayout) SS_SINYALCLUSTER2);

                            //4
                            ImageView SS_BATRAI_IMG_CHARGE = (ImageView) SS_STATUSBARICONS.getChildAt(4);
                            ImageView SS_BATRAI_IMG = (ImageView) SS_STATUSBARICONS.getChildAt(5);
                            TextView SS_BATRAI_NUM = (TextView) SS_STATUSBARICONS.getChildAt(6);


                            SS_BATRAI = new LinearLayout(RL_ROOT.getContext());
                            SS_BATRAI.setLayoutParams(RLParam.WRAP_KONTEN());
                            SS_BATRAI.setId(BATTID);

                            // removeadd
                            SS_STATUSBARICONS.removeView(SS_TRAFIK);
                            RL_ROOT.addView(SS_TRAFIK);

                            boolean mode = getPrefBool(C.PREF_LAYOUT_SIM_ORDER, false);

                            SS_SINYAL.removeView(SS_SINYALCLUSTER1);
                            SS_SINYAL.removeView(SS_SINYALCLUSTER2);
                            SS_STATUSBARICONS.removeView(SS_SINYAL);

                            if (!mode) {

                                SS_SINYAL.addView(SS_SINYALCLUSTER1);
                                SS_SINYAL.addView(SS_SINYALCLUSTER2);
                            } else {

                                SS_SINYAL.addView(SS_SINYALCLUSTER2);
                                SS_SINYAL.addView(SS_SINYALCLUSTER1);
                            }

                            RL_ROOT.addView(SS_SINYAL);
                            RL_ROOT.removeView(SS_JAM_ORI);
                            SS_JAM = new LinearLayout(RL_ROOT.getContext());
                            SS_JAM.setLayoutParams(RLParam.WRAP_KONTEN());
                            SS_JAM.setId(JAMID);
                            SS_JAM.addView(SS_JAM_ORI);
                            RL_ROOT.addView(SS_JAM);



                            SS_STATUSBARICONS.removeView(SS_BATRAI_IMG_CHARGE);
                            SS_STATUSBARICONS.removeView(SS_BATRAI_IMG);
                            SS_STATUSBARICONS.removeView(SS_BATRAI_NUM);


                            SS_BATRAI.addView(SS_BATRAI_IMG_CHARGE);
                            SS_BATRAI.addView(SS_BATRAI_IMG);
                            SS_BATRAI.addView(SS_BATRAI_NUM);

                            RL_ROOT.addView(SS_BATRAI);

                            MODUtils.AntiGravity(SS_JAM, Gravity.CENTER_VERTICAL);
                            MODUtils.AntiGravity(SS_NOTIF_ICON, Gravity.CENTER_VERTICAL);
                            MODUtils.AntiGravity(SS_STATUSBARICONS,
                                    Gravity.CENTER_VERTICAL);
                            MODUtils.AntiGravity(SS_TRAFIK, Gravity.CENTER_VERTICAL);
                            MODUtils.AntiGravity(SS_SINYAL, Gravity.CENTER_VERTICAL);
                            MODUtils.AntiGravity(SS_BATRAI, Gravity.CENTER_VERTICAL);

                            SS_JAM.setLayoutParams(RLParam.KANAN());
                            SS_BATRAI.setLayoutParams(RLParam.LEFT_OF(SS_JAM
                                    .getId()));
                            SS_SINYAL.setLayoutParams(RLParam.LEFT_OF(SS_BATRAI
                                    .getId()));
                            SS_STATUSBARICONS.setLayoutParams(RLParam
                                    .LEFT_OF(SS_SINYAL.getId()));
                            SS_TRAFIK.setLayoutParams(RLParam
                                    .LEFT_OF(SS_STATUSBARICONS.getId()));
                            SS_NOTIF_ICON.setLayoutParams(RLParam.KIRI());
                        }
                        FormatUlang(getPrefString(C.PREF_SB_KANAN, "B,G,EF"),
                                getPrefString(C.PREF_SB_KIRI, "D,666K/s"),
                                getPrefString(C.PREF_SB_TENGAH, "12:00PM"));
                        MenganukanAnuStatusbarRuntime();


                        doModifyMobileBitches();
                        doModifyWifiBitches();


                    }
                });

    }

    private int posView(String[] tumpukanJerami, String jerami) {
        for (int i = 0; i < tumpukanJerami.length; i++) {
            if (jerami.equals(tumpukanJerami[i]))
                return i;
        }
        return -1;
    }

    private View posView(View[] v, String[] tumpukanJerami, String jerami) {
        int pos = posView(tumpukanJerami, jerami);
        if (pos > -1)
            return v[pos];
        else
            return null;
    }

    enum POSISI {
        KIRI, TENGAH, KANAN
    }

    POSISI PosisiIcon;

    private void FormatUlang(String kn, String kr, String ldtengah) {

        View[] SS_V = new View[]{ // which
                SS_JAM, // 0
                SS_NOTIF_ICON, // 1
                SS_STATUSBARICONS, // 2
                SS_TRAFIK, // 3
                SS_SINYAL, // 4
                SS_BATRAI}; // 5
        String[] viewtag = new String[]{ // tag
                JAM, // 0
                ICO, // 1
                STAT_IC,// 2
                TRF, // 3
                SIG, // 4
                BAT};// 5

        ArrayList<String> ldkanan = StringUtils.getListFromString(kn);
        ArrayList<String> ldkiri = StringUtils.getListFromString(kr);
        if (kananView != null)
            kananView.clear();
        else
            kananView = new ArrayList<>();
        if (KiriView != null)
            KiriView.clear();
        else
            KiriView = new ArrayList<>();
        if (tengahView != null)
            tengahView.clear();
        else
            tengahView = new ArrayList<>();


        View kananroot = null;
        View kananchild = null;
        for (String d : ldkanan) {
            if (d.equals(ICO))
                PosisiIcon = POSISI.KANAN;
            if (kananroot == null) {
                kananroot = posView(SS_V, viewtag, d);
                kananroot.setLayoutParams(RLParam.KANAN());
                MODUtils.AntiGravity(kananroot, Gravity.RIGHT | Gravity.CENTER);
                kananroot.requestLayout();
                kananView.add(kananroot);
            } else {

                if (kananchild == null) {
                    kananchild = posView(SS_V, viewtag, d);
                    kananchild.setLayoutParams(RLParam.LEFT_OF(kananroot
                            .getId()));
                    MODUtils.AntiGravity(kananchild, Gravity.RIGHT | Gravity.CENTER);
                    kananchild.requestLayout();
                    kananView.add(kananchild);
                } else {
                    View baru = posView(SS_V, viewtag, d);
                    if (baru != null) {
                        baru.setLayoutParams(RLParam.LEFT_OF(kananchild.getId()));
                        MODUtils.AntiGravity(baru, Gravity.RIGHT | Gravity.CENTER);
                        baru.requestLayout();
                        kananchild = baru;
                        kananView.add(baru);
                    }
                }

            }
        }

        View kiriroot = null;
        View kirichild = null;
        for (String d : ldkiri) {
            if (d.equals(ICO))
                PosisiIcon = POSISI.KIRI;
            if (kiriroot == null) {
                kiriroot = posView(SS_V, viewtag, d);
                kiriroot.setLayoutParams(RLParam.KIRI());
                MODUtils.AntiGravity(kiriroot, Gravity.LEFT | Gravity.CENTER);
                kiriroot.requestLayout();
                KiriView.add(kiriroot);
            } else {

                if (kirichild == null) {
                    kirichild = posView(SS_V, viewtag, d);
                    kirichild
                            .setLayoutParams(RLParam.RIGHT_OF(kiriroot.getId()));
                    MODUtils.AntiGravity(kirichild, Gravity.LEFT | Gravity.CENTER);
                    kirichild.requestLayout();
                    KiriView.add(kirichild);
                } else {
                    View baru = posView(SS_V, viewtag, d);
                    if (baru != null) {
                        baru.setLayoutParams(RLParam.RIGHT_OF(kirichild.getId()));
                        MODUtils.AntiGravity(baru, Gravity.LEFT | Gravity.CENTER);
                        baru.requestLayout();
                        kirichild = baru;
                        KiriView.add(kirichild);
                    }
                }

            }
        }
        View tengah = null;
        if (!ldtengah.equals("")) {
            if (ldtengah.equals(ICO))
                PosisiIcon = POSISI.TENGAH;
            tengah = posView(SS_V, viewtag, ldtengah);
            tengah.setLayoutParams(RLParam.TENGAH());

            tengah.requestLayout();
            tengahView.add(tengah);
        }

        AturPosisi(PosisiIcon);

    }

    private void AturPosisi(POSISI posisiIcon) {
        SS_NOTIF_ICON.removeAllViews();
        switch (posisiIcon) {
            case KANAN:
                SS_NOTIF_ICON.addView(ICON_MORE, 0);
                SS_NOTIF_ICON.addView(ICONMERGER, 1);


                break;
            case KIRI:
            case TENGAH:
                SS_NOTIF_ICON.addView(ICONMERGER, 0);
                SS_NOTIF_ICON.addView(ICON_MORE, 1);


                break;
        }
        SS_NOTIF_ICON.requestLayout();
    }

}
