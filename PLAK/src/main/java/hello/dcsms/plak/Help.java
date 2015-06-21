package hello.dcsms.plak;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jmkl on 4/26/2015.
 */
public class Help extends Activity {
    ViewPager mpager;
    SharedPreferences pref;

    MPagerAdapter adapter;
    String result="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kenalan);
        pref = getSharedPreferences("help",MODE_PRIVATE);

        mpager = (ViewPager) findViewById(R.id.pager);

        adapter = new MPagerAdapter();
        mpager.setAdapter(adapter);
        mpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                    if(adapter.getCount()-1==i)
                       adapter.setText("Got it!");

            }
        });


    }

    private class MPagerAdapter extends PagerAdapter {
        TextView tv;
        int[] data = new int[]{
                R.drawable.prev_iconsize,
                R.drawable.prev_ws
        };

        public MPagerAdapter() {

        }
        public void setText(String str){
            if(tv!=null)
                tv.setText(str);
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View vi = getLayoutInflater().inflate(R.layout.kenalan_item,null);
            ImageView iv = (ImageView)vi.findViewById(R.id.img);
            tv = (TextView)vi.findViewById(R.id.txtexit);
            tv.setText(result);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putBoolean("helped",true);
                    edit.commit();
                    finish();
                }
            });
            iv.setImageDrawable(getResources().getDrawable(data[position]));
            container.addView(vi);
            return vi;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
