package com.zhongmei.bty.snack.customer;

import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

import java.util.List;


public class ModifyCustomerReq extends BaseRequest {

    public Long tradeId;

    public List<TradeCustomer> tradeCustomers;

}
