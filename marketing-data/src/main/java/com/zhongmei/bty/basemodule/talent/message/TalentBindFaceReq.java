package com.zhongmei.bty.basemodule.talent.message;

public class TalentBindFaceReq {

    private Long shopID;

    private Long accountId;

    private String faceCode;

    public void setFaceCode(String faceCode) {
        this.faceCode = faceCode;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setShopID(Long shopID) {
        this.shopID = shopID;
    }
}
