package com.zhongmei.bty.basemodule.devices.led;

import java.util.Timer;

/**
 * @Date：2015年11月26日
 * @Description:提供led灯控制接口
 * @Version: 1.0
 */
public class LedControl {
    private static LedControl ledControl;
    //private PadManager mPadManager;

    private Timer mNewTradeLedControlTimer;

    private Timer mTradeCancelLedControlTimer;

    private Timer mPrintFailLedControlTimer;

    private boolean tradeStatus = false;
    //private int useLedIndex=Values.PadLedNumber.LED_3;

    public static LedControl getInstance() {
        synchronized (LedControl.class) {
            if (ledControl == null) {
                ledControl = new LedControl();
            }
        }
        return ledControl;
    }

    private LedControl() {
        /*mPadManager = new PadManager(BaseApplication.sInstance);
        if (Product.getPadLedNumber() == 1) {//ep900只有一个led灯
            useLedIndex= Values.PadLedNumber.LED_1;
        }*/
    }

    /**
     * @param event
     * @param open
     * @Date 2015年11月23日
     * @Description: 外部调用此方法设置led
     * @Return void
     */
    public void setLed(Event event, boolean open) {
        /*if (!Product.isOnPosMachine()) {
            return;
        }
        switch (event) {
            case NEW_TRADE_COMING:
            case CUSTOMER_ARRIVE:
                newTradeComing(open);
                break;
            case TRADE_CANCEL:
                tradeCancel(open);
                break;
            case PRINT_FAIL:
                printFail(open);
                break;
            default:
                break;
        }*/

    }

    private synchronized void newTradeComing(boolean open) {
        mNewTradeLedControlTimer = controlLedBlueOrRed(open, mNewTradeLedControlTimer, true);
    }

    private synchronized void tradeCancel(boolean open) {
        mTradeCancelLedControlTimer = controlLedBlueOrRed(open, mTradeCancelLedControlTimer, true);
    }

    private synchronized void printFail(boolean open) {
        mPrintFailLedControlTimer = controlLedBlueOrRed(open, mPrintFailLedControlTimer, false);
    }

    private Timer controlLedBlueOrRed(boolean open, Timer timer, final boolean blue) {
        /*if (timer != null) {
            timer.cancel();
        }
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                if (PadManager.ensurePadManagerAttached()) {
                    mPadManager.setLEDSystem(useLedIndex, Values.PadLedColor.LED_CLOSE);
                }
                return null;
            }
        }.execute();
        if (open) {
            tradeStatus = false;
            TimerTask newTradeTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (PadManager.ensurePadManagerAttached()) {
                        if (!tradeStatus) {
                            if (blue) {
                                mPadManager.setLEDSystem(useLedIndex, Values.PadLedColor.LED_BLUE);
                            } else {
                                mPadManager.setLEDSystem(useLedIndex, Values.PadLedColor.LED_RED);
                            }
                        } else {
                            mPadManager.setLEDSystem(useLedIndex, Values.PadLedColor.LED_CLOSE);
                        }
                        tradeStatus = !tradeStatus;
                    }
                }
        } ;
        timer = new Timer();
        timer.schedule(newTradeTimerTask, 500, 500);
        return timer;
    }*/
        return null;
    }

    public enum Event {
        NEW_TRADE_COMING, TRADE_CANCEL, PRINT_FAIL, CUSTOMER_ARRIVE
    }

    public void release() {
        /*ledControl = null;
        PadManager.detach();*/
    }
}
