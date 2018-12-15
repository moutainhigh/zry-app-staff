package com.zhongmei.yunfu.http;

import com.zhongmei.yunfu.context.base.BaseApplication;

/**
 * 实时请求数据的封装类
 *
 * @param <T>
 */
public class QSRequestObject<T> {

    private Long brandId;
    private Long shopId;
    private String deviceId;
    private T content;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public static <T> QSRequestObject<T> create(T content) {
        QSRequestObject<T> request = new QSRequestObject<T>();
        request.setBrandId(BaseApplication.sInstance.getBrandIdenty());
        request.setShopId(BaseApplication.sInstance.getShopIdenty());
        request.setDeviceId(BaseApplication.sInstance.getDeviceIdenty());
        request.setContent(content);
        return request;
    }
}
