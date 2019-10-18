package com.zhongmei.bty.basemodule.trade.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.AbsBasicEntityBase;



@DatabaseTable(tableName = "trade_extra_secrecy_phone")
public class TradeExtraSecrecyPhone extends AbsBasicEntityBase {
    private static final long serialVersionUID = 1L;

    public interface $ extends AbsBasicEntityBase.$ {

        String tradeExtraId = "trade_extra_id";

        String virtualPhone = "virtual_phone";

        String virtualPhoneExt = "virtual_phone_ext";

        String shopIdenty = "shop_identy";


        String deviceIdenty = "device_identy";
    }


    @DatabaseField(columnName = "trade_extra_id")
    private Long tradeExtraId;

    @DatabaseField(columnName = "virtual_phone")
    private String virtualPhone;

    @DatabaseField(columnName = "virtual_phone_ext")
    private String virtualPhoneExt;

    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty;

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
