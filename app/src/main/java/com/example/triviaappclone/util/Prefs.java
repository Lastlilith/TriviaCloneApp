package com.example.triviaappclone.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    public static final String HIGHEST_SCORE = "highestScore";
    public static final String TRIVIA_STATE = "triviaState";
    private SharedPreferences preferences;

    public Prefs(Activity context) {
        this.preferences = context.getPreferences(Context.MODE_PRIVATE);
    }

    public void saveHighestScore(int score) {

        int lastScore = preferences.getInt(HIGHEST_SCORE, 0);

        if (score > lastScore) {
            //We have a new highest score
            preferences.edit().putInt(HIGHEST_SCORE, score).apply();
        }
    }

    public int getHighestScore() {
        return preferences.getInt(HIGHEST_SCORE, 0);
    }

    public void setState(int index) {
        preferences.edit().putInt(TRIVIA_STATE, index).apply();
    }

    public int getState() {
       return preferences.getInt(TRIVIA_STATE,0);
    }
}
