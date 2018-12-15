package com.zhongmei.bty.dinner.table.event;

/**
 * 正餐开台后的通知。
 *
 * @version: 1.0
 * @date 2015年10月22日
 * @see EventDinnertableTradeVoNotice
 */
public class EventInsertDinnerNotice {

    public final String tradeUuid;
    public final Long tableId;

    public EventInsertDinnerNotice(String tradeUuid, Long tableId) {
        this.tradeUuid = tradeUuid;
        this.tableId = tableId;
    }

}
