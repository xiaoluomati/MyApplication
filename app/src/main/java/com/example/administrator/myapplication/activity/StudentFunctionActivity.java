package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.activity.AnalysisListActivity;
import com.example.administrator.myapplication.activity.EEHListActivity;

import java.util.HashMap;

public class StudentFunctionActivity extends AppCompatActivity {

    private Activity activity;

    private static HashMap<Integer, String> typeMap ;

    static {
        typeMap = new HashMap<>();
        typeMap.put(R.id.button6, "exam");
        typeMap.put(R.id.button7, "homework");
        typeMap.put(R.id.button8, "exercise");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_student_function);

        final Bundle argument = getIntent().getExtras();
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

        Button checkAnalysisButton = (Button)findViewById(R.id.button9);
        checkAnalysisButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AnalysisListActivity.class);
                intent.putExtras(argument);
                activity.startActivity(intent);
            }
        });
    }
}
