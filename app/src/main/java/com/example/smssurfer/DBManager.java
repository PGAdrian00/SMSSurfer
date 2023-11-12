package com.example.smssurfer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sms.db";
    private static final int DATABASE_VERSION = 1;

    // Numele tabelului și numele coloanelor
    public static final String TABLE_SMS = "sms";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_SHORT_DATE = "short_date";
    public static final String COLUMN_LONG_DATE = "long_date";
    private Context context;

    private static final String DATABASE_CREATE = "create table " + TABLE_SMS + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_PHONE + " text not null, " +
            COLUMN_MESSAGE + " text not null, " +
            COLUMN_TYPE + " integer not null, " +
            COLUMN_TIMESTAMP + " integer not null, " +
            COLUMN_SHORT_DATE + " text not null, " +
            COLUMN_LONG_DATE + " text not null);";

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    public Cursor getMessagesForPhoneNumber(String phoneNumber) {
        String query = "SELECT * FROM " + TABLE_SMS + " WHERE " + COLUMN_PHONE + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, new String[]{phoneNumber});
        }

        return cursor;
    }

    public long addNewMessage(String phone, String message, int type, String timestamp, String shortDate, String longDate) {
        SQLiteDatabase db = null;
        long id = -1; // Default ID value if insertion fails

        try {
            db = this.getWritableDatabase();

            if (db != null) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_PHONE, phone);
                values.put(COLUMN_MESSAGE, message);
                values.put(COLUMN_TYPE, type);
                values.put(COLUMN_TIMESTAMP, timestamp);
                values.put(COLUMN_SHORT_DATE, shortDate);
                values.put(COLUMN_LONG_DATE, longDate);

                // Attempt to insert the data
                id = db.insert(TABLE_SMS, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions, e.g., database errors
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return id; // Return the ID of the newly inserted message (or -1 if failed)
    }

    public Cursor getMessages(){
        String query = "SELECT * FROM " + TABLE_SMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMS);
        onCreate(db);
    }

    void deleteMessage(long messageId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_SMS, COLUMN_ID + " = ?", new String[]{String.valueOf(messageId)});

        db.close();
    }
}
