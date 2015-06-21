package hello.dcsms.plak.Test;

import android.app.Activity;
import android.content.Context;
import android.os.*;
import android.util.Log;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.Command;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Process;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import hello.dcsms.plak.C;
import hello.dcsms.plak.Utils.Debugger;
import hello.dcsms.plak.Utils.PrefUtils;

public class Test extends Activity {
    boolean isRunning = false;
    CaptureThread thread;
    Debugger d;
    private boolean running = false;

    public Test() {
        d = new Debugger();
    }

    public void TestSetStringPreferences(Context c){
        PrefUtils pref = new PrefUtils(c);
        Set<String> preftes = pref.getPref().getStringSet(C.PREF_XPANDED_ICON_VIEW,null);
        if(preftes!=null){
            for (String s : preftes){
                d.log(s);
            }
        }
    }


    public void start() {
        running = true;
        List<String> cmd = new ArrayList<>();
        cmd.add("su");
        cmd.add("-C");
        cmd.add("system/bin/screenrecord");
        cmd.add("/sdcard/zzz_tes.mp4");
        cmd.add("--verbose");
        thread = new CaptureThread(cmd);
        thread.start();
    }
    public void stop(){
        thread.interrupt();
    }

    public boolean isRunning() {
        return running;
    }


    private class CaptureThread extends Thread {
        List<String> command;
        public CaptureThread(List<String> command){
            this.command = command;
        }
        public void run() {
            try {
                final Class<?> classProcImpl = Class.forName("java.lang.ProcessManager$ProcessImpl");
                final Field fieldPid = classProcImpl.getDeclaredField("pid");
                fieldPid.setAccessible(true);
                ProcessBuilder pb = new ProcessBuilder();
                pb.command(command);
                pb.redirectErrorStream(true);
                Process proc = pb.start();
                final int pid = fieldPid.getInt(proc);

                while (!isInterrupted()) {
                    try {
                        int code = proc.exitValue();
                        d.log(code);
                    } catch (IllegalThreadStateException ignore) {

                    }
                }

                android.os.Process.sendSignal(pid, 2);
            } catch (IOException e) {


            } catch (NoSuchFieldException e) {

            } catch (IllegalArgumentException e) {

            } catch (IllegalAccessException e) {

            } catch (ClassNotFoundException e) {
            }
        }
    }



}

