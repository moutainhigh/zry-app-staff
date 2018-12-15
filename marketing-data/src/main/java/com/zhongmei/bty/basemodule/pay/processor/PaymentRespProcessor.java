package com.zhongmei.bty.basemodule.pay.processor;

import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.basemodule.database.entity.pay.PaymentDevice;
import com.zhongmei.bty.basemodule.database.entity.pay.PaymentItemUnionpay;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.PaymentItemExtra;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGroupon;
import com.zhongmei.bty.basemodule.pay.message.PaymentResp;
import com.zhongmei.bty.basemodule.trade.processor.TradeRespProcessor;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;

import java.util.concurrent.Callable;

/**
 * Created by demo on 2018/12/15
 */

public class PaymentRespProcessor extends OpsRequest.SaveDatabaseResponseProcessor<PaymentResp> {
    @Override
    protected Callable<Void> getCallable(final DatabaseHelper helper, final PaymentResp resp) {
        return new Callable<Void>() {

            @Override
            public Void call()
                    throws Exception {
                TradeRespProcessor.saveTradeResp(helper, resp);
                DBHelperManager.saveEntities(helper, Payment.class, resp.getPayments());
                DBHelperManager.saveEntities(helper, PaymentItem.class, resp.getPaymentItems());
                DBHelperManager.saveEntities(helper, PaymentItemExtra.class, resp.getPaymentItemExtras());
                DBHelperManager.saveEntities(helper, PaymentItemUnionpay.class, resp.getPaymentItemUnionpays());
                DBHelperManager.saveEntities(helper, PaymentDevice.class, resp.getPaymentDevices());
                DBHelperManager.saveEntities(helper, PaymentItemGroupon.class, resp.getPaymentItemGroupons());//add 20160927
                DBHelperManager.saveEntities(helper, TradeDeposit.class, resp.getTradeDeposit());//add 20160927
                return null;

            }
        };
    }
}
