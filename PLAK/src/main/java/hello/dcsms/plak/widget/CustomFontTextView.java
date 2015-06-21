package hello.dcsms.plak.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import hello.dcsms.plak.R;

public class CustomFontTextView extends TextView {
	private int defaultDimension = 0;
	private int TYPE_BOLD = 1;
	private int TYPE_ITALIC = 2;
	private int FONT_DCSMS = 1;
	private int fontType;
	private int fontName;

	public CustomFontTextView(Context context) {
		super(context);
		init(null, 0);
	}

	public CustomFontTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {
		// Load attributes
		final TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.font, defStyle, 0);
		fontName = a.getInt(R.styleable.font_name, defaultDimension);
		fontType = a.getInt(R.styleable.font_type, defaultDimension);
		a.recycle();

		setFontType(null);

	}

	

	private void setFontType(Typeface font) {
		if (fontType == TYPE_BOLD) {
			setTypeface(font, Typeface.BOLD);
		} else if (fontType == TYPE_ITALIC) {
			setTypeface(font, Typeface.ITALIC);
		} else {
			setTypeface(font);
		}
	}
}