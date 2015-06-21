package hello.dcsms.plak.Frgmnt;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringChain;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.facebook.rebound.ui.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
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
import hello.dcsms.plak.ss.SSFrameData;
import hello.dcsms.plak.ss.WallpeperCuser;
import hello.dcsms.plak.ss.lazylist.LocalImageLoader;
import hello.dcsms.plak.widget.CustomButton;
import hello.dcsms.plak.widget.MToast;
import miui.yellowpage.ImageLoader;

/**
 * Created by jmkl on 5/11/2015.
 */
public class SSFragmentDetail extends Activity {

    @InjectViews({R.id.templ_img, R.id.templ_title, R.id.templ_info, R.id.templ_apply, R.id.templ_wallp, R.id.templ_layout})
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
    int mLeftDelta;
    int mTopDelta;
    float mWidthScale;
    float mHeightScale;
    Spring mSpring;
    int thumbnailHeight;
    int thumbnailWidth;
    int thumbnailTop;
    int thumbnailLeft;
    SSFrameData ssframe;
    String path;
    private HashMap<String, Object> data;
    LocalImageLoader loader;
    Bundle bundle;

    public int WALLPAPER=1001,DELETE=1002,APPLY=1003;
    public class BundleDetail implements Serializable {
        String PATH;
        int ID;
        boolean HASCHANGE;
        int WHAT;
    }

    int RETURNRESULT = RESULT_CANCELED;
    BundleDetail bundelresult = new BundleDetail();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.mcardview);
        ButterKnife.inject(this);


        bundle = getIntent().getExtras();
        thumbnailTop = bundle.getInt(".top");
        thumbnailLeft = bundle.getInt(".left");
        thumbnailWidth = bundle.getInt(".width");
        thumbnailHeight = bundle.getInt(".height");
        data = (HashMap<String, Object>) bundle.getSerializable("data");

        ssframe = (SSFrameData) data.get("data");
        path = (String) data.get("path");
        bundelresult.PATH = path;
        bundelresult.ID = bundle.getInt("viewid");
        bundelresult.HASCHANGE = false;
        bundelresult.WHAT=-100;
        loader = new LocalImageLoader(this);

        loader.DisplayImage(path, templImg);

        templTitle.setText(ssframe.getSsframe());
        String tambahan = "";
        if (ssframe.getSs_x() != bundle.getInt(".x") || ssframe.getSs_y() != bundle.getInt(".y"))
            tambahan = "your screensize is not equal with this template";
        templInfo.setText(String.format("screenshot size : %d x %d\n%s", ssframe.getSs_x(), ssframe.getSs_y(), tambahan));


        int[] screenLocation = new int[2];
        templImg.getLocationOnScreen(screenLocation);
        mLeftDelta = thumbnailLeft - screenLocation[0];
        mTopDelta = thumbnailTop - screenLocation[1];
        mSpring = SpringSystem
                .create()
                .createSpring()
                .addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        super.onSpringUpdate(spring);
                        runEnterAnimation();
                        if (type == TYPE.FINISH && spring.isAtRest()) {
                            finish();
                        }
                    }
                })
                .setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(40, 5));


        // Scale factors to make the large version the same size as the thumbnail
        mWidthScale = (float) thumbnailWidth / templImg.getWidth();
        mHeightScale = (float) thumbnailHeight / templImg.getHeight();

        if (savedInstanceState == null) {
            ViewTreeObserver observer = templImg.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {

                    templImg.getViewTreeObserver().removeOnPreDrawListener(this);

                    // Figure out where the thumbnail and full size versions are, relative
                    // to the screen and each other
                    int[] screenLocation = new int[2];
                    templImg.getLocationOnScreen(screenLocation);
                    mLeftDelta = thumbnailLeft - screenLocation[0];
                    mTopDelta = thumbnailTop - screenLocation[1];

                    // Scale factors to make the large version the same size as the thumbnail
                    mWidthScale = (float) thumbnailWidth / templImg.getWidth();
                    mHeightScale = (float) thumbnailHeight / templImg.getHeight();


                    mSpring.setEndValue(1);
                    runEnterAnimation();

                    return true;
                }
            });

        }
    }

    public boolean deleteRec(String path) {
        File f = new File(path);
        try {
            if (f.exists() && f.isDirectory()) {
                File[] file = f.listFiles();
                for (File x : file) {
                    if (x.isDirectory())
                        deleteRec(x.getAbsolutePath());
                    else if (x.isFile())
                        x.delete();
                    else
                        return false;
                }
                f.delete();
            }
        } catch (Exception ex) {
            return false;
        }


        return true;
    }

    public PopupMenu.OnMenuItemClickListener onMenuClick = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.item_template_delete:
                    deleteRec(path);
                    bundelresult.HASCHANGE = true;
                    RETURNRESULT = RESULT_OK;
                    bundelresult.WHAT=DELETE;
                    onBackPressed();
                    break;
                case R.id.item_template_apply:
                    onClick1();
                    break;
                case R.id.item_template_wallp:
                    onClick2();
                    break;
                default:
                    return false;
            }
            return true;
        }
    };

    @Override
    protected void onDestroy() {
        if (loader != null) loader.freeMemory();
        super.onDestroy();
    }

    @OnClick(R.id.preview_menu)
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.item_template_menu);
        popup.setOnMenuItemClickListener(onMenuClick);
        popup.show();
    }

    public void onClick1() {
        PrefUtils pref = new PrefUtils(SSFragmentDetail.this);
        pref.edit(C.MOCKUP_PATH, data.get("path"));
        MToast.show(SSFragmentDetail.this, "Success");
        bundelresult.HASCHANGE = true;
        RETURNRESULT = RESULT_OK;
        bundelresult.WHAT=APPLY;

        onBackPressed();
    }

    int CODE_WALLPAPER = 123;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CODE_WALLPAPER) {

            if (loader != null) {
                loader = new LocalImageLoader(this);
                loader.ReloadCurrent(path, templImg);
                bundelresult.HASCHANGE = true;
                RETURNRESULT = RESULT_OK;
                bundelresult.WHAT=WALLPAPER;
                templImg.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                },500);

            }
        }
    }

    public void onClick2() {
        if (ssframe == null) return;
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
        Intent i = new Intent();

        i.putExtra("path", bundelresult.PATH);
        i.putExtra("ischange", bundelresult.HASCHANGE);
        i.putExtra("id",bundelresult.ID);
        i.putExtra("what",bundelresult.WHAT);
        setResult(RETURNRESULT, i);
        mSpring.setEndValue(0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    enum TYPE {
        FINISH, START, ANON, WALLPAPERCHOOSER
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
                (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, mWidthScale, 1);
        float yscale =
                (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, mHeightScale, 1);
        xscale = Math.max(xscale, 0); // Clamp the value so we don't go below 0.
        yscale = Math.max(yscale, 0);
        templImg.setScaleX(xscale);
        templImg.setScaleY(yscale);

        // Map the spring to the selected photo translation from its position in the grid
        float selectedPhotoTranslateX = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, mLeftDelta, 0);
        float selectedPhotoTranslateY = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, mTopDelta, 0);
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
