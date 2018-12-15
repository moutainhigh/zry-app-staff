package com.zhongmei.bty.basemodule.hetong;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.commonmodule.database.entity.local.ContractOverdue;
import com.zhongmei.yunfu.resp.ResponseListener;

import java.util.List;

/**
 * 合同到期提醒的接口
 */
public interface HeTongExpireNotifyOperates extends IOperates {

    /**
     * 请求合同到期消息
     *
     * @param listener
     */
    void requestHeTongDaoqi(ResponseListener<List<ContractOverdue>> listener);

}
