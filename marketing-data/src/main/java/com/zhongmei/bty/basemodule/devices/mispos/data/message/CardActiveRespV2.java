package com.zhongmei.bty.basemodule.devices.mispos.data.message;

/**
 * 实体卡激活下行数据
 * <p>
 * Created by demo on 2018/12/15
 */
public class CardActiveRespV2 extends CardBaseResp<CardActiveRespV2.ActiveRespV2> {

    public class ActiveRespV2 {
        public Long customerId;

        public String cardNum;


    }
}
