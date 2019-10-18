package com.zhongmei.bty.data.operates.impl;

import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;

import java.util.concurrent.Callable;

import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.data.db.common.OrderNotify;
import com.zhongmei.bty.data.operates.CallDishNotifyOperates;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.http.OpsRequest.SaveDatabaseResponseProcessor;


public class CallDishNotifyOperatesImpl extends AbstractOpeartesImpl implements CallDishNotifyOperates {

    private static final String TAG = CallDishNotifyOperatesImpl.class.getSimpleName();

    public CallDishNotifyOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void notifyVoice(NotifyReq req, ResponseListener<OrderNotify> listener) {

    }


    public static class NotifyReq {

        private String tradeUuid;

        private int type;


        private String mobile;


        private String serialNo;


        private String tradeNo;

        public String getTradeUuid() {
            return tradeUuid;
        }

        public void setTradeUuid(String tradeUuid) {
            this.tradeUuid = tradeUuid;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(String serialNo) {
            this.serialNo = serialNo;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

    }


    private static class NotifyProcessor extends SaveDatabaseResponseProcessor<OrderNotify> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final OrderNotify resp) {
            return new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    DBHelperManager.saveEntities(helper, OrderNotify.class, resp);
                    return null;
                }
            };
        }

    }
}
