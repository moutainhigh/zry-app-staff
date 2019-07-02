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
import com.zhongmei.bty.basemodule.customer.bean.CustomerDocReq;
import com.zhongmei.bty.basemodule.customer.bean.TaskCreateOrEditReq;
import com.zhongmei.bty.basemodule.customer.bean.TaskQueryReq;
import com.zhongmei.bty.basemodule.shopmanager.utils.DateTimeUtil;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.view.calendar.CalendarTimeDialog;
import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.bean.YFResponseList;
import com.zhongmei.yunfu.bean.req.CustomerResp;
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

public class BeautyCreateOrEditTaskDialog extends BasicDialogFragment implements View.OnClickListener {
    public static final int OPERATE_TYPE_NEW_CREATE=0x01;
    public static final int OPERATE_TYPE_EDIT=0x02;
    public static final int OPERATE_TYPE_SCAN=0x03;

    View parentView;

    private TextView tv_title;

    private TextView tv_memberName;

    private LinearLayout layout_tradeInfo;
    private TextView tv_tradeNo;
    private TextView tv_tradeInfo;//订单组合信息

    private LinearLayout layout_task;
    private EditText et_taskTitle;
    private LinearLayout layout_executeTime;
    private TextView tv_taskTime;
    private LinearLayout layout_executor;
    private TextView tv_executor;
    private EditText et_taskContent;
    private LinearLayout layout_taskResult;
    private TextView tv_taskResult;

    private LinearLayout ll_bottom;
    private Button btn_submit;
    private ImageButton ib_close;

    private TaskOperatorLisnter taskOperatorListener;

    private TaskRemind mTask;
    private TaskCreateOrEditReq mTaskReq;
    private UserVo mExecutor;//执行人，默认为当前登陆的店员
    private CustomerResp mCustomer;
    private Date mSelectDate=new Date(System.currentTimeMillis());

    private int curOperateType=OPERATE_TYPE_NEW_CREATE;

    public void setTaskOperatorListener(TaskOperatorLisnter taskOperatorListener) {
        this.taskOperatorListener = taskOperatorListener;
    }

    public void setCustomerInfo(CustomerResp customer){
        this.mCustomer=customer;
    }

    public void setTaskInfo(TaskRemind task){
        this.mTask=task;
    }

