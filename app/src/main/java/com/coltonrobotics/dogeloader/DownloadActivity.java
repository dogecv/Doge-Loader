package com.coltonrobotics.dogeloader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

public class DownloadActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private DownloaderUtil downloaderUtil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        final FileParser.LibEntry entry = (FileParser.LibEntry)getIntent().getSerializableExtra(MyAdapter.LIB_CLASS);
        setContentView(R.layout.download_activity);

        ((TextView)findViewById(R.id.text_name)).setText(entry.name);
        ((TextView)findViewById(R.id.text_author)).setText(entry.author);
        ((TextView)findViewById(R.id.text_desc)).setText(entry.desc);
        ((TextView)findViewById(R.id.text_filename)).setText(entry.title);
        ((TextView)findViewById(R.id.text_version)).setText(entry.version);


        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        downloaderUtil = new DownloaderUtil(this,progressBar, entry.title);

        ((Button)findViewById(R.id.button_install)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView)findViewById(R.id.text_status)).setText("Downloading from: " + entry.url);
                downloaderUtil.execute(entry.url);
            }
        });
        if(FileUtil.isInstalled(entry.title)){
            ((Button)findViewById(R.id.button_install)).setVisibility(View.INVISIBLE);
            ((Button)findViewById(R.id.button_remove)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.text_status)).setText("Installed.");
        }else{
            ((Button)findViewById(R.id.button_remove)).setVisibility(View.INVISIBLE);
            ((Button)findViewById(R.id.button_install)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.text_status)).setText("Not Installed.");
        }


        ((Button)findViewById(R.id.button_remove)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    FileUtil.Delete(entry.title);
            }
        });

    }
}
