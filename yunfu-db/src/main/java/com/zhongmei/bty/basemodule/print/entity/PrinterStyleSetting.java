package com.zhongmei.bty.basemodule.print.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.UuidEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;


@DatabaseTable(tableName = "printer_style_setting")
public class PrinterStyleSetting extends UuidEntityBase {
    private static final long serialVersionUID = 1L;


    public interface $ extends UuidEntityBase.$ {


        public static final String id = "id";


        public static final String printerType = "printer_type";


        public static final String styleId = "style_id";


        public static final String serverCreateTime = "server_create_time";


        public static final String serverUpdateTime = "server_update_time";


        public static final String statusFlag = "status_flag";


        public static final String brandIdenty = "brand_identy";


        public static final String shopIdenty = "shop_identy";


        public static final String deviceIdenty = "device_identy";


        public static final String updatorName = "updator_name";






        public static final String updatorId = "updator_id";





    }

    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    @DatabaseField(columnName = "device_identy")
    private String deviceIdenty;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;
    

    


    @DatabaseField(columnName = "updator_id")
    private String updatorId;

    @DatabaseField(columnName = "id")
    private Long id;

    @DatabaseField(columnName = "status_flag", canBeNull = false)
    private Integer statusFlag;

    @DatabaseField(columnName = "printer_type", canBeNull = false)
    private Integer printerType;

    @DatabaseField(columnName = "style_id", canBeNull = false)
    private Integer styleId;

    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;

    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;
    


    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty;


    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public String getDeviceIdenty() {
        return deviceIdenty;
    }

    public void setDeviceIdenty(String deviceIdenty) {
        this.deviceIdenty = deviceIdenty;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }



    public String getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(String updatorId) {
        this.updatorId = updatorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

    public Integer getPrinterType() {
        return printerType;
    }

    public void setPrinterType(Integer printerType) {
        this.printerType = printerType;
    }

    public Integer getStyleId() {
        return styleId;
    }

    public void setStyleId(Integer styleId) {
        this.styleId = styleId;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }



    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    public void validateCreate() {
        setStatusFlag(StatusFlag.VALID.value());
        setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        setDeviceIdenty(BaseApplication.sInstance.getDeviceIdenty());
                if (this instanceof ICreator) {
            AuthUser user = Session.getAuthUser();
            if (user != null) {
                ICreator creator = (ICreator) this;
                creator.setCreatorId(user.getId());
                creator.setCreatorName(user.getName());
            }
        }
        validateUpdate();
    }

    public void validateUpdate() {
                setChanged(true);
        if (this instanceof IUpdator) {
            AuthUser user = Session.getAuthUser();
            if (user != null) {
                IUpdator updator = (IUpdator) this;
                updator.setUpdatorId(user.getId());
                updator.setUpdatorName(user.getName());
            }
        }
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(shopIdenty, statusFlag, printerType, styleId, brandIdenty);
    }
}
