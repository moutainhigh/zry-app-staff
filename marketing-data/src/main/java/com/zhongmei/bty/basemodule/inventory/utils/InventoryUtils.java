package com.zhongmei.bty.basemodule.inventory.utils;

import android.text.TextUtils;

import com.zhongmei.bty.basemodule.inventory.bean.InventoryItem;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryVo;
import com.zhongmei.bty.basemodule.inventory.message.InventoryChangeReq;
import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlySetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */

public class InventoryUtils {

    /**
     * 获取菜品库存数据（包含退回和扣减）
     *
     * @param inventoryVo
     * @return
     */
    public static InventoryChangeReq makeInventoryChangeReq(InventoryVo inventoryVo) {
        /*if(inventoryVo == null){
            return null;
        }*/
        InventoryChangeReq inventoryChangeReq = new InventoryChangeReq();
        inventoryChangeReq.setReturnInventoryItems(getReturnInventoryItemReq(inventoryVo));
        inventoryChangeReq.setDeductInventoryItems(getDeductInventoryItems(inventoryVo));

        return inventoryChangeReq;
    }

    /**
     * 库存退回数据
     *
     * @param inventoryVo
     * @return
     */
    private static List<InventoryItemReq> getReturnInventoryItemReq(InventoryVo inventoryVo) {
        if (inventoryVo != null) {
            if (inventoryVo.getReturnInventoryItemMap() != null &&
                    !inventoryVo.getReturnInventoryItemMap().isEmpty()) {
                List<InventoryItemReq> returnInventoryItems = new ArrayList<>();

                for (InventoryItem item : inventoryVo.getReturnInventoryItemMap().values()) {
                    if (item.getTradeItem().getType() == DishType.EXTRA) {
                        continue;
                    }
                    InventoryItemReq returnInventoryItem = new InventoryItemReq();
                    returnInventoryItem.setSkuUuid(item.getTradeItem().getSkuUuid());
                    returnInventoryItem.setSkuName(item.getTradeItem().getDishName());
                    returnInventoryItem.setQuantity(item.getReturnInventoryNum());
                    returnInventoryItem.setPrice(item.getTradeItem().getPrice());
                    returnInventoryItem.setAmount(returnInventoryItem.getQuantity().multiply(returnInventoryItem.getPrice()));
                    returnInventoryItem.setReturnQuantity(item.getMaxInventoryNum());
                    returnInventoryItems.add(returnInventoryItem);
                    if (Utils.isNotEmpty(item.getChildTradeItem())) {
                        for (TradeItem tradeItem : item.getChildTradeItem()) {
                            returnInventoryItem = new InventoryItemReq();
                            returnInventoryItem.setDishId(tradeItem.getDishId());
                            returnInventoryItem.setSkuUuid(tradeItem.getSkuUuid());
                            returnInventoryItem.setSkuName(tradeItem.getDishName());
                            returnInventoryItem.setQuantity(item.getChildQuantityValue(tradeItem.getQuantity()));
                            returnInventoryItem.setPrice(tradeItem.getPrice());
                            returnInventoryItem.setAmount(returnInventoryItem.getQuantity().multiply(returnInventoryItem.getPrice()));
                            returnInventoryItem.setReturnQuantity(tradeItem.getQuantity());
                            returnInventoryItems.add(returnInventoryItem);
                        }
                    }
                }

                return returnInventoryItems;
            }
        }
        return null;
    }

    /**
     * 库存扣减数据
     *
     * @param inventoryVo
     * @return
     */
    private static List<InventoryItemReq> getDeductInventoryItems(InventoryVo inventoryVo) {
        if (inventoryVo != null) {
            if (Utils.isNotEmpty(inventoryVo.getNewAddDishList())) {
                List<InventoryItemReq> deductInventoryItems = new ArrayList<>();

                for (TradeItem item : inventoryVo.getNewAddDishList()) {
                    if (item.getType() == DishType.EXTRA) {
                        continue;
                    }
                    InventoryItemReq deductInventoryItem = new InventoryItemReq();
                    deductInventoryItem.setDishId(item.getDishId());
                    deductInventoryItem.setSkuUuid(item.getSkuUuid());
                    deductInventoryItem.setSkuName(item.getDishName());
                    deductInventoryItem.setQuantity(item.getQuantity());
                    deductInventoryItem.setPrice(item.getPrice());
                    deductInventoryItem.setAmount(item.getAmount());
                    deductInventoryItems.add(deductInventoryItem);
                }
                return deductInventoryItems;
            }
        }
        return null;
    }

