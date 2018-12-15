package com.zhongmei.bty.basemodule.discount.bean;

import com.zhongmei.bty.commonmodule.database.enums.ServerPrivilegeType;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.enums.StatusFlag;

/**
 * 次卡服务对象
 */

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

    /**
     * 获取购买的记录id
     *
     * @return
     */
    public Long getServerRecordId() {
        if (tradePrivilege == null) {
            return -1L;
        }
        return tradePrivilege.getPromoId();
    }

    /**
     * 优惠是否有效
     *
     * @return
     */
    public boolean isPrivilegeValid() {
        return tradePrivilege != null && tradePrivilege.getStatusFlag() == StatusFlag.VALID;
    }

}
