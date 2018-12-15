package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.bean.ICustomer;
import com.zhongmei.bty.basemodule.customer.message.CustomerInfoResp;
import com.zhongmei.bty.basemodule.database.entity.customer.CustomerV5;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
public class NewCardLoginResp implements ICustomer {

    private EcCard cardInstance;

    private CustomerV5 customer;

    private List<CustomerInfoResp.Card> cardList;

    public EcCard getCardInstance() {
        return cardInstance;
    }

    public void setCardInstance(EcCard cardInstance) {
        this.cardInstance = cardInstance;
    }

    public CustomerV5 getCustomerV5() {
        return customer;
    }

    public void setCustomerV5(CustomerV5 customerV5) {
        this.customer = customerV5;
    }

    public List<CustomerInfoResp.Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<CustomerInfoResp.Card> cardList) {
        this.cardList = cardList;
    }

    @Override
    public CustomerResp getCustomer() {
        CustomerResp customerNew = new CustomerResp();
        customerNew.brandId = customer.getBrandid();
        customerNew.birthday = customer.getBirthday();
        customerNew.address = customer.getAddress();
        customerNew.customerId = customer.getCustomerid();
        customerNew.customerName = customer.getName();
        customerNew.interest = customer.getInterest();
        customerNew.groupId = customer.getGroupid();
        customerNew.memberId = customer.getMemberid();
        customerNew.mobile = customer.getMobile();
        customerNew.invoice = customer.getInvoice();
        customerNew.levelId = customer.getLevelid();
        customerNew.sex = customer.getSex().value();
        customerNew.memo = customer.getMemo();
        customerNew.isDisable = customer.getIsdisable().intValue();
        customerNew.card = cardInstance;
        customerNew.otherCardList = cardList;
        return customerNew;
    }

}
