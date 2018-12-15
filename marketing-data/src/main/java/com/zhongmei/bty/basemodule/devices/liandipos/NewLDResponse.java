package com.zhongmei.bty.basemodule.devices.liandipos;


import com.landicorp.model.ResponseData;
import com.zhongmei.bty.basemodule.devices.liandipos.contants.LDResponseConstant;

/**
 * Created by demo on 2018/12/15
 */
public class NewLDResponse {

    private ResponseData responseData;

    public NewLDResponse(ResponseData responseData) {
        this.responseData = responseData;
    }

    //是否成功
    public boolean isSuccess() {
        return "00".equals(getRejCode());
    }

    //是否存储已满
    public boolean isStorageFull() {
        return "XP".equals(getRejCode());
    }

    /**
     * 是否因为结算了而不能撤销
     * "XR"是服务器自动结算的情况
     * "XS"是pos机发起结算而清空了数据的情况
     *
     * @return the boolean
     */
    public boolean isCancelFailedBecauseOfCheckout() {
        return "XR".equals(getRejCode()) || "XS".equals(getRejCode());
    }

    //是否pos机收到服务器的结果，但在发送给收银机的时候失败了，
    // 这种情况需要调重印接口来获取最后一笔交易的信息，用getTransCheck()对比交易唯一标识
    // TODO: 2016/1/6  未测试
    public boolean isDoNotReceiveResultFromPos() {
        return "-119".equals(getRejCode());
    }

    /**
     * 撤销时本pos机没有些流水号
     *
     * @return the boolean
     */
    public boolean isCancelWithNotExistTradeNo() {
        return "XS".equals(getRejCode());
    }


    /**
     * 此笔交易已经撤销过了
     *
     * @return the boolean
     */
    public boolean isAlreadyCanceled() {
        return "XR".equals(getRejCode());
    }

    //小费
    public String getTips() {
        return responseData.GetValue(LDResponseConstant.TIPS);
    }

    //总计
    public String getTotal() {
        return responseData.GetValue(LDResponseConstant.TOTAL);
    }

    //金额
    public String getAmount() {
        return responseData.GetValue(LDResponseConstant.AMOUNT);
    }

    //余额
    public String getBalanceAmount() {
        return responseData.GetValue(LDResponseConstant.BALANCE_AMOUNT);
    }

    //流水号
    public String getPosTraceNumber() {
        return responseData.GetValue(LDResponseConstant.POS_TRACE_NUMBER);
    }

    //原始流水号
    public String getOldTraceNumber() {
        return responseData.GetValue(LDResponseConstant.OLD_TRACE_NUMBER);
    }

    //有效期
    public String getExpireDate() {
        return responseData.GetValue(LDResponseConstant.EXPIRE_DATE);
    }

    //批次号
    public String getBatchNumber() {
        return responseData.GetValue(LDResponseConstant.BATCH_NUMBER);
    }

    //商户号
    public String getMerchantNumber() {
        return responseData.GetValue(LDResponseConstant.MERCHANT_NUMBER);
    }

    //商户名
    public String getMerchantName() {
        return responseData.GetValue(LDResponseConstant.MERCHANT_NAME);
    }

    //终端号
    public String getTerminalNumber() {
        return responseData.GetValue(LDResponseConstant.TERMINAL_NUMBER);
    }

    //系统参考号
    public String getHostSerialNumber() {
        return responseData.GetValue(LDResponseConstant.HOST_SERIAL_NUMBER);
    }

    //授权码
    public String getAuthNumber() {
        return responseData.GetValue(LDResponseConstant.AUTH_NUMBER);
    }

    //返回码
    public String getRejCode() {
        return responseData.GetValue(LDResponseConstant.REJ_CODE);
    }

    //发卡行号
    public String getIssNumber() {
        return responseData.GetValue(LDResponseConstant.ISS_NUMBER);
    }

    //发卡行名称
    public String getIssName() {
        return responseData.GetValue(LDResponseConstant.ISS_NAME);
    }

    //卡号
    public String getCardNumber() {
        return responseData.GetValue(LDResponseConstant.CARD_NUMBER);
    }

    public String getCardName() {
        return responseData.GetValue(LDResponseConstant.CARD_NAME);
    }

    //交易日期
    public String getTransDate() {
        return responseData.GetValue(LDResponseConstant.TRANS_DATE);
    }

    //交易时间
    public String getTransTime() {
        return responseData.GetValue(LDResponseConstant.TRANS_TIME);
    }

    public String getTransType() {
        return responseData.GetValue(LDResponseConstant.TRANS_TYPE);
    }

    //返回码解释
    public String getRejCodeExplain() {
        return responseData.GetValue(LDResponseConstant.REJ_CODE_EXPLAIN);
    }

    //卡片回收标志
    public String getCardBack() {
        return responseData.GetValue(LDResponseConstant.CARD_BACK);
    }

    //备注
    public String getMemo() {
        return responseData.GetValue(LDResponseConstant.MEMO);
    }

    //交易唯一标识
    public String getTransCheck() {
        return responseData.GetValue(LDResponseConstant.TRANS_CHECK);
    }

    // 收单行号
    public String getAcqNumber() {
        return responseData.GetValue(LDResponseConstant.ACQ_NUMBER);
    }

    public String getAppName() {
        return responseData.GetValue(LDResponseConstant.APP_NAME);
    }

    public ResponseData getResponseData() {
        return responseData;
    }

    //pos机里存储的交易数
    public String getStoredBusinessCount() {
        return responseData.GetValue(LDResponseConstant.POS_TRANS_COUNTS);
    }


    //返回POS的键盘值
    public String getKeyValue() {
        return responseData.GetValue(LDResponseConstant.KEY_VALUE);
    }
}
