package com.zhongmei.bty.basemodule.trade.message.uniontable;

import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.orderdish.bean.DishMenuVo;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.bty.basemodule.trade.bean.DepositInfo;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.DomainType;
import com.zhongmei.yunfu.db.enums.SourceChild;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayForm;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */

public class BuffetUnionTradeReq {

    public Trade tradeCreateRequest;
    public TradeDeposit deposit;
    public TradeItem menuTradeItem;//餐标
    public List<TradeBuffetPeople> peoples;
    public List<EmptyTableTrade> emptyTableTrades;//空桌台构建订单信息
    public List<BTrade> trades;//已有订单
    public List<TradeTax> taxs;//消费税
    public List<TradeUser> tradeUsers;
    public List<TradeInitConfig> tradeInitConfigs;

    public BuffetUnionTradeReq(BigDecimal subTradesToatalAmount, DishMenuVo dishMenuVo, List<TradeBuffetPeople> subTradeBuffetPeoples,
                               List<Tables> emptyTablesList, List<Trade> subTrades) {
        //获取总人数
        BigDecimal totalPeopleCount = getTotalPeopleCount(subTradeBuffetPeoples);

        //押金＋子单saleamount之和＋消费税＋空桌餐标价格
        tradeCreateRequest = createTradeCreateRequest(subTradesToatalAmount);
        deposit = createDeposit(tradeCreateRequest.getUuid(), totalPeopleCount);
        menuTradeItem = createMenuTradeItem(dishMenuVo, totalPeopleCount, subTradeBuffetPeoples);
        peoples = createTradeBuffetPeople(tradeCreateRequest.getUuid(), subTradeBuffetPeoples);
        emptyTableTrades = createEmptyTableTrades(emptyTablesList);
        trades = createBTrades(subTrades);


        //this.taxs = createTradeTax();
    }

    public void setTaxs(List<TradeTax> taxs) {
        this.taxs = taxs;
    }

    public void setTradeInitConfigs(List<TradeInitConfig> tradeInitConfigs) {
        this.tradeInitConfigs = tradeInitConfigs;
    }

    private BigDecimal getTotalPeopleCount(List<TradeBuffetPeople> subTradeBuffetPeoples) {
        BigDecimal totalPeopleCount = BigDecimal.ZERO;
        if (Utils.isNotEmpty(subTradeBuffetPeoples)) {
            for (TradeBuffetPeople tradeBuffetPeople : subTradeBuffetPeoples) {
                if (tradeBuffetPeople.getPeopleCount() != null) {
                    totalPeopleCount = totalPeopleCount.add(tradeBuffetPeople.getPeopleCount());
                }
            }
        }

        return totalPeopleCount;
    }

    private Trade createTradeCreateRequest(BigDecimal amount) {
        Trade tradeCreateRequest = new Trade();
        tradeCreateRequest.validateCreate();
        tradeCreateRequest.setUuid(SystemUtils.genOnlyIdentifier());
        tradeCreateRequest.setTradeNo(SystemUtils.getBillNumber());

        // 所属领域:1:RESTAURANT:餐饮业
        tradeCreateRequest.setDomainType(DomainType.RESTAURANT);

        tradeCreateRequest.setBusinessType(BusinessType.BUFFET);
        tradeCreateRequest.setTradePayForm(TradePayForm.OFFLINE);
        tradeCreateRequest.setStatusFlag(StatusFlag.VALID);

        // 来源。如店内:1、微信等
        // Long sourceId;
        tradeCreateRequest.setSource(SourceId.POS);
        // 1:ANDROID 2:IPAD 3:官微 4:商微
        tradeCreateRequest.setSourceChild(SourceChild.ANDROID);

        // 支付状态：1:UNPAID:未支付
        // 2:PAYING:支付中，微信下单选择了在线支付但实际上未完成支付的 3:PAID:已支付
        tradeCreateRequest.setTradePayStatus(TradePayStatus.UNPAID);

        // 默认订单类型为内用
        tradeCreateRequest.setDeliveryType(DeliveryType.HERE);

        // 各种优惠折扣的减免金额，销货时为负数，退货时为正数
        tradeCreateRequest.setPrivilegeAmount(new BigDecimal(0));

        // 退货所对应的销货单
        // Long relateTradeId;
        // 原单UUID
        // String relateTradeUuid;

        // 销售金额，明细SALE_AMOUNT之和
        tradeCreateRequest.setSaleAmount(amount);

        // 单号
        // String seqNo;

        // 交易金额，等于SALE_AMOUNT与PRIVILEGE_AMOUNT之和
        tradeCreateRequest.setTradeAmountBefore(amount);
        tradeCreateRequest.setTradeAmount(amount);

        // 交易状态：1:UNPROCESSED:未处理 2:TEMPORARY:挂单，不需要厨房打印
        // 3:CONFIRMED:已确认 4:FINISH:已完成(全部支付)
        // 5:RETURNED:已退货 6:INVALID:已作废 7:REFUSED:已拒绝
        tradeCreateRequest.setTradeStatus(TradeStatus.CONFIRMED);

        // 商品种类，默认传0
        tradeCreateRequest.setDishKindCount(0);

        // 交易时间
        // java.util.Date tradeTime;
        tradeCreateRequest.setTradeTime((new Date()).getTime());

        // 交易类型 1:SELL:售货 2:REFUND:退货
        // com.zhongmei.bty.commonmodule.database.enums.TradeType
        // tradeType;
        tradeCreateRequest.setTradeType(TradeType.SELL);
        tradeCreateRequest.setBizDate(DateTimeUtils.getCurrentDayStart());

        return tradeCreateRequest;
    }

