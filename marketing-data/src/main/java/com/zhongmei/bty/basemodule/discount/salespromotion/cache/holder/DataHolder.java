package com.zhongmei.bty.basemodule.discount.salespromotion.cache.holder;

import com.zhongmei.bty.basemodule.discount.salespromotion.cache.DataFilter;
import com.zhongmei.yunfu.db.IdEntityBase;

import java.util.Collection;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public interface DataHolder<T extends IdEntityBase> {

    /**
     * 刷新缓存数据
     */
    void refresh() throws Exception;

    /**
     * 返回包含的对象个数
     */
    int getCount();

    /**
     * 获取所有对象
     */
    Collection<T> getAll();

    /**
     * 根据ID获取对象
     */
    T get(Long id);

    /**
     * 返回符合给定过滤规则的对象列表
     */
    List<T> filter(DataFilter<T> filter);

}
