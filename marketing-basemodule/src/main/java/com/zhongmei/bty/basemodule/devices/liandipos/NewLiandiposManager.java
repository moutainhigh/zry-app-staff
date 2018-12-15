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

/**
 * @version: 1.0
 * @date 2016年1月8日
 */
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

    /**
     * 根据UUID查询刷卡交易记录
     *
     * @param transUuid 交易记录的UUID（与PaymentItem.uuid相同）
     * @return
     */
    public PosTransLog queryTrans(String transUuid) {
        try {
            return DBHelperManager.queryById(PosTransLog.class, transUuid);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 判断指定的刷卡支付是否能撤消（即这笔刷卡支付有没有被结算）
     * <p>
     * //	 * @param transUuid 交易记录的UUID（与PaymentItem.uuid相同）
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public boolean canRepeal(RefundRef ref) {
        boolean issame = false;
        boolean iscan = false;
        if (!isSamePosDeviceId(ref.terminalNumber)) {
            issame = false;
        } else {
            issame = true;
        }

        // 不能撤消当前日期之前的交易
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String today = df.format(new Date());
        if (compare_date(today, ref.transDate) == 0) {// 当天交易才可以刷银行卡
            iscan = true;
        } else {
            iscan = false;
        }
        Log.d(TAG, TAG + ":iscan=" + iscan + "____issame=" + issame);
        return issame && iscan;
    }

    /**
     * 是否是相同的POS设备ID号
     *
     * @Title: isSamePosDeviceId
     * @Description:
     * @Param @param terminalNumber
     * @Param @return
     * @Return boolean 返回类型
     */
    private static boolean isSamePosDeviceId(String terminalNumber) {
        String localnum = SharedPreferenceUtil.getSpUtil().getString(Constant.SP_POS_DEVICIE_ID, "");
        Log.d(TAG, TAG + "------SP_POS_DEVICIE_ID=" + localnum + "terminalNumber=" + terminalNumber);
        if (TextUtils.isEmpty(localnum)) {
            return true;
        } else {
            return localnum.equals(terminalNumber);
        }
    }

    /**
     * 开始刷卡消费操作
     *
     * @param amount   要支付的金额，单位为元
     * @param listener
     */
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
            // throw new
            // IllegalStateException("The MisPos working.");
        }
        new Thread() {
            @Override
            public void run() {
                doPay(amount, createProxy(listener));
            }
        }.start();
    }

    /**
     * 完成刷卡消费操作
     *
     * @param transUuid 交易记录的UUID（与PaymentItem.uuid相同）
     */
    public void completePay(String transUuid) {
        deleteTrans(transUuid);
    }

    /**
     * 取消刷卡消费操作，在向服务端提交结账请求失败时调用此方法取消刷卡交易
     *
     * @param transLog
     * @param listener
     */
    public void cancelPay(final PosTransLog transLog, final OnTransListener listener) {
        Checks.verifyNotNull(transLog, "transLog");
        Checks.verifyNotNull(listener, "listener");
        if (misPosWorking) {
            onOccupied(listener);
            return;
            // throw new
            // IllegalStateException("The MisPos working.");
        }
        new Thread() {
            @Override
            public void run() {
                RefundRef ref = RefundRef.valueOf(transLog);
                BigDecimal amount = RefundRef.getAmount(ref);
                // 取消支付操作不需要保存交易记录
                doRepealOrRefund(amount, ref, false, createCancelPayProxy(transLog.getUuid(), listener));
            }
        }.start();
    }

    /**
     * 开始刷卡退款操作 退款需要Payment的UUID传入进来
     * <p>
     * //	 * @param amount 要退款的金额，单位为元
     * //	 * @param ref
     *
     * @param listener
     */
    public void startRefund(final String uuid, final String posTraceNo, final OnTransListener listener) {
        Checks.verifyNotNull(posTraceNo, "posTraceNo");
        Checks.verifyNotNull(listener, "listener");
        if (misPosWorking) {
            onOccupied(listener);
            return;
            // throw new
            // IllegalStateException("The MisPos working.");
        }
        new Thread() {
            @Override
            public void run() {
                RefundRef ref = new RefundRef(uuid, null, null, null, null, posTraceNo, null);// 只需知道posTraceNo单号就可以发起撤消退货
                doRepealOrRefund(null, ref, true, createProxy(listener));
            }
        }.start();
    }

    /**
     * 开始刷卡消费操作
     * <p>
     * //	 * @param amount 要支付的金额，单位为元
     *
     * @param listener isdelay是否延迟几秒发指令
     */
    public void startReadCardID(final Context context, final OnTransListener listener, final boolean isdelay) {
        Checks.verifyNotNull(listener, "listener");

        if (misPosWorking) {
            onOccupied(listener);
            return;
            // throw new
            // IllegalStateException("The MisPos working.");
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
                    // TODO Auto-generated catch block
                    Log.d(TAG, TAG + e.toString());
                    e.printStackTrace();
                }

            }
        }.start();
    }

    /**
     * 开始读取键盘输入的密码（读取键盘值）
     *
     * @Title: startReadKeyboardNum
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
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

    /**
     * 完成刷卡退款操作
     *
     * @param transUuid
     */
    public void completeRefund(String transUuid) {
        deleteTrans(transUuid);
    }

    /**
     * 创建一个注册监听了状态变化的MisPos实例
     *
     * @param listener
     * @return
     */
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

    /**
     * 执行刷卡交易操作
     *
     * @param amount
     * @param listener
     */
    private void doPay(BigDecimal amount, ListenerProxy listener) {
        try {
            RequestData request = createPayRequest(amount);
            NewLDResponse ldResponse = trans(createMisPos(listener), request);
            if (ldResponse.isSuccess()) {
                // 保存刷卡交易记录
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

    /**
     * 执行撤消或退款操作
     *
     * @param amount
     * @param ref
     * @param logEnable
     * @param listener
     */
    private void doRepealOrRefund(BigDecimal amount, RefundRef ref, boolean logEnable, ListenerProxy listener) {
        try {
            // 先执行撤消操作
            RequestData request = createRepealRequest(ref.posTraceNo, ref.transUuid);
            request.PutValue(LDRequestConstant.AMOUNT, fillString("0"));

            NewLDResponse ldResponse = trans(createMisPos(listener), request);
            if (ldResponse.isSuccess()) {
                PosTransLog log = toTransLog(ldResponse);
                // 取消支付时不记录刷卡记录
                if (logEnable) {
                    storeLog(log);
                }
                listener.onConfirm(log);
            } else {
                // 如果撤消失败，再执行退款操作
                // TODO 暂不提供退款功能
                listener.onFailure(ldResponse);
            }
        } catch (Exception e) {
            Log.e(TAG, "doRepealOrRefund error!", e);
            listener.onFailure(null);
        }
    }

    /**
     * 执行读取会员卡的操作
     *
     * @param listener
     */
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

    /**
     * 执行读取键盘值的操作（读取密码）
     *
     * @Title: doReadKeyboardNum
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
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

    /**
     * 签到
     *
     * @param listener
     */
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

    /**
     * 发起交易请求并在需要结算时执行结算
     *
     * @param misPos
     * @param request
     * @return
     */
    private NewLDResponse trans(MisPos misPos, RequestData request) {
        misPosWorking = true;
        try {
            NewLDResponse ldResponse = submit(misPos, request);
            if (ldResponse.isStorageFull()) {
                // 需要结算时执行结算，POS机上显示结算时不接受外部指令，待一会儿再发送后续指令
                await(SHORT_DELAY);
                ldResponse = submit(misPos, createSettlementRequestData());
                if (ldResponse.isSuccess()) {
                    // 保存结算记录
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

    /**
     * POS刷卡机上显示“存储已满”会很快消失
     */
    private static final int SHORT_DELAY = 500;

    /**
     * POS刷卡机结算需要较长时间
     */
    private static final int LONG_DELAY = 4000;

    /**
     * 签到
     */
    private static final String TRANS_TYPE_REGISER = "01";

    /**
     * 支付
     */
    private static final String TRANS_TYPE_PAY = "02";

    /**
     * 撤消
     */
    private static final String TRANS_TYPE_REPEAL = "03";

    /**
     * 退货
     */
    private static final String TRANS_TYPE_RETURN = "04";
    /**
     * 结算
     */
    private static final String TRANS_TYPE_SETTLEMENT = "14";

    /**
     * 读取卡号
     */
    private static final String TRANS_TYPE_READCARDID = "101";

    /**
     * 读取键盘输入值
     */
    private static final String TRANS_TYPE_READKEYBORD = "102";

    /**
     * 向银联POS机发送请求
     *
     * @param misPos
     * @param request
     * @return
     */
    private static NewLDResponse submit(MisPos misPos, RequestData request) {
        //Log.i(TAG, "submit... " + request.getRequest());
        ResponseData response = new ResponseData();
        long code = 0;
        try {
            code = misPos.TransProcess(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "code: " + code);
        //Log.i(TAG, "response: " + response.getResponse());
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
        /*DatabaseHelper helper = DBHelper.getHelper();
		try {
			Dao<PosSettlementLog, String> dao = helper.getDao(PosSettlementLog.class);
			dao.create(log);
			dao.deleteBuilder().where().lt(PosSettlementLog.$.transDate, log.getTransDate());
		} catch (Exception e) {
			Log.e(TAG, "Store log error!", e);
		} finally {
			DBHelper.releaseHelper(helper);
		}
		return log;*/
        PosLogsDal posLogsDal = new PosLogsDal();
        posLogsDal.storePosSettlementLog(log);
        return log;
    }

    private static PosTransLog storeLog(PosTransLog log) {
        //modify 20170301 统一调用dal处理数据库业务
		/*DatabaseHelper helper = DBHelper.getHelper();
		try {
			DBHelper.saveEntities(helper, PosTransLog.class, log);
		} catch (Exception e) {
			Log.e(TAG, "Store log error!", e);
		} finally {
			DBHelper.releaseHelper(helper);
		}
		return log;*/
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
                // 删除本地pos收银记录
                deleteTrans(transUuid);
            }
        }.start();
    }

    /**
     * 等待指定时间
     *
     * @param time 等待的时间，单位ms
     */
    private static void await(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Log.i(TAG, "", e);
        }
    }

    /**
     * 创建刷卡支付的请求体
     *
     * @param amount 刷卡金额，单位为元
     * @return
     */
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

    /**
     * 创建刷卡获取卡号的方法
     *
     * @return
     */
    private static RequestData createCardIdRequest() {
        RequestData reqData = createRequestData();
        reqData.PutValue(LDRequestConstant.TRANS_TYPE, TRANS_TYPE_READCARDID);
        reqData.PutValue(LDRequestConstant.TRANS_CHECK, uuid());
        return reqData;
    }


    /**
     * 创建读取键盘输入值得方法
     *
     * @return
     */
    private static RequestData createKeyboardRequest() {
        RequestData reqData = createRequestData();
        reqData.PutValue(LDRequestConstant.TRANS_TYPE, TRANS_TYPE_READKEYBORD);
        reqData.PutValue(LDRequestConstant.TRANS_CHECK, uuid());
        return reqData;
    }

    /**
     * 创建撤消支付的请求体
     *
     * @param oldPosTraceNo 要被撤消的交易号(原交易号)
     * @return
     */
    private static RequestData createRepealRequest(String oldPosTraceNo, String uuid) {
        RequestData reqData = createRequestData();
        reqData.PutValue(LDRequestConstant.TRANS_TYPE, TRANS_TYPE_REPEAL);
        reqData.PutValue(LDRequestConstant.TRANS_CHECK, uuid);
        reqData.PutValue(LDRequestConstant.OLD_TRACE_NUMBER, oldPosTraceNo);
        return reqData;
    }

    /**
     * 创建结算的请求体
     *
     * @return
     */
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

    /**
     * @version: 1.0
     * @date 2016年1月13日
     */
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
            //TODO 联迪的返回是1标示开始交易
            if (!mNotifiedIsAcive && mMessage.type == 0x01) {
                mNotifiedIsAcive = true;
                mListener.onActive();
            } else if (!mNotified && mMessage.type == 0x00) {
                mNotified = true;
                mListener.onStart();
            }
        }

    }

    /**
     * OnTransListener代理，保证OnTransListener中的方法在UI线程中执行
     *
     * @version: 1.0
     * @date 2016年1月15日
     */
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

    /**
     * @version: 1.0
     * @date 2016年1月15日
     */
    private static class CancelPayListenerProxy extends ListenerProxy {

        private final String transUuid;

        CancelPayListenerProxy(String transUuid, Handler poster, OnTransListener listener) {
            super(poster, listener);
            this.transUuid = transUuid;
        }

        @Override
        public void onConfirm(final PosTransLog log) {
            // 取消支付后删除本地刷卡交易记录
            deleteTrans(transUuid);
            super.onConfirm(log);
        }

    }

    /**
     * @version: 1.0
     * @date 2016年1月11日
     */
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

        /**
         * 根据PosTransLog生成退款参考数据
         *
         * @param log
         * @return
         */
        public static RefundRef valueOf(PosTransLog log) {
            return new RefundRef(log.getUuid(), log.getTerminalNumber(), log.getAmount(), log.getTransDate(),
                    log.getTransTime(), log.getPosTraceNumber(), log.getHostSerialNumber());
        }

        /**
         * 根据PaymentItemUnionpay生成退款参考数据
         *
         * @Title: valueOf
         * @Param @param paymentItemUnionpay
         * @Return RefundRef 返回类型
         */
        public static RefundRef valueOf(PaymentItemUnionpay paymentItemUnionpay, PaymentDevice paymentdevice) {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String transDateStr = dfs.format(new Date(paymentItemUnionpay.getTransDate()));
            String[] transDates = transDateStr.split(" ");
            return new RefundRef(paymentItemUnionpay.getUuid(), paymentdevice.getDeviceNumber(),
                    paymentItemUnionpay.getAmount().intValue(), transDates[0], transDates[1],
                    paymentItemUnionpay.getPosTraceNumber(), paymentItemUnionpay.getHostSerialNumber());
        }

        /**
         * 获取参考金额，单位为元。 由于RefundRef中的amount是以分为单位，此方法将分转换为元
         *
         * @param ref
         * @return
         */
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

    /**
     * POS刷卡处理过程的监听。所有回调方法都将在UI线程中执行。
     *
     * @version: 1.0
     * @date 2016年1月8日
     */
    public static interface OnTransListener {

        /**
         * pos当前是否激活
         */
        void onActive();

        /**
         * 完成刷卡和输入密码后正在与银联服务器交易时回调
         */
        void onStart();

        /**
         * 刷卡并扣款成功后回调
         *
         * @param log 交易产生的刷卡记录，在此回调中创建PaymentItem时使用log的uuid
         *            。 取消支付(调用
         *            {@link NewLiandiposManager#cancelPay(PosTransLog, OnTransListener)}
         *            )时为null
         */
        void onConfirm(PosTransLog log);

        /**
         * 刷卡交易失败时回调
         * <p>
         * //		 * @param msg
         */
        void onFailure(NewLDResponse ldResponse);

    }

    /**
     * 得到真正的金额
     *
     * @param intener
     * @Title: getRealMoney
     * @Description:
     * @Return BigDecimal 返回类型
     */
    public static BigDecimal getRealMoney(Integer intener) {
        BigDecimal bigDecimal = new BigDecimal(intener);
        BigDecimal div = bigDecimal.divide(new BigDecimal(100));
        return div;
    }

    private static final String MESSAGE_OCCUPIED = BaseApplication.sInstance.getString(R.string.mispos_pos_occupying);

    /**
     * 生成本地返回的错误信息
     *
     * @Title: GenerateErroLDResponse
     * @Description:
     * @Param message
     * @Return NewLDResponse 返回类型
     */
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
//				System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
//				System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 判断初始化化是否成功，如果不成功则重新初始化联迪SDK
     *
     * @param context
     */
    private void isSuccessInit(Context context) {
        try {
            if (!MisPos.isInitSuccess()) {
                Log.d("LiandiPos", "LiandiPos--init_failed");
                //如果初始化失败则重新初始化
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

    /**
     * 取消链接
     */
    public void cancelConnect() {
        TaskContext.execute(new Runnable() {
            @Override
            public void run() {
                new MisPos().TestConnect();
            }
        });
    }
}
