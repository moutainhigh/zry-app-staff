package com.zhongmei.bty.basemodule.pay.event;

import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.pay.message.PayResp;



public class PushPayRespEvent {

    private NewPushPayMent mPushPayMent;

    public PushPayRespEvent(NewPushPayMent pushPayMent) {
        this.mPushPayMent = pushPayMent;
    }

    public NewPushPayMent getPushPayMent() {
        return mPushPayMent;
    }

    public String getTradeUuid() {
        String tUuid = "";
        if (this.mPushPayMent != null && this.mPushPayMent.getContent() != null && Utils.isNotEmpty(this.mPushPayMent.getContent().getTrades())) {
            tUuid = this.mPushPayMent.getContent().getTrades().get(0).getUuid();
        }
        return tUuid;
    }

    public static class NewPushPayMent {
        private PayResp content;

        public PayResp getContent() {
            return content;
        }

        public void setContent(PayResp content) {
            this.content = content;
        }
    }
}
