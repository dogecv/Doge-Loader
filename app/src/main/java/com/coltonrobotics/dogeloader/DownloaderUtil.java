package com.coltonrobotics.dogeloader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

class DownloaderUtil extends AsyncTask<String, String, String> {

    /**
     * Before starting background thread Show Progress Bar Dialog
     * */
    private Activity activity;
    private ProgressBar progressBar;
    private String title;




    public DownloaderUtil(Activity activity, ProgressBar progressBar, String title){
        this.activity = activity;
        this.progressBar = progressBar;
        this.title = title;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
    }

    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection conection = url.openConnection();
            conection.connect();

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            int lenghtOfFile = conection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            // Output stream
            OutputStream outputSrc = new FileOutputStream(Environment
                    .getExternalStorageDirectory().toString()
                    + "/FIRST/java/src/jars/"+title);

            OutputStream outputLibs = new FileOutputStream(Environment
                    .getExternalStorageDirectory().toString()
                    + "/FIRST/java/lib/"+title);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                // writing data to file
                outputSrc.write(data, 0, count);
                outputLibs.write(data, 0, count);
            }

            // flushing output
            outputSrc.flush();
            outputLibs.flush();
            // closing streams
            outputSrc.close();
            outputLibs.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        progressBar.setProgress(Integer.parseInt(progress[0]));
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after the file was downloaded
        progressBar.setVisibility(View.INVISIBLE);

    }

}