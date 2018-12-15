package com.zhongmei.bty.basemodule.devices.ews;

import android.content.Context;

public final class ElecScaleManagerWrapper {
    private final String TAG = ElecScaleManagerWrapper.class.getSimpleName();
    //ElecScaleManager elecScaleManager;

    public ElecScaleManagerWrapper(Context context) {
        //elecScaleManager = new ElecScaleManager(context);
    }

    public void startGetElecScaleData(final ElecScaleManagerWrapper.DataReceivedListener listener) {
        /*elecScaleManager.startGetElecScaleData(new ElecScaleManager.ElecScaleDataListener() {
            @Override
            public void onDeviceConnect(boolean connect) {//USB线和pos连接ok
                if (connect) {
                    listener.onDeviceConnect(true);
                } else {
                    listener.onEndLoading();
                    listener.onDeviceConnect(false);
                }
            }

            @Override
            public void onDataReport(String data) {
                try {
                    float weight = Float.parseFloat(elecScaleManager.getWeightFromReportString(data));
                    listener.onWeightReceived(weight);
                } catch (NumberFormatException e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onErrorReport(String errorMessage) {
                if ("ERROR_NO_HEARTBEAT".equals(errorMessage)) { //电子秤问题或者电子秤和USB线连接问题(不能正常读取到数据)
                    listener.onEndLoading();
                    listener.onDeviceConnect(false);
                } else if ("ERROR_OK".equals(errorMessage)) {    //电子秤连接ok(能够正常都会数据)
                    listener.onDeviceConnect(true);
                }
            }
        });*/
    }

    public void stopGetElecScaleData() {
        //elecScaleManager.stopGetElecScaleData();
    }

    public interface DataReceivedListener {
        void onStartLoading();

        void onEndLoading();

        void onDeviceConnect(boolean connect);

        void onWeightReceived(float weight);
    }
}
