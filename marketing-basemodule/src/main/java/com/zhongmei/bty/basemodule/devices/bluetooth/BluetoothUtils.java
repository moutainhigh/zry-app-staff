package com.zhongmei.bty.basemodule.devices.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by demo on 2018/12/15
 */

public class BluetoothUtils {

    private final static String TAG = BluetoothUtils.class.getSimpleName();

    public final static int STATE_NOT_SUPPORT_BLUETOOTH = -999;

    /**
     * 检测蓝牙状态
     *
     * @return
     */
    public static int getBluetoothState() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            return STATE_NOT_SUPPORT_BLUETOOTH;
        }

        //预防蓝牙状态获取的bug
        if (!adapter.isEnabled()) {
            Log.e(TAG, "BluetoothAdapter is not enabled!");
            return BluetoothAdapter.STATE_OFF;
        }

        return adapter.getState();
    }

    /**
     * 跳转至设置界面操作蓝牙
     *
     * @param context
     */
    public static void gotoSetBluetooth(Context context) {
        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        context.startActivity(intent);
    }

}
