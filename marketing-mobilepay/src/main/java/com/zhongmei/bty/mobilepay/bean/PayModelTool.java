package com.zhongmei.bty.mobilepay.bean;

import static com.zhongmei.yunfu.db.enums.BusinessType.ANONYMOUS_ENTITY_CARD_SELL;

import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.pay.bean.PaymentCard;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.yunfu.db.entity.ErpCommercialRelation;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.commonmodule.database.entity.local.PosTransLog;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PayModelGroup;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.bty.commonmodule.database.enums.PosBusinessType;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.PaymentItemExtra;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGroupon;
import com.zhongmei.bty.basemodule.pay.message.PaymentDeviceReq;
import com.zhongmei.bty.basemodule.pay.message.PaymentItemUnionCardReq;
import com.zhongmei.bty.basemodule.pay.message.PaymentItemUnionpayReq;
import com.zhongmei.yunfu.db.enums.PaySource;
import com.zhongmei.yunfu.db.enums.PayType;
import com.zhongmei.yunfu.db.enums.RefundWay;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



public class PayModelTool {
    private IPaymentInfo paymentInfo;
    private List<PayModelItem> payModelItemList;
    private double exemptAmount;
    private String operatorName;
    private long operatorId;
    private Payment paidPayment;
    public PayModelTool(IPaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
        this.payModelItemList = paymentInfo.getOtherPay().getAllPayModelItems();
        this.exemptAmount = paymentInfo.getExemptAmount();
        this.paidPayment = paymentInfo.getPaidPayment();
    }

    public PaymentVo creatTradePaymentVo() {
        PaymentVo paymenVo = null;
        Trade trade = null;
        if ((trade = paymentInfo.getTradeVo().getTrade()) != null) {
            paymenVo = new PaymentVo();
            Payment payment = null;

            if (this.paidPayment != null) {
                payment = this.paidPayment;
                if (payment.getShopActualAmount() != null) {
                    payment.setShopActualAmount(null);                }
            } else {
                payment = new Payment();
                payment.validateCreate();
                                payment.setUuid(paymentInfo.getPaymentUuid());
            }

            final List<PaymentItem> paymentItemList = new ArrayList<PaymentItem>();
                        operatorName = Session.getAuthUser() != null
                    ? Session.getAuthUser().getName() : trade.getCreatorName();
            operatorId = Session.getAuthUser() != null
                    ? Session.getAuthUser().getId() : trade.getCreatorId();

                        payment.setReceivableAmount(paymentInfo.getTradeVo().getTrade().getTradeAmount());            payment.setBeforePrivilegeAmount(paymentInfo.getTradeVo().getBeforePrivilegeAmount());             double actualAmount = CashInfoManager
                    .floatSubtract(paymentInfo.getTradeVo().getTrade().getTradeAmount().doubleValue(), paymentInfo.getExemptAmount());
            payment.setActualAmount(BigDecimal.valueOf(actualAmount));            payment.setExemptAmount(BigDecimal.valueOf(this.exemptAmount));
            payment.setRelateUuid(trade.getUuid());            payment.setRelateId(trade.getId());                        BusinessType businessType = paymentInfo.getTradeBusinessType();
            PaymentType paymentType = PaymentType.TRADE_SELL;
            if (businessType != null) {
                switch (businessType) {
                    case ONLINE_RECHARGE:                        paymentType = PaymentType.MEMBER_RECHARGE;
                        break;
                    case CARD_RECHARGE:                        paymentType = PaymentType.ENTITY_CARD_RECHARGE;
                        break;
                    case ANONYMOUS_ENTITY_CARD_SELL_AND_RECHARGE:                        paymentType = PaymentType.ANONYMOUS_ENTITY_CARD_SELL_RECHARGE;
                        break;
                    case ANONYMOUS_ENTITY_CARD_RECHARGE:                        paymentType = PaymentType.ANONYMOUS_ENTITY_CARD_RECHARGE;
                        break;
                    default:
                        if (businessType == ANONYMOUS_ENTITY_CARD_SELL && ServerSettingCache.getInstance().isJinChBusiness()) {
                                                        paymentType = PaymentType.ANONYMOUS_ENTITY_CARD_SELL;

                        } else {
                            paymentType = PaymentType.TRADE_SELL;
                        }
                        break;
                }
            }
            payment.setPaymentType(paymentType);            paymenVo.setPayment(payment);            paymenVo.setPaymentItemList(paymentItemList);            createPaymentItem(payment, paymentItemList);
        }
        return paymenVo;
    }

