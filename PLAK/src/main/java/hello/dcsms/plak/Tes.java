package hello.dcsms.plak;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import hello.dcsms.plak.Utils.Debugger;
/**
 * Created by jmkl on 4/27/2015.
 */
public class Tes extends Activity {
    static{
        System.loadLibrary("bitmaputils");
    }

    Debugger d = new Debugger();
    public static native String def();
    public  static native String publish();
    public static void fixPref() {
        File shit = new File(
                Environment.getDataDirectory()
                        + "/data/hello.dcsms.plak/shared_prefs/hello.dcsms.plak_preferences.xml");
        if (shit.exists()) {
            shit.setReadable(true, false);
        }
    }

    @Override
    public void onBackPressed() {
        fixPref();
        super.onBackPressed();

    }




    void initActionBar() {
        ActionBar ab = getActionBar();
        ab.setLogo(R.drawable.logo);
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayUseLogoEnabled(true);
    }



    public String Bundleable(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "hello.dcsms.plak",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                String def = (Base64.encodeToString(md.digest(), Base64.DEFAULT)).trim();
                return def;
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        return null;
    }

    //32120112;
    void calculate(Debugger d){}




    //TODO MUSTCHANGE ONPUBLISH
    static boolean debug = false;
    public static String getkey(){
        if(debug)
            return def();
        else
            return publish();
    }

    void explain(){
        if(!(Bundleable().equals(getkey())))
            finish();
    }

    //keytool -exportcert -alias <RELEASE_KEY_ALIAS> -keystore <RELEASE_KEY_PATH> | openssl sha1 -binary | openssl base64
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Debugger d = new Debugger(true);
        calculate(d);
        d.log((Bundleable().equals(publish())));
        runOnUiThread(new Runnable() {
           @Override
           public void run() {
               explain();
           }
       });



    }


}
