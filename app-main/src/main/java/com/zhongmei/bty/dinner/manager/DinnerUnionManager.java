package com.zhongmei.bty.dinner.manager;

import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.inventory.message.InventoryChangeReq;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryUtils;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.orderdish.message.TradeItemResp;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShopcartItemTool;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRel;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.message.TradeUnionModifyMainWarpReq;
import com.zhongmei.bty.basemodule.trade.message.TradeUnionModifySubWarpReq;
import com.zhongmei.bty.basemodule.trade.message.TradeUnionRequest;
import com.zhongmei.bty.basemodule.trade.message.UnionTradeItemOperationReq;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.dinner.orderdish.manager.DinnerDishManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 联台请求工具类
 * Created by demo on 2018/12/15
 */

public class DinnerUnionManager {

    private static final String TAG = "DinnerUnionManager";

    /**
     * @param tradeVo 主单对应的tradevO
     *                联台主单改单
     */
    public static void modifyUnionMainTrade(TradeVo tradeVo, ResponseListener<TradeResp> listener, boolean isAsync, TradeOperates operates) {
        modifyUnionMainTrade(tradeVo, null, listener, isAsync, operates);
    }


    public static void modifyUnionMainTrade(TradeVo tradeVo, List<IShopcartItem> shopcartItemList, ResponseListener<TradeResp> listener, boolean isAsync, TradeOperates operates) {
        MathShopcartItemTool.mathMainShopcartItemsAmount(tradeVo, shopcartItemList);
        TradeUnionModifyMainWarpReq tradeUnionModifyMainReq = createUnionMainModifyReq(tradeVo, null, null);
        operates.modifyUnionMainTrade(tradeVo, tradeUnionModifyMainReq, listener, isAsync);
    }

    /**
     * 创建联台主单改单请求
     *
     * @param tradeVo
     * @param dinnertableTradeInfo
     * @return
     */
    public static TradeUnionModifyMainWarpReq createUnionMainModifyReq(TradeVo tradeVo, DinnertableTradeInfo dinnertableTradeInfo, List<String> selectUuids) {
        TradeUnionModifyMainWarpReq tradeUnionMainWrapReq = new TradeUnionModifyMainWarpReq();
        TradeUnionModifyMainWarpReq.TradeUnionModifyMainReq tradeUnionMainReq = new TradeUnionModifyMainWarpReq.TradeUnionModifyMainReq();
        tradeUnionMainWrapReq.setModifyRequest(tradeUnionMainReq);
        Trade trade = tradeVo.getTrade();
        TradeUnionRequest tradeUnionRequest = covertTradeToUnionRequest(trade);
        tradeUnionMainReq.setMainTrade(tradeUnionRequest);
        if (tradeVo.getTradeExtra() != null && tradeVo.getTradeExtra().isChanged())
            tradeUnionMainReq.setTradeExtra(tradeVo.getTradeExtra());
        tradeUnionMainReq.setTradeCustomers(tradeVo.getTradeCustomerList());
        tradeUnionMainReq.setTradePrivileges(tradeVo.getTradePrivileges());
        tradeUnionMainReq.setTradeReasonRels(tradeVo.getTradeReasonRelList());
        List<TradeUnionModifyMainWarpReq.TradeUnionTradeItemReq> tradeItemReqList = new ArrayList<>();
        List<TradeReasonRel> tradeReasonRelList = new ArrayList<>();
        buildModiyItemReq(tradeVo, tradeItemReqList, selectUuids, tradeReasonRelList);
        tradeUnionMainReq.setTradeItems(tradeItemReqList);
        if (tradeVo.getTradeUser() != null && tradeVo.getTradeUser().isChanged())
            tradeUnionMainReq.setTradeUser(tradeVo.getTradeUser());
        //设置联桌的子单id
        TradeUnionModifyMainWarpReq.TradeUnionModifyRelReq tradeUnionModifyIdReq = new TradeUnionModifyMainWarpReq.TradeUnionModifyRelReq();
        tradeUnionModifyIdReq.setAddItemSubTradeIds(tradeVo.getSubTradeIdList());
        tradeUnionMainReq.setTradeReasonRels(tradeReasonRelList);
        tradeUnionMainReq.setTradeRelRequest(tradeUnionModifyIdReq);
        InventoryUtils.setInventoryVoValue(tradeVo);
        InventoryChangeReq inventoryChangeReq = InventoryUtils.makeInventoryChangeReq(tradeVo.inventoryVo);
        tradeUnionMainWrapReq.setInventoryRequest(inventoryChangeReq);
        return tradeUnionMainWrapReq;
    }

