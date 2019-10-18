package com.zhongmei.yunfu.db.entity.booking;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.EntityBase;
import com.zhongmei.yunfu.context.util.SystemUtils;

import java.util.ArrayList;
import java.util.List;


@DatabaseTable(tableName = "booking_table")
public class BookingTable extends EntityBase<Long> {

    private static final long serialVersionUID = 1L;

    public interface $ {

        String btid = "btid";

        String commercialID = "commercial_id";

        String orderID = "order_id";

        String tableID = "table_id";

        String status = "status";

        String createDateTime = "create_date_time";

        String serverUpdateTime = "server_update_time";

        String uuid = "uuid";

        String bookingUuid = "booking_uuid";
    }


    @DatabaseField(columnName = "btid", id = true, canBeNull = false)
    private Long btid;


    @DatabaseField(columnName = "uuid")
    private String uuid;


    @DatabaseField(columnName = "booking_uuid")
    private String bookingUuid;


    @DatabaseField(columnName = "commercial_id")
    private Long commercialID;


    @DatabaseField(columnName = "order_id")
    private Long orderID;


    @DatabaseField(columnName = "table_id")
    private String tableID;


    @DatabaseField(columnName = "status")
    private Integer status;


    @DatabaseField(columnName = "create_date_time")
    private Long createDateTime;


    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;

    public Long getCommercialID() {
        return commercialID;
    }

    public void setCommercialID(Long commercialID) {
        this.commercialID = commercialID;
    }

    public Long getBtid() {
        return btid;
    }

    public void setBtid(Long btid) {
        this.btid = btid;
    }

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public String getTableID() {
        return tableID;
    }

    public void setTableID(String tableID) {
        this.tableID = tableID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Long createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBookingUuid() {
        return bookingUuid;
    }

    public void setBookingUuid(String bookingUuid) {
        this.bookingUuid = bookingUuid;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    @Override
    public boolean isValid() {
        return status != null && status == 0;
    }

    @Override
    public Long pkValue() {
        return btid;
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(btid);
    }

    public static List<BookingTable> toBookingTables(String bookingUuid, List<Tables> tablesList) {
        List<BookingTable> result = new ArrayList<>();
        for (Tables tVo : tablesList) {
            BookingTable bookingTable = new BookingTable();
            bookingTable.setBookingUuid(bookingUuid);
            bookingTable.setUuid(SystemUtils.genOnlyIdentifier());
            bookingTable.setStatus(0);
            bookingTable.setTableID(tVo.getUuid());
            result.add(bookingTable);
        }
        return result;
    }

}
