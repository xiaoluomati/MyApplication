package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.StaticData;
import com.example.administrator.myapplication.activity.QuestionDetailActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ScoreDetailActivity extends AppCompatActivity {

    private GetDetailTask getDetailTask;

    private Activity activity;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_score_detail);
        listView = (ListView)findViewById(R.id.assignment_detail);
        Bundle argument = getIntent().getExtras();
        getDetailTask = new GetDetailTask(argument.getString("username"), argument.getString("password"), argument.getString("assignmentId"));
        getDetailTask.execute();
    }

    class GetDetailTask extends AsyncTask<Void, Void, Boolean>{

        private String username;

        private String password;

        private String assignmentId;

        private String listdata;

        private List<HashMap<String, String>> questionDetailMap;

        private List<String> questionStu;

        GetDetailTask(String username, String password, String assignmentId) {
            this.username = username;
            this.password = password;
            this.assignmentId = assignmentId;
            this.listdata = "";
            this.questionDetailMap = new ArrayList<>();
            this.questionStu = new ArrayList<>();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS).build();
            String url = StaticData.BASE_URl + "/assignment/"+assignmentId+"/score";
            String token = "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);

            Request request = new Request.Builder().addHeader("Authorization", token).url(url).build();
            try {
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                if (result != "") {
                    listdata = result;
                    return true;
                }
                return false;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            getDetailTask = null;
            if (success) {
                try {
                    JSONObject jsonObject = new JSONObject(listdata);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("questions"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject ques = jsonArray.getJSONObject(i);
                        JSONObject quesDetail = new JSONObject(ques.getString("questionInfo"));
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", getString(R.string.question_id) + quesDetail.getString("id"));
                        map.put("title", getString(R.string.question_title) + quesDetail.getString("title"));
                        questionDetailMap.add(map);
                        questionStu.add(ques.getString("students"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SimpleAdapter adapter = new SimpleAdapter(activity, questionDetailMap, android.R.layout.simple_list_item_2, new String[] {"id", "title"}, new int[] {android.R.id.text1, android.R.id.text2});
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(activity, QuestionDetailActivity.class);
                        Bundle args = new Bundle();
                        args.putString("studata", questionStu.get(position));
                        intent.putExtras(args);
                        activity.startActivity(intent);
                    }
                });
            } else {

            }
        }
    }
}
