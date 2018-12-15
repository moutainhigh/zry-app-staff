package com.zhongmei.beauty.operates.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

/**
 * 请求
 *
 * @date 2018/6/14
 */
public class BeautyCardServiceReq extends BaseRequest {

    public Integer source = 1;

    /**
     * 用户ID
     */
    public Long userId;
    /**
     * 顾客id
     */
    public Long customerId;
}
