package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.basemodule.customer.bean.MemberAddressNew;

import java.util.List;

/**
 * @Date： 2017/3/16
 * @Description:会员配送地址返回
 * @Version: 1.0
 */
public class MemberAddressResp {

    private List<MemberAddressNew> memberAddresses;

    public List<MemberAddressNew> getMemberAddresses() {
        return memberAddresses;
    }

    public void setMemberAddresses(List<MemberAddressNew> memberAddresses) {
        this.memberAddresses = memberAddresses;
    }
}
