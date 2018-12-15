package com.zhongmei.bty.commonmodule.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.R;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.bty.commonmodule.database.enums.ConnectionType;
import com.zhongmei.bty.commonmodule.database.enums.HealthStatus;
import com.zhongmei.bty.commonmodule.database.enums.PrinterDeviceModel;
import com.zhongmei.bty.commonmodule.database.enums.PrinterDeviceType;
import com.zhongmei.bty.commonmodule.database.enums.PrinterKind;
import com.zhongmei.yunfu.db.enums.StatusFlag;

/**
 * PrinterDevice is a ORMLite bean type. Corresponds to the database table "printer_device"
 */
@DatabaseTable(tableName = "printer_device")
public class PrinterDevice extends DataEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "printer_device"
     */
    public interface $ extends DataEntityBase.$ {

        /**
         * address
         */
        public static final String address = "address";

        /**
         * connection_type
         */
        public static final String connectionType = "connection_type";

        /**
         * creator_id
         */
        public static final String creatorId = "creator_id";

        /**
         * creator_name
         */
        public static final String creatorName = "creator_name";

        /**
         * device_name
         */
        public static final String deviceName = "device_name";

        /**
         * printer_device_type
         */
        public static final String printerDeviceType = "printer_device_type";

        /**
         * printer_kind
         */
        public static final String printerKind = "printer_kind";

        /**
         * updator_id
         */
        public static final String updatorId = "updator_id";

        /**
         * updator_name
         */
        public static final String updatorName = "updator_name";

        /**
         * paper_size
         */
        public static final String paperSize = "paper_size";

        public static final String healthStatus = "health_status";

        /**
         * ring
         */
        public static final String isOpenRing = "is_open_ring";

        public static final String ringCount = "ring_count";

        public static final String printerDeviceModel = "printer_device_model";


    }

    @DatabaseField(columnName = "address")
    private String address;

    @DatabaseField(columnName = "connection_type")
    private Integer connectionType;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "device_name")
    private String deviceName;

    @DatabaseField(columnName = "printer_device_type")
    private Integer printerDeviceType;

    @DatabaseField(columnName = "printer_kind")
    private Integer printerKind;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "paper_size")
    private Integer paperSize;

    @DatabaseField(columnName = "health_status", defaultValue = "1")
    private Integer healthStatus;

    @DatabaseField(columnName = "is_open_ring", defaultValue = "0")
    private Integer isOpenRing;

    @DatabaseField(columnName = "ring_count")
    public Integer ringCount; // 响铃次数，后续同步会增加该字段

    @DatabaseField(columnName = "printer_device_model")
    private Integer printerDeviceModel; //打印机型号 用于在打印时设置行间距


    public Integer getRingCount() {
        return ringCount;
    }

    public void setRingCount(Integer ringCount) {
        this.ringCount = ringCount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ConnectionType getConnectionType() {
        return ValueEnums.toEnum(ConnectionType.class, connectionType);
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = ValueEnums.toValue(connectionType);
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public PrinterDeviceType getPrinterDeviceType() {
        return ValueEnums.toEnum(PrinterDeviceType.class, printerDeviceType);
    }

    public void setPrinterDeviceType(PrinterDeviceType printerDeviceType) {
        this.printerDeviceType = ValueEnums.toValue(printerDeviceType);
    }

    public PrinterKind getPrinterKind() {
        return ValueEnums.toEnum(PrinterKind.class, printerKind);
    }

    public void setPrinterKind(PrinterKind printerKind) {
        this.printerKind = ValueEnums.toValue(printerKind);
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public Integer getPaperSize() {
        return paperSize;
    }

    public void setPaperSize(Integer paperSize) {
        this.paperSize = paperSize;
    }

    public HealthStatus getHealthStatus() {
        return ValueEnums.toEnum(HealthStatus.class, healthStatus);
    }

    public void setHealthStatus(HealthStatus healthStatus) {
        this.healthStatus = ValueEnums.toValue(healthStatus);
    }


    public PrinterDeviceModel getPrinterDeviceModel() {
        if (printerDeviceModel == null) {
            return PrinterDeviceModel.OTHRE;
        }
        PrinterDeviceModel deviceModel = ValueEnums.toEnum(PrinterDeviceModel.class, printerDeviceModel);
        if (null == deviceModel) {
            return PrinterDeviceModel.OTHRE;
        }
        return deviceModel;
    }

    public void setPrinterDeviceModel(Integer printerDeviceModel) {
        this.printerDeviceModel = printerDeviceModel;
    }

    public boolean getIsOpenRing() {
        if (isOpenRing != null && isOpenRing == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void setIsOpenRing(boolean isOpenRing) {
        this.isOpenRing = isOpenRing ? 1 : 0;
    }

    public void validateCreate() {
        setStatusFlag(StatusFlag.VALID);
        setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        setClientCreateTime(System.currentTimeMillis());
        if (this instanceof ICreator) {
            IAuthUser user = IAuthUser.Holder.get();
            if (user != null) {
                ICreator creator = (ICreator) this;
                creator.setCreatorId(user.getId());
                creator.setCreatorName(user.getName());
            }
        }
        validateUpdate();
    }

    /*
     *获取打印机类型名称
     */
    public String getPrintModelName() {
        switch (getPrinterDeviceModel()) {

            case WPT_990:
                return BaseApplication.sInstance.getString(R.string.commonmodule_setting_print_kr_900);
            case WPT_810:
                return BaseApplication.sInstance.getString(R.string.commonmodule_setting_print_kr_810);
            case KR_610:
                return BaseApplication.sInstance.getString(R.string.commonmodule_setting_print_kr_860);
            case OTHRE:
                return BaseApplication.sInstance.getString(R.string.commonmodule_setting_print_kr_other);
            case WPT_300i:
                return BaseApplication.sInstance.getString(R.string.commonmodule_setting_print_kr_300);
            default:
                return BaseApplication.sInstance.getString(R.string.commonmodule_setting_print_kr_unknow);
        }
    }

}

