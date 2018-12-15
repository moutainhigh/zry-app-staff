package com.zhongmei.yunfu.orm;

import android.net.Uri;

import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.IEntity;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 下行数据分发中心
 * Created by demo on 2018/12/15
 */

public class DataDistributionCenter {

    private static final Set<DataCallback> callbacks =
            Collections.synchronizedSet(new LinkedHashSet<DataCallback>());

    /**
     * 注册对数据库数据变更的监听。 在不需要监听时需要调用
     * 方法取消注册，否则会引起资源泄漏。
     */
    public static void register(DataCallback callback) {
        callbacks.add(callback);
    }

    /**
     * 取消对数据库数据变更的监听。
     */
    public static void unregister(DataCallback callback) {
        callbacks.remove(callback);
    }

    /**
     * 数据处理类
     */
    public static <T extends IEntity<?>> void deal(DatabaseHelper helper, Class<T> entityClass,
                                                   List<T> entityList
    ) {
        if (entityClass.equals(Trade.class)) {
            dealTrade(helper, entityList);
        }
    }

    /**
     * 处理trade数据
     */
    private static <T extends IEntity<?>> void dealTrade(DatabaseHelper helper,
                                                         List<T> entityList
    ) {
        if (entityList == null || entityList.isEmpty()) {
            return;
        }
        java.util.Iterator<DataCallback> iterableCallback = callbacks.iterator();
        while (iterableCallback.hasNext()) {
            DataCallback callback = iterableCallback.next();
            callback.onTradeChange(entityList);
        }
    }

    //add 20170316   数据库监听回调返回原始数据
    public static void notifyDataChange(Set<Uri> tables, final Map<Uri, List<Object>> dataMap) {
        java.util.Iterator<DataCallback> iterableCallback = callbacks.iterator();
        while (iterableCallback.hasNext()) {
            DataCallback callback = iterableCallback.next();
            callback.onChange(tables, dataMap);
        }
    }

    //add 20170316   数据库监听回调返回原始数据
    public interface DataCallback {
        public <T extends IEntity<?>> void onTradeChange(List<T> entityList);

        void onChange(Set<Uri> tables, final Map<Uri, List<Object>> dataMap);//add 20170316 数据库监听回调返回原始数据
    }

}