    public void setOperatorType(int type){
        this.curOperateType=type;
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
        parentView=inflater.inflate(R.layout.beauty_create_edit_task_dialog,null);
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
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    /**
     * 初始化view
     * @param view
     */
    private void initView(View view){
        tv_title=(TextView)view.findViewById(R.id.tv_title);
        tv_memberName=(TextView)view.findViewById(R.id.tv_member_name);
        layout_tradeInfo=(LinearLayout)view.findViewById(R.id.layout_trade_info);
        tv_tradeNo=(TextView) view.findViewById(R.id.tv_trade_no);
        tv_tradeInfo=(TextView) view.findViewById(R.id.tv_trade_info);


        layout_task=(LinearLayout) view.findViewById(R.id.layout_task_info);
        et_taskTitle=(EditText) view.findViewById(R.id.et_task_title);
        layout_executeTime=(LinearLayout)view.findViewById(R.id.layout_execute_time);
        tv_taskTime=(TextView)view.findViewById(R.id.tv_task_time);
        layout_executor=(LinearLayout)view.findViewById(R.id.layout_executor);
        tv_executor=(TextView)view.findViewById(R.id.tv_executor);
        et_taskContent=(EditText) view.findViewById(R.id.et_task_content);
        layout_taskResult=(LinearLayout) view.findViewById(R.id.layout_task_result);
        tv_taskResult=(TextView)view.findViewById(R.id.tv_task_result);

        ll_bottom =(LinearLayout)view.findViewById(R.id.ll_bottom);
        btn_submit=(Button) view.findViewById(R.id.btn_submit);
        ib_close=(ImageButton) view.findViewById(R.id.btn_close);

        btn_submit.setOnClickListener(this);
        ib_close.setOnClickListener(this);
        layout_executor.setOnClickListener(this);
        layout_executeTime.setOnClickListener(this);
    }


    /**
     * 初始化数据
     */
    private void initData(){
        showExecutor(null);

        if(mCustomer!=null){
            tv_memberName.setText(mCustomer.getCustomerName(getContext()));
        }

        if(mTask!=null){
            if(!TextUtils.isEmpty(mTask.getCustomerName())){
                tv_memberName.setVisibility(View.VISIBLE);
                tv_memberName.setText(mTask.getCustomerName());
            }

//            tv_tradeNo.setText();
//            et_taskContent.setText();
            et_taskTitle.setText(mTask.getTitle());
            et_taskContent.setText(mTask.getContent());
            mSelectDate=new Date(mTask.getRemindTime());

            if(mTask.getStatus()==2){
                layout_taskResult.setVisibility(View.VISIBLE);
                tv_taskResult.setText(mTask.getTaskResult());
            }
        }
        showTaskRemindTime(mSelectDate);

        switch (curOperateType){
            case OPERATE_TYPE_NEW_CREATE:
                tv_title.setText("添加任务");
                break;
            case OPERATE_TYPE_EDIT:
                tv_title.setText("编辑任务");
                break;
            case OPERATE_TYPE_SCAN:
                tv_title.setText("查看详情");
                ll_bottom.setVisibility(View.GONE);
                et_taskTitle.setEnabled(false);
                et_taskContent.setEnabled(false);
                layout_executor.setOnClickListener(null);
                layout_executeTime.setOnClickListener(null);
                break;
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


        mTaskReq = new TaskCreateOrEditReq();

        if(mTask!=null){//编辑
            //需要修改
            mTaskReq.setCustomerName(mTask.getCustomerName());
            mTaskReq.setCustomerId(mTask.getCustomerId());
            mTaskReq.setCustomerMobile(mTask.getCustomerMobile());
            mTaskReq.setStatus(mTask.getStatus());
            mTaskReq.setTaskId(mTask.getId());
        }else{//新增
            mTaskReq.validateCreate();
            mTaskReq.setStatus(1);

            if(mCustomer!=null){
                mTaskReq.setCustomerName(mCustomer.customerName);
                mTaskReq.setCustomerId(mCustomer.customerId);
                mTaskReq.setCustomerMobile(mCustomer.mobile);
            }
        }

        mTaskReq.setTitle(taskTitle);
        mTaskReq.setContent(taskContent);
        mTaskReq.setRemindTime(mSelectDate.getTime());
        mTaskReq.setUserId(mExecutor.getUser().getId());
        mTaskReq.setUserName(mExecutor.getUser().getName());


        mTaskReq.validateUpdate();


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

    /**
     * 保存或创建任务
     * @param taskReq
     */
    private void saveTask(TaskCreateOrEditReq taskReq){
        if(taskReq!=null){
            YFResponseListener listener = LoadingYFResponseListener.ensure(new YFResponseListener<YFResponse<TaskRemind>>() {

                @Override
                public void onResponse(YFResponse<TaskRemind> response) {
                    if (YFResponseList.isOk(response)) {
                        //需要隐藏一下详情页面
                       if(taskOperatorListener!=null){
                           taskOperatorListener.save(response.getContent());
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

            BeautyCustomerOperates operates = OperatesFactory.create(BeautyCustomerOperates.class);
            operates.saveTask(taskReq, listener);
        }
    }


    @Override
    public void onClick(View v) {
        if(v == btn_submit){
            if(checkDataValid()){
                saveTask(mTaskReq);
            }
        }else if(v==ib_close){
            dismissAllowingStateLoss();
        }else if(v==layout_executeTime){
            selectExecuteTime();
        }else if(v==layout_executor){
            selectExecutor();
        }
    }

    public interface TaskOperatorLisnter{
        public void save(TaskRemind taskObj);
    }
}
