package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.ActionType;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.TradeStatus;

import java.util.List;


public class TradeOpsReq {

    private Long tradeId;
    private String tradeUuid;
    private Integer tradeStatus;
    private Long reasonId;
    private String reasonContent;
    private Long clientCreateTime;
    private Long clientUpdateTime;
    private Long serverUpdateTime;
    private Long updatorId;
    private String updatorName;
    private String serialNumber;
    private Integer actionType;
    private Boolean reviseStock;
    private Integer genBatchNo;    public Long waiterId;
    public String waiterName;
    private List<InventoryItemReq> returnInventoryItems;

        private String refuseCode;

        private TradeTableRequest tradeTableRequest;

    public List<InventoryItemReq> getRetInventoryItems() {
        return returnInventoryItems;
    }

    public void setReturnInventoryItems(List<InventoryItemReq> returnInventoryItems) {
        this.returnInventoryItems = returnInventoryItems;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public TradeStatus getTradeStatus() {
        return ValueEnums.toEnum(TradeStatus.class, tradeStatus);
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = ValueEnums.toValue(tradeStatus);
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonContent() {
        return reasonContent;
    }

    public void setReasonContent(String reasonContent) {
        this.reasonContent = reasonContent;
    }

    public Long getClientCreateTime() {
        return clientCreateTime;
    }

    public void setClientCreateTime(Long clientCreateTime) {
        this.clientCreateTime = clientCreateTime;
    }

    public Long getClientUpdateTime() {
        return clientUpdateTime;
    }

    public void setClientUpdateTime(Long clientUpdateTime) {
        this.clientUpdateTime = clientUpdateTime;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public ActionType getActionType() {
        return ValueEnums.toEnum(ActionType.class, actionType);
    }

    public void setActionType(ActionType actionType) {
        this.actionType = ValueEnums.toValue(actionType);
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Boolean getReviseStock() {
        return reviseStock;
    }

    public void setReviseStock(Boolean reviseStock) {
        this.reviseStock = reviseStock;
    }

    public Bool getGenBatchNo() {
        return ValueEnums.toEnum(Bool.class, genBatchNo);
    }

    public void setGenBatchNo(Bool genBatchNo) {
        this.genBatchNo = ValueEnums.toValue(genBatchNo);
    }

    public String getRefuseCode() {
        return refuseCode;
    }

    public void setRefuseCode(String refuseCode) {
        this.refuseCode = refuseCode;
    }

    public TradeTableRequest getTradeTableRequest() {
        return tradeTableRequest;
    }

    public void setTradeTableRequest(TradeTableRequest tradeTableRequest) {
        this.tradeTableRequest = tradeTableRequest;
    }

    public static class TradeTableRequest {
        public long creatorId;
        public String creatorName;
        public String memo;
        public long tableId;
        public String tableName;
        public long waiterId;
        public String waiterName;
        public long tablePeopleCount;
        public long tradeId;
        public String tradeUuid;
        public long updatorId;
        public String updatorName;
        public long brandIdenty;
        public long clientCreateTime;
        public long clientUpdateTime;
        public String deviceIdenty;
        public long serverCreateTime;
        public long serverUpdateTime;
        public long shopIdenty;
        public String uuid;
        public long id;
        public long statusFlag;
        public boolean changed;
    }
}
