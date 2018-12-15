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

/**
 * Created by demo on 2018/12/15
 */

public class TradeTableInfo implements Comparable<TradeTableInfo> {
    /**
     * trade_table.id
     */
    public final Long id;

    /**
     * trade_table.uuid
     */
    public final String uuid;

    /**
     * trade_table.server_update_time
     */
    public final Long serverUpdateTime;

    /**
     * trade.uuid
     */
    public final String tradeUuid;

    /**
     * trade.id
     */
    public final Long tradeId;

    /**
     * trade.server_update_time
     */
    public final Long tradeServerUpdateTime;

    /**
     * trade.client_update_time
     */
    public final Long tradeClientUpdateTime;

    /**
     * trade.trade_status
     */
    public final TradeStatus tradeStatus;

    public final BusinessType businessType;

    /**
     * trade_extra.serial_number
     */
    public final String serialNumber;

    public final TradePayStatus tradePayStatus;

    /**
     * tables.id
     */
    public final Long tableId;

    /**
     * trade_talbe.client_create_time
     */
    public final Long startTimeMillis;

    /**
     * 单据在桌台的就餐人数
     */
    public final int numberOfMeals;

    public final DinnertableStatus dishStatus;

    public final TradeType tradeType;

    public BigDecimal tradeSaleAmount;//订单金额

    public YesOrNo printPreCash = YesOrNo.NO;//是否已经打印预结单
    public List<AsyncHttpRecord> httpRecord;//订单相关httpqing

    public List<AddItemVo> addItemVoList;//订单相关加菜数据


    public TradeTableInfo(Trade trade, TradeTable tradeTable,
                          DinnertableStatus dishStatus, PrintOperation preCashPrintOperation, List<AddItemVo> addItemVoList) {
        this.id = tradeTable.getId();
        this.uuid = tradeTable.getUuid();
        this.serverUpdateTime = tradeTable.verValue();
        this.startTimeMillis = tradeTable.getServerCreateTime();


        this.tableId = tradeTable.getTableId();
        this.numberOfMeals = tradeTable.getTablePeopleCount();
        this.tradeUuid = trade.getUuid();
        this.tradeId = trade.getId();
        this.tradeServerUpdateTime = trade.getServerUpdateTime();
        this.tradeStatus = trade.getTradeStatus();
        this.serialNumber = trade.getSerialNumber();
        this.dishStatus = dishStatus;
//			tradeSaleAmount=trade.getDishAmount();
        tradeSaleAmount = trade.getTradeAmount();
        tradeClientUpdateTime = trade.getClientUpdateTime();
        printPreCash = preCashPrintOperation == null ? YesOrNo.NO : YesOrNo.YES;
//			this.httpRecord=httpRecord;
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
