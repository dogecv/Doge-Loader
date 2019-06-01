package com.coltonrobotics.dogeloader;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Progress Dialog


    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeContainer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);

        recyclerView = (RecyclerView) findViewById(R.id.repo_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)



        CollectionReference cities = db.collection("libs");

        final RecyclerView.Adapter mAdapter = new MyAdapter(FileParser.parse(this));
        recyclerView.setAdapter(mAdapter);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                db.collection("libs")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    List<FileParser.LibEntry> libEntries = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        FileParser.LibEntry newEntry = new FileParser.LibEntry();
                                        Log.d("DogeLoader", document.getId() + " => " + document.getData());
                                        newEntry.name =document.getString("name");
                                        newEntry.author =document.getString("author");
                                        newEntry.desc =document.getString("desc");
                                        newEntry.version =document.getString("version");
                                        newEntry.url =document.getString("url");
                                        newEntry.title =document.getString("filename");


                                        libEntries.add(newEntry);
                                    }
                                    ((MyAdapter) mAdapter).updateData(libEntries);

                                } else {
                                    Log.d("DogeLoader", "Error getting documents: ", task.getException());
                                }

                                swipeContainer.setRefreshing(false);
                            }
                        });
            }
        });


        db.collection("libs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<FileParser.LibEntry> libEntries = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FileParser.LibEntry newEntry = new FileParser.LibEntry();
                                Log.d("DogeLoader", document.getId() + " => " + document.getData());
                                newEntry.name =document.getString("name");
                                newEntry.author =document.getString("author");
                                newEntry.desc =document.getString("desc");
                                newEntry.version =document.getString("version");
                                newEntry.url =document.getString("url");
                                newEntry.title =document.getString("filename");


                                libEntries.add(newEntry);
                            }
                            ((MyAdapter) mAdapter).updateData(libEntries);

                        } else {
                            Log.d("DogeLoader", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );


        }

    }


}
