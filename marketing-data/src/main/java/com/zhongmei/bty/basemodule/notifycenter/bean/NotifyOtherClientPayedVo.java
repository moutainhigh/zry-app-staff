package com.zhongmei.bty.basemodule.notifycenter.bean;

import com.zhongmei.yunfu.db.UuidEntityBase;
import com.zhongmei.yunfu.db.enums.PaySource;

/**
 * Created by demo on 2018/12/15
 * 微信端支付订单信息
 */
public class NotifyOtherClientPayedVo extends UuidEntityBase {
    public String areaName;//add 20180130
    private String tradeUuid;
    private String tableName;
    private String serialNumber;
    private Long payTime;
    public PaySource paySource;


    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public Long verValue() {
        return null;
    }

    public final static class PayContent {
        public PaySource paySource;
        public Long payTime;
    }

}
