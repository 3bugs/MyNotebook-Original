package com.example.mydiary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Promlert on 2018-03-21.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notebook.db";
    private static final int DATABASE_VERSION = 4;

    public static final String TABLE_NOTEBOOK = "notebook";
    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "title";
    public static final String COL_DETAILS = "details";
    public static final String COL_PICTURE = "picture";

    private static final String SQL_CREATE_TABLE_NOTEBOOK = "CREATE TABLE " + TABLE_NOTEBOOK + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_TITLE + " TEXT, "
            + COL_DETAILS + " TEXT, "
            + COL_PICTURE + " TEXT"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_NOTEBOOK);
        insertInitialData(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, "เรียนแอนดรอยด์");
        cv.put(COL_DETAILS, "20-22 มี.ค. 61 เวลา 9.00-16.00 น. ห้อง 506");
        db.insert(TABLE_NOTEBOOK, null, cv);

        cv = new ContentValues();
        cv.put(COL_TITLE, "หนังสือ Android ฉบับรวมโค้ด");
        cv.put(COL_DETAILS, "goo.gl/qfHyX9");
        cv.put(COL_PICTURE, "book_cover.jpg");
        db.insert(TABLE_NOTEBOOK, null, cv);

        cv = new ContentValues();
        cv.put(COL_TITLE, "ออเจ้าใบ้หวย");
        cv.put(COL_PICTURE, "bai_huay.jpg");
        db.insert(TABLE_NOTEBOOK, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTEBOOK);
        onCreate(db);
    }
}
