package com.zhongmei.bty.basemodule.booking.message;

/**
 * 封装预订创建或修改请求
 */
public class BookingTableReq {

    /**
     * BookingTableId
     */
    private Long bookingTableId;

    /**
     * table_id
     */
    private Long tableLocalId;

    /**
     * table_uuid
     */
    private String tableServerId;

    /**
     * tableName
     */
    private String tableName;

    /**
     * 预订桌台状态
     */
    private int status;

    private String tableId;    //桌台唯一 ID

    private Long id;

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getBookingTableId() {
        return bookingTableId;
    }

    public void setBookingTableId(Long bookingTableId) {
        this.bookingTableId = bookingTableId;
    }

    public Long getTableLocalId() {
        return tableLocalId;
    }

    public void setTableLocalId(Long tableLocalId) {
        this.tableLocalId = tableLocalId;
    }

    public String getTableServerId() {
        return tableServerId;
    }

    public void setTableServerId(String tableServerId) {
        this.tableServerId = tableServerId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
