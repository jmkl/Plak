package hello.dcsms.plak.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import hello.dcsms.plak.R;

public class NavDrawAdapter extends BaseAdapter {
	private String[] menu;
	private Context c;

	public NavDrawAdapter(Context c, String[] menu) {
		this.menu = menu;
		this.c = c;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return menu.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return menu[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int pos, View v, ViewGroup arg2) {
		ViewHolder vh = null;
		if (v == null) {
			LayoutInflater infl = (LayoutInflater) c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = infl.inflate(R.layout.nav_menu, null);
			vh = new ViewHolder();
			vh.tv = (TextView) v.findViewById(R.id.nav_tv);
			v.setTag(vh);
		} else {
			vh = (ViewHolder) v.getTag();
		}
		vh.tv.setText(menu[pos]);
		return v;
	}

	public class ViewHolder {
		TextView tv;
	}

}
