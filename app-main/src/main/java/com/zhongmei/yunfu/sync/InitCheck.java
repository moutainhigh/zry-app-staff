package com.zhongmei.yunfu.sync;

/**
 * 初始化检查接口
 * Created by demo on 2018/12/15
 */
public interface InitCheck {

    int CHECK_UPDATE_APP = 0x00000001;
    int CHECK_UPDATE_PRINT = 0x00000001 << 1;
    int ERROR_CODE_DEVICE = 0x00000001 << 2;
    int ERROR_CODE_VERSION = 0x00000001 << 3;
    int ERROR_CODE_AUTO_SET = 0x00000001 << 4;
    int ERROR_CODE_AUTO_CONFIG = 0x00000001 << 5;
    int ERROR_CODE_OTHER = 0x00000001 << 6;
    int ERROR_VERSIONS_MATCHING = 0x00000001 << 7;
    int ERROR_VERSIONS_FAILED = 0x00000001 << 8;
    int ERROR_VERSIONS_NOT_CONFIG = 0x00000001 << 9;
    int ERROR_AUTH = 0x00000001 << 10;
    int ERROR_CONTRACT_RENEWAL = 0x00000001 << 11;
    int ERROR_NETWORK = 0x00000001 << 12;

    enum CheckType {
        ALL,
        BaseInitCheck,
        SyncInitCheck,
        ConfigInitCheck,
        CacheInitCheck
    }

    CheckType getType();

    //在子线程中调用
    void check(InitCheckCallback callback);

    void cancel();

    int getErrorCode(String error);

    String getErrorMessage(String error);
}
