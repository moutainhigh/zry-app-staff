package com.zhongmei.bty.commonmodule.database.entity.local;

import java.io.Serializable;


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

        private String indentifer;

        private int classType;

        private String orderNum;

        private String content;

        private String ip;

        private String printerName;

        private int printStatus;
        private String saveTime;

        private long saveMillisTime;

        private String ticketName;

        private String uuid;

        private String serialNumber;

        private String faultCause;

    public PrintInfo() {
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
