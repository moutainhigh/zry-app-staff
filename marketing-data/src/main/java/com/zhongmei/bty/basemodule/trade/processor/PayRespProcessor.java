package com.zhongmei.bty.basemodule.trade.processor;

import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.basemodule.database.entity.pay.PaymentItemUnionpay;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilegeExtra;
import com.zhongmei.yunfu.db.entity.discount.TradePromotion;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.PaymentItemExtra;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGrouponDish;
import com.zhongmei.bty.basemodule.pay.message.PayResp;
import com.zhongmei.bty.basemodule.print.entity.PrintOperation;
import com.zhongmei.bty.basemodule.trade.entity.TradeDepositPayRelation;
import com.zhongmei.bty.basemodule.trade.entity.TradeStatusLog;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.List;
import java.util.concurrent.Callable;



public class PayRespProcessor extends OpsRequest.SaveDatabaseResponseProcessor<PayResp> {

    protected boolean isSuccessful(ResponseObject<PayResp> response) {
        return ResponseObject.isOk(response) || ResponseObject.isExisted(response) || response.getStatusCode() == ResponseObject.BAINUOCOUPONSPARTOK || (response.getContent() != null && !Utils.isEmpty(response.getContent().getPayments()));
    }

    @Override
    protected Callable<Void> getCallable(final DatabaseHelper helper, final PayResp resp) {
        return new Callable<Void>() {
            @Override
            public Void call()
                    throws Exception {
                DBHelperManager.saveEntities(helper, Trade.class, resp.getTrades());
                DBHelperManager.saveEntities(helper, TradeTable.class, resp.getTradeTables());
                DBHelperManager.saveEntities(helper, Tables.class, resp.getTables());
                DBHelperManager.saveEntities(helper, Payment.class, resp.getPayments());
                DBHelperManager.saveEntities(helper, PaymentItem.class, resp.getPaymentItems());
                DBHelperManager.saveEntities(helper, TradePromotion.class, resp.getTradePromotions());
                DBHelperManager.saveEntities(helper, PaymentItemExtra.class, resp.getPaymentItemExtras());
                DBHelperManager.saveEntities(helper, TradeStatusLog.class, resp.getTradeStatusLogs());
                DBHelperManager.saveEntities(helper, PrintOperation.class, resp.getPrintOperations());
                DBHelperManager.saveEntities(helper, PaymentItemUnionpay.class, resp.getPaymentItemUnionpays());                DBHelperManager.saveEntities(helper, TradeDepositPayRelation.class, resp.getTradeDepositPayRelations());                DBHelperManager.saveEntities(helper, PaymentItemGrouponDish.class, resp.getPaymentItemGrouponDishes());                savePriveleResult(helper, resp.getTradePrivilegeResults());
                return null;
            }
        };
    }

    private void savePriveleResult(DatabaseHelper helper, List<PayResp.PrivilegeRes> privilegeResList) throws Exception {
        if (Utils.isEmpty(privilegeResList)) {
            return;
        }
        for (PayResp.PrivilegeRes privilegeRes : privilegeResList) {
            if (privilegeRes.getTradePrivilegeExtra() == null) {
                continue;
            }
            DBHelperManager.saveEntities(helper, TradePrivilegeExtra.class, privilegeRes.getTradePrivilegeExtra());
        }
    }
}
