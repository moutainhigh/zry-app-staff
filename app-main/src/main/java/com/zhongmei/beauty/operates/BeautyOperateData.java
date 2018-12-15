package com.zhongmei.beauty.operates;

import com.zhongmei.bty.basemodule.shopmanager.handover.operators.HandoverOperates;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;


import java.util.HashMap;

/**
 * Created by demo on 2018/12/15
 */

public class BeautyOperateData {
    public static HashMap<Class, Class> sOperataDataMap = new HashMap<>();

    static {
        sOperataDataMap.put(HandoverOperates.class, BeautyHandoverOperatesImpl.class);
        sOperataDataMap.put(TradeOperates.class, BeautyTradeOperatesImpl.class);
    }
}
