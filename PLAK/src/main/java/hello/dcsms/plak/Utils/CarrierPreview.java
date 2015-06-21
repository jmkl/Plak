package hello.dcsms.plak.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by jmkl on 5/16/2015.
 */
public class CarrierPreview {
    public static Bitmap makepreview(int wi, int hi, Bitmap bmpcarrier, int pos){

        Paint p = new Paint();

        p.setColor(Color.MAGENTA);
        p.setStrokeWidth(1);
        Matrix m = new Matrix();
        Bitmap result = Bitmap.createBitmap(wi,hi, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(result);
        if(pos<2)
            c.drawColor(Color.parseColor("#ffdddddd"));
        else
            c.drawColor(Color.BLACK);

        int cw= bmpcarrier.getWidth();
        int ch= bmpcarrier.getHeight();
        int x1=(wi/2-cw/2);
        int x2 =(wi/2+cw/2);
        int y1 =(hi/2-ch/2);
        int y2 =(hi/2+ch/2);
        m.postTranslate(x1, y1);
        c.drawBitmap(bmpcarrier,m,p);
        m.postTranslate(-x1, -y1);

        //tidur1
        c.drawLine(x1-10, y1, x2+10, y1, p);
        //tidur2
        c.drawLine(x1-10,y2,x2+10,y2,p);
        //tegak1
        c.drawLine(x1,y1-10,x1,y2+10,p);
        //tegak2
        c.drawLine(x2,y1-10,x2,y2+10,p);

        return result;

    }
}