    private TradeDeposit createDeposit(String tradeUuid, BigDecimal peopleCount) {
        if (peopleCount == null) {
            return null;
        }

        boolean depositEnable = ServerSettingCache.getInstance().getBuffetDepositEnable();
        DepositInfo depositInfo = ServerSettingCache.getInstance().getBuffetTradeDeposit();
        if (depositEnable && depositInfo != null) {
            TradeDeposit deposit = new TradeDeposit();
            deposit.validateCreate();
            deposit.setUuid(SystemUtils.genOnlyIdentifier());
            deposit.setTradeUuid(tradeUuid);
            deposit.setType(depositInfo.getType());
            deposit.setUnitPrice(depositInfo.getValue());

            if (deposit.getType() == DepositInfo.TYPE_BY_TRADE) {
                deposit.setDepositPay(deposit.getUnitPrice());
            } else {
                deposit.setDepositPay(deposit.getUnitPrice().multiply(peopleCount));
            }

            return deposit;
        }

        return null;
    }

    private List<TradeBuffetPeople> createTradeBuffetPeople(String tradeUuid, List<TradeBuffetPeople> subTradeBuffetPeoples) {
        Map<Long, TradeBuffetPeople> tradeBuffetPeopleMap = new HashMap<>();
        if (Utils.isNotEmpty(subTradeBuffetPeoples)) {
            for (TradeBuffetPeople subTradeBuffetPeople : subTradeBuffetPeoples) {
                Long dishCarteNormId = subTradeBuffetPeople.getCarteNormsId();
                TradeBuffetPeople tbp = tradeBuffetPeopleMap.get(dishCarteNormId);
                if (tbp == null) {
                    tbp = new TradeBuffetPeople();
                    tbp.setPeopleCount(subTradeBuffetPeople.getPeopleCount());
                    tbp.setCarteNormsId(subTradeBuffetPeople.getCarteNormsId());
                    tbp.setCarteNormsName(subTradeBuffetPeople.getCarteNormsName());
                    tbp.setCartePrice(subTradeBuffetPeople.getCartePrice());
                    tradeBuffetPeopleMap.put(subTradeBuffetPeople.getCarteNormsId(), tbp);
                } else {
                    BigDecimal oldPeopleCount = tbp.getPeopleCount() == null ? BigDecimal.ZERO : tbp.getPeopleCount();
                    BigDecimal subPeopleCount = subTradeBuffetPeople.getPeopleCount() == null ? BigDecimal.ZERO : subTradeBuffetPeople.getPeopleCount();
                    tbp.setPeopleCount(oldPeopleCount.add(subPeopleCount));
                }
            }

            List<TradeBuffetPeople> tradeBuffetPeoples = new ArrayList<>();

            for (TradeBuffetPeople tbp : tradeBuffetPeopleMap.values()) {
                TradeBuffetPeople tradeBuffetPeople = new TradeBuffetPeople();
                tradeBuffetPeople.validateCreate();
                tradeBuffetPeople.setTradeUuid(tradeUuid);
                tradeBuffetPeople.setPeopleCount(tbp.getPeopleCount());
                tradeBuffetPeople.setCarteNormsId(tbp.getCarteNormsId());
                tradeBuffetPeople.setCarteNormsName(tbp.getCarteNormsName());
                tradeBuffetPeople.setCartePrice(tbp.getCartePrice());

                tradeBuffetPeoples.add(tradeBuffetPeople);
            }

            return tradeBuffetPeoples;
        }

        return null;
    }

