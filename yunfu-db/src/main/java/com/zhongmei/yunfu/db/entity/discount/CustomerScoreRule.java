package com.zhongmei.yunfu.db.entity.discount;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * 积分规则
 */
@DatabaseTable(tableName = "customer_score_rule")
public class CustomerScoreRule extends IdEntityBase {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "crm_customer_level_rights"
     */
    public interface $ extends IdEntityBase.$ {


        public final String type = "type";

        public final String convertValue = "convert_value";

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
     * 积分抵扣限制Type
     */
    @DatabaseField(columnName = "type", canBeNull = false)
    private Integer type;


    /**
     * 积分抵现规则
     */
    @DatabaseField(columnName = "convert_value", canBeNull = false)
    private Integer convertValue;

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

    /**
     * 服务器创建时间
     */
    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;

    /**
     * 服务器最后修改时间
     */
    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getConvertValue() {
        return convertValue;
    }

    public void setConvertValue(Integer convertValue) {
        this.convertValue = convertValue;
    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
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
        return super.checkNonNull() && checkNonNull(convertValue);
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }
}
