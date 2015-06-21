package hello.dcsms.plak.Frgmnt;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hello.dcsms.plak.C;
import hello.dcsms.plak.Hello;
import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.PrefUtils;
import hello.dcsms.plak.data.PlakZip;
import hello.dcsms.plak.ss.LoadSSTemplateTask;
import hello.dcsms.plak.ss.SSFrameData;
import hello.dcsms.plak.ss.WallpeperCuser;
import hello.dcsms.plak.task.iface.OnTaskCaput;
import hello.dcsms.plak.widget.AwesomeFrameLayout;
import hello.dcsms.plak.widget.CustomButton;
import hello.dcsms.plak.widget.MToast;
import hello.dcsms.plak.widget.PlakConf;


public class SSFragment extends Activity implements LoadSSTemplateTask.loadSSTemplateTaskLisetener {


    File template;
    FrameLayout flroot;
    List<HashMap<String, Object>> data;
    Debugger debug;
    ViewPager mpager;
    MVPagerAdapter adapter;
    Point point = new Point();
    PrefUtils pref;
    @InjectView(R.id.templ_viewpager)
    ViewPager templViewpager;
    @InjectView(R.id.preview_menu)
    ImageView previewMenu;
    @InjectView(R.id.templ_title)
    TextView templTitle;
    @InjectView(R.id.templ_info)
    TextView templInfo;
    @InjectView(R.id.templ_apply)
    CustomButton templApply;
    @InjectView(R.id.templ_wallp)
    CustomButton templWallp;
    @InjectView(R.id.templ_layout)
    RelativeLayout templLayout;
    @InjectView(R.id.templ_isapply)
    ImageView templIsapply;
    @InjectView(R.id.templ_root)
    RelativeLayout templRoot;
    @InjectView(R.id.frame_root_tmplt)
    AwesomeFrameLayout frameRootTmplt;


    void initActionBar() {
        getActionBar().setLogo(R.drawable.logo);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(true);
    }

