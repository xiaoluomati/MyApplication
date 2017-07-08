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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class GroupListActivity extends AppCompatActivity {

    private GetStudentListTask getStudentListTask;

    private ArrayList<Group> groups = new ArrayList<>();

    private ListView listView;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_group_list);
        listView = (ListView) findViewById(R.id.group_list);
        Bundle argument = getIntent().getExtras();
        getStudentListTask = new GetStudentListTask(argument.getString("username"), argument.getString("password"));
        getStudentListTask.execute();
    }

    class Group{
        String id;

        String name;

        Group(String id, String name){
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    class GetStudentListTask extends AsyncTask<Void, Void, Boolean>{

        private String username;

        private String password;

        private String stulistdata;

        GetStudentListTask(String username, String password) {
            this.username = username;
            this.password = password;
            this.stulistdata = "";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS).build();
            String url = StaticData.BASE_URl + "/group";
            String token = "Basic " + Base64.encodeToString((username+":"+password).getBytes(), Base64.NO_WRAP);

            Request request = new Request.Builder().addHeader("Authorization", token).url(url).build();
            try {
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                if(result != ""){
                    stulistdata = result;
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
            getStudentListTask = null;
            if (success) {
                try {
                    JSONArray jsonArray = new JSONArray(stulistdata);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        groups.add(new Group(jsonObject.getString("id"), jsonObject.getString("name")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ArrayAdapter<Group> adapter = new ArrayAdapter<>(
                        activity,
                        android.R.layout.simple_list_item_1,
                        groups);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(activity, StudentDetailActivity.class);
                        Bundle args = new Bundle();
                        args.putString("username", username);
                        args.putString("password", password);
                        args.putString("groupid", groups.get(position).id);
                        intent.putExtras(args);
                        activity.startActivity(intent);
                    }
                });
            } else {

            }
        }
    }
}
