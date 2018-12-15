package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.ServerEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.math.BigDecimal;

/**
 * 小程序优惠关联表
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "trade_privilege_applet")
public class TradePrivilegeApplet extends ServerEntityBase {

    private static final long serialVersionUID = 1L;

    public interface $ extends ServerEntityBase.$ {
        public String tradeId = "trade_id";
        public String tradeUuid = "trade_uuid";
        public String tradePrivilegeId = "trade_privilege_id";
        public String activityId = "activity_id";
        public String customerId = "customer_id";
        public String tradeItemId = "trade_item_id";

    }

    @DatabaseField(columnName = "uuid")
    private String uuid;
    //订单id
    @DatabaseField(columnName = "trade_id")
    private Long tradeId;
    //    订单uuid
    @DatabaseField(columnName = "trade_uuid")
    private String tradeUuid;
    //    订单优惠id
    @DatabaseField(columnName = "trade_privilege_id")
    private Long tradePrivilegeId;
    //    关联trade_privilege 的uuid
    @DatabaseField(columnName = "trade_privilege_uuid")
    private String tradePrivilegeUuid;
    //    订单商品id
    @DatabaseField(columnName = "trade_item_id")
    private Long tradeItemId;
    @DatabaseField(columnName = "trade_item_uuid")
    private String tradeItemUuid;
    //    品牌商品id
    @DatabaseField(columnName = "brand_dish_id")
    private Long brandDishId;
    //    品牌商品名
    @DatabaseField(columnName = "brand_dish_name")
    private String brandDishName;
    @DatabaseField(columnName = "brand_dish_num")
    private BigDecimal brandDishNum;
    //    小程序活动id
    @DatabaseField(columnName = "activity_id")
    private String activityId;
    //    会员id
    @DatabaseField(columnName = "customer_id")
    private Long customerId;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public Long getTradePrivilegeId() {
        return tradePrivilegeId;
    }

    public void setTradePrivilegeId(Long tradePrivilegeId) {
        this.tradePrivilegeId = tradePrivilegeId;
    }

    public String getTradePrivilegeUuid() {
        return tradePrivilegeUuid;
    }

    public void setTradePrivilegeUuid(String tradePrivilegeUuid) {
        this.tradePrivilegeUuid = tradePrivilegeUuid;
    }

    public Long getTradeItemId() {
        return tradeItemId;
    }

    public void setTradeItemId(Long tradeItemId) {
        this.tradeItemId = tradeItemId;
    }

    public String getTradeItemUuid() {
        return tradeItemUuid;
    }

    public void setTradeItemUuid(String tradeItemUuid) {
        this.tradeItemUuid = tradeItemUuid;
    }

    public Long getBrandDishId() {
        return brandDishId;
    }

    public void setBrandDishId(Long brandDishId) {
        this.brandDishId = brandDishId;
    }

    public String getBrandDishName() {
        return brandDishName;
    }

    public void setBrandDishName(String brandDishName) {
        this.brandDishName = brandDishName;
    }

    public BigDecimal getBrandDishNum() {
        return brandDishNum;
    }

    public void setBrandDishNum(BigDecimal brandDishNum) {
        this.brandDishNum = brandDishNum;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setInVaild() {
        setStatusFlag(StatusFlag.INVALID);
    }
}
