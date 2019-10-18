
package com.zhongmei.bty.basemodule.database.print.db;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.util.ValueEnums;

import java.text.SimpleDateFormat;
import java.util.Date;


@DatabaseTable(tableName = "SendData")
public class SendData {
    public static final String COL_UUID = "uuid";
    public static final String COL_CONTENT = "content";
    public static final String ticket_type = "ticket_type";
    public static final String Time = "time";
    public static final String DISHMAP_CONTENT = "dishMap";    public static final String PRINT_TYPE = "printType";    public static final String IS_REPRINT = "is_reprint";

    @DatabaseField(id = true, columnName = COL_UUID, canBeNull = false)
    private String uuid;

    @DatabaseField(columnName = "tradeNo")
    private String tradeNo;

    @DatabaseField(columnName = "batchNo")
    private String batchNo;

    @DatabaseField(columnName = COL_CONTENT)
    private String content;

    @DatabaseField(columnName = "time", dataType = DataType.LONG)
    public long time;

    @DatabaseField(columnName = "ticket_type")
    private Integer ticketType;

    @DatabaseField(columnName = DISHMAP_CONTENT)
    public String dishMapContent;
    @DatabaseField(columnName = "printType")
    public Integer printType;

    @DatabaseField(columnName = IS_REPRINT)
    public Integer isReprint;
    public Integer getPrintType() {
        return printType;
    }

    public void setPrintType(Integer printType) {
        this.printType = printType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Integer getTicketType() {
        return ticketType;
    }

    public void setTicketType(Integer ticketType) {
        this.ticketType = ticketType;
    }

    public Bool isReprint() {
        return ValueEnums.toEnum(Bool.class, isReprint);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SendData other = (SendData) obj;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("uuid = ");
        sb.append(uuid);
        sb.append(", tradeNo = ");
        sb.append(tradeNo);
        sb.append(", batchNo = ");
        sb.append(batchNo);
        sb.append(", saveTime = ");
        sb.append(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(time)));
        return super.toString();
    }
}
