package com.zhongmei.bty.commonmodule.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.commonmodule.database.enums.SetDeliveryType;
import com.zhongmei.bty.commonmodule.database.enums.SetType;

/**
 * TableNumberSetting is a ORMLite bean type. Corresponds to the database table "table_number_setting"
 */
@DatabaseTable(tableName = "table_number_setting")
public class TableNumberSetting extends DataEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "table_number_setting"
     */
    public interface $ extends DataEntityBase.$ {

        /**
         * creator_id
         */
        public static final String creatorId = "creator_id";

        /**
         * creator_name
         */
        public static final String creatorName = "creator_name";

        /**
         * updator_id
         */
        public static final String updatorId = "updator_id";

        /**
         * updator_name
         */
        public static final String updatorName = "updator_name";

        /**
         * type
         */
        public static final String type = "type";

        /**
         * delivery_type
         */
        public static final String deliveryType = "deliveryType";

    }

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "type", canBeNull = false)
    private Integer type;

    @DatabaseField(columnName = "deliveryType", canBeNull = false)
    private Integer deliveryType;

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

    public SetType getType() {
        return ValueEnums.toEnum(SetType.class, type);
    }

    public void setType(SetType type) {
        this.type = ValueEnums.toValue(type);
    }

    public SetDeliveryType getDeliveryType() {
        return ValueEnums.toEnum(SetDeliveryType.class, deliveryType);
    }

    public void setDeliveryType(SetDeliveryType deliveryType) {
        this.deliveryType = ValueEnums.toValue(deliveryType);
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(type, deliveryType);
    }
}

