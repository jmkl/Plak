package hello.dcsms.plak.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import hello.dcsms.plak.R;

public class TestView extends TextView {


    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
           setBackgroundResource(R.drawable.sb_ic_batt);
    }
}
