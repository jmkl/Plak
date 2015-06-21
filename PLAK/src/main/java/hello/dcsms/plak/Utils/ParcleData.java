package hello.dcsms.plak.Utils;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import hello.dcsms.plak.C;
import hello.dcsms.plak.Hello;
import hello.dcsms.plak.Tes;

/**
 * Created by jmkl on 5/6/2015.
 */
public class ParcleData {

    public static String en(String data) {
        return Base64.encodeToString(data.trim().getBytes(), Base64.DEFAULT);
    }

    public static boolean cek(Context hello) {
        PrefUtils pref = new PrefUtils(hello);
        String serialkey = pref.getPref().getString(C.DISABLENAG,"0");
        String id = android.provider.Settings.Secure.getString(hello.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        String keys = Tes.getkey();
        String ids = ParcleData.en(id).trim();
        String serial = ParcleData.mixmax(keys, ids);
        return (ParcleData.fx(serial).equals(serialkey));
    }

    public static String de(String data) {
        return new String(Base64.decode(data.trim(), Base64.DEFAULT));
    }

    public static String mixmax(String mix, String max) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update((mix + max).getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return mix+max;

    }

    static String[] ok = new String[]{"d", "f", "b", "c", "o", "r", "v", "w", "x", "y", "e", "i", "j", "7", "8", "0", "1", "2", "z", "g", "k", "l", "m", "a", "s", "p", "h", "q", "n", "9", "3", "4", "t", "u", "5", "6"};

    public static String fx(String mixmax) {
        StringBuilder sb = new StringBuilder();
        for(String s:mixmax.split("")){
            sb.append(Integer.toString(find(s)));        }
        String s1 = mixmax.substring(0, 1);
        String s2 = mixmax.substring(1, 2);
        String s3 = mixmax.substring(2, 3);
        String s4 = mixmax.substring(3, 4);
        String s5 = mixmax.substring(4, 5);
        return  sb.toString();//String.format("%d%d%d%d%d",find(s1),find(s2),find(s3),find(s4),find(s5)).trim();
    }

    static int find(String f){
        for (int i = 0; i < ok.length; i++) {
            if(f.equalsIgnoreCase(ok[i]))
                return i;
        }
        return 101;
    }


}
