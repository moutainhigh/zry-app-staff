package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;

import java.util.List;

public class TradeUnionDeleteReq {

    /**
     * 订单id
     */
    public Long tradeId;

    /**
     * 服务器更新时间
     */
    public Long serverUpdateTime;

    /**
     * 最后修改此记录的用户
     */
    public Long updatorId;

    /**
     * 最后修改者姓名
     */
    public String updatorName;

    /**
     * 理由id
     */
    public Long reasonId;

    /**
     * 理由信息
     */
    public String reasonContent;

    /**
     * 是否自动清台，默认写死为true
     */
    public boolean reviseStock = true;

    public List<InventoryItemReq> returnInventoryItems;

}
