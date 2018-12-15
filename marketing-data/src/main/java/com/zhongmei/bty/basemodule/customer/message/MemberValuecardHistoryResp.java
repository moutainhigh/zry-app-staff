package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.yunfu.bean.req.CustomerStoredBalanceResp;

import java.util.List;

public class MemberValuecardHistoryResp {

    private List<CustomerStoredBalanceResp> valuecardHistorys;

    public List<CustomerStoredBalanceResp> getValuecardHistorys() {
        return valuecardHistorys;
    }

    public void setValuecardHistorys(List<CustomerStoredBalanceResp> valuecardHistorys) {
        this.valuecardHistorys = valuecardHistorys;
    }

}
