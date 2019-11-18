package com.example.xavin.flowmusic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayer extends Activity implements OnClickListener{

    MediaPlayer mediaplayer =PlayControl.getMediaPlayer();
    private Handler handler = new Handler();
    AudioManager audiomanager;
    ArrayList<String> songs;
    Cursor cursor;
    Uri uri;
    FavDatabase db;
    RecentDatabase db1;
    int Count=0;

    private int index=0;
    private int index1=0;
    private boolean isShuffle = false;
    private boolean isRepeat = false;



    SeekBar seekbar;
    static TextView tvSongName;
    TextView top;
    TextView maxTime;
    TextView intTime;
    ImageView backgroung;
    ImageButton play, next, previous, back, list , shuffle, repeat,favorite, lyrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db1 = new RecentDatabase(this.getApplicationContext());
        setContentView(R.layout.player_layout);

        seekbar = (SeekBar) findViewById(R.id.seekBar);
        tvSongName = (TextView) findViewById(R.id.tvSongName);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        back = findViewById(R.id.back);
        backgroung = findViewById(R.id.backgroung);
        top = findViewById(R.id.top);
        list = findViewById(R.id.list1);
        maxTime = findViewById(R.id.maxTime);
        intTime = findViewById(R.id.intTime);
        shuffle = findViewById(R.id.shuffle);
        repeat= findViewById(R.id.repeat);
        favorite=findViewById(R.id.favorite);
        db = new FavDatabase(this);


        play.setOnClickListener(this);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        shuffle.setOnClickListener(this);
        repeat.setOnClickListener(this);
        favorite.setOnClickListener(this);





        audiomanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        getMusic();

        Bundle b=getIntent().getExtras();
        index = b.getInt("index");
        playSong(index);


        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicPlayer.this, MainActivity.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicPlayer.this, MainActivity.class);
                startActivity(intent);
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String time = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(progress),
                        TimeUnit.MILLISECONDS.toSeconds(progress) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(progress))
                );
                intTime.setText(String.valueOf(time));
            }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            intTime.setVisibility(View.VISIBLE);

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (mediaplayer != null && mediaplayer.isPlaying())
            {
                mediaplayer.seekTo(seekBar.getProgress());
            }
        }

    });
    }


    @SuppressWarnings("deprecation")
    private void getMusic() {
        uri=android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String projection[]={android.provider.MediaStore.Audio.Media.DATA,
                android.provider.MediaStore.Audio.Media.TITLE,
                android.provider.MediaStore.Audio.Media.ARTIST,
                android.provider.MediaStore.Audio.Media.ALBUM,
                android.provider.MediaStore.Audio.Media.DURATION};
        cursor=this.managedQuery(uri, projection, null, null,null);
        songs=new ArrayList<String>();
        while (cursor.moveToNext()) {
            songs.add(cursor.getString(1));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
            {
                if (mediaplayer.isPlaying()) {
                    mediaplayer.pause();
                    play.setBackgroundResource(R.drawable.play1);
                } else if (mediaplayer != null) {
                    mediaplayer.start();
                    play.setBackgroundResource(R.drawable.pause1);

                }
                break;
            }
            case R.id.next:
            {
                if (index < (songs.size() - 1)) {
                    index += 1;
                    playSong(index);
                } else {
                    index = 0;
                    playSong(index);
                }
                break;
            }
            case R.id.previous:
            {
                if (index > 0) {
                    index -= 1;
                    playSong(index);
                } else {
                    index = songs.size() - 1;
                    playSong(index);
                }
                break;
            }
            case R.id.shuffle:
            {
                if (isShuffle) {
                    isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    shuffle.setBackgroundResource(R.drawable.shuffle);
                } else {
                    isShuffle = true;
                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    shuffle.setBackgroundResource(R.drawable.shuffle0n);
                }
                break;
            }
            case R.id.repeat:
            {
                if (isRepeat) {
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    repeat.setBackgroundResource(R.drawable.repeat);
                } else {
                    isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    repeat.setBackgroundResource(R.drawable.repeat0n);

                }
                break;
             }
            case R.id.favorite:
            {
                boolean isFavorite = readState();
                if (isFavorite) {
                    isFavorite = false;
                    favorite.setBackgroundResource(R.drawable.favorite);
                    saveState(isFavorite);

                } else {
                    isFavorite = true;
                    addData();
                    Toast.makeText(getApplicationContext(), "Song Added", Toast.LENGTH_LONG).show();
                    favorite.setBackgroundResource(R.drawable.favou);
                    saveState(isFavorite);
                }
                break;
            }

            default:
                break;
        }

//
    }

        private void saveState(boolean isFavourite) {
            SharedPreferences aSharedPreferences = this.getSharedPreferences(
                    "Favourite", Context.MODE_PRIVATE);
            SharedPreferences.Editor aSharedPreferencesEdit = aSharedPreferences
                    .edit();
            aSharedPreferencesEdit.putBoolean("State", isFavourite);
            aSharedPreferencesEdit.commit();
        }

        private boolean readState() {
            SharedPreferences aSharedPreferences = this.getSharedPreferences(
                    "Favourite", Context.MODE_PRIVATE);
            return aSharedPreferences.getBoolean("State", true);
        }

        public void addData()
            {
                db.insertData(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            }


    private void playSong(int index2) {
        cursor.moveToPosition(index2);
        int song_id=cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        String song_name=cursor.getString(song_id);

        int Singer=cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        String singer=cursor.getString(Singer);

        final int song =cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        String title = cursor.getString(song);
        tvSongName.setText(title);
        tvSongName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvSongName.setSingleLine(true);
        tvSongName.setSelected(true);



        int duration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
        String Duration = cursor.getString(duration);
        String time = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(Duration)),
                TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(Duration)) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(Duration)))
        );
         maxTime.setText(time);

        updateSeekBar();
        db1.dataInsert(title,Count);

        try {
            mediaplayer.stop();
            mediaplayer.reset();
            mediaplayer.setDataSource(song_name);
            mediaplayer.prepare();
            mediaplayer.start();
            mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    if (isShuffle)
                    {
                             index = (int) (Math.random() * songs.size());
                            playSong(index);
                    }
                    else if (isRepeat)
                    {
                        playSong(index);
                    }
                    else
                     {
                         if (index < (songs.size() - 1))
                            {
                                playSong(index + 1);
                                index = index + 1;
                            }
                            else
                              {
                                playSong(0);
                                index = 0;
                              }
                     }
                 }

            });


        } catch (IllegalArgumentException | SecurityException | IOException | IllegalStateException e) {
            e.printStackTrace();
        }

    }
    private void updateSeekBar() {

        seekbar.setProgress(mediaplayer.getCurrentPosition()/100);
        handler.postDelayed(runnable, 100);
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            int current = mediaplayer.getCurrentPosition();
            int MaxTime = mediaplayer.getDuration();
            seekbar.setMax(MaxTime);
            seekbar.setProgress(current);
        }
    };



}