package com.zhongmei.bty.commonmodule.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

import java.math.BigDecimal;


@DatabaseTable(tableName = "trade_earnest_money")
public class TradeEarnestMoney extends IdEntityBase {
    private static final long serialVersionUID = 1L;


    public interface $ extends IdEntityBase.$ {
        String tradeId = "tradeId";
        String brandIdenty = "brandIdenty";
        String shopIdenty = "shopIdenty";
        String statusFlag = "statusFlag";
        String serverCreateTime = "serverCreateTime";
        String serverUpdateTime = "serverUpdateTime";
        String creatorId = "creatorId";
        String creatorName = "creatorName";
        String earnestMoney = "earnestMoney";
        String payModeId = "payModeId";
    }

    @DatabaseField(columnName = $.tradeId)
    private Long tradeId;
    @DatabaseField(columnName = $.brandIdenty)
    private Long brandIdenty;
    @DatabaseField(columnName = $.shopIdenty)
    private Long shopIdenty;
    @DatabaseField(columnName = $.statusFlag)
    private Integer statusFlag;
    @DatabaseField(columnName = $.serverCreateTime)
    private Long serverCreateTime;
    @DatabaseField(columnName = $.serverUpdateTime)
    private Long serverUpdateTime;
    @DatabaseField(columnName = $.creatorId)
    private Long creatorId;
    @DatabaseField(columnName = $.creatorName)
    private String creatorName;
    @DatabaseField(columnName = $.earnestMoney)
    private BigDecimal earnestMoney;
    @DatabaseField(columnName = $.payModeId)
    private Long payModeId;
    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(YesOrNo.YES, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    public BigDecimal getEarnestMoney() {
        return earnestMoney;
    }

    public Long getPayModeId() {
        return payModeId;
    }


    public Long getTradeId() {
        return tradeId;
    }
}
