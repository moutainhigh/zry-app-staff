package com.zhongmei.bty.dinner.action;

import com.zhongmei.bty.entity.enums.CommercialStatus;

/**
 * Created by demo on 2018/12/15
 */
public class ActionContractStatus {

    private CommercialStatus status;

    public ActionContractStatus(CommercialStatus status) {
        this.status = status;
    }

    public CommercialStatus getStatus() {
        return status;
    }

    public void setStatus(CommercialStatus status) {
        this.status = status;
    }
}
