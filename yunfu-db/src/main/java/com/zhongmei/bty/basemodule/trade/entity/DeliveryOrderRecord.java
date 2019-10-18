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


    public interface $ extends BasicEntityBase.$ {


        String uuid = "uuid";


        String deliveryOrderId = "delivery_order_id";


        String deliveryOrderUuid = "delivery_order_uuid";


        String shopIdenty = "shop_identy";


        String brandIdenty = "brand_identy";


        String deliveryPlatform = "delivery_platform";


        String statusFlag = "status_flag";


        String opType = "op_type";


        String childOpType = "child_op_type";


        String opTypeDesc = "op_type_desc";


        String amount = "amount";


        String opStatus = "op_status";


        String operaterId = "operater_id";


        String operaterNo = "operater_no";


        String operaterName = "operater_name";


        String operateSource = "operate_source";


        String memo = "memo";


        String serverCreateTime = "server_create_time";


        String serverUpdateTime = "server_update_time";


        String ext1 = "ext1";


        String ext2 = "ext2";


        String ext3 = "ext3";
    }

    @DatabaseField(columnName = "uuid")
    private String uuid;


    @DatabaseField(columnName = "delivery_order_id")
    private Long deliveryOrderId;


    @DatabaseField(columnName = "delivery_order_uuid")
    private String deliveryOrderUuid;


    @DatabaseField(columnName = "shop_identy")
    private Long shopIdenty;


    @DatabaseField(columnName = "brand_identy")
    private Long brandIdenty;


    @DatabaseField(columnName = "delivery_platform")
    private Integer deliveryPlatform;


    @DatabaseField(columnName = "status_flag")
    private Integer statusFlag;


    @DatabaseField(columnName = "op_type")
    private Integer opType;



    @DatabaseField(columnName = "child_op_type")
    private Integer childOpType;



    @DatabaseField(columnName = "op_type_desc")
    private String opTypeDesc;


    @DatabaseField(columnName = "amount")
    private BigDecimal amount;


    @DatabaseField(columnName = "op_status")
    private Integer opStatus;


    @DatabaseField(columnName = "operater_id")
    private Long operaterId;


    @DatabaseField(columnName = "operater_no")
    private String operaterNo;



    @DatabaseField(columnName = "operater_name")
    private String operaterName;


    @DatabaseField(columnName = "operate_source")
    private Integer operateSource;


    @DatabaseField(columnName = "memo")
    private String memo;


    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;


    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;


    @DatabaseField(columnName = "ext1")
    private String ext1;


    @DatabaseField(columnName = "ext2")
    private String ext2;


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
