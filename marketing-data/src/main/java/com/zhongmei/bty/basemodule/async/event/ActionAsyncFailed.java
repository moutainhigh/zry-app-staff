package com.zhongmei.bty.basemodule.async.event;

import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;



public class ActionAsyncFailed {

    public AsyncHttpRecord asyncRec;

    public String errorMsg;

        public boolean canRetry = true;

}
