package com.zhongmei.beauty.customer;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zhongmei.beauty.customer.constants.BeautyCustomerConstants;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.base.MainBaseActivity;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardInfo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;


@EActivity(R.layout.customer_balance_charging_layout)
public class BeautyCustomerChargingBalanceActivity extends MainBaseActivity {

    public final static String KEY_CUSTOMER = "key_customer";

    private BeautyCustomerBalanceFragment customerBalanceFragment;

    @AfterViews
    void initView() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        customerBalanceFragment = new BeautyCustomerBalanceFragment_();
        fragmentTransaction.replace(R.id.customer_charging_left, customerBalanceFragment);
        fragmentTransaction.commit();
        loadData();

    }

    private void loadData() {
        Bundle bundle = this.getIntent().getExtras();
        String customerId = bundle.getString("customerId");        final int type = bundle.getInt("type");
        final String integarl = bundle.getString("integral");
        final String balance = bundle.getString("balance");
        final EcCardInfo ecCard = (EcCardInfo) bundle.getSerializable("ecCard");
        final CustomerResp customer = (CustomerResp) bundle.getSerializable(BeautyCustomerConstants.KEY_CUSTOMER);
        bindFragemnt(customer, type, integarl, ecCard, balance);
    }

    private void bindFragemnt(CustomerResp customer, int type, String integarl, EcCardInfo ecCard, String balance) {
        customerBalanceFragment.bindData(customer, type, integarl, ecCard);
    }
}
