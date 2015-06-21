package hello.dcsms.plak.prefscreen;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import org.askerov.dynamicgrid.DynamicGridView;
import org.askerov.dynamicgrid.DynamicGridView.OnDropListener;

import java.util.List;

import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.adapter.SIDAdapter;
import hello.dcsms.plak.data.SIData;

public class DialogPrefs extends DialogPreference {
	LayoutInflater li;
	View myView;
	private String dialogResultString = SIData.DEFAULT;
	private DynamicGridView mGView;
	List<SIData> data;
	SIDAdapter adapter;

	public DialogPrefs(Context context, AttributeSet attrs) {
		super(context, attrs);
		li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	protected View onCreateDialogView() {
		View view = li.inflate(R.layout.dialog_mobile_icons, null);
		mGView = (DynamicGridView) view.findViewById(R.id.dialog_din_grid);
		mGView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> paramAdapterView,
					View paramView, int paramInt, long paramLong) {
				mGView.startEditMode(paramInt);
				return false;
			}
		});
		mGView.setOnDropListener(new OnDropListener() {
			@Override
			public void onActionDrop() {
				mGView.stopEditMode();
				List<SIData> data = (List<SIData>) ((SIDAdapter) mGView.getAdapter()).getAllItems();

				dialogResultString = SIData.convertData(data);

			}
		});

		return view;
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		builder.setTitle(getTitle());
		super.onPrepareDialogBuilder(builder);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		// TODO Auto-generated method stub
		super.onDialogClosed(positiveResult);
		if (positiveResult) {
			Editor editor = getEditor();
			editor.putString(getKey(), dialogResultString);
			editor.commit();
		}
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		// updatevalue of prefs
		// the view was created by my custom onCreateDialogView()
		myView = view;
		SharedPreferences pref = getSharedPreferences();
		dialogResultString = pref.getString(getKey(), "");
		if (!dialogResultString.equals("")) {
			List<SIData> data = SIData.getListFromString(dialogResultString);
			if (data != null)
				adapter = new SIDAdapter(getContext(), data, 4);
			else
				adapter = new SIDAdapter(getContext(), new SIData().getData(),
						4);
		} else
			adapter = new SIDAdapter(getContext(), new SIData().getData(), 4);
		mGView.setAdapter(adapter);

	}
}