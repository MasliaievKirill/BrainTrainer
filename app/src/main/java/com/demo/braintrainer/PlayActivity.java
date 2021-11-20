package com.demo.braintrainer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class PlayActivity extends AppCompatActivity {

    private TextView textViewCurrentStatistic;
    private TextView textViewTimer;
    private TextView textViewTask;
    private TextView textViewAnswer1;
    private TextView textViewAnswer2;
    private TextView textViewAnswer3;
    private TextView textViewAnswer4;
    ArrayList<TextView> options = new ArrayList<>();

    private String question;
    private int rightAnswer;
    private int rightAnswerPosition;
    private boolean isPositive;
    private int min = 5;
    private int max = 30;

    private int numberOfAnswers = 0;
    private int numberOfRightAnswers = 0;

    private boolean gameOver = false;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.orange));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        textViewCurrentStatistic = findViewById(R.id.textViewCurrentStatistic);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewTask = findViewById(R.id.textViewTask);
        textViewAnswer1 = findViewById(R.id.textViewAnswer1);
        textViewAnswer2 = findViewById(R.id.textViewAnswer2);
        textViewAnswer3 = findViewById(R.id.textViewAnswer3);
        textViewAnswer4 = findViewById(R.id.textViewAnswer4);
        options.add(textViewAnswer1);
        options.add(textViewAnswer2);
        options.add(textViewAnswer3);
        options.add(textViewAnswer4);
        startPlay();
        CountDownTimer playTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTimer.setText(getTime(millisUntilFinished));
                if (millisUntilFinished < 10000) {
                    textViewTimer.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }

            @Override
            public void onFinish() {
                gameOver = true;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int max = preferences.getInt("max", 0);
                if (numberOfRightAnswers >= max) {
                    preferences.edit().putInt("max" ,numberOfAnswers).apply();
                }
                Intent intent = new Intent(PlayActivity.this, ResultActivity.class);
                intent.putExtra("result",numberOfRightAnswers);
                startActivity(intent);

            }
        }.start();
    }

    public void onClickAnswer(View view) {
        if (!gameOver) {
            TextView textView = (TextView) view;
            String answer = textView.getText().toString();
            int chosenAnswer = Integer.parseInt(answer);
            if (chosenAnswer == rightAnswer) {
                Toast.makeText(this, "Верно", Toast.LENGTH_SHORT).show();
                numberOfRightAnswers++;
            } else {
                Toast.makeText(this, "Неправильно, правильный ответ " + rightAnswer, Toast.LENGTH_SHORT).show();
            }
            numberOfAnswers++;
            startPlay();
        }
    }

    private void startPlay () {
        generateTask();
        for (int i = 0; i < options.size(); i++) {
            if (i == rightAnswerPosition) {
                options.get(i).setText(Integer.toString(rightAnswer));
            } else {
                options.get(i).setText(Integer.toString(generateWrongAnswer()));
            }
        }
        String currentStatistic = String.format("%s/%s", numberOfRightAnswers, numberOfAnswers);
        textViewCurrentStatistic.setText(currentStatistic);
    }

    private void generateTask () {
        int num1 = (int) (Math.random() * (max - min +1) + min);
        int num2 = (int) (Math.random() * (max - min +1) + min);
        int mark = (int) (Math.random() * 2);
        isPositive = mark == 1;
        if (isPositive) {
            rightAnswer = num1 + num2;
            question = String.format("%s + %s", num1, num2);
        } else {
            rightAnswer = num1 - num2;
            question = String.format("%s - %s", num1, num2);
        }
        rightAnswerPosition = (int) (Math.random() * 4);
        textViewTask.setText(question);
    }

    private int generateWrongAnswer () {
        int result;
        do {
        result = (int) (Math.random() * max * 2 + 1) - (max - min);
        } while (result == rightAnswer);
        return result;
    }

    private String getTime (long millis) {
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }
}