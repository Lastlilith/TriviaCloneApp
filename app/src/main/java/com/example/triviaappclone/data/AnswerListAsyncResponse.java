package com.example.triviaappclone.data;

import com.example.triviaappclone.model.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void processFinished(ArrayList<Question> questionArrayList);
}
