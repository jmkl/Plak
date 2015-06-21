package hello.dcsms.plak.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hello.dcsms.plak.R;
import hello.dcsms.plak.ss.lazylist.LocalImageLoader;

/**
 * Created by jmkl on 5/3/2015.
 */
public class GalleryAdapter extends BaseAdapter {
    LocalImageLoader imageLoader;
    List<HashMap<String, String>> data;
    private Context context;

    public GalleryAdapter(List<HashMap<String, String>> data,Context context) {
        this.data = data;
        this.context=context;
        imageLoader = new LocalImageLoader(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 10000+i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.galeri_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        imageLoader.DisplayImage(data.get(i).get("path"), holder.galItemIv);
        return view;
    }


    static class ViewHolder {
        @InjectView(R.id.gal_item_iv)
        ImageView galItemIv;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}