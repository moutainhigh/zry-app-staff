package com.zhongmei.bty.basemodule.discount.bean;

import android.text.TextUtils;

import com.zhongmei.yunfu.data.R;
import com.zhongmei.bty.basemodule.discount.enums.WeiXinCardType;
import com.zhongmei.yunfu.context.base.BaseApplication;


public class WeiXinCouponsInfo implements java.io.Serializable {

    private String code;
    private WeiXinCardType card_type;

    private WeiXinCashCouponsInfo cash;
    private WeiXinCodeInfo code_info;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public WeiXinCardType getCard_type() {
        return card_type;
    }

    public void setCard_type(WeiXinCardType card_type) {
        this.card_type = card_type;
    }

    public WeiXinCashCouponsInfo getCash() {
        return cash;
    }

    public void setCash(WeiXinCashCouponsInfo cash) {
        this.cash = cash;
    }

    public WeiXinCodeInfo getCode_info() {
        return code_info;
    }

    public void setCode_info(WeiXinCodeInfo code_info) {
        this.code_info = code_info;
    }

    public static class WeiXinCodeInfo implements java.io.Serializable {

        public static final String CONSUMED = "CONSUMED";
        public static final String EXPIRE = "EXPIRE";
        public static final String DELETE = "DELETE";
        public static final String UNAVAILABLE = "UNAVAILABLE";

        private String errcode;
        private String errmsg;

        private Boolean can_consume;

        public Boolean isCanConsume() {
            if (getErrcode().equals("0") && getCan_consume()) {
                return true;
            } else {
                return false;
            }
        }


        public String getStatus() {
            int id = 0;
            if (TextUtils.isEmpty(getUser_card_status())) {
                return "";
            }
            if (user_card_status.equals(WeiXinCodeInfo.CONSUMED)) {
                id = R.string.coupons_counsumed;
            } else if (user_card_status.equals(WeiXinCodeInfo.EXPIRE)) {
                id = R.string.coupons_expire;
            } else if (user_card_status.equals(WeiXinCodeInfo.DELETE)) {
                id = R.string.coupons_delete;
            } else if (user_card_status.equals(WeiXinCodeInfo.UNAVAILABLE)) {
                id = R.string.coupons_unavailable;
            }
            if (id == 0) {
                return "";
            }
            return BaseApplication.sInstance.getString(id);
        }


        private String user_card_status;

        public String getErrcode() {
            return errcode;
        }

        public void setErrcode(String errcode) {
            this.errcode = errcode;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        public Boolean getCan_consume() {
            return can_consume;
        }

        public void setCan_consume(Boolean can_consume) {
            this.can_consume = can_consume;
        }

        public String getUser_card_status() {
            return user_card_status;
        }

        public void setUser_card_status(String user_card_status) {
            this.user_card_status = user_card_status;
        }
    }


}
