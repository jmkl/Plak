package hello.dcsms.plak;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.RLParam;
import hello.dcsms.plak.Utils.StringUtils;

public class ModSystemUI {
	ClassLoader mClassloader;
	private static String JAM = "12:00PM", BAT = "B", TRF = "666K/s",
			SIG = "D", ICO = "EF", STAT_IC = "G";
	static RelativeLayout RL_ROOT = null;
	static LinearLayout SS_JAM;
	static LinearLayout SS_NOTIF_ICON;
	static LinearLayout SS_STATUSBARICONS;
	static TextView SS_TRAFIK;
	static LinearLayout SS_SINYAL;
	static LinearLayout SS_BATRAI;
	static private int BATTID = 555666;
	static private int JAMID = 555667;
	private XSharedPreferences pref;
	private static InitPackageResourcesParam resparam;

	public ModSystemUI(InitPackageResourcesParam resparam) {

		ModSystemUI.resparam = resparam;
		initPackageResource(resparam);
		JabatTangandenganLayout();
	}

	private void JabatTangandenganLayout() {

		resparam.res.hookLayout(C.NAMAPAKET_SYSTEMUI, "layout", "status_bar_simple",
				new XC_LayoutInflated() {

					@Override
					public void handleLayoutInflated(LayoutInflatedParam liparam)
							throws Throwable {
						if(RL_ROOT!=null)
							return;
						RL_ROOT = (RelativeLayout) liparam.view
								.findViewById(liparam.res.getIdentifier(
										"icons", "id", C.NAMAPAKET_SYSTEMUI));
						// 0

						TextView SS_JAM_ORI = (TextView) RL_ROOT.getChildAt(0);
						// 1
						SS_NOTIF_ICON = (LinearLayout) RL_ROOT.getChildAt(1);
						// 2
						SS_STATUSBARICONS = (LinearLayout) RL_ROOT
								.getChildAt(2);
						// 2.0
						ImageView SS_MOREICON = (ImageView) SS_STATUSBARICONS
								.getChildAt(0);
						// 2.1
						SS_TRAFIK = (TextView) SS_STATUSBARICONS.getChildAt(1);

						// 2.2
						LinearLayout SS_STATUSICON = (LinearLayout) SS_STATUSBARICONS
								.getChildAt(2);

						// 2.3
						SS_SINYAL = (LinearLayout) SS_STATUSBARICONS
								.getChildAt(3);

						// 2.3.0
						ViewGroup SS_SINYALCLUSTER1 = (ViewGroup) SS_SINYAL
								.getChildAt(0);
						// 2.3.1
						ViewGroup SS_SINYALCLUSTER2 = (ViewGroup) SS_SINYAL
								.getChildAt(1);

						ImageView SS_BATRAI_IMG = (ImageView) SS_STATUSBARICONS
								.getChildAt(4);

						TextView SS_BATRAI_NUM = (TextView) SS_STATUSBARICONS
								.getChildAt(5);

						SS_BATRAI = new LinearLayout(RL_ROOT.getContext());
						SS_BATRAI.setLayoutParams(RLParam.WRAP_KONTEN());
						SS_BATRAI.setId(BATTID);

						// removeadd
						SS_STATUSBARICONS.removeView(SS_TRAFIK);
						RL_ROOT.addView(SS_TRAFIK);
						SS_SINYAL.removeView(SS_SINYALCLUSTER1);
						SS_SINYAL.removeView(SS_SINYALCLUSTER2);
						SS_STATUSBARICONS.removeView(SS_SINYAL);
						SS_SINYAL.addView(SS_SINYALCLUSTER1);
						SS_SINYAL.addView(SS_SINYALCLUSTER2);
						RL_ROOT.addView(SS_SINYAL);
						RL_ROOT.removeView(SS_JAM_ORI);
						SS_JAM = new LinearLayout(RL_ROOT.getContext());
						SS_JAM.setLayoutParams(RLParam.WRAP_KONTEN());
						SS_JAM.setId(JAMID);
						SS_JAM.addView(SS_JAM_ORI);
						RL_ROOT.addView(SS_JAM);

						SS_STATUSBARICONS.removeView(SS_BATRAI_IMG);
						SS_STATUSBARICONS.removeView(SS_BATRAI_NUM);
						SS_BATRAI.addView(SS_BATRAI_IMG);
						SS_BATRAI.addView(SS_BATRAI_NUM);
						RL_ROOT.addView(SS_BATRAI);

						AntiGravity(SS_JAM, Gravity.CENTER_VERTICAL);
						AntiGravity(SS_NOTIF_ICON, Gravity.CENTER_VERTICAL);
						AntiGravity(SS_STATUSBARICONS, Gravity.CENTER_VERTICAL);
						AntiGravity(SS_TRAFIK, Gravity.CENTER_VERTICAL);
						AntiGravity(SS_SINYAL, Gravity.CENTER_VERTICAL);
						AntiGravity(SS_BATRAI, Gravity.CENTER_VERTICAL);
						// inisiasi leot
						inisiasiLeot();
						setSBLayoutFromPreferences();

						Debugger.logcat(
								"hookLayout(C.SYSUI, layout, status_bar_simple",
								"ISCALLED");

					}
				});

	}

