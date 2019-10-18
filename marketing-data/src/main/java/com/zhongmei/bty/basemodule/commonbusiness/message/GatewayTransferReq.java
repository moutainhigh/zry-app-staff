package com.zhongmei.bty.basemodule.commonbusiness.message;

import com.zhongmei.yunfu.context.util.SystemUtils;



public class GatewayTransferReq<T> {

    private Integer vender;


    private String platform;


    private String device_id;


    private String version;


    private Long timestamp;


    private String req_id;


    private String sign;

    private T content;

    public GatewayTransferReq(T content) {
        this.vender = 99999;
        this.platform = SystemUtils.getAppType();
        this.device_id = SystemUtils.getMacAddress();
        this.version = "1.14.0";
        this.timestamp = System.currentTimeMillis() / 1000L;
        String uuid = SystemUtils.genOnlyIdentifier();
        this.req_id = uuid;
        this.sign = uuid;
        this.content = content;
    }

    public Integer getVender() {
        return vender;
    }

    public void setVender(Integer vender) {
        this.vender = vender;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getReq_id() {
        return req_id;
    }

    public void setReq_id(String req_id) {
        this.req_id = req_id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
