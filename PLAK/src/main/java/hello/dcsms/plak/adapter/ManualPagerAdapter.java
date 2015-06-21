package hello.dcsms.plak.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import java.util.List;
import hello.dcsms.plak.Frgmnt.MSChildFragment;
import hello.dcsms.plak.Utils.fa;

/**
 * Created by jmkl on 5/3/2015.
 */
public class ManualPagerAdapter extends FragmentPagerAdapter {
    List<MSChildFragment> mfragment;
    public ManualPagerAdapter(FragmentManager fm, List<MSChildFragment> mfragment) {
        super(fm);
        this.mfragment=mfragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String result = "";
        switch (position){
            case 0:
                result= fa._list;
                break;
            case 1:
                result= fa._anchor;
                break;
        }
        return result;

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
