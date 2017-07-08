package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.activity.EEHListActivity;
import com.example.administrator.myapplication.activity.GroupListActivity;
import com.example.administrator.myapplication.activity.ScoreListActivity;

import java.util.HashMap;

public class TeacherFunctionsActivity extends AppCompatActivity {

    private Activity activity;

    private static HashMap<Integer, String> typeMap ;

    static {
        typeMap = new HashMap<>();
        typeMap.put(R.id.button2, "exam");
        typeMap.put(R.id.button3, "homework");
        typeMap.put(R.id.button4, "exercise");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_teacher_functions);
        final Bundle argument = getIntent().getExtras();
        Button checkStuListButton = (Button)findViewById(R.id.button1);

        checkStuListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, GroupListActivity.class);
                intent.putExtras(argument);
                activity.startActivity(intent);
            }
        });

        for (final Integer id : typeMap.keySet()) {
            Button button = (Button)findViewById(id);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, EEHListActivity.class);
                    argument.putString("type", typeMap.get(id));
                    intent.putExtras(argument);
                    activity.startActivity(intent);
                }
            });
        }

        Button checkScoreButton = (Button)findViewById(R.id.button5);
        checkScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ScoreListActivity.class);
                intent.putExtras(argument);
                activity.startActivity(intent);
            }
        });
    }
}
