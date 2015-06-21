package hello.dcsms.plak.Frgmnt;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;

import java.util.Locale;

import hello.dcsms.plak.R;

public class PYSDFrag extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		WebView tvinfo = new WebView(getActivity());
		tvinfo.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		Locale local = Locale.getDefault();
		tvinfo.loadUrl("file:///android_asset/info-en.html");
		tvinfo.setBackgroundColor(getResources().getColor(R.color.merah));
		return tvinfo;
	}
}
