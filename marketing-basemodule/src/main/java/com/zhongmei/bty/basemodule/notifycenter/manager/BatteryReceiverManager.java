package com.zhongmei.bty.basemodule.notifycenter.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Observable;
import android.os.BatteryManager;
import android.util.Log;

public class BatteryReceiverManager extends Observable<BatteryReceiverManager.BatteryChangedListener> {
    private static final String TAG = "BatteryReceiverManager";

    private Context mContext;
    int status = BatteryManager.BATTERY_STATUS_UNKNOWN;
    int level = 0;
    int maxLevel = 100;
    boolean plugged = false;

    private static BatteryReceiverManager batteryReceiverManager;

    private BatteryReceiverManager(Context context) {
        mContext = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(broadcastReceiver, filter);
    }

    public static BatteryReceiverManager registerReceiver(Context context) {
        synchronized (BatteryReceiverManager.class) {
            if (batteryReceiverManager == null) {
                batteryReceiverManager = new BatteryReceiverManager(context);
            }
        }
        return batteryReceiverManager;
    }

    public static BatteryReceiverManager getReceiver() {
        return batteryReceiverManager;
    }

    public void unregisterReceiver() {
        mContext.unregisterReceiver(broadcastReceiver);
    }

    /**
     * 是否存在电池
     */
    public boolean exist() {
        return status != BatteryManager.BATTERY_STATUS_UNKNOWN;
    }

    public int getStatus() {
        return status;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getLevelPercent() {
        return (level * 100) / maxLevel;
    }

    @Override
    public void registerObserver(BatteryChangedListener observer) {
        super.registerObserver(observer);
        notifyChanged(status, level, maxLevel, plugged);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
                level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                maxLevel = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
                plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) != 0;
                if (status == BatteryManager.BATTERY_STATUS_UNKNOWN) {

                } else {


                }

                notifyChanged(status, level, maxLevel, plugged);
            }
        }
    };

    private void notifyChanged(int status, int level, int maxLevel, boolean plugged) {
        Log.i(TAG, String.format("status=%s, level=%s, maxLevel=%s, plugged=%s", status, level, maxLevel, plugged));
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onBatteryChanged(status, level, maxLevel, plugged);
            }
        }
    }

    public interface BatteryChangedListener {
        void onBatteryChanged(int status, int level, int maxLevel, boolean plugged);
    }


}
