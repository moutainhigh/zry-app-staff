package com.zhongmei.bty.data.operates.message.content;

import java.util.List;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.Bool;

public class DinnerSetTableAndAcceptReq {

    private Long tradeId;

    private Long clientUpdateTime;

    private Long serverUpdateTime;

    private Long updatorId;

    private String updatorName;

    private String serialNumber;

    private List<TradeTable> tradeTables;

    private Integer genBatchNo;

    public DinnerSetTableAndAcceptReq(Long tradeId, Long clientUpdateTime, Long serverUpdateTime, String serialNumber, List<TradeTable> tradeTables) {
        this.tradeId = tradeId;
        this.clientUpdateTime = clientUpdateTime;
        this.serverUpdateTime = serverUpdateTime;
        this.serialNumber = serialNumber;
        this.tradeTables = tradeTables;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public List<TradeTable> getTradeTables() {
        return tradeTables;
    }

    public void setTradeTables(List<TradeTable> tradeTables) {
        this.tradeTables = tradeTables;
    }

    public Bool getGenBatchNo() {
        return ValueEnums.toEnum(Bool.class, genBatchNo);
    }

    public void setGenBatchNo(Bool genBatchNo) {
        this.genBatchNo = ValueEnums.toValue(genBatchNo);
    }
}
