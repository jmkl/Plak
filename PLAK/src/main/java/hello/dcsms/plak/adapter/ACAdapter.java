package hello.dcsms.plak.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.manual.AutoCompleteData;
import hello.dcsms.plak.manual.ManualItemData;
import hello.dcsms.plak.widget.AddItemDialog;

public class ACAdapter extends ArrayAdapter<AutoCompleteData> implements Filterable {

    private ArrayList<AutoCompleteData> fullList;
    private ArrayList<AutoCompleteData> mOriginalValues;
    private ArrayFilter mFilter;
    private AddItemDialog.ITEMTYPE type;
    private LayoutInflater inflater;

    public ACAdapter(Context context, int resource, int textViewResourceId, List<AutoCompleteData> objects, AddItemDialog.ITEMTYPE type) {

        super(context, resource, textViewResourceId, objects);
        fullList = (ArrayList<AutoCompleteData>) objects;
        mOriginalValues = new ArrayList<AutoCompleteData>(fullList);
        this.type = type;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return fullList.size();
    }

    @Override
    public AutoCompleteData getItem(int position) {
        return fullList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH vh = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.autocomplete_item, null);
            vh = new VH();
            vh.tv = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(vh);
        } else
            vh = (VH) convertView.getTag();

        vh.tv.setText(fullList.get(position).getNama());
        return convertView;
    }

    class VH {
        TextView tv;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }


    private class ArrayFilter extends Filter {
        private Object lock;

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((AutoCompleteData)resultValue).getNama();
        }

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (lock) {
                    mOriginalValues = new ArrayList<AutoCompleteData>(fullList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    ArrayList<AutoCompleteData> list = new ArrayList<AutoCompleteData>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                final String constraint = prefix.toString().toLowerCase();

                ArrayList<AutoCompleteData> values = mOriginalValues;
                int count = values.size();

                ArrayList<AutoCompleteData> newValues = new ArrayList<AutoCompleteData>(count);

                for (int i = 0; i < count; i++) {
                    AutoCompleteData d = mOriginalValues.get(i);
                    String tipe = d.getType();

                    switch (type) {
                        case BOOLEAN:
                            if (tipe.equals(ManualItemData.TBOOL) && d.getNama().contains(constraint))
                                newValues.add(d);
                            break;
                        case STRING:
                            if (tipe.equals(ManualItemData.TSTRING) && d.getNama().contains(constraint))
                                newValues.add(d);
                            break;
                        case INTEGER:
                            if (tipe.equals(ManualItemData.TINT) && d.getNama().contains(constraint))
                                newValues.add(d);
                            break;
                        case DIMEN:
                            if (tipe.equals(ManualItemData.TDIMEN) && d.getNama().contains(constraint))
                                newValues.add(d);
                            break;
                        case COLOR:
                            if (tipe.equals(ManualItemData.TCOL) && d.getNama().contains(constraint))
                                newValues.add(d);
                            break;
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (results.values != null) {
                fullList = (ArrayList<AutoCompleteData>) results.values;
            } else {
                fullList = new ArrayList<AutoCompleteData>();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
//    private Filter f = new Filter() {
//        @Override
//        public CharSequence convertResultToString(Object resultValue) {
//            return ((AutoCompleteData)resultValue).getNama();
//        }
//
//        @Override
//        protected void publishResults(CharSequence paramCharSequence,
//                                      FilterResults results) {
//            clear();
//            if (results != null && results.count > 0) {
//                // we have filtered results
//                addAll((ArrayList<AutoCompleteData>) results.values);
//            } else {
//                addAll(data);
//            }
//            notifyDataSetChanged();
//
//        }
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            FilterResults result = new FilterResults();
//            List<AutoCompleteData> filterArr = new ArrayList<AutoCompleteData>();
//            constraint = constraint.toString().toLowerCase();
//            for (int i = 0; i < data.size(); i++) {
//                AutoCompleteData d = data.get(i);
//                String tipe = d.getType();
//
//                switch (type){
//                    case BOOLEAN:
//                        if(tipe.equals(ManualItemData.TBOOL)&&d.getNama().contains(constraint))
//                            filterArr.add(d);
//                        break;
//                    case STRING:
//                        if(tipe.equals(ManualItemData.TSTRING)&&d.getNama().contains(constraint))
//                            filterArr.add(d);
//                        break;
//                    case INTEGER:
//                        if(tipe.equals(ManualItemData.TINT)&&d.getNama().contains(constraint))
//                            filterArr.add(d);
//                        break;
//                    case DIMEN:
//                        if(tipe.equals(ManualItemData.TDIMEN)&&d.getNama().contains(constraint))
//                            filterArr.add(d);
//                        break;
//                    case COLOR:
//                        if(tipe.equals(ManualItemData.TCOL)&&d.getNama().contains(constraint))
//                            filterArr.add(d);
//                        break;
//
//                }
//
//
//            }
//
//            result.values = filterArr;
//            result.count = filterArr.size();
//            return result;
//        }
//    };
//
//
//}
