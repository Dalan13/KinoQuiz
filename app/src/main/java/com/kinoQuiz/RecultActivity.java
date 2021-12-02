package com.kinoQuiz;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.google.firebase.firestore.Query.Direction.DESCENDING;


public class RecultActivity extends AppCompatActivity {
    private AdView mAdView;
    TextView tv, tv2, tv3;
    Button btnRestart, btnTop;
    EditText edName;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onBackPressed(){
        Intent in = new Intent(getApplicationContext(), Main.class);
        startActivity(in);
        QuestionsActivity.correct=0;
        QuestionsActivity.wrong=0;
        QuestionsActivity.points=0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recult);
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
        edName=(EditText)findViewById(R.id.editName);
        tv = (TextView)findViewById(R.id.tvres);
        tv2 = (TextView)findViewById(R.id.tvres2);
        tv3 = (TextView)findViewById(R.id.tvres3);
        btnRestart = (Button) findViewById(R.id.btnRestart);
        btnTop = (Button) findViewById(R.id.btnTop);

        StringBuffer sb = new StringBuffer();
        sb.append("Правильных ответов: " + QuestionsActivity.correct + "\n");
        StringBuffer sb2 = new StringBuffer();
        sb2.append("Ошибочных ответов: " + QuestionsActivity.wrong + "\n");
        StringBuffer sb3 = new StringBuffer();
        sb3.append(QuestionsActivity.points);
        tv.setText(sb);
        tv2.setText(sb2);
        tv3.setText(sb3);



        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), Main.class);
                startActivity(in);
                QuestionsActivity.correct=0;
                QuestionsActivity.wrong=0;
                QuestionsActivity.points=0;

            }
        });

        btnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edName.getText().toString().length() > 0) {
                    addUserScore(v);
                    Toast.makeText(getApplicationContext(),
                            "Результат сохранён", Toast.LENGTH_SHORT).show();
                    btnTop.setClickable(false);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Введите имя", Toast.LENGTH_SHORT).show();
                }
            }

            public void addUserScore (View view) {

                String userName = edName.getText().toString().trim();

                if(!userName.equals("")) {

                    Map<String, Object> user = new HashMap<>();
                    user.put("name", userName);
                    user.put("score", QuestionsActivity.points);
                    user.put("time", QuestionsActivity.time);

                    db.collection("top")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("WRITING SCORE ", "Failed");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Log.d("WRITING SCORE ", "Failed");
                        }
                    });

                 //   Intent intent = new Intent(FinishActivity.this, AllScoresActivity.class);
                  //  finish();
                  //  startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Сначала введите имя", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}