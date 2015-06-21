package hello.dcsms.plak.online;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import hello.dcsms.plak.R;
import hello.dcsms.plak.widget.NotifyDialog;

/**
 * Created by jmkl on 5/19/2015.
 */
public class OnlineActivity extends Activity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_main);
        new ApplicationUpdateCek(appsUpdateLisneter).execute();
    }
    private ApplicationUpdateCek.onUpdateListener appsUpdateLisneter = new ApplicationUpdateCek.onUpdateListener() {
        @Override
        public void onUpdateAvailable(int versi, final String link) {
            int curversion = getResources().getInteger(R.integer.kodeversi);
            if(curversion<versi){
               AlertDialog.Builder dialog = new AlertDialog.Builder(OnlineActivity.this);
                dialog.setCancelable(true);
                dialog.setTitle("Update Available");
                dialog.setMessage("the new version of PLAK is available. Update Now?");
                dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new DownloadNewVersionTask(getApplicationContext(),link).execute();
                    }
                });
                dialog.show();
            }
        }
    };
}
