package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.basemodule.customer.bean.MemberAddressNew;

import java.util.List;


public class MemberAddressResp {

    private List<MemberAddressNew> memberAddresses;

    public List<MemberAddressNew> getMemberAddresses() {
        return memberAddresses;
    }

    public void setMemberAddresses(List<MemberAddressNew> memberAddresses) {
        this.memberAddresses = memberAddresses;
    }
}
