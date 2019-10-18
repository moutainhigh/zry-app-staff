package com.zhongmei.beauty.operates;

import android.annotation.SuppressLint;
import android.util.Log;

import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.basemodule.monitor.shopmanager.handover.entitys.HandoverData;
import com.zhongmei.bty.basemodule.shopmanager.handover.data.HandOverDataReq;
import com.zhongmei.bty.basemodule.shopmanager.handover.data.HandoverResponseData;
import com.zhongmei.bty.basemodule.shopmanager.handover.operators.HandoverOperates;
import com.zhongmei.beauty.utils.BeautyServerAddressUtil;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.resp.ResponseListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressLint("SimpleDateFormat")
public class BeautyHandoverOperatesImpl extends AbstractOpeartesImpl implements HandoverOperates {

    private static final String TAG = BeautyHandoverOperatesImpl.class.getSimpleName();

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public BeautyHandoverOperatesImpl(ImplContext context) {
        super(context);
    }


    @Override
    public void query(ResponseListener<HandoverData> listener) {
        String url = BeautyServerAddressUtil.getHandOverInfoApi();
        OpsRequest.Executor<Object, HandoverData> executor = OpsRequest.Executor.create(url);
        executor.requestValue(new Object()).responseClass(HandoverData.class).execute(listener, "handoverNoParam");
    }

    @Override
    public void query(HandOverDataReq req, boolean intercept, final ResponseListener<HandoverData> listener) {
        boolean enable = (req.getBussinessType().equals(BusinessType.SNACK.value())
                || req.getBussinessType().equals(BusinessType.TAKEAWAY.value()));
        String url = BeautyServerAddressUtil.getHandOverInfoApi();
        OpsRequest.Executor<Object, HandoverData> executor = OpsRequest.Executor.create(url);
        executor.interceptEnable(enable);
        executor.requestValue(req).responseClass(HandoverData.class).execute(listener, "handoverParam");
    }

    @Override
    public void queryHistory(Date date, boolean intercept, ResponseListener<HandoverResponseData> listener) {
        String url = BeautyServerAddressUtil.findHandOverHistoryInfoApi();
        QueryHistoryCondition dateCondition = new QueryHistoryCondition();
        String startDate = dateFormat.format(date) + " 00:00:00";
        String endDate = dateFormat.format(date) + " 23:59:59";
        try {
            dateCondition.setStartDate(dateTimeFormat.parse(startDate).getTime());
            dateCondition.setEndDate(dateTimeFormat.parse(endDate).getTime());
        } catch (ParseException e) {
            Log.e(TAG, "the date of conversion of abnormal", e);
        }

        OpsRequest.Executor<QueryHistoryCondition, HandoverResponseData> executor = OpsRequest.Executor.create(url);
        executor.requestValue(dateCondition)
                .responseClass(HandoverResponseData.class)
                .interceptEnable(true)
                .execute(listener, "handoverHistory");
    }

    @Override
    public void insert(HandoverData data, boolean intercept, ResponseListener<HandoverResponseData> listener) {
        String url = BeautyServerAddressUtil.createHandOverInfoApi();
        OpsRequest.Executor<HandoverData, HandoverResponseData> executor = OpsRequest.Executor.create(url);
        executor.requestValue(data)
                .responseClass(HandoverResponseData.class)
                .interceptEnable(true)
                .execute(listener, "handoverSave");
    }

    @Override
    public boolean isOfflineDataDealOk() {
        return true;
    }

    @Override
    public boolean isNetWorkAvailable() {
        return false;
    }


    static class QueryHistoryCondition {
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
}
