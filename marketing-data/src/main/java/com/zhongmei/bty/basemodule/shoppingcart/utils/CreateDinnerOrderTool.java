package com.zhongmei.bty.basemodule.shoppingcart.utils;

import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date：2015年9月14日 下午7:47:32
 * @Description: 用于操作正餐订单数据信息
 * @Version: 1.0
 */
public class CreateDinnerOrderTool {

    /**
     * @Title: openTable
     * @Description: 添加开台信息
     * @Param @param mTradeVo
     * @Param @param mTradeTable TODO
     * @Return void 返回类型
     */
    public static void openTable(TradeVo mTradeVo, TradeTable mTradeTable) {
        List<TradeTable> mTradetables = mTradeVo.getTradeTableList();
        if (mTradetables == null) {
            mTradetables = new ArrayList<TradeTable>();
            mTradeVo.setTradeTableList(mTradetables);
        }
        Boolean isHave = false;
        for (int i = 0; i < mTradetables.size(); i++) {
            //判断是新加桌台还是修改桌台数据信息
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
