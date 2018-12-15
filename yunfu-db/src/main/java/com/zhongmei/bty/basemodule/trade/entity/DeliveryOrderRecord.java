package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderRecordOpType;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * 小费记录
 *
 * @Version: 1.0
 */
@DatabaseTable(tableName = "delivery_order_record")
public class DeliveryOrderRecord extends IdEntityBase {

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(YesOrNo.YES, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    /**
     * The columns of table "trade"
     */
    public interface $ extends BasicEntityBase.$ {

        /**
         * uuid
         */
        String uuid = "uuid";

        /**
         * delivery_order_id
         */
        String deliveryOrderId = "delivery_order_id";

        /**
         * delivery_order_uuid
         */
        String deliveryOrderUuid = "delivery_order_uuid";

        /**
         * shop_identy
         */
        String shopIdenty = "shop_identy";

        /**
         * brand_identy
         */
        String brandIdenty = "brand_identy";

        /**
         * delivery_platform
         */
        String deliveryPlatform = "delivery_platform";

        /**
         * status_flag
         */
        String statusFlag = "status_flag";

        /**
         * op_type
         */
        String opType = "op_type";

        /**
         * child_op_type
         */
        String childOpType = "child_op_type";

        /**
         * op_type_desc
         */
        String opTypeDesc = "op_type_desc";

        /**
         * amount
         */
        String amount = "amount";

        /**
         * op_status
         */
        String opStatus = "op_status";

        /**
         * operater_id
         */
        String operaterId = "operater_id";

        /**
         * operater_no
         */
        String operaterNo = "operater_no";

        /**
         * operater_name
         */
        String operaterName = "operater_name";

        /**
         * operate_source
         */
        String operateSource = "operate_source";

        /**
         * memo
         */
        String memo = "memo";

        /**
         * server_create_time
         */
        String serverCreateTime = "server_create_time";

        /**
         * server_update_time
         */
        String serverUpdateTime = "server_update_time";

        /**
         * ext1
         */
        String ext1 = "ext1";

        /**
         * ext2
         */
        String ext2 = "ext2";

        /**
         * ext3
         */
        String ext3 = "ext3";
    }

    @DatabaseField(columnName = "uuid")
    private String uuid;

    /**
     * '配送订单id，关联delivery_order.id
     */
    @DatabaseField(columnName = "delivery_order_id")
    private Long deliveryOrderId;

    /**
     * 配送订单uuid
     */
    @DatabaseField(columnName = "delivery_order_uuid")
    private String deliveryOrderUuid;

    /**
     * 商户id
     */
    @DatabaseField(columnName = "shop_identy")
    private Long shopIdenty;

    /**
     * 品牌id
     */
    @DatabaseField(columnName = "brand_identy")
    private Long brandIdenty;

    /**
     * '配送平台
     */
    @DatabaseField(columnName = "delivery_platform")
    private Integer deliveryPlatform;

    /**
     * 是否有效，1有效，0无效
     */
    @DatabaseField(columnName = "status_flag")
    private Integer statusFlag;

    /**
     * 操作类型：1-配送单操作记录、2-加小费
     */
    @DatabaseField(columnName = "op_type")
    private Integer opType;


    /**
     * 操作子类型：1-新订单，2-状态变更等，10-手动加小费，11-自动加小费
     */
    @DatabaseField(columnName = "child_op_type")
    private Integer childOpType;


    /**
     * '类型描述
     */
    @DatabaseField(columnName = "op_type_desc")
    private String opTypeDesc;

    /**
     * 金额，当操作类型为加小费时，为小费金额
     */
    @DatabaseField(columnName = "amount")
    private BigDecimal amount;

    /**
     * 配送单变更的状态
     */
    @DatabaseField(columnName = "op_status")
    private Integer opStatus;

    /**
     * 操作人id
     */
    @DatabaseField(columnName = "operater_id")
    private Long operaterId;

    /**
     * 操作人编号
     */
    @DatabaseField(columnName = "operater_no")
    private String operaterNo;


    /**
     * 操作人名称
     */
    @DatabaseField(columnName = "operater_name")
    private String operaterName;

    /**
     * 操作方，1-POS,2-第三方，3-系统
     */
    @DatabaseField(columnName = "operate_source")
    private Integer operateSource;

    /**
     * 操作备注信息
     */
    @DatabaseField(columnName = "memo")
    private String memo;

    /**
     * 创建时间
     */
    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;

    /**
     * 更新时间
     */
    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;

    /**
     */
    @DatabaseField(columnName = "ext1")
    private String ext1;

    /**
     */
    @DatabaseField(columnName = "ext2")
    private String ext2;

    /**
     */
    @DatabaseField(columnName = "ext3")
    private String ext3;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getDeliveryOrderId() {
        return deliveryOrderId;
    }

    public void setDeliveryOrderId(Long deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }

    public String getDeliveryOrderUuid() {
        return deliveryOrderUuid;
    }

    public void setDeliveryOrderUuid(String deliveryOrderUuid) {
        this.deliveryOrderUuid = deliveryOrderUuid;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    public Integer getDeliveryPlatform() {
        return deliveryPlatform;
    }

    public void setDeliveryPlatform(Integer deliveryPlatform) {
        this.deliveryPlatform = deliveryPlatform;
    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

    public DeliveryOrderRecordOpType getOpType() {
        return ValueEnums.toEnum(DeliveryOrderRecordOpType.class, opType);
    }

    public void setOpType(DeliveryOrderRecordOpType opType) {
        this.opType = ValueEnums.toValue(opType);
    }

    public Integer getChildOpType() {
        return childOpType;
    }

    public void setChildOpType(Integer childOpType) {
        this.childOpType = childOpType;
    }

    public String getOpTypeDesc() {
        return opTypeDesc;
    }

    public void setOpTypeDesc(String opTypeDesc) {
        this.opTypeDesc = opTypeDesc;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getOpStatus() {
        return opStatus;
    }

    public void setOpStatus(Integer opStatus) {
        this.opStatus = opStatus;
    }

    public Long getOperaterId() {
        return operaterId;
    }

    public void setOperaterId(Long operaterId) {
        this.operaterId = operaterId;
    }

    public String getOperaterNo() {
        return operaterNo;
    }

    public void setOperaterNo(String operaterNo) {
        this.operaterNo = operaterNo;
    }

    public String getOperaterName() {
        return operaterName;
    }

    public void setOperaterName(String operaterName) {
        this.operaterName = operaterName;
    }

    public Integer getOperateSource() {
        return operateSource;
    }

    public void setOperateSource(Integer operateSource) {
        this.operateSource = operateSource;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public static List<DeliveryOrderRecord> findDeliveryOrderRecordList(String tradeUuid) {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<DeliveryOrderRecord, String> tradeDao = helper.getDao(DeliveryOrderRecord.class);
            QueryBuilder<DeliveryOrderRecord, String> qb = tradeDao.queryBuilder();
            Where<DeliveryOrderRecord, String> where = qb.where();
            // 未支付的必胜客订单
            where.eq(DeliveryOrderRecord.$.deliveryOrderUuid, tradeUuid);
            qb.orderBy(Trade.$.serverCreateTime, false);

            return qb.query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }
}
