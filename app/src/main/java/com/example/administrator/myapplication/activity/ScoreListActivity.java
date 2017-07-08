package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.StaticData;
import com.example.administrator.myapplication.activity.ScoreDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class ScoreListActivity extends AppCompatActivity {

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_score_list);
        final Bundle argument = getIntent().getExtras();
        ListView listView = (ListView)findViewById(R.id.assignment_list);
        List<String> assignmentids = new ArrayList<>();
        for (String availableAssignmentId : StaticData.AVAILABLE_ASSIGNMENT_IDS) {
            assignmentids.add("assignment " + availableAssignmentId);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                assignmentids);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, ScoreDetailActivity.class);
                Bundle args = new Bundle();
                args.putString("username", argument.getString("username"));
                args.putString("password", argument.getString("password"));
                args.putString("assignmentId", StaticData.AVAILABLE_ASSIGNMENT_IDS.get(position));
                intent.putExtras(args);
                activity.startActivity(intent);
            }
        });
    }
}
