package hello.dcsms.plak.Utils;

import android.os.Environment;
import android.util.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import hello.dcsms.plak.manual.ManualItemData;

public class JSONUtils {
	static String datadir = "/data/data/hello.dcsms.plak/files/";
	static File f = new File(Environment.getExternalStorageDirectory()
			+ "/hello.json");
	private static String P_SYSTEMUI = "com.android.systemui",
			P_MIHOME = "com.miui.mihome";

	private static void WriteItemValue(JsonWriter jw, ManualItemData md)
			throws IOException {
		String type = md.getTipe();
		jw.beginObject();
		jw.setIndent("\t");
		jw.name("tipe").value(md.getTipe());
		if (type.equals("string"))
			jw.name("nilai").value(md.getNilai());
		else if (type.equals("dimen") || type.equals("color")
				|| type.equals("integer"))
			jw.name("nilai").value(Integer.parseInt(md.getNilai()));
		else if (type.equals("bool"))
			jw.name("nilai").value(Integer.parseInt(md.getNilai()) == 1);
		else
			jw.name("nilai").value(md.getNilai());

		jw.name("catatan").value(md.getCatatan());
		jw.endObject();
	}

	public static void DoJSON(List<ManualItemData> data) throws JSONException,
			IOException {
		FileOutputStream fos = new FileOutputStream(f);
		StringWriter writer = new StringWriter();

		List<ManualItemData> systemui = new ArrayList<ManualItemData>();
		List<ManualItemData> mihome = new ArrayList<ManualItemData>();
		for (ManualItemData md : data) {
			if (md.getNamaPaket().equals(P_SYSTEMUI))
				systemui.add(md);
			else if (md.getNamaPaket().equals(P_MIHOME))
				mihome.add(md);
		}

		JsonWriter jw = new JsonWriter(writer);
		jw.beginArray();

		jw.beginObject();
		jw.name(P_SYSTEMUI);
		jw.beginArray();
		for (ManualItemData md : systemui) {

			jw.beginObject();
			jw.setIndent("\t");
			jw.name(md.getNamaField());
			WriteItemValue(jw, md);
			jw.endObject();
		}
		jw.endArray();
		jw.endObject();

		jw.beginObject();
		jw.name(P_MIHOME);
		jw.beginArray();
		for (ManualItemData md : mihome) {

			jw.beginObject();
			jw.setIndent("\t");
			jw.name(md.getNamaField());
			WriteItemValue(jw, md);
			jw.endObject();
		}
		jw.endArray();
		jw.endObject();

		jw.endArray();
		jw.close();

		String dataWrite = writer.toString();
		fos.write(dataWrite.getBytes());
		fos.close();
	}

	public static List<ItemData> get() {
		List<ItemData> result = new ArrayList<ItemData>();
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = new JSONObject(parser.getJSONObjFromSD(datadir + "mihome.json"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(obj==null)
			return null;
		try {
			JSONObject mihome = obj.getJSONObject("mihome");
			Iterator<String> mihome_child_keys = mihome.keys();
			List<String> mihome_child_array = new ArrayList<String>();
			while (mihome_child_keys.hasNext()) {
				mihome_child_array.add(mihome_child_keys.next());
			}

			if (mihome_child_array.size() > 0) {

				for (int i = 0; i < mihome_child_array.size(); i++) {
					ItemData d = new ItemData();
					d.setItem(mihome_child_array.get(i));
					String nama_item = mihome_child_array.get(i);
					JSONObject obitem = mihome.getJSONObject(nama_item);
					Iterator<String> item = obitem.keys();
					while (item.hasNext()) {
						String string = item.next();
						if (string.equals("tipe")) {
							String otipe = obitem.getString("tipe");
							d.setTipe(otipe);
							if (otipe.equals("integer")) {
								d.setIntValue(obitem.getInt("nilai"));
							} else if (otipe.equals("string")) {
								d.setStrValue(obitem.getString("nilai"));
							} else if (otipe.equals("boolean")) {
								d.setBoolValue(obitem.getBoolean("nilai"));
							} else if (otipe.equals("dimen")) {
								d.setDimValue(obitem.getInt("nilai"));
							} else if (otipe.equals("color")) {
								d.setColValue(obitem.getInt("nilai"));
							}

						}
					}
					result.add(d);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
