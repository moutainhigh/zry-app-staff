package com.zhongmei.bty.basemodule.trade.processor;

import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.basemodule.trade.entity.TradeInvoiceNo;
import com.zhongmei.bty.basemodule.trade.message.GetTaxNoResp;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;

import java.util.concurrent.Callable;

/**
 * Created by demo on 2018/12/15
 * 查询消费税号后保存工具
 */

public class GetTaxNoProcessor extends OpsRequest.SaveDatabaseResponseProcessor<GetTaxNoResp> {

    @Override
    protected Callable<Void> getCallable(final DatabaseHelper helper, final GetTaxNoResp resp) throws Exception {
        return new Callable<Void>() {

            @Override
            public Void call()
                    throws Exception {
                saveTradeResp(helper, resp);
                return null;
            }
        };
    }

    public static void saveTradeResp(DatabaseHelper helper, GetTaxNoResp resp)
            throws Exception {
        DBHelperManager.saveEntities(helper, TradeInvoiceNo.class, resp.getCode());
    }
}
