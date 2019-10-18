package com.zhongmei.bty.snack.customer;

import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.util.EmptyUtils;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.ArrayList;


public class TradeCustomerConvert {


    public static TradeCustomerConvert newInstance() {
        return new TradeCustomerConvert();
    }

    private TradeCustomerConvert() {
    }

    public ModifyCustomerReq toModifyCustomerReq(Trade trade, CustomerResp customerNew, EcCard ecCard) {
        ModifyCustomerReq modifyCustomerReq = new ModifyCustomerReq();
        modifyCustomerReq.tradeId = trade.getId();
        TradeCustomer tradeCustomer = buildTradeCustomer(trade.getId(), trade.getUuid(), customerNew, ecCard, CustomerType.PAY);
        modifyCustomerReq.tradeCustomers = new ArrayList<>();
        modifyCustomerReq.tradeCustomers.add(tradeCustomer);
        return modifyCustomerReq;
    }

    public TradeCustomer buildTradeCustomer(Long tradeId, String tradeUuid, CustomerResp customerNew, EcCard ecCard, CustomerType customerType) {
        TradeCustomer customer = new TradeCustomer();
        customer.validateCreate();
                customer.setTradeId(tradeId);
        customer.setTradeUuid(tradeUuid);
        customer.setCustomerType(customerType);
        customer.setUuid(SystemUtils.genOnlyIdentifier());
        customer.setCustomerName(customerNew.customerName);
        customer.setCustomerPhone(customerNew.mobile);
        customer.setCustomerId(customerNew.customerId);
        if (EmptyUtils.isNotEmpty(ecCard)) {
            customer.setEntitycardNum(ecCard.getCardNum());
        }
        Integer sexInt = customerNew.sex;
        if (sexInt != null) {
            customer.setCustomerSex(Utils.equals(sexInt, 0) ? Sex.FEMALE : Sex.MALE);
        }
        return customer;
    }

}
