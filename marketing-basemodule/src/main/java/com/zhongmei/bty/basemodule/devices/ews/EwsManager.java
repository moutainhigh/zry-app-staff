package com.zhongmei.bty.basemodule.devices.ews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.context.util.DeviceNodeUtil;

import java.io.InputStream;


public class EwsManager {

    private static final String TAG = "EwsManager";

    private static final String DEFALUT_WEIGTH_DATA = "0000";

    private static final int BAUDRATE = 9600;
        private static final int PARITY = 0;
        private static final int STOP = 1;
    private static final int SUCCESS = 1;
    private static final int LOADING = 2;
    private static final int END_LOADING = 3;
    private static final int CONNECTION_STATE = 4;
    private InputStream mInputStream = null;


    private ReadThread mReadThread;

    private DataReceivedListener mCallback;

        private boolean mSend = false;

        private boolean isLoading = false;

        private String mPreData = DEFALUT_WEIGTH_DATA;

    private Context mContext;

    private UsbDeviceReceiver mAttachReceiver;

    public EwsManager(Context context, DataReceivedListener listener) {
        this.mContext = context;
        this.mCallback = listener;
        registerUsbReceiver();
    }


    public boolean isDeviceConnection() {
        return DeviceNodeUtil.isDeviceConnected();
    }


    public void start() {
        Log.d(TAG, "start:->......");
        clearLast();
        openDataPort();
        startNewTesk();
    }


    private void clearLast() {
        mPreData = DEFALUT_WEIGTH_DATA;
        stopCurrentThread();
        chearCurrentHandler();
    }


    private void stopCurrentThread() {
        if (mReadThread != null && mReadThread.isRunning()) {
            mReadThread.setRunning(false);
            mReadThread = null;
        }
    }


    private void startNewTesk() {
        mReadThread = new ReadThread();
        mReadThread.start();
    }


    private void chearCurrentHandler() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }


    private void stop() {

    }


    public void destory() {
        Log.d(TAG, "destory:->......");
        stop();
        if (mAttachReceiver != null) {
            mContext.getApplicationContext().unregisterReceiver(mAttachReceiver);
        }
    }


    private boolean openDataPort() {

        return false;
    }

    private class ReadThread extends Thread {
        private boolean running = true;

        @Override
        public void run() {
            super.run();

            while (running) {
                try {
                    if (mInputStream == null) {
                        return;
                    }
                    String inputStr = "";
                    if (mInputStream.available() > 0) {
                        byte[] buffer = new byte[128];
                        int size = mInputStream.read(buffer);
                        if (size > 0) {
                            inputStr = new String(buffer, 0, size);
                        }
                        if (!TextUtils.isEmpty(inputStr.trim())) {

                            sendWeightData(inputStr);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                Log.d(TAG, "", e);
                            }
                        }
                    } else {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            Log.e(TAG, "", e);
                        }
                    }

                } catch (Exception e) {
                    Log.e(TAG, "", e);
                    return;
                }
            }
        }

        public boolean isRunning() {
            return running;
        }

        public void setRunning(boolean _running) {
            this.running = _running;
        }
    }

    private void onReceived(String data) {
        Log.d(TAG, "onReceived:->data >" + data);
        Message ms = mHandler.obtainMessage();
        ms.what = SUCCESS;
        ms.obj = data;
        mHandler.sendMessage(ms);

    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    mCallback.onDataReceivedOver((String) msg.obj);
                    break;
                case LOADING:
                    mCallback.onStartLoading();
                    break;
                case END_LOADING:
                    mCallback.onEndLoading();
                    break;
                case CONNECTION_STATE:
                    mCallback.onDeviceConnect((Boolean) msg.obj);
                    break;
                default:
                    break;
            }

        }
    };

    public interface DataReceivedListener {

        void onDataReceivedOver(String data);

        void onDeviceConnect(boolean connect);

        void onStartLoading();

        void onEndLoading();
    }


    private void sendWeightData(String data) {
        String tempData = splitWeight(data).trim();

        if (isValid(tempData) && mPreData.equals(tempData) && !mSend) {
            onReceived(tempData);
            mSend = true;
            mPreData = tempData;
            if (isLoading) {
                isLoading = false;
                mHandler.sendEmptyMessage(END_LOADING);
            }

        } else if (!mPreData.equals(tempData)) {            mSend = false;
            mPreData = tempData;
            if (!isLoading) {
                isLoading = true;
                mHandler.sendEmptyMessage(LOADING);
            }
        }

                if (!isValid(tempData)) {
            if (isLoading) {
                isLoading = false;
                mHandler.sendEmptyMessage(END_LOADING);
            }
        }

    }


    private boolean isValid(String tempData) {
        if (!tempData.contains(" ")
                && !TextUtils.isEmpty(tempData)
                && tempData.matches("[0-9]+")) {
            return true;
        }
        return false;
    }


    private String splitWeight(String data) {
        String value[] = data.split("\n\r");
        if (value.length == 0) {
            return "";
        }
        String temp = value[value.length - 1].trim();
        temp = temp.replaceAll("\\s+", "-");
        String tempValue[] = temp.split("-");
        if (tempValue.length > 0) {
            return tempValue[0];
        }
        return "";
    }


    private void registerUsbReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        mAttachReceiver = new UsbDeviceReceiver();
        mContext.getApplicationContext().registerReceiver(mAttachReceiver, filter);
    }


    private class UsbDeviceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(intent.getAction())) {
                UsbDevice device = intent.getExtras().getParcelable("device");
                if (DeviceNodeUtil.isEwsDevice(device)) {
                    start();
                    mCallback.onDeviceConnect(true);
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(intent.getAction())) {
                UsbDevice device = intent.getExtras().getParcelable("device");
                if (DeviceNodeUtil.isEwsDevice(device)) {
                    stop();
                    mCallback.onDeviceConnect(false);
                }
            }
        }
    }
}
