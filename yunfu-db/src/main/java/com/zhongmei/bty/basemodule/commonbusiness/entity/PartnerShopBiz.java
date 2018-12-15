package com.zhongmei.bty.basemodule.commonbusiness.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.yunfu.util.ValueEnums;

/**
 * PartnerShopBiz is a ORMLite bean type. Corresponds to the database table "partner_shop_biz"
 */
@DatabaseTable(tableName = "partner_shop_biz")
public class PartnerShopBiz extends BasicEntityBase {
    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "partner_shop_biz"
     */
    public interface $ extends BasicEntityBase.$ {
        /**
         * 商户标识
         */
        String shopIdenty = "shop_identy";

        /**
         * 业务类型（1-外卖，2-点餐，3-配送，4-发票,5, "数据导出",6,"CRM",7, "打印",8, "库存",9, "排队"
         */
        String bizType = "biz_type";

        /**
         * 业务编码（外卖点餐即订单来源，配送即配送平台编码）
         */
        String source = "source";

        /**
         * 业务编码名称
         */
        String sourceName = "source_name";

        /**
         * 业务子编码（外卖点餐即订单子来源编码）
         */
        String sourceChild = "source_child";

        /**
         * 业务子来源名称
         */
        String sourceChildName = "source_child_name";

        /**
         * 是否启用（1-是，0-否）
         */
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

    /**
     * 是否支持小费
     *
     * @return true为支持，false为不支持
     */
    public boolean isSupportTip() {
        return bizType == 3 && source == 5;
    }

    /**
     * 是否支持配送费
     *
     * @return true为支持，false为不支持
     */
    public boolean isSupportDeliveryFee() {
        return bizType == 3 && (source == 7 || source == 8);
    }
}
