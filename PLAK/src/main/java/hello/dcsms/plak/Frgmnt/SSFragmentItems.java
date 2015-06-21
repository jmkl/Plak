package hello.dcsms.plak.Frgmnt;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import hello.dcsms.plak.C;
import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.PrefUtils;
import hello.dcsms.plak.ss.SSFrameData;
import hello.dcsms.plak.ss.WallpeperCuser;
import hello.dcsms.plak.ss.lazylist.LocalImageLoader;
import hello.dcsms.plak.widget.CustomButton;
import hello.dcsms.plak.widget.MToast;

/**
 * Created by jmkl on 5/11/2015.
 */
public class SSFragmentItems extends Fragment {


    @InjectView(R.id.templ_img)
    ImageView templImg;


    SSFrameData ssframe;
    String path;
    private HashMap<String, Object> data;
    LocalImageLoader loader;
    Bundle bundle;
    Debugger debug = new Debugger();



    int RETURNRESULT = getActivity().RESULT_CANCELED;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mcardview, null);
        ButterKnife.inject(this, view);



        bundle = getArguments();
        data = (HashMap<String, Object>) bundle.getSerializable("data");
        ssframe = (SSFrameData) data.get("data");
        path = (String) data.get("path");
        loader = new LocalImageLoader(getActivity());
        loader.DisplayImage(path, templImg);
        return view;


    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (loader != null) loader.freeMemory();
        ButterKnife.reset(this);
    }



    public void updateWallpaper() {
        loader = new LocalImageLoader(getActivity());
        loader.ReloadCurrent(path, templImg);
        debug.log("ON UPDATE WALLPAPER");
    }

    public void isActivated() {

    }
}
