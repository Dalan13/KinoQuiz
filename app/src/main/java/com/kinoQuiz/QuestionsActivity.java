package com.kinoQuiz;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;

public class QuestionsActivity extends AppCompatActivity {

    private ImageView mImView;
    private AdView mAdView;
    TextView tv;
    Button submitbutton, quitbutton, ansbutton, helpbutton, button50;
    RadioGroup radio_g;
    RadioButton rb1, rb2, rb3, rb4;
    public int milsec;
    public static String time;

    boolean helpIsUsing = false;
    int pressHelp = 3;
    boolean pressQuit = false;
    boolean pressBack = false;
    boolean press50 = false;
    boolean runningTimer = true;

    String ans1, ans2, ans3, ans4;
    String imgURL;
    String rightAnswer;
    String question;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<String> numbers = new ArrayList<String>();
    ArrayList<String> numbersAns = new ArrayList<String>();

    int flag = 0;
    public static int correct = 0,wrong = 0;
    public static double points = 0;

    @Override
    public void onBackPressed() {
        if (pressBack) {
            pressBack = false;
            runningTimer = false;
            int minutes = milsec / 6000;
            int sec = milsec % 6000 / 100;
            int mils = milsec % 100;
            setTime(minutes, mils, sec);
            Intent intent = new Intent(getApplicationContext(), RecultActivity.class);
            startActivity(intent);
        } else {
            pressBack = true;
            Toast.makeText(getApplicationContext(), "Нажмите ещё раз чтобы выйти", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        runTimer();

        CollectionReference col = db.collection("questions");

        for (int i = 0; i < 20; i++) {
            numbers.add("" + (i +1));
        }

        numbersAns.add("1"); numbersAns.add("2"); numbersAns.add("3"); numbersAns.add("4");

        Collections.shuffle(numbers);
        Collections.shuffle(numbersAns);

        DocumentReference doc = col.document("quest_" + numbers.get(flag));

        getAnswers(doc, numbersAns);

        mAdView = findViewById(R.id.adView);
        mImView = findViewById(R.id.imView);
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

        final TextView score = (TextView)findViewById(R.id.textView4);

        helpbutton = (Button)findViewById(R.id.button_help);
        button50 = (Button)findViewById(R.id.button50);
        submitbutton=(Button)findViewById(R.id.button_next);
        submitbutton.setVisibility(View.GONE);
        quitbutton=(Button)findViewById(R.id.buttonquit);
        ansbutton = (Button)findViewById(R.id.button_reply);
        ansbutton.setVisibility(View.VISIBLE);
        tv=(TextView) findViewById(R.id.tvque);

        radio_g=(RadioGroup)findViewById(R.id.answersgrp);
        rb1=(RadioButton)findViewById(R.id.radioButton);
        rb2=(RadioButton)findViewById(R.id.radioButton2);
        rb3=(RadioButton)findViewById(R.id.radioButton3);
        rb4=(RadioButton)findViewById(R.id.radioButton4);


        ansbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(radio_g.getCheckedRadioButtonId()==-1)
                {
                    Toast.makeText(getApplicationContext(), "Выберите вариант ответа", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton uans = (RadioButton) findViewById(radio_g.getCheckedRadioButtonId());
                String ansText = uans.getText().toString();

                if(ansText.equals(rightAnswer)) {
                    if(press50) {
                        points += 0.5;
                    } else points++;
                    correct++;
                    rb1.setEnabled(false);
                    rb2.setEnabled(false);
                    rb3.setEnabled(false);
                    rb4.setEnabled(false);
                    if (rb1.isChecked()) rb1.setTextColor(Color.GREEN); else rb1.setTextColor(Color.BLACK);
                    if (rb2.isChecked()) rb2.setTextColor(Color.GREEN); else rb2.setTextColor(Color.BLACK);
                    if (rb3.isChecked()) rb3.setTextColor(Color.GREEN); else rb3.setTextColor(Color.BLACK);
                    if (rb4.isChecked()) rb4.setTextColor(Color.GREEN); else rb4.setTextColor(Color.BLACK);

                }
                else {
                    wrong++;
                    rb1.setEnabled(false);
                    rb2.setEnabled(false);
                    rb3.setEnabled(false);
                    rb4.setEnabled(false);
                    if (rb1.isChecked()) rb1.setTextColor(Color.RED); else rb1.setTextColor(Color.BLACK);
                    if (rb2.isChecked()) rb2.setTextColor(Color.RED); else rb2.setTextColor(Color.BLACK);
                    if (rb3.isChecked()) rb3.setTextColor(Color.RED); else rb3.setTextColor(Color.BLACK);
                    if (rb4.isChecked()) rb4.setTextColor(Color.RED); else rb4.setTextColor(Color.BLACK);

                }

                submitbutton.setVisibility(View.VISIBLE);
                ansbutton.setVisibility(View.GONE);

                score.setText(Double.toString(points));
                flag++;
            }
        });

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressQuit = false;
                pressBack = false;

                rb1.setEnabled(true);
                rb2.setEnabled(true);
                rb3.setEnabled(true);
                rb4.setEnabled(true);
                press50 = false;

                if((pressHelp < 3) && (helpIsUsing)) {
                    ArrayList<RadioButton> radioButtons = new ArrayList<RadioButton>();
                    radioButtons.add(rb1);
                    radioButtons.add(rb2);
                    radioButtons.add(rb3);
                    radioButtons.add(rb4);

                    for (int i = 0, a = 0; a < 2; i++) {
                        if (radioButtons.get(i).getVisibility() == View.INVISIBLE) {
                            radioButtons.get(i).setVisibility(View.VISIBLE);
                            a++;
                        }
                    }
                    helpIsUsing = false;
                }

                if (rb1.isChecked()) rb1.setTextColor(Color.BLACK);
                if (rb2.isChecked()) rb2.setTextColor(Color.BLACK);
                if (rb3.isChecked()) rb3.setTextColor(Color.BLACK);
                if (rb4.isChecked()) rb4.setTextColor(Color.BLACK);

                ansbutton.setVisibility(View.VISIBLE);
                submitbutton.setVisibility(View.GONE);

                if(flag<10) {
                    DocumentReference doc = col.document("quest_" + numbers.get(flag));
                    getAnswers(doc, numbersAns);
                } else {
                    runningTimer = false;
                    int minutes = milsec / 6000;
                    int sec = milsec % 6000 / 100;
                    int mils = milsec % 100;
                    setTime(minutes, mils, sec);
                    Intent in = new Intent(getApplicationContext(),RecultActivity.class);
                    startActivity(in);
                }
                radio_g.clearCheck();
            }
        });

        quitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pressQuit) {
                    pressQuit = false;
                    runningTimer = false;
                    int minutes = milsec / 6000;
                    int sec = milsec % 6000 / 100;
                    int mils = milsec % 100;
                    setTime(minutes, mils, sec);
                    Intent intent = new Intent(getApplicationContext(), RecultActivity.class);
                    startActivity(intent);
                } else {
                    pressQuit = true;
                    Toast.makeText(getApplicationContext(), "Нажмите ещё раз чтобы выйти", Toast.LENGTH_SHORT).show();
                }
            }
        });

        helpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String text1 = ""; String text2 = ""; String text3 = ""; String text4 = "";
                if(ans1 != "") {
                    text1 = "\n1) " + rb1.getText().toString();
                }
                if(ans2 != "") {
                    text2 = "\n2) " + rb2.getText().toString();
                }
                if(ans3 != "") {
                    text3 = "\n3) " + rb3.getText().toString();
                }
                if(ans4 != "") {
                    text4 = "\n4) " + rb4.getText().toString();
                }


                sendIntent.putExtra(Intent.EXTRA_TEXT, "Мне нужна твоя помощь с вопросом\n" + question + "\nВарианты ответа" +
                         text1 + text2 + text3 + text4);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,"Поделиться"));
            }
        });

        button50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!press50) && (ansbutton.getVisibility() == View.VISIBLE)){
                    ArrayList<RadioButton> radioButtons = new ArrayList<RadioButton>();
                    radioButtons.add(rb1);
                    radioButtons.add(rb2);
                    radioButtons.add(rb3);
                    radioButtons.add(rb4);
                    Collections.shuffle(radioButtons);
                    press50 = true;

                    if (pressHelp > 0) {
                        for (int i = 0, a = 0; a < 2; i++) {
                            if (!radioButtons.get(i).getText().equals(rightAnswer)) {
                                if (ans1.equals(radioButtons.get(i).getText().toString())) ans1 = "";
                                if (ans2.equals(radioButtons.get(i).getText().toString())) ans2 = "";
                                if (ans3.equals(radioButtons.get(i).getText().toString())) ans3 = "";
                                if (ans4.equals(radioButtons.get(i).getText().toString())) ans4 = "";
                                radioButtons.get(i).setVisibility(View.INVISIBLE);
                                a++;
                            }
                        }
                        pressHelp--;
                        helpIsUsing = true;
                        Toast.makeText(getApplicationContext(), "Осталось подсказок " + pressHelp + "\nВы получаете только половину балла используя подсказки", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "У вас кончились подсказки", Toast.LENGTH_SHORT).show();
                        helpIsUsing = false;
                    }
                } else if (ansbutton.getVisibility() == View.VISIBLE){
                    Toast.makeText(getApplicationContext(), "Вы уже использовали подсказку", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Вы уже ответили на вопрос", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void getAnswers (DocumentReference doc, ArrayList<String> numbersAns) {

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        rb1.setText(document.getString("ans" + numbersAns.get(0)));
                        rb2.setText(document.getString("ans" + numbersAns.get(1)));
                        rb3.setText(document.getString("ans" + numbersAns.get(2)));
                        rb4.setText(document.getString("ans" + numbersAns.get(3)));
                        ans1 = document.getString("ans" + numbersAns.get(0));
                        ans2 = document.getString("ans" + numbersAns.get(1));
                        ans3 = document.getString("ans" + numbersAns.get(2));
                        ans4 = document.getString("ans" + numbersAns.get(3));
                        question = document.getString("question");
                        tv.setText(document.getString("question"));
                        rightAnswer = document.getString("rightAns");
                        imgURL = (document.getString("image"));
                        Glide.with(getApplicationContext())
                                .load(imgURL)
                                .into(mImView);
                    } else {
                        Log.d("READING DATA ", "No such document");
                    }
                } else {
                    Log.d("READING DATA ", "get failed with ", task.getException());
                }
            }
        });
    }

    private void runTimer() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (runningTimer) milsec++;
                handler.postDelayed(this, 10);
            }
        });
    }

    private void setTime(int minutes, int mils, int sec) {
        String m, s, ml;
        if (minutes < 10) m = "0" + minutes;
        else m = "" + minutes;
        if (sec < 10) s = "0" + sec;
        else s = "" + sec;
        if (mils < 10) ml = "0" + mils;
        else ml = "" + mils;
        time = m + ":" + s + ":" + ml;
    }
}