package com.zhongmei.bty.commonmodule.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.commonmodule.database.enums.TradeInitConfigKeyId;


@DatabaseTable(tableName = "trade_init_config")
public class TradeInitConfig extends BasicEntityBase {

    public interface $ extends BasicEntityBase.$ {

        String trade_id = "trade_id";

        String shop_identy = "shop_identy";

        String key_id = "key_id";

        String value = "value";
    }

    @DatabaseField(columnName = $.trade_id)
    private Long tradeId;

    @DatabaseField(columnName = $.key_id)
    private Integer keyId;

    @DatabaseField(columnName = $.value)
    private String value;

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public TradeInitConfigKeyId getKeyId() {
        return ValueEnums.toEnum(TradeInitConfigKeyId.class, keyId);
    }

    public void setKeyId(TradeInitConfigKeyId keyId) {
        this.keyId = ValueEnums.toValue(keyId);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void validateCreate() {
        setStatusFlag(StatusFlag.VALID);
        setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        setShopIdenty(BaseApplication.sInstance.getShopIdenty());
                validateUpdate();
    }

    public void validateUpdate() {
                setChanged(true);
    }

    public static TradeInitConfig create(TradeInitConfig config) {
        TradeInitConfig tradeInitConfig = new TradeInitConfig();
        tradeInitConfig.validateCreate();
        tradeInitConfig.tradeId = config.tradeId;
        tradeInitConfig.keyId = config.keyId;
        tradeInitConfig.value = config.value;
        return tradeInitConfig;
    }
}
