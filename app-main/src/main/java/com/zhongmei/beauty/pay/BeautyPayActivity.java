package com.zhongmei.beauty.pay;

import android.os.Bundle;
import android.view.KeyEvent;

import com.zhongmei.bty.base.MainBaseActivity;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.mobilepay.IPayConstParame;
import com.zhongmei.bty.mobilepay.enums.PayActionPage;
import com.zhongmei.bty.pay.fragment.MainPayFragment;
import com.zhongmei.bty.pay.fragment.MainPayFragment_;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.resp.UserActionEvent;



public class BeautyPayActivity extends MainBaseActivity implements IPayConstParame {
    private static final String TAG = BeautyPayActivity.class.getSimpleName();
    private static final String EXTRA_LAG_INFO = "lag_info";
    private MainPayFragment mMainPayFragment;
    private TradeVo mTradeVo;
    private boolean isOrderCenter = false;    private boolean isGroupPay = false;    private int defaultPaymentMenuType = -1;    private int quickPayType = -1;
    private PayScene payScene = PayScene.SCENE_CODE_SHOP;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        getExtras();
        mMainPayFragment = new MainPayFragment_();
        mMainPayFragment.setDoPayApi(BeautyDoPayApi.getNewInstance());
        setContentView(R.layout.pay_activity_layout);
        initView();
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(EXTRA_TRADE_VO)) {
                this.mTradeVo = (TradeVo) extras.getSerializable(EXTRA_TRADE_VO);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onResume() {
        super.onResume();
        UserActionEvent.end(UserActionEvent.FAST_FOOD_PAY);
    }


    private void initView() {
        showViews();
    }


    private void showViews() {
        mMainPayFragment.setArguments(createArguments());
        this.addFragment(R.id.snack_pay_content, mMainPayFragment, "MainPayFragment");
    }


        private Bundle createArguments() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isOrderCenter", isOrderCenter);
        bundle.putBoolean("isOrdered", isOrderCenter);
        bundle.putBoolean("isGroupPay", isGroupPay);
        bundle.putBoolean("isSplit", false);
        bundle.putInt("paymenutype", defaultPaymentMenuType);
        bundle.putSerializable("payScene", payScene);
        bundle.putSerializable(EXTRA_TRADE_VO, mTradeVo);
        bundle.putInt(EXTRA_QUICK_PAY_TYPE, quickPayType);
        bundle.putSerializable(EXTRA_PAY_ACTION_PAGE, PayActionPage.COMPAY);

        return bundle;
    }

    @Override
    protected void onDestroy() {
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
