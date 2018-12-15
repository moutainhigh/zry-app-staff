package com.zhongmei.yunfu.resp;

import com.google.gson.annotations.SerializedName;
import com.zhongmei.yunfu.context.util.NoProGuard;

/**
 * 实时请求返回数据的封装
 *
 * @param <T>
 */
public class ResponseObject<T> implements NoProGuard {

    @SerializedName("status")
    private int statusCode;
    private String message;
    private String messageId;//适配erp接口
    private String errors;
    private T content;

    /*@Deprecated
    public boolean isOk() {
        return statusCode == ResponseObject.OK;
    }*/

    /**
     * 返回状态码
     *
     * @return
     */
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * 返回附加信息
     *
     * @return
     */
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

    /**
     * 返回回复的内容，状态不是成功时可能返回null
     *
     * @return
     */
    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getMessageId() {
        return messageId;
    }

    /**
     * 操作成功
     */
    public static final int OK = 1000;
    /**
     * 操作成功数据被其它状态更新
     */
    public static final int OK_DATA_UPDATED = 10001000;
    /**
     * 翼支付支付错误重试
     */
    public static final int PAY_ERROR_RETRY = 10001001;

    /**
     * 数据验证失败
     */
    public static final int VALIDATE_ERROR = 1001;

    /**
     * 业务处理失败
     */
    public static final int BusinessOperationFailed = 1100;

    /**
     * 百糯券部分成功，部分失败
     */
    public static final int BAINUOCOUPONSPARTOK = 1110;

    /**
     * 订单已经被支付
     */
    public static final int TRADE_HAS_PAID = 1202;
    /**
     * 服务器内部错误
     */
    public static final int InternalServerError = 2000;

    /**
     * 会员优惠劵验证是失败
     */
    public static final int COUPON_FAILED = 1301;

    /**
     * 微信优惠劵验证是失败
     */
    public static final int WEIXIN_COUPON_FAILED = 1302;

    /**
     * 积分验证是失败
     */
    public static final int INTEGRAL_FAILED = 1131;

    /**
     * 会员被禁用
     */
    public static final int MEMBER_REJECT = 1126;

    /**
     * 支付过程失败（payment paymentItem 都已经写入）
     */
    public static final int PAY_PROCESS_FAIL = 1714;


    //core api 核销失败码
    //次卡检查失败
    public static final int CARD_SERVICE_CHECK_FAILED = 5000;
    //微信卡劵检查失败
    public static final int WEIXINCODE_CHECK_FAILED = 5001;
    //    优惠劵核销失败
    public static final int COUPON_CHECK_FAILED = 5002;
    //core 支付过程失败
    public static final int CORE_PAY_PROCESS_FAIL = 5004;
    //core api 核销失败码 end

    /**
     * 判断指定的回复信息是否是成功状态，是成功状态返回true
     *
     * @param obj
     * @return
     */
    public static boolean isOk(ResponseObject<?> obj) {
        if (obj == null) {
            return false;
        }
        return ResponseObject.OK == obj.getStatusCode();
    }

    /**
     * 判断指定的回复信息是否是服务端已经存在，是已经存在返回true
     *
     * @param obj
     * @return
     */
    public static boolean isExisted(ResponseObject<?> obj) {
        if (obj == null) {
            return false;
        }
        // 两个码都表示数据已存在
        return ResponseObject.OK_DATA_UPDATED == obj.getStatusCode();
    }

}
