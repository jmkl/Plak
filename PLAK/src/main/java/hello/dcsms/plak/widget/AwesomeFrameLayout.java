package hello.dcsms.plak.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.FrameLayout;

import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;

/**
 * Created by jmkl on 5/11/2015.
 */
public class AwesomeFrameLayout extends FrameLayout implements SensorEventListener {
    private Display mDisplay;
    private float mSensorX;
    private float mSensorY;
    private Sensor mAccelerometer;
    private SensorManager mSensorManager;
    Paint paint = new Paint();
    Point point;

    public AwesomeFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(context.getResources().getColor(R.color.bulat));
        mDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        point = new Point();
        mDisplay.getSize(point);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    int[] ball = new int[]{100, 200,400,600,800,1000};
    float[] factor = new float[]{0.5f,0.6f,0.7f,0.8f,0.9f,1f};
    float friction =10f;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x = Math.round(((mSensorX ) * friction));
        float y = Math.round(((mSensorY ) * friction));
        int xx = factor.length-1;
        for (int i = 0; i < ball.length; i++) {
            canvas.drawCircle(x*factor[i],y*factor[xx-i],ball[xx-i],paint);
        }

        invalidate();

    }

    Debugger d = new Debugger();

    @Override
    public void onSensorChanged(SensorEvent event) {


        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;
        switch (mDisplay.getRotation()) {
            case Surface.ROTATION_0:
                mSensorX = event.values[0];
                mSensorY = event.values[1];
                break;
            case Surface.ROTATION_90:
                mSensorX = -event.values[1];
                mSensorY = event.values[0];
                break;
            case Surface.ROTATION_180:
                mSensorX = -event.values[0];
                mSensorY = -event.values[1];
                break;
            case Surface.ROTATION_270:
                mSensorX = event.values[1];
                mSensorY = -event.values[0];
                break;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {


    }
}
