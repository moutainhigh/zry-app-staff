package com.zhongmei.beauty.customer.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhongmei.beauty.customer.constants.BeautyCustomerConstants;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.customer.util.CustomerUtil;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.customer.event.EventCreateOrEditCustomer;
import com.zhongmei.bty.customer.util.CustomerContants;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.bean.req.CustomerCreateResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.data.LoadingYFResponseListener;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.resp.YFResponseListener;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

/**
 * Created by dingzb on 2019/5/14.
 */

public class BeautyCustomerBindCardDialog extends BasicDialogFragment implements View.OnClickListener{
    private final String TAG=BeautyCustomerBindCardDialog.class.getSimpleName();

    private ImageButton ib_close; //关闭按钮

    private TextView tv_memberName; //会员名称

    private EditText et_cardNo; //会员编号

    private Button btn_bind; //绑定按钮

    private CustomerResp mCustomer;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_LAYOUT_FLAGS | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        Window window = dialog.getWindow();
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.beauty_customer_bind_card_dialog_layout, container, false);
        ib_close=(ImageButton)view.findViewById(R.id.ib_close);
        tv_memberName=(TextView)view.findViewById(R.id.tv_member_name);
        et_cardNo=(EditText)view.findViewById(R.id.et_card_no);
        btn_bind=(Button)view.findViewById(R.id.btn_bind);
        btn_bind.setOnClickListener(this);
        ib_close.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(getArguments());
    }

    private void initData(Bundle bundle){
        if(bundle!=null){
            mCustomer= (CustomerResp) bundle.getSerializable(BeautyCustomerConstants.KEY_CUSTOMER);
        }

        if(mCustomer!=null){
            tv_memberName.setText(mCustomer.getCustomerName(getContext()));
        }

        et_cardNo.requestFocus();

    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.ib_close:
                dismissAllowingStateLoss();
                break;
            case R.id.btn_bind:
                bindCardNo();
                break;
        }

    }


    private void bindCardNo(){
        String cardNo=et_cardNo.getText().toString().trim();
        if(TextUtils.isEmpty(cardNo)){
            ToastUtil.showShortToast("请输入卡号!");
            return;
        }
        mCustomer.cardNo=cardNo;
        //网络请求
        doCreateCustomer(mCustomer);
    }


    private void doCreateCustomer(CustomerResp customer) {
        CustomerOperates oper = OperatesFactory.create(CustomerOperates.class);
        YFResponseListener<YFResponse<CustomerCreateResp>> listener = new YFResponseListener<YFResponse<CustomerCreateResp>>() {

            @Override
            public void onResponse(YFResponse<CustomerCreateResp> response) {
                try {
                    if (YFResponse.isOk(response)) {
                        ToastUtil.showShortToast("绑定会员卡成功!");
                        dismissAllowingStateLoss();
                    } else {
                        ToastUtil.showShortToast(response.getMessage());
                    }
                    UserActionEvent.end(UserActionEvent.CUSTOMER_CREATE);
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                UserActionEvent.end(UserActionEvent.CUSTOMER_CREATE);
                ToastUtil.showShortToast(error.getMessage());
            }

        };
        //if (TextUtils.isEmpty(uniqueCode)) {
        oper.customerBindCard(customer.cardNo,customer.customerId, LoadingYFResponseListener.ensure(listener, getFragmentManager()));
        /*} else {
            oper.createMemberByPresetCustomer(customer, uniqueCode, LoadingYFResponseListener.ensure(listener, getSupportFragmentManager()));
        }*/
    }

}
