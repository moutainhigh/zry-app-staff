package com.zhongmei.bty.basemodule.discount.bean;

import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.TradePrivilegeApplet;
import com.zhongmei.yunfu.db.enums.StatusFlag;

/**
 * 小程序优惠vo
 * Created by demo on 2018/12/15
 */

public class AppletPrivilegeVo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    TradePrivilege tradePrivilege;

    public TradePrivilege getTradePrivilege() {
        return tradePrivilege;
    }

    public void setTradePrivilege(TradePrivilege tradePrivilege) {
        this.tradePrivilege = tradePrivilege;
    }

    /**
     * 优惠是否有效
     *
     * @return
     */
    public boolean isPrivilegeValid() {
        return tradePrivilege != null && tradePrivilege.getStatusFlag() == StatusFlag.VALID;
    }

    /**
     * 获取微信小程序活动id
     *
     * @return
     */
    public Long getActivityId() {
        if (tradePrivilege != null && tradePrivilege.getPromoId() != null) {
            return tradePrivilege.getPromoId();
        }
        return -1L;
    }
}
