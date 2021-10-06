package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button startButtton;
    private TextView progressText;
    private volatile boolean stopThread = false;

    class ExampleRunnable implements Runnable {
        @Override
        public void run() {
            mockFileDownloader();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButtton = findViewById(R.id.button);
        progressText = (TextView) findViewById(R.id.textView);
        progressText.setText("Download Progress:");
    }

    public void mockFileDownloader(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButtton.setText("DOWNLOADING...");
                progressText.setVisibility(View.VISIBLE);
            }
        });

        for (int downloadProgress = 0; downloadProgress <= 100; downloadProgress = downloadProgress + 10){
            if (stopThread){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startButtton.setText("Start");
                        progressText.setVisibility(View.INVISIBLE);
                    }
                });
                return;
            }

            Log.d(TAG, "Download Progress: " + downloadProgress + "%");
            int x = downloadProgress;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressText.setText("Download Progress: " + x + "%");
                }
            });
            try{
                Thread.sleep(1000);
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButtton.setText("Start");
                progressText.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void startDownload(View view){
        stopThread = false;
        ExampleRunnable runnable = new ExampleRunnable();
        new Thread(runnable).start();
    }

    public void stopDownload(View view){
        stopThread = true;
    }
}