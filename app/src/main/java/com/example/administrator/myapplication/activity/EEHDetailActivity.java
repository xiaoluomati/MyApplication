package com.example.administrator.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.StaticData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EEHDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_detail);
        Bundle argument = getIntent().getExtras();
        String typeString = argument.getString("type");
        setTitle(getTypeString(typeString));
        String data = argument.getString("data");
        ListView listView = (ListView)findViewById(R.id.exam_detail);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                getStrings(data));
        listView.setAdapter(adapter);
    }

    private String getTypeString(String type){
        switch (type){
            case "exam":return getString(R.string.exam_detail);
            case "homework":return getString(R.string.homework_detail);
            case "exercise":return getString(R.string.exam_detail);
        }
        return "";
    }

    private List<String> getStrings(String data){
        List<String> datas = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            datas.add(getString(R.string.exam_title)+":"+jsonObject.getString("title"));
            datas.add(getString(R.string.exam_description)+":"+jsonObject.getString("description"));
            datas.add(getString(R.string.exam_startAt)+":"+jsonObject.getString("startAt"));
            datas.add(getString(R.string.exam_endAt)+":"+jsonObject.getString("endAt"));
            datas.add(getString(R.string.exam_course)+":"+jsonObject.getString("course"));
            datas.add(getString(R.string.exam_status)+":"+getString(StaticData.EXAM_STATUS.get(jsonObject.getString("status"))));
            JSONArray questions = new JSONArray(jsonObject.getString("questions"));
            for (int i = 0; i < questions.length(); i++) {
                JSONObject question = questions.getJSONObject(i);
                datas.add(getString(R.string.question_id)+":"+question.getString("id"));
                datas.add(getString(R.string.question_title)+":"+question.getString("title"));
                datas.add(getString(R.string.question_description)+":"+question.getString("description"));
                datas.add(getString(R.string.question_difficulty)+":"+question.getString("difficulty"));
                datas.add(getString(R.string.question_gitUrl)+":"+question.getString("gitUrl"));
                JSONObject creator = new JSONObject(question.getString("creator"));
                datas.add(getString(R.string.question_creator_name)+":"+creator.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datas;
    }
}
