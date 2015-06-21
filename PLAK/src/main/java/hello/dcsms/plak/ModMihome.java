package hello.dcsms.plak;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.XModuleResources;
import android.graphics.Color;
import android.os.Process;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.manual.ManualDataJsonHelper;
import hello.dcsms.plak.manual.ManualItemData;

public class ModMihome {
	InitPackageResourcesParam resparam;
	XSharedPreferences pref;
	ClassLoader mClassloader;
	private XModuleResources modres;

	public ModMihome(InitPackageResourcesParam resparam,
			XModuleResources mXModuleResources) {
		this.resparam = resparam;
		this.modres = mXModuleResources;
		initPackageResource(resparam);
	}

	public void initPackageResource(InitPackageResourcesParam resparam) {

		resparam.res.hookLayout(C.NAMAPAKET_MIHOME, "layout", "launcher",
				new XC_LayoutInflated() {

					@Override
					public void handleLayoutInflated(LayoutInflatedParam liparam)
							throws Throwable {
						Context c = liparam.view.getContext();
						IntentFilter filter = new IntentFilter();
						filter.addAction(C.MODMIHOME);
						c.registerReceiver(receiver, filter);

					}
				});

		MODMeNOW();

	}

	@SuppressWarnings("unused")
	private void ReplaceValue(String type, String item, Object value) {
		try {
			resparam.res.setReplacement(C.NAMAPAKET_MIHOME, type, item, value);
		} catch (Exception e) {

		}
	}

