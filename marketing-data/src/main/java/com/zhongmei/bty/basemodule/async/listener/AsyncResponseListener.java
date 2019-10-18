package com.zhongmei.bty.basemodule.async.listener;

import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.yunfu.resp.ResponseListener;


public interface AsyncResponseListener<T> extends ResponseListener<T> {

    public AsyncHttpRecord getAsyncRec();

    public void setAsyncRec(AsyncHttpRecord asyncRec);
}
