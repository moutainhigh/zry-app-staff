package com.zhongmei.bty.basemodule.devices.liandipos;


import com.landicorp.model.ResponseData;
import com.zhongmei.bty.basemodule.devices.liandipos.contants.LDResponseConstant;


public class NewLDResponse {

    private ResponseData responseData;

    public NewLDResponse(ResponseData responseData) {
        this.responseData = responseData;
    }

        public boolean isSuccess() {
        return "00".equals(getRejCode());
    }

        public boolean isStorageFull() {
        return "XP".equals(getRejCode());
    }


    public boolean isCancelFailedBecauseOfCheckout() {
        return "XR".equals(getRejCode()) || "XS".equals(getRejCode());
    }

                public boolean isDoNotReceiveResultFromPos() {
        return "-119".equals(getRejCode());
    }


    public boolean isCancelWithNotExistTradeNo() {
        return "XS".equals(getRejCode());
    }



    public boolean isAlreadyCanceled() {
        return "XR".equals(getRejCode());
    }

        public String getTips() {
        return responseData.GetValue(LDResponseConstant.TIPS);
    }

        public String getTotal() {
        return responseData.GetValue(LDResponseConstant.TOTAL);
    }

        public String getAmount() {
        return responseData.GetValue(LDResponseConstant.AMOUNT);
    }

        public String getBalanceAmount() {
        return responseData.GetValue(LDResponseConstant.BALANCE_AMOUNT);
    }

        public String getPosTraceNumber() {
        return responseData.GetValue(LDResponseConstant.POS_TRACE_NUMBER);
    }

        public String getOldTraceNumber() {
        return responseData.GetValue(LDResponseConstant.OLD_TRACE_NUMBER);
    }

        public String getExpireDate() {
        return responseData.GetValue(LDResponseConstant.EXPIRE_DATE);
    }

        public String getBatchNumber() {
        return responseData.GetValue(LDResponseConstant.BATCH_NUMBER);
    }

        public String getMerchantNumber() {
        return responseData.GetValue(LDResponseConstant.MERCHANT_NUMBER);
    }

        public String getMerchantName() {
        return responseData.GetValue(LDResponseConstant.MERCHANT_NAME);
    }

        public String getTerminalNumber() {
        return responseData.GetValue(LDResponseConstant.TERMINAL_NUMBER);
    }

        public String getHostSerialNumber() {
        return responseData.GetValue(LDResponseConstant.HOST_SERIAL_NUMBER);
    }

        public String getAuthNumber() {
        return responseData.GetValue(LDResponseConstant.AUTH_NUMBER);
    }

        public String getRejCode() {
        return responseData.GetValue(LDResponseConstant.REJ_CODE);
    }

        public String getIssNumber() {
        return responseData.GetValue(LDResponseConstant.ISS_NUMBER);
    }

        public String getIssName() {
        return responseData.GetValue(LDResponseConstant.ISS_NAME);
    }

        public String getCardNumber() {
        return responseData.GetValue(LDResponseConstant.CARD_NUMBER);
    }

    public String getCardName() {
        return responseData.GetValue(LDResponseConstant.CARD_NAME);
    }

        public String getTransDate() {
        return responseData.GetValue(LDResponseConstant.TRANS_DATE);
    }

        public String getTransTime() {
        return responseData.GetValue(LDResponseConstant.TRANS_TIME);
    }

    public String getTransType() {
        return responseData.GetValue(LDResponseConstant.TRANS_TYPE);
    }

        public String getRejCodeExplain() {
        return responseData.GetValue(LDResponseConstant.REJ_CODE_EXPLAIN);
    }

        public String getCardBack() {
        return responseData.GetValue(LDResponseConstant.CARD_BACK);
    }

        public String getMemo() {
        return responseData.GetValue(LDResponseConstant.MEMO);
    }

        public String getTransCheck() {
        return responseData.GetValue(LDResponseConstant.TRANS_CHECK);
    }

        public String getAcqNumber() {
        return responseData.GetValue(LDResponseConstant.ACQ_NUMBER);
    }

    public String getAppName() {
        return responseData.GetValue(LDResponseConstant.APP_NAME);
    }

    public ResponseData getResponseData() {
        return responseData;
    }

        public String getStoredBusinessCount() {
        return responseData.GetValue(LDResponseConstant.POS_TRANS_COUNTS);
    }


        public String getKeyValue() {
        return responseData.GetValue(LDResponseConstant.KEY_VALUE);
    }
}
