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

/**
 * 语音通知接口
 */
public class CallDishNotifyOperatesImpl extends AbstractOpeartesImpl implements CallDishNotifyOperates {

    private static final String TAG = CallDishNotifyOperatesImpl.class.getSimpleName();

    public CallDishNotifyOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void notifyVoice(NotifyReq req, ResponseListener<OrderNotify> listener) {
        String url = ServerAddressUtil.getInstance().callDishNotifyUrl();
        OpsRequest.Executor<NotifyReq, OrderNotify> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(OrderNotify.class)
                .responseProcessor(new NotifyProcessor())
                .execute(listener, TAG);
    }

    /**
     * 语音通知请求
     */
    public static class NotifyReq {
        /**
         * 订单UUID
         */
        private String tradeUuid;// 订单UUID

        /**
         * 1=微信叫号,2=IVR叫号
         */
        private int type;

        /**
         * 客户手机号
         */
        private String mobile;

        /**
         * 流水号
         */
        private String serialNo;

        /**
         * 订单号
         */
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

    /**
     * 将创建订单但保存数据保存到数据库的处理器
     *
     * @version: 1.0
     * @date 2015年4月15日
     */
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
