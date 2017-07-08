package com.example.administrator.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import com.example.administrator.myapplication.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionDetailActivity extends AppCompatActivity {

    private List<Map<String, String>> groupData;

    private List<List<Map<String, String>>> childrenData;

    private static String[] groupid = {"studentId"};

    private static String[] childrenid = {"detail"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        Bundle argument = getIntent().getExtras();
        String studata = argument.getString("studata");
//        ListView listView = (ListView)findViewById(R.id.student_scores);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                this,
//                android.R.layout.simple_list_item_1,
//                getStringList(studata));
//        listView.setAdapter(adapter);
        addData(studata);
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.student_scores);
        SimpleExpandableListAdapter simpleExpandableListAdapter = new SimpleExpandableListAdapter(this, groupData, android.R.layout.simple_list_item_1, groupid, new int[]{android.R.id.text1}, childrenData, android.R.layout.simple_list_item_1, childrenid, new int[]{android.R.id.text1});
        expandableListView.setAdapter(simpleExpandableListAdapter);
    }

    private void addData(String studata) {
        groupData = new ArrayList<>();
        childrenData = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(studata);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject student = jsonArray.getJSONObject(i);
                Map<String, String> gmap = new HashMap<>();
                gmap.put(groupid[0], "*"+getString(R.string.student_id) + ":" + student.getString(groupid[0]));
                groupData.add(gmap);
                List<Map<String, String>> clist = new ArrayList<>();
                Map<String, String> cmap1 = new HashMap<>();
                cmap1.put(childrenid[0],
                        getString(R.string.student_name) + ":" + student.getString("studentName"));
                Map<String, String> cmap2 = new HashMap<>();
                cmap2.put(childrenid[0],
                        getString(R.string.student_number) + ":" + student.getString("studentNumber"));
                Map<String, String> cmap3 = new HashMap<>();
                cmap3.put(childrenid[0],
                        getString(R.string.student_score) + ":" + student.getString("score"));
                Map<String, String> cmap4 = new HashMap<>();
                if (student.getString("scored").equals("true")) {
                    cmap4.put(childrenid[0], getString(R.string.student_scored) + ":是");
                } else {
                    cmap4.put(childrenid[0], getString(R.string.student_scored) + ":否");
                }
                clist.add(cmap1);
                clist.add(cmap2);
                clist.add(cmap3);
                clist.add(cmap4);
                childrenData.add(clist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private List<String> getStringList(String studata) {
        List<String> result = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(studata);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject student = jsonArray.getJSONObject(i);
                result.add(getString(R.string.student_id) + ":" + student.getString("studentId"));
                result.add(getString(R.string.student_name) + ":" + student.getString("studentName"));
                result.add(getString(R.string.student_number) + ":" + student.getString("studentNumber"));
                result.add(getString(R.string.student_score) + ":" + student.getString("score"));
                if (student.getString("scored").equals("true")) {
                    result.add(getString(R.string.student_scored) + ":是");
                } else {
                    result.add(getString(R.string.student_scored) + ":否");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
