package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsInfo;

/**
 * 对返回的微信卡券数据的封装
 * Created by demo on 2018/12/15
 */
public class WeiXinCouponsInfoResp {

    private String code;

    private String msg;

    private WeiXinCouponsInfo data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public WeiXinCouponsInfo getData() {
        return data;
    }

    public void setData(WeiXinCouponsInfo data) {
        this.data = data;
    }
}
