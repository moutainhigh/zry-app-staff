package com.zhongmei.bty.basemodule.shoppingcart.utils;

import android.util.Log;

import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyOrderProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlySetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItemBase;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRel;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRelExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.util.Beans;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class DinnerUnionShopcartUtil {

    private static final String TAG = "DinnerUnionShopcartUtil";


    public static void initMainTradeSubItems(DinnertableTradeInfo tradeInfo, List<IShopcartItem> shopcartItemLIst) {
        if (Utils.isEmpty(tradeInfo.getSubTradeInfoList())) {
            return;
        }
        Integer totalPeople = 0;
        for (DinnertableTradeInfo dinnertableTradeInfo : tradeInfo.getSubTradeInfoList()) {
            List<IShopcartItem> shopcartItemList = dinnertableTradeInfo.getItems();
            if (Utils.isNotEmpty(dinnertableTradeInfo.getTradeVo().getTradeTableList())) {
                for (TradeTable tradeTable : dinnertableTradeInfo.getTradeVo().getTradeTableList()) {
                    totalPeople = totalPeople + tradeTable.getTablePeopleCount();
                }
            }
            if (Utils.isEmpty(shopcartItemList)) {
                continue;
            }
            for (IShopcartItem iShopcartItem : shopcartItemList) {
                                if (iShopcartItem.getStatusFlag() == StatusFlag.INVALID) {
                    continue;
                }
                ReadonlyShopcartItem newShopcartItem = null;
                                if (iShopcartItem instanceof ReadonlyShopcartItem) {
                    newShopcartItem = (ReadonlyShopcartItem) iShopcartItem;
                    newShopcartItem = ShopcartItemUtils.copyReadonlyShopcartItem(newShopcartItem, true);
                    if (newShopcartItem == null) {
                        continue;
                    }
                    newShopcartItem.setShopcartItemType(ShopcartItemType.MAINSUB);
                }
                shopcartItemLIst.add(newShopcartItem);
            }
        }
        tradeInfo.getTradeVo().getTrade().setTradePeopleCount(totalPeople);
    }


    public static void initBuffetMainTradeSubItems(DinnertableTradeInfo tradeInfo, List<IShopcartItem> oldShopcartItemList) {
        if (Utils.isEmpty(tradeInfo.getSubTradeInfoList())) {
            return;
        }

        Map<String, IShopcartItem> oldShopcartItemMap = new HashMap<>();
        for (IShopcartItem shopcartItem : oldShopcartItemList) {
            oldShopcartItemMap.put(shopcartItem.getUuid(), shopcartItem);
        }
        for (DinnertableTradeInfo dinnertableTradeInfo : tradeInfo.getSubTradeInfoList()) {
            List<IShopcartItem> shopcartItemList = dinnertableTradeInfo.getItems();

            if (Utils.isEmpty(shopcartItemList)) {
                continue;
            }
            for (IShopcartItem iShopcartItem : shopcartItemList) {
                                if (iShopcartItem.getStatusFlag() == StatusFlag.INVALID || MathDecimal.isZero(iShopcartItem.getTotalQty())) {
                    continue;
                }
                iShopcartItem.setShopcartItemType(ShopcartItemType.MAINSUB);
                                if (!oldShopcartItemMap.containsKey(iShopcartItem.getUuid()))
                    oldShopcartItemList.add(iShopcartItem);
            }
        }
    }


    public static void initSubTradeBatchItem(DinnertableTradeInfo mainTradeInfo, DinnertableTradeInfo currentTradeInfo, List<IShopcartItem> shopcartItemList) {

        if (mainTradeInfo == null || Utils.isEmpty(mainTradeInfo.getItems()) || mainTradeInfo.getTradeVo() == null) {
            return;
        }

        Map<Long, TradeItemMainBatchRelExtra> batchRelExtraMap = new HashMap<>();
        initBatchExtraMap(batchRelExtraMap, mainTradeInfo.getTradeVo(), currentTradeInfo.getTradeVo().getTrade().getId());
                String currentTradeUuid = currentTradeInfo.getTradeVo().getTrade().getUuid();
        for (IShopcartItem iShopcartItem : mainTradeInfo.getItems()) {
            if (iShopcartItem.getShopcartItemType() != ShopcartItemType.MAINBATCH || iShopcartItem.getStatusFlag() == StatusFlag.INVALID) {
                continue;
            }
                        if (MathDecimal.isZero(iShopcartItem.getTotalQty())) {
                continue;
            }
            ReadonlyShopcartItem newShopcartItem = null;
            if (iShopcartItem instanceof ReadonlyShopcartItem) {
                ReadonlyShopcartItem oldShopcartItem = (ReadonlyShopcartItem) iShopcartItem;
                if (Utils.isEmpty(oldShopcartItem.getTradeItemMainBatchRelList())) {
                    continue;
                }
                Long tradeId = currentTradeInfo.getTradeVo().getTrade().getId();
                                TradeItemMainBatchRel relTradeItemMainBatchRel = getCurrentBatchRel(oldShopcartItem, tradeId);
                if (relTradeItemMainBatchRel == null) {
                    continue;
                }
                newShopcartItem = doVirtualShopcartItem(oldShopcartItem, relTradeItemMainBatchRel, batchRelExtraMap, tradeId, currentTradeUuid);
                List<TradeItemMainBatchRel> tradeItemMainBatchRelList = new ArrayList<TradeItemMainBatchRel>();
                tradeItemMainBatchRelList.add(relTradeItemMainBatchRel);
                newShopcartItem.setTradeItemMainBatchRelList(tradeItemMainBatchRelList);
                newShopcartItem.setMainShopcartItem(oldShopcartItem);
                newShopcartItem.setShopcartItemType(ShopcartItemType.SUBBATCH);
                ShopcartItemUtils.modifyReadonlyItemByQty(newShopcartItem, relTradeItemMainBatchRel.getTradeItemNum());
                shopcartItemList.add(newShopcartItem);
            }
        }
    }

    private static TradeItemMainBatchRel getCurrentBatchRel(ReadonlyShopcartItemBase oldShopcartItem, Long currentTradeId) {
        TradeItemMainBatchRel relTradeItemMainBatchRel = null;
        for (TradeItemMainBatchRel tradeItemMainBatchRel : oldShopcartItem.getTradeItemMainBatchRelList()) {
            if (tradeItemMainBatchRel.getStatusFlag() == StatusFlag.VALID && tradeItemMainBatchRel.getSubTradeId().compareTo(currentTradeId) == 0) {
                relTradeItemMainBatchRel = tradeItemMainBatchRel;
                break;
            }
        }
        return relTradeItemMainBatchRel;
    }

    private static void initBatchExtraMap(Map<Long, TradeItemMainBatchRelExtra> batchExtraMap, TradeVo tradeVo, Long currentTradeId) {
        if (Utils.isEmpty(tradeVo.getTradeItemMainBatchRelExtraList())) {
            return;
        }
        for (TradeItemMainBatchRelExtra tradeItemMainBatchRelExtra : tradeVo.getTradeItemMainBatchRelExtraList()) {
            if (tradeItemMainBatchRelExtra.getSubTradeId().compareTo(currentTradeId) != 0) {
                continue;
            }
            batchExtraMap.put(tradeItemMainBatchRelExtra.getMainId(), tradeItemMainBatchRelExtra);
        }
    }


    private static ReadonlyShopcartItem doVirtualShopcartItem(ReadonlyShopcartItem oldShopcartItem, TradeItemMainBatchRel tradeItemMainBatchRel, Map<Long, TradeItemMainBatchRelExtra> batchRelExtraMap, Long tradeId, String tradeUuid) {
        TradeItem newTradeItem = new TradeItem();
        ReadonlyShopcartItem newShopcartItem = null;
        try {
            Beans.copyProperties(oldShopcartItem.tradeItem, newTradeItem);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        BigDecimal newQty = doRelateItemKey(newTradeItem, tradeItemMainBatchRel, tradeUuid);

                newShopcartItem = new ReadonlyShopcartItem(newTradeItem, oldShopcartItem.getReturnQtyReason());
        if (newQty != null) {
            newShopcartItem.modifyQty(newQty);
        }
        doVirtualExtraItem(oldShopcartItem, newShopcartItem, tradeId, tradeUuid);
        List<ReadonlyOrderProperty> propertyList = doVirtualProperties(oldShopcartItem.getProperties(), batchRelExtraMap, newTradeItem);
        newShopcartItem.setProperties(propertyList);

        TradeItemExtra tradeItemExtra = doVirtualTradeItemExtras(oldShopcartItem.getTradeItemExtra(), batchRelExtraMap, newTradeItem);
        newShopcartItem.setTradeItemExtra(tradeItemExtra);

        List<TradeItemOperation> tradeItemOperationList = doVirtualItemOperations(oldShopcartItem.getTradeItemOperations(), batchRelExtraMap, newTradeItem.getUuid(), newTradeItem.getId());
        newShopcartItem.setTradeItemOperations(tradeItemOperationList);

        if (Utils.isNotEmpty(oldShopcartItem.getSetmealItems())) {
            newShopcartItem.setSetmealItems(new ArrayList<ReadonlySetmealShopcartItem>());
            for (ISetmealShopcartItem setmealShopcartItem : oldShopcartItem.getSetmealItems()) {
                                ReadonlySetmealShopcartItem setmealItem = ((ReadonlySetmealShopcartItem) setmealShopcartItem);
                TradeItem oldSetTradeItem = setmealItem.tradeItem;
                TradeItem newSetTradeItem = new TradeItem();
                try {
                    Beans.copyProperties(oldSetTradeItem, newSetTradeItem);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                TradeItemMainBatchRel relTradeItemMainBatchRel = getCurrentBatchRel(setmealItem, tradeId);
                if (relTradeItemMainBatchRel == null) {
                    continue;
                }
                BigDecimal newSetmealQty = doRelateItemKey(newSetTradeItem, relTradeItemMainBatchRel, tradeUuid);
                newSetTradeItem.modifyQty(newSetmealQty);
                ReadonlySetmealShopcartItem newSetShopcartItem = new ReadonlySetmealShopcartItem(newSetTradeItem, newShopcartItem);
                List<ReadonlyOrderProperty> setmelPropertyList = doVirtualProperties(setmealItem.getProperties(), batchRelExtraMap, newSetTradeItem);
                newSetShopcartItem.setProperties(setmelPropertyList);

                TradeItemExtra setmealItemExtra = doVirtualTradeItemExtras(setmealItem.getTradeItemExtra(), batchRelExtraMap, newSetTradeItem);
                newSetShopcartItem.setTradeItemExtra(setmealItemExtra);

                List<TradeItemOperation> setmealItemOperationList = doVirtualItemOperations(setmealItem.getTradeItemOperations(), batchRelExtraMap, newSetTradeItem.getUuid(), newSetTradeItem.getId());
                newSetShopcartItem.setTradeItemOperations(setmealItemOperationList);

                doVirtualExtraItem((ReadonlySetmealShopcartItem) setmealShopcartItem, newSetShopcartItem, tradeId, tradeUuid);
                newSetShopcartItem.setShopcartItemType(ShopcartItemType.SUBBATCH);
                newShopcartItem.getSetmealItems().add(newSetShopcartItem);
            }
        }

        return newShopcartItem;
    }


    private static void doVirtualExtraItem(ReadonlyShopcartItemBase oldShopcartItem, ReadonlyShopcartItemBase newShopcartItem, Long tradeId, String tradeUuid) {
        if (oldShopcartItem.getExtraItems() != null) {
            newShopcartItem.setExtraItems(new ArrayList<ReadonlyExtraShopcartItem>());
            for (ReadonlyExtraShopcartItem extraShopcartItem : oldShopcartItem.getExtraItems()) {
                TradeItem newExtraTradeItem = new TradeItem();
                try {
                    Beans.copyProperties(extraShopcartItem.tradeItem, newExtraTradeItem);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                TradeItemMainBatchRel relTradeItemMainBatchRel = getCurrentBatchRel(extraShopcartItem, tradeId);
                if (relTradeItemMainBatchRel == null) {
                    continue;
                }
                BigDecimal newQty = doRelateItemKey(newExtraTradeItem, relTradeItemMainBatchRel, tradeUuid);
                if (newQty != null)
                    newExtraTradeItem.modifyQty(newQty);
                ReadonlyExtraShopcartItem extraItem = new ReadonlyExtraShopcartItem(newExtraTradeItem, newShopcartItem);
                extraItem.setShopcartItemType(ShopcartItemType.SUBBATCH);
                newShopcartItem.getExtraItems().add(extraItem);
            }
        }
    }


    private static List<ReadonlyOrderProperty> doVirtualProperties(List<ReadonlyOrderProperty> orderPropertyList, Map<Long, TradeItemMainBatchRelExtra> batchRelExtraMap, TradeItem relateItem) {
        if (Utils.isEmpty(orderPropertyList)) {
            return null;
        }
        List<ReadonlyOrderProperty> newOrderPropertyList = new ArrayList<>();
        for (ReadonlyOrderProperty orderProperty : orderPropertyList) {
            TradeItemProperty newTradeItemProperty = new TradeItemProperty();
            try {
                Beans.copyProperties(orderProperty.tradeItemProperty, newTradeItemProperty);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            TradeItemMainBatchRelExtra tradeItemMainBatchRelExtra = batchRelExtraMap.get(orderProperty.tradeItemProperty.getId());
            if (tradeItemMainBatchRelExtra == null) {
                continue;
            }
            newTradeItemProperty.setId(tradeItemMainBatchRelExtra.getSubId());
            newTradeItemProperty.setUuid(tradeItemMainBatchRelExtra.getSubUuid());
            newTradeItemProperty.setTradeItemUuid(relateItem.getUuid());
            newTradeItemProperty.setTradeItemId(relateItem.getId());
            newTradeItemProperty.setBatchId(tradeItemMainBatchRelExtra.getMainId());
            ReadonlyOrderProperty readonlyOrderProperty = new ReadonlyOrderProperty(newTradeItemProperty);
            ShopcartItemUtils.modifyPropertyByQty(readonlyOrderProperty, tradeItemMainBatchRelExtra.getQuantity());
            newOrderPropertyList.add(readonlyOrderProperty);
        }
        return newOrderPropertyList;
    }

    private static TradeItemExtra doVirtualTradeItemExtras(TradeItemExtra tradeItemExtra, Map<Long, TradeItemMainBatchRelExtra> batchRelExtraMap, TradeItem relateItem) {
        if (tradeItemExtra == null) {
            return null;
        }
        TradeItemExtra newTradeItemExtra = new TradeItemExtra();
        try {
            Beans.copyProperties(tradeItemExtra, newTradeItemExtra);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        TradeItemMainBatchRelExtra tradeItemMainBatchRelExtra = batchRelExtraMap.get(tradeItemExtra.getId());
        if (tradeItemMainBatchRelExtra == null) {
            return null;
        }
        newTradeItemExtra.setId(tradeItemMainBatchRelExtra.getSubId());
        newTradeItemExtra.setUuid(tradeItemMainBatchRelExtra.getSubUuid());
        newTradeItemExtra.setTradeItemUuid(relateItem.getUuid());
        newTradeItemExtra.setTradeItemId(relateItem.getId());
        return newTradeItemExtra;
    }

    private static List<TradeItemOperation> doVirtualItemOperations(List<TradeItemOperation> operationList, Map<Long, TradeItemMainBatchRelExtra> batchRelExtraMap, String tradeItemUuid, Long tradeItemId) {
        if (Utils.isEmpty(operationList)) {
            return null;
        }
        List<TradeItemOperation> newOperationList = new ArrayList<>();
        for (TradeItemOperation tradeItemOperation : operationList) {
                        if (tradeItemOperation.getStatusFlag() == StatusFlag.INVALID) {
                continue;
            }
            TradeItemOperation newTradeItemOpeartion = new TradeItemOperation();
            try {
                Beans.copyProperties(tradeItemOperation, newTradeItemOpeartion);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            TradeItemMainBatchRelExtra tradeItemMainBatchRelExtra = batchRelExtraMap.get(tradeItemOperation.getId());
            if (tradeItemMainBatchRelExtra == null) {
                continue;
            }
            newTradeItemOpeartion.setId(tradeItemMainBatchRelExtra.getSubId());
            newTradeItemOpeartion.setTradeItemUuid(tradeItemUuid);
            newTradeItemOpeartion.setTradeItemId(tradeItemId);
            newOperationList.add(newTradeItemOpeartion);
        }
        return newOperationList;
    }

    private static BigDecimal doRelateItemKey(TradeItem newTradeItem, TradeItemMainBatchRel tradeItemMainBatchRel, String tradeUuid) {
        if (tradeItemMainBatchRel != null) {
            newTradeItem.setId(tradeItemMainBatchRel.getSubItemId());
            newTradeItem.setUuid(tradeItemMainBatchRel.getSubItemUuid());
            newTradeItem.setParentId(tradeItemMainBatchRel.getParentItemId());
            newTradeItem.setParentUuid(tradeItemMainBatchRel.getParentItemUuid());
            newTradeItem.setRelateTradeItemId(tradeItemMainBatchRel.getRelateItemId());
            newTradeItem.setRelateTradeItemUuid(tradeItemMainBatchRel.getRelateItemUuid());
            newTradeItem.setBatchId(tradeItemMainBatchRel.getMainItemId());
            newTradeItem.setTradeId(tradeItemMainBatchRel.getSubTradeId());
            newTradeItem.setTradeUuid(tradeUuid);
            newTradeItem.setTradeTableId(tradeItemMainBatchRel.getTradeTableId());
            newTradeItem.setTradeTableUuid(tradeItemMainBatchRel.getTradeTableUuid());
            return tradeItemMainBatchRel.getTradeItemNum();
        }
        return null;
    }
}
