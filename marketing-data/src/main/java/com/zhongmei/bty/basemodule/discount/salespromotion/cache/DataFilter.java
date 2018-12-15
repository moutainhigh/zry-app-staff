package com.zhongmei.bty.basemodule.discount.salespromotion.cache;

import com.zhongmei.yunfu.db.IdEntityBase;

/**
 * Created by demo on 2018/12/15
 */
public interface DataFilter<T extends IdEntityBase> {

    /**
     * 返回true表示此entity是符合条件的
     */
    boolean accept(T entity);
}
