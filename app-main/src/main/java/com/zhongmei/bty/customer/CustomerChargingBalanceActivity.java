package com.zhongmei.bty.customer;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.base.MainBaseActivity;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardInfo;
import com.zhongmei.yunfu.bean.req.CustomerResp;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * 余额、积分、优惠券、充值页面
 */
@EActivity(R.layout.customer_balance_charging_layout)
public class CustomerChargingBalanceActivity extends MainBaseActivity {

    public final static String KEY_CUSTOMER = "key_customer";

    private CustomerBalanceFragment customerBalanceFragment;
//	private CustomerChargingFragment customerChargingFragment;
//	public int whereFrom=FROM_MEMBER_CUSTOMER;
//	public  static  final int FROM_MEMBER_PAY=0;//来自支付界面
//	public  static final  int FROM_MEMBER_CUSTOMER=1;//来自顾客界面

    @AfterViews
    void initView() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        customerBalanceFragment = new CustomerBalanceFragment_();
//		customerChargingFragment = new CustomerChargingFragment_();
        fragmentTransaction.replace(R.id.customer_charging_left, customerBalanceFragment);
//		fragmentTransaction.replace(R.id.customer_charging_right, customerChargingFragment);
        fragmentTransaction.commit();
        loadData();

    }

    private void loadData() {
        Bundle bundle = this.getIntent().getExtras();
//		whereFrom = bundle.getInt("whereFrom");
        String customerId = bundle.getString("customerId");// 会员id
        final int type = bundle.getInt("type");
        final String integarl = bundle.getString("integral");
        final String balance = bundle.getString("balance");
        final EcCardInfo ecCard = (EcCardInfo) bundle.getSerializable("ecCard");
        final CustomerResp customer = (CustomerResp) bundle.getSerializable(KEY_CUSTOMER);
        bindFragemnt(customer, type, integarl, ecCard, balance);
    }

    private void bindFragemnt(CustomerResp customer, int type, String integarl, EcCardInfo ecCard, String balance) {
        customerBalanceFragment.bindData(customer, type, integarl, ecCard);
//		customerChargingFragment.bindData(whereFrom,customer, balance,ecCard);
    }
}
