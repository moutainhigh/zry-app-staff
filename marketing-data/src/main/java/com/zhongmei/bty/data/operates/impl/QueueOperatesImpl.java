package com.zhongmei.bty.data.operates.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.data.R;
import com.zhongmei.yunfu.http.CalmNetWorkRequest;
import com.zhongmei.yunfu.http.CalmStringRequest;
import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.yunfu.http.processor.CalmDatabaseProcessor;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.database.queue.QueueExtra;
import com.zhongmei.bty.basemodule.database.queue.QueueStatus;
import com.zhongmei.bty.basemodule.database.queue.SuccessOrFaild;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.bty.basemodule.queue.CommercialQueueLine;
import com.zhongmei.bty.basemodule.queue.QueueDal;
import com.zhongmei.yunfu.net.volley.Request;
import com.zhongmei.yunfu.net.volley.Response.ErrorListener;
import com.zhongmei.yunfu.net.volley.Response.Listener;
import com.zhongmei.yunfu.context.util.Gsons;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.bty.data.operates.QueueOperates;
import com.zhongmei.bty.data.operates.message.content.QueueCallNumberReq;
import com.zhongmei.bty.data.operates.message.content.QueueCallNumberResp;
import com.zhongmei.bty.data.operates.message.content.QueueModifyReq;
import com.zhongmei.bty.data.operates.message.content.QueuePredictWaitTimeReq;
import com.zhongmei.bty.data.operates.message.content.QueuePredictWaitTimeResp;
import com.zhongmei.bty.data.operates.message.content.QueueQTCodeReq;
import com.zhongmei.bty.data.operates.message.content.QueueRecoverInvalidReq;
import com.zhongmei.bty.data.operates.message.content.QueueReq;
import com.zhongmei.bty.data.operates.message.content.QueueResp;
import com.zhongmei.bty.data.operates.message.content.QueueUpdateReq;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * 排队相关接口
 */
@SuppressLint("SimpleDateFormat")
public class QueueOperatesImpl extends AbstractOpeartesImpl implements QueueOperates {

    private static final String TAG = QueueOperatesImpl.class.getSimpleName();
    ImplContext context;

    public QueueOperatesImpl(ImplContext context) {
        super(context);
        this.context = context;
    }

    @Override
    public void sendMessage(Queue queue, Listener<String> listener, ErrorListener errorListener) {
        String url = ServerAddressUtil.getInstance().queueMessage();
        QueueMessReq req = toQueueMessReq(queue);
        Gson mGson = Gsons.gsonBuilder().create();
        String json = mGson.toJson(req);
        String reqStr = genMessageRequest(json);
        String value = url + reqStr;
        /*BusinessStringRequest request = new BusinessStringRequest(value, listener, errorListener);
		RequestManagerCompat.addRequest(BaseApplication.getInstance(), request, TAG);*/

        CalmStringRequest stringRequest = new CalmStringRequest(BaseApplication.getInstance(), Request.Method.GET,
                value, listener, errorListener);
        stringRequest.executeRequest(TAG);
    }

    @Override
    public void queuePhone(Queue queue, Listener<String> listener, ErrorListener errorListener) {
        String url = ServerAddressUtil.getInstance().queuePhone();
        QueuePhoneReq req = toQueuePhoneReq(queue);
        Gson mGson = Gsons.gsonBuilder().create();
        String json = mGson.toJson(req);
        String reqStr = genMessageRequest(json);
        String value = url + reqStr;
		/*BusinessStringRequest request = new BusinessStringRequest(value, listener, errorListener);
		RequestManagerCompat.addRequest(BaseApplication.getInstance(), request, TAG);*/

        CalmStringRequest stringRequest = new CalmStringRequest(BaseApplication.getInstance(), Request.Method.GET,
                value, listener, errorListener);
        stringRequest.executeRequest(TAG);
    }

    public void queueCallNumber(Long queueID, ResponseListener<QueueCallNumberResp> listener) {
        HashMap<String, String> requstContent = new HashMap<>();
        requstContent.put("queueID", queueID + "");
        String url = ServerAddressUtil.getInstance().queueCallNumber();
        OpsRequest.Executor<HashMap<String, String>, QueueCallNumberResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(requstContent)
                .responseClass(QueueCallNumberResp.class)
                .execute(listener, TAG);
    }

