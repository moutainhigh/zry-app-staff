package com.zhongmei.bty.dinner.table.event;


public class EventInsertDinnerNotice {

    public final String tradeUuid;
    public final Long tableId;

    public EventInsertDinnerNotice(String tradeUuid, Long tableId) {
        this.tradeUuid = tradeUuid;
        this.tableId = tableId;
    }

}
