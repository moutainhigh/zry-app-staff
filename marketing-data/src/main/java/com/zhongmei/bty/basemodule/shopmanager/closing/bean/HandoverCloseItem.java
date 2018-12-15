package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import com.zhongmei.bty.basemodule.database.entity.shopmanager.CashHandoverItem;

/**
 * 关账时交接记录查询
 *
 * @date:2015年12月25日上午10:27:21
 */
public class HandoverCloseItem extends CashHandoverItem {
    /**
     * @date：2015年12月25日 上午10:54:52
     * @Description:TODO
     */
    private static final long serialVersionUID = 1L;

    Long handoverDate;// 交班时间 客户端时间

    String handoverUserName; // 交接用户名

    public Long getHandoverDate() {
        return handoverDate;
    }

    public void setHandoverDate(Long handoverDate) {
        this.handoverDate = handoverDate;
    }

    public String getHandoverUserName() {
        return handoverUserName;
    }

    public void setHandoverUserName(String handoverUserName) {
        this.handoverUserName = handoverUserName;
    }


}
