package com.zhongmei.beauty.widgets;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.booking.bean.BookingTradeItemVo;
import com.zhongmei.bty.commonmodule.util.DateUtil;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.TaskRemind;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.List;


@EViewGroup(R.layout.layout_doc_details_task_view)
public class DocDetailsTaskView extends RelativeLayout  {


    @ViewById(R.id.tv_task_index)
    protected TextView tv_taskIndex;


    @ViewById(R.id.tv_title)
    protected TextView tv_title;

    @ViewById(R.id.tv_content)
    protected TextView tv_content;

    @ViewById(R.id.tv_remind_time)
    protected TextView tv_remindTime;

    @ViewById(R.id.layout_result)
    protected LinearLayout layout_result;

    @ViewById(R.id.tv_result)
    protected TextView tv_result;

    private TaskRemind mTask;


    @AfterViews
    public void init() {

    }

    public void refreshUI(TaskRemind task,int index) {
        mTask = task;
                tv_taskIndex.setText("提醒任务 "+index);
        tv_title.setText(mTask.getTitle()+getTaskStatus(mTask.getStatus()));
        tv_content.setText(mTask.getContent());
        tv_remindTime.setText(DateUtil.format(mTask.getRemindTime()));

        if(mTask.getStatus()==2){
            layout_result.setVisibility(View.VISIBLE);
            tv_result.setText(mTask.getTaskResult());
        }

    }

    private String getTaskStatus(int status){
        StringBuffer statusBuf=new StringBuffer("(");
        switch(status){
            case 1:
                statusBuf.append("待执行");
                break;
            case 2:
                statusBuf.append("已执行");
                break;
                default:
                    statusBuf.append("未知状态");
                    break;
        }
        statusBuf.append(")");

        return statusBuf.toString().trim();
    }


    public DocDetailsTaskView(Context context) {
        super(context);
    }

    public DocDetailsTaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DocDetailsTaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
