package com.zhongmei.bty.cashier.dal;

import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.DeliveryOrderVo;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.basemodule.trade.entity.TradeExtraSecrecyPhone;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.cashier.ordercenter.bean.FilterCondition;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.bty.db.entity.VerifyKoubeiOrder;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
public interface OrderCenterDal extends Dal {

    /**
     * 根据不同条件获取订单列表
     *
     * @param childTab  当前选中的子tab
     * @param position  当前tab下的位置
     * @param keyword   搜索关键字
     * @param condition 搜索条件
     * @param lastData  最后一条订单
     * @return 订单列表
     */
    List<Trade> queryTrade(int childTab, int position, String keyword,
                           FilterCondition condition, Trade lastData, BusinessType... businessTypes);

    /**
     * 根据订单获取PaymentVo里诶包
     *
     * @param tradeUUID 目标订单(trade)
     * @return 返回订单的PaymentVos列表
     */
    List<PaymentVo> getPaymentVos(String tradeUUID);

    /**
     * 根据订单的uuid获取订单交易扩展
     *
     * @param tradeUUID 订单uuid
     * @return 返回对应的交易扩展
     */
    TradeExtra getTradeExtra(String tradeUUID);

    /**
     * 根据TradeExtra Id获取第三方隐私电话
     *
     * @param tradeExtraId TradeId
     * @return 返回对应的第三方隐私电话
     */
    TradeExtraSecrecyPhone getTradeExtraSecrecyPhone(Long tradeExtraId);

    /**
     * 根据订单的uuid获取订单的押金
     *
     * @param tradeUUID 订单uuid
     * @return 返回对应的交易押金
     */
    TradeDeposit getTradeDeposit(String tradeUUID);

    /**
     * 根据订单的uuid获取这笔订单对应的桌台信息
     *
     * @param tradeUUID 订单的uuid
     * @return 返回对应的桌台信息
     */
    List<TradeTable> getTradeTables(String tradeUUID);

    /**
     * 根据订单的uuid获取订单的的顾客信息
     *
     * @param tradeUUID 订单的uuid
     * @return 返回对应的顾客信息
     */
    List<TradeCustomer> getTradeCustomer(String tradeUUID);

    /**
     * 根据订单的uuid获取订单的的配送单（获取配送取消的单据）
     */
    List<DeliveryOrderVo> getDeliveryOrder(String tradeUUID);

    long countOf(int tab);

    VerifyKoubeiOrder getVerifyKoubeiOrder(String tradeUuid);
}
