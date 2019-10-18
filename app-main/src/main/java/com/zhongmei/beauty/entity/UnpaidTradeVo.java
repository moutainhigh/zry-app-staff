package com.zhongmei.beauty.entity;

import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.bty.commonmodule.util.DateUtil;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.List;



public class UnpaidTradeVo extends TradeVo {
    private List<TradeCustomer> mTradeCustomers;

    public void setmTradeCustomer(List<TradeCustomer> mTradeCustomers) {
        this.mTradeCustomers = mTradeCustomers;
    }

    public String getTradeTime() {
        return DateUtil.format(getTrade().getServerCreateTime());
    }

    public String getTradeAmount() {
        return getTrade().getTradeAmount().toString();
    }

    public String getServices() {
        return "";
    }

    public TradeCustomer getMember() {
        if (Utils.isEmpty(mTradeCustomers)) {
            return null;
        }
        return mTradeCustomers.get(0);
    }

    public Long getTradeId() {
        return getTrade().getId();
    }

    public TradeStatus getTradeStatus() {
        return getTrade().getTradeStatus();
    }

    public TradePayStatus getTradePayStatus() {
        return getTrade().getTradePayStatus();
    }
}
