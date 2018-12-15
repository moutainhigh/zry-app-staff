package com.zhongmei.bty.basemodule.devices.mispos.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.devices.mispos.enums.WorkStatus;
import com.zhongmei.yunfu.db.CrmBasicEntityBase;
import com.zhongmei.yunfu.db.EntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.basemodule.devices.mispos.enums.CardKindStatus;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.yunfu.util.ValueEnums;

import java.math.BigDecimal;

/**
 * EcCardKind is a ORMLite bean type. Corresponds to the database table "ec_card_kind"
 */
@DatabaseTable(tableName = "ec_card_kind")
public class EcCardKind extends CrmBasicEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "ec_card_kind"
     */
    public interface $ extends CrmBasicEntityBase.$ {

        /**
         * card_kind_name
         */
        public static final String cardKindName = "card_kind_name";

        /**
         * is_need_cost
         */
        public static final String isNeedCost = "is_need_cost";

        /**
         * card_cost
         */
        public static final String cardCost = "card_cost";

        /**
         * is_need_pwd
         */
        public static final String isNeedPwd = "is_need_pwd";

        /**
         * is_integral
         */
        public static final String isIntegral = "is_integral";

        /**
         * is_discount
         */
        public static final String isDiscount = "is_discount";

        /**
         * is_value_card
         */
        public static final String isValueCard = "is_value_card";

        /**
         * is_send
         */
        public static final String isSend = "is_send";

        /**
         * max_batch_num
         */
        public static final String maxBatchNum = "max_batch_num";

        /**
         * max_seq_num
         */
        public static final String maxSeqNum = "max_seq_num";

        /**
         * card_seq_num
         */
        public static final String cardSeqNum = "card_seq_num";

        /**
         * card_kind_status
         */
        public static final String cardKindStatus = "card_kind_status";

        /**
         * is_made
         */
        public static final String isMade = "is_made";

        /**
         * dish_brand_id
         */
        public static final String dishBrandId = "dish_brand_id";

        /**
         * dish_type_id
         */
        public static final String dishTypeId = "dish_type_id";

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

        /**
         * card_type
         */
        public static final String cardType = "card_type";

        /**
         * work_status
         */
        public static final String workStatus = "work_status";
        /**
         * price
         */
        public static final String price = "price";
    }

    @DatabaseField(columnName = "card_kind_name", canBeNull = false)
    private String cardKindName;

    @DatabaseField(columnName = "is_need_cost")
    private Integer isNeedCost;

    @DatabaseField(columnName = "card_cost")
    private java.math.BigDecimal cardCost;

    @DatabaseField(columnName = "is_need_pwd")
    private Integer isNeedPwd;

    @DatabaseField(columnName = "is_integral")
    private Integer isIntegral;

    @DatabaseField(columnName = "is_discount")
    private Integer isDiscount;

    @DatabaseField(columnName = "is_value_card")
    private Integer isValueCard;

    @DatabaseField(columnName = "is_send")
    private Integer isSend;

    @DatabaseField(columnName = "max_batch_num")
    private String maxBatchNum;

    @DatabaseField(columnName = "max_seq_num")
    private String maxSeqNum;

    @DatabaseField(columnName = "card_kind_status", canBeNull = false)
    private Integer cardKindStatus;

    @DatabaseField(columnName = "is_made")
    private Integer isMade;

    @DatabaseField(columnName = "card_seq_num")
    private Integer cardSeqNum;

    @DatabaseField(columnName = "dish_brand_id")
    private Long dishBrandId;

    @DatabaseField(columnName = "dish_type_id")
    private Long dishTypeId;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "card_type")
    private Integer cardType;

    @DatabaseField(columnName = "price_Limit")//add v8.2 添加会员权益卡权益开关
    public Integer priceLimit;//0:不限制。1:使用储值支付',

    @DatabaseField(columnName = "work_status") // v8.3 卡状态
    public Integer workStatus;

    @DatabaseField(columnName = "price") // beauty 添加下行售价
    public BigDecimal price;

    public void setWorkStatus(WorkStatus workStatus) {
        this.workStatus = ValueEnums.toValue(workStatus);
    }

    public WorkStatus getWorkStatus() {
        return ValueEnums.toEnum(WorkStatus.class, workStatus);
    }


    public String getCardKindName() {
        return cardKindName;
    }

    public void setCardKindName(String cardKindName) {
        this.cardKindName = cardKindName;
    }

    public Bool getIsNeedCost() {
        return ValueEnums.toEnum(Bool.class, isNeedCost);
    }

    public void setIsNeedCost(Bool isNeedCost) {
        this.isNeedCost = ValueEnums.toValue(isNeedCost);
    }

    public java.math.BigDecimal getCardCost() {
        return cardCost;
    }

    public void setCardCost(java.math.BigDecimal cardCost) {
        this.cardCost = cardCost;
    }

    public Bool getIsNeedPwd() {
        return ValueEnums.toEnum(Bool.class, isNeedPwd);
    }

    public void setIsNeedPwd(Bool isNeedPwd) {
        this.isNeedPwd = ValueEnums.toValue(isNeedPwd);
    }

    public Bool getIsIntegral() {
        return ValueEnums.toEnum(Bool.class, isIntegral);
    }

    public void setIsIntegral(Bool isIntegral) {
        this.isIntegral = ValueEnums.toValue(isIntegral);
    }

    public Bool getIsDiscount() {
        return ValueEnums.toEnum(Bool.class, isDiscount);
    }

    public void setIsDiscount(Bool isDiscount) {
        this.isDiscount = ValueEnums.toValue(isDiscount);
    }

    public Bool getIsValueCard() {
        return ValueEnums.toEnum(Bool.class, isValueCard);
    }

    public void setIsValueCard(Bool isValueCard) {
        this.isValueCard = ValueEnums.toValue(isValueCard);
    }

    public Bool getIsSend() {
        return ValueEnums.toEnum(Bool.class, isSend);
    }

    public void setIsSend(Bool isSend) {
        this.isSend = ValueEnums.toValue(isSend);
    }

    public String getMaxBatchNum() {
        return maxBatchNum;
    }

    public void setMaxBatchNum(String maxBatchNum) {
        this.maxBatchNum = maxBatchNum;
    }

    public String getMaxSeqNum() {
        return maxSeqNum;
    }

    public void setMaxSeqNum(String maxSeqNum) {
        this.maxSeqNum = maxSeqNum;
    }

    public CardKindStatus getCardKindStatus() {
        return ValueEnums.toEnum(CardKindStatus.class, cardKindStatus);
    }

    public void setCardKindStatus(CardKindStatus cardKindStatus) {
        this.cardKindStatus = ValueEnums.toValue(cardKindStatus);
    }

    public Bool getIsMade() {
        return ValueEnums.toEnum(Bool.class, isMade);
    }

    public void setIsMade(Bool isMade) {
        this.isMade = ValueEnums.toValue(isMade);
    }

    public Integer getCardSeqNum() {
        return cardSeqNum;
    }

    public void setCardSeqNum(Integer cardSeqNum) {
        this.cardSeqNum = cardSeqNum;
    }

    public Long getDishTypeId() {
        return dishTypeId;
    }

    public void setDishTypeId(Long dishTypeId) {
        this.dishTypeId = dishTypeId;
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

    public Long getDishBrandId() {
        return dishBrandId;
    }

    public void setDishBrandId(Long dishBrandId) {
        this.dishBrandId = dishBrandId;
    }

    public EntityCardType getCardType() {
        return ValueEnums.toEnum(EntityCardType.class, cardType);
    }

    public void setCardType(EntityCardType entityCardType) {
        this.cardType = ValueEnums.toValue(entityCardType);
    }


    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && EntityBase.checkNonNull(cardKindName, cardKindStatus);
    }
}

