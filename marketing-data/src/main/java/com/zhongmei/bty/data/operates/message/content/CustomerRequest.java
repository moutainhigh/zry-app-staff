package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;


public class CustomerRequest extends BaseRequest {

    public static final long PHONE = 0;
    public static final long WEIX_OPENID = 1;
    public static final long CUSTOMER_ID = 102;

        private Long loginType;
    private String loginId;
    public CustomerRequest(Long loginType, String loginId) {
        super();
        this.loginType = loginType;
        this.loginId = loginId;
    }
}
