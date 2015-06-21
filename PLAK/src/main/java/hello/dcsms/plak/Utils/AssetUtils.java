package hello.dcsms.plak.Utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jmkl on 4/27/2015.
 */
public class AssetUtils {
    private static String PlakFilesRoot = Environment.getDataDirectory()
            + "/data/hello.dcsms.plak/config/";
    //copywithspecification
    public static boolean copy(Context c,String whatasset,String to){
        AssetManager assetManager = c.getAssets();
        String[] files = null;
        try {
            files = assetManager.list(whatasset);
        } catch (IOException e) {
            return false;
        }
        for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            File ss_temp = new File(to);
            if(!ss_temp.exists())
                ss_temp.mkdirs();
            try {

                in = assetManager.open(whatasset+"/" + filename);
                out = new FileOutputStream(to + filename);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    public static boolean copy(Context c, String to) {


        AssetManager assetManager = c.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("files");
        } catch (IOException e) {
          return false;
        }
        for (String filename : files) {

            InputStream in = null;
            OutputStream out = null;
            File redmi = null;
            try {

                in = assetManager.open("files/" + filename);
                if(filename.contains("redmi")) {
                 redmi = new File(PlakFilesRoot+filename);
                    out = new FileOutputStream(redmi.getAbsolutePath());
                }else
                    out = new FileOutputStream(to + filename);
                copyFile(in, out);
                if(redmi!=null){
                    redmi.setWritable(true,false);
                    redmi.setReadable(true,false);
                    redmi.setExecutable(true,false);
                }
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);

        }
    }

}

