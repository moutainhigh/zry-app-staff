package com.zhongmei.bty.mobilepay.fragment.mobilepay;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.commonbusiness.view.ShowBarcodeView;
import com.zhongmei.bty.basemodule.devices.scaner.DeWoScanCode;
import com.zhongmei.bty.basemodule.devices.scaner.ScanDataReceivedListener;
import com.zhongmei.bty.basemodule.devices.scaner.innerscanner.IScannerManager;
import com.zhongmei.bty.basemodule.devices.scaner.innerscanner.InnerScannerManager1;
import com.zhongmei.bty.basemodule.pay.event.RegisterDeWoOnlinePayScanEvent;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.bean.MobilePayMenuItem;
import com.zhongmei.bty.mobilepay.bean.MobliePayMenuTool;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.ViewUtil;

import java.util.List;

/**
 * 移动支付扫描枪界面
 * Created by demo on 2018/12/15
 */
public class MobilePayScanFragment extends BasicFragment implements ScanDataReceivedListener, TextView.OnEditorActionListener, View.OnClickListener {

    public static final String TAG = MobilePayScanFragment.class.getSimpleName();

    protected TextView mTvRefreshPayState;

    protected ShowBarcodeView mShowBarcodeView;

    protected LinearLayout mLlScanTypes;

    private EditText mWechatIdET;// 外接扫码枪输入框

    private IPaymentInfo mPaymentInfo;

    private MobliePayMenuTool mobliePayMenuTool;

    private IScannerManager mScannerManager;// 一体机扫描

    private MobilePayDialog.MobilePayCallBack mobilePayCallBack;

    public static MobilePayScanFragment newInstance(IPaymentInfo info, MobilePayDialog.MobilePayCallBack payCallBack) {
        MobilePayScanFragment mobilePayScanFragment = new MobilePayScanFragment();
        mobilePayScanFragment.mobilePayCallBack = payCallBack;
        mobilePayScanFragment.mPaymentInfo = info;
        return mobilePayScanFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay_mobile_pay_scan_fragment_layout, container, false);
        mobliePayMenuTool = new MobliePayMenuTool(getContext());
        bindView(view);
        setSupportPayTypeView();
        initScanManager();
        if (mScannerManager != null) {
            mScannerManager.start();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.registerEventBus();
        this.registerDewoScan();
    }

    @Override
    public void onDestroyView() {
        if (mScannerManager != null) {
            mScannerManager.stop();
        }
        this.unregisterEventBus();
        this.unRegisterDewoScan();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (mScannerManager != null) {
            mScannerManager.destory();
        }
        super.onDestroy();
    }

    private void bindView(View view) {
        mTvRefreshPayState = (TextView) view.findViewById(R.id.tv_refresh_pay_state);
        mShowBarcodeView = (ShowBarcodeView) view.findViewById(R.id.showBarcode);
        mShowBarcodeView.setShowType(ShowBarcodeView.SHOW_SCAN_TO_CUSTOMER);
        mLlScanTypes = (LinearLayout) view.findViewById(R.id.ll_scan_type);
        mWechatIdET = (EditText) view.findViewById(R.id.pay_online_wechat_id);
        mTvRefreshPayState.setOnClickListener(this);
        mWechatIdET.setOnEditorActionListener(this);
        mWechatIdET.setInputType(InputType.TYPE_NULL);
        mWechatIdET.requestFocus();
    }

    private void setSupportPayTypeView() {
        List<MobilePayMenuItem> menusList = mobliePayMenuTool.getScanCodeMenus();
        if (Utils.isNotEmpty(menusList)) {
            ImageView imageView;
            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            for (MobilePayMenuItem item : menusList) {
                imageView = new ImageView(getContext());
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setPadding(DensityUtil.dip2px(getContext(), 2), DensityUtil.dip2px(getContext(), 2), DensityUtil.dip2px(getContext(), 2), DensityUtil.dip2px(getContext(), 2));
                imageView.setImageResource(item.payModeIcon);
                mLlScanTypes.addView(imageView);
            }
        }
    }

    private void initScanManager() {
        /*if (DensityUtil.isHaveMiniScreen(this.getActivity()) && Product.isSupportSoftwareScanner()) {//如果有副屏且软解码
            mScannerManager = InnerScanerManager.newInstance(this.getActivity(), this);//副屏进程
        } else {*/
        mScannerManager = InnerScannerManager1.newInstance(this.getActivity(), this);//当前进程
        //}
    }

    //获取副屏扫描数据
    @Override
    public void onDataReceivedOver(String authCode) {
        try {
            //PLog.d(PLog.TAG_CALLPRINT_KEY, "info:内置扫码枪扫码时间:" + System.currentTimeMillis() + ",TradeUuid:" + mPaymentInfo.getTradeVo().getTrade().getUuid() + ",authCode:" + authCode + "->onDataReceivedOver()");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果下单成功，启动被扫支付
        if (!TextUtils.isEmpty(authCode)) {
            // TODO: 2018/6/5 主扫 回调接口
            mobilePayCallBack.payByAuthCode(authCode, PayModeId.MOBILE_PAY);
            showScanOverIcon();
        } else {
            ToastUtil.showLongToast(R.string.pay_authcode_can_not_empty);
        }
    }

    private void registerDewoScan() {
        DeWoScanCode.getInstance().registerReceiveDataListener(new DeWoScanCode.OnReceiveDataListener() {
            @Override
            public void onReceiveData(String authCode) {
                mobilePayCallBack.payByAuthCode(authCode, PayModeId.MOBILE_PAY);
                showScanOverIcon();
            }
        });
    }

    private void unRegisterDewoScan() {
        DeWoScanCode.getInstance().unRegisterReceiveDataListener();
    }

    public void onEventMainThread(RegisterDeWoOnlinePayScanEvent event) {
        registerDewoScan();
    }

    @Override
    public void onClick(View v) {
        if (ClickManager.getInstance().isClicked()) return;
        if (v.getId() == R.id.tv_refresh_pay_state) {
            // TODO: 2018/6/5 刷新支付状态
            mobilePayCallBack.getPayState();
        }
    }

    //显示扫码完成提示
    public void showScanOverIcon() {
        mShowBarcodeView.setShowType(ShowBarcodeView.SHOW_SCAN_SUCCESS);
    }

    public void showPayStatusButton() {
        mTvRefreshPayState.setVisibility(View.VISIBLE);
    }

    public void showBarcode(Bitmap bitmap) {
        mShowBarcodeView.setShowQR(bitmap);
    }

    public void showBarcodeType(int showType) {
        mShowBarcodeView.setShowType(showType);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String authCode = mWechatIdET.getText().toString();
        try {
            //PLog.d(PLog.TAG_CALLPRINT_KEY, "info:外置扫码枪扫码时间:" + System.currentTimeMillis() + ",TradeUuid:" + mPaymentInfo.getTradeVo().getTrade().getUuid() + ",authCode:" + authCode + "->onEditorAction()");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果下单成功，启动被扫支付
        if (!TextUtils.isEmpty(authCode)) {
            // TODO: 2018/6/5 主扫 回调接口
            mobilePayCallBack.payByAuthCode(authCode, PayModeId.MOBILE_PAY);
            mWechatIdET.setText("");
            mWechatIdET.requestFocus();
            ViewUtil.hiddenSoftKeyboard(mWechatIdET);
            showScanOverIcon();
        } else {
            ToastUtil.showLongToast(R.string.pay_authcode_can_not_empty);
        }

        return false;
    }
}
