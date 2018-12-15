package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.Constant;

/**
 * Created by demo on 2018/12/15
 */
public class TokenReq {

    //七牛空间名称，固定不变
    private String bucketName = Constant.QINIU_BUCKET_NAME;
    //token有效时间(小时)
    private int expiresTime;

    public TokenReq(int expiresTime) {
        this.expiresTime = expiresTime;
    }

    public String getBucketName() {
        return bucketName;
    }

//    public void setBucketName(String bucketName) {
//        this.bucketName = bucketName;
//    }

    public int getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(int expiresTime) {
        this.expiresTime = expiresTime;
    }
}
