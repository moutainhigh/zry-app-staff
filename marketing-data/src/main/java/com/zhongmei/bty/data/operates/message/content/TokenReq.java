package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.Constant;


public class TokenReq {

        private String bucketName = Constant.QINIU_BUCKET_NAME;
        private int expiresTime;

    public TokenReq(int expiresTime) {
        this.expiresTime = expiresTime;
    }

    public String getBucketName() {
        return bucketName;
    }


    public int getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(int expiresTime) {
        this.expiresTime = expiresTime;
    }
}
