package hello.dcsms.plak.prefscreen;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.adapters.NumericWheelAdapter;
import hello.dcsms.plak.R;

public class WheelPrefs extends DialogPreference implements OnWheelChangedListener {
    LayoutInflater li;
    View myView;
    private String dialogResultString;
    AbstractWheel wheel1, wheel2;
    private int x1, x2, y1, y2;
    String key1 = null;
    String key2 = null;
    String title1 = null, title2 = null;
    TextView tv_title1, tv_title2;
    private Context mContext;
    NumericWheelAdapter adapter1, adapter2;
    String value1, value2;
    String defaultvalue1, defaultvalue2;
    boolean isSingle = false;

    public WheelPrefs(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    public WheelPrefs(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    public void setSummary(CharSequence summary) {
        super.setSummary(summary);
    }

    private void init(Context context, AttributeSet attrs) {
        li = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WPrefs);
            x1 = a.getInt(R.styleable.WPrefs_nilaiMin1, 0);
            x2 = a.getInt(R.styleable.WPrefs_nilaiMax1, 0);
            y1 = a.getInt(R.styleable.WPrefs_nilaiMin2, 0);
            y2 = a.getInt(R.styleable.WPrefs_nilaiMax2, 0);
            key1 = a.getString(R.styleable.WPrefs_key_1);
            key2 = a.getString(R.styleable.WPrefs_key_2);
            title1 = a.getString(R.styleable.WPrefs_title_1);
            title2 = a.getString(R.styleable.WPrefs_title_2);
            defaultvalue1 = a.getString(R.styleable.WPrefs_default_value_1);
            defaultvalue2 = a.getString(R.styleable.WPrefs_default_value_2);
            isSingle = a.getBoolean(R.styleable.WPrefs_single, false);

            setSummary("Hellooooooooooooooooooooooo");
            a.recycle();
        }


    }


    @Override
    protected View onCreateDialogView() {
        SharedPreferences pref = getSharedPreferences();
        View view = li.inflate(R.layout.wheel_dialog, null);
        wheel1 = (AbstractWheel) view.findViewById(R.id.whell1);
        tv_title1 = (TextView) view.findViewById(R.id.wheel1_title);
        tv_title1.setText(title1);
        adapter1 = new NumericWheelAdapter(li.getContext(), x1, x2);
        adapter1.setItemResource(R.layout.wheel_items);
        adapter1.setItemTextResource(R.id.whell_item_txt);
        wheel1.setViewAdapter(adapter1);
        value1 = pref.getString(key1, defaultvalue1);
        int pos1 = intToStr(value1) - x1;
        wheel1.addChangingListener(this);
        wheel1.setCurrentItem(pos1);


        tv_title2 = (TextView) view.findViewById(R.id.wheel2_title);
        wheel2 = (AbstractWheel) view.findViewById(R.id.whell2);
        if (!isSingle) {
            tv_title2.setText(title2);
            adapter2 = new NumericWheelAdapter(li.getContext(), y1, y2);
            adapter2.setItemResource(R.layout.wheel_items);
            adapter2.setItemTextResource(R.id.whell_item_txt);
            wheel2.setViewAdapter(adapter2);
            value2 = pref.getString(key2, defaultvalue2);
            int pos2 = intToStr(value2) - y1;
            wheel2.addChangingListener(this);
            wheel2.setCurrentItem(pos2);
        }

        if (isSingle) {
            wheel2.setVisibility(View.GONE);
            tv_title2.setVisibility(View.GONE);
        }

        return view;
    }

    private String str2int(int val) {
        return Integer.toString(val);
    }

    private int intToStr(String val) {
        int value = Integer.parseInt(val);
        return value;
    }

    @Override
    protected void onPrepareDialogBuilder(Builder builder) {
        builder.setTitle(getTitle());

        super.onPrepareDialogBuilder(builder);
        setNegativeButtonText(null);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {

        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            Editor editor = getEditor();
            editor.putString(key1, value1);
            if (!isSingle)
                editor.putString(key2, value2);
            editor.commit();
            setPreviewValue();
        }
    }



    @Override
    protected void onAttachedToActivity() {
        super.onAttachedToActivity();
        setPreviewValue();
    }

    private void setPreviewValue(){
        SharedPreferences pref = getSharedPreferences();
        try {
            String result;
            if (isSingle) {
                result = String.format("%s\t: %s",
                        title1, pref.getString(key1, defaultvalue1));
            } else {
                result = String.format("%s\t: %s\n%s\t: %s",
                        title1, pref.getString(key1, defaultvalue1),
                        title2, pref.getString(key2, defaultvalue2));
            }
            setSummary(result);
        }catch (NullPointerException ex){}


    }

    @Override
    public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
        switch (wheel.getId()) {
            case R.id.whell1:
                value1 = (String) adapter1.getItemText(newValue);
                break;
            case R.id.whell2:
                if (!isSingle)
                    value2 = (String) adapter2.getItemText(newValue);
                break;
        }
    }
}