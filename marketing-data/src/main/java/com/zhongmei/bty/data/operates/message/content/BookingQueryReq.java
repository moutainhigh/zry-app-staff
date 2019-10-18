package com.zhongmei.bty.data.operates.message.content;


public class BookingQueryReq {

    private final String queryParam;
    private int additionalPageNum = 1;     private int additionalOffset = 0;     private int pageSize;
    public BookingQueryReq(String queryParam) {
        this.queryParam = queryParam;
        this.pageSize = 50;
    }
}
