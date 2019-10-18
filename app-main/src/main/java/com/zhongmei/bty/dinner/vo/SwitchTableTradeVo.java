package com.zhongmei.bty.dinner.vo;

import java.io.Serializable;



public class SwitchTableTradeVo implements Serializable {

    public static final String KEY = SwitchTableTradeVo.class.getSimpleName();

    private Long zoneId;

    private Long tableId;

    private Long TradeId;

    private boolean isCloseTableInfoFragment;

    public SwitchTableTradeVo(Long zoneId, Long tableId) {
        this(zoneId, tableId, null, true);
    }


    public SwitchTableTradeVo(Long zoneId, Long tableId, Long tradeId) {
        this(zoneId, tableId, tradeId, true);
    }

    public SwitchTableTradeVo(Long zoneId, Long tableId, Long tradeId, boolean closeTableInfoFragment) {
        setZoneId(zoneId);
        setTableId(tableId);
        setTradeId(tradeId);
        setCloseTableInfoFragment(closeTableInfoFragment);
    }


    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Long getTradeId() {
        return TradeId;
    }

    public void setTradeId(Long tradeId) {
        TradeId = tradeId;
    }

    public boolean isCloseTableInfoFragment() {
        return isCloseTableInfoFragment;
    }

    public void setCloseTableInfoFragment(boolean closeTableInfoFragment) {
        isCloseTableInfoFragment = closeTableInfoFragment;
    }
}
