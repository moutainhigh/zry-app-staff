package com.zhongmei.bty.mobilepay.fragment;

/**
 * Created by demo on 2018/12/15
 */


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


    private int mHorizontalSize = 3;//水平列数


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
        //隐藏时清空已输入金额
        mPaymentInfo.getOtherPay().clear();
        if (!hidden) {
            loadDatas();//重新加载view对象
            updateNotPayMent();
            DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
        } else {
           /* DisplayServiceManager.updateDisplay(getActivity().getApplicationContext(),
                    DisplayServiceManager.buildPayMessage(DisplayUserInfo.COMMAND_CACEL, ""));*/
        }
        super.onHiddenChanged(hidden);
    }

    /**
     * @Title: updateNotPayMent
     * @Description: 刷新未支付或找零、溢收
     * @Return void 返回类型
     */
    private void updateNotPayMent() {
        if (mPaymentInfo != null && mPaymentInfo.getOtherPay() != null && mPaymentInfo.getOtherPay().getGroupAmount() > 0) {
            double restAmout = mPaymentInfo.getNotPayMent();
            if (restAmout == 0) {
                mPayAlterTV.setVisibility(View.GONE);
            } else {
                //还剩
                if (restAmout > 0) {
                    mPayAlterTV.setText(this.getActivity().getString(R.string.pay_rest_payment_text) + CashInfoManager.formatCash(restAmout));
                } else {
                    //溢收
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

    //获取用户输入金额
    public double getInputValue() {
        return this.mPaymentInfo.getOtherPay().getGroupAmount();
    }
  /*  public boolean enablePay() {
        if (isSuportGroupPay()) {//如果支持组合支付
            return this.mPaymentInfo.getOtherPay().getGroupAmount() > 0 ? true : false;
        } else {
            return this.mPaymentInfo.getOtherPay().getGroupAmount() >= this.mPaymentInfo.getActualAmount();//不分步支付，输入金额必须大于等于应付金额
        }
    }*/

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

                    //如果有溢收
                    if (restAmout < 0) {
                        List<PayModelItem> payModelItemList = mPaymentInfo.getOtherPay().getAllPayModelItems();
                        if (payModelItemList != null) {
                            for (PayModelItem payModelItem : payModelItemList) {
                                //add v8.16 自定义输入不支持溢收
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
                    //清空已输入金额
                    // mPaymentInfo.getOtherPay().clear();
                    loadDatas();//重新加载view对象
                    // updateNotPayMent();
                    DoPayUtils.updatePayEnable(getActivity(), mPay, enablePay());
                } else {
                    mPay.setEnabled(true);
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    };

    /**
     * 加载数据
     */
    public void loadDatas() {
        //缓存里面获取其它支付方式
        if (mPaymentInfo == null) return;
        PayScene payScene = mPaymentInfo.getPayScene();
        List<PaymentModeShop> modeShopList = PaySettingCache.getOthersPaymentModeShops(payScene == PayScene.SCENE_CODE_CHARGE ? PayScene.SCENE_CODE_CHARGE.value() : PayScene.SCENE_CODE_SHOP.value());
        if (modeShopList == null) {
            modeShopList = new ArrayList<>();
            modeShopList.add(createPaymentModeShop(PayModeId.OTHER_WX_PAY, "微信支付"));
            modeShopList.add(createPaymentModeShop(PayModeId.OTHER_ALI_PAY, "支付宝支付"));
        }

        List<PayModelItem> listData;
        listData = new ArrayList<PayModelItem>();
        if (modeShopList != null && modeShopList.size() > 0) {
            for (PaymentModeShop modeShop : modeShopList) {
                //小于零的其它支付是系统默认的，暂不展示,用户自定义的都大于0
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
            //if (mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT || mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_WRITEOFF || mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_BOOKING_DEPOSIT) {//如果押金、销账不支持组合
                adapter.setSuportMulti(false);//
            //}
            adapter.refreshView();
        }
        //modify 20170110
    }

    private PaymentModeShop createPaymentModeShop(PayModeId modeId, String modeIdName) {
        PaymentModeShop paymentModeShop = new PaymentModeShop();
        paymentModeShop.setErpModeId(modeId.value());
        paymentModeShop.setName(modeIdName);
        paymentModeShop.setFaceValue(mPaymentInfo.getTradeVo().getTrade().getTradeAmount());
        return paymentModeShop;
    }

    private OtherPayModelAdapter adapter;

    /***
     * 监听抹零事件,抹零后清空所有输入金额
     *
     * @param event
     */
    public void onEventMainThread(ExemptEventUpdate event) {
        if (this.isAdded() && !this.isHidden()) {
            //add v8.16 解决bug start 如果是正餐结算，订单金额变化不清空输入金额,抹零操作会清空
            if (mPaymentInfo.getPayActionPage() == PayActionPage.BALANCE && !event.isExempt()) {
                updateNotPayMent();
            } else { //add v8.16  解决bug end
                mPaymentInfo.getOtherPay().clear();//清空已输入金额
                loadDatas();//重新加载view对象
                updateNotPayMent();
                DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
            }
        }
    }

    /**
     * 刷新
     *
     * @param selectEvent
     */
    public void onEventMainThread(PaymentModeOtherChangeEvent selectEvent) {
        updateNotPayMent();
    }
}

