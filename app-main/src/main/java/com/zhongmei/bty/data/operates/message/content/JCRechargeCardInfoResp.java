package com.zhongmei.bty.data.operates.message.content;

import java.math.BigDecimal;

/**
 * 金诚消费卡信息
 */
public class JCRechargeCardInfoResp {
    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardKey() {
        return cardKey;
    }

    public void setCardKey(String cardKey) {
        this.cardKey = cardKey;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    String cardNum;//充值卡卡号
    String cardKey;//	充值卡卡密
    BigDecimal amount;    //充值卡面额
    Integer status;//  	状态，0 未使用；1 已发放；2 已使用
}
