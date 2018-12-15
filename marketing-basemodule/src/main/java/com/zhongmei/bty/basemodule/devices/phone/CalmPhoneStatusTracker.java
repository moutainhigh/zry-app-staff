package com.zhongmei.bty.basemodule.devices.phone;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.zhongmei.bty.basemodule.devices.phone.bean.BluetoothPhoneConnectStatus;
import com.zhongmei.bty.basemodule.devices.phone.bean.BluetoothPhoneInfo;
import com.zhongmei.bty.basemodule.devices.phone.bean.FixedPhoneConnectStatus;
import com.zhongmei.bty.basemodule.devices.phone.entity.CallHistory;
import com.zhongmei.bty.basemodule.devices.phone.entity.CallHistorySession;
import com.zhongmei.bty.basemodule.devices.phone.event.ActionCallFinish;
import com.zhongmei.bty.basemodule.devices.phone.event.ActionHangup;
import com.zhongmei.bty.basemodule.devices.phone.event.ActionObserveDataChange;
import com.zhongmei.bty.basemodule.devices.phone.event.ActionShowCallNumber;
import com.zhongmei.bty.basemodule.devices.phone.event.ActionShowCallingDialog;
import com.zhongmei.bty.basemodule.devices.phone.event.ActionStartTimer;
import com.zhongmei.bty.basemodule.devices.phone.event.ActionUpateUIManager;
import com.zhongmei.bty.basemodule.devices.phone.event.EventClearCalllog;
import com.zhongmei.bty.basemodule.devices.phone.event.EventConnectResult;
import com.zhongmei.bty.basemodule.devices.phone.event.EventDial;
import com.zhongmei.bty.basemodule.devices.phone.event.EventHangUp;
import com.zhongmei.bty.basemodule.devices.phone.event.EventNoAnswer;
import com.zhongmei.bty.basemodule.devices.phone.event.EventPickup;
import com.zhongmei.bty.basemodule.devices.phone.event.EventReceivePhoneNum;
import com.zhongmei.bty.basemodule.devices.phone.event.EventRing;
import com.zhongmei.bty.basemodule.devices.phone.event.EventUpdateMissCallCount;
import com.zhongmei.bty.basemodule.devices.phone.manager.CalmPhoneManager;
import com.zhongmei.bty.basemodule.devices.phone.manager.CalmPhoneManager.PhoneType;
import com.zhongmei.bty.basemodule.devices.phone.utils.Lg;
import com.zhongmei.bty.basemodule.devices.phone.utils.NotificationUtil;
import com.zhongmei.yunfu.context.util.helper.SpHelper;

import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * 负责接收不同电话模块上报的状态信息, 保存状态, 并统一处理通话记录.<br/>
 * 暂时不考虑同时使用2种电话的情况<br/>
 * <p>
 * 电话盒: 呼出流程时无法确定对方是否接听.
 *
 * @date 2014-10-17
 */
public class CalmPhoneStatusTracker {
    private static final String TAG = CalmPhoneStatusTracker.class.getSimpleName();

    /**
     * 未连接
     */
    public static final int STATUS_UNCONNECT = -1;
    /**
     * 空闲
     */
    public static final int STATUS_IDEL = 0;

    /**
     * 摘机
     */
    public static final int STATUS_PICKUPED = 11;
    /**
     * 呼出通话中
     */
    public static final int STATUS_OUTCALLING = 12;

    /**
     * 振铃
     */
    public static final int STATUS_RINGING = 21;
    /**
     * 呼入通话中
     */
    public static final int STATUS_INCALLING = 22;

    private static CalmPhoneStatusTracker mInstance;

    private int mStatus = STATUS_UNCONNECT;

    public static boolean FIRST_DISCONNECT = true;

    private Context mContext;

    private CalmPhoneStatusTracker() {
    }

    public static synchronized CalmPhoneStatusTracker getDefault() {
        if (mInstance == null) {
            mInstance = new CalmPhoneStatusTracker();
        }
        return mInstance;
    }

