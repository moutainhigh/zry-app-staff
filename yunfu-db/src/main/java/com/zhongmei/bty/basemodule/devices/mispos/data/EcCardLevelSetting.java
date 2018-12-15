package com.zhongmei.bty.basemodule.devices.mispos.data;

import java.math.BigDecimal;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.CrmBasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.commonmodule.database.enums.LimitType;
import com.zhongmei.bty.commonmodule.database.enums.SendType;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * 实体卡级别规则设定
 */
@DatabaseTable(tableName = "ec_card_level_setting")
public class EcCardLevelSetting extends CrmBasicEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "ec_card_level_setting"
     */
    public interface $ extends CrmBasicEntityBase.$ {

        /**
         * card_level_id
         */
        public static final String cardLevelId = "card_level_id";

        /**
         * member_price_templet_id
         */
        public static final String memberPriceTempletId = "member_price_templet_id";

        /**
         * consume_value
         */
        public static final String consumeValue = "consume_value";

        /**
         * consume_gain_value
         */
        public static final String consumeGainValue = "consume_gain_value";

        /**
         * is_exchange_cash
         */
        public static final String isExchangeCash = "is_exchange_cash";

        /**
         * is_integral_value_card
         */
        public static final String isIntegralValueCard = "is_integral_value_card";

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
         * value_card_send_type
         */
        public static final String valueCardSendType = "value_card_send_type";

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


    /**
     * 卡种等级id
     */
    @DatabaseField(columnName = "card_level_id")
    private Long cardLevelId;

    /**
     * 会员价策略id
     */
    @DatabaseField(columnName = "member_price_templet_id")
    private Long memberPriceTempletId;

    /**
     * 消费额
     */
    @DatabaseField(columnName = "consume_value")
    private BigDecimal consumeValue;

    /**
     * 获取积分值
     */
    @DatabaseField(columnName = "consume_gain_value")
    private BigDecimal consumeGainValue;

    /**
     * 是否抵现1是2否
     */
    @DatabaseField(columnName = "is_exchange_cash")
    private Integer isExchangeCash;

    /**
     * 储值消费是否积分1是2否
     */
    @DatabaseField(columnName = "is_integral_value_card")
    private Integer isIntegralValueCard;

    /**
     * 抵现消费积分
     */
    @DatabaseField(columnName = "exchange_integral_value")
    private BigDecimal exchangeIntegralValue;

    /**
     * 抵现金额
     */
    @DatabaseField(columnName = "exchange_cash_value")
    private BigDecimal exchangeCashValue;

    /**
     * 抵现限制类型1无上限，2积分个数限制
     */
    @DatabaseField(columnName = "limit_type")
    private Integer limitType;

    /**
     * 限制可用积分
     */
    @DatabaseField(columnName = "limit_integral")
    private BigDecimal limitIntegral;

    /**
     * 储值返现方式：1按金额2按百分比
     */
    @DatabaseField(columnName = "value_card_send_type")
    private Integer valueCardSendType;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    public Long getCardLevelId() {
        return cardLevelId;
    }

    public void setCardLevelId(Long cardLevelId) {
        this.cardLevelId = cardLevelId;
    }

    public Long getMemberPriceTempletId() {
        return memberPriceTempletId;
    }

    public void setMemberPriceTempletId(Long memberPriceTempletId) {
        this.memberPriceTempletId = memberPriceTempletId;
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

    public Bool getIsExchangeCash() {
        return ValueEnums.toEnum(Bool.class, isExchangeCash);
    }

    public void setIsExchangeCash(Bool isExchangeCash) {
        this.isExchangeCash = ValueEnums.toValue(isExchangeCash);
    }

    public Bool getIsIntegralValueCard() {
        return ValueEnums.toEnum(Bool.class, isIntegralValueCard);
    }

    public void setIsIntegralValueCard(Bool isIntegralValueCard) {
        this.isIntegralValueCard = ValueEnums.toValue(isIntegralValueCard);
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

    public SendType getValueCardSendType() {
        return ValueEnums.toEnum(SendType.class, valueCardSendType);
    }

    public void setValueCardSendType(SendType valueCardSendType) {
        this.valueCardSendType = ValueEnums.toValue(valueCardSendType);
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

}

