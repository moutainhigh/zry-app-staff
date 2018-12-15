package com.zhongmei.bty.basemodule.devices.liandipos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.zhongmei.yunfu.util.Checks;
import com.zhongmei.bty.basemodule.devices.liandipos.utils.LDUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @version: 1.0
 * @date 2016年1月15日
 */
public final class PosConnectManager {

    /**
     * 判断是否已经连接上POS刷卡机
     *
     * @return
     */
    public static boolean isPosConnected() {
        return LDUtil.getLDDevicePort() != null;
    }

    private final Context mContext;
    private UsbDeviceReceiver mReceiver;

    /**
     * 注意：在不用时一定要调用{@link #close()}方法释放资源
     *
     * @param context
     * @param listener
     */
    public PosConnectManager(Context context, PosConnectListener listener) {
        Checks.verifyNotNull(context, "context");
        Checks.verifyNotNull(listener, "listener");
        mContext = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_CHECKING);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        mReceiver = new UsbDeviceReceiver(listener);
        mContext.registerReceiver(mReceiver, filter);
    }

    public boolean isClosed() {
        return mReceiver == null;
    }

    public void close() {
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    /**
     * POS刷卡机连接状态监听器
     *
     * @version: 1.0
     * @date 2016年1月15日
     */
    public static interface PosConnectListener {

        void onConnected();

        void onDisconnected();

    }

    /**
     * 监听pos机设备的拔插事件，以保证联迪的代码能正确识别pos机端口
     */
    private static class UsbDeviceReceiver extends BroadcastReceiver {

        private final PosConnectListener mListener;

        UsbDeviceReceiver(PosConnectListener listener) {
            this.mListener = listener;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(intent.getAction())) {
                UsbDevice device = intent.getExtras().getParcelable("device");
                if (LDUtil.isLDDevice(device)) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("comport", LDUtil.getLDDevicePort());
                    //Manager_PropertiesUtil.storeProperties(map);
                    mListener.onConnected();
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(intent.getAction())) {
                UsbDevice device = intent.getExtras().getParcelable("device");
                if (LDUtil.isLDDevice(device)) {
                    mListener.onDisconnected();
                    // SerialPortHelper.getInstance().disConnect();
                }
            }
        }
    }

}
