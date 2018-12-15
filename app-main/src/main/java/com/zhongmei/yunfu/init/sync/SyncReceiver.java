package com.zhongmei.yunfu.init.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.init.sync.bean.SyncItem;

/**
 * Created by demo on 2018/12/15
 */

public class SyncReceiver extends BroadcastReceiver {

    public static final String ACTION = "com.zhongmei.sync.SyncReceiver";
    private boolean isCommercialExpire = false;

    public static void registerReceiver(Context context) {
        // 注册广播接收
        SyncReceiver receiveBroadCast = new SyncReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        //context.registerReceiver(receiveBroadCast, filter);
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
        lbm.registerReceiver(receiveBroadCast, filter);
    }

    public static void notifyModule(SyncItem<?> syncItem, Class<?> contentClazz) {
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(BaseApplication.sInstance);
        lbm.sendBroadcast(new Intent(SyncReceiver.ACTION)
                .putExtra("contentClazz", contentClazz)
                .putExtra("syncItem", syncItem)
        );
    }

    @Override
    public final void onReceive(Context context, Intent intent) {
        Class<?> clazz = (Class<?>) intent.getSerializableExtra("contentClazz");
        SyncItem<?> syncItem = (SyncItem<?>) intent.getSerializableExtra("syncItem");
        onReceive(clazz, syncItem);
    }

    public void onReceive(Class<?> clazz, SyncItem<?> syncItem) {
        /*if (clazz == CommercialExpireConfig.class) {
            boolean commercialExpire = isCommercialExpire((SyncItem<CommercialExpireConfig>) syncItem);
            if (commercialExpire != isCommercialExpire) {
                isCommercialExpire = commercialExpire;
                if (isCommercialExpire) {
                    AppDialog.showContractOverdueDialog();
                }
            }
        }*/
    }

    /*private boolean isCommercialExpire(SyncItem<CommercialExpireConfig> syncItem) {
        if (syncItem != null) {
            List<CommercialExpireConfig> commercialExpireConfigList = syncItem.getDatas();
            if (commercialExpireConfigList != null && commercialExpireConfigList.size() > 0) {
                return commercialExpireConfigList.get(0).isExpired;
            }
        }
        return false;
    }*/
}
