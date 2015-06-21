package hello.dcsms.plak.data;

import java.util.ArrayList;
import java.util.List;

import hello.dcsms.plak.Utils.fa;

public class SIData {
	public String key;
	public String text;
	public static String INOUT = "INOUT";
	public static String SIGNAL = "SIGNAL";
	public static String CARRIER = "CARRIER";
	public static String SIGNAL_TYPE = "SIGNAL_TYPE";
	public static String DEFAULT = "INOUT,SIGNAL,CARRIER,SIGNAL_TYPE";

	public List<SIData> getData() {
		List<SIData> data = new ArrayList<SIData>();
		data.add(new SIData().set(INOUT, fa._compress));
		data.add(new SIData().set(SIGNAL, fa._signal));
		data.add(new SIData().set(CARRIER, fa._flag));
		data.add(new SIData().set(SIGNAL_TYPE, fa._h_square));
		return data;
	}

	private static SIData getDataFromKey(String key) {
		if (key.equals(INOUT))
			return new SIData().set(INOUT, fa._compress);
		else if (key.equals(SIGNAL))
			return new SIData().set(SIGNAL, fa._signal);
		else if (key.equals(CARRIER))
			return new SIData().set(CARRIER, fa._flag);
		else if (key.equals(SIGNAL_TYPE))
			return new SIData().set(SIGNAL_TYPE, fa._h_square);
		else
			return null;
	}

	public static List<SIData> getListFromString(String str) {
		String[] data = str.split(",");
		List<SIData> datas = new ArrayList<SIData>();
		for (String x : data) {
			SIData dd = getDataFromKey(x);
			if (dd != null)
				datas.add(dd);
		}
		if (datas.size() > 0)
			return datas;
		else
			return null;
	}

	public static String convertData(List<SIData> data) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < data.size(); i++) {
			if (i < data.size() - 1)
				builder.append(data.get(i).getKey() + ",");
			else
				builder.append(data.get(i).getKey());

		}
		return builder.toString();
	}

	public SIData set(String key, String txt) {
		setKey(key);
		setText(txt);
		return this;
	}

	public String getKey() {
		return key;
	}

	public String getText() {
		return text;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setText(String text) {
		this.text = text;
	}

}
