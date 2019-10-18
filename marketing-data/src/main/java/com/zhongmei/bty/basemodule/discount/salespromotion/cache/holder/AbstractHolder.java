package com.zhongmei.bty.basemodule.discount.salespromotion.cache.holder;

import com.zhongmei.bty.basemodule.discount.salespromotion.cache.DataFilter;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.IdEntityBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;



public abstract class AbstractHolder<T extends IdEntityBase> implements DataHolder<T> {

    @Override
    public synchronized void refresh() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            refresh(helper);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public int getCount() {
        return getDatas() == null ? 0 : getDatas().size();
    }

    @Override
    public Collection<T> getAll() {
        return getDatas() == null ? new ArrayList<T>() : getDatas().values();
    }

    @Override
    public T get(Long id) {
        return getDatas() == null ? null : getDatas().get(id);
    }

    @Override
    public List<T> filter(DataFilter<T> filter) {
        List<T> resultList = new ArrayList<T>();
        if (filter == null) {
            resultList.addAll(getAll());
        } else {
            for (T entity : getAll()) {
                if (filter.accept(entity)) {
                    resultList.add(entity);
                }
            }
        }
        return resultList;
    }

    protected abstract Map<Long, T> getDatas();

    protected abstract void refresh(DatabaseHelper helper) throws Exception;
}
