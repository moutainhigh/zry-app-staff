package com.zhongmei.beauty.widgets;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by demo on 2018/12/15
 * 排班设置（看板单元格）
 */
public class SchedulingView extends LinearLayout {

    private TextView tv_workNo;

    private TextView tv_workTime;

    private DrawableCenterTextView tv_rest;

    public SchedulingView(Context context) {
        this(context, null);
    }

    public SchedulingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SchedulingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SchedulingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    protected void init() {
        LinearLayout.inflate(getContext(), R.layout.beauty_view_scheduling_cell, this);
        tv_workNo = (TextView) findViewById(R.id.tv_work_no);
        tv_workTime = (TextView) findViewById(R.id.tv_work_time);
        tv_rest = (DrawableCenterTextView) findViewById(R.id.tv_rest);
    }

    public void setScheduling(String scheduling) {
        if (tv_workNo != null) {
            tv_workNo.setText(scheduling);
        }
    }

    public void setTime(String timeStance) {
        if (tv_workTime != null) {
            tv_workTime.setText(timeStance);
        }
    }
}
