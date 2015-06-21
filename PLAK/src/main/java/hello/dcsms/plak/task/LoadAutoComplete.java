package hello.dcsms.plak.task;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import hello.dcsms.plak.manual.AutoCompleteData;
import hello.dcsms.plak.manual.AutoCompleteItemDB;
import hello.dcsms.plak.task.iface.OnLoadAutoComplete;

public class LoadAutoComplete extends AsyncTask<Void, String, List<AutoCompleteData>> {
    AutoCompleteItemDB db;
    OnLoadAutoComplete listener;
    String keys;
    String what;
    String namapaket;

    /**
     * keys = "_type" what = "integer"
     */
    public LoadAutoComplete(Context c) {
        db = new AutoCompleteItemDB(c);
        db.Open();
    }

    public void setOnLoadListener(OnLoadAutoComplete listener) {
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(List<AutoCompleteData> result) {
        super.onPostExecute(result);
        listener.onComplete(result);
        db.Close();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected List<AutoCompleteData> doInBackground(Void... v) {
        return db.getAllData();
    }

}
