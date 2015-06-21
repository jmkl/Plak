package hello.dcsms.plak.Frgmnt;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import hello.dcsms.plak.C;
import hello.dcsms.plak.Hello;
import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.PrefUtils;
import hello.dcsms.plak.Utils.fa;
import hello.dcsms.plak.adapter.FavListAdapter;
import hello.dcsms.plak.db.DBDataSource;
import hello.dcsms.plak.db.DBdata;

/**
 * Created by jmkl on 4/28/2015.
 */
public abstract class DragnDropAbstract extends BaseActivity implements View.OnLongClickListener,
        View.OnClickListener

{
    public RelativeLayout root;
    private TextView jam, batrai, sinyal, icon, trafik, staticon;
    private String JAM = "12:00PM", BAT = "B", TRF = "666K/s", SIG = "D",
            ICO = "EF", STAT_IC = "G";
    private TextView posinfo;
    Button apply;
    private LinearLayout parent;
    private FrameLayout fr;
    int root_w;
    private SwipeMenuListView lv;
    SlidingUpPanelLayout slideup;
    DBDataSource db;
    private ImageView iv_slidepanel;
    private View hover_view;
    char __jam = 0xF017;
    char __icon = 0xF075;
    char __sinyal = 0xF012;
    char __batrai = 0xF0E7;
    char __trafik = 0xF0E4;
    char __stat_icon = 0xF072;
    String _jam = __jam + "";
    String _icon = __icon + "";
    String _sinyal = __sinyal + "";
    String _batrai = __batrai + "";
    String _trafik = __trafik + "";
    String _stat_icon = __stat_icon + "";
    List<LayoutData> ldkiri = new ArrayList<LayoutData>();
    List<LayoutData> ldkanan = new ArrayList<LayoutData>();
    List<LayoutData> ldtengah = new ArrayList<LayoutData>();
    List<DBdata> database_data;
    FavListAdapter adapter;
    protected abstract void addToview(String dragData, float x);
    int w3;

    public int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }



    @Override
    public void onPause() {
        db._CLose();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        db = new DBDataSource(this);
        db._Open();
    }

    private boolean is10TimeAlready() {
        SharedPreferences pref = getSharedPreferences("blinkme",
                Context.MODE_PRIVATE);
        int count = pref.getInt("brapakali", 0);
        if (count < 10) {
            SharedPreferences.Editor edit = pref.edit();
            edit.putInt("brapakali", count + 1);
            edit.commit();
            return false;
        } else
            return true;

    }

    private String[] awesome = new String[]{_jam, _batrai, _sinyal, _icon,
            _trafik, _stat_icon};

    @Override
    protected int getLayoutResource() {
        return R.layout.editstatusbar;
    }

    @Override
    public void onCreateX(Bundle savedInstanceState) {
        onCreateView();
    }

    public void onCreateView() {
        db = new DBDataSource(this);
        db._Open();
        findView();
        setTag();
        setOnClick(jam, batrai, sinyal, icon, trafik, staticon);
        setText(jam, batrai, sinyal, icon, trafik, staticon);
        apply.setOnClickListener(this);
        root.setOnDragListener(new MyDragListener());
        apply.setEnabled(false);
        if (!is10TimeAlready())
            animateROOT();
    }

    protected Typeface getFontAwesome() {
        return fa._fonts(getAssets());
    }

    private void setText(TextView... v) {
        for (int i = 0; i < v.length; i++) {
            v[i].setTypeface(getFontAwesome());
            v[i].setText(awesome[i]);
        }

    }

    private ObjectAnimator in(View v) {
        return ObjectAnimator.ofFloat(v, View.ALPHA, 1.0F);
    }

    private ObjectAnimator out(View v) {
        return ObjectAnimator.ofFloat(v, View.ALPHA, 0.0F);
    }

    private Animator[] get(int howmuch, View... v) {
        List<ObjectAnimator> obj = new ArrayList<ObjectAnimator>();
        for (View view : v) {
            for (int i = 0; i < howmuch; i++) {
                obj.add(out(view));
                obj.add(in(view));
            }
        }
        return obj.toArray(new Animator[obj.size()]);

    }

    private void animThisShit() {
        // jam, batrai, sinyal, icon, trafik, staticon;
        AnimatorSet ani = new AnimatorSet();
        ani.playSequentially(get(3, root, jam, icon, sinyal, batrai, trafik,
                staticon));
        ani.setInterpolator(new AccelerateDecelerateInterpolator());
        ani.setDuration(100);
        ani.start();

    }

    private void animateROOT() {
        animThisShit();

    }

    private void setOnClick(View... v) {
        for (View view : v) {
            view.setOnLongClickListener(this);
        }

    }

    private void setTag() {

        jam.setTag(JAM);
        batrai.setTag(BAT);
        sinyal.setTag(SIG);
        icon.setTag(ICO);
        trafik.setTag(TRF);
        staticon.setTag(STAT_IC);
    }

    private void findView() {
        slideup = (SlidingUpPanelLayout) findViewById(R.id.editor_root);
        hover_view = findViewById(R.id.hover_view);
        lv = (SwipeMenuListView) findViewById(R.id.fav_list);
        posinfo = (TextView) findViewById(R.id.pos_info);
        root = (RelativeLayout) findViewById(R.id.ll_host);
        jam = (TextView) findViewById(R.id.v_jam);
        batrai = (TextView) findViewById(R.id.v_batrai);
        sinyal = (TextView) findViewById(R.id.v_sinyal);
        icon = (TextView) findViewById(R.id.v_icon);
        trafik = (TextView) findViewById(R.id.v_trafik);
        staticon = (TextView) findViewById(R.id.v_staticon);
        apply = (Button) findViewById(R.id.tblApply);
        parent = (LinearLayout) findViewById(R.id.v_parent);
        fr = (FrameLayout) findViewById(R.id.statusbarsimple);
        iv_slidepanel = (ImageView) findViewById(R.id.tbl_menu_up);
        root.setDrawingCacheEnabled(true);
        preparePreviewRoot();
        //inisiasiStatusbarSimple();
        inisiasiListView();
        inisiasislideUp();
    }

    public abstract void preparePreviewRoot();
    private void inisiasislideUp() {
        slideup.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            float curfloat;

            @Override
            public void onPanelSlide(View panel, float f) {
                iv_slidepanel.setRotation(f * 180.0F);
            }

            @Override
            public void onPanelHidden(View panel) {
            }

            @Override
            public void onPanelExpanded(View panel) {

            }

            @Override
            public void onPanelCollapsed(View panel) {

            }

            @Override
            public void onPanelAnchored(View panel) {
            }
        });

    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "open" item

            SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
            openItem.setBackground(new ColorDrawable(getResources().getColor(R.color.merah)));
            openItem.setWidth(dp2px(60));
            openItem.setIcon(getResources().getDrawable(
                    R.drawable.menu_apply));

            menu.addMenuItem(openItem);

            SwipeMenuItem deleteItem = new SwipeMenuItem(DragnDropAbstract.this);
            deleteItem.setBackground(new ColorDrawable(
                    getResources().getColor(R.color.merahtua)));
            deleteItem.setWidth(dp2px(60));
            deleteItem.setIcon(getResources().getDrawable(
                    R.drawable.menu_delete));

            menu.addMenuItem(deleteItem);
        }
    };

    // set creator

    @Override
    public boolean onLongClick(View v) {
        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
        ClipData dragData = new ClipData((CharSequence) v.getTag(),
                new String[]{"text/plain"}, item);
        View.DragShadowBuilder myShadow = new MyDragShadBuilder(v);
        boolean isdrag = v.startDrag(dragData, myShadow, null, 0);
        v.setVisibility(isdrag ? View.INVISIBLE : View.VISIBLE);
        Vibrator vibrea = (Vibrator) this.getSystemService(
                Context.VIBRATOR_SERVICE);
        vibrea.vibrate(100);
        return false;
    }

    private class MyDragShadBuilder extends View.DragShadowBuilder {
        public Drawable shadow;

        public MyDragShadBuilder(View v) {
            super(v);

            Drawable d = getResources().getDrawable(
                    R.drawable.stat_drag);

            shadow = d;
        }

        @Override
        public void onProvideShadowMetrics(Point size, Point touch) {
            int w = getView().getWidth();
            int h = getView().getHeight();

            shadow.setBounds(0, 0, w * 2, h * 2);
            size.set(w * 2, h * 2);
            touch.set(w, h);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            shadow.draw(canvas);
        }

    }

    boolean tengah = false;

    void animhover(float from, float to) {
        if (to == hover_view.getX())
            return;
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(hover_view, View.TRANSLATION_X,
                from, to));
        set.setDuration(0);
        set.start();
    }

    private class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            root_w = root.getWidth();
            w3 = root_w / 3;
            hover_view.setLayoutParams(new RelativeLayout.LayoutParams(w3, root
                    .getHeight()));
            animhover(hover_view.getX(), w3 * 3);
            int action = event.getAction();
            boolean result = false;

            if (action == DragEvent.ACTION_DRAG_STARTED) {
                v.setBackgroundColor(Color.RED);
                v.invalidate();

                return true;

            } else if (action == DragEvent.ACTION_DRAG_ENTERED
                    || action == DragEvent.ACTION_DRAG_LOCATION) {
                int x = (int) event.getX();
                tengah = false;
                if (x < w3) {
                    animhover(hover_view.getX(), 0);
                    // posinfo.setText("Di kiri");
                } else if (x > w3 && x < w3 * 2)
                    if (ldtengah.size() > 0) {

                        tengah = true;
                        v.setBackgroundColor(Color.RED);
                        v.invalidate();
                        return false;
                    } else {
                        animhover(hover_view.getX(), w3);
                        // posinfo.setText("Di tengah");

                    }
                else if (x > w3 * 2) {
                    animhover(hover_view.getX(), w3 * 2);
                    // posinfo.setText("Di kanan");

                } else {
                    animhover(hover_view.getX(), w3 * 3);
                    // posinfo.setText("");
                }
                v.setBackgroundColor(Color.BLACK);
                v.invalidate();

                return true;

            } else if (action == DragEvent.ACTION_DRAG_EXITED) {
                v.setBackgroundColor(Color.RED);
                v.invalidate();
                return false;

            } else if (action == DragEvent.ACTION_DROP) {
                if (tengah) {
                    v.setBackgroundColor(Color.RED);
                    v.invalidate();
                    return false;
                }
                ClipData.Item item = event.getClipData().getItemAt(0);
                String dragData = (String) item.getText();
                v.setBackgroundColor(Color.BLACK);
                v.invalidate();
                if (dragData.equals(JAM))
                    jam.setVisibility(View.GONE);
                else if (dragData.equals(BAT))
                    batrai.setVisibility(View.GONE);
                else if (dragData.equals(SIG))
                    sinyal.setVisibility(View.GONE);
                else if (dragData.equals(ICO))
                    icon.setVisibility(View.GONE);
                else if (dragData.equals(TRF))
                    trafik.setVisibility(View.GONE);
                else if (dragData.equals(STAT_IC))
                    staticon.setVisibility(View.GONE);
                addToview(dragData, event.getX());
                // posinfo.setText("");
                animhover(hover_view.getX(), w3 * 3);
                return true;

            } else if (action == DragEvent.ACTION_DRAG_ENDED) {
                v.setBackgroundColor(Color.BLACK);
                v.invalidate();
                Log.e("END",
                        " pos : " + Boolean.toString(result)
                                + Float.toString(event.getX()));
                if (!event.getResult()) {

                    viewDenLiak(false);
                }
                animhover(hover_view.getX(), w3 * 3);
                // posinfo.setText("");

                return event.getResult();
            }
            return true;

        }

    }


    public void viewDenLiak(boolean b) {
        int VISI;
        if (b)
            VISI = View.GONE;
        else
            VISI = View.INVISIBLE;
        if (jam.getVisibility() == VISI)
            jam.setVisibility(View.VISIBLE);
        if (batrai.getVisibility() == VISI)
            batrai.setVisibility(View.VISIBLE);
        if (sinyal.getVisibility() == VISI)
            sinyal.setVisibility(View.VISIBLE);
        if (icon.getVisibility() == VISI)
            icon.setVisibility(View.VISIBLE);
        if (trafik.getVisibility() == VISI)
            trafik.setVisibility(View.VISIBLE);
        if (staticon.getVisibility() == VISI)
            staticon.setVisibility(View.VISIBLE);

    }

    public View getViewByTag(String tag) {
        if (tag.equals(BAT))
            return batrai;
        else if (tag.equals(ICO))
            return icon;
        else if (tag.equals(JAM))
            return jam;
        else if (tag.equals(SIG))
            return sinyal;
        else if (tag.equals(TRF))
            return trafik;
        else if (tag.equals(STAT_IC))
            return staticon;
        else
            return null;
    }


    boolean cekAllItem() {
        Log.e("CHEK", Integer.toString(parent.getChildCount()));
        return parent.getChildCount() <= 0;
    }

    public class LayoutData {
        public View v;
    }

    private int posView(String[] tumpukanJerami, String jerami) {
        for (int i = 0; i < tumpukanJerami.length; i++) {
            if (jerami.equals(tumpukanJerami[i]))
                return i;
        }
        return -1;
    }

    @SuppressWarnings("unused")
    private View posView(View[] v, String[] tumpukanJerami, String jerami) {
        int pos = posView(tumpukanJerami, jerami);
        if (pos > -1)
            return v[pos];
        else
            return null;
    }

    private ArrayList<String> getListLD(List<LayoutData> data) {
        ArrayList<String> d = new ArrayList<String>();
        for (LayoutData ld : data) {
            d.add((String) ((TextView) ld.v).getText());
        }
        return d;
    }

    private void UpdateDataBaseNpreferences() {

        // DBdata d = new DBdata();
        String kiri = DBdata.List2str(getListLD(ldkiri));
        String kanan = DBdata.List2str(getListLD(ldkanan));
        String tengah = DBdata.List2str(getListLD(ldtengah));

        SharedPreferences pref = this.getSharedPreferences(
                C.PAKETPLAK + "_preferences", Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(C.PREF_SB_KANAN, kanan);
        edit.putString(C.PREF_SB_KIRI, kiri);
        edit.putString(C.PREF_SB_TENGAH, tengah);
        edit.commit();

        Intent intent = new Intent();
        intent.setAction(C.MODSTATUSBAR);
        intent.putExtra(C.PREF_SB_KANAN, kanan);
        intent.putExtra(C.PREF_SB_KIRI, kiri);
        intent.putExtra(C.PREF_SB_TENGAH, tengah);
        this.sendBroadcast(intent);

        PrefUtils.fix();

        root.buildDrawingCache();
        DBdata da = new DBdata();
        da.setKanan(kanan);
        da.setKiri(kiri);
        da.setTengah(tengah);
        da.setTanggal(System.currentTimeMillis());
        Bitmap prev = root.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        prev.compress(Bitmap.CompressFormat.PNG, 100, stream);
        da.setPreview(stream.toByteArray());
        prev.recycle();
        db.BikinData(da);

        updateDatabase(da);

        resetView();

    }


    private void updateDatabase(DBdata db) {
        if (database_data != null) {
            database_data.add(db);
            adapter.notifyDataSetChanged();
        }
    }

    private void resetView() {


    }

    private void inisiasiListView() {
        lv.setMenuCreator(creator);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int pos,
                                    long arg3) {
                lv.smoothOpenMenu(pos);

            }
        });
        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu,
                                           int index) {
                DBdata dbdata = (DBdata) adapter.getItem(position);
                switch (index) {
                    case 0:

                        SharedPreferences pref =
                                getSharedPreferences(C.PAKETPLAK + "_preferences",
                                        Context.MODE_WORLD_READABLE);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString(C.PREF_SB_KANAN, dbdata.getKanan());
                        edit.putString(C.PREF_SB_KIRI, dbdata.getKiri());
                        edit.putString(C.PREF_SB_TENGAH, dbdata.getTengah());
                        edit.commit();

                        Intent intent = new Intent();
                        intent.setAction(C.MODSTATUSBAR);
                        intent.putExtra(C.PREF_SB_KANAN, dbdata.getKanan());
                        intent.putExtra(C.PREF_SB_KIRI, dbdata.getKiri());
                        intent.putExtra(C.PREF_SB_TENGAH, dbdata.getTengah());
                        sendBroadcast(intent);
                        PrefUtils.fix();
                        break;
                    case 1:
                        db.DeleteData(dbdata.getIDs());
                        database_data.remove(position);
                        adapter.notifyDataSetChanged();
                        // slideup.setPanelState(PanelState.COLLAPSED);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        if (database_data == null) {
            database_data = new ArrayList<DBdata>();
            for (DBdata dBdata : db.getAllData()) {
                database_data.add(dBdata);
            }
        }
        adapter = new FavListAdapter(this, database_data);
        lv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        animateThis();
        UpdateDataBaseNpreferences();
/*

        View[] SS_V = new View[]{ // which
                SS_JAM, // 0
                SS_NOTIF_ICON, // 1
                SS_STATUSBARICONS, // 2
                SS_TRAFIK, // 3
                SS_SINYAL, // 4
                SS_BATRAI}; // 5
        String[] viewtag = new String[]{ // tag
                JAM, // 0
                ICO, // 1
                STAT_IC,// 2
                TRF, // 3
                SIG, // 4
                BAT};// 5
*/

		/*
         * View kananroot = null; View kananchild = null;
		 *
		 * for (LayoutData d : ldkanan) { if (kananroot == null) { kananroot =
		 * posView(SS_V, viewtag, (String) ((TextView) d.v).getText());
		 * kananroot.setLayoutParams(RLParam.KANAN()); AntiGravity(kananroot,
		 * Gravity.RIGHT | Gravity.CENTER); } else {
		 *
		 * if (kananchild == null) { kananchild = posView(SS_V, viewtag,
		 * (String) ((TextView) d.v).getText());
		 * kananchild.setLayoutParams(RLParam.LEFT_OF(kananroot .getId()));
		 * AntiGravity(kananchild, Gravity.RIGHT | Gravity.CENTER); } else {
		 * View baru = posView(SS_V, viewtag, (String) ((TextView)
		 * d.v).getText()); if (baru != null) {
		 * baru.setLayoutParams(RLParam.LEFT_OF(kananchild.getId()));
		 * AntiGravity(baru, Gravity.RIGHT | Gravity.CENTER);
		 *
		 * kananchild = baru; } }
		 *
		 * } }
		 *
		 * View kiriroot = null; View kirichild = null; for (LayoutData d :
		 * ldkiri) { if (kiriroot == null) { kiriroot = posView(SS_V, viewtag,
		 * (String) ((TextView) d.v).getText());
		 * kiriroot.setLayoutParams(RLParam.KIRI()); AntiGravity(kiriroot,
		 * Gravity.LEFT | Gravity.CENTER); } else {
		 *
		 * if (kirichild == null) { kirichild = posView(SS_V, viewtag, (String)
		 * ((TextView) d.v).getText()); kirichild
		 * .setLayoutParams(RLParam.RIGHT_OF(kiriroot.getId()));
		 * AntiGravity(kirichild, Gravity.LEFT | Gravity.CENTER); } else { View
		 * baru = posView(SS_V, viewtag, (String) ((TextView) d.v).getText());
		 * if (baru != null) {
		 * baru.setLayoutParams(RLParam.RIGHT_OF(kirichild.getId()));
		 * AntiGravity(baru, Gravity.LEFT | Gravity.CENTER);
		 *
		 * kirichild = baru; } }
		 *
		 * } } for (LayoutData d : ldtengah) { View tengah = posView(SS_V,
		 * viewtag, (String) ((TextView) d.v).getText());
		 * tengah.setLayoutParams(RLParam.TENGAH());
		 *
		 * }
		 */

    }

    private void animateThis() {
        AnimatorSet set = new AnimatorSet();

        set.playTogether(ObjectAnimator.ofFloat(apply, View.SCALE_X, 1.1f),
                ObjectAnimator.ofFloat(apply, View.SCALE_Y, 1.1f));
        set.setInterpolator(new AccelerateDecelerateInterpolator());

        set.setDuration(100);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                AnimatorSet set = new AnimatorSet();
                set.playTogether(ObjectAnimator.ofFloat(apply, View.SCALE_X, 1f),
                        ObjectAnimator.ofFloat(apply, View.SCALE_Y, 1f));
                set.setInterpolator(new AccelerateDecelerateInterpolator());
                set.setDuration(50);
                set.start();
            }
        });
        set.start();
    }

    private void AntiGravity(View v, int g) {

        if (v instanceof RelativeLayout)
            ((RelativeLayout) v).setGravity(g);
        else if (v instanceof LinearLayout)
            ((LinearLayout) v).setGravity(g);
        else if (v instanceof TextView)
            ((TextView) v).setGravity(g);

    }