    /**
     * 构建联台tradeItem请求关联关系
     *
     * @param tradeVo
     * @param tradeItemReqList
     */
    public static void buildModiyItemReq(TradeVo tradeVo, List<TradeUnionModifyMainWarpReq.TradeUnionTradeItemReq> tradeItemReqList, List<String> selectUuids, List<TradeReasonRel> tradeReasonRelList) {
        if (Utils.isEmpty(tradeVo.getTradeItemList())) {
            return;
        }
        Map<String, TradeItemVo> parentFinder = new LinkedHashMap<String, TradeItemVo>();
        List<TradeItemVo> setmealList = new ArrayList<TradeItemVo>();
        List<TradeItemVo> extraList = new ArrayList<TradeItemVo>();
        for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
            if (selectUuids != null && !selectUuids.contains(tradeItemVo.getTradeItem().getUuid())
                    && TextUtils.isEmpty(tradeItemVo.getTradeItem().getParentUuid())
                    && tradeItemVo.getTradeItem().getStatusFlag() != StatusFlag.INVALID)
                continue;
            //联台主单中的子菜，不上传,未改变的数据不上传
            if (tradeVo.getTrade().getId().compareTo(tradeItemVo.getTradeItem().getTradeId()) != 0 || !tradeItemVo.isChanged() || tradeItemVo.getShopcartItemType() == ShopcartItemType.MAINSUB) {
                continue;
            }
            TradeItem tradeItem = tradeItemVo.getTradeItem();
            if (tradeItem.getType() == DishType.EXTRA) {
                extraList.add(tradeItemVo);
            } else if (TextUtils.isEmpty(tradeItem.getParentUuid())) {
                //单菜或者套餐外壳
                parentFinder.put(tradeItem.getUuid(), tradeItemVo);
            } else {
                setmealList.add(tradeItemVo);
            }
        }
        Map<String, TradeUnionModifyMainWarpReq.TradeUnionTradeItemReq> singleFinder = new HashMap<String, TradeUnionModifyMainWarpReq.TradeUnionTradeItemReq>();

