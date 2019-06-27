package com.zhongmei.beauty.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.beauty.entity.UserVo;
import com.zhongmei.beauty.operates.BeautyCustomerOperates;
import com.zhongmei.bty.basemodule.customer.bean.TaskCreateOrEditReq;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.view.calendar.CalendarTimeDialog;
import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.bean.YFResponseList;
import com.zhongmei.yunfu.beauty.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.data.LoadingYFResponseListener;
import com.zhongmei.yunfu.db.entity.TaskRemind;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.YFResponseListener;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dingzb on 2019/6/25.
 */

public class BeautyResultTaskDialog extends BasicDialogFragment implements View.OnClickListener ,CompoundButton.OnCheckedChangeListener{

    View parentView;

    private LinearLayout layout_taskInfo;
    private LinearLayout layout_memberInfo;
    private TextView tv_memberInfo;
    private TextView tv_taskTitle;
    private TextView tv_taskContent;
    private EditText et_taskResult;

    private CheckBox cb_addTask;

    private LinearLayout layout_newTaskInfo;
    private EditText et_taskTitle;
    private LinearLayout layout_executeTime;
    private TextView tv_taskTime;
    private LinearLayout layout_executor;
    private TextView tv_executor;
    private EditText et_taskContent;

    private Button btn_submit;
    private ImageButton ib_close;

    private CallBackListener callBackListener;

    private TaskRemind mTask;
    private TaskCreateOrEditReq mTaskReq;
    private TaskCreateOrEditReq mNewTaskReq;
    private UserVo mExecutor;//执行人，默认为当前登陆的店员
    private Date mSelectDate=new Date(System.currentTimeMillis());

    public void setCallBackListener(CallBackListener listener) {
        this.callBackListener = listener;
    }

