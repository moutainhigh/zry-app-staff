package com.zhongmei.bty.basemodule.async.event;

import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;

/**
 * Created by demo on 2018/12/15
 */

public class ActionAsyncFailed {

    public AsyncHttpRecord asyncRec;

    public String errorMsg;

    //默认可以重试
    public boolean canRetry = true;

}
