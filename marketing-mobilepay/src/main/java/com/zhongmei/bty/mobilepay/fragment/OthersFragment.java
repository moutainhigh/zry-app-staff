package com.zhongmei.bty.mobilepay.fragment;




import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;


import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.enums.PayActionPage;
import com.zhongmei.bty.mobilepay.fragment.com.BasePayFragment;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;
import com.zhongmei.bty.mobilepay.adapter.OtherPayModelAdapter;
import com.zhongmei.bty.mobilepay.event.ExemptEventUpdate;
import com.zhongmei.bty.mobilepay.v1.event.PaymentModeOtherChangeEvent;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;

import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.yunfu.db.entity.trade.PaymentModeShop;


import java.util.ArrayList;
import java.util.List;


public class OthersFragment extends BasePayFragment implements View.OnClickListener {
    private final String TAG = OthersFragment.class.getSimpleName();

    private TextView mPayAlterTV;

    private Button mPay;

    private ScrollView mGridView;


    private int mHorizontalSize = 3;

    public static OthersFragment newInstance(IPaymentInfo info, DoPayApi doPayApi) {
        OthersFragment f = new OthersFragment();
        f.setPaymentInfo(info);
        f.setDoPayApi(doPayApi);
        f.setArguments(new Bundle());
        return f;
    }


    public void setHorizontalSize(int horizontalSize) {
        this.mHorizontalSize = horizontalSize;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.pay_others_fragment_new, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignViews(view);
        initView();
    }

    private void assignViews(View view) {
        mPayAlterTV = (TextView) view.findViewById(R.id.cash_pay_alerttext);
        mGridView = (ScrollView) view.findViewById(R.id.gridView);
        mPay = (Button) view.findViewById(R.id.pay);
    }


    private void initView() {
        mPay.setOnClickListener(this);
        updateNotPayMent();
        this.registerEventBus();
        loadDatas();
        if (!mPaymentInfo.isDinner() || mPaymentInfo.isOrderCenter())
            DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
                mPaymentInfo.getOtherPay().clear();
        if (!hidden) {
            loadDatas();            updateNotPayMent();
            DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
        } else {

        }
        super.onHiddenChanged(hidden);
    }


    private void updateNotPayMent() {
        if (mPaymentInfo != null && mPaymentInfo.getOtherPay() != null && mPaymentInfo.getOtherPay().getGroupAmount() > 0) {
            double restAmout = mPaymentInfo.getNotPayMent();
            if (restAmout == 0) {
                mPayAlterTV.setVisibility(View.GONE);
            } else {
                                if (restAmout > 0) {
                    mPayAlterTV.setText(this.getActivity().getString(R.string.pay_rest_payment_text) + CashInfoManager.formatCash(restAmout));
                } else {
                                        mPayAlterTV.setText(String.format(this.getActivity().getString(R.string.more_pay_cash_text), CashInfoManager.formatCash(0 - restAmout)));
                }
                mPayAlterTV.setVisibility(View.VISIBLE);
            }

        } else {
            mPayAlterTV.setVisibility(View.GONE);
        }
        if (mPaymentInfo != null)
            DoPayUtils.updatePayEnable(getActivity(), mPay, enablePay());
    }

        public double getInputValue() {
        return this.mPaymentInfo.getOtherPay().getGroupAmount();
    }


