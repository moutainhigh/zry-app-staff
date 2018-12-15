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

/**
 * 读取电子秤数据
 */
public class EwsManager {

    private static final String TAG = "EwsManager";

    private static final String DEFALUT_WEIGTH_DATA = "0000";

    private static final int BAUDRATE = 9600;// 波特率

    // 0: none, 1: odd, 2: even parity;
    private static final int PARITY = 0;// 无奇偶校验

    // 0: none, 1: have stop bit
    private static final int STOP = 1;// 停止位

    private static final int SUCCESS = 1;//回调数据

    private static final int LOADING = 2;//加载中

    private static final int END_LOADING = 3;//加载完成

    private static final int CONNECTION_STATE = 4;//状态信息

    private InputStream mInputStream = null;

    //private SerialPort mSerialPort = null;

    private ReadThread mReadThread;

    private DataReceivedListener mCallback;

    // 标记是否发送重量数据
    private boolean mSend = false;

    //是否在读数据中
    private boolean isLoading = false;

    // 上一次传回的重量数据
    private String mPreData = DEFALUT_WEIGTH_DATA;

    private Context mContext;

    private UsbDeviceReceiver mAttachReceiver;

    public EwsManager(Context context, DataReceivedListener listener) {
        this.mContext = context;
        this.mCallback = listener;
        registerUsbReceiver();
    }

    /**
     * 判断电子秤是否连接
     */
    public boolean isDeviceConnection() {
        return DeviceNodeUtil.isDeviceConnected();
    }

    /**
     * 开启扫描服务
     */
    public void start() {
        Log.d(TAG, "start:->......");
        clearLast();
        openDataPort();
        startNewTesk();
    }

    /**
     * 清楚历史
     */
    private void clearLast() {
        mPreData = DEFALUT_WEIGTH_DATA;
        stopCurrentThread();
        chearCurrentHandler();
    }

    /**
     * 关闭当前线程
     */
    private void stopCurrentThread() {
        if (mReadThread != null && mReadThread.isRunning()) {
            mReadThread.setRunning(false);
            mReadThread = null;
        }
    }

    /**
     * 开启新线程服务
     */
    private void startNewTesk() {
        mReadThread = new ReadThread();
        mReadThread.start();
    }

    /**
     * 清空当前Handler消息
     */
    private void chearCurrentHandler() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 停止扫描服务
     */
    private void stop() {
		/*clearLast();
		if (mInputStream != null) {
			try {
				mInputStream.close();
				mInputStream = null;
			} catch (IOException e) {
				Log.e(TAG, "", e);
			}
		}
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}*/
    }

    /**
     * 停止扫描服务并注销广播
     */
    public void destory() {
        Log.d(TAG, "destory:->......");
        stop();
        if (mAttachReceiver != null) {
            mContext.getApplicationContext().unregisterReceiver(mAttachReceiver);
        }
    }

    /**
     * 打开串口
     */
    private boolean openDataPort() {
		/*if (mSerialPort != null) {
			return true;
		}
		try {
			// 获取节点信息
			String deviceNodePath = DeviceNodeUtil.getDeviceNodeAddress();
			Log.d(TAG, "DEVICE_PATH:->..." + deviceNodePath);

			Message ms = mHandler.obtainMessage();
			ms.what = CONNECTION_STATE;
			if (deviceNodePath == null) {
				// 没连接电子秤
				ms.obj = false;
				mHandler.sendMessage(ms);
				return false;
			} else {
				// 电子秤已连接
				ms.obj = true;
				mHandler.sendMessage(ms);
			}

			mSerialPort = new SerialPort(new File(deviceNodePath), BAUDRATE, PARITY, STOP, 8);
			if (mSerialPort != null) {
				mInputStream = mSerialPort.getInputStream();
			}

		} catch (SecurityException e) {
			Log.e(TAG, "", e);
		} catch (IOException e) {
			Log.e(TAG, "", e);
		}
		return mSerialPort != null ? true : false;*/
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

    /**
     * 比较电子秤回传的数据两次重量相同才能确定重量
     */
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

        } else if (!mPreData.equals(tempData)) {//新数据与之前数据不一相等
            mSend = false;
            mPreData = tempData;
            if (!isLoading) {
                isLoading = true;
                mHandler.sendEmptyMessage(LOADING);
            }
        }

        //异常数据的判断
        if (!isValid(tempData)) {
            if (isLoading) {
                isLoading = false;
                mHandler.sendEmptyMessage(END_LOADING);
            }
        }

    }

    /**
     * 判断数据是否有效
     */
    private boolean isValid(String tempData) {
        if (!tempData.contains(" ")
                && !TextUtils.isEmpty(tempData)
                && tempData.matches("[0-9]+")) {
            return true;
        }
        return false;
    }

    /**
     * 获取重量数据 重量 单价 总价 数据格式：0584 000 000\n\r0584 000 000
     */
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

    /**
     * 注册Usb插拔监听
     */
    private void registerUsbReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        mAttachReceiver = new UsbDeviceReceiver();
        mContext.getApplicationContext().registerReceiver(mAttachReceiver, filter);
    }

    /**
     * 监听Usb的插拔得到电子秤的连接状态
     */
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
