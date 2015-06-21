package hello.dcsms.plak.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import hello.dcsms.plak.R;
import hello.dcsms.plak.manual.ManualItemData;

public class ExItemListAdapter extends BaseExpandableListAdapter {
	public class ViewHolder {
		TextView item, type, val;
		CheckBox cb;
	}

	private LayoutInflater inflater;
	private Context mContext;
	private HashMap<String, List<ManualItemData>> data;
	private List<String> group;

	public ExItemListAdapter(Context c,
			HashMap<String, List<ManualItemData>> data) {
		mContext = c;
		this.data = data;

	}

	@Override
	public ManualItemData getChild(int groupPosition, int childPosition) {
		return data.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View paramView, ViewGroup parent) {
		ViewHolder vh = null;
		if (paramView == null) {
			LayoutInflater infl = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			paramView = infl.inflate(R.layout.manual_list_item, null);
			vh = new ViewHolder();
			vh.item = (TextView) paramView.findViewById(R.id.ml_item_title);
			vh.type = (TextView) paramView.findViewById(R.id.ml_item_type);
			vh.val = (TextView) paramView.findViewById(R.id.ml_item_value);
			vh.cb = (CheckBox) paramView.findViewById(R.id.ml_item_cb);
			paramView.setTag(vh);
		} else {
			vh = (ViewHolder) paramView.getTag();
		}

		ManualItemData md = (ManualItemData) data.get(childPosition);
		vh.item.setText(md.getNamaField());
		vh.type.setText(md.getTipe());
		vh.val.setText(md.getNilai());
		vh.cb.setChecked(md.isCek()==1);

		return paramView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return data.get(groupPosition).size();
	}

	@Override
	public List<ManualItemData> getGroup(int groupPosition) {
		return data.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return data.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TextView textView = (TextView) convertView;
		if (textView == null)
			textView = (TextView) inflater.inflate(
					android.R.layout.preference_category, null);
		textView.setText("asdasd");
		return textView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
}
