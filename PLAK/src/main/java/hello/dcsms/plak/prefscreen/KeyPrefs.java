package hello.dcsms.plak.prefscreen;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.DialogPreference;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import hello.dcsms.plak.C;
import hello.dcsms.plak.Frgmnt.PrefFragment;
import hello.dcsms.plak.R;
import hello.dcsms.plak.Tes;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.ParcleData;

import static butterknife.OnTextChanged.Callback.TEXT_CHANGED;

public class KeyPrefs extends DialogPreference {
    LayoutInflater li;
    @InjectView(R.id.device_id)
    EditText deviceId;
    @InjectView(R.id.key)
    EditText key;
    Debugger debug;


    public KeyPrefs(Context context, AttributeSet attrs) {
        super(context, attrs);
        li = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    String id;
    //32120112;
    @Override
    protected View onCreateDialogView() {
        View view = li.inflate(R.layout.dialog_key, null);
        ButterKnife.inject(this, view);
        debug = new Debugger();
        id = android.provider.Settings.Secure.getString(getContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        deviceId.setText(ParcleData.en(id).trim());
        key.setText(getSharedPreferences().getString(C.DISABLENAG,"NaN"));

        return view;
    }

    @OnTextChanged(value = R.id.key, callback = TEXT_CHANGED)
    void onTextChanged(CharSequence text) {
        String k = text.toString();
        String ke = Tes.getkey();
        String mi = ParcleData.mixmax(ke,deviceId.getText().toString());


        if(k.equalsIgnoreCase(ParcleData.fx(mi))){
            key.setTextColor(Color.BLACK);
        }else
            key.setTextColor(Color.RED);

    }

    @Override
    protected void onPrepareDialogBuilder(Builder builder) {
        builder.setTitle(getTitle());
        super.onPrepareDialogBuilder(builder);
        setNegativeButtonText(null);


    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
            if(positiveResult){
               SharedPreferences.Editor editor = getEditor();
                editor.putString(getKey(), key.getText().toString().trim());
                editor.commit();
            }
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);


    }
}