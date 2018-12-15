package com.zhongmei.bty.basemodule.orderdish.message;

import com.zhongmei.bty.basemodule.orderdish.entity.KdsTradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRel;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRelExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.bty.basemodule.orderdish.entity.KdsTradeItemPart;

import java.util.List;

public class DishServiceV2Resp {

    //public static final int OK = 1000;

    public List<TradeItem> tradeItems;
    public List<TradeExtra> tradeExtras;
    public List<KdsTradeItem> kdsTradeItems;
    public List<KdsTradeItemPart> kdsTradeItemParts;
    /**
     * 联台批次关系表
     */
    public List<TradeItemMainBatchRel> tradeItemMainBatchRels;
    /**
     * 联台菜品相关属性批次关系表
     */
    public List<TradeItemMainBatchRelExtra> tradeItemMainBatchRelExtras;
}