    private TradeItem createMenuTradeItem(DishMenuVo dishMenuVo, BigDecimal count, List<TradeBuffetPeople> subTradeBuffetPeoples) {
        if (dishMenuVo == null) {
            return null;
        }

        BigDecimal amount = BigDecimal.ZERO;
        for (TradeBuffetPeople tradeBuffetPeople : subTradeBuffetPeoples) {
            if (tradeBuffetPeople.getPeopleCount().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            amount = amount.add(tradeBuffetPeople.getCartePrice().multiply(tradeBuffetPeople.getPeopleCount()));
        }
        menuTradeItem = new TradeItem();
        menuTradeItem.validateCreate();
        menuTradeItem.validateUpdate();
        menuTradeItem.setPrice(dishMenuVo.getPrice());
        menuTradeItem.setAmount(amount);
        menuTradeItem.setActualAmount(amount);
        menuTradeItem.setDishId(dishMenuVo.getSkuId());
        menuTradeItem.setDishName(dishMenuVo.getSkuName());
        menuTradeItem.setSkuUuid(dishMenuVo.getSkuUuid());
//        mTradeItem.setTradeMemo(mDishMenuVo.getName());
        menuTradeItem.setStatusFlag(StatusFlag.VALID);
        menuTradeItem.setType(DishType.BUFFET_COMBO_SHELL);
        menuTradeItem.setPropertyAmount(BigDecimal.ZERO);
        menuTradeItem.setQuantity(count);
        menuTradeItem.setUuid(SystemUtils.genOnlyIdentifier());
        menuTradeItem.setSort(0);

        return menuTradeItem;
    }

    private List<EmptyTableTrade> createEmptyTableTrades(List<Tables> tablesList) {
        if (Utils.isNotEmpty(tablesList)) {
            /*设置登录操作员为默认服务员*/
            AuthUser user = Session.getAuthUser();
            List<EmptyTableTrade> emptyTableTrades = new ArrayList<>();
            for (Tables tables : tablesList) {
                EmptyTableTrade emptyTableTrade = new EmptyTableTrade();
                emptyTableTrade.skuKindCount = 0;
                emptyTableTrade.tableId = tables.getId();
                emptyTableTrade.tradeNo = SystemUtils.getBillNumber();
                emptyTableTrade.uuid = SystemUtils.genOnlyIdentifier();
                emptyTableTrade.tradePeopleCount = new BigDecimal(tables.getTablePersonCount());
                if (user != null) {
                    emptyTableTrade.waiterId = user.getId();
                    emptyTableTrade.waiterName = user.getName();
                }
                emptyTableTrades.add(emptyTableTrade);
            }
            return emptyTableTrades;
        }

        return null;
    }

    private List<BTrade> createBTrades(List<Trade> subTrades) {
        if (Utils.isNotEmpty(subTrades)) {
            List<BTrade> trades = new ArrayList<>();
            for (Trade trade : subTrades) {
                BTrade bTrade = new BTrade();
                bTrade.tradeId = trade.getId();
                bTrade.serverUpdateTime = trade.getServerUpdateTime();
                trades.add(bTrade);
            }

            return trades;
        }

        return null;
    }

    public final static class EmptyTableTrade {
        public Long tableId;
        public Integer skuKindCount;
        public BigDecimal tradePeopleCount;
        public String tradeNo;
        public String uuid;
        public Long waiterId;
        public String waiterName;
    }

    public final static class BTrade {
        public Long tradeId;
        public Long serverUpdateTime;
    }

    public static class TradeUser {
        public Long userId;
        public String userName;
        public Integer type;
        public Integer userType;
    }

}