	private static void inisiasiLeot() {
		SS_JAM.setLayoutParams(RLParam.KANAN());
		SS_BATRAI.setLayoutParams(RLParam.LEFT_OF(SS_JAM.getId()));
		SS_SINYAL.setLayoutParams(RLParam.LEFT_OF(SS_BATRAI.getId()));
		SS_STATUSBARICONS.setLayoutParams(RLParam.LEFT_OF(SS_SINYAL.getId()));
		SS_TRAFIK.setLayoutParams(RLParam.LEFT_OF(SS_STATUSBARICONS.getId()));
		SS_NOTIF_ICON.setLayoutParams(RLParam.KIRI());
	}

	static void setSBLayoutFromPreferences() {

		XSharedPreferences pref = new XSharedPreferences(C.PAKETPLAK);
		pref.makeWorldReadable();

		ArrayList<String> kanan = StringUtils.getListFromString(pref.getString(
				C.PREF_SB_KANAN, "B,G,EF"));
		ArrayList<String> kiri = StringUtils.getListFromString(pref.getString(
				C.PREF_SB_KIRI, "D,666K/s"));
		String tengah = pref.getString(C.PREF_SB_TENGAH, "12:00PM");
		FormatUlang(kanan, kiri, tengah);

	}

	public void onLoadPackage(LoadPackageParam lpparam) {
		mClassloader = lpparam.classLoader;

		try {
			XposedHelpers
					.findAndHookMethod(
							Class.forName("com.android.systemui.statusbar.phone.PhoneStatusBar"),
							"onConfigurationChanged", Configuration.class,
							new XC_MethodHook() {
								@Override
								protected void afterHookedMethod(
										MethodHookParam param) throws Throwable {
									super.afterHookedMethod(param);

									setSBLayoutFromPreferences();
								}
							});
		} catch (ClassNotFoundException e) {

			Debugger.logcat(
					"com.android.systemui.statusbar.phone.PhoneStatusBar onConfigurationChanged",
					e.getMessage());
		}

	}

	public void initPackageResource(InitPackageResourcesParam resparam) {

		resparam.res.hookLayout(C.NAMAPAKET_SYSTEMUI, "layout", "status_bar_simple",
				new XC_LayoutInflated() {

					@Override
					public void handleLayoutInflated(LayoutInflatedParam liparam)
							throws Throwable {
						Context c = liparam.view.getContext();
						IntentFilter filter = new IntentFilter();
						filter.addAction(C.MODSTATUSBAR);
						c.registerReceiver(receiver, filter);

					}
				});

	}

