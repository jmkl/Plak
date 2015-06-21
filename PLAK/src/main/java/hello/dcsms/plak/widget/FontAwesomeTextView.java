package hello.dcsms.plak.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Hashtable;

import hello.dcsms.plak.R;

/**
 * Created by jmkl on 4/25/2015.
 */
public class FontAwesomeTextView extends TextView {
    private static final String TAG = "TextView";

    public FontAwesomeTextView(Context context) {
        super(context);
    }

    public FontAwesomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public FontAwesomeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.AwesomeFont);
        String customFont = a.getString(R.styleable.AwesomeFont_font);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {
            tf = Typefaces.get(ctx, "files/"+asset);
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: " + e.getMessage());
            return false;
        }

        setTypeface(tf);
        return true;
    }
    public static class Typefaces {
        private static final String TAG = "Typefaces";

        private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

        public static Typeface get(Context c, String assetPath) {
            synchronized (cache) {
                if (!cache.containsKey(assetPath)) {
                    try {
                        Typeface t = Typeface.createFromAsset(c.getAssets(),
                                assetPath);
                        cache.put(assetPath, t);
                    } catch (Exception e) {
                        Log.e(TAG, "Could not get typeface '" + assetPath
                                + "' because " + e.getMessage());
                        return null;
                    }
                }
                return cache.get(assetPath);
            }
        }
    }

}