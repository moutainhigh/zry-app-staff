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

/**
 * 处理输入的各种支付数据.
 */

public class PayModelTool {
    private IPaymentInfo paymentInfo;
    private List<PayModelItem> payModelItemList;
    private double exemptAmount;
    private String operatorName;
    private long operatorId;
    private Payment paidPayment;//已经付款的记录

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
                    payment.setShopActualAmount(null);//如果服务器产生商家实收，提交数据时清空
                }
            } else {
                payment = new Payment();
                payment.validateCreate();
                // 生成PaymentUUID并缓存
                payment.setUuid(paymentInfo.getPaymentUuid());
            }

            final List<PaymentItem> paymentItemList = new ArrayList<PaymentItem>();
            // 收银员
            operatorName = Session.getAuthUser() != null
                    ? Session.getAuthUser().getName() : trade.getCreatorName();
            operatorId = Session.getAuthUser() != null
                    ? Session.getAuthUser().getId() : trade.getCreatorId();

            // 设置支付信息
            payment.setReceivableAmount(paymentInfo.getTradeVo().getTrade().getTradeAmount());// 可收金额
            payment.setBeforePrivilegeAmount(paymentInfo.getTradeVo().getBeforePrivilegeAmount()); //订单优惠前金额
            double actualAmount = CashInfoManager
                    .floatSubtract(paymentInfo.getTradeVo().getTrade().getTradeAmount().doubleValue(), paymentInfo.getExemptAmount());
            payment.setActualAmount(BigDecimal.valueOf(actualAmount));// 实际该收金额(应收-抹零)
            payment.setExemptAmount(BigDecimal.valueOf(this.exemptAmount));
            payment.setRelateUuid(trade.getUuid());// 主单uuid
            payment.setRelateId(trade.getId());// 关联id
            //支付类型
            BusinessType businessType = paymentInfo.getTradeBusinessType();
            PaymentType paymentType = PaymentType.TRADE_SELL;
            if (businessType != null) {
                switch (businessType) {
                    case ONLINE_RECHARGE://会员虚拟卡充值
                        paymentType = PaymentType.MEMBER_RECHARGE;
                        break;
                    case CARD_RECHARGE://会员实体卡充值
                        paymentType = PaymentType.ENTITY_CARD_RECHARGE;
                        break;
                    case ANONYMOUS_ENTITY_CARD_SELL_AND_RECHARGE://匿名卡售卡储值
                        paymentType = PaymentType.ANONYMOUS_ENTITY_CARD_SELL_RECHARGE;
                        break;
                    case ANONYMOUS_ENTITY_CARD_RECHARGE://匿名卡储值
                        paymentType = PaymentType.ANONYMOUS_ENTITY_CARD_RECHARGE;
                        break;
                    default:
                        if (businessType == ANONYMOUS_ENTITY_CARD_SELL && ServerSettingCache.getInstance().isJinChBusiness()) {
                            // 金城商户 临时卡售卡
                            paymentType = PaymentType.ANONYMOUS_ENTITY_CARD_SELL;

                        } else {
                            paymentType = PaymentType.TRADE_SELL;
                        }
                        break;
                }
            }
            payment.setPaymentType(paymentType);// 交易支付
            paymenVo.setPayment(payment);// 支付主单
            paymenVo.setPaymentItemList(paymentItemList);// 支付明细列表
            createPaymentItem(payment, paymentItemList);
        }
        return paymenVo;
    }

    private void createPaymentItem(Payment payment, List<PaymentItem> paymentItemList) {
        if (!Utils.isEmpty(payModelItemList)) {
            double restAmount = paymentInfo.getActualAmount();// 未收金额初始值为实收
            for (PayModelItem model : payModelItemList) {

                if (model.getPayModelId() != null) {

                    PaymentItemExtra paymentItemExtra = null;
                    PaymentItem paymentItem = new PaymentItem();
                    paymentItemList.add(paymentItem);
                    paymentItem.setPaySource(PaySource.CASHIER);//支付来源
                    paymentItem.setUuid(model.getUuid());
                    paymentItem.setPaymentUuid(payment.getUuid());
                    if (paymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT || paymentInfo.getPayScene() == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) { //add 20170706 for deposit 1 支付押金 2不是支付押金  默认2
                        paymentItem.setIsDeposit(1);
                    }
                    switch (model.getPayMode()) {
                        case CASH://现金
                            paymentItem.setPayModeId(model.getPayMode().value());//支付方式
                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));//支付方式名称
                            paymentItem.setPayModelGroup(PayModelGroup.CASH);//支付类型
                            paymentItem.setRefundWay(RefundWay.NONEED_REFUND);//退款类型
                            paymentItem.setFaceAmount(model.getUsedValue());// 票面金额
                            paymentItem.setChangeAmount(model.getChangeAmount());// 找零金额
                            //modify begin 20170418 start 支持溢收
                            if (this.paymentInfo.getActualAmount() >= model.getUsedValue().doubleValue()) {
                                paymentItem.setUsefulAmount(MathDecimal.round(model.getUsedValue(), 2));// 应付款
                            } else {//溢收或找零
                                paymentItem.setUsefulAmount(BigDecimal.valueOf(this.paymentInfo.getActualAmount()));// 应付款
                            }
                            //modify begin 20170418 end
                            break;

                        case BANK_CARD://银行卡(记账)
                            paymentItem.setPayModeId(model.getPayMode().value());//支付方式
                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));//支付方式名称
                            paymentItem.setPayModelGroup(PayModelGroup.BANK_CARD);
                            paymentItem.setRefundWay(RefundWay.NONEED_REFUND);
                            paymentItem.setFaceAmount(model.getUsedValue());// 票面金额
                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额
                            paymentItem.setUsefulAmount(model.getUsedValue());// 实付款
                            break;
                        case OTHER_WX_PAY:
                        case OTHER_ALI_PAY:
                            paymentItem.setPayModeId(model.getPayMode().value());//支付方式
                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));//支付方式名称
                            paymentItem.setPayModelGroup(PayModelGroup.OTHER);
                            paymentItem.setRefundWay(RefundWay.NONEED_REFUND);
                            paymentItem.setFaceAmount(model.getUsedValue());// 票面金额
                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额
                            paymentItem.setUsefulAmount(model.getUsedValue());// 实付款
                            break;
                        case POS_CARD://银联pos刷卡
                            paymentItem.setPayModeId(model.getPayMode().value());//支付方式
                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));//支付方式名称
                            paymentItem.setPayModelGroup(PayModelGroup.BANK_CARD);
                            paymentItem.setRefundWay(RefundWay.HAND_REFUND);
                            paymentItem.setFaceAmount(model.getUsedValue());// 票面金额
                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额
                            paymentItem.setUsefulAmount(model.getUsedValue());// 实付款
                            PaymentItemUnionCardReq cardReq = getPaymentItemUnionpayCardReq(model.getPosTransLog(), operatorId, operatorName);
                            paymentItem.setPaymentItemUnionPay(cardReq);
                            break;
                        case WEIXIN_PAY://微信支付
                        case ALIPAY://支付宝
                        case BAIFUBAO://百度钱包
                        case MEITUAN_FASTPAY://美团闪付
                        case UNIONPAY_CLOUD_PAY://银联云闪付 add v8.11
                        case ICBC_E_PAY://工商e支付 add v8.11
                        case MOBILE_PAY: //移动支付 add v8.12
                        case DIANXIN_YIPAY://翼支付 addv8.16
                            paymentItem.setPayModeId(model.getPayMode().value());//支付方式
                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));//支付方式名称
                            paymentItem.setPayModelGroup(PayModelGroup.ONLINE);
                            paymentItem.setRefundWay(RefundWay.AUTO_REFUND);
                            paymentItem.setFaceAmount(model.getUsedValue());// 票面金额
                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额
                            paymentItem.setUsefulAmount(model.getUsedValue());// 实付款
                            if (model.getPayType() == PayType.SCAN) {//主扫
                                paymentItem.setAuthCode(model.getAuthCode());
                            }
                            //美团闪惠需要不参与优惠金额
                            if (model.getPayMode() == PayModeId.MEITUAN_FASTPAY) {
                                paymentItem.setNoDiscountAmount(model.getNoDiscountAmount());//
                            }
                            paymentItemExtra = new PaymentItemExtra();
                            paymentItemExtra.setUuid(model.getUuid());
                            paymentItemExtra.setPayType(model.getPayType());
                            paymentItem.setPaymentItemExtra(paymentItemExtra);
                            break;
                        case MEMBER_CARD://会员虚拟卡余额
                            paymentItem.setPayModeId(model.getPayMode().value());//支付方式
                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));//支付方式名称
                            paymentItem.setPayModelGroup(PayModelGroup.VALUE_CARD);
                            paymentItem.setRefundWay(RefundWay.AUTO_REFUND);
                            paymentItem.setRelateId(paymentInfo.getCustomerId() + "");// 会员id
                            paymentItem.setFaceAmount(model.getUsedValue());// 票面金额
                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额
                            paymentItem.setUsefulAmount(model.getUsedValue());// 实付款
                            if (model.getPasswordType() != null)//add 20170612 for customer passowrd type
                            {
                                paymentItem.setType(model.getPasswordType().value());
                            }
                            paymentItem.setConsumePassword(paymentInfo.getMemberPassword());
                            paymentItemExtra = new PaymentItemExtra();
                            paymentItemExtra.setUuid(model.getUuid());
                            paymentItemExtra.setCustomerId(paymentInfo.getCustomerId());//如果是虚拟卡就不传会员id
                            if (ServerSettingCache.getInstance().isJinChBusiness()) {
                                paymentItemExtra.setEntityNo(paymentInfo.getEcCard().getCardNum());
                            }
                            paymentItemExtra.setPayType(model.getPayType());
                            paymentItem.setPaymentItemExtra(paymentItemExtra);
                            break;
                        case ENTITY_CARD://会员实体卡
                            paymentItem.setPayModeId(model.getPayMode().value());//支付方式
                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));//支付方式名称
                            paymentItem.setPayModelGroup(PayModelGroup.VALUE_CARD);
                            paymentItem.setRefundWay(RefundWay.AUTO_REFUND);
                            paymentItem.setRelateId(paymentInfo.getEcCard().getCustomer().getCustomerid() + "");// 会员id
                            paymentItem.setFaceAmount(model.getUsedValue());// 票面金额
                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额
                            paymentItem.setUsefulAmount(model.getUsedValue());// 实付款
                            if (paymentInfo.getEcCard().getCardKind().getIsNeedPwd() == Bool.YES) {
                                paymentItem.setConsumePassword(paymentInfo.getMemberPassword());
                            }
                            paymentItemExtra = new PaymentItemExtra();
                            paymentItemExtra.setUuid(SystemUtils.genOnlyIdentifier());
                            paymentItemExtra.setCustomerId(paymentInfo.getEcCard().getCustomer().getCustomerid());//传入实体卡绑定的会员ID号
                            paymentItemExtra.setEntityNo(paymentInfo.getEcCard().getCardNum());//实体卡就传入实体卡卡号
                            paymentItem.setPaymentItemExtra(paymentItemExtra);
                            break;
                        case ANONYMOUS_ENTITY_CARD://匿名卡余额
                            paymentItem.setPayModeId(model.getPayMode().value());//支付方式
                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));//支付方式名称
                            paymentItem.setPayModelGroup(PayModelGroup.VALUE_CARD);
                            paymentItem.setRefundWay(RefundWay.AUTO_REFUND);
                            paymentItem.setRelateId(paymentInfo.getEcCard().getCardNum());// 匿名卡应该传入卡号
                            paymentItem.setFaceAmount(model.getUsedValue());// 票面金额
                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额
                            paymentItem.setUsefulAmount(model.getUsedValue());// 实付款

                            paymentItemExtra = new PaymentItemExtra();
                            paymentItemExtra.setUuid(SystemUtils.genOnlyIdentifier());
                            paymentItemExtra.setEntityNo(paymentInfo.getEcCard().getCardNum());//实体卡就传入实体卡卡号
                            paymentItem.setPaymentItemExtra(paymentItemExtra);
                            break;

                        case MEITUAN_TUANGOU://美团团购
                            paymentItem.setPayModeId(model.getPayMode().value());//支付方式
                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));//支付方式名称
                            paymentItem.setPayModelGroup(PayModelGroup.OTHER);
                            paymentItem.setRelateId(model.getTuanGouCouponDetail().getSerialNumber());
                            paymentItem.setFaceAmount(model.getFaceValue());// 美团团购券市场价 add 8.3
                            paymentItem.setRefundWay(RefundWay.HAND_REFUND);
                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额
                            //modify begin 20170418 start 支持溢收
                            if (this.paymentInfo.getActualAmount() >= model.getUsedValue().doubleValue()) {
                                paymentItem.setUsefulAmount(MathDecimal.round(model.getUsedValue(), 2));// 应付款
                            } else {//溢收或找零
                                paymentItem.setUsefulAmount(BigDecimal.valueOf(this.paymentInfo.getActualAmount()));// 应付款
                            }
                            //modify begin 20170418 end
                            //美团团购券详细信息
                            PaymentItemGroupon pig = new PaymentItemGroupon();
                            pig.setGrouponId(model.getTuanGouCouponDetail().getGrouponId());
                            pig.setDealTitle(model.getTuanGouCouponDetail().getDealTitle());
                            pig.setMarketPrice(model.getTuanGouCouponDetail().getMarketPrice());
                            pig.setPrice(model.getTuanGouCouponDetail().getPrice());
                            pig.setUseCount(model.getUsedCount());
                            pig.setSerialNo(model.getTuanGouCouponDetail().getSerialNumber());
                            paymentItem.setPaymentItemGroupon(pig);
                            // payment.setShopActualAmount(model.getActualValue());
                            break;
                        case BAINUO_TUANGOU://百度糯米券
                        case KOUBEI_TUANGOU://口碑券
                            paymentItem.setPayModeId(model.getPayMode().value());//支付方式
                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));//支付方式名称
                            paymentItem.setPayModelGroup(PayModelGroup.OTHER);
                            paymentItem.setRelateId(model.getTuanGouCouponDetail().getSerialNumber());
                            paymentItem.setFaceAmount(model.getUsedValue());// 点评券市场价
                            paymentItem.setRefundWay(RefundWay.HAND_REFUND);
                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额
                            //modify begin 20170418 start 支持溢收
                            if (this.paymentInfo.getActualAmount() >= model.getUsedValue().doubleValue()) {
                                paymentItem.setUsefulAmount(MathDecimal.round(model.getUsedValue(), 2));// 应付款
                            } else {//溢收或找零
                                paymentItem.setUsefulAmount(BigDecimal.valueOf(this.paymentInfo.getActualAmount()));// 应付款
                            }
                            //modify begin 20170418 end
                            //百度糯米券详细信息
                            PaymentItemGroupon paymentItemGroupon = new PaymentItemGroupon();
                            paymentItemGroupon.setGrouponId(model.getTuanGouCouponDetail().getGrouponId());
                            paymentItemGroupon.setDealTitle(model.getTuanGouCouponDetail().getDealTitle());
                            paymentItemGroupon.setMarketPrice(model.getTuanGouCouponDetail().getMarketPrice());
                            paymentItemGroupon.setPrice(model.getTuanGouCouponDetail().getPrice());
                            paymentItemGroupon.setUseCount(model.getUsedCount());
                            paymentItemGroupon.setSerialNo(model.getTuanGouCouponDetail().getSerialNumber());
                            paymentItem.setPaymentItemGroupon(paymentItemGroupon);

                            break;
                        case JIN_CHENG://金诚APP
                            paymentItem.setPayModeId(model.getPayMode().value());//支付方式
                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));//支付方式名称
                            paymentItem.setPayModelGroup(PayModelGroup.OTHER);
                            paymentItem.setRefundWay(RefundWay.AUTO_REFUND);
                            paymentItem.setFaceAmount(model.getUsedValue());// 票面金额
                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额
                            paymentItem.setUsefulAmount(model.getUsedValue());// 实付款
                            if (model.getPayType() == PayType.SCAN) {//主扫
                                paymentItem.setAuthCode(model.getAuthCode());
                            }
                            paymentItemExtra = new PaymentItemExtra();
                            paymentItemExtra.setUuid(SystemUtils.genOnlyIdentifier());
                            paymentItemExtra.setPayType(model.getPayType());
                            paymentItem.setPaymentItemExtra(paymentItemExtra);
                            break;
                        case JIN_CHENG_VALUE_CARD://金诚充值卡
                            paymentItem.setPayModeId(model.getPayMode().value());//支付方式
                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));//支付方式名称
                            paymentItem.setPayModelGroup(PayModelGroup.OTHER);
                            paymentItem.setRefundWay(RefundWay.AUTO_REFUND);
                            paymentItem.setRelateId(paymentInfo.getCustomerId() + "");// 会员id
                            paymentItem.setFaceAmount(model.getUsedValue());// 票面金额
                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额
                            paymentItem.setUsefulAmount(model.getUsedValue());// 实付款
                            if (model.getPasswordType() != null) {
                                paymentItem.setType(model.getPasswordType().value());
                            }
                            paymentItem.setConsumePassword(paymentInfo.getMemberPassword());
                            paymentItemExtra = new PaymentItemExtra();
                            paymentItemExtra.setUuid(SystemUtils.genOnlyIdentifier());
                            paymentItemExtra.setEntityNo(paymentInfo.getEcCard().getCardNum());
                            paymentItemExtra.setPayType(model.getPayType());
                            paymentItem.setPaymentItemExtra(paymentItemExtra);
                            break;

                        case FENGHUO_WRISTBAND://烽火手环 v8.7
                            paymentItem.setPayModeId(model.getPayMode().value());//支付方式
                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));//支付方式名称
                            paymentItem.setPayModelGroup(PayModelGroup.OTHER);
                            paymentItem.setRefundWay(RefundWay.AUTO_REFUND);
                            paymentItem.setFaceAmount(model.getUsedValue());// 票面金额
                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额
                            paymentItem.setUsefulAmount(model.getUsedValue());// 实付款
                            if (model.getPasswordType() != null) {
                                paymentItem.setType(model.getPasswordType().value());
                            }
                            paymentItem.setConsumePassword(paymentInfo.getMemberPassword());

                            paymentItemExtra = new PaymentItemExtra();
                            paymentItemExtra.setUuid(SystemUtils.genOnlyIdentifier());
                            if (paymentInfo.getEcCard() != null)
                                paymentItemExtra.setEntityNo(paymentInfo.getEcCard().getCardNum());//手环id
                            paymentItem.setPaymentItemExtra(paymentItemExtra);
                            break;

                        case EARNEST_DEDUCT://订金抵扣 add 8.13
                            paymentItem.setPayModeId(model.getPayMode().value());//支付方式
                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));//支付方式名称
                            paymentItem.setPayModelGroup(PayModelGroup.OTHER);
                            paymentItem.setRefundWay(RefundWay.NONEED_REFUND);
                            paymentItem.setFaceAmount(model.getUsedValue());// 票面金额
                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额
                            paymentItem.setUsefulAmount(model.getUsedValue());// 实付款
                            break;

                        default://其它自定义支付
                            if (model.getPaymentModeShop() != null) {
                                paymentItem.setPayModeId(model.getPaymentModeShop().getErpModeId());//支付方式Id
                                paymentItem.setPayModeName(model.getPaymentModeShop().getName());//支付方式名称
                                paymentItem.setPayModelGroup(PayModelGroup.OTHER);
                                paymentItem.setRefundWay(RefundWay.NONEED_REFUND);
                                paymentItem.setFaceAmount(model.getUsedValue());// 点评券市场价
                                paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额
                                if (restAmount >= model.getUsedValue().doubleValue()) { // 不满足溢收
                                    paymentItem.setUsefulAmount(model.getUsedValue());// 实付款
                                } else {// 满足溢收
                                    paymentItem.setUsefulAmount(BigDecimal.valueOf(restAmount));// 实付款
                                    restAmount = 0;
                                }
                            }
                            break;
                    }
                    //剩余金额
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
            //卡信息
            cardReq = new PaymentItemUnionCardReq();
            PaymentCard card = new PaymentCard();
            card.setCreatorId(operatorId);
            card.setCreatorName(operatorName);
            card.setCardNumber(log.getCardNumber());
            card.setCardName(log.getCardName());
            card.setExpireDate(log.getExpireDate());
            card.setIssNumber(log.getIssNumber());
            card.setIssName(log.getIssName());

            // 设备终端号
            PaymentDeviceReq device = new PaymentDeviceReq();
            device.setDeviceNumber(log.getTerminalNumber());
            if (PaySettingCache.getmErpComRel() != null) {
                device.setPosChannelId(PaySettingCache.getmErpComRel().getBankChannelId());
            }
            //交易信息
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
                    req.setRates(backRates);// 费率
                    Double fee = log.getAmount() * backRates;
                    req.setFee(MathDecimal.round(fee, 4));// 手续费
                } else {
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
        req.setTerminalNumber(log.getTerminalNumber());// 设备终端号
        req.setAppname(log.getAppName());
        return req;
    }
}
