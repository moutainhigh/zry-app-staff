package com.zhongmei.bty.basemodule.shopmanager.closing.message;

import java.util.List;

import com.zhongmei.bty.basemodule.shopmanager.closing.bean.ClosingAccountRecord;

/**
 * @date:2015年12月17日下午4:56:02
 */
public class CloseHistoryResp {

    List<ClosingAccountRecord> closingAccountRecords;

    public List<ClosingAccountRecord> getClosingAccountRecords() {
        return closingAccountRecords;
    }

    public void setClosingAccountRecords(List<ClosingAccountRecord> closingAccountRecords) {
        this.closingAccountRecords = closingAccountRecords;
    }


}
