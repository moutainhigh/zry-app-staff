package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.beauty.booking.bean.BeautyBookingVo;
import com.zhongmei.bty.basemodule.booking.bean.BookingTradeItemVo;
import com.zhongmei.bty.commonmodule.util.DateUtil;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.TaskRemind;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.yunfu.db.enums.Sex;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
@EViewGroup(R.layout.beauty_task_item)
public class TaskItemView extends RelativeLayout implements View.OnClickListener {


    @ViewById(R.id.tv_user_name)
    protected TextView tv_userName;


    @ViewById(R.id.tv_title)
    protected TextView tv_title;

    @ViewById(R.id.tv_remind_time)
    protected TextView tv_remindTime;

    @ViewById(R.id.tv_member_info)
    protected TextView tv_memberInfo;

    @ViewById(R.id.tv_details)
    protected TextView tv_details;//任务详情

    @ViewById(R.id.btn_scan)
    protected Button btn_scan;

    @ViewById(R.id.btn_modify)
    protected Button btn_modify;

    @ViewById(R.id.btn_execute)
    protected Button btn_execute;

    private OnOperateListener mOperateListener;

    private TaskRemind mTask;

    public void setmOperateListener(OnOperateListener mOperateListener) {
        this.mOperateListener = mOperateListener;
    }

    @AfterViews
    public void init() {
        btn_scan.setOnClickListener(this);
        btn_modify.setOnClickListener(this);
        btn_execute.setOnClickListener(this);
    }

    public void refreshUI(TaskRemind task) {
        mTask = task;
        //设置UI信息
        tv_userName.setText(mTask.getUserName());
        tv_title.setText(mTask.getTitle());
        tv_remindTime.setText(DateUtil.format(mTask.getRemindTime()));
        tv_memberInfo.setText(getMemberInfo(mTask));
        tv_details.setText(task.getContent());

        if(mTask.getStatus()==1){
            btn_modify.setVisibility(View.VISIBLE);
            btn_execute.setVisibility(View.VISIBLE);
            btn_scan.setVisibility(View.GONE);
        }else{
            btn_modify.setVisibility(View.GONE);
            btn_execute.setVisibility(View.GONE);
            btn_scan.setVisibility(View.VISIBLE);
        }

    }




  private String getMemberInfo(TaskRemind task){
        if(task==null || task.getCustomerId()==null){
            return "暂无";
        }

        StringBuffer buffer=new StringBuffer();
        buffer.append(task.getCustomerName());

        if(!TextUtils.isEmpty(task.getCustomerName())){
            buffer.append("(");
            buffer.append(task.getCustomerMobile());
            buffer.append(")");
        }

        return buffer.toString();
  }

    private String getTradeItemInfo(List<BookingTradeItemVo> tradeItemVos) {
        if (Utils.isEmpty(tradeItemVos)) {
            return getContext().getResources().getString(R.string.beauty_no_service);
        }

        int itemLen = tradeItemVos.size() > 3 ? 3 : tradeItemVos.size();

        StringBuffer itemNameBuffer = new StringBuffer();
        for (int i = 0; i < itemLen; i++) {
            BookingTradeItem vo = tradeItemVos.get(i).getTradeItem();
            itemNameBuffer.append(vo.getDishName());
            itemNameBuffer.append("x" + vo.getQuantity());
            itemNameBuffer.append(",\n");
        }

        return itemNameBuffer.substring(0, itemNameBuffer.length() - 2);
    }


    public TaskItemView(Context context) {
        super(context);
    }

    public TaskItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TaskItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_modify:
                if (mOperateListener != null) {
                    mOperateListener.taskModify(mTask);
                }
                break;
            case R.id.btn_execute:
                if (mOperateListener != null) {
                    mOperateListener.taskException(mTask);
                }
                break;
            case R.id.btn_scan:
                if (mOperateListener != null) {
                    mOperateListener.taskScan(mTask);
                }
                break;
        }
    }

    public interface OnOperateListener {
        void taskScan(TaskRemind task);

        void taskModify(TaskRemind task);

        void taskException(TaskRemind task);
    }
}
