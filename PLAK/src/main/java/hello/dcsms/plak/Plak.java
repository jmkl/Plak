package hello.dcsms.plak;

import android.app.Application;
import android.content.Intent;

import hello.dcsms.plak.Utils.Debugger;

public class Plak extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable e) {
				handleUncaughtException(thread, e);
			}
		});
	}

	public void handleUncaughtException(Thread thread, Throwable e) {
		e.printStackTrace();
		System.exit(1);
	}
}