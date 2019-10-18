package com.zhongmei.yunfu.db.entity.crm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.customer.enums.CouponRuleType;
import com.zhongmei.bty.basemodule.customer.enums.FullSend;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.CrmBasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.bty.commonmodule.database.enums.SendType;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

import java.math.BigDecimal;

@DatabaseTable(tableName = "crm_level_store_rule")
public class CrmLevelStoreRule extends CrmBasicEntityBase implements ICreator, IUpdator {


    private static final long serialVersionUID = 1L;


    public interface $ extends CrmBasicEntityBase.$ {


        public static final String isFullSend = "is_full_send";


        public static final String sendType = "send_type";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        String couponType = "coupon_type";
    }


    @DatabaseField(columnName = "is_full_send")
    private Integer isFullSend;


    @DatabaseField(columnName = "send_type")
    private Integer sendType;


    @DatabaseField(columnName = "creator_id")
    private Long creatorId;


    @DatabaseField(columnName = "creator_name")
    private String creatorName;


    @DatabaseField(columnName = "updator_id")
    private Long updatorId;


    @DatabaseField(columnName = "updator_name")
    private String updatorName;


    @DatabaseField(columnName = "coupon_type")
    private Integer couponType;


    @DatabaseField(columnName = "consume_order")
    private Integer consumeOrder;


    @DatabaseField(columnName = "group_id")
    private Long groupId;


    @DatabaseField(columnName = "pay_no_pwd")
    private Integer payNoPwd;


    @DatabaseField(columnName = "pay_no_pwd_amount")
    private BigDecimal payNoPwdAmount;

    public FullSend getIsFullSend() {
        return ValueEnums.toEnum(FullSend.class, isFullSend);
    }

    public void setIsFullSend(FullSend isFullSend) {
        this.isFullSend = ValueEnums.toValue(isFullSend);
    }

    public SendType getSendType() {
        return ValueEnums.toEnum(SendType.class, sendType);
    }

    public void setSendType(SendType sendType) {
        this.sendType = ValueEnums.toValue(sendType);
    }

    public CouponRuleType getCouponType() {
        return ValueEnums.toEnum(CouponRuleType.class, couponType);
    }

    public void setCouponType(CouponRuleType couponType) {
        this.couponType = ValueEnums.toValue(couponType);
    }

    @Override
    public Long getUpdatorId() {
        return updatorId;
    }

    @Override
    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    @Override
    public String getUpdatorName() {
        return updatorName;
    }

    @Override
    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    @Override
    public Long getCreatorId() {
        return creatorId;
    }

    @Override
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String getCreatorName() {
        return creatorName;
    }

    @Override
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Integer getConsumeOrder() {
        return consumeOrder;
    }

    public void setConsumeOrder(Integer consumeOrder) {
        this.consumeOrder = consumeOrder;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public YesOrNo getPayNoPwd() {
        return ValueEnums.toEnum(YesOrNo.class, payNoPwd);
    }

    public void setPayNoPwd(YesOrNo payNoPwd) {
        this.payNoPwd = ValueEnums.toValue(payNoPwd);
    }

    public BigDecimal getPayNoPwdAmount() {
        return payNoPwdAmount;
    }

    public void setPayNoPwdAmount(BigDecimal payNoPwdAmount) {
        this.payNoPwdAmount = payNoPwdAmount;
    }
}
