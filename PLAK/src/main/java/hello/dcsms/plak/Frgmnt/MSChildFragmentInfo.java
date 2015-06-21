package hello.dcsms.plak.Frgmnt;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.preference.PreferenceActivity.Header;

import com.hb.views.PinnedSectionListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hello.dcsms.plak.C;
import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.manual.ManualDataJsonHelper;
import hello.dcsms.plak.manual.ManualItemData;

/**
 * Created by jmkl on 5/3/2015.
 */
public class MSChildFragmentInfo extends MSChildFragment {


    private PinnedSectionListView mList;
    private CurInfoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View Header = inflater.inflate(R.layout.manual_setting_layout_info, null);
        mList = (PinnedSectionListView) Header.findViewById(R.id.man_setting_info_list);
        inisiasiData();

        return Header;
    }


    void inisiasiData() {
        adapter = new CurInfoAdapter();
        mList.setAdapter(adapter);
    }

    private boolean cekNamaPaket(List<String> namapaket,String key) {
       for(String nama:namapaket){
           if(nama.equals(key))
               return true;
       }
        return false;
    }




    public void updateInfo() {

        inisiasiData();
    }


    private class CurInfoAdapter extends  BaseAdapter implements  PinnedSectionListView.PinnedSectionListAdapter{

        List<ManualItemData> data;
        public CurInfoAdapter(){
            data = ManualDataJsonHelper.ReadJson(C.PLAKMANUALSETTINGSFILE);
        }
        /**
         * This method shall return 'true' if views of given type has to be pinned.
         *
         * @param viewType
         */
        @Override
        public boolean isItemViewTypePinned(int viewType) {
            return false;
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
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder vh;
            if(view==null){
                view = getActivity().getLayoutInflater().inflate(R.layout.txt_layout,null);
                vh = new ViewHolder();
                vh.tvitem = (TextView)view.findViewById(R.id.txt_list_item);
                vh.tvsubitem = (TextView)view.findViewById(R.id.txt_list_subitem);
                view.setTag(vh);
            }else{
                vh = (ViewHolder) view.getTag();
            }

            ManualItemData d = data.get(i);
            String val_paket = d.getNamaPaket();
            String val_field = d.getNamaField();
            String val_val = d.getNilai();
            vh.tvitem.setText(val_field+" : "+val_val);
             vh.tvsubitem.setText(val_paket);
            return view;

        }

        class ViewHolder{
            TextView tvitem,tvsubitem;
        }
    }

  
}
