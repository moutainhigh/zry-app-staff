package com.zhongmei.beauty.operates.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

/**
 * 请求
 *
 * @date 2018/6/14
 */
public class BeautyCardServiceHistoryReq extends BaseRequest {

    public Integer source = 1;

    public Long userId;

    /**
     * 卡号
     */
    public String cardNo;
}
