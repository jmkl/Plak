package hello.dcsms.plak.widget;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.MiuiProcess;

import java.io.File;

import hello.dcsms.plak.R;
import miui.content.res.ThemeNativeUtils;
import miui.maml.NotifierManager;

public class Notif {
	NotificationManager notificationManager;
	int mNotificationId;
	Notification.Builder notif;
	PendingIntent pIntent;
	Context mContext;
	public Notif(Context context, String msg, int mNotificationId,String url){

		mContext = context;
		notif = new Notification.Builder(context)
				.setAutoCancel(false)
				.setContentTitle("Hi")
				.setContentText(msg)
				.setSmallIcon(android.R.drawable.ic_menu_gallery);
		this.mNotificationId = mNotificationId;
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(mNotificationId, notif.build());


	}
	public void updateNotif(String msg){
		notif.setContentText(msg)
		.setAutoCancel(false);
		notificationManager.notify(mNotificationId,notif.build());
	}
	public void updateNotifAndFinish(String msg,String url){

		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		File file = new File(url);
		intent.setDataAndType(Uri.fromFile(file), "image/*");
		pIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
		notif.setContentText(msg)
				.setContentIntent(pIntent)
				.setAutoCancel(true);
		notificationManager.notify(mNotificationId,notif.build());
	}
	public void updateNotifAndFinishonError(String msg){

		notif.setContentText(msg)
				.setAutoCancel(true);
		notificationManager.notify(mNotificationId,notif.build());
	}

	public Notif(Context context, String msg, int mNotificationId) {
		Notification.Builder mBuilder = new Notification.Builder(
				context)
				.setSmallIcon(R.drawable.logo)
				.setContentTitle("Hi")
				.setContentText(msg)
				.setAutoCancel(true)
				.setOnlyAlertOnce(true)
				.setContentInfo(msg);
		NotificationManager mNotifyMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		
		mNotifyMgr.notify(mNotificationId, mBuilder.build());
	

	}
}
