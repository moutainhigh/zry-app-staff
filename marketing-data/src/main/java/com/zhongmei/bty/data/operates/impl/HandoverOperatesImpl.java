package com.zhongmei.bty.data.operates.impl;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.yunfu.http.QSOpsRequest;
import com.zhongmei.yunfu.http.QSResponseListener;
import com.zhongmei.yunfu.http.QSResponseObject;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.basemodule.database.entity.shopmanager.CashHandover;
import com.zhongmei.bty.basemodule.database.entity.shopmanager.CashHandoverItem;
import com.zhongmei.bty.basemodule.monitor.shopmanager.handover.entitys.HandoverData;
import com.zhongmei.bty.basemodule.shopmanager.handover.data.HandOverDataReq;
import com.zhongmei.bty.basemodule.shopmanager.handover.data.HandoverResponseData;
import com.zhongmei.bty.basemodule.shopmanager.handover.data.OfflineDataDealStatusReq;
import com.zhongmei.bty.basemodule.shopmanager.handover.data.OfflineDataDealStatusResp;
import com.zhongmei.bty.basemodule.shopmanager.handover.operators.HandoverOperates;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.offline.backup.backup4qs.QSBackup;
import com.zhongmei.bty.snack.offline.Snack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

/**

 */
@SuppressLint("SimpleDateFormat")
public class HandoverOperatesImpl extends AbstractOpeartesImpl implements HandoverOperates {

    private static final String TAG = HandoverOperatesImpl.class.getSimpleName();

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public HandoverOperatesImpl(ImplContext context) {
        super(context);
    }


    @Override
    public void query(ResponseListener<HandoverData> listener) {
        String url = ServerAddressUtil.getInstance().getHandOverInfoApi();
        OpsRequest.Executor<Object, HandoverData> executor = OpsRequest.Executor.create(url);
        executor.requestValue(new Object()).responseClass(HandoverData.class).execute(listener, "handover");
    }

    @Override
    public void query(HandOverDataReq req, boolean intercept, final ResponseListener<HandoverData> listener) {
        String url = ServerAddressUtil.getInstance().getHandOverInfoApi();
        OpsRequest.Executor<Object, HandoverData> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(HandoverData.class)
                .interceptEnable(intercept)
                .execute(listener, "handover");
    }

    @Override
    public void queryHistory(Date date, boolean intercept, ResponseListener<HandoverResponseData> listener) {
        String url = ServerAddressUtil.getInstance().findHandOverHistoryInfoApi();
        QueryHistoryReq dateCondition = createQueryHistoryReq(date);
        boolean interceptEnable = intercept && Snack.netWorkUnavailable();
        OpsRequest.Executor<QueryHistoryReq, HandoverResponseData> executor = OpsRequest.Executor.create(url);
        executor.requestValue(dateCondition)
                .responseClass(HandoverResponseData.class)
                .interceptEnable(interceptEnable)
                .execute(listener, "handover");
    }

    @Override
    public void insert(HandoverData data, boolean intercept, ResponseListener<HandoverResponseData> listener) {
        String url = ServerAddressUtil.getInstance().createHandOverInfoApi();
        OpsRequest.Executor<HandoverData, HandoverResponseData> executor = OpsRequest.Executor.create(url);
        boolean interceptEnable = intercept && Snack.netWorkUnavailable();
        executor.requestValue(data)
                .responseClass(HandoverResponseData.class)
                .responseProcessor(new CreateHandoverProcessor())
                .interceptEnable(interceptEnable)
                .execute(listener, "handover");
    }

    @Override
    public boolean isOfflineDataDealOk() {
        if (!QSBackup.isUploadFinish()) {
            return false;
        }
        // FIXME: 2018/6/19 放到异步的moudle
        String url = ServerAddressUtil.getInstance().getLogErrorQuantityApi();
        QSOpsRequest.Executor<OfflineDataDealStatusReq, OfflineDataDealStatusResp> executor = QSOpsRequest.Executor.create(url);
        final ArrayBlockingQueue<Boolean> arrayBlockingQueue = new ArrayBlockingQueue<>(1);//阻塞异步调用
        executor.requestValue(createOfflineDataDealStatusReq())
                .responseClass(OfflineDataDealStatusResp.class)
                .execute(new QSResponseListener<OfflineDataDealStatusResp>() {
                    @Override
                    public void onResponse(QSResponseObject<OfflineDataDealStatusResp> response) {
                        boolean success = false;
                        if (response.getCode() == 1000
                                && response.getContent() != null) {
                            success = response.getContent().getFailedQuantity() == 0
                                    && response.getContent().getUntreatedQuantity() == 0;
                        }
                        arrayBlockingQueue.add(success);
                    }

                    @Override
                    public void onError(VolleyError error) {
                        arrayBlockingQueue.add(false);
                    }
                }, url);
        try {
            return arrayBlockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isNetWorkAvailable() {
        return Snack.netWorkAvailable();
    }

    private OfflineDataDealStatusReq createOfflineDataDealStatusReq() {
        OfflineDataDealStatusReq req = new OfflineDataDealStatusReq();
        req.setDateStart(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 7);
        req.setDateEnd(System.currentTimeMillis());
        req.setBrandId(BaseApplication.getInstance().getBrandIdenty());
        req.setShopId(BaseApplication.getInstance().getShopIdenty());
        return req;
    }

    @NonNull
    private QueryHistoryReq createQueryHistoryReq(Date date) {
        QueryHistoryReq req = new QueryHistoryReq();
        String startDate = dateFormat.format(date) + " 00:00:00";
        String endDate = dateFormat.format(date) + " 23:59:59";
        try {
            req.setStartDate(dateTimeFormat.parse(startDate).getTime());
            req.setEndDate(dateTimeFormat.parse(endDate).getTime());
        } catch (ParseException e) {
            Log.e(TAG, "the date of conversion of abnormal", e);
        }
        return req;
    }

    static class QueryHistoryReq {
        private long startDate;

        private long endDate;

        public long getStartDate() {
            return startDate;
        }

        public void setStartDate(long startDate) {
            this.startDate = startDate;
        }

        public long getEndDate() {
            return endDate;
        }

        public void setEndDate(long endDate) {
            this.endDate = endDate;
        }
    }

    private static class CreateHandoverProcessor extends OpsRequest.SaveDatabaseResponseProcessor<HandoverResponseData> {

        protected boolean isSuccessful(ResponseObject<HandoverResponseData> response) {
            return ResponseObject.isOk(response) || ResponseObject.isExisted(response);
        }

        @Override
        protected Callable<Void> getCallable(DatabaseHelper helper, HandoverResponseData resp) throws Exception {
            List<CashHandover> cashHandoverList = new ArrayList<CashHandover>(resp.getCashHandovers());
            List<CashHandoverItem> cashHandoverItemList = new ArrayList<CashHandoverItem>(resp.getCashHandoverItems());
            DBHelperManager.saveEntities(helper, CashHandover.class, cashHandoverList);
            DBHelperManager.saveEntities(helper, CashHandoverItem.class, cashHandoverItemList);
            return null;
        }
    }
}
