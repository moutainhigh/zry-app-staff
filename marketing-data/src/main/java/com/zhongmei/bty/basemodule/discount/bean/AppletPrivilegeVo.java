package com.zhongmei.bty.basemodule.discount.bean;

import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.TradePrivilegeApplet;
import com.zhongmei.yunfu.db.enums.StatusFlag;



public class AppletPrivilegeVo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    TradePrivilege tradePrivilege;

    public TradePrivilege getTradePrivilege() {
        return tradePrivilege;
    }

    public void setTradePrivilege(TradePrivilege tradePrivilege) {
        this.tradePrivilege = tradePrivilege;
    }


    public boolean isPrivilegeValid() {
        return tradePrivilege != null && tradePrivilege.getStatusFlag() == StatusFlag.VALID;
    }


    public Long getActivityId() {
        if (tradePrivilege != null && tradePrivilege.getPromoId() != null) {
            return tradePrivilege.getPromoId();
        }
        return -1L;
    }
}
