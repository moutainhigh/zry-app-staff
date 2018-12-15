package com.zhongmei.bty.basemodule.discount.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.util.CarryBitRule;
import com.zhongmei.yunfu.db.CrmBasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.bty.commonmodule.database.enums.LimitType;
import com.zhongmei.bty.commonmodule.database.enums.TrueOrFalse;

import java.math.BigDecimal;

/**
 * 积分规则
 */
@DatabaseTable(tableName = "crm_customer_level_rights")
public class CrmCustomerLevelRights extends CrmBasicEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "crm_customer_level_rights"
     */
    public interface $ extends CrmBasicEntityBase.$ {


        /**
         * customer_level_id
         */
        public static final String customerLevelId = "customer_level_id";

        /**
         * consume_value
         */
        public static final String consumeValue = "consume_value";

        /**
         * consume_gain_value
         */
        public static final String consumeGainValue = "consume_gain_value";

        /**
         * is_gain_all
         */
        public static final String isGainAll = "is_gain_all";

        /**
         * is_exchange_cash
         */
        public static final String isExchangeCash = "is_exchange_cash";

        /**
         * exchange_integral_value
         */
        public static final String exchangeIntegralValue = "exchange_integral_value";

        /**
         * exchange_cash_value
         */
        public static final String exchangeCashValue = "exchange_cash_value";

        /**
         * limit_type
         */
        public static final String limitType = "limit_type";

        /**
         * limit_integral
         */
        public static final String limitIntegral = "limit_integral";

        /**
         * discount
         */
        public static final String discount = "discount";

        /**
         * updator_id
         */
        public static final String updatorId = "updator_id";

        /**
         * updator_name
         */
        public static final String updatorName = "updator_name";

        /**
         * creator_id
         */
        public static final String creatorId = "creator_id";

        /**
         * creator_name
         */
        public static final String creatorName = "creator_name";

        /**
         * carry_bit_rule
         */
        public static final String carryBitRule = "carry_bit_rule";

        /**
         * is_discount_all
         */
        public static final String isDiscountAll = "is_discount_all";

        /**
         * is_discount
         */
        public static final String isDiscount = "is_discount";

        /**
         * is_integral
         */
        public static final String isIntegral = "is_integral";


        String priceTempletId = "price_templet_id";

    }

    /**
     * 绑定的会员价方案id
     */
    @DatabaseField(columnName = "price_templet_id")
    private Long priceTempletId;

    /**
     * 会员等级id
     */
    @DatabaseField(columnName = "customer_level_id", canBeNull = false)
    private Long customerLevelId;

    /**
     * 消费值
     */
    @DatabaseField(columnName = "consume_value")
    private BigDecimal consumeValue;

    /**
     * 获取积分值
     */
    @DatabaseField(columnName = "consume_gain_value")
    private BigDecimal consumeGainValue;

    /**
     * '是否全部可积分,0:是，1：否',
     */
    @DatabaseField(columnName = "is_gain_all")
    private Integer isGainAll;

    /**
     * '是否抵现,0:是，1：否',
     */
    @DatabaseField(columnName = "is_exchange_cash")
    private Integer isExchangeCash;

    /**
     * 抵现消耗积分
     */
    @DatabaseField(columnName = "exchange_integral_value")
    private BigDecimal exchangeIntegralValue;

    /**
     * 抵现金额
     */
    @DatabaseField(columnName = "exchange_cash_value")
    private BigDecimal exchangeCashValue;

    /**
     * 抵现限制类型(1,无上限，2积分个数限制 3 金额百分比限制)',
     */
    @DatabaseField(columnName = "limit_type")
    private Integer limitType;

    /**
     * 限制可用积分
     */
    @DatabaseField(columnName = "limit_integral")
    private BigDecimal limitIntegral;

    /**
     * 折扣
     */
    @DatabaseField(columnName = "discount")
    private BigDecimal discount;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    /**
     * 进位规则 1：四舍五入，2 无条件进位 3, 无条件抹零
     */
    @DatabaseField(columnName = "carry_bit_rule")
    private Integer carryBitRule;

    /**
     * 是否全部享受商品折扣
     */
    @DatabaseField(columnName = "is_discount_all")
    private Integer isDiscountAll;

    /**
     * 是否折扣
     */
    @DatabaseField(columnName = "is_discount")
    private Integer isDiscount;

    /**
     * 是否积分
     */
    @DatabaseField(columnName = "is_integral")
    private Integer isIntegral;


    public Long getCustomerLevelId() {
        return customerLevelId;
    }

    public void setCustomerLevelId(Long customerLevelId) {
        this.customerLevelId = customerLevelId;
    }

    public BigDecimal getConsumeValue() {
        return consumeValue;
    }

    public void setConsumeValue(BigDecimal consumeValue) {
        this.consumeValue = consumeValue;
    }

    public BigDecimal getConsumeGainValue() {
        return consumeGainValue;
    }

    public void setConsumeGainValue(BigDecimal consumeGainValue) {
        this.consumeGainValue = consumeGainValue;
    }

    public TrueOrFalse getIsGainAll() {
        return ValueEnums.toEnum(TrueOrFalse.class, isGainAll);
    }

    public void setIsGainAll(TrueOrFalse isGainAll) {
        this.isGainAll = ValueEnums.toValue(isGainAll);
    }

    public TrueOrFalse getIsExchangeCash() {
        return ValueEnums.toEnum(TrueOrFalse.class, isExchangeCash);
    }

    public void setIsExchangeCash(TrueOrFalse isExchangeCash) {
        this.isExchangeCash = ValueEnums.toValue(isExchangeCash);
    }

    public BigDecimal getExchangeIntegralValue() {
        return exchangeIntegralValue;
    }

    public void setExchangeIntegralValue(BigDecimal exchangeIntegralValue) {
        this.exchangeIntegralValue = exchangeIntegralValue;
    }

    public BigDecimal getExchangeCashValue() {
        return exchangeCashValue;
    }

    public void setExchangeCashValue(BigDecimal exchangeCashValue) {
        this.exchangeCashValue = exchangeCashValue;
    }

    public LimitType getLimitType() {
        return ValueEnums.toEnum(LimitType.class, limitType);
    }

    public void setLimitType(LimitType limitType) {
        this.limitType = ValueEnums.toValue(limitType);
    }

    public BigDecimal getLimitIntegral() {
        return limitIntegral;
    }

    public void setLimitIntegral(BigDecimal limitIntegral) {
        this.limitIntegral = limitIntegral;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
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

    public CarryBitRule getCarryBitRule() {
        return ValueEnums.toEnum(CarryBitRule.class, carryBitRule);
    }

    public void setCarryBitRule(CarryBitRule carryBitRule) {
        this.carryBitRule = ValueEnums.toValue(carryBitRule);
    }

    public TrueOrFalse getIsDiscountAll() {
        return ValueEnums.toEnum(TrueOrFalse.class, isDiscountAll);
    }

    public void setIsDiscountAll(TrueOrFalse isDiscountAll) {
        this.isDiscountAll = ValueEnums.toValue(isDiscountAll);
    }

    public TrueOrFalse getIsDiscount() {
        return ValueEnums.toEnum(TrueOrFalse.class, isDiscount);
    }

    public void setIsDiscount(TrueOrFalse isDiscount) {
        this.isDiscount = ValueEnums.toValue(isDiscount);
    }

    public TrueOrFalse getIsIntegral() {
        return ValueEnums.toEnum(TrueOrFalse.class, isIntegral);
    }

    public void setIsIntegral(TrueOrFalse isIntegral) {
        this.isIntegral = ValueEnums.toValue(isIntegral);
    }


    public Long getPriceTempletId() {
        return priceTempletId;
    }

    public void setPriceTempletId(Long priceTempletId) {
        this.priceTempletId = priceTempletId;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(customerLevelId);
    }
}
