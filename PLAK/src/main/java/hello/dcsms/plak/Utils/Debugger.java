package hello.dcsms.plak.Utils;

import android.util.Log;

public class Debugger {
    private static boolean DEBUG = false;
    private static boolean DEBUGAPP = false;
    private boolean ENABLE = true;

    public Debugger(boolean ENABLE) {
        this.ENABLE = ENABLE;
    }

    public Debugger() {

    }

    public static void SETDEBUGMODE(boolean bool) {
        DEBUG = bool;
        DEBUGAPP = bool;
    }


    public static void logcat(String tag, String msg) {
        if (DEBUG)
            Log.e("PLAK", tag + " : " + msg);
    }

    public static void logcat(String tag, int msg) {
        if (DEBUG)
            Log.e("PLAK", tag + " : " + Integer.toString(msg));
    }

    public static void logcat(String tag, float msg) {
        if (DEBUG)
            Log.e("PLAK", tag + " : " + Float.toString(msg));
    }

    public static void logcat(String tag, boolean msg) {
        if (DEBUG)
            Log.e("PLAK", tag + " : " + Boolean.toString(msg));
    }


    public static void app(String tag, String msg) {
        if (DEBUGAPP)
            Log.e("PLAK", tag + " : " + msg);
    }

    public static void app(String tag, int msg) {
        if (DEBUGAPP)
            Log.e("PLAK", tag + " : " + Integer.toString(msg));
    }

    public static void app(String tag, float msg) {
        if (DEBUGAPP)
            Log.e("PLAK", tag + " : " + Float.toString(msg));
    }

    public static void app(String tag, boolean msg) {
        if (DEBUGAPP)
            Log.e("PLAK", tag + " : " + Boolean.toString(msg));
    }

    public static void app(String msg) {
        if (DEBUGAPP)
            Log.e("PLAK", msg);
    }

    public static void app(int msg) {
        if (DEBUGAPP)
            Log.e("PLAK", Integer.toString(msg));
    }

    public static void app(float msg) {
        if (DEBUGAPP)
            Log.e("PLAK", Float.toString(msg));
    }

    public static void app(boolean msg) {
        if (DEBUGAPP)
            Log.e("PLAK", Boolean.toString(msg));
    }

    public static void me(Object msg) {

        String m = "ERROR can't cast object object";
        if (msg instanceof Float)
            m = Float.toString((Float) msg);
        if (msg instanceof Integer)
            m = Integer.toString((Integer) msg);
        else if (msg instanceof Boolean)
            m = Boolean.toString((Boolean) msg);
        else if (msg instanceof Long)
            m = Long.toString((Long) msg);
        else if (msg instanceof String)
            m = (String) msg;

        Log.e("PLAK", m);
    }

    public static void me(String tag, Object msg) {
        String m = "ERROR can't cast objectobject";
        if (msg instanceof Float)
            m = Float.toString((Float) msg);
        if (msg instanceof Integer)
            m = Integer.toString((Integer) msg);
        else if (msg instanceof Boolean)
            m = Boolean.toString((Boolean) msg);
        else if (msg instanceof Long)
            m = Long.toString((Long) msg);
        else if (msg instanceof String)
            m = (String) msg;

        Log.e("PLAK", tag + " : " + m);
    }

    //NONSTATIC
    public void log(Object msg) {
        if (!ENABLE)
            return;

        String m = "ERROR can't cast object object";
        if (msg instanceof Float)
            m = Float.toString((Float) msg);
        if (msg instanceof Integer)
            m = Integer.toString((Integer) msg);
        else if (msg instanceof Boolean)
            m = Boolean.toString((Boolean) msg);
        else if (msg instanceof Long)
            m = Long.toString((Long) msg);
        else if (msg instanceof String)
            m = (String) msg;

        Log.e("PLAK", m);
    }

    public void log(String tag, Object msg) {
        if (!ENABLE)
            return;

        String m = "ERROR can't cast objectobject";
        if (msg instanceof Float)
            m = Float.toString((Float) msg);
        if (msg instanceof Integer)
            m = Integer.toString((Integer) msg);
        else if (msg instanceof Boolean)
            m = Boolean.toString((Boolean) msg);
        else if (msg instanceof Long)
            m = Long.toString((Long) msg);
        else if (msg instanceof String)
            m = (String) msg;

        Log.e("PLAK", tag + " : " + m);
    }
}

