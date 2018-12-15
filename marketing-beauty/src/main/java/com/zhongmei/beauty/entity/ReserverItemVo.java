package com.zhongmei.beauty.entity;

import com.zhongmei.beauty.booking.bean.BeautyBookingVo;
import com.zhongmei.beauty.order.view.ReserverTradeView;

/**
 * Created by demo on 2018/12/15
 */

public class ReserverItemVo {
    private float offsetX;
    private float offserY;
    private float width;
    private float height;

    private BeautyBookingVo mReserverVo;

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffserY() {
        return offserY;
    }

    public void setOffserY(float offserY) {
        this.offserY = offserY;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public BeautyBookingVo getmReserverVo() {
        return mReserverVo;
    }

    public void setmReserverVo(BeautyBookingVo mReserverVo) {
        this.mReserverVo = mReserverVo;
    }

    public ReserverItemVo(int offsetX, int offserY, int width, int height) {
        this.offsetX = offsetX;
        this.offserY = offserY;
        this.width = width;
        this.height = height;
    }

    public ReserverItemVo() {
    }

    ;
}
