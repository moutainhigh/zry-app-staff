package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.trade.bean.DinnertableState;

import java.util.List;


public class RecisionDinnerReq {


    private Long tradeId;


    private Long serverUpdateTime;


    private Long updatorId;


    private String updatorName;


    private Long reasonId;


    private String reasonContent;

    private List<DinnertableState> tables;

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
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

    public List<DinnertableState> getTables() {
        return tables;
    }

    public void setTables(List<DinnertableState> tables) {
        this.tables = tables;
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

}
