package hello.dcsms.plak.widget;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;

/**
 * Created by jmkl on 5/13/2015.
 */
public class HoriListMenu extends HorizontalScrollView {

    public interface onHoriListItemClickListener {
        void onClick(View view, int position);
    }



    onHoriListItemClickListener listener;
    LinearLayout linearLayout;
    Point point;
    int showCount =4;


    private static final int SWIPE_MIN_DISTANCE = 5;
    private static final int SWIPE_THRESHOLD_VELOCITY = 300;
    private GestureDetector mGestureDetector;
    private int mActiveFeature = 0;


    public HoriListMenu(Context context) {
        super(context);
        init(context);
    }

    public HoriListMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }
    public HoriListMenu setShowCount(int count){
        showCount = count;
        return this;
    }

    /*"Name", "PackageName", "Checked", "Type"*/
    public void setUpMenu(Context context, String[] menu){

        for (int i = 0; i < menu.length; i++) {
            Button mButton = new Button(context);
            mButton.setBackground(getResources().getDrawable(R.drawable.nav_bg_state));
            mButton.setLayoutParams(new LayoutParams(point.x/showCount, LayoutParams.WRAP_CONTENT));
            mButton.setText(menu[i]);
            mButton.setOnClickListener(onmenuClick);
            mButton.setId(i);
            mButton.setTextAppearance(context, R.style.txtKecil_Putiah);
            mButton.setGravity(Gravity.CENTER);
            linearLayout.addView(mButton);
        }
    }

    public void setOnSortirMenuListener(onHoriListItemClickListener listener){
        this.listener = listener;
    }

    private OnClickListener onmenuClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if(listener!=null)
                listener.onClick(view,view.getId());
        }
    };

    private void init(Context context) {
        mGestureDetector = new GestureDetector(new MyGestureDetector());
        Display d = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        point = new Point();
        d.getSize(point);
        setHorizontalScrollBarEnabled(false);
        setFillViewport(true);
        linearLayout = new LinearLayout(context);
        linearLayout.setBackgroundColor(context.getResources().getColor(R.color.merah));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        addView(linearLayout);
        setOnTouchListener(TouchMeh);

    }


    Debugger debug = new Debugger();

    View.OnTouchListener TouchMeh = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //If the user swipes
            if (mGestureDetector.onTouchEvent(event)) {
                return true;
            }
            else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL ){
                int scrollX = getScrollX();
                int featureWidth = v.getMeasuredWidth()/showCount;
                mActiveFeature = ((scrollX + (featureWidth/2))/featureWidth);
                int scrollTo = mActiveFeature*featureWidth;
                smoothScrollTo(scrollTo, 0);
                return true;
            }
            else{
                return false;
            }
        }
    };

    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                //right to left
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    int featureWidth = getMeasuredWidth()/showCount;
                    mActiveFeature = (mActiveFeature < (linearLayout.getChildCount() - 1))? mActiveFeature + 1:linearLayout.getChildCount()-1;
                    smoothScrollTo(mActiveFeature*featureWidth, 0);
                    return true;
                }
                //left to right
                else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    int featureWidth = getMeasuredWidth()/showCount;
                    mActiveFeature = (mActiveFeature > 0)? mActiveFeature - 1:0;
                    smoothScrollTo(mActiveFeature*featureWidth, 0);
                    return true;
                }
            } catch (Exception e) {
                Log.e("PLAK", "There was an error processing the Fling event:" + e.getMessage());
            }
            return false;
        }
    }


}
