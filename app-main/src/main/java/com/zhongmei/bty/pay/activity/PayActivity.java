package com.zhongmei.bty.pay.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.zhongmei.bty.base.MainBaseActivity;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.util.ServerHeartbeat;
import com.zhongmei.bty.mobilepay.IPayConstParame;
import com.zhongmei.bty.mobilepay.enums.PayActionPage;
import com.zhongmei.bty.pay.fragment.MainPayFragment;
import com.zhongmei.bty.pay.fragment.MainPayFragment_;
import com.zhongmei.bty.pay.utils.PayUtils;
import com.zhongmei.bty.snack.offline.Snack;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.JsonUtil;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.ui.view.CalmLoadingDialogFragment;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

/**
 * Created by demo on 2018/12/15
 * 快餐收银主界面
 */
@EActivity(R.layout.pay_activity_layout)
public class PayActivity extends MainBaseActivity implements IPayConstParame {
    private static final String TAG = PayActivity.class.getSimpleName();

    @Extra("tradeVo")
    protected TradeVo mTradeVo;
    /*  private static TradeVo STradeVo;

      public static void setTradeVo(TradeVo tradeVo) {
          STradeVo = tradeVo;
      }*/
    @Extra("doPayApi")
    protected String doPayApiClassName;
    @Extra("isOrderCenter")
    protected boolean isOrderCenter;

    @Extra("isGroupPay")
    protected boolean isGroupPay = true;//add v8.9

    @Extra("payScene")
    protected PayScene payScene = PayScene.SCENE_CODE_SHOP;//支付场景

    @Extra("paymenutype")
    protected int defaultPaymentMenuType = -1;//默认支付菜单
    private boolean isFromOpenPlatform = false;//是否来自开放平台 add v8.5
    private String mPartnerId;//是否来自开放平台 add v8.5
    MainPayFragment mMainPayFragment;

    @Extra(IPayConstParame.EXTRA_QUICK_PAY_TYPE)
    protected int quickPayType = -1;

    @Extra(IPayConstParame.EXTRA_MENU_DISPLAY_TYPE)
    protected int menuDisplayType;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        UserActionEvent.start(UserActionEvent.FAST_FOOD_PAY);
        mMainPayFragment = new MainPayFragment_();
    }

    @Override
    protected void onStop() {
        setActivityResult();
        super.onStop();
    }

    private void setActivityResult() {
        if (mMainPayFragment != null && this.isFromOpenPlatform) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_PARTNER_ID, this.mPartnerId);
            this.setResult(mMainPayFragment.getPayResult(), intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserActionEvent.end(UserActionEvent.FAST_FOOD_PAY);
    }

    @AfterViews
    void initView() {
        if (mTradeVo != null && mTradeVo.getTrade() != null) {
            /*PLog.d(PLog.QUICK_SERVICE_KEY,
                    "info:进入快餐收银界面;tradeUuid:" + mTradeVo.getTrade().getUuid() + ";position:" + TAG + "->initView()");*/
        }
        //modify v8.5 begin  对接开放平台结账
        Bundle extras_ = getIntent().getExtras();
        if (extras_ != null) {
            if (extras_.containsKey(EXTRA_PARTNER_ID)) {
                this.mPartnerId = extras_.getString(EXTRA_PARTNER_ID);
            }
            if (extras_.containsKey(EXTRA_TRADE_INFO)) {
                String tradeVo_json = extras_.getString(EXTRA_TRADE_INFO);
                initViewByTradeInfo(tradeVo_json);
            } else if (extras_.containsKey(EXTRA_TRADE_ID)) {
                Long tradeId = extras_.getLong(EXTRA_TRADE_ID);
                initViewByTradeId(tradeId);
            } else {
                showViews();
            }
            setActivityResult();
        } else {
            showViews();
        }
        //modify v8.5 end  对接开放平台结账
    }
//add v8.5 begin  对接开放平台结账

    private void showViews() {
        mMainPayFragment.setDoPayApi(PayUtils.doPayApiFactory(doPayApiClassName));//add v8.14
        mMainPayFragment.setArguments(createArguments());
        this.addFragment(R.id.snack_pay_content, mMainPayFragment, "MainPayFragment");
    }

    //根据tradeVo json 来初始化结算界面
    private void initViewByTradeInfo(String tradeInfo) {
        try {
            this.mTradeVo = JsonUtil.jsonToObject(tradeInfo, TradeVo.class);
            if (mTradeVo != null) {
                isFromOpenPlatform = true;
                isOrderCenter = true;
                showViews();
                PayUtils.saveTradeVoAsync(this.mTradeVo);
            } else {
                errorAlert();
            }
        } catch (Exception e) {
            errorAlert();
        }
    }

    //根据tradeid 先查询tradeVo 再始化结算界面
    private void initViewByTradeId(final Long tradeId) {
        new AsyncTask<Void, Void, TradeVo>() {
            private TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
            private CalmLoadingDialogFragment mDialogFragment = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //显示咖啡杯
                mDialogFragment = CalmLoadingDialogFragment.show(getSupportFragmentManager());
            }

            @Override
            protected TradeVo doInBackground(Void... params) {

                TradeVo tradeVo = null;
                try {
                    tradeVo = tradeDal.findTrade(tradeId);
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
                return tradeVo;
            }

            protected void onPostExecute(TradeVo tradeVo) {
                //关闭咖啡杯
                mTradeVo = tradeVo;
                if (mTradeVo != null) {
                    isFromOpenPlatform = true;
                    isOrderCenter = true;
                    showViews();
                    CalmLoadingDialogFragment.hide(mDialogFragment);
                } else {
                    errorAlert();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void errorAlert() {
        ToastUtil.showLongToast(getString(R.string.toast_data_error));
        this.finish();
    }

    //add v8.5 end
    //支付界面初始化参数
    private Bundle createArguments() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isOrderCenter", isOrderCenter);
        bundle.putBoolean("isOrdered", isOrderCenter);
        bundle.putBoolean("isGroupPay", isGroupPay);
        bundle.putBoolean("isSplit", false);
        bundle.putInt("paymenutype", defaultPaymentMenuType);
        bundle.putBoolean("isFromOpenPlatform", isFromOpenPlatform);
        bundle.putSerializable("payScene", payScene == null ? PayScene.SCENE_CODE_SHOP : payScene);//add 20170704
        bundle.putSerializable("tradeVo", mTradeVo);
        bundle.putInt(IPayConstParame.EXTRA_QUICK_PAY_TYPE, quickPayType);
        bundle.putInt(IPayConstParame.EXTRA_MENU_DISPLAY_TYPE, menuDisplayType);
        bundle.putSerializable(EXTRA_PAY_ACTION_PAGE, PayActionPage.COMPAY);  //add 20180309
        boolean networkAvailable = ServerHeartbeat.getInstance().getNetworkState() == ServerHeartbeat.NetworkState.NetworkAvailable;
        bundle.putBoolean("isSupportOnline", networkAvailable);
        bundle.putBoolean("isSupportMobilePay", !Snack.isOfflineTrade(mTradeVo));
        bundle.putBoolean("isSupportYIPay", !Snack.isOfflineTrade(mTradeVo));
        return bundle;
    }

    @Override
    protected void onDestroy() {
        /* setTradeVo(null);*/
        DisplayServiceManager.doCancel(getApplicationContext());
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
