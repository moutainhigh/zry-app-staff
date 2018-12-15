package com.zhongmei.bty.basemodule.pay.operates;

import com.zhongmei.bty.basemodule.database.entity.pay.PaymentItemUnionpay;
import com.zhongmei.bty.basemodule.pay.bean.PaymentItemUnionpayVo;
import com.zhongmei.yunfu.db.entity.trade.PaymentItemExtra;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGrouponDish;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;

public interface PaymentItemDal extends IOperates {

    PaymentItemUnionpay findByPaymentItemId(Long paymentItemId) throws Exception;

    PaymentItemUnionpay findByPaymentItemUuid(String paymentItemUuid) throws Exception;

    PaymentItemUnionpayVo findPaymentItemUnionpayVoByPaymentItemId(Long paymentItemId) throws Exception;

    PaymentItemUnionpayVo findPaymentItemUnionpayVoByPaymentItemUuid(String paymentItemUuid) throws Exception;

    PaymentItemExtra findExtraByPaymentItemId(Long paymentItemId);

    void insert(PaymentItemUnionpay paymentItemUnionpay) throws Exception;

    public List<PaymentItemGrouponDish> findPaymentItemGroupDishsByTradeId(Long tradeId);
}
