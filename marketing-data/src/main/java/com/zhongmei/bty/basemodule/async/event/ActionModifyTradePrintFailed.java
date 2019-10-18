package com.zhongmei.bty.basemodule.async.event;

import com.zhongmei.bty.basemodule.database.enums.PrintTicketTypeEnum;
import com.zhongmei.bty.basemodule.database.print.db.SendData;


public class ActionModifyTradePrintFailed {

    private String tableName;
    private String serialNumber;

    public SendData receiptSendData;
    public SendData kitchenAllSendData;
    public SendData kitchenCellSendData;
    public SendData labelSendData;

    public PrintTicketTypeEnum receiptTicketType;
    public PrintTicketTypeEnum kitchenTicketType;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

}
