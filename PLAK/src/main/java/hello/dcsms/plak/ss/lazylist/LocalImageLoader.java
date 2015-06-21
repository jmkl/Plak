package hello.dcsms.plak.ss.lazylist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;
import android.widget.ImageView;

import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.ss.SSConfigurationParser;
import hello.dcsms.plak.ss.SSFrameData;

public class LocalImageLoader {
    
    MemoryCache memoryCache=new MemoryCache();
    FileCache fileCache;
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    
    public LocalImageLoader(Context context){
        fileCache=new FileCache(context);
        executorService=Executors.newFixedThreadPool(5);
    }
    
    final int stub_id= R.drawable.preview_holder;
    public void ReloadCurrent(String url, ImageView imageView)
    {

        File f=fileCache.getFile(url);
        if(f.exists()){
            f.delete();
        }

        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else
        {
            queuePhoto(url, imageView);
            imageView.setImageResource(stub_id);
        }
    }
    public void DisplayImage(String url, ImageView imageView)
    {
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else
        {
            queuePhoto(url, imageView);
            imageView.setImageResource(stub_id);
        }
    }
        
    private void queuePhoto(String url, ImageView imageView)
    {
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }


    
    private Bitmap getBitmap(String url)
    {
        File f=fileCache.getFile(url);

        //from SD cache
        Bitmap bn = decodeFile(f);
        if(bn!=null)
            return bn;


        try {
            Bitmap bitmap = null;
            String path = url;
            if (path == null)
                return null;
            SSFrameData ssdata = SSConfigurationParser.parse(path + "/");
            SSFrameData.frameData fd = ssdata.getSsframedata();
            String root = path + "/";
            Bitmap bg = BitmapFactory.decodeFile(root + fd.getBackground());
            Bitmap frame = BitmapFactory.decodeFile(root + fd.getFrame());
            Matrix m = new Matrix();
            Bitmap b = Bitmap.createBitmap(fd.getBg_width(), fd.getBg_height(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(b);
            canvas.drawBitmap(bg, 0, 0, null);
            m.postTranslate(fd.getFrame_x(),fd.getFrame_y());
            canvas.drawBitmap(frame, m, null);
            m.postTranslate(0-fd.getFrame_x(),0-fd.getFrame_y());
            //canvas.drawBitmap(ss, f.getFrame_x(), f.getFrame_y(), null);

            for (SSFrameData.Overlay o : fd.getOverlay()) {
                m.postTranslate(o.overlay_x, o.overlay_y);
                canvas.drawBitmap(BitmapFactory.decodeFile(root + o.overlay_img), m, null);
                m.postTranslate(0-o.overlay_x, 0-o.overlay_y);
            }
            OutputStream os = new FileOutputStream(f);
            b.compress(Bitmap.CompressFormat.JPEG, 90, os);
            os.close();
            bg.recycle();
            frame.recycle();
            b.recycle();
            System.gc();
            File fx=fileCache.getFile(url);
            bitmap = decodeFile(fx);
            if(bitmap!=null)
                return bitmap;
            else
                return null;
        } catch (Throwable ex){
           ex.printStackTrace();
           if(ex instanceof OutOfMemoryError)
               memoryCache.clear();
           return null;
        }
    }
    int REQUIRED_SIZE=300;
    public void setSize(int size){
        REQUIRED_SIZE=size;
    }
    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1=new FileInputStream(f);
            BitmapFactory.decodeStream(stream1,null,o);
            stream1.close();
            
            //Find the correct scale value. It should be the power of 2.
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            FileInputStream stream2=new FileInputStream(f);
            Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u; 
            imageView=i;
        }
    }
    
    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }
        
        @Override
        public void run() {
            try{
                if(imageViewReused(photoToLoad))
                    return;
                Bitmap bmp=getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bmp);
                if(imageViewReused(photoToLoad))
                    return;
                BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
                Activity a=(Activity)photoToLoad.imageView.getContext();
                a.runOnUiThread(bd);
            }catch(Throwable th){
                th.printStackTrace();
            }
        }
    }
    
    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        return tag == null || !tag.equals(photoToLoad.url);
    }
    
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(stub_id);
        }
    }
    public void freeMemory() {
        memoryCache.clear();


    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

}
