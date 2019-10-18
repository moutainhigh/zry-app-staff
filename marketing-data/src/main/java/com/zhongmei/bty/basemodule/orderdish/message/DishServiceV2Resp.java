package com.zhongmei.bty.basemodule.orderdish.message;

import com.zhongmei.bty.basemodule.orderdish.entity.KdsTradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRel;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRelExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.bty.basemodule.orderdish.entity.KdsTradeItemPart;

import java.util.List;

public class DishServiceV2Resp {


    public List<TradeItem> tradeItems;
    public List<TradeExtra> tradeExtras;
    public List<KdsTradeItem> kdsTradeItems;
    public List<KdsTradeItemPart> kdsTradeItemParts;

    public List<TradeItemMainBatchRel> tradeItemMainBatchRels;

    public List<TradeItemMainBatchRelExtra> tradeItemMainBatchRelExtras;
}
