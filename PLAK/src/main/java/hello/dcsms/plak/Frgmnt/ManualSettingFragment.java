package hello.dcsms.plak.Frgmnt;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import hello.dcsms.plak.R;
import hello.dcsms.plak.adapter.ManualPagerAdapter;
import hello.dcsms.plak.task.iface.FragmentUpdateInfoListener;
import hello.dcsms.plak.widget.HoriListMenu;

public class ManualSettingFragment extends Activity implements FragmentUpdateInfoListener {


    ViewPager mvpager;
    ManualPagerAdapter adapter;
    List<MSChildFragment> mfragment;
    ActionBar.Tab tab;
    ActionBar actionBar;
    HoriListMenu mlistmenu;

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    void initActionBar() {
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        getActionBar().setLogo(R.drawable.logo);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(true);
        tab = actionBar.newTab().setText("Content").setTabListener(tabListener);
        actionBar.addTab(tab);
        tab = actionBar.newTab().setText("Current Applied").setTabListener(tabListener);
        actionBar.addTab(tab);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_setting_layout_main);
        initActionBar();
        mvpager = (ViewPager) findViewById(R.id.manual_setting_viewpager);


        mfragment = new ArrayList<>();
        mfragment.add(new MSChildFragmentList().setListener(this));
        mfragment.add(new MSChildFragmentInfo());

        adapter = new ManualPagerAdapter(getFragmentManager(), mfragment);
        mvpager.setAdapter(adapter);


        mvpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                actionBar.setSelectedNavigationItem(i);
                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            if (mvpager != null)
                mvpager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }


    };

    @Override
    public void onUpdate() {
        MSChildFragment ms = (MSChildFragment) adapter.getItem(1);
        if (ms instanceof MSChildFragmentInfo) {
            ((MSChildFragmentInfo) ms).updateInfo();
        }
    }
}
