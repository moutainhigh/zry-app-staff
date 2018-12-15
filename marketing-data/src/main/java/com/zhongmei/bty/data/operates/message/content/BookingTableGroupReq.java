package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.db.entity.booking.BookingTable;

/**
 * Created by demo on 2018/12/15
 */
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
