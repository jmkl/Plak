package hello.dcsms.plak;

import android.os.Environment;

public class C {
	public static final String PLAK_SD_ROOT = Environment.getExternalStorageDirectory() + "/Plak";
	public static final String PLAK_SS_MOCKUP_ROOT = Environment.getExternalStorageDirectory() + "/Plak/Mockup Template/";
	public static final String PLAK_SS_ROOT = Environment.getExternalStorageDirectory() + "/Plak/Screenshoots/";

	public static final String ENABLE_FRAME_SS="customize_ss";
	public static final String KEEPMIHOMEINMEMORY = "keep_in_memory" ;
    public static final String ORDERSIM = "hello.dcsms.plak.ORDERSIM";
    public static final String PREF_CARRIER_1 ="carrier_1" ;
    public static final String PREF_CARRIER_2 ="carrier_2" ;
	public static final String MOCKUP_PATH = "mockup_template_path";
	public static final String MAX_NOTIF_NUM = "max_notif_num";
	public static final String OVERRIDE_ORI_SS_NOTIF = "ss_notif_ori";
	public static final String DISABLENAG = "disable_nag";
	public static final String PLAK_VERSION = "current_version";
	public static final String PREF_CARRIERTEXT1 = "pref_carrier_text1";
	public static final String PREF_CARRIERTEXT2 = "pref_carrier_text2";
	public static final String ENABLE_CUSTOM_CARRIER = "enable_custom_carrier";
	public static final String MOD_EXPANDED_STATUSBAR ="hello.dcsms.plak.MODSTATUSBAREXPANDED" ;
	public static final String PREF_XPANDED_ICON_VIEW ="prefs_sb_expanded_view" ;
	public static final String SSASMUCHASUCAN = "hello.dcsms.plak.SSMASAL";
	public static final String SS_DIKIRIM ="hello.dcsms.plak.SS" ;

	public static String PLAKMANUALSETTINGSFILE = Environment
			.getDataDirectory()
			+ "/data/hello.dcsms.plak/config/setting.plakconf";
	public static String PLAK_CARRIERIMAGEFOLDER = Environment
			.getDataDirectory()
			+ "/data/hello.dcsms.plak/carrier/";
	public static String PLAK_CARRIERIMAGE_SIM1 = Environment
			.getDataDirectory()
			+ "/data/hello.dcsms.plak/carrier/sim1";
	public static String PLAK_CARRIERIMAGE_SIM2 = Environment
			.getDataDirectory()
			+ "/data/hello.dcsms.plak/carrier/sim2";
	public static String PLAK_CARRIERIMAGE_SIM1_DARK = Environment
			.getDataDirectory()
			+ "/data/hello.dcsms.plak/carrier/sim1dark";
	public static String PLAK_CARRIERIMAGE_SIM2_DARK = Environment
			.getDataDirectory()
			+ "/data/hello.dcsms.plak/carrier/sim2dark";
	public static final String MODSTATUSBARUPDATECARRIERONLY ="hello.dcsms.plak.MODCARRIER" ;
	public static final String MODMIHOME = "hello.dcsms.plak.MODMIHOME";
	public static final String MODSTATUSBAR = "hello.dcsms.plak.MODSYSTEMUI";
	public static final String MODSTATUSBARRUNTIME = "hello.dcsms.plak.MODSYSTEMUI.RUNTIMEPANTEKAMAKANG";

	public static final String MODSTATUSBAR_ONBOOT = "hello.dcsms.plak.MODSYSTEMUI.ONBOOT";

	public static final String DENSITY = "density_dpi";

	protected static final String PREF_FITICONTEXT = "icon_text_fit";
	public static final String NAMAPAKET_LAINNYA = "lainlain";
	public static final String NAMAPAKET_SYSTEMUI = "com.android.systemui";
	public static final String ENABLEMODSYSUI = "mod_system_ui";
	public static final String ENABLEMODMIHOME = "mod_mihome_enabler";
	protected static final String STATUSBARLAYOUTBUNDLE = "hadiah_buat_yang_diatas";
	public static final String ENABLEMANUAL_CONFIG = "manual_conf";
	public static final String SBL_KANAN = "hadiah_buat_yang_kanan";
	public static final String SBL_KIRI = "hadiah_buat_yang_kiri";
	public static final String SBL_TENGAH = "hadiah_buat_yang_tengah";
	public static final String PREF_HOTSEAT = "tampilkan_hotseat";
	public static final String FORMATJAM = "custom_format_jam";
	protected static final String MODJAM = "enable_custom_format_jam";

