package com.zhongmei.bty.basemodule.commonbusiness.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.yunfu.util.ValueEnums;


@DatabaseTable(tableName = "partner_shop_biz")
public class PartnerShopBiz extends BasicEntityBase {
    private static final long serialVersionUID = 1L;


    public interface $ extends BasicEntityBase.$ {

        String shopIdenty = "shop_identy";


        String bizType = "biz_type";


        String source = "source";


        String sourceName = "source_name";


        String sourceChild = "source_child";


        String sourceChildName = "source_child_name";


        String enableFlag = "enable_flag";
    }

    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    @DatabaseField(columnName = "biz_type", canBeNull = false)
    private Integer bizType;

    @DatabaseField(columnName = "source", canBeNull = false)
    private Integer source;

    @DatabaseField(columnName = "source_name", canBeNull = false)
    private String sourceName;

    @DatabaseField(columnName = "source_child")
    private Integer sourceChild;

    @DatabaseField(columnName = "source_child_name")
    private String sourceChildName;

    @DatabaseField(columnName = "enable_flag", canBeNull = false)
    private Integer enableFlag;

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Integer getSourceChild() {
        return sourceChild;
    }

    public void setSourceChild(Integer sourceChild) {
        this.sourceChild = sourceChild;
    }

    public String getSourceChildName() {
        return sourceChildName;
    }

    public void setSourceChildName(String sourceChildName) {
        this.sourceChildName = sourceChildName;
    }

    public YesOrNo getEnableFlag() {
        return ValueEnums.toEnum(YesOrNo.class, enableFlag);
    }

    public void setEnableFlag(YesOrNo enableFlag) {
        this.enableFlag = ValueEnums.toValue(enableFlag);
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(shopIdenty, bizType, source, sourceName, enableFlag);
    }


    public boolean isSupportTip() {
        return bizType == 3 && source == 5;
    }


    public boolean isSupportDeliveryFee() {
        return bizType == 3 && (source == 7 || source == 8);
    }
}