    @Override
    public void updateQueue(@QueueReq.Type int type, Queue queue, ResponseListener<QueueResp> listener) {
        changeQueue(type, queue.getUuid(), queue.getQueueLineId(), queue.getModifyDateTime(), listener);
    }

    @Override
    public void changeQueue(@QueueReq.Type int type, String serverId, Long queueLineId, Long lastSyncMarker, ResponseListener<QueueResp> listener) {
        String url = ServerAddressUtil.getInstance().changeQueue();
        QueueReq queueRsq = new QueueReq();
        queueRsq.setType(type);
        queueRsq.setQueueLineId(queueLineId);
        queueRsq.setServerId(serverId);
        queueRsq.setLastSyncMarker(lastSyncMarker);
        OpsRequest.Executor<QueueReq, QueueResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(queueRsq)
                .responseClass(QueueResp.class)
                .responseProcessor(new OpsRequest.SaveDatabaseResponseProcessor<QueueResp>() {
                    @Override
                    protected Callable<Void> getCallable(DatabaseHelper helper, QueueResp resp) throws Exception {
                        if (resp != null) {
                            resp.setUuid(resp.synFlag);
                            resp.setIsOk(SuccessOrFaild.SUCCESS);
                            if (resp.getQueueStatus() == QueueStatus.ADMISSION) {
                                resp.setInDateTime(new Date().getTime());
                            } else if (resp.getQueueStatus() == QueueStatus.INVALID) {
                                resp.setSkipDatetime(new Date().getTime());
                            }
                            DBHelperManager.saveEntities(helper, Queue.class, resp);
                            if (resp.queueExtra != null)
                                DBHelperManager.saveEntities(helper, QueueExtra.class, resp.queueExtra);
                        }
                        return null;
                    }
                })
                .execute(listener, TAG);
    }

    @Override
    public void createAndUpdateQueue(int type, final Queue queue, ResponseListener<Queue> listener) {

        String url = ServerAddressUtil.getInstance().createAndUpdateQueue();
        Log.e("QueueOperatesImpl", "createAndUpdateQueue() <===> url = " + url);
        QueueUpdateReq queueUpdateReq = toQueueOrderReq(type, queue);

        OpsRequest.Executor<QueueUpdateReq, Queue> executor = OpsRequest.Executor.create(url);
        executor.requestValue(queueUpdateReq)
                .responseClass(Queue.class)
                .responseProcessor(new OpsRequest.DatabaseResponseProcessor<Queue>() {
                    @Override
                    protected void transactionCallable(DatabaseHelper helper, Queue resp) throws Exception {
                        resp.setUuid(queue.getUuid());
                        DBHelperManager.saveEntities(helper, Queue.class, resp);
                        if (resp.queueExtra != null) {
                            DBHelperManager.saveEntities(helper, QueueExtra.class, resp.queueExtra);
                        }
                    }
                })
                .execute(listener, TAG);
    }

    @Override
    public void queueRecoverInvalid(String queueUuid, Long modifyDateTime, FragmentActivity fragment, CalmResponseListener<ResponseObject<Queue>> listener) {
        String url = ServerAddressUtil.getInstance().queueRevocationCall();
        QueueRecoverInvalidReq req = new QueueRecoverInvalidReq();
        req.setSynFlag(queueUuid);
        req.setModifyDateTime(modifyDateTime);
        CalmNetWorkRequest.with(fragment)
                .url(url)
                .requestContent(req)
                .responseClass(Queue.class)
                .showLoading()
                .responseProcessor(new CalmDatabaseProcessor<Queue>() {
                    @Override
                    protected boolean isSuccessful(ResponseObject response) {
                        return super.isSuccessful(response);
                    }

                    @Override
                    protected void transactionCallable(DatabaseHelper helper, Queue resp) throws Exception {
                        try {
                            if (resp != null) {
                                DBHelperManager.saveEntities(helper, Queue.class, resp);
                            }
                        } catch (Exception e) {
                            Log.e("saveSlideDish", e.getMessage());
                        }
                    }
                })
                .successListener(listener)
                .errorListener(listener)
                .tag("queueRecoverInvalid")
                .create();
    }

