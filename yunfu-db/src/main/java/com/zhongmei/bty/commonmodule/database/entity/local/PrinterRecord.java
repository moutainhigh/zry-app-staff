package com.zhongmei.bty.commonmodule.database.entity.local;

import com.j256.ormlite.field.DatabaseField;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.commonmodule.database.enums.PrintStatesEnum;



public class PrinterRecord extends LocalEntityBase implements Cloneable {

    public interface $ extends LocalEntityBase.$ {
        public static final String cashierId = "cashier_id";

        public static final String cashierName = "cashier_name";

        public static final String printConfigName = "print_config_name";

        public static final String errorCode = "error_code";

        public static final String errorMessage = "error_message";

        public static final String deviceIpAddress = "device_ip_address";

        public static final String printTicketType = "print_ticket_type";
    }


    @DatabaseField(columnName = "cashier_id")
    public Long cashierId;


    @DatabaseField(columnName = "cashier_name")
    public String cashierName;


    @DatabaseField(columnName = "print_config_name")
    public String printConfigName;


    @DatabaseField(columnName = "error_code", canBeNull = false)
    public Integer errorCode;


    @DatabaseField(columnName = "error_message")
    public String errorMessage;


    @DatabaseField(columnName = "device_ip_address")
    public String deviceIpAddress;


    @DatabaseField(columnName = "print_ticket_type")
    public String printTicketType;


    public Long getCashierId() {
        return cashierId;
    }

    public void setCashierId(Long cashierId) {
        this.cashierId = cashierId;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getPrintConfigName() {
        return printConfigName;
    }

    public void setPrintConfigName(String printConfigName) {
        this.printConfigName = printConfigName;
    }

    public PrintStatesEnum getErrorCode() {
        return ValueEnums.toEnum(PrintStatesEnum.class, errorCode);
    }

    public void setErrorCode(PrintStatesEnum printState) {
        this.errorCode = ValueEnums.toValue(printState);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getDeviceIpAddress() {
        return deviceIpAddress;
    }

    public void setDeviceIpAddress(String deviceIpAddress) {
        this.deviceIpAddress = deviceIpAddress;
    }

    public String getPrintTicketType() {
        return printTicketType;
    }

    public void setPrintTicketType(String printTicketType) {
        this.printTicketType = printTicketType;
    }
}
