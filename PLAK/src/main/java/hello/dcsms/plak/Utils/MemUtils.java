package hello.dcsms.plak.Utils;

import android.os.Handler;

import miui.util.HardwareInfo;

public class MemUtils {
	public interface MemUtilsListener {
		void getMem(String mem);
	}

	public static String getMEM() {
		long len = HardwareInfo.getFreeMemory() / 1024L;
		long l1 = len / 1024L;
		if (l1 < 1024L)
			return l1 + "MB";

		long l2 = l1 * 10L / 1024L;
		long l3 = l2 / 10L;
		if (l2 % 10L != 0L)
			return l3 + "." + l2 % 10L + "GB";
		return l3 + "GB";
	}

	private Handler mHandler;
	private MemUtilsListener listener;
	private int DELAY = 1000;
	int x = 0;

	public MemUtils() {
		mHandler = new Handler();
	}

	public void setListener(MemUtilsListener listener) {
		this.listener = listener;
	}

	private boolean isStarting = false;

	public boolean isAlreadyStart() {
		return isStarting;
	}

	public void start() {
		if (mHandler == null)
			mHandler = new Handler();
		else
			mHandler.removeCallbacks(r);
		mHandler.postDelayed(r, DELAY);
		isStarting=true;
	}

	public void stop() {
		if (mHandler != null)
			mHandler.removeCallbacks(r);
		x = 0;
		isStarting=false;
	}

	private Runnable r = new Runnable() {

		@Override
		public void run() {
			mHandler.postDelayed(r, DELAY);
			//Debugger.logcat("PLAK", getMEM() + " " + Integer.toString(x));
			if (listener != null)
				listener.getMem(getMEM());
			//x++;
		}
	};
}
