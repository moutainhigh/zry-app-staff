package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.EntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.util.ValueEnums;

import com.zhongmei.yunfu.db.enums.ActionType;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.DomainType;
import com.zhongmei.yunfu.db.enums.SourceChild;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.TradePayForm;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;

import java.math.BigDecimal;


@DatabaseTable(tableName = "trade")
public class Trade extends DataEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends DataEntityBase.$ {

        public static final String serialNumber = "serial_number";


        public static final String actionType = "action_type";


        public static final String bizDate = "biz_date";


        public static final String businessType = "business_type";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String deliveryType = "delivery_type";


        public static final String domainType = "domain_type";


        public static final String privilegeAmount = "privilege_amount";


        public static final String relateTradeId = "relate_trade_id";


        public static final String relateTradeUuid = "relate_trade_uuid";


        public static final String saleAmount = "sale_amount";


        public static final String skuKindCount = "sku_kind_count";


        public static final String sourceId = "source_id";


        public static final String sourceChild = "source_child";


        public static final String tradeAmount = "trade_amount";


        public static final String tradeAmountBefore = "trade_amount_before";


        public static final String tradeMemo = "trade_memo";


        public static final String tradeNo = "trade_no";


        public static final String tradePayForm = "trade_pay_form";


        public static final String tradePayStatus = "trade_pay_status";


        public static final String tradeStatus = "trade_status";


        public static final String tradeTime = "trade_time";


        public static final String tradeType = "trade_type";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";


        public static final String printTime = "print_time";

