package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.resp.data.MindTransferResp;

/**
 * Created by demo on 2018/12/15
 */
public class TokenResp extends MindTransferResp<String> {
//    //token字符串
//    private String data;
//
//    public String getData() {
//        return data;
//    }
//
//    public void setData(String data) {
//        this.data = data;
//    }
    /*
    由于父类添加了data字段。会导致Gson解析的时候认为字段重复的异常。目前注释掉data
     */
}
