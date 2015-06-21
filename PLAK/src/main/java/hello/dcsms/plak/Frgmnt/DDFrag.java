package hello.dcsms.plak.Frgmnt;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.RLParam;
import hello.dcsms.plak.widget.PlakView;

public class DDFrag extends DragnDropAbstract implements PlakView.onReLayout {
    PlakView pv_kanan, pv_tengah, pv_kiri;
    PlakView.onReLayout plakviewListener;

    private void setLayoutParamsaddToRoot(PlakView q, PlakView w, PlakView e) {
        q.setLayoutParams(RLParam.KANAN());
        e.setLayoutParams(RLParam.TENGAH());
        w.setLayoutParams(RLParam.KIRI());

        q.setGravity(Gravity.CENTER | Gravity.RIGHT);
        e.setGravity(Gravity.CENTER);
        w.setGravity(Gravity.CENTER | Gravity.LEFT);

        root.addView(q);
        root.addView(w);
        root.addView(e);


    }

    private void setIds(PlakView q, PlakView w, PlakView e) {
        q.setId(R.id.plakview_kanan);
        w.setId(R.id.plakview_kiri);
        e.setId(R.id.plakview_tengah);
    }

    @Override
    public void preparePreviewRoot() {
/*        pv_kanan = new PlakView(getActivity());
        pv_kiri = new PlakView(getActivity());
        pv_tengah = new PlakView(getActivity());
        setLayoutParamsaddToRoot(pv_kanan, pv_kiri, pv_tengah);
        setIds(pv_kanan, pv_kiri, pv_tengah);
        pv_kanan.setRelayoutLisener(this);
        pv_kiri.setRelayoutLisener(this);
        pv_tengah.setRelayoutLisener(this);*/
    }


    @Override
    public void addToview(String d, float xx) {

        LayoutData ld = new LayoutData();
        int x = (int) xx;
        Typeface font = Typeface.createFromAsset(getAssets(), "files/hellodcsms.otf");
        TextView t = (TextView) getViewByTag(d);
        if (t == null)
            return;
        t.setTypeface(font);
        ViewGroup parent = (ViewGroup) t.getParent();
        parent.removeView(t);
        t.setOnLongClickListener(null);
        t.setText(d);
        t.setTextSize(dp2px(8));
        t.setPadding(4, 0, 4, 0);
        t.setVisibility(View.VISIBLE);
        t.setGravity(Gravity.CENTER);
        t.setTextColor(Color.WHITE);
        t.setBackgroundColor(Color.TRANSPARENT);

/*
        t.setLayoutParams(LLParam.WRAP_CONTENT());
        if(x<w3){
            //kiri
            pv_kiri.addView(t);

        }else if (x > w3 && x < w3 * 2) {
            //tengah
            pv_tengah.addView(t);
        }else{
            //kanan
            pv_kanan.addView(t);
        }*/



/*        boolean handle = false;
        if(!handle)
            return;*/

        boolean isAll = cekAllItem();

        if (x > w3 && x < w3 * 2) {
            if (ldtengah.size() > 0) {
                View v = ldtengah.get(ldtengah.size() - 1).v;
                t.setLayoutParams(RLParam.LEFT_OF(v.getId()));
//                t.setGravity(Gravity.CENTER);
                ld.v = t;
            } else {
                t.setLayoutParams(RLParam.TENGAH());
//                t.setGravity(Gravity.CENTER);
                ld.v = t;
            }
            ldtengah.add(ld);
        } else if (x < w3) {
            if (ldkiri.size() > 0) {
                View v = ldkiri.get(ldkiri.size() - 1).v;
                /*if (isAll) {
                    if (cekTengah()) {
                        t.setLayoutParams(RLParam.RIGHT_OF_LEFT_OF(v.getId(), ldtengah.get(0).v.getId()));

                    } else {
                        if (cekKanan()) {
                            t.setLayoutParams(RLParam.RIGHT_OF_LEFT_OF(v.getId(), ldkanan.get(ldkanan.size() - 1).v.getId()));
                        } else {
                            t.setLayoutParams(RLParam.RIGHT_OF(v.getId()));
                        }
                    }
                } else*/
                    t.setLayoutParams(RLParam.RIGHT_OF(v.getId()));
//                t.setGravity(Gravity.LEFT);
                ld.v = t;


            } else {

                t.setLayoutParams(RLParam.KIRI());
                ld.v = t;
            }
            ldkiri.add(ld);

        } else {
            if (ldkanan.size() > 0) {
                View v = ldkanan.get(ldkanan.size() - 1).v;
/*                if (isAll) {
                    if (cekTengah()) {
                        t.setLayoutParams(RLParam.RIGHT_OF_LEFT_OF(ldtengah.get(0).v.getId(), v.getId()));
                    } else {
                        if (cekKiri()) {
                            t.setLayoutParams(RLParam.RIGHT_OF_LEFT_OF(ldkiri.get(ldkiri.size() - 1).v.getId(), v.getId()));
                        } else {
                            t.setLayoutParams(RLParam.LEFT_OF(v.getId()));
                        }
                    }
                } else*/
                    t.setLayoutParams(RLParam.LEFT_OF(v.getId()));
//                t.setGravity(Gravity.RIGHT);
                ld.v = t;
            } else {
//                t.setGravity(Gravity.RIGHT);
                t.setLayoutParams(RLParam.KANAN());
                ld.v = t;
            }
            ldkanan.add(ld);

        }
        root.addView(t);


        apply.setEnabled(isAll);
    }

    private int getLDId(LayoutData d) {
        return d.v.getId();
    }

    private boolean cekTengah() {
        return ldtengah.size() > 0;
    }

    private boolean cekKanan() {
        return ldkanan.size() > 0;
    }

    private boolean cekKiri() {
        return ldkiri.size() > 0;
    }

    @Override
    public void change(boolean changed, int l, int t, int r, int b) {

    }
}
