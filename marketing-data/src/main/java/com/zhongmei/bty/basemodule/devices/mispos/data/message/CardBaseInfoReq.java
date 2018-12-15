package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

/**
 * @Date： 2017/5/10
 * @Description:卡基本信息请求
 * @Version: 1.0
 */
public class CardBaseInfoReq extends BaseRequest {

    private String cardNum;

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }
}
