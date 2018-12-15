package com.zhongmei.analysis.check;

/**
 * Created by demo on 2018/12/15
 */

public interface IDataCheck<T> {
    boolean isDataValid(T data);
}
