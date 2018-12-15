package com.zhongmei.yunfu.db;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.db.enums.StatusFlag;

/**
 *

 *
 */
public class DataEntityBase extends UuidEntityBase {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected interface $ extends UuidEntityBase.$ {

        /**
         * status_flag
         */
        String statusFlag = "status_flag";

        /**
         * brand_identy
         */
        String brandIdenty = "brand_identy";

        /**
         * client_create_time
         */
        String clientCreateTime = "client_create_time";

        /**
         * client_update_time
         */
        String clientUpdateTime = "client_update_time";

        /**
         * id
         */
        String id = "id";

        /**
         * server_create_time
         */
        String serverCreateTime = "server_create_time";

        /**
         * server_update_time
         */
        String serverUpdateTime = "server_update_time";

        /**
         * shop_identy
         */
        String shopIdenty = "shop_identy";

        /**
         * device_identy
         */
        String deviceIdenty = "device_identy";

    }

    /**
     * 状态
     */
    @DatabaseField(columnName = "status_flag", canBeNull = false)
    private Integer statusFlag;

    /**
     * 品牌Identy
     */
    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty;

    /**
     * 客户端创建时间
     */
    @DatabaseField(columnName = "client_create_time")
    private Long clientCreateTime;

    /**
     * 客户端最后修改时间
     */
    @DatabaseField(columnName = "client_update_time")
    private Long clientUpdateTime;

    /**
     * 门店Identy
     */
    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    /**
     * 设备Identy
     */
    @DatabaseField(columnName = "device_identy")
    private String deviceIdenty;

    @DatabaseField(columnName = "id", index = true)
    private Long id;

    /**
     * 服务器创建时间
     */
    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;

    /**
     * 服务器最后修改时间
     */
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
