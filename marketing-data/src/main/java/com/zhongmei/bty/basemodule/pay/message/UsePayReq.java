package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ShopUtils;
import com.zhongmei.bty.basemodule.customer.message.MemberValueReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.EntityCardValueReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.TmpCardValueReq;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PayType;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



public class UsePayReq {
    private Base base;    private Payment payment;
    public UsePayReq(NPayReq payReq) {
        this.base = toBase(payReq);
        this.payment = toPayment(payReq);
    }

    public UsePayReq(Trade trade, PaymentVo paymentVo) {
        this.base = toBase(trade);
        this.payment = toPayment(paymentVo);
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    private Base toBase(NPayReq payReq) {
        Base base = new Base();
        base.setOperateId(payReq.getOperateId());
        base.setOperateName(payReq.getOperateName());
        base.setTradeUuid(payReq.getTrade().getTradeUuid());
        base.setBizDate(ShopUtils.getBizDate().getTime());
        base.setBizType(ValueEnums.toEnum(BusinessType.class, payReq.getTrade().getBusinessType()));
        base.setTradeNo(payReq.getTrade().getTradeNo());
        return base;
    }

        private Base toBase(Trade trade) {
        Base base = new Base();
        AuthUser authUser = Session.getAuthUser();
        if (authUser != null) {
            base.setOperateId(authUser.getId());
            base.setOperateName(authUser.getName());
        }
        base.setTradeUuid(trade.getUuid());
        base.setBizDate(ShopUtils.getBizDate().getTime());
        base.setBizType(trade.getBusinessType());
        base.setTradeNo(trade.getTradeNo());
        return base;
    }

    private Payment toPayment(NPayReq payReq) {
        NPaymentReq newPaymentReq = payReq.getPayment();
        Payment payment = new Payment();
        List<PaymentItemReq> aliItemScanCodes = new ArrayList<PaymentItemReq>();        List<ShowCodeReq> aliItemShowCodes = new ArrayList<ShowCodeReq>();        List<PaymentItemReq> baiduItemScanCodes = new ArrayList<PaymentItemReq>();        List<ShowCodeReq> baiduItemShowCodes = new ArrayList<ShowCodeReq>();        List<PaymentItemReq> weixinItemScanCodes = new ArrayList<PaymentItemReq>();        List<ShowCodeReq> weixinItemShowCodes = new ArrayList<ShowCodeReq>();        List<ShowCodeReq> flashItemShowCodes = new ArrayList<ShowCodeReq>();        List<EntityCardValueReq> entityCardValues = new ArrayList<EntityCardValueReq>();        List<MemberValueReq> itemMemberValues = new ArrayList<MemberValueReq>();        List<TmpCardValueReq> tmpCardValues = new ArrayList<TmpCardValueReq>();        List<GrouponMeituanPayReq> itemGrouponMeituanPays = new ArrayList<GrouponMeituanPayReq>();        List<GrouponNuomiPayReq> itemGrouponNuomiPays = new ArrayList<GrouponNuomiPayReq>();
        List<NPaymentItemReq> paymentItems = newPaymentReq.getPaymentItemList();
        if (Utils.isNotEmpty(paymentItems)) {
            for (PaymentItem paymentItem : paymentItems) {
                PayModeId payModeId = ValueEnums.toEnum(PayModeId.class, paymentItem.getPayModeId());
                switch (payModeId) {
                    case ALIPAY:                        if (paymentItem.getPaymentItemExtra().getPayType() == PayType.SCAN) {
                            aliItemShowCodes.add(new ShowCodeReq(paymentItem));
                        } else if (paymentItem.getPaymentItemExtra().getPayType() == PayType.QCODE) {
                            aliItemScanCodes.add(new PaymentItemReq(paymentItem));
                        }
                        break;
                    case BAIFUBAO:                        if (paymentItem.getPaymentItemExtra().getPayType() == PayType.SCAN) {
                            baiduItemShowCodes.add(new ShowCodeReq(paymentItem));
                        } else if (paymentItem.getPaymentItemExtra().getPayType() == PayType.QCODE) {
                            baiduItemScanCodes.add(new PaymentItemReq(paymentItem));
                        }
                        break;
                    case WEIXIN_PAY:                        if (paymentItem.getPaymentItemExtra().getPayType() == PayType.SCAN) {
                            weixinItemShowCodes.add(new ShowCodeReq(paymentItem));
                        } else if (paymentItem.getPaymentItemExtra().getPayType() == PayType.QCODE) {
                            weixinItemScanCodes.add(new PaymentItemReq(paymentItem));
                        }
                        break;
                    case MEITUAN_FASTPAY:                        flashItemShowCodes.add(new ShowCodeReq(paymentItem));
                        break;
                    case ENTITY_CARD:                        entityCardValues.add(new EntityCardValueReq(paymentItem));
                        break;
                    case MEMBER_CARD:                        itemMemberValues.add(new MemberValueReq(paymentItem));
                        break;
                    case ANONYMOUS_ENTITY_CARD:                        tmpCardValues.add(new TmpCardValueReq(paymentItem));
                        break;
                    case MEITUAN_TUANGOU:                        itemGrouponMeituanPays.add(new GrouponMeituanPayReq(paymentItem));
                        break;
                    case BAINUO_TUANGOU:                        itemGrouponNuomiPays.add(new GrouponNuomiPayReq(paymentItem));
                        break;
                }
            }
        }
        payment.setPaymentInfo(payment.new PaymentInfo(newPaymentReq));
        payment.setAliItemScanCodes(aliItemScanCodes);
        payment.setAliItemShowCodes(aliItemShowCodes);
        payment.setBaiduItemScanCodes(baiduItemScanCodes);
        payment.setBaiduItemShowCodes(baiduItemShowCodes);
        payment.setWeixinItemScanCodes(weixinItemScanCodes);
        payment.setWeixinItemShowCodes(weixinItemShowCodes);
        payment.setFlashItemShowCodes(flashItemShowCodes);
        payment.setEntityCardValues(entityCardValues);
        payment.setItemMemberValues(itemMemberValues);
        payment.setTmpCardValues(tmpCardValues);
        payment.setItemGrouponMeituanPays(itemGrouponMeituanPays);
        payment.setItemGrouponNuomiPays(itemGrouponNuomiPays);

        return payment;
    }

        private UsePayReq.Payment toPayment(PaymentVo paymentVo) {
        Payment payment = new Payment();
        List<PaymentItemReq> aliItemScanCodes = new ArrayList<PaymentItemReq>();        List<ShowCodeReq> aliItemShowCodes = new ArrayList<ShowCodeReq>();        List<PaymentItemReq> baiduItemScanCodes = new ArrayList<PaymentItemReq>();        List<ShowCodeReq> baiduItemShowCodes = new ArrayList<ShowCodeReq>();        List<PaymentItemReq> weixinItemScanCodes = new ArrayList<PaymentItemReq>();        List<ShowCodeReq> weixinItemShowCodes = new ArrayList<ShowCodeReq>();        List<ShowCodeReq> flashItemShowCodes = new ArrayList<ShowCodeReq>();        List<EntityCardValueReq> entityCardValues = new ArrayList<EntityCardValueReq>();        List<MemberValueReq> itemMemberValues = new ArrayList<MemberValueReq>();        List<TmpCardValueReq> tmpCardValues = new ArrayList<TmpCardValueReq>();        List<GrouponMeituanPayReq> itemGrouponMeituanPays = new ArrayList<GrouponMeituanPayReq>();        List<GrouponNuomiPayReq> itemGrouponNuomiPays = new ArrayList<GrouponNuomiPayReq>();
        List<PaymentItem> paymentItems = paymentVo.getPaymentItemList();
        if (Utils.isNotEmpty(paymentItems)) {
            for (PaymentItem paymentItem : paymentItems) {
                PayModeId payModeId = ValueEnums.toEnum(PayModeId.class, paymentItem.getPayModeId());
                switch (payModeId) {
                    case ALIPAY:                        if (paymentItem.getPaymentItemExtra().getPayType() == PayType.SCAN) {
                            aliItemShowCodes.add(new ShowCodeReq(paymentItem));
                        } else if (paymentItem.getPaymentItemExtra().getPayType() == PayType.QCODE) {
                            aliItemScanCodes.add(new PaymentItemReq(paymentItem));
                        }
                        break;
                    case BAIFUBAO:                        if (paymentItem.getPaymentItemExtra().getPayType() == PayType.SCAN) {
                            baiduItemShowCodes.add(new ShowCodeReq(paymentItem));
                        } else if (paymentItem.getPaymentItemExtra().getPayType() == PayType.QCODE) {
                            baiduItemScanCodes.add(new PaymentItemReq(paymentItem));
                        }
                        break;
                    case WEIXIN_PAY:                        if (paymentItem.getPaymentItemExtra().getPayType() == PayType.SCAN) {
                            weixinItemShowCodes.add(new ShowCodeReq(paymentItem));
                        } else if (paymentItem.getPaymentItemExtra().getPayType() == PayType.QCODE) {
                            weixinItemScanCodes.add(new PaymentItemReq(paymentItem));
                        }
                        break;
                    case MEITUAN_FASTPAY:                        flashItemShowCodes.add(new ShowCodeReq(paymentItem));
                        break;
                    case ENTITY_CARD:                        entityCardValues.add(new EntityCardValueReq(paymentItem));
                        break;
                    case MEMBER_CARD:                        itemMemberValues.add(new MemberValueReq(paymentItem));
                        break;
                    case ANONYMOUS_ENTITY_CARD:                        tmpCardValues.add(new TmpCardValueReq(paymentItem));
                        break;
                    case MEITUAN_TUANGOU:                        itemGrouponMeituanPays.add(new GrouponMeituanPayReq(paymentItem));
                        break;
                    case BAINUO_TUANGOU:                        itemGrouponNuomiPays.add(new GrouponNuomiPayReq(paymentItem));
                        break;
                }
            }
        }
        payment.setPaymentInfo(payment.new PaymentInfo(paymentVo));
        payment.setAliItemScanCodes(aliItemScanCodes);
        payment.setAliItemShowCodes(aliItemShowCodes);
        payment.setBaiduItemScanCodes(baiduItemScanCodes);
        payment.setBaiduItemShowCodes(baiduItemShowCodes);
        payment.setWeixinItemScanCodes(weixinItemScanCodes);
        payment.setWeixinItemShowCodes(weixinItemShowCodes);
        payment.setFlashItemShowCodes(flashItemShowCodes);
        payment.setEntityCardValues(entityCardValues);
        payment.setItemMemberValues(itemMemberValues);
        payment.setTmpCardValues(tmpCardValues);
        payment.setItemGrouponMeituanPays(itemGrouponMeituanPays);
        payment.setItemGrouponNuomiPays(itemGrouponNuomiPays);

        return payment;
    }

    public static class Base {
        private Long bizDate;        private Integer bizType;        private Long operateId;        private String operateName;        private String tradeUuid;        private String tradeNo;
        public Long getBizDate() {
            return bizDate;
        }

        public void setBizDate(Long bizDate) {
            this.bizDate = bizDate;
        }

        public BusinessType getBizType() {
            return ValueEnums.toEnum(BusinessType.class, bizType);
        }

        public void setBizType(BusinessType bizType) {
            this.bizType = ValueEnums.toValue(bizType);
        }

        public Long getOperateId() {
            return operateId;
        }

        public void setOperateId(Long operateId) {
            this.operateId = operateId;
        }

        public String getOperateName() {
            return operateName;
        }

        public void setOperateName(String operateName) {
            this.operateName = operateName;
        }

        public String getTradeUuid() {
            return tradeUuid;
        }

        public void setTradeUuid(String tradeUuid) {
            this.tradeUuid = tradeUuid;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }
    }

    public class Payment {
        private List<PaymentItemReq> aliItemScanCodes;        private List<ShowCodeReq> aliItemShowCodes;        private List<PaymentItemReq> baiduItemScanCodes;        private List<ShowCodeReq> baiduItemShowCodes;        private List<EntityCardValueReq> entityCardValues;        private List<ShowCodeReq> flashItemShowCodes;        private List<GrouponMeituanPayReq> itemGrouponMeituanPays;        private List<GrouponNuomiPayReq> itemGrouponNuomiPays;        private List<MemberValueReq> itemMemberValues;        private PaymentInfo paymentInfo;        private List<TmpCardValueReq> tmpCardValues;        private List<PaymentItemReq> weixinItemScanCodes;        private List<ShowCodeReq> weixinItemShowCodes;
        public List<PaymentItemReq> getAliItemScanCodes() {
            return aliItemScanCodes;
        }

        public void setAliItemScanCodes(List<PaymentItemReq> aliItemScanCodes) {
            this.aliItemScanCodes = aliItemScanCodes;
        }

        public List<ShowCodeReq> getAliItemShowCodes() {
            return aliItemShowCodes;
        }

        public void setAliItemShowCodes(List<ShowCodeReq> aliItemShowCodes) {
            this.aliItemShowCodes = aliItemShowCodes;
        }

        public List<PaymentItemReq> getBaiduItemScanCodes() {
            return baiduItemScanCodes;
        }

        public void setBaiduItemScanCodes(List<PaymentItemReq> baiduItemScanCodes) {
            this.baiduItemScanCodes = baiduItemScanCodes;
        }

        public List<ShowCodeReq> getBaiduItemShowCodes() {
            return baiduItemShowCodes;
        }

        public void setBaiduItemShowCodes(List<ShowCodeReq> baiduItemShowCodes) {
            this.baiduItemShowCodes = baiduItemShowCodes;
        }

        public List<EntityCardValueReq> getEntityCardValues() {
            return entityCardValues;
        }

        public void setEntityCardValues(List<EntityCardValueReq> entityCardValues) {
            this.entityCardValues = entityCardValues;
        }

        public List<ShowCodeReq> getFlashItemShowCodes() {
            return flashItemShowCodes;
        }

        public void setFlashItemShowCodes(List<ShowCodeReq> flashItemShowCodes) {
            this.flashItemShowCodes = flashItemShowCodes;
        }

        public List<GrouponMeituanPayReq> getItemGrouponMeituanPays() {
            return itemGrouponMeituanPays;
        }

        public void setItemGrouponMeituanPays(List<GrouponMeituanPayReq> itemGrouponMeituanPays) {
            this.itemGrouponMeituanPays = itemGrouponMeituanPays;
        }

        public List<GrouponNuomiPayReq> getItemGrouponNuomiPays() {
            return itemGrouponNuomiPays;
        }

        public void setItemGrouponNuomiPays(List<GrouponNuomiPayReq> itemGrouponNuomiPays) {
            this.itemGrouponNuomiPays = itemGrouponNuomiPays;
        }

        public List<MemberValueReq> getItemMemberValues() {
            return itemMemberValues;
        }

        public void setItemMemberValues(List<MemberValueReq> itemMemberValues) {
            this.itemMemberValues = itemMemberValues;
        }

        public PaymentInfo getPaymentInfo() {
            return paymentInfo;
        }

        public void setPaymentInfo(PaymentInfo paymentInfo) {
            this.paymentInfo = paymentInfo;
        }

        public List<TmpCardValueReq> getTmpCardValues() {
            return tmpCardValues;
        }

        public void setTmpCardValues(List<TmpCardValueReq> tmpCardValues) {
            this.tmpCardValues = tmpCardValues;
        }

        public List<PaymentItemReq> getWeixinItemScanCodes() {
            return weixinItemScanCodes;
        }

        public void setWeixinItemScanCodes(List<PaymentItemReq> weixinItemScanCodes) {
            this.weixinItemScanCodes = weixinItemScanCodes;
        }

        public List<ShowCodeReq> getWeixinItemShowCodes() {
            return weixinItemShowCodes;
        }

        public void setWeixinItemShowCodes(List<ShowCodeReq> weixinItemShowCodes) {
            this.weixinItemShowCodes = weixinItemShowCodes;
        }

        public class PaymentInfo {
            private BigDecimal actualAmount;            private BigDecimal exemptAmount;            private Integer paymentType;            private BigDecimal receivableAmount;            private String uuid;
            public PaymentInfo(PaymentVo paymentVo) {
                actualAmount = paymentVo.getPayment().getActualAmount();
                exemptAmount = paymentVo.getPayment().getExemptAmount();
                paymentType = ValueEnums.toValue(paymentVo.getPayment().getPaymentType());
                receivableAmount = paymentVo.getPayment().getReceivableAmount();
                uuid = paymentVo.getPayment().getUuid();
            }

            public PaymentInfo(NPaymentReq newPaymentReq) {
                actualAmount = newPaymentReq.getActualAmount();
                exemptAmount = newPaymentReq.getExemptAmount();
                paymentType = newPaymentReq.getPaymentType();
                receivableAmount = newPaymentReq.getReceivableAmount();
                uuid = newPaymentReq.getUuid();
            }

            public BigDecimal getActualAmount() {
                return actualAmount;
            }

            public void setActualAmount(BigDecimal actualAmount) {
                this.actualAmount = actualAmount;
            }

            public BigDecimal getExemptAmount() {
                return exemptAmount;
            }

            public void setExemptAmount(BigDecimal exemptAmount) {
                this.exemptAmount = exemptAmount;
            }

            public PaymentType getPaymentType() {
                return ValueEnums.toEnum(PaymentType.class, paymentType);
            }

            public void setPaymentType(PaymentType paymentType) {
                this.paymentType = ValueEnums.toValue(paymentType);
            }

            public BigDecimal getReceivableAmount() {
                return receivableAmount;
            }

            public void setReceivableAmount(BigDecimal receivableAmount) {
                this.receivableAmount = receivableAmount;
            }

            public String getUuid() {
                return uuid;
            }

            public void setUuid(String uuid) {
                this.uuid = uuid;
            }
        }

    }
}
