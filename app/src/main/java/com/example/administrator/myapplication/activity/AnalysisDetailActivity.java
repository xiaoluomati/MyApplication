package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.StaticData;
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

public class AnalysisDetailActivity extends AppCompatActivity {

    private Activity activity;

    private GetAnalysisDetailTask getAnalysisDetailTask;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_analysis_detail);
        listView = (ListView)findViewById(R.id.analysis_detail_list);
        Bundle args = getIntent().getExtras();
        getAnalysisDetailTask = new GetAnalysisDetailTask(args.getString("username"), args.getString("password"), args.getString("assignmentId"), args.getString("stuid"));
        getAnalysisDetailTask.execute();
    }

    class GetAnalysisDetailTask extends AsyncTask<Void, Void, Boolean> {

        private String username;

        private String password;

        private String assignmentId;

        private String studentId;

        private List<Map<String, String>> analysisDetailList;

        private String analysisData;

        GetAnalysisDetailTask(String username, String password, String assignmentId, String studentId) {
            this.username = username;
            this.password = password;
            this.assignmentId = assignmentId;
            this.studentId = studentId;
            this.analysisDetailList = new ArrayList<>();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS).build();
            String url = StaticData.BASE_URl + "/assignment/"+assignmentId+"/student/"+studentId+"/analysis";
            String token = "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
            Request request = new Request.Builder().addHeader("Authorization", token).url(url).build();

            try {
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                if (result != "") {
                    analysisData = result;
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
            getAnalysisDetailTask = null;
            if (success) {
                try {
                    Log.d("ss", analysisData);
                    JSONObject jsonObject = new JSONObject(analysisData);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("questionResults"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Map<String, String> quesMap = new HashMap<>();
                        quesMap.put("name", getString(R.string.question_title) + ":" +jsonObject.getString("questionTitle"));
                        quesMap.put("detail", getString(R.string.question_id) + ":" + jsonObject.getString("questionId"));
                        analysisDetailList.add(quesMap);

                        JSONObject scoredResult = new JSONObject(jsonObject.getString("scoreResult"));
                        Map<String, String> scoreMap1 = new HashMap<>();
                        scoreMap1.put("name", getString(R.string.student_score)+":"+scoredResult.getString("score"));
                        if(scoredResult.getString("scored").equals("true"))
                            scoreMap1.put("detail", getString(R.string.student_scored)+":是");
                        else
                            scoreMap1.put("detail", getString(R.string.student_scored)+":否");
                        analysisDetailList.add(scoreMap1);
                        Map<String, String> giturlMap = new HashMap<>();
                        giturlMap.put("name", getString(R.string.question_gitUrl) + ":" + scoredResult.getString("git_url"));
                        giturlMap.put("detail", "");
                        analysisDetailList.add(giturlMap);

                        JSONObject testResult = new JSONObject(jsonObject.getString("testResult"));
                        Map<String, String> compileMap = new HashMap<>();
                        compileMap.put("detail", "");
                        if(testResult.getString("compile_succeeded").equals("true"))
                            compileMap.put("name", getString(R.string.compile_succeeded)+":是");
                        else
                            compileMap.put("name", getString(R.string.compile_succeeded)+":否");
                        analysisDetailList.add(compileMap);
                        Map<String, String> testMap = new HashMap<>();
                        testMap.put("detail", "");
                        if(testResult.getString("tested").equals("true"))
                            testMap.put("name", getString(R.string.tested)+":是");
                        else
                            testMap.put("name", getString(R.string.tested)+":否");
                        analysisDetailList.add(testMap);
                        JSONArray testcaseArray = new JSONArray(testResult.getString("testcases"));
                        for (int j = 0; j<testcaseArray.length(); j++){
                            JSONObject object = testcaseArray.getJSONObject(i);
                            Map<String, String> testcaseMap = new HashMap<>();
                            testcaseMap.put("name", getString(R.string.testcase_name)+":"+object.getString("name"));
                            if(object.getString("passed").equals("true"))
                                testcaseMap.put("detail", getString(R.string.testcase_ispassed)+":是");
                            else
                                testcaseMap.put("detail", getString(R.string.testcase_ispassed)+":否");
                            analysisDetailList.add(testcaseMap);
                        }

                        JSONObject metricData = new JSONObject(jsonObject.getString("metricData"));
                        Map<String, String> totalcountMap = new HashMap<>();
                        totalcountMap.put("name", getString(R.string.total_line_count)+":"+metricData.getString("total_line_count"));
                        totalcountMap.put("detail", "");
                        analysisDetailList.add(totalcountMap);
                        Map<String, String> commentcountMap = new HashMap<>();
                        commentcountMap.put("name", getString(R.string.comment_line_count)+":"+metricData.getString("comment_line_count"));
                        commentcountMap.put("detail", "");
                        analysisDetailList.add(commentcountMap);
                        Map<String, String> fieldcountMap = new HashMap<>();
                        fieldcountMap.put("name", getString(R.string.field_count)+":"+metricData.getString("field_count"));
                        fieldcountMap.put("detail", "");
                        analysisDetailList.add(fieldcountMap);
                        Map<String, String> methodcountMap = new HashMap<>();
                        methodcountMap.put("name", getString(R.string.method_count)+":"+metricData.getString("method_count"));
                        methodcountMap.put("detail", "");
                        analysisDetailList.add(methodcountMap);
                        Map<String, String> maxcocMap = new HashMap<>();
                        maxcocMap.put("name", "max_coc:"+metricData.getString("max_coc"));
                        maxcocMap.put("detail", "");
                        analysisDetailList.add(maxcocMap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SimpleAdapter adapter = new SimpleAdapter(activity, analysisDetailList, android.R.layout.simple_list_item_2, new String[]{"name", "detail"}, new int[]{android.R.id.text1, android.R.id.text2});
                listView.setAdapter(adapter);
            } else {

            }
        }
    }
}
