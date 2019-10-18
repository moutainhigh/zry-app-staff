package com.zhongmei.bty.dinner.util;

import android.text.TextUtils;

import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.enums.ActivityRuleEffective;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.trade.bean.MealShellVo;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.snack.offline.Snack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class DinnerOrderCenterDetailDataBuildTool {
    private static String NO_HAVE_ACTIVITY_KEY = "no_have_activity_key";

    public List<TradeDishDataItem> buildDishDataList(TradeVo tradeVo, boolean isquit) {
        List<TradeItemVo> tradeItemVos = null;
        if (isquit) {
            tradeItemVos = getTradeItemListByInvalidType(tradeVo.getTradeItemList(), InvalidType.RETURN_QTY);
        } else {
            tradeItemVos = getValidTradeItemList(tradeVo.getTradeItemList());        }
        if (tradeItemVos == null) {
            return null;
        }

        boolean hasAllOrderDiscount = tradeVo.getTradePrivilege() != null;


        List<TradeItemVo> itemVos = new LinkedList<TradeItemVo>();
                Map<String, List<TradeItemVo>> setmealFinder = new HashMap<String, List<TradeItemVo>>();
        Map<String, List<TradeItemVo>> extraFinder = new HashMap<String, List<TradeItemVo>>();
        for (TradeItemVo itemVo : tradeItemVos) {
            TradeItem tradeItem = itemVo.getTradeItem();
            switch (tradeItem.getType()) {
                case SINGLE:
                    if (TextUtils.isEmpty(tradeItem.getParentUuid()) || (tradeItem.getParentUuid() != null && tradeVo.getMealShellVo() != null && tradeItem.getParentUuid().equals(tradeVo.getMealShellVo().getUuid()))) {
                                                itemVos.add(itemVo);
                    } else {
                                                List<TradeItemVo> list = setmealFinder.get(tradeItem.getParentUuid());
                        if (list == null) {
                            list = new ArrayList<TradeItemVo>();
                            setmealFinder.put(tradeItem.getParentUuid(), list);
                        }
                        list.add(itemVo);
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
                case BUFFET_COMBO_SHELL:                    break;

                default:
                    break;
            }
        }

        MealShellVo mealShellVo = tradeVo.getMealShellVo();

        if (!isquit && mealShellVo != null && Utils.isNotEmpty(tradeVo.getTradeBuffetPeoples())) {
            for (TradeBuffetPeople tradeBuffetPeople : tradeVo.getTradeBuffetPeoples()) {

                if (tradeBuffetPeople.getPeopleCount().compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }

                TradeItemVo tradeItemVo = new TradeItemVo();
                TradeItem TradeItem = new TradeItem();
                tradeItemVo.setTradeItem(TradeItem);
                TradeItem.setId(-1l);
                TradeItem.setAmount(tradeBuffetPeople.getCartePrice().multiply(tradeBuffetPeople.getPeopleCount()));
                TradeItem.setActualAmount(TradeItem.getAmount());
                TradeItem.setDishName(mealShellVo.getSkuName() + "-" + tradeBuffetPeople.getCarteNormsName());
                TradeItem.setPrice(tradeBuffetPeople.getCartePrice());
                TradeItem.setQuantity(tradeBuffetPeople.getPeopleCount());
                TradeItem.setType(DishType.BUFFET_COMBO_SHELL);
                TradeItem.setStatusFlag(StatusFlag.VALID);

                itemVos.add(tradeItemVo);
            }
        }


                List<TradePlanActivity> tradePlanActivityList = tradeVo.getTradePlanActivityList();        List<TradeItemPlanActivity> tradeItemPlanActivityList = tradeVo.getTradeItemPlanActivityList();        if (tradePlanActivityList == null) {
            tradePlanActivityList = Collections.emptyList();
        }
        if (tradeItemPlanActivityList == null) {
            tradeItemPlanActivityList = Collections.emptyList();
        }
        Map<String, TradePlanActivity> tradePlanActivityMap = new HashMap<String, TradePlanActivity>();        for (TradePlanActivity tradePlanActivity : tradePlanActivityList) {
            if (tradePlanActivity != null && tradePlanActivity.getStatusFlag() == StatusFlag.VALID
                    && tradePlanActivity.getRuleEffective() == ActivityRuleEffective.VALID) {
                tradePlanActivityMap.put(tradePlanActivity.getUuid(), tradePlanActivity);            }
        }
        Map<String, TradeItemPlanActivity> tradeItemPlanActivityMap = new HashMap<String, TradeItemPlanActivity>();        for (TradeItemPlanActivity tradeItemPlanActivity : tradeItemPlanActivityList) {
            String key = Snack.isOfflineTrade(tradeVo) ? tradeItemPlanActivity.getTradeItemUuid() : Long.toString(tradeItemPlanActivity.getTradeItemId());            tradeItemPlanActivityMap.put(key, tradeItemPlanActivity);
        }

        Map<String, List<TradeItemVo>> tradItemVoMap = splitTradeDishDataItemList(tradePlanActivityMap, tradeItemPlanActivityMap, itemVos, tradeVo);

        List<TradeDishDataItem> tradeDishDataItemList = new ArrayList<TradeDishDataItem>();

        for (TradePlanActivity tradePlanActivity : tradePlanActivityList) {
            List<TradeItemVo> tradeItemVoList = tradItemVoMap.get(tradePlanActivity.getUuid());
            if (Utils.isEmpty(tradeItemVoList)) {
                continue;
            }
            List<TradeDishDataItem> activitytradeDishDataItemList = buildTradeDishDataItemList(tradeItemVoList, tradePlanActivity, hasAllOrderDiscount, setmealFinder, extraFinder, isquit);
            tradeDishDataItemList.addAll(activitytradeDishDataItemList);
            if (tradePlanActivity != null && tradePlanActivity.getStatusFlag() == StatusFlag.VALID
                    && tradePlanActivity.getRuleEffective() == ActivityRuleEffective.VALID) {
                tradeDishDataItemList.add(buildTradeDishDataItemPlanActivity(tradePlanActivity));
            }
        }

        List<TradeDishDataItem> nohavetradeDishDataItemList = buildTradeDishDataItemList(tradItemVoMap.get(NO_HAVE_ACTIVITY_KEY), null, hasAllOrderDiscount, setmealFinder, extraFinder, isquit);
        tradeDishDataItemList.addAll(nohavetradeDishDataItemList);


        return tradeDishDataItemList;
    }


    private Map<String, List<TradeItemVo>> splitTradeDishDataItemList(Map<String, TradePlanActivity> tradePlanActivityMap, Map<String, TradeItemPlanActivity> tradeItemPlanActivityMap, List<TradeItemVo> tradeItemVos, TradeVo tradeVo) {


        Map<String, List<TradeItemVo>> tradeItemListMap = new HashMap<String, List<TradeItemVo>>();        tradeItemListMap.put(NO_HAVE_ACTIVITY_KEY, new ArrayList<TradeItemVo>());

        for (String key : tradePlanActivityMap.keySet()) {
            tradeItemListMap.put(key, new ArrayList<TradeItemVo>());        }


        for (TradeItemVo tradeItemVo : tradeItemVos) {
            TradeItem tradeItem = tradeItemVo.getTradeItem();
            if (tradeItem == null)
                continue;
            String key = Snack.isOfflineTrade(tradeVo) ? tradeItem.getUuid() : Long.toString(tradeItem.getId());
            TradeItemPlanActivity tradeItemPlanActivity = tradeItemPlanActivityMap.get(key);
            if (tradeItemPlanActivity != null) {                TradePlanActivity tradePlanActivity = tradePlanActivityMap.get(tradeItemPlanActivity.getRelUuid());                if (tradePlanActivity != null && tradePlanActivity.getStatusFlag() == StatusFlag.VALID
                        && tradePlanActivity.getRuleEffective() == ActivityRuleEffective.VALID) {
                    List<TradeItemVo> tradeItemVoList = tradeItemListMap.get(tradePlanActivity.getUuid());
                    tradeItemVoList.add(tradeItemVo);
                    continue;
                }
            }
            List<TradeItemVo> tradeItemVoList = tradeItemListMap.get(NO_HAVE_ACTIVITY_KEY);
            tradeItemVoList.add(tradeItemVo);
        }
        return tradeItemListMap;
    }


    private TradeDishDataItem buildTradeDishDataItemPlanActivity(TradePlanActivity tradePlanActivity) {
        TradeDishDataItem item = new TradeDishDataItem(TradeDishDataItem.ITEM_TYPE_PLAN_ACTIVITY, null, tradePlanActivity, false);
        return item;
    }


    protected List<TradeDishDataItem> buildTradeDishDataItemList(List<TradeItemVo> itemVos, TradePlanActivity tradePlanActivity, boolean hasAllOrderDiscount, Map<String, List<TradeItemVo>> setmealFinder, Map<String, List<TradeItemVo>> extraFinder, boolean isquit) {
                List<TradeDishDataItem> resultList = new ArrayList<TradeDishDataItem>();
        Collections.reverse(itemVos);
        for (TradeItemVo tradeItemVo : itemVos) {
            if (tradeItemVo.getTradeItem().getType() == DishType.COMBO) {
                                resultList.add(new TradeDishDataItem(TradeDishDataItem.ITEM_TYPE_COMBO, tradeItemVo, tradePlanActivity,
                        hasAllOrderDiscount));
                                List<TradeItemVo> comboSetmealList = setmealFinder.get(tradeItemVo.getTradeItem().getUuid());
                if (comboSetmealList != null && !comboSetmealList.isEmpty()) {
                    for (TradeItemVo setmealTradeItemVo : comboSetmealList) {
                                                TradeDishDataItem tradeDishDataItem =
                                new TradeDishDataItem(TradeDishDataItem.ITEM_TYPE_COMBO_CHILD, setmealTradeItemVo, tradePlanActivity,
                                        hasAllOrderDiscount);
                        tradeDishDataItem.setParentTradeItemVo(tradeItemVo);
                        List<TradeItemVo> childExtraList = extraFinder.get(setmealTradeItemVo.getTradeItem().getUuid());
                        tradeDishDataItem.setExtraList(childExtraList);
                        resultList.add(tradeDishDataItem);
                                                String memo = setmealTradeItemVo.getTradeItem().getTradeMemo();
                        List<TradeItemProperty> memoList =
                                filterTradeItemProperty(setmealTradeItemVo, PropertyKind.MEMO);
                        if (!TextUtils.isEmpty(memo) || !memoList.isEmpty()) {
                            resultList.add(new TradeDishDataItem(TradeDishDataItem.ITEM_TYPE_COMBO_CHILD_MEMO,
                                    setmealTradeItemVo, tradePlanActivity, hasAllOrderDiscount));
                        }
                    }
                }
                                if (isquit && tradeItemVo.getRejectQtyReason() != null) {
                    resultList.add(new TradeDishDataItem(TradeDishDataItem.ITEM_TYPE_REJECT_REASON_TYPE, tradeItemVo, null, false));
                }
                                String memo = tradeItemVo.getTradeItem().getTradeMemo();
                List<TradeItemProperty> memoList = filterTradeItemProperty(tradeItemVo, PropertyKind.MEMO);
                if (tradeItemVo.getTradeItemPrivilege() != null || !TextUtils.isEmpty(memo) || !memoList.isEmpty()) {
                    resultList.add(new TradeDishDataItem(TradeDishDataItem.ITEM_TYPE_COMBO_MEMO_AND_DISCOUNT,
                            tradeItemVo, tradePlanActivity, hasAllOrderDiscount));                }
            } else {
                                TradeDishDataItem tradeDishDataItem =
                        new TradeDishDataItem(TradeDishDataItem.ITEM_TYPE_SINGLE, tradeItemVo, tradePlanActivity, hasAllOrderDiscount);
                List<TradeItemVo> singleExtraList = extraFinder.get(tradeItemVo.getTradeItem().getUuid());
                tradeDishDataItem.setExtraList(singleExtraList);
                resultList.add(tradeDishDataItem);
                                if (isquit && tradeItemVo.getRejectQtyReason() != null) {
                    resultList.add(new TradeDishDataItem(TradeDishDataItem.ITEM_TYPE_REJECT_REASON_TYPE, tradeItemVo, null, false));
                }
                                String memo = tradeItemVo.getTradeItem().getTradeMemo();
                List<TradeItemProperty> memoList = filterTradeItemProperty(tradeItemVo, PropertyKind.MEMO);
                if (tradeItemVo.getTradeItemPrivilege() != null || !TextUtils.isEmpty(memo) || !memoList.isEmpty() || tradeItemVo.getCouponPrivilegeVo() != null) {
                    resultList.add(new TradeDishDataItem(TradeDishDataItem.ITEM_TYPE_SINGLE_MEMO_AND_DISCOUNT,
                            tradeItemVo, tradePlanActivity, hasAllOrderDiscount));
                }
            }
        }

        return resultList;
    }


    public List<TradeItemProperty> filterTradeItemProperty(TradeItemVo tradeItemVo, PropertyKind propertyKind) {
        List<TradeItemProperty> tradeItemProperties = new ArrayList<TradeItemProperty>();

        List<TradeItemProperty> tradeItemPropertyList = tradeItemVo.getTradeItemPropertyList();
        if (tradeItemPropertyList != null) {
            for (TradeItemProperty tradeItemProperty : tradeItemPropertyList) {
                if (tradeItemProperty.getPropertyType() == propertyKind) {
                    tradeItemProperties.add(tradeItemProperty);
                }
            }
        }

        return tradeItemProperties;
    }


    protected List<TradeItemVo> getValidTradeItemList(List<TradeItemVo> tradeItemList) {
        if (Utils.isNotEmpty(tradeItemList)) {
            List<TradeItemVo> validTradeItemList = new ArrayList<TradeItemVo>();
            for (TradeItemVo tradeItemVo : tradeItemList) {
                TradeItem tradeItem = tradeItemVo.getTradeItem();
                if (tradeItem.getStatusFlag() == StatusFlag.VALID
                        && tradeItem.getQuantity().compareTo(BigDecimal.ZERO) != 0) {
                    validTradeItemList.add(tradeItemVo);
                }
            }

            return validTradeItemList;
        }

        return Collections.emptyList();
    }



    public static class TradeDishDataItem {
        public static final int ITEM_TYPE_SINGLE = 1;
        public static final int ITEM_TYPE_SINGLE_MEMO_AND_DISCOUNT = 2;
        public static final int ITEM_TYPE_COMBO = 3;
        public static final int ITEM_TYPE_COMBO_CHILD = 4;
        public static final int ITEM_TYPE_COMBO_CHILD_MEMO = 5;
        public static final int ITEM_TYPE_COMBO_MEMO_AND_DISCOUNT = 6;
        public static final int ITEM_TYPE_PLAN_ACTIVITY = 7;
        public static final int ITEM_TYPE_REJECT_REASON_TYPE = 8;
        public static final int ITEM_TYPE_REJECT_NAME_TIME_TYPE = 9;
        private int type;

        private TradeItemVo parentTradeItemVo;

        private TradeItemVo tradeItemVo;

        private boolean hasAllOrderDiscount;

        private List<TradeItemVo> extraList;

        private TradePlanActivity tradePlanActivity;

        public TradeDishDataItem(int type, TradeItemVo tradeItemVo, TradePlanActivity tradePlanActivity, boolean hasAllOrderDiscount) {
            this.type = type;
            this.tradeItemVo = tradeItemVo;
            this.hasAllOrderDiscount = hasAllOrderDiscount;
            this.tradePlanActivity = tradePlanActivity;
        }

        public int getType() {
            return type;
        }

        public TradeItemVo getTradeItemVo() {
            return tradeItemVo;
        }

        public boolean isHasAllOrderDiscount() {
            return hasAllOrderDiscount;
        }

        public void setHasAllOrderDiscount(boolean hasAllOrderDiscount) {
            this.hasAllOrderDiscount = hasAllOrderDiscount;
        }

        public List<TradeItemVo> getExtraList() {
            return extraList;
        }

        public void setExtraList(List<TradeItemVo> extraList) {
            this.extraList = extraList;
        }

        public TradeItemVo getParentTradeItemVo() {
            return parentTradeItemVo;
        }

        public void setParentTradeItemVo(TradeItemVo parentTradeItemVo) {
            this.parentTradeItemVo = parentTradeItemVo;
        }

        public TradePlanActivity getTradePlanActivity() {
            return tradePlanActivity;
        }

        public void setTradePlanActivity(TradePlanActivity tradePlanActivity) {
            this.tradePlanActivity = tradePlanActivity;
        }
    }

    public int getAdaterViewType(TradeDishDataItem dishDataItem) {
        int itemViewType = -1;
        switch (dishDataItem.getType()) {
            case TradeDishDataItem.ITEM_TYPE_SINGLE:
            case TradeDishDataItem.ITEM_TYPE_COMBO:
            case TradeDishDataItem.ITEM_TYPE_COMBO_CHILD:
                itemViewType = 0;
                break;
            case TradeDishDataItem.ITEM_TYPE_SINGLE_MEMO_AND_DISCOUNT:
            case TradeDishDataItem.ITEM_TYPE_COMBO_MEMO_AND_DISCOUNT:
            case TradeDishDataItem.ITEM_TYPE_COMBO_CHILD_MEMO:
                itemViewType = 1;
                break;
            case TradeDishDataItem.ITEM_TYPE_PLAN_ACTIVITY:
                itemViewType = 2;
                break;
            case TradeDishDataItem.ITEM_TYPE_REJECT_REASON_TYPE:
                itemViewType = 3;
                break;
            case TradeDishDataItem.ITEM_TYPE_REJECT_NAME_TIME_TYPE:
                itemViewType = 4;
                break;
            default:
                break;
        }
        return itemViewType;
    }


    public static List<TradeItemVo> getInvalidTradeItemList(List<TradeItemVo> tradeItemList, InvalidType invalidType) {
        return getTradeItemListByInvalidType(tradeItemList, invalidType);
    }

    public static List<TradeItemVo> getTradeItemListByInvalidType(List<TradeItemVo> tradeItemList, InvalidType invalidType) {
        List<TradeItemVo> invalidTradeItemList = new ArrayList<>();
        if (Utils.isNotEmpty(tradeItemList)) {
            for (TradeItemVo tradeItemVo : tradeItemList) {
                TradeItem tradeItem = tradeItemVo.getTradeItem();
                if (tradeItem.getQuantity().compareTo(BigDecimal.ZERO) != 0) {
                    if (tradeItem.getStatusFlag() == StatusFlag.INVALID && tradeItem.getInvalidType() == invalidType) {
                        invalidTradeItemList.add(tradeItemVo);
                    } else if (tradeItem.getStatusFlag() == StatusFlag.VALID) {
                        TradeItemVo itemVo = getTradeItemVoModifyQty(tradeItemList, tradeItem);
                        if (itemVo != null) {
                            invalidTradeItemList.add(itemVo);
                        }
                    }
                }
            }
        }
        return invalidTradeItemList;
    }


    private static TradeItemVo getTradeItemVoModifyQty(List<TradeItemVo> tradeItemList, TradeItem tradeItem) {
        if (!TextUtils.isEmpty(tradeItem.getRelateTradeItemUuid())) {
            for (TradeItemVo itemVo : tradeItemList) {
                if (itemVo.getTradeItem().getUuid().equals(tradeItem.getRelateTradeItemUuid())) {
                                        if (itemVo.getTradeItem().getInvalidType() == InvalidType.MODIFY_DISH) {
                        if (tradeItem.getQuantity().compareTo(itemVo.getTradeItem().getQuantity()) < 0) {
                            itemVo.setModifyQuantity(tradeItem.getQuantity());
                            return itemVo;
                        }

                    }
                }
            }
        }
        return null;
    }


    private static boolean hasTradeItemChildModifyQty(List<TradeItemVo> tradeItemList, TradeItem tradeItem) {
        if (!TextUtils.isEmpty(tradeItem.getRelateTradeItemUuid())) {
            for (TradeItemVo itemVo : tradeItemList) {
                if (tradeItem.getStatusFlag() == StatusFlag.VALID) {
                    if (itemVo.getTradeItem().getUuid().equals(tradeItem.getRelateTradeItemUuid())) {
                        return tradeItem.getQuantity().compareTo(itemVo.getTradeItem().getQuantity()) < 0;
                    }
                } else {
                    if (tradeItem.getUuid().equals(itemVo.getTradeItem().getRelateTradeItemUuid())) {
                        return tradeItem.getQuantity().compareTo(itemVo.getTradeItem().getQuantity()) > 0;
                    }
                }
            }
        }
        return false;
    }


    private static List<TradeItem> getTradeItemChild(List<TradeItemVo> tradeItemList, TradeItem tradeItem) {
        List<TradeItem> result = new ArrayList<>();
        for (TradeItemVo itemVo : tradeItemList) {
            if (tradeItem.getUuid().equals(itemVo.getTradeItem().getParentUuid())) {
                result.add(itemVo.getTradeItem());
            }
        }
        return result;
    }
}
