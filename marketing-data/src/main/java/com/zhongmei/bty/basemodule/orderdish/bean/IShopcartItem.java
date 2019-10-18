package com.zhongmei.bty.basemodule.orderdish.bean;

import com.zhongmei.bty.basemodule.orderdish.manager.DishSetmealManager;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;

import java.math.BigDecimal;
import java.util.List;


public interface IShopcartItem extends IShopcartItemBase {

    List<? extends ISetmealShopcartItem> getSetmealItems();

    List<? extends ISetmealShopcartItem> getServerItems();

    DishSetmealManager getSetmealManager();

    boolean hasSetmeal();


    IShopcartItem split();


    void cancelSplit();


    ShopcartItem modifyDish();

    void cancelModifyDish();


    IShopcartItem returnQty(BigDecimal qty, TradeReasonRel reason);


    IShopcartItem returnQty(TradeReasonRel reason);


    void cancelReturnQty();

        String getTradePlanUUID();


    void setTradePlanUUID(String tradePlanUuid);

    void addSetmeal(ISetmealShopcartItem setmealShopcartItem);

    void deleteSetmeal(ISetmealShopcartItem setmealShopcartItem);

    long getClientCreateTime();

}
