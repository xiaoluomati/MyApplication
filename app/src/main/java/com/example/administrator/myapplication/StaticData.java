package com.example.administrator.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuding on 2017/6/30.
 */
public class StaticData {

    private StaticData(){}

    public static final String BASE_URl = "http://115.29.184.56:8090/api";

    public static final String COURSE_ID = "2";

    public static final String AVAILABLE_GROUP_NAME = "有考试数据的班级";

    public static final Map<String, Integer> EXAM_STATUS;

    static {
        EXAM_STATUS = new HashMap<>();
        EXAM_STATUS.put("newly", R.string.exam_status_newly);
        EXAM_STATUS.put("initing", R.string.exam_status_initing);
        EXAM_STATUS.put("analyzing", R.string.exam_status_analyzing);
        EXAM_STATUS.put("analyzingFinish", R.string.exam_status_analyzingFinish);
        EXAM_STATUS.put("initFail", R.string.exam_status_initFail);
        EXAM_STATUS.put("initSuccess", R.string.exam_status_initSuccess);
        EXAM_STATUS.put("ongoing", R.string.exam_status_ongoing);
        EXAM_STATUS.put("timeup", R.string.exam_status_timeup);
    }

    public static final List<String> AVAILABLE_ASSIGNMENT_IDS;

    static {
        AVAILABLE_ASSIGNMENT_IDS = new ArrayList<>();
        AVAILABLE_ASSIGNMENT_IDS.add("38");
        AVAILABLE_ASSIGNMENT_IDS.add("93");
    }
}
