package hello.dcsms.plak.Test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import hello.dcsms.plak.R;

/**
 * Created by jmkl on 5/3/2015.
 */
public class VectorDrawableTest extends View {
    Drawable mDrawable;

    public VectorDrawableTest(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public VectorDrawableTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
     }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        mDrawable.draw(canvas);
    }

}
