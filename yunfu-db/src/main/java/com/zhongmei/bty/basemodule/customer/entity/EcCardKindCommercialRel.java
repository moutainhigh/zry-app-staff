package com.zhongmei.bty.basemodule.customer.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.customer.enums.EntityCardCommercialType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.CrmBasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;


@DatabaseTable(tableName = "ec_card_kind_commercial_rel")
public class EcCardKindCommercialRel extends CrmBasicEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends CrmBasicEntityBase.$ {


        public static final String cardKindId = "card_kind_id";


        public static final String commercialId = "commercial_id";


        public static final String commercialType = "commercial_type";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";
    }

    @DatabaseField(columnName = "card_kind_id", canBeNull = false)
    private Long cardKindId;

    @DatabaseField(columnName = "commercial_id", canBeNull = false)
    private Long commercialId;

    @DatabaseField(columnName = "commercial_type")
    private Integer commercialType;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

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

    public Long getCardKindId() {
        return cardKindId;
    }

    public void setCardKindId(Long cardKindId) {
        this.cardKindId = cardKindId;
    }

    public Long getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(Long commercialId) {
        this.commercialId = commercialId;
    }

    public EntityCardCommercialType getCommercialType() {
        return ValueEnums.toEnum(EntityCardCommercialType.class, commercialType);
    }

    public void setCommercialType(EntityCardCommercialType commercialType) {
        this.commercialType = ValueEnums.toValue(commercialType);
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(cardKindId, commercialId);
    }
}

