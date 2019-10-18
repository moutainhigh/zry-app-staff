package com.zhongmei.bty.basemodule.orderdish.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.AbsBasicEntityBase;

import java.math.BigDecimal;


@DatabaseTable(tableName = "kds_trade_item_part")
public class KdsTradeItemPart extends AbsBasicEntityBase {

    @DatabaseField
    public long shopId;
    @DatabaseField
    public long brandId;
    @DatabaseField
    public long tradeItemId;
    @DatabaseField
    public BigDecimal quantity;
    @DatabaseField
    public long clientCreateTime;
    @DatabaseField
    public long clientUpdateTime;

    public interface $ extends BasicEntityBase.$ {

        String shopId = "shopId";
        String brandId = "brandId";
        String tradeItemId = "tradeItemId";
        String quantity = "quantity";
        String clientCreateTime = "clientCreateTime";
        String clientUpdateTime = "clientUpdateTime";
    }

}
