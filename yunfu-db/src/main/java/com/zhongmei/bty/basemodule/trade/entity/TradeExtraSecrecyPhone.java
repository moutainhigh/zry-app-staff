package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.AbsBasicEntityBase;

/**
 * 第三方隐私手机号
 */

@DatabaseTable(tableName = "trade_extra_secrecy_phone")
public class TradeExtraSecrecyPhone extends AbsBasicEntityBase {
    private static final long serialVersionUID = 1L;

    public interface $ extends AbsBasicEntityBase.$ {
        /**
         * trade_extra_id
         */
        String tradeExtraId = "trade_extra_id";
        /**
         * virtual_phone
         */
        String virtualPhone = "virtual_phone";
        /**
         * virtual_phone_ext
         */
        String virtualPhoneExt = "virtual_phone_ext";
        /**
         * shop_identy
         */
        String shopIdenty = "shop_identy";

        /**
         * device_identy
         */
        String deviceIdenty = "device_identy";
    }

    /**
     * TradeExtra表Id
     */
    @DatabaseField(columnName = "trade_extra_id")
    private Long tradeExtraId;
    /**
     * 虚拟手机号
     */
    @DatabaseField(columnName = "virtual_phone")
    private String virtualPhone;
    /**
     * 虚拟手机分机号
     */
    @DatabaseField(columnName = "virtual_phone_ext")
    private String virtualPhoneExt;
    /**
     * 品牌Identy
     */
    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty;
    /**
     * 门店Identy
     */
    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    public Long getTradeExtraId() {
        return tradeExtraId;
    }

    public void setTradeExtraId(Long tradeExtraId) {
        this.tradeExtraId = tradeExtraId;
    }

    public String getVirtualPhone() {
        return virtualPhone;
    }

    public void setVirtualPhone(String virtualPhone) {
        this.virtualPhone = virtualPhone;
    }

    public String getVirtualPhoneExt() {
        return virtualPhoneExt;
    }

    public void setVirtualPhoneExt(String virtualPhoneExt) {
        this.virtualPhoneExt = virtualPhoneExt;
    }

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }
}
