package com.zhongmei.bty.basemodule.devices.mispos.data.bean;

import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.customer.enums.CustomerAppConfig;
import com.zhongmei.bty.basemodule.customer.operates.interfaces.CustomerDal;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PayModelGroup;
import com.zhongmei.yunfu.db.enums.PaySource;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.enums.RefundWay;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.DomainType;
import com.zhongmei.yunfu.db.enums.IssueStatus;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.db.enums.SourceChild;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayForm;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.context.util.SystemUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date：2016-4-5 下午4:32:39
 * @Description: 用于处理部分退卡
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class ReturnCardDataModel {
    private Map<Long, CardKindVo> mDataMap;

    private List<CardNumber> mCards;

    private BigDecimal mTradeAmount;

    private int mKindCount;

    private CustomerDal mCustomerDal;

    public ReturnCardDataModel(CustomerDal dal) {
        mCustomerDal = dal;
        mDataMap = new HashMap<Long, CardKindVo>();
        mCards = new ArrayList<CardNumber>();
    }

    public void initData(List<CustomerSaleCardInfo> list) {
        mDataMap.clear();
        mCards.clear();
        mKindCount = 0;
        mTradeAmount = BigDecimal.ZERO;
        if (list != null && !list.isEmpty()) {
            for (CustomerSaleCardInfo cardInfo : list) {
                mCards.add(new CardNumber(cardInfo.getCardNo(), cardInfo.getCardKindId(), cardInfo.getTradeItemId()));
                mTradeAmount = mTradeAmount.add(cardInfo.getCardCost());
//				if (cardInfo.getPrice() != null && cardInfo.getPrice().compareTo(BigDecimal.ZERO) > 0) {
//					mTradeAmount = mTradeAmount.add(cardInfo.getPrice());
//				}
                addDataToMap(cardInfo);
            }
        }
    }

    /**
     * 生成CardNumber对象
     */
    public CardNumber getCardNumber(String cardNo, Long cardKindId, Long srcTradeItemId) {
        return new CardNumber(cardNo, cardKindId, srcTradeItemId);
    }

    private void addDataToMap(CustomerSaleCardInfo cardInfo) {
        Long key = cardInfo.getCardKindId();
        if (mDataMap.get(key) != null) {
            mDataMap.get(key).addData(cardInfo);
        } else {
            CardKindVo vo = new CardKindVo(key);
            vo.addData(cardInfo);
            mDataMap.put(key, vo);
            mKindCount++;
        }
    }

    /**
     * @param businessType 业态区分
     * @return
     * @see CustomerAppConfig.CustomerBussinessType
     */
    public TradeVo createTradeVo(Integer businessType) {
        TradeVo tradeVo = new TradeVo();
        Trade trade = creatTrade(businessType);
        tradeVo.setTrade(trade);
        int sort = 0;
        CardKindVo vo = null;
        List<TradeItemVo> tradeItemVolist = new ArrayList<TradeItemVo>();
        tradeVo.setTradeItemList(tradeItemVolist);
        for (Long key : mDataMap.keySet()) {
            vo = mDataMap.get(key);
            String tradeItemUuid = SystemUtils.genOnlyIdentifier();
            //往mcards里关联tradeItemUuid
            for (CardNumber cardNumber : mCards) {
                if (cardNumber.getCardKindId().equals(key)) {
                    cardNumber.setRefundTradeItemUuid(tradeItemUuid);
                }
            }
            tradeItemVolist.add(createTradeItemVo(tradeItemUuid, sort, trade, vo));
            sort++;
        }
        return tradeVo;
    }

    public TradeVo createTradeVo() {
        return createTradeVo(CustomerAppConfig.CustomerBussinessType.DINNER); // 默认正餐
    }

    public List<PaymentVo> getReturnPayment(Trade trade) {

        List<PaymentVo> paymentVoList = new ArrayList<PaymentVo>(1);

        PaymentVo paymenVo = null;

        if (trade != null) {
            paymenVo = new PaymentVo();
            paymentVoList.add(paymenVo);

            Payment payment = new Payment();

            payment.validateCreate();

            final List<PaymentItem> paymentItemList = new ArrayList<PaymentItem>();
            // 收银员
            String _operatorName =
                    Session.getAuthUser() != null ? Session.getAuthUser()
                            .getName() : trade.getCreatorName();
            long _operatorId =
                    Session.getAuthUser() != null ? Session.getAuthUser()
                            .getId() : trade.getCreatorId();

            // 设置支付信息
            payment.setReceivableAmount(trade.getTradeAmount());// 可收金额

            payment.setActualAmount(trade.getTradeAmount());// 实际收金额

            payment.setExemptAmount(BigDecimal.ZERO);// 抹零金额
            payment.setRelateUuid(trade.getUuid());// 主单uuid
            payment.setRelateId(trade.getId());// 关联id
            payment.setPaymentType(PaymentType.TRADE_REFUND);// 交易支付
            // 生成PaymentUUID
            payment.setUuid(SystemUtils.getBillNumber());
            payment.setCreatorId(_operatorId);// 收银员id
            payment.setCreatorName(_operatorName);// 收银员名字
            payment.setUpdatorId(_operatorId);// 收银员id
            payment.setUpdatorName(_operatorName);// 收银员名字

            paymenVo.setPayment(payment);// 支付主单
            paymenVo.setPaymentItemList(paymentItemList);// 支付明细列表

            PaymentItem paymentItem = new PaymentItem();
            paymentItem.setPaySource(PaySource.CASHIER);
            paymentItem.validateCreate();
            // 生成UUID并缓存
            paymentItem.setUuid(SystemUtils.getBillNumber());
            paymentItem.setPaymentUuid(payment.getUuid());
            paymentItem.setPayModeId(PayModeId.CASH.value());
            paymentItem.setPayModelGroup(PayModelGroup.CASH);
            paymentItem.setRefundWay(RefundWay.NONEED_REFUND);
            // 支付类型名称
            paymentItem.setPayModeName(PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()));

            paymentItem.setFaceAmount(trade.getTradeAmount());// 票面金额
            paymentItem.setChangeAmount(BigDecimal.ZERO);// 找零金额
            paymentItem.setUsefulAmount(trade.getTradeAmount());// 实付款
            paymentItem.setCreatorId(_operatorId);// 收银员id
            paymentItem.setCreatorName(_operatorName);// 收银员名字

            paymentItemList.add(paymentItem);
        }
        return paymentVoList;
    }

    public List<CardNumber> getCardList() {
        return mCards;
    }

    private Trade creatTrade(Integer businessType) {
        Trade trade = creatTrade();
        if (businessType == CustomerAppConfig.CustomerBussinessType.BEAUTY) {
            trade.setDomainType(DomainType.BEAUTY);
        }
        return trade;
    }

    private Trade creatTrade() {
        Trade trade = new Trade();
        trade.validateCreate();
        trade.validateUpdate();
        trade.setDomainType(DomainType.RESTAURANT);
        trade.setBusinessType(BusinessType.CARD);
        trade.setTradeType(TradeType.REFUND);//退货单
        trade.setTradeTime((new Date()).getTime());
        trade.setTradeStatus(TradeStatus.CONFIRMED);
        trade.setDeliveryType(DeliveryType.HERE);
        trade.setSource(SourceId.POS);
        trade.setSourceChild(SourceChild.ANDROID);
        trade.setTradeNo(SystemUtils.getBillNumber());
        trade.setDishKindCount(mKindCount);
        trade.setSaleAmount(mTradeAmount.negate());
        trade.setTradeAmount(mTradeAmount.negate());
        trade.setPrivilegeAmount(BigDecimal.ZERO);
        trade.setUuid(SystemUtils.genOnlyIdentifier());
        trade.setTradePeopleCount(1);
        trade.setTradePayStatus(TradePayStatus.UNPAID);
        trade.setTradePayForm(TradePayForm.OFFLINE);
        return trade;
    }

    private TradeItemVo createTradeItemVo(String tradeItemUuid, final int sort, final Trade trade, final CardKindVo vo) {

        TradeItemVo tradeItemVo = new TradeItemVo();

        tradeItemVo.setTradeItem(createTradeItem(tradeItemUuid, sort, trade.getUuid(), vo));

        return tradeItemVo;
    }

    private TradeItem createTradeItem(String tradeItemUuid, final int sort, final String tradeUuid, final CardKindVo vo) {
        DishShop dishShop = mCustomerDal.findDishopByEcCardKindId(vo.getCardKindId());
        TradeItem tradeItem = new TradeItem();
        tradeItem.validateCreate();
        tradeItem.setTradeUuid(tradeUuid);
        tradeItem.setUuid(tradeItemUuid);
        if (dishShop != null) {
            tradeItem.setDishId(dishShop.getId());
            tradeItem.setDishName(dishShop.getName());
            tradeItem.setSkuUuid(dishShop.getUuid());
        } else {
            tradeItem.setDishId(0L);
            tradeItem.setDishName("not find dishshop");
            tradeItem.setSkuUuid(SystemUtils.genOnlyIdentifier());
        }
        tradeItem.setSort(sort);
        tradeItem.setPrice(vo.getPrice());
        tradeItem.setQuantity(BigDecimal.valueOf(vo.getCount()).negate());
        tradeItem.setAmount(vo.getAmount().negate());
        tradeItem.setPropertyAmount(BigDecimal.ZERO);
        tradeItem.setActualAmount(vo.getAmount().negate());
        tradeItem.setFeedsAmount(BigDecimal.ZERO);
        tradeItem.setType(DishType.CARD);
        tradeItem.setIssueStatus(IssueStatus.DIRECTLY);
        tradeItem.setEnableWholePrivilege(Bool.YES);
        // tradeItem.setUnitName(unitName);
        tradeItem.setStatusFlag(StatusFlag.VALID);
        return tradeItem;
    }

    private class CardKindVo {

        private Long CardKindId;

        private BigDecimal Amount = BigDecimal.ZERO;

        private BigDecimal Price;

        private List<CustomerSaleCardInfo> BaseData;// 基础数据

        public CardKindVo(Long cardKindId) {
            CardKindId = cardKindId;
            BaseData = new ArrayList<CustomerSaleCardInfo>();
        }

        public void addData(CustomerSaleCardInfo cardinfo) {
            Amount = Amount.add(cardinfo.getCardCost());
            BaseData.add(cardinfo);
            if (Price == null)
                Price = cardinfo.getCardCost();
        }

        public BigDecimal getAmount() {
            return this.Amount;
        }

        public List<CustomerSaleCardInfo> getData() {
            return BaseData;
        }

        public long getCount() {

            return BaseData == null ? 0 : BaseData.size();
        }

        public boolean isEmpty() {
            if (BaseData == null) {
                return true;
            } else {
                return BaseData.isEmpty();
            }
        }

        public Long getCardKindId() {
            return CardKindId;
        }

        public BigDecimal getPrice() {
            return Price;
        }
    }

    public class CardNumber {
        private String cardNo;

        private Long cardKindId;

        private Long srcTradeItemId;

        private String refundTradeItemUuid;

        private CardNumber(String cardNo, Long cardKindId, Long srcTradeItemId) {
            this.cardNo = cardNo;
            this.cardKindId = cardKindId;
            this.srcTradeItemId = srcTradeItemId;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public Long getSrcTradeItemId() {
            return srcTradeItemId;
        }

        public void setSrcTradeItemId(Long srcTradeItemId) {
            this.srcTradeItemId = srcTradeItemId;
        }

        public String getRefundTradeItemUuid() {
            return refundTradeItemUuid;
        }

        public void setRefundTradeItemUuid(String refundTradeItemUuid) {
            this.refundTradeItemUuid = refundTradeItemUuid;
        }

        public Long getCardKindId() {
            return cardKindId;
        }

        public void setCardKindId(Long cardKindId) {
            this.cardKindId = cardKindId;
        }

    }
}
