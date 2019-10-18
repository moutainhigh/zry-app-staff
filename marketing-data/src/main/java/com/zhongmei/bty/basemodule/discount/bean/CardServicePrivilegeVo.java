package com.zhongmei.bty.basemodule.discount.bean;

import com.zhongmei.bty.commonmodule.database.enums.ServerPrivilegeType;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.enums.StatusFlag;



public class CardServicePrivilegeVo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    TradePrivilege tradePrivilege;

    public TradePrivilege getTradePrivilege() {
        return tradePrivilege;
    }

    public void setTradePrivilege(TradePrivilege tradePrivilege) {
        this.tradePrivilege = tradePrivilege;
    }

    public ServerPrivilegeType getType() {
        switch (tradePrivilege.getPrivilegeType()) {
            case CARD_SERVICE:
                return ServerPrivilegeType.COUNT_SERVER;
            default:
                return ServerPrivilegeType.COUNT_SERVER;
        }
    }


    public Long getServerRecordId() {
        if (tradePrivilege == null) {
            return -1L;
        }
        return tradePrivilege.getPromoId();
    }


    public boolean isPrivilegeValid() {
        return tradePrivilege != null && tradePrivilege.getStatusFlag() == StatusFlag.VALID;
    }

}
