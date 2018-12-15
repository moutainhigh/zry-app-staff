package com.zhongmei.bty.customer.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.auth.application.CustomerApplication;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.enums.CustomerAppConfig;
import com.zhongmei.bty.basemodule.customer.message.MemberResetPswdReq;
import com.zhongmei.bty.basemodule.customer.message.MemberResetPswdResp;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.UserActionCode;
import com.zhongmei.bty.commonmodule.util.MD5;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.bty.customer.CustomerActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.dialog_customer_reset_pswd)
public class CustomerResetPswdDialog extends BasicDialogFragment {

    @FragmentArg(CustomerActivity.PARAMKEY_CUSTOMER)
    CustomerResp mCustomer;

    @FragmentArg(CustomerVerifyCodeDialog.PARAMKEY_CHECKCODE)
    String checkCode;

    @FragmentArg(CustomerVerifyCodeDialog.PARAMKEY_CHECKCODEID)
    Long checkCodeId;

    @ViewById(R.id.et_input_new_pswd)
    EditText etInputNewPswd;

    @ViewById(R.id.et_confirm_new_pswd)
    EditText etConfirmNewPswd;

    @ViewById(R.id.btn_negative)
    ImageView btnNegative;

    @ViewById(R.id.btn_positive)
    Button btnPositive;

    @AfterViews
    protected void initView() {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        setupView();
    }

    private void setupView() {
        if (CustomerApplication.mCustomerBussinessType == CustomerAppConfig.CustomerBussinessType.BEAUTY) {
            btnPositive.setBackgroundResource(R.drawable.beauty_customer_pwd_btn_selector);
        }
    }

    public void show(FragmentManager manager, Bundle bundle, String tag) {
        if (manager == null) {
            //网络请求中调用，可能出现NullPointerException问题
            return;
        }
        FragmentTransaction ft = manager.beginTransaction();
        setArguments(bundle);
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Click({R.id.btn_negative, R.id.btn_positive})
    void click(View v) {
        switch (v.getId()) {
            case R.id.btn_negative:
                hideSoftInputFromWindow(v);
                dismissAllowingStateLoss();
                break;
            case R.id.btn_positive:
                MobclickAgentEvent.onEvent(UserActionCode.GK010029);
                hideSoftInputFromWindow(v);
                if (TextUtils.isEmpty(etInputNewPswd.getText())) {
                    ToastUtil.showShortToast(R.string.input_new_pswd);
                    etInputNewPswd.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(etConfirmNewPswd.getText())) {
                    ToastUtil.showShortToast(R.string.confirm_new_pswd);
                    etConfirmNewPswd.requestFocus();
                    return;
                }

                if (!TextUtils.equals(etInputNewPswd.getText(), etConfirmNewPswd.getText())) {
                    ToastUtil.showShortToast(R.string.customer_password_not_match);
                    return;
                }

                if (etInputNewPswd.getText().length() != 6) {
                    ToastUtil.showShortToast(R.string.customer_pswd_length_must_be_6);
                    return;
                }

                String md5Pswd = new MD5().getMD5ofStr(etInputNewPswd.getText().toString());
                MemberResetPswdReq req =
                        new MemberResetPswdReq(mCustomer.synFlag, mCustomer.mobile,
                                checkCodeId, checkCode, md5Pswd);
                req.setCountry(mCustomer.country);
                req.setNation(mCustomer.nation);
                req.setNationalTelCode(mCustomer.nationalTelCode);
                CustomerOperates op = OperatesFactory.create(CustomerOperates.class);
                ResponseListener<MemberResetPswdResp> responseListener =
                        LoadingResponseListener.ensure(new ResponseListener<MemberResetPswdResp>() {

                            @Override
                            public void onResponse(ResponseObject<MemberResetPswdResp> response) {
                                if (ResponseObject.isOk(response)) {
                                    ToastUtil.showShortToast(R.string.modify_pswd_success);
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
                op.modifyPassword(req, responseListener);
                break;
            default:
                break;
        }
    }

}
