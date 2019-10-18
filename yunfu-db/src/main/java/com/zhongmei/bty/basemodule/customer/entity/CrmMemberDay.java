package com.zhongmei.bty.basemodule.customer.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.CrmBasicEntityBase;
import com.zhongmei.yunfu.db.enums.Bool;


@DatabaseTable(tableName = "crm_member_day")
public class CrmMemberDay extends CrmBasicEntityBase {


    private static final long serialVersionUID = 1L;


    public interface $ extends CrmBasicEntityBase.$ {


        String commercialId = "commercial_id";


        String memberDayType = "member_day_type";


        String memberDayValue = "member_day_value";


        String integraln = "integraln";


        String memberPriceTempletId = "member_price_templet_id";


        String setType = "set_type";


        String isSwitch = "is_switch";
    }

    @DatabaseField(columnName = "commercial_id")
    private Long commercialId;

    @DatabaseField(columnName = "member_day_type")
    private Integer memberDayType;

    @DatabaseField(columnName = "member_day_value")
    private String memberDayValue;

    @DatabaseField(columnName = "integraln")
    private Integer integraln;

    @DatabaseField(columnName = "member_price_templet_id")
    private Long memberPriceTempletId;

    @DatabaseField(columnName = "set_type")
    private Integer setType;

    @DatabaseField(columnName = "is_switch")
    private Integer isSwitch;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(Long commercialId) {
        this.commercialId = commercialId;
    }

    public Integer getMemberDayType() {
        return memberDayType;
    }

    public void setMemberDayType(Integer memberDayType) {
        this.memberDayType = memberDayType;
    }

    public String getMemberDayValue() {
        return memberDayValue;
    }

    public void setMemberDayValue(String memberDayValue) {
        this.memberDayValue = memberDayValue;
    }

    public Integer getIntegraln() {
        return integraln;
    }

    public void setIntegraln(Integer integraln) {
        this.integraln = integraln;
    }

    public Long getMemberPriceTempletId() {
        return memberPriceTempletId;
    }

    public void setMemberPriceTempletId(Long memberPriceTempletId) {
        this.memberPriceTempletId = memberPriceTempletId;
    }

    public Integer getSetType() {
        return setType;
    }

    public void setSetType(Integer setType) {
        this.setType = setType;
    }

    public boolean getIsSwitch() {
        return ValueEnums.equalsValue(Bool.YES, isSwitch);
    }

    public void setIsSwitch(Integer isSwitch) {
        this.isSwitch = isSwitch;
    }
}
