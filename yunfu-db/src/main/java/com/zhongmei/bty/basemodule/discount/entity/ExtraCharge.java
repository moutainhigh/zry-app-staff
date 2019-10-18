package com.zhongmei.bty.basemodule.discount.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.discount.enums.ExtraChargeCalcWay;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.commonmodule.database.enums.TradeInitConfigKeyId;
import com.zhongmei.yunfu.context.util.GsonUtil;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;


@DatabaseTable(tableName = "extra_charge")
public class ExtraCharge extends BasicEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends BasicEntityBase.$ {



        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String calcWay = "calc_way";


        public static final String content = "content";


        public static final String orderFlag = "order_flag";


        public static final String discountFlag = "discount_flag";


        public static final String enabledFlag = "enabled_flag";


        public static final String name = "name";


        public static final String shopIdenty = "shop_identy";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";


        public static final String code = "code";


        public static final String contentByTime = "content_by_time";

        String businessType = "business_type";

        String taxCode = "tax_code";

        String privilegeFlag = "privilege_flag";

        String enableTableArea = "enable_table_area";
    }

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "code")
    private String code;

    @DatabaseField(columnName = "calc_way", canBeNull = false)
    private Integer calcWay;

    @DatabaseField(columnName = "order_flag", canBeNull = false)
    private Integer orderFlag;

    @DatabaseField(columnName = "discount_flag", canBeNull = false)
    private Integer discountFlag;

    @DatabaseField(columnName = "enabled_flag", canBeNull = false)
    private Integer enabledFlag;

    @DatabaseField(columnName = "content", canBeNull = false)
    private String content;

    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;

    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "content_by_time")
    private BigDecimal contentByTime;

    @DatabaseField(columnName = $.businessType)
    private Integer businessType;

    @DatabaseField(columnName = $.taxCode)
    private String taxCode;

    @DatabaseField(columnName = $.privilegeFlag)
    private Integer privilegeFlag;

    @DatabaseField(columnName = $.enableTableArea)
    private Integer enableTableArea;

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

    public ExtraChargeCalcWay getCalcWay() {
        return ValueEnums.toEnum(ExtraChargeCalcWay.class, calcWay);
    }

    public void setCalcWay(ExtraChargeCalcWay calcWay) {
        this.calcWay = ValueEnums.toValue(calcWay);
    }

    public Bool getOrderFlag() {
        return ValueEnums.toEnum(Bool.class, orderFlag);
    }

    public void setOrderFlag(Bool orderFlag) {
        this.orderFlag = ValueEnums.toValue(orderFlag);
    }

    public Bool getDiscountFlag() {
        return ValueEnums.toEnum(Bool.class, discountFlag);
    }

    public void setDiscountFlag(Bool discountFlag) {
        this.discountFlag = ValueEnums.toValue(discountFlag);
    }

    public Bool getEnabledFlag() {
        return ValueEnums.toEnum(Bool.class, enabledFlag);
    }

    public void setEnabledFlag(Bool enabledFlag) {
        this.enabledFlag = ValueEnums.toValue(enabledFlag);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getContentByTime() {
        return contentByTime;
    }

    public void setContentByTime(BigDecimal contentByTime) {
        this.contentByTime = contentByTime;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Integer getPrivilegeFlag() {
        return privilegeFlag;
    }

    public void setPrivilegeFlag(Integer privilegeFlag) {
        this.privilegeFlag = privilegeFlag;
    }


    public boolean isAutoJoinTrade() {
        return getOrderFlag() == Bool.YES;
    }

    public boolean isDiscountAfter() {
        return Utils.equals(privilegeFlag, 2);
    }

    public Integer getEnableTableArea() {
        return enableTableArea;
    }

    public void setEnableTableArea(Integer enableTableArea) {
        this.enableTableArea = enableTableArea;
    }


    public boolean isTableAreaEnable() {
        return Utils.equals(enableTableArea, 1);
    }

    public static ExtraCharge create(ExtraCharge extraCharge) {
        ExtraCharge newExtraCharge = new ExtraCharge();
        newExtraCharge.validateCreate();
        newExtraCharge.creatorId = extraCharge.creatorId;
        newExtraCharge.creatorName = extraCharge.creatorName;
        newExtraCharge.code = extraCharge.code;
        newExtraCharge.calcWay = extraCharge.calcWay;
        newExtraCharge.orderFlag = extraCharge.orderFlag;
        newExtraCharge.discountFlag = extraCharge.discountFlag;
        newExtraCharge.enabledFlag = extraCharge.enabledFlag;
        newExtraCharge.content = extraCharge.content;
        newExtraCharge.name = extraCharge.name;
        newExtraCharge.shopIdenty = extraCharge.shopIdenty;
        newExtraCharge.updatorId = extraCharge.updatorId;
        newExtraCharge.updatorName = extraCharge.updatorName;
        newExtraCharge.contentByTime = extraCharge.contentByTime;
        newExtraCharge.businessType = extraCharge.businessType;
        newExtraCharge.taxCode = extraCharge.taxCode;
        newExtraCharge.privilegeFlag = extraCharge.privilegeFlag;
        newExtraCharge.enableTableArea = extraCharge.enableTableArea;
        return newExtraCharge;
    }

    public TradeInitConfig toTradeInitConfig() {
        TradeInitConfig initConfig = new TradeInitConfig();
        initConfig.validateCreate();
        initConfig.setKeyId(TradeInitConfigKeyId.SERVICE_CHARGE_RATE);
        ServiceCharge mapValue = new ServiceCharge();
        mapValue.id = id;
        mapValue.name = name;
        mapValue.calcWay = calcWay;
        mapValue.feeRate = content;
        mapValue.discountType = isDiscountAfter() ? 1 : 2;
        initConfig.setValue(GsonUtil.objectToJson(mapValue));
        return initConfig;
    }

    static public class ServiceCharge {
        public Long id;         public String name;         public Integer calcWay;         public String feeRate;         public Integer discountType;
        public Integer getPrivilegeFlag() {
            return Utils.equals(discountType, 2) ? 1 : 2;
        }
    }
}
