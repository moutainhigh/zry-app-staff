package com.zhongmei.bty.dinner.table.bean;

import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;

import java.util.List;


/**
 * @Date 2016/10/20
 * @Description:add_item_record mock_trade_item_data字段封装
 */
public class AddItemJsonBean {
    List<TradeItem> tradeItems;
    List<TradeItemProperty> tradeItemProperties;

    public List<TradeItem> getTradeItems() {
        return tradeItems;
    }

    public void setTradeItems(List<TradeItem> tradeItems) {
        this.tradeItems = tradeItems;
    }

    public List<TradeItemProperty> getTradeItemProperties() {
        return tradeItemProperties;
    }

    public void setTradeItemProperties(List<TradeItemProperty> tradeItemProperties) {
        this.tradeItemProperties = tradeItemProperties;
    }
}
