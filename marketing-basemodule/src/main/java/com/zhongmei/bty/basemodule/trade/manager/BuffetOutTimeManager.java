package com.zhongmei.bty.basemodule.trade.manager;

import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.trade.bean.OutTimeInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class BuffetOutTimeManager {

    /**
     * 自助餐超时费计算
     *
     * @param tradeVo
     */
    public static BigDecimal calculateOutTimeFee(TradeVo tradeVo) {
        BigDecimal outTimeFee = BigDecimal.ZERO;

        boolean outTimeEnable = ServerSettingCache.getInstance().getBuffetOutTimeFeeEnable();

        if (!outTimeEnable) {
            return outTimeFee;
        }

        OutTimeInfo outTimeInfo = ServerSettingCache.getInstance().getBuffetOutTimeFeeRule();

        long mServerTime = System.currentTimeMillis() - tradeVo.getTrade().getClientCreateTime();
        long timeMinute = mServerTime / (60 * 1000);//用餐时长（分钟）

        long diffTime = outTimeInfo.getLimitTimeLine() - timeMinute;

        if (diffTime < 0) {
            //计算超时费
            double outTimeCount = Math.ceil(Math.abs((double) diffTime) / outTimeInfo.getOutTimeUnit());
            outTimeFee = BigDecimal.valueOf(outTimeCount).multiply(outTimeInfo.getOutTimeFee()).multiply(BigDecimal.valueOf(tradeVo.getTrade().getTradePeopleCount()));
        }

        return outTimeFee;
    }


    /**
     * 计算已支付的超时费
     *
     * @param tradeVo
     * @return
     */
    public static BigDecimal getPaidOutTimeFee(TradeVo tradeVo) {
        BigDecimal paidOutTimeFee = BigDecimal.ZERO;

        boolean outTimeEnable = ServerSettingCache.getInstance().getBuffetOutTimeFeeEnable();

        if (!outTimeEnable) {
            return paidOutTimeFee;
        }

        List<TradePrivilege> listPrivilege = tradeVo.getTradePrivileges();
        ExtraCharge outTimeExtraCharge = ServerSettingCache.getInstance().getmOutTimeRule();

        if (Utils.isNotEmpty(listPrivilege)) {
            for (TradePrivilege tradePrivilege : listPrivilege) {
                if (tradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL && tradePrivilege.getPromoId().longValue() == outTimeExtraCharge.getId().longValue()) {
                    paidOutTimeFee = paidOutTimeFee.add(tradePrivilege.getPrivilegeAmount());
                }
            }
        }

        return paidOutTimeFee;

    }

    /**
     * 获取已经支付过的超时费金额
     *
     * @param tradeVo
     * @return
     */
    public static int getPaidOutTimeFeeCount(TradeVo tradeVo) {
        int count = 0;
        if (tradeVo.getTrade().getTradePayStatus() == TradePayStatus.UNPAID) {//如果未支付的，直接返回0
            return count;
        }
        List<TradePrivilege> listPrivilege = tradeVo.getTradePrivileges();
        ExtraCharge outTimeExtraCharge = ServerSettingCache.getInstance().getmOutTimeRule();

        if (Utils.isNotEmpty(listPrivilege)) {
            for (TradePrivilege tradePrivilege : listPrivilege) {
                if (tradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL && tradePrivilege.getPromoId().longValue() == outTimeExtraCharge.getId().longValue()) {
                    count++;
                }
            }
        }

        return count;
    }


    /**
     * 计算超时费用
     *
     * @param outTime 超时时间
     * @return
     */
    public static BigDecimal calculOutTimeFee(long outTime, OutTimeInfo outTimeInfo, TradeVo tradeVo, BigDecimal peopleCount) {
        BigDecimal outTimeFee = BigDecimal.ZERO;
        int paidOutTimeCount = getPaidOutTimeFeeCount(tradeVo);
        if (paidOutTimeCount > 0) {
            outTimeFee = outTimeFee.add(getPaidOutTimeFee(tradeVo));
        }

        double outTimeCount = Math.ceil(Math.abs((double) outTime) / outTimeInfo.getOutTimeUnit()) - paidOutTimeCount;
        //未支付的超时费
        BigDecimal unPaidOutTimeFee = BigDecimal.valueOf(outTimeCount).multiply(outTimeInfo.getOutTimeFee()).multiply(peopleCount);
        return outTimeFee.add(unPaidOutTimeFee);
    }

    /**
     * 计算超时费用
     *
     * @return
     */
    public static BigDecimal calculOutTimeFee(TradeVo tradeVo) {
        BigDecimal outTimeFee = BigDecimal.ZERO;

        boolean outTimeEnable = ServerSettingCache.getInstance().getBuffetOutTimeFeeEnable();

        if (!outTimeEnable) {
            return outTimeFee;
        }

        OutTimeInfo outTimeInfo = ServerSettingCache.getInstance().getBuffetOutTimeFeeRule();

        long mServerTime = System.currentTimeMillis() - tradeVo.getTrade().getClientCreateTime();
        long timeMinute = mServerTime / (60 * 1000);//用餐时长（分钟）

        long diffTime = outTimeInfo.getLimitTimeLine() - timeMinute;

        if (diffTime >= 0) {//没有超时
            return outTimeFee;
        }

        return calculOutTimeFee(diffTime, outTimeInfo, tradeVo, BigDecimal.valueOf(tradeVo.getTrade().getTradePeopleCount()));
    }
}
