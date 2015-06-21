package hello.dcsms.plak.Frgmnt;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import hello.dcsms.plak.C;
import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.PrefUtils;
import hello.dcsms.plak.jni.Helper;
import hello.dcsms.plak.ss.SSFrameData;
import hello.dcsms.plak.ss.WallpeperCuser;
import hello.dcsms.plak.ss.lazylist.LocalImageLoader;
import hello.dcsms.plak.widget.MToast;

/**
 * Created by jmkl on 5/1/2015.
 */
public class TemplateFragment extends Fragment implements View.OnClickListener,IonTemplateApply {

    LocalImageLoader imgLoader;
    HashMap<String, Object> data;
    ImageView img;
    RelativeLayout mainlayout,rootlayout;
    TextView tv_title;
    TextView tv_info;
    TextView tv_apply,tv_wallp;
    ImageView tv_cek;
    Display display;
    Point point;
    PrefUtils prefutils;
    SharedPreferences pref;
   // Helper plakHelper= new Helper();
    IonTemplateApply listener;

    public TemplateFragment() {
    }
    public void setListener(IonTemplateApply listener){
        this.listener=listener;
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        prefutils = new PrefUtils(getActivity());
        pref = prefutils.getPref();
        data = (HashMap<String, Object>) getArguments().get("data");
        display = getActivity().getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        View convertView = inflater.inflate(R.layout.pic_view, null);
        imgLoader = new LocalImageLoader(getActivity());
        img = (ImageView) convertView.findViewById(R.id.templ_img);
        rootlayout = (RelativeLayout) convertView.findViewById(R.id.templ_root);
        mainlayout = (RelativeLayout) convertView.findViewById(R.id.templ_layout);
        tv_title = (TextView) convertView.findViewById(R.id.templ_title);
        tv_info = (TextView) convertView.findViewById(R.id.templ_info);
        tv_apply = (TextView) convertView.findViewById(R.id.templ_apply);
        tv_wallp = (TextView) convertView.findViewById(R.id.templ_wallp);
        tv_cek = (ImageView) convertView.findViewById(R.id.templ_applycek);
        setListener(this);
        setViewInfo(data, mainlayout, img, tv_title, tv_info, tv_apply, tv_cek);


        return convertView;
    }

    boolean visible = false;
    SSFrameData ssframe=null;
    String path;
    private void setViewInfo(HashMap<String, Object> data, final RelativeLayout mainlayout, ImageView img, TextView tv_title, TextView tv_info, TextView tv_apply, ImageView tv_cek) {

        ssframe = (SSFrameData) data.get("data");
        path = (String) data.get("path");
        //plakHelper.storeBitmap(BitmapFactory.decodeFile(path+"/preview.jpg"));
        //img.setImageBitmap(plakHelper.getBitmapAndFree());
        imgLoader.DisplayImage(path, img);

        tv_title.setText(ssframe.getSsframe());
        String tambahan ="";



        if(ssframe.getSs_x()!=point.x||ssframe.getSs_y()!=point.y)
            tambahan = "your screensize is not equal with this template";
        tv_info.setText(String.format("screenshot size : %d x %d\n%s", ssframe.getSs_x(), ssframe.getSs_y(), tambahan));
         tv_apply.setOnClickListener(this);
        if(listener!=null)
          listener.onApply();

        tv_wallp.setOnClickListener(this);
        img.setOnClickListener(this);
    }


    @Override
    public void onResume() {

        super.onResume();
        if(listener!=null)
            listener.onApply();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.templ_apply:
                PrefUtils pref = new PrefUtils(getActivity());
                pref.edit(C.MOCKUP_PATH, data.get("path"));
                MToast.show(getActivity(), "Success");
                if(listener!=null)
                    listener.onApply();
                break;
            case R.id.templ_wallp:
                if(ssframe==null)return;
                Intent it = new Intent(getActivity(), WallpeperCuser.class);
                Bundle b = new Bundle();
                b.putInt(WallpeperCuser.KEY_H, ssframe.getSsframedata().getBg_height());
                b.putInt(WallpeperCuser.KEY_W, ssframe.getSsframedata().getBg_width());
                b.putString(WallpeperCuser.KEY_WALLPIMAGE,path+"/"+ssframe.getSsframedata().getBackground());
                it.putExtra(WallpeperCuser.BUNDLE_KEY,b);
                getActivity().startActivity(it);
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onApply();
    }

    @Override
    public void onApply() {

        if(pref==null || prefutils==null)
            return;
        String curpath = pref.getString(C.MOCKUP_PATH,null);
        if(curpath==null)
            return;
        tv_cek.setVisibility(curpath.equals(path) ? View.VISIBLE : View.GONE);
        tv_apply.setVisibility(curpath.equals(path)?View.GONE:View.VISIBLE);

    }
}
