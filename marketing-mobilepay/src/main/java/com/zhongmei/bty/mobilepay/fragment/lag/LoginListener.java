package com.zhongmei.bty.mobilepay.fragment.lag;

import com.zhongmei.yunfu.bean.req.CustomerResp;



public interface LoginListener {
        public void onPhoneSuccess(CustomerResp customer);

        public void onCardSuccess(CustomerResp customer);
}
