package com.zhongmei.bty.basemodule.shopmanager.handover.operators;

import com.zhongmei.bty.basemodule.monitor.shopmanager.handover.entitys.HandoverData;
import com.zhongmei.bty.basemodule.shopmanager.handover.data.HandOverDataReq;
import com.zhongmei.bty.basemodule.shopmanager.handover.data.HandoverResponseData;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.resp.ResponseListener;

import java.util.Date;


public interface HandoverOperates extends IOperates {


    @Deprecated
    void query(ResponseListener<HandoverData> listener);



    void query(HandOverDataReq req, boolean intercept, ResponseListener<HandoverData> listener);



    void queryHistory(Date date, boolean intercept, ResponseListener<HandoverResponseData> listener);


    void insert(HandoverData data, boolean intercept, ResponseListener<HandoverResponseData> listener);


    boolean isOfflineDataDealOk();

    boolean isNetWorkAvailable();
}
