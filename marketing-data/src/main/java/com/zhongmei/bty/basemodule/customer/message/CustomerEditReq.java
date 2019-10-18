package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.yunfu.bean.req.CustomerResp;



public class CustomerEditReq {

    public String address;     public String birthday;     public Long id;
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

    public String nation;

    public String country;

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
        interest = customerNew.hobby;
        hobby = customerNew.hobby;
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
