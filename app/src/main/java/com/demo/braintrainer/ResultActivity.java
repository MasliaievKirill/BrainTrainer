package com.demo.braintrainer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView textViewResult;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.orange));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        textViewResult = findViewById(R.id.textViewTotalResult);
        Intent intent = getIntent();
        if (intent.hasExtra("result")){
            int result = intent.getIntExtra("result", 0 );
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            int max = preferences.getInt("max", 0);
            String totalResult = String.format("Результат: %s\nМаксимальный результат: %s", result, max);
            textViewResult.setText(totalResult);
        }
    }

    public void onClickStartOver(View view) {
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}