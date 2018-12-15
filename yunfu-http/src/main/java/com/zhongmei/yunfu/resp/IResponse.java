package com.zhongmei.yunfu.resp;

public interface IResponse {

    boolean isOk();

    Integer getCode();

    String getMessage();
}
