package com.zhongmei;

import android.content.Context;
import android.content.Intent;

import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.math.BigDecimal;

public abstract class ZMIntent {

    public static void pay(Context context, TradeVo tradeVo) {
                Intent intent = new Intent();
                intent.setClassName(context.getPackageName(), "com.zhongmei.beauty.pay.BeautyPayActivity");
        intent.putExtra("tradeVo", tradeVo);
        context.startActivity(intent);
    }

    public static void payRecharge(Context context, TradeVo tradeVo, Long customerId, BigDecimal chargeMoney) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), "com.zhongmei.beauty.pay.BeautyPayActivity");
        intent.putExtra("tradeVo", tradeVo);
        intent.putExtra("customerId", customerId);
        intent.putExtra("chargeMoney", chargeMoney);
        context.startActivity(intent);
    }
}
