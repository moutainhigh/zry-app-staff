package com.zhongmei.bty.pay.fragment.memberpay;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.yunfu.R;

import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.mobilepay.IPaymentMenuType;
import com.zhongmei.bty.mobilepay.v1.event.MemberLoginEvent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;


import java.util.List;

@EFragment(R.layout.pay_member_main_fragment_layout)
public class MemberMainFragment extends BasicFragment implements IPaymentMenuType {

    private CustomerResp mCustomer;
    private EcCard mecCard;
    private boolean mIsSuportGroupPay = true;
    private TradeCustomer memberCustomer = null;
    private MemberPayFragment memberPayFragment;

    private MemberLoginFragment memberLoginFragment;
        private IPaymentInfo mPaymentInfo;

    private DoPayApi mDoPayApi;
    private boolean isDefaultShowCode = false;
    public void setPaymentInfo(IPaymentInfo cashInfoManager) {
        this.mPaymentInfo = cashInfoManager;
    }

    public void setDoPayApi(DoPayApi doPayApi) {
        this.mDoPayApi = doPayApi;
    }

        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
    }

    @AfterViews
    public void init() {
        if (this.mPaymentInfo != null) {
            mCustomer = mPaymentInfo.getCustomer();            mecCard = mPaymentInfo.getEcCard();            if (mecCard == null && mCustomer == null && mPaymentInfo.getTradeVo() != null) {
                                List<TradeCustomer> tradeCustomerList = mPaymentInfo.getTradeVo().getTradeCustomerList();

                if (tradeCustomerList != null && tradeCustomerList.size() > 0) {
                    for (TradeCustomer tadeCustomer : tradeCustomerList) {
                        if (CustomerType.MEMBER == tadeCustomer.getCustomerType()
                                && tadeCustomer.getStatusFlag() == StatusFlag.VALID) {
                            memberCustomer = tadeCustomer;
                            break;
                        }
                        if (CustomerType.CARD == tadeCustomer.getCustomerType()
                                && tadeCustomer.getStatusFlag() == StatusFlag.VALID) {
                            memberCustomer = tadeCustomer;
                            break;
                        }
                    }
                }
            }
            if (mCustomer != null && mCustomer.isMember()) {                showLoginOrPayFragment(false, false);

            } else if (mecCard != null) {
                showLoginOrPayFragment(false, false);
            } else if (memberCustomer != null) {
                showLoginOrPayFragment(true, true);            } else {                showLoginOrPayFragment(true, false);
            }
        }
    }



    private void showLoginOrPayFragment(boolean isLogin, boolean isautoLogin) {
        if (isLogin) {
            memberLoginFragment = new MemberLoginFragment_();
            memberLoginFragment.setCashInfoManager(mPaymentInfo);
            memberLoginFragment.setAutoLogin(isautoLogin);            memberLoginFragment.setDefaultShowCode(isDefaultShowCode);
            memberLoginFragment.setmCustomerType(CustomerType.PAY);
            memberLoginFragment.setDoPayApi(mDoPayApi);            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (memberPayFragment != null) {
                fragmentTransaction.remove(memberPayFragment);
                memberPayFragment = null;
            }
            fragmentTransaction.commit();
            replaceChildFragment(R.id.fragmetlayout, memberLoginFragment, MemberLoginFragment.class.getSimpleName());

        } else {
            memberPayFragment = new MemberPayFragment_();
            memberPayFragment.setIsSuportGroupPay(this.mIsSuportGroupPay);            memberPayFragment.setPaymentInfo(mPaymentInfo);
            memberPayFragment.setDoPayApi(mDoPayApi);            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (memberLoginFragment != null) {
                fragmentTransaction.remove(memberLoginFragment);
                memberLoginFragment = null;
            }
            fragmentTransaction.commit();
            replaceChildFragment(R.id.fragmetlayout, memberPayFragment, MemberPayFragment.class.getSimpleName());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (memberPayFragment != null && memberPayFragment.isAdded()) {
            memberPayFragment.onHiddenChanged(hidden);
        }
        if (memberLoginFragment != null && memberLoginFragment.isAdded()) {
            memberLoginFragment.onHiddenChanged(hidden);
        }
        super.onHiddenChanged(hidden);
    }

    public void onEventMainThread(MemberLoginEvent event) {

        showLoginOrPayFragment(event.isshowLogin(), false);

    }

        public void setSuportGroupPay(Boolean isSuportGroupPay) {
        mIsSuportGroupPay = isSuportGroupPay;
        if (memberPayFragment != null)
            memberPayFragment.setIsSuportGroupPay(isSuportGroupPay);
    }

    public void setDefaultShowCode(boolean defaultShowCode) {
        isDefaultShowCode = defaultShowCode;
        if (memberLoginFragment != null) {
            memberLoginFragment.setDefaultShowCode(isDefaultShowCode);
        }
    }
}
