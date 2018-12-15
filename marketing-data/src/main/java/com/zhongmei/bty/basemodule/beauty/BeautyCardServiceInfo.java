package com.zhongmei.bty.basemodule.beauty;

import java.util.List;

/**
 * 次卡服务信息
 *
 * @date 2018/6/14
 */
public class BeautyCardServiceInfo {

    /**
     * 次卡id
     */
    public Long cardInstanceId;
    /**
     * 卡号
     */
    public String cardNo;
    /**
     * 卡名称
     */
    public String cardName;
    /**
     * 有效期起始日期
     */
    public Long validStartDay;
    /**
     * 有效期结束日期
     */
    public Long validEndDay;

    /**
     * 单卡所有服务总次数
     */
    public Integer allTimes;

    /**
     * 单卡所有服务总剩余次数
     */
    public Integer remainderTimes;

    /**
     * 是否已过期
     */
    public Integer rightStatus;

    /**
     * 次卡服务列表
     */
    public List<BeautyCardServiceAccount> cardServiceAccountList;
}
