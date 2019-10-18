package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.bty.basemodule.orderdish.bean.AddItemVo;
import com.zhongmei.bty.basemodule.print.entity.PrintOperation;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

import java.math.BigDecimal;
import java.util.List;



public class TradeTableInfo implements Comparable<TradeTableInfo> {

    public final Long id;


    public final String uuid;


    public final Long serverUpdateTime;


    public final String tradeUuid;


    public final Long tradeId;


    public final Long tradeServerUpdateTime;


    public final Long tradeClientUpdateTime;


    public final TradeStatus tradeStatus;

    public final BusinessType businessType;


    public final String serialNumber;

    public final TradePayStatus tradePayStatus;


    public final Long tableId;


    public final Long startTimeMillis;


    public final int numberOfMeals;

    public final DinnertableStatus dishStatus;

    public final TradeType tradeType;

    public BigDecimal tradeSaleAmount;
    public YesOrNo printPreCash = YesOrNo.NO;    public List<AsyncHttpRecord> httpRecord;
    public List<AddItemVo> addItemVoList;

    public TradeTableInfo(Trade trade, TradeTable tradeTable,
                          DinnertableStatus dishStatus, PrintOperation preCashPrintOperation, List<AddItemVo> addItemVoList) {
        this.id = tradeTable.getId();
        this.uuid = trade.getUuid();
        this.serverUpdateTime = trade.getServerUpdateTime();
        this.startTimeMillis = trade.getServerCreateTime();


        this.tableId = tradeTable.getTableId();
        this.numberOfMeals = trade.getTradePeopleCount() != null ? trade.getTradePeopleCount() : 0;
        this.tradeUuid = trade.getUuid();
        this.tradeId = trade.getId();
        this.tradeServerUpdateTime = trade.getServerUpdateTime();
        this.tradeStatus = trade.getTradeStatus();
        this.serialNumber = trade.getSerialNumber();
        this.dishStatus = dishStatus;
        tradeSaleAmount = trade.getTradeAmount();
        tradeClientUpdateTime = trade.getClientUpdateTime();
        printPreCash = preCashPrintOperation == null ? YesOrNo.NO : YesOrNo.YES;
        this.addItemVoList = addItemVoList;
        this.tradePayStatus = trade.getTradePayStatus();
        this.businessType = trade.getBusinessType();
        this.tradeType = trade.getTradeType();
    }

    public int getNumberOfMeals() {
        return numberOfMeals;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TradeTableInfo other = (TradeTableInfo) obj;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }

    @Override
    public int compareTo(TradeTableInfo another) {
        int v = startTimeMillis.compareTo(another.startTimeMillis);
        if (v == 0) {
            v = serialNumber.compareTo(another.serialNumber);
        }
        if (v == 0) {
            v = uuid.compareTo(another.uuid);
        }
        return v;
    }
}
