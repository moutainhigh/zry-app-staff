package com.zhongmei.bty.snack.customer;

import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;

import java.util.concurrent.Callable;

/**
 * @since 2018.05.17.
 */
public class ModifyCustomerProcessor extends OpsRequest.SaveDatabaseResponseProcessor<ModifyCustomerResp> {

    @Override
    protected Callable<Void> getCallable(final DatabaseHelper helper, final ModifyCustomerResp resp) throws Exception {
        return new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                DBHelperManager.saveEntities(helper, TradeCustomer.class, resp.tradeCustomers);
                return null;
            }
        };
    }
}
