package com.zhongmei.yunfu.db;

/**
 *

 *
 */
public interface IEntity<ID> extends java.io.Serializable {

    boolean isValid();

    ID pkValue();

    Long verValue();

    /**
     * 效验非空值字段
     *
     * @return true为检验通过 false为不通过
     */
    void checkNullValue();

    /**
     * 效验ID空值字段
     */
    void checkNullIDValue();
}
