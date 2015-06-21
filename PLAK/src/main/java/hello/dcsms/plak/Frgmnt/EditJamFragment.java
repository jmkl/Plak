package hello.dcsms.plak.Frgmnt;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import hello.dcsms.plak.C;
import hello.dcsms.plak.Hello;
import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.MemUtils;
import hello.dcsms.plak.Utils.PrefUtils;
import hello.dcsms.plak.Utils.StringUtils;
import hello.dcsms.plak.data.SuperScriptTagData;

public class EditJamFragment extends Fragment implements OnItemClickListener {
	TextView preview;
	EditText edit;
	ListView lv;
	MenuItem apply;
	private String MEM = "mem";
	boolean savespot = false;
	String dateformat = null;
	private String[] data = { "'mem'", "{ }", "$ $", "D", "E", "F", "G", "H",
			"K", "L", "M", "S", "W", "Z", "a", "c", "d", "h", "k", "m", "s",
			"w", "y", "z", "'", "''" };

	private String[] keterangan = { "Available Free Memory", "Superscript Tag",
			"Subscript Tag", "day in year", "day of week",
			"day of week in month", "era designator", "hour in day (0-23)",
			"hour in am/pm (0-11)", "stand-alone month", "month in year",
			"fractional seconds", "week in month", "time zone (RFC 822)",
			"am/pm marker", "stand-alone day of week", "day in month",
			"hour in am/pm (1-12)", "hour in day (1-24)", "minute in hour",
			"second in minute", "week in year", "year", "time zone",
			"escape for text", "single quote" };

	SharedPreferences pref;
	SharedPreferences.Editor editor;

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_apply, menu);

	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.sdf_menu_apply) {
			if (savespot && dateformat != null) {
				editor = pref.edit();
				editor.putString(C.FORMATJAM, dateformat);
				editor.commit();
				PrefUtils.fix();
				getActivity().sendBroadcast(
						new Intent().setAction(C.MODSTATUSBARRUNTIME));

			} else {
				Toast.makeText(getActivity(), "Date format invalid",
						Toast.LENGTH_SHORT).show();
			}
		}
		return true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.edittime_config, null);

		setHasOptionsMenu(true);
		pref = getActivity().getSharedPreferences(
				"hello.dcsms.plak_preferences", Context.MODE_WORLD_READABLE);
		preview = (TextView) v.findViewById(R.id.etc_preview);

		edit = (EditText) v.findViewById(R.id.etc_edit);
		lv = (ListView) v.findViewById(R.id.sdf_list);
		edit.setText(pref.getString(C.FORMATJAM, ""));
		DataJam d = new DataJam();
		d = isTimeFormatTrue(pref.getString(C.FORMATJAM, ""));
		if (d.boolres)
			doUpdateTextPreviewShit(d.txtres.replace(MEM, MemUtils.getMEM()));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, keterangan);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence paramCharSequence,
					int paramInt1, int paramInt2, int paramInt3) {




			}

			@Override
			public void beforeTextChanged(CharSequence paramCharSequence,
					int paramInt1, int paramInt2, int paramInt3) {




			}

			@Override
			public void afterTextChanged(Editable edt) {
				String result = edt.toString();
				DataJam d = new DataJam();
				d = isTimeFormatTrue(result);
				edit.setTextColor(d.boolres ? Color.BLACK : Color.RED);
				savespot = d.boolres;
				if (d.boolres) {
					dateformat = d.format;
					doUpdateTextPreviewShit(d.txtres.replace(MEM,
							MemUtils.getMEM()));

				} else {
					dateformat = null;
					preview.setText("");
				}

			}
		});
		return v;
	}

	private void doUpdateTextPreviewShit(String tempres) {
		// String tempres = d.txtres.replace(MEM, MemUtils.getMEM());
		// String span = StringUtils.findSuperScriptTag(tempres);
		long tes = System.currentTimeMillis();
		List<SuperScriptTagData> match = StringUtils
				.RegexSuperScriptTag(tempres);
		for (SuperScriptTagData ss : match) {

		}
		if (match.size() > 0)
			preview.setText((StringUtils.SuperscriptIt(tempres, match)));
		else
			preview.setText(tempres);



	}

	public class DataJam {
		boolean boolres;
		String txtres;
		String format;
	}

	public DataJam isTimeFormatTrue(String format) {
		long time = System.currentTimeMillis();
		DataJam result = new DataJam();
		result.boolres = false;
		try {
			SimpleDateFormat sdt = new SimpleDateFormat(format,
					Locale.getDefault());
			result.format = format;
			result.txtres = sdt.format(time);
			result.boolres = true;
		} catch (Exception e) {
			result.boolres = false;
			result.txtres = format;
		}
		return result;
	}

	// /asdasd{}
	// /as{}dasd
	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View paramView,
			int pos, long paramLong) {
		String format = data[pos];
		int x = edit.getSelectionStart();
		int x2 = edit.getSelectionEnd();
		String old = edit.getText().toString();
		edit.setText(old + format);
		if (format.equals("{ }") || format.equals("$ $")) {
			String[] c = format.split(" ");
			edit.setText(old.substring(0, x) + c[0] + old.substring(x,x2) + c[1]+old.substring(x2));
			edit.setSelection((format.length() / 2) + x);
		} else {

			edit.setText(old.substring(0, x) + format + old.substring(x));
			edit.setSelection(format.length() + x);
		}
	}

}
