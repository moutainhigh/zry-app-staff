package com.zhongmei.bty.dinner.table.manager;

import com.zhongmei.bty.basemodule.inventory.message.InventoryChangeReq;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryUtils;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.bty.basemodule.shoppingcart.utils.DinnerUnionShopcartUtil;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShopcartItemTool;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeMainSubRelation;
import com.zhongmei.bty.basemodule.trade.message.uniontable.UnionTradeSplitReq;
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

/**
 * Created by demo on 2018/12/15
 * 构建取消联台上行数据
 */

public class DinnerSplitUnionManager {
    private TradeDal tradeDal = OperatesFactory.create(TradeDal.class);

    private DinnerConnectTablesVo connectTablesVo;

    private Long mainTradeId;

    private Long subTradeId;


    public UnionTradeSplitReq createUnionTradeSplitReq(List<DinnerConnectTablesVo> tablesVoList) {
        this.connectTablesVo = tablesVoList.get(0);
        UnionTradeSplitReq splitReq = null;
        TradeVo mainTradeVo = null;
        TradeVo subTradeVo = null;
        List<TradeMainSubRelation> subTradeListForMainTrade = null;
        if (verifyTablesVo()) {
            try {
                mainTradeVo = tradeDal.findTrade(mainTradeId);
                subTradeVo = tradeDal.findTrade(subTradeId);
                subTradeListForMainTrade = tradeDal.getTradeSubRelationByMainTrade(mainTradeId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //生成拆单请求数据包括库存
            if (subTradeVo != null && mainTradeVo != null) {
                if (Utils.isNotEmpty(mainTradeVo.getTradeEarnestMoneys()) && Utils.isNotEmpty(subTradeListForMainTrade) && subTradeListForMainTrade.size() == 1) {
                    return null;
                }
                splitReq = initDinnertableTradeInfos(mainTradeVo, subTradeVo);
            }
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

    //生成拆单请求数据包括库存
    private UnionTradeSplitReq initDinnertableTradeInfos(TradeVo mainTradeVo, TradeVo subTradeVo) {
        UnionTradeSplitReq splitReq = null;
        //复制主单消费税到子单
        BuffetUnionTrade.copyMainTradeTaxToSubTradeTax(mainTradeVo, subTradeVo);
        BuffetUnionTrade.copyMainTradeInitConfigToSubTradeInitConfig(mainTradeVo, subTradeVo);

        DinnertableTradeInfo mainDinnertableTradeInfo = new DinnertableTradeInfo(mainTradeVo);
        DinnertableTradeInfo subDinnertableTradeInfo = new DinnertableTradeInfo(subTradeVo);

        List<IShopcartItem> subTradeShopcartItems = new ArrayList<IShopcartItem>(subDinnertableTradeInfo.getItems());
        //出始化子单批量菜
        DinnerUnionShopcartUtil.initSubTradeBatchItem(mainDinnertableTradeInfo, subDinnertableTradeInfo, subTradeShopcartItems);
        //遍历子单菜品，拆除主单的批量菜
        /* BigDecimal batchItemAmountSum = BigDecimal.ZERO;//子单批量菜总价*/
        if (Utils.isNotEmpty(subTradeShopcartItems)) {
            for (IShopcartItem iShopcartItem : subTradeShopcartItems) {
                if (iShopcartItem.getStatusFlag() == StatusFlag.VALID && iShopcartItem.getShopcartItemType() == ShopcartItemType.SUBBATCH) {
                    ShopcartItemUtils.splitBatchItem((ReadonlyShopcartItem) iShopcartItem);
                }
            }
        }
        //计算主单金额（主单-子单联台菜金额）
        MathShopcartItemTool.mathMainShopcartItemsAmount(mainTradeVo, mainDinnertableTradeInfo.getItems());
        //计算子单金额
        MathShopcartItemTool.mathSubShopcartItemsAmount(subTradeVo, subTradeShopcartItems);
        //生成拆单库存
        splitReq = new UnionTradeSplitReq();//取消联台请求
        UnionTradeSplitReq.TradeUnionSplitSubReq tradeRequest = new UnionTradeSplitReq.TradeUnionSplitSubReq();
        tradeRequest.setMainTrade(mainTradeVo.getTrade());
        tradeRequest.setSubTrade(subTradeVo.getTrade());
        tradeRequest.setMainTradeTaxs(mainTradeVo.getTradeTaxs());
        tradeRequest.setSubTradeTaxs(subTradeVo.getTradeTaxs());
        tradeRequest.setSubTradeInitConfigs(subTradeVo.getTradeInitConfigs());
        splitReq.setTradeRequest(tradeRequest);

        splitReq.setInventoryRequest(createInventoryChangeReq());
        return splitReq;
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
}
