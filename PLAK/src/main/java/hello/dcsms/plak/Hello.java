package hello.dcsms.plak;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import hello.dcsms.plak.Frgmnt.CarrierFragment;
import hello.dcsms.plak.Frgmnt.PrefFragment;
import hello.dcsms.plak.Utils.Debugger;

public class Hello extends Tes {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initActionBar();
        new Handler().postDelayed(new Runner(), DELAYED);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            CarrierFragment fragment = (CarrierFragment) getFragmentManager().findFragmentByTag("CarrierTag");
            if (fragment != null)
                fragment.sendIntentData(requestCode, data);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isfromSim = false;
    }

    @Override
    protected void onPause() {
        fixPref();
        super.onPause();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    static boolean isfromSim = false;

    public static void updateSIM() {
        isfromSim = true;
    }

    @Override
    protected void onDestroy() {
        fixPref();

        super.onDestroy();

    }


    long DELAYED = 400;

    private class Runner implements Runnable {
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }

        @Override
        public void run() {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new PrefFragment()).commit();
        }


    }

}
