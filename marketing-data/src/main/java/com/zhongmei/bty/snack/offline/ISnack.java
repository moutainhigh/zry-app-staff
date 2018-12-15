package com.zhongmei.bty.snack.offline;

//import com.zhongmei.bty.push.PushPacket;

import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;

/**
 * Created by demo on 2018/12/15
 */
public interface ISnack {

    void release();

//    boolean handle(PushPacket pushPacket);

    boolean netWorkAvailable();

    boolean netWorkUnavailable();

    boolean isOfflineTrade(TradeVo tradeVo);

    boolean isOfflineTrade(Trade trade);

    boolean isSnackBusiness(TradeVo tradeVo);

    boolean isSnackBusiness(BusinessType businessType);

    boolean isSnackBusiness(Trade trade);

    boolean isOfflineEnable();
}
