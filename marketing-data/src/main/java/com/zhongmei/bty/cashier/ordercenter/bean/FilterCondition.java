package com.zhongmei.bty.cashier.ordercenter.bean;

import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderStatus;
import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderSubStatus;
import com.zhongmei.yunfu.db.enums.DeliveryStatus;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.PaySource;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.List;


public class FilterCondition {
    private List<DeliveryType> deliveryTypes;

    private List<SourceId> sourceIds;
    private List<DeliveryOrderStatus> deliveryOrderStatuses;

    private List<DeliveryOrderSubStatus> deliveryOrderSubStatuses;

    private DeliveryStatus deliveryStatus;

    private boolean hasBindDeliveryUser;

    private boolean excludePosOrder;

    private PaySource paySource;

    private PayModeId payModeId;

    private long updatorId;

    private List<TradeStatus> tradeStatus;


    private Object[] deliveryInfo;
    private boolean excludeOfflineOrder;

    public Object[] getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(Object[] deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public List<SourceId> getSourceIds() {
        return sourceIds;
    }

    public void setSourceIds(List<SourceId> sourceIds) {
        this.sourceIds = sourceIds;
    }

    public List<DeliveryType> getDeliveryTypes() {
        return deliveryTypes;
    }

    public void setDeliveryTypes(List<DeliveryType> deliveryTypes) {
        this.deliveryTypes = deliveryTypes;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public boolean isHasBindDeliveryUser() {
        return hasBindDeliveryUser;
    }

    public void setHasBindDeliveryUser(boolean hasBindDeliveryUser) {
        this.hasBindDeliveryUser = hasBindDeliveryUser;
    }

    public PaySource getPaySource() {
        return paySource;
    }

    public void setPaySource(PaySource paySource) {
        this.paySource = paySource;
    }

    public PayModeId getPayModeId() {
        return payModeId;
    }

    public void setPayModeId(PayModeId payModeId) {
        this.payModeId = payModeId;
    }

    public long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(long updatorId) {
        this.updatorId = updatorId;
    }

    public List<TradeStatus> getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(List<TradeStatus> tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public List<DeliveryOrderStatus> getDeliveryOrderStatuses() {
        return deliveryOrderStatuses;
    }

    public void setDeliveryOrderStatuses(List<DeliveryOrderStatus> deliveryOrderStatuses) {
        this.deliveryOrderStatuses = deliveryOrderStatuses;
    }

    public List<DeliveryOrderSubStatus> getDeliveryOrderSubStatuses() {
        return deliveryOrderSubStatuses;
    }

    public void setDeliveryOrderSubStatuses(List<DeliveryOrderSubStatus> deliveryOrderSubStatuses) {
        this.deliveryOrderSubStatuses = deliveryOrderSubStatuses;
    }

    public boolean isExcludePosOrder() {
        return excludePosOrder;
    }

    public void setExcludePosOrder(boolean excludePosOrder) {
        this.excludePosOrder = excludePosOrder;
    }

    public boolean isExcludeOfflineOrder() {
        return excludeOfflineOrder;
    }

    public void setExcludeOfflineOrder(boolean excludeOfflineOrder) {
        this.excludeOfflineOrder = excludeOfflineOrder;
    }


    public boolean isCleanMode() {
        return (DeliveryStatus.REAL_DELIVERY == deliveryStatus
                || (Utils.isNotEmpty(tradeStatus) && tradeStatus.contains(TradeStatus.SQUAREUP)))
                && PayModeId.CASH == payModeId
                && PaySource.ON_MOBILE == paySource;
    }


}
