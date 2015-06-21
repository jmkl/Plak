package hello.dcsms.plak.Utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.robv.android.xposed.XSharedPreferences;
import hello.dcsms.plak.C;
import hello.dcsms.plak.ss.SSConfigurationParser;
import hello.dcsms.plak.ss.SSFrameData;
import hello.dcsms.plak.widget.Notif;

/**
 * Created by jmkl on 4/30/2015.
 */
public class BitmapUtils {

    public static void modipBitmap(Bitmap bmp) {

    }


    public static void SSUriToCrot(Context context, Uri url) {
        XSharedPreferences pref = new XSharedPreferences(C.PAKETPLAK);
        boolean hajar =pref.getBoolean(C.ENABLE_FRAME_SS,false);
        if(hajar) {
            System.gc();
            Intent intent = new Intent();
            intent.setClassName("hello.dcsms.plak", "hello.dcsms.plak.service.SSHelper");
            intent.putExtra("path",getPath(context,url));
            context.startService(intent);

//            NgeweinSS  ngewe= new NgeweinSS(context, url);
//            ngewe.execute();

        }


    }

    public static Bitmap getBitmapCarrier(String files) {
        File f = new File(files);
        Bitmap result = null;
        if(f.exists()&&f.canRead()){
            result = BitmapFactory.decodeFile(f.getAbsolutePath());
        }
        return  result;
    }


    private static class NgeweinSS extends AsyncTask<Void, String, String> {
        private Uri uri;
        private Context c;
        private Notif mnotif;
        private  int NOT_ID=0x666;

        public NgeweinSS(Context co, Uri uri) {

            this.uri = uri;
            this.c = co;
            mnotif = new Notif(co,"Preparing mockup. Please wait",NOT_ID,getPath(co,uri));
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            mnotif.updateNotif(values[0]);
        }

        @Override
        protected void onPostExecute(String st) {
            super.onPostExecute(st);
            if(st!=null)
                mnotif.updateNotifAndFinish("Mockup complete",st);
            else
                mnotif.updateNotifAndFinishonError("Duh, failed to create mockup, please try again. clearing RAM might help");


        }

        @Override
        protected String doInBackground(Void... voids) {



                XSharedPreferences pref = new XSharedPreferences(C.PAKETPLAK);
                pref.makeWorldReadable();
                String path = pref.getString(C.MOCKUP_PATH, C.PLAK_SS_MOCKUP_ROOT+"Redmi 1S");
                if (path == null && !(new File(path).exists())){
                    return null;
                }


                SSFrameData ssdata = SSConfigurationParser.parse(path + "/");

                SSFrameData.frameData f = ssdata.getSsframedata();
                String root = path + "/";
                String url = getPath(c, uri);
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
                m.postTranslate(f.getPos_ss_x(),f.getPos_ss_y());
                canvas.drawBitmap(ss, m, null);
                m.postTranslate(0 - f.getPos_ss_x(), 0 - f.getPos_ss_y());
                for (SSFrameData.Overlay o : f.getOverlay()) {
                    m.postTranslate(o.overlay_x, o.overlay_y);
                    canvas.drawBitmap(BitmapFactory.decodeFile(root + o.overlay_img), m, null);
                    m.postTranslate(0-o.overlay_x,0-o.overlay_y);
                }
                OutputStream outStream = null;

                File files = new File(C.PLAK_SS_ROOT);
                if(!files.exists())
                    files.mkdirs();
                File file = new File(C.PLAK_SS_ROOT+ssfile.getName());


                try {

                    outStream = new FileOutputStream(file);
                    b.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
                    outStream.flush();
                    outStream.close();

                    b.recycle();

                    MediaScannerConnection.scanFile(c,
                            new String[]{file.getAbsolutePath()}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                   //publishProgress("Saving complete");
                                }
                            });

                    publishProgress("Saving mockup complete");
                } catch (FileNotFoundException e) {

                    publishProgress("Duh! Error occured");

                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    publishProgress("Duh! Error occured");

                    return  null;
                }


            return file.getAbsolutePath();
        }
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
