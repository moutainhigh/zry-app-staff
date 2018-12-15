package com.zhongmei.bty.mobilepay.bean;

import static com.zhongmei.yunfu.db.enums.BusinessType.ANONYMOUS_ENTITY_CARD_SELL;

import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.pay.bean.PaymentCard;
import com.zhongmei.bty.mobilepay.bean.meituan.MeituanDishItem;
import com.zhongmei.bty.mobilepay.bean.meituan.MeituanDishItemVo;
import com.zhongmei.bty.mobilepay.bean.meituan.MeituanDishVo;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.yunfu.db.entity.trade.PaymentItemExtra;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGrouponDish;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGroupon;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PayModelGroup;
import com.zhongmei.yunfu.db.enums.PaySource;
import com.zhongmei.yunfu.db.enums.PayType;
import com.zhongmei.bty.basemodule.pay.message.PaymentDeviceReq;
import com.zhongmei.bty.basemodule.pay.message.PaymentItemUnionCardReq;
import com.zhongmei.bty.basemodule.pay.message.PaymentItemUnionpayReq;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.db.enums.RefundWay;
import com.zhongmei.bty.basemodule.trade.message.EarnestDeductReq;
import com.zhongmei.bty.basemodule.trade.message.PrePayRefundReq;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.db.entity.ErpCommercialRelation;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.commonmodule.database.entity.local.PosTransLog;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.bty.commonmodule.database.enums.PosBusinessType;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.pay.message.NPaymentItemReq;
import com.zhongmei.bty.basemodule.pay.message.NPaymentReq;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 构建V3支付请求数据
 * Created by demo on 2018/12/15
 */

public class PaymentReqTool {
    private IPaymentInfo paymentInfo;
    private List<PayModelItem> payModelItemList;
    private double exemptAmount;
    private String operatorName;
    private long operatorId;
    private Payment paidPayment;//已经付款的记录

    public PaymentReqTool(IPaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
        this.payModelItemList = paymentInfo.getOtherPay().getAllPayModelItems();
        this.exemptAmount = paymentInfo.getExemptAmount();
        this.paidPayment = paymentInfo.getPaidPayment();
    }

    public NPaymentReq creatPaymentReq() {
        NPaymentReq paymentReq = null;
        Trade trade = null;
        if ((trade = paymentInfo.getTradeVo().getTrade()) != null) {
            paymentReq = new NPaymentReq();

            if (this.paidPayment != null) {
                paymentReq.setUuid(this.paidPayment.getUuid());
            } else {
                // 生成PaymentUUID并缓存
                paymentReq.setUuid(paymentInfo.getPaymentUuid());
            }

            final List<NPaymentItemReq> paymentItemList = new ArrayList<NPaymentItemReq>();
            // 收银员
            operatorName = Session.getAuthUser() != null
                    ? Session.getAuthUser().getName() : trade.getCreatorName();
            operatorId = Session.getAuthUser() != null
                    ? Session.getAuthUser().getId() : trade.getCreatorId();

            // 设置支付信息
            paymentReq.setReceivableAmount(paymentInfo.getTradeVo().getTrade().getTradeAmount());// 可收金额
            double actualAmount = CashInfoManager
                    .floatSubtract(paymentInfo.getTradeVo().getTrade().getTradeAmount().doubleValue(), paymentInfo.getExemptAmount());
            paymentReq.setActualAmount(BigDecimal.valueOf(actualAmount));// 实际该收金额(应收-抹零)
            paymentReq.setExemptAmount(BigDecimal.valueOf(this.exemptAmount));
            paymentReq.setShopActualAmount(BigDecimal.valueOf(actualAmount));
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
            paymentReq.setPaymentType(paymentType.value());// 交易支付
            paymentReq.setPaymentItemList(paymentItemList);// 支付明细列表
            createPaymentItem(paymentReq, paymentItemList);
        }
        return paymentReq;
    }

