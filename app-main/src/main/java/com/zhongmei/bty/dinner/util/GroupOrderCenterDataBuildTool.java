package com.zhongmei.bty.dinner.util;

import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



public class GroupOrderCenterDataBuildTool extends DinnerOrderCenterDetailDataBuildTool {


    public List<TradeDishDataItem> buildDishDataList(TradeVo tradeVo, boolean isquit) {
        List<TradeItemVo> tradeItemVos = null;
        if (isquit) {
            tradeItemVos = getInvalidTradeItemList(tradeVo.getTradeItemList(), InvalidType.RETURN_QTY);
        } else {
            tradeItemVos = getValidTradeItemList(tradeVo.getTradeItemList());        }
        if (tradeItemVos == null) {
            return null;
        }

        boolean hasAllOrderDiscount = tradeVo.getTradePrivilege() != null;
        TradeItem shellTradeItem = null;
        if (tradeVo.getMealShellVo() != null) {
            shellTradeItem = tradeVo.getMealShellVo().getTradeItem();
        }

        List<TradeItemVo> itemVos = new LinkedList<TradeItemVo>();
                Map<String, List<TradeItemVo>> setmealFinder = new HashMap<String, List<TradeItemVo>>();
        Map<String, List<TradeItemVo>> extraFinder = new HashMap<String, List<TradeItemVo>>();
        for (TradeItemVo itemVo : tradeItemVos) {
            TradeItem tradeItem = itemVo.getTradeItem();
            switch (tradeItem.getType()) {
                case SINGLE:
                    if (tradeItem.getParentUuid() != null && !isGroupItem(shellTradeItem, tradeItem.getParentUuid())) {
                                                List<TradeItemVo> list = setmealFinder.get(tradeItem.getParentUuid());
                        if (list == null) {
                            list = new ArrayList<TradeItemVo>();
                            setmealFinder.put(tradeItem.getParentUuid(), list);
                        }
                        list.add(itemVo);
                    } else {
                                                itemVos.add(itemVo);
                    }
                    break;

                case COMBO:                     itemVos.add(itemVo);
                    break;

                case EXTRA:                     List<TradeItemVo> list = extraFinder.get(tradeItem.getParentUuid());
                    if (list == null) {
                        list = new ArrayList<TradeItemVo>();
                        extraFinder.put(tradeItem.getParentUuid(), list);
                    }
                    list.add(itemVo);
                    break;

                default:
                    break;
            }
        }


        List<TradeDishDataItem> tradeDishDataItemList = new ArrayList<TradeDishDataItem>();        List<TradeDishDataItem> nohavetradeDishDataItemList = buildTradeDishDataItemList(itemVos, null, hasAllOrderDiscount, setmealFinder, extraFinder, isquit);
                if (shellTradeItem != null) {
            TradeItemVo shellItemVo = new TradeItemVo();
            shellItemVo.setTradeItem(shellTradeItem);
            TradeDishDataItem tradeDishDataItem = new TradeDishDataItem(TradeDishDataItem.ITEM_TYPE_COMBO, shellItemVo, null,
                    hasAllOrderDiscount);
            tradeDishDataItemList.add(tradeDishDataItem);
        }
        tradeDishDataItemList.addAll(nohavetradeDishDataItemList);
        return tradeDishDataItemList;
    }


    private boolean isGroupItem(TradeItem shellTradeItem, String parentUuid) {
        if (shellTradeItem == null) {
            return false;
        }
        if (shellTradeItem.getUuid().equals(parentUuid)) {
            return true;
        }
        return false;
    }
}
