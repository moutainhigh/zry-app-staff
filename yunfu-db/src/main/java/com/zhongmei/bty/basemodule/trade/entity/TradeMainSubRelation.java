package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;

/**
 * Created by demo on 2018/12/15
 * CREATE TABLE `trade_main_sub_relation` (
 * `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '服务端自增ID',
 * `main_trade_id` bigint(20) NOT NULL COMMENT '主单ID',
 * `sub_trade_id` bigint(20) NOT NULL COMMENT '子单ID',
 * `status_flag` tinyint(4) NOT NULL COMMENT '1:VALID:有效的\r\n2:INVALID:无效的',
 * `server_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '服务端创建时间',
 * `server_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '服务端最后修改时间',
 * `brand_identy` bigint(20) NOT NULL COMMENT '品牌标识',
 * `shop_identy` bigint(20) NOT NULL COMMENT '门店标识',
 * PRIMARY KEY (`id`),
 * KEY `idx_server_update_time` (`shop_identy`,`server_update_time`),
 * KEY `idx_sub_trade_id_main_trade_id` (`main_trade_id`,`sub_trade_id`),
 * KEY `idx_brand_identy_shop_identy` (`brand_identy`,`shop_identy`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单主单子单关系表'
 */

@DatabaseTable(tableName = "trade_main_sub_relation")
public class TradeMainSubRelation extends BasicEntityBase {
    private static final long serialVersionUID = 1L;

    public interface $ extends BasicEntityBase.$ {

        public static final String mainTradeId = "main_trade_id";
        public static final String subTradeId = "sub_trade_id";
        public static final String shopIdenty = "shop_identy";
    }

    @DatabaseField(columnName = "main_trade_id")
    private Long mainTradeId;


    @DatabaseField(columnName = "sub_trade_id")
    private Long subTradeId;


    public Long getMainTradeId() {
        return mainTradeId;
    }

    public void setMainTradeId(Long mainTradeId) {
        this.mainTradeId = mainTradeId;
    }

    public Long getSubTradeId() {
        return subTradeId;
    }

    public void setSubTradeId(Long subTradeId) {
        this.subTradeId = subTradeId;
    }

}
