package com.zhongmei.bty.data.operates.impl;

import android.util.Log;

import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.bty.commonmodule.database.entity.local.ContractOverdue;
import com.zhongmei.bty.basemodule.hetong.HeTongExpireNotifyOperates;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.data.operates.message.content.HeTongExpireNotifyReq;

import java.util.List;

public class HeTongExpireNotifyOpetatesImpl extends AbstractOpeartesImpl implements HeTongExpireNotifyOperates {

    public HeTongExpireNotifyOpetatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void requestHeTongDaoqi(ResponseListener<List<ContractOverdue>> listener) {
        try {
            String url = ServerAddressUtil.getInstance().HeTongDaoqiRequest();

            OpsRequest.Executor<HeTongExpireNotifyReq, List<ContractOverdue>> executor =
                    OpsRequest.Executor.create(url);

            executor.requestValue(new HeTongExpireNotifyReq())
                    .responseType(OpsRequest.getListContentResponseType(ContractOverdue.class))
                    .execute(listener, "HeTongExpireNotify");
        } catch (Exception e) {

            Log.w("", "", e);
        }
    }

}
