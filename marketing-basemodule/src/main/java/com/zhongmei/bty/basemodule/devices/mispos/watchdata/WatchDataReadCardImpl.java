package com.zhongmei.bty.basemodule.devices.mispos.watchdata;

import android.content.Context;

import com.zhongmei.bty.basemodule.devices.mispos.watchdata.readcard.IReadCard;
import com.zhongmei.bty.basemodule.devices.mispos.watchdata.readcard.ReadCardResponseListener;

/**
 * 万能读卡器读卡实现
 */

public class WatchDataReadCardImpl implements IReadCard {
    @Override
    public void connectDevice(Context context, ReadCardResponseListener listener, boolean isDelay
    ) {
        WatchDataManager.getInstance().openDevice(context, listener);
    }

    @Override
    public void disconnectDevice() {
        WatchDataManager.getInstance().closeDevice();
    }
}
