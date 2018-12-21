package com.zhongmei.bty.basemodule.customer.bean

import java.math.BigDecimal

class CustomerExpenseRecordResp {

    var id: Long? = null//序号
    var modifyDateTime: Long = 0// 修改时间
    var tradeType: String? = null //交易类型 1:SELL:售货 2:REFUND:退货 3:REPAY:反结账 4:REPAY_FOR_REFUND:反结账退货
    var tradeId: Long? = null
    var tradeNo: String? = null //订单编号
    var tradeValue: BigDecimal? = null//交易值
    var userId: String? = null// 操作员

}
