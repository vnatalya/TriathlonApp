package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "triathlon.db";
    private static final int DATABASE_VERSION = 6;

    public static final String TABLE_TRIATHLON = "triathlon";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String COLUMN_DATE = "date";

    private static final String CREATE_TABLE_TRIATHLON = "create table "
            + TABLE_TRIATHLON + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_TYPE + " integer, " +
            COLUMN_DURATION + " double, " +
            COLUMN_DISTANCE + " integer, " +
            COLUMN_DATE + " integer);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_TRIATHLON);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + i + " to "
                        + i2 + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIATHLON);
        onCreate(sqLiteDatabase);

    }
}
