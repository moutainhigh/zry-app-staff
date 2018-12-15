package com.zhongmei.bty.basemodule.trade.bean;

/**
 * 单据桌台信息
 * Created by demo on 2018/12/15
 */

public class DinnerTableInfo {
    private Long tableId;
    /**
     * 座位数
     */
    private int tableSeatCount;

    /**
     * 就餐人数
     */
    private int tableMealCount;

    /**
     * 区域名称
     */
    private String tableZoneName;

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public int getTableSeatCount() {
        return tableSeatCount;
    }

    public void setTableSeatCount(int tableSeatCount) {
        this.tableSeatCount = tableSeatCount;
    }

    public int getTableMealCount() {
        return tableMealCount;
    }

    public void setTableMealCount(int tableMealCount) {
        this.tableMealCount = tableMealCount;
    }

    public String getTableZoneName() {
        return tableZoneName;
    }

    public void setTableZoneName(String tableZoneName) {
        this.tableZoneName = tableZoneName;
    }

}
