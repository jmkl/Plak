package hello.dcsms.plak.Utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.MetricAffectingSpan;
import android.text.style.RelativeSizeSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hello.dcsms.plak.data.SuperScriptTagData;
import hello.dcsms.plak.data.SuperScriptTagData.STIPE;

public class StringUtils {
	public static ArrayList<String> getListFromString(String data) {
		ArrayList<String> d = new ArrayList<String>();
		String[] ld = data.split(",");
		for (int i = 0; i < ld.length; i++) {
			if (!ld[i].equals(""))
				d.add(ld[i]);
		}
		return d;
	}

	public static boolean isMIUI(Context c) {
		PackageManager p = c.getPackageManager();
		try {
			p.getApplicationInfo("com.miui.cloudservice", 0);
		} catch (NameNotFoundException e) {
			return false;
		}
		return true;
	}

	private static boolean isSup(String tes) {
		return tes.contains("{") || tes.contains("}");
	}

	public static List<SuperScriptTagData> RegexSuperScriptTag(String text) {
		Pattern p = Pattern.compile("\\{.*?\\}|\\$.*?\\$");
		Matcher m = p.matcher(text);
		List<SuperScriptTagData> matches = new ArrayList<SuperScriptTagData>();
		while (m.find()) {
			SuperScriptTagData data = new SuperScriptTagData();
			data.setData(m.group(), m.start(), m.group().length(),
					isSup(m.group()) ? STIPE.SUPERSCR : STIPE.SUBSCR);
			matches.add(data);
		}

		return matches;
	}

	public static String findSuperScriptTag(String format) {
		if (format.contains("{") && format.contains("}")) {
			return format.substring(format.indexOf("{"),
					format.indexOf("}") + 1);
		}
		return null;
	}

	public static SpannableStringBuilder SuperscriptIt(String txt,
			List<SuperScriptTagData> sdata) {

		SpannableStringBuilder formatted = new SpannableStringBuilder(txt);

		for (int i = 0; i < sdata.size(); i++) {
			int pos = sdata.get(i).getPosisi();
			int len = sdata.get(i).getLen();
			formatted.replace(pos - i * 2, pos + 1 - i * 2, "");
			formatted.replace(pos + len - 2 - i * 2, pos + len - 1 - i * 2, "");
			CharacterStyle style = new RelativeSizeSpan(0.75f);
			formatted.setSpan(style, pos - i * 2, pos + len - 2 - i * 2,
					Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			if (sdata.get(i).getType() == STIPE.SUPERSCR)
				formatted.setSpan(new CustomSupScripts(), pos - i * 2, pos
						+ len - 2 - i * 2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		}	
		return formatted;
	}

	private static class CustomSupScripts extends MetricAffectingSpan implements
			ParcelableSpan {
		public CustomSupScripts() {
			// TODO Auto-generated constructor stub
		}

		public CustomSupScripts me() {
			return this;
		}

		@Override
		public int getSpanTypeId() {
			return TextUtils.CAP_MODE_CHARACTERS;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
		}

		@Override
		public void updateDrawState(TextPaint tp) {
			tp.baselineShift += (int) (tp.ascent() / 3.25);
		}

		@Override
		public void updateMeasureState(TextPaint tp) {
			tp.baselineShift += (int) (tp.ascent() / 3.25);
		}
	}

	public static String getPathFromURI(Context context, Uri uri) {
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = {"_data"};
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection,
						null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
				// Eat it
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;

	}
}