    public void setTaskInfo(TaskRemind task){
        this.mTask=task;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initData();
        super.onViewCreated(view, savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentView=inflater.inflate(R.layout.beauty_result_task_dialog,null);
        setupWindow();
        return parentView;
    }

    private void setupWindow() {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        Window window = getDialog().getWindow();
        if (window != null) {
            //设置宽高
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = DensityUtil.dip2px(getActivity(), 460f);
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(attributes);
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    /**
     * 初始化view
     * @param view
     */
    private void initView(View view){
        layout_taskInfo=(LinearLayout) view.findViewById(R.id.layout_task_info);
        layout_memberInfo=(LinearLayout)view.findViewById(R.id.layout_member_info);
        tv_memberInfo=(TextView) view.findViewById(R.id.tv_member_info);
        tv_taskTitle=(TextView) view.findViewById(R.id.tv_task_title);
        tv_taskContent=(TextView) view.findViewById(R.id.tv_task_content);
        et_taskResult=(EditText)view.findViewById(R.id.et_task_result) ;

        cb_addTask=(CheckBox)view.findViewById(R.id.cb_add_task);

        layout_newTaskInfo=(LinearLayout) view.findViewById(R.id.layout_new_task_info);
        et_taskTitle=(EditText) view.findViewById(R.id.et_task_title);
        layout_executeTime=(LinearLayout)view.findViewById(R.id.layout_execute_time);
        tv_taskTime=(TextView)view.findViewById(R.id.tv_task_time);
        layout_executor=(LinearLayout)view.findViewById(R.id.layout_executor);
        tv_executor=(TextView)view.findViewById(R.id.tv_executor);
        et_taskContent=(EditText) view.findViewById(R.id.et_task_content);

        btn_submit=(Button) view.findViewById(R.id.btn_submit);
        ib_close=(ImageButton) view.findViewById(R.id.btn_close);

        btn_submit.setOnClickListener(this);
        ib_close.setOnClickListener(this);
        layout_executor.setOnClickListener(this);
        layout_executeTime.setOnClickListener(this);
        cb_addTask.setOnCheckedChangeListener(this);
    }


    /**
     * 初始化数据
     */
    private void initData(){
        showExecutor(null);

        if(mTask!=null){
            if(!TextUtils.isEmpty(mTask.getCustomerName())){
                layout_memberInfo.setVisibility(View.VISIBLE);
                tv_memberInfo.setText(mTask.getCustomerName());
            }
            tv_taskTitle.setText(mTask.getTitle());
            tv_taskContent.setText(mTask.getContent());

            et_taskTitle.setText(mTask.getTitle());
            et_taskContent.setText(mTask.getContent());
            mSelectDate=new Date(mTask.getRemindTime());
            showTaskRemindTime(mSelectDate);
        }
    }


    /**
     * 显示执行人
     * @param userVo
     */
    private void showExecutor(UserVo userVo){
        if(userVo==null){
            IAuthUser authUser=ShopInfoCfg.getInstance().authUser;
            User user=new User();
            user.setId(authUser.getId());
            user.setName(authUser.getName());
            userVo=new UserVo(user);
        }

        mExecutor=userVo;

        if(mExecutor!=null){
            tv_executor.setText(mExecutor.getUser().getName());
        }
    }

    private void showTaskRemindTime(Date date){
        tv_taskTime.setText(DateTimeUtils.formatDate(date,DateTimeUtils.DATE_TIME_FORMAT));
    }


    /**
     * 检查数据的有效性
     */
    private boolean checkDataValid(){
        String taskResult = et_taskResult.getText().toString().trim();


        if(cb_addTask.isChecked()){

        String taskTitle = et_taskTitle.getText().toString().trim();
        if (TextUtils.isEmpty(taskTitle)) {
            ToastUtil.showShortToast("任务标题不能为空！");
            return false;
        }

        String taskContent = et_taskContent.getText().toString().trim();
        if (TextUtils.isEmpty(taskContent)) {
            ToastUtil.showShortToast("任务内容不能为空！");
            return false;
        }

        String executeTime = tv_taskTime.getText().toString().trim();
        if (TextUtils.isEmpty(executeTime)) {
            ToastUtil.showShortToast("请选择执行时间！");
            return false;
        }

            mNewTaskReq=new TaskCreateOrEditReq();
            mNewTaskReq.validateCreate();
            mNewTaskReq.setTitle(taskTitle);
            mNewTaskReq.setContent(taskContent);
            mNewTaskReq.setRemindTime(mSelectDate.getTime());
            mNewTaskReq.setUserId(mExecutor.getUser().getId());
            mNewTaskReq.setUserName(mExecutor.getUser().getName());
            mNewTaskReq.setCustomerName(mTask.getCustomerName());
            mNewTaskReq.setCustomerId(mTask.getCustomerId());
            mNewTaskReq.setCustomerMobile(mTask.getCustomerMobile());
            mNewTaskReq.setStatus(1);
        }


        mTaskReq = new TaskCreateOrEditReq();
        mTaskReq.setTaskId(mTask.getId());
        mTaskReq.setTaskResult(taskResult);
        mTaskReq.validateUpdate();
        mTaskReq.setStatus(2);

        return true;
    }


    /**
     * 选择执行时间（用户自己选择）
     */
    private void selectExecuteTime(){
        CalendarTimeDialog  mCalendarTimeDialog = new CalendarTimeDialog(getContext(), new CalendarTimeDialog.SelectedDateTimeListener() {
            @Override
            public void selectedDateTime(Date date, String time) {
                mSelectDate=date;
                showTaskRemindTime(mSelectDate);
            }
        },true);
        mCalendarTimeDialog.setDefaultSelected(mSelectDate);
        mCalendarTimeDialog.showDialog();
    }

    /**
     * 选择这行人，默认为当前服务员
     */
    private void selectExecutor(){
        List<UserVo> selectUser=new ArrayList<UserVo>();
        selectUser.add(mExecutor);
        BeautyBookingWaiterDialog dialog =new BeautyBookingWaiterDialog();
        dialog.setOnBeautyWaiterListener(new BeautyBookingWaiterDialog.OnBeautyWaiterListener(){

            @Override
            public void onChoiceUserListener(@org.jetbrains.annotations.Nullable List<? extends UserVo> userVo) {
                if(Utils.isNotEmpty(userVo)){
                    showExecutor(userVo.get(0));
                }else{
                    showExecutor(null);
                }
            }
        });
        dialog.setSelectUsers(selectUser); // 编辑时传入锁定状态
        dialog.setIsNotFreeUsers(new ArrayList<Long>());
        dialog.show(getChildFragmentManager(), "BeautyBookingWaiterDialog");
    }


    private void resultTask(){
        YFResponseListener listener = LoadingYFResponseListener.ensure(new YFResponseListener<YFResponse<TaskRemind>>() {

            @Override
            public void onResponse(YFResponse<TaskRemind> response) {
                if (YFResponseList.isOk(response)) {
                    //需要隐藏一下详情页面
                    if(callBackListener!=null){
                        callBackListener.onSuccess(response.getContent());
                    }
                    dismissAllowingStateLoss();
                } else {
                    ToastUtil.showShortToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        }, getFragmentManager());

        request(mTaskReq,listener);
    }

    private void createTask(){
        YFResponseListener listener = LoadingYFResponseListener.ensure(new YFResponseListener<YFResponse<TaskRemind>>() {

            @Override
            public void onResponse(YFResponse<TaskRemind> response) {
                if (YFResponseList.isOk(response)) {
                    //需要隐藏一下详情页面
                    resultTask();
                } else {
                    ToastUtil.showShortToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        }, getFragmentManager());

        request(mNewTaskReq,listener);
    }


    private void request(TaskCreateOrEditReq req,YFResponseListener listener){
        BeautyCustomerOperates operates = OperatesFactory.create(BeautyCustomerOperates.class);
        operates.saveTask(req, listener);
    }


    @Override
    public void onClick(View v) {
        if(v == btn_submit){
            if(checkDataValid()){
                if(mNewTaskReq!=null){
                    createTask();//先创建任务
                }else{
                    resultTask();//直接结束任务
                }
            }
        }else if(v==ib_close){
            dismissAllowingStateLoss();
        }else if(v==layout_executeTime){
            selectExecuteTime();
        }else if(v==layout_executor){
            selectExecutor();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int visiable = isChecked?View.VISIBLE:View.GONE;
        layout_newTaskInfo.setVisibility(visiable);
    }

    public interface CallBackListener{
        public void onSuccess(TaskRemind task);
    }
}
