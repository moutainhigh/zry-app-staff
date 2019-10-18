package com.zhongmei.bty.dinner.shopcart.adapter;

import android.content.Context;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.enums.ExtraItemType;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.basemodule.trade.bean.MealShellVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BuffetShopCartAdapter extends DinnerShopCartAdapter {
    private Context mContext;
    private Map<Integer, List<IShopcartItem>> dishGroup = null;
    private final int comboVoGroup = 0x01;
    private final int singleGroup = 0x02;
    private final int otherGroup = 0x03;

    protected BigDecimal shellActumalAmount = BigDecimal.ZERO;


    public BuffetShopCartAdapter(Context context) {
        super(context);
        this.mContext = context;
    }


    public void updateData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid) {
        initCommonData(tradeVo);
        updateGroupData(dataList, tradeVo, false);

        initialDishCheckStatus();        updateTrade(tradeVo, isShowInvalid);        initialRelateDishInfo();    }



    public BigDecimal getShellActumalAmount() {
        return shellActumalAmount;
    }


    public void updateGroupData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid) {
        this.data.clear();        sortByTime(dataList);
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


        this.mAllDishCount = BigDecimal.ZERO;
                dishGroup = new HashMap<Integer, List<IShopcartItem>>();
        if (dataList != null && dataList.size() > 0) {
                        for (int i = 0; i < dataList.size(); i++) {

                IShopcartItem shopCartItem = dataList.get(i);

                if (shopCartItem.getStatusFlag() == StatusFlag.INVALID
                        && (shopCartItem.getInvalidType() != InvalidType.SPLIT)
                        && (shopCartItem.getInvalidType() != InvalidType.RETURN_QTY)
                        && (shopCartItem.getInvalidType() != InvalidType.MODIFY_DISH)) {
                    continue;
                }

                                sumAllDishCount(shopCartItem);

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
            createItems(dishGroup.get(comboVoGroup), data);
        }

        if (dishGroup.containsKey(singleGroup)) {
            DishDataItem item = new DishDataItem(ItemType.TITLE_ITEM);
            item.setEnabled(false);
            item.setName(context.getResources().getString(R.string.buffet_group_single));
            data.add(item);
            createItems(dishGroup.get(singleGroup), data);
        }

        if (dishGroup.containsKey(otherGroup)) {
            DishDataItem item = new DishDataItem(ItemType.TITLE_ITEM);
            item.setEnabled(false);
            item.setName(context.getResources().getString(R.string.buffet_group_other));
            data.add(item);
            initOtherDataItem(tradeVo, data);
            createItems(dishGroup.get(otherGroup), data);
        }
    }


    private void initOtherDataItem(TradeVo tradeVo, ArrayList<DishDataItem> data) {
        if (tradeVo.getTradeDeposit() != null && tradeVo.getTradeDeposit().isValid()) {
            DishDataItem dishDataItem = new DishDataItem(ItemType.BUFFET_EXTRA_DEPOSIT);
            dishDataItem.setName(mContext.getString(R.string.buffet_deposit));
            if (tradeVo.getTradeDepositPaymentItem() != null)
                dishDataItem.setStandText(tradeVo.getTradeDepositPaymentItem().getPayModeName() + mContext.getString(R.string.record_pay));
            else
                dishDataItem.setStandText(mContext.getString(R.string.buffet_deposit_property));
            dishDataItem.setExtraType(ExtraItemType.DEPOSIT);
            dishDataItem.setValue(tradeVo.getTradeDeposit().getDepositPay().doubleValue());
            data.add(dishDataItem);
        }
    }


    public Map<Integer, List<IShopcartItem>> getGroup() {
        return dishGroup;
    }

    public void sortByTime(List<IShopcartItem> listShopCartItems) {
        if (listShopCartItems == null || listShopCartItems.size() < 2) {
            return;
        }

        Collections.sort(listShopCartItems, new Comparator<IShopcartItem>() {
            @Override
            public int compare(IShopcartItem item1, IShopcartItem item2) {
                BigDecimal item1Time = BigDecimal.valueOf(item1.getClientCreateTime());
                BigDecimal item2Time = BigDecimal.valueOf(item2.getClientCreateTime());
                return item1Time.compareTo(item2Time);
            }
        });
    }
}
