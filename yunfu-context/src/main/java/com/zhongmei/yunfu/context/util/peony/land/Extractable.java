package com.zhongmei.yunfu.context.util.peony.land;



public interface Extractable<T, E> {

    T extract(E value);
}
