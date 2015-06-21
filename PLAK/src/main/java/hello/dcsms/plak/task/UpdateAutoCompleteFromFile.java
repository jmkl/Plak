package hello.dcsms.plak.task;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import hello.dcsms.plak.Test.JSONData;
import hello.dcsms.plak.Test.JSONData.Items;
import hello.dcsms.plak.Test.JSONData.ListJSONData;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.JSONParser;
import hello.dcsms.plak.manual.AutoCompleteData;
import hello.dcsms.plak.manual.AutoCompleteItemDB;
import hello.dcsms.plak.task.iface.OnTaskCaput;

public class UpdateAutoCompleteFromFile extends
		AsyncTask<Void, AutoCompleteData, Void> {

	private OnTaskCaput listener;
	AutoCompleteItemDB ac_db;
	private File autocompletefile;

	public UpdateAutoCompleteFromFile(Context context, File jsonFile) {
		ac_db = new AutoCompleteItemDB(context);
		ac_db.Open();
		this.autocompletefile = jsonFile;
	}

	public void setListener(OnTaskCaput listener) {
		this.listener = listener;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		listener.onFinish();
		ac_db.Close();
	}

	@Override
	protected void onProgressUpdate(AutoCompleteData... values) {
		super.onProgressUpdate(values);

	}

	@Override
	protected Void doInBackground(Void... arg0) {

		ac_db.Open();

		JSONParser parser = new JSONParser();
		String json = parser.getJSONObjFromSD(autocompletefile
				.getAbsolutePath());
		Gson gs = new Gson();
		ListJSONData data = gs.fromJson(json, ListJSONData.class);

		List<JSONData> d = data.data;
		for (JSONData jsonData : d) {
			String key_namapaket = jsonData.getKey();
			List<Items> it = jsonData.getData();
			for (Items items : it) {
				String tipe = items.getType();
				String[] value = items.getValue();
				for (String field_value : value) {
					if (!ac_db.isItemExist(field_value)) {
						AutoCompleteData acd = new AutoCompleteData();
						acd.setNamapaket(key_namapaket);

						acd.setNama(field_value);
						acd.setType(tipe);
						AutoCompleteData newdata = ac_db.createACD(acd);
						publishProgress(newdata);
					}
				}

			}
		}
		return null;
	}

}
