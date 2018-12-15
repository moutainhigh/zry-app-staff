package com.zhongmei.bty.basemodule.devices.phone;

import android.content.Context;

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.devices.phone.bean.BluetoothPhoneConnectStatus;
import com.zhongmei.bty.basemodule.devices.phone.bean.BluetoothPhoneInfo;
import com.zhongmei.bty.basemodule.devices.phone.bean.CalmPhoneFixedPhone;
import com.zhongmei.bty.basemodule.devices.phone.bean.CalmPhoneInfo;
import com.zhongmei.bty.basemodule.devices.phone.bean.CalmPhoneNmBlueTooth;
import com.zhongmei.bty.basemodule.devices.phone.bean.FixedPhoneConnectStatus;
import com.zhongmei.bty.basemodule.devices.phone.bean.FixedPhoneInfo;
import com.zhongmei.bty.basemodule.devices.phone.bean.ICalmPhone;
import com.zhongmei.bty.basemodule.devices.phone.exception.CalmPhoneException;
import com.zhongmei.bty.basemodule.devices.phone.exception.CalmPhoneStateException;
import com.zhongmei.bty.basemodule.devices.phone.manager.CalmPhoneManager;
import com.zhongmei.bty.basemodule.devices.phone.manager.CalmPhoneManager.PhoneType;
import com.zhongmei.yunfu.util.ToastUtil;

/**
 * 协调真正干活的ClamPhone的Proxy
 *
 * @date 2014-8-6
 */
public class CalmPhoneProxy implements ICalmPhone {

    /**
     * 用于保存上次连接的设备类型, 参考 {@link PhoneType}
     */
    public static final String SP_LAST_CONNECT_TYPE = "last_connect";

    private CalmPhoneNmBlueTooth mCalmPhoneBluetooth;
    private CalmPhoneFixedPhone mCalmPhoneFixedPhone;

    private ICalmPhone mCurrentPhone;

    private Context mContext;

    public CalmPhoneProxy(Context context) {
        mContext = context;
        mCalmPhoneBluetooth = new CalmPhoneNmBlueTooth();
        mCalmPhoneFixedPhone = new CalmPhoneFixedPhone();
        CalmPhoneStatusTracker.getDefault().init(context);
    }


    @Override
    public void autoConnect() {
        // 读取本地配置, 如果有上次成功连接的设备则在这里自动连接
        // String lastConnect =
        // SpHelper.getDefault().getLastConnectType();
        if (PhoneType.BLUETOOTH.toString().equals(CalmPhoneManager.getDefault().getPhoneType().toString())) {
            mCalmPhoneBluetooth.autoConnect();
            mCurrentPhone = mCalmPhoneBluetooth;
            FixedPhoneConnectStatus.isConnect = false;
        } else if (PhoneType.FIXEDPHONE.toString().equals(CalmPhoneManager.getDefault().getPhoneType().toString())) {
            mCurrentPhone = mCalmPhoneFixedPhone;
            mCalmPhoneFixedPhone.autoConnect();
            BluetoothPhoneConnectStatus.isConnect = false;
        }

    }

    @Override
    public void dial(String phoneNumber) throws CalmPhoneStateException,
            CalmPhoneException, IllegalArgumentException {
        if (mCurrentPhone == null) {
            ToastUtil.showShortToast(mContext.getString(R.string.string_phone_bind));
            return;
        }

        mCurrentPhone.dial(phoneNumber);
    }

    @Override
    public void connect(CalmPhoneInfo calmPhone) {
        if (calmPhone instanceof BluetoothPhoneInfo) {
            mCurrentPhone = mCalmPhoneBluetooth;
        } else if (calmPhone instanceof FixedPhoneInfo) {
            mCurrentPhone = mCalmPhoneFixedPhone;
        }
        mCurrentPhone.connect(calmPhone);
    }

    @Override
    public void search(PhoneType type) {
        switch (type) {
            case BLUETOOTH:
                mCalmPhoneBluetooth.search(type);
                break;
            case NONE:
                break;
        }
    }

    @Override
    public void disconnect() {
        mCurrentPhone.disconnect();
    }

    @Override
    public void hangup() {
        mCurrentPhone.hangup();
    }

}
