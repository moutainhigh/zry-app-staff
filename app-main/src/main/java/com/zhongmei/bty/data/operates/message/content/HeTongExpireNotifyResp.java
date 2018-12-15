package com.zhongmei.bty.data.operates.message.content;

import java.util.List;

import com.zhongmei.bty.commonmodule.database.entity.local.ContractOverdue;

public class HeTongExpireNotifyResp {

    private List<ContractOverdue> ContractOverdueList;

    public List<ContractOverdue> getList() {
        return ContractOverdueList;
    }

    public void setList(List<ContractOverdue> list) {
        this.ContractOverdueList = list;
    }

}
