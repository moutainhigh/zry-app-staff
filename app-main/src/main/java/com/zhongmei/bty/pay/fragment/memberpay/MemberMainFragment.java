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
    /**
     * 登录会员（可能不是单据的会员）
     */
    private CustomerResp mCustomer;//mCustomer和mecCard不能并存

    private EcCard mecCard;//mCustomer和mecCard不能并存

    private boolean mIsSuportGroupPay = true;//默认支持分步支付

    private TradeCustomer memberCustomer = null;// 单据里面的会员

    private MemberPayFragment memberPayFragment;

    private MemberLoginFragment memberLoginFragment;
    //add 20160714 start
    private IPaymentInfo mPaymentInfo;

    private DoPayApi mDoPayApi;//add v8.11

    private boolean isDefaultShowCode = false;//true默认初始化就显示付款码

    public void setPaymentInfo(IPaymentInfo cashInfoManager) {
        this.mPaymentInfo = cashInfoManager;
    }

    public void setDoPayApi(DoPayApi doPayApi) {
        this.mDoPayApi = doPayApi;
    }

    //and 20160714 end
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
            mCustomer = mPaymentInfo.getCustomer();//mCustomer和mecCard不能并存
            mecCard = mPaymentInfo.getEcCard();//mCustomer和mecCard不能并存
            if (mecCard == null && mCustomer == null && mPaymentInfo.getTradeVo() != null) {
                // 没有登录会员,看看单据里面是否有会员
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
            if (mCustomer != null && mCustomer.isMember()) {// 标识已登录会员 直接显示会员余额信息
                showLoginOrPayFragment(false, false);

            } else if (mecCard != null) {
                showLoginOrPayFragment(false, false);
            } else if (memberCustomer != null) {
                showLoginOrPayFragment(true, true);// 需要登录  传入需要自动登录的TradeCustomer  进入登录界面进行自动登录
            } else {// 需要登录 显示登录界面
                showLoginOrPayFragment(true, false);
            }
        }
    }

    /**
     * 判断是否是登录会员界面 isLogin true:显示登录界面 :false显示支付界面
     *
     * @param isLogin
     */

    private void showLoginOrPayFragment(boolean isLogin, boolean isautoLogin) {
        if (isLogin) {
            memberLoginFragment = new MemberLoginFragment_();
            memberLoginFragment.setCashInfoManager(mPaymentInfo);
            memberLoginFragment.setAutoLogin(isautoLogin);//设置是否需要自动登录，需要TradeCumtor有值
            memberLoginFragment.setDefaultShowCode(isDefaultShowCode);
            memberLoginFragment.setDoPayApi(mDoPayApi);//add v8.11
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (memberPayFragment != null) {
                fragmentTransaction.remove(memberPayFragment);
                memberPayFragment = null;
            }
            fragmentTransaction.commit();
            replaceChildFragment(R.id.fragmetlayout, memberLoginFragment, MemberLoginFragment.class.getSimpleName());

        } else {
            memberPayFragment = new MemberPayFragment_();
            memberPayFragment.setIsSuportGroupPay(this.mIsSuportGroupPay);//add v8.2 添加会员权益判断
            memberPayFragment.setPaymentInfo(mPaymentInfo);
            memberPayFragment.setDoPayApi(mDoPayApi);//add v8.11
            FragmentManager fragmentManager = getChildFragmentManager();
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

    //设置是否支持组合支付 add v8.2 添加会员权益判断
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
