package com.zhongmei.bty.basemodule.devices.mispos.watchdata;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.zhongmei.bty.basemodule.devices.mispos.watchdata.readcard.ReadCardResponseListener;
import com.zhongmei.bty.commonmodule.util.Standard;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 万能读卡器管理器
 */

public class WatchDataManager {
    private static final String TAG = WatchDataManager.class.getSimpleName();

    //工作线程休眠间隔
    private static final long SLEEP_INTERVAL = 500L;
    //读卡超时时间
    private static final long READ_CARD_TIMEOUT = 60 * 1000L;

    private static class LazySingletonHolder {
        private static final WatchDataManager INSTANCE = new WatchDataManager();
    }

    public synchronized static WatchDataManager getInstance() {
        return LazySingletonHolder.INSTANCE;
    }

    private ExecutorService mExecutorService;
    //private CardReader mCardReader;
    private Handler mHandler;
    private ReadCardTask mReadCardTask;
    private OpenCardTask mOpenCardTask;

    private WatchDataManager() {
        mExecutorService = Executors.newSingleThreadExecutor();
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 开启读卡器设备，并开始读卡任务
     */
    public void openDevice(Context context, final ReadCardResponseListener listener) {
        /*if (mCardReader == null) {
            mCardReader = new CardReader(context) {
                @Override
                public void onDevOpen(int status) {
                    //打开读卡器结果的回调函数，若status返回0则打开成功，其他打开失败
                    if (status == 0) {
                        if (isOpen) {
                            startReadCardNumber(listener);
                        }
                        if (listener != null) {
                            listener.onActive();
                        }
                    } else {
                        isOpen = false;
                        if (listener != null) {
                            listener.onError("T5", "");
                        }
                    }
                }

                @Override
                public void onDevClose(int status) {
                    isOpen = false;
                }
            };
        }
        //如果当前设备已打开，那么直接开启轮训读卡；如果当前设备没有被打开，那么先开启设备再开启轮训读卡
        if (mCardReader.getopenstate()) {
            startReadCardNumber(listener);
        } else {*/
        // FIX BUG 37523
        if (mOpenCardTask == null) {
            mOpenCardTask = new OpenCardTask();
        }
        mExecutorService.execute(mOpenCardTask);
        //}
    }

    private void startReadCardNumber(ReadCardResponseListener listener) {
        synchronized (this) {
            if (!isRead) {
                isRead = true;
                startReadCardTask(listener);
            }
        }
    }

    private void startReadCardTask(ReadCardResponseListener listener) {
        if (mExecutorService != null) {
            mReadCardTask = new ReadCardTask(mHandler, listener);
            mReadCardTask.setRunning(true);
            mExecutorService.execute(mReadCardTask);
        }
    }

    private void stopReadCardTask() {
        if (mExecutorService != null && !mExecutorService.isShutdown() && mReadCardTask != null) {
            mReadCardTask.setRunning(false);
        }
    }

    /**
     * 关闭读卡器设备，并停止读卡任务
     */
    public void closeDevice() {
        // 停止读卡任务
        stopReadCardTask();
        isOpen = false;
        isRead = false;
        /*if (mCardReader != null && mCardReader.getopenstate()) {
            mCardReader.close();
            Log.d(TAG, TAG + "----------------closeDevice()");
            mCardReader = null;
        }*/

    }

    private boolean isOpen = false;

    private boolean isRead = false;

    /**
     * 开卡任务
     */
    private class OpenCardTask implements Runnable {

        @Override
        public void run() {
            synchronized (this) {
                if (!isOpen) {
                    isOpen = true;
                    /*try {
                        mCardReader.open();
                    } catch (Exception e) {
                        e.printStackTrace();
                        isOpen = false;
                    }*/
                }
            }
        }
    }

    /**
     * 读卡任务
     */
    private class ReadCardTask implements Runnable {
        private boolean running = true;
        private ReadCardResponseListener mListener;
        private Handler mHandler;
        //开始读卡的时间
        private long mStartReadCardTime;

        public ReadCardTask(Handler handler, ReadCardResponseListener listener) {
            mHandler = handler;
            mListener = listener;
            running = true;
            mStartReadCardTime = Standard.Time.currentTime();
        }

        @Override
        public void run() {
            try {
                while (running) {
                    String cardNumber = "";
                    /*try {
                        cardNumber = mCardReader.readCardNumber();
                    } catch (CardException e) {

                        Log.d(TAG, e.getMessage());
                    }*/

                    if (TextUtils.isEmpty(cardNumber)) {
                        //如果当前时间减去读卡开始时间大于超时时间,那么返回失败回调;反之，则休眠500毫秒
                        long currentTime = Standard.Time.currentTime();
                        if (currentTime - mStartReadCardTime >= READ_CARD_TIMEOUT) {
                            running = false;
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mListener.onError("XY", "");
                                    //一旦读到卡，直接断开读卡设备，并停止读卡任务
                                    closeDevice();
                                }
                            });
                        } else {
                            try {
                                Thread.sleep(SLEEP_INTERVAL);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        running = false;
                        final String cardNo = cardNumber;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mListener.onResponse(cardNo);
                                //一旦读到卡，直接断开读卡设备，并停止读卡任务
                                closeDevice();
                            }
                        });
                    }
                }
            } catch (Exception e) {

            }
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        public void setListener(ReadCardResponseListener listener) {
            mListener = listener;
        }
    }
}
