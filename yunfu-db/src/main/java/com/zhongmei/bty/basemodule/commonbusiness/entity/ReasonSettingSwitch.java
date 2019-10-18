package com.zhongmei.bty.basemodule.commonbusiness.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.ValueEnums;


@DatabaseTable(tableName = "reason_setting_switch")
public class ReasonSettingSwitch extends IdEntityBase {

        public static final int SWITCH_ON = 1;
    public static final int SWITCH_OFF = 2;

    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty;
    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;
    @DatabaseField(columnName = "reason_value")
    private String reasonValue;
    @DatabaseField(columnName = "enable_switch")
    private Integer enableSwitch;
    @DatabaseField(columnName = "status_flag")
    private Integer statusFlag;
    @DatabaseField(columnName = "creator_id")
    private Long creatorId;
    @DatabaseField(columnName = "creator_name")
    private String creatorName;
    @DatabaseField(columnName = "updator_id")
    private Long updatorId;
    @DatabaseField(columnName = "updator_name")
    private String updatorName;
    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;
    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;
    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
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

    public String getReasonValue() {
        return reasonValue;
    }

    public void setReasonValue(String reasonValue) {
        this.reasonValue = reasonValue;
    }

    public Integer getEnableSwitch() {
        return enableSwitch;
    }

    public void setEnableSwitch(Integer enableSwitch) {
        this.enableSwitch = enableSwitch;
    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
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
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(brandIdenty, shopIdenty);
    }


    public interface $ extends IdEntityBase.$ {


        String brandIdenty = "brand_identy";

        String shopIdenty = "shop_identy";

        String reasonValue = "reason_value";

        String enableSwitch = "enable_switch";

        String statusFlag = "status_flag";

        String creatorId = "creator_id";

        String creatorName = "creator_name";

        String updatorId = "updator_id";

        String updatorName = "updator_name";

        String serverCreateTime = "server_create_time";

        String serverUpdateTime = "server_update_time";
    }

}