    /**
     * 构建退库存请求
     *
     * @param inventoryItemList
     * @return
     */
    public static List<InventoryItemReq> buildInventoryItemReqs(List<InventoryItem> inventoryItemList) {
        if (Utils.isEmpty(inventoryItemList)) {
            return null;
        }
        List<InventoryItemReq> inventoryItemReqs = new ArrayList<>();
        for (InventoryItem inventoryItem : inventoryItemList) {
            if (inventoryItem.getTradeItem().getType() == DishType.COMBO) {
                for (TradeItem childTradeItem : inventoryItem.getChildTradeItem()) {
                    InventoryItemReq inventoryItemReq = new InventoryItemReq();
                    inventoryItemReq.setPrice(childTradeItem.getPrice());
                    //套餐按比例计算退库存数
                    BigDecimal total = inventoryItem.getReturnInventoryNum()
                            .multiply(childTradeItem.getQuantity());
                    BigDecimal quantity = MathDecimal.div(total, inventoryItem.getMaxInventoryNum());
                    //退库存数量
                    inventoryItemReq.setQuantity(quantity);
                    //退货数量
                    inventoryItemReq.setReturnQuantity(childTradeItem.getQuantity());
                    inventoryItemReq.setAmount(childTradeItem.getPrice().multiply(inventoryItemReq.getQuantity()));
                    inventoryItemReq.setSkuName(childTradeItem.getDishName());
                    inventoryItemReq.setSkuUuid(childTradeItem.getSkuUuid());
                    inventoryItemReqs.add(inventoryItemReq);
                }
            } else {
                InventoryItemReq inventoryItemReq = new InventoryItemReq();
                inventoryItemReq.setPrice(inventoryItem.getTradeItem().getPrice());
                inventoryItemReq.setQuantity(inventoryItem.getReturnInventoryNum());
                inventoryItemReq.setReturnQuantity(inventoryItem.getMaxInventoryNum());
                inventoryItemReq.setAmount(inventoryItemReq.getPrice().multiply(inventoryItemReq.getQuantity()));
                inventoryItemReq.setSkuName(inventoryItem.getTradeItem().getDishName());
                inventoryItemReq.setSkuUuid(inventoryItem.getTradeItem().getSkuUuid());
                inventoryItemReqs.add(inventoryItemReq);
            }
        }
        return inventoryItemReqs;
    }

    /***
     * 单菜品操作
     * 将已生效的DishDataItem转换成InventoryItem
     * @param dishDataItem
     * @return
     */
    public static InventoryItem transformInventoryItem(DishDataItem dishDataItem, BigDecimal returnCount) {
        InventoryItem inventoryItem = null;
        if (dishDataItem == null) return null;
        TradeItem tradeItem = ((ReadonlyShopcartItem) dishDataItem.getBase()).tradeItem;
//        if(tradeItem.getSaleType() == SaleType.WEIGHING){
//            inventoryItem = new InventoryItem(tradeItem);
//        }else {
//            if(TextUtils.isEmpty(dishDataItem.getBase().getBatchNo()) || dishDataItem.getBase().getShopcartItemType() == ShopcartItemType.MAINBATCH){
//                inventoryItem = new InventoryItem(tradeItem);
//            }else {
//                inventoryItem = new InventoryItem(tradeItem,
//                        returnCount,InventoryItem.TAG_INVENTORY_NUM);
//            }
//            if(tradeItem.getType() == DishType.COMBO){
//                inventoryItem.setDishQuantity(((ReadonlyShopcartItem)dishDataItem.getBase()).tradeItem.getQuantity());
//                List<TradeItem> childTradeItem = new ArrayList<>();
//                for (ReadonlySetmealShopcartItem item:((ReadonlyShopcartItem)dishDataItem.getBase()).getSetmealItems()) {
//                    childTradeItem.add(item.tradeItem);
//                }
//                inventoryItem.setChildTradeItem(childTradeItem);
//            }
//        }
        inventoryItem = new InventoryItem(tradeItem, returnCount, InventoryItem.TAG_INVENTORY_NUM);
        if (tradeItem.getType() == DishType.COMBO) {
            inventoryItem.setDishQuantity(((ReadonlyShopcartItem) dishDataItem.getBase()).tradeItem.getQuantity());
            List<TradeItem> childTradeItem = new ArrayList<>();
            for (ReadonlySetmealShopcartItem item : ((ReadonlyShopcartItem) dishDataItem.getBase()).getSetmealItems()) {
                childTradeItem.add(item.tradeItem);
            }
            inventoryItem.setChildTradeItem(childTradeItem);
        }
        if (dishDataItem.getBase().isGroupDish()) {
            inventoryItem.setGroupDish(true);
        }
        return inventoryItem;
    }

    /**
     * 批量菜品操作
     *
     * @return
     */
    public static List<InventoryItem> transformInventoryItemList(List<DishDataItem> dishDataItemList) {
        if (Utils.isEmpty(dishDataItemList)) return null;
        List<InventoryItem> inventoryItemList = new ArrayList<>();
        InventoryItem inventoryItem;
        for (DishDataItem item : dishDataItemList) {
            TradeItem tradeItem = ((ReadonlyShopcartItem) item.getBase()).tradeItem;
            inventoryItem = new InventoryItem(tradeItem);
            if (tradeItem.getType() == DishType.COMBO) {
                inventoryItem.setDishQuantity(((ReadonlyShopcartItem) item.getBase()).tradeItem.getQuantity());
                List<TradeItem> childTradeItem = new ArrayList<>();
                for (ReadonlySetmealShopcartItem childItem : ((ReadonlyShopcartItem) item.getBase()).getSetmealItems()) {
                    childTradeItem.add(childItem.tradeItem);
                }
                inventoryItem.setChildTradeItem(childTradeItem);
            }
            inventoryItemList.add(inventoryItem);
        }
        return inventoryItemList;
    }

    /**
     * 用于联台功能的拆单，将TradeItem转换成InventoryItemReq
     *
     * @param tradeItems
     * @return
     */
    public static List<InventoryItemReq> getInventoryItemReqList(List<TradeItem> tradeItems) {
        if (Utils.isNotEmpty(tradeItems)) {
            List<InventoryItemReq> inventoryItemReqList = new ArrayList<>();
            for (TradeItem item : tradeItems) {
                InventoryItemReq deductInventoryItem = new InventoryItemReq();
                deductInventoryItem.setSkuUuid(item.getSkuUuid());
                deductInventoryItem.setSkuName(item.getDishName());
                deductInventoryItem.setQuantity(item.getQuantity());
                deductInventoryItem.setPrice(item.getPrice());
                deductInventoryItem.setAmount(item.getAmount());
                inventoryItemReqList.add(deductInventoryItem);
            }
            return inventoryItemReqList;
        } else {
            return null;
        }
    }

    public static void setInventoryVoValue(TradeVo tradeVo) {
        if (tradeVo.inventoryVo == null) tradeVo.inventoryVo = new InventoryVo();
        List<TradeItem> newAddDishList = new ArrayList<>();
        Map<String, TradeItemVo> tradeItemMap = new HashMap<String, TradeItemVo>();
        if (tradeVo != null && Utils.isNotEmpty(tradeVo.getTradeItemList())) {
            // 把tradeitemvo存在一个map中，查找关联菜品

            for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
                tradeItemMap.put(tradeItemVo.getTradeItem().getUuid(), tradeItemVo);
            }

            for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
                String relateTradeItemUuid = tradeItemVo.getTradeItem().getRelateTradeItemUuid();

                if (tradeItemVo.getTradeItem().getId() == null) {
                    if (TextUtils.isEmpty(relateTradeItemUuid)) {//新点的菜
                        newAddDishList.add(tradeItemVo.getTradeItem());
                    } else {
                        if (tradeItemMap.containsKey(relateTradeItemUuid)) {
                            TradeItemVo relateTradeItemVo = tradeItemMap.get(relateTradeItemUuid);
                            if (relateTradeItemVo.getTradeItem().getInvalidType() == InvalidType.MODIFY_DISH) {
                                newAddDishList.add(tradeItemVo.getTradeItem());
                            }
                        }
                    }
                }
            }
        }
        if (tradeVo.inventoryVo != null) {
            tradeVo.inventoryVo.addNewAddDishList(newAddDishList);
        }

    }
}
