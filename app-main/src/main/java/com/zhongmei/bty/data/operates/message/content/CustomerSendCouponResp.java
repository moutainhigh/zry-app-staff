package com.zhongmei.bty.data.operates.message.content;

/**
 * Created by demo on 2018/12/15
 */

public class CustomerSendCouponResp {

    private Long result;

    private Integer code;//1——成功    2——失败

    private String errorMessage;

    public Long getResult() {
        return result;
    }

    public Integer getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
