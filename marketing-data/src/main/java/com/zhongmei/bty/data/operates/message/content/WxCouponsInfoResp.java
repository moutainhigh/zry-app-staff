package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsInfo;

/**
 * 对返回的微信卡券数据的封装
 * Created by demo on 2018/12/15
 */
public class WxCouponsInfoResp {

    private Long id;

    private WeiXinCouponsInfo weixinCard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WeiXinCouponsInfo getWeixinCard() {
        return weixinCard;
    }

    public void setWeixinCard(WeiXinCouponsInfo weixinCard) {
        this.weixinCard = weixinCard;
    }
}