/*
    RelativeLayout RL_ROOT;
    TextView SS_JAM;
    LinearLayout SS_NOTIF_ICON;
    LinearLayout SS_STATUSBARICONS;
    TextView SS_TRAFIK;
    LinearLayout SS_SINYAL;
    LinearLayout SS_BATRAI;
*/

/*    // xjam, xbatrai, xsinyal, xicon, xtrafik, staticon;
    private void inisiasiStatusbarSimple() {
        if (RL_ROOT != null)
            return;
        RL_ROOT = (RelativeLayout) fr.getChildAt(2);
        // 0
        SS_JAM = (TextView) RL_ROOT.getChildAt(0);
        // 1
        SS_NOTIF_ICON = (LinearLayout) RL_ROOT.getChildAt(1);
        // 2
        SS_STATUSBARICONS = (LinearLayout) RL_ROOT.getChildAt(2);
        // 2.0
        ImageView SS_MOREICON = (ImageView) SS_STATUSBARICONS.getChildAt(0);
        // 2.1
        SS_TRAFIK = (TextView) SS_STATUSBARICONS.getChildAt(1);

        // 2.2
        LinearLayout SS_STATUSICON = (LinearLayout) SS_STATUSBARICONS
                .getChildAt(2);

        // 2.3
        SS_SINYAL = (LinearLayout) SS_STATUSBARICONS.getChildAt(3);

        // 2.3.0
        ViewGroup SS_SINYALCLUSTER1 = (ViewGroup) SS_SINYAL.getChildAt(0);
        // 2.3.1
        ViewGroup SS_SINYALCLUSTER2 = (ViewGroup) SS_SINYAL.getChildAt(1);

        ImageView SS_BATRAI_IMG = (ImageView) SS_STATUSBARICONS.getChildAt(4);

        TextView SS_BATRAI_NUM = (TextView) SS_STATUSBARICONS.getChildAt(5);

        SS_BATRAI = new LinearLayout(RL_ROOT.getContext());
        SS_BATRAI.setLayoutParams(RLParam.WRAP_KONTEN());
        SS_BATRAI.setId(BATTID);

        // removeadd
        SS_STATUSBARICONS.removeView(SS_TRAFIK);
        RL_ROOT.addView(SS_TRAFIK);
        // SS_STATUSBARICONS.removeView(SS_STATUSICON);
        // RL_ROOT.addView(SS_STATUSICON);
        SS_SINYAL.removeView(SS_SINYALCLUSTER1);
        SS_SINYAL.removeView(SS_SINYALCLUSTER2);
        SS_STATUSBARICONS.removeView(SS_SINYAL);
        SS_SINYAL.addView(SS_SINYALCLUSTER1);
        SS_SINYAL.addView(SS_SINYALCLUSTER2);
        RL_ROOT.addView(SS_SINYAL);

        SS_STATUSBARICONS.removeView(SS_BATRAI_IMG);
        SS_STATUSBARICONS.removeView(SS_BATRAI_NUM);
        SS_BATRAI.addView(SS_BATRAI_IMG);
        SS_BATRAI.addView(SS_BATRAI_NUM);
        RL_ROOT.addView(SS_BATRAI);

        // AntiGravity(SS_JAM, Gravity.CENTER_VERTICAL);
        // AntiGravity(SS_NOTIF_ICON, Gravity.CENTER_VERTICAL);
        // AntiGravity(SS_STATUSBARICONS, Gravity.CENTER_VERTICAL);
        // AntiGravity(SS_TRAFIK, Gravity.CENTER_VERTICAL);
        // AntiGravity(SS_SINYAL, Gravity.CENTER_VERTICAL);
        // AntiGravity(SS_BATRAI, Gravity.CENTER_VERTICAL);

        SS_JAM.setLayoutParams(RLParam.KANAN());
        SS_NOTIF_ICON.setLayoutParams(RLParam.LEFT_OF(SS_JAM.getId()));
        SS_STATUSBARICONS
                .setLayoutParams(RLParam.LEFT_OF(SS_NOTIF_ICON.getId()));
        SS_TRAFIK.setLayoutParams(RLParam.LEFT_OF(SS_STATUSBARICONS.getId()));
        SS_SINYAL.setLayoutParams(RLParam.LEFT_OF(SS_TRAFIK.getId()));
        SS_BATRAI.setLayoutParams(RLParam.LEFT_OF(SS_SINYAL.getId()));
    }

    private int BATTID = 666;
    // SS_JAM, // 0
    // SS_NOTIF_ICON, // 1
    // SS_STATUSBARICONS, // 2
    // SS_TRAFIK, // 3
    // SS_SINYAL, // 4
    // SS_BATRAI }; /*/

}
