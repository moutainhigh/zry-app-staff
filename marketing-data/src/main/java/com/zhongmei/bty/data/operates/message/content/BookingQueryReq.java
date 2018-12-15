package com.zhongmei.bty.data.operates.message.content;

/**
 * Desc
 *
 * @created 2017/8/24
 */
public class BookingQueryReq {

    private final String queryParam;
    private int additionalPageNum = 1; //es 服务页数默认值1
    private int additionalOffset = 0; //es 服务页数
    private int pageSize; //页大小

    public BookingQueryReq(String queryParam) {
        this.queryParam = queryParam;
        this.pageSize = 50;
    }
}
