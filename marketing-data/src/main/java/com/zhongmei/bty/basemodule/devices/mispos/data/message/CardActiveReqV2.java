package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;


public class CardActiveReqV2 extends BaseRequest {
    public Long userId;    public String cardNum;    public Long customerId;    public String mobile;    public String nation;     public String country;      public String nationalTelCode;     public Integer source = 1; }
