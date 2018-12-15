package com.zhongmei.bty.commonmodule.data.entity;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.commonmodule.database.enums.PrintStatesEnum;

/**
 * 调用打印后返回给正快餐的具体错误信息
 */
public class ReturnCashierTicketBean {
    public String cashierName; //收银点或者出票口名称
    public String printConfigName; //针对是否是标签还是收银点出票口
    public Integer errorCode; //针对每一个出票口或者收银点拆单后的错误码
    public String ipAddress; //出票口的打印机ip地址,可能为空,主要用来判断直连还是打印服务用

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
