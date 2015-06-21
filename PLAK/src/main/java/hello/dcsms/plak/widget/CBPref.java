package hello.dcsms.plak.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import hello.dcsms.plak.R;

public class CBPref extends CheckBoxPreference {

	public CBPref(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		if (attrs != null) {
			TypedArray ta = context.obtainStyledAttributes(attrs,
					R.styleable.CBPref);
			if(ta!=null){
			}
			ta.recycle();

		}
	}

	public CBPref(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CBPref(Context context) {
		this(context, null);
	}
	
	@Override
	protected void onBindView(View view) {
	
	
		LayoutInflater infl = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout cb = (LinearLayout) infl.inflate(R.layout.cbpref, null);
		view = cb;
	}
	

}
