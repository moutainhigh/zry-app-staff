package com.zhongmei.bty.basemodule.notifycenter.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.notifycenter.enums.BizStatus;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.CommonEntityBase;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.context.util.Utils;

/**
 * 服务铃
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "call_waiter")
public class CallWaiter extends CommonEntityBase {

    public interface $ extends CommonEntityBase.$ {

        public final static String brandId = "brand_id";

        public final static String shopId = "shop_id";

        public final static String tableId = "table_id";

        public final static String tradeId = "trade_id";

        public final static String uuid = "uuid";

        public final static String source = "source";

        public final static String bizStatus = "biz_status";

        public final static String waiterId = "waiter_id";

        public final static String waiterName = "waiter_name";

        public final static String contentCode = "content_code";

        public final static String content = "content";

        public final static String count = "count";

        public final static String revceiveTime = "receive_time";

        public final static String lastCallTime = "last_call_time";
    }

    @DatabaseField(columnName = "brand_id", canBeNull = false)
    private Long brandId;

    @DatabaseField(columnName = "shop_id", canBeNull = false)
    private Long shopId;

    @DatabaseField(columnName = "table_id")
    private Long tableId;

    @DatabaseField(columnName = "trade_id")
    private Long tradeId;

    @DatabaseField(columnName = "uuid", canBeNull = false)
    private String uuid;

    @DatabaseField(columnName = "source", canBeNull = false)
    private Integer source;

    @DatabaseField(columnName = "biz_status", canBeNull = false)
    private Integer bizStatus;

    @DatabaseField(columnName = "waiter_id")
    private Long waiterId;

    @DatabaseField(columnName = "waiter_name")
    private String waiterName;

    //对应的枚举类CallWaiterType 该字段可能存放多个枚举值以"｜"隔开
    @DatabaseField(columnName = "content_code")
    private String contentCode;

    @DatabaseField(columnName = "content")
    private String content;

    @DatabaseField(columnName = "count")
    private Integer count;

    @DatabaseField(columnName = "receive_time", canBeNull = false)
    private Long receiveTime;

    @DatabaseField(columnName = "last_call_time")
    private Long lastCallTime;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public SourceId getSource() {
        return ValueEnums.toEnum(SourceId.class, source);
    }

    public void setSource(SourceId source) {
        this.source = ValueEnums.toValue(source);
    }

    public BizStatus getBizStatus() {
        return ValueEnums.toEnum(BizStatus.class, bizStatus);
    }

    public void setBizStatus(BizStatus bizStatus) {
        this.bizStatus = ValueEnums.toValue(bizStatus);
    }

    public Long getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(Long waiterId) {
        this.waiterId = waiterId;
    }

    public String getWaiterName() {
        return waiterName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }

    public String getContentCode() {
        return contentCode;
    }

    public void setContentCode(String contentCode) {
        this.contentCode = contentCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Long getLastCallTime() {
        return lastCallTime;
    }

    public void setLastCallTime(Long lastCallTime) {
        this.lastCallTime = lastCallTime;
    }

    public boolean equals(Object obj) {
        if (super.equals(obj) && Utils.equals(count, ((CallWaiter) obj).getCount())) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + (count == null ? 0 : count.hashCode());
    }

    //本地冗余字段，用于通知中心的展示
    private String tableName = "";

    private String serialNumber = "";

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(brandId, shopId, uuid, source, bizStatus, receiveTime);
    }
}
