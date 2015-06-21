package hello.dcsms.plak.manual;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteItemDB extends SQLiteOpenHelper {

	private SQLiteDatabase sqlite;
	private static String DB = Environment.getExternalStorageDirectory()
			+ "/Plak/autocomplete_database.db";
	public static String NamaTabel = "NamaField", _ID = "_id",
			_NAMAPAKET = "_namapaket", _NAMA = "_namafield", _TYPE = "_tipe";
	private String CREATE = "CREATE TABLE " + NamaTabel + " (" + _ID
			+ " integer primary key autoincrement, " + _NAMAPAKET
			+ " text not null," + _NAMA + " text not null," + _TYPE
			+ " text not null)";

	private String[] all_cols = new String[] { _ID, _NAMAPAKET, _NAMA, _TYPE };
	
	public AutoCompleteItemDB(Context context) {
		super(context, Environment.getExternalStorageDirectory()
				+ "/Plak/autocomplete.db", null, 1);
	}

	public void Open() {
		sqlite = getWritableDatabase();

	}

	public void Close() {
		this.close();
	}

	@Override
	public void onCreate(SQLiteDatabase sql) {
		sql.execSQL(CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase sql, int paramInt1, int paramInt2) {
		sql.execSQL("DROP TABLE IF EXISTS " + NamaTabel);
		onCreate(sql);

	}

	// TODO CREATE
	public AutoCompleteData createACD(AutoCompleteData data) {
		ContentValues c = new ContentValues();
		c.put(_NAMAPAKET, data.getNamapaket());
		c.put(_NAMA, data.getNama());
		c.put(_TYPE, data.getType());
		long id = sqlite.insert(NamaTabel, null, c);
		Cursor cursor = sqlite.query(NamaTabel, all_cols, _ID + " = " + id,
				null, null, null, null, null);
		cursor.moveToFirst();

		AutoCompleteData newdata = cursorToData(cursor);
		cursor.close();
		return newdata;
	}
	

	// TODO READ	
	/***
	 * Ambil data dimana keys menyatakan nilai
	 * misa
	 * _TYPE,INTEGER
	 */
	public List<AutoCompleteData> getAllData(String keys, String val) {
		List<AutoCompleteData> data = new ArrayList<AutoCompleteData>();
		Cursor cursor = sqlite.query(NamaTabel, all_cols, keys + " = '" + val+"'",
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			AutoCompleteData d = cursorToData(cursor);
			data.add(d);
			cursor.moveToNext();
		}
		cursor.close();
		return data;
	}

	public List<AutoCompleteData> getAllData() {
		List<AutoCompleteData> data = new ArrayList<AutoCompleteData>();
		Cursor cursor = sqlite.query(NamaTabel, all_cols, null, null, null,
				null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			AutoCompleteData d = cursorToData(cursor);
			data.add(d);
			cursor.moveToNext();
		}
		cursor.close();
		return data;
	}

	// TODO UPDATE
	public void updateData(long id, ContentValues c) {
		sqlite.update(NamaTabel, c, _ID + " = " + id, null);
	}

	// TODO DELETE
	public void deleteData(long id) {
		sqlite.delete(NamaTabel, _ID + " = " + id, null);
	}

	public boolean isItemExist(String NAMAFIELD_KEY) {
		List<AutoCompleteData> data = getAllData();
		for (AutoCompleteData manualItemData : data) {
			if (manualItemData.getNama().equals(NAMAFIELD_KEY))
				return true;
		}
		return false;

	}

	private AutoCompleteData cursorToData(Cursor cursor) {
		AutoCompleteData d = new AutoCompleteData();
		d.setId(cursor.getLong(0));
		d.setNamapaket(cursor.getString(1));
		d.setNama(cursor.getString(2));
		d.setType(cursor.getString(3));
		return d;
	}

}