        for (TradeItemVo tradeItemVo : parentFinder.values()) {
            TradeItem tradeItem = tradeItemVo.getTradeItem();
            if (tradeItem == null)
                continue;

            TradeUnionModifyMainWarpReq.TradeUnionTradeItemReq item = convertToItemReq(tradeItemVo, tradeReasonRelList);
            singleFinder.put(tradeItem.getUuid(), item);
            tradeItemReqList.add(item);
        }
        // 套餐子菜
        Map<String, TradeUnionModifyMainWarpReq.TradeUnionTradeItemReq> setmealFinder = new HashMap<String, TradeUnionModifyMainWarpReq.TradeUnionTradeItemReq>();
        for (TradeItemVo tradeItemVo : setmealList) {
            TradeItem tradeItem = tradeItemVo.getTradeItem();
            String parentUuid = tradeItem.getParentUuid();
            TradeUnionModifyMainWarpReq.TradeUnionTradeItemReq parent = singleFinder.get(parentUuid);
            if (parent == null) {
                Log.e(TAG, "Not found the tradeItem's parent! tradeItem.uuid=" + tradeItem.getUuid());
            } else {
                TradeUnionModifyMainWarpReq.TradeUnionTradeItemReq item = convertToItemReq(tradeItemVo, tradeReasonRelList);
                if (parent.getSubTradeItems() == null) {
                    parent.setSubTradeItems(new ArrayList<TradeUnionModifyMainWarpReq.TradeUnionTradeItemReq>());
                }
                parent.getSubTradeItems().add(item);
                setmealFinder.put(tradeItem.getUuid(), item);
            }
        }
        // 加料
        for (TradeItemVo tradeItemVo : extraList) {
            TradeItem tradeItem = tradeItemVo.getTradeItem();
            String parentUuid = tradeItem.getParentUuid();
            TradeUnionModifyMainWarpReq.TradeUnionTradeItemReq parent = singleFinder.get(parentUuid);
            if (parent != null) {
                TradeUnionModifyMainWarpReq.TradeUnionTradeItemReq item = convertToItemReq(tradeItemVo, tradeReasonRelList);
                if (parent.getSubTradeItems() == null) {
                    parent.setSubTradeItems(new ArrayList<TradeUnionModifyMainWarpReq.TradeUnionTradeItemReq>());
                }
                parent.getSubTradeItems().add(item);
            } else {
                TradeUnionModifyMainWarpReq.TradeUnionTradeItemReq setmealParent = setmealFinder.get(parentUuid);
                if (setmealParent != null) {
                    TradeUnionModifyMainWarpReq.TradeUnionTradeItemReq item = convertToItemReq(tradeItemVo, tradeReasonRelList);
                    if (setmealParent.getSubTradeItems() == null) {
                        setmealParent.setSubTradeItems(new ArrayList<TradeUnionModifyMainWarpReq.TradeUnionTradeItemReq>());
                    }
                    setmealParent.getSubTradeItems().add(item);
                } else {
                    Log.e(TAG, "Not found the tradeItem's parent! tradeItem.uuid=" + tradeItem.getUuid());
                }
            }
        }
    }

    private static TradeUnionModifyMainWarpReq.TradeUnionTradeItemReq convertToItemReq(TradeItemVo tradeItemVo, List<TradeReasonRel> tradeReasonRelList) {
        TradeUnionModifyMainWarpReq.TradeUnionTradeItemReq item = new TradeUnionModifyMainWarpReq.TradeUnionTradeItemReq();
        item.setTradeItemExtra(tradeItemVo.getTradeItemExtra());
        item.setTradeItemOperations(tradeItemVo.getTradeItemOperations());
        item.setTradeItemProperties(tradeItemVo.getTradeItemPropertyList());
        item.setTradeItem(tradeItemVo.getTradeItem());
        if (Utils.isNotEmpty(tradeItemVo.getReasonRelList()))
            tradeReasonRelList.addAll(tradeItemVo.getReasonRelList());
        return item;
    }


    /**
     * 联台子单改单
     *
     * @param tradeVo  子单对应的tradeVo
     * @param listener
     * @param isAsync
     * @param operates
     */
    public static void modifyUnionSubTrade(TradeVo tradeVo, DinnertableTradeInfo mainTradeInfo, ResponseListener<TradeResp> listener, boolean isAsync, TradeOperates operates) {
        modifyUnionSubTrade(tradeVo, null, mainTradeInfo, listener, isAsync, operates);
    }

    public static void modifyUnionSubTrade(TradeVo tradeVo, List<IShopcartItem> shopcartItemList, DinnertableTradeInfo mainTradeInfo, ResponseListener<TradeResp> listener, boolean isAsync, TradeOperates operates) {
        //改单时只传主单、子单
        if (mainTradeInfo != null) {
            MathShopcartItemTool.mathMainShopcartItemsAmount(mainTradeInfo.getTradeVo(), mainTradeInfo.getItems());
        }
        MathShopcartItemTool.mathSubShopcartItemsAmount(tradeVo, shopcartItemList);
        TradeUnionModifySubWarpReq tradeUnionModifySubReq = createUnionSubModifyReq(tradeVo, mainTradeInfo, null);
        operates.modifyUnionSubTrade(tradeVo, tradeUnionModifySubReq, listener, isAsync);
    }

    public static TradeUnionModifySubWarpReq createUnionSubModifyReq(TradeVo tradeVo, DinnertableTradeInfo mainTradeInfo, List<String> selectUuids) {
        TradeUnionModifySubWarpReq tradeUnionSubWarpReq = new TradeUnionModifySubWarpReq();
        TradeUnionModifySubWarpReq.TradeUnionModifySubReq tradeUnionSubReq = new TradeUnionModifySubWarpReq.TradeUnionModifySubReq();
        tradeUnionSubWarpReq.setModifyRequest(tradeUnionSubReq);
        Trade trade = tradeVo.getTrade();
        TradeUnionRequest tradeUnionRequest = covertTradeToUnionRequest(trade);
        //加上主trade 信息
        TradeUnionRequest tradeMainUnionRequest = getMainTradeRequest(mainTradeInfo);
        tradeUnionSubReq.setMainTrade(tradeMainUnionRequest);

        tradeUnionSubReq.setSubTradeExtra(tradeVo.getTradeExtra());
        tradeUnionSubReq.setSubTrade(tradeUnionRequest);
        tradeUnionSubReq.setTradeCustomers(tradeVo.getTradeCustomerList());
        tradeUnionSubReq.setTradeItemExtras(tradeVo.getTradeItemExtraList());
        tradeUnionSubReq.setTradePrivileges(tradeVo.getTradePrivileges());
        tradeUnionSubReq.setTradeReasonRels(tradeVo.getTradeReasonRelList());
        List<TradeItem> tradeItemList = new ArrayList<>();
        List<TradeItemMainBatchRel> tradeItemBatchList = new ArrayList<>();
        List<TradeItemProperty> tradeItemPropertyList = new ArrayList<>();
        List<TradePrivilege> tradePrivilegeList = new ArrayList<>();
        List<TradeReasonRel> tradeReasonRelList = new ArrayList<>();
        List<TradeItemOperation> tradeItemOperations = new ArrayList<>();
        List<TradeItemExtra> tradeItemExtraList = new ArrayList<>();
        if (Utils.isNotEmpty(tradeVo.getTradeItemList())) {
            for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
                if (selectUuids != null && !selectUuids.contains(tradeItemVo.getTradeItem().getUuid()) && tradeItemVo.getTradeItem().getStatusFlag() != StatusFlag.INVALID)
                    continue;
                //联台主单中的子菜，不上传
                if (trade.getId().compareTo(tradeItemVo.getTradeItem().getTradeId()) != 0 || !tradeItemVo.isChanged() || tradeItemVo.getShopcartItemType() == ShopcartItemType.SUBBATCH) {
                    continue;
                }
                tradeItemList.add(tradeItemVo.getTradeItem());
                if (Utils.isNotEmpty(tradeItemVo.getTradeItemPropertyList())) {
                    List<TradeItemProperty> newPropertyList = new ArrayList<>();
                    for (TradeItemProperty tradeItemProperty : tradeItemVo.getTradeItemPropertyList()) {
                        if (!tradeItemProperty.isChanged()) {
                            continue;
                        }
                        newPropertyList.add(tradeItemProperty);
                    }
                    tradeItemPropertyList.addAll(newPropertyList);
                }
                if (Utils.isNotEmpty(tradeItemVo.getTradeItemMainBatchRelList()))
                    tradeItemBatchList.addAll(tradeItemVo.getTradeItemMainBatchRelList());
                if (tradeItemVo.getTradeItemPrivilege() != null && tradeItemVo.getTradeItemPrivilege().isChanged()) {
                    tradePrivilegeList.add(tradeItemVo.getTradeItemPrivilege());
                }

                if (tradeItemVo.getTradeItemExtra() != null && tradeItemVo.getTradeItemExtra().isChanged()) {
                    tradeItemExtraList.add(tradeItemVo.getTradeItemExtra());
                }
                List<TradeReasonRel> reasonRelList = tradeItemVo.getReasonRelList();
                if (reasonRelList != null) {
                    tradeReasonRelList.addAll(reasonRelList);
                }
                if (tradeItemVo.getTradeItemOperations() != null && !tradeItemVo.getTradeItemOperations().isEmpty()) {
                    for (TradeItemOperation tradeItemOperation : tradeItemVo.getTradeItemOperations()) {
                        if (!tradeItemOperation.isChanged())
                            continue;
                        tradeItemOperation.setTradeItemUuid(tradeItemVo.getTradeItem().getUuid());
                        tradeItemOperations.add(tradeItemOperation);
                    }
                }
            }
        }

        tradeUnionSubReq.setTradeItems(tradeItemList);
        tradeUnionSubReq.setTradeItemMainBatchRels(tradeItemBatchList);
        tradeUnionSubReq.setTradeItemProperties(tradeItemPropertyList);
        tradeUnionSubReq.setTradeItemOperations(tradeItemOperations);
        tradeUnionSubReq.setTradePrivileges(tradePrivilegeList);
        tradeUnionSubReq.setTradeReasonRels(tradeReasonRelList);
        tradeUnionSubReq.setTradeItemExtras(tradeItemExtraList);
        if (tradeVo.getTradeUser() != null && tradeVo.getTradeUser().isChanged())
            tradeUnionSubReq.setTradeUser(tradeVo.getTradeUser());
        List<TradeTable> tradeTableList = new ArrayList<>();
        if (Utils.isNotEmpty(tradeVo.getTradeTableList())) {
            for (TradeTable tradeTable : tradeVo.getTradeTableList()) {
                if (!tradeTable.isChanged()) {
                    continue;
                }
                tradeUnionRequest.setTradePeopleCount(tradeTable.getTablePeopleCount());
                tradeTableList.add(tradeTable);
            }
        }
        tradeUnionSubReq.setTradeTables(tradeTableList);
        InventoryUtils.setInventoryVoValue(tradeVo);
        InventoryChangeReq inventoryChangeReq = InventoryUtils.makeInventoryChangeReq(tradeVo.inventoryVo);
        tradeUnionSubWarpReq.setInventoryRequest(inventoryChangeReq);
        return tradeUnionSubWarpReq;
    }


    /**
     * 联台菜品操作
     *
     * @param tradeVo
     * @param listener
     * @param operates
     */
    public static void unionTradeOperationDish(TradeVo tradeVo, List<IShopcartItem> shopcartItemList,
                                               DinnertableTradeInfo mainTradeInfo, List<DishDataItem> selectedItems, ResponseListener<TradeItemResp> listener, TradeOperates operates) {
        //改单时只传主单、子单
        if (mainTradeInfo != null) {
            MathShopcartItemTool.mathMainShopcartItemsAmount(mainTradeInfo.getTradeVo(), mainTradeInfo.getItems());
        }
        if (tradeVo.isUnionSubTrade()) {
            MathShopcartItemTool.mathSubShopcartItemsAmount(tradeVo, shopcartItemList);
        }
        UnionTradeItemOperationReq req = createUnionOperateionReq(tradeVo, mainTradeInfo, selectedItems);

        operates.unionOperationDish(req, listener);
    }

    private static UnionTradeItemOperationReq createUnionOperateionReq(TradeVo tradeVo, DinnertableTradeInfo mainTradeInfo, List<DishDataItem> selectedItems) {
        UnionTradeItemOperationReq req = new UnionTradeItemOperationReq();
        req.setTradeId(tradeVo.getTrade().getId());
        List<String> newSelectedUUids = DinnerDishManager.getInstance().getSingleAndComboUuids(selectedItems);
        List<Long> selectedItemIds = DinnerDishManager.getInstance().getSingleAndComboIds(selectedItems);
        List<String> kitchenUUIDs = DinnerDishManager.getInstance().getNoBatchNo(selectedItems);
        if (tradeVo.isUnionMainTrade()) { // 主单
            UnionTradeItemOperationReq.BatchTradeItemOperationRequest batchTradeItemOperationRequest = new UnionTradeItemOperationReq.BatchTradeItemOperationRequest();
            if (selectedItemIds.isEmpty() || selectedItemIds.size() < selectedItems.size() || !kitchenUUIDs.isEmpty()) {
                TradeUnionModifyMainWarpReq tradeUnionMainReq = createUnionMainModifyReq(tradeVo, mainTradeInfo, newSelectedUUids);
                batchTradeItemOperationRequest.setModifyRequest(tradeUnionMainReq);
            }
            batchTradeItemOperationRequest.setTradeItemOperationsForUuid(createTradeItemOperation(selectedItems));
            req.setBatchTradeItemOperationRequest(batchTradeItemOperationRequest);
        } else if (tradeVo.isUnionSubTrade()) { // 子单等叫主单菜，先拆，然后拆出来的菜当作modityRequest进行封装
            //for (DishDataItem item : selectedItems){ // 有新菜等叫起菜才需要传 modifyRequest ， 没有新菜则不需要传modifyRequest
            //    IShopcartItemBase shopcartItem = item.getBase();
            //    if (shopcartItem.getId() != null && (shopcartItem.getShopcartItemType() == ShopcartItemType.SUBBATCHMODIFY
            //            || shopcartItem.getShopcartItemType() == ShopcartItemType.SUBBATCH)){
            //} else { // 作废的子单菜菜新增一个菜品记录，并移除掉该菜品
            //    if (shopcartItem.getStatusFlag() == StatusFlag.INVALID){
            //        newTradeItem.add(item);
            //        dishDataItems.remove(item);
            //    }
            //}
            //    }
            //}
            boolean hasBatch = DinnerDishManager.getInstance().hasBatchDish(selectedItems);
            UnionTradeItemOperationReq.TradeItemOperationRequest tradeItemOperationRequest = new UnionTradeItemOperationReq.TradeItemOperationRequest();
            if (selectedItemIds.isEmpty() || selectedItemIds.size() < selectedItems.size() || !kitchenUUIDs.isEmpty()
                    || hasBatch) { // 有新菜 ， 组装modityRequest
                TradeUnionModifySubWarpReq tradeUnionSubReq = createUnionSubModifyReq(tradeVo, mainTradeInfo, newSelectedUUids);
                tradeItemOperationRequest.setModifyRequest(tradeUnionSubReq);
            }
            tradeItemOperationRequest.setTradeItemOperationsForUuid(createTradeItemOperation(selectedItems));
            req.setTradeItemOperationRequest(tradeItemOperationRequest);
        }
        return req;
    }

    private static List<TradeItemOperation> createTradeItemOperation(List<DishDataItem> selectedItems) {
        List<TradeItemOperation> tradeItemOperationList = new ArrayList<>();
        for (DishDataItem item : selectedItems) {
            if ((item.getType() == ItemType.WEST_CHILD || item.getType() == ItemType.CHILD) && item.getBase() != null
                    && item.getBase().getTradeItemOperations() != null) {

                for (TradeItemOperation operation : item.getBase().getTradeItemOperations()) {
                    if (!operation.isChanged())
                        continue;
                    if (item.getBase().getId() != null)
                        operation.setTradeItemId(item.getBase().getId());
                    if (item.getBase().getUuid() != null)
                        operation.setTradeItemUuid(item.getBase().getUuid());
                    //if(item.getBase().getBatchId() != null)
                    //    operation.setBatchId(item.getBase().getBatchId());
                    tradeItemOperationList.add(operation);
                }

            } else if ((item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO)
                    && item.getItem() != null && item.getItem().getTradeItemOperations() != null) {

                for (TradeItemOperation operation : item.getItem().getTradeItemOperations()) {
                    if (!operation.isChanged())
                        continue;
                    if (item.getItem().getId() != null)
                        operation.setTradeItemId(item.getItem().getId());
                    if (item.getItem().getUuid() != null)
                        operation.setTradeItemUuid(item.getItem().getUuid());
                    //if(item.getItem().getBatchId() != null)
                    //    operation.setBatchId(item.getItem().getBatchId());
                    tradeItemOperationList.add(operation);
                }

                if (item.getType() == ItemType.COMBO) {
                    IShopcartItem iShopcartItem = item.getItem();
                    List<? extends ISetmealShopcartItem> iSetmealShopcartItems = iShopcartItem.getSetmealItems();
                    for (ISetmealShopcartItem iSetmealShopcartItem : iSetmealShopcartItems) {
                        if (iSetmealShopcartItem != null && iSetmealShopcartItem.getTradeItemOperations() != null) {
                            for (TradeItemOperation operation : iSetmealShopcartItem.getTradeItemOperations()) {
                                if (!operation.isChanged())
                                    continue;
                                if (iSetmealShopcartItem.getId() != null)
                                    operation.setTradeItemId(iSetmealShopcartItem.getId());
                                if (iSetmealShopcartItem.getUuid() != null)
                                    operation.setTradeItemUuid(iSetmealShopcartItem.getUuid());
                                //if(iSetmealShopcartItem.getBatchId() != null)
                                //    operation.setBatchId(iSetmealShopcartItem.getBatchId());
                                tradeItemOperationList.add(operation);
                            }
                        }
                    }
                }
            }
        }
        return tradeItemOperationList;
    }

    /**
     * 如果主单改变 加上主单信息
     *
     * @param dinnertableTradeInfo
     * @return
     */
    public static TradeUnionRequest getMainTradeRequest(DinnertableTradeInfo dinnertableTradeInfo) {
        if (dinnertableTradeInfo == null) {
            return null;
        }
        Trade trade = dinnertableTradeInfo.getTradeVo().getTrade();
        TradeUnionRequest tradeUnionRequest = covertTradeToUnionRequest(trade);
        return tradeUnionRequest;
    }


    public static TradeUnionRequest covertTradeToUnionRequest(Trade trade) {
        TradeUnionRequest tradeUnionRequest = new TradeUnionRequest();
        tradeUnionRequest.setId(trade.getId());
        tradeUnionRequest.setUuid(trade.getUuid());
        tradeUnionRequest.setClientUpdateTime(trade.getClientUpdateTime());
        tradeUnionRequest.setPrivilegeAmount(trade.getPrivilegeAmount());
        tradeUnionRequest.setSaleAmount(trade.getSaleAmount());
        tradeUnionRequest.setServerUpdateTime(trade.getServerUpdateTime());
        tradeUnionRequest.setSkuKindCount(trade.getDishKindCount());
        tradeUnionRequest.setTradeAmount(trade.getTradeAmount());
        tradeUnionRequest.setTradeAmountBefore(trade.getTradeAmountBefore());
        tradeUnionRequest.setTradeMemo(trade.getTradeMemo());
        tradeUnionRequest.setTradePayForm(trade.getTradePayForm().value());
        tradeUnionRequest.setTradePeopleCount(trade.getTradePeopleCount());
        return tradeUnionRequest;
    }


}
