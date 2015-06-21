package hello.dcsms.plak.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import hello.dcsms.plak.R;
import hello.dcsms.plak.db.DBdata;

public class FavListAdapter extends BaseAdapter {
	private List<DBdata> data;
	private Context mContext;

	public FavListAdapter(Context c, List<DBdata> data) {
		this.data = data;
		this.mContext = c;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		ViewHolder vh = null;
		if (view == null) {
			LayoutInflater iff = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = iff.inflate(R.layout.fav_item, null);

			vh = new ViewHolder();
			vh.tv = (TextView) view.findViewById(R.id.fav_keterangan);
			vh.iv = (ImageView) view.findViewById(R.id.fav_image);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		DBdata d = data.get(pos);

		SimpleDateFormat sdf = new SimpleDateFormat(mContext.getString(R.string.ket_sb_layout),
				Locale.getDefault());

		System.out.println(sdf.format(d.getTanggal()));
		vh.tv.setText(" " + sdf.format(d.getTanggal()));
		vh.iv.setImageBitmap(BitmapFactory.decodeByteArray(d.getPreview(), 0,
				d.getPreview().length));

		return view;
	}

	public class ViewHolder {
		TextView tv;
		ImageView iv;
	}

}
