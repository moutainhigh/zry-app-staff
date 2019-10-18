package com.zhongmei.yunfu.orm;

import android.net.Uri;

import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.IEntity;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class DataDistributionCenter {

    private static final Set<DataCallback> callbacks =
            Collections.synchronizedSet(new LinkedHashSet<DataCallback>());


    public static void register(DataCallback callback) {
        callbacks.add(callback);
    }


    public static void unregister(DataCallback callback) {
        callbacks.remove(callback);
    }


    public static <T extends IEntity<?>> void deal(DatabaseHelper helper, Class<T> entityClass,
                                                   List<T> entityList
    ) {
        if (entityClass.equals(Trade.class)) {
            dealTrade(helper, entityList);
        }
    }


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

        public static void notifyDataChange(Set<Uri> tables, final Map<Uri, List<Object>> dataMap) {
        java.util.Iterator<DataCallback> iterableCallback = callbacks.iterator();
        while (iterableCallback.hasNext()) {
            DataCallback callback = iterableCallback.next();
            callback.onChange(tables, dataMap);
        }
    }

        public interface DataCallback {
        public <T extends IEntity<?>> void onTradeChange(List<T> entityList);

        void onChange(Set<Uri> tables, final Map<Uri, List<Object>> dataMap);    }

}
