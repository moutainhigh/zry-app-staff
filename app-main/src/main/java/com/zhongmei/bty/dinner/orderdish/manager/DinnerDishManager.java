package com.zhongmei.bty.dinner.orderdish.manager;

import android.text.TextUtils;

import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.dinner.manager.DinnerTradeItemManager;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.enums.GuestPrinted;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DinnerDishManager {

    private final static String TAG = DinnerDishManager.class.getSimpleName();

    private static DinnerDishManager instance = new DinnerDishManager();

    private DinnerDishManager() {
    }

    public static DinnerDishManager getInstance() {
        synchronized (instance) {
            return instance;
        }
    }

    /**
     * 移除批量菜品对应操作记录标签
     *
     * @Title: removeSelectedTradeItemOperations
     * @Param @param selectedItems
     * @Param @param opType
     * @Return boolean 返回类型
     */
    public boolean removeSelectedTradeItemOperations(List<DishDataItem> dishDataItems, PrintOperationOpType opType) {
        Map<IShopcartItemBase, PrintOperationOpType> map = new HashMap<>();
        for (DishDataItem item : dishDataItems) {
            if ((item.getType() == ItemType.WEST_CHILD || item.getType() == ItemType.CHILD) && item.getBase() != null) {
                map.put(item.getBase(), opType);
            } else if ((item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO) && item.getItem() != null
                    && item.getItem().getId() != null) {
                map.put(item.getItem(), opType);
            }
        }
        if (!map.isEmpty()) {
            DinnerShoppingCart.getInstance().removeTradeItemOperations(map);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 批量添加菜品操作
     *
     * @param dishDataItems
     * @param opType
     * @return
     */
    public boolean addSelectedTradeItemOperations(List<DishDataItem> dishDataItems, PrintOperationOpType opType) {
        Map<IShopcartItemBase, PrintOperationOpType> map = new HashMap<>();
        for (DishDataItem item : dishDataItems) {
            if ((item.getType() == ItemType.WEST_CHILD || item.getType() == ItemType.CHILD) && item.getBase() != null) {
                map.put(item.getBase(), opType);
            } else if (item.getType() == ItemType.SINGLE && item.getItem() != null) {
                map.put(item.getItem(), opType);
            } else if (item.getType() == ItemType.COMBO && item.getItem() != null) {
                IShopcartItem iShopcartItem = item.getItem();
                map.put(iShopcartItem, opType);
                List<? extends ISetmealShopcartItem> iSetmealShopcartItems = iShopcartItem.getSetmealItems();
                for (ISetmealShopcartItem iSetmealShopcartItem : iSetmealShopcartItems) {
                    if (iSetmealShopcartItem != null && !map.containsKey(iSetmealShopcartItem)
                            && DinnerTradeItemManager.getInstance().getShopCartItemCheckStatus(iSetmealShopcartItem, opType) == DishDataItem.DishCheckStatus.NOT_CHECK)
                        map.put(iSetmealShopcartItem, opType);
                }
            }
        }
        if (!map.isEmpty()) {
            DinnerShoppingCart.getInstance().addTradeItemOperations(map);
            return true;
        } else {
            return false;
        }
    }

    public void removeInvoidTradeItemOperations(List<DishDataItem> dishDataItems) {
        List<TradeItemOperation> tradeItemOperations;
        for (DishDataItem item : dishDataItems) {
            if ((item.getType() == ItemType.WEST_CHILD || item.getType() == ItemType.CHILD) && item.getBase() != null) {
                tradeItemOperations = item.getBase().getTradeItemOperations();
                if (tradeItemOperations == null || tradeItemOperations.isEmpty())
                    continue;
                Iterator<TradeItemOperation> itemOperationIterator = tradeItemOperations.iterator();
                while (itemOperationIterator.hasNext()) {
                    TradeItemOperation operation = itemOperationIterator.next();
                    if (operation.getId() == null)
                        itemOperationIterator.remove();
                }
            } else if ((item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO) && item.getItem() != null) {
                tradeItemOperations = item.getItem().getTradeItemOperations();
                if (tradeItemOperations != null && !tradeItemOperations.isEmpty()) {
                    Iterator<TradeItemOperation> itemOperationIterator = tradeItemOperations.iterator();
                    while (itemOperationIterator.hasNext()) {
                        TradeItemOperation operation = itemOperationIterator.next();
                        if (operation.getId() == null)
                            itemOperationIterator.remove();
                    }
                }
                if (item.getType() == ItemType.COMBO) {
                    List<? extends ISetmealShopcartItem> iSetmealShopcartItems = item.getItem().getSetmealItems();
                    if (iSetmealShopcartItems == null || iSetmealShopcartItems.isEmpty())
                        continue;
                    for (ISetmealShopcartItem iSetmealShopcartItem : iSetmealShopcartItems) {
                        tradeItemOperations = iSetmealShopcartItem.getTradeItemOperations();
                        if (tradeItemOperations == null || tradeItemOperations.isEmpty())
                            continue;
                        Iterator<TradeItemOperation> OperationIterator = tradeItemOperations.iterator();
                        while (OperationIterator.hasNext()) {
                            TradeItemOperation operation = OperationIterator.next();
                            if (operation.getId() == null)
                                OperationIterator.remove();
                        }
                    }
                }
            }
        }
    }

    /**
     * 是否有未生成批次号的
     *
     * @param dishDataItems
     * @return
     */
    public List<String> getNoBatchNo(List<DishDataItem> dishDataItems) {
        List<String> uuids = new ArrayList<>();
        for (DishDataItem item : dishDataItems) {
            if ((item.getType() == ItemType.WEST_CHILD || item.getType() == ItemType.CHILD) && item.getBase() != null) {
                if (TextUtils.isEmpty(item.getBase().getBatchNo())) {
                    uuids.add(item.getBase().getUuid());

                    Collection<? extends IExtraShopcartItem> extralist = item.getBase().getExtraItems();
                    if (extralist != null && !extralist.isEmpty()) {
                        for (IExtraShopcartItem extra : extralist) {
                            uuids.add(extra.getUuid());
                        }
                    }
                    uuids.add(item.getItem().getUuid());

                    extralist = item.getItem().getExtraItems();
                    if (extralist != null && !extralist.isEmpty()) {
                        for (IExtraShopcartItem extra : extralist) {
                            uuids.add(extra.getUuid());
                        }
                    }
                }
            } else if ((item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO) && item.getItem() != null) {
                if (TextUtils.isEmpty(item.getItem().getBatchNo())) {
                    uuids.add(item.getItem().getUuid());

                    Collection<? extends IExtraShopcartItem> extralist = item.getItem().getExtraItems();
                    if (extralist != null && !extralist.isEmpty()) {
                        for (IExtraShopcartItem extra : extralist) {
                            uuids.add(extra.getUuid());
                        }
                    }

                    if (item.getType() == ItemType.COMBO) {
                        List<? extends ISetmealShopcartItem> iSetmealShopcartItems = item.getItem().getSetmealItems();
                        if (iSetmealShopcartItems == null || iSetmealShopcartItems.isEmpty())
                            continue;
                        for (ISetmealShopcartItem iSetmealShopcartItem : iSetmealShopcartItems) {
                            uuids.add(iSetmealShopcartItem.getUuid());

                            extralist = iSetmealShopcartItem.getExtraItems();
                            if (extralist != null && !extralist.isEmpty()) {
                                for (IExtraShopcartItem extra : extralist) {
                                    uuids.add(extra.getUuid());
                                }
                            }
                        }
                    }
                }
            }
        }
        return uuids;
    }

    public boolean hasBatchDish(List<DishDataItem> dishDataItems) {
        boolean hasBatch = false;
        for (DishDataItem dishDataItem : dishDataItems) {
            if (dishDataItem.getBase() == null)
                continue;
            if (dishDataItem.getBase().getShopcartItemType() == ShopcartItemType.SUBBATCH
                    || dishDataItem.getBase().getShopcartItemType() == ShopcartItemType.SUBBATCHMODIFY)
                hasBatch = true;
        }
        return hasBatch;
    }

    /**
     * 是否加菜
     *
     * @param dishDataItems
     * @return
     */
    public boolean isAddDish(List<DishDataItem> dishDataItems) {

        if (filterNoBatchNo(dishDataItems).isEmpty())
            return false;
        else
            return true;
    }


    public boolean isAddDishEx(List<TradeItemVo> tradeItemVos) {
        for (TradeItemVo tradeItemVo : tradeItemVos) {
            if (!TextUtils.isEmpty(tradeItemVo.getTradeItem().getBatchNo())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 过滤未生成批次号的
     */
    public List<DishDataItem> filterNoBatchNo(List<DishDataItem> dishDataItems) {
        List<DishDataItem> tmpList = new ArrayList<>();
        for (DishDataItem item : dishDataItems) {
            if ((item.getType() == ItemType.WEST_CHILD || item.getType() == ItemType.CHILD) && item.getBase() != null) {
                if (!TextUtils.isEmpty(item.getBase().getBatchNo()))
                    tmpList.add(item);
            } else if ((item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO) && item.getItem() != null) {
                if (!TextUtils.isEmpty(item.getItem().getBatchNo()))
                    tmpList.add(item);
            }
        }
        return tmpList;
    }

    /**
     * 获取菜品列表中单菜和套餐外壳的id
     *
     * @Title: getSingleAndComboIds
     * @Param @param dishDataItems
     * @Return List<Long> 返回类型
     */
    public List<Long> getSingleAndComboIds(List<DishDataItem> dishDataItems) {
        List<Long> selectedItemIds = new ArrayList<Long>();
        for (DishDataItem item : dishDataItems) {
            if ((item.getType() == ItemType.WEST_CHILD || item.getType() == ItemType.CHILD) && item.getBase() != null && item.getBase().getId() != null) {
                selectedItemIds.add(item.getBase().getId());
            } else if ((item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO)
                    && item.getItem() != null && item.getItem().getId() != null) {
                selectedItemIds.add(item.getItem().getId());
            }
        }

        return selectedItemIds;
    }

    public boolean isChanged(List<DishDataItem> dishDataItems) {
        for (DishDataItem item : dishDataItems) {
            if (item.getBase() != null && item.getBase().isChanged())
                return true;
        }

        return false;
    }

    /**
     * 获取菜品列表中单菜和套餐外壳的id
     *
     * @Title: getSingleAndComboUuids
     * @Param @param dishDataItems
     * @Return List<String> 返回类型
     */
    public List<String> getSingleAndComboUuids(List<DishDataItem> dishDataItems) {
        List<String> selectedItemUuids = new ArrayList<String>();
        for (DishDataItem item : dishDataItems) {
            if ((item.getType() == ItemType.WEST_CHILD || item.getType() == ItemType.CHILD) && item.getBase() != null && item.getBase().getUuid() != null) {
                selectedItemUuids.add(item.getBase().getUuid());
                if (item.getItem() != null && !selectedItemUuids.contains(item.getItem().getUuid()))    //加入套餐外壳的UUid
                    selectedItemUuids.add(item.getItem().getUuid());
            } else if (item.getType() == ItemType.SINGLE && item.getItem() != null && item.getItem().getUuid() != null) {
                selectedItemUuids.add(item.getItem().getUuid());
            } else if (item.getType() == ItemType.COMBO && item.getItem() != null && item.getItem().getUuid() != null) {    //加入套餐子菜UUid
                IShopcartItem iShopcartItem = item.getItem();
                if (iShopcartItem.getUuid() != null && !selectedItemUuids.contains(iShopcartItem.getUuid()))
                    selectedItemUuids.add(iShopcartItem.getUuid());
                List<? extends ISetmealShopcartItem> iSetmealShopcartItems = iShopcartItem.getSetmealItems();
                for (ISetmealShopcartItem iSetmealShopcartItem : iSetmealShopcartItems) {
                    if (iSetmealShopcartItem != null && !selectedItemUuids.contains(iSetmealShopcartItem.getUuid()))
                        selectedItemUuids.add(iSetmealShopcartItem.getUuid());
                }
            }
        }

        return selectedItemUuids;
    }


    /**
     * 设置菜品ID
     *
     * @param tradeItems
     * @param dishDataItems
     */
    public void setDishItemsId(List<TradeItem> tradeItems, List<DishDataItem> dishDataItems) {
        Map<String, Long> tmpMap = new HashMap<>();
        for (TradeItem item : tradeItems) {
            tmpMap.put(item.getUuid(), item.getId());
        }

        for (DishDataItem item : dishDataItems) {
            if ((item.getType() == ItemType.WEST_CHILD || item.getType() == ItemType.CHILD) && item.getBase() != null && item.getBase().getUuid() != null) {
                Long id = tmpMap.get(item.getBase().getUuid());
                List<TradeItemOperation> operations = item.getBase().getTradeItemOperations();
                if (Utils.isNotEmpty(operations)) {
                    for (TradeItemOperation operation : operations) {
                        operation.setTradeItemId(id);
                    }
                }
            } else if ((item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO)
                    && item.getItem() != null && item.getItem().getUuid() != null) {
                Long id = tmpMap.get(item.getItem().getUuid());
                List<TradeItemOperation> operations = item.getItem().getTradeItemOperations();
                if (Utils.isNotEmpty(operations)) {
                    for (TradeItemOperation operation : operations) {
                        operation.setTradeItemId(id);
                    }
                }

                if (item.getType() == ItemType.COMBO) {
                    IShopcartItem iShopcartItem = item.getItem();
                    List<? extends ISetmealShopcartItem> iSetmealShopcartItems = iShopcartItem.getSetmealItems();
                    for (ISetmealShopcartItem iSetmealShopcartItem : iSetmealShopcartItems) {
                        if (iSetmealShopcartItem != null) {
                            id = tmpMap.get(iSetmealShopcartItem.getUuid());
                            for (TradeItemOperation operation : iSetmealShopcartItem.getTradeItemOperations()) {
                                //if(iSetmealShopcartItem.getId() != null)
                                operation.setTradeItemId(id);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取最新的菜品操作记录
     *
     * @param tradeItemOperations
     * @return
     */
    public TradeItemOperation getLastOperation(List<TradeItemOperation> tradeItemOperations) {
        if (Utils.isEmpty(tradeItemOperations)) {
            return null;
        }

        TradeItemOperation wakeUpTio = null;
        TradeItemOperation riseUpTio = null;
        TradeItemOperation cancelWakeUpTio = null;
        TradeItemOperation cancelRiseUpTio = null;
        //挑选催菜、起菜、等叫操作，如果遇到催菜（优先级最高），直接返回
        for (int i = tradeItemOperations.size() - 1; i >= 0; i--) {
            TradeItemOperation tradeItemOperation = tradeItemOperations.get(i);
            if (tradeItemOperation.getStatusFlag() == StatusFlag.VALID) {
                switch (tradeItemOperation.getOpType()) {
                    case REMIND_DISH:
                        return tradeItemOperation;
                    case WAKE_UP:
                        wakeUpTio = tradeItemOperation;
                        break;
                    case RISE_DISH:
                        riseUpTio = tradeItemOperation;
                        break;
                    case WAKE_UP_CANCEL:
                        cancelWakeUpTio = tradeItemOperation;
                        break;
                    case RISE_DISH_CANCEL:
                        cancelRiseUpTio = tradeItemOperation;
                        break;
                    default:
                        break;
                }
            }
        }

        //没有催菜时，依次按照取消起菜、取消等叫、起菜和等叫的优先级返回
        if (cancelRiseUpTio != null) {
            return cancelRiseUpTio;
        } else if (cancelWakeUpTio != null) {
            return cancelWakeUpTio;
        } else if (riseUpTio != null) {
            return riseUpTio;
        } else if (wakeUpTio != null) {
            return wakeUpTio;
        }

        return null;
    }

    public static void prepareCustomPrintData(TradeVo tradeVo, List<String> customAddDishUuids, List<String> customModifyDishUuids,
                                              List<String> customDeleteDishUuids) {
        //团餐餐标
        if (tradeVo.getMealShellVo() != null && tradeVo.getMealShellVo().getTradeItem() != null)
            customAddDishUuids.add(tradeVo.getMealShellVo().getTradeItem().getUuid());

        //缓存所有菜品
        Map<String, TradeItem> tradeItemMap = new HashMap<>();
        for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
            tradeItemMap.put(tradeItemVo.getTradeItem().getUuid(), tradeItemVo.getTradeItem());
        }

        for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
            TradeItem tradeItem = tradeItemVo.getTradeItem();
            String relateUuid = tradeItem.getRelateTradeItemUuid();
            TradeItem relateTradeItem = tradeItemMap.get(relateUuid);

            if (tradeItem.getGuestPrinted() == GuestPrinted.UNPRINT) {
                /*DinnerPrintUtil.prepareDishCustomPrintData(tradeItem, relateTradeItem, tradeItemMap, customAddDishUuids,
                        customDeleteDishUuids, customModifyDishUuids);*/
            }
        }
    }


}
