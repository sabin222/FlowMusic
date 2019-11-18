package com.example.xavin.flowmusic;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class Lyrics extends AppCompatActivity {
    static TextView status;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyrics_fragment);

        status = findViewById(R.id.status);
        if (AppStatus.getInstance(this).isOnline()) {
            DataFetch process = new DataFetch();
            process.execute();

        } else {

            Toast.makeText(getApplicationContext(), "No Connection Available", Toast.LENGTH_LONG).show();

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {


                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
                status = (TextView) findViewById(R.id.status);

                // searchView.getQuery().toString();
                status.setText(searchView.getQuery().toString());
                return false;
            }
        });
            return true;
    }
}
