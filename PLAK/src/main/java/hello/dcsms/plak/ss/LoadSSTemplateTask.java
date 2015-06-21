package hello.dcsms.plak.ss;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hello.dcsms.plak.C;
import hello.dcsms.plak.Hello;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.task.iface.OnTaskCaput;

/**
 * Created by jmkl on 5/1/2015.
 */
public class LoadSSTemplateTask extends AsyncTask<Void,HashMap<String,Object>, List<HashMap<String,Object>>> {
    private File f;

    public interface loadSSTemplateTaskLisetener{
        void Update(HashMap<String, Object> data);
        void Komplit(List<HashMap<String, Object>> result);
    }
    private Context context;
    File root = new File(C.PLAK_SS_MOCKUP_ROOT);
    private loadSSTemplateTaskLisetener listener;

    public LoadSSTemplateTask(Context context, loadSSTemplateTaskLisetener listener) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected void onPostExecute(List<HashMap<String,Object>> s) {
        super.onPostExecute(s);
        if(listener!=null)
            listener.Komplit(s);
    }

    @Override
    protected void onProgressUpdate(HashMap<String,Object>... values) {
        super.onProgressUpdate(values);
        if(listener!=null){
            listener.Update(values[0]);
    }}

    @Override
    protected List<HashMap<String,Object>> doInBackground(Void... voids) {

        return ListAllMockup();
    }
    int pos = 0;
    private List<HashMap<String,Object>> ListAllMockup() {
        List<HashMap<String,Object>> ssFrameDatas = new ArrayList<>();
        File[] f = root.listFiles();
        for (File fs : f) {

            if (fs.isDirectory()) {
                SSFrameData ss = getConfigurationFile(fs);
                if(ss!=null) {
                    HashMap<String, Object> ndata = new HashMap<>();
                    ndata.put("path", fs.getAbsolutePath());
                    ndata.put("data", ss);
                    ssFrameDatas.add(ndata);
                    publishProgress(ndata);

                }
            }
        }
        return ssFrameDatas;
    }

    public SSFrameData getConfigurationFile(File f) {
        this.f = f;
        File fconf = new File(f.getAbsolutePath() + "/" + "prop.plk");
        if (fconf.exists()) {

            return configParser(fconf);
        } else
            return null;
    }

    /**
     * parsing data dari
    * string
    * */
    public static SSFrameData configParser(String buffer){
        Gson gson = new Gson();
        return gson.fromJson(buffer,SSFrameData.class);
    }

    /**parsing data dari file*/
    public static SSFrameData configParser(File file) {
        String json = null;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            reader.close();
            json = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (json != null) {
            Gson gson = new Gson();
            return gson.fromJson(json, SSFrameData.class);
        } else
            return null;
    }

    private void anuinframedata(SSFrameData ssdata) {
//        SharedPreferences pref = context.getSharedPreferences("hello.dcsms.plak_preferences", Context.MODE_WORLD_READABLE);
//        String path = pref.getString(C.MOCKUP_PATH, null);
//        String root = path + "/";
//        SSFrameData.frameData f = ssdata.getSsframedata();
//        Bitmap bg = BitmapFactory.decodeFile(root + f.getBackground());
//        Bitmap frame = BitmapFactory.decodeFile(root + f.getFrame());
//
//        Bitmap b = Bitmap.createBitmap(f.getBg_width(), f.getBg_height(), Bitmap.Config.ARGB_8888);
//        List<Bitmap> str = new ArrayList<Bitmap>();
//        Matrix m = new Matrix();
//        m.postTranslate(0, 0);
//        Paint p = new Paint();
//        Canvas canvas = new Canvas(b);
//        canvas.drawBitmap(bg, 0, 0, p);
//        canvas.drawBitmap(frame, 0, 0, p);
//
//        for (SSFrameData.Overlay o : f.getOverlay()) {
//            canvas.drawBitmap(BitmapFactory.decodeFile(root + o.overlay_img), m, p);
//
//        }
//
//        Bitmap plak = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        plak.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        String s = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
//
//


    }

}
