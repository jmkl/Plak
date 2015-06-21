package hello.dcsms.plak;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * Created by jmkl on 5/3/2015.
 */
public abstract class BaseFragmentActivity extends Activity {
    protected abstract void doOnCreate(Bundle savedInstanceState);
    void initACTIONBAR() {
        getActionBar().setLogo(R.drawable.logo);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(true);

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doOnCreate(savedInstanceState);
        initACTIONBAR();
    }


}
