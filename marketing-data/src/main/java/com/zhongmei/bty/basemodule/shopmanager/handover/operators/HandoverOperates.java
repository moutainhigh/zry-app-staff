package com.zhongmei.bty.basemodule.shopmanager.handover.operators;

import com.zhongmei.bty.basemodule.monitor.shopmanager.handover.entitys.HandoverData;
import com.zhongmei.bty.basemodule.shopmanager.handover.data.HandOverDataReq;
import com.zhongmei.bty.basemodule.shopmanager.handover.data.HandoverResponseData;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.resp.ResponseListener;

import java.util.Date;

/**
 * 交班相关的操作
 */
public interface HandoverOperates extends IOperates {

    /**
     * 查询未交班的记录
     *
     * @param listener
     */
    @Deprecated
    void query(ResponseListener<HandoverData> listener);


    /**
     * 查询未交班的记录
     *
     * @param intercept
     * @param listener
     */
    void query(HandOverDataReq req, boolean intercept, ResponseListener<HandoverData> listener);


    /**
     * 查询交班历史
     *
     * @param date
     * @param intercept
     * @param listener
     */
    void queryHistory(Date date, boolean intercept, ResponseListener<HandoverResponseData> listener);

    /**
     * 交班
     *
     * @param data
     * @param intercept
     * @param listener
     */
    void insert(HandoverData data, boolean intercept, ResponseListener<HandoverResponseData> listener);

    /**
     * 离线产生的数据是否处理完成,可能阻塞线程。
     *
     * @return
     */
    boolean isOfflineDataDealOk();

    boolean isNetWorkAvailable();
}
