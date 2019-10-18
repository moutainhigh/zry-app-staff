package com.zhongmei.bty.basemodule.discount.salespromotion.cache.holder;

import com.zhongmei.bty.basemodule.discount.salespromotion.cache.DataFilter;
import com.zhongmei.yunfu.db.IdEntityBase;

import java.util.Collection;
import java.util.List;



public interface DataHolder<T extends IdEntityBase> {


    void refresh() throws Exception;


    int getCount();


    Collection<T> getAll();


    T get(Long id);


    List<T> filter(DataFilter<T> filter);

}
