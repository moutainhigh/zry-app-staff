package com.zhongmei.beauty;

import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.aspsine.swipetoloadlayout.SwipeRefreshHeaderLayout;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.zhongmei.beauty.adapter.TaskAdapter;
import com.zhongmei.beauty.adapter.TaskItemView;
import com.zhongmei.beauty.booking.groundrecycleradapter.GroupItemDecoration;
import com.zhongmei.beauty.customer.BeautyCustomerBalanceFragment;
import com.zhongmei.beauty.dialog.BeautyCreateOrEditMemberDocDialog;
import com.zhongmei.beauty.dialog.BeautyCreateOrEditTaskDialog;
import com.zhongmei.beauty.dialog.BeautyResultTaskDialog;
import com.zhongmei.beauty.operates.BeautyCustomerOperates;
import com.zhongmei.bty.basemodule.customer.bean.CustomerDocRecordReq;
import com.zhongmei.bty.basemodule.customer.bean.CustomerDocRecordResp;
import com.zhongmei.bty.basemodule.customer.bean.CustomerDocReq;
import com.zhongmei.bty.basemodule.customer.bean.TaskCreateOrEditReq;
import com.zhongmei.bty.basemodule.customer.bean.TaskQueryReq;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.view.calendar.CalendarDialog;
import com.zhongmei.bty.commonmodule.view.calendar.CalendarView;
import com.zhongmei.bty.customer.adapter.CustomerDocRecordAdapter;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.bean.YFResponseList;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.data.LoadingYFResponseListener;
import com.zhongmei.yunfu.db.entity.TaskRemind;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.YFResponseListener;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Date;


@EFragment(R.layout.beauty_task_fragment)
public class BeautyTaskFragment extends BasicFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, OnRefreshListener, OnLoadMoreListener, BeautyCreateOrEditTaskDialog.TaskOperatorLisnter, TaskItemView.OnOperateListener, BeautyResultTaskDialog.CallBackListener {


    @ViewById(R.id.btn_create_task)
    protected Button btn_createTask;

    @ViewById(R.id.rg_status)
    protected RadioGroup rg_status;
    @ViewById(R.id.rg_date)
    protected RadioGroup rg_date;

    @ViewById(R.id.rb_custom_date)
    protected RadioButton rb_customDate;
    @ViewById(R.id.tv_custom_date)
    protected TextView tv_customDate;
    @ViewById(R.id.swipeToLoadLayout)
    protected SwipeToLoadLayout stl_refreshDataLayout;
    @ViewById(R.id.swipe_refresh_header)
    protected SwipeRefreshHeaderLayout stl_refreshHeaderLayout;
    @ViewById(R.id.swipe_target)
    protected RecyclerView lv_task;
    @ViewById(R.id.swipe_load_more_footer)
    protected SwipeLoadMoreFooterLayout stl_refreshFooterLayout;

    @ViewById(R.id.iv_empty_view)
    protected TextView iv_emptyView;

    private Date selectedDate;

    private CalendarDialog calendarDialog;
    private int taskStatus = 1;
    private TaskQueryReq taskQueryReq = new TaskQueryReq();

    @Bean
    protected TaskAdapter mTaskAdapter;

    private int totalPageSize = 1;

    BeautyCreateOrEditTaskDialog dialog;

    @AfterViews
    public void initView() {
        mTaskAdapter.setOperateListener(this);
        lv_task.setLayoutManager(new LinearLayoutManager(getContext()));
        lv_task.setAdapter(mTaskAdapter);

        stl_refreshDataLayout.setOnRefreshListener(this);
        stl_refreshDataLayout.setOnLoadMoreListener(this);

        btn_createTask.setOnClickListener(this);
        tv_customDate.setOnClickListener(this);
        rb_customDate.setOnClickListener(this);

        rg_status.setOnCheckedChangeListener(this);
        rg_date.setOnCheckedChangeListener(this);

        selectedDate = getDate(0);
        taskQueryReq.setPageNo(1);
        taskQueryReq.setPageSize(20);
        taskQueryReq.setRemindTime(selectedDate.getTime());
        taskQueryReq.setStatus(taskStatus);
        taskQueryReq.setType(1);
        taskQueryReq.setUserId(Session.getAuthUser().getId());//设置userid
        requestTasks(false);
    }

    private Date getDate(int afterDay) {
        Long curTime = System.currentTimeMillis();
        Long tarTime = curTime + (afterDay * 24 * 60 * 60 * 1000);
        return new Date(tarTime);
    }


    private void setCustomDate(Date date) {
        if (date == null) {
            String custom = getResources().getString(com.zhongmei.yunfu.beauty.R.string.beauty_custom);
            tv_customDate.setText("(" + custom + ")");
            return;
        }

        String currentTime = DateTimeUtils.formatDate(date);
        tv_customDate.setText("(" + currentTime + ")");

    }


    private void showCalendarDialog() {
        calendarDialog = new CalendarDialog(getActivity(), listner);
        calendarDialog.setDefaultSelected(selectedDate);
        calendarDialog.showDialog();
    }

