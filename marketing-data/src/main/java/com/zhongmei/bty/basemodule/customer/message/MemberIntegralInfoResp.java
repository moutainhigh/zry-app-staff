package com.zhongmei.bty.basemodule.customer.message;

import java.util.List;

import com.zhongmei.bty.basemodule.customer.bean.IntegralRecord;

public class MemberIntegralInfoResp {

    private List<IntegralRecord> memberIntegral;

    public List<IntegralRecord> getMemberIntegral() {
        return memberIntegral;
    }

    public void setMemberIntegral(List<IntegralRecord> memberIntegral) {
        this.memberIntegral = memberIntegral;
    }


}
