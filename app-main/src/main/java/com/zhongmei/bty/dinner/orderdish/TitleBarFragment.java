package com.zhongmei.bty.dinner.orderdish;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.notifycenter.manager.BatteryReceiverManager;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.ui.base.BasicFragment;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;



@EFragment(R.layout.dinner_title_bar)
public class TitleBarFragment extends BasicFragment {
    private static final String TAG = TitleBarFragment.class.getSimpleName();

    @ViewById(R.id.title)
    RelativeLayout contentLayout;

    @ViewById(R.id.battery_state_layout)
    ViewGroup batteryStateLayout;
    @ViewById(R.id.battery_state)
    public TextView batteryStateTx;

    @ViewById(R.id.wifi_state_img)
    public ImageView wifiStateImg;

    @ViewById(R.id.tv_offline_upload_description)
    TextView offlineUploadDescriptionTv;

    @ViewById(R.id.iv_offline_upload_uploading)
    ImageView offlineUploadLoadImg;

    @ViewById(R.id.iv_offline_upload_status)
    ImageView offlineUploadStatusImg;

    @ViewById(R.id.ll_offline_upload_status)
    LinearLayout offlineUploadStatsuLl;

    private final static float MAX_BATTERY_HEIGHT = 10;


    public void showGrayBg() {
        setBgColor(R.color.titlebar_bg2);
    }

    public void setBgColor(int color) {
        contentLayout.setBackgroundColor(getResources().getColor(color));
    }


    public void registerBatteryReceiver() {
        BatteryReceiverManager.getReceiver().registerObserver(batteryChangedListener);
    }

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (getActivity().isFinishing())
                return;
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                int strength = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 4);
                if (strength == 0) {
                    wifiStateImg.setImageResource(R.drawable.wifi1);
                } else if (strength == 1) {
                    wifiStateImg.setImageResource(R.drawable.wifi2);
                } else if (strength == 2) {
                    wifiStateImg.setImageResource(R.drawable.wifi3);
                } else if (strength == 3) {
                    wifiStateImg.setImageResource(R.drawable.wifi4);
                }
            }
        }
    };


    public void registerWiFiReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter(WifiManager.RSSI_CHANGED_ACTION);
            getActivity().registerReceiver(wifiReceiver, intentFilter);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerBatteryReceiver();
        registerWiFiReceiver();
        registerOfflineUpdateReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        BatteryReceiverManager.getReceiver().unregisterObserver(batteryChangedListener);
        getActivity().unregisterReceiver(wifiReceiver);
        unRegisterOfflineUpdateReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    BatteryReceiverManager.BatteryChangedListener batteryChangedListener = new BatteryReceiverManager.BatteryChangedListener() {
        @Override
        public void onBatteryChanged(int status, int level, int maxLevel, boolean plugged) {
            if (getActivity().isFinishing())
                return;

            if (status == BatteryManager.BATTERY_STATUS_UNKNOWN) {
                batteryStateLayout.setVisibility(View.GONE);
            } else {
                batteryStateLayout.setVisibility(View.VISIBLE);
                int batteryNum = (level * 100) / maxLevel;
                float perDpBatteryNum = 100 / MAX_BATTERY_HEIGHT;
                float tempDpBatteryHeight = batteryNum / perDpBatteryNum;
                BigDecimal tempDpBatteryHeight1 =
                        new BigDecimal(tempDpBatteryHeight).setScale(0, BigDecimal.ROUND_HALF_UP);
                int dpBatteryHeight = tempDpBatteryHeight1.intValue();
                float pxBatteryHeight = DensityUtil.dip2px(MainApplication.getInstance(), dpBatteryHeight);
                LayoutParams layoutParams = batteryStateTx.getLayoutParams();
                layoutParams.height = (int) pxBatteryHeight;
                batteryStateTx.setLayoutParams(layoutParams);
                if (dpBatteryHeight <= 1) {
                    batteryStateTx.setBackgroundColor(getResources().getColor(R.color.battery_warning_bg));
                } else {
                    batteryStateTx.setBackgroundColor(getResources().getColor(R.color.battery_bg));
                }
            }
        }
    };



    private void registerOfflineUpdateReceiver() {
            }

    private void unRegisterOfflineUpdateReceiver() {
            }
}
