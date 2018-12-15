package com.zhongmei.bty.mobilepay.fragment.lag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongmei.bty.mobilepay.IDoLag;
import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.event.ActionClose;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.yunfu.ui.base.BasicFragment;

/**
 * Created by demo on 2018/12/15
 */

public class LagMainFragment extends BasicFragment implements LoginListener {
    private final String TAG = LagMainFragment.class.getSimpleName();

    private String currentPhone;

    private Long mCustomerId;
    //add 20160714 start
    private IPaymentInfo mPaymentInfo;
    private IDoLag mIDoLag;
    private DoPayApi mDoPayApi;//add v8.9

    public void setPaymentInfo(IPaymentInfo iPaymentInfo) {
        this.mPaymentInfo = iPaymentInfo;
    }

    private void setDoPayApi(DoPayApi doPayApi) {
        this.mDoPayApi = doPayApi;
    }

    public void setIDoLag(IDoLag iDoLag) {
        this.mIDoLag = iDoLag;
    }


    public static LagMainFragment newInstance(IPaymentInfo info, DoPayApi doPayApi) {
        LagMainFragment f = new LagMainFragment();
        f.setPaymentInfo(info);
        f.setDoPayApi(doPayApi);
        f.setArguments(new Bundle());
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.pay_lagmain_fragment, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    private void initView() {
        registerEventBus();
        verifyChangePage();
    }

    private void verifyChangePage() {
        currentPhone = getCustomerPhoneNo();
        changeLoginFragment();
    }

    /**
     * 是否有登录用户
     *
     * @return 用户电话号码
     */
    private String getCustomerPhoneNo() {
        boolean isDinner = mPaymentInfo.isDinner();
        if (isDinner) {
            CustomerResp customer = DinnerShopManager.getInstance().getLoginCustomer();
            if (customer != null && customer.isMember() && customer.card == null) {
                mCustomerId = customer.customerId;
                return customer.mobile;
            }
        }
        return null;
    }

    private void changeLoginFragment() {
        LagLoginFragment loginFragment = LagLoginFragment.newInstance(mPaymentInfo, mDoPayApi);
        loginFragment.setListener(this);
        loginFragment.setPhone(currentPhone, mCustomerId);
        replaceFragment(R.id.pay_lag_mainlayout, loginFragment, loginFragment.getClass().getSimpleName());
    }

    private void toLagPayPage(CustomerResp customer) {
        LagPayFragment infoFragment = LagPayFragment.newInstance(mPaymentInfo, mDoPayApi);
        infoFragment.setIDoPay(mIDoLag);//add 20161122
        if (customer != null) {
            infoFragment.setCustomerInfo(customer);
        }
        replaceFragment(R.id.pay_lag_mainlayout, infoFragment, infoFragment.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
    }

    @Override
    public void onPhoneSuccess(CustomerResp customer) {
        toLagPayPage(customer);
    }

    @Override
    public void onCardSuccess(CustomerResp customer) {
        // toLagPayPage();
    }

    public void onEventMainThread(ActionClose close) {
        if (getActivity() == null) {
            return;
        }
        currentPhone = null;
        mCustomerId = null;
        changeLoginFragment();
    }

}
