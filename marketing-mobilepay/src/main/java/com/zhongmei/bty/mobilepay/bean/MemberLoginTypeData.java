package com.zhongmei.bty.mobilepay.bean;



public class MemberLoginTypeData {
    public static final int UI_TYPE_MOBILE = 0;

    public static final int UI_TYPE_CARD = 1;

    private int uiType;

    private Integer memberTypeImgRes;

    private String memberTypeTextRes;

    public int getUiType() {
        return uiType;
    }

    public void setUiType(int uiType) {
        this.uiType = uiType;
    }

    public Integer getMemberTypeImgRes() {
        return memberTypeImgRes;
    }

    public void setMemberTypeImgRes(Integer memberTypeImgRes) {
        this.memberTypeImgRes = memberTypeImgRes;
    }

    public String getMemberTypeTextRes() {
        return memberTypeTextRes;
    }

    public void setMemberTypeTextRes(String memberTypeTextRes) {
        this.memberTypeTextRes = memberTypeTextRes;
    }
}
