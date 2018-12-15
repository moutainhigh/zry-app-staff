package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.commonmodule.database.entity.PrinterCashierTicket;

/**
 * 修复收银点打印机ID的Resp
 */
public class ModifyCashierPointResp {

    private PrinterCashierTicket printerCashierTicket;

    public PrinterCashierTicket getPrinterCashierTicket() {
        return printerCashierTicket;
    }

    public void setPrinterCashierTicket(PrinterCashierTicket printerCashierTicket) {
        this.printerCashierTicket = printerCashierTicket;
    }
}
