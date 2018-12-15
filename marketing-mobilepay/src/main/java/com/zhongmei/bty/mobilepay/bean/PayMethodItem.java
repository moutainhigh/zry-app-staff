package com.zhongmei.bty.mobilepay.bean;

import com.zhongmei.yunfu.db.enums.PayModelGroup;

import java.io.Serializable;

public class PayMethodItem implements Serializable {

    public PayModelGroup payModelGroup;//与methodId映射
    public String methodName;
    public boolean isFocused;
    public boolean isSelected;
    public double cash = 0f;
    public int selectedIconSrc;
    public int unSelectedIconSrc;
    public int pressedIconSrc;
    public int methodId;
    public Integer order;//add20170119
    public boolean enabled = true;//add 20170411
//    -----follow[yanm]---------------

    /**
     * 支付方式的图标(实际项目中，代表着支付方式的Drawable)
     */
    public int methodResId;

    public PayMethodItem() {
    }

    public PayMethodItem(int menuType) {
        methodId = menuType;// 菜单类别
    }
}
