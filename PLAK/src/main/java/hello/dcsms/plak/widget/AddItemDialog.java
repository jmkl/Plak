package hello.dcsms.plak.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import hello.dcsms.plak.R;
import hello.dcsms.plak.adapter.ACAdapter;
import hello.dcsms.plak.manual.AutoCompleteData;

public class AddItemDialog extends Dialog {

    public class ShakeAnimation {
        View view;
        private long duration;

        public ShakeAnimation(View view) {
            this.view = view;
        }

        public ShakeAnimation setDuration(long dur) {
            this.duration = dur;
            return this;
        }

        public void animate() {
            int w = view.getWidth();
            AnimatorSet set = new AnimatorSet();
            set.setDuration(duration);
            set.play(ObjectAnimator.ofFloat(view, View.TRANSLATION_X,
                    179.7F, 116.7F, 105.6F, 59.1F, 1.1F, -48.3F,
                    -70.8F, -64.1F, -35.8F, -1.3F, 29.3F, 42.9F, 38.8F,
                    21.7F, 1.2F, -17.8F, -26.0F, -23.5F, -13.1F, -9.9F,
                    10.7F, 15.8F, 14.3F, 7.9F, 7.5F, -6.5F, -9.5F,
                    -8.6F, -4.8F, -5.4F, 3.9F, 5.8F, 5.2F, 2.9F, 3.8F,
                    -2.4F, -3.5F, -3.1F, -1.7F, -2.6F, 1.4F, 2.1F,
                    1.9F, 1.0F, 1.8F, -0.8F, -1.2F, -1.1F, -0.6F,
                    -1.2F, 0.5F, 0.7F, 0.7F, 0.3F, 3.0F, -0.3F, -0.4F,
                    -0.4F, -0.2F, -5.4F, 0.1F, 0.2F, 0.2F, 0.1F, -4.4F,
                    -0.1F, -0.1F, -0.1F, -0.0F, -2.3F, 0.0F, 0.1F,
                    0.0F, 0.0F, 4.4F, -0.0F, -0.0F, -0.0F, -0.0F,
                    -9.8F, 0.0F, 0.0F, 0.0F, 0.0F, -4.4F, -0.0F, -0.0F,
                    -0.0F, -0.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 6.6F,
                    -0.0F, -0.0F, -0.0F, -0.0F));
            set.start();

        }
    }

    public interface OnItemAddListener {
        void getValue(ITEMTYPE type, String item, String itemvalue,
                      String item_desc, String packname);
    }

    public enum ITEMTYPE {
        STRING, DIMEN, INTEGER, BOOLEAN, COLOR
    }

    LinearLayout view;
    EditText et_val, et_desc;
    View colorpicker;
    AutoCompleteTextView at_packname, et_item;
    TextView tv_title, tv_item, tv_val, TV_APPLY;
    private ITEMTYPE type;
    private String title, item, value, description, packagename;
    private OnItemAddListener listener;
    private Context mContext;
    private List<AutoCompleteData> acData = null;
    ACAdapter field_adapter = null;

    public AddItemDialog(Context context) {
        super(context, android.R.style.Theme_Translucent);
        mContext = context;
    }

    public AddItemDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public AddItemDialog setType(ITEMTYPE type) {
        this.type = type;
        return this;
    }

    public AddItemDialog setItem(String item) {
        this.item = item;
        return this;
    }

    public AddItemDialog setDesc(String desc) {
        this.description = desc;
        return this;
    }

    public AddItemDialog setPackage(String pkg) {
        this.packagename = pkg;
        return this;
    }

    public AddItemDialog setValue(String value) {
        this.value = value;
        return this;
    }

    String hint;

    public AddItemDialog setValueHint(String val) {
        this.hint = val;
        return this;
    }

