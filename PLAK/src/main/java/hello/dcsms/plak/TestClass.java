package hello.dcsms.plak;

import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.JSONParser;
import hello.dcsms.plak.data.TestData;

public class TestClass {

	public static void Test() {
		File f = new File(Environment.getExternalStorageDirectory()
				+ "/tesdata.json");

		JSONParser parser = new JSONParser();
		JSONArray ja = null;
		try {
			ja = new JSONArray(parser.getJSONObjFromSD(f.getAbsolutePath()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ja != null) {

			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo;
				try {
					jo = ja.getJSONObject(i);
					Iterator<String> keys = jo.keys();
					List<TestData> pkg = new ArrayList<TestData>();
					while (keys.hasNext()) {
						TestData tes = new TestData();
						JSONArray jar = jo.getJSONArray(keys.next());
						tes.setAraidata(jar);
						tes.setNamapaket(keys.next());
						pkg.add(tes);

					}

					for (TestData td : pkg) {
						if (td.getAraidata().length() > 0) {
							JSONObject jobj = td.getAraidata().getJSONObject(0);
							Iterator<String> j_k = jobj.keys();
							while (j_k.hasNext()) {

							}
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}

	}
}
