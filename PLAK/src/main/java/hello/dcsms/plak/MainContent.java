package hello.dcsms.plak;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringChain;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.Command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import butterknife.OnTouch;
import hello.dcsms.plak.Frgmnt.DDFrag;
import hello.dcsms.plak.Test.Test;
import hello.dcsms.plak.Utils.Miui;
import hello.dcsms.plak.Utils.ParcleData;
import hello.dcsms.plak.Utils.PrefUtils;
import hello.dcsms.plak.Utils.PreparationTask;
import hello.dcsms.plak.Utils.Utils;
import hello.dcsms.plak.online.OnlineActivity;
import hello.dcsms.plak.widget.LoadingDialog;
import hello.dcsms.plak.widget.NotifyDialog;

import static hello.dcsms.plak.TestClass.Test;

/**
 * Created by jmkl on 5/10/2015.
 */
public class MainContent extends Activity {
    enum SState {
        PAUSE, RESUME, DESTROY, KLIK
    }

    private static String[] tips = new String[]{
            "You can override any of value on every application on your device by using Manual Settings. for more info about Manual Settings, please visit this link : http://bit.ly/Plak_MS",
            "Your support help us keep update",
            "You can post any feedback & suggestion in our facebook page http://fb.com/helloplak",
            "The file format for Screenshoot template is [*.pz]. It's just an ordinary zip file. You can find how make one of your template. Visit this link for more iufo http://bit.ly/Plak_SS",
            "You can purchase key to remove popup dialog by contact our facebook page http://fb.com/helloplak",
            "To Plak or Not To Plak",
            "Whatever this is not the best mod thought but it's the only one around, so be it"


    };

    @InjectViews({R.id.glob_menu_sett, R.id.glob_menu_layoutsett, R.id.glob_menu_help, R.id.glob_menu_about})
    List<ImageView> ivs;
    Spring spring;
    Spring cSpring;
    View curview = null;
    @InjectView(R.id.mainconten_root)
    LinearLayout root_layout;
    SpringSystem springSystem;
    SpringChain springChain = SpringChain.create();
    @InjectView(R.id.glob_txt_version_code)
    TextView version_kode;
    @InjectView(R.id.glob_txt_info)
    TextView txt_info;
    List<View> allviews;
    @InjectView(R.id.glob_layout_hello)
    LinearLayout hello;
    LoadingDialog loadingdialog;

    SState STATE;


    long TIPS_DELAYED = 5000;
    Handler tipsHandler = new Handler();
    Runnable runTips = new Runnable() {
        @Override
        public void run() {
            if (root_layout != null)
                cSpring.setCurrentValue(-root_layout.getWidth());
            if (txt_info != null)
                txt_info.setText(randomTips());
            cSpring.setEndValue(1);
            tipsHandler.postDelayed(runTips, TIPS_DELAYED);
        }
    };

    static String randomTips() {
        Random r = new Random();
        return tips[r.nextInt(tips.length)];
    }

    //TODOPLAK HANDLE SHIT
    boolean isRootDevice() {
        boolean isroot = RootTools.isRootAvailable();
        if (isroot)
            return RootTools.isAccessGiven();
        else
            return false;
    }
    private boolean EXIST() {
        File f = new File(C.PLAK_SS_MOCKUP_ROOT + "Redmi 1S");
        File f2 = new File(C.PLAK_SD_ROOT + "/autocomplete.db");
        return f.exists() && f2.exists();
    }
    boolean darkmode = false;

