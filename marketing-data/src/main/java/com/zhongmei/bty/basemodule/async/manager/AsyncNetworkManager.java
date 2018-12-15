package com.zhongmei.bty.basemodule.async.manager;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v4.os.AsyncTaskCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.zhongmei.bty.basemodule.async.event.ActionAsyncFailed;
import com.zhongmei.bty.basemodule.async.event.ActionModifyTradeFailed;
import com.zhongmei.bty.basemodule.async.event.ActionOpenTableFailed;
import com.zhongmei.bty.basemodule.async.event.ActionPayFailed;
import com.zhongmei.bty.basemodule.async.listener.AsyncBatchModifyResponseListener;
import com.zhongmei.bty.basemodule.async.listener.AsyncModifyResponseListener;
import com.zhongmei.bty.basemodule.async.listener.AsyncOpenTableResponseListener;
import com.zhongmei.bty.basemodule.async.listener.AsyncResponseListener;
import com.zhongmei.bty.basemodule.async.operates.AsyncDal;
import com.zhongmei.bty.basemodule.async.util.AsyncNetworkUtil;
import com.zhongmei.bty.basemodule.pay.message.PaymentResp;
import com.zhongmei.bty.basemodule.pay.processor.PaymentRespProcessor;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.trade.message.TradeNewReq;
import com.zhongmei.bty.basemodule.trade.message.TradeReq;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.basemodule.trade.processor.TradeRespProcessor;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.db.local.LocalDBManager;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpState;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpType;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.util.Gsons;
import com.zhongmei.yunfu.context.util.NetworkUtil;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.data.R;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.yunfu.http.RequestObject;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.Beans;
import com.zhongmei.yunfu.util.ToastUtil;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import de.greenrobot.event.EventBus;

/**
 * Created by demo on 2018/12/15
 */
public class AsyncNetworkManager {

    private final static String TAG = AsyncNetworkManager.class.getSimpleName();

    private final static int AUTO_RETRY_TIME = 2 * 60 * 1000;//自动重试时限，单位ms

    private final static int AUTO_RETRY_INTERVAL = 5 * 1000;//自动重试间隔时间

    private final static String ASYNC_HANDLER_THREAD_TAG = "async_handler_thread";

    private static AsyncNetworkManager mInstance;

    private HandlerThread mHandlerThread;//子线程，供Handler使用

    private Handler mThreadHandler;//子线程交互的handler

    private Handler mMainHandler;//主线程交互的handler

    private List<AsyncHttpRecord> mListAllAsyncRecordCache;//缓存所有的异步操作纪录，其他模块的所有数据由这儿分发出去。

    private List<AsyncHttpRecordChange> mAsyncRecordChangeListeners;//存放异步纪录改变之后的回调监听

    private DataChangeObserver observer;

    private AsyncQueryAllRecord queryAllTask;

    private AsyncNetworkManager() {
        mHandlerThread = new HandlerThread(ASYNC_HANDLER_THREAD_TAG);
        mHandlerThread.start();
        mThreadHandler = new Handler(mHandlerThread.getLooper());
        mMainHandler = new Handler(Looper.getMainLooper());
    }

    public static AsyncNetworkManager getInstance() {
        synchronized (AsyncNetworkManager.class) {
            if (mInstance == null) {
                mInstance = new AsyncNetworkManager();
            }
            return mInstance;
        }
    }

    public void addAsyncRecordChangeListener(AsyncHttpRecordChange listener) {
        if (mAsyncRecordChangeListeners == null) {
            mAsyncRecordChangeListeners = new ArrayList<AsyncHttpRecordChange>();
        }
        mAsyncRecordChangeListeners.add(listener);
    }

    public void removeAsyncRecordChangeListener(AsyncHttpRecordChange listener) {
        if (mAsyncRecordChangeListeners != null && mAsyncRecordChangeListeners.contains(listener)) {
            mAsyncRecordChangeListeners.remove(listener);
        }
    }

