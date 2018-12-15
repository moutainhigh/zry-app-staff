package com.zhongmei.bty.dinner.table.event;

import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeVo;

/**
 * 用于通知桌台订单数据。在收到{@link EventInsertDinnerNotice}后将回复此通知
 *
 * @version: 1.0
 * @date 2015年10月22日
 * @see EventInsertDinnerNotice
 */
public class EventDinnertableTradeVoNotice {
    public final DinnertableTradeVo dinnertableTradeVo;

    public EventDinnertableTradeVoNotice(DinnertableTradeVo dinnertableTradeVo) {
        this.dinnertableTradeVo = dinnertableTradeVo;
    }

}
