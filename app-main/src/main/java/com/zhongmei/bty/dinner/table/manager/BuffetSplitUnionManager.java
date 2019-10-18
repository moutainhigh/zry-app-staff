package com.zhongmei.bty.dinner.table.manager;

import com.zhongmei.bty.basemodule.inventory.message.InventoryChangeReq;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryUtils;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.bty.basemodule.shoppingcart.utils.DinnerUnionShopcartUtil;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShoppingCartTool;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.MealShellVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.trade.entity.TradeMainSubRelation;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.basemodule.trade.message.uniontable.BuffetUnionTradeCancelReq;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.dinner.vo.DinnerConnectTablesVo;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class BuffetSplitUnionManager {
    private TradeDal tradeDal = OperatesFactory.create(TradeDal.class);

    private DinnerConnectTablesVo connectTablesVo;
    private Long mainTradeId;
    private Long subTradeId;
    private TradeVo mainTradeVo = null;
    private TradeVo subTradeVo = null;
    private List<TradeMainSubRelation> tradeMainSubRelationList;
    private List<TradeVo> subTradeList;

    public BuffetUnionTradeCancelReq createUnionTradeSplitReq(List<DinnerConnectTablesVo> tablesVoList) {
        this.connectTablesVo = tablesVoList.get(0);
        BuffetUnionTradeCancelReq splitReq = null;
        if (verifyTablesVo()) {
            try {
                mainTradeVo = tradeDal.findTrade(mainTradeId);
                subTradeVo = tradeDal.findTrade(subTradeId);
                tradeMainSubRelationList = tradeDal.getTradeSubRelationByMainTrade(mainTradeVo.getTrade().getId());
                subTradeList = new ArrayList<>();
                subTradeList.add(subTradeVo);
                if (tradeMainSubRelationList != null) {
                    for (TradeMainSubRelation relation : tradeMainSubRelationList) {
                        if (relation.getSubTradeId().compareTo(subTradeId) != 0) {
                            TradeVo tradeVo = tradeDal.findTrade(relation.getSubTradeId());
                            subTradeList.add(tradeVo);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
                        if (subTradeVo != null && mainTradeVo != null) {
                splitReq = new BuffetUnionTradeCancelReq();             }
            return splitReq;
        } else {
            return null;
        }
    }

    private boolean verifyTablesVo() {
        if (connectTablesVo.tradeMainSubRelationList != null && connectTablesVo.tradeMainSubRelationList.size() == 1) {
            TradeMainSubRelation mainSubRelation = connectTablesVo.tradeMainSubRelationList.get(0);
            mainTradeId = mainSubRelation.getMainTradeId();
            subTradeId = mainSubRelation.getSubTradeId();
            return true;
        } else {
            return false;
        }
    }

        private BuffetUnionTradeCancelReq initDinnertableTradeInfos(TradeVo mainTradeVo, TradeVo subTradeVo) {
        return createBuffetUnionTradeCancelReq(mainTradeVo, subTradeVo, true);
    }


    public BuffetUnionTradeCancelReq createBuffetUnionTradeCancelReq(TradeVo mainTradeVo, TradeVo subTradeVo, boolean calculateCurrentSubTrade) {
        calculateTradePrice(mainTradeVo, subTradeList, subTradeVo, calculateCurrentSubTrade);
        BuffetUnionTradeCancelReq splitReq = new BuffetUnionTradeCancelReq();        BuffetUnionTradeCancelReq.TradeUnionReq tradeUnionReq = new BuffetUnionTradeCancelReq.TradeUnionReq();
        tradeUnionReq.setMainTrade(mainTradeVo.getTrade());
        tradeUnionReq.setSubTrade(subTradeVo.getTrade());
        tradeUnionReq.setMainTradeBuffetPeoples(mainTradeVo.getTradeBuffetPeoples());
        tradeUnionReq.setSubTradeBuffetPeoples(getChangedObj(subTradeVo.getTradeBuffetPeoples()));
        tradeUnionReq.setMainTradeDeposit(mainTradeVo.getTradeDeposit());
        tradeUnionReq.setSubTradeDeposit(subTradeVo.getTradeDeposit());
        tradeUnionReq.setMainTradeTaxs(mainTradeVo.getTradeTaxs());
        tradeUnionReq.setSubTradeTaxs(subTradeVo.getTradeTaxs());
        tradeUnionReq.setSubTradeTable(getTradeTable(subTradeVo));
        tradeUnionReq.setMainMenuTradeItem(getMainMealShellTradeItem(mainTradeVo));
        tradeUnionReq.setSubMenuTradeItem(getMainMealShellTradeItem(subTradeVo));
        tradeUnionReq.setSubTradeInitConfigs(subTradeVo.getTradeInitConfigs());
        splitReq.setTradeRequest(tradeUnionReq);
        splitReq.setInventoryRequest(createInventoryChangeReq());
        return splitReq;
    }

    private List<TradeBuffetPeople> getChangedObj(List<TradeBuffetPeople> tradeBuffetPeoples) {
        List<TradeBuffetPeople> result = new ArrayList<>();
        if (tradeBuffetPeoples != null) {
            for (TradeBuffetPeople buffetPeople : tradeBuffetPeoples) {
                if (buffetPeople.isChanged()) {
                    result.add(buffetPeople);
                }
            }
        }
        return result;
    }

    private TradeItem getMainMealShellTradeItem(TradeVo mainTradeVo) {
        MealShellVo mealShellVo = mainTradeVo.getMealShellVo();
        if (mealShellVo != null) {
            return mealShellVo.getTradeItem();
        }
        return null;
    }

    private TradeTable getTradeTable(TradeVo subTradeVo) {
        List<TradeTable> tradeTableList = subTradeVo.getTradeTableList();
        if (tradeTableList != null) {
            for (TradeTable tradeTable : tradeTableList) {
                if (tradeTable.isValid()) {
                    return tradeTable;
                }
            }
        }
        return null;
    }


    public void calculateTradePrice(TradeVo mainTradeVo, List<TradeVo> subTradeList, TradeVo subTradeVo, boolean calculateCurrentSubTrade) {
        DinnertableTradeInfo mainDinnertableTradeInfo = new DinnertableTradeInfo(mainTradeVo);
        for (TradeVo tradeVo : subTradeList) {
            if (!(calculateCurrentSubTrade && tradeVo.equals(subTradeVo))) {
                calculateTradePrice(mainDinnertableTradeInfo, tradeVo);
            }
        }

        countMealShellVo(mainTradeVo);
        MathShoppingCartTool.mathTotalPrice(mainDinnertableTradeInfo.getItems(), mainTradeVo);
    }

    private void calculateTradePrice(DinnertableTradeInfo mainDinnertableTradeInfo, TradeVo subTradeVo) {
        DinnertableTradeInfo subDinnertableTradeInfo = new DinnertableTradeInfo(subTradeVo);
        List<IShopcartItem> subTradeShopcartItems = new ArrayList<IShopcartItem>(subDinnertableTradeInfo.getItems());
                DinnerUnionShopcartUtil.initSubTradeBatchItem(mainDinnertableTradeInfo, subDinnertableTradeInfo, subTradeShopcartItems);

        if (Utils.isNotEmpty(subTradeShopcartItems)) {
            for (IShopcartItem iShopcartItem : subTradeShopcartItems) {
                if (iShopcartItem.getStatusFlag() == StatusFlag.VALID && iShopcartItem.getShopcartItemType() == ShopcartItemType.SUBBATCH) {
                    ShopcartItemUtils.splitBatchItem((ReadonlyShopcartItem) iShopcartItem);
                }
            }
        }

                countMealShellVo(subTradeVo);

                MathShoppingCartTool.mathTotalPrice(subTradeShopcartItems, subTradeVo);

                mainDinnertableTradeInfo.getItems().addAll(subTradeShopcartItems);
    }


    private void countMealShellVo(TradeVo tradeVo) {
        if (tradeVo.getMealShellVo() != null) {
            BigDecimal totalAmount = BigDecimal.ZERO;
            BigDecimal totalPeopleCount = BigDecimal.ZERO;
            List<TradeBuffetPeople> tradeBuffetPeoples = tradeVo.getTradeBuffetPeoples();
            if (tradeBuffetPeoples != null) {
                for (TradeBuffetPeople buffetPeople : tradeBuffetPeoples) {
                    totalAmount = totalAmount.add(buffetPeople.getPeopleCount().multiply(buffetPeople.getCartePrice()));
                    totalPeopleCount = totalPeopleCount.add(buffetPeople.getPeopleCount());
                }
            }

            tradeVo.getTrade().setTradePeopleCount(totalPeopleCount.intValue());
            TradeItem tradeItem = tradeVo.getMealShellVo().getTradeItem();
            if (tradeItem != null) {
                tradeItem.setActualAmount(totalAmount);
                tradeItem.setAmount(totalAmount);
                tradeItem.setQuantity(totalPeopleCount);
                tradeItem.validateUpdate();
            }
        }
    }

    private InventoryChangeReq createInventoryChangeReq() {
        InventoryChangeReq inventoryChangeReq = new InventoryChangeReq();
        List<TradeItem> tradeItemList = new ArrayList<>();
        List<TradeItem> childItemList = getChildTradeItemList();
        List<TradeItem> mainItemList = getMainTradeItemList();
        if (Utils.isNotEmpty(childItemList)) tradeItemList.addAll(childItemList);
        if (Utils.isNotEmpty(mainItemList)) tradeItemList.addAll(mainItemList);
        inventoryChangeReq.setDeductInventoryItems(InventoryUtils.getInventoryItemReqList(tradeItemList));
        inventoryChangeReq.setReturnInventoryItems(InventoryUtils.getInventoryItemReqList(tradeItemList));
        return inventoryChangeReq;
    }


    private List<TradeItem> getChildTradeItemList() {
        if (connectTablesVo == null && Utils.isEmpty(connectTablesVo.tradeMainSubRelationList))
            return null;
        TradeMainSubRelation tradeMainSubRelation = connectTablesVo.tradeMainSubRelationList.get(0);
        try {
            return tradeDal.listTradeItemByTradeId(tradeMainSubRelation.getSubTradeId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<TradeItem> getMainTradeItemList() {
        if (connectTablesVo == null && Utils.isEmpty(connectTablesVo.tradeMainSubRelationList))
            return null;
        TradeMainSubRelation tradeMainSubRelation = connectTablesVo.tradeMainSubRelationList.get(0);
        try {
            List<TradeItem> tradeItemList = tradeDal.listTradeItemByTradeId(tradeMainSubRelation.getMainTradeId());
            List<TradeItem> tempTradeItemList;

            if (Utils.isNotEmpty(tradeItemList)) {
                Map<Long, BigDecimal> quantityMap = getTradeItemQuantityMap(tradeMainSubRelation.getSubTradeId());
                if (quantityMap == null || quantityMap.size() == 0) return null;
                tempTradeItemList = new ArrayList<>();
                for (TradeItem tradeItem : tradeItemList) {
                    if (quantityMap.containsKey(tradeItem.getId())) {
                        tradeItem.setQuantity(quantityMap.get(tradeItem.getId()));
                        tradeItem.setAmount(tradeItem.getPrice().multiply(tradeItem.getQuantity()));
                        tempTradeItemList.add(tradeItem);
                    }
                }
                return tempTradeItemList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<Long, BigDecimal> getTradeItemQuantityMap(Long subTradeId) {
        try {
            return tradeDal.getTradeItemQuantityMap(subTradeId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public TradeVo getMainTradeVo() {
        return mainTradeVo;
    }

    public TradeVo getSubTradeVo() {
        return subTradeVo;
    }

    public List<TradeMainSubRelation> getTradeMainSubRelationList() {
        return tradeMainSubRelationList;
    }
}
