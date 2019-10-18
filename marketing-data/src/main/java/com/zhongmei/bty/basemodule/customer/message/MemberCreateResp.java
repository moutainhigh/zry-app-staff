package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.basemodule.customer.message.MemberCreateResp.MemberInfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardBaseResp;
import com.zhongmei.yunfu.bean.req.CustomerCreateResp;


public class MemberCreateResp extends CardBaseResp<MemberInfo> {


    public static class MemberInfo extends CustomerCreateResp {

    }


}
