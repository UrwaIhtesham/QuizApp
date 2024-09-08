package com.example.quizapp.Activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;

public class QuizActivity extends AppCompatActivity {

    private TextView timerTextView, questionTextView, scoreTextView, totalQuestionsTextView;
    private Button option1, option2, option3, btnNext, btnPrev, checkCorrectAnswer;
    private int currentIndex = 0;
    private int score = 0;
    private static final int totalQuestions = 25;
    private String[] questions = new String[totalQuestions];
    private String[][] options = new String[totalQuestions][3];
    private String[] correctAns = new String[totalQuestions];
    private int[] selectedAns = new int[totalQuestions];
    private boolean[] answerChecked = new boolean[totalQuestions];
    private CountDownTimer time;
    private static final long Time_Limit = 120000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_quiz);

        timerTextView = findViewById(R.id.timer);
        questionTextView = findViewById(R.id.question);
        scoreTextView = findViewById(R.id.score);
        totalQuestionsTextView = findViewById(R.id.totalQuestions);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        checkCorrectAnswer = findViewById(R.id.correctans);

        loadQuestionsAndOptions();

        displayQuestions();

        startTimer();

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOptionClick(0);
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOptionClick(1);
            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOptionClick(2);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnNext.getText().toString().equals("Submit")){
                    finishQuiz();
                }else {
                    if (currentIndex < totalQuestions - 1) {
                        currentIndex++;
                        displayQuestions();
                    }
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex > 0) {
                    currentIndex--;
                    displayQuestions();
                }
            }
        });

        checkCorrectAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCorrectAnswer();
                answerChecked[currentIndex] = true;
                if (score > 0) {
                    score--;
                }
                scoreTextView.setText("Score: " + score);
            }
        });
    }

    private void loadQuestionsAndOptions() {
        for (int i = 0; i < totalQuestions; i++) {
            String q_ID = "question" + (i + 1);
            int res_ID = getResources().getIdentifier(q_ID, "string", getPackageName());
            questions[i] = getResources().getString(res_ID);

            for (int j = 0; j < 3; j++) {
                String opt_ID = "option_" + (i + 1) + "_" + (j + 1);
                int opt_res_ID = getResources().getIdentifier(opt_ID, "string", getPackageName());
                options[i][j] = getResources().getString(opt_res_ID);
            }

            String ca_ID = "ca_" + (i+1);
            int ca_res_ID = getResources().getIdentifier(ca_ID, "string", getPackageName());
            correctAns[i] = getResources().getString(ca_res_ID);

            selectedAns[i] = -1;
            answerChecked[i] = false;
        }
    }

    private void startTimer() {
        time = new CountDownTimer(Time_Limit, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(millisUntilFinished/1000 + " seconds");
            }

            @Override
            public void onFinish() {
                if (currentIndex < totalQuestions - 1) {
                    currentIndex++;
                    displayQuestions();
                    startTimer();
                } else {
                    finishQuiz();
                }
            }
        }.start();
    }

    private void displayQuestions() {
        questionTextView.setText(questions[currentIndex]);
        option1.setText(options[currentIndex][0]);
        option2.setText(options[currentIndex][1]);
        option3.setText(options[currentIndex][2]);
        totalQuestionsTextView.setText((currentIndex+1)+"/"+totalQuestions);

        resetButtonColors();

        if (selectedAns[currentIndex] != -1) {
            highlightSelectedAnswer(selectedAns[currentIndex], answerChecked[currentIndex]);
        }

        if (answerChecked[currentIndex]) {
            Log.d("QuizActivity", String.valueOf(answerChecked[currentIndex]));
            showCorrectAnswer();
        }

        if (currentIndex == totalQuestions - 1) {
            btnNext.setText("Submit");
        } else {
            btnNext.setText("Next");
        }
    }

    private void handleOptionClick(int selectedAnswer) {
        if (answerChecked[currentIndex]) {
            return;
        }
        selectedAns[currentIndex] = selectedAnswer;

        boolean isCorrect = options[currentIndex][selectedAnswer].equals(correctAns[currentIndex]);

        if (!isCorrect){
            if (score > 0) {
                score--;
            }
        }

        if (isCorrect) {
            score += 5;
        }

        scoreTextView.setText("Score: " + score);

        highlightSelectedAnswer(selectedAnswer, isCorrect);
    }

    private void highlightSelectedAnswer(int optionIndex, boolean isCorrect) {
        Button selectedButton = getButtonByIndex(optionIndex);
        if (selectedButton != null) {
            resetButtonColors();

            // Highlight the selected option
            if (isCorrect) {
                selectedButton.setBackgroundColor(getResources().getColor(R.color.green));
            } else {
                selectedButton.setBackgroundColor(getResources().getColor(R.color.red));
            }
            ((TextView) selectedButton).setTextColor(getResources().getColor(R.color.white));

            if (!isCorrect) {
                for (int i = 0; i < 3; i++) {
                    if (options[currentIndex][i].equals(correctAns[currentIndex])) {
                        Button correctButton = getButtonByIndex(i);
                        if (correctButton != null) {
                            correctButton.setBackgroundColor(getResources().getColor(R.color.green));
                            ((TextView) correctButton).setTextColor(getResources().getColor(R.color.white));
                        }
                        break;
                    }
                }
            }
        }
    }

    private void resetButtonColors() {
        Button[] buttons = {option1, option2, option3};
        for (Button button : buttons) {
            button.setBackgroundColor(getResources().getColor(R.color.cream)); // Reset to default
            ((TextView) button).setTextColor(getResources().getColor(R.color.black)); // Reset to default
        }
    }

    private void showCorrectAnswer() {
//        if (answerChecked[currentIndex]) {
//            correctButton.setBackgroundColor(getResources().getColor(R.color.light_purple));
//            ((TextView) correctButton).setTextColor(getResources().getColor(R.color.white));
//        }

        for (int i = 0; i < 3; i++) {
            if (options[currentIndex][i].equals(correctAns[currentIndex])) {
                Button correctButton = getButtonByIndex(i);
                if (correctButton != null) {
                    correctButton.setBackgroundColor(getResources().getColor(R.color.light_purple));
                    ((TextView) correctButton).setTextColor(getResources().getColor(R.color.white));
                }
                break;
            }
        }

        answerChecked[currentIndex] = true;
    }


    private Button getButtonByIndex(int index) {
        switch (index) {
            case 0: return option1;
            case 1: return option2;
            case 2: return option3;
            default: return null;
        }
    }

    private void finishQuiz() {
        Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("totalMarks", 125);
        startActivity(intent);
        finish();
    }

}
