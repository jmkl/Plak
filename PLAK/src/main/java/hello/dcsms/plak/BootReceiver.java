package hello.dcsms.plak;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import hello.dcsms.plak.db.DBDataSource;
import hello.dcsms.plak.widget.Notif;

//Once you enable the receiver this way, it will stay enabled, even if the user reboots the device. In other words, programmatically enabling the receiver overrides the manifest setting, even across reboots. The receiver will stay enabled until your app disables it. You can disable a receiver (for example, if the user cancels an alarm) as follows:

//ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
//PackageManager pm = context.getPackageManager();
//
//pm.setComponentEnabledSetting(receiver,
//        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//        PackageManager.DONT_KILL_APP);

public class BootReceiver extends BroadcastReceiver {
	DBDataSource db;
	String tes = "";


	public BootReceiver(String tes) {
	}

	public BootReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent it) {
		if (it.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			BuildSomeNotificationAndShit(context);
			String text ="Hello text";
			text = "Hello";
			db = new DBDataSource(context);
			db._Open();

//			DBdata dbaca = db.getAllData();
//			List<String> kiri = DBdata.str2List(dbaca.getKiri());
//			List<String> kanan = DBdata.str2List(dbaca.getKanan());
//			List<String> tengah = DBdata.str2List(dbaca.getTengah());
//
//			if (kiri.size() > 0 || kanan.size() > 0 || tengah.size() > 0) {
//				Intent intent = new Intent();
//				intent.setAction(C.MODSTATUSBAR);
//				intent.putStringArrayListExtra(C.SBL_KIRI, getListLD(kiri));
//				intent.putStringArrayListExtra(C.SBL_KANAN, getListLD(kanan));
//				intent.putStringArrayListExtra(C.SBL_TENGAH, getListLD(tengah));
//				context.sendBroadcast(intent);
//
//			}

			db._CLose();
		}

	}

	private void BuildSomeNotificationAndShit(Context context) {
		new Notif(context, "Hi, kamu abis reboot ya? kenapa?", 666);
	}

	private ArrayList<String> getListLD(List<String> data) {
		ArrayList<String> d = new ArrayList<String>();
		for (String str : data) {
			d.add(str);
		}
		return d;
	}
}
