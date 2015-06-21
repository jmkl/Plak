package hello.dcsms.plak.manual;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ManualItemDB extends SQLiteOpenHelper {

    private SQLiteDatabase sqlite;
    private String NAMATABLE = "item_table", _ID = "_id", _ITEM = "_item",
            _ITEMTYPE = "_itemtype", _ITEMVALUE = "_itemval",
            _BOOL = "_itembool", _ITEMDESC = "_itemdesc",
            _ITEMPACKAGE = "_itempackage";
    private static String DBNAME = "data_manual_items.db";
    String str = String.format("CREATE TABLE %s ("
                    + "%s integer primary key autoincrement," + "%s text not null,"
                    + "%s text not null," + "%s text not null,"
                    + "%s integer, %s text not null, %s text not null)", NAMATABLE,
            _ID, _ITEM, _ITEMTYPE, _ITEMVALUE, _BOOL, _ITEMDESC, _ITEMPACKAGE);

    private String[] all_colums = new String[]{_ID, _ITEM, _ITEMTYPE,
            _ITEMVALUE, _BOOL, _ITEMDESC, _ITEMPACKAGE};

    private static File dbfile = new File(Environment.getDataDirectory()
            + "data/hello.dcsms.plak/databases/" + DBNAME);

    public ManualItemDB(Context context) {
        super(context, DBNAME, null, 1);

    }

    public void Open() {
        sqlite = getWritableDatabase();

    }

    public void Close() {
        this.close();
    }

    public ManualItemData createItemData(ManualItemData data) {
        try {
            ContentValues c = new ContentValues();
            c.put(_ITEM, data.getNamaField());
            c.put(_ITEMTYPE, data.getTipe());
            c.put(_ITEMVALUE, data.getNilai());
            c.put(_BOOL, data.isCek());
            c.put(_ITEMDESC, data.getCatatan());
            c.put(_ITEMPACKAGE, data.getNamaPaket());

            long id = sqlite.insert(NAMATABLE, null, c);
            Cursor cursor = sqlite.query(NAMATABLE, all_colums, _ID + " = " + id,
                    null, null, null, _ITEMPACKAGE);
            cursor.moveToFirst();
            ManualItemData newdata = CursorToMID(cursor);
            cursor.close();
            return newdata;
        } catch (Exception e) {
            return null;
        }

    }

    public boolean isItemExist(String namafield_key) {
        List<ManualItemData> data = getAllData();
        for (ManualItemData manualItemData : data) {
            if (manualItemData.getNamaField().equals(namafield_key))
                return true;
        }
        return false;

    }

    public List<ManualItemData> getAllData() {
        List<ManualItemData> data = new ArrayList<ManualItemData>();
        Cursor cursor = sqlite.query(NAMATABLE, all_colums, null, null, null,
                null, _ITEMPACKAGE);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ManualItemData d = CursorToMID(cursor);
            data.add(d);
            cursor.moveToNext();
        }
        cursor.close();
        return data;
    }

    private ManualItemData CursorToMID(Cursor cursor) {
        ManualItemData data = new ManualItemData();
        data.setId(cursor.getLong(0));
        data.setNamaField(cursor.getString(1));
        data.setItemType(cursor.getString(2));
        data.setItemValue(cursor.getString(3));
        data.setCek(cursor.getLong(4));
        data.setCatatan(cursor.getString(5));
        data.setNamaPaket(cursor.getString(6));
        return data;

    }

    public void updateCekData(ManualItemData data) {
        ContentValues c = new ContentValues();
        c.put(_BOOL, data.isCek());
        sqlite.update(NAMATABLE, c, _ID + " = " + data.getId(), null);
    }

    public void updateData(ManualItemData data) {
        ContentValues c = new ContentValues();
        c.put(_ITEM, data.getNamaField());
        c.put(_ITEMDESC, data.getCatatan());
        c.put(_ITEMPACKAGE, data.getNamaPaket());
        c.put(_ITEMVALUE, data.getNilai());
        sqlite.update(NAMATABLE, c, _ID + " = " + data.getId(), null);
    }

    public int dropAllData() {
        return sqlite.delete(NAMATABLE, null, null);
    }

    public void DeleteData(long id) {
        sqlite.delete(NAMATABLE, _ID + " = " + id, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sql) {
        sql.execSQL(str);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sql, int paramInt1, int paramInt2) {
        sql.execSQL("DROP TABLE IF EXISTS " + NAMATABLE);
        onCreate(sql);

    }

}
