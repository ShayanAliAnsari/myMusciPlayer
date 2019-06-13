package com.example.smartplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    private  String[] itemAll;
    private ListView mlistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mlistview = findViewById(R.id.songslist);

checkexternalstoragepermission();
    }
   public void checkexternalstoragepermission()
    {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response)
                    {
                        displayaudiosongname();

                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response)
                    {

                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token)
                    {
                      token.continuePermissionRequest();

                    }
                }).check();

    }
    public ArrayList<File> readonlymusicaudio(File file)
    {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] allfiles = file.listFiles();
           for (File individualFiles : allfiles)
           {
               if(individualFiles.isDirectory() && !individualFiles.isHidden())
               {
                   arrayList.addAll(readonlymusicaudio(individualFiles));

               }
               else
               {
                   if (individualFiles.getName().endsWith(".mp3") || individualFiles.getName().endsWith(".aac") || individualFiles.getName().endsWith(".wav") || individualFiles.getName().endsWith(".wma"))
                   {
                    arrayList.add(individualFiles);
                   }
               }
           }
        return arrayList;
    }
    private void displayaudiosongname()
    {
      final  ArrayList<File> audiosongs =  readonlymusicaudio(Environment.getExternalStorageDirectory());
      itemAll = new String[audiosongs.size()];
      for ( int songcounter=0; songcounter<audiosongs.size();songcounter++)
      {
          itemAll[songcounter] = audiosongs.get(songcounter).getName();
      }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Main2Activity.this,android.R.layout.simple_list_item_1,itemAll);
      mlistview.setAdapter(arrayAdapter);
      mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id)
          {
              String songname = mlistview.getItemAtPosition(position).toString();
              Intent intent = new Intent(Main2Activity.this,MainActivity.class);
              intent.putExtra("song",audiosongs);
              intent.putExtra("name",songname);
              intent.putExtra("position",position);
              startActivity(intent);
          }
      });

    }
}
