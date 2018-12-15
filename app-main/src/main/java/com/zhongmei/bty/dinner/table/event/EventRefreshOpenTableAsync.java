package com.zhongmei.bty.dinner.table.event;

import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;

import java.util.List;
import java.util.Map;

/**
 * @Date 2016/8/12
 * @Description:刷新预定信息
 */
public class EventRefreshOpenTableAsync {
    public Map<Long, List<AsyncHttpRecord>> mapTableAsyncHttpRecord;
    public Map<String, List<AsyncHttpRecord>> mapTradeAsyncHttpRecord;

    public EventRefreshOpenTableAsync(Map<Long, List<AsyncHttpRecord>> tableAsync, Map<String, List<AsyncHttpRecord>> tradeAsync) {
        this.mapTableAsyncHttpRecord = tableAsync;
        this.mapTradeAsyncHttpRecord = tradeAsync;
    }
}
