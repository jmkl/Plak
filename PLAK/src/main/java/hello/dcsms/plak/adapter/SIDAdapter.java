package hello.dcsms.plak.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import java.util.List;

import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.fa;
import hello.dcsms.plak.data.SIData;

public class SIDAdapter extends BaseDynamicGridAdapter {
	List<SIData> datas;

	public SIDAdapter(Context context, List<?> items, int columnCount) {
		super(context, items, columnCount);
		this.datas = (List<SIData>) items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		VHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.sid_item_grid, null);
			holder = new VHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (VHolder) convertView.getTag();
		}
		holder.build((SIData) getItem(position));
		return convertView;
	}

	public List<?> getAllItems() {
		return getItems();
	}

	private class VHolder {
		private TextView titleText;

		private VHolder(View view) {
			titleText = (TextView) view.findViewById(R.id.sid_item_ic);
			titleText.setTypeface(fa._fonts(getContext().getAssets()));

		}

		void build(SIData sd) {
			titleText.setText(sd.getText());
			titleText.setTag(sd.getKey());
		}
	}
}