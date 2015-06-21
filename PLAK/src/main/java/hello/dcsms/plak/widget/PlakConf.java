package hello.dcsms.plak.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.Utils;
import hello.dcsms.plak.Utils.fa;

public class PlakConf extends Activity {
	private LinearLayout pathview;
	private ListView pickerlist;
	private String ROOTSD = Environment.getExternalStorageDirectory() + "";
	private String ROOTPARENT = "/storage";
	private DirListAdapter listadapter;
	private String CURDIR;
	private Utils u;
	String filetype = null;
	public static String RESULT ="RESULT";
	public static String FILE_TYPE="type";
	public static String TYPE_CONF="plakconf";
	public static String TYPE_TEMPLATE="pz";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plakconfpicker);
		filetype = getIntent().getStringExtra(FILE_TYPE);
		if(filetype==null)
			filetype="*";

		u = new Utils(getApplicationContext());
		setFinishOnTouchOutside(false);
		pathview = (LinearLayout) findViewById(R.id.picker_dir_view);
		pickerlist = (ListView) findViewById(R.id.picker_list);
		pickerlist.setOnItemClickListener(onListclick);
		updateListAdapter(getList(new File(ROOTSD)));

	}

	@Override
	public void onBackPressed() {
		File parent = new File(CURDIR).getParentFile();
		if (parent != null && parent.exists() && CURDIR != null
				&& !CURDIR.equals(ROOTPARENT) && parent.canRead())
			updateListAdapter(getList(parent));
		else {
			setResult(RESULT_CANCELED);
			super.onBackPressed();
		}
	}

	private void addDirectory(String[] path) {
		if (pathview.getChildCount() > 0)
			pathview.removeAllViews();

		for (int i = 0; i < path.length; i++) {
			TextView tv = new TextView(getApplicationContext());
			tv.setText(path[i]);
			tv.setPadding(5, 0, 5, 0);
			tv.setTextSize(u.dp2px(10));
			tv.setId(i);
			tv.setTag(path);
			tv.setTextColor(Color.WHITE);
			tv.setBackground(getResources().getDrawable(
					R.drawable.path_drawable));
			tv.setOnClickListener(onpathdirclick);
			pathview.addView(tv);

		}

	}

	private OnClickListener onpathdirclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int pos = v.getId();
			String[] path = (String[]) v.getTag();

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i <= pos; i++) {

				sb.append(path[i] + "/");
			}

			updateListAdapter(getList(new File(sb.toString())));
		}
	};

	private List<PickerListData> getList(File f) {
		if (!f.canRead())
			return null;
		CURDIR = f.getAbsolutePath();
		addDirectory(getDir(f.getAbsolutePath()));
		File[] files = f.listFiles();
		List<PickerListData> list = new ArrayList<PlakConf.PickerListData>();

		for (File file : files) {

			if (file.canRead() && !file.isHidden()) {

				if (file.isDirectory()) {
					PickerListData data = new PickerListData();
					data.setFile(false);
					data.setNamafile(file.getName());
					data.setPathfile(file.getAbsolutePath());
					list.add(data);
				} else {
					if (file.getName().endsWith(filetype)) {
						PickerListData data = new PickerListData();
						data.setFile(true);
						data.setNamafile(file.getName());
						data.setPathfile(file.getAbsolutePath());
						list.add(data);
					}
				}

			}

		}
		return list;
	}

	private String[] getDir(String dir) {

		String[] res = dir.split("/");
		for (String string : res) {

		}
		return res;
	}

	private void updateListAdapter(List<PickerListData> data) {
		if (data == null)
			return;
		listadapter = new DirListAdapter(data);
		listadapter.sortByName(true);
		pickerlist.setAdapter(listadapter);
	}

	private OnItemClickListener onListclick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> paramAdapterView, View v,
				int pos, long id) {

			PickerListData data = (PickerListData) listadapter.getItem(pos);

			if (!data.isFile())
				updateListAdapter(getList(new File(data.getPathfile())));
			else {
				File f = new File(data.getPathfile());
				if (f.exists() && f.canRead()
						&& data.getNamafile().endsWith(filetype)) {
					Intent i = new Intent();
					i.putExtra(RESULT, data.getPathfile());
					setResult(RESULT_OK, i);
					finish();
				}
			}

		}
	};

	private class DirListAdapter extends BaseAdapter {
		private List<PickerListData> data;

		public DirListAdapter(List<PickerListData> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int paramInt) {
			return data.get(paramInt);
		}

		@Override
		public long getItemId(int paramInt) {
			return paramInt;
		}

		@Override
		public View getView(int pos, View paramView, ViewGroup paramViewGroup) {
			VHolder v = null;
			if (paramView == null) {
				LayoutInflater infl = (LayoutInflater) getApplicationContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				paramView = infl.inflate(R.layout.pickerlist_item, null);
				v = new VHolder();
				v.ic = (TextView) paramView
						.findViewById(R.id.pickerlist_item_icon);
				v.filename = (TextView) paramView
						.findViewById(R.id.pickerlist_item_teks);
				paramView.setTag(v);
			} else {
				v = (VHolder) paramView.getTag();
			}
			PickerListData p = data.get(pos);

			if (p.isFile)
				v.ic.setText(fa._file);
			else
				v.ic.setText(fa._folder);

			v.filename.setText(p.namafile);
			v.filename.setTag(p.pathfile);

			return paramView;
		}

		public void sortByName(final boolean asc) {
			Comparator<PickerListData> comparator = new Comparator<PickerListData>() {
				@Override
				public int compare(PickerListData dbData, PickerListData dbData2) {
					int resasc = dbData.getNamafile().compareToIgnoreCase(
							dbData2.getNamafile());
					int resdesc = dbData2.getNamafile().compareToIgnoreCase(
							dbData.getNamafile());
					return asc ? resasc : resdesc;
				}
			};
			Collections.sort(data, comparator);
			notifyDataSetChanged();
		}
	}

	private class PickerListData {
		public String getNamafile() {
			return namafile;
		}

		public String getPathfile() {
			return pathfile;
		}

		public boolean isFile() {
			return isFile;
		}

		public void setNamafile(String namafile) {
			this.namafile = namafile;
		}

		public void setPathfile(String pathfile) {
			this.pathfile = pathfile;
		}

		public void setFile(boolean isFile) {
			this.isFile = isFile;
		}

		String namafile;
		String pathfile;
		boolean isFile;
	}

	private class VHolder {
		TextView ic, filename;
	}

}
