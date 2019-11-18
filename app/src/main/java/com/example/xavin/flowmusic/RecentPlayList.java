package com.example.xavin.flowmusic;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecentPlayList extends AppCompatActivity {

    List<String> recentPlay;
    ArrayAdapter<String> items;
    RecentDatabase db;
    ListView listView;
    Uri uri;
    Cursor cursor;
    List<String> music;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_play);

        recentPlay = new ArrayList<>();
        listView = findViewById(R.id.list_View);
        db = new RecentDatabase(this);
        showData();
        items = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recentPlay);
        listView.setAdapter(items);

    }

    public void showData()
    {
        cursor = db.readData();
        if (cursor.getCount()==0)
        {
            Toast.makeText(getApplicationContext(), "No Songs played Recently", Toast.LENGTH_SHORT).show();
        }

        while (cursor.moveToNext())
        {

            recentPlay.add(cursor.getString(1 ));

        }

    }
}
