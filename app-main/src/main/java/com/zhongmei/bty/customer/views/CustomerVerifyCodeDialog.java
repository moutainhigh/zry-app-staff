package com.zhongmei.bty.customer.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.auth.application.CustomerApplication;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.enums.CustomerAppConfig;
import com.zhongmei.bty.basemodule.customer.message.MemberCheckCodeReq;
import com.zhongmei.bty.basemodule.customer.message.MemberCheckCodeResp;
import com.zhongmei.bty.basemodule.customer.message.MemberValidateCheckCodeReq;
import com.zhongmei.bty.basemodule.customer.message.MemberValidateCheckCodeResp;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.UserActionCode;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.bty.customer.CustomerActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.dialog_customer_verify_code)
public class CustomerVerifyCodeDialog extends BasicDialogFragment {

    public final static String PARAMKEY_CHECKCODEID = "checkCodeId";

    public final static String PARAMKEY_CHECKCODE = "checkCode";

    @FragmentArg(CustomerActivity.PARAMKEY_CUSTOMER)
    CustomerResp mCustomer;

    @ViewById(R.id.tv_phone_no)
    TextView tvPhoneNo;

    @ViewById(R.id.et_verify_code)
    EditText etVerifyCode;

    @ViewById(R.id.btn_get_verify_code)
    Button btnGetVerifyCode;

    @ViewById(R.id.tv_error)
    TextView tvError;

    @ViewById(R.id.btn_negative)
    ImageView btnNegative;

    @ViewById(R.id.btn_positive)
    Button btnPositive;

    private Long checkCodeId = -1L;

    private int leaveSecond = 0;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (leaveSecond > 0 && leaveSecond <= 60) {
                btnGetVerifyCode.setText(leaveSecond + "s");
                leaveSecond--;

                handler.removeMessages(0);
                sendEmptyMessageDelayed(0, 1000);
            } else {
                btnGetVerifyCode.setText(R.string.get_again);
                btnGetVerifyCode.setEnabled(true);
            }
        }

    };

    @AfterViews
    protected void initView() {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        setupView();
    }

    private void setupView() {
        if (CustomerApplication.mCustomerBussinessType == CustomerAppConfig.CustomerBussinessType.BEAUTY) {
            btnPositive.setBackgroundResource(R.drawable.beauty_customer_pwd_btn_selector);
            btnNegative.setImageResource(R.drawable.ic_property_close);
        }
    }

    public void show(FragmentManager manager, Bundle bundle, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        setArguments(bundle);
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Click({R.id.btn_get_verify_code, R.id.btn_negative, R.id.btn_positive})
    void click(View v) {
        switch (v.getId()) {
            case R.id.btn_get_verify_code:
                MobclickAgentEvent.onEvent(UserActionCode.GK010027);
                btnGetVerifyCode.setEnabled(false);

                leaveSecond = 60;
                handler.removeMessages(0);
                handler.sendEmptyMessage(0);

                MemberCheckCodeReq req =
                        new MemberCheckCodeReq(2, mCustomer.synFlag, mCustomer.mobile);
                req.setCountry(mCustomer.country);
                req.setNation(mCustomer.nation);
                req.setNationalTelCode(mCustomer.nationalTelCode);
                CustomerOperates op = OperatesFactory.create(CustomerOperates.class);
                op.getCheckCode(req, new ResponseListener<MemberCheckCodeResp>() {

                    @Override
                    public void onResponse(ResponseObject<MemberCheckCodeResp> response) {
                        if (ResponseObject.isOk(response)) {
                            checkCodeId = response.getContent().getCheckCodeId();
                        } else {
                            ToastUtil.showShortToast(response.getMessage());

                            leaveSecond = 0;
                            handler.removeMessages(0);
                            handler.sendEmptyMessage(0);
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        ToastUtil.showShortToast(error.getMessage());

                        leaveSecond = 0;
                        handler.removeMessages(0);
                        handler.sendEmptyMessage(0);
                    }

                });
                break;
            case R.id.btn_negative:
                hideSoftInputFromWindow(v);
                dismissAllowingStateLoss();
                break;
            case R.id.btn_positive:
                MobclickAgentEvent.onEvent(UserActionCode.GK010028);
                hideSoftInputFromWindow(v);
                if (checkCodeId == -1) {
                    ToastUtil.showShortToast(R.string.get_check_code_first);
                    return;
                }
                if (TextUtils.isEmpty(etVerifyCode.getText())) {
                    ToastUtil.showShortToast(R.string.please_input_check_code);
                    etVerifyCode.requestFocus();
                    return;
                }
                btnPositive.setEnabled(false);
                MemberValidateCheckCodeReq validateReq =
                        new MemberValidateCheckCodeReq(mCustomer.mobile, checkCodeId,
                                etVerifyCode.getText().toString());
                op = OperatesFactory.create(CustomerOperates.class);
                ResponseListener<MemberValidateCheckCodeResp> responseListener =
                        LoadingResponseListener.ensure(new ResponseListener<MemberValidateCheckCodeResp>() {

                            @Override
                            public void onResponse(ResponseObject<MemberValidateCheckCodeResp> response) {
                                btnPositive.setEnabled(true);
                                if (ResponseObject.isOk(response)) {
                                    showResetPswdDialog();
                                    dismissAllowingStateLoss();
                                } else {
                                    tvError.setVisibility(View.VISIBLE);
                                    tvError.setText(response.getMessage());
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                btnPositive.setEnabled(true);
                                ToastUtil.showShortToast(error.getMessage());
                            }

                        }, getFragmentManager());
                op.validateCheckCode(validateReq, responseListener);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        removeHandler();
        super.onDestroy();
    }

    private void showResetPswdDialog() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CustomerActivity.PARAMKEY_CUSTOMER, mCustomer);
        bundle.putLong(PARAMKEY_CHECKCODEID, checkCodeId);
        bundle.putString(PARAMKEY_CHECKCODE, etVerifyCode.getText().toString());
        CustomerResetPswdDialog resetPswdDialog = new CustomerResetPswdDialog_();
        resetPswdDialog.show(getFragmentManager(), bundle, "resetPswdDialog");
    }

    private void removeHandler() {
        if (handler != null) {
            handler.removeMessages(0);
            handler = null;
        }
    }
}
