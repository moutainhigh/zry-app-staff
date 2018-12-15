package com.zhongmei.bty.basemodule.database.entity.shopmanager;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.util.ValueEnums;

import java.math.BigDecimal;

/**
 * CashHandover is a ORMLite bean type. Corresponds to the
 * database table "cash_handover"
 */
@DatabaseTable(tableName = "cash_handover")
public class CashHandover extends DataEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;

    public int getCalibrateStatus() {
        return calibrateStatus;
    }

    public void setCalibrateStatus(int calibrateStatus) {
        this.calibrateStatus = calibrateStatus;
    }

    /**
     * The columns of table "cash_handover"
     */
    public interface $ extends DataEntityBase.$ {
        /**
         * creator_id
         */
        public static final String creatorId = "creator_id";

        /**
         * creator_name
         */
        public static final String creatorName = "creator_name";

        /**
         * end_time
         */
        public static final String endTime = "end_time";

        /**
         * handover_date
         */
        public static final String handoverDate = "handover_date";

        /**
         * handover_user_id
         */
        public static final String handoverUserId = "handover_user_id";

        /**
         * handover_user_name
         */
        public static final String handoverUserName = "handover_user_name";

        /**
         * refund_order_count
         */
        public static final String refundOrderCount = "refund_order_count";

        /**
         * sale_order_count
         */
        public static final String saleOrderCount = "sale_order_count";

        /**
         * start_time
         */
        public static final String startTime = "start_time";

        /**
         * updator_id
         */
        public static final String updatorId = "updator_id";

        /**
         * updator_name
         */
        public static final String updatorName = "updator_name";

        /**
         * 日营业额合计
         */
        public static final String totalAmount = "total_amount";

        /**
         * 开机合计
         */
        public static final String totalCashBoxAmount = "total_cash_box_amount";

        /**
         * 交接合计
         */
        public static final String totalActualAmount = "total_handover_amount";

        /**
         * 差额合计
         */
        public static final String totalDiffAmount = "total_diff_amount";

        /**
         * first
         */
        public static final String first = "first";

        /**
         * bizDate
         */
        public static final String bizDate = "biz_date";

        /**
         * is_restat
         */
        public static final String isRestat = "is_restat";

        /**
         * 当前pos的挂账笔数
         */
        public static final String creditNum = "credit_num";

        /**
         * 当前pos的挂账金额
         */
        public static final String creditAmount = "credit_amount";
        /**
         * 交接类型 1明交接 2 暗交接
         */
        public static final String type = "type";


        public static final String calibrateStatus = "calibrateStatus";

    }

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "end_time", canBeNull = false)
    private Long endTime;

    @DatabaseField(columnName = "handover_date", canBeNull = false)
    private Long handoverDate;

    @DatabaseField(columnName = "handover_user_id", canBeNull = false)
    private Long handoverUserId;

    @DatabaseField(columnName = "handover_user_name", canBeNull = false)
    private String handoverUserName;

    @DatabaseField(columnName = "refund_order_count", canBeNull = false)
    private Integer refundOrderCount;

    @DatabaseField(columnName = "sale_order_count", canBeNull = false)
    private Integer saleOrderCount;

    @DatabaseField(columnName = "refund_valuecard_count", canBeNull = false)
    private Integer refundValuecardCount;

    @DatabaseField(columnName = "sale_valuecard_count", canBeNull = false)
    private Integer saleValuecardCount;

    @DatabaseField(columnName = "start_time", canBeNull = false)
    private Long startTime;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "total_amount", canBeNull = false)
    private java.math.BigDecimal totalAmount;

    @DatabaseField(columnName = "total_cash_box_amount", canBeNull = false)
    private java.math.BigDecimal totalCashBoxAmount;

    @DatabaseField(columnName = "total_actual_amount", canBeNull = false)
    private java.math.BigDecimal totalActualAmount;

    @DatabaseField(columnName = "total_diff_amount", canBeNull = false)
    private java.math.BigDecimal totalDiffAmount;

    @DatabaseField(columnName = "credit_num")
    private Integer creditNum;

    @DatabaseField(columnName = "credit_amount")
    private java.math.BigDecimal creditAmount;

    @DatabaseField(columnName = "first", canBeNull = false)
    private boolean isFirst;


    @DatabaseField(columnName = "type")
    private int type;
    @DatabaseField(columnName = "calibrateStatus")
    private int calibrateStatus;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 是否累加 1累加  2清零
     */
    @DatabaseField(columnName = "is_restat")
    private Integer isRestat;

    public Bool getIsRestat() {
        return ValueEnums.toEnum(Bool.class, isRestat);
    }

    public void setIsRestat(Bool isRestat) {
        this.isRestat = ValueEnums.toValue(isRestat);
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        this.isFirst = first;
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

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getHandoverDate() {
        return handoverDate;
    }

    public void setHandoverDate(Long handoverDate) {
        this.handoverDate = handoverDate;
    }

    public Long getHandoverUserId() {
        return handoverUserId;
    }

    public void setHandoverUserId(Long handoverUserId) {
        this.handoverUserId = handoverUserId;
    }

    public String getHandoverUserName() {
        return handoverUserName;
    }

    public void setHandoverUserName(String handoverUserName) {
        this.handoverUserName = handoverUserName;
    }

    public Integer getRefundOrderCount() {
        return refundOrderCount;
    }

    public void setRefundOrderCount(Integer refundOrderCount) {
        this.refundOrderCount = refundOrderCount;
    }

    public Integer getSaleOrderCount() {
        return saleOrderCount;
    }

    public void setSaleOrderCount(Integer saleOrderCount) {
        this.saleOrderCount = saleOrderCount;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public java.math.BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(java.math.BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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

    public java.math.BigDecimal getTotalCashBoxAmount() {
        return totalCashBoxAmount;
    }

    public void setTotalCashBoxAmount(java.math.BigDecimal totalCashBoxAmount) {
        this.totalCashBoxAmount = totalCashBoxAmount;
    }

    public java.math.BigDecimal getTotalDiffAmount() {
        return totalDiffAmount;
    }

    public void setTotalDiffAmount(java.math.BigDecimal totalDiffAmount) {
        this.totalDiffAmount = totalDiffAmount;
    }

    public java.math.BigDecimal getTotalActualAmount() {
        return totalActualAmount;
    }

    public void setTotalActualAmount(java.math.BigDecimal totalActualAmount) {
        this.totalActualAmount = totalActualAmount;
    }

    public Integer getRefundValuecardCount() {
        return refundValuecardCount;
    }

    public void setRefundValuecardCount(Integer refundValuecardCount) {
        this.refundValuecardCount = refundValuecardCount;
    }

    public Integer getSaleValuecardCount() {
        return saleValuecardCount;
    }

    public void setSaleValuecardCount(Integer saleValuecardCount) {
        this.saleValuecardCount = saleValuecardCount;
    }

    public void setCreditNum(Integer creditNum) {
        this.creditNum = creditNum;
    }

    public Integer getCreditNum() {
        return creditNum;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(endTime, handoverDate, handoverUserId,
                handoverUserName, refundOrderCount, saleOrderCount, refundValuecardCount,
                saleValuecardCount, startTime, totalAmount, totalCashBoxAmount,
                totalActualAmount, totalDiffAmount, isFirst);
    }
}
