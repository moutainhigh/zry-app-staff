package com.zhongmei.bty.basemodule.discount.salespromotion.cache;

import com.zhongmei.yunfu.db.IdEntityBase;


public interface DataFilter<T extends IdEntityBase> {


    boolean accept(T entity);
}