    private int setAlphaForARGB(int argbColor, int alpha) {
        int r = Color.red(argbColor);
        int b = Color.blue(argbColor);
        int g = Color.green(argbColor);
        return Color.argb(alpha, r, g, b);
    }
    Test test= new Test();
    @OnClick (R.id.logotoes)
    public void onCrot(View v){
        int[] mIconColors = { Color.parseColor("#80000000"), Color.parseColor("#99ffffff") };
        int mAlphaFilter = 100;
        int color = setAlphaForARGB(mIconColors[darkmode ? 0 : 1], mAlphaFilter);
        int alpha = Color.alpha(mIconColors[darkmode ? 0 : 1]) + 255 - mAlphaFilter;
        alpha = alpha < 255 ? alpha : 255;
        ((ImageView)v).setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        v.setAlpha(alpha);
        if(!test.isRunning())
              test.start();
        else
            test.stop();



      darkmode =!darkmode;

    }
    File whichconf = null;
    String xposed_config=null;
    void tes() {
        String plak_apk = null;
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> plak = pm.getInstalledApplications(0);
        for (ApplicationInfo ai : plak) {
            if (ai.packageName.equals("hello.dcsms.plak")) {
                plak_apk = ai.sourceDir;
            }
        }
        String config = null;
        File conf = new File(Environment.getDataDirectory()
                + "/data/pro.burgerz.wsm.manager/conf/modules.list");
        File confxposed = new File(Environment.getDataDirectory()
                + "/data/de.robv.android.xposed.installer/conf/modules.list");


        if(conf.exists())
            whichconf=conf;
        else if(confxposed.exists())
            whichconf=confxposed;





        if (whichconf.exists()) {
            try {
                BufferedReader bf = new BufferedReader(new FileReader(whichconf));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = bf.readLine()) != null) {
                    sb.append(line + "\n");
                }
                bf.close();
                xposed_config = sb.toString();
                for (String string : xposed_config.split("\n")) {
                    if (string.length() > 0
                            && string.contains("hello.dcsms.plak"))
                        config = string;
                }



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        int version = Miui.getMIUIVersion();

        if (plak_apk != null && config != null) {
            if (!plak_apk.trim().equals(config.trim())) {
                boolean success = fcukthoseConfigFile(plak_apk);
                if(!success)
                    new NotifyDialog(this)
                            .setTitle("Hi")
                            .setMessage(
                                    String.format(
                                            "PLAK installed in %s, otherwise in wsm modules.list registered as %s. u should edit those /data/data/pro.burgerz.wsm.manager/conf/modules.list line as %s",
                                            plak_apk, config, plak_apk)).show();
            }

        }
        if (config == null)
            new NotifyDialog(this)

                    .setTitle("Hi")
                    .setMessage(
                            "Ups, seems like PLAK is not enabled. Please enable it on WSM manager")
                    .isCancelable(false).show();

        PrefUtils pref = new PrefUtils(this);
        int plak_version = pref.getLatestVersion();
        int current_version  = getResources().getInteger(R.integer.kodeversi);
        if(current_version>plak_version){
            new NotifyDialog(this)
                    .setTitle("Hi")
                    .setMessage(getResources().getString(R.string.changelog,current_version))
                    .isCancelable(true).show();

            pref.edit(C.PLAK_VERSION,current_version);
        }

    }

