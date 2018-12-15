package com.zhongmei.beauty.order.event

import com.zhongmei.yunfu.bean.req.CustomerResp

/**
 * 改单时，点单界面初始化完成

 */
class BeautyCustmoerEvent() {

    var customerNew: CustomerResp? = null

    constructor(customerNew: CustomerResp?) : this() {
        this.customerNew = customerNew
    }
}