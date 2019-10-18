package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import java.math.BigDecimal;



public class DetailsKeyValue {
    private String name;     private String value;     private BigDecimal count;     private String unit;
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
