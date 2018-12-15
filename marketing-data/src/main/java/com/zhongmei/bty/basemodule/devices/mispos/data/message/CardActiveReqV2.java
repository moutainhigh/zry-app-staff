package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

/**
 * 实体卡激活上行数据
 * Created by demo on 2018/12/15
 */
public class CardActiveReqV2 extends BaseRequest {
    public Long userId;// 用户ID
    public String cardNum;// 卡号
    public Long customerId;// 会员ID
    public String mobile;// 手机号
    public String nation; //国家英文名称
    public String country;  // 国家中文名称
    public String nationalTelCode; // 电话国际区码
    public Integer source = 1; // 默认为1
}
