package com.zhongmei.bty.basemodule.shoppingcart.utils;

import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;

import java.util.ArrayList;
import java.util.List;


public class CreateDinnerOrderTool {


    public static void openTable(TradeVo mTradeVo, TradeTable mTradeTable) {
        List<TradeTable> mTradetables = mTradeVo.getTradeTableList();
        if (mTradetables == null) {
            mTradetables = new ArrayList<TradeTable>();
            mTradeVo.setTradeTableList(mTradetables);
        }
        Boolean isHave = false;
        for (int i = 0; i < mTradetables.size(); i++) {
                        TradeTable table = mTradetables.get(i);
            if (mTradeTable.getUuid().equals(table.getUuid())) {
                mTradetables.add(i, table);
                isHave = true;
                break;
            }
        }
        if (!isHave) {
            mTradetables.add(mTradeTable);
        }
    }


}
