package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.trade.bean.DinnertableState;

import java.util.List;

/**
 * 用于封装作废正餐请求数据
 *
 * @version: 1.0
 * @date 2015年9月25日
 */
public class RecisionDinnerReq {

    /**
     * 订单id
     */
    private Long tradeId;

    /**
     * 服务器更新时间
     */
    private Long serverUpdateTime;

    /**
     * 最后修改此记录的用户
     */
    private Long updatorId;

    /**
     * 最后修改者姓名
     */
    private String updatorName;

    /**
     * 理由id
     */
    private Long reasonId;

    /**
     * 理由信息
     */
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
