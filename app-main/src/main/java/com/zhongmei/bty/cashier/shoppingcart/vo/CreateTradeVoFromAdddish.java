package com.zhongmei.bty.cashier.shoppingcart.vo;

import com.google.gson.Gson;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.dinner.table.bean.AddItemJsonBean;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeVo;
import com.zhongmei.bty.basemodule.orderdish.entity.AddItemRecord;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.basemodule.shoppingcart.utils.CreateTradeTool;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.orderdish.bean.AddItemVo;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class CreateTradeVoFromAdddish {
    public static TradeVo create(AddItemVo addItemVo, DinnertableTradeVo dinnertableTradeVo) {
        List<AddItemRecord> records = addItemVo.getmAddItemRecords();
        List<TradeItem> tradeItems = new ArrayList<>();
        List<TradeItemProperty> tradeItemProperties = new ArrayList<>();
        if (records != null) {
            for (AddItemRecord record : records) {
                String tradeItemStr = record.getMockTradeItemData();
                Gson gson = new Gson();
                AddItemJsonBean addItemJsonBean = gson.fromJson(tradeItemStr, AddItemJsonBean.class);

                for (TradeItem tradeItem : addItemJsonBean.getTradeItems()) {
                    tradeItem.setTradeId(record.getTradeId());
                }
                if (Utils.isNotEmpty(addItemJsonBean.getTradeItems())) {
                    tradeItems.addAll(addItemJsonBean.getTradeItems());
                }
                if (Utils.isNotEmpty(addItemJsonBean.getTradeItemProperties())) {
                    tradeItemProperties.addAll(addItemJsonBean.getTradeItemProperties());
                }

            }
        }
        TradeVo tradeVo = new TradeVo();
        CreateTradeTool.buildMainTradeVo(tradeVo);
        tradeVo.getTrade().setId(0L);

                Map<String, List<TradeItemProperty>> tradeItemPropertyListMap = new HashMap<String, List<TradeItemProperty>>();

        for (TradeItemProperty property : tradeItemProperties) {
            property.setChanged(true);
            if (tradeItemPropertyListMap.get(property.getTradeItemUuid()) != null) {
                tradeItemPropertyListMap.get(property.getTradeItemUuid()).add(property);
            } else {
                List<TradeItemProperty> itemProperties = new ArrayList<TradeItemProperty>();
                itemProperties.add(property);
                tradeItemPropertyListMap.put(property.getTradeItemUuid(), itemProperties);
            }
        }

        List<TradeItemVo> tiVoList = new ArrayList<TradeItemVo>();
        TradeTable tradeTable = dinnertableTradeVo.getTradeVo().getTradeTableList().get(0);
        for (TradeItem tradeItem : tradeItems) {
                        if (tradeTable != null) {
                tradeItem.setTradeTableUuid(tradeTable.getUuid());
                tradeItem.setTradeTableId(tradeTable.getId());
            }

            TradeItemVo tiVo = new TradeItemVo();
            tradeItem.setChanged(true);
            tiVo.setTradeItem(tradeItem);

                        List<TradeItemProperty> tips = tradeItemPropertyListMap.get(tradeItem.getUuid());
            tiVo.setTradeItemPropertyList(tips);
            tiVoList.add(tiVo);
        }

        tradeVo.setTradeItemList(tiVoList);
        return tradeVo;
    }

    public static List<IShopcartItem> getShopcartItems(TradeVo tradeVo) {
        final List<IShopcartItem> shopcartItems = DinnertableTradeInfo.buildShopcartItem(tradeVo);
        return shopcartItems;
    }

}
