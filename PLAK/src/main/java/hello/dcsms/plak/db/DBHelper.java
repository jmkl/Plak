package hello.dcsms.plak.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static String DB_NAMA = "statusbar_layout_fav.db";
	public static String _NAMATABLE = "statusbar_layout", _ID = "_id",
			_KIRI = "_kiri", _KANAN = "_kanan", _TENGAH = "_tengah",
			_TANGGAL = "_tanggal", _PREVIEW = "_preview";
	private String CREATE_DB = "CREATE TABLE " + _NAMATABLE + " (" + _ID
			+ " integer primary key autoincrement," + _KIRI + " text not null,"
			+ _KANAN + " text not null," + _TENGAH + " text not null,"
			+ _TANGGAL + " integer not null," + _PREVIEW + " blob not null);";

	public DBHelper(Context context) {
		super(context, DB_NAMA, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase sql) {
		sql.execSQL(CREATE_DB);

	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int paramInt1,
			int paramInt2) {
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + _NAMATABLE);
		onCreate(sqLiteDatabase);

	}

}