    private void createPaymentItem(NPaymentReq payment, List<NPaymentItemReq> paymentItemList) {
        if (!Utils.isEmpty(payModelItemList)) {
            double restAmount = paymentInfo.getActualAmount();// 未收金额初始值为实收
            for (PayModelItem model : payModelItemList) {

                if (model.getPayModelId() != null) {

                    PaymentItemExtra paymentItemExtra = null;
                    NPaymentItemReq paymentItem = new NPaymentItemReq();
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
                        case MOBILE_PAY://移动支付 add v8.12
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
                            PaymentItemGroupon pig = createPaymentItemGroupon(model);//modify v8.9
                           /* pig.setGrouponId(model.getTuanGouCouponDetail().getGrouponId());
                            pig.setDealTitle(model.getTuanGouCouponDetail().getDealTitle());
                            pig.setMarketPrice(model.getTuanGouCouponDetail().getMarketPrice());
                            pig.setPrice(model.getTuanGouCouponDetail().getPrice());
                            pig.setUseCount(model.getUsedCount());
                            pig.setSerialNo(model.getTuanGouCouponDetail().getSerialNumber());*/
                            paymentItem.setPaymentItemGroupon(pig);
                            // payment.setShopActualAmount(model.getActualValue());
                            if (model.getMeituanDishVo() != null) {//add v8.3
                                paymentItem.setPaymentItemGrouponDish(creatPaymentItemGroupDishs(model.getMeituanDishVo(), this.paymentInfo.getTradeVo().getTrade().getId(), model.getUuid(), model.getTuanGouCouponDetail().getSerialNumber()));
                            }
                            break;
                        case BAINUO_TUANGOU://百度糯米券
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
                            PaymentItemGroupon paymentItemGroupon = createPaymentItemGroupon(model);//modify v8.9
                           /* paymentItemGroupon.setGrouponId(model.getTuanGouCouponDetail().getGrouponId());
                            paymentItemGroupon.setDealTitle(model.getTuanGouCouponDetail().getDealTitle());
                            paymentItemGroupon.setMarketPrice(model.getTuanGouCouponDetail().getMarketPrice());
                            paymentItemGroupon.setPrice(model.getTuanGouCouponDetail().getPrice());
                            paymentItemGroupon.setUseCount(model.getUsedCount());
                            paymentItemGroupon.setSerialNo(model.getTuanGouCouponDetail().getSerialNumber());*/
                            paymentItem.setPaymentItemGroupon(paymentItemGroupon);

                            break;

                        case KOUBEI_TUANGOU://口碑券add v8.9
                            paymentItem.setPayModeId(model.getPayMode().value());//支付方式
                            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));//支付方式名称
                            paymentItem.setPayModelGroup(PayModelGroup.OTHER);
                            paymentItem.setRelateId(model.getTuanGouCouponDetail().getSerialNumber());
                            paymentItem.setFaceAmount(model.getUsedValue());// 点评券市场价
                            paymentItem.setRefundWay(RefundWay.HAND_REFUND);
                            paymentItem.setChangeAmount(BigDecimal.valueOf(0));// 找零金额
                            if (this.paymentInfo.getActualAmount() >= model.getUsedValue().doubleValue()) {
                                paymentItem.setUsefulAmount(MathDecimal.round(model.getUsedValue(), 2));// 应付款
                            } else {//溢收或找零
                                paymentItem.setUsefulAmount(BigDecimal.valueOf(this.paymentInfo.getActualAmount()));// 应付款
                            }
                            //口碑券详细信息
                            PaymentItemGroupon paymentItemGrouponKoubei = createPaymentItemGroupon(model);
                            paymentItem.setPaymentItemGroupon(paymentItemGrouponKoubei);

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
                            paymentItemExtra.setEntityNo(model.getDeviceId());//手环id
                            paymentItem.setPaymentItemExtra(paymentItemExtra);
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

    //add v8.9 构建第3方团购券信息
    private PaymentItemGroupon createPaymentItemGroupon(PayModelItem model) {
        if (model == null) return null;

        PaymentItemGroupon paymentItemGroupon = new PaymentItemGroupon();
        paymentItemGroupon.setGrouponId(model.getTuanGouCouponDetail().getGrouponId());
        paymentItemGroupon.setDealTitle(model.getTuanGouCouponDetail().getDealTitle());
        paymentItemGroupon.setMarketPrice(model.getTuanGouCouponDetail().getMarketPrice());
        paymentItemGroupon.setPrice(model.getTuanGouCouponDetail().getPrice());
        paymentItemGroupon.setUseCount(model.getUsedCount());
        paymentItemGroupon.setSerialNo(model.getTuanGouCouponDetail().getSerialNumber());

        return paymentItemGroupon;
    }

    //add v8.3  美团券与菜品关联关系
    private List<PaymentItemGrouponDish> creatPaymentItemGroupDishs(MeituanDishVo meituanDishVo, Long tradeId, String paymentItemUuid, String serialNo) {
        List<PaymentItemGrouponDish> dishList = null;
        if (meituanDishVo != null && Utils.isNotEmpty(meituanDishVo.matchDishItemVoList)) {

            for (MeituanDishItemVo meituanDishItemVo : meituanDishVo.matchDishItemVoList) {
                if (Utils.isNotEmpty(meituanDishItemVo.meituanItemVoList)) {
                    for (MeituanDishItem mdItem : meituanDishItemVo.meituanItemVoList) {
                        if (mdItem.isNeedRelate) {
                            if (dishList == null)
                                dishList = new ArrayList<PaymentItemGrouponDish>();
                            PaymentItemGrouponDish groupDish = new PaymentItemGrouponDish();
                            dishList.add(groupDish);
                            groupDish.setTradeId(tradeId);
                            groupDish.setTradeItemUuid(mdItem.tradeItemUuid);
                            //groupDish.setDishUuid(mdItem.skuUuid);
                            groupDish.setDishId(mdItem.skuId);
                            groupDish.setDishNum(mdItem.num);
                            groupDish.setPaymentItemUuid(paymentItemUuid);
                            groupDish.setSerialNo(serialNo);
                        }
                    }
                }
            }
        }
        return dishList;
    }

    // add v8.13  构建订单抵扣上行数据
    public EarnestDeductReq createEarnestDeductReq() {
        EarnestDeductReq req = new EarnestDeductReq();
        Trade trade = paymentInfo.getTradeVo().getTrade();
        req.tradeId = trade.getId();
        // 收银员
        req.operateName = Session.getAuthUser() != null ? Session.getAuthUser().getName() : trade.getCreatorName();
        req.operateId = Session.getAuthUser() != null ? Session.getAuthUser().getId() : trade.getCreatorId();
        req.exemptAmount = BigDecimal.valueOf(this.exemptAmount);
        return req;
    }

    public PrePayRefundReq createPrePayRefundReq() {
        PrePayRefundReq refundReq = new PrePayRefundReq();
        refundReq.setTradeId(paymentInfo.getTradeVo().getTrade().getId());
        refundReq.setClientUpdateTime(new Date().getTime());
        refundReq.setServerUpdateTime(paymentInfo.getTradeVo().getTrade().getServerUpdateTime());
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            refundReq.setUpdatorId(user.getId());
            refundReq.setUpdatorName(user.getName());
        }
        Double restAmount = CashInfoManager.floatSubtract(paymentInfo.getTradeVo().getTradeEarnestMoney(), paymentInfo.getActualAmount());
        Double deductionAmount = paymentInfo.getTradeVo().getTradeEarnestMoney();
        Double exemptAmount = paymentInfo.getExemptAmount();
        refundReq.setRefundAmount(BigDecimal.valueOf(restAmount));
        refundReq.setDeductionAmount(BigDecimal.valueOf(deductionAmount));
        refundReq.setExemptAmount(BigDecimal.valueOf(exemptAmount));
        return refundReq;
    }
}
