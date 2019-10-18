package com.zhongmei.yunfu.db.entity.crm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.CommonEntityBase;


@DatabaseTable(tableName = "crm_customer_rights_config")
public class CrmCustomerRightsConfig extends CommonEntityBase {
    public interface $ extends CommonEntityBase.$ {
        String priceLimit = "price_limit";
        String commercialId = "commercial_id";
    }

    @DatabaseField(columnName = "brand_id")
    public Long brandId;

    @DatabaseField(columnName = "commercial_id")
    public Long commercialId;

    @DatabaseField(columnName = "price_limit", canBeNull = false)
    public Integer priceLimit;

    @DatabaseField(columnName = "creator_id")
    public Long creatorId;

    @DatabaseField(columnName = "creator_name")
    public String creatorName;

    @DatabaseField(columnName = "updator_id")
    public Long updatorId;

    @DatabaseField(columnName = "updator_name")
    public String updatorName;
}
