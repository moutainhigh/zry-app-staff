package com.zhongmei.bty.commonmodule.database.entity.local;

import java.io.Serializable;

/**
 * 此类用于从打印服务传递数据， 变量需要和打印服务的printInfo类保持一致
 */
public class PrintInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int STATUS_ING = 0;

    public static final int STATUS_HAVE = 1;

    public static final int STATUS_WAIT = 2;

    public static final int STATUS_ERROR = -1;

    public static final int STATUS_REING = 3;

    public static final String TABLE_NAME = "print_info";

    public static final String COL_INDENTIFER = "indentifer";

    public static final String COL_CLASS_TYPE = "classType";

    public static final String COL_ORDER_NUM = "orderNum";

    public static final String COL_CONTENT = "content";

    public static final String COL_IP = "ip";

    public static final String COL_PRINTER_NAME = "printerName";

    public static final String COL_PRINT_STATUS = "printStatus";

    public static final String COL_SAVE_TIME = "saveTime";

    public static final String COL_SAVE_MILLIS_TIME = "saveMillisTime";

    public static final String COL_TICKET_NAME = "ticketName";

    public static final String COL_UUID = "uuid";

    public static final String COL_SERIAL_NUMBER = "serialNumber";

    public static final String COL_FAULT_CAUSE = "faultCause";

    //	@DatabaseField(id = true, columnName = COL_INDENTIFER)
    private String indentifer;

    //	@DatabaseField(columnName = COL_CLASS_TYPE)
    private int classType;

    //	@DatabaseField(columnName = COL_ORDER_NUM)
    private String orderNum;

    //	@DatabaseField(columnName = COL_CONTENT)
    private String content;

    //	@DatabaseField(columnName = COL_IP)
    private String ip;

    //	@DatabaseField(columnName = COL_PRINTER_NAME)
    private String printerName;

    //	@DatabaseField(columnName = COL_PRINT_STATUS, dataType = DataType.INTEGER)
    private int printStatus; // 0:未打印 1：已经打印 2：等待打印 -1:打印故障

    //	@DatabaseField(columnName = COL_SAVE_TIME)
    private String saveTime;

    //	@DatabaseField(columnName = COL_SAVE_MILLIS_TIME)
    private long saveMillisTime;

    //	@DatabaseField(columnName = COL_TICKET_NAME)
    private String ticketName;

    //	@DatabaseField(columnName = COL_UUID)
    private String uuid;

    //	@DatabaseField(columnName = COL_SERIAL_NUMBER, canBeNull = true)
    private String serialNumber;

    //	@DatabaseField(columnName = COL_FAULT_CAUSE, canBeNull = true)
    private String faultCause;

    public PrintInfo() {
        // TODO Auto-generated constructor stub
    }

    public String getIndentifer() {
        return indentifer;
    }

    public void setIndentifer(String indentifer) {
        this.indentifer = indentifer;
    }

    public int getClassType() {
        return classType;
    }

    public void setClassType(int classType) {
        this.classType = classType;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(int printStatus) {
        this.printStatus = printStatus;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }

    public long getSaveMillisTime() {
        return saveMillisTime;
    }

    public void setSaveMillisTime(long saveMillisTime) {
        this.saveMillisTime = saveMillisTime;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getFaultCause() {
        return faultCause;
    }

    public void setFaultCause(String faultCause) {
        this.faultCause = faultCause;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.uuid == null) ? 0 : this.uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof PrintInfo) {
            PrintInfo i = (PrintInfo) o;
            return this.indentifer.equals(i.indentifer);
        } else {
            return false;
        }
    }

}
