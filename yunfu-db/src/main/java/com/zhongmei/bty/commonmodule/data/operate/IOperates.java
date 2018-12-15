package com.zhongmei.bty.commonmodule.data.operate;

import android.content.Context;

import com.zhongmei.yunfu.context.util.NoProGuard;

/**
 *

 *
 */
public interface IOperates extends NoProGuard {

    /**
     * 为防止直接调用实现类的构造方法而创建，要使用IOperates的实现类请使用
     * {@link OperatesFactory#create(Context, Class)}方法
     *
     * @version: 1.0
     * @date 2015年6月26日
     */
    interface ImplContext {

        Context getContext();
    }
}