    int REQ_CODE_DETAIL = 2013;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_CODE) {
                String file = data.getStringExtra(PlakConf.RESULT);
                PlakZip plak = new PlakZip();
                plak.CekZipFile(this, file, new OnTaskCaput() {
                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void onFinish(Object result) {

                    }
                });

            } else if (requestCode == CODE_WALLPAPER) {

                ((SSFragmentItems) adapter.getItem(POSITION)).updateWallpaper();

            }
        }
    }

    int REQ_CODE = 1003;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_import:
                Intent ichoose = new Intent(this, PlakConf.class);
                ichoose.putExtra(PlakConf.FILE_TYPE, PlakConf.TYPE_TEMPLATE);
                startActivityForResult(ichoose, REQ_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.templatemenu, menu);
        return super.onCreateOptionsMenu(menu);
    }




    class MVPagerAdapter extends FragmentPagerAdapter {

        List<SSFragmentItems> mfragment;

        public MVPagerAdapter(FragmentManager fm, List<SSFragmentItems> mfragment) {
            super(fm);
            this.mfragment = mfragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public Fragment getItem(int i) {
            return mfragment.get(i);
        }

        @Override
        public int getCount() {
            return mfragment.size();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ss_template_layout);
        ButterKnife.inject(this);
        debug = new Debugger();
        Display d = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        d.getSize(point);
        pref = new PrefUtils(this);
        initActionBar();
        flroot = (FrameLayout) findViewById(R.id.frame_root_tmplt);
        mpager = (ViewPager) findViewById(R.id.templ_viewpager);
        template = new File(C.PLAK_SS_MOCKUP_ROOT);
        data = new ArrayList<>();
        new LoadSSTemplateTask(SSFragment.this, SSFragment.this).execute();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        System.gc();
        Hello.updateSIM();
        super.onDestroy();
    }


    @Override
    public void Update(HashMap<String, Object> data) {
        this.data.add(data);

    }

    public boolean deleteRec(String path) {
        File f = new File(path);
        try {
            if (f.exists() && f.isDirectory()) {
                File[] file = f.listFiles();
                for (File x : file) {
                    if (x.isDirectory())
                        deleteRec(x.getAbsolutePath());
                    else if (x.isFile())
                        x.delete();
                    else
                        return false;
                }
                f.delete();
            }
        } catch (Exception ex) {
            return false;
        }


        return true;
    }

    public PopupMenu.OnMenuItemClickListener onMenuClick = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.item_template_delete:
                    String folder = (String) data.get(POSITION).get("path");
                    if(!folder.equals(C.PLAK_SS_MOCKUP_ROOT+"Redmi 1S")) {
                        deleteRec((String) data.get(POSITION).get("path"));
                        if(data!=null){
                            data.clear();
                            data = null;
                        }
                        data = new ArrayList<>();
                        new LoadSSTemplateTask(SSFragment.this, SSFragment.this).execute();
                    }

                    break;
                case R.id.item_template_apply:
                    onClick1();
                    break;
                case R.id.item_template_wallp:
                    onClick2();
                    break;
                default:
                    return false;
            }

            return true;
        }
    };

    @OnClick(R.id.preview_menu)
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.item_template_menu);
        popup.setOnMenuItemClickListener(onMenuClick);
        popup.show();
    }

    public void onClick1() {
        String current  = (String) data.get(POSITION).get("path");
        pref.edit(C.MOCKUP_PATH, current);
        MToast.show(this, "Success");
        updateInfo();


    }

    public static int CODE_WALLPAPER = 123;


    public void onClick2() {
        SSFrameData ssframe = (SSFrameData) data.get(POSITION).get("data");
        Intent it = new Intent(this, WallpeperCuser.class);
        Bundle b = new Bundle();
        b.putInt(WallpeperCuser.KEY_H, ssframe.getSsframedata().getBg_height());
        b.putInt(WallpeperCuser.KEY_W, ssframe.getSsframedata().getBg_width());
        b.putString(WallpeperCuser.KEY_WALLPIMAGE, data.get(POSITION).get("path") + "/" + ssframe.getSsframedata().getBackground());
        it.putExtra(WallpeperCuser.BUNDLE_KEY, b);
        startActivityForResult(it, CODE_WALLPAPER);

    }


    List<SSFragmentItems> listtemplate;
    int POSITION = 0;

    private void doUpdateData(){
        if(listtemplate!=null){
            listtemplate.clear();
            listtemplate=null;
        }
        POSITION = 0;
        listtemplate = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            SSFragmentItems fragment = new SSFragmentItems();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", data.get(i));
            fragment.setArguments(bundle);
            listtemplate.add(fragment);
        }
    }

    void updateInfo(){
        String defaulttemplate= pref.getPref().getString(C.MOCKUP_PATH,C.PLAK_SS_MOCKUP_ROOT+"Redmi 1S");
        SSFrameData ssframe = (SSFrameData) data.get(POSITION).get("data");
        templTitle.setText(ssframe.getSsframe());
        String tambahan = "";
        if(ssframe.getSs_x()!=point.x || ssframe.getSs_y()!=point.y)
            tambahan = "your screensize is not equal with this template";
        templInfo.setText(String.format("screenshot size : %d x %d\n%s", ssframe.getSs_x(), ssframe.getSs_y(), tambahan));
        String path = (String) data.get(POSITION).get("path");
        templIsapply.setVisibility(path.equals(defaulttemplate)?View.VISIBLE:View.GONE);
    }

    @Override
    public void Komplit(List<HashMap<String, Object>> result) {
        doUpdateData();
        adapter = new MVPagerAdapter(getFragmentManager(), listtemplate);
        mpager.setAdapter(adapter);
        mpager.setCurrentItem(0);
        updateInfo();
        mpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                debug.log(position);

                POSITION = position;
                updateInfo();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}