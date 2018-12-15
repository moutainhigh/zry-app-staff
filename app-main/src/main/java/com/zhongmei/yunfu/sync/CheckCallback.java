package com.zhongmei.yunfu.sync;

public interface CheckCallback {

    int CHECK_RESULT_RESTART = 0;
    int CHECK_RESULT_SUCCESS = 1;

    void onCheckStart();

    void onCheckRunning(String hint, int process);

    void onCheckOver(int type, String hint, Throwable err);

}
