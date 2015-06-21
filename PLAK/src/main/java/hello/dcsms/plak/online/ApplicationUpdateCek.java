package hello.dcsms.plak.online;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.online.model.AppsVersiModel;

/**
 * Created by jmkl on 5/19/2015.
 */
public class ApplicationUpdateCek extends AsyncTask<Void,Void,AppsVersiModel> {
    public interface onUpdateListener{
        void onUpdateAvailable(int versi, String link);
    }






    String local = "http://192.168.43.172/plak/cek.php?update";
    static InputStream is = null;
    Debugger d;
    onUpdateListener listener;
    public  ApplicationUpdateCek(onUpdateListener listener){
        this.listener = listener;
        d = new Debugger();

    }

    @Override
    protected void onPostExecute(AppsVersiModel appsVersiModel) {
        super.onPostExecute(appsVersiModel);
        if(appsVersiModel!=null && listener!=null){
            listener.onUpdateAvailable(appsVersiModel.getVersi(),appsVersiModel.getLink());
        }
    }

    @Override
    protected AppsVersiModel doInBackground(Void... voids) {
        AppsVersiModel result = null;
        try {
            String url = local;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntitiy = httpResponse.getEntity();
            is = httpEntitiy.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            String json = sb.toString();
            Gson gson = new Gson();
            result = gson.fromJson(json, AppsVersiModel.class);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JsonSyntaxException e){
            e.printStackTrace();
        }
        return result;
    }


}
