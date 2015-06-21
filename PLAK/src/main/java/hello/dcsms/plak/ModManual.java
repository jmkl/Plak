package hello.dcsms.plak;

import android.content.res.XModuleResources;
import android.graphics.Color;

import java.util.List;

import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.manual.ManualItemData;

/**
 * Created by jmkl on 4/30/2015.
 */
public class ModManual {
    private static void setReplaceMentShit(String namapaket, XC_InitPackageResources.InitPackageResourcesParam resparam,
                                           String tipe, String namafield, Object nilai) {
        try {
            resparam.res.setReplacement(namapaket, tipe, namafield,
                    nilai);
        } catch (Exception e) {
            Debugger.logcat("PLAK", e.getMessage());
        }
    }

    private static int str2int(String integer) {
        return Integer.parseInt(integer);
    }

    private static boolean str2bool(String bool) {
        return Integer.parseInt(bool) == 1;
    }


    public static void Hajar(XC_InitPackageResources.InitPackageResourcesParam resparam, List<ManualItemData> data, XModuleResources modres) {

        for (ManualItemData mi : data) {
            String paket = mi.getNamaPaket();
            if (mi.getTipe().equals(ManualItemData.TSTRING))
                setReplaceMentShit(paket, resparam, "string",
                        mi.getNamaField(), mi.getNilai());
            else if (mi.getTipe().equals(ManualItemData.TBOOL))
                setReplaceMentShit(paket, resparam, "bool",
                        mi.getNamaField(), str2bool(mi.getNilai()));
            else if (mi.getTipe().equals(ManualItemData.TCOL))
                setReplaceMentShit(paket, resparam, "color",
                        mi.getNamaField(),
                        Color.parseColor(mi.getNilai()));
            else if (mi.getTipe().equals(ManualItemData.TINT))
                setReplaceMentShit(paket, resparam, "integer",
                        mi.getNamaField(), str2int(mi.getNilai()));
            else if (mi.getTipe().equals(ManualItemData.TDIMEN))
                setReplaceMentShit(paket,
                        resparam,
                        "dimen",
                        mi.getNamaField(),
                        modres.fwd(C.DIMENS[str2int(mi.getNilai())]));
        }
    }
}