    public void setOnItemAddListener(OnItemAddListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_add_dialog);
        view = (LinearLayout) findViewById(R.id.contentDialog);
        AnimateIn();
        LinearLayout backView = (LinearLayout) findViewById(R.id.dialog_rootView);
        backView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getX() < view.getLeft()
                        || event.getX() > view.getRight()
                        || event.getY() > view.getBottom()
                        || event.getY() < view.getTop()) {
                    dismiss();
                }
                return false;
            }
        });
        findView();
        setOnListener();
        setAdapter();
    }

    private void setOnListener() {
        colorpicker.setVisibility(type == ITEMTYPE.COLOR ? View.VISIBLE : View.GONE);
        tv_title.setText(title);
        if (value != null)
            et_val.setText(value);
        if (item != null)
            et_item.setText(item);
        if (description != null)
            et_desc.setText(description);
        if (packagename != null)
            at_packname.setText(packagename);
        if (hint != null)
            et_val.setHint(hint);
        switch (type) {
            case INTEGER:
                et_val.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case DIMEN:
                et_val.setInputType(InputType.TYPE_CLASS_NUMBER);

                break;
            case STRING:
                et_val.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case BOOLEAN:
                et_val.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case COLOR:
                et_val.setInputType(InputType.TYPE_CLASS_TEXT);
                try {
                    colorpicker.setBackgroundColor(Color.parseColor(value));
                }catch (RuntimeException ex){}

                break;
        }

        TV_APPLY.setOnClickListener(onApply);
        colorpicker.setOnClickListener(oncolorPickerClick);

    }
    private String colorToHexString(int color) {
        return String.format("#%06X", 0xFFFFFFFF & color);
    }
    private View.OnClickListener oncolorPickerClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            final ColorPickerDialog dialog = new ColorPickerDialog(mContext, Color.WHITE);
            dialog.setAlphaSliderVisible(true);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    et_val.setText(colorToHexString(dialog.getColor()));
                    colorpicker.setBackgroundColor(dialog.getColor());
                }
            });
            dialog.show();
        }
    };

    private View.OnClickListener onApply = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (et_val.getText().toString().isEmpty()
                    || et_item.getText().toString().isEmpty()
                    || et_desc.getText().toString().isEmpty()
                    || at_packname.getText().toString().isEmpty()) {
                tv_title.setText("dont left the field empty :(");
                new ShakeAnimation(view).setDuration(100).animate();

                return;
            } else {
                listener.getValue(type, et_item.getText().toString(), et_val
                                .getText().toString(), et_desc.getText().toString(),
                        at_packname.getText().toString());
                dismiss();
            }
        }
    };

    private void findView() {
        colorpicker = findViewById(R.id.man_dial_col_picker);
        et_val = (EditText) findViewById(R.id.et_man_dial_add_val);
        et_item = (AutoCompleteTextView) findViewById(R.id.et_man_dial_add_item);
        tv_title = (TextView) findViewById(R.id.tv_man_dial_title);
        tv_item = (TextView) findViewById(R.id.tv_man_dial_add_item);
        tv_val = (TextView) findViewById(R.id.tv_man_dial_add_val);
        et_desc = (EditText) findViewById(R.id.et_man_dial_add_desc);
        at_packname = (AutoCompleteTextView) findViewById(R.id.man_pack_name);
        TV_APPLY = (TextView) findViewById(R.id.tv_man_add_apply);

    }

    private void setAdapter() {
        ArrayAdapter<String> package_adapter = new ArrayAdapter<String>(
                mContext, R.layout.autocomplete_item,android.R.id.text1, new String[]{
                "com.android.systemui", "com.miui.home"});

        at_packname.setAdapter(package_adapter);
//		ArrayAdapter<String> adapterac = new ArrayAdapter<String>(mContext,
//				R.layout.autocomplete_item,
//				acData.toArray(new String[acData.size()]));
        ACAdapter adapterac = new ACAdapter(mContext, R.layout.autocomplete_item, android.R.id.text1, acData, type);
        et_item.setAdapter(adapterac);

    }

    @Override
    public void show() {
        super.show();

    }

    private void AnimateIn() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(view, View.ALPHA, new float[]{
                0f, 1f}));
        set.setDuration(500);
        set.start();

    }

    public void setAutoCompleteData(List<AutoCompleteData> autocompletedata) {
        this.acData = autocompletedata;

    }

}