    private boolean fcukthoseConfigFile(String plak_apk) {
        if(!whichconf.canExecute()){
            Command cmd = new Command(0, "chmod 777 "+whichconf.getAbsolutePath()) {
                @Override
                public void output(int i, String s) {}
            };
            try {
                RootTools.getShell(true).add(cmd).waitForFinish();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (TimeoutException e) {
                e.printStackTrace();
                return false;
            } catch (RootDeniedException e) {
                e.printStackTrace();
                return false;
            }
        }
        boolean result = false;
        String x = "\\/data\\/app\\/hello.dcsms.plak-\\d\\.apk";
        Pattern pattern = Pattern.compile(x,Pattern.MULTILINE);

        Matcher matcher = pattern.matcher(xposed_config);
        while (matcher.find()){

            String r = matcher.group();
            String all = matcher.replaceAll(plak_apk);
            try {
                FileWriter fw = new FileWriter(whichconf);
                fw.write(all);
                fw.flush();
                fw.close();
                whichconf.setReadable(true, false);
                whichconf.setWritable(true, false);
                whichconf.setExecutable(true, false);
                result = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        return result;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maincontent);
        ButterKnife.inject(this);
        new Test().TestSetStringPreferences(this);
        if (savedInstanceState == null) {
            tes();
            boolean isroot = isRootDevice();
            if (!isroot)
                Toast.makeText(MainContent.this,
                        getResources().getString(R.string.root_info),
                        Toast.LENGTH_LONG).show();


            if (!EXIST())
                new PreparationTask(MainContent.this).execute();
        }





        version_kode.setText(getResources().getString(R.string.versi, getResources().getString(R.string.readable_versi)));
        txt_info.setText(randomTips());
        // Create a system to run the physics loop for a set of springs.
        springSystem = SpringSystem.create();
        // Add a spring to the system.
        spring = springSystem.createSpring();
        cSpring = springSystem.createSpring();
        spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(120, 6));
        cSpring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(120, 6));
        allviews = new ArrayList<>();
        allviews.addAll(ivs);
        allviews.add(version_kode);
        allviews.add(txt_info);
        allviews.add(hello);
        for (final View iv : allviews) {
            springChain.addSpring(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    float value = (float) spring.getCurrentValue();
                    iv.setTranslationY(value);
                }
            });
        }


        root_layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                root_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                List<Spring> springs = springChain.getAllSprings();
                for (int i = 0; i < springs.size(); i++) {
                    if (allviews.get(i) == hello)
                        springs.get(i).setCurrentValue(-root_layout.getHeight());
                    else
                        springs.get(i).setCurrentValue(root_layout.getHeight());
                }
                root_layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        springChain
                                .setControlSpringIndex(0)
                                .getControlSpring()
                                .setEndValue(0);
                    }
                }, 500);
            }
        });

    }

    @OnClick({R.id.glob_menu_sett, R.id.glob_menu_layoutsett, R.id.glob_menu_help, R.id.glob_menu_about})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.glob_menu_sett:
                Intent intent = new Intent(MainContent.this, Hello.class);
                startActivity(intent);
                break;
            case R.id.glob_menu_layoutsett:
                Intent intent2 = new Intent(MainContent.this, DDFrag.class);
                startActivity(intent2);
                break;
            case R.id.glob_menu_help:
                break;
            case R.id.glob_menu_about:
                Intent intent4 = new Intent(MainContent.this, OnlineActivity.class);
                startActivity(intent4);
                break;

        }
    }

    @OnTouch({R.id.glob_menu_sett, R.id.glob_menu_layoutsett, R.id.glob_menu_help, R.id.glob_menu_about})
    boolean onTouchEvent(View v, MotionEvent event) {
        curview = v;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                spring.setEndValue(1);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                STATE = SState.KLIK;
                loadingdialog = new LoadingDialog(this);
                loadingdialog.show();
                spring.setEndValue(0);
                break;
        }
        return true;
    }

    MSpringListener mSpringListener = new MSpringListener();
    CustSprListener cuslistener;

    class CustSprListener extends SimpleSpringListener {
        private View v;

        public CustSprListener(View v) {
            this.v = v;
        }

        @Override
        public void onSpringUpdate(Spring spring) {
            super.onSpringUpdate(spring);
            float value = (float) spring.getCurrentValue();
            float scale = 1f - (value * 0.5f);
            v.setTranslationX(scale);
        }
    }

    class MSpringListener extends SimpleSpringListener {

        @Override
        public void onSpringUpdate(Spring spring) {
            // You can observe the updates in the spring
            // state by asking its current value in onSpringUpdate.

            if (curview != null) {
                render(curview);

                if (STATE == SState.KLIK && spring.isAtRest()) {
                    root_layout.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            curview.performClick();
                            if(loadingdialog.isShowing())
                                loadingdialog.dismiss();
                        }
                    },1);

                }
            }


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        STATE = SState.RESUME;
        cuslistener = new CustSprListener(txt_info);
        cSpring.addListener(cuslistener);
        spring.addListener(mSpringListener);
        tipsHandler.postDelayed(runTips, TIPS_DELAYED);
        springChain
                .setControlSpringIndex(0)
                .getControlSpring().setEndValue(0);
    }

    @Override
    protected void onDestroy() {
        if (!ParcleData.cek(this)) {
            Intent i = new Intent(MainContent.this, Applican.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        STATE = SState.PAUSE;

        root_layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (cuslistener != null)
                    cSpring.removeListener(cuslistener);
                spring.removeListener(mSpringListener);
                tipsHandler.removeCallbacks(runTips);
                springChain
                        .getControlSpring()
                        .setCurrentValue(root_layout.getHeight());
            }
        }, 500);
    }

    private void setScaleall(float scale) {
        for (ImageView iv : ivs) {
            iv.setScaleX(scale);
            iv.setScaleX(scale);
        }
    }


    private void render(View v) {
        float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.5);
        v.setScaleX(mappedValue);
        v.setScaleY(mappedValue);

    }


}
