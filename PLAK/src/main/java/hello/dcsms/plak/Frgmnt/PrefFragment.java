package hello.dcsms.plak.Frgmnt;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.provider.MiuiSettings;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringChain;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hello.dcsms.plak.C;
import hello.dcsms.plak.Hello;
import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.PrefUtils;
import hello.dcsms.plak.task.ShellTask;
import hello.dcsms.plak.task.iface.OnShellTaskListener;
import hello.dcsms.plak.widget.CustomButton;
import hello.dcsms.plak.widget.LoadingDialog;

public class PrefFragment extends PreferenceFragment implements
        OnSharedPreferenceChangeListener, OnPreferenceClickListener, FragmentManager.OnBackStackChangedListener {
    int dpi;
    ListView lv;
    @InjectView(R.id.pref_mainroot)
    RelativeLayout main_root;
    private CustomButton apply_statusbar, apply_mihome;
    private LinearLayout ll_tombol;


    private final SpringChain mSpringChain = SpringChain.create();


    SharedPreferences.Editor editor;
    float[] goyang = new float[]{779.7F, 116.7F, 105.6F, 59.1F, 1.1F, -48.3F,
            -70.8F, -64.1F, -35.8F, -1.3F, 29.3F, 42.9F, 38.8F, 21.7F, 1.2F,
            -17.8F, -26.0F, -23.5F, -13.1F, -9.9F, 10.7F, 15.8F, 14.3F, 7.9F,
            7.5F, -6.5F, -9.5F, -8.6F, -4.8F, -5.4F, 3.9F, 5.8F, 5.2F, 2.9F,
            3.8F, -2.4F, -3.5F, -3.1F, -1.7F, -2.6F, 1.4F, 2.1F, 1.9F, 1.0F,
            1.8F, -0.8F, -1.2F, -1.1F, -0.6F, -1.2F, 0.5F, 0.7F, 0.7F, 0.3F,
            3.0F, -0.3F, -0.4F, -0.4F, -0.2F, -5.4F, 0.1F, 0.2F, 0.2F, 0.1F,
            -4.4F, -0.1F, -0.1F, -0.1F, -0.0F, -2.3F, 0.0F, 0.1F, 0.0F, 0.0F,
            4.4F, -0.0F, -0.0F, -0.0F, -0.0F, -9.8F, 0.0F, 0.0F, 0.0F, 0.0F,
            -4.4F, -0.0F, -0.0F, -0.0F, -0.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.0F,
            6.6F, -0.0F, -0.0F, -0.0F, -0.0F,};

    float[] goyang_rev = new float[]{-0.00f, -0.00f, -0.00f, -0.00f, 6.60f,
            0.00f, 0.01f, 0.01f, 0.00f, -4.08f, -0.01f, -0.02f, -0.02f, -0.01f,
            -4.48f, 0.01f, 0.03f, 0.03f, 0.02f, -9.85f, -0.03f, -0.05f, -0.06f,
            -0.04f, 4.47f, 0.05f, 0.09f, 0.10f, 0.07f, -2.34f, -0.08f, -0.15f,
            -0.17f, -0.11f, -4.42f, 0.14f, 0.26f, 0.28f, 0.19f, -5.46f, -0.24f,
            -0.43f, -0.47f, -0.32f, 3.00f, 0.39f, 0.71f, 0.78f, 0.53f, -1.23f,
            -0.65f, -1.17f, -1.29f, -0.88f, 1.83f, 1.08f, 1.93f, 2.13f, 1.46f,
            -2.69f, -1.78f, -3.19f, -3.52f, -2.40f, 3.88f, 2.94f, 5.26f, 5.81f,
            3.97f, -5.48f, -4.85f, -8.67f, -9.58f, -6.54f, 7.53f, 7.99f,
            14.30f, 15.80f, 10.79f, -9.94f, -13.18f, -23.58f, -26.06f, -17.80f,
            1.22f, 21.74f, 38.87f, 42.96f, 29.34f, -1.35f, -35.84f, -64.10f,
            -70.84f, -48.38f, 1.11f, 59.10f, 105.68f, 116.79f, 779.77f};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pref_fragment, null);
        ButterKnife.inject(this, v);


        lv = (ListView) v.findViewById(android.R.id.list);
        lv.setBackgroundColor(Color.WHITE);


        apply_statusbar = (CustomButton) v
                .findViewById(R.id.apply_statusbar_config);
        apply_mihome = (CustomButton) v.findViewById(R.id.apply_mihome_config);
        ll_tombol = (LinearLayout) v.findViewById(R.id.ll_tombol);
        apply_statusbar.setText("APPLY STATUSBAR CONFIG");
        apply_mihome.setText("APPLY MIHOME CONFIG");
        apply_statusbar.setOnClickListener(applyonclik);
        apply_mihome.setOnClickListener(applyonclik);
        lv.setDividerHeight(0);
        lv.setOnScrollListener(onscroll);


        for (int i = 0; i < main_root.getChildCount(); i++) {
            final View view = main_root.getChildAt(i);
            mSpringChain.addSpring(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    float value = (float) spring.getCurrentValue();
                    view.setTranslationY(value);
                }
            });
        }
        main_root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                main_root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                List<Spring> springs = mSpringChain.getAllSprings();
                for (int i = 0; i < springs.size(); i++) {

                    springs.get(i).setCurrentValue(main_root.getHeight());
                }
                main_root.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSpringChain
                                .setControlSpringIndex(0)
                                .getControlSpring()
                                .setEndValue(0);
                    }
                }, 200);
            }
        });


        return v;
    }

    private OnClickListener applyonclik = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.apply_statusbar_config:
                    animateThis(apply_statusbar);
                    PrefUtils.fix();
                    getActivity().sendBroadcast(
                            new Intent().setAction(C.MODSTATUSBARRUNTIME));
                    break;

                case R.id.apply_mihome_config:
                    animateThis(apply_mihome);
                    PrefUtils.fix();
                    Intent i = new Intent();
                    i.setAction(C.MODMIHOME);
                    getActivity().sendBroadcast(i);
                    getActivity().finish();
                    break;
            }
            MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.crot);

            mp.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer paramMediaPlayer) {
                    paramMediaPlayer.release();
                    paramMediaPlayer = null;

                }
            });
            mp.start();

        }
    };


    private void animateThis(TextView v) {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.play(ObjectAnimator.ofFloat(v, View.TRANSLATION_Y, goyang));
        set.start();
    }

    private OnScrollListener onscroll = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView arg0, int scrSTATE) {

        }

        int MEASURE = 2;

        Debugger d = new Debugger();

        @Override
        public void onScroll(AbsListView v, int topchild, int childVisible, int totalChild) {

            if (lv.getChildCount() > 0) {


                int count_vis = topchild + childVisible;
                int must = totalChild - MEASURE;
                if (must > count_vis)
                    showTombol(true);
                else if (must < count_vis)
                    showTombol(false);

            }
        }
    };




    boolean isShow = false;

    void showTombol(boolean show) {
        isShow = show;

        if (isanimRun)
            return;
        if (show) {
            if (ll_tombol.getVisibility() == View.VISIBLE)
                return;
        } else {
            if (ll_tombol.getVisibility() == View.GONE)
                return;
        }
        ViewGroup parent = (ViewGroup) ll_tombol.getParent();
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.play(ObjectAnimator.ofFloat(ll_tombol, View.TRANSLATION_Y,
                show ? new float[]{120f, 0f}
                        : new float[]{0f, 120f}));
        set.addListener(aniListener);
        set.start();

    }

    boolean isanimRun = false;
    private AnimatorListener aniListener = new AnimatorListener() {

        @Override
        public void onAnimationStart(Animator arg0) {
            isanimRun = true;
            ll_tombol.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animator arg0) {
        }

        @Override
        public void onAnimationEnd(Animator anim) {
            ll_tombol.setVisibility(isShow ? View.VISIBLE : View.GONE);
            isanimRun = false;
        }

        @Override
        public void onAnimationCancel(Animator arg0) {
        }
    };
    ColorDrawable col;
    CheckBoxPreference prefcarrier;
    ActionBar ab;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        col = new ColorDrawable(getResources().getColor(R.color.merah));
        ab = getActivity().getActionBar();
        ab.setBackgroundDrawable(col);
        addPreferencesFromResource(R.xml.pref);

        dpi = getActivity().getResources().getConfiguration().densityDpi;
        editor = getPreferenceScreen().getSharedPreferences().edit();
        editor.putInt(C.DENSITY, dpi);
        editor.commit();
        findPreference("pref_edit_jam").setOnPreferenceClickListener(this);
        findPreference("pref_edit_carrier").setOnPreferenceClickListener(this);
        findPreference("manual_conf_setting").setOnPreferenceClickListener(this);
        findPreference("screenshot_ov").setOnPreferenceClickListener(this);
         updateManualSettingDesc();
        prefcarrier = (CheckBoxPreference) findPreference("enable_custom_carrier");

    }


    private int carrierCek(){
        int result = -1;

        try {

            boolean  isCarrierEnable = MiuiSettings.System.getBoolean(getActivity().getContentResolver(),MiuiSettings.System.STATUS_BAR_SHOW_CUSTOM_CARRIER,false);

            result = isCarrierEnable?1:0;
        }catch (Exception e){
            e.printStackTrace();
            result = -99;
        }
        return result;
    }

    private void updateManualSettingDesc() {
        boolean manual = getPreferenceScreen().getSharedPreferences()
                .getBoolean("manual_conf", false);
        if (manual) {
            findPreference("manual_conf_setting").setSummary(
                    getSummaryforManualConfig());
        }

    }

    private String getSummaryforManualConfig() {
        return "Yeah!";
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences pref, String key) {


        PrefUtils.fix();

        if (key.equals(C.PREF_LAYOUT_SIM_ORDER) || key.equals(C.ENABLE_CUSTOM_CARRIER)) {
            Hello.updateSIM();
            ld = new LoadingDialog(getActivity());
            ld.show();
            new Handler().postDelayed(runBroadCast, 1000);
        }


    }
    LoadingDialog ld;

    ShellTask task = null;
    private Runnable runBroadCast = new Runnable() {

        @Override
        public void run() {
            if(ld!=null && ld.isShowing())
                ld.dismiss();
            getActivity().sendBroadcast(new Intent().setAction(C.ORDERSIM));
        }
    };

    private void doGrepSystemUI() {
        if (task != null)
            return;
        task = new ShellTask("top -n 1 | grep systemui");
        task.setListener(new OnShellTaskListener() {
            @Override
            public void onUpdate(String resutl) {
                Toast.makeText(getActivity(), resutl, Toast.LENGTH_LONG).show();
                task = null;
                if (resutl.length() > 0)
                    new ShellTask("kill " + resutl.trim()).execute();
            }
        });
        task.execute();
    }

    @Override
    public boolean onPreferenceClick(Preference pref) {
        if (pref.getKey().equals("manual_conf_setting") && pref.isEnabled()) {
            getActivity().startActivity(new Intent(getActivity(), ManualSettingFragment.class));

        } else if (pref.getKey().equals("pref_edit_jam") && pref.isEnabled()) {

            getActivity().getFragmentManager().beginTransaction().addToBackStack("pref_edit_jam")

                    .replace(R.id.container, new EditJamFragment()).commit();

        } else if (pref.getKey().equals("screenshot_ov")) {
            getActivity().startActivity(new Intent(getActivity(), SSFragment.class));
        }else if (pref.getKey().equals("pref_edit_carrier")) {

            getActivity().getFragmentManager().beginTransaction().addToBackStack("pref_edit_carrier")
                    .replace(R.id.container, new CarrierFragment(),"CarrierTag").commit();

        }
        return false;
    }

    @Override
    public void onPause() {

        System.gc();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        getActivity().getFragmentManager().removeOnBackStackChangedListener(this);
        super.onPause();

    }


    @Override
    public void onResume() {
        getActivity().getFragmentManager().addOnBackStackChangedListener(this);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        super.onResume();
        if(prefcarrier==null)
            return;
        int carrierceck = carrierCek();
        switch (carrierCek()){
            case 0:
                prefcarrier.setEnabled(false);
                prefcarrier.setSummary("Please enable show carrier in MIUI Settings - Notifications section");
                break;
            case 1:
                prefcarrier.setEnabled(true);
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackStackChanged() {
        int count = getActivity().getFragmentManager().getBackStackEntryCount();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}