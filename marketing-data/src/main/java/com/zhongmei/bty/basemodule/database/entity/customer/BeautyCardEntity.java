package com.zhongmei.bty.basemodule.database.entity.customer;

import java.io.Serializable;

/**
 * Created by dingzb on 2019/5/28.
 */

public class BeautyCardEntity implements Serializable {

    private long cardId;

    private String cardNo;

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
}
