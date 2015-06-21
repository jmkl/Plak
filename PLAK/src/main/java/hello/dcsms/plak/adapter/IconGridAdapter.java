package hello.dcsms.plak.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import java.util.List;

import hello.dcsms.plak.R;

public class IconGridAdapter extends BaseDynamicGridAdapter {
	public IconGridAdapter(Context context, List<?> items, int columnCount) {
		super(context, items, columnCount);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		VHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_grid, null);
			holder = new VHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (VHolder) convertView.getTag();
		}
		holder.build(getItem(position).toString());
		return convertView;
	}

	private class VHolder {
		private TextView titleText;
		private ImageView image;

		private VHolder(View view) {
			titleText = (TextView) view.findViewById(R.id.item_title);
			image = (ImageView) view.findViewById(R.id.item_img);
		}

		void build(String title) {
			titleText.setText(title);
			image.setImageResource(R.drawable.ic_launcher);
		}
	}
}