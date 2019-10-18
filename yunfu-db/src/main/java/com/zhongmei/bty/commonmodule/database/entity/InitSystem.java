package com.zhongmei.bty.commonmodule.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.EntityBase;
import com.zhongmei.yunfu.util.ValueEnums;

import com.zhongmei.bty.commonmodule.database.enums.InitSystemDeviceType;
import com.zhongmei.bty.commonmodule.database.enums.InitSystemEntityBase;
import com.zhongmei.yunfu.db.enums.IsDelete;

import java.math.BigDecimal;


@DatabaseTable(tableName = "init_system")
public class InitSystem extends InitSystemEntityBase {
    private static final long serialVersionUID = 1L;


    public interface $ {
        String initID = "initID";
        String commercialID = "commercialID";
        String deviceID = "deviceID";
        String padNo = "padNo";
        String status = "status";
        String createDateTime = "createDateTime";
        String modifyDateTime = "modifyDateTime";
        String secretKey = "secretKey";
        String autobind = "autobind";
        String clientId = "clientId";
        String deviceType = "deviceType";
        String longitude = "longitude";
        String latitude = "latitude";
        String location = "location";
        String module = "module";
        String isMainPos = "isMainPos";
        String creatorId = "creator_id";
        String updaterId = "updater_id";
        String goodsNo = "goods_no";
        String isCashierEquip = "is_cashier_equip";
        String applicationTypeId = "application_type_id";
        String bindDeviceType = "bind_device_type";
        String isBindPos = "is_bind_pos";
        String posNo = "pos_no";
        String isKdsDevice = "is_kds_device";
    }

    @DatabaseField(columnName = "initID", id = true, canBeNull = false)
    private Long initID;

    @DatabaseField(columnName = "commercialID")
    private Long commercialID;

    @DatabaseField(columnName = "deviceID")
    private String deviceID;

    @DatabaseField(columnName = "padNo")
    private Long padNo;

    @DatabaseField(columnName = "status")
    private Integer status;

    @DatabaseField(columnName = "createDateTime")
    private Long createDateTime;

    @DatabaseField(columnName = "modifyDateTime")
    private Long modifyDateTime;

    @DatabaseField(columnName = "secretKey")
    private String secretKey;

    @DatabaseField(columnName = "autobind")
    private String autobind;

    @DatabaseField(columnName = "clientId")
    private String clientId;

    @DatabaseField(columnName = "deviceType")
    private Integer deviceType;

    @DatabaseField(columnName = "longitude")
    private BigDecimal longitude;

    @DatabaseField(columnName = "latitude")
    private BigDecimal latitude;

    @DatabaseField(columnName = "location")
    private String location;

    @DatabaseField(columnName = "module")
    private Integer module;

    @DatabaseField(columnName = "isMainPos")
    private Integer isMainPos;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "updater_id")
    private Long updatorId;

    @DatabaseField(columnName = "goods_no")
    private String goodsNo;

    @DatabaseField(columnName = "is_cashier_equip")
    private Integer isCashierEquip;

    @DatabaseField(columnName = "application_type_id")
    private String applicationTypeId;

    @DatabaseField(columnName = "bind_device_type")
    private Integer bindDeviceType;


    @DatabaseField(columnName = "is_bind_pos")
    private Integer isBindPos;

    @DatabaseField(columnName = "pos_no")
    private String posNo;

    @DatabaseField(columnName = $.isKdsDevice, defaultValue = "2")
    private String isKdsDevice;
    public Long getInitID() {
        return initID;
    }

    public void setInitID(Long initID) {
        this.initID = initID;
    }

    public Long getCommercialID() {
        return commercialID;
    }

    public void setCommercialID(Long commercialID) {
        this.commercialID = commercialID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public Long getPadNo() {
        return padNo;
    }

    public void setPadNo(Long padNo) {
        this.padNo = padNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Long createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Long getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(Long modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAutobind() {
        return autobind;
    }

    public void setAutobind(String autobind) {
        this.autobind = autobind;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public InitSystemDeviceType getDeviceType() {
        return ValueEnums.toEnum(InitSystemDeviceType.class, deviceType);
    }

    public void setDeviceType(InitSystemDeviceType deviceType) {
        this.deviceType = ValueEnums.toValue(deviceType);
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getModule() {
        return module;
    }

    public void setModule(Integer module) {
        this.module = module;
    }

    public Integer getIsMainPos() {
        return isMainPos;
    }

    public void setIsMainPos(Integer isMainPos) {
        this.isMainPos = isMainPos;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public Integer getIsCashierEquip() {
        return isCashierEquip;
    }

    public void setIsCashierEquip(Integer isCashierEquip) {
        this.isCashierEquip = isCashierEquip;
    }

    public String getApplicationTypeId() {
        return applicationTypeId;
    }

    public void setApplicationTypeId(String applicationTypeId) {
        this.applicationTypeId = applicationTypeId;
    }

    public Integer getBindDeviceType() {
        return bindDeviceType;
    }

    public void setBindDeviceType(Integer bindDeviceType) {
        this.bindDeviceType = bindDeviceType;
    }

    public Integer getIsBindPos() {
        return isBindPos;
    }

    public void setIsBindPos(Integer isBindPos) {
        this.isBindPos = isBindPos;
    }

    public String getPosNo() {
        return posNo;
    }

    public void setPosNo(String posNo) {
        this.posNo = posNo;
    }

    public String getIsKdsDevice() {
        return isKdsDevice;
    }

    public void setIsKdsDevice(String isKdsDevice) {
        this.isKdsDevice = isKdsDevice;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(IsDelete.VALID, status);
    }

    @Override
    public Long pkValue() {
        return initID;
    }

    @Override
    public Long verValue() {
        return modifyDateTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && EntityBase.checkNonNull(initID);
    }
}
