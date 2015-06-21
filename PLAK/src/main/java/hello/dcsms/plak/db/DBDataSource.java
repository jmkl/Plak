package hello.dcsms.plak.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBDataSource {
	private SQLiteDatabase sqlite;
	private DBHelper dbhelper;
	private String[] all_cols = new String[] { DBHelper._ID, DBHelper._KIRI,
			DBHelper._KANAN, DBHelper._TENGAH, DBHelper._TANGGAL,
			DBHelper._PREVIEW };

	public DBDataSource(Context context) {
		dbhelper = new DBHelper(context);

	}

	public void _Open() {
		sqlite = dbhelper.getWritableDatabase();

	}

	public void _CLose() {
		sqlite.close();
	}

	// TODO CRUD MADAFAKA
	// TODO CREATE
	public DBdata BikinData(DBdata data) {
		ContentValues val = new ContentValues();
		val.put(DBHelper._KIRI, data.getKiri());
		val.put(DBHelper._KANAN, data.getKanan());
		val.put(DBHelper._TENGAH, data.getTengah());
		val.put(DBHelper._TANGGAL, data.getTanggal());
		val.put(DBHelper._PREVIEW, data.getPreview());

		// id otomatis
		long id = sqlite.insert(DBHelper._NAMATABLE, null, val);
		// cursor semuadata
		Cursor cursor = sqlite.query(DBHelper._NAMATABLE, all_cols,
				DBHelper._ID + " = " + id, null, null, null, null);
		cursor.moveToFirst();
		DBdata _newDBDATA = CursorToDBData(cursor);
		cursor.close();
		return _newDBDATA;
	}

	// TODO READ
	public List<DBdata> getAllData() {
		List<DBdata> result = new ArrayList<DBdata>();
		Cursor cursor = sqlite.query(DBHelper._NAMATABLE, all_cols, null, null,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			DBdata d = CursorToDBData(cursor);
			result.add(d);
			cursor.moveToNext();
		}
		cursor.close();
		return result;
	}

	// TODO UPDATE
	// TODO DELETE
	public void DeleteData(long id) {
		sqlite.delete(DBHelper._NAMATABLE, DBHelper._ID + " = " + id, null);
	}

	public void DropThisShitOutofMeh() {
		sqlite.delete(DBHelper._NAMATABLE, null, null);
	}

	private DBdata CursorToDBData(Cursor cursor) {
		DBdata db = new DBdata();
		db.setIDs(cursor.getLong(0));
		db.setKiri(cursor.getString(1));
		db.setKanan(cursor.getString(2));
		db.setTengah(cursor.getString(3));
		db.setTanggal(cursor.getLong(4));
		db.setPreview(cursor.getBlob(5));
		return db;

	}

}