    @Override
    public void onDestroy() {
        this.unregisterEventBus();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pay) {
            if (!ClickManager.getInstance().isClicked()) {
                if (doPayChecked(false)) {

                    final double restAmout = mPaymentInfo.getNotPayMent();

                                        if (restAmout < 0) {
                        List<PayModelItem> payModelItemList = mPaymentInfo.getOtherPay().getAllPayModelItems();
                        if (payModelItemList != null) {
                            for (PayModelItem payModelItem : payModelItemList) {
                                                                if (payModelItem.getPaymentModeShop() != null && payModelItem.getPaymentModeShop().getFaceValue() == null) {
                                    ToastUtil.showShortToast(R.string.auto_input_paymode_not_paymore);
                                    return;
                                }
                            }
                        }
                        CommonDialogFragment.CommonDialogFragmentBuilder dialogBuilder = new CommonDialogFragment.CommonDialogFragmentBuilder(
                                getActivity()).title(R.string.pay_more_attention_text)
                                .iconType(CommonDialogFragment.ICON_WARNING)
                                .positiveText(R.string.invoice_btn_ok)
                                .positiveLinstner(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View arg0) {
                                        mPaymentInfo.getOtherPay().setMorePayAmount(0 - restAmout);
                                        doPay();

                                    }
                                })
                                .negativeText(R.string.invoice_btn_cancel);

                        dialogBuilder.build().show(this.getActivity().getSupportFragmentManager(), "paymoredialog");
                    } else {
                        doPay();
                    }
                }
            }
        }
    }

    private void doPay() {
        UserActionEvent.start(UserActionEvent.DINNER_PAY_SETTLE_ORDER);
        mPay.setEnabled(false);
        if (mDoPayApi != null)
            mDoPayApi.doPay(getActivity(), mPaymentInfo, mPayOverCallback);
    }

    IPayOverCallback mPayOverCallback = new IPayOverCallback() {

        @Override
        public void onFinished(boolean isOK, int errorCode) {
            try {
                if (isOK) {
                                                            loadDatas();                                        DoPayUtils.updatePayEnable(getActivity(), mPay, enablePay());
                } else {
                    mPay.setEnabled(true);
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    };


    public void loadDatas() {
                if (mPaymentInfo == null) return;
        PayScene payScene = mPaymentInfo.getPayScene();
        List<PaymentModeShop> modeShopList = PaySettingCache.getOthersPaymentModeShops(payScene == PayScene.SCENE_CODE_CHARGE ? PayScene.SCENE_CODE_CHARGE.value() : PayScene.SCENE_CODE_SHOP.value());
        if (modeShopList == null) {
            modeShopList = new ArrayList<>();
            modeShopList.add(createPaymentModeShop(PayModeId.OTHER_WX_PAY, "(自)微信"));
            modeShopList.add(createPaymentModeShop(PayModeId.OTHER_ALI_PAY, "(自)支付宝"));
        }

        List<PayModelItem> listData;
        listData = new ArrayList<PayModelItem>();
        if (modeShopList != null && modeShopList.size() > 0) {
            for (PaymentModeShop modeShop : modeShopList) {
                                if (modeShop.getErpModeId() < 0) {
                    continue;
                }
                PayModelItem item = new PayModelItem(modeShop);
                item.setUsedCount(0);
                item.setUsedValue(null);
                listData.add(item);
            }
        }
        if (isAdded() && listData != null && listData.size() > 0) {
            mGridView.removeAllViews();
            adapter = new OtherPayModelAdapter(getActivity(), listData, mGridView);
            adapter.setHorizontalSize(mHorizontalSize);
            adapter.setCashInfoManager(mPaymentInfo);
                            adapter.setSuportMulti(false);                        adapter.refreshView();
        }
            }

    private PaymentModeShop createPaymentModeShop(PayModeId modeId, String modeIdName) {
        PaymentModeShop paymentModeShop = new PaymentModeShop();
        paymentModeShop.setErpModeId(modeId.value());
        paymentModeShop.setName(modeIdName);
        paymentModeShop.setFaceValue(mPaymentInfo.getTradeVo().getTrade().getTradeAmount());
        return paymentModeShop;
    }

    private OtherPayModelAdapter adapter;


    public void onEventMainThread(ExemptEventUpdate event) {
        if (this.isAdded() && !this.isHidden()) {
                        if (mPaymentInfo.getPayActionPage() == PayActionPage.BALANCE && !event.isExempt()) {
                updateNotPayMent();
            } else {                 mPaymentInfo.getOtherPay().clear();                loadDatas();                updateNotPayMent();
                DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
            }
        }
    }


    public void onEventMainThread(PaymentModeOtherChangeEvent selectEvent) {
        updateNotPayMent();
    }
}

