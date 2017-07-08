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
import com.example.administrator.myapplication.activity.AnalysisDetailActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AnalysisStuListActivity extends AppCompatActivity {

    private Activity activity;

    private ListView listView;

    private GetStudentListTask getStudentListTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_analysis_stu_list);
        listView = (ListView)findViewById(R.id.analysis_stulist);
        Bundle argument = getIntent().getExtras();
        getStudentListTask = new GetStudentListTask(argument.getString("username"), argument.getString("password"), argument.getString("assignmentId"));
        getStudentListTask.execute();
    }

    class GetStudentListTask extends AsyncTask<Void, Void, Boolean> {

        private String username;

        private String password;

        private String assignmentId;

        private String stulistdata;

        private List<Map<String, String>> studentids;

        GetStudentListTask(String username, String password, String assignmentId) {
            this.username = username;
            this.password = password;
            this.assignmentId = assignmentId;
            this.studentids = new ArrayList<>();
            this.stulistdata = "";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS).build();
            String url = StaticData.BASE_URl + "/group";
            String token = "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
            Request request = new Request.Builder().addHeader("Authorization", token).url(url).build();

            try {
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                if (result == "") {
                    return false;
                }
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject;
                String groupid = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.getString("name").equals(StaticData.AVAILABLE_GROUP_NAME)) {
                        groupid = jsonObject.getString("id");
                        break;
                    }
                }
                url += "/" + groupid + "/students";
                request = new Request.Builder().addHeader("Authorization", token).url(url).build();
                response = client.newCall(request).execute();
                result = response.body().string();
                if (result != "") {
                    stulistdata = result;
                    return true;
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            getStudentListTask = null;
            if (success) {
                try {
                    JSONArray jsonArray = new JSONArray(stulistdata);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Map<String, String> map = new HashMap<>();
                        map.put("id", jsonObject.getString("id"));
                        map.put("name", jsonObject.getString("name"));
                        studentids.add(map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SimpleAdapter adapter = new SimpleAdapter(activity, studentids, android.R.layout.simple_list_item_2, new String[]{"id", "name"}, new int[]{android.R.id.text1, android.R.id.text2});
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(activity, AnalysisDetailActivity.class);
                        Bundle args = new Bundle();
                        args.putString("username", username);
                        args.putString("password", password);
                        args.putString("assignmentId", assignmentId);
                        args.putString("stuid", studentids.get(position).get("id"));
                        intent.putExtras(args);
                        activity.startActivity(intent);
                    }
                });
            } else {

            }
        }
    }
}
