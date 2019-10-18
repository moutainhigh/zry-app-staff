package com.zhongmei.bty.basemodule.discount.salespromotion.cache.holder;

import android.net.Uri;

import com.j256.ormlite.dao.Dao;
import com.zhongmei.bty.basemodule.discount.salespromotion.cache.DataObserver;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.IdEntityBase;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



public abstract class BasicHolder<T extends IdEntityBase> extends AbstractHolder<T> {

    private final Class<T> classType;
    private final Uri uri;
    private DatabaseHelper.DataChangeObserver observer;
    private Map<Long, T> datas;

    BasicHolder(Class<T> classType) {
        this.classType = classType;
        uri = DBHelperManager.getUri(classType);
        datas = new LinkedHashMap<>();
    }

    public void registerObserver() {
        if (observer == null) {
            observer = new DataObserver(this, uri);
        }
        DatabaseHelper.Registry.register(observer);
    }

    public void unregisterObserver() {
        if (observer != null) {
            DatabaseHelper.Registry.unregister(observer);
            observer = null;
        }
    }

    @Override
    protected Map<Long, T> getDatas() {
        return datas;
    }

    @Override
    public synchronized void refresh(DatabaseHelper helper) throws Exception {
        try {
            Dao<T, Long> dao = helper.getDao(classType);
            List<T> list = query(helper, dao);
            renewDatas(cache(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Map<Long, T> cache(List<T> list) {
        Map<Long, T> map = new LinkedHashMap<Long, T>();
        for (T entity : list) {
            cacheEntity(map, entity);
        }
        return map;
    }

    private void cacheEntity(Map<Long, T> map, T entity) {
        map.put(entity.getId(), entity);
    }

    private void renewDatas(Map<Long, T> theNew) {
        datas = theNew;
    }

    protected abstract List<T> query(DatabaseHelper helper, Dao<T, Long> dao) throws Exception;
}
