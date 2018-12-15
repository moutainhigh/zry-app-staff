package com.zhongmei.beauty.order.vo;

import android.graphics.drawable.Drawable;

/**
 * 中间操作条
 */
public class BeautyOpActionVo {

    /**
     * 会员
     */
    public static final int TYPE_CUSTOMER = 1;

    /**
     * 积分
     */
    public static final int TYPE_INTEGRAL = 2;

    /**
     * 优惠券
     */
    public static final int TYPE_COUPONS = 3;

    /**
     * 折扣
     */
    public static final int TYPE_DISCOUNT = 4;

    /**
     * 活动
     */
    public static final int TYPE_ACTIVITY = 5;

    /**
     * 微信卡劵
     */
    public static final int TYPE_WEIXINCODE = 6;


    /**
     * 附加费
     */
    public static final int TYPE_EXTRA_CHARGE = 7;


    private int type;

    private String name;

    private Drawable drawable;

    private int backgroundResId;

    private boolean enable;

    public BeautyOpActionVo(int type, String name, Drawable drawable) {
        this.type = type;
        this.name = name;
        this.drawable = drawable;
    }

    public BeautyOpActionVo(int type, String name, Drawable drawable, int backgroundResId, boolean enable) {
        this.type = type;
        this.name = name;
        this.drawable = drawable;
        this.backgroundResId = backgroundResId;
        this.enable = enable;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public int getBackgroundResId() {
        return backgroundResId;
    }

    public void setBackgroundResId(int backgroundResId) {
        this.backgroundResId = backgroundResId;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
