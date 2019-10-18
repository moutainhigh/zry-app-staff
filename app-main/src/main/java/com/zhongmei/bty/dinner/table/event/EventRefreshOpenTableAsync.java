package com.zhongmei.bty.dinner.table.event;

import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;

import java.util.List;
import java.util.Map;


public class EventRefreshOpenTableAsync {
    public Map<Long, List<AsyncHttpRecord>> mapTableAsyncHttpRecord;
    public Map<String, List<AsyncHttpRecord>> mapTradeAsyncHttpRecord;

    public EventRefreshOpenTableAsync(Map<Long, List<AsyncHttpRecord>> tableAsync, Map<String, List<AsyncHttpRecord>> tradeAsync) {
        this.mapTableAsyncHttpRecord = tableAsync;
        this.mapTradeAsyncHttpRecord = tradeAsync;
    }
}
