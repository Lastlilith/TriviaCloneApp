package com.example.triviaappclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.triviaappclone.data.Repository;
import com.example.triviaappclone.databinding.ActivityMainBinding;
import com.example.triviaappclone.model.Question;
import com.example.triviaappclone.model.Score;
import com.example.triviaappclone.util.Prefs;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int currentQuestionIndex = 0;
    List<Question> questionList;
    private int scoreCounter = 0;
    private Score score;
    private Prefs prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        score = new Score();
        prefs = new Prefs(MainActivity.this);
        //Retrieve the last state
        currentQuestionIndex = prefs.getState();

        binding.highestScoreText.setText(MessageFormat.format("Highest: {0}",
                String.valueOf(prefs.getHighestScore())));
        binding.scoreTextView.setText(MessageFormat.format("Score: {0}", score.getScore()));

        questionList = new Repository().getQuestions(questionArrayList -> {
                    binding.questionTextView.setText(questionArrayList.get(currentQuestionIndex)
                            .getAnswer());

                    updateCounter(questionArrayList);
                }

        );

        binding.buttonNext.setOnClickListener(view -> getNextQuestion());

        binding.buttonTrue.setOnClickListener(view -> {
            checkAnswer(true);
            updateQuestion();
        });

        binding.buttonFalse.setOnClickListener(view -> {
            checkAnswer(false);
            updateQuestion();
        });

        binding.shareButton.setOnClickListener(view -> shareScore());
    }

    private void shareScore() {

        String message = "My current score is "+ score.getScore() + " and "+
                "My highest score is "+ prefs.getHighestScore();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "I am playing Trivia");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(intent);
    }

    private void getNextQuestion() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
        updateQuestion();
    }

    private void checkAnswer(boolean userChoseCorrect) {
        boolean answer = questionList.get(currentQuestionIndex).isAnswerTrue();
        int snackMessageId;
        if (userChoseCorrect == answer) {
            snackMessageId = R.string.correct_answer;
            fadeAnimation();
            addPoints();
        } else {
            deductPoints();
            snackMessageId = R.string.incorrect_answer;
            shakeAnimation();
        }

        Snackbar.make(binding.cardView, snackMessageId, Snackbar.LENGTH_SHORT).show();
    }


    private void updateCounter(ArrayList<Question> questionArrayList) {
        binding.textViewOutOf.setText(String.format(getString(R.string.text_formatted),
                currentQuestionIndex, questionArrayList.size()));
    }

    private void fadeAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        binding.questionTextView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextView.setTextColor(Color.WHITE);
                getNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        binding.questionTextView.setText(question);
        updateCounter((ArrayList<Question>) questionList);
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.shake_animation);
        binding.cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextView.setTextColor(Color.WHITE);
                getNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void deductPoints() {

        if (scoreCounter > 0) {
            scoreCounter -= 10;
            score.setScore(scoreCounter);
            binding.scoreTextView.setText(MessageFormat.format("Score: {0}", score.getScore()));
        } else {
            scoreCounter = 0;
            score.setScore(scoreCounter);
        }
    }

    private void addPoints() {
        scoreCounter += 10;
        score.setScore(scoreCounter);
        binding.scoreTextView.setText(MessageFormat.format("Score: {0}", score.getScore()));
    }

    @Override
    protected void onPause() {
        prefs.saveHighestScore(score.getScore());
        prefs.setState(currentQuestionIndex);
        super.onPause();
    }
}