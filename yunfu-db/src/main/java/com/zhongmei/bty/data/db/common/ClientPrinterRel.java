package com.zhongmei.bty.data.db.common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.ServerEntityBase;
import com.zhongmei.bty.entity.enums.PrintType;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * 客户端设备打印机配置关系表，存储客户端所指定的打印设备关系
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "client_printer_rel")
public class ClientPrinterRel extends ServerEntityBase {

    public interface $ extends ServerEntityBase.$ {
        public static final String clientDeviceIdetity = "client_device_identy";

        public static final String printerDeviceId = "printer_device_id";

        public static final String printerDeviceAddress = "printer_device_address";

        public static final String documentCategory = "document_category";
    }

    @DatabaseField(columnName = "client_device_identy", canBeNull = false)
    private String clientDeviceIdenty;

    @DatabaseField(columnName = "printer_device_id", canBeNull = false, defaultValue = "-1")
    private Long printerDeviceId;

    @DatabaseField(columnName = "printer_device_address", canBeNull = false)
    private String printerDeviceAddress;

    @DatabaseField(columnName = "document_category", canBeNull = false)
    private Integer documentCategory;

    public String getClientDeviceIdenty() {
        return clientDeviceIdenty;
    }

    public void setClientDeviceIdenty(String clientDeviceIdenty) {
        this.clientDeviceIdenty = clientDeviceIdenty;
    }

    public Long getPrinterDeviceId() {
        return printerDeviceId;
    }

    public void setPrinterDeviceId(Long printerDeviceId) {
        this.printerDeviceId = printerDeviceId;
    }

    public String getPrinterDeviceAddress() {
        return printerDeviceAddress;
    }

    public void setPrinterDeviceAddress(String printerDeviceAddress) {
        this.printerDeviceAddress = printerDeviceAddress;
    }

    public PrintType getDocumentCategory() {
        return ValueEnums.toEnum(PrintType.class, documentCategory);
    }

    public void setDocumentCategory(PrintType documentCategory) {
        this.documentCategory = ValueEnums.toValue(documentCategory);
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(clientDeviceIdenty, printerDeviceId, printerDeviceAddress, documentCategory);
    }
}
