package hello.dcsms.plak.Test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import org.askerov.dynamicgrid.DynamicGridView;
import org.askerov.dynamicgrid.DynamicGridView.OnDragListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hello.dcsms.plak.R;
import hello.dcsms.plak.adapter.IconGridAdapter;

public class SignalCluster extends Activity implements OnClickListener {
	Button tbl, tbl_mobile;
	LinearLayout CLUSTER_LAYOUT, CLUSTER_LAYOUT2, MOBILE_COMBO, ROOT;
	ViewGroup WIFI_PARENT, MOB_PARENT;
	View roaming, MOB_INOUT, MOB_SIG, MOB_CARRIER, MOB_TYPE;
	WIFIMOBILE icon_layout = WIFIMOBILE.WIFI;

	private DynamicGridView GRIDVIEW;

	enum WIFIMOBILE {
		WIFI, MOBILE
	}

	String[] MOBILESIGN = new String[] { "InOut", "Signal", "Carrier",
			"Signal Type" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teslayout);
		tbl = (Button) findViewById(R.id.button1);
		tbl_mobile = (Button) findViewById(R.id.button2);
		ROOT = (LinearLayout) findViewById(R.id.root_include_layout);
		CLUSTER_LAYOUT = (LinearLayout) findViewById(R.id.cluster_layout);
		CLUSTER_LAYOUT2 = (LinearLayout) findViewById(R.id.cluster_layout2);
		GRIDVIEW = (DynamicGridView) findViewById(R.id.dynamic_grid);
		tbl.setOnClickListener(this);
		tbl_mobile.setOnClickListener(this);
		findViewCluster();

		GRIDVIEW.setAdapter(new IconGridAdapter(this, new ArrayList<String>(
				Arrays.asList(MOBILESIGN)), 4));
		GRIDVIEW.setOnDragListener(gridOnDrag);
		GRIDVIEW.setOnItemLongClickListener(onGridLongClick);
		GRIDVIEW.setOnDropListener(new DynamicGridView.OnDropListener() {
			@Override
			public void onActionDrop() {
				GRIDVIEW.stopEditMode();
				List<Object> items = ((IconGridAdapter) GRIDVIEW
						.getAdapter()).getItems();
				MOBILE_COMBO.removeAllViews();
				for (Object object : items) {
					String obj = (String) object;
					if (obj.equals("InOut"))
						MOBILE_COMBO.addView(MOB_INOUT);
					else if (obj.equals("Signal"))
						MOBILE_COMBO.addView(MOB_SIG);
					else if (obj.equals("Carrier"))
						MOBILE_COMBO.addView(MOB_CARRIER);
					else if (obj.equals("Signal Type"))
						MOBILE_COMBO.addView(MOB_TYPE);
				}
			}
		});

	}

	private OnItemLongClickListener onGridLongClick = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> paramAdapterView,
				View paramView, int paramInt, long paramLong) {
			GRIDVIEW.startEditMode(paramInt);

			return true;
		}
	};
	private OnDragListener gridOnDrag = new OnDragListener() {

		@Override
		public void onDragStarted(int position) {
		}

		@Override
		public void onDragPositionsChanged(int oldPosition, int newPosition) {

		}
	};

	private void findViewCluster() {
		WIFI_PARENT = (ViewGroup) CLUSTER_LAYOUT.getChildAt(0);
		roaming = CLUSTER_LAYOUT.getChildAt(1);
		MOB_PARENT = (ViewGroup) CLUSTER_LAYOUT.getChildAt(2);
		// Mobile part
		MOBILE_COMBO = (LinearLayout) MOB_PARENT.getChildAt(0);
		findViewMobile_Child(MOBILE_COMBO);
	}

	private void findViewMobile_Child(ViewGroup v) {
		MOB_INOUT = v.getChildAt(0);
		MOB_SIG = v.getChildAt(1);
		MOB_CARRIER = v.getChildAt(2);
		MOB_TYPE = v.getChildAt(3);

	}

	boolean ltr = false;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			doButton1();
			break;

		case R.id.button2:
			if (ltr) {
				ltr = false;
				ROOT.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
			} else {
				ltr = true;
				ROOT.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
			}
			ROOT.requestLayout();
			break;
		}

	}

	private void doButton1() {
		switch (icon_layout) {
		case WIFI:
			CLUSTER_LAYOUT.removeAllViews();
			CLUSTER_LAYOUT.addView(WIFI_PARENT);
			CLUSTER_LAYOUT.addView(MOB_PARENT);
			CLUSTER_LAYOUT.addView(roaming);
			icon_layout = WIFIMOBILE.MOBILE;
			break;

		case MOBILE:
			CLUSTER_LAYOUT.removeAllViews();
			CLUSTER_LAYOUT.addView(MOB_PARENT);
			CLUSTER_LAYOUT.addView(WIFI_PARENT);
			CLUSTER_LAYOUT.addView(roaming);
			icon_layout = WIFIMOBILE.WIFI;
			break;
		}
	}

}