	public static String PREF_WSICONSIZE = "workspace_icon_hi";

	public static String NAMAPAKET_MIHOME = "com.miui.home";
	public static String PAKETPLAK = "hello.dcsms.plak";
	public static String PAKETPLAK_MAINCONTENT = "hello.dcsms.plak.MainContent";
	public static String PREF_COLX = "horizontal_col_num";
	public static String PREF_COLY = "vertical_col_num";
	public static String PREF_ICONSHAD = "icon_shadow";
	public static String PREF_ICONSIZE = "icon_size";
	public static String PREF_ICONTITLE = "icon_launcher_text";
	public static String PREF_ICONTEXTSIZE = "icon_text_size";
	public static String PREF_WSPACE_W = "wspace_w";
	public static String PREF_SB_KANAN = "sb_kanan_cepet";
	public static String PREF_SB_KIRI = "sb_kiri_cepet";
	public static String PREF_SB_TENGAH = "sb_tengah_cepet";
	public static String PREF_SB_BATTERYORDER = "switch_batt_icon_n_text";
    public static String PREF_WS_WIDTH="ws_width";
    public static String PREF_WS_HEIGHT="ws_height";
	public static String PREF_SH_JAM = "pref_sh_jam";
	public static String PREF_SH_SINYAL = "pref_sh_sinyal";
	public static String PREF_SH_NOTIF = "pref_sh_notif";
	public static String PREF_SH_ICON = "pref_sh_icon";
	public static String PREF_SH_BATT = "pref_sh_battery";

	public static String PREF_LAYOUT_MOBILE_ICON = "pref_mobile_icon";
	public static String PREF_LAYOUT_WIFI_ICON = "pref_wifi_icon";
	public static final String PREF_LAYOUT_SIM_ORDER = "pref_sim_order";

