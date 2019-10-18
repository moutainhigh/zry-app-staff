package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.Sex;


@DatabaseTable(tableName = "trade_customer")
public class TradeCustomer extends DataEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends DataEntityBase.$ {


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String customerId = "customer_id";


        public static final String customerName = "customer_name";


        public static final String customerPhone = "customer_phone";


        public static final String customerSex = "customer_sex";


        public static final String customerType = "customer_type";


        public static final String entitycardNum = "entitycard_num";


        public static final String tradeId = "trade_id";


        public static final String tradeUuid = "trade_uuid";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";

    }

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "customer_id")
    private Long customerId;

    @DatabaseField(columnName = "customer_name")
    private String customerName;

    @DatabaseField(columnName = "customer_phone")
    private String customerPhone;

    @DatabaseField(columnName = "customer_sex")
    private Integer customerSex;

    @DatabaseField(columnName = "customer_type", canBeNull = false)
    private Integer customerType;

    @DatabaseField(columnName = "entitycard_num")
    private String entitycardNum;

    @DatabaseField(columnName = "trade_id")
    private Long tradeId;

    @DatabaseField(columnName = "trade_uuid", canBeNull = false)
    private String tradeUuid;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;


    public transient String nation;
    public transient String country;
    public transient String nationalTelCode;


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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Sex getCustomerSex() {
        return ValueEnums.toEnum(Sex.class, customerSex);
    }

    public void setCustomerSex(Sex customerSex) {
        this.customerSex = ValueEnums.toValue(customerSex);
    }

    public CustomerType getCustomerType() {
        return ValueEnums.toEnum(CustomerType.class, customerType);
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = ValueEnums.toValue(customerType);
    }

    public String getEntitycardNum() {
        return entitycardNum;
    }

    public void setEntitycardNum(String entitycardNum) {
        this.entitycardNum = entitycardNum;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
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

    public Long levelId;

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(customerType, tradeUuid);
    }
}

