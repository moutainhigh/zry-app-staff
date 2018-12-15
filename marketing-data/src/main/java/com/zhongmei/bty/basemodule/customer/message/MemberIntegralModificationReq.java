package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.bty.commonmodule.data.operate.message.BaseRequest;

import java.util.Date;

/**
 * Created by demo on 2018/12/15
 */

public class MemberIntegralModificationReq extends BaseRequest {
    //顾客ID
    private Long customerId;
    //积分操作类型1补录、2扣除
    private Integer operateType;
    //积分额
    private Integer integral;
    //操作人
    private long userId;
    //理由
    private String reason;
    /**
     * 积分来源
     * 1 POS，2 手机app，3 其他系统导入，4 微信，5 支付宝，6 商家官网，7 百度 ,
     * 8 loyalty，9 OnMobile,10 OsMobile,13 熟客，15 自助终端，99 账号合并
     */
    private Integer source;
    //营业日期(可为空)
    private Date bizDate;

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public void setBizDate(Date bizDate) {
        this.bizDate = bizDate;
    }
}
