package hello.dcsms.plak.task;

import android.os.AsyncTask;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.Command;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import hello.dcsms.plak.task.iface.OnShellTaskListener;

/**
 * Created by jmkl on 4/26/2015.
 */
public class ShellTask extends AsyncTask<Void,String,String> {
    private String cmd;
    private OnShellTaskListener listener;

    public void setListener(OnShellTaskListener listener){
        this.listener=listener;
    }
    public ShellTask(String cmd) {
        this.cmd = cmd;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        String result = values[0];
        listener.onUpdate(result);

    }

    @Override
    protected String doInBackground(Void... voids) {

        Command cmds = new Command(0,cmd) {
            @Override
            public void output(int i, String s) {
                String[] resplit = s.split(" ");
                if(resplit.length>0){
                    publishProgress(resplit[0]);
                }


            }
        };
        try {
            RootTools.getShell(true).add(cmds).waitForFinish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (RootDeniedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
