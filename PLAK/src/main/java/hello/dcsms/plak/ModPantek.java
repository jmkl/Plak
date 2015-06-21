package hello.dcsms.plak;

import android.content.res.XModuleResources;
import android.content.res.XResources;
import android.graphics.Color;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.manual.ManualDataJsonHelper;
import hello.dcsms.plak.manual.ManualItemData;
import hello.dcsms.plak.widget.ModExpanded;

public class ModPantek implements IXposedHookLoadPackage,
        IXposedHookZygoteInit, IXposedHookInitPackageResources {
    private ModStatusbar ModSUI;
    private String mModulePath;
    private XModuleResources mXModuleResources;
    private ModMihome modmihome;

    public ModPantek() {
    }

    private XSharedPreferences getSharedPreferences() {
        XSharedPreferences localXSharedPreferences = new XSharedPreferences(
                C.PAKETPLAK);
        localXSharedPreferences.makeWorldReadable();
        return localXSharedPreferences;
    }

    @Override
    public void handleInitPackageResources(
            InitPackageResourcesParam par)
            throws Throwable {
        this.mXModuleResources = XModuleResources.createInstance(
                this.mModulePath, par.res);
        boolean bool = getSharedPreferences().getBoolean("mod_system_ui", true);
        boolean boolmihome = getSharedPreferences().getBoolean(
                C.ENABLEMODMIHOME, true);
        if (par.packageName
                .equals(C.NAMAPAKET_MIHOME) && boolmihome) {
            if (this.modmihome == null) {
                this.modmihome = new ModMihome(par,
                        this.mXModuleResources);
            }
        }



        if (par.packageName.equals(C.NAMAPAKET_SYSTEMUI)) {
            ModExpanded.handleInitPackageResources(par);
            if(bool)
                ModSUI = new ModStatusbar(par,
                        this.mXModuleResources);
        }

        boolean isManualModEnable = getSharedPreferences().getBoolean(C.ENABLEMANUAL_CONFIG, false);
        if (isManualModEnable) {

            if (!par.equals(C.NAMAPAKET_MIHOME) || !par.equals(C.NAMAPAKET_SYSTEMUI)) {
                List<ManualItemData> data = ManualDataJsonHelper.ReadJson(C.PLAKMANUALSETTINGSFILE);

                Collections.sort(data, comparator);
                ModManual.Hajar(par, data, mXModuleResources);
            }
        }
    }

    private Comparator<ManualItemData> comparator = new Comparator<ManualItemData>() {
        @Override
        public int compare(ManualItemData manualItemData, ManualItemData t1) {
            return manualItemData.getNamaPaket().compareToIgnoreCase(t1.getNamaPaket());
        }
    };

    @Override
    public void handleLoadPackage(
            XC_LoadPackage.LoadPackageParam par)
            throws Throwable {
        boolean bool = getSharedPreferences().getBoolean("mod_system_ui", true);
        boolean boolmihome = getSharedPreferences().getBoolean(
                C.ENABLEMODMIHOME, true);
        if (par.packageName
                .equals(C.NAMAPAKET_MIHOME) && boolmihome) {
            if (this.modmihome != null) {
                this.modmihome.hndlLoadPackage(par);
            }
        }
        if (par.packageName.equals("com.android.settings")) {
            ModSettings.handleInitPackageResources(par);
        }
        if (par.packageName
                .equals("com.android.systemui") && bool) {
            ModStatusbar.onLoadPackage(par);
            ModExpanded.onLoadPakage(par);
        }

    }

    private static int str2int(String integer) {
        return Integer.parseInt(integer);
    }

    private static boolean str2bool(String bool) {
        return Integer.parseInt(bool) == 1;
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam param)
            throws Throwable {
        this.mModulePath = param.modulePath;

        boolean keep = getSharedPreferences().getBoolean(C.KEEPMIHOMEINMEMORY, false);
        boolean isManualModEnable = getSharedPreferences().getBoolean(C.ENABLEMANUAL_CONFIG, false);
        if (isManualModEnable) {
            List<ManualItemData> data = ManualDataJsonHelper.ReadJson(C.PLAKMANUALSETTINGSFILE);
            for (ManualItemData mi : data) {
                String paket = mi.getNamaPaket();
                String tipe = mi.getTipe();
                if (paket.equals("android")) {
                    if (tipe.equals(ManualItemData.TSTRING))
                        ModManualZygote.setReplacement("string",
                                mi.getNamaField(), mi.getNilai());
                    else if (tipe.equals(ManualItemData.TBOOL))
                        ModManualZygote.setReplacement("bool",
                                mi.getNamaField(), str2bool(mi.getNilai()));
                    else if (tipe.equals(ManualItemData.TCOL))
                        ModManualZygote.setReplacement("color",
                                mi.getNamaField(),
                                Color.parseColor(mi.getNilai()));
                    else if (tipe.equals(ManualItemData.TINT))
                        ModManualZygote.setReplacement("integer",
                                mi.getNamaField(), str2int(mi.getNilai()));
                    else if (tipe.equals(ManualItemData.TDIMEN) && mXModuleResources != null)
                        ModManualZygote.setReplacement(
                                "dimen",
                                mi.getNamaField(),
                                mXModuleResources.fwd(C.DIMENS[str2int(mi.getNilai())]));
                }
            }

        }


        if (keep)
            Resident.KeepMihomeOnMemory();

    }
}

