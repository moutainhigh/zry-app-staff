package com.zhongmei.yunfu.context.session;

public interface Callback {
    void onBindSuccess();

    void onBindError(String message);
}
