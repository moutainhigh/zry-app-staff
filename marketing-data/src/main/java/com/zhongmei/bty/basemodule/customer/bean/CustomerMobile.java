package com.zhongmei.bty.basemodule.customer.bean;

import com.zhongmei.bty.basemodule.database.entity.customer.CustomerCardItem;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.bean.req.CustomerResp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
public class CustomerMobile implements ICustomer, Serializable {

    public static final int SEX_FEMALE = 0; //女
    public static final int SEX_MALE = 1; //男
    public Long customerId; //	顾客Id
    public String customerName; //	顾客名字
    public String mobile; //	手机号码
    public Integer sex = -1; //	性别Code
    public Long levelId; //	等级Id
    public Long level; //	等级数（1~5）
    public String levelName;
    public String synFlag;
    public String country;
    public String nation;
    public String nationalTelCode;
    public List<CustomerCardItem> cardList;

    // v8.13.0新增字段 修改无法回显的BUG
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

    /**
     * 判断是否有会员卡
     *
     * @return false 无  true 有
     */
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
