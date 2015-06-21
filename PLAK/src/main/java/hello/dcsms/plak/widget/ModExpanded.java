package hello.dcsms.plak.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import hello.dcsms.plak.C;
import hello.dcsms.plak.MODUtils;
import hello.dcsms.plak.Utils.Debugger;

/**
 * Created by jmkl on 5/17/2015.
 */
public class ModExpanded {
    static boolean ENABLE = false;
    private static String super_status_bar = "super_status_bar";
    private static String sb_expanded = "status_bar_expanded";

    //statusbarsimple
    private static String battery_indicator = "battery_indicator";
    private static String battery_indicator_charging = "battery_indicator_charging";
    private static String icons = "icons";
    private static String clock = "clock";
    private static String notification_icon_area = "notification_icon_area";
    private static String statusbar_icon = "statusbar_icon";
    private static String moreIcon = "moreIcon";
    private static String network_speed_view = "network_speed_view";
    private static String statusIcons = "statusIcons";
    private static String signal_cluster_container = "signal_cluster_container";
    private static String battery = "battery";
    private static String battery_num = "battery_num";

    private static FrameLayout includeLayout;
    private static String[] allviews = {"battery_indicator", "battery_indicator_charging", "icons", "clock", "notification_icon_area", "statusbar_icon", "moreIcon", "network_speed_view", "statusIcons", "signal_cluster_container", "battery", "battery_num"};
    private static List<View> allChildView;
    private static  Context mContext;


    public static void onLoadPakage(XC_LoadPackage.LoadPackageParam par) {
        if(!ENABLE)return;
    }


    private static BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(C.MOD_EXPANDED_STATUSBAR)){
                isTimeforUpdate();
            }
        }
    };



    static boolean findViewPosition(String key,Set<String> arrai){
        for(String ini:arrai){
            if(ini.equals(key))
                return true;
        }
        return false;
    }

    public static void isTimeforUpdate() {
        if(!ENABLE)return;
        if(allChildView==null)return;


        Set<String> whichView = (Set<String>) MODUtils.getXPrefs(C.PREF_XPANDED_ICON_VIEW,allviews);

        for (int i = 0; i < allChildView.size(); i++) {
            allChildView.get(i).setVisibility(findViewPosition(allviews[i],whichView)?View.VISIBLE:View.GONE);
        }
    }

    public static void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam par) {
        if(!ENABLE)return;
        final Debugger d = new Debugger();
        try {


            par.res.hookLayout(C.NAMAPAKET_SYSTEMUI, "layout", super_status_bar, new XC_LayoutInflated() {
                @Override
                public void handleLayoutInflated(LayoutInflatedParam param) throws Throwable {
                    if (includeLayout == null) {
                        FrameLayout leot_statubar_expanded = (FrameLayout) param.view.findViewById(param.res.getIdentifier(sb_expanded, "id", C.NAMAPAKET_SYSTEMUI));
                        includeLayout = (FrameLayout) leot_statubar_expanded.findViewById(param.res.getIdentifier("status_bar_simple_expanded", "id", C.NAMAPAKET_SYSTEMUI));
/*              <com.android.systemui.statusbar.phone.BatteryIndicator android:id="@id/battery_indicator"/>
                <com.android.systemui.statusbar.phone.BatteryIndicatorCharging android:id="@id/battery_indicator_charging" />
                <RelativeLayout android:id="@id/icons" >
                <com.android.systemui.statusbar.policy.Clock android:id="@id/clock"  />
                <LinearLayout  android:id="@id/notification_icon_area" >
                </LinearLayout>
                <com.android.systemui.statusbar.phone.StatusBarIcons  android:id="@id/statusbar_icon">
                <com.android.systemui.statusbar.StatusBarIconView android:id="@id/moreIcon"     />
                <com.android.systemui.statusbar.NetworkSpeedView android:id="@id/network_speed_view"/>
                <LinearLayout   android:id="@id/statusIcons"   />
                <LinearLayout  android:id="@id/signal_cluster_container"    >
                </LinearLayout>
                <com.android.systemui.statusbar.phone.BatteryStatusIconView android:id="@id/battery"   />
                <TextView   android:id="@id/battery_num"/>
                </com.android.systemui.statusbar.phone.StatusBarIcons>
                </RelativeLayout>*/

                        if (allChildView == null)
                            allChildView = new ArrayList<View>();
                        for (int i = 0; i < allviews.length; i++) {
                            allChildView.add(includeLayout.findViewById(param.res.getIdentifier(allviews[i], "id", C.NAMAPAKET_SYSTEMUI)));
                        }


                        d.log("allChildView",allChildView.size());

                        mContext = param.view.getContext();
                        IntentFilter filter = new IntentFilter();
                        filter.addAction(C.MOD_EXPANDED_STATUSBAR);
                        mContext.registerReceiver(mReceiver, filter);
                    }
                }
            });

        }catch (Throwable t){
            t.printStackTrace();

            d.log(t.getMessage());
        }
    }


}
