package com.zhongmei.bty.mobilepay.bean;

import com.zhongmei.yunfu.db.enums.PayModelGroup;

import java.io.Serializable;

public class PayMethodItem implements Serializable {

    public PayModelGroup payModelGroup;    public String methodName;
    public boolean isFocused;
    public boolean isSelected;
    public double cash = 0f;
    public int selectedIconSrc;
    public int unSelectedIconSrc;
    public int pressedIconSrc;
    public int methodId;
    public Integer order;    public boolean enabled = true;

    public int methodResId;

    public PayMethodItem() {
    }

    public PayMethodItem(int menuType) {
        methodId = menuType;    }
}
