package com.example.triviaappclone.data;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.triviaappclone.controller.AppController;
import com.example.triviaappclone.model.Question;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    ArrayList<Question> questionArrayList = new ArrayList<>();

    String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestions(final AnswerListAsyncResponse callBack) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Question question = new Question(response.getJSONArray(i).get(0).toString(),
                                    response.getJSONArray(i).getBoolean(1) );

                            //Add questions to arrayList

                            questionArrayList.add(question);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if(null != callBack) callBack.processFinished(questionArrayList);

                }, error -> {

        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionArrayList;
    }
}
