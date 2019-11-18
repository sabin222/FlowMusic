package com.example.xavin.flowmusic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class FavDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="FavoriteData.db";
    public static final String TABLE_NAME="SongsInfo";
    public static final String col_1="ID";
    public static final String col_2="SONGS_NAME";
    public static final String col_3="SINGER";


    public FavDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+"("+col_1+" INTEGER PRIMARY KEY AUTOINCREMENT, SONGS_NAME TEXT, SINGER TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String Song_Name, String Singer)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(col_2,Song_Name);
        value.put(col_3,Singer);
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
    public Cursor getData()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null );
        return cursor;
    }

    public int Delete(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
       return db.delete(TABLE_NAME,"ID=?", new String[] {String.valueOf(id)});

    }


    public boolean Exists(String searchItem) {

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { col_2 };
        String selection = col_2 + " =?";
        String[] selectionArgs = { searchItem };
        String limit = "1";

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
