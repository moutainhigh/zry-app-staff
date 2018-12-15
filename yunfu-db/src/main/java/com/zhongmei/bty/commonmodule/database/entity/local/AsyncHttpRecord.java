package com.zhongmei.bty.commonmodule.database.entity.local;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpState;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpType;

/**
 * Model class of 网络异步请求记录.
 *
 * @version $Id$
 */
@DatabaseTable(tableName = "async_http_record")
public class AsyncHttpRecord extends LocalEntityBase implements ICreator, IUpdator, Cloneable {


    public interface $ extends LocalEntityBase.$ {

        public static final String tradeId = "trade_id";

        public static final String tradeUuId = "trade_uuid";

        public static final String tableId = "table_id";

        public static final String serialNumber = "serial_number";

        public static final String tableName = "table_name";

        public static final String tradeUpdateTime = "trade_update_time";

        public static final String type = "type";

        public static final String status = "status";

        public static final String reason = "reason";

        public static final String retryCount = "retry_count";

        public static final String reqStr = "req_str";

        public static final String reqUrl = "req_url";

        public static final String printBeanJson = "print_bean_json";

    }

    /**
     * 订单ID.
     */
    @DatabaseField(columnName = "trade_id")
    private Long tradeId;

    /**
     * 订单uuid.
     */
    @DatabaseField(columnName = "trade_uuid")
    private String tradeUuId;

    /**
     * 桌台ID
     **/
    @DatabaseField(columnName = "table_id")
    private Long tableId;

    /**
     * 流水号
     */
    @DatabaseField(columnName = "serial_number")
    private String serialNumber;

    /**
     * 桌台号
     */
    @DatabaseField(columnName = "table_name")
    private String tableName;

    /**
     * 服务器最后一次更新时间.
     */
    @DatabaseField(columnName = "trade_update_time")
    private Long tradeUpdateTime;

    /**
     * 业务类型. AsyncHttpType
     */
    @DatabaseField(columnName = "type")
    private Integer type;

    /**
     * 状态. AsyncHttpState
     */
    @DatabaseField(columnName = "status")
    private Integer status = AsyncHttpState.EXCUTING.value();

    /**
     * 原因.
     */
    @DatabaseField(columnName = "reason")
    private String reason;

    /**
     * 重试次数.
     */
    @DatabaseField(columnName = "retry_count")
    private Integer retryCount = 0;

    /**
     * 网络请求参数.
     */
    @DatabaseField(columnName = "req_str")
    private String reqStr;

    /**
     * 网络请求地址.
     */
    @DatabaseField(columnName = "req_url")
    private String reqUrl;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "device_no")
    private String deviceNo;

    //异步改单类接口触发的打印json数据
    @DatabaseField(columnName = "print_bean_json")
    private String printBeanJson;

    /**
     * Constructor.
     */
    public AsyncHttpRecord() {
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeUuId() {
        return tradeUuId;
    }

    public void setTradeUuId(String tradeUuId) {
        this.tradeUuId = tradeUuId;
    }

    public Long getTradeUpdateTime() {
        return tradeUpdateTime;
    }

    public void setTradeUpdateTime(Long tradeUpdateTime) {
        this.tradeUpdateTime = tradeUpdateTime;
    }

    public AsyncHttpType getType() {
        return ValueEnums.toEnum(AsyncHttpType.class, type);
    }

    public void setType(AsyncHttpType type) {
        this.type = ValueEnums.toValue(type);
    }

    public AsyncHttpState getStatus() {
        return ValueEnums.toEnum(AsyncHttpState.class, status);
    }

    public void setStatus(AsyncHttpState status) {
        this.status = ValueEnums.toValue(status);
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getRetryCount() {
        if (retryCount == null) {
            return 0;
        }
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getReqStr() {
        return reqStr;
    }

    public void setReqStr(String reqStr) {
        this.reqStr = reqStr;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    @Override
    public Long getCreatorId() {
        return creatorId;
    }

    @Override
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String getCreatorName() {
        return creatorName;
    }

    @Override
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    public Long getUpdatorId() {
        return updatorId;
    }

    @Override
    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    @Override
    public String getUpdatorName() {
        return updatorName;
    }

    @Override
    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getPrintBeanJson() {
        return printBeanJson;
    }

    public void setPrintBeanJson(String printBeanJson) {
        this.printBeanJson = printBeanJson;
    }

    public AsyncHttpRecord clone() {
        AsyncHttpRecord tmpAsyncHttpRecord = null;
        try {
            tmpAsyncHttpRecord = (AsyncHttpRecord) super.clone();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return tmpAsyncHttpRecord;
    }
}
