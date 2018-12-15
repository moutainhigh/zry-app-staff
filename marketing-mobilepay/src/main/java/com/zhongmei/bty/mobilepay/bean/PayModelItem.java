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

/**
 * @Date：2015-9-16 下午7:45:35
 * @Description: 保存一个其它支付数据
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class PayModelItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private PaymentModeShop paymentModeShop;//自定义支付数据
    private IGroupBuyingCoupon tuanGouCouponDetail;//美团团购券信息(百度糯米券口碑券)
    private PosTransLog posTransLog;// 银联pos扣款信息add 20161128
    private PayModeId payModeId;//支付方式类型
    private int usedCount;//使用次数
    private PayType payType;//区分主扫还是被扫
    private String authCode;//付款码，支付上行数据
    private BigDecimal noDiscountAmount;//不参与优惠金额
    private PasswordType passwordType;//密码类别 add 20170612 会员扫码支付需要
    private String uuid;
    private BigDecimal usedValue;//使用的金额
    private BigDecimal ChangeAmount = BigDecimal.ZERO;//找零金额//add 20161123
    private MeituanDishVo meituanDishVo;//add 2017.11.16
    private String deviceId;//add 8.7 手环id

    // 构造方法1
    public PayModelItem(PaymentModeShop payModeShop) {
        this.paymentModeShop = payModeShop;
        this.payModeId = ValueEnums.toEnum(PayModeId.class, paymentModeShop.getErpModeId());
    }

    //构造方法2 modify 20161127
    /*public PayModelItem(TuanGouCouponDetail meiTuanCoupon) {
        this.tuanGouCouponDetail = meiTuanCoupon;
        this.payModeId = PayModeId.MEITUAN_TUANGOU;
    }*/

    //构造方法3 add 20161123
    public PayModelItem(PayModeId payModeId) {
        this.payModeId = payModeId;
    }

    //构造方法4 add 20161226 for BaiDuNuoMiQuan
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
        if (posTransLog != null) {//add 20170314银联刷卡uuid取PosTransLog记录uuid
            this.uuid = posTransLog.getUuid();
        }
    }

    public PayModeId getPayMode() {
        return payModeId;
    }

    public void setPayMode(PayModeId payModeId) {
        this.payModeId = payModeId;
    }

    //面值
    public BigDecimal getUsedValue() {
        if (this.payModeId == PayModeId.MEITUAN_TUANGOU || this.payModeId == PayModeId.BAINUO_TUANGOU || this.payModeId == PayModeId.KOUBEI_TUANGOU) {
            return this.usedValue;//美团、百度糯米券、口碑
        } else {
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

    //成本价
    public BigDecimal getActualValue() {

        if (this.payModeId == PayModeId.MEITUAN_TUANGOU || this.payModeId == PayModeId.BAINUO_TUANGOU || this.payModeId == PayModeId.KOUBEI_TUANGOU) {
            return this.tuanGouCouponDetail.getPrice().multiply(new BigDecimal(usedCount));//美团售价、 百度糯米券
        } else if (this.payModeId == PayModeId.CASH) {//现金实收要减去找零
            return MathDecimal.round(getUsedValue().subtract(getChangeAmount()), 2);
        } else {
            if (isAutoInput()) {
                return usedValue;//手动输入金额
            } else {
                if (usedCount > 0) {
                    return paymentModeShop.getFaceValue().multiply(new BigDecimal(usedCount));//面值
                } else {
                    return BigDecimal.ZERO;
                }
            }
        }
    }

    //票面金额
    public BigDecimal getFaceValue() {
        if (this.payModeId == PayModeId.MEITUAN_TUANGOU || this.payModeId == PayModeId.BAINUO_TUANGOU || this.payModeId == PayModeId.KOUBEI_TUANGOU) {
            return this.tuanGouCouponDetail.getMarketPrice().multiply(new BigDecimal(usedCount));//美团、 百度糯米券、口碑券面额
        } else if (this.payModeId == PayModeId.CASH) {//现金实收要减去找零
            return MathDecimal.round(getUsedValue(), 2);
        } else {
            if (isAutoInput()) {
                return usedValue;//手动输入金额
            } else {
                if (usedCount > 0 && paymentModeShop != null) {
                    return paymentModeShop.getFaceValue().multiply(new BigDecimal(usedCount));//面值
                } else {
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
            case CASH://现金
            case BANK_CARD://银行卡(记账)
            case POS_CARD://银联pos刷卡
            case WEIXIN_PAY://微信支付
            case ALIPAY://支付宝
            case BAIFUBAO://百度钱包
            case MEMBER_CARD://会员卡余额
            case ENTITY_CARD://会员实体卡
            case ANONYMOUS_ENTITY_CARD://匿名实体卡余额
            case MEITUAN_FASTPAY://美团闪付
            case MEITUAN_TUANGOU://美团团购
            case DIANPING_COUPON://点评团购劵
            case BAINUO_TUANGOU://百度糯米券
            case KOUBEI_TUANGOU://口碑券
                return true;

            default://其它自定义支付
                if (paymentModeShop != null) {
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
        if (this.payModeId == PayModeId.MEITUAN_TUANGOU) {//美团券
            return Long.valueOf(tuanGouCouponDetail.getSerialNumber());

        } else if (this.payModeId == PayModeId.BAINUO_TUANGOU) {//百度糯米券
            return Long.valueOf(tuanGouCouponDetail.getSerialNumber());

        } else if (this.payModeId == PayModeId.KOUBEI_TUANGOU) {//口碑券券
            return Long.valueOf(tuanGouCouponDetail.getSerialNumber());

        } else {
            if (paymentModeShop != null) {
                return paymentModeShop.getErpModeId();//其它自定义券

            } else {
                return this.payModeId.value();//现金、银联等手动输入金额

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
