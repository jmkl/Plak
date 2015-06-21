package hello.dcsms.plak.Frgmnt;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import hello.dcsms.plak.C;
import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.PrefUtils;
import hello.dcsms.plak.Utils.Utils;
import hello.dcsms.plak.data.PlakZip;
import hello.dcsms.plak.ss.LoadSSTemplateTask;
import hello.dcsms.plak.ss.SSFrameData;
import hello.dcsms.plak.ss.WallpeperCuser;
import hello.dcsms.plak.ss.lazylist.LocalImageLoader;
import hello.dcsms.plak.task.iface.OnTaskCaput;
import hello.dcsms.plak.widget.CustomButton;
import hello.dcsms.plak.widget.MToast;
import hello.dcsms.plak.widget.PlakConf;

/**
 * Created by jmkl on 5/11/2015.
 */
public class ImportTemplate extends Activity {
    @InjectViews({R.id.templ_img,R.id.templ_title,R.id.templ_info,R.id.templ_apply,R.id.templ_wallp,R.id.templ_layout})
    List<View> allview;
    @InjectView(R.id.templ_img)
    ImageView templImg;
    @InjectView(R.id.templ_title)
    TextView templTitle;
    @InjectView(R.id.templ_info)
    TextView templInfo;
    @InjectView(R.id.templ_apply)
    CustomButton templApply;
    @InjectView(R.id.templ_wallp)
    CustomButton templWallp;
    @InjectView(R.id.templ_layout)
    RelativeLayout templLayout;

    @InjectView(R.id.templ_root)
    RelativeLayout templRoot;

    Spring mSpring ;
    SSFrameData ssframe;
    String path;
    private HashMap<String,Object> data;
    LocalImageLoader loader;

