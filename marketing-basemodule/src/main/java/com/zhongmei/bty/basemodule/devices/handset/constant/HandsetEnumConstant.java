package com.zhongmei.bty.basemodule.devices.handset.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class HandsetEnumConstant {

    public static final int UNKNOWN = -999;//未知类型统一入口

    @IntDef({HandsetEnumConstant.HSLaunchMode.GET_BRACELET_INFO, HandsetEnumConstant.HSLaunchMode.INPUT_PASSWORD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HSLaunchMode {
        int GET_BRACELET_INFO = 1;//获取手环信息
        int INPUT_PASSWORD = 2;//输入密码
    }

}