    @Override
    public void queueModify(FragmentActivity fragment, QueueModifyReq req, CalmResponseListener<ResponseObject<Queue>> listener) {
        String url = ServerAddressUtil.getInstance().queueModify();
        CalmNetWorkRequest.with(fragment)
                .url(url)
                .requestContent(req)
                .responseClass(Queue.class)
                .showLoading()
                .responseProcessor(new CalmDatabaseProcessor<Queue>() {
                    @Override
                    protected boolean isSuccessful(ResponseObject response) {
                        return super.isSuccessful(response);
                    }

                    @Override
                    protected void transactionCallable(DatabaseHelper helper, Queue resp) throws Exception {
                        try {
                            if (resp != null) {
                                DBHelperManager.saveEntities(helper, Queue.class, resp);
                            }
                        } catch (Exception e) {
                            Log.e("saveSlideDish", e.getMessage());
                        }
                    }
                })
                .successListener(listener)
                .errorListener(listener)
                .tag("queueModify")
                .create();
    }

    @Override
    public void queueCallNum(FragmentActivity fragment, QueueCallNumberReq req, CalmResponseListener<ResponseObject<QueueCallNumberResp>> listener) {
        String url = ServerAddressUtil.getInstance().queueCallNumber();
        CalmNetWorkRequest.with(fragment)
                .url(url)
                .requestContent(req)
                .responseClass(QueueCallNumberResp.class)
                .showLoading()
                .responseProcessor(new CalmDatabaseProcessor<QueueCallNumberResp>() {
                    @Override
                    protected boolean isSuccessful(ResponseObject response) {
                        return super.isSuccessful(response);
                    }

                    @Override
                    protected void transactionCallable(DatabaseHelper helper, QueueCallNumberResp resp) throws Exception {
                        try {
                            if (resp != null && resp.queueExtra != null) {
                                DBHelperManager.saveEntities(helper, QueueExtra.class, resp.queueExtra);
                            }
                        } catch (Exception e) {
                            Log.e("saveSlideDish", e.getMessage());
                        }
                    }
                })
                .successListener(listener)
                .errorListener(listener)
                .tag("QueueCallNumber")
                .create();
    }

    @Override
    public void predictWaitTime(Context context, String queueUuid, ResponseListener<QueuePredictWaitTimeResp> listener) {
        String url = ServerAddressUtil.getInstance().predictWaitTime();
        OpsRequest.Executor<QueuePredictWaitTimeReq, QueuePredictWaitTimeResp> executor = OpsRequest.Executor.create(url);
        QueuePredictWaitTimeReq req = new QueuePredictWaitTimeReq();
        req.setSyncFlag(queueUuid);
        executor.requestValue(req)
                .responseClass(QueuePredictWaitTimeResp.class)
                .execute(listener, "predictWaitTime");
    }

    @Override
    public void queryqtcode(Long queueId, ResponseListener<QueueExtra> listener) {

        String url = ServerAddressUtil.getInstance().queryQTCode();
        final QueueQTCodeReq req = new QueueQTCodeReq(queueId);
        OpsRequest.Executor<QueueQTCodeReq, QueueExtra> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(QueueExtra.class)
                .responseProcessor(new OpsRequest.DatabaseResponseProcessor<QueueExtra>() {
                    @Override
                    protected void transactionCallable(DatabaseHelper helper, QueueExtra resp) throws Exception {
                        if (resp != null) {
                            DBHelperManager.saveEntities(helper, QueueExtra.class, resp);
                        }
                    }
                })
                .execute(listener, "queryqtcode");
    }

