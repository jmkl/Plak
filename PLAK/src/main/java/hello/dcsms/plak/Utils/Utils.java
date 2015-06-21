package hello.dcsms.plak.Utils;

import android.content.Context;
import android.util.TypedValue;

import java.io.File;

public class Utils {
	Context c;

	public Utils(Context c) {
		this.c = c;
	}

	public int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				c.getResources().getDisplayMetrics());
	}

	public static void cekDir(String droot) {
		File f = new File(droot);
		if(!f.exists())
			f.mkdirs();
	}
}
