package com.zhongmei.bty.common.util;

import android.content.res.Resources;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.TradePayStatus;

public class ValueEnumsToStringUtils {

    public static String gettradePayStatusNameToString(TradePayStatus tradePayStatus) {
        Resources res = MainApplication.getInstance().getResources();
        if (tradePayStatus == TradePayStatus.UNPAID) {
            return res.getString(R.string.order_un_pay_st);
        } else if (tradePayStatus == TradePayStatus.PAID) {
            return res.getString(R.string.order_pay_st);
        } else if (tradePayStatus == TradePayStatus.REFUNDING) {
            return res.getString(R.string.refunding);
        } else if (tradePayStatus == TradePayStatus.REFUNDED) {
            return res.getString(R.string.refund_done);
        } else if (tradePayStatus == TradePayStatus.REFUND_FAILED) {
            return res.getString(R.string.refund_failed);
        } else if (tradePayStatus == TradePayStatus.PREPAID) {
            return res.getString(R.string.prepay);

        } else if (tradePayStatus == TradePayStatus.WAITING_REFUND) {
            return res.getString(R.string.refunding);
        } else {
            return res.getString(R.string.customer_sex_unknown);
        }

    }

    public static String gettradePayStatusNameToString(Integer tradePayStatusInt) {
        Resources res = MainApplication.getInstance().getResources();
        TradePayStatus tradePayStatus = ValueEnums.toEnum(TradePayStatus.class, tradePayStatusInt);
        if (tradePayStatus == TradePayStatus.UNPAID) {
            return res.getString(R.string.order_un_pay_st);
        } else if (tradePayStatus == TradePayStatus.PAID) {
            return res.getString(R.string.order_pay_st);
        } else if (tradePayStatus == TradePayStatus.REFUNDING) {
            return res.getString(R.string.refunding);
        } else if (tradePayStatus == TradePayStatus.REFUNDED) {
            return res.getString(R.string.refund_done);
        } else if (tradePayStatus == TradePayStatus.REFUND_FAILED) {
            return res.getString(R.string.refund_failed);
        } else if (tradePayStatus == TradePayStatus.PREPAID) {
            return res.getString(R.string.prepay);

        } else if (tradePayStatus == TradePayStatus.WAITING_REFUND) {
            return res.getString(R.string.refunding);
        } else {
            return res.getString(R.string.customer_sex_unknown);
        }
    }

}
