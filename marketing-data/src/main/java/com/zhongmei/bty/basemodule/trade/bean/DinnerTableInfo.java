package com.zhongmei.bty.basemodule.trade.bean;



public class DinnerTableInfo {
    private Long tableId;

    private int tableSeatCount;


    private int tableMealCount;


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
