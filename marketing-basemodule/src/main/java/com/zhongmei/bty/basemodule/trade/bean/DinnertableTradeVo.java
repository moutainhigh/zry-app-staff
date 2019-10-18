package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.shoppingcart.utils.DinnerUnionShopcartUtil;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShoppingCartTool;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.bty.basemodule.trade.manager.BuffetOutTimeManager;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeType;

import java.util.ArrayList;
import java.util.List;


public class DinnertableTradeVo {


    private final IDinnertableTrade dinnertableTrade;

    private final DinnertableTradeInfo info;

    private DinnertableTradeInfo unionMainTradeInfo;

    private IDinnertable tableModel;

    public DinnertableTradeVo(IDinnertableTrade dinnertableTrade, DinnertableTradeInfo info) {
        this.dinnertableTrade = dinnertableTrade;
        this.info = info;
    }

    public IDinnertableTrade getDinnertableTrade() {
        return dinnertableTrade;
    }

    public DinnertableTradeInfo getInfo() {
        return info;
    }

    public IDinnertable getDinnertable() {
        return dinnertableTrade.getDinnertable();
    }

    public String getUuid() {
        return dinnertableTrade.getUuid();
    }

    public String getTradeUuid() {
        return dinnertableTrade.getTradeUuid();
    }

    public Long getTradeId() {
        return dinnertableTrade.getTradeId();
    }

    public Long getTableId() {
        return dinnertableTrade.getDinnertable().getId();
    }

    public Long getZoneId() {
        if (tableModel != null) {
            return tableModel.getZone().getId();
        }
        return -1L;
    }

    public String getSn() {
        return dinnertableTrade.getSn();
    }

    public int getSpendTime() {
        return dinnertableTrade.getSpendTime();
    }

    public int getNumberOfMeals() {
        return dinnertableTrade.getNumberOfMeals();
    }

    public DinnertableStatus getStatus() {
        return dinnertableTrade.getStatus();
    }

    public TradeVo getTradeVo() {
        if (info == null) {
            return null;
        }
        return info.getTradeVo();
    }

    public List<IShopcartItem> getItems() {
        List<IShopcartItem> shopcartItemList = new ArrayList<>();
        if (info == null) {
            return null;
        }
                if (unionMainTradeInfo != null && info.getTradeVo().isUnionSubTrade() && !info.getTradeVo().isBuffet()) {
            DinnerUnionShopcartUtil.initSubTradeBatchItem(unionMainTradeInfo, info, shopcartItemList);
        }
        shopcartItemList.addAll(info.getItems());
                if (info.getTradeVo().isBuffetUnionMainTrade()) {
            DinnerUnionShopcartUtil.initBuffetMainTradeSubItems(info, shopcartItemList);
        } else if (info.getTradeVo().isUnionMainTrade()) {
            DinnerUnionShopcartUtil.initMainTradeSubItems(info, shopcartItemList);
        }
                if (info.getTradeVo().isSellTrade() || info.getTradeVo().isUnionSubTrade() || info.getTradeVo().isUnionMainTrade()) {
            MathShoppingCartTool.mathTotalPrice(shopcartItemList, info.getTradeVo());
            dinnertableTrade.setTradeAmount(info.getTradeVo().getTrade().getTradeAmount());
        }
        return shopcartItemList;
    }


    public boolean isNeedFinishTrade() {
        TradeVo tradeVo = getTradeVo();
        boolean paidOutTimeFee = BuffetOutTimeManager.calculateOutTimeFee(tradeVo).compareTo(BuffetOutTimeManager.getPaidOutTimeFee(tradeVo)) <= 0;
        if (tradeVo != null && tradeVo.getTrade().getTradePayStatus() == TradePayStatus.PREPAID && paidOutTimeFee) {
            return true;
        }
        return false;
    }


    public boolean isNeedReturnDeposit() {
        TradeVo tradeVo = getTradeVo();
        if (tradeVo != null && tradeVo.getTradeDeposit() != null) {
            return true;
        }
        return false;
    }

    public IDinnertable getTableModel() {
        return tableModel;
    }

    public void setTableModel(IDinnertable tableModel) {
        this.tableModel = tableModel;
    }

    public DinnertableTradeInfo getUnionMainTradeInfo() {
        return unionMainTradeInfo;
    }

    public void setUnionMainTradeInfo(DinnertableTradeInfo unionMainTradeInfo) {
        this.unionMainTradeInfo = unionMainTradeInfo;
    }
}
