package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;

import java.util.List;

public class TradeUnionDeleteReq {


    public Long tradeId;


    public Long serverUpdateTime;


    public Long updatorId;


    public String updatorName;


    public Long reasonId;


    public String reasonContent;


    public boolean reviseStock = true;

    public List<InventoryItemReq> returnInventoryItems;

}
