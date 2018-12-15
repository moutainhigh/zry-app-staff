package com.zhongmei.beauty.order.event;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.zhongmei.yunfu.bean.req.CustomerResp;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 开单登录 eventbus
 * <p>
 * LOGIN 的时候有Customer对象
 * EXIT 的时候没有Customer对象
 * <p>
 * Created by demo on 2018/12/15
 */
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
