package hello.dcsms.plak.Utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import hello.dcsms.plak.R;


public class SSService extends Service {

    private NotificationManager mNM;

    private ImageView ss;
    private WindowManager wman;
    private WindowManager.LayoutParams params;
    private int posx, posy, d_width, d_height;
    boolean dragme = false;
    private int Mode;
    int iSize = 300;
    Handler hdl;
    long lastShake = 0;
    private int MODE_LONGCLICK = 1, MODE_CLICK = 2;
    Runnable rnbl;
    int nowX, nowY;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void setImageViewParams() {
        params = new WindowManager.LayoutParams(iSize, iSize, posx, posy,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

    }



    public void HideMe(boolean hide) {
        ss.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Hi")
                .setContentText("wtf")
                .setContentInfo("meik");
        NotificationManager mNotifyMgr = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mNM = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mNM.notify(666, mBuilder.build());
        startForeground(666, mBuilder.build());
        MakeView();
    }


    void startAnim(){
        AnimatorSet ani = new AnimatorSet();
        ani.playTogether(ObjectAnimator.ofFloat(ss,View.TRANSLATION_Y,new float[]{100f,50f,120f,105f,90f,1f}));
        ani.start();
    }

    private void MakeView() {
        wman = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wman.getDefaultDisplay().getMetrics(dm);
        d_width = dm.widthPixels;// intent.getIntExtra("lebar", 240);
        d_height = dm.heightPixels;// intent.getIntExtra("tinggi", 320);
        posx = d_width / 2 - d_width;
        posy = d_height / 2 - d_height;
        hdl = new Handler();




        ss = new ImageView(this);
        ss.setImageResource(R.drawable.logo);
        setImageViewParams();
        ss.setOnTouchListener(onTouch);
        wman.addView(ss, params);
        rnbl = new Runnable() {
            @Override
            public void run() {
                startAnim();
                Mode = MODE_LONGCLICK;
                dragme = true;
                Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vi.vibrate(100);

            }
        };

    }



    private View.OnTouchListener onTouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent e) {


            int action = e.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    Mode = MODE_CLICK;
                    hdl.postDelayed(rnbl, 500);

                    break;

                case MotionEvent.ACTION_MOVE:
                    if (Mode == MODE_LONGCLICK) {
                        if (dragme) {
                            Mode = MODE_LONGCLICK;
                            posx = (int) e.getRawX() - d_width / 2;
                            posy = (int) e.getRawY() - d_height / 2;
                            setImageViewParams();
                            wman.updateViewLayout(ss, params);
                            nowX = (int) e.getRawX();
                            nowY = (int) e.getRawY();
                        }
                    }
                    break;

                case MotionEvent.ACTION_UP:

                    if (Mode == MODE_CLICK) {
//                        HideMe(true);
//                        setImageViewParams();
//                        wman.updateViewLayout(ss, params);
                        hdl.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                meClick();

                            }
                        }, 500);
//                        hdl.removeCallbacks(rnbl);
//                        dragme = false;

                    } else if (Mode == MODE_LONGCLICK) {
                        hdl.removeCallbacks(rnbl);

                        StopMeHarder();
                        dragme = false;
                        HideMe(false);
                        setImageViewParams();
                        wman.updateViewLayout(ss, params);

                    }

                    break;
            }
            return true;

        }
    };

    public void meClick() {
        Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vi.vibrate(100);


    }

    protected void StopMeHarder() {
        if (nowX > d_width - ss.getWidth() * 2
                && nowY > d_height - ss.getHeight() * 2) {

            //stopForeground(true);
            //stopSelf();

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (wman != null) {
            if (ss != null)
                wman.removeView(ss);
        }



    }



}
