package com.example.smssurfer;

import android.annotation.SuppressLint;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SMS {
    private long id; // Unique identifier for the SMS (e.g., from the database)
    private String phone; // Phone number of the sender or recipient
    private String message; // Content of the SMS
    private int type; // Type of the message (1 for received, 2 for sent)
    private long timestamp; // Timestamp in milliseconds (e.g., for sorting)
    private String shortDate; // Short date format (e.g., "dd/MM/yyyy")
    private String longDate; // Long date format (e.g., "dd/MM/yyyy HH:mm:ss")

    public SMS(String phone, String message, int type, long timestamp, String shortDate, String longDate) {
        this.phone = phone;
        this.message = message;
        this.type = type;
        this.timestamp = timestamp;
        this.shortDate = shortDate;
        this.longDate = longDate;
    }

    public SMS() {

    }

    // Getters and setters for each attribute

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public String getTime() {
        // Create a SimpleDateFormat with the desired time format
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // Format the timestamp as a time string and return it
        return sdf.format(new Date(timestamp));
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getShortDate() {
        return shortDate;
    }

    public void setShortDate(String shortDate) {
        this.shortDate = shortDate;
    }

    public String getLongDate() {
        return longDate;
    }

    public void setLongDate(String longDate) {
        this.longDate = longDate;
    }
    @SuppressLint("Range")
    public static SMS fromCursor(Cursor cursor) {
        SMS sms = new SMS();

        if (cursor != null) {
            sms.setId(cursor.getLong(cursor.getColumnIndex(DBManager.COLUMN_ID)));
            sms.setPhone(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_PHONE)));
            sms.setMessage(cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_MESSAGE)));
            sms.setType(cursor.getInt(cursor.getColumnIndex(DBManager.COLUMN_TYPE)));

            String timeString = cursor.getString(cursor.getColumnIndex(DBManager.COLUMN_TIMESTAMP));

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

            Date timeDate = null;
            try {
                timeDate = sdf.parse(timeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (timeDate != null) {
                long timestamp = timeDate.getTime();
                sms.setTimestamp(timestamp);
            }
        }

        return sms;
    }
}

