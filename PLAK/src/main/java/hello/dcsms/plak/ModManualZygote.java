package hello.dcsms.plak;

import android.content.res.XModuleResources;
import android.content.res.XResources;
import android.graphics.Color;

import java.util.List;

import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.manual.ManualItemData;

/**
 * Created by jmkl on 4/30/2015.
 */
public class ModManualZygote {
    public static void setReplacement(String tipe, String namaField, Object nilai){
        XResources.setSystemWideReplacement("android", tipe,namaField,nilai);
    }
}