    Point p;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mcardview);
        ButterKnife.inject(this);
        Display ds = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        p = new Point();
        ds.getSize(p);

        Uri d = getIntent().getData();
        if(d!=null) {

            PlakZip plak = new PlakZip();
            plak.CekZipFile(this, d.getPath(), new OnTaskCaput() {

                @Override
                public void onFinish() {
                }

                @Override
                public void onFinish(Object result) {
                    if(result!=null){
                        DoInisiasi((String) result, savedInstanceState);
                    }else finish();
                }
            });
        }else
            finish();


    }
    Debugger debug = new Debugger();
    void DoInisiasi(String result ,Bundle savedInstanceState){

        path = result+"/";
        debug.log("DoInisiasi",path);
        ssframe = LoadSSTemplateTask.configParser(new File(result+"/prop.plk"));

        if(ssframe==null)
            finish();
        debug.log("GSON",new Gson().toJson(ssframe,SSFrameData.class));

        loader = new LocalImageLoader(this);
        loader.DisplayImage(path,templImg);
        templTitle.setText(ssframe.getSsframe());
        String tambahan = "";
        if (ssframe.getSs_x() != p.x || ssframe.getSs_y() != p.y)
            tambahan = "your screensize is not equal with this template";
        templInfo.setText(String.format("screenshot size : %d x %d\n%s", ssframe.getSs_x(), ssframe.getSs_y(), tambahan));


        mSpring= SpringSystem
                .create()
                .createSpring()
                .addListener(new SimpleSpringListener(){
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        super.onSpringUpdate(spring);
                        runEnterAnimation();
                        if(type== TYPE.FINISH && spring.isAtRest()){

                            finish();
                        }
                    }
                })
                .setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(40, 5));


        // Scale factors to make the large version the same size as the thumbnail

        if (savedInstanceState == null) {
            ViewTreeObserver observer = templImg.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {

                    templImg.getViewTreeObserver().removeOnPreDrawListener(this);
                    mSpring.setEndValue(1);
                    runEnterAnimation();
                    return true;
                }
            });

        }
    }


    @OnClick (R.id.templ_apply)
    public void onClick1(){
        PrefUtils pref = new PrefUtils(ImportTemplate.this);
        pref.edit(C.MOCKUP_PATH, data.get("path"));
        MToast.show(ImportTemplate.this, "Success");
    }

    int CODE_WALLPAPER=123;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==CODE_WALLPAPER){
            new Debugger().log("RESULT OK CODE OK");
            if(loader!=null){
                loader = new LocalImageLoader(this);
                loader.ReloadCurrent(path,templImg);
            }
        }
    }

    @OnClick (R.id.templ_wallp)
    public void onClick2(){
        if(ssframe==null)return;
        Intent it = new Intent(this, WallpeperCuser.class);
        Bundle b = new Bundle();
        b.putInt(WallpeperCuser.KEY_H, ssframe.getSsframedata().getBg_height());
        b.putInt(WallpeperCuser.KEY_W, ssframe.getSsframedata().getBg_width());
        b.putString(WallpeperCuser.KEY_WALLPIMAGE, path + "/" + ssframe.getSsframedata().getBackground());
        it.putExtra(WallpeperCuser.BUNDLE_KEY, b);
        startActivityForResult(it, CODE_WALLPAPER);

    }
    @Override
    public void onBackPressed() {
        type = TYPE.FINISH;
        mSpring.setEndValue(0);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
    enum TYPE {
        FINISH,START,ANON,WALLPAPERCHOOSER
    }
    TYPE type = TYPE.ANON;
    private void runEnterAnimation() {
        final long duration = (long) (500);
        templImg.setPivotX(0);
        templImg.setPivotY(0);
        // Set starting values for properties we're going to animate. These
        // values scale and position the full size version down to the thumbnail
        // size/location, from which we'll animate it back up

        Resources resources = getResources();
        // Get the current spring value.
        double value = mSpring.getCurrentValue();

        float alpha =
                (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0f, 1f);
        templImg.setAlpha(alpha);

        // Map the spring to the selected photo scale as it moves into and out of the grid.
        float xscale =
                (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0, 1);
        float yscale =
                (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0,1);
        xscale = Math.max(xscale, 0); // Clamp the value so we don't go below 0.
        yscale = Math.max(yscale, 0);
        templImg.setScaleX(xscale);
        templImg.setScaleY(yscale);

        // Map the spring to the selected photo translation from its position in the grid
        float selectedPhotoTranslateX = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0, 0);
        float selectedPhotoTranslateY = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, templRoot.getHeight(), 0);
        templImg.setTranslationX(selectedPhotoTranslateX);
        templImg.setTranslationY(selectedPhotoTranslateY);

        float mtl = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, templLayout.getHeight(), 0);

        templLayout.setTranslationY(mtl);


//        templImg.setScaleX(mWidthScale);
//        templImg.setScaleY(mHeightScale);
//        templImg.setTranslationX(mLeftDelta);
//        templImg.setTranslationY(mTopDelta);

        // We'll fade the text in later
        //mTextView.setAlpha(0);

        // Animate scale and translation to go from thumbnail to full size
//        templImg.animate().setDuration(duration).
//                scaleX(1).scaleY(1).
//                translationX(0).translationY(0).
//                setInterpolator(sDecelerator).
//                withEndAction(new Runnable() {
//                    public void run() {
//                        // Animate the description in after the image animation
//                        // is done. Slide and fade the text in from underneath
//                        // the picture.
//                    }
//                });

//        // Fade in the black background
//        ObjectAnimator bgAnim = ObjectAnimator.ofInt(mBackground, "alpha", 0, 255);
//        bgAnim.setDuration(duration);
//        bgAnim.start();
//
//        // Animate a color filter to take the image from grayscale to full color.
//        // This happens in parallel with the image scaling and moving into place.
//        ObjectAnimator colorizer = ObjectAnimator.ofFloat(PictureDetailsActivity.this,
//                "saturation", 0, 1);
//        colorizer.setDuration(duration);
//        colorizer.start();
//
//        // Animate a drop-shadow of the image
//        ObjectAnimator shadowAnim = ObjectAnimator.ofFloat(mShadowLayout, "shadowDepth", 0, 1);
//        shadowAnim.setDuration(duration);
//        shadowAnim.start();
    }


}
