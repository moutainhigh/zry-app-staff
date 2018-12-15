package com.zhongmei.bty.basemodule.orderdish.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.ServerEntityBase;

/**
 * Created by demo on 2018/12/15
 * V8.1新增表
 * 菜品座位号关联表（以后可能会扩展其他的内容）
 */
@DatabaseTable(tableName = "trade_item_extra_dinner")
public class TradeItemExtraDinner extends ServerEntityBase {

    public interface $ extends ServerEntityBase.$ {

        public static final String trade_item_id = "trade_item_id";

        public static final String trade_item_uuid = "trade_item_uuid";

        public static final String seat_id = "seat_id";

        public static final String seat_number = "seat_number";

        public static final String clientCreateTime = "client_create_time";

        public static final String clientUpdateTime = "client_update_time";
        /**
         * creator_id
         */
        public static final String creatorId = "creator_id";

        /**
         * creator_name
         */
        public static final String creatorName = "creator_name";

        /**
         * updator_id
         */
        public static final String updatorId = "updator_id";

        /**
         * updator_name
         */
        public static final String updatorName = "updator_name";
    }

    @DatabaseField(columnName = "trade_item_id")
    private Long tradeItemId;

    @DatabaseField(columnName = "trade_item_uuid")
    private String tradeItemUuid;

    @DatabaseField(columnName = "seat_id")
    private Long seatId;

    @DatabaseField(columnName = "seat_number")
    private String seatNumber;

    @DatabaseField(columnName = "client_create_time")
    private Long clientCreateTime;

    @DatabaseField(columnName = "client_update_time")
    private Long clientUpdateTime;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "servingOrder")
    private Integer servingOrder; //上菜顺序 小于等于0为没有设置顺序

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

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
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

    public Integer getServingOrder() {
        return servingOrder;
    }

    public void setServingOrder(Integer servingOrder) {
        this.servingOrder = servingOrder;
    }
}
