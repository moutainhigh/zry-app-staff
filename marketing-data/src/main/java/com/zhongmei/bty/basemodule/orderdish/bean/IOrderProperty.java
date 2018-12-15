package com.zhongmei.bty.basemodule.orderdish.bean;

import java.math.BigDecimal;

import com.zhongmei.yunfu.db.enums.PropertyKind;

/**
 * @version: 1.0
 * @date 2015年9月11日
 */
public interface IOrderProperty {

    String getPropertyUuid();

    String getPropertyName();

    BigDecimal getPropertyPrice();

    PropertyKind getPropertyKind();

}