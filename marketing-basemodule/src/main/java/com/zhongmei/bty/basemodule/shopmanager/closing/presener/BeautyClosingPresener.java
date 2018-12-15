package com.zhongmei.bty.basemodule.shopmanager.closing.presener;

import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.commonmodule.data.operate.BeautyOperatesFactory;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;

/**
 * Created by demo on 2018/12/15
 */

public class BeautyClosingPresener implements IClosingPresener {
    @Override
    public boolean isShowPrePayInfo() {
        return false;
    }

    @Override
    public TradeOperates createTradeOperates() {
        return BeautyOperatesFactory.create(TradeOperates.class);
    }
}