    private void createPaymentItem(Payment payment, List<PaymentItem> paymentItemList) {
        if (!Utils.isEmpty(payModelItemList)) {
            double restAmount = paymentInfo.getActualAmount();            for (PayModelItem model : payModelItemList) {

                if (model.getPayModelId() != null) {

                    PaymentItemExtra paymentItemExtra = null;
                    PaymentItem paymentItem = new PaymentItem();
                    paymentItemList.add(paymentItem);
                    paymentItem.setPaySource(PaySource.CASHIER);                    paymentItem.setUuid(model.getUuid());
                    paymentItem.setPaymentUuid(payment.getUuid());
                    if (paymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT || paymentInfo.getPayScene() == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) {                         paymentItem.setIsDeposit(1);
                    }
                    switch (model.getPayMode()) {
                        case CASH:                            paymentItem.setPayModeId(model.getPayMode().value());                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));                            paymentItem.setPayModelGroup(PayModelGroup.CASH);                            paymentItem.setRefundWay(RefundWay.NONEED_REFUND);                            paymentItem.setFaceAmount(model.getUsedValue());                            paymentItem.setChangeAmount(model.getChangeAmount());                                                        if (this.paymentInfo.getActualAmount() >= model.getUsedValue().doubleValue()) {
                                paymentItem.setUsefulAmount(MathDecimal.round(model.getUsedValue(), 2));                            } else {                                paymentItem.setUsefulAmount(BigDecimal.valueOf(this.paymentInfo.getActualAmount()));                            }
                                                        break;

                        case BANK_CARD:                            paymentItem.setPayModeId(model.getPayMode().value());                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));                            paymentItem.setPayModelGroup(PayModelGroup.BANK_CARD);
                            paymentItem.setRefundWay(RefundWay.NONEED_REFUND);
                            paymentItem.setFaceAmount(model.getUsedValue());                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));                            paymentItem.setUsefulAmount(model.getUsedValue());                            break;
                        case OTHER_WX_PAY:
                        case OTHER_ALI_PAY:
                            paymentItem.setPayModeId(model.getPayMode().value());                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));                            paymentItem.setPayModelGroup(PayModelGroup.OTHER);
                            paymentItem.setRefundWay(RefundWay.NONEED_REFUND);
                            paymentItem.setFaceAmount(model.getUsedValue());                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));                            paymentItem.setUsefulAmount(model.getUsedValue());                            break;
                        case POS_CARD:                            paymentItem.setPayModeId(model.getPayMode().value());                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));                            paymentItem.setPayModelGroup(PayModelGroup.BANK_CARD);
                            paymentItem.setRefundWay(RefundWay.HAND_REFUND);
                            paymentItem.setFaceAmount(model.getUsedValue());                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));                            paymentItem.setUsefulAmount(model.getUsedValue());                            PaymentItemUnionCardReq cardReq = getPaymentItemUnionpayCardReq(model.getPosTransLog(), operatorId, operatorName);
                            paymentItem.setPaymentItemUnionPay(cardReq);
                            break;
                        case WEIXIN_PAY:                        case ALIPAY:                        case BAIFUBAO:                        case MEITUAN_FASTPAY:                        case UNIONPAY_CLOUD_PAY:                        case ICBC_E_PAY:                        case MOBILE_PAY:                         case DIANXIN_YIPAY:                            paymentItem.setPayModeId(model.getPayMode().value());                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));                            paymentItem.setPayModelGroup(PayModelGroup.ONLINE);
                            paymentItem.setRefundWay(RefundWay.AUTO_REFUND);
                            paymentItem.setFaceAmount(model.getUsedValue());                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));                            paymentItem.setUsefulAmount(model.getUsedValue());                            if (model.getPayType() == PayType.SCAN) {                                paymentItem.setAuthCode(model.getAuthCode());
                            }
                                                        if (model.getPayMode() == PayModeId.MEITUAN_FASTPAY) {
                                paymentItem.setNoDiscountAmount(model.getNoDiscountAmount());                            }
                            paymentItemExtra = new PaymentItemExtra();
                            paymentItemExtra.setUuid(model.getUuid());
                            paymentItemExtra.setPayType(model.getPayType());
                            paymentItem.setPaymentItemExtra(paymentItemExtra);
                            break;
                        case MEMBER_CARD:                            paymentItem.setPayModeId(model.getPayMode().value());                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));                            paymentItem.setPayModelGroup(PayModelGroup.VALUE_CARD);
                            paymentItem.setRefundWay(RefundWay.AUTO_REFUND);
                            paymentItem.setRelateId(paymentInfo.getCustomerId() + "");                            paymentItem.setFaceAmount(model.getUsedValue());                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));                            paymentItem.setUsefulAmount(model.getUsedValue());                            if (model.getPasswordType() != null)                            {
                                paymentItem.setType(model.getPasswordType().value());
                            }
                            paymentItem.setConsumePassword(paymentInfo.getMemberPassword());
                            paymentItemExtra = new PaymentItemExtra();
                            paymentItemExtra.setUuid(model.getUuid());
                            paymentItemExtra.setCustomerId(paymentInfo.getCustomerId());                            if (ServerSettingCache.getInstance().isJinChBusiness()) {
                                paymentItemExtra.setEntityNo(paymentInfo.getEcCard().getCardNum());
                            }
                            paymentItemExtra.setPayType(model.getPayType());
                            paymentItem.setPaymentItemExtra(paymentItemExtra);
                            break;
                        case ENTITY_CARD:                            paymentItem.setPayModeId(model.getPayMode().value());                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));                            paymentItem.setPayModelGroup(PayModelGroup.VALUE_CARD);
                            paymentItem.setRefundWay(RefundWay.AUTO_REFUND);
                            paymentItem.setRelateId(paymentInfo.getEcCard().getCustomer().getCustomerid() + "");                            paymentItem.setFaceAmount(model.getUsedValue());                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));                            paymentItem.setUsefulAmount(model.getUsedValue());                            if (paymentInfo.getEcCard().getCardKind().getIsNeedPwd() == Bool.YES) {
                                paymentItem.setConsumePassword(paymentInfo.getMemberPassword());
                            }
                            paymentItemExtra = new PaymentItemExtra();
                            paymentItemExtra.setUuid(SystemUtils.genOnlyIdentifier());
                            paymentItemExtra.setCustomerId(paymentInfo.getEcCard().getCustomer().getCustomerid());                            paymentItemExtra.setEntityNo(paymentInfo.getEcCard().getCardNum());                            paymentItem.setPaymentItemExtra(paymentItemExtra);
                            break;
                        case ANONYMOUS_ENTITY_CARD:                            paymentItem.setPayModeId(model.getPayMode().value());                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));                            paymentItem.setPayModelGroup(PayModelGroup.VALUE_CARD);
                            paymentItem.setRefundWay(RefundWay.AUTO_REFUND);
                            paymentItem.setRelateId(paymentInfo.getEcCard().getCardNum());                            paymentItem.setFaceAmount(model.getUsedValue());                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));                            paymentItem.setUsefulAmount(model.getUsedValue());
                            paymentItemExtra = new PaymentItemExtra();
                            paymentItemExtra.setUuid(SystemUtils.genOnlyIdentifier());
                            paymentItemExtra.setEntityNo(paymentInfo.getEcCard().getCardNum());                            paymentItem.setPaymentItemExtra(paymentItemExtra);
                            break;

                        case MEITUAN_TUANGOU:                            paymentItem.setPayModeId(model.getPayMode().value());                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));                            paymentItem.setPayModelGroup(PayModelGroup.OTHER);
                            paymentItem.setRelateId(model.getTuanGouCouponDetail().getSerialNumber());
                            paymentItem.setFaceAmount(model.getFaceValue());                            paymentItem.setRefundWay(RefundWay.HAND_REFUND);
                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));                                                        if (this.paymentInfo.getActualAmount() >= model.getUsedValue().doubleValue()) {
                                paymentItem.setUsefulAmount(MathDecimal.round(model.getUsedValue(), 2));                            } else {                                paymentItem.setUsefulAmount(BigDecimal.valueOf(this.paymentInfo.getActualAmount()));                            }
                                                                                    PaymentItemGroupon pig = new PaymentItemGroupon();
                            pig.setGrouponId(model.getTuanGouCouponDetail().getGrouponId());
                            pig.setDealTitle(model.getTuanGouCouponDetail().getDealTitle());
                            pig.setMarketPrice(model.getTuanGouCouponDetail().getMarketPrice());
                            pig.setPrice(model.getTuanGouCouponDetail().getPrice());
                            pig.setUseCount(model.getUsedCount());
                            pig.setSerialNo(model.getTuanGouCouponDetail().getSerialNumber());
                            paymentItem.setPaymentItemGroupon(pig);
                                                        break;
                        case BAINUO_TUANGOU:                        case KOUBEI_TUANGOU:                            paymentItem.setPayModeId(model.getPayMode().value());                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));                            paymentItem.setPayModelGroup(PayModelGroup.OTHER);
                            paymentItem.setRelateId(model.getTuanGouCouponDetail().getSerialNumber());
                            paymentItem.setFaceAmount(model.getUsedValue());                            paymentItem.setRefundWay(RefundWay.HAND_REFUND);
                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));                                                        if (this.paymentInfo.getActualAmount() >= model.getUsedValue().doubleValue()) {
                                paymentItem.setUsefulAmount(MathDecimal.round(model.getUsedValue(), 2));                            } else {                                paymentItem.setUsefulAmount(BigDecimal.valueOf(this.paymentInfo.getActualAmount()));                            }
                                                                                    PaymentItemGroupon paymentItemGroupon = new PaymentItemGroupon();
                            paymentItemGroupon.setGrouponId(model.getTuanGouCouponDetail().getGrouponId());
                            paymentItemGroupon.setDealTitle(model.getTuanGouCouponDetail().getDealTitle());
                            paymentItemGroupon.setMarketPrice(model.getTuanGouCouponDetail().getMarketPrice());
                            paymentItemGroupon.setPrice(model.getTuanGouCouponDetail().getPrice());
                            paymentItemGroupon.setUseCount(model.getUsedCount());
                            paymentItemGroupon.setSerialNo(model.getTuanGouCouponDetail().getSerialNumber());
                            paymentItem.setPaymentItemGroupon(paymentItemGroupon);

                            break;
                        case JIN_CHENG:                            paymentItem.setPayModeId(model.getPayMode().value());                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));                            paymentItem.setPayModelGroup(PayModelGroup.OTHER);
                            paymentItem.setRefundWay(RefundWay.AUTO_REFUND);
                            paymentItem.setFaceAmount(model.getUsedValue());                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));                            paymentItem.setUsefulAmount(model.getUsedValue());                            if (model.getPayType() == PayType.SCAN) {                                paymentItem.setAuthCode(model.getAuthCode());
                            }
                            paymentItemExtra = new PaymentItemExtra();
                            paymentItemExtra.setUuid(SystemUtils.genOnlyIdentifier());
                            paymentItemExtra.setPayType(model.getPayType());
                            paymentItem.setPaymentItemExtra(paymentItemExtra);
                            break;
                        case JIN_CHENG_VALUE_CARD:                            paymentItem.setPayModeId(model.getPayMode().value());                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));                            paymentItem.setPayModelGroup(PayModelGroup.OTHER);
                            paymentItem.setRefundWay(RefundWay.AUTO_REFUND);
                            paymentItem.setRelateId(paymentInfo.getCustomerId() + "");                            paymentItem.setFaceAmount(model.getUsedValue());                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));                            paymentItem.setUsefulAmount(model.getUsedValue());                            if (model.getPasswordType() != null) {
                                paymentItem.setType(model.getPasswordType().value());
                            }
                            paymentItem.setConsumePassword(paymentInfo.getMemberPassword());
                            paymentItemExtra = new PaymentItemExtra();
                            paymentItemExtra.setUuid(SystemUtils.genOnlyIdentifier());
                            paymentItemExtra.setEntityNo(paymentInfo.getEcCard().getCardNum());
                            paymentItemExtra.setPayType(model.getPayType());
                            paymentItem.setPaymentItemExtra(paymentItemExtra);
                            break;

                        case FENGHUO_WRISTBAND:                            paymentItem.setPayModeId(model.getPayMode().value());                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));                            paymentItem.setPayModelGroup(PayModelGroup.OTHER);
                            paymentItem.setRefundWay(RefundWay.AUTO_REFUND);
                            paymentItem.setFaceAmount(model.getUsedValue());                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));                            paymentItem.setUsefulAmount(model.getUsedValue());                            if (model.getPasswordType() != null) {
                                paymentItem.setType(model.getPasswordType().value());
                            }
                            paymentItem.setConsumePassword(paymentInfo.getMemberPassword());

                            paymentItemExtra = new PaymentItemExtra();
                            paymentItemExtra.setUuid(SystemUtils.genOnlyIdentifier());
                            if (paymentInfo.getEcCard() != null)
                                paymentItemExtra.setEntityNo(paymentInfo.getEcCard().getCardNum());                            paymentItem.setPaymentItemExtra(paymentItemExtra);
                            break;

                        case EARNEST_DEDUCT:                            paymentItem.setPayModeId(model.getPayMode().value());                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));                            paymentItem.setPayModelGroup(PayModelGroup.OTHER);
                            paymentItem.setRefundWay(RefundWay.NONEED_REFUND);
                            paymentItem.setFaceAmount(model.getUsedValue());                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));                            paymentItem.setUsefulAmount(model.getUsedValue());                            break;

                        default:                            if (model.getPaymentModeShop() != null) {
                                paymentItem.setPayModeId(model.getPaymentModeShop().getErpModeId());                                paymentItem.setPayModeName(model.getPaymentModeShop().getName());                                paymentItem.setPayModelGroup(PayModelGroup.OTHER);
                                paymentItem.setRefundWay(RefundWay.NONEED_REFUND);
                                paymentItem.setFaceAmount(model.getUsedValue());                                paymentItem.setChangeAmount(BigDecimal.valueOf(0));                                if (restAmount >= model.getUsedValue().doubleValue()) {                                     paymentItem.setUsefulAmount(model.getUsedValue());                                } else {                                    paymentItem.setUsefulAmount(BigDecimal.valueOf(restAmount));                                    restAmount = 0;
                                }
                            }
                            break;
                    }
                                        if (restAmount > 0) {
                        restAmount = CashInfoManager.floatSubtract(restAmount, model.getUsedValue().doubleValue());
                    }
                }
            }
        }
    }

    private PaymentItemUnionCardReq getPaymentItemUnionpayCardReq(PosTransLog log, Long operatorId, String operatorName) {
        PaymentItemUnionCardReq cardReq = null;
        if (log != null) {
                        cardReq = new PaymentItemUnionCardReq();
            PaymentCard card = new PaymentCard();
            card.setCreatorId(operatorId);
            card.setCreatorName(operatorName);
            card.setCardNumber(log.getCardNumber());
            card.setCardName(log.getCardName());
            card.setExpireDate(log.getExpireDate());
            card.setIssNumber(log.getIssNumber());
            card.setIssName(log.getIssName());

                        PaymentDeviceReq device = new PaymentDeviceReq();
            device.setDeviceNumber(log.getTerminalNumber());
            if (PaySettingCache.getmErpComRel() != null) {
                device.setPosChannelId(PaySettingCache.getmErpComRel().getBankChannelId());
            }
                        PaymentItemUnionpayReq record = getPaymentItemUnionpayReq(PaySettingCache.getmErpComRel(), log, operatorId, operatorName);
            cardReq.setPaymentCard(card);
            cardReq.setPaymentDevice(device);
            cardReq.setRecord(record);
        }
        return cardReq;
    }

    private PaymentItemUnionpayReq getPaymentItemUnionpayReq(ErpCommercialRelation erpComRel, PosTransLog log,
                                                             Long operatorId, String operatorName
    ) {
        PaymentItemUnionpayReq req = new PaymentItemUnionpayReq();
        req.setCreatorId(operatorId);
        req.setCreatorName(operatorName);
        req.setUuid(SystemUtils.genOnlyIdentifier());
        req.setPaymentItemUuid(log.getUuid());
        req.setTransDate(DateTimeUtils.parseStringDateTime(log.getTransDate() + log.getTransTime()));
        req.setTransType(PosBusinessType.EXPENSE);
        if (log.getAmount() != null) {
            req.setAmount(log.getAmount());
            if (erpComRel != null) {
                Double backRates = erpComRel.getBankRates();
                if (backRates != null) {
                    req.setRates(backRates);                    Double fee = log.getAmount() * backRates;
                    req.setFee(MathDecimal.round(fee, 4));                } else {
                    req.setFee(0D);
                }
                req.setPosChannelId(erpComRel.getBankChannelId());
            }
        } else {
            req.setAmount(0);
            req.setFee(0D);
            if (erpComRel != null) {
                req.setRates(erpComRel.getBankRates());
                req.setPosChannelId(erpComRel.getBankChannelId());
            }
        }
        req.setHostSerialNumber(log.getHostSerialNumber());
        req.setPosTraceNumber(log.getPosTraceNumber());
        req.setBatchNumber(log.getBatchNumber());
        req.setTerminalNumber(log.getTerminalNumber());        req.setAppname(log.getAppName());
        return req;
    }
}
