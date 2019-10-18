package com.zhongmei.bty.basemodule.devices.liandipos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;


import com.landicorp.impl.IMessageListener;
import com.landicorp.model.MessageBean;
import com.landicorp.model.RequestData;
import com.landicorp.model.ResponseData;
import com.landicorp.service.MisPos;
import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.yunfu.context.util.log.BaseLogAction;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.bty.commonmodule.database.entity.local.PosSettlementLog;
import com.zhongmei.bty.commonmodule.database.entity.local.PosTransLog;
import com.zhongmei.bty.basemodule.database.entity.pay.PaymentDevice;
import com.zhongmei.bty.basemodule.database.entity.pay.PaymentItemUnionpay;
import com.zhongmei.bty.basemodule.database.operates.impl.PosLogsDal;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.bty.basemodule.devices.liandipos.contants.LDRequestConstant;
import com.zhongmei.bty.basemodule.devices.liandipos.contants.LDResponseConstant;
import com.zhongmei.bty.commonmodule.database.enums.PosOpType;
import com.zhongmei.yunfu.util.Checks;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.context.util.SystemUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public final class NewLiandiposManager {

    private static final String TAG = NewLiandiposManager.class.getSimpleName();

    private static NewLiandiposManager instance;

    public static synchronized NewLiandiposManager getInstance() {
        if (instance == null) {
            instance = new NewLiandiposManager();
        }
        return instance;
    }

    private final Handler mPoster;

    private boolean misPosWorking;

    private NewLiandiposManager() {
        mPoster = new Handler(Looper.getMainLooper());
        misPosWorking = false;
    }

    public void setMisPosWorking(boolean misPosWorking) {
        this.misPosWorking = misPosWorking;
    }


    public PosTransLog queryTrans(String transUuid) {
        try {
            return DBHelperManager.queryById(PosTransLog.class, transUuid);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }


    @SuppressLint("SimpleDateFormat")
    public boolean canRepeal(RefundRef ref) {
        boolean issame = false;
        boolean iscan = false;
        if (!isSamePosDeviceId(ref.terminalNumber)) {
            issame = false;
        } else {
            issame = true;
        }

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String today = df.format(new Date());
        if (compare_date(today, ref.transDate) == 0) {            iscan = true;
        } else {
            iscan = false;
        }
        Log.d(TAG, TAG + ":iscan=" + iscan + "____issame=" + issame);
        return issame && iscan;
    }


    private static boolean isSamePosDeviceId(String terminalNumber) {
        String localnum = SharedPreferenceUtil.getSpUtil().getString(Constant.SP_POS_DEVICIE_ID, "");
        Log.d(TAG, TAG + "------SP_POS_DEVICIE_ID=" + localnum + "terminalNumber=" + terminalNumber);
        if (TextUtils.isEmpty(localnum)) {
            return true;
        } else {
            return localnum.equals(terminalNumber);
        }
    }


    public void startPay(final BigDecimal amount, final OnTransListener listener) {
        Checks.verifyNotNull(amount, "amount");
        Checks.verifyNotNull(listener, "listener");
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("The amount must > 0.");
        }
        if (amount.scale() > 3) {
            throw new IllegalArgumentException("The amount scale must <= 3.");
        }
        if (misPosWorking) {
            onOccupied(listener);
            return;
                                }
        new Thread() {
            @Override
            public void run() {
                doPay(amount, createProxy(listener));
            }
        }.start();
    }


    public void completePay(String transUuid) {
        deleteTrans(transUuid);
    }


    public void cancelPay(final PosTransLog transLog, final OnTransListener listener) {
        Checks.verifyNotNull(transLog, "transLog");
        Checks.verifyNotNull(listener, "listener");
        if (misPosWorking) {
            onOccupied(listener);
            return;
                                }
        new Thread() {
            @Override
            public void run() {
                RefundRef ref = RefundRef.valueOf(transLog);
                BigDecimal amount = RefundRef.getAmount(ref);
                                doRepealOrRefund(amount, ref, false, createCancelPayProxy(transLog.getUuid(), listener));
            }
        }.start();
    }


    public void startRefund(final String uuid, final String posTraceNo, final OnTransListener listener) {
        Checks.verifyNotNull(posTraceNo, "posTraceNo");
        Checks.verifyNotNull(listener, "listener");
        if (misPosWorking) {
            onOccupied(listener);
            return;
                                }
        new Thread() {
            @Override
            public void run() {
                RefundRef ref = new RefundRef(uuid, null, null, null, null, posTraceNo, null);                doRepealOrRefund(null, ref, true, createProxy(listener));
            }
        }.start();
    }


    public void startReadCardID(final Context context, final OnTransListener listener, final boolean isdelay) {
        Checks.verifyNotNull(listener, "listener");

        if (misPosWorking) {
            onOccupied(listener);
            return;
                                }
        new Thread() {
            @Override
            public void run() {
                try {
                    isSuccessInit(context);
                    if (isdelay) {
                        Thread.sleep(200);
                    }

                    doReadCardId(createProxy(listener));
                } catch (InterruptedException e) {
                                        Log.d(TAG, TAG + e.toString());
                    e.printStackTrace();
                }

            }
        }.start();
    }


    public void startReadKeyboardNum(final Context context, final OnTransListener listener) {
        Checks.verifyNotNull(listener, "listener");

        if (misPosWorking) {
            onOccupied(listener);
            return;
        }
        new Thread() {
            @Override
            public void run() {
                isSuccessInit(context);
                doReadKeyboardNum(createProxy(listener));
            }
        }.start();
    }

    public void register(final Context context, final OnTransListener listener) {
        Checks.verifyNotNull(listener, "listener");

        if (misPosWorking) {
            onOccupied(listener);
            return;
        }
        new Thread() {
            @Override
            public void run() {
                isSuccessInit(context);
                doRegister(createProxy(listener));
            }
        }.start();
    }


    public void completeRefund(String transUuid) {
        deleteTrans(transUuid);
    }


    private MisPos createMisPos(ListenerProxy listener) {
        MisPos misPos = new MisPos();
        misPos.setOnMessageListener(new MessageListenerImpl(listener));
        return misPos;
    }

    private ListenerProxy createProxy(OnTransListener listener) {
        return new ListenerProxy(mPoster, listener);
    }

    private ListenerProxy createCancelPayProxy(String transUuid, OnTransListener listener) {
        return new CancelPayListenerProxy(transUuid, mPoster, listener);
    }

    private void onOccupied(final OnTransListener listener) {
        if (isMainThread()) {
            listener.onFailure(generateErroLDResponse(MESSAGE_OCCUPIED));
        } else {
            mPoster.post(new Runnable() {
                @Override
                public void run() {
                    listener.onFailure(generateErroLDResponse(MESSAGE_OCCUPIED));
                }
            });
        }
    }


    private void doPay(BigDecimal amount, ListenerProxy listener) {
        try {
            RequestData request = createPayRequest(amount);
            NewLDResponse ldResponse = trans(createMisPos(listener), request);
            if (ldResponse.isSuccess()) {
                                PosTransLog log = storeLog(toTransLog(ldResponse));
                listener.onConfirm(log);
            } else {
                listener.onFailure(ldResponse);
            }
        } catch (Exception e) {
            Log.e(TAG, "doPay error!", e);
            listener.onFailure(null);
        }
    }


    private void doRepealOrRefund(BigDecimal amount, RefundRef ref, boolean logEnable, ListenerProxy listener) {
        try {
                        RequestData request = createRepealRequest(ref.posTraceNo, ref.transUuid);
            request.PutValue(LDRequestConstant.AMOUNT, fillString("0"));

            NewLDResponse ldResponse = trans(createMisPos(listener), request);
            if (ldResponse.isSuccess()) {
                PosTransLog log = toTransLog(ldResponse);
                                if (logEnable) {
                    storeLog(log);
                }
                listener.onConfirm(log);
            } else {
                                                listener.onFailure(ldResponse);
            }
        } catch (Exception e) {
            Log.e(TAG, "doRepealOrRefund error!", e);
            listener.onFailure(null);
        }
    }


    private void doReadCardId(ListenerProxy listener) {
        try {
            RequestData request = createCardIdRequest();
            NewLDResponse ldResponse = trans(createMisPos(listener), request);
            if (ldResponse.isSuccess()) {
                PosTransLog log = toTransLog(ldResponse);
                listener.onConfirm(log);
            } else {
                listener.onFailure(ldResponse);
            }
        } catch (Exception e) {
            Log.e(TAG, "doPay error!", e);
            listener.onFailure(null);
        }
    }


    private void doReadKeyboardNum(ListenerProxy listener) {

        try {
            RequestData request = createKeyboardRequest();
            NewLDResponse ldResponse = trans(createMisPos(listener), request);
            if (ldResponse.isSuccess()) {
                PosTransLog log = toTransLog(ldResponse);
                listener.onConfirm(log);
            } else {
                listener.onFailure(ldResponse);
            }
        } catch (Exception e) {
            Log.e(TAG, "doPay error!", e);
            listener.onFailure(null);
        }
    }


    private void doRegister(ListenerProxy listener) {
        try {
            RequestData request = createRegisterData();
            NewLDResponse ldResponse = trans(createMisPos(listener), request);
            if (ldResponse.isSuccess()) {
                PosTransLog log = toTransLog(ldResponse);
                listener.onConfirm(log);
            } else {
                listener.onFailure(ldResponse);
            }
        } catch (Exception e) {
            Log.e(TAG, "doRegester error!", e);
            listener.onFailure(null);
        }
    }


    private NewLDResponse trans(MisPos misPos, RequestData request) {
        misPosWorking = true;
        try {
            NewLDResponse ldResponse = submit(misPos, request);
            if (ldResponse.isStorageFull()) {
                                await(SHORT_DELAY);
                ldResponse = submit(misPos, createSettlementRequestData());
                if (ldResponse.isSuccess()) {
                                        storeLog(toSettlementLog(ldResponse));
                    await(LONG_DELAY);
                    ldResponse = submit(misPos, request);

                }
            }
            return ldResponse;
        } finally {
            misPosWorking = false;
        }
    }


    private static final int SHORT_DELAY = 500;


    private static final int LONG_DELAY = 4000;


    private static final String TRANS_TYPE_REGISER = "01";


    private static final String TRANS_TYPE_PAY = "02";


    private static final String TRANS_TYPE_REPEAL = "03";


    private static final String TRANS_TYPE_RETURN = "04";

    private static final String TRANS_TYPE_SETTLEMENT = "14";


    private static final String TRANS_TYPE_READCARDID = "101";


    private static final String TRANS_TYPE_READKEYBORD = "102";


    private static NewLDResponse submit(MisPos misPos, RequestData request) {
                ResponseData response = new ResponseData();
        long code = 0;
        try {
            code = misPos.TransProcess(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "code: " + code);
                return new NewLDResponse(response);
    }

    private static PosSettlementLog toSettlementLog(NewLDResponse response) {
        PosSettlementLog log = new PosSettlementLog();
        log.validateCreate();
        log.setUuid(uuid());
        log.setTransDate(response.getTransDate());
        log.setTransTime(response.getTransTime());
        log.setTerminalNumber(response.getTerminalNumber());
        return log;
    }

    private static PosTransLog toTransLog(NewLDResponse response) {
        PosTransLog log = new PosTransLog();
        log.validateCreate();
        log.setUuid(response.getTransCheck());
        log.setAcqNumber(response.getAcqNumber());
        log.setAppName(response.getAppName());
        log.setBatchNumber(response.getBatchNumber());
        log.setCardName(response.getCardName());
        log.setCardNumber(response.getCardNumber());
        log.setExpireDate(response.getExpireDate());
        log.setHostSerialNumber(response.getHostSerialNumber());
        log.setIssName(response.getIssName());
        log.setIssNumber(response.getIssNumber());
        log.setMerchantName(response.getMerchantName());
        log.setMerchantNumber(response.getMerchantNumber());
        log.setPosTraceNumber(response.getPosTraceNumber());
        log.setTerminalNumber(response.getTerminalNumber());
        log.setTransDate(response.getTransDate());
        log.setTransTime(response.getTransTime());
        log.setTransType(response.getTransType());
        log.setKeyValue(response.getKeyValue());
        log.setMemo(response.getMemo());
        try {
            if (!TextUtils.isEmpty(response.getAmount())) {
                log.setAmount(Integer.valueOf(response.getAmount()));
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        PosOpType posOpType = PosOpType.PAY;
        if (TRANS_TYPE_REPEAL.equals(response.getTransType()) || TRANS_TYPE_RETURN.equals(response.getTransType())) {
            posOpType = PosOpType.REFUND;
        }
        log.setPosOpType(posOpType);
        return log;
    }

    private static PosSettlementLog storeLog(PosSettlementLog log) {

        PosLogsDal posLogsDal = new PosLogsDal();
        posLogsDal.storePosSettlementLog(log);
        return log;
    }

    private static PosTransLog storeLog(PosTransLog log) {

        PosLogsDal posLogsDal = new PosLogsDal();
        posLogsDal.storePosTransLog(log);
        return log;
    }

    private static void deleteTrans(String transUuid) {
        try {
            DBHelperManager.deleteById(PosTransLog.class, transUuid);
        } catch (Exception e) {
            Log.e(TAG, "Delete PosTransLog error! uuid=" + transUuid, e);
        }
    }


    public void deletePosTransLogAsync(final String transUuid) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                                deleteTrans(transUuid);
            }
        }.start();
    }


    private static void await(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Log.i(TAG, "", e);
        }
    }


    private static RequestData createPayRequest(BigDecimal amount) {
        RequestData reqData = createRequestData();
        reqData.PutValue(LDRequestConstant.TRANS_TYPE, TRANS_TYPE_PAY);
        String value = MathDecimal.toTrimZeroString(amount.multiply(BigDecimal.valueOf(100)));
        reqData.PutValue(LDRequestConstant.AMOUNT, fillString(value));
        reqData.PutValue(LDRequestConstant.TRANS_CHECK, uuid());
        return reqData;
    }


    private static String fillString(String str) {
        String fillstr = "000000000000000000000000000000000000";
        int len = 12 - str.length();
        return fillstr.substring(0, len) + str;
    }


    private static RequestData createCardIdRequest() {
        RequestData reqData = createRequestData();
        reqData.PutValue(LDRequestConstant.TRANS_TYPE, TRANS_TYPE_READCARDID);
        reqData.PutValue(LDRequestConstant.TRANS_CHECK, uuid());
        return reqData;
    }



    private static RequestData createKeyboardRequest() {
        RequestData reqData = createRequestData();
        reqData.PutValue(LDRequestConstant.TRANS_TYPE, TRANS_TYPE_READKEYBORD);
        reqData.PutValue(LDRequestConstant.TRANS_CHECK, uuid());
        return reqData;
    }


    private static RequestData createRepealRequest(String oldPosTraceNo, String uuid) {
        RequestData reqData = createRequestData();
        reqData.PutValue(LDRequestConstant.TRANS_TYPE, TRANS_TYPE_REPEAL);
        reqData.PutValue(LDRequestConstant.TRANS_CHECK, uuid);
        reqData.PutValue(LDRequestConstant.OLD_TRACE_NUMBER, oldPosTraceNo);
        return reqData;
    }


    private static RequestData createSettlementRequestData() {
        RequestData reqData = createRequestData();
        reqData.PutValue(LDRequestConstant.TRANS_TYPE, TRANS_TYPE_SETTLEMENT);
        return reqData;
    }

    private static RequestData createRequestData() {
        RequestData reqData = new RequestData();
        reqData.PutValue(LDRequestConstant.CARD_TYPE, "01");
        reqData.PutValue("appname", BaseLogAction.S_BRAND_NAME);
        return reqData;
    }

    private static RequestData createRegisterData() {
        RequestData reqData = new RequestData();
        reqData.PutValue(LDRequestConstant.TRANS_TYPE, TRANS_TYPE_REGISER);
        reqData.PutValue(LDRequestConstant.CARD_TYPE, "01");
        reqData.PutValue("appname", BaseLogAction.S_BRAND_NAME);
        return reqData;
    }

    private static String uuid() {
        return SystemUtils.genOnlyIdentifier();
    }

    private static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }


    private static class MessageListenerImpl implements IMessageListener {

        private final ListenerProxy mListener;

        private boolean mNotified;

        private boolean mNotifiedIsAcive;

        private MessageBean mMessage;

        MessageListenerImpl(ListenerProxy listener) {
            mListener = listener;
            mNotified = false;
            mNotifiedIsAcive = false;
        }

        @Override
        public MessageBean getMsg() {
            return mMessage;
        }

        @Override
        public void setMsg(MessageBean msg) {
            this.mMessage = msg;
        }

        @Override
        public void showMessage() {
                        if (!mNotifiedIsAcive && mMessage.type == 0x01) {
                mNotifiedIsAcive = true;
                mListener.onActive();
            } else if (!mNotified && mMessage.type == 0x00) {
                mNotified = true;
                mListener.onStart();
            }
        }

    }


    private static class ListenerProxy implements OnTransListener {

        private final Handler mPoster;

        private final OnTransListener mListener;

        ListenerProxy(Handler poster, OnTransListener listener) {
            this.mPoster = poster;
            this.mListener = listener;
        }

        @Override
        public void onActive() {
            mPoster.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onActive();
                    Log.i(TAG, "onActive");
                }
            });
        }

        @Override
        public void onStart() {
            mPoster.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onStart();
                    Log.i(TAG, "onStart");
                }
            });
        }

        @Override
        public void onConfirm(final PosTransLog log) {
            mPoster.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onConfirm(log);
                    Log.i(TAG, "onConfirm");
                }
            });
        }

        @Override
        public void onFailure(final NewLDResponse ldResponse) {
            mPoster.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onFailure(ldResponse);
                    Log.i(TAG, "onFailure");
                }
            });
        }

    }


    private static class CancelPayListenerProxy extends ListenerProxy {

        private final String transUuid;

        CancelPayListenerProxy(String transUuid, Handler poster, OnTransListener listener) {
            super(poster, listener);
            this.transUuid = transUuid;
        }

        @Override
        public void onConfirm(final PosTransLog log) {
                        deleteTrans(transUuid);
            super.onConfirm(log);
        }

    }


    public static class RefundRef {

        public final String transUuid;

        public final String terminalNumber;

        public final String transDate;

        public final String transTime;

        public final String posTraceNo;

        public final String hostSerialNo;

        public final Integer amount;

        private RefundRef(String transUuid, String terminalNumber, Integer amount, String transDate, String transTime,
                          String posTraceNo, String hostSerialNo) {
            this.transUuid = transUuid;
            this.terminalNumber = terminalNumber;
            this.amount = amount;
            this.transDate = transDate;
            this.transTime = transTime;
            this.posTraceNo = posTraceNo;
            this.hostSerialNo = hostSerialNo;
        }


        public static RefundRef valueOf(PosTransLog log) {
            return new RefundRef(log.getUuid(), log.getTerminalNumber(), log.getAmount(), log.getTransDate(),
                    log.getTransTime(), log.getPosTraceNumber(), log.getHostSerialNumber());
        }


        public static RefundRef valueOf(PaymentItemUnionpay paymentItemUnionpay, PaymentDevice paymentdevice) {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String transDateStr = dfs.format(new Date(paymentItemUnionpay.getTransDate()));
            String[] transDates = transDateStr.split(" ");
            return new RefundRef(paymentItemUnionpay.getUuid(), paymentdevice.getDeviceNumber(),
                    paymentItemUnionpay.getAmount().intValue(), transDates[0], transDates[1],
                    paymentItemUnionpay.getPosTraceNumber(), paymentItemUnionpay.getHostSerialNumber());
        }


        static BigDecimal getAmount(RefundRef ref) {
            if (ref.amount == null) {
                return BigDecimal.ZERO;
            } else {
                BigDecimal amount = BigDecimal.valueOf(ref.amount);
                amount = amount.divide(BigDecimal.valueOf(100));
                return amount;
            }
        }

    }


    public static interface OnTransListener {


        void onActive();


        void onStart();


        void onConfirm(PosTransLog log);


        void onFailure(NewLDResponse ldResponse);

    }


    public static BigDecimal getRealMoney(Integer intener) {
        BigDecimal bigDecimal = new BigDecimal(intener);
        BigDecimal div = bigDecimal.divide(new BigDecimal(100));
        return div;
    }

    private static final String MESSAGE_OCCUPIED = BaseApplication.sInstance.getString(R.string.mispos_pos_occupying);


    private static NewLDResponse generateErroLDResponse(String message) {
        ResponseData responseData = new ResponseData();
        responseData.PutValue(LDResponseConstant.REJ_CODE, "X001");
        responseData.PutValue(LDResponseConstant.REJ_CODE_EXPLAIN, message);
        NewLDResponse ldResponse = new NewLDResponse(responseData);
        return ldResponse;

    }

    public static int compare_date(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }


    private void isSuccessInit(Context context) {
        try {
            if (!MisPos.isInitSuccess()) {
                Log.d("LiandiPos", "LiandiPos--init_failed");
                                MisPos.deleteFile(context);
                MisPos.reInit(context);
                if (MisPos.isInitSuccess()) {
                    Log.d("LiandiPos", "LiandiPos--init_success");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void cancelConnect() {
        TaskContext.execute(new Runnable() {
            @Override
            public void run() {
                new MisPos().TestConnect();
            }
        });
    }
}
