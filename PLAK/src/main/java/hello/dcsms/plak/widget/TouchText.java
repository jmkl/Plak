package hello.dcsms.plak.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import hello.dcsms.plak.R;

public class TouchText extends EditText {
	int minvalue = 0;
	int maxvalue = 500;

	public TouchText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
		isInEditMode();
	}

	@SuppressLint("ClickableViewAccessibility")
	private void init(Context context, AttributeSet attrs) {
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.TouchText, 0, 0);

		try {
			minvalue = a.getInteger(R.styleable.TouchText_NilaiMin, 0);
			maxvalue = a.getInteger(R.styleable.TouchText_NilaiMax, 500);

		} finally {
			a.recycle();
		}
		setText(0);
		setOnTouchListener(ontouch);

	}

	void addValue(int value) {
		if (Integer.parseInt(getText().toString()) == minvalue) {
			setText(minvalue);
			return;
		} else if (Integer.parseInt(getText().toString()) == maxvalue) {
			setText(maxvalue);
			return;
		}
		setText(Integer.parseInt(getText().toString())+value);
	}

	int mDownX = 0;
	private OnTouchListener ontouch = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent ev) {
			v.performClick();
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mDownX = (int) ev.getX();
				break;

			case MotionEvent.ACTION_UP:
				break;
			case MotionEvent.ACTION_MOVE:
				if(mDownX>(ev.getX()+30))
					addValue(-1);
				else if(mDownX<(ev.getX()+30))
					addValue(1);
				break;
			}
			return true;
		}
	};

}
