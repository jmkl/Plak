package hello.dcsms.plak.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hello.dcsms.plak.R;
import hello.dcsms.plak.manual.ManualItemData;

public class ManualItemListAdapter extends BaseAdapter implements Filterable{
	private List<ManualItemData> data;
    private List<ManualItemData> data_temp;
	private Context mContext;
	boolean asc = true;

	public ManualItemListAdapter(Context c, List<ManualItemData> data) {
		this.data = data;
        data_temp = new ArrayList<>(this.data);
		this.mContext = c;
	}

	public List<ManualItemData> getAllListData() {
		return data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int paramInt) {
		// TODO Auto-generated method stub
		return data.get(paramInt);
	}

	@Override
	public long getItemId(int paramInt) {
		// TODO Auto-generated method stub
		return paramInt;
	}

	@Override
	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		ViewHolder vh = null;
		if (paramView == null) {
			LayoutInflater infl = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			paramView = infl.inflate(R.layout.manual_list_item, null);
			vh = new ViewHolder();
			vh.item = (TextView) paramView.findViewById(R.id.ml_item_title);
			vh.type = (TextView) paramView.findViewById(R.id.ml_item_type);
			vh.val = (TextView) paramView.findViewById(R.id.ml_item_value);
			vh.desc = (TextView) paramView.findViewById(R.id.ml_item_desc);
			vh.packagename = (TextView) paramView
					.findViewById(R.id.ml_item_pack);
			vh.cb = (CheckBox) paramView.findViewById(R.id.ml_item_cb);
			vh.cb.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					CheckBox cb = (CheckBox) view;
					ManualItemData d = (ManualItemData) cb.getTag();
					d.setCek(cb.isChecked() ? 1 : 0);
				}
			});

			paramView.setTag(vh);
		} else {
			vh = (ViewHolder) paramView.getTag();
		}

		ManualItemData md = data.get(paramInt);
		vh.item.setText(md.getNamaField());
		vh.type.setText(md.getTipe());
		vh.val.setText(md.getNilai());
		vh.desc.setText(md.getCatatan());
		vh.packagename.setText(md.getNamaPaket());
		vh.cb.setChecked(md.isCek() == 1);
		vh.cb.setTag(md);

		return paramView;
	}

    @Override
    public Filter getFilter() {
        data = data_temp;
        Filter f = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults result = new FilterResults();
                List<ManualItemData> filterArr = new ArrayList<ManualItemData>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < data.size(); i++) {
                    ManualItemData dt = data.get(i);
                    if (dt.getNamaPaket().toLowerCase()
                            .contains(constraint.toString())
                            || dt.getNamaField().toLowerCase()
                            .contains(constraint.toString())
                            || dt.getTipe().toLowerCase()
                            .contains(constraint.toString())
                            || dt.getNilai().toLowerCase()
                            .contains(constraint.toString())) {
                        filterArr.add(dt);
                    }
                }

                result.count = filterArr.size();
                result.values = filterArr;

                return result;
            }

            @Override
            protected void publishResults(CharSequence charSequence,
                                          FilterResults filterResults) {

                data = (List<ManualItemData>) filterResults.values;
                notifyDataSetChanged();

            }
        };

        return f;
    }
// "name","packagename","checked","type"
	private String b2s(long val){
		if((int)val==1)
			return "true";
		else
			return "false";
	}

	public void sortBy(final int mode) {
		Comparator<ManualItemData> compare = new Comparator<ManualItemData>() {
			@Override
			public int compare(ManualItemData mit1, ManualItemData mit2) {
				if(asc) {
					if (mode == 0)
						return mit1.getNamaField().compareToIgnoreCase(mit2.getNamaField());
					else if (mode == 1)
						return mit1.getNamaPaket().compareToIgnoreCase(mit2.getNamaPaket());
					else if (mode == 2)
						return b2s(mit1.isCek()).compareToIgnoreCase(b2s(mit2.isCek()));
					else
						return mit1.getTipe().compareToIgnoreCase(mit2.getTipe());
				}else{
					if (mode == 0)
						return mit2.getNamaField().compareToIgnoreCase(mit1.getNamaField());
					else if (mode == 1)
						return mit2.getNamaPaket().compareToIgnoreCase(mit1.getNamaPaket());
					else if (mode == 2)
						return b2s(mit2.isCek()).compareToIgnoreCase(b2s(mit1.isCek()));
					else
						return mit2.getTipe().compareToIgnoreCase(mit1.getTipe());
				}


			}
		};
		asc =!asc;
		Collections.sort(data, compare);
		notifyDataSetChanged();
	}

	public class ViewHolder {
		TextView item, type, val, desc, packagename;
		CheckBox cb;
	}

}
