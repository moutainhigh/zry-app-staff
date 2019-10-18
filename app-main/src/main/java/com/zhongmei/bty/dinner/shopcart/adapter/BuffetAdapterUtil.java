package com.zhongmei.bty.dinner.shopcart.adapter;

import android.content.Context;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.enums.ExtraItemType;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.bty.basemodule.trade.bean.MealShellVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



public class BuffetAdapterUtil {

    private static final int comboVoGroup = 0x01;
    private static final int singleGroup = 0x02;
    private static final int otherGroup = 0x03;

    public static Map<Integer, List<IShopcartItem>> buildBuffetShopcartData(Context context, TradeVo tradeVo, List<IShopcartItem> dataList, ArrayList<DishDataItem> data, SuperShopCartAdapter adapter) {
        MealShellVo mealShellVo = tradeVo.getMealShellVo();

        if (mealShellVo != null && Utils.isNotEmpty(tradeVo.getTradeBuffetPeoples())) {
            for (TradeBuffetPeople tradeBuffetPeople : tradeVo.getTradeBuffetPeoples()) {
                if (tradeBuffetPeople.getPeopleCount().compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }

                DishDataItem dishDataItem = new DishDataItem(ItemType.BUFFET_TRADE_PEOPLE);
                dishDataItem.setName(mealShellVo.getDishMenuVo().getSkuName() + "-" + tradeBuffetPeople.getCarteNormsName());
                dishDataItem.setCount(tradeBuffetPeople.getPeopleCount().intValue());
                dishDataItem.setValue(tradeBuffetPeople.getCartePrice().multiply(tradeBuffetPeople.getPeopleCount()).doubleValue());

                data.add(dishDataItem);
            }
        } else if (mealShellVo != null && mealShellVo.getDishMenuVo() != null) {
                        DishDataItem dishDataItem = new DishDataItem(ItemType.BUFFET_TRADE_PEOPLE);
            dishDataItem.setName(mealShellVo.getDishMenuVo().getSkuName());
            data.add(dishDataItem);
        }


                Map<Integer, List<IShopcartItem>> dishGroup = new HashMap<Integer, List<IShopcartItem>>();

                Map<Long, List<IShopcartItem>> tableItemMap = new LinkedHashMap<>();

        if (dataList != null && dataList.size() > 0) {
                        for (int i = 0; i < dataList.size(); i++) {

                IShopcartItem shopCartItem = dataList.get(i);

                if (shopCartItem.getStatusFlag() == StatusFlag.INVALID
                        && (shopCartItem.getInvalidType() != InvalidType.SPLIT)
                        && (shopCartItem.getInvalidType() != InvalidType.RETURN_QTY)
                        && (shopCartItem.getInvalidType() != InvalidType.MODIFY_DISH)) {
                    continue;
                }

                                adapter.sumAllDishCount(shopCartItem);

                if (shopCartItem.isGroupDish()) {
                                        if (!dishGroup.containsKey(comboVoGroup)) {
                        dishGroup.put(comboVoGroup, new ArrayList<IShopcartItem>());
                    }
                    dishGroup.get(comboVoGroup).add(shopCartItem);
                    continue;
                }

                switch (shopCartItem.getType()) {
                    case SINGLE:
                    case COMBO:
                        if (!dishGroup.containsKey(singleGroup)) {
                            dishGroup.put(singleGroup, new ArrayList<IShopcartItem>());
                        }
                        dishGroup.get(singleGroup).add(shopCartItem);
                                                List singleDeskList = tableItemMap.get(shopCartItem.getTradeTableId());
                        if (singleDeskList == null) {
                            singleDeskList = new ArrayList();
                            tableItemMap.put(shopCartItem.getTradeTableId(), singleDeskList);
                        }
                        singleDeskList.add(shopCartItem);
                        break;
                    default:
                        if (!dishGroup.containsKey(otherGroup)) {
                            dishGroup.put(otherGroup, new ArrayList<IShopcartItem>());
                        }
                        dishGroup.get(otherGroup).add(shopCartItem);
                        break;
                }

            }
        }

        if (!dishGroup.containsKey(otherGroup) && tradeVo.getTradeDeposit() != null) {
            dishGroup.put(otherGroup, new ArrayList<IShopcartItem>());
        }

        if (dishGroup.containsKey(comboVoGroup)) {
            DishDataItem item = new DishDataItem(ItemType.TITLE_ITEM);
            item.setEnabled(false);
            item.setName(context.getResources().getString(R.string.buffet_group_combo));
            data.add(item);
            adapter.createItems(dishGroup.get(comboVoGroup), data);
        }

        if (dishGroup.containsKey(singleGroup)) {
            DishDataItem item = new DishDataItem(ItemType.TITLE_ITEM);
            item.setEnabled(false);
            item.setName(context.getResources().getString(R.string.buffet_group_single));
            data.add(item);
            if (tableItemMap.size() > 1) {
                createDeskDataItem(tableItemMap, tradeVo.getSubTableMap(), data, adapter);
            } else {
                adapter.createItems(dishGroup.get(singleGroup), data);
            }
        }

        if (dishGroup.containsKey(otherGroup)) {
            DishDataItem item = new DishDataItem(ItemType.TITLE_ITEM);
            item.setEnabled(false);
            item.setName(context.getResources().getString(R.string.buffet_group_other));
            data.add(item);
            initOtherDataItem(tradeVo, data, context);
            adapter.createItems(dishGroup.get(otherGroup), data);
        }
        return dishGroup;
    }


    private static void createDeskDataItem(Map<Long, List<IShopcartItem>> tableItemMap, Map<Long, TradeTable> tradeTableMap, ArrayList<DishDataItem> data, SuperShopCartAdapter adapter) {
        Iterator<Long> iterator = tableItemMap.keySet().iterator();
        while (iterator.hasNext()) {
            Long tradeTableId = iterator.next();
            List<IShopcartItem> shopcartItemList = tableItemMap.get(tradeTableId);
            if (Utils.isNotEmpty(shopcartItemList)) {
                DishDataItem sitem = new DishDataItem(ItemType.TITLE_ITEM);
                if (tradeTableMap != null) {
                    TradeTable tradeTable = tradeTableMap.get(tradeTableId);
                    if (tradeTable != null) {
                        sitem.setName(tradeTable.getTableName());
                        data.add(sitem);
                    }
                }
                adapter.createItems(shopcartItemList, data);
            }
        }
    }

    private static void initOtherDataItem(TradeVo tradeVo, ArrayList<DishDataItem> data, Context mContext) {
        if (tradeVo.getTradeDeposit() != null && tradeVo.getTradeDeposit().isValid()) {
            DishDataItem item = new DishDataItem(ItemType.BUFFET_EXTRA_DEPOSIT);
            item.setName(mContext.getString(R.string.buffet_deposit));
            if (tradeVo.getTradeDepositPaymentItem() != null) {
                item.setStandText(tradeVo.getTradeDepositPaymentItem().getPayModeName() + mContext.getString(R.string.record_pay));
                item.setPaid(true);
            } else {
                item.setStandText(mContext.getString(R.string.buffet_deposit_property));
            }

            item.setExtraType(ExtraItemType.DEPOSIT);
            item.setValue(tradeVo.getTradeDeposit().getDepositPay().doubleValue());
            data.add(item);
        }
    }
}
