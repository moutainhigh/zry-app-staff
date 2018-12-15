package com.zhongmei.bty.basemodule.customer.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.CrmBasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;

@DatabaseTable(tableName = "crm_customer_level_rights_dish")
public class CrmCustomerLevelRightsDish extends CrmBasicEntityBase implements ICreator, IUpdator {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "crm_customer_level_rights"
     */
    public interface $ extends CrmBasicEntityBase.$ {

        /**
         * rights_type
         */
        public static final String rightsType = "rights_type";

        /**
         * customer_level_rights_id
         */
        public static final String customerLevelRightsId = "customer_level_rights_id";

        /**
         * dish_brand_id
         */
        public static final String dishBrandId = "dish_brand_id";

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

    }

    /**
     * 权益类型（1：积分，2折扣）
     */
    @DatabaseField(columnName = "rights_type")
    private Short rightsType;

    /**
     * 会员权益id
     */
    @DatabaseField(columnName = "customer_level_rights_id", canBeNull = false)
    private Long customerLevelRightsId;

    /**
     * 品牌菜id
     */
    @DatabaseField(columnName = "dish_brand_id")
    private Long dishBrandId;

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

    public Short getRightsType() {
        return rightsType;
    }

    public void setRightsType(Short rightsType) {
        this.rightsType = rightsType;
    }

    public Long getCustomerLevelRightsId() {
        return customerLevelRightsId;
    }

    public void setCustomerLevelRightsId(Long customerLevelRightsId) {
        this.customerLevelRightsId = customerLevelRightsId;
    }

    public Long getDishBrandId() {
        return dishBrandId;
    }

    public void setDishBrandId(Long dishBrandId) {
        this.dishBrandId = dishBrandId;
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

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(customerLevelRightsId);
    }
}
