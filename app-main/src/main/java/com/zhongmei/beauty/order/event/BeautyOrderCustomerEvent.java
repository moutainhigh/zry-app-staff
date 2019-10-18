package com.zhongmei.beauty.order.event;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.zhongmei.yunfu.bean.req.CustomerResp;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class BeautyOrderCustomerEvent {

    @IntDef({EventFlag.LOGIN,
            EventFlag.EXIT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface EventFlag {
        int LOGIN = 1;
        int EXIT = 2;
    }

    @EventFlag
    public int mEventFlag;

    public CustomerResp mCustomerNew;

    public BeautyOrderCustomerEvent(@NotNull @EventFlag int eventFlag, @Nullable CustomerResp customerNew) {
        this.mEventFlag = eventFlag;
        this.mCustomerNew = customerNew;
    }
}
