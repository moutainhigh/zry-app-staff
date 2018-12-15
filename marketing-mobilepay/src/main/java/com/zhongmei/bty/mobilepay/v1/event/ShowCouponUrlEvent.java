package com.zhongmei.bty.mobilepay.v1.event;

/**
 * @Date： 16/9/27
 * @Description:
 * @Version: 1.0
 */
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
