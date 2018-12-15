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

/**
 * 订单中心订单列表过滤条件
 */

public class FilterCondition {
    //筛选条件：配送类型
    private List<DeliveryType> deliveryTypes;

    //筛选条件：订单来源
    private List<SourceId> sourceIds;
    //筛选条件: 配送状态(新)
    private List<DeliveryOrderStatus> deliveryOrderStatuses;

    private List<DeliveryOrderSubStatus> deliveryOrderSubStatuses;

    //特殊过滤条件：配送状态用于快餐清账
    private DeliveryStatus deliveryStatus;

    //特殊过滤条件：是否绑定外卖员
    private boolean hasBindDeliveryUser;

    //特殊过滤条件：是否排除POS来源的订单
    private boolean excludePosOrder;

    //特殊过滤条件：支付来源用于清账
    private PaySource paySource;

    //特殊过滤条件：支付方式用于清账
    private PayModeId payModeId;

    //特殊过滤条件：最后修改此记录的用户名字
    private long updatorId;

    //特殊过滤条件：交易状态用于正餐清账筛选
    private List<TradeStatus> tradeStatus;

    //特殊过滤条件:配送员信息

    private Object[] deliveryInfo;
    //特殊过滤条件:是否展示离线订单
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

    /**
     * 判断是否是清账模式
     *
     * @return true：处于清账模式；反之为false
     */
    public boolean isCleanMode() {
        return (DeliveryStatus.REAL_DELIVERY == deliveryStatus
                || (Utils.isNotEmpty(tradeStatus) && tradeStatus.contains(TradeStatus.SQUAREUP)))
                && PayModeId.CASH == payModeId
                && PaySource.ON_MOBILE == paySource;
    }


}
