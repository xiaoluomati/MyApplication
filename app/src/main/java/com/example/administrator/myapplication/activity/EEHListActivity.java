package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.StaticData;
import com.example.administrator.myapplication.activity.EEHDetailActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EEHListActivity extends AppCompatActivity {

    private Activity activity;

    private GetExamListTask getListTask;

    private ListView listView;

    private List<String> ids;

    private List<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_exam_list);
        Bundle argument = getIntent().getExtras();
        String typeString = argument.getString("type");
        setTitle(getTypeString(typeString) + getString(R.string.list_name));
        listView = (ListView)findViewById(R.id.exam_list);
        ids = new ArrayList<>();
        datas = new ArrayList<>();

        getListTask = new GetExamListTask(argument.getString("username"), argument.getString("password"), typeString);
        getListTask.execute();
    }

    private String getTypeString(String type){
        switch (type){
            case "exam":return getString(R.string.exam_id);
            case "homework":return getString(R.string.homework_id);
            case "exercise":return getString(R.string.exercise_id);
        }
        return "";
    }

    class GetExamListTask extends AsyncTask<Void, Void, Boolean> {

        private String username;

        private String password;

        private String type;

        private String listdata;

        GetExamListTask(String username, String password, String type) {
            this.username = username;
            this.password = password;
            this.type = type;
            this.listdata = "";
        }



        @Override
        protected Boolean doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS).build();
            String url = StaticData.BASE_URl + "/course/"+StaticData.COURSE_ID+"/" +type;
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
             getListTask = null;
            if (success) {
                try {
                    JSONArray jsonArray = new JSONArray(listdata);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        ids.add(getTypeString(type)+":"+jsonObject.getString("id"));
                        datas.add(jsonObject.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        activity,
                        android.R.layout.simple_list_item_1,
                        ids);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(activity, EEHDetailActivity.class);
                        Bundle args = new Bundle();
                        args.putString("data", datas.get(position));
                        args.putString("type", type);
                        intent.putExtras(args);
                        activity.startActivity(intent);
                    }
                });
            } else {

            }
        }
    }
}
