package com.example.quizapp.Activities;

import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;

public class ResultsActivity extends AppCompatActivity {

    private TextView scoreTextView, percentageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        scoreTextView = findViewById(R.id.finalscore);
        percentageTextView = findViewById(R.id.percentage);

        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        int totalMarks = intent.getIntExtra("totalMarks", 125);

        float percentage = ((float) score/totalMarks) * 100;

        scoreTextView.setText(("Score: " + score +"/" + totalMarks));
        percentageTextView.setText("Percentage: " + String.format("%.2f", percentage) + "%");
    }
}