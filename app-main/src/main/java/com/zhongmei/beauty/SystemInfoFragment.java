package com.zhongmei.beauty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.ShopInfo;
import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.ui.base.BasicFragment;

/**
 * 设备 信息界面
 * Created by txg on 2016/10/24.
 */

public class SystemInfoFragment extends BasicFragment {
    private TextView mTvShopId;
    private TextView mTvTabCode;
    private TextView mOrderId;
    private TextView mTvWaiter;
    private TextView mTvUsage;
    private TextView mTvWifi;
    private TextView mTvUrl;
    private TextView mTvV;
    private TextView mTvDeviceid;
    private TextView mTvDeviceCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sysinfo ,null);
        initView(view);
        process();
        return view;
    }

    @Override
    public void onResume() {
        mOrderId.setText(getCurrentTradeNo());
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void initView(View view) {
        mTvShopId = (TextView) view.findViewById(R.id.tvShopid);
        mTvTabCode = (TextView) view.findViewById(R.id.tvTabCode);
        mOrderId = (TextView) view.findViewById(R.id.tvOrderid);
        mTvWaiter = (TextView) view.findViewById(R.id.tvWaiter);
        mTvUsage = (TextView) view.findViewById(R.id.tvUsage);
        mTvWifi = (TextView) view.findViewById(R.id.tvWifi);
        mTvUrl = (TextView) view.findViewById(R.id.tvUrl);
        mTvV = (TextView) view.findViewById(R.id.tvV);
        mTvDeviceid = (TextView) view.findViewById(R.id.tvDeviceid);
        mTvDeviceCode = (TextView) view.findViewById(R.id.tvDeviceCode);
    }



    private String getCurrentTradeNo(){
        return "";
    }

    private void process() {

            mTvWifi.setText(SystemUtils.getConnectWifiSsid());
            mTvUrl.setText("mk.ziranyu.com");
            mTvV.setText(SystemUtils.getVersionName());
                mTvDeviceid.setText(SystemUtils.getMacAddress());

            setShopInfo();
            setTabName();
            getLocalWaiterInfo();
    }


    private void setShopInfo(){
        ShopInfo shopInfo= ShopInfoManager.getInstance().getShopInfo();
        if(shopInfo!=null){
            mTvShopId.setText(shopInfo.getShopId().toString());
            mTvDeviceCode.setText(shopInfo.getShopName());
        }
    }

    /**
     * 获取本地服务员
     */
    private void getLocalWaiterInfo(){
        AuthUser user = Session.getAuthUser();
        if(user != null){
            mTvWaiter.setText(user.getName());
        }
    }

    public void setTabName() {
        String tableName= SharedPreferenceUtil.getSpUtil().getString(Constant.SP_TABLE_NAME,"--");

        mTvTabCode.setText(tableName);
    }

}
