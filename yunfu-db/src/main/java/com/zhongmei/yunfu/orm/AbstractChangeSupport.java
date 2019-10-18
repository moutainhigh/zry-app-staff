package com.zhongmei.yunfu.orm;

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zhongmei.yunfu.orm.DatabaseHelper.ChangeSupportable;
import com.zhongmei.yunfu.db.IEntity;


public abstract class AbstractChangeSupport implements ChangeSupportable {

    private final ThreadLocal<Set<Class<?>>> mThreadLocal;

    private final ThreadLocal<Map<Uri, List<Object>>> mThreadLocalData;
    protected AbstractChangeSupport() {
        mThreadLocal = new ThreadLocal<Set<Class<?>>>();
        mThreadLocalData = new ThreadLocal<Map<Uri, List<Object>>>();     }

    @Override
    public final void addChange(Class<?> tableClass) {
        Set<Class<?>> tables = mThreadLocal.get();
        if (tables == null) {
            tables = new LinkedHashSet<Class<?>>();
            mThreadLocal.set(tables);
        }
        tables.add(tableClass);
    }

        @Override
    public <T extends IEntity<?>> void addChange(Class<?> tableClass, List<T> entities) {
        this.addChange(tableClass);
        List<Object> list = this.createDataContainer(tableClass);
        list.addAll(entities);
    }

    @Override
    public <T extends IEntity<?>> void addChange(Class<?> tableClass, T entity) {
        this.addChange(tableClass);
        List<Object> list = this.createDataContainer(tableClass);
        list.add(entity);
    }

    private List<Object> createDataContainer(Class<?> tableClass) {
        Map<Uri, List<Object>> dataMap = mThreadLocalData.get();
        if (dataMap == null) {
            dataMap = new HashMap<Uri, List<Object>>();
            mThreadLocalData.set(dataMap);
        }
        final Uri tableUri = DBHelperManager.getUri(tableClass);
        List<Object> list = dataMap.get(tableUri);
        if (list == null) {
            list = new ArrayList<Object>();
            dataMap.put(tableUri, list);
        }
        return list;
    }

    private void doDataChangeNotify(Set<Class<?>> tables) {
        Map<Uri, List<Object>> dataMap = mThreadLocalData.get();
        if (dataMap != null) {
            mThreadLocalData.set(null);
            this.notifyChange(dataMap.keySet(), dataMap);
        }
    }

    private void clearLocalData() {
        Map<Uri, List<Object>> dataMap = mThreadLocalData.get();
        if (dataMap != null) {
            dataMap.clear();
        }
    }

        @Override
    public final boolean isChange() {
        Set<Class<?>> tables = mThreadLocal.get();
        if (tables == null) {
            return false;
        }
        return !tables.isEmpty();
    }

    @Override
    public final void clearChange() {
        clearLocalData();
        Set<Class<?>> tables = mThreadLocal.get();
        if (tables == null) {
            return;
        }
        tables.clear();
    }

    @Override
    public final void notifyChange() {
        final Set<Class<?>> tables = mThreadLocal.get();
        if (tables == null) {
            return;
        }
        mThreadLocal.set(null);
        notifyChange(tables.toArray(new Class<?>[0]));

        doDataChangeNotify(tables);     }

    protected abstract void notifyChange(Class<?>... tables);

    protected abstract void notifyChange(Set<Uri> tables, Map<Uri, List<Object>> dataMap);}
