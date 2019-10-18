package com.zhongmei.bty.basemodule.shopmanager.closing.message;

import java.util.List;

import com.zhongmei.bty.basemodule.shopmanager.closing.bean.ClosingAccountRecord;


public class CloseHistoryResp {

    List<ClosingAccountRecord> closingAccountRecords;

    public List<ClosingAccountRecord> getClosingAccountRecords() {
        return closingAccountRecords;
    }

    public void setClosingAccountRecords(List<ClosingAccountRecord> closingAccountRecords) {
        this.closingAccountRecords = closingAccountRecords;
    }


}
