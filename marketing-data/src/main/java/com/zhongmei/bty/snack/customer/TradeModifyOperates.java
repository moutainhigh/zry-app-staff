package com.zhongmei.bty.snack.customer;

import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.resp.ResponseListener;

/**
 * @since 2018.05.14.
 */
public class TradeModifyOperates {


    public static TradeModifyOperates newInstance() {
        return new TradeModifyOperates();
    }

    private TradeModifyOperates() {
    }

    public void requestModifyCustomer(Trade trade, CustomerResp customerNew, EcCard ecCard, ResponseListener<ModifyCustomerResp> listener) {
        /*String url = ServerAddressUtil.getInstance().updateCustomer();
        TradeCustomerConvert tradeCustomerConvert = TradeCustomerConvert.newInstance();
        ModifyCustomerReq modifyCustomerReq = tradeCustomerConvert.toModifyCustomerReq(trade, customerNew, ecCard);
        OpsRequest.Executor<ModifyCustomerReq, ModifyCustomerResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(modifyCustomerReq)
                .responseClass(ModifyCustomerResp.class)
                .responseProcessor(new ModifyCustomerProcessor())
                .execute(listener, "requestModifyCustomer");*/
    }
}
