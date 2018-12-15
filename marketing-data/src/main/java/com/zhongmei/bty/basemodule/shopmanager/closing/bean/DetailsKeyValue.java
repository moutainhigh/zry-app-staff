package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 * 关账明细Item
 */

public class DetailsKeyValue {
    private String name; //显示名称
    private String value; //显示的值
    private BigDecimal count; //数量
    private String unit; //单位

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
