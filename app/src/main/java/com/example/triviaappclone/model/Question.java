package com.example.triviaappclone.model;

import androidx.annotation.NonNull;

public class Question {
    private String answer;
    private boolean isAnswerTrue;

    public Question() {
    }

    public Question(String answer, boolean isAnswerTrue) {
        this.answer = answer;
        this.isAnswerTrue = isAnswerTrue;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isAnswerTrue() {
        return isAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        isAnswerTrue = answerTrue;
    }

    @NonNull
    @Override
    public String toString() {
        return "Question{" +
                "answer='" + answer + '\'' +
                ", answerTrue=" + isAnswerTrue +
                '}';
    }
}