	private void MODMeNOW() {

		pref = new XSharedPreferences(C.PAKETPLAK);
		pref.makeWorldReadable();

		int col_x = Integer.parseInt(pref.getString(C.PREF_COLX, "4"));
		int col_y = Integer.parseInt(pref.getString(C.PREF_COLY, "5"));
		boolean iconshadow = pref.getBoolean(C.PREF_ICONSHAD, false);

		final int dens = (pref.getInt(C.DENSITY, 320)) / 160;

		resparam.res.setReplacement(C.NAMAPAKET_MIHOME, "integer",
				"config_cell_count_x", col_x);
		resparam.res.setReplacement(C.NAMAPAKET_MIHOME, "integer",
				"config_cell_count_y", col_y);
		resparam.res.setReplacement(C.NAMAPAKET_MIHOME, "bool",
				"config_enable_icon_shadow", iconshadow);

		//int ws = Integer.parseInt(pref.getString(C.PREF_WSICONSIZE, "100"));

		boolean showHotSeat = pref.getBoolean(C.PREF_HOTSEAT, true);

		resparam.res.setReplacement(C.NAMAPAKET_MIHOME, "dimen",
				"hotseats_height",
				modres.fwd(showHotSeat ? R.dimen.hotseats_height : R.dimen.dimennull));

		resparam.res.setReplacement(C.NAMAPAKET_MIHOME, "dimen",
				"workspace_cell_padding_bottom",
				modres.fwd(showHotSeat ? R.dimen.workspace_cell_padding_bottom
						: R.dimen.dimen25));
		resparam.res
				.setReplacement(
						C.NAMAPAKET_MIHOME,
						"dimen",
						"workspace_indicator_margin_bottom",
						modres.fwd(showHotSeat ? R.dimen.workspace_indicator_margin_bottom
								: R.dimen.dimen2));


        //teksize
        int txtsize = Integer
                .parseInt(pref.getString(C.PREF_ICONTEXTSIZE, "7"));
			resparam.res.setReplacement(C.NAMAPAKET_MIHOME, "dimen",
					"workspace_icon_text_size", modres.fwd(C.DIMENS[txtsize]));



        int ws_w = Integer.parseInt(pref.getString(C.PREF_WS_WIDTH,"70"));
        int ws_h= Integer.parseInt(pref.getString(C.PREF_WS_HEIGHT,"76"));
			resparam.res.setReplacement(C.NAMAPAKET_MIHOME, "dimen",
					"workspace_cell_height", modres.fwd(C.DIMENS[ws_h]));
        resparam.res.setReplacement(C.NAMAPAKET_MIHOME, "dimen",
                "workspace_cell_width", modres.fwd(C.DIMENS[ws_w]));
        final int iconsize = Integer.parseInt(pref.getString(C.PREF_ICONSIZE,"60"))*dens;
		resparam.res.hookLayout(C.NAMAPAKET_MIHOME, "layout", "icon_title",
				new XC_LayoutInflated() {

					@Override
					public void handleLayoutInflated(LayoutInflatedParam liparam)
							throws Throwable {
						TextView icon_title = (TextView) liparam.view.findViewById(liparam.res
								.getIdentifier("icon_title", "id",
										C.NAMAPAKET_MIHOME));

						boolean showicontext = pref.getBoolean(
								C.PREF_ICONTITLE, true);

						icon_title.setVisibility(showicontext ? View.VISIBLE
								: View.INVISIBLE);

					}
				});
		resparam.res.hookLayout(C.NAMAPAKET_MIHOME, "layout", "application",
				new XC_LayoutInflated() {

					@Override
					public void handleLayoutInflated(LayoutInflatedParam liparam)
							throws Throwable {
						ImageView icon = (ImageView) liparam.view
								.findViewById(liparam.res.getIdentifier(
										"icon_icon", "id", C.NAMAPAKET_MIHOME));
						FrameLayout.LayoutParams par = new FrameLayout.LayoutParams(
								iconsize, iconsize, Gravity.CENTER);
						icon.setScaleType(ScaleType.FIT_CENTER);
						icon.setLayoutParams(par);
						// Debugger.log("HELLO OnAPPLIACATION HOOK");

					}
				});
		resparam.res.hookLayout(C.NAMAPAKET_MIHOME, "layout", "folder_icon",
				new XC_LayoutInflated() {

					@Override
					public void handleLayoutInflated(LayoutInflatedParam liparam)
							throws Throwable {
						ImageView icon = (ImageView) liparam.view
								.findViewById(liparam.res.getIdentifier(
										"icon_icon", "id", C.NAMAPAKET_MIHOME));
						LinearLayout lo = (LinearLayout) liparam.view
								.findViewById(liparam.res.getIdentifier(
										"preview_icons_container", "id",
										C.NAMAPAKET_MIHOME));
						FrameLayout.LayoutParams par = new FrameLayout.LayoutParams(
								iconsize, iconsize, Gravity.CENTER);
						icon.setScaleType(ScaleType.FIT_CENTER);
						FrameLayout.LayoutParams par2 = new FrameLayout.LayoutParams(
								iconsize - (10 * dens), iconsize - (10 * dens),
								Gravity.CENTER);
						icon.setLayoutParams(par);
						lo.setLayoutParams(par2);

					}
				});
		if (mClassloader != null) {

		}
		// kaga bisa anuin dimen pke integer
		// List<ItemData> data = ParseConfig.get();
		// for (ItemData i : data) {
		// Debugger.log("Hello ItemData" + i.getItem());
		// Object value = null;
		// if (i.getTipe().equals("boolean"))
		// value = i.getIntValue();
		// else if (i.getTipe().equals("string"))
		// value = i.getStrValue();
		// else if (i.getTipe().equals("integer"))
		// value = i.getIntValue();
		// else if (i.getTipe().equals("dimen"))
		// value = i.getDimValue();
		// else if (i.getTipe().equals("color"))
		// value = i.getColValue();
		// ReplaceValue(i.getTipe().equals("boolean") ? "bool" : i.getTipe(),
		// i.getItem(), value);
		// }

		boolean isManualModEnable = pref.getBoolean(C.ENABLEMANUAL_CONFIG,
				false);
		if (isManualModEnable) {

			List<ManualItemData> data = ManualDataJsonHelper
					.ReadJson(C.PLAKMANUALSETTINGSFILE);

			if (data.size() > 0) {
				for (ManualItemData mi : data) {

					if (mi.getNamaPaket().equals(C.NAMAPAKET_MIHOME)) {
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
			resparam.res.setReplacement(C.NAMAPAKET_MIHOME, tipe, namafield,
					nilai);
		} catch (Exception e) {

		}
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context c, Intent it) {
			if (it.getAction().equals(C.MODMIHOME)) {

				MODMeNOW();
				Process.sendSignal(Process.myPid(), Process.SIGNAL_KILL);
			}

		}

	};

	public void hndlLoadPackage(LoadPackageParam lpparam) {
		mClassloader = lpparam.classLoader;

	}

}
