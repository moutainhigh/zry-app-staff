package com.zhongmei.bty.basemodule.shopmanager.closing.presener;

import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;

/**
 * Created by demo on 2018/12/15
 */

public interface IClosingPresener {
    boolean isShowPrePayInfo();//是否显示预付金信息

    TradeOperates createTradeOperates();
}
