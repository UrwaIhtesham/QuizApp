package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.Activities.QuizActivity;

public class MainActivity extends AppCompatActivity {

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn=findViewById(R.id.btnGetStarted);

        btn.setOnClickListener(view -> {
            Log.d("MainActivity", "Get Started button clicked");
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            startActivity(intent);
        });

    }
}