    /**
     * 请求json
     *
     * @param json
     * @return
     */
    public String genMessageRequest(String json) {
        try {
            return "?&content=" + URLEncoder.encode(json, "UTF-8") + "&versionCode="
                    + SystemUtils.getVersionCode() + "&versionName="
                    + SystemUtils.getVersionName() + "&macAddress="
                    + SystemUtils.getMacAddress() + "&shopID="
                    + ShopInfoCfg.getInstance().shopId;
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "", e);
        }
        return null;
    }

    /**
     * 自助语音请求
     *
     * @param queue
     * @return
     */
    private QueuePhoneReq toQueuePhoneReq(Queue queue) {
        QueuePhoneReq req = new QueuePhoneReq();
        req.setName(queue.getName());
        String sex = getContext().getString(R.string.male);
        if (queue.getSex() != null && queue.getSex().equalsValue(Sex.FEMALE.value())) {
            sex = getContext().getString(R.string.female);
        }
        req.setSex(sex);
        req.setPhone(queue.getMobile());
        return req;
    }

    /**
     * 短信请求
     *
     * @param queue
     * @return
     */
    private QueueMessReq toQueueMessReq(Queue queue) {
        QueueMessReq req = new QueueMessReq();
        req.setName(queue.getName());
        QueueDal dal = new QueueDalImpl(context);

        try {
            CommercialQueueLine queueLine = dal.findQueueLineByid(queue.getQueueLineId());
            req.setTableTypeName(queueLine.getQueueName());
            long waitCount = dal.getCountByQueueUuid(queue);
            req.setWaitQueueNumber(waitCount + "");
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        req.setQueueNumber(queue.getQueueNumber() + "");

        String sex = getContext().getString(R.string.male);
        if (queue.getSex() != null && queue.getSex().equalsValue(Sex.FEMALE.value())) {
            sex = getContext().getString(R.string.female);
        }
        req.setSex(sex);
        req.setMobile(queue.getMobile());
        String erpNationCode = ShopInfoCfg.getInstance().getCurrency().getAreaCode();
        req.setNationalTelCode(queue.getNationalTelCode() != null ? queue.getNationalTelCode() : erpNationCode);
        if (queue.getName() == null) {
            req.setType("2");
        } else {
            req.setType("1");
        }
        return req;
    }

    /**
     * 自助语音请求
     */
    static class QueuePhoneReq {
        String name;

        String sex;

        String phone;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

    }

    /**
     * 短信请求
     */
    static class QueueMessReq {

        String timeZone = ShopInfoCfg.getInstance().timeZone;

        String name;

        String tableTypeName;

        String queueNumber;

        String waitQueueNumber;

        String sex;

        String mobile;

        String nationalTelCode;

        String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTableTypeName() {
            return tableTypeName;
        }

        public void setTableTypeName(String tableTypeName) {
            this.tableTypeName = tableTypeName;
        }

        public String getQueueNumber() {
            return queueNumber;
        }

        public void setQueueNumber(String queueNumber) {
            this.queueNumber = queueNumber;
        }

        public String getWaitQueueNumber() {
            return waitQueueNumber;
        }

        public void setWaitQueueNumber(String waitQueueNumber) {
            this.waitQueueNumber = waitQueueNumber;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setNationalTelCode(String nationalTelCode) {
            this.nationalTelCode = nationalTelCode;
        }
    }

	/*public static class QueueResp implements Type {
		int status;
		
		String message;
		
		public int getStatus() {
			return status;
		}
		
		public void setStatus(int status) {
			this.status = status;
		}
		
		public String getMessage() {
			return message;
		}
		
		public void setMessage(String message) {
			this.message = message;
		}
		
	}*/


    /**
     * 更新排队状态请求
     */
    private QueueReq toQueueStatusReq(int type, Queue queue) {
        QueueReq rsq = new QueueReq();
        rsq.setType(type);
        rsq.setQueueLineId(queue.getQueueLineId());
        rsq.setServerId(queue.getUuid());
        rsq.setLastSyncMarker(queue.getModifyDateTime());
        return rsq;
    }


    /**
     * 创建排队和排队入场;
     *
     * @param type
     * @param queue
     * @return
     */
    private QueueUpdateReq toQueueOrderReq(int type, Queue queue) {

        QueueUpdateReq queueUpdateReq = new QueueUpdateReq();
        queueUpdateReq.setName(queue.getName());
        queueUpdateReq.setSyncFlag(queue.getUuid());
        queueUpdateReq.setSex(queue.getSex().toString());
        queueUpdateReq.setMobile(queue.getMobile());
        queueUpdateReq.setQueueStatus(type + "");
        queueUpdateReq.setRepastPersonCount(queue.getRepastPersonCount());
        queueUpdateReq.setIsZeroOped(queue.getIsZeroOped().value());
        queueUpdateReq.setQueueSource(queue.getQueueSource().toString());
        queueUpdateReq.setQueueProof(queue.getQueueProof());
        queueUpdateReq.setNotifyType(queue.getNotifyType().toString());
        queueUpdateReq.setQueueLineId(queue.getQueueLineId());
        if (queue.getInDateTime() != null) {
            queueUpdateReq.setInDateTime(queue.getInDateTime().toString());
        }
//		queueUpdateReq.setIsOfficial(queue.getIsOfficial());
        queueUpdateReq.setTradeId(-1);
        queueUpdateReq.setNationalTelCode(queue.getNationalTelCode());
        queueUpdateReq.setCountry(queue.country);
        queueUpdateReq.setNation(queue.nation);
        queueUpdateReq.setMemo(queue.getMemo());
        return queueUpdateReq;
    }
}
