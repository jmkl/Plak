package hello.dcsms.plak.Frgmnt;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringChain;

import java.util.ArrayList;
import java.util.List;

import hello.dcsms.plak.R;
import hello.dcsms.plak.widget.AwesomeFrameLayout;

/**
 * Created by jmkl on 5/12/2015.
 */
public abstract class BaseActivity extends Activity {

    private void initActionBar() {
        ActionBar ab = getActionBar();
        ab.setLogo(R.drawable.logo);
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayUseLogoEnabled(true);
    }

    List<View> allViews = new ArrayList<>();
    SpringChain mSpringchain = SpringChain.create();
    FrameLayout root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int resource = getLayoutResource();
        setContentView(resource);
        onCreateX(savedInstanceState);
        initActionBar();
        root = (FrameLayout)findViewById(R.id.statusbar_edit_root);
        if(root!=null && root instanceof FrameLayout){
            getChildren();
        }

        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                List<Spring> springs = mSpringchain.getAllSprings();
                for (int i = 0; i < springs.size(); i++) {
                    springs.get(i).setCurrentValue(allViews.get(i).getHeight());
                }
                root.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSpringchain
                                .setControlSpringIndex(0)
                                .getControlSpring()
                                .setEndValue(0);
                    }
                },400);
            }
        });

    }

    private void getChildren() {
      int count =  root.getChildCount();
        for (int i = 0; i < count; i++) {
            final View v = root.getChildAt(i);
               allViews.add(v);
                mSpringchain.addSpring(new SimpleSpringListener(){
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        super.onSpringUpdate(spring);
                        float value = (float) spring.getCurrentValue();
                        v.setTranslationY(value);
                    }
                });
        }
    }

    private void searchView(View v){
            int count = ((ViewGroup) v).getChildCount();
        for (int i = 0; i < count; i++) {
            final View vv = ((ViewGroup) v).getChildAt(i);
            if(vv instanceof ViewGroup)
                searchView(vv);
            else {
                allViews.add(vv);
                mSpringchain.addSpring(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        super.onSpringUpdate(spring);
                        float value = (float) spring.getCurrentValue();
                        vv.setTranslationY(value);
                    }
                });
        }}

    }

    protected abstract int getLayoutResource();

    public abstract void onCreateX(Bundle savedInstanceState);
}
