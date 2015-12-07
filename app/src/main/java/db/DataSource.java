package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bean.TriathlonRecord;

/**
 *
 */
public class DataSource {
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allTriathlonColumns = {DBHelper.COLUMN_ID,
            DBHelper.COLUMN_TYPE,
            DBHelper.COLUMN_DURATION,
            DBHelper.COLUMN_DISTANCE,
            DBHelper.COLUMN_DATE};

    public enum Period {TODAY, WEEK, MONTH, ALL, CHOOSE}


    ;
    public long MILLIS_IN_MONTH = (long) 30 * 24 * 60 * 60 * 1000;
    public long MILLIS_IN_WEEK = 604800000;
    public long MILLIS_IN_HOUR = 3600000;
    public long MILLIS_IN_MINUTE = 60000;
    public long MILLIS_IN_SECOND = 1000;


    public DataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createRecord(int type, double duration, int distance, Date date) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TYPE, type);
        values.put(DBHelper.COLUMN_DURATION, duration);
        values.put(DBHelper.COLUMN_DISTANCE, distance);
        values.put(DBHelper.COLUMN_DATE, date.getTime());
        long insertId = database.insert(DBHelper.TABLE_TRIATHLON, null,
                values);
    }

    public boolean deleteRecord(int id) {
        return database.delete(dbHelper.TABLE_TRIATHLON, dbHelper.COLUMN_ID + "=" +
                String.valueOf(id), null) > 0;
    }


    public TriathlonRecord getRecordsById(int id) {
        String query = "SELECT * FROM " + dbHelper.TABLE_TRIATHLON +
                " WHERE " + dbHelper.COLUMN_ID + " = " + String.valueOf(id);
        Cursor c = database.rawQuery(query, null);
        c.moveToFirst();
        return cursorToRecord(c);
    }

    public List<TriathlonRecord> getAllRecords(Period period, int type) {
        Calendar cal = Calendar.getInstance();
        long time = 1000000;
        switch (period) {
            case TODAY:
                time = cal.get(Calendar.HOUR) * MILLIS_IN_HOUR + cal.get(Calendar.MINUTE) * MILLIS_IN_MINUTE
                        + cal.get(Calendar.SECOND) * MILLIS_IN_SECOND + cal.get(Calendar.MILLISECOND);
                break;
            case WEEK:
                time = MILLIS_IN_WEEK + cal.get(Calendar.HOUR) * MILLIS_IN_HOUR +
                        cal.get(Calendar.MINUTE) * MILLIS_IN_MINUTE + cal.get(Calendar.SECOND) * MILLIS_IN_SECOND
                        + cal.get(Calendar.MILLISECOND);
                break;
            case MONTH:
                time = MILLIS_IN_MONTH + cal.get(Calendar.HOUR) * MILLIS_IN_HOUR +
                        cal.get(Calendar.MINUTE) * MILLIS_IN_MINUTE + cal.get(Calendar.SECOND) * MILLIS_IN_SECOND
                        + cal.get(Calendar.MILLISECOND);
                break;
            default:
                time = cal.getTimeInMillis();
                break;

        }
        Cursor cursor;
        switch (type) {
            case TriathlonRecord.SWIMMING:
                cursor = database.rawQuery("SELECT * FROM " + dbHelper.TABLE_TRIATHLON +
                                " WHERE " + dbHelper.COLUMN_DATE + " > " + String.valueOf(cal.getTimeInMillis() - time) + " AND "
                                + dbHelper.COLUMN_TYPE + " < 7 ORDER BY "
                                + dbHelper.COLUMN_DATE + " DESC",
                        null);
                break;
            case TriathlonRecord.RUNNING:
                cursor = database.rawQuery("SELECT * FROM " + dbHelper.TABLE_TRIATHLON +
                                " WHERE " + dbHelper.COLUMN_DATE + " > " + String.valueOf(cal.getTimeInMillis() - time) + " AND ( "
                                + dbHelper.COLUMN_TYPE + " = 8 OR "
                                + dbHelper.COLUMN_TYPE + " = 9 ) ORDER BY "
                                + dbHelper.COLUMN_DATE + " DESC",
                        null);
                break;
            case TriathlonRecord.CYCLING:
                cursor = database.rawQuery("SELECT * FROM " + dbHelper.TABLE_TRIATHLON +
                                " WHERE " + dbHelper.COLUMN_DATE + " > " + String.valueOf(cal.getTimeInMillis() - time) + " AND ( "
                                + dbHelper.COLUMN_TYPE + " = 10 OR "
                                + dbHelper.COLUMN_TYPE + " = 11 ) ORDER BY "
                                + dbHelper.COLUMN_DATE + " DESC",
                        null);
                break;
            case TriathlonRecord.TRIATHLON:
                cursor = database.rawQuery("SELECT * FROM " + dbHelper.TABLE_TRIATHLON +
                                " WHERE " + dbHelper.COLUMN_DATE + " > "  + String.valueOf(cal.getTimeInMillis() - time)
                                + " ORDER BY " + dbHelper.COLUMN_DATE + " DESC",
                        null);
                break;
            default:
                cursor = database.rawQuery("SELECT * FROM " + dbHelper.TABLE_TRIATHLON +
                                " WHERE " + dbHelper.COLUMN_DATE + " > " + String.valueOf(cal.getTimeInMillis()- time) + " AND "
                                + dbHelper.COLUMN_TYPE + " = " + String.valueOf(type) + " ORDER BY "
                                + dbHelper.COLUMN_DATE + " DESC",
                        null);
                break;
        }
        List<TriathlonRecord> records = new ArrayList<TriathlonRecord>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TriathlonRecord triathlon = cursorToRecord(cursor);
            records.add(triathlon);
            cursor.moveToNext();
        }
        cursor.close();
        return records;
    }

    public List<TriathlonRecord> getChoosePeriodRecords(long fromDate, long toDate, int type) {
        List<TriathlonRecord> records = new ArrayList<TriathlonRecord>();
        Cursor cursor;
        switch (type) {
            case TriathlonRecord.SWIMMING:
                cursor = database.rawQuery("SELECT * FROM " + dbHelper.TABLE_TRIATHLON +
                                " WHERE " + dbHelper.COLUMN_DATE + " > " + String.valueOf(fromDate) + " AND "
                                + dbHelper.COLUMN_DATE + " < " + String.valueOf(toDate) + " AND "
                                + dbHelper.COLUMN_TYPE + " < 7  AND "
                                + dbHelper.COLUMN_TYPE + " > 0 ORDER BY "
                                + dbHelper.COLUMN_DATE + " DESC",
                        null);
                break;
            case TriathlonRecord.RUNNING:
                cursor = database.rawQuery("SELECT * FROM " + dbHelper.TABLE_TRIATHLON +
                                " WHERE " + dbHelper.COLUMN_DATE + " > " + String.valueOf(fromDate) + " AND "
                                + dbHelper.COLUMN_DATE + " < " + String.valueOf(toDate) + " AND ("
                                + dbHelper.COLUMN_TYPE + " = 8 OR "
                                + dbHelper.COLUMN_TYPE + " = 9 ) ORDER BY "
                                + dbHelper.COLUMN_DATE + " DESC",
                        null);
                break;
            case TriathlonRecord.CYCLING:
                cursor = database.rawQuery("SELECT * FROM " + dbHelper.TABLE_TRIATHLON +
                                " WHERE " + dbHelper.COLUMN_DATE + " > " + String.valueOf(fromDate) + " AND "
                                + dbHelper.COLUMN_DATE + " < " + String.valueOf(toDate) + " AND ("
                                + dbHelper.COLUMN_TYPE + " = 10 OR "
                                + dbHelper.COLUMN_TYPE + " = 11 ) ORDER BY "
                                + dbHelper.COLUMN_DATE + " DESC",
                        null);
                break;
            case TriathlonRecord.TRIATHLON:
                cursor = database.rawQuery("SELECT * FROM " + dbHelper.TABLE_TRIATHLON +
                                " WHERE " + dbHelper.COLUMN_DATE + " > " + String.valueOf(fromDate) + " AND "
                                + dbHelper.COLUMN_DATE + " < " + String.valueOf(toDate)
                                + " ORDER BY "+ dbHelper.COLUMN_DATE + " DESC",
                        null);
                break;
            default:
                cursor = database.rawQuery("SELECT * FROM " + dbHelper.TABLE_TRIATHLON +
                        " WHERE " + dbHelper.COLUMN_DATE + " > " + String.valueOf(fromDate) + " AND "
                        + dbHelper.COLUMN_DATE + " < " + String.valueOf(toDate) + " AND "
                        + dbHelper.COLUMN_TYPE + " = " + String.valueOf(type)+ " ORDER BY "
                        + dbHelper.COLUMN_DATE + " DESC",
                        null);
                break;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TriathlonRecord triathlon = cursorToRecord(cursor);
            records.add(triathlon);
            cursor.moveToNext();
        }
        cursor.close();
        return records;
    }

    private TriathlonRecord cursorToRecord(Cursor cursor) {
        TriathlonRecord record = new TriathlonRecord();
        record.setId(cursor.getInt(0));
        record.setType(cursor.getInt(1));
        record.setDuration(cursor.getDouble(2));
        record.setDistance(cursor.getInt(3));
        record.setDate(new Date(cursor.getLong(4)));
        return record;
    }


}
