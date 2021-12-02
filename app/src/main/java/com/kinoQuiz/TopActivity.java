package com.kinoQuiz;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.google.firebase.firestore.Query.Direction.DESCENDING;


public class TopActivity extends AppCompatActivity {

    ArrayList<Point> points = new ArrayList<>();

    Button menuButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private AdView mAdView;

    public TopActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }


            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        menuButton = findViewById(R.id.button);

        getTop();

        for (int i = 0; i < points.size(); i++)
            addTableRow(points.get(i).getUser(), points.get(i).getScore(), points.get(i).getTime());



        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Main.class);
            startActivity(intent);
        });
    }

    public void addTableRow(String name, String score, String time) {
        TableLayout stk = findViewById(R.id.table_top);
        TableRow tbrow = new TableRow(this);
        TextView t1v = new TextView(this);
        t1v.setText(name);
        t1v.setTextColor(Color.BLACK);
        t1v.setTextSize(24);
        t1v.setWidth(400);
        t1v.setBackgroundResource(R.drawable.white_back);
        t1v.setGravity(Gravity.CENTER);
        tbrow.addView(t1v);
        TextView t2v = new TextView(this);
        t2v.setText(score);
        t2v.setTextColor(Color.BLACK);
        t2v.setTextSize(24);
        t2v.setWidth(260);
        t2v.setBackgroundResource(R.drawable.white_back);
        t2v.setGravity(Gravity.CENTER);
        tbrow.addView(t2v);
        TextView t3v = new TextView(this);
        t3v.setText(time);
        t3v.setTextColor(Color.BLACK);
        t3v.setTextSize(24);
        t3v.setWidth(420);
        t3v.setBackgroundResource(R.drawable.white_back);
        t3v.setGravity(Gravity.CENTER);
        tbrow.addView(t3v);
        stk.addView(tbrow);
    }

    public void getTop() {
        db.collection("top")
                .orderBy("time")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("READING ALL", document.getId() + " => " + document.getData());
                                System.out.println(document.getId());
                                Point point = new Point();
                                point.setUser(document.get("name").toString());
                                point.setScore(document.get("score").toString());
                                point.setTime(document.get("time").toString());
                                points.add(point);
                                i++;
                            }
                            points.sort(new SortPoints());
                            for(i = 0; i < points.size(); i++) {
                                addTableRow(points.get(i).getUser(), points.get(i).getScore(), points.get(i).getTime());
                            }

                        } else {
                            Log.d("READING ALL", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}

