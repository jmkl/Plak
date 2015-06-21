package hello.dcsms.plak.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import hello.dcsms.plak.Utils.Debugger;

/**
 * Created by jmkl on 4/28/2015.
 */
public class PlakView extends LinearLayout {
    public interface onReLayout{
        void change(boolean changed, int l, int t, int r, int b);
    }

    private onReLayout listener;
    public PlakView(Context context) {
        super(context);
        init(null, 0);
    }
    public void setRelayoutLisener(onReLayout listener){
        this.listener = listener;
    }

    public PlakView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PlakView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(listener!=null)
            listener.change(changed,l,t,r,b);
        Debugger.me("ID :", getId());
        Debugger.me("change", changed);
        int w = r-l;
        Debugger.me("WIDTH %",w);
    }


}
