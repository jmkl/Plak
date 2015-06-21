package hello.dcsms.plak.service;

import android.animation.ObjectAnimator;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hello.dcsms.plak.R;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.fa;

import android.animation.AnimatorSet;

/**
 * Created by jmkl on 5/4/2015.
 */
public class TombolPintas extends Service {
    public enum ClickMode {
        CLICK,
        LONGCLICK,
        DOUBLECLICK
    }

    WindowManager mWindowManager;
    private NotificationManager mNotificationManager;
    LayoutInflater mLayoutInflater;
    private boolean startedForeground;
    ClickMode MODE = ClickMode.CLICK;
    boolean DragMe = false;
    Display d;
    Point p;
    private int MINFLING = 100;
    boolean DEBUG = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    WindowManager.LayoutParams initLayoutParams(int posx, int posy) {
        return new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                posx,
                posy,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
//        return new WindowManager.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_STATUS_BAR_PANEL,
//                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
//                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                        | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
//                PixelFormat.TRANSLUCENT);
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


        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        startForeground(666, mBuilder.build());
        InitView();
    }


    TextView txt,txt2,txt3,txt4;
    Vibrator vi;
    android.os.Handler mHandler;

    private void InitView() {
        txt = (TextView) mLayoutInflater.inflate(R.layout.tombolpintas_tombol, null);
        txt2 = (TextView) mLayoutInflater.inflate(R.layout.tombolpintas_tombol, null);
        txt3 = (TextView) mLayoutInflater.inflate(R.layout.tombolpintas_tombol, null);
        txt4 = (TextView) mLayoutInflater.inflate(R.layout.tombolpintas_tombol, null);
        txt.setText(fa._ambulance);
        txt2.setText(fa._facebook);
        txt3.setText(fa._twitter);
        txt4.setText(fa._calculator);

        txt.setOnTouchListener(onTouchMeh);

        mWindowManager.addView(txt2, initLayoutParams(0, 0));
        mWindowManager.addView(txt3, initLayoutParams(0, 0));
        mWindowManager.addView(txt4, initLayoutParams(0, 0));
        mWindowManager.addView(txt, initLayoutParams(0, 0));
        d = mWindowManager.getDefaultDisplay();

        p = new Point();
        d.getSize(p);
        mHandler = new android.os.Handler();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    RelativeLayout.LayoutParams moveme(View v, int left, int top) {
        RelativeLayout.LayoutParams par = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        par.setMargins((v.getWidth() / 2) + left, (v.getHeight() / 2) + top, 0, 0);
        return par;
    }

    int curX = 0;
    int curY = 0;
    int nowX = 0;
    int nowY = 0;

    private Runnable rnbl = new Runnable() {
        @Override
        public void run() {
            MODE = ClickMode.LONGCLICK;
            DragMe = true;
            vi.vibrate(100);
        }
    };

    public View.OnTouchListener onTouchMeh = new View.OnTouchListener() {
        @Override
        public boolean onTouch(final View view, MotionEvent e) {


            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    curX = (int) e.getRawX() - p.x / 2;
                    curY = (int) e.getRawY() - p.y / 2;

                    MODE = ClickMode.CLICK;
                    mHandler.postDelayed(rnbl, 10);
                    break;
                case MotionEvent.ACTION_UP:
                    if (MODE == ClickMode.CLICK) {


                    } else if (MODE == ClickMode.LONGCLICK) {
                        mHandler.removeCallbacks(rnbl);
                        DragMe = false;
                        mWindowManager.updateViewLayout(view, initLayoutParams(curX, curY));
                        AnimateTo(curX);

                    }


                    break;
                case MotionEvent.ACTION_MOVE:
                    if (MODE == ClickMode.LONGCLICK) {
                        if (DragMe) {
                            MODE = ClickMode.LONGCLICK;
                            curX = (int) e.getRawX() - p.x / 2;
                            curY = (int) e.getRawY() - p.y / 2;
                            updateMainChild(view);

                        }
                    } else if (MODE == ClickMode.CLICK) {
                        int tempX = (int) e.getRawX() - p.x / 2;
                        Debugger.me("FLing", tempX - curX);

                    }
                    break;
            }
            return false;
        }
    };

    int curX(int nilai, int faktor){
        int curX = nilai;
        int temp = curX-txt.getWidth()*faktor;
        if(temp<(txt.getWidth() / 2) - (p.x / 2))
            return (txt.getWidth() / 2) - (p.x / 2);
        else return temp;
    }
    int curY(int nilai, int faktor){
        int curY = nilai;
        int temp = curY-txt.getHeight()*faktor;
        if(temp<(txt.getHeight() / 2) - (p.y / 2))
            return (txt.getHeight() / 2) - (p.y / 2);
        else return temp;
    }


    void updateMainChild(View view){
        mWindowManager.updateViewLayout(view, initLayoutParams(curX, curY));
        mWindowManager.updateViewLayout(txt2, initLayoutParams(curX(curX, 1), curY(curY, 1)));
        mWindowManager.updateViewLayout(txt3, initLayoutParams(curX(curX, 2), curY(curY, 2)));
        mWindowManager.updateViewLayout(txt4, initLayoutParams(curX(curX, 3), curY(curY, 3)));
    }

    void AnimateView() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(
                        txt, View.SCALE_X,
                        1f, 0.8f, 0.6f, 0.2f, 0.8f, 1f),
                ObjectAnimator.ofFloat(
                        txt, View.SCALE_Y,
                        1f, 0.8f, 0.6f, 0.2f, 0.8f, 1f)
        );
        set.start();
    }


    Runnable run;
    Handler h;
    int tipe = 0;

    private synchronized void AnimateTo(final int x) {
        curX = x;
        if (curX < p.x/2)
            tipe = 1;
        else
            tipe=0;
        h = new Handler();

        run = new Runnable() {
            @Override
            public void run() {


                boolean min = curX > (txt.getWidth() / 2) - (p.x / 2);
                boolean max = curX < (p.x / 2);
                if (tipe == 0) {
                    if (max) {
                        curX += 20;
                        h.postDelayed(run, 1);
                    } else {
                        h.removeCallbacks(run);
                        AnimateView();
                    }
                } else {
                    if (min) {
                        curX -= 20;
                        h.postDelayed(run, 1);
                    } else {
                        h.removeCallbacks(run);
                        AnimateView();
                    }
                }
                updateMainChild(txt);
                //mWindowManager.updateViewLayout(txt, initLayoutParams(curX, curY));

            }
        };
        h.postDelayed(run, 1);
    }


}
