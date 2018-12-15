package com.zhongmei.bty.commonmodule.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.commonmodule.database.enums.TradeInitConfigKeyId;

/**
 * CREATE TABLE `trade_init_config` (
 * `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '服务端自增ID',
 * `trade_id` bigint(20) NOT NULL COMMENT '订单ID',
 * `brand_identy` bigint(20) NOT NULL COMMENT '品牌标识',
 * `shop_identy` bigint(20) NOT NULL COMMENT '门店标识',
 * `status_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1:VALID:有效的 2:INVALID:无效的',
 * `server_create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '服务端创建时间',
 * `server_update_time` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '服务端最后修改时间',
 * `key_id` tinyint(4) NOT NULL COMMENT '本打算新增码表用来记录 key_id 有哪些，但目前sync已有几个码表，暂时没加 ： 1、货币类型  2、服务费费率',
 * `value` varchar(50) NOT NULL COMMENT '配置值',
 * PRIMARY KEY (`id`),
 * KEY `idx_trade_id` (`trade_id`) USING BTREE,
 * KEY `idx_trade_id_key` (`trade_id`,`key_id`) USING BTREE,
 * KEY `idx_brand_shop_identy` (`brand_identy`,`shop_identy`) USING BTREE,
 * KEY `idx_shop_identy_server_update_time` (`shop_identy`,`server_update_time`) USING BTREE
 * ) ENGINE=InnoDB AUTO_INCREMENT=93077410282676225 DEFAULT CHARSET=utf8 COMMENT='订单初始化配置（下单后不会变化的配置，切勿乱用）'
 * <p>
 * value 2
 * {
 * id:主键
 * name:名称
 * calc_way:计算方式:1,按例比;2,按人数;3,固定金额;4、最低消费;5,按人数/香蕉费',
 * feeRate:计算值
 * discount_type: 折扣类型，1：after discount-折扣后, 2, before discount-折扣前
 * }
 */
@DatabaseTable(tableName = "trade_init_config")
public class TradeInitConfig extends BasicEntityBase {

    public interface $ extends BasicEntityBase.$ {

        String trade_id = "trade_id";

        String shop_identy = "shop_identy";

        String key_id = "key_id";

        String value = "value";
    }

    @DatabaseField(columnName = $.trade_id)
    private Long tradeId;

    @DatabaseField(columnName = $.key_id)
    private Integer keyId;

    @DatabaseField(columnName = $.value)
    private String value;

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public TradeInitConfigKeyId getKeyId() {
        return ValueEnums.toEnum(TradeInitConfigKeyId.class, keyId);
    }

    public void setKeyId(TradeInitConfigKeyId keyId) {
        this.keyId = ValueEnums.toValue(keyId);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void validateCreate() {
        setStatusFlag(StatusFlag.VALID);
        setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        //setClientCreateTime(System.currentTimeMillis());
        validateUpdate();
    }

    public void validateUpdate() {
        //setClientUpdateTime(System.currentTimeMillis());
        setChanged(true);
    }

    public static TradeInitConfig create(TradeInitConfig config) {
        TradeInitConfig tradeInitConfig = new TradeInitConfig();
        tradeInitConfig.validateCreate();
        tradeInitConfig.tradeId = config.tradeId;
        tradeInitConfig.keyId = config.keyId;
        tradeInitConfig.value = config.value;
        return tradeInitConfig;
    }
}
