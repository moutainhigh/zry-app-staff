package com.zhongmei.bty.basemodule.database.entity.shopmanager;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;

import java.math.BigDecimal;

/**
 * CashHandoverItem is a ORMLite bean type. Corresponds to
 * the database table "cash_handover_item"
 */
@DatabaseTable(tableName = "cash_handover_item")
public class CashHandoverItem extends DataEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "cash_handover_item"
     */
    public interface $ extends DataEntityBase.$ {

        /**
         * actual_amount
         */
        public static final String actualAmount = "actual_amount";

        /**
         * amount
         */
        public static final String amount = "amount";

        /**
         * creator_id
         */
        public static final String creatorId = "creator_id";

        /**
         * creator_name
         */
        public static final String creatorName = "creator_name";

        /**
         * handover_id
         */
        public static final String handoverId = "handover_id";

        /**
         * handover_uuid
         */
        public static final String handoverUuid = "handover_uuid";

        /**
         * pay_mode_id
         */
        public static final String payModeId = "pay_mode_id";

        /**
         * pay_mode_name
         */
        public static final String payModeName = "pay_mode_name";

        /**
         * updator_id
         */
        public static final String updatorId = "updator_id";

        /**
         * updator_name
         */
        public static final String updatorName = "updator_name";

        /**
         * cash_box_amount
         */
        public static final String cashBoxAmount = "cash_box_amount";

        /**
         * diff_amount
         */
        public static final String diffAmount = "diff_amount";
        /**
         * other_income
         */
        public static final String otherIncome = "other_income";


    }

    @DatabaseField(columnName = "actual_amount", canBeNull = false)
    private java.math.BigDecimal actualAmount;

    @DatabaseField(columnName = "amount", canBeNull = false)
    private java.math.BigDecimal amount;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "handover_id")
    private Long handoverId;

    @DatabaseField(columnName = "handover_uuid", canBeNull = false)
    private String handoverUuid;

    @DatabaseField(columnName = "pay_mode_id", canBeNull = false)
    private Long payModeId;

    @DatabaseField(columnName = "pay_mode_name", canBeNull = false)
    private String payModeName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    /**
     * 差额
     */
    @DatabaseField(columnName = "diff_amount")
    private BigDecimal diffAmount;

    /**
     * 开机金额
     */
    @DatabaseField(columnName = "cash_box_amount")
    private BigDecimal cashBoxAmount;

    /**
     * 消费金额
     */
    @DatabaseField(columnName = "order_amount")
    private BigDecimal orderAmount;

    /**
     * 储值金额
     */
    @DatabaseField(columnName = "valuecard_amount")
    private BigDecimal valuecardAmount;


    /**
     * 其他收入
     *
     * @return
     */
    @DatabaseField(columnName = "other_income")
    private BigDecimal otherIncome;
    /**
     * 预付金
     */
    private BigDecimal bookAmount;//预付金

    public BigDecimal getOtherIncome() {
        return otherIncome;
    }

    public void setOtherIncome(BigDecimal otherIncome) {
        this.otherIncome = otherIncome;
    }

    public java.math.BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(java.math.BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public java.math.BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(java.math.BigDecimal amount) {
        this.amount = amount;
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

    public Long getHandoverId() {
        return handoverId;
    }

    public void setHandoverId(Long handoverId) {
        this.handoverId = handoverId;
    }

    public String getHandoverUuid() {
        return handoverUuid;
    }

    public void setHandoverUuid(String handoverUuid) {
        this.handoverUuid = handoverUuid;
    }

    public Long getPayModeId() {
        return payModeId;
    }

    public void setPayModeId(Long payModeId) {
        this.payModeId = payModeId;
    }

    public String getPayModeName() {
        return payModeName;
    }

    public void setPayModeName(String payModeName) {
        this.payModeName = payModeName;
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

    public BigDecimal getDiffAmount() {
        return diffAmount;
    }

    public void setDiffAmount(BigDecimal diffAmount) {
        this.diffAmount = diffAmount;
    }

    public BigDecimal getCashBoxAmount() {
        return cashBoxAmount;
    }

    public void setCashBoxAmount(BigDecimal cashBoxAmount) {
        this.cashBoxAmount = cashBoxAmount;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getValuecardAmount() {
        return valuecardAmount;
    }

    public void setValuecardAmount(BigDecimal valuecardAmount) {
        this.valuecardAmount = valuecardAmount;
    }

    public BigDecimal getBookAmount() {
        return bookAmount == null ? BigDecimal.ZERO : bookAmount;
    }

    public void setBookAmount(BigDecimal bookAmount) {
        this.bookAmount = bookAmount;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(actualAmount, amount, handoverId, handoverUuid, payModeId, payModeName);
    }
}
