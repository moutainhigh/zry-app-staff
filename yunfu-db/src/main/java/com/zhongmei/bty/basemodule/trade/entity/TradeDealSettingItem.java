package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;


@DatabaseTable(tableName = "trade_deal_setting_item")
public class TradeDealSettingItem extends BasicEntityBase {

    private static final long serialVersionUID = 1L;


    public interface $ extends BasicEntityBase.$ {


        public static final String creatorId = "creator_id";


        public static final String endTime = "end_time";


        public static final String orderNum = "order_num";


        public static final String settingId = "setting_id";


        public static final String shopIdenty = "shop_identy";


        public static final String sort = "sort";


        public static final String startTime = "start_time";


        public static final String updatorId = "updator_id";

    }

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "end_time", canBeNull = false)
    private String endTime;

    @DatabaseField(columnName = "order_num", canBeNull = false)
    private Integer orderNum;

    @DatabaseField(columnName = "setting_id", canBeNull = false)
    private Long settingId;

    @DatabaseField(columnName = "shop_identy")
    private Long shopIdenty;

    @DatabaseField(columnName = "sort", canBeNull = false)
    private Integer sort;

    @DatabaseField(columnName = "start_time", canBeNull = false)
    private String startTime;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Long getSettingId() {
        return settingId;
    }

    public void setSettingId(Long settingId) {
        this.settingId = settingId;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(endTime, orderNum, settingId, sort, startTime);
    }
}

