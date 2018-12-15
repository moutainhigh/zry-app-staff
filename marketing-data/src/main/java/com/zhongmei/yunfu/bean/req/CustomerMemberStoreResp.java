package com.zhongmei.yunfu.bean.req;

import com.zhongmei.bty.basemodule.devices.mispos.enums.AccountStatus;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;

import java.math.BigDecimal;

/**
 * 实体卡储值记录
 *
 * @version: 1.0
 * @date 2015年5月13日
 */
public class CustomerMemberStoreResp {

    private Integer padNo; //
    private String mobile; //手机号
    private Long customerId; //顾客Id
    private Integer accountStatus; //是否到账：1已到账、0未到账
    private Long tradeId;
    private String tradeNo; //订单号
    private BigDecimal tradeAmount; //订单金额
    private String creatorName; //操作人名称
    private Integer tradeStatus; //交易状态(订单状态) 1:UNPROCESSED:未处理 2:TEMPORARY:挂单，不需要厨房打印(客户端本地的.) 3:CONFIRMED:已确认 4:FINISH:已完成(全部支付) 5:RETURNED:已退货 6:INVALID:已作废 7:REFUSED:已拒绝, 8:已取消 10:已反结账
    private Integer tradePayStatus; //订单支付状态支付状态： 1:UNPAID:未支付 2:PAYING:支付中，微信下单选择了在线支付但实际上未完成支付的 (删了) 3:PAID:已支付 4:REFUNDING:退款中 5:REFUNDED:已退款 6:REFUND_FAILED:退款失败 7:PREPAID:预支付(现在都没用) 8:WAITING_REFUND:等待退款 9:PAID_FAIL:支付失败
    private Long brandIdenty; //品牌Id
    private Long shopIdenty; //门店Id
    private String deviceIdenty; //设备号Id
    private Long tradeServerUpdateTime; //订单创建时间
    private Integer storeType; //储值类型：0储值、1储值退款 2、消费
    private BigDecimal beforeRealValue; //操作前实储金额
    private BigDecimal beforeSendValue; //操作前赠送金额
    private BigDecimal beforePrepareValue; //操作前预储金额
    private BigDecimal currentRealValue; //本次操作实储金额
    private BigDecimal currentSendValue; //本次操作赠送金额
    private BigDecimal currentPrepareValue; //本次操作预储金额
    private BigDecimal endRealValue; //操作后实储金额
    private BigDecimal endSendValue; //操作后实储金额
    private BigDecimal endPrepareValue; //操作后实储金额
    private Long historyId; //储值记录id
    private String paymentUuid; //订单paymentUuid

    private Long serverCreateTime; //订单创建时间
    private Integer addValueType;//储值类型 1：现金，2:银行卡
    private Long bizDate;

    public Integer getPadNo() {
        return padNo;
    }

    public String getMobile() {
        return mobile;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public AccountStatus getAccountStatus() {
        return ValueEnums.toEnum(AccountStatus.class, accountStatus);
    }

    public Long getTradeId() {
        return tradeId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public TradeStatus getTradeStatus() {
        return ValueEnums.toEnum(TradeStatus.class, tradeStatus);
    }

    public TradePayStatus getTradePayStatus() {
        return ValueEnums.toEnum(TradePayStatus.class, tradePayStatus);
    }

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public String getDeviceIdenty() {
        return deviceIdenty;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public Long getTradeServerUpdateTime() {
        return tradeServerUpdateTime;
    }

    public Integer getStoreType() {
        return storeType;
    }

    public BigDecimal getBeforeRealValue() {
        return beforeRealValue;
    }

    public BigDecimal getBeforeSendValue() {
        return beforeSendValue;
    }

    public BigDecimal getBeforePrepareValue() {
        return beforePrepareValue;
    }

    public BigDecimal getCurrentRealValue() {
        return currentRealValue;
    }

    public BigDecimal getCurrentSendValue() {
        return currentSendValue;
    }

    public BigDecimal getCurrentPrepareValue() {
        return currentPrepareValue;
    }

    public BigDecimal getEndRealValue() {
        return endRealValue;
    }

    public BigDecimal getEndSendValue() {
        return endSendValue;
    }

    public BigDecimal getEndPrepareValue() {
        return endPrepareValue;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public String getPaymentUuid() {
        return paymentUuid;
    }

    public Integer getAddValueType() {
        return addValueType;
    }

    public Long getBizDate() {
        return bizDate;
    }
}
