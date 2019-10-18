package com.zhongmei.yunfu.db.entity.crm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.CrmBasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.util.ValueEnums;


@DatabaseTable(tableName = "crm_customer_threshold")
public class CrmCustomerThreshold extends CrmBasicEntityBase implements ICreator, IUpdator {



    public interface $ extends CrmBasicEntityBase.$ {


        public static final String isOpen = "is_open";



        public static final String customerThreSholdType = "customer_threshold_type";

    }

    @DatabaseField(columnName = "is_open")
    private Integer isOpen;

    @DatabaseField(columnName = "customer_threshold_type", canBeNull = false)
    private Long customerThresholdType;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    public Bool getIsOpen() {
        return ValueEnums.toEnum(Bool.class, isOpen);
    }

    public void setIsOpen(Bool isOpen) {
        this.isOpen = ValueEnums.toValue(isOpen);
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

    public Long getCustomerThresholdType() {
        return customerThresholdType;
    }

    public void setCustomerThresholdType(Long customerThresholdType) {
        this.customerThresholdType = customerThresholdType;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(customerThresholdType);
    }
}