	private static BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context c, Intent it) {
			if (it.getAction().equals(C.MODSTATUSBAR)) {
				Debugger.logcat("BROADCAST", "onReceive(Context c, Intent it)");

				// Process.sendSignal(Process.myPid(), Process.SIGNAL_KILL);
				setSBLayoutFromPreferences();
			}

		}

	};

	private static void AntiGravity(View v, int g) {

		if (v instanceof RelativeLayout)
			((RelativeLayout) v).setGravity(g);
		else if (v instanceof LinearLayout)
			((LinearLayout) v).setGravity(g);
		else if (v instanceof TextView)
			((TextView) v).setGravity(g);

	}

	private static int posView(String[] tumpukanJerami, String jerami) {
		for (int i = 0; i < tumpukanJerami.length; i++) {
			if (jerami.equals(tumpukanJerami[i]))
				return i;
		}
		return -1;
	}

	private static View posView(View[] v, String[] tumpukanJerami, String jerami) {
		int pos = posView(tumpukanJerami, jerami);
		if (pos > -1)
			return v[pos];
		else
			return null;
	}

	private static void FormatUlang(ArrayList<String> ldkanan,
			ArrayList<String> ldkiri, String ldtengah) {

		Debugger.logcat("FormatUlang", "ONFORMAT ULANG");
		Debugger.logcat("ldkiri", ldkiri.toString());
		Debugger.logcat("kanan", ldkanan.toString());
		Debugger.logcat("tengah", ldtengah);
		View[] SS_V = new View[] { // which
		SS_JAM, // 0
				SS_NOTIF_ICON, // 1
				SS_STATUSBARICONS, // 2
				SS_TRAFIK, // 3
				SS_SINYAL, // 4
				SS_BATRAI }; // 5
		String[] viewtag = new String[] { // tag
		JAM, // 0
				ICO, // 1
				STAT_IC,// 2
				TRF, // 3
				SIG, // 4
				BAT };// 5

		View kananroot = null;
		View kananchild = null;
		for (String d : ldkanan) {
			if (kananroot == null) {
				kananroot = posView(SS_V, viewtag, d);
				kananroot.setLayoutParams(RLParam.KANAN());
				AntiGravity(kananroot, Gravity.RIGHT | Gravity.CENTER);
				kananroot.requestLayout();
			} else {

				if (kananchild == null) {
					kananchild = posView(SS_V, viewtag, d);
					kananchild.setLayoutParams(RLParam.LEFT_OF(kananroot
							.getId()));
					AntiGravity(kananchild, Gravity.RIGHT | Gravity.CENTER);
					kananchild.requestLayout();
				} else {
					View baru = posView(SS_V, viewtag, d);
					if (baru != null) {
						baru.setLayoutParams(RLParam.LEFT_OF(kananchild.getId()));
						AntiGravity(baru, Gravity.RIGHT | Gravity.CENTER);
						baru.requestLayout();
						kananchild = baru;
					}
				}

			}
		}

		View kiriroot = null;
		View kirichild = null;
		for (String d : ldkiri) {
			if (kiriroot == null) {
				kiriroot = posView(SS_V, viewtag, d);
				kiriroot.setLayoutParams(RLParam.KIRI());
				AntiGravity(kiriroot, Gravity.LEFT | Gravity.CENTER);
				kiriroot.requestLayout();
			} else {

				if (kirichild == null) {
					kirichild = posView(SS_V, viewtag, d);
					kirichild
							.setLayoutParams(RLParam.RIGHT_OF(kiriroot.getId()));
					AntiGravity(kirichild, Gravity.LEFT | Gravity.CENTER);
					kirichild.requestLayout();
				} else {
					View baru = posView(SS_V, viewtag, d);
					if (baru != null) {
						baru.setLayoutParams(RLParam.RIGHT_OF(kirichild.getId()));
						AntiGravity(baru, Gravity.LEFT | Gravity.CENTER);
						baru.requestLayout();
						kirichild = baru;
					}
				}

			}
		}
		if (!ldtengah.equals("")) {
			View tengah = posView(SS_V, viewtag, ldtengah);
			tengah.setLayoutParams(RLParam.TENGAH());
			tengah.requestLayout();
		}

		//

	}

	public static void HandleInitPackageResources(
			InitPackageResourcesParam resparams) {
		resparam = resparams;
		resparam.res.hookLayout(C.NAMAPAKET_SYSTEMUI, "layout", "status_bar_simple",
				new XC_LayoutInflated() {

					@Override
					public void handleLayoutInflated(LayoutInflatedParam liparam)
							throws Throwable {

						RL_ROOT = (RelativeLayout) liparam.view
								.findViewById(liparam.res.getIdentifier(
										"icons", "id", C.NAMAPAKET_SYSTEMUI));
						// 0

						TextView SS_JAM_ORI = (TextView) RL_ROOT.getChildAt(0);
						// 1
						SS_NOTIF_ICON = (LinearLayout) RL_ROOT.getChildAt(1);
						// 2
						SS_STATUSBARICONS = (LinearLayout) RL_ROOT
								.getChildAt(2);
						// 2.0
						ImageView SS_MOREICON = (ImageView) SS_STATUSBARICONS
								.getChildAt(0);
						// 2.1
						SS_TRAFIK = (TextView) SS_STATUSBARICONS.getChildAt(1);

						// 2.2
						LinearLayout SS_STATUSICON = (LinearLayout) SS_STATUSBARICONS
								.getChildAt(2);

						// 2.3
						SS_SINYAL = (LinearLayout) SS_STATUSBARICONS
								.getChildAt(3);

						// 2.3.0
						ViewGroup SS_SINYALCLUSTER1 = (ViewGroup) SS_SINYAL
								.getChildAt(0);
						// 2.3.1
						ViewGroup SS_SINYALCLUSTER2 = (ViewGroup) SS_SINYAL
								.getChildAt(1);

						ImageView SS_BATRAI_IMG = (ImageView) SS_STATUSBARICONS
								.getChildAt(4);

						TextView SS_BATRAI_NUM = (TextView) SS_STATUSBARICONS
								.getChildAt(5);

						SS_BATRAI = new LinearLayout(RL_ROOT.getContext());
						SS_BATRAI.setLayoutParams(RLParam.WRAP_KONTEN());
						SS_BATRAI.setId(BATTID);

						// removeadd
						SS_STATUSBARICONS.removeView(SS_TRAFIK);
						RL_ROOT.addView(SS_TRAFIK);
						SS_SINYAL.removeView(SS_SINYALCLUSTER1);
						SS_SINYAL.removeView(SS_SINYALCLUSTER2);
						SS_STATUSBARICONS.removeView(SS_SINYAL);
						SS_SINYAL.addView(SS_SINYALCLUSTER1);
						SS_SINYAL.addView(SS_SINYALCLUSTER2);
						RL_ROOT.addView(SS_SINYAL);
						RL_ROOT.removeView(SS_JAM_ORI);
						SS_JAM = new LinearLayout(RL_ROOT.getContext());
						SS_JAM.setLayoutParams(RLParam.WRAP_KONTEN());
						SS_JAM.setId(JAMID);
						SS_JAM.addView(SS_JAM_ORI);
						RL_ROOT.addView(SS_JAM);

						SS_STATUSBARICONS.removeView(SS_BATRAI_IMG);
						SS_STATUSBARICONS.removeView(SS_BATRAI_NUM);
						SS_BATRAI.addView(SS_BATRAI_IMG);
						SS_BATRAI.addView(SS_BATRAI_NUM);
						RL_ROOT.addView(SS_BATRAI);

						AntiGravity(SS_JAM, Gravity.CENTER_VERTICAL);
						AntiGravity(SS_NOTIF_ICON, Gravity.CENTER_VERTICAL);
						AntiGravity(SS_STATUSBARICONS, Gravity.CENTER_VERTICAL);
						AntiGravity(SS_TRAFIK, Gravity.CENTER_VERTICAL);
						AntiGravity(SS_SINYAL, Gravity.CENTER_VERTICAL);
						AntiGravity(SS_BATRAI, Gravity.CENTER_VERTICAL);
						// inisiasi leot

						Context c = liparam.view.getContext();
						IntentFilter filter = new IntentFilter();
						filter.addAction(C.MODSTATUSBAR);
						c.registerReceiver(receiver, filter);

						Debugger.logcat(
								"hookLayout(C.SYSUI, layout, status_bar_simple",
								"ISCALLED");

						inisiasiLeot();
						setSBLayoutFromPreferences();

					}
				});

	}
}
