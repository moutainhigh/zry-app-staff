package com.zhongmei.bty.basemodule.shopmanager.closing.message;

import com.zhongmei.bty.basemodule.shopmanager.closing.bean.ClosingAccountRecord;

/**
 * 关账返回数据
 *
 * @date:2015年12月14日下午1:54:18
 */
public class CloseBillResp {

    private ClosingAccountRecord closingAccountRecord;

    public ClosingAccountRecord getClosingAccountRecord() {
        return closingAccountRecord;
    }

    public void setClosingAccountRecord(ClosingAccountRecord closingAccountRecord) {
        this.closingAccountRecord = closingAccountRecord;
    }


}
