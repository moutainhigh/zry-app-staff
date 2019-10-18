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


public interface OrderCenterDal extends Dal {


    List<Trade> queryTrade(int childTab, int position, String keyword,
                           FilterCondition condition, Trade lastData, BusinessType... businessTypes);


    List<PaymentVo> getPaymentVos(String tradeUUID);


    TradeExtra getTradeExtra(String tradeUUID);


    TradeExtraSecrecyPhone getTradeExtraSecrecyPhone(Long tradeExtraId);


    TradeDeposit getTradeDeposit(String tradeUUID);


    List<TradeTable> getTradeTables(String tradeUUID);


    List<TradeCustomer> getTradeCustomer(String tradeUUID);


    List<DeliveryOrderVo> getDeliveryOrder(String tradeUUID);

    long countOf(int tab);

    VerifyKoubeiOrder getVerifyKoubeiOrder(String tradeUuid);
}
