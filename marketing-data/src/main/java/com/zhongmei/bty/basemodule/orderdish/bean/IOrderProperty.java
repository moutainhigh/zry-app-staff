package com.zhongmei.bty.basemodule.orderdish.bean;

import java.math.BigDecimal;

import com.zhongmei.yunfu.db.enums.PropertyKind;


public interface IOrderProperty {

    String getPropertyUuid();

    String getPropertyName();

    BigDecimal getPropertyPrice();

    PropertyKind getPropertyKind();

}