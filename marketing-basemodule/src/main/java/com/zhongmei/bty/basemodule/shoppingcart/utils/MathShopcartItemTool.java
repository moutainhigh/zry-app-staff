package com.zhongmei.bty.basemodule.shoppingcart.utils;

import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */

public class MathShopcartItemTool {

    /**
     * 只计算主单菜品的价格
     *
     * @param shopcartItemList
     * @return
     */
    public static void mathMainShopcartItemsAmount(TradeVo tradeVo, List<IShopcartItem> shopcartItemList) {
        mathShopcartItemsAmount(tradeVo, shopcartItemList, true);
    }

    /**
     * 只计算子单菜品的价格
     *
     * @param shopcartItemList
     * @return
     */
    public static void mathSubShopcartItemsAmount(TradeVo tradeVo, List<IShopcartItem> shopcartItemList) {
        mathShopcartItemsAmount(tradeVo, shopcartItemList, false);
    }

    /**
     * @param tradeVo
     * @param shopcartItemList
     * @param isMainTrade      是否是主单
     */
    private static void mathShopcartItemsAmount(TradeVo tradeVo, List<IShopcartItem> shopcartItemList, boolean isMainTrade) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal saleAmount = BigDecimal.ZERO;
        BigDecimal totalPrivilegeAmount = BigDecimal.ZERO;
        if (Utils.isEmpty(shopcartItemList)) {
            setTradeAmount(tradeVo.getTrade(), saleAmount, totalPrivilegeAmount, totalAmount);
            return;
        }
        Map<String, Object> kindCountMap = new HashMap<>();
        for (IShopcartItem item : shopcartItemList) {
            if (isMainTrade) {
                if (!isMainTradeItemCanMath(item)) {
                    continue;
                }
            } else if (!isSubTradeItemCanMath(item)) {
                continue;
            }
            saleAmount = saleAmount.add(item.getActualAmount());
            TradePrivilege privilege = item.getPrivilege();
            if (privilege != null
                    && privilege.getStatusFlag() == StatusFlag.VALID) {
                totalPrivilegeAmount = totalPrivilegeAmount.add(privilege.getPrivilegeAmount());
            }
            kindCountMap.put(item.getSkuUuid(), null);
        }
        totalAmount = saleAmount.add(totalPrivilegeAmount);
        tradeVo.getTrade().setDishKindCount(kindCountMap.size());
        setTradeAmount(tradeVo.getTrade(), saleAmount, totalPrivilegeAmount, totalAmount);
    }

    private static void setTradeAmount(Trade trade, BigDecimal saleAmount, BigDecimal totalPrivilegeAmount, BigDecimal totalAmount) {
        trade.setSaleAmount(MathDecimal.round(saleAmount, 2));
        trade.setDishAmount(MathDecimal.round(saleAmount, 2));
        trade.setPrivilegeAmount(MathDecimal.round(totalPrivilegeAmount, 2));
        trade.setTradeAmountBefore(MathDecimal.round(totalAmount, 2));
        trade.setTradeAmount(MathShoppingCartTool.getAmountByCarryLimit(totalAmount));
        trade.validateUpdate();
    }

    /**
     * 主单的shopcartItem是否参与计算
     *
     * @param item
     * @return
     */
    private static boolean isMainTradeItemCanMath(IShopcartItem item) {
        if (item.getStatusFlag() == StatusFlag.VALID && item.getShopcartItemType() == ShopcartItemType.MAINBATCH) {
            return true;
        }
        return false;
    }


    /**
     * 子单中的shopcarItem是否参与计算
     *
     * @param item
     * @return
     */
    private static boolean isSubTradeItemCanMath(IShopcartItem item) {
        if (!item.isGroupDish() && (item.getStatusFlag() == StatusFlag.VALID && (item.getShopcartItemType() == ShopcartItemType.SUB || item.getShopcartItemType() == ShopcartItemType.SUBBATCHMODIFY))) {
            return true;
        }
        return false;
    }


}
