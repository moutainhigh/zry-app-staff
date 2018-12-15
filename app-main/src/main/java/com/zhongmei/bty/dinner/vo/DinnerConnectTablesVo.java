package com.zhongmei.bty.dinner.vo;

import com.zhongmei.bty.basemodule.trade.entity.TradeMainSubRelation;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.commonmodule.database.entity.TradeEarnestMoney;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.List;

/**
 * 正餐桌台批量操作的桌台Vo
 * Created by demo on 2018/12/15
 */

public class DinnerConnectTablesVo {

    /**
     * 桌台信息
     */
    public Tables tables;

    /**
     * 区域名字
     */
    public String areaName;

    /**
     * 是否选中
     */
    public boolean isSelected;

    /**
     * 是否联台
     */
    public boolean isConnected;

    /**
     * 联台信息
     */
    public List<TradeMainSubRelation> tradeMainSubRelationList;

    public List<TradeTable> tradeTableList;

    public List<Trade> tradeList;

    public List<TradeEarnestMoney> tradeEarnestMoneyList;

    public boolean enableUnion() {
        //空桌台
        if (Utils.isEmpty(this.tradeList) || this.tables.getTableStatus() == TableStatus.EMPTY) {
            return true;
        } else {//占桌
            if (this.tradeList.size() == 1 && Utils.isEmpty(tradeEarnestMoneyList)) {//一桌一单情况
                Trade trade = tradeList.get(0);
                if (trade != null && isCanUnionTrade(trade)) {
                    return true;
                } else {
                    return false;
                }
            } else {//其它一桌多单情况
                return false;
            }
        }
    }

    public boolean isCanUnionTrade(Trade trade) {
        return trade.getBusinessType() == BusinessType.DINNER && trade.getTradeType() == TradeType.SELL
                && trade.getTradeStatus() == TradeStatus.CONFIRMED && trade.getTradePayStatus() == TradePayStatus.UNPAID;
    }

    public Long getMainTradeId() {
        if (tradeMainSubRelationList != null && tradeMainSubRelationList.size() > 0) {
            return tradeMainSubRelationList.get(0).getMainTradeId();
        }
        return null;
    }

    public Trade getSubTrade() {
        Trade subTrade = null;
        if (tradeList != null && tradeList.size() > 0) {
            for (Trade t : tradeList) {
                if (t.getTradeType() == TradeType.UNOIN_TABLE_SUB) {
                    subTrade = t;
                    break;
                }
            }
        }
        return subTrade;
    }
}
