package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

/**
 * 根据手机号获取顾客信息
 * Created by demo on 2018/12/15
 */
public class CustomerRequest extends BaseRequest {

    public static final long PHONE = 0;
    public static final long WEIX_OPENID = 1;
    public static final long CUSTOMER_ID = 102;

    //0手机号 1微信OPENID 102顾客customerId
    private Long loginType;
    private String loginId; //手机号，微信，顾客ID

    public CustomerRequest(Long loginType, String loginId) {
        super();
        this.loginType = loginType;
        this.loginId = loginId;
    }
}
