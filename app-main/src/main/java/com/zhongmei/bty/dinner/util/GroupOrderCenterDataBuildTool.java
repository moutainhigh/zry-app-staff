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

/**
 * 团餐菜品构建
 * Created by demo on 2018/12/15
 */

public class GroupOrderCenterDataBuildTool extends DinnerOrderCenterDetailDataBuildTool {


    public List<TradeDishDataItem> buildDishDataList(TradeVo tradeVo, boolean isquit) {
        List<TradeItemVo> tradeItemVos = null;
        if (isquit) {
            tradeItemVos = getInvalidTradeItemList(tradeVo.getTradeItemList(), InvalidType.RETURN_QTY);
        } else {
            tradeItemVos = getValidTradeItemList(tradeVo.getTradeItemList());//TradeItemVo获取有效的Traditem的Vo
        }
        if (tradeItemVos == null) {
            return null;
        }

        boolean hasAllOrderDiscount = tradeVo.getTradePrivilege() != null;
        TradeItem shellTradeItem = null;
        if (tradeVo.getMealShellVo() != null) {
            shellTradeItem = tradeVo.getMealShellVo().getTradeItem();
        }

        List<TradeItemVo> itemVos = new LinkedList<TradeItemVo>();
        // 先分出单品(含套餐外壳)、套餐明细、加料，存放套餐明细和加料的map的key为父条目的uuid
        Map<String, List<TradeItemVo>> setmealFinder = new HashMap<String, List<TradeItemVo>>();
        Map<String, List<TradeItemVo>> extraFinder = new HashMap<String, List<TradeItemVo>>();
        for (TradeItemVo itemVo : tradeItemVos) {
            TradeItem tradeItem = itemVo.getTradeItem();
            switch (tradeItem.getType()) {
                case SINGLE:
                    if (tradeItem.getParentUuid() != null && !isGroupItem(shellTradeItem, tradeItem.getParentUuid())) {
                        // 套餐明细
                        List<TradeItemVo> list = setmealFinder.get(tradeItem.getParentUuid());
                        if (list == null) {
                            list = new ArrayList<TradeItemVo>();
                            setmealFinder.put(tradeItem.getParentUuid(), list);
                        }
                        list.add(itemVo);
                    } else {
                        // 单品
                        itemVos.add(itemVo);
                    }
                    break;

                case COMBO: // 套餐外壳
                    itemVos.add(itemVo);
                    break;

                case EXTRA: // 加料
                    List<TradeItemVo> list = extraFinder.get(tradeItem.getParentUuid());
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


        List<TradeDishDataItem> tradeDishDataItemList = new ArrayList<TradeDishDataItem>();//生成需要返回的List<TradeDishDataItem>
        List<TradeDishDataItem> nohavetradeDishDataItemList = buildTradeDishDataItemList(itemVos, null, hasAllOrderDiscount, setmealFinder, extraFinder, isquit);
        //添加餐标外壳
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

    /**
     * 是否是团餐下的菜品
     *
     * @param shellTradeItem
     * @param parentUuid
     * @return
     */
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
