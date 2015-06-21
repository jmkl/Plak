package hello.dcsms.plak.manual;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hello.dcsms.plak.C;
import hello.dcsms.plak.Utils.Debugger;

public class ManualDataJsonHelper {
	private static String ROOT_PATH = Environment.getExternalStorageDirectory()
			+ "/plak/manual_settings/";
	private static String FileName = "_man.plakconf";

	public static List<ManualItemData> ReadJson(String url) {
		List<ManualItemData> listmanualitemdata = new ArrayList<ManualItemData>();

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(url));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			reader.close();
			String json = sb.toString();



			Gson gson = new Gson();
			AllListManResItems all = gson.fromJson(json,
					AllListManResItems.class);
			List<ListManResItem> lmri = all.getData();
			for (ListManResItem l : lmri) {
				if (l.getData().size() > 0) {
					for (ManResItem la : l.getData()) {
						ManualItemData data = new ManualItemData();
						if(la.getNamapaket()!=null)
							data.setNamaPaket(la.getNamapaket());
						else
							data.setNamaPaket(l.getKey());
						data.setNamaField(la.getObjek());
						data.setItemType(la.getTipe());
						data.setItemValue((String) la.getNilai());
						data.setCatatan(la.getCatatan());
						data.setCek(0);
						listmanualitemdata.add(data);

					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listmanualitemdata;

	}

	public static String SaveJson(List<ManualItemData> data, String saveto) {
        File rootdir = null;
        SimpleDateFormat sdf = new SimpleDateFormat("d_MMMM_y_HH_mm_s_", Locale.getDefault());

		if (saveto == null) {
            saveto = ROOT_PATH + sdf.format(System.currentTimeMillis());
            if(!new File(ROOT_PATH).exists())
                new File(ROOT_PATH).mkdirs();
            rootdir=new File(saveto);
        }else{
            rootdir=new File(saveto);
            if (!rootdir.exists())
                rootdir.mkdirs();
            rootdir.setReadable(true, false);
            rootdir.setWritable(true, false);
            rootdir.setExecutable(true, false);
        }



		AllListManResItems all = new AllListManResItems();
		ListManResItem lmri_sysui = new ListManResItem();
		ListManResItem lmri_mihome = new ListManResItem();
		ListManResItem lmri_lain = new ListManResItem();
		for (ManualItemData _mid : data) {
			ManResItem mri = new ManResItem();
			String type = _mid.getTipe();

			if (_mid.getNamaPaket().equals(C.NAMAPAKET_SYSTEMUI)) {
				lmri_sysui.add(mri);
			} else if (_mid.getNamaPaket().equals(C.NAMAPAKET_MIHOME)) {
				lmri_mihome.add(mri);
			}else{
				lmri_lain.add(mri);
				mri.setNamapaket(_mid.getNamaPaket());			}

			mri.setCatatan(_mid.getCatatan());
			mri.setNilai(_mid.getNilai());
			mri.setTipe(type);
			mri.setObjek(_mid.getNamaField());

		}
		lmri_lain.setKey(C.NAMAPAKET_LAINNYA);
		lmri_sysui.setKey(C.NAMAPAKET_SYSTEMUI);
		lmri_mihome.setKey(C.NAMAPAKET_MIHOME);
		all.addData(lmri_sysui);
		all.addData(lmri_mihome);
		all.addData(lmri_lain);
		//Gson gson = new Gson();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
		
		String json_result = gson.toJson(all);

		try {
			File mFile = new File(saveto + "setting.plakconf");
			FileWriter fw = new FileWriter(mFile);
			fw.write(json_result);
			fw.close();
			mFile.setReadable(true, false);
			mFile.setWritable(true, false);
			mFile.setExecutable(true, false);
			return mFile.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
			return "Failed";
		}

	}

	public static void JsonParser() {

		ListManResItem lmri = new ListManResItem();
		lmri.setKey("com.android.systemui");
		lmri.add(new ManResItem().setTipe("bool").setNilai(true)
				.setCatatan("Catatan penting mengenai item ini"));
		lmri.add(new ManResItem().setTipe("dimen").setNilai(20)
				.setCatatan("dimen dengan angka 20"));

		AllListManResItems mri = new AllListManResItems();
		mri.addData(lmri);

		Gson gson = new Gson();
		String json_set = gson.toJson(mri);

		AllListManResItems parse = gson.fromJson(json_set,
				AllListManResItems.class);

	}
}