    CalendarView.OnItemClickListener listner = new CalendarView.OnItemClickListener() {

        @Override
        public void OnItemClick(Date downDate) {
            selectedDate = downDate;
            setCustomDate(downDate);
            calendarDialog.dismiss();
            taskQueryReq.setRemindTime(selectedDate.getTime());
            requestTasks(true);
        }
    };

    private void requestTasks(boolean isClearData) {
        if (isClearData && mTaskAdapter != null) {
            iv_emptyView.setVisibility(View.VISIBLE);
            mTaskAdapter.getItems().clear();
            mTaskAdapter.notifyDataSetChanged();
        }

        YFResponseListener listener = LoadingYFResponseListener.ensure(new YFResponseListener<YFResponseList<TaskRemind>>() {

            @Override
            public void onResponse(YFResponseList<TaskRemind> response) {
                if (YFResponseList.isOk(response)) {
                    mTaskAdapter.getItems().addAll(response.getContent());
                    mTaskAdapter.notifyDataSetChanged();
                    totalPageSize = (int) response.getPage().getPageCount();
                } else {
                    ToastUtil.showShortToast(response.getMessage());
                }

                if (Utils.isNotEmpty(mTaskAdapter.getItems())) {
                    iv_emptyView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        }, getFragmentManager());

        BeautyCustomerOperates operates = OperatesFactory.create(BeautyCustomerOperates.class);
        operates.getTaskList(taskQueryReq, listener);
    }

    private void showCreateDocDialog(TaskRemind task, int type) {
        dialog = new BeautyCreateOrEditTaskDialog();
        dialog.setTaskOperatorListener(this);
        dialog.setTaskInfo(task);
        dialog.setOperatorType(type);
        dialog.show(getChildFragmentManager(), "BeautyCreateOrEditMemberDocDialog");
    }


    private void showExecuteDialog(TaskRemind task) {
        BeautyResultTaskDialog dialog = new BeautyResultTaskDialog();
        dialog.setCallBackListener(this);
        dialog.setTaskInfo(task);
        dialog.show(getChildFragmentManager(), "BeautyResultTaskDialog");
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_task:
                taskStatus = 1;
                taskQueryReq.setStatus(taskStatus);
                requestTasks(true);
                break;
            case R.id.rb_outtime_task:
                taskStatus = 2;
                taskQueryReq.setStatus(taskStatus);
                requestTasks(true);
                break;
            case R.id.rb_today:
                selectedDate = getDate(0);
                taskQueryReq.setRemindTime(selectedDate.getTime());
                setCustomDate(null);
                requestTasks(true);
                break;
            case R.id.rb_tomorrow:
                selectedDate = getDate(1);
                taskQueryReq.setRemindTime(selectedDate.getTime());
                setCustomDate(null);
                requestTasks(true);
                break;
            case R.id.rb_after_tomorrow:
                selectedDate = getDate(2);
                taskQueryReq.setRemindTime(selectedDate.getTime());
                setCustomDate(null);
                requestTasks(true);
                break;
            case R.id.rb_custom_date:
                setCustomDate(selectedDate);
                break;
        }
    }

    private void refreshOver() {
        if (stl_refreshDataLayout.isRefreshing()) {
            stl_refreshDataLayout.setRefreshing(false);
        }
        if (stl_refreshDataLayout.isLoadingMore()) {
            stl_refreshDataLayout.setLoadingMore(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_custom_date:
                rb_customDate.setChecked(true);
            case R.id.rb_custom_date:
                showCalendarDialog();
                break;
            case R.id.btn_create_task:
                showCreateDocDialog(null, BeautyCreateOrEditTaskDialog.OPERATE_TYPE_NEW_CREATE);
                break;
        }
    }

    @Override
    public void onLoadMore() {
        if (taskQueryReq.getPageNo() < totalPageSize) {
            taskQueryReq.setPageNo(taskQueryReq.getPageNo() + 1);
            requestTasks(false);
        } else {
            ToastUtil.showShortToast("没有更多数据了");
        }
        refreshOver();
    }

    @Override
    public void onRefresh() {
        taskQueryReq.setPageNo(1);
        mTaskAdapter.getItems().clear();
        requestTasks(true);
        refreshOver();
    }

    @Override
    public void save(TaskRemind taskObj) {
        onRefresh();
    }

    @Override
    public void taskModify(TaskRemind task) {
        showCreateDocDialog(task, BeautyCreateOrEditTaskDialog.OPERATE_TYPE_EDIT);
    }

    @Override
    public void taskScan(TaskRemind task) {
        showCreateDocDialog(task, BeautyCreateOrEditTaskDialog.OPERATE_TYPE_SCAN);
    }

    @Override
    public void taskException(TaskRemind task) {
        showExecuteDialog(task);
    }

    @Override
    public void onSuccess(TaskRemind task) {
        onRefresh();
    }
}
