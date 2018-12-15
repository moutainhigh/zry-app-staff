package com.zhongmei.yunfu.bean;


import java.util.List;

public class YFResponseList<T> extends YFResponse<List<T>> {

    private YFPageResp page;

    public YFPageResp getPage() {
        return page;
    }
}
