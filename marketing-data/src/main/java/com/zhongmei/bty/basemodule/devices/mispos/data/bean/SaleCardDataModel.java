package com.zhongmei.bty.basemodule.devices.mispos.data.bean;

import com.zhongmei.bty.basemodule.customer.enums.CustomerAppConfig;
import com.zhongmei.bty.basemodule.customer.operates.interfaces.CustomerDal;
import com.zhongmei.bty.basemodule.devices.mispos.data.CardSaleInfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardInfo;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.DomainType;
import com.zhongmei.yunfu.db.enums.IssueStatus;
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
 * @Date：2016-3-15 上午11:29:08
 * @Description: 处理售卡数据
 * @Version: 1.0
 */
public class SaleCardDataModel {
    private Map<Long, CardKindVo> mDataMap;

    private BigDecimal mTradeAmount;

    private int mKindCount;

    private CustomerDal mCustomerDal;

    public SaleCardDataModel(CustomerDal dal) {
        mCustomerDal = dal;
        mDataMap = new HashMap<Long, CardKindVo>();
    }

    private void initData(List<EcCardInfo> list) {
        mDataMap.clear();
        mKindCount = 0;
        mTradeAmount = BigDecimal.ZERO;
        if (list != null && !list.isEmpty()) {
            for (EcCardInfo cardInfo : list) {
                if (cardInfo.getCardKindId() != null) {
                    mTradeAmount = mTradeAmount.add(cardInfo.getCardCost());
                    if (cardInfo.getPrice() != null && cardInfo.getPrice().compareTo(BigDecimal.ZERO) > 0) {
                        mTradeAmount = mTradeAmount.add(cardInfo.getPrice());
                    }
                    addDataToMap(cardInfo);
                }
            }
        }
    }

