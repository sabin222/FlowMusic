package com.example.xavin.flowmusic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecentDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="RecentlyPlayedData.db";
    public static final String TABLE_NAME="Songs";
    public static final String col_1="ID";
    public static final String col_2="SONGS_NAME";
    public static final String col_3="COUNT";

    public RecentDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+" ("+col_1+" INTEGER PRIMARY KEY AUTOINCREMENT, SONGS_NAME TEXT, COUNT INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public boolean dataInsert(String Song_Name, Integer Count)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(col_2,Song_Name);
        value.put(col_3,Count);
        long result = db.insert(TABLE_NAME,null,value);

        if (result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public Cursor readData()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null );
        return cursor;
    }

}
