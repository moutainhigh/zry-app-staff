package com.zhongmei.bty.basemodule.customer.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.CrmBasicEntityBase;
import com.zhongmei.yunfu.db.enums.Bool;

/**
 * @since 2018.05.30.
 */
@DatabaseTable(tableName = "crm_member_day")
public class CrmMemberDay extends CrmBasicEntityBase {
//    commercialId	Long	门店id
//    memberDayType	Integer	会员日类型(1:星期;2:日期)
//    memberDayValue	String	会员日(1,3,7）（星期1，星期3，星期日是会员日或者1号、3号、7号为会员日，由memberDayType决定）
//            integraln	Integer	N倍积分(2-99)
//    memberPriceTempletId	Long	会员价模板id
//    setType	Integer	设置类型(1:取品牌;2:取门店)
//    isSwitch	Integer	开关(1:开启,2:关闭)

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "crm_member_day"
     */
    public interface $ extends CrmBasicEntityBase.$ {

        /**
         * commercial_id
         */
        String commercialId = "commercial_id";

        /**
         * member_day_type
         */
        String memberDayType = "member_day_type";

        /**
         * member_day_value
         */
        String memberDayValue = "member_day_value";

        /**
         * integraln
         */
        String integraln = "integraln";

        /**
         * member_price_templet_id
         */
        String memberPriceTempletId = "member_price_templet_id";

        /**
         * set_type
         */
        String setType = "set_type";

        /**
         * is_switch
         */
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
