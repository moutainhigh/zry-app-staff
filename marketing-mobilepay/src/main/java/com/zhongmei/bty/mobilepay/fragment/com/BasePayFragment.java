package com.zhongmei.bty.mobilepay.fragment.com;

import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.dialog.BookingDeductionRefundDialog;
import com.zhongmei.bty.mobilepay.dialog.PayDepositPromptDialog;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.ToastUtil;

/**
 * Created by demo on 2018/12/15
 * 收银公共fragment，处理一些公共逻辑
 */

public abstract class BasePayFragment extends BasicFragment {
    protected boolean mIsSuportGroupPay = true;//默认支持分步支付
    protected IPaymentInfo mPaymentInfo;
    protected DoPayApi mDoPayApi;

    public void setPaymentInfo(IPaymentInfo iPaymentInfo) {
        this.mPaymentInfo = iPaymentInfo;
    }

    public void setDoPayApi(DoPayApi doPayApi) {
        this.mDoPayApi = doPayApi;
    }

    public void setIsSuportGroupPay(boolean isSuportGroupPay) {
        mIsSuportGroupPay = isSuportGroupPay;
    }

    //判断是否支持组合或分步支付
    public boolean isSuportGroupPay() {
        return (this.mIsSuportGroupPay && DoPayUtils.isSupportGroupPay(mPaymentInfo, PaySettingCache.isSupportGroupPay()));
    }

    //判断是否要做预付金抵扣
    public boolean isNeedToDeductionEarnest() {
        if (this.mPaymentInfo.getPaidAmount() <= 0 && this.mPaymentInfo.getTradeVo().getTradeEarnestMoney() > 0) {
            return true;
        }
        return false;
    }

    //公共支付检测,false 不能支付，true 可以支付
    public boolean doPayChecked(boolean isAllowZero) {
        //判断是否容许0元支付
        if (this.mPaymentInfo.getActualAmount() == 0 && !isAllowZero) {
            ToastUtil.showShortToast(R.string.pay_zero_cannot_use);
            return false;
        }
        //判断是否要交押金
        if (this.mPaymentInfo.isNeedToPayDeposit()) {
            PayDepositPromptDialog.start(this.getActivity(), mDoPayApi, this.mPaymentInfo);
            return false;
        }
        //判断是否要订金抵扣
        if (this.isNeedToDeductionEarnest()) {
            showBookingDeductionDialog();
            return false;
        }
        return true;
    }

    //获取用户输入金额
    public abstract double getInputValue();

    //判断结账按理是否可以用
    public boolean enablePay() {
        if (isSuportGroupPay()) {
            return getInputValue() > 0;
        } else {
            return getInputValue() >= this.mPaymentInfo.getActualAmount();
        }
    }

    //显示预付金抵扣界面
    public void showBookingDeductionDialog() {
        int optype = this.mPaymentInfo.getTradeVo().getTradeEarnestMoney() > this.mPaymentInfo.getActualAmount() ? 2 : 1;
        BookingDeductionRefundDialog.start(getFragmentManager(), mPaymentInfo, mDoPayApi, optype);
    }
}
