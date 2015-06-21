package hello.dcsms.plak.Frgmnt;

import android.app.Fragment;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.widget.CustomButton;

/**
 * Created by jmkl on 4/28/2015.
 */
public class NestedPrefFragment extends Fragment {
    private EditText editText;
    private CustomButton view2;
    private String[] tes = new String[]{"a",
            "d",
            "e",
            "f",
            "i",
            "z",
            "s",
            "6",
            "7",
            "8",
            "c",
            "j",
            "m",
            "n",
            "4",
            "5",
            "0",
            "p",
            "g",
            "3",
            "9",
            "h",
            "y",
            "0",
            "r",
            "t",
            "k",
            "l",
            "u",
            "q",
            "x",
            "v",
            "w",
            "1",
            "2",
            "b"};

    private void assignViews(View v) {
        editText = (EditText) v.findViewById(R.id.editText);
        view2 = (CustomButton) v.findViewById(R.id.view2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.hello_nested, null);
        assignViews(v);

        String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        byte[] enc = Base64.encode(deviceId.getBytes(), Base64.DEFAULT);
        String code = new String(enc).toLowerCase();
        int x =find(code.substring(0,1));
        int xx=find(code.substring(1,2));
        int xxx=find(code.substring(2,3));
        int xxxx=find(code.substring(3,4));


        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return v;
    }
    String i2s(int x){
        return Integer.toString(x);
    }

    int find(String s){
        int pos=0;
        for(String x :tes){
            if(x.equals(s)){
                return pos;
            }
            pos++;
        }
       return pos;
    }
}
