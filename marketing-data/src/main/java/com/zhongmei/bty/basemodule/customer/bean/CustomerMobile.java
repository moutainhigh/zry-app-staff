package com.zhongmei.bty.basemodule.customer.bean;

import com.zhongmei.bty.basemodule.database.entity.customer.CustomerCardItem;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.bean.req.CustomerResp;

import java.io.Serializable;
import java.util.List;


public class CustomerMobile implements ICustomer, Serializable {

    public static final int SEX_FEMALE = 0;     public static final int SEX_MALE = 1;     public Long customerId;     public String customerName;     public String mobile;     public Integer sex = -1;     public Long levelId;     public Long level;     public String levelName;
    public String synFlag;
    public String country;
    public String nation;
    public String nationalTelCode;
    public List<CustomerCardItem> cardList;

        public String memo;

    public String interest;

    public String invoiceTitle;

    public String address;

    public Long groupId;

    public boolean isMember() {
        return levelId != null;
    }

    public Integer getSex() {
        return sex != null ? sex : -1;
    }

    public Sex getCustomerSex() {
        return ValueEnums.toEnum(Sex.class, sex);
    }


    public boolean hasCustomerEntityCard() {
        for (CustomerCardItem item : cardList) {
            if (item.cardType == EntityCardType.GENERAL_CUSTOMER_CARD.value()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CustomerResp getCustomer() {
        CustomerResp customer = new CustomerResp();
        customer.customerId = customerId;
        customer.customerName = customerName;
        customer.mobile = mobile;
        customer.sex = sex;
        customer.level = level;
        customer.levelId = levelId;
        customer.levelName = levelName;
        customer.synFlag = synFlag;
        customer.cardList = cardList;
        return customer;
    }
}
