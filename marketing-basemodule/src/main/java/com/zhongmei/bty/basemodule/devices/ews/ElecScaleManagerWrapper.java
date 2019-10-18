package com.zhongmei.bty.basemodule.devices.ews;

import android.content.Context;

public final class ElecScaleManagerWrapper {
    private final String TAG = ElecScaleManagerWrapper.class.getSimpleName();

    public ElecScaleManagerWrapper(Context context) {
            }

    public void startGetElecScaleData(final ElecScaleManagerWrapper.DataReceivedListener listener) {

    }

    public void stopGetElecScaleData() {
            }

    public interface DataReceivedListener {
        void onStartLoading();

        void onEndLoading();

        void onDeviceConnect(boolean connect);

        void onWeightReceived(float weight);
    }
}
