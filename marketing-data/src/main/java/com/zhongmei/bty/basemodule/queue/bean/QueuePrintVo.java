package com.zhongmei.bty.basemodule.queue.bean;

import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * 预订打印类
 */
public class QueuePrintVo {
    /**
     * uuid
     */
    private String uuid;

    /**
     * 客户名称
     */
    private String customerName;

    private Integer sex;

    /**
     * 商户名称
     */
    private String shopName;

    /**
     * 队列名称
     */
    private String queueLineName;

    /**
     * 排队号码
     */
    private String queueNo;

    /**
     * 排队人数
     */
    private int repastPersonCount;

    /**
     * 等待桌数
     */
    private int waitingCount;

    /**
     * 二维码URL地址
     */
    private String qrCodeUrl;

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getQueueLineName() {
        return queueLineName;
    }

    public void setQueueLineName(String queueLineName) {
        this.queueLineName = queueLineName;
    }

    public String getQueueNo() {
        return queueNo;
    }

    public void setQueueNo(String queueNo) {
        this.queueNo = queueNo;
    }

    public int getRepastPersonCount() {
        return repastPersonCount;
    }

    public void setRepastPersonCount(int repastPersonCount) {
        this.repastPersonCount = repastPersonCount;
    }

    public int getWaitingCount() {
        return waitingCount;
    }

    public void setWaitingCount(int waitingCount) {
        this.waitingCount = waitingCount;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Sex getSex() {
        return ValueEnums.toEnum(Sex.class, sex);
    }

    public void setSex(Sex sex) {
        this.sex = ValueEnums.toValue(sex);
    }
}