    private void addDataToMap(EcCardInfo cardInfo) {
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
     * @param chargeMoney
     * @param businessType         业务形态
     * @param customerBusinessType 顾客业态区分  美业 or 其他
     * @return
     * @see CustomerAppConfig.CustomerBussinessType
     */
    public TradeVo createMemberRechargeTradeVo(BigDecimal chargeMoney, BusinessType businessType, Integer customerBusinessType) {
        TradeVo tradeVo = new TradeVo();
        Trade trade = creatTrade(customerBusinessType);
        tradeVo.setTrade(trade);
        trade.setTradeAmount(chargeMoney);
        trade.setTradeAmountBefore(chargeMoney);
        trade.setSaleAmount(chargeMoney);
        trade.setBusinessType(businessType);
        return tradeVo;
    }

    public TradeVo createMemberRechargeTradeVo(BigDecimal chargeMoney, BusinessType businessType) {
        return createMemberRechargeTradeVo(chargeMoney, businessType, CustomerAppConfig.CustomerBussinessType.DINNER);
    }

    public TradeVo createTradeVo(Integer businessType, final List<EcCardInfo> list) {
        initData(list);
        TradeVo tradeVo = new TradeVo();
        /*
         * TradeExtra tradeExtra = new TradeExtra();
         * tradeExtra.validateCreate();
         * tradeExtra.setUuid(SystemUtil
         * .getSystemUtil().genOnlyIdentifier());
         * tradeVo.setTradeExtra(tradeExtra);
         */

        Trade trade = creatTrade(businessType);
        tradeVo.setTrade(trade);
        int sort = 0;
        CardKindVo vo = null;
        List<TradeItemVo> tradeItemVolist = new ArrayList<TradeItemVo>();
        tradeVo.setTradeItemList(tradeItemVolist);
        for (Long key : mDataMap.keySet()) {
            vo = mDataMap.get(key);
            tradeItemVolist.add(createTradeItemVo(sort, trade, vo));
            sort++;
        }
        return tradeVo;
    }

    public TradeVo createTradeVo(final List<EcCardInfo> list) {
        return createTradeVo(CustomerAppConfig.CustomerBussinessType.DINNER, list);// 默认正餐
    }

    public TradeVo AddSaleTradeItem(TradeVo vo, EcCardInfo info) {
        TradeItemVo tradeItemVo = createTradeItemVo(1, vo.getTrade(), mDataMap.get(info.getCardKindId()));
        tradeItemVo.getTradeItem().setType(DishType.ANONYMOUS_ENTITY_CARD_SELL);
        tradeItemVo.getTradeItem().setAmount(info.getCardCost());
        vo.getTradeItemList().add(tradeItemVo);
        return vo;
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
        trade.setTradeType(TradeType.SELL);
        trade.setTradeTime((new Date()).getTime());
        trade.setTradeStatus(TradeStatus.CONFIRMED);
        trade.setDeliveryType(DeliveryType.HERE);
        trade.setSource(SourceId.POS);
        trade.setSourceChild(SourceChild.ANDROID);
        trade.setTradeNo(SystemUtils.getBillNumber());
        trade.setDishKindCount(mKindCount);
        trade.setSaleAmount(mTradeAmount);
        trade.setTradeAmount(mTradeAmount);
        trade.setPrivilegeAmount(BigDecimal.ZERO);
        trade.setUuid(SystemUtils.genOnlyIdentifier());
        trade.setTradePeopleCount(1);
        trade.setTradePayStatus(TradePayStatus.UNPAID);
        trade.setTradePayForm(TradePayForm.OFFLINE);
        return trade;
    }

    private TradeItemVo createTradeItemVo(final int sort, final Trade trade, final CardKindVo vo) {

        TradeItemVo tradeItemVo = new TradeItemVo();

        tradeItemVo.setTradeItem(createTradeItem(sort, trade.getUuid(), vo));

        tradeItemVo.setCardSaleInfos(getCardSaleInfosByCardKindVo(vo));

        return tradeItemVo;
    }

    private List<CardSaleInfo> getCardSaleInfosByCardKindVo(final CardKindVo vo) {
        List<CardSaleInfo> list = null;
        if (vo != null && !vo.isEmpty()) {
            list = new ArrayList<CardSaleInfo>();
            CardSaleInfo saleInfo = null;
            for (EcCardInfo cardInfo : vo.getData()) {
                saleInfo = new CardSaleInfo();
                saleInfo.setCardCost(cardInfo.getCardCost());
                saleInfo.setCardKindId(cardInfo.getCardKindId());
                saleInfo.setCardKindName(cardInfo.getCardKindName());
                saleInfo.setCardNum(cardInfo.getCardNum());
                saleInfo.setCardStatus(ValueEnums.toValue(cardInfo.getCardStatus()));
                list.add(saleInfo);
            }
        }
        return list;
    }

    private TradeItem createTradeItem(final int sort, final String tradeUuid, final CardKindVo vo) {
        DishShop dishShop = mCustomerDal.findDishopByEcCardKindId(vo.getCardKindId());
        TradeItem tradeItem = new TradeItem();
        tradeItem.validateCreate();
        // tradeItem.validateUpdate();
        tradeItem.setTradeUuid(tradeUuid);
        tradeItem.setUuid(SystemUtils.genOnlyIdentifier());
        if (dishShop != null) {
            tradeItem.setDishId(dishShop.getId());
            tradeItem.setDishName(dishShop.getName());
            tradeItem.setSkuUuid(dishShop.getUuid());
        } /*else {
			tradeItem.setSkuId(10L);
			tradeItem.setSkuName("haha cards");
			tradeItem.setSkuUuid(SystemUtils.genOnlyIdentifier());
		}*/
        tradeItem.setSort(sort);
        tradeItem.setPrice(vo.getPrice());
        tradeItem.setQuantity(BigDecimal.valueOf(vo.getCount()));
        tradeItem.setAmount(vo.getAmount());
        tradeItem.setPropertyAmount(BigDecimal.ZERO);
        tradeItem.setActualAmount(vo.getAmount());
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

        private List<EcCardInfo> BaseData;// 基础数据

        public CardKindVo(Long cardKindId) {
            CardKindId = cardKindId;
            BaseData = new ArrayList<EcCardInfo>();
        }

        public void addData(EcCardInfo cardinfo) {
            BigDecimal cardPrice = BigDecimal.ZERO; // 卡售价
            if (cardinfo.getPrice() != null) {
                cardPrice = cardinfo.getPrice();
            }
            Amount = Amount.add(cardinfo.getCardCost().add(cardPrice));
            BaseData.add(cardinfo);
            if (Price == null) {
                Price = cardinfo.getCardCost().add(cardPrice);
            }
        }

        public BigDecimal getAmount() {
            return this.Amount;
        }

        public List<EcCardInfo> getData() {
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
}
