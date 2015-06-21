package hello.dcsms.plak.task;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import hello.dcsms.plak.C;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.PrefUtils;
import hello.dcsms.plak.ss.SSConfigurationParser;
import hello.dcsms.plak.ss.SSFrameData;

/**
 * Created by jmkl on 5/12/2015.
 */
public class SavingImageTask extends AsyncTask<Void, String, String> {
    public interface onSavingImageListener {
        void onUpdateStatus(String status);
        void onComplete(String result, int STATUSCODE);
    }
    private Context c;
    onSavingImageListener listener;
    public static int SUCCESS = 1;
    public static int ERROR = -1;
    int STATUSCODE;
    String url;

    public SavingImageTask(Context co, String url) {
        this.url = url;
        this.c = co;
    }

    public void setListener(onSavingImageListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if (listener != null) {
            listener.onUpdateStatus(values[0]);
        }
    }

    @Override
    protected void onPostExecute(String st) {
        super.onPostExecute(st);
        if (st != null) {
            if (listener != null)
                listener.onComplete(st, SUCCESS);
        } else
            listener.onComplete("Ouch", ERROR);


    }

    @Override
    protected String doInBackground(Void... voids) {


        PrefUtils pref = new PrefUtils(c);
        String path = pref.getPref().getString(C.MOCKUP_PATH, C.PLAK_SS_MOCKUP_ROOT + "Redmi 1S");
        PrefUtils.fix();
        if (path == null && !(new File(path).exists())) {
            return null;
        }

        String root = path + "/";
        SSFrameData ssdata = SSConfigurationParser.parse(root);
        SSFrameData.frameData f = ssdata.getSsframedata();
        File ssfile = new File(url);
        Bitmap ss = BitmapFactory.decodeFile(url);
        Bitmap bg = BitmapFactory.decodeFile(root + f.getBackground());
        Bitmap frame = BitmapFactory.decodeFile(root + f.getFrame());
        Matrix m = new Matrix();
        Bitmap b = Bitmap.createBitmap(f.getBg_width(), f.getBg_height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        canvas.drawBitmap(bg, 0, 0, null);
        m.postTranslate(f.getFrame_x(), f.getFrame_y());
        canvas.drawBitmap(frame, m, null);
        m.postTranslate(0 - f.getFrame_x(), 0 - f.getFrame_y());
        m.postTranslate(f.getPos_ss_x(), f.getPos_ss_y());
        canvas.drawBitmap(ss, m, null);
        m.postTranslate(0 - f.getPos_ss_x(), 0 - f.getPos_ss_y());

        for (SSFrameData.Overlay o : f.getOverlay()) {
            m.postTranslate(o.overlay_x, o.overlay_y);
            canvas.drawBitmap(BitmapFactory.decodeFile(root + o.overlay_img), m, null);
            m.postTranslate(0 - o.overlay_x, 0 - o.overlay_y);
        }

        OutputStream outStream = null;
        File files = new File(C.PLAK_SS_ROOT);
        if (!files.exists())files.mkdirs();
        File file = new File(C.PLAK_SS_ROOT + ssfile.getName());
        try {
            outStream = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
            outStream.flush();
            outStream.close();
            b.recycle();
            publishProgress("Saving mockup complete");
        } catch (FileNotFoundException e) {
            publishProgress("Duh! Error occured");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            publishProgress("Duh! Error occured");
            return null;
        }
        return file.getAbsolutePath();
    }


    public static String getPath(Context context, Uri uri) {
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