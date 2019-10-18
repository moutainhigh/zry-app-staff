package com.zhongmei.bty.basemodule.orderdish.bean;

import java.math.BigDecimal;

import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.db.enums.StatusFlag;


public class ReadonlyOrderProperty implements IOrderProperty {

    public final TradeItemProperty tradeItemProperty;

    public ReadonlyOrderProperty(TradeItemProperty tradeItemProperty) {
        this.tradeItemProperty = tradeItemProperty;
    }

    @Override
    public String getPropertyUuid() {
        return tradeItemProperty.getPropertyUuid();
    }

    @Override
    public String getPropertyName() {
        return tradeItemProperty.getPropertyName();
    }

    @Override
    public BigDecimal getPropertyPrice() {
        return tradeItemProperty.getPrice();
    }

    @Override
    public PropertyKind getPropertyKind() {
        return tradeItemProperty.getPropertyType();
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        tradeItemProperty.setStatusFlag(statusFlag);
    }

}
