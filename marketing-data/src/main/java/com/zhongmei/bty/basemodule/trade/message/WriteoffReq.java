package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.yunfu.http.RequestObject;

import java.math.BigDecimal;

public class WriteoffReq implements RequestObject.IContent {
    public String tradeNo;
    public String uuid;
    public Long customerId;
    public String clientType;
    public Long updatorId;
    public String updatorName;
    public BigDecimal privilegeAmount;
    public String sourceCode;
    public Integer payModelGroup;
    public BigDecimal amount;
    public BigDecimal faceAmount;
    public BigDecimal changeAmount = BigDecimal.ZERO;
    public Long payModeId;
    public int payType;
    public String payModeName;
    public String authCode;
    public MemberPayInfo membershipCard;

    @Override
    public Long getUserId() {
        return updatorId;
    }

    @Override
    public String getUserName() {
        return updatorName;
    }

    public static class MemberPayInfo {
        public Long type;
        public Long customerId;
        public String cardNo;
        public int isCheckPwd;
        public String paidPwd;
        public int pwdType;
        public String phoneNo;
    }
}
