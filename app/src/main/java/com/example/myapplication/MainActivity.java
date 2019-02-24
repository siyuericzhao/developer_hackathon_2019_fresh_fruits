package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        CanonAPI canon = new CanonAPI();
//        canon.initialize();
//        CanonAPI canon = new CanonAPI();
////                canon.initialize();
//        canon.takePhoto();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //final CanonAPI canon = new CanonAPI();
        final String[] temp = new String[1];
        final boolean[] dirty = new boolean[1];
        //Wire up the buttons to do stuff
        //.. get the buttons to work
        Button bth = findViewById(R.id.button);
        //set what happens when the user clicks
        bth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("My App","A photo has been taken");
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CanonAPI canon = new CanonAPI();
//                canon.initialize();
                        canon.takePhoto();
                        temp[0] = canon.getRecentPhotoLink();
                        System.out.println(temp[0]);


                        String path = temp[0];
                        System.out.println(path);
                        String apiKey = "d0ec67e47f394dbb91acf261ea4a33ea";
                        String modelId = "food";
                        String versionId = "cbfe143fc2524c9f9c3a9d3221999ab9";
                        Model model = new Model(apiKey, modelId, versionId);
//                        String path = "https://thumbs.dreamstime.com/z/bad-orange-23807886.jpg";
//                        boolean fresh = model.predictImageByURL(path);
                        boolean fresh = false;
                        System.out.println("path: " + path);

                        if (path.equals("")) {
                            System.out.println("empty");
                            path = "https://thumbs.dreamstime.com/z/bad-orange-23807886.jpg";
                            fresh = model.predictImageByURL(path);
                        } else {
                            fresh = model.predictImageByURL(path);
                        }
                        dirty[0] = fresh;


                    }
                });
                t.start();
//                Toast.makeText(getApplicationContext(), "A photo has been taken",Toast.LENGTH_SHORT)
//                        .show();

                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (dirty[0]) {
                    Toast.makeText(getApplicationContext(), "FRESH ORANGE",Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "BAD ORANGE",Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        Button bth2 = findViewById(R.id.button2);
        bth2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean fresh = false;
                Log.i("My App","A photo has been taken");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CanonAPI canon = new CanonAPI();
                        String path = canon.getRecentPhotoLink();

                    }
                }).start();

                Toast.makeText(getApplicationContext(), "A photo has been taken",Toast.LENGTH_LONG)
                        .show();





            }
        });

//        final boolean[] dirty = new boolean[1];
        Button bth3 = findViewById(R.id.button3);

        bth3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("My App","A photo has been taken");
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        CanonAPI canon = new CanonAPI();
                        String path = temp[0];
                        System.out.println(path);
                        String apiKey = "d0ec67e47f394dbb91acf261ea4a33ea";
                        String modelId = "food";
                        String versionId = "cbfe143fc2524c9f9c3a9d3221999ab9";
                        Model model = new Model(apiKey, modelId, versionId);
//                        String path = "https://thumbs.dreamstime.com/z/bad-orange-23807886.jpg";
//                        boolean fresh = model.predictImageByURL(path);
                        boolean fresh = false;
                        System.out.println("path: " + path);

                        if (path.equals("")) {
                            System.out.println("empty");
                            path = "https://thumbs.dreamstime.com/z/bad-orange-23807886.jpg";
                            fresh = model.predictImageByURL(path);
                        } else {
                            fresh = model.predictImageByFilePath(path);
                        }
                        dirty[0] = fresh;
                    }
                });
                t.start();
//                try {
//                    t.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                if (dirty[0]) {
//                    Toast.makeText(getApplicationContext(), "FRESH ORANGE",Toast.LENGTH_LONG)
//                            .show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "BAD ORANGE",Toast.LENGTH_LONG)
//                            .show();
//                }







            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
