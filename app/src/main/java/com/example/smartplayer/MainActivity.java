package com.example.smartplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout parentlayoutrelative;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private String keeper = "";
    private ImageView imprevious,impause,imnext;
    private Button btnEnablevoice;
    private TextView tvSongname;
    private ImageView imageView;
    private  RelativeLayout rlLowerLayout;
    private String mode = "ON";
    private MediaPlayer mediaPlayer;
    private int position;
    private ArrayList<File> mysoongs;
    private String msongName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkvoicepermissioncommand();
        imprevious = (ImageView)findViewById(R.id.previousbtn);
        impause = (ImageView)findViewById(R.id.pausebtn);
        imnext =(ImageView)findViewById(R.id.nextbtn);
        tvSongname = (TextView)findViewById(R.id.songsName);
        btnEnablevoice =(Button)findViewById(R.id.voiceEnableBtn);
        imageView =(ImageView)findViewById(R.id.logo);
        rlLowerLayout=findViewById(R.id.lower);


        parentlayoutrelative= findViewById(R.id.parentrelativelayuout);
        speechRecognizer = speechRecognizer.createSpeechRecognizer(MainActivity.this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        validateandrelaseplayornot();
        imageView.setBackgroundResource(R.drawable.logo);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results)
            {
                ArrayList<String> matchesfound = results.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);
                if(matchesfound != null)
                {
                   if(mode.equals("ON"))
                   {
                       keeper = matchesfound.get(0);
                       if(keeper.equals("pause the song") || keeper.equals("shayan says pause") || keeper.equals("pause"))
                       {
                           playpausenextsong();
                           Toast.makeText(MainActivity.this,"Command =" + keeper,Toast.LENGTH_LONG ).show();

                       }
                       else  if(keeper.equals("play the song") || keeper.equals("shayan says play") || keeper.equals("play"))
                       {
                           playpausenextsong();
                           Toast.makeText(MainActivity.this,"Command =" + keeper,Toast.LENGTH_LONG ).show();

                       }
                       else  if(keeper.equals("next song") || keeper.equals("shayan says next song") || keeper.equals("next song"))
                       {
                           playnextsong();
                           Toast.makeText(MainActivity.this,"Command =" + keeper,Toast.LENGTH_LONG ).show();

                       }
                       else  if(keeper.equals("previous song") || keeper.equals("shayan says previous song") || keeper.equals("previous song"))
                       {
                           previoussong();
                           Toast.makeText(MainActivity.this,"Command =" + keeper,Toast.LENGTH_LONG ).show();

                       }
                       else
                       {
                           Toast.makeText(MainActivity.this,"Voice Command Error",Toast.LENGTH_SHORT).show();
                       }
                   }
                 //   Toast.makeText(MainActivity.this,"Result = " + keeper, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
        parentlayoutrelative.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
              switch (event.getAction())
              {
                  case MotionEvent.ACTION_DOWN:

                      speechRecognizer.startListening(speechRecognizerIntent);
                      keeper = "";
                  break;
                  case MotionEvent.ACTION_UP:
                      speechRecognizer.stopListening();
                      break;
              }
              return false;
            }
        });

        btnEnablevoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode.equals("ON"))
                {

                    mode = "OFF";
                    btnEnablevoice.setText("ENABLE VOICE : OFF");
                    rlLowerLayout.setVisibility(View.VISIBLE);
                }
                else
                {

                    mode = "ON";
                    btnEnablevoice.setText("ENABLE VOICE : ON");
                    rlLowerLayout.setVisibility(View.GONE);
                }
            }
        });
        impause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playpausenextsong();
            }
        });
        imprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.getCurrentPosition()>0)
                {
                    previoussong();
                }
            }
        });
        imnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.getCurrentPosition()>0)
                {
                    playnextsong();
                }
            }
        });
    }

    private void validateandrelaseplayornot()
    {
        if(mediaPlayer !=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mysoongs =(ArrayList)bundle.getParcelableArrayList("song");
          msongName=mysoongs.get(position).getName();
          String songName = intent.getStringExtra("name");
           tvSongname.setText(songName);
           tvSongname.setSelected(true);
           position = bundle.getInt("position",0);
           Uri uri  = Uri.parse(mysoongs.get(position).toString());
           mediaPlayer= MediaPlayer.create(MainActivity.this,uri);
           mediaPlayer.start();
        }
    private void checkvoicepermissioncommand()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
        {

            if(!(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED))
            {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }

    }
    private void playpausenextsong()
    {

        imageView.setBackgroundResource(R.drawable.four);
        if(mediaPlayer.isPlaying())
        {
            impause.setImageResource(R.drawable.play);
            mediaPlayer.pause();
        }
        else
        {
            impause.setImageResource(R.drawable.pause);
            mediaPlayer.start();
            imageView.setBackgroundResource(R.drawable.five);
        }
    }

    private void  playnextsong()
    {
        mediaPlayer.pause();
        mediaPlayer.stop();
        mediaPlayer.release();
        position=((position+1)%mysoongs.size());
        Uri uri = Uri.parse(mysoongs.get(position).toString());
        mediaPlayer=MediaPlayer.create(MainActivity.this,uri);
        msongName=mysoongs.get(position).toString();
        tvSongname.setText(msongName);
        mediaPlayer.start();
        imageView.setBackgroundResource(R.drawable.three);
        if(mediaPlayer.isPlaying())
        {
            impause.setImageResource(R.drawable.pause);

        }
        else
        {
            impause.setImageResource(R.drawable.play);

            imageView.setBackgroundResource(R.drawable.five);
        }

    }
    private void previoussong()
    {
        mediaPlayer.pause();
        mediaPlayer.stop();
        mediaPlayer.release();
        position = ((position-1)<0 ? (mysoongs.size()-1): (position-1));
        Uri uri = Uri.parse(mysoongs.get(position).toString());
        mediaPlayer=MediaPlayer.create(MainActivity.this,uri);

        msongName=mysoongs.get(position).toString();
        tvSongname.setText(msongName);
        mediaPlayer.start();
        imageView.setBackgroundResource(R.drawable.two);
        if(mediaPlayer.isPlaying())
        {
            impause.setImageResource(R.drawable.pause);

        }
        else
        {
            impause.setImageResource(R.drawable.play);

            imageView.setBackgroundResource(R.drawable.five);
        }


    }
}
