package com.zhongmei.yunfu.db;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.db.enums.StatusFlag;


public class DataEntityBase extends UuidEntityBase {


    private static final long serialVersionUID = 1L;

    protected interface $ extends UuidEntityBase.$ {


        String statusFlag = "status_flag";


        String brandIdenty = "brand_identy";


        String clientCreateTime = "client_create_time";


        String clientUpdateTime = "client_update_time";


        String id = "id";


        String serverCreateTime = "server_create_time";


        String serverUpdateTime = "server_update_time";


        String shopIdenty = "shop_identy";


        String deviceIdenty = "device_identy";

    }


    @DatabaseField(columnName = "status_flag", canBeNull = false)
    private Integer statusFlag;


    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty;


    @DatabaseField(columnName = "client_create_time")
    private Long clientCreateTime;


    @DatabaseField(columnName = "client_update_time")
    private Long clientUpdateTime;


    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;


    @DatabaseField(columnName = "device_identy")
    private String deviceIdenty;

    @DatabaseField(columnName = "id", index = true)
    private Long id;


    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;


    @DatabaseField(columnName = "server_update_time")
    @SerializedName(value = "serverUpdateTime", alternate = {"updateTime"})
    private Long serverUpdateTime;

    public StatusFlag getStatusFlag() {
        return ValueEnums.toEnum(StatusFlag.class, statusFlag);
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
    }

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    public Long getClientCreateTime() {
        return clientCreateTime;
    }

    public void setClientCreateTime(Long clientCreateTime) {
        this.clientCreateTime = clientCreateTime;
    }

    public Long getClientUpdateTime() {
        return clientUpdateTime;
    }

    public void setClientUpdateTime(Long clientUpdateTime) {
        this.clientUpdateTime = clientUpdateTime;
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public void validateCreate() {
        super.validateCreate();
        setStatusFlag(StatusFlag.VALID);
        setBrandIdenty(ShopInfoManager.getInstance().getShopInfo().getBrandId());
        setShopIdenty(ShopInfoManager.getInstance().getShopInfo().getShopId());
        setDeviceIdenty(ShopInfoManager.getInstance().getShopInfo().getDeviceId());
        setClientCreateTime(System.currentTimeMillis());
    }

    public boolean hasValidateCreate() {
        return clientCreateTime != null;
    }

    @Override
    public void validateUpdate() {
        super.validateUpdate();
        setClientUpdateTime(System.currentTimeMillis());
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && EntityBase.checkNonNull(statusFlag, brandIdenty, shopIdenty);
    }
}
