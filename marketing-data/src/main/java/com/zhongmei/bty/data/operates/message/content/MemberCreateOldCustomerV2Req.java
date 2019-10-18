package com.zhongmei.bty.data.operates.message.content;

import android.text.TextUtils;

import com.zhongmei.yunfu.bean.req.CustomerCreateReq;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.util.DateTimeUtils;


public class MemberCreateOldCustomerV2Req extends BaseRequest {

    public String uniqueCode;     public int loginType = 0;     public String loginId;
    public Long userId;
    public Integer source = 1;    public String nation;    public String country;    public String nationalTelCode;    public String mobile;    public String name;    public String consumePassword;    public String faceCode;    public Long birthday;    public String sex;
    public String address;
    public Long groupId;    public String environmentHobby;
    public String invoiceTitle;
    public String memo;

    public void cloneReq(CustomerCreateReq req) {
        this.name = req.name;
        this.sex = req.sex + "";
        this.address = req.address;
        this.birthday = DateTimeUtils.formatDate(req.birthday);
        this.consumePassword = req.consumePassword;
        this.environmentHobby = req.environmentHobby;
        this.groupId = req.groupId;
        this.invoiceTitle = req.invoiceTitle;
        this.memo = req.memo;
        this.mobile = req.mobile;
        this.nation = req.nation;
        this.country = req.country;
        this.nationalTelCode = req.nationalTelCode;
        this.userId = Session.getAuthUser().getId();
        if (!TextUtils.isEmpty(req.faceCode)) {
            this.faceCode = req.faceCode;
        }
        this.loginId = req.mobile;
    }
}
