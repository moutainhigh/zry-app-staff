package com.zhongmei.bty.mobilepay.v1.event;


public class ShowCouponUrlEvent {

    private String url;

    public ShowCouponUrlEvent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
