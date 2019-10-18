package com.zhongmei.yunfu.bean.req;

import com.zhongmei.bty.basemodule.customer.message.CustomerListResp;
import com.zhongmei.yunfu.bean.YFPageReq;


public class CustomerListReq extends YFPageReq {

    public CustomerListReq(int pageNo, int pageSize) {
        super(pageNo, pageSize);
    }

    public Long brandId;     public String nameOrMobile;     public String cardNum;     public String openId;     public String groupId;
    public String customerType;
    public String levelId;
    public String refresh;


    public Long queryTime;
}
