package com.zhongmei.bty.dinner.manager;

import java.util.concurrent.Future;

/**
 * Desc
 *
 * @created 2017/8/18
 */
public interface SyncFuture<T> extends Future<T> {

    /**
     * 发起同步阻塞请求
     */
    void sync();

}
