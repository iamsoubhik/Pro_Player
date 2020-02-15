package com.example.musicplayer;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class Player_Activityt extends AppCompatActivity {
    Button btn_next,btn_pre,btn_pause;
    TextView textView;
    SeekBar seekBar;
    static MediaPlayer myMediaPlayer;
    int position;
    String sname;
    ArrayList<File>mySongs;
    Thread updateseekbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player__activityt);

        btn_next=(Button)findViewById(R.id.btnnext);
        btn_pause=(Button)findViewById(R.id.btnpause);
        btn_pre=(Button)findViewById(R.id.btnpre);

        textView=(TextView)findViewById(R.id.txtvw);

        seekBar=(SeekBar)findViewById(R.id.seekBar);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        updateseekbar=new Thread(){

            @Override

            public void run() {

                int totalDuration=myMediaPlayer.getDuration();
                int currentPosition=0;
                while(currentPosition<totalDuration){
                    try{
                        sleep(500);
                        currentPosition=myMediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);

                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        };

        if(myMediaPlayer!=null){
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }

        Intent i=getIntent();
        Bundle bundle=i.getExtras();

        mySongs=(ArrayList) bundle.getParcelableArrayList("songs");
        sname=mySongs.get(position).getName().toString();

        String songName=i.getStringExtra("songname");
        textView.setText(songName);
        textView.setSelected(true);



        position=bundle.getInt("pos",0);

        Uri u=Uri.parse(mySongs.get(position).toString());

        myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);

        myMediaPlayer.start();

        seekBar.setMax(myMediaPlayer.getDuration());
        updateseekbar.start();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                myMediaPlayer.seekTo(seekBar.getProgress());

            }
        });


        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setMax(myMediaPlayer.getDuration());

                if(myMediaPlayer.isPlaying()){
                    btn_pause.setBackgroundResource(R.drawable.icon_play);
                    myMediaPlayer.pause();
                }
                else{
                    btn_pause.setBackgroundResource(R.drawable.icon_pause);
                    myMediaPlayer.start();
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer.stop();
                myMediaPlayer.release();
                position=((position+1)%mySongs.size());

                Uri u=Uri.parse(mySongs.get(position).toString());

                myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);

                sname=mySongs.get(position).getName().toString();
                textView.setText(sname);

                myMediaPlayer.start();
            }
        });

        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer.stop();
                myMediaPlayer.release();

                position=((position-1)<0)?(mySongs.size()-1):(position-1);

                Uri u=Uri.parse(mySongs.get(position).toString());
                myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);

                sname=mySongs.get(position).getName().toString();
                textView.setText(sname);

                myMediaPlayer.start();


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == android.R.id.home){
            onBackPressed();

        }

        return super.onOptionsItemSelected(item);

    }

}
