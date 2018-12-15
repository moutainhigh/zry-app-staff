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

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "crm_customer_level_rights"
     */
    public interface $ extends CrmBasicEntityBase.$ {

        /**
         * is_full_send
         */
        public static final String isFullSend = "is_full_send";

        /**
         * send_type
         */
        public static final String sendType = "send_type";

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
         * coupon_type
         */
        String couponType = "coupon_type";
    }

    /**
     * 是否满赠 0 是 1 否
     */
    @DatabaseField(columnName = "is_full_send")
    private Integer isFullSend;

    /**
     * 赠送方式 1元 2比例
     */
    @DatabaseField(columnName = "send_type")
    private Integer sendType;

    /**
     * 创建者id
     */
    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    /**
     * 创建者名称
     */
    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    /**
     * 更新者id
     */
    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    /**
     * 最后修改者姓名
     */
    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    /**
     * 优惠方式
     */
    @DatabaseField(columnName = "coupon_type")
    private Integer couponType;

    /**
     * 扣款顺序 1：按比例 2：先实储后赠送 3：先赠送后实储
     */
    @DatabaseField(columnName = "consume_order")
    private Integer consumeOrder;

    /**
     * 储值门店分组Id
     */
    @DatabaseField(columnName = "group_id")
    private Long groupId;

    /**
     * 是否支持免密支付：0，不支持；1，支持
     */
    @DatabaseField(columnName = "pay_no_pwd")
    private Integer payNoPwd;

    /**
     * 免密支付的额度：-1表示不限额
     */
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
