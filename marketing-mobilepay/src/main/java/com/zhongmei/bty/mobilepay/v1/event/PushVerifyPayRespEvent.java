package com.zhongmei.bty.mobilepay.v1.event;

import com.zhongmei.bty.basemodule.trade.message.VerifyPayResp;

public class PushVerifyPayRespEvent {
    private PushPayMent mPushPayMent;

    public PushVerifyPayRespEvent(PushPayMent mPushPayMent) {

        this.mPushPayMent = mPushPayMent;
    }

    public PushPayMent getmPushPayMent() {
        return mPushPayMent;
    }

    public void setmPushPayMent(PushPayMent mPushPayMent) {
        this.mPushPayMent = mPushPayMent;
    }

    public static class PushPayMent {
        private Integer state;

        private String desc;

        private VerifyPayResp content;

        public Integer getState() {
            return state;
        }

        public void setState(Integer state) {
            this.state = state;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public VerifyPayResp getContent() {
            return content;
        }

        public void setContent(VerifyPayResp content) {
            this.content = content;
        }

    }
}
