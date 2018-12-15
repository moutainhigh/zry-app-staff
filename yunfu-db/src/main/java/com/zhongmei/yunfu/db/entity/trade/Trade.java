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

/**
 * Trade is a ORMLite bean type. Corresponds to the database table "trade"
 */
@DatabaseTable(tableName = "trade")
public class Trade extends DataEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "trade"
     */
    public interface $ extends DataEntityBase.$ {
        /**
         * serial_number 流水号
         */
        public static final String serialNumber = "serial_number";

        /**
         * action_type 操作类型 1为手动 2为自动
         */
        public static final String actionType = "action_type";

        /**
         * biz_date 营业日期，由服务端生成
         */
        public static final String bizDate = "biz_date";

        /**
         * business_type 业务形态 1:SNACK:快餐 2:DINNER:正餐
         */
        public static final String businessType = "business_type";

        /**
         * creator_id 创建者，创建此记录的系统用户
         */
        public static final String creatorId = "creator_id";

        /**
         * creator_name 创建者姓名
         */
        public static final String creatorName = "creator_name";

        /**
         * delivery_type
         * 订单形式：1:HERE:内用，在店内点餐，可以绑定桌号或者叫餐号
         * 2:SEND:外送，需要填写收货人信息和期望送达时间
         * 3:TAKE:自提，需要填写取货人和约定取货时间
         * 4:CARRY:外带，顾客立即拿走的，以便告知是否打包
         * 15:UNKNOW:未知（比如无单收银）
         */
        public static final String deliveryType = "delivery_type";

        /**
         * domain_type 所属领域:RESTAURANT:餐饮业
         */
        public static final String domainType = "domain_type";

        /**
         * privilege_amount 各种优惠折扣的减免金额，销货时为负数，退货时为正数
         */
        public static final String privilegeAmount = "privilege_amount";

        /**
         * relate_trade_id 1、退货所对应的销货单 2、拆单时对应的原单
         */
        public static final String relateTradeId = "relate_trade_id";

        /**
         * relate_trade_uuid
         */
        public static final String relateTradeUuid = "relate_trade_uuid";

        /**
         * sale_amount 销售金额，明细SALE_AMOUNT之和
         */
        public static final String saleAmount = "sale_amount";

        /**
         * sku_kind_count 商品种数，每种商品计1，组合套餐明细不计
         */
        public static final String skuKindCount = "sku_kind_count";

        /**
         * source
         * 来源：1:android(删了)，2:ios(删了)，3:微信，4:百度外卖，5:百度直达号，6:百度糯米，
         * 7:百度地图，8:呼叫中心，9:自助终端，10:商户收银终端，11:商户官网，12:电话订单，
         * 13:loyal，14:OnMobile，15：熟客,16:饿了么 ,17:点评点菜,18:美团外卖
         */
        public static final String sourceId = "source_id";

        /**
         * source_child
         * 子来源：1:ANDROID收银终端，2:IPAD自助终端，3:ipad收银终端，31:微信官微，32:微信商微，
         * 33:微信快捷支付，41:百度外卖，51:百度直达号，61:百度糯米点菜，71:百度地图，
         * 81:呼叫中心，91:必胜客自助，92味千,111:商户官网，131:loyal，141:OnMobile，
         * 151:熟客，161:饿了么,171:点评正餐，181：美团外卖菜
         */
        public static final String sourceChild = "source_child";

        /**
         * trade_amount 交易金额，等于SALE_AMOUNT与PRIVILEGE_AMOUNT之和
         */
        public static final String tradeAmount = "trade_amount";

        /**
         * trade_amount_before 进位处理之前金额
         */
        public static final String tradeAmountBefore = "trade_amount_before";

        /**
         * trade_memo 备注
         */
        public static final String tradeMemo = "trade_memo";

        /**
         * trade_no 单号,生成规则:固定编码(3)+年月日时分秒(yyMMddHHmmSS)+erp后台设置硬件编号(6)
         */
        public static final String tradeNo = "trade_no";

        /**
         * trade_pay_form 默认值1：线下支付2：在线支付3：组合支付 暂时是微信在使用
         */
        public static final String tradePayForm = "trade_pay_form";

        /**
         * trade_pay_status
         * 支付状态： 1:UNPAID:未支付  2:PAYING:支付中，微信下单选择了在线支付但实际上未完成支付的  (删了)
         * 3:PAID:已支付  4:REFUNDING:退款中  5:REFUNDED:已退款  6:REFUND_FAILED:退款失败
         * 7:PREPAID:预支付(现在都没用)  8:WAITING_REFUND:等待退款 9:PAID_FAIL:支付失败
         */
        public static final String tradePayStatus = "trade_pay_status";

        /**
         * trade_status
         * 交易状态(订单状态)  1:UNPROCESSED:未处理  2:TEMPORARY:挂单，不需要厨房打印(客户端本地的.)
         * 3:CONFIRMED:已确认  4:FINISH:已完成(全部支付)  5:RETURNED:已退货
         * 6:INVALID:已作废  7:REFUSED:已拒绝,  8:已取消 10:已反结账 11:已挂账 12:已销账 13:待清账
         */
        public static final String tradeStatus = "trade_status";

        /**
         * trade_time 交易时间
         */
        public static final String tradeTime = "trade_time";

        /**
         * trade_type
         * 交易类型 1:SELL:售货 2:REFUND:退货 3:SPLIT:拆单 4:REPAY:反结账 5:REPAY_FOR_REFUND:反结账退货,6冲账售货单,7冲账退货单
         */
        public static final String tradeType = "trade_type";

        /**
         * updator_id 最后修改此记录的用户
         */
        public static final String updatorId = "updator_id";

        /**
         * updator_name 最后修改者姓名
         */
        public static final String updatorName = "updator_name";

        /**
         * print_time 订单打印时间
         */
        public static final String printTime = "print_time";
        /**
         * trade_people_count 订单就餐人数订单实际就餐人数，放在总表里面,是方便后台根据总金额做统计
         */
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


    /**
     * 订单菜品金额总和，其中不包含附加费和优惠信息
     */
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

