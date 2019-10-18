package com.zhongmei.bty.mobilepay.bean;

import android.text.TextUtils;

import com.zhongmei.bty.basemodule.commonbusiness.enums.PasswordType;
import com.zhongmei.bty.mobilepay.bean.meituan.IGroupBuyingCoupon;
import com.zhongmei.bty.mobilepay.bean.meituan.MeituanDishVo;
import com.zhongmei.yunfu.db.entity.trade.PaymentModeShop;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PayType;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.commonmodule.database.entity.local.PosTransLog;
import com.zhongmei.yunfu.context.util.SystemUtils;

import java.io.Serializable;
import java.math.BigDecimal;


public class PayModelItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private PaymentModeShop paymentModeShop;    private IGroupBuyingCoupon tuanGouCouponDetail;    private PosTransLog posTransLog;    private PayModeId payModeId;    private int usedCount;    private PayType payType;    private String authCode;    private BigDecimal noDiscountAmount;    private PasswordType passwordType;    private String uuid;
    private BigDecimal usedValue;    private BigDecimal ChangeAmount = BigDecimal.ZERO;    private MeituanDishVo meituanDishVo;    private String deviceId;
        public PayModelItem(PaymentModeShop payModeShop) {
        this.paymentModeShop = payModeShop;
        this.payModeId = ValueEnums.toEnum(PayModeId.class, paymentModeShop.getErpModeId());
    }



        public PayModelItem(PayModeId payModeId) {
        this.payModeId = payModeId;
    }

        public PayModelItem(PayModeId payModeId, IGroupBuyingCoupon meiTuanCoupon) {
        this.payModeId = payModeId;
        this.tuanGouCouponDetail = meiTuanCoupon;
    }

    public PasswordType getPasswordType() {
        return passwordType;
    }

    public void setPasswordType(PasswordType passwordType) {
        this.passwordType = passwordType;
    }

    public BigDecimal getChangeAmount() {
        return ChangeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        ChangeAmount = changeAmount;
    }

    public BigDecimal getNoDiscountAmount() {
        return noDiscountAmount;
    }

    public void setNoDiscountAmount(BigDecimal noDiscountAmount) {
        this.noDiscountAmount = noDiscountAmount;
    }


    public PaymentModeShop getPaymentModeShop() {
        return paymentModeShop;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public IGroupBuyingCoupon getTuanGouCouponDetail() {
        return tuanGouCouponDetail;
    }

    public PosTransLog getPosTransLog() {
        return posTransLog;
    }

    public void setPosTransLog(PosTransLog posTransLog) {
        this.posTransLog = posTransLog;
        if (posTransLog != null) {            this.uuid = posTransLog.getUuid();
        }
    }

    public PayModeId getPayMode() {
        return payModeId;
    }

    public void setPayMode(PayModeId payModeId) {
        this.payModeId = payModeId;
    }

        public BigDecimal getUsedValue() {
        if (this.payModeId == PayModeId.MEITUAN_TUANGOU || this.payModeId == PayModeId.BAINUO_TUANGOU || this.payModeId == PayModeId.KOUBEI_TUANGOU) {
            return this.usedValue;        } else {
            if (isAutoInput()) {
                return usedValue;
            } else {
                if (usedCount > 0) {
                    return paymentModeShop.getFaceValue().multiply(new BigDecimal(usedCount));
                } else {
                    return BigDecimal.ZERO;
                }
            }
        }
    }

        public BigDecimal getActualValue() {

        if (this.payModeId == PayModeId.MEITUAN_TUANGOU || this.payModeId == PayModeId.BAINUO_TUANGOU || this.payModeId == PayModeId.KOUBEI_TUANGOU) {
            return this.tuanGouCouponDetail.getPrice().multiply(new BigDecimal(usedCount));        } else if (this.payModeId == PayModeId.CASH) {            return MathDecimal.round(getUsedValue().subtract(getChangeAmount()), 2);
        } else {
            if (isAutoInput()) {
                return usedValue;            } else {
                if (usedCount > 0) {
                    return paymentModeShop.getFaceValue().multiply(new BigDecimal(usedCount));                } else {
                    return BigDecimal.ZERO;
                }
            }
        }
    }

        public BigDecimal getFaceValue() {
        if (this.payModeId == PayModeId.MEITUAN_TUANGOU || this.payModeId == PayModeId.BAINUO_TUANGOU || this.payModeId == PayModeId.KOUBEI_TUANGOU) {
            return this.tuanGouCouponDetail.getMarketPrice().multiply(new BigDecimal(usedCount));        } else if (this.payModeId == PayModeId.CASH) {            return MathDecimal.round(getUsedValue(), 2);
        } else {
            if (isAutoInput()) {
                return usedValue;            } else {
                if (usedCount > 0 && paymentModeShop != null) {
                    return paymentModeShop.getFaceValue().multiply(new BigDecimal(usedCount));                } else {
                    return BigDecimal.ZERO;
                }
            }
        }
    }

    public void setUsedValue(BigDecimal value) {

        usedValue = value;
    }

    public boolean isUsed() {
        if (usedCount > 0)
            return true;
        else return false;
    }

    public boolean isAutoInput() {
        if (this.payModeId == null) {
            return true;
        }

        switch (this.payModeId) {
            case CASH:            case BANK_CARD:            case POS_CARD:            case WEIXIN_PAY:            case ALIPAY:            case BAIFUBAO:            case MEMBER_CARD:            case ENTITY_CARD:            case ANONYMOUS_ENTITY_CARD:            case MEITUAN_FASTPAY:            case MEITUAN_TUANGOU:            case DIANPING_COUPON:            case BAINUO_TUANGOU:            case KOUBEI_TUANGOU:                return true;

            default:                if (paymentModeShop != null) {
                    if (paymentModeShop.getFaceValue() != null) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return true;
                }
        }
    }

    public void addOne() {
        usedCount++;
    }

    public void subOne() {
        usedCount--;
    }

    public Long getPayModelId() {
        if (this.payModeId == PayModeId.MEITUAN_TUANGOU) {            return Long.valueOf(tuanGouCouponDetail.getSerialNumber());

        } else if (this.payModeId == PayModeId.BAINUO_TUANGOU) {            return Long.valueOf(tuanGouCouponDetail.getSerialNumber());

        } else if (this.payModeId == PayModeId.KOUBEI_TUANGOU) {            return Long.valueOf(tuanGouCouponDetail.getSerialNumber());

        } else {
            if (paymentModeShop != null) {
                return paymentModeShop.getErpModeId();
            } else {
                return this.payModeId.value();
            }
        }
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getUuid() {
        if (TextUtils.isEmpty(uuid)) {
            uuid = SystemUtils.genOnlyIdentifier();
        }
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public MeituanDishVo getMeituanDishVo() {
        return meituanDishVo;
    }

    public void setMeituanDishVo(MeituanDishVo meituanDishVo) {
        this.meituanDishVo = meituanDishVo;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
