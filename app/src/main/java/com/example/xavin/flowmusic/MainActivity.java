package com.example.xavin.flowmusic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final int MY_PERMISSION_REQUEST = 1;

    Cursor cursor;
    Menu menu;

    int i, a;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle aToggle;

    ListView listView;
    ArrayList<String> songs;
    ArrayAdapter<String> songs_items;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer);
        aToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(aToggle);
        aToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }

        } else {
            doStuff();
        }



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        final MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> templist = new ArrayList<>();
                for (String temp: songs) {
                    if (temp.toLowerCase().contains(newText.toLowerCase()))
                    {
                        templist.add(temp);
                    }
                    songs_items = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, templist);
                    listView.setAdapter(songs_items);
                }
                return true;
            }
        });


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (aToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        if(id==R.id.MyFavriout)
        {
            Intent intent = new Intent(this, Favorite.class);
            startActivity(intent);
        }
        if(id==R.id.equalizer)

        {
            Intent intent = new Intent(this, EqualizerFunction.class);
            startActivity(intent);
        }
        if(id==R.id.recent)
        {
            Intent intent = new Intent(this, RecentPlayList.class);
            startActivity(intent);
        }
        if(id==R.id.Lyrics)
        {
            Intent intent = new Intent(this, Lyrics.class);
            startActivity(intent);
        }


        return true;
    }
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            return ;
        }


    }
    public void doStuff()
    {

        listView = (ListView) findViewById(R.id.ListView);
        loadMusic();
        songs_items = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songs);
        listView.setAdapter(songs_items);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index1=position;
                Intent i=new Intent(getApplicationContext(),MusicPlayer.class);
                i.putExtra("index", index1);
                startActivity(i);


            }
        });
    }

    @SuppressWarnings("deprecation")
    private void loadMusic() {
        uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String projection[] = { android.provider.MediaStore.Audio.Media.DATA,
                android.provider.MediaStore.Audio.Media.TITLE,
                android.provider.MediaStore.Audio.Media.ARTIST,
                android.provider.MediaStore.Audio.Media.ALBUM,
                android.provider.MediaStore.Audio.Media.DURATION};
        cursor=this.managedQuery(uri, projection, null, null, null);
        songs=new ArrayList<String>();
        while(cursor.moveToNext())
        {
            songs.add(cursor.getString(1));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                        doStuff();
                    }
                } else {
                    Toast.makeText(this, "No Permission granted!", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        }
    }


}