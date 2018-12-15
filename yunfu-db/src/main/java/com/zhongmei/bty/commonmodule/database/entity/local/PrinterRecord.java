package com.zhongmei.bty.commonmodule.database.entity.local;

import com.j256.ormlite.field.DatabaseField;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.commonmodule.database.enums.PrintStatesEnum;

/**
 * Created by demo on 2018/12/15
 * 打印失败记录表
 */

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

    /**
     * 收银点ID
     */
    @DatabaseField(columnName = "cashier_id")
    public Long cashierId;

    /**
     * 收银点或者出票口名称
     */
    @DatabaseField(columnName = "cashier_name")
    public String cashierName;

    /**
     * 针对是否是标签还是收银点出票口
     */
    @DatabaseField(columnName = "print_config_name")
    public String printConfigName;

    /**
     * 针对每一个出票口或者收银点拆单后的错误码
     */
    @DatabaseField(columnName = "error_code", canBeNull = false)
    public Integer errorCode;

    /**
     * 错误信息
     */
    @DatabaseField(columnName = "error_message")
    public String errorMessage;

    /**
     * 出票口的打印机ip地址,可能为空,主要用来判断直连还是打印服务用
     */
    @DatabaseField(columnName = "device_ip_address")
    public String deviceIpAddress;

    /**
     * 票据类型
     */
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
