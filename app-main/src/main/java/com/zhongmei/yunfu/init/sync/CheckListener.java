package com.zhongmei.yunfu.init.sync;

public interface CheckListener {

    void update(Check check, String hint);

    void onSuccess(Check check, String hint);

    void onError(Check check, String errorMsg, Throwable err);
}
