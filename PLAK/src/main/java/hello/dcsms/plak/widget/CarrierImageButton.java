package hello.dcsms.plak.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringChain;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hello.dcsms.plak.C;
import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.CarrierPreview;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.RLParam;

/**
 * Created by jmkl on 5/16/2015.
 */
public class CarrierImageButton extends FrameLayout implements SpringListener {
    private static final int ROWS = 2;
    private static final int COLS = 2;

    private final List<View> mImageViews = new ArrayList<View>();
    private final List<Point> mPositions = new ArrayList<Point>();
    private final SpringChain mSpringChain = SpringChain.create();
    private final Spring mSpring = SpringSystem
            .create()
            .createSpring()
            .addListener(this)
            .setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(15, 7));

    private int mActiveIndex;
    private int mPadding;
    onCardClickListener listener;

    int prevW=-1,prevH=-1;
    Debugger debug = new Debugger();


    public CarrierImageButton(Context context) {
        super(context);
        inisiasiLayout(context);
    }

    public CarrierImageButton(Context context, AttributeSet set) {
        super(context, set);

        inisiasiLayout(context);
    }

    public CarrierImageButton setCardClickListener(onCardClickListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        render();
    }

    @Override
    public void onSpringAtRest(Spring spring) {

    }

    @Override
    public void onSpringActivate(Spring spring) {

    }

    @Override
    public void onSpringEndStateChange(Spring spring) {

    }

    public void setAllChildText(String[] allChildText) {
        for (int i = 0; i < allChildText.length; i++) {
            setTextInfo(i, allChildText[i]);
        }
    }

    File[] allcarrierfiles = new File[]{
            new File(C.PLAK_CARRIERIMAGE_SIM1_DARK),
            new File(C.PLAK_CARRIERIMAGE_SIM2_DARK),
            new File(C.PLAK_CARRIERIMAGE_SIM1),
            new File(C.PLAK_CARRIERIMAGE_SIM2)
    };

    public void setImageIconIfPossible(int pos, File imagefiles) {
        if(imagefiles.exists() && prevW>0 && prevH>0 ) {
            Bitmap carrier = BitmapFactory.decodeFile(imagefiles.getAbsolutePath());
            if(carrier.getWidth()<1 || carrier.getHeight()<1)
                return;
            Bitmap ewe = CarrierPreview.makepreview(prevW, prevH, carrier,pos);
            carrier.recycle();
            setImageIcon(pos, ewe);
        }
    }
    public void inisiateImageIfPossible(String[] allmenus) {
        for (int i = 0; i < allmenus.length; i++) {
            File imagefiles = allcarrierfiles[i];
                if(imagefiles.exists() && prevW>0 && prevH>0) {
                    Bitmap carrier =  BitmapFactory.decodeFile(imagefiles.getAbsolutePath());
                    Bitmap ewe = CarrierPreview.makepreview(prevW,prevH,carrier, i);
                    carrier.recycle();
                    setImageIcon(i, ewe);
                }
        }
    }

    public interface onCardClickListener {
        void onClick(View v);
    }

    private OnClickListener onCardClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (listener != null)
                listener.onClick(view);

        }
    };

    public void setImageIcon(int pos, Bitmap bmp) {
        RelativeLayout me = (RelativeLayout) this.getChildAt(pos);
        ImageView iv = (ImageView) me.findViewById(R.id.carrier_image_iv);
        iv.setImageBitmap(bmp);
    }

    public void setTextInfo(int pos, String info) {
        RelativeLayout card = (RelativeLayout) this.getChildAt(pos);
        TextView tv = (TextView) card.findViewById(R.id.carrier_image_tv);
        tv.setText(info+" preview");
    }


    int iv_id = -1, tv_id = -2;


    public void inisiasiLayout(Context context) {

        int viewCount = ROWS * COLS;
        for (int i = 0; i < viewCount; i++) {
            final int j = i;

            final RelativeLayout rl = new RelativeLayout(context);
            rl.setBackgroundColor(Color.BLACK);
            ImageView imageView = new ImageView(context);
            imageView.setId(R.id.carrier_image_iv);
            TextView tv = new TextView(context);
            tv.setTextAppearance(context, R.style.txtKecil_Putiah);
            if(i<2)
                tv.setTextColor(Color.BLACK);
            else
                tv.setTextColor(Color.parseColor("#ffdddddd"));
            tv.setId(R.id.carrier_image_tv);
            tv.setPadding(10,10,10,10);
            imageView.setImageResource(R.drawable.logo);
            imageView.setLayoutParams(RLParam.TENGAH());
            rl.addView(imageView);
            rl.addView(tv);
            mImageViews.add(rl);
            addView(rl);
            rl.setAlpha(0f);


            // Add a click listener to handle scaling up the view.
            rl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onClick(v);
                    int endValue = mSpring.getEndValue() == 0 ? 1 : 0;
                    rl.bringToFront();
                    mActiveIndex = j;
                    mSpring.setEndValue(endValue);
                }
            });

            // Add a spring to the SpringChain to do an entry animation.
            mSpringChain.addSpring(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    render();
                }
            });
        }

        // Wait for layout.
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                layout();
                prevW = getWidth()/ROWS;
                prevH = getHeight()/COLS;
                getViewTreeObserver().removeOnGlobalLayoutListener(this);

                postOnAnimationDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSpringChain.setControlSpringIndex(0).getControlSpring().setEndValue(1);
                    }
                }, 500);
            }
        });

    }

    private void render() {

        for (int i = 0; i < mImageViews.size(); i++) {
            View imageView = mImageViews.get(i);
            if (mSpring.isAtRest() && mSpring.getCurrentValue() == 0) {
                // Performing the initial entry transition animation.
                Spring spring = mSpringChain.getAllSprings().get(i);
                float val = (float) spring.getCurrentValue();
                imageView.setScaleX(val);
                imageView.setScaleY(val);
                imageView.setAlpha(val);
                Point pos = mPositions.get(i);
                imageView.setTranslationX(pos.x);
                imageView.setTranslationY(pos.y);
            } else {
                // Scaling up a photo to fullscreen size.
                Point pos = mPositions.get(i);
                if (i == mActiveIndex) {
                    float ww = imageView.getWidth();
                    float hh = imageView.getHeight();
                    float sx = getWidth() / ww;
                    float sy = getHeight() / hh;
                    float s = sx > sy ? sx : sy;

                    float centerX = (getWidth() - ww) / 2;
                    float centerY = (getHeight() - hh) / 2;

                    float xlatX = (float) SpringUtil.mapValueFromRangeToRange(mSpring.getCurrentValue(), 0, 1, pos.x, 0);
                    float xlatY = (float) SpringUtil.mapValueFromRangeToRange(mSpring.getCurrentValue(), 0, 1, pos.y, 0);
                    imageView.setPivotX(0);
                    imageView.setPivotY(0);
                    imageView.setTranslationX(xlatX);
                    imageView.setTranslationY(xlatY);

                    float ss = (float) SpringUtil.mapValueFromRangeToRange(mSpring.getCurrentValue(), 0, 1, 1, s);
                    imageView.setScaleX(ss);
                    imageView.setScaleY(ss);
                } else {
                    float val = (float) Math.max(0.2, 1 - mSpring.getCurrentValue());
                    imageView.setAlpha(val);
                }
            }
        }
    }

    private void layout() {
        float width = getWidth();
        float height = getHeight();

        // Determine the size for each image given the screen dimensions.
        Resources res = getResources();
        mPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, res.getDisplayMetrics());
        int colWidth = (int) Math.ceil((width - 2 * mPadding) / COLS) - 2 * mPadding;
        int rowHeight = (int) Math.ceil((height - 2 * mPadding) / ROWS) - 2 * mPadding;

        // Determine the resting position for each view.
        int k = 0;
        int py = 0;
        for (int i = 0; i < ROWS; i++) {
            int px = 0;
            py += mPadding * 2;
            for (int j = 0; j < COLS; j++) {
                px += mPadding * 2;
                View imageView = mImageViews.get(k);
                imageView.setLayoutParams(new LayoutParams(colWidth, rowHeight));
                mPositions.add(new Point(px, py));
                px += colWidth;
                k++;
            }
            py += rowHeight;
        }
    }

}
