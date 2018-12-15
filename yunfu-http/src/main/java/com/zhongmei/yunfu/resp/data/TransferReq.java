package com.zhongmei.yunfu.resp.data;

/**
 * 透传接口通用请求体
 * Created by demo on 2018/12/15
 */
public class TransferReq<T> {

    private String url;

    private T postData;

    private String method;

    public TransferReq() {
    }

    public TransferReq(String url, T postData) {
        this.url = url;
        this.postData = postData;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public T getPostData() {
        return postData;
    }

    public void setPostData(T postData) {
        this.postData = postData;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
