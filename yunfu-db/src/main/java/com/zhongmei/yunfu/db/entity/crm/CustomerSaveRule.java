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

@DatabaseTable(tableName = "customer_save_rule")
public class CustomerSaveRule extends CrmBasicEntityBase implements ICreator, IUpdator {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "crm_customer_level_rights"
     */
    public interface $ extends CrmBasicEntityBase.$ {

        /**
         * 充值金额
         */
        public static final String storedValue = "stored_value";

        /**
         * 赠送金额
         */
        public static final String giveValue = "give_value";

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
     * 充值金额
     */
    @DatabaseField(columnName = "stored_value")
    private BigDecimal storedValue;

    /**
     * 赠送金额
     */
    @DatabaseField(columnName = "give_value")
    private BigDecimal giveValue;

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


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public BigDecimal getStoredValue() {
        return storedValue;
    }

    public void setStoredValue(BigDecimal storedValue) {
        this.storedValue = storedValue;
    }

    public BigDecimal getGiveValue() {
        return giveValue;
    }

    public void setGiveValue(BigDecimal giveValue) {
        this.giveValue = giveValue;
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
}