        public static final String tradePeopleCount = "trade_people_count";

    }

    @DatabaseField(columnName = "serial_number")
    private String serialNumber;

    @DatabaseField(columnName = "action_type")
    private Integer actionType;

    @DatabaseField(columnName = "biz_date")
    private Long bizDate;

    @DatabaseField(columnName = "business_type", canBeNull = false)
    private Integer businessType;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "delivery_type", canBeNull = false)
    private Integer deliveryType;

    @DatabaseField(columnName = "domain_type", canBeNull = false)
    private Integer domainType;

    @DatabaseField(columnName = "privilege_amount", canBeNull = false)
    private java.math.BigDecimal privilegeAmount;

    @DatabaseField(columnName = "relate_trade_id")
    private Long relateTradeId;

    @DatabaseField(columnName = "relate_trade_uuid")
    private String relateTradeUuid;

    @DatabaseField(columnName = "sale_amount", canBeNull = false)
    private java.math.BigDecimal saleAmount;

    @DatabaseField(columnName = "dish_kind_count", canBeNull = false)
    private Integer dishKindCount;

    @DatabaseField(columnName = "source_id", canBeNull = false, index = true)
    private Integer source;

    @DatabaseField(columnName = "source_child")
    private Integer sourceChild;

    @DatabaseField(columnName = "trade_amount", canBeNull = false)
    private java.math.BigDecimal tradeAmount;

    @DatabaseField(columnName = "trade_amount_before")
    private java.math.BigDecimal tradeAmountBefore;

    @DatabaseField(columnName = "trade_memo")
    private String tradeMemo;

    @DatabaseField(columnName = "trade_no", canBeNull = false)
    private String tradeNo;

    @DatabaseField(columnName = "trade_pay_form", canBeNull = false)
    private Integer tradePayForm = TradePayForm.OFFLINE.value();

    @DatabaseField(columnName = "trade_pay_status", canBeNull = false)
    private Integer tradePayStatus;

    @DatabaseField(columnName = "trade_status", canBeNull = false, index = true)
    private Integer tradeStatus;

    @DatabaseField(columnName = "trade_time")
    private Long tradeTime;

    @DatabaseField(columnName = "trade_type", canBeNull = false)
    private Integer tradeType;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "print_time")
    private Long printTime;

    @DatabaseField(columnName = "trade_people_count")
    private Integer tradePeopleCount;



    private BigDecimal dishAmount;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public ActionType getActionType() {
        return ValueEnums.toEnum(ActionType.class, actionType);
    }

    public void setActionType(ActionType actionType) {
        this.actionType = ValueEnums.toValue(actionType);
    }

    public Long getBizDate() {
        return bizDate;
    }

    public void setBizDate(Long bizDate) {
        this.bizDate = bizDate;
    }

    public BusinessType getBusinessType() {
        return ValueEnums.toEnum(BusinessType.class, businessType);
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = ValueEnums.toValue(businessType);
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

    public DeliveryType getDeliveryType() {
        return ValueEnums.toEnum(DeliveryType.class, deliveryType);
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = ValueEnums.toValue(deliveryType);
    }

    public DomainType getDomainType() {
        return ValueEnums.toEnum(DomainType.class, domainType);
    }

    public void setDomainType(DomainType domainType) {
        this.domainType = ValueEnums.toValue(domainType);
    }

    public java.math.BigDecimal getPrivilegeAmount() {
        return privilegeAmount;
    }

    public void setPrivilegeAmount(java.math.BigDecimal privilegeAmount) {
        this.privilegeAmount = privilegeAmount;
    }

    public Long getRelateTradeId() {
        return relateTradeId;
    }

    public void setRelateTradeId(Long relateTradeId) {
        this.relateTradeId = relateTradeId;
    }

    public String getRelateTradeUuid() {
        return relateTradeUuid;
    }

    public void setRelateTradeUuid(String relateTradeUuid) {
        this.relateTradeUuid = relateTradeUuid;
    }

    public java.math.BigDecimal getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(java.math.BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
    }

    public Integer getDishKindCount() {
        return dishKindCount;
    }

    public void setDishKindCount(Integer dishKindCount) {
        this.dishKindCount = dishKindCount;
    }

    public SourceId getSource() {
        return ValueEnums.toEnum(SourceId.class, source);
    }

    public void setSource(SourceId source) {
        this.source = ValueEnums.toValue(source);
    }

    public SourceChild getSourceChild() {
        return ValueEnums.toEnum(SourceChild.class, sourceChild);
    }

    public void setSourceChild(SourceChild sourceChild) {
        this.sourceChild = ValueEnums.toValue(sourceChild);
    }

    public java.math.BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(java.math.BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public java.math.BigDecimal getTradeAmountBefore() {
        return tradeAmountBefore;
    }

    public void setTradeAmountBefore(java.math.BigDecimal tradeAmountBefore) {
        this.tradeAmountBefore = tradeAmountBefore;
    }

    public String getTradeMemo() {
        return tradeMemo;
    }

    public void setTradeMemo(String tradeMemo) {
        this.tradeMemo = tradeMemo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public TradePayForm getTradePayForm() {
        return ValueEnums.toEnum(TradePayForm.class, tradePayForm);
    }

    public void setTradePayForm(TradePayForm tradePayForm) {
        this.tradePayForm = ValueEnums.toValue(tradePayForm);
    }

    public TradePayStatus getTradePayStatus() {
        return ValueEnums.toEnum(TradePayStatus.class, tradePayStatus);
    }

    public void setTradePayStatus(TradePayStatus tradePayStatus) {
        this.tradePayStatus = ValueEnums.toValue(tradePayStatus);
    }

    public TradeStatus getTradeStatus() {
        return ValueEnums.toEnum(TradeStatus.class, tradeStatus);
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = ValueEnums.toValue(tradeStatus);
    }

    public Long getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Long tradeTime) {
        this.tradeTime = tradeTime;
    }

    public TradeType getTradeType() {
        return ValueEnums.toEnum(TradeType.class, tradeType);
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = ValueEnums.toValue(tradeType);
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

    public Long getPrintTime() {
        return printTime;
    }

    public void setPrintTime(Long printTime) {
        this.printTime = printTime;
    }

    public Integer getTradePeopleCount() {
        return tradePeopleCount;
    }

    public void setTradePeopleCount(Integer tradePeopleCount) {
        this.tradePeopleCount = tradePeopleCount;
    }

    public BigDecimal getDishAmount() {
        return dishAmount;
    }

    public void setDishAmount(BigDecimal dishAmount) {
        this.dishAmount = dishAmount;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && EntityBase.checkNonNull(businessType, deliveryType, domainType, privilegeAmount,
                saleAmount, dishKindCount, source, tradeAmount,
                tradeNo, tradePayForm, tradePayStatus, tradeStatus, tradeType);
    }
}

