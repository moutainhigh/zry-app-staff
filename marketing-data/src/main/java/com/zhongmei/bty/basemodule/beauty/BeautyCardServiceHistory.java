package com.zhongmei.bty.basemodule.beauty;

import java.io.Serializable;

/**
 * @date 2018/6/20
 */
public class BeautyCardServiceHistory implements Serializable {
    /**
     * 服务名称（多个服务拼接在一起）
     */
    public String serviceName;
    /**
     * 订单号
     */
    public String tradeNo;
    /**
     * 交易日期）
     */
    public Long bizDate;
    /**
     * 订单uuid
     */
    public String tradeUUid;
    /**
     * 备注
     */
    public String remark;
}
