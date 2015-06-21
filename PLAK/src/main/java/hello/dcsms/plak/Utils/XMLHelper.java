package hello.dcsms.plak.Utils;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import hello.dcsms.plak.manual.ManualItemData;

public class XMLHelper {
	public interface OnWriteXMLListener {
		void CompleteWriteShit();
	}

	public static class writeXML extends AsyncTask<Void, Void, Void> {
		private List<ManualItemData> data;
		File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/PLAK/");
		String tempfile = "/temp.xml";
		private String MAINTAG = "PlakCustom";
		private String SYSTEMUI = "com.android.systemui";
		private String MIHOME = "com.miui.mihome";
		private OnWriteXMLListener listener;

		public writeXML(List<ManualItemData> data) {
			this.data = data;
		}

		public void setOnWriteListener(OnWriteXMLListener listener) {
			this.listener = listener;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			listener.CompleteWriteShit();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			List<ManualItemData> sui = new ArrayList<ManualItemData>();
			List<ManualItemData> mihome = new ArrayList<ManualItemData>();
			if (!f.exists())
				f.mkdirs();
			for (int i = 0; i < data.size(); i++) {
				if (data.get(i).getNamaPaket().equals("com.android.systemui"))
					sui.add(data.get(i));
				else if (data.get(i).getNamaPaket().equals("com.miui.mihome"))
					mihome.add(data.get(i));
				
				Log.e("PLAK "+Integer.toString(i), data.get(i).toString());
			}
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(new File(f.getAbsolutePath()
						+ tempfile));

				// FileOutputStream fileos= c.openFileOutput(xmlFile,
				// Context.MODE_PRIVATE);
				XmlSerializer xms = Xml.newSerializer();
				StringWriter writer = new StringWriter();
				xms.setOutput(writer);
				xms.startDocument("UTF-8", true);

				xms.startTag(null, MAINTAG);

				xms.startTag(null, SYSTEMUI);
				for (ManualItemData md : sui) {
					xms.startTag(null, md.getNamaField());
				
					xms.startTag(null, md.getTipe());
					xms.text(md.getNilai());
					xms.endTag(null, md.getTipe());
					xms.endTag(null, md.getNamaField());
				}
				xms.endTag(null, SYSTEMUI);

				xms.startTag(null, MIHOME);
				for (ManualItemData md : mihome) {
					xms.startTag(null, md.getNamaField());
					xms.startTag(null, md.getTipe());
					xms.text(md.getNilai());
					xms.endTag(null, md.getTipe());
					xms.endTag(null, md.getNamaField());
				}
				xms.endTag(null, MIHOME);

				xms.endTag(null, MAINTAG);

				xms.endDocument();
				xms.flush();
				String dataWrite = writer.toString();
				fos.write(dataWrite.getBytes());
				fos.close();

			} catch (FileNotFoundException e) {
				Log.e("PLAK",e.getMessage());
			} catch (IllegalArgumentException e) {
				Log.e("PLAK",e.getMessage());
			} catch (IllegalStateException e) {
				Log.e("PLAK",e.getMessage());
			} catch (IOException e) {
				Log.e("PLAK",e.getMessage());
			}
			return null;
		}
	}

}
