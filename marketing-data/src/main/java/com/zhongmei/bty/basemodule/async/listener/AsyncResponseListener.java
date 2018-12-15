package com.zhongmei.bty.basemodule.async.listener;

import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.yunfu.resp.ResponseListener;

/**
 * Created by demo on 2018/12/15
 */
public interface AsyncResponseListener<T> extends ResponseListener<T> {

    public AsyncHttpRecord getAsyncRec();

    public void setAsyncRec(AsyncHttpRecord asyncRec);
}
