package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.yunfu.bean.req.CustomerResp;

/**
 * Created by demo on 2018/12/15
 */

public class CustomerEditReq {

    public String address; //	地址
    public String birthday; //	生日
    public Long id;
    public Long customerMainId;
    public Long cMemberCreateDateTime;
    public Long groupID;
    public String groupId;
    public String interest;
    public String hobby;
    public String invoice;
    public Integer isDisable;
    public Long modifyDateTime;
    public Long lastSyncMarker;
    public Long levelId;
    public String loginId;
    public Integer loginType;
    public String memo;
    public String mobile;
    public String name;
    public String serverId;
    public Integer sex;
    public Integer source;
    public Long localCreateDateTime;
    public String uuid;
    public String birthdate;
    /**
     * 国家英文名称(为空默认中国) = countryEN
     */
    public String nation;
    /**
     * 国家中文名称(为空默认中国) = countryZH
     */
    public String country;
    /**
     * 电话国际区码(为空默认中国) = AreaCode
     */
    public String nationalTelCode;


    int bindFlag = 1;
    int ctype = 0;
    int isNeedConfirm = 0;

    public CustomerEditReq fromCustomer(CustomerResp customerNew) {
        address = customerNew.address;
        birthday = customerNew.birthday;
        id = customerNew.customerId;
        customerMainId = customerNew.customerMainId;
        cMemberCreateDateTime = customerNew.upgradeTime;
        groupID = customerNew.groupId == null ? 999999 : customerNew.groupId;
        groupId = customerNew.groupId == null ? "999999" : customerNew.groupId + "";
        interest = customerNew.interest;
        hobby = customerNew.interest;
        invoice = customerNew.invoice;
        isDisable = customerNew.isDisable;
        modifyDateTime = customerNew.modifyDateTime;
        if (customerNew.modifyDateTime != null) {
            lastSyncMarker = customerNew.modifyDateTime / 1000;
        }
        levelId = customerNew.levelId;
        loginId = customerNew.loginId;
        loginType = customerNew.loginType;
        memo = customerNew.memo;
        mobile = customerNew.mobile;
        name = customerNew.customerName;
        serverId = customerNew.synFlag;
        sex = customerNew.sex;
        source = customerNew.source;
        localCreateDateTime = customerNew.upgradeTime;
        uuid = customerNew.synFlag;
        birthdate = birthday + "";
        nation = customerNew.nation;
        country = customerNew.country;
        nationalTelCode = customerNew.nationalTelCode;
        return this;
    }

}
