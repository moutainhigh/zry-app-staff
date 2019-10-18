package com.zhongmei.bty.basemodule.devices.scaner;

import android.hardware.usb.UsbDevice;
import android.text.TextUtils;

import com.dewo.tzc.dewoserialport.bean.ComBean;
import com.dewo.tzc.dewoserialport.util.SerialPortUtil;
import com.dewo.tzc.dewoserialport.util.USBBroadcastReceiver;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.util.DeviceNodeUtil;

import static com.zhongmei.yunfu.context.util.ThreadUtils.runOnUiThread;


public class DeWoScanCode {

    private USBBroadcastReceiver mUSBBroadcastReceiver;

    private static DeWoScanCode mInstance = new DeWoScanCode();

    public static DeWoScanCode getInstance() {
        return mInstance;
    }

    private DeWoScanCode() {
    }


    public void start() {
        final SerialPortUtil serialPortUtil = SerialPortUtil.getInstance();
        try {
            String devicePath = DeviceNodeUtil.getCharNodeByUSBid(516, 2954);
            if (!TextUtils.isEmpty(devicePath)) {
                serialPortUtil.setPath(devicePath);
                serialPortUtil.setSerilaPortResult(new SerialPortUtil.iSerilaPortResult() {
                    @Override
                    public void SerilaPortResult(final ComBean comBean) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (onReceiveDataListener != null) {
                                    onReceiveDataListener.onReceiveData(new String(comBean.bRec));
                                }
                            }
                        });
                    }
                });
                serialPortUtil.startSerialPort(SerialPortUtil.READ_SLEEP_MODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        final SerialPortUtil serialPortUtil = SerialPortUtil.getInstance();
        try {
            serialPortUtil.closeSerialPort();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerReceiver() {
        if (mUSBBroadcastReceiver == null) {
            mUSBBroadcastReceiver = new USBBroadcastReceiver(BaseApplication.getInstance());
            mUSBBroadcastReceiver.setiReceiver(new USBBroadcastReceiver.Receiver() {
                @Override
                public void UpViewData(UsbDevice device, int flag) {
                    if (flag == 0) {
                        start();
                    } else if (flag == 1) {
                        stop();
                    }
                }
            });
        }
    }

    public void unregisterReceiver() {
        if (mUSBBroadcastReceiver != null) {
            try {
                mUSBBroadcastReceiver.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mUSBBroadcastReceiver = null;
        }
    }

    private OnReceiveDataListener onReceiveDataListener;

    public void registerReceiveDataListener(OnReceiveDataListener receiveDataListener) {
        onReceiveDataListener = receiveDataListener;
    }

    public void unRegisterReceiveDataListener() {
        onReceiveDataListener = null;
    }

    public interface OnReceiveDataListener {
        void onReceiveData(final String data);
    }

}
