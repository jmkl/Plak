package hello.dcsms.plak.data;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import hello.dcsms.plak.C;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.ss.LoadSSTemplateTask;
import hello.dcsms.plak.ss.SSFrameData;
import hello.dcsms.plak.task.iface.OnTaskCaput;
import hello.dcsms.plak.widget.MToast;

/**
 * Created by jmkl on 5/6/2015.
 */
public class PlakZip {
    private OnTaskCaput lisener;
    private static final int BUFFER_SIZE = 1024;
    private String TAG_CEKZIP="CekZipFile";
    public static String CONFIG_FILE = "prop.plk";
    Context c;

    public void CekZipFile(Context c,String file,OnTaskCaput lisener){
        this.lisener = lisener;
        this.c=c;
        Debugger debug = new Debugger();
        File f=null;
        try {
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE));
            ZipEntry ze = null;
            while ((ze = zis.getNextEntry())!=null){
                    debug.log(TAG_CEKZIP, ze.getName());
                if(ze.getName().equals(CONFIG_FILE)){
                    InputStream fis = new ZipFile(file).getInputStream(ze);
                    BufferedReader reader = new BufferedReader(new InputStreamReader( fis, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    SSFrameData data = LoadSSTemplateTask.configParser(sb.toString());
                    String namaframe = data.getSsframe();
                    f = new File(C.PLAK_SS_MOCKUP_ROOT+namaframe);
                    int i=0;
                    while (f.exists()){
                        f = new File(C.PLAK_SS_MOCKUP_ROOT+namaframe+"_"+Integer.toString(i));
                        i++;
                        debug.log(f.getAbsolutePath());
                    }
                    if(!f.exists())
                        f.mkdirs();
                    break;
                }
            }
            zis.close();
            new UnzipTemplateTask(file,f.getAbsolutePath()).execute();
        } catch (FileNotFoundException e) {
            debug.log(TAG_CEKZIP, e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            debug.log(TAG_CEKZIP, e.getMessage());
            e.printStackTrace();
        }catch (NullPointerException e){
            debug.log(TAG_CEKZIP, e.getMessage());
            e.printStackTrace();
        }
    }

    public class UnzipTemplateTask extends AsyncTask<Void,Void,String>{

        Debugger debug;
        String path,file;
        public UnzipTemplateTask(String file, String absolutePath) {
            this.file=file;
            debug = new Debugger();
            this.path = absolutePath;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            MToast.show(c,"Unzip Complete");
            if(lisener!=null)
                lisener.onFinish(aVoid);

        }

        @Override
        protected String doInBackground(Void... voids) {
            int size;
            byte[] buffer = new byte[BUFFER_SIZE];
            try {
                ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE));
                ZipEntry ze = null;
                while ((ze = zis.getNextEntry())!=null){
                    debug.log(ze.getName());
                    File unzipFile = new File(path+"/"+ze.getName());
                    FileOutputStream out = new FileOutputStream( unzipFile, false);
                    BufferedOutputStream fout = new BufferedOutputStream(out, BUFFER_SIZE);
                    try {
                        while ((size = zis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                            fout.write(buffer, 0, size);
                        }
                        zis.closeEntry();
                    } finally {
                        fout.flush();
                        fout.close();
                    }

                }
                zis.close();
            } catch (FileNotFoundException e) {
                debug.log(TAG_CEKZIP, e.getMessage());
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                debug.log(TAG_CEKZIP, e.getMessage());
                e.printStackTrace();
                return null;
            }catch (NullPointerException e){
                debug.log(TAG_CEKZIP, e.getMessage());
                e.printStackTrace();
                return null;
            }
            return path;
        }
    }
}
