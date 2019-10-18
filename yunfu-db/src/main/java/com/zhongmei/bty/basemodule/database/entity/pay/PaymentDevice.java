package com.zhongmei.bty.basemodule.database.entity.pay;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.ValueEnums;


@DatabaseTable(tableName = "payment_device")
public class PaymentDevice extends IdEntityBase implements java.io.Serializable, ICreator {

    private static final long serialVersionUID = 1L;


    public interface $ extends IdEntityBase.$ {

        String statusFlag = "status_flag";


        String brandIdenty = "brand_identy";


        String serverCreateTime = "server_create_time";


        String serverUpdateTime = "server_update_time";


        String shopIdenty = "shop_identy";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";

        public static final String posChannelId = "pos_channel_id";

        public static final String deviceNumber = "device_number";

    }


    @DatabaseField(columnName = "status_flag", canBeNull = false)
    private Integer statusFlag;


    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty;


    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;


    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;


    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "pos_channel_id", canBeNull = false)
    private Long posChannelId;

    @DatabaseField(columnName = "device_number", canBeNull = false)
    private String deviceNumber;

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

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
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

    public Long getPosChannelId() {
        return posChannelId;
    }

    public void setPosChannelId(Long posChannelId) {
        this.posChannelId = posChannelId;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(statusFlag, brandIdenty, shopIdenty, posChannelId, deviceNumber);
    }
}
