package com.zhongmei.bty.commonmodule.data.entity;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.commonmodule.database.enums.PrintStatesEnum;


public class ReturnCashierTicketBean {
    public String cashierName;     public String printConfigName;     public Integer errorCode;     public String ipAddress;
    public ReturnCashierTicketBean(String name, String printConfigName, int errorCode) {
        this.cashierName = name;
        this.printConfigName = printConfigName;
        this.errorCode = errorCode;
    }

    public ReturnCashierTicketBean(String name, String printConfigName, String ipAddress, int errorCode) {
        this.cashierName = name;
        this.printConfigName = printConfigName;
        this.errorCode = errorCode;
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
        return "ReturnCashierTicketBean{" +
                "cashierName='" + cashierName + '\'' +
                ", printConfigName='" + printConfigName + '\'' +
                ", errorCode=" + errorCode + "(" + ValueEnums.toEnum(PrintStatesEnum.class, errorCode) + ")" +
                '}';
    }
}