	public static int[] DIMENS = new int[] { R.dimen.dimen0, R.dimen.dimen1,
			R.dimen.dimen2, R.dimen.dimen3, R.dimen.dimen4, R.dimen.dimen5,
			R.dimen.dimen6, R.dimen.dimen7, R.dimen.dimen8, R.dimen.dimen9,
			R.dimen.dimen10, R.dimen.dimen11, R.dimen.dimen12, R.dimen.dimen13,
			R.dimen.dimen14, R.dimen.dimen15, R.dimen.dimen16, R.dimen.dimen17,
			R.dimen.dimen18, R.dimen.dimen19, R.dimen.dimen20, R.dimen.dimen21,
			R.dimen.dimen22, R.dimen.dimen23, R.dimen.dimen24, R.dimen.dimen25,
			R.dimen.dimen26, R.dimen.dimen27, R.dimen.dimen28, R.dimen.dimen29,
			R.dimen.dimen30, R.dimen.dimen31, R.dimen.dimen32, R.dimen.dimen33,
			R.dimen.dimen34, R.dimen.dimen35, R.dimen.dimen36, R.dimen.dimen37,
			R.dimen.dimen38, R.dimen.dimen39, R.dimen.dimen40, R.dimen.dimen41,
			R.dimen.dimen42, R.dimen.dimen43, R.dimen.dimen44, R.dimen.dimen45,
			R.dimen.dimen46, R.dimen.dimen47, R.dimen.dimen48, R.dimen.dimen49,
			R.dimen.dimen50, R.dimen.dimen51, R.dimen.dimen52, R.dimen.dimen53,
			R.dimen.dimen54, R.dimen.dimen55, R.dimen.dimen56, R.dimen.dimen57,
			R.dimen.dimen58, R.dimen.dimen59, R.dimen.dimen60, R.dimen.dimen61,
			R.dimen.dimen62, R.dimen.dimen63, R.dimen.dimen64, R.dimen.dimen65,
			R.dimen.dimen66, R.dimen.dimen67, R.dimen.dimen68, R.dimen.dimen69,
			R.dimen.dimen70, R.dimen.dimen71, R.dimen.dimen72, R.dimen.dimen73,
			R.dimen.dimen74, R.dimen.dimen75, R.dimen.dimen76, R.dimen.dimen77,
			R.dimen.dimen78, R.dimen.dimen79, R.dimen.dimen80, R.dimen.dimen81,
			R.dimen.dimen82, R.dimen.dimen83, R.dimen.dimen84, R.dimen.dimen85,
			R.dimen.dimen86, R.dimen.dimen87, R.dimen.dimen88, R.dimen.dimen89,
			R.dimen.dimen90, R.dimen.dimen91, R.dimen.dimen92, R.dimen.dimen93,
			R.dimen.dimen94, R.dimen.dimen95, R.dimen.dimen96, R.dimen.dimen97,
			R.dimen.dimen98, R.dimen.dimen99, R.dimen.dimen100,
			R.dimen.dimen101, R.dimen.dimen102, R.dimen.dimen103,
			R.dimen.dimen104, R.dimen.dimen105, R.dimen.dimen106,
			R.dimen.dimen107, R.dimen.dimen108, R.dimen.dimen109,
			R.dimen.dimen110, R.dimen.dimen111, R.dimen.dimen112,
			R.dimen.dimen113, R.dimen.dimen114, R.dimen.dimen115,
			R.dimen.dimen116, R.dimen.dimen117, R.dimen.dimen118,
			R.dimen.dimen119, R.dimen.dimen120, R.dimen.dimen121,
			R.dimen.dimen122, R.dimen.dimen123, R.dimen.dimen124,
			R.dimen.dimen125, R.dimen.dimen126, R.dimen.dimen127,
			R.dimen.dimen128, R.dimen.dimen129, R.dimen.dimen130,
			R.dimen.dimen131, R.dimen.dimen132, R.dimen.dimen133,
			R.dimen.dimen134, R.dimen.dimen135, R.dimen.dimen136,
			R.dimen.dimen137, R.dimen.dimen138, R.dimen.dimen139,
			R.dimen.dimen140, R.dimen.dimen141, R.dimen.dimen142,
			R.dimen.dimen143, R.dimen.dimen144, R.dimen.dimen145,
			R.dimen.dimen146, R.dimen.dimen147, R.dimen.dimen148,
			R.dimen.dimen149, R.dimen.dimen150, R.dimen.dimen151,
			R.dimen.dimen152, R.dimen.dimen153, R.dimen.dimen154,
			R.dimen.dimen155, R.dimen.dimen156, R.dimen.dimen157,
			R.dimen.dimen158, R.dimen.dimen159, R.dimen.dimen160,
			R.dimen.dimen161, R.dimen.dimen162, R.dimen.dimen163,
			R.dimen.dimen164, R.dimen.dimen165, R.dimen.dimen166,
			R.dimen.dimen167, R.dimen.dimen168, R.dimen.dimen169,
			R.dimen.dimen170, R.dimen.dimen171, R.dimen.dimen172,
			R.dimen.dimen173, R.dimen.dimen174, R.dimen.dimen175,
			R.dimen.dimen176, R.dimen.dimen177, R.dimen.dimen178,
			R.dimen.dimen179, R.dimen.dimen180, R.dimen.dimen181,
			R.dimen.dimen182, R.dimen.dimen183, R.dimen.dimen184,
			R.dimen.dimen185, R.dimen.dimen186, R.dimen.dimen187,
			R.dimen.dimen188, R.dimen.dimen189, R.dimen.dimen190,
			R.dimen.dimen191, R.dimen.dimen192, R.dimen.dimen193,
			R.dimen.dimen194, R.dimen.dimen195, R.dimen.dimen196,
			R.dimen.dimen197, R.dimen.dimen198, R.dimen.dimen199,
			R.dimen.dimen200, R.dimen.dimen201, R.dimen.dimen202,
			R.dimen.dimen203, R.dimen.dimen204, R.dimen.dimen205,
			R.dimen.dimen206, R.dimen.dimen207, R.dimen.dimen208,
			R.dimen.dimen209, R.dimen.dimen210, R.dimen.dimen211,
			R.dimen.dimen212, R.dimen.dimen213, R.dimen.dimen214,
			R.dimen.dimen215, R.dimen.dimen216, R.dimen.dimen217,
			R.dimen.dimen218, R.dimen.dimen219, R.dimen.dimen220,
			R.dimen.dimen221, R.dimen.dimen222, R.dimen.dimen223,
			R.dimen.dimen224, R.dimen.dimen225, R.dimen.dimen226,
			R.dimen.dimen227, R.dimen.dimen228, R.dimen.dimen229,
			R.dimen.dimen230, R.dimen.dimen231, R.dimen.dimen232,
			R.dimen.dimen233, R.dimen.dimen234, R.dimen.dimen235,
			R.dimen.dimen236, R.dimen.dimen237, R.dimen.dimen238,
			R.dimen.dimen239, R.dimen.dimen240, R.dimen.dimen241,
			R.dimen.dimen242, R.dimen.dimen243, R.dimen.dimen244,
			R.dimen.dimen245, R.dimen.dimen246, R.dimen.dimen247,
			R.dimen.dimen248, R.dimen.dimen249, R.dimen.dimen250,
			R.dimen.dimen251, R.dimen.dimen252, R.dimen.dimen253,
			R.dimen.dimen254, R.dimen.dimen255, R.dimen.dimen256,
			R.dimen.dimen257, R.dimen.dimen258, R.dimen.dimen259,
			R.dimen.dimen260, R.dimen.dimen261, R.dimen.dimen262,
			R.dimen.dimen263, R.dimen.dimen264, R.dimen.dimen265,
			R.dimen.dimen266, R.dimen.dimen267, R.dimen.dimen268,
			R.dimen.dimen269, R.dimen.dimen270, R.dimen.dimen271,
			R.dimen.dimen272, R.dimen.dimen273, R.dimen.dimen274,
			R.dimen.dimen275, R.dimen.dimen276, R.dimen.dimen277,
			R.dimen.dimen278, R.dimen.dimen279, R.dimen.dimen280,
			R.dimen.dimen281, R.dimen.dimen282, R.dimen.dimen283,
			R.dimen.dimen284, R.dimen.dimen285, R.dimen.dimen286,
			R.dimen.dimen287, R.dimen.dimen288, R.dimen.dimen289,
			R.dimen.dimen290, R.dimen.dimen291, R.dimen.dimen292,
			R.dimen.dimen293, R.dimen.dimen294, R.dimen.dimen295,
			R.dimen.dimen296, R.dimen.dimen297, R.dimen.dimen298,
			R.dimen.dimen299, R.dimen.dimen300, R.dimen.dimen301,
			R.dimen.dimen302, R.dimen.dimen303, R.dimen.dimen304,
			R.dimen.dimen305, R.dimen.dimen306, R.dimen.dimen307,
			R.dimen.dimen308, R.dimen.dimen309, R.dimen.dimen310,
			R.dimen.dimen311, R.dimen.dimen312, R.dimen.dimen313,
			R.dimen.dimen314, R.dimen.dimen315, R.dimen.dimen316,
			R.dimen.dimen317, R.dimen.dimen318, R.dimen.dimen319,
			R.dimen.dimen320, R.dimen.dimen321, R.dimen.dimen322,
			R.dimen.dimen323, R.dimen.dimen324, R.dimen.dimen325,
			R.dimen.dimen326, R.dimen.dimen327, R.dimen.dimen328,
			R.dimen.dimen329, R.dimen.dimen330, R.dimen.dimen331,
			R.dimen.dimen332, R.dimen.dimen333, R.dimen.dimen334,
			R.dimen.dimen335, R.dimen.dimen336, R.dimen.dimen337,
			R.dimen.dimen338, R.dimen.dimen339, R.dimen.dimen340,
			R.dimen.dimen341, R.dimen.dimen342, R.dimen.dimen343,
			R.dimen.dimen344, R.dimen.dimen345, R.dimen.dimen346,
			R.dimen.dimen347, R.dimen.dimen348, R.dimen.dimen349,
			R.dimen.dimen350, R.dimen.dimen351, R.dimen.dimen352,
			R.dimen.dimen353, R.dimen.dimen354, R.dimen.dimen355,
			R.dimen.dimen356, R.dimen.dimen357, R.dimen.dimen358,
			R.dimen.dimen359, R.dimen.dimen360, R.dimen.dimen361,
			R.dimen.dimen362, R.dimen.dimen363, R.dimen.dimen364,
			R.dimen.dimen365, R.dimen.dimen366, R.dimen.dimen367,
			R.dimen.dimen368, R.dimen.dimen369, R.dimen.dimen370,
			R.dimen.dimen371, R.dimen.dimen372, R.dimen.dimen373,
			R.dimen.dimen374, R.dimen.dimen375, R.dimen.dimen376,
			R.dimen.dimen377, R.dimen.dimen378, R.dimen.dimen379,
			R.dimen.dimen380, R.dimen.dimen381, R.dimen.dimen382,
			R.dimen.dimen383, R.dimen.dimen384, R.dimen.dimen385,
			R.dimen.dimen386, R.dimen.dimen387, R.dimen.dimen388,
			R.dimen.dimen389, R.dimen.dimen390, R.dimen.dimen391,
			R.dimen.dimen392, R.dimen.dimen393, R.dimen.dimen394,
			R.dimen.dimen395, R.dimen.dimen396, R.dimen.dimen397,
			R.dimen.dimen398, R.dimen.dimen399, R.dimen.dimen400,
			R.dimen.dimen401, R.dimen.dimen402, R.dimen.dimen403,
			R.dimen.dimen404, R.dimen.dimen405, R.dimen.dimen406,
			R.dimen.dimen407, R.dimen.dimen408, R.dimen.dimen409,
			R.dimen.dimen410, R.dimen.dimen411, R.dimen.dimen412,
			R.dimen.dimen413, R.dimen.dimen414, R.dimen.dimen415,
			R.dimen.dimen416, R.dimen.dimen417, R.dimen.dimen418,
			R.dimen.dimen419, R.dimen.dimen420, R.dimen.dimen421,
			R.dimen.dimen422, R.dimen.dimen423, R.dimen.dimen424,
			R.dimen.dimen425, R.dimen.dimen426, R.dimen.dimen427,
			R.dimen.dimen428, R.dimen.dimen429, R.dimen.dimen430,
			R.dimen.dimen431, R.dimen.dimen432, R.dimen.dimen433,
			R.dimen.dimen434, R.dimen.dimen435, R.dimen.dimen436,
			R.dimen.dimen437, R.dimen.dimen438, R.dimen.dimen439,
			R.dimen.dimen440, R.dimen.dimen441, R.dimen.dimen442,
			R.dimen.dimen443, R.dimen.dimen444, R.dimen.dimen445,
			R.dimen.dimen446, R.dimen.dimen447, R.dimen.dimen448,
			R.dimen.dimen449, R.dimen.dimen450, R.dimen.dimen451,
			R.dimen.dimen452, R.dimen.dimen453, R.dimen.dimen454,
			R.dimen.dimen455, R.dimen.dimen456, R.dimen.dimen457,
			R.dimen.dimen458, R.dimen.dimen459, R.dimen.dimen460,
			R.dimen.dimen461, R.dimen.dimen462, R.dimen.dimen463,
			R.dimen.dimen464, R.dimen.dimen465, R.dimen.dimen466,
			R.dimen.dimen467, R.dimen.dimen468, R.dimen.dimen469,
			R.dimen.dimen470, R.dimen.dimen471, R.dimen.dimen472,
			R.dimen.dimen473, R.dimen.dimen474, R.dimen.dimen475,
			R.dimen.dimen476, R.dimen.dimen477, R.dimen.dimen478,
			R.dimen.dimen479, R.dimen.dimen480, R.dimen.dimen481,
			R.dimen.dimen482, R.dimen.dimen483, R.dimen.dimen484,
			R.dimen.dimen485, R.dimen.dimen486, R.dimen.dimen487,
			R.dimen.dimen488, R.dimen.dimen489, R.dimen.dimen490,
			R.dimen.dimen491, R.dimen.dimen492, R.dimen.dimen493,
			R.dimen.dimen494, R.dimen.dimen495, R.dimen.dimen496,
			R.dimen.dimen497, R.dimen.dimen498, R.dimen.dimen499,
			R.dimen.dimen500 };

}
