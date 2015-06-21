package hello.dcsms.plak;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.Command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

import hello.dcsms.plak.Utils.Debugger;

public class ErrorLog extends Activity {
	String extra = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log);
		Intent i = getIntent();
		extra = i.getStringExtra("command");
		File f = new File(extractLogToFile());
		if (f.exists()) {
			Toast.makeText(getApplicationContext(),
					"Log file are saved in : " + f.getAbsolutePath(),
					Toast.LENGTH_LONG).show();
			BufferedReader reader;
			StringBuilder sb = null;
			try {
				reader = new BufferedReader(new FileReader(f.getAbsolutePath()));
				sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				reader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (sb != null) {
				((EditText) findViewById(R.id.logtext)).setText(sb.toString());
				findViewById(R.id.senderror)
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View paramView) {
								sendLogFile();

							}
						});
			}
		}
	}

	private String extractLogToFile() {
		PackageManager manager = this.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(this.getPackageName(), 0);
		} catch (NameNotFoundException e2) {
		}
		String model = Build.MODEL;
		if (!model.startsWith(Build.MANUFACTURER))
			model = Build.MANUFACTURER + " " + model;

		// Make file name - file must be saved to external storage or it wont be
		// readable by
		// the email app.
		String path = Environment.getExternalStorageDirectory() + "/" + "Plak/";
		File folder = new File(path);
		if (!folder.exists())
			folder.mkdirs();
		String fullName = path + System.currentTimeMillis() + "-log.txt";

		// Extract to file.
		File file = new File(fullName);
		InputStreamReader reader = null;
		FileWriter writer = null;
		try {
			// For Android 4.0 and earlier, you will get all app's log output,
			// so filter it to
			// mostly limit it to your app's output. In later versions, the
			// filtering isn't needed.
			// String cmd = (Build.VERSION.SDK_INT <=
			// Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) ?
			// "logcat -d -v time XPosed:v dalvikvm:v System.err:v *:s"
			// : "logcat -d -v time";
			String cmd;
			if (extra != null) {
				cmd = extra;
				Command command = new Command(0,extra+" > "+fullName) {
					
					@Override
					public void output(int arg0, String arg1) {						
					}
				};
				RootTools.getShell(true).add(command).waitForFinish();
			} else {
				cmd = "logcat -d -v time PLAK:E dalvikvm:v System.err:v *:s";

				// get input stream
				Process process = Runtime.getRuntime().exec(cmd);
				reader = new InputStreamReader(process.getInputStream());

				// write output stream
				writer = new FileWriter(file);

				writer.write("Android code: " + Build.VERSION.INCREMENTAL
						+ "\n");
				writer.write("Android version: " + Build.VERSION.SDK_INT + "\n");
				writer.write("Device: " + model + "\n");
				writer.write("App version: "
						+ (info == null ? "(null)" : info.versionCode) + "\n");

				char[] buffer = new char[10000];
				do {
					int n = reader.read(buffer, 0, buffer.length);
					if (n == -1)
						break;
					writer.write(buffer, 0, n);
				} while (true);

				reader.close();
				writer.close();

			}
		} catch (IOException e) {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e1) {

				}
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e1) {

				}

			// You might want to write a failure message to the log here.
			return null;
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RootDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fullName;
	}

	private void sendLogFile() {
		String fullName = extractLogToFile();
		if (fullName == null)
			return;

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("plain/text");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "dcsms@live.com" });
		intent.putExtra(Intent.EXTRA_SUBJECT, "[PLAK] Log File");
		intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + fullName));
		intent.putExtra(Intent.EXTRA_TEXT, "Log file attached."); // do this so
		// body.
		startActivity(intent);
	}
}
