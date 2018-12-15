package com.zhongmei.beauty.operates;

import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.basemodule.shopmanager.bean.TransferCloseBillData;
import com.zhongmei.bty.basemodule.shopmanager.closing.bean.CloseBillDataInfo;
import com.zhongmei.bty.basemodule.shopmanager.closing.message.CloseBillReq;
import com.zhongmei.bty.basemodule.shopmanager.closing.message.CloseBillResp;
import com.zhongmei.bty.basemodule.shopmanager.closing.message.CloseHistoryReq;
import com.zhongmei.bty.basemodule.shopmanager.closing.message.CloseHistoryResp;
import com.zhongmei.bty.basemodule.shopmanager.closing.message.ClosingHandOverResp;
import com.zhongmei.bty.basemodule.shopmanager.handover.data.ClosingReq;
import com.zhongmei.bty.basemodule.shopmanager.message.CloseDetailReq;
import com.zhongmei.beauty.utils.BeautyServerAddressUtil;
import com.zhongmei.yunfu.resp.data.TransferReq;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.data.operates.impl.TradeOperatesImpl;
import com.zhongmei.bty.data.operates.message.content.NullReq;

/**
 * Created by demo on 2018/12/15
 */

public class BeautyTradeOperatesImpl extends TradeOperatesImpl {

    public BeautyTradeOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void closeDetail(TransferReq transferReq, ResponseListener<TransferCloseBillData> listener) {
        String url = ServerAddressUtil.getInstance().mindTransfer();
        OpsRequest.Executor<TransferReq, TransferCloseBillData> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq).responseClass(TransferCloseBillData.class).timeout(30000).execute(listener,
                "doTransferCloseBill");
    }

    @Override
    public void closeDetail(CloseDetailReq closeDetailReq, ResponseListener<CloseBillDataInfo> listener) {
        String url = BeautyServerAddressUtil.doCloseDetail();
        OpsRequest.Executor<CloseDetailReq, CloseBillDataInfo> executor = OpsRequest.Executor.create(url);
        executor.requestValue(closeDetailReq).responseClass(CloseBillDataInfo.class).timeout(15000).execute(listener,
                "docloseDetail");
    }

    @Override
    public void doCloseBill(CloseBillReq closeBillReq, ResponseListener<CloseBillResp> listener) {
        String url = BeautyServerAddressUtil.doCloseBill();
        OpsRequest.Executor<CloseBillReq, CloseBillResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(closeBillReq).responseClass(CloseBillResp.class).timeout(30000).execute(listener,
                "doCloseBill");
    }

    @Override
    public void queryCloseHistoryList(CloseHistoryReq closeHistoryReq, ResponseListener<CloseHistoryResp> listener) {
        String url = BeautyServerAddressUtil.queryCloseHistoryList();
        OpsRequest.Executor<CloseHistoryReq, CloseHistoryResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(closeHistoryReq).responseClass(CloseHistoryResp.class).execute(listener,
                "queryCloseHistory");
    }

    @Override
    public void queryHandOverHistory(ResponseListener<ClosingHandOverResp> listener) {
        String url = BeautyServerAddressUtil.queryCloseHandoverhistory();
        OpsRequest.Executor<NullReq, ClosingHandOverResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(new NullReq()).responseClass(ClosingHandOverResp.class).timeout(30000).execute(listener,
                "queryHandoverHistory");
    }

    @Override
    public void queryHandOverHistory(ClosingReq req, ResponseListener<ClosingHandOverResp> listener) {
        String url = BeautyServerAddressUtil.queryCloseHandoverhistory();
        OpsRequest.Executor<ClosingReq, ClosingHandOverResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(ClosingHandOverResp.class).timeout(30000).execute(listener,
                "queryHandoverHistory");
    }
}
