package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.StaticData;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StudentDetailActivity extends AppCompatActivity {


    private ArrayList<String> studentNames = new ArrayList<>();

    private ArrayList<Student> students = new ArrayList<>();

    private ExpandableListView expandableListView;

    private GetStuDetailTask getStuDetailTask;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_student_detail);
        expandableListView = (ExpandableListView) findViewById(R.id.student_detail_list);
        Bundle args = getIntent().getExtras();
        getStuDetailTask = new GetStuDetailTask(args.getString("username"), args.getString("password"), args.getString("groupid"));
        getStuDetailTask.execute();
    }

    class Student{
        static final int NUMBER_OF_ATTR = 6;
        String id;
        String username;
        String gender;
        String schoolId;
        String gitId;
        String gitUsername;

        String getByPosition(int position){
            List<String> attrs = new ArrayList<>();
            attrs.add(id);
            attrs.add(username);
            attrs.add(gender);
            attrs.add(schoolId);
            attrs.add(gitId);
            attrs.add(gitUsername);
            return attrs.get(position);
        }
    }

    class GetStuDetailTask extends AsyncTask<Void, Void, Boolean>{

        private String username;

        private String password;

        private String groupid;

        private String studetaildata;

        public GetStuDetailTask(String username, String password, String groupid) {
            this.username = username;
            this.password = password;
            this.groupid = groupid;
            this.studetaildata = "";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS).build();
            String url = StaticData.BASE_URl + "/group/" + groupid + "/students";
            String token = "Basic " + Base64.encodeToString((username+":"+password).getBytes(), Base64.NO_WRAP);

            Request request = new Request.Builder().addHeader("Authorization", token).url(url).build();
            try {
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                if(result != ""){
                    studetaildata = result;
                    return true;
                }
                return false;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(final Boolean success) {
            getStuDetailTask = null;
            if (success) {
                try {
                    JSONArray jsonArray = new JSONArray(studetaildata);
                    JSONObject jsonObject;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        studentNames.add(jsonObject.getString("name"));
                        Student student = new Student();
                        student.id = getString(R.string.student_id) + ":" +jsonObject.getString("id");
                        student.gender = getString(R.string.student_gender) + ":" + jsonObject.getString("gender");
                        student.gitId = getString(R.string.student_gitId) + ":" + jsonObject.getString("gitId");
                        student.gitUsername = getString(R.string.student_gitUsername) + ":" + jsonObject.getString("gitUsername");
                        student.username = getString(R.string.student_username) + ":" + jsonObject.getString("username");
                        student.schoolId = getString(R.string.student_schoolId) + ":" + jsonObject.getString("schoolId");
                        students.add(student);
                    }
                    MyExpandableListAdapter myExpandableListAdapter = new MyExpandableListAdapter(studentNames, students, activity);
                    expandableListView.setAdapter(myExpandableListAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MyExpandableListAdapter extends BaseExpandableListAdapter{

        private List<String> stuNames;

        private List<Student> students;

        private Activity activity;

        public MyExpandableListAdapter(List<String> stuNames, List<Student> students, Activity activity) {
            this.stuNames = stuNames;
            this.students = students;
            this.activity = activity;
        }

        @Override
        public int getGroupCount() {
            return stuNames.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return Student.NUMBER_OF_ATTR;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return students.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return students.get(groupPosition).getByPosition(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            return getGenericView(stuNames.get(groupPosition));
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            return getGenericView((String)getChild(groupPosition, childPosition));
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
        private TextView getGenericView(String string )
        {
            AbsListView.LayoutParams  layoutParams =new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            TextView  textView =new TextView(activity);
            textView.setLayoutParams(layoutParams);

            textView.setGravity(Gravity.CENTER_VERTICAL |Gravity.LEFT);

            textView.setPadding(64, 16, 16, 0);
            textView.setText(string);
            return textView;
        }
    }
}