    public void init(Context context) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            mStatus = STATUS_UNCONNECT;
            mContext = context;
        }

    }

    public int getStatus() {
        return mStatus;
    }

    /**
     * 绑定结果
     *
     * @param event
     */
    public void onEvent(EventConnectResult event) {

        Lg.d(TAG, "CalmPhoneStatusTracker::onEvent() <===> EventConnectResult");
        // 连接成功时保存连接类型供以后自动连接使用
        if (event.isSuccess()) {
//			SpHelper.getDefault().saveLastConnectType(
//					event.getType().toString());
            if (event.getType().toString().equals(PhoneType.FIXEDPHONE.toString())) {
                FixedPhoneConnectStatus.isConnect = true;
                if (mStatus == STATUS_UNCONNECT) {
                    mStatus = STATUS_IDEL;
                }

            } else {
                mStatus = STATUS_IDEL;
                SpHelper.getDefault().saveBluetoothConnectMac(
                        event.getPhone().getMac());
                SpHelper.getDefault().saveBluetoothConnectName(
                        ((BluetoothPhoneInfo) event.getPhone()).getName());
            }

        } else {
            if (event.getType().toString().equals(PhoneType.FIXEDPHONE.toString())) {
                FixedPhoneConnectStatus.isConnect = false;
            } else if (event.getType().toString().equals(PhoneType.BLUETOOTH.toString())) {
                BluetoothPhoneConnectStatus.isConnect = false;
            }
            Intent intent = new Intent();
            intent.setAction(PhoneConstants.ACTION_FORBIN_PHONE);
            intent.putExtra(PhoneConstants.isForbin, false);
            mContext.sendBroadcast(intent);

            EventBus.getDefault().post(new ActionCallFinish());
            mStatus = STATUS_UNCONNECT;
        }
        EventBus.getDefault().post(
                new ActionUpateUIManager(ActionUpateUIManager.PhonesceneActivity));
        NotificationUtil.showPhoneStatusNotification(mContext,
                event.isSuccess());
    }

    /**
     * 摘机
     *
     * @param event
     */
    public void onEvent(EventPickup event) {

        Log.d(TAG, "CalmPhoneStatusTracker::onEvent() <===> EventPickup :: mStatus = " + mStatus);
        switch (mStatus) {
            case STATUS_IDEL:
                mStatus = STATUS_PICKUPED;
                EventBus.getDefault().post(new ActionStartTimer());
                break;
            case STATUS_RINGING:
                mStatus = STATUS_INCALLING;
                CallHistorySession.getSession()
                        .setStatus(CallHistory.STATUS_NORMAL);
                CallHistorySession.getSession().setStartTime(
                        new Date());
                EventBus.getDefault().post(new ActionStartTimer());
                break;
            default:
        }
    }

    /**
     * 拨号
     *
     * @param event
     */
    public void onEvent(EventDial event) {

        Log.d(TAG, "CalmPhoneStatusTracker::onEvent() <===> EventDial");
        // 蓝牙时不管状态, 电话盒时要求摘机状态才能拨
        if (event.getPhoneType() == PhoneType.BLUETOOTH || event.getPhoneType() == PhoneType.FIXEDPHONE
                || mStatus == STATUS_PICKUPED) {
            // 进入呼出流程
            mStatus = STATUS_OUTCALLING;
            CallHistorySession.openOutCallSession()
                    .setNumber(event.getPhoneNumber())
                    .setStatus(CallHistory.STATUS_NORMAL);

            Intent intent2 = new Intent();
            intent2.setAction(PhoneConstants.ACTION_FORBIN_PHONE);
            intent2.putExtra(PhoneConstants.isForbin, true);
            mContext.sendBroadcast(intent2);
            EventBus.getDefault().post(new ActionShowCallingDialog());
            CalmPhoneManager.getDefault().setCallBussinessType(CalmPhoneManager.PhoneBusinessType.OUTCALL);
        }
    }

    public void onEvent(ActionHangup action) {
        // TODO Auto-generated method stub
        mStatus = STATUS_IDEL;


    }

    /**
     * 挂机
     *
     * @param event
     */
    public void onEvent(EventHangUp event) {


        Log.d(TAG, "CalmPhoneStatusTracker::onEvent() <===> EventHangUp");
        mStatus = STATUS_IDEL;
        CallHistorySession.closeSession();
        //EventBus.getDefault().post(new ActionUpateUIManager(ActionUpateUIManager.CalllogFragment));
        Intent intent = new Intent();
        intent.setAction(PhoneConstants.ACTION_LOAD_ALL_CALLLOG);
        mContext.sendBroadcast(intent);
        Intent intent2 = new Intent();
        intent2.setAction(PhoneConstants.ACTION_FORBIN_PHONE);
        intent2.putExtra(PhoneConstants.isForbin, false);
        mContext.sendBroadcast(intent2);
        EventBus.getDefault().post(new ActionCallFinish());
    }

    /**
     * 振铃
     *
     * @param event
     */
    public void onEvent(EventRing event) {

        Log.d(TAG, "CalmPhoneStatusTracker::onEvent() <===> EventRing");
        Lg.d("onEventRing--status--" + mStatus);
        if (mStatus != STATUS_RINGING) {
            mStatus = STATUS_RINGING;
            CallHistorySession.openInCallSession();
            // 发送弹屏动作
            EventBus.getDefault().post(new ActionShowCallingDialog());
            CalmPhoneManager.getDefault().setCallBussinessType(CalmPhoneManager.PhoneBusinessType.INCALL);
        }
    }

    /**
     * 收到来电号码
     *
     * @param event
     */
    public void onEvent(EventReceivePhoneNum event) {

        Log.d(TAG, "CalmPhoneStatusTracker::onEvent() <===> EventReceivePhoneNum");
        // 额外处理一下没有Session的情况, 因为电话盒会经常掉事件
        if (CallHistorySession.getSession() == null) {
            onEvent(new EventRing());
        }
        Intent intent = new Intent();
        intent.setAction(PhoneConstants.ACTION_FORBIN_PHONE);
        intent.putExtra(PhoneConstants.isForbin, true);
        mContext.sendBroadcast(intent);
        CallHistorySession.getSession().setNumber(event.getPhoneNum());
        // 发送弹屏动作
//		EventBus.getDefault().post(new ActionShowCallingDialog());//获取到电话号码后再弹出对话框;
        // 发送显示号码动作
        EventBus.getDefault().post(new ActionShowCallNumber(event.getPhoneNum()));

    }

    /**
     * 未接
     *
     * @param evnet
     */
    public void onEvent(EventNoAnswer evnet) {

        Log.d(TAG, "CalmPhoneStatusTracker::onEvent() <===> EventNoAnswer ");
        mStatus = STATUS_IDEL;
        CallHistorySession.getSession().setStatus(CallHistory.STATUS_NO_ANSWER);
        CallHistorySession.closeSession();
        EventBus.getDefault().post(new ActionCallFinish());
        EventBus.getDefault().post(new EventUpdateMissCallCount());
        Intent intent = new Intent();
        intent.setAction(PhoneConstants.ACTION_FORBIN_PHONE);
        intent.putExtra(PhoneConstants.isForbin, false);
        mContext.sendBroadcast(intent);
    }

    /**
     * 删除calllog记录
     *
     * @param event
     */
    public void onEventAsync(EventClearCalllog event) {
        //CallHistorySession.getSession().

        Log.d(TAG, "CalmPhoneStatusTracker::onEventAsync() <===> EventClearCalllog");
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                CallDBHelper.getService(CallService.class).deleteTimeOutLog();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                EventBus.getDefault().post(new ActionObserveDataChange());
            }
        }.execute();
    }

}
