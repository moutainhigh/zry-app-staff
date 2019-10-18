package com.zhongmei.yunfu.resp;

import com.google.gson.annotations.SerializedName;
import com.zhongmei.yunfu.context.util.NoProGuard;


public class ResponseObject<T> implements NoProGuard {

    @SerializedName("status")
    private int statusCode;
    private String message;
    private String messageId;    private String errors;
    private T content;




    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }


    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getMessageId() {
        return messageId;
    }


    public static final int OK = 1000;
    public static final int OK_TIMEOUT = 1010;

    public static final int OK_DATA_UPDATED = 10001000;

    public static final int PAY_ERROR_RETRY = 10001001;


    public static final int VALIDATE_ERROR = 1001;


    public static final int BusinessOperationFailed = 1100;


    public static final int BAINUOCOUPONSPARTOK = 1110;


    public static final int TRADE_HAS_PAID = 1202;

    public static final int InternalServerError = 2000;


    public static final int COUPON_FAILED = 1301;


    public static final int WEIXIN_COUPON_FAILED = 1302;


    public static final int INTEGRAL_FAILED = 1131;


    public static final int MEMBER_REJECT = 1126;


    public static final int PAY_PROCESS_FAIL = 1714;


            public static final int CARD_SERVICE_CHECK_FAILED = 5000;
        public static final int WEIXINCODE_CHECK_FAILED = 5001;
        public static final int COUPON_CHECK_FAILED = 5002;
        public static final int CORE_PAY_PROCESS_FAIL = 5004;


    public static boolean isOk(ResponseObject<?> obj) {
        if (obj == null) {
            return false;
        }
        return ResponseObject.OK == obj.getStatusCode();
    }


    public static boolean isExisted(ResponseObject<?> obj) {
        if (obj == null) {
            return false;
        }
                return obj.getStatusCode() == ResponseObject.OK_TIMEOUT
                || ResponseObject.OK_DATA_UPDATED == obj.getStatusCode();
    }

    public static boolean isOkExisted(ResponseObject<?> obj) {
        return isOk(obj) || isExisted(obj);
    }
}