    /**
     * 添加异步请求到队列中
     *
     * @param tradeVo
     * @param executor
     * @param listener
     * @param tag
     * @param <T>
     */
    public <T> void addRequest(TradeVo tradeVo, final AsyncHttpType type, OpsRequest.Executor executor,
                               final ResponseListener<T> listener, String tag) {
        try {
            final AsyncDal asyncDal = OperatesFactory.create(AsyncDal.class);
            //先构建异步记录
            final String recUuid = SystemUtils.genOnlyIdentifier();
            String url = executor.getUrl();
            String reqJson = executor.getRequestJson();
            //DinnerModifyPrintBean printBean = null;
            if (listener != null) {
                if (listener instanceof AsyncModifyResponseListener) {
                    AsyncModifyResponseListener modifyListener = (AsyncModifyResponseListener) listener;
                    //printBean = modifyListener.printBean;
                }

                if (listener instanceof AsyncBatchModifyResponseListener) {
                    AsyncBatchModifyResponseListener modifyListener = (AsyncBatchModifyResponseListener) listener;
                    //printBean = modifyListener.printBean;
                }
            }
            final AsyncHttpRecord rec = createAsyncRecord(recUuid, tradeVo, type, url, reqJson);
            //如果tradeid为空，先把状态置为失败，然后走没有tradeid的异步记录那套做处理
            if (rec.getTradeId() == null && rec.getType() != AsyncHttpType.OPENTABLE) {
                asyncDal.update(rec, AsyncHttpState.FAILED, "");
                noTradeIdModifyOrCash(rec, listener);
                return;
            }

            ResponseListener<T> tempListener = new EventResponseListener<T>(EventResponseListener.getEventName(listener)) {

                public void onResponse(final ResponseObject<T> response) {
                    try {
                        AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... params) {
                                try {
                                    updateAsyncRecordByResponse(rec, type, asyncDal, response);
                                    if (listener != null && listener instanceof AsyncResponseListener) {
                                        ((AsyncResponseListener) listener).setAsyncRec(rec);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                listener.onResponse(response);
                                super.onPostExecute(aVoid);
                            }
                        });

                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }

                public void onError(final VolleyError error) {
                    AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                asyncDal.update(rec, AsyncHttpState.FAILED, error.getMessage());
                                //设置异步记录给回调对象
                                if (listener != null && listener instanceof AsyncResponseListener) {
                                    ((AsyncResponseListener) listener).setAsyncRec(rec);
                                }

                                String recUuid = null;
                                if (rec != null) {
                                    recUuid = rec.getUuid();
                                }
                                autoRetry(recUuid, null);
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage(), e);
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            listener.onError(error);
                            super.onPostExecute(aVoid);
                        }
                    });
                }
            };
            executor.execute(tempListener, tag);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * 对应异步记录没有tradeid时，进行改单／收银
     *
     * @param rec
     * @param listener 改单／收银的回调
     * @throws Exception
     */
    private void noTradeIdModifyOrCash(AsyncHttpRecord rec, ResponseListener listener) throws Exception {
        TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
        TradeVo tradeVo = tradeDal.findTrade(rec.getTradeUuId());
        //确实没有开台成功
        if (tradeVo == null) {
            //先开台
            AsyncDal asyncDal = OperatesFactory.create(AsyncDal.class);
            List<AsyncHttpRecord> openTableRecs = asyncDal.query(rec.getTradeUuId(), Arrays.asList(AsyncHttpType.OPENTABLE));
            if (Utils.isNotEmpty(openTableRecs)) {
                AsyncHttpRecord openTableRec = openTableRecs.get(0);
                if (openTableRec.getStatus() == AsyncHttpState.EXCUTING || openTableRec.getStatus() == AsyncHttpState.RETRING) {
                    //对应的开台正在执行／重试
                    Log.i(TAG, "正在开台／重试，请稍后重试！");
                    return;
                } else {
                    Log.i(TAG, "准备重试开台！");
                    AsyncOpenTableResponseListener openTableResponseListener = new AsyncOpenTableResponseListener(EventResponseListener.getEventName(listener));
                    openTableResponseListener.setAsyncSourceRec(rec);
                    doRequestRetry(openTableRec, openTableResponseListener, "retry");
                }
            }
            //已经开台成功了，只是数据没有刷新到异步记录里
        } else {
            if (hasBeenModified(tradeVo)) {
                //订单开台成功后，已经被其他端修改了，删除要处理的异步记录
                Log.i(TAG, "订单虽然已经存在了，但是被其他客户端更新！");
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(R.string.async_trade_has_been_modified_after_open_table);
                    }
                });
                DBHelperManager.deleteById(AsyncHttpRecord.class, rec.getUuid());
                return;
            } else {
                //订单已经开台了，刷新AsyncHttpRecord的数据，然后进行重试
                Log.i(TAG, "刷新异步请求，准备重试异步改单／收银！");
                refreshReqJson(tradeVo, rec);
                doRequestRetry(rec, listener, "retry");
            }
        }
    }

    /**
     * 异步重试
     *
     * @param rec
     * @param listener
     * @param <T>
     */
    public <T> void retry(final AsyncHttpRecord rec, final ResponseListener<T> listener) {
        new Thread() {
            @Override
            public void run() {
                try {
                    AsyncDal asyncDal = OperatesFactory.create(AsyncDal.class);
//                    AsyncHttpRecord rec = asyncDal.query(recUuid);
                    //状态不是失败，退出重试
                    if (rec == null || rec.getStatus() != AsyncHttpState.FAILED) {
                        return;
                    }

//            doRequestRetry(rec, listener, "retry");

                    //开台除外，并且没有取到tradeId的异步记录，要先做开台或刷新订单数据的操作
                    if (rec.getTradeId() == null && rec.getType() != AsyncHttpType.OPENTABLE) {
                        noTradeIdModifyOrCash(rec, listener);
                    } else {
                        if (rec.getType() == AsyncHttpType.OPENTABLE) {
                            Trade trade = DBHelperManager.queryById(Trade.class, rec.getTradeUuId());
                            if (trade != null) {
                                //订单已经下单成功了，直接删除开台的异步记录
                                Log.i(TAG, "订单已经存在了，不再重试开台！");
                                mMainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showShortToast(R.string.async_trade_has_opened_table);
                                    }
                                });
                                DBHelperManager.deleteById(AsyncHttpRecord.class, rec.getUuid());
                                return;
                            }
                        }

                        Log.i(TAG, "准备重试异步开他／改单／收银！");
                        doRequestRetry(rec, listener, "retry");
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                super.run();
            }
        }.start();
    }

    public void cancel(final AsyncHttpRecord record) {
        new Thread() {
            @Override
            public void run() {
                try {
           /* AsyncDal asyncDal = OperatesFactory.create(AsyncDal.class);
            asyncDal.deleteByUUID(record.getUuid());*/
                    //统一调用线程安全的删除方法 modify 20170216
                    AsyncDal asyncDal = OperatesFactory.create(AsyncDal.class);
                    asyncDal.deleteByUUID(record.getUuid());
                    DBHelperManager.deleteById(AsyncHttpRecord.class, record.getUuid());
                    if (record.getType() == AsyncHttpType.OPENTABLE) {
                        //asyncDal.deleteByTradeUUID(record.getTradeUuId());
                        //统一调用线程安全的删除方法 modify 20170216
                        //这样删除会有问题的，服务器并没有删除 modify by dzb 20170302
//                DBHelper.deleteById(Trade.class, record.getTradeUuId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    /**
     * 订单是否被改过单
     *
     * @param tradeVo
     * @return
     * @throws Exception
     */
    private boolean hasBeenModified(TradeVo tradeVo) throws Exception {
        if (tradeVo != null && tradeVo.getTrade() != null) {
            Trade trade = tradeVo.getTrade();
            //订单对应的几个金额不为0时，说明已经被改单过
            if (trade.getSaleAmount().compareTo(BigDecimal.ZERO) != 0
                    || trade.getPrivilegeAmount().compareTo(BigDecimal.ZERO) != 0
                    || trade.getTradeAmount().compareTo(BigDecimal.ZERO) != 0
                    || trade.getTradeAmountBefore().compareTo(BigDecimal.ZERO) != 0) {
                return true;
            }
            //有菜品或者优惠时，说明已经被改单过
            if (Utils.isNotEmpty(tradeVo.getTradeItemList()) || Utils.isNotEmpty(tradeVo.getTradePrivileges())) {
                return true;
            }
        }

        return false;
    }

    private void refreshReqJson(TradeVo tradeVo, AsyncHttpRecord rec) throws Exception {
        if (rec == null || rec.getTradeId() != null || TextUtils.isEmpty(rec.getReqStr())) {
            return;
        }
        switch (rec.getType()) {
            case MODIFYTRADE:
                refreshModifyReqJson(tradeVo, rec);
                break;
            case UNION_MAIN_MODIFYTRADE:
                break;
            case UNION_SUB_MODIFYTRADE:
                break;
            case CASHER:
                break;
            default:
                break;
        }
    }

    /**
     * 根据开台的订单信息，更新改单的请求数据
     *
     * @param tradeVo
     * @param rec
     * @throws Exception
     */
    private void refreshModifyReqJson(TradeVo tradeVo, AsyncHttpRecord rec) throws Exception {
        if (rec == null || rec.getTradeId() != null || TextUtils.isEmpty(rec.getReqStr())) {
            return;
        }
        String oldJsonStr = rec.getReqStr();
        Gson gson = Gsons.gsonBuilder().create();
        Type type = OpsRequest.getRequestType(TradeNewReq.class);
        RequestObject<TradeNewReq> req = gson.fromJson(oldJsonStr, type);
        TradeNewReq tradeNewReq = req.getContent();
        if (tradeNewReq == null) {
            return;
        }

        TradeReq tradeReq = tradeNewReq.getTradeRequest();
        Long tradeId = null;
        String serialNumber = null;
        //更新请求里，开台后相关的信息
        if (tradeReq != null) {
            tradeReq.setRelatedId(tradeVo.getRelatedId());
            tradeReq.setRelatedType(tradeVo.getRelatedType());
            Trade trade = tradeVo.getTrade();
            if (trade != null) {
                tradeId = trade.getId();
                //更新订单基本信息
                tradeReq.setId(trade.getId());
                tradeReq.setBizDate(trade.getBizDate());
                tradeReq.setServerCreateTime(trade.getServerCreateTime());
                tradeReq.setServerUpdateTime(trade.getServerUpdateTime());
                tradeReq.setActionType(trade.getActionType());
            }

            //更新tradeextra信息
            TradeExtra tradeExtra = tradeVo.getTradeExtra();
            if (tradeExtra != null) {
                if (tradeReq.getTradeExtra() != null) {
                    Beans.copyProperties(tradeExtra, tradeReq.getTradeExtra(), "clientUpdateTime", "updatorId", "updatorName");
                }
                if (Utils.isNotEmpty(tradeReq.getTradeExtras())) {
                    for (TradeExtra reqExtra : tradeReq.getTradeExtras()) {
                        Beans.copyProperties(tradeExtra, reqExtra, "clientUpdateTime", "updatorId", "updatorName");
                    }
                }
                serialNumber = tradeExtra.getSerialNumber();
            }

            //更新tradetable信息
            if (Utils.isNotEmpty(tradeVo.getTradeTableList()) && Utils.isNotEmpty(tradeReq.getTradeTables())) {
                TradeTable tradeTable = tradeVo.getTradeTableList().get(0);
                TradeTable reqTable = tradeReq.getTradeTables().get(0);

                reqTable.setId(tradeTable.getId());
                reqTable.setTradeId(tradeTable.getTradeId());
                reqTable.setServerCreateTime(tradeTable.getServerCreateTime());
                reqTable.setServerUpdateTime(tradeTable.getServerUpdateTime());
                if (Utils.isNotEmpty(tradeReq.getTradeItems())) {
                    for (TradeItem tradeItem : tradeReq.getTradeItems()) {
                        tradeItem.setTradeId(tradeId);
                        tradeItem.setTradeTableId(tradeTable.getId());
                    }
                }
            }

            //更新各个对象的tradeid
            if (Utils.isNotEmpty(tradeReq.getTradeCustomers())) {
                for (TradeCustomer reqCustomer : tradeReq.getTradeCustomers()) {
                    reqCustomer.setTradeId(tradeId);
                }
            }
            if (tradeReq.getTradeDeposit() != null) {
                tradeReq.getTradeDeposit().setTradeId(tradeId);
            }
            if (Utils.isNotEmpty(tradeReq.getTradeItems())) {
                for (TradeItem tradeItem : tradeReq.getTradeItems()) {
                    tradeItem.setTradeId(tradeId);
                }
            }
            if (Utils.isNotEmpty(tradeReq.getTradePlanActivitys())) {
                for (TradePlanActivity tradePlanActivity : tradeReq.getTradePlanActivitys()) {
                    tradePlanActivity.setTradeId(tradeId);
                }
            }
            if (Utils.isNotEmpty(tradeReq.getTradeItemPlanActivitys())) {
                for (TradeItemPlanActivity tradeItemPlanActivity : tradeReq.getTradeItemPlanActivitys()) {
                    tradeItemPlanActivity.setTradeId(tradeId);
                }
            }
            if (Utils.isNotEmpty(tradeReq.getTradePrivileges())) {
                for (TradePrivilege tradePrivilege : tradeReq.getTradePrivileges()) {
                    tradePrivilege.setTradeId(tradeId);
                }
            }
            if (tradeReq.getTradeUser() != null) {
                tradeReq.getTradeUser().setTradeId(tradeId);
            }
            if (Utils.isNotEmpty(tradeReq.getTradeBuffetPeoples())) {
                for (TradeBuffetPeople tradeBuffetPeople : tradeReq.getTradeBuffetPeoples()) {
                    tradeBuffetPeople.setTradeId(tradeId);
                }
            }
            if (tradeReq.getTradeGroup() != null) {
                tradeReq.getTradeGroup().setTradeId(tradeId);
            }
        }

        AsyncDal asyncDal = OperatesFactory.create(AsyncDal.class);
        RequestObject<TradeNewReq> requestObject = RequestObject.create(tradeNewReq);
        requestObject.reqMarker = req.reqMarker;//拷贝原来请求的标签
        String newReqJson = gson.toJson(requestObject);
        rec.setReqStr(newReqJson);
        rec.setTradeId(tradeId);
        rec.setSerialNumber(serialNumber);
        asyncDal.update(rec.getUuid(), tradeId, newReqJson, serialNumber);
    }

    private void refreshCashReqJson(TradeVo tradeVo, AsyncHttpRecord rec) {

    }

    /**
     * 构建一条异步记录对象
     *
     * @param tradeVo
     * @param type
     * @return
     */
    private AsyncHttpRecord createAsyncRecord(String uuid, TradeVo tradeVo, AsyncHttpType type, String url, String jsonRequestStr) {
        final AsyncHttpRecord asyncHttpRecord = new AsyncHttpRecord();
        asyncHttpRecord.setUuid(uuid);
        asyncHttpRecord.validateCreate();
        asyncHttpRecord.setStatus(AsyncHttpState.EXCUTING);//新建默认执行中
        asyncHttpRecord.setReqUrl(url);
        asyncHttpRecord.setReqStr(jsonRequestStr);
        //if (printBean != null) {
        //asyncHttpRecord.setPrintBeanJson(new Gson().toJson(printBean));
        //}
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            asyncHttpRecord.setCreatorId(user.getId());
            asyncHttpRecord.setCreatorName(user.getName());
            asyncHttpRecord.setUpdatorId(user.getId());
            asyncHttpRecord.setUpdatorName(user.getName());
        }
        if (ShopInfoCfg.getInstance() != null) {
            asyncHttpRecord.setDeviceNo(ShopInfoCfg.getInstance().getTabletNumberFormat());
        }

        if (tradeVo != null) {
            if (Utils.isNotEmpty(tradeVo.getTradeTableList())) {
                TradeTable tradeTable = tradeVo.getTradeTableList().get(0);
                asyncHttpRecord.setTableName(tradeTable.getTableName());
                asyncHttpRecord.setTableId(tradeTable.getTableId());
            }
            //联台主单没有tradeTable
            if (tradeVo.getTableId() != null) {
                asyncHttpRecord.setTableId(tradeVo.getTableId());
                asyncHttpRecord.setTableName(tradeVo.getTableName());
            }

            if (tradeVo.getTradeExtra() != null) {
                asyncHttpRecord.setSerialNumber(tradeVo.getTradeExtra().getSerialNumber());
            }
            Trade trade = tradeVo.getTrade();
            if (trade != null) {
                asyncHttpRecord.setTradeId(trade.getId());
                asyncHttpRecord.setTradeUuId(trade.getUuid());
                asyncHttpRecord.setTradeUpdateTime(trade.getServerUpdateTime());
            }
            asyncHttpRecord.setType(type);
        }
        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            //更新后的数据插入数据库
            // modify 20170216 start 添加事务来实现线程同步
            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, AsyncHttpRecord.class, asyncHttpRecord);
                    return null;
                }
            });
            // modify 20170216 end 添加事务来实现线程同步
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            LocalDBManager.releaseHelper();
        }
        return asyncHttpRecord;
    }

    // 根据下行接口更新记录状态
    private AsyncHttpRecord updateAsyncRecordByResponse(AsyncHttpRecord rec, AsyncHttpType type, AsyncDal asyncDal, ResponseObject response) throws Exception {
        ActionAsyncFailed action = null;
        switch (type) {
            case OPENTABLE://开台
                action = new ActionOpenTableFailed();
                break;
            case MODIFYTRADE://改单
            case UNION_MAIN_MODIFYTRADE:
            case UNION_SUB_MODIFYTRADE:
                action = new ActionModifyTradeFailed();
                break;
            case CASHER://收银
                action = new ActionPayFailed();
                break;
        }

        if (ResponseObject.isOk(response)) {
            rec = asyncDal.update(rec, AsyncHttpState.SUCCESS, response.getMessage());
        } else {
            if (action != null) {
                //业务失败，发送eventbus，通知界面弹框（只能删除数据）
                action.asyncRec = rec;
                action.errorMsg = response.getMessage();
                action.canRetry = false;
                EventBus.getDefault().post(action);
            }

            //直接再这里删除异步记录
            asyncDal.deleteByUUID(rec.getUuid());

        }

        return rec;
    }

    /**
     * 执行异步重试
     *
     * @param rec
     * @param listener
     * @param tag
     * @param <T>
     */
    private <T> void doRequestRetry(final AsyncHttpRecord rec, final ResponseListener<T> listener, String tag) throws Exception {
        //设置异步记录给回调对象
        if (listener != null && listener instanceof AsyncResponseListener) {
            ((AsyncResponseListener) listener).setAsyncRec(rec);
        }

        Class responseClass = TradeResp.class;
        //考虑如果listener或者responseClass为空
        OpsRequest.Executor<String, T> executor = OpsRequest.Executor.create(rec.getReqUrl());
        String reqStr = rec.getReqStr();
        switch (rec.getType()) {
            case OPENTABLE:
            case MODIFYTRADE:
            case UNION_MAIN_MODIFYTRADE:
            case UNION_SUB_MODIFYTRADE:
                responseClass = TradeResp.class;
                executor.responseClass(responseClass).responseProcessor(new TradeRespProcessor());
                break;
            case CASHER:
                responseClass = PaymentResp.class;
                executor.responseClass(responseClass).responseProcessor(new PaymentRespProcessor());
                break;
            default:
                break;
        }
        executor.requestValue(reqStr);
//                .responseClass(responseClass)
//                .responseProcessor(TradeOperatesImpl.getPaymentRespProcessor());

        final AsyncDal asyncDal = OperatesFactory.create(AsyncDal.class);
        asyncDal.retryCountPlus(rec);//重试次数＋1
        final String recUuid = rec.getUuid();
        ResponseListener<T> tempListener = new EventResponseListener<T>(EventResponseListener.getEventName(listener)) {

            public void onResponse(final ResponseObject<T> response) {

                AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            updateAsyncRecordByResponse(rec, rec.getType(), asyncDal, response);
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        listener.onResponse(response);
                        super.onPostExecute(aVoid);
                    }
                });
            }

            public void onError(final VolleyError error) {

                AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            asyncDal.update(rec, AsyncHttpState.FAILED, error.getMessage());

                            String recUuid = null;
                            if (rec != null) {
                                recUuid = rec.getUuid();
                            }
                            autoRetry(recUuid, null);
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        listener.onError(error);
                        super.onPostExecute(aVoid);
                    }
                });


            }

        };
        executor.execute(tempListener, tag, true);
        asyncDal.update(rec, AsyncHttpState.RETRING, BaseApplication.sInstance.getString(R.string.async_is_retry));
    }

    /**
     * 自动重试异步请求
     *
     * @param recUuid
     * @param sourceRec
     */
    private void autoRetry(String recUuid, final AsyncHttpRecord sourceRec) throws Exception {
        if (!NetworkUtil.isNetworkConnected()) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showShortToast(R.string.connect_network_error);
                }
            });
            return;
        }

        if (TextUtils.isEmpty(recUuid)) {
            return;
        }
        AsyncDal asyncDal = OperatesFactory.create(AsyncDal.class);

        final AsyncHttpRecord rec = asyncDal.query(recUuid);
        //时限内自动重试
        if (rec != null && rec.getClientCreateTime() != null
                && (System.currentTimeMillis() - rec.getClientCreateTime()) <= AUTO_RETRY_TIME) {
            //间隔时间，呈阶梯式增长
            int delay = rec.getRetryCount().intValue() * AUTO_RETRY_INTERVAL;
            if (mThreadHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //延时任务真正执行时，再进行一次时限判断
                    if (rec != null && rec.getClientCreateTime() != null
                            && (System.currentTimeMillis() - rec.getClientCreateTime()) <= AUTO_RETRY_TIME) {
                        AsyncNetworkUtil.retryAsyncOperate(rec, sourceRec);
                    }
                }
            }, delay)) ;
        }
    }

    /**
     * @Date 2016/10/14
     * @Description:根据订单号查询是否有未成功的记录
     * @Param
     * @Return
     */
    public boolean queryNotSuccess(Context context, Long tradeId) {
        AsyncHttpRecord record = null;

        if (Utils.isNotEmpty(mListAllAsyncRecordCache)) {
            for (AsyncHttpRecord asyncHttpRecord : mListAllAsyncRecordCache) {
                if (tradeId.equals(asyncHttpRecord.getTradeId()) && asyncHttpRecord.getStatus() != AsyncHttpState.SUCCESS) {
                    record = asyncHttpRecord;
                    break;
                }
            }
        }

        String message = AsyncNetworkUtil.getAsyncOperateTip(record);

        if (!TextUtils.isEmpty(message)) {
            ToastUtil.showShortToast(message);

            return true;
        }

        return false;
    }


    /**
     * @Date 2016/10/14
     * @Description:根据订单号查询是否有未成功的记录，不提示
     * @Param
     * @Return
     */
    public boolean queryNotSuccessNotTip(Long tradeId) {
        if (Utils.isEmpty(mListAllAsyncRecordCache)) {
            return false;
        }

        for (AsyncHttpRecord asyncHttpRecord : mListAllAsyncRecordCache) {
            if (tradeId.equals(asyncHttpRecord.getTradeId()) && asyncHttpRecord.getStatus() != AsyncHttpState.SUCCESS) {
                return true;
            }

        }

        return false;
    }

    /**
     * 主动调用查询，将查询的数据返回
     *
     * @param listener
     */
    public void queryAllAsyncRecord(final AsyncHttpRecordChange listener) {
        mThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Utils.isEmpty(mListAllAsyncRecordCache)) {
                        mListAllAsyncRecordCache = queryAllAsyncRecord();
                    }
                    //数据分发
                    if (listener != null) {
                        listener.onChange(mListAllAsyncRecordCache);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private List<AsyncHttpRecord> queryAllAsyncRecord() throws Exception {
        AsyncDal asyncDal = OperatesFactory.create(AsyncDal.class);
        return asyncDal.queryAllRecord();
    }

    public void registDataChangeObserver() {
        if (observer == null) {
            observer = new DataChangeObserver();
        }
        DatabaseHelper.Registry.register(observer);
    }

    public void unregistDataChangeObsserver() {
        if (observer != null) {
            DatabaseHelper.Registry.unregister(observer);
            observer = null;
        }
    }

    /**
     * 数据库数据更改后的监听
     */
    private class DataChangeObserver implements DatabaseHelper.DataChangeObserver {
        @Override
        public void onChange(Collection<Uri> uris) {
            if (uris.contains(DBHelperManager.getUri(AsyncHttpRecord.class))) {
                if (queryAllTask == null || queryAllTask.getStatus() == AsyncTask.Status.FINISHED) {
                    queryAllTask = new AsyncQueryAllRecord();
                    queryAllTask.execute();
                }

            }
        }
    }

    class AsyncQueryAllRecord extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                synchronized (mListAllAsyncRecordCache) {
                    mListAllAsyncRecordCache = queryAllAsyncRecord();
                    //数据分发
                    if (Utils.isNotEmpty(mAsyncRecordChangeListeners)) {
                        for (AsyncHttpRecordChange mAsyncRecordChangeListener : mAsyncRecordChangeListeners) {
                            mAsyncRecordChangeListener.onChange(mListAllAsyncRecordCache);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public interface AsyncHttpRecordChange {
        void onChange(List<AsyncHttpRecord> allRecord);
    }

    public void onDestory() {
        if (mListAllAsyncRecordCache != null) {
            mListAllAsyncRecordCache.clear();
            mListAllAsyncRecordCache = null;
        }
    }
}
