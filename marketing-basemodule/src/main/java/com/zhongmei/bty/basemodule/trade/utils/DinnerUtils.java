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

/**
 * Created by demo on 2018/12/15
 */

public class DinnerUtils {

    /**
     * 是否是西餐样式
     *
     * @return
     */
    public static boolean isWestStyle() {
        DinnerWesternIPanelSettings iPanelSettings = SettingManager.getSettings(DinnerWesternIPanelSettings.class);
        int currentPanel = iPanelSettings.getPanel();
        if (currentPanel == DinnerWesternIPanelSettings.PANEL_TYPE_2) {
            return true;
        }
        return false;
    }

    /**
     * 西餐界面显示是否是中类排序
     *
     * @return
     */
    public static boolean isMediumStyle() {
        DinnerWesternDishSortSettings iPanelSettings = SettingManager.getSettings(DinnerWesternDishSortSettings.class);
        int currentPanel = iPanelSettings.getType();
        if (currentPanel == DinnerWesternDishSortSettings.MED_TYPE_1) {
            return true;
        }
        return false;
    }

    /**
     * 按中类排序显示
     *
     * @return
     */
    public static boolean isServingStyle() {
        DinnerWesternDishSortSettings iPanelSettings = SettingManager.getSettings(DinnerWesternDishSortSettings.class);
        int currentPanel = iPanelSettings.getType();
        if (currentPanel == DinnerWesternDishSortSettings.SERVING_TYPE_2) {
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

    /**
     * 取未保存的操作
     *
     * @param iShopcartItem
     * @param opType
     * @return
     */
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
