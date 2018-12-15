package com.zhongmei.bty.buffet.table.view;

import com.zhongmei.bty.basemodule.trade.bean.DepositInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BuffetUnionCancelVo {

    public BuffetDeposit buffetDeposit;
    public List<BuffetPeople> buffetPeople;

    public List<BuffetPeople> filterBuffetPeople(boolean enabledShow) {
        List<BuffetPeople> result = new ArrayList<>();
        if (buffetPeople != null) {
            for (BuffetPeople people : buffetPeople) {
                if (people.enabledShow == enabledShow) {
                    result.add(people);
                }
            }
        }
        return result;
    }

    public static BuffetUnionCancelVo createBuffetUnionCancelVo(TradeVo tradeVo) {
        return createBuffetUnionCancelVo(tradeVo, null, null);
    }

    public static BuffetUnionCancelVo createBuffetUnionCancelVo(TradeVo tradeVo, BigDecimal depositPay, BigDecimal peopleCount) {
        /*boolean depositEnable = ServerSettingCache.getInstance().getBuffetDepositEnable();
        DepositInfo depositInfo = ServerSettingCache.getInstance().getBuffetTradeDeposit();
        if (depositEnable && depositInfo != null) {
            buffetUnionBean.depositType = deposit.getType();
            buffetUnionBean.depositPay = BigDecimal.ZERO;
        }*/

        BuffetUnionCancelVo buffetUnion = new BuffetUnionCancelVo();
        if (tradeVo.getTradeDeposit() != null) {
            TradeDeposit deposit = tradeVo.getTradeDeposit();
            BuffetUnionCancelVo.BuffetDeposit buffetDeposit = new BuffetUnionCancelVo.BuffetDeposit();
            buffetDeposit.depositType = deposit.getType();
            buffetDeposit.unitPrice = deposit.getUnitPrice();
            buffetDeposit.depositPay = depositPay != null ? depositPay : deposit.getDepositPay();
            buffetUnion.buffetDeposit = buffetDeposit;
        }

        buffetUnion.buffetPeople = new ArrayList<>();
        List<TradeBuffetPeople> buffetPeopleList = tradeVo.getTradeBuffetPeoples();
        if (buffetPeopleList != null) {
            for (TradeBuffetPeople people : buffetPeopleList) {
                if (people.getStatusFlag() == StatusFlag.VALID) {
                    BuffetUnionCancelVo.BuffetPeople buffetPeople = new BuffetUnionCancelVo.BuffetPeople();
                    buffetPeople.carteNormsId = people.getCarteNormsId();
                    buffetPeople.carteNormsName = people.getCarteNormsName();
                    buffetPeople.cartePrice = people.getCartePrice();
                    buffetPeople.peopleCount = peopleCount != null ? peopleCount : people.getPeopleCount();
                    buffetPeople.enabledShow = people.getPeopleCount().compareTo(BigDecimal.ZERO) > 0;
                    buffetUnion.buffetPeople.add(buffetPeople);
                }
            }
        }
        return buffetUnion;
    }

    public BigDecimal getPeopleCount() {
        BigDecimal peopleCount = BigDecimal.ZERO;
        if (buffetPeople != null) {
            for (BuffetPeople people : buffetPeople) {
                peopleCount = peopleCount.add(people.peopleCount);
            }
        }
        return peopleCount;
    }

    static public class BuffetDeposit {
        public Integer depositType;
        public BigDecimal unitPrice;
        public BigDecimal depositPay;

        public BigDecimal countDepositPay(BigDecimal peopleCount) {
            //只有按人数时，才计算押金
            if (depositType == DepositInfo.TYPE_BY_PEOPLE) {
                return peopleCount.multiply(unitPrice);
            }

            return depositPay;
        }
    }

    static public class BuffetPeople {
        public Long carteNormsId;
        public String carteNormsName;
        public BigDecimal cartePrice;
        public BigDecimal peopleCount;
        public boolean enabledShow = true;
    }
}
