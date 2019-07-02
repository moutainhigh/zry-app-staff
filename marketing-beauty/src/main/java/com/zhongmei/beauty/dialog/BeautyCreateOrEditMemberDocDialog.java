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
import com.zhongmei.bty.basemodule.customer.bean.CustomerDocRecordResp;
import com.zhongmei.bty.basemodule.customer.bean.CustomerDocReq;
import com.zhongmei.bty.basemodule.customer.bean.TaskCreateOrEditReq;
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

public class BeautyCreateOrEditMemberDocDialog extends BasicDialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    View parentView;

    private TextView tv_memberName;
    private EditText et_docTitle;
    private EditText et_docContent;

    private CheckBox cb_createTask;

    private LinearLayout layout_task;
    private EditText et_taskTitle;
    private LinearLayout layout_executeTime;
    private TextView tv_taskTime;
    private LinearLayout layout_executor;
    private TextView tv_executor;
    private EditText et_taskContent;

    private Button btn_submit;
    private ImageButton ib_close;

    private DocOperatorLisnter docOperatorListener;

    private CustomerDocRecordResp customerDocDetail;
    private CustomerDocReq customerDocReq;
    private UserVo mExecutor;//执行人，默认为当前登陆的店员
    private CustomerResp mCustomer;

    private Date mSelectDate=new Date(System.currentTimeMillis());

    public void setDocOperatorListener(DocOperatorLisnter docOperatorListener) {
        this.docOperatorListener = docOperatorListener;
    }

    public void setCustomerInfo(CustomerResp customer){
        mCustomer=customer;
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
        parentView=inflater.inflate(R.layout.beauty_create_edit_member_doc_dialog,null);
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
        tv_memberName=(TextView)view.findViewById(R.id.tv_member_name);
        et_docTitle=(EditText) view.findViewById(R.id.et_doc_title);
        et_docContent=(EditText) view.findViewById(R.id.et_doc_content);

        cb_createTask=(CheckBox) view.findViewById(R.id.cb_add_task);

        layout_task=(LinearLayout) view.findViewById(R.id.layout_task_info);
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
        cb_createTask.setOnCheckedChangeListener(this);
    }


    /**
     * 初始化数据
     */
    private void initData(){
        showExecutor(null);

        if(mCustomer!=null){
            tv_memberName.setText(mCustomer.getCustomerName(getContext()));
        }

        if(customerDocDetail!=null){
            tv_memberName.setText(customerDocDetail.getCustomerName());
            et_taskTitle.setText(customerDocDetail.getTitle());
            et_taskContent.setText(customerDocDetail.getContent());
        }
        showTaskRemindTime(mSelectDate);
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


    /**
     * 检查数据的有效性
     */
    private boolean checkDataValid(){
        String docTitle=et_docTitle.getText().toString().trim();
        if(TextUtils.isEmpty(docTitle)){
            ToastUtil.showShortToast("档案标题不能为空！");
            return false;
        }

        String docContent=et_docContent.getText().toString().trim();
        if(TextUtils.isEmpty(docContent)){
            ToastUtil.showShortToast("档案内容不能为空！");
            return false;
        }

        TaskCreateOrEditReq taskReq=null;

        if(cb_createTask.isChecked()){
            String taskTitle=et_taskTitle.getText().toString().trim();
            if(TextUtils.isEmpty(taskTitle)){
                ToastUtil.showShortToast("任务标题不能为空！");
                return false;
            }

            String taskContent=et_taskContent.getText().toString().trim();
            if(TextUtils.isEmpty(taskContent)){
                ToastUtil.showShortToast("任务内容不能为空！");
                return false;
            }

            String executeTime=tv_taskTime.getText().toString().trim();
            if(TextUtils.isEmpty(executeTime)){
                ToastUtil.showShortToast("请选择执行时间！");
                return false;
            }

            taskReq=new TaskCreateOrEditReq();
            taskReq.validateCreate();
            taskReq.validateUpdate();
            taskReq.setTitle(taskTitle);
            taskReq.setContent(taskContent);
            taskReq.setRemindTime(mSelectDate.getTime());
            taskReq.setUserId(mExecutor.getUser().getId());
            taskReq.setUserName(mExecutor.getUser().getName());
            taskReq.setStatus(1);
            taskReq.setType(1);

            taskReq.setCustomerName(mCustomer.customerName);
            taskReq.setCustomerId(mCustomer.customerId);
            taskReq.setCustomerMobile(mCustomer.mobile);
        }

        customerDocReq = new CustomerDocReq();
        customerDocReq.setTaskReq(taskReq);

        if(customerDocDetail==null){
            customerDocReq.validateCreate();
        }

        customerDocReq.validateUpdate();

        customerDocReq.setTitle(docTitle);
        customerDocReq.setContent(docContent);

        if(mCustomer!=null){
            customerDocReq.setCustomerName(mCustomer.customerName);
            customerDocReq.setCustomerId(mCustomer.customerId);
        }
        return true;
    }

    private void showTaskRemindTime(Date date){
        tv_taskTime.setText(DateTimeUtils.formatDate(date,DateTimeUtils.DATE_TIME_FORMAT));
    }

    /**
     * 选择执行时间（用户自己选择）
     */
    private void selectExecuteTime(){
        CalendarTimeDialog mCalendarTimeDialog = new CalendarTimeDialog(getContext(), new CalendarTimeDialog.SelectedDateTimeListener() {
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

    private void saveDocDetail(CustomerDocReq docDetail){
        if(docDetail!=null){

            YFResponseListener listener = LoadingYFResponseListener.ensure(new YFResponseListener<YFResponse<CustomerDocRecordResp>>() {

                @Override
                public void onResponse(YFResponse<CustomerDocRecordResp> response) {
                    if (YFResponseList.isOk(response)) {
                        //需要隐藏一下详情页面
                        if(docOperatorListener!=null){
                            docOperatorListener.save(response.getContent());
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
            operates.saveDocRecord(docDetail, listener);
        }
    }



    @Override
    public void onClick(View v) {
        if(v == btn_submit){

            if(checkDataValid()){
                saveDocDetail(customerDocReq);
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
        int visiable=isChecked?View.VISIBLE:View.GONE;
        if(layout_task!=null){
            layout_task.setVisibility(visiable);
        }
    }


    public interface DocOperatorLisnter{
        public void save(CustomerDocRecordResp docObj);
    }
}
