package com.zhongmei.yunfu.context.util.peony.land;

/**
 * Created by demo on 2018/12/15
 * 通用转换接口 E 转换成 T
 */

public interface Extractable<T, E> {

    T extract(E value);
}
