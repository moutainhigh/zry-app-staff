package com.zhongmei.bty.basemodule.notifycenter.event;

import com.zhongmei.bty.basemodule.notifycenter.enums.NotificationType;
import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.yunfu.db.enums.DeliveryType;

import java.util.Arrays;
import java.util.List;

public class ActionShowTrade {

    String uuid;

    private DeliveryType type;

        private int orderType;
        private NotificationType notificationType;
    private List<ValueEnum> filterConditions;

    public ActionShowTrade(String uuid, DeliveryType type, int orderType) {
        this.uuid = uuid;
        this.type = type;
        this.orderType = orderType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public List<ValueEnum> getFilterConditions() {
        return filterConditions;
    }

    public void setFilterConditions(List<ValueEnum> filterConditions) {
        this.filterConditions = filterConditions;
    }

    public void setFilterConditions(ValueEnum... filterConditions) {
        setFilterConditions(Arrays.asList(filterConditions));
    }

    public DeliveryType getType() {
        return type;
    }

    public void setType(DeliveryType type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String tradeUuid) {
        this.uuid = tradeUuid;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

}
