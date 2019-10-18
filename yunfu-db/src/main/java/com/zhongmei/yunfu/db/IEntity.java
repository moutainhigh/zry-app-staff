package com.zhongmei.yunfu.db;


public interface IEntity<ID> extends java.io.Serializable {

    boolean isValid();

    ID pkValue();

    Long verValue();


    void checkNullValue();


    void checkNullIDValue();
}
