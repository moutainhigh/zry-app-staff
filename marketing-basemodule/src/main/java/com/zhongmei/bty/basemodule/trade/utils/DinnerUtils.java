package com.zhongmei.bty.basemodule.trade.utils;

import com.zhongmei.util.SettingManager;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.bty.basemodule.trade.settings.DinnerWesternDishSortSettings;
import com.zhongmei.bty.basemodule.trade.settings.DinnerWesternIPanelSettings;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.util.ArrayList;
import java.util.List;



public class DinnerUtils {


    public static boolean isWestStyle() {
        DinnerWesternIPanelSettings iPanelSettings = SettingManager.getSettings(DinnerWesternIPanelSettings.class);
        int currentPanel = iPanelSettings.getPanel();
        if (currentPanel == DinnerWesternIPanelSettings.PANEL_TYPE_2) {
            return true;
        }
        return false;
    }


    public static boolean isMediumStyle() {
        DinnerWesternDishSortSettings iPanelSettings = SettingManager.getSettings(DinnerWesternDishSortSettings.class);
        int currentPanel = iPanelSettings.getType();
        if (currentPanel == DinnerWesternDishSortSettings.MED_TYPE_1) {
            return true;
        }
        return false;
    }


    public static boolean isServingStyle() {
        DinnerWesternDishSortSettings iPanelSettings = SettingManager.getSettings(DinnerWesternDishSortSettings.class);
        int currentPanel = iPanelSettings.getType();
        if (currentPanel == DinnerWesternDishSortSettings.SERVING_TYPE_2) {
            return true;
        }
        return false;
    }


    public static List<String> getSingleAndComboUuids(List<DishDataItem> dishDataItems) {
        List<String> selectedItemUuids = new ArrayList<String>();
        for (DishDataItem item : dishDataItems) {
            if ((item.getType() == ItemType.WEST_CHILD || item.getType() == ItemType.CHILD) && item.getBase() != null && item.getBase().getUuid() != null) {
                selectedItemUuids.add(item.getBase().getUuid());
                selectedItemUuids.add(item.getItem().getUuid());
            } else if ((item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO)
                    && item.getItem() != null && item.getItem().getUuid() != null) {
                selectedItemUuids.add(item.getItem().getUuid());
                if (item.getType() == ItemType.COMBO) {
                    IShopcartItem iShopcartItem = item.getItem();
                    List<? extends ISetmealShopcartItem> iSetmealShopcartItems = iShopcartItem.getSetmealItems();
                    for (ISetmealShopcartItem iSetmealShopcartItem : iSetmealShopcartItems) {
                        if (iSetmealShopcartItem != null && !selectedItemUuids.contains(iSetmealShopcartItem.getUuid())) {
                            selectedItemUuids.add(iSetmealShopcartItem.getUuid());
                        }
                    }
                }
            }
        }

        return selectedItemUuids;
    }

    public static List<String> getSingleAndComboUuids(List<DishDataItem> dishDataItems, PrintOperationOpType opType) {
        List<String> selectedItemUuids = new ArrayList<String>();
        for (DishDataItem item : dishDataItems) {
            if ((item.getType() == ItemType.WEST_CHILD || item.getType() == ItemType.CHILD) && item.getBase() != null && item.getBase().getUuid() != null) {
                selectedItemUuids.add(item.getBase().getUuid());
                selectedItemUuids.add(item.getItem().getUuid());

            } else if ((item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO)
                    && item.getItem() != null && item.getItem().getUuid() != null) {
                selectedItemUuids.add(item.getItem().getUuid());

                if (item.getType() == ItemType.COMBO) {
                    IShopcartItem iShopcartItem = item.getItem();
                    List<? extends ISetmealShopcartItem> iSetmealShopcartItems = iShopcartItem.getSetmealItems();
                    for (ISetmealShopcartItem iSetmealShopcartItem : iSetmealShopcartItems) {
                        if (iSetmealShopcartItem != null && !selectedItemUuids.contains(iSetmealShopcartItem.getUuid())
                                && isAddOpType(iSetmealShopcartItem, opType)) {
                            selectedItemUuids.add(iSetmealShopcartItem.getUuid());
                        }
                    }
                }
            }
        }

        return selectedItemUuids;
    }


    private static boolean isAddOpType(ISetmealShopcartItem iShopcartItem, PrintOperationOpType opType) {
        List<TradeItemOperation> operations = iShopcartItem.getTradeItemOperations();
        if (operations == null || operations.isEmpty())
            return false;
        for (TradeItemOperation operation : operations) {
            if (operation.getOpType() == opType && operation.getStatusFlag() == StatusFlag.VALID && operation.getId() == null)
                return true;
        }
        return false;
    }

}
