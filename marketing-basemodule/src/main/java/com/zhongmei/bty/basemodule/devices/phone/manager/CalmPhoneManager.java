package com.zhongmei.bty.basemodule.devices.phone.manager;

import android.app.Application;
import android.util.Log;

import com.zhongmei.bty.basemodule.devices.phone.CalmPhoneProxy;
import com.zhongmei.bty.basemodule.devices.phone.bean.BluetoothPhoneConnectStatus;
import com.zhongmei.bty.basemodule.devices.phone.bean.FixedPhoneConnectStatus;
import com.zhongmei.bty.basemodule.devices.phone.bean.ICalmPhone;
import com.zhongmei.yunfu.context.base.BaseApplication;

/**
 * 电话模块的统一管理器, 所有操作的初始入口
 *
 * @date 2014-8-5
 */
public class CalmPhoneManager {

    private static CalmPhoneManager mInstance;

    private CalmPhoneProxy mCalmPhoneProxy;

    private Application mContext;

    private PhoneType mPhoneType;

    private PhoneBusinessType callBussinessType;

    /**
     * 手柄状态
     */
    private boolean isPickUp = false;

    public void setPhoneType(PhoneType phoneType) {

        this.mPhoneType = phoneType;
    }

    public PhoneType getPhoneType() {
        return mPhoneType;
    }

    public enum PhoneState {
        /**
         * 空闲
         */
        IDEL,
        /**
         * 振铃
         */
        RINGING,
        /**
         * 通话中
         */
        CALLING,
        /**
         * 摘机
         */
        PICKUP,
        /**
         * 拨号
         */
        DIALING
    }

    public enum PhoneType {
        /**
         * 未知或者没有
         */
        NONE,
        /**
         * 电话盒
         */
        PHONEB_BOX,
        /**
         * 蓝牙
         */
        BLUETOOTH,
        /**
         * 固定电话
         */
        FIXEDPHONE;

        public String toString() {
            switch (this) {
                case PHONEB_BOX:
                    return "phone_box";
                case BLUETOOTH:
                    return "bluetooth";
                case FIXEDPHONE:
                    return "fixedphone";
                default:
                    return "none";
            }
        }

        ;

    }

    private CalmPhoneManager(Application context) {
        mContext = context;
    }

    public static synchronized CalmPhoneManager getDefault() {
        if (mInstance == null) {
            mInstance = new CalmPhoneManager(BaseApplication.sInstance);
        }
        return mInstance;
    }

    /**
     * 初始化, 在此传入Application, 防止使用的时候每次传入Context
     *
     * @param context
     * @deprecated
     */
    public void init(Application context) {
        mContext = context;
    }

    public boolean isConnectSuccess() {
        Log.e("ClamPhoneManager", "isConnectSuccess()::FixedPhoneState = " + FixedPhoneConnectStatus.isConnect + "---BluetoothState = " + BluetoothPhoneConnectStatus.isConnect);
        return (BluetoothPhoneConnectStatus.isConnect || FixedPhoneConnectStatus.isConnect);
    }


    /**
     * 获取一个ICalmPhone实例, 调用此方法前请确保已经调用{@link #init}
     *
     * @return
     */
    public synchronized ICalmPhone getCalmPhone() {
        if (mContext == null) {
            throw new IllegalStateException(
                    "you must invoke the init() method fisrt!");
        }
        if (mCalmPhoneProxy == null) {
            mCalmPhoneProxy = new CalmPhoneProxy(mContext);
        }
        return mCalmPhoneProxy;
    }

    public void setPickUp(boolean b) {
        this.isPickUp = b;
    }

    public boolean isPickUp() {
        return isPickUp;
    }

    public PhoneBusinessType getCallBussinessType() {
        return callBussinessType;
    }

    public void setCallBussinessType(PhoneBusinessType callBussinessType) {
        this.callBussinessType = callBussinessType;
    }

    public enum PhoneBusinessType {
        /**
         * 来电
         */
        INCALL,
        /**
         * 拨打
         */
        OUTCALL
    }

}
