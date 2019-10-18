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



public abstract class BasePayFragment extends BasicFragment {
    protected boolean mIsSuportGroupPay = true;    protected IPaymentInfo mPaymentInfo;
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

        public boolean isSuportGroupPay() {
        return (this.mIsSuportGroupPay && DoPayUtils.isSupportGroupPay(mPaymentInfo, PaySettingCache.isSupportGroupPay()));
    }

        public boolean isNeedToDeductionEarnest() {
        if (this.mPaymentInfo.getPaidAmount() <= 0 && this.mPaymentInfo.getTradeVo().getTradeEarnestMoney() > 0) {
            return true;
        }
        return false;
    }

        public boolean doPayChecked(boolean isAllowZero) {
                if (this.mPaymentInfo.getActualAmount() == 0 && !isAllowZero) {
            ToastUtil.showShortToast(R.string.pay_zero_cannot_use);
            return false;
        }
                if (this.mPaymentInfo.isNeedToPayDeposit()) {
            PayDepositPromptDialog.start(this.getActivity(), mDoPayApi, this.mPaymentInfo);
            return false;
        }
                if (this.isNeedToDeductionEarnest()) {
            showBookingDeductionDialog();
            return false;
        }
        return true;
    }

        public abstract double getInputValue();

        public boolean enablePay() {
        if (isSuportGroupPay()) {
            return getInputValue() > 0;
        } else {
            return getInputValue() >= this.mPaymentInfo.getActualAmount();
        }
    }

        public void showBookingDeductionDialog() {
        int optype = this.mPaymentInfo.getTradeVo().getTradeEarnestMoney() > this.mPaymentInfo.getActualAmount() ? 2 : 1;
        BookingDeductionRefundDialog.start(getFragmentManager(), mPaymentInfo, mDoPayApi, optype);
    }
}
