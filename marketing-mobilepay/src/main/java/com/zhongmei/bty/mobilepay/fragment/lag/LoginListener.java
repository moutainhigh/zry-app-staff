package com.zhongmei.bty.mobilepay.fragment.lag;

import com.zhongmei.yunfu.bean.req.CustomerResp;

/**
 * Created by demo on 2018/12/15
 */

public interface LoginListener {
    // 虚拟卡登录成功
    public void onPhoneSuccess(CustomerResp customer);

    // 实体卡登录成功
    public void onCardSuccess(CustomerResp customer);
}
