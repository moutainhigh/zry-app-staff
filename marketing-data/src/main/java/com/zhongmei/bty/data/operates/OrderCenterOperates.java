package com.zhongmei.bty.data.operates;

import com.zhongmei.bty.cashier.exception.OperateException;
import com.zhongmei.bty.cashier.ordercenter.bean.FilterCondition;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;

import java.util.List;


public interface OrderCenterOperates extends IOperates {



    List<Trade> queryTrade(int childTab, int position, String keyword, FilterCondition condition, Trade lastData);

    List<PaymentVo> getPaymentVos(String tradeUUID) throws OperateException;

    TradeExtra getTradeExtra(String tradeUUID) throws OperateException;

    TradeDeposit getTradeDeposit(String taradeUUID) throws OperateException;

    List<TradeTable> getTradeTableList(String tradeUUID) throws OperateException;

    List<TradeCustomer> getTradeCustomers(String tradeUUID) throws OperateException;

    int countOrder(int tab) throws OperateException;
}
