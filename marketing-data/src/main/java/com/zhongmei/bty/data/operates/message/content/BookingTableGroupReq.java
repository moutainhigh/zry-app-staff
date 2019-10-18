package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.db.entity.booking.BookingTable;


public class BookingTableGroupReq extends BookingTable {

    private Long id;

    private String tableId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
}
