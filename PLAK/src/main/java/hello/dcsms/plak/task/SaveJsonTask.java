package hello.dcsms.plak.task;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import hello.dcsms.plak.manual.ManualDataJsonHelper;
import hello.dcsms.plak.manual.ManualItemDB;
import hello.dcsms.plak.manual.ManualItemData;

/**
 * Created by jmkl on 4/30/2015.
 */
public class SaveJsonTask extends AsyncTask<Context,String,String> {
    public interface onSaveTaskListener{
        void onComplete(String res);
    }
    List<ManualItemData> data;
    private onSaveTaskListener listener;
    private String to;
    public SaveJsonTask(List<ManualItemData> data,String to) {
    this.data =data;
        this.to=to;
    }
    public void setListener(onSaveTaskListener listener){
        this.listener=listener;
    }



    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(listener!=null)
            listener.onComplete(s);
    }

    @Override
    protected String doInBackground(Context... contexts) {
        if(to==null)
            return ManualDataJsonHelper.SaveJson(data, null);
        else{

            ManualItemDB dbhelper = new ManualItemDB(contexts[0]);
                dbhelper.Open();

            List<ManualItemData> newdata = new ArrayList<ManualItemData>();
            for (ManualItemData mi : data) {
                dbhelper.updateCekData(mi);
                if (mi.isCek() == 1) {
                    newdata.add(mi);
                }
            }
            if (newdata.size() > 0) {
                ManualDataJsonHelper.SaveJson(newdata, to);
                return "Succesfully applied";
            } else {
                return "Nothing selected";
            }
        }

    }
}
