package com.zhongmei.bty.mobilepay.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.fragment.com.BasePayFragment;
import com.zhongmei.bty.mobilepay.fragment.com.CashEditTextWatcher;
import com.zhongmei.bty.mobilepay.fragment.com.PayView;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.database.entity.local.PosTransLog;

import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.devices.liandipos.NewLDResponse;
import com.zhongmei.bty.basemodule.devices.liandipos.PosConnectManager;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.bty.basemodule.devices.mispos.dialog.UionPayDialogFragment;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;

import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.PayKeyPanel;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;
import com.zhongmei.bty.mobilepay.event.ExemptEventUpdate;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 * 银联支付输入界面
 */

public class UnionFragment extends BasePayFragment implements PosConnectManager.PosConnectListener, View.OnClickListener, PayView {
    private final String TAG = UnionFragment.class.getSimpleName();
    private Context mContext;
    private PayKeyPanel mNumberKeyBorad;
    private PosConnectManager mPosConnectManager;// 银联pos连接管理

    private TextView mUnionPayUnreceived;
    private LinearLayout mInputLayout;
    private RelativeLayout mInput;
    private EditText mCashPayEditValue;
    private ImageView mCashPayDeleteCash;
    private ImageView mPosStatusIcon;
    private TextView mUnionPayErrortext;
    private TextView mCashPayAlerttext;
    private Button mPay;
    private PosTransLog mPosTransLog;

    public static UnionFragment newInstance(IPaymentInfo info, DoPayApi doPayApi) {
        UnionFragment f = new UnionFragment();
        f.setPaymentInfo(info);
        f.setDoPayApi(doPayApi);
        f.setArguments(new Bundle());
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // mPaymentInfo = (IPaymentInfo) getArguments().getSerializable("paymentInfo");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay_union_fragment_new, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignViews(view);
        mCashPayDeleteCash.setOnClickListener(this);
        mCashPayEditValue.setOnClickListener(this);
        mPay.setOnClickListener(this);
        mCashPayEditValue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocus();
                return true;
            }
        });
        mNumberKeyBorad = new PayKeyPanel(view);
        mContext = this.getActivity();
        mPosConnectManager = new PosConnectManager(mContext, this);
        mCashPayEditValue.setText(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()));
        mNumberKeyBorad.setEnabled(true);
        mNumberKeyBorad.setDefaultValue(true);
        if (!mPaymentInfo.isDinner() || mPaymentInfo.isOrderCenter())
            DisplayServiceManager.updateDisplayPay(getActivity(), mPaymentInfo.getActualAmount());
        mNumberKeyBorad.setCurrentInput(mCashPayEditValue);
        mCashPayDeleteCash.setVisibility(View.VISIBLE);
        updateNotPayMent();
        registerEventBus();
        setVewEvents();
        getCurrentPosStatus();// 刷新pos连接情况
    }

    private void assignViews(View view) {
        mUnionPayUnreceived = (TextView) view.findViewById(R.id.union_pay_unreceived);
        mInputLayout = (LinearLayout) view.findViewById(R.id.input_layout);
        mInput = (RelativeLayout) view.findViewById(R.id.input);
        mCashPayEditValue = (EditText) view.findViewById(R.id.cash_pay_edit_value);
        mCashPayAlerttext = (TextView) view.findViewById(R.id.cash_pay_alerttext);
        mCashPayDeleteCash = (ImageView) view.findViewById(R.id.delete_all_text);
        mPosStatusIcon = (ImageView) view.findViewById(R.id.pos_status_icon);
        mUnionPayErrortext = (TextView) view.findViewById(R.id.union_pay_errortext);
        mPay = (Button) view.findViewById(R.id.pay);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            mCashPayEditValue.setText(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()));
            mNumberKeyBorad.setDefaultValue(true);
            mNumberKeyBorad.setEnabled(true);
            DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
        } else {
            //隐藏时清空已输入金额
            clearInputData();
           /* DisplayServiceManager.updateDisplay(getActivity().getApplicationContext(),
                    DisplayServiceManager.buildPayMessage(DisplayUserInfo.COMMAND_CACEL, ""));*/
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.pay) {
            if (!ClickManager.getInstance().isClicked()) {
                if (doPayChecked(false)) {
                    //准备支付
                    preparePay();
                }
            }
        } else if (vId == R.id.cash_pay_edit_value) {
            mNumberKeyBorad.setCurrentInput(mCashPayEditValue);

        } else if (vId == R.id.delete_all_text) {

            mCashPayEditValue.setText("");
        }
    }

    /**
     * 设置编辑框改变事件
     */
    private void setVewEvents() {
        //modify 20180309
        mCashPayEditValue.addTextChangedListener(new CashEditTextWatcher(this, this.mPaymentInfo, mCashPayEditValue, ShopInfoCfg.getInstance().getCurrencySymbol()));

    }

    /**
     * @Description: 刷新pos连接情况
     * @Return void 返回类型
     */
    private void getCurrentPosStatus() {
        if (PaySettingCache.isUnionpay()) {
            updatePosStatusIcon(PosConnectManager.isPosConnected());
        } else {
            mPosStatusIcon.setVisibility(View.GONE);
        }
    }

    private void updatePosStatusIcon(boolean isPosConnected) {
        if (mPosStatusIcon.getVisibility() == View.GONE)
            mPosStatusIcon.setVisibility(View.VISIBLE);
        if (isPosConnected) {
            mPosStatusIcon.setBackground(mContext.getResources().getDrawable(R.drawable.pos_icon_online));
        } else {
            mPosStatusIcon.setBackground(mContext.getResources().getDrawable(R.drawable.pos_icon_offline));
        }
    }

    IPayOverCallback mPayOverCallback = new IPayOverCallback() {

        @Override
        public void onFinished(boolean isOK, int errorCode) {
            try {
                if (isOK) {
                    mNumberKeyBorad.setEnabled(true);
                    mCashPayEditValue.setText("");
                    //  DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());

                } else {
                    mNumberKeyBorad.setEnabled(true);
                    mPay.setEnabled(true);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    };

    @Override
    public void onDestroy() {
        unregisterEventBus();
        if (mPosConnectManager != null) {
            mPosConnectManager.close();
            mPosConnectManager = null;
        }
        super.onDestroy();
    }

    /***
     * 监听抹零事件
     *
     * @param event
     */
    public void onEventMainThread(ExemptEventUpdate event) {
        if (this.isAdded() && !this.isHidden()) {
            mCashPayEditValue.setText(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()));
            mNumberKeyBorad.setDefaultValue(true);
            updateNotPayMent();
        }
    }

    /**
     * @Title: updateNotPayMent
     * @Description: 刷新未支付或找零
     * @Return void 返回类型
     */
    public void updateNotPayMent() {
        double inputvalue = getInputValue();
        double notpayvalue = mPaymentInfo.getActualAmount();
        double value = inputvalue - notpayvalue;
        if (value > 0) {
            mCashPayAlerttext.setVisibility(View.VISIBLE);
            mCashPayAlerttext.setText(String.format(getString(R.string.cash_change_text), CashInfoManager.formatCash(value)));
        } else if (value < 0) {
            mCashPayAlerttext.setVisibility(View.VISIBLE);
            mCashPayAlerttext.setText(getString(R.string.pay_rest_payment_text) + CashInfoManager.formatCash(-value));
        } else {
            mCashPayAlerttext.setVisibility(View.INVISIBLE);
        }
        DoPayUtils.updatePayEnable(getActivity(), mPay, enablePay());
    }

    public void inputMoreAlter() {
        ToastUtil.showLongToastCenter(getActivity(),
                getActivity().getString(R.string.pay_more_input_alter));
    }

    @Override
    public void showClearTB(boolean isShow) {
        if (isShow)
            mCashPayDeleteCash.setVisibility(View.VISIBLE);
        else
            mCashPayDeleteCash.setVisibility(View.INVISIBLE);
    }

    @Override
    public void clearInputData() {
        if (mPaymentInfo != null && mPaymentInfo.getOtherPay() != null)
            mPaymentInfo.getOtherPay().clear();
    }

   /* //add begin 20170424 for 组合支付开关
    private boolean enablePay() {
        if (isSuportGroupPay()) {//如果支持组合支付
            return getInputValue() > 0;
        } else {
            return getInputValue() >= mPaymentInfo.getActualAmount();//不分步支付，输入金额必须大于等于应付金额
        }
    }*/

    //add end 20170424 for 组合支付开关
    @Override
    public void onConnected() {
        getCurrentPosStatus();// 刷新pos连接情况

    }

    @Override
    public void onDisconnected() {
        getCurrentPosStatus(); // 刷新pos连接情况
    }

    //获取输入金额
    public double getInputValue() {
        String inputValueStr = mCashPayEditValue.getText().toString();
        return DoPayUtils.formatInputCash(inputValueStr);
    }

    //点击结账按钮调用
    private void preparePay() {
        UserActionEvent.start(UserActionEvent.DINNER_PAY_SETTLE_UNION);
        if (PaySettingCache.isUnionpay()) {//如果是pos机
            //同一订单不允许相同账号重复支付
            List<PaymentItem> paidItems = mPaymentInfo.getPaidPaymentItems();
            if (!Utils.isEmpty(paidItems)) {
                //PaymentItemDal paymentItemDal = OperatesFactory.create(PaymentItemDal.class);
                for (PaymentItem item : paidItems) {
                    if (PayModeId.POS_CARD.value().equals(item.getPayModeId())) {
                        ToastUtil.showShortToast(R.string.pay_union_pos_pay_repay_alter);
                        return;
                    }
                }
            }
            mNumberKeyBorad.setEnabled(false);
            mPay.setEnabled(false);
            checkUnionPosPay(mPayOverCallback);
        } else {//银联记账模式
            //调用收银接口
            mNumberKeyBorad.setEnabled(false);
            mPay.setEnabled(false);
            this.mPaymentInfo.getOtherPay().clear();
            this.mPaymentInfo.getOtherPay().addPayModelItem(getPayModelItem());
            if (mDoPayApi != null)
                mDoPayApi.doPay(this.getActivity(), this.mPaymentInfo, mPayOverCallback);
        }
    }

    //生成支付信息对象(PayModelItem 将转换成paymentItem)
    private PayModelItem getPayModelItem() {
        PayModelItem item = null;
        if (PaySettingCache.isUnionpay()) {//如果是pos机
            item = new PayModelItem(PayModeId.POS_CARD);
            item.setPosTransLog(this.mPosTransLog);//刷卡日志
        } else {//银联记账模式
            item = new PayModelItem(PayModeId.BANK_CARD);//银行卡记账
        }
        item.setUsedValue(BigDecimal.valueOf(this.getInputValue()));
        return item;
    }

    private boolean isPosUnionPayed(PosTransLog log) {
        boolean isPos = false;
        if (log != null && log.getAmount() != null) {
            Double unionAmount = getInputValue() * 100;
            if (log.getAmount() == unionAmount.intValue()) {
                isPos = true;
            }
        }
        return isPos;
    }

    //验证银联付款
    private void checkUnionPosPay(final IPayOverCallback callback) {
        //如果pos机连接正常
        if (PosConnectManager.isPosConnected()) {
            if (this.mPosTransLog != null && this.isPosUnionPayed(this.mPosTransLog))// 已经有刷卡记录了,直接收银
            {
                //调用收银接口
                this.mPaymentInfo.getOtherPay().clear();
                this.mPaymentInfo.getOtherPay().addPayModelItem(getPayModelItem());
                if (mDoPayApi != null)
                    mDoPayApi.doPay(this.getActivity(), this.mPaymentInfo, mPayOverCallback);
            } else {
                doUnionPosProcess(getInputValue(), this.getActivity(), callback);// 进入银联刷卡
            }
        } else {
            callback.onFinished(false, 0);// 失败回调
            ToastUtil.showLongToastCenter(this.getActivity(), getString(R.string.pay_pos_connection_closed));
        }
    }

    /**
     * @Description: 开启pos 刷卡
     */
    private void doUnionPosProcess(final double unionPayAmount, final FragmentActivity context, final IPayOverCallback callback) {
        // 如果用集成pos机收银
        UionPayDialogFragment uionPayDialogFragment = new UionPayDialogFragment.UionPayDialogFragmentBuilder()
                .closeListener(null).buildPay(BigDecimal.valueOf(unionPayAmount),
                        mPaymentInfo.getTradeVo().getTrade().getTradeNo(),
                        new UionPayDialogFragment.PosOvereCallback() {

                            @Override
                            public void onFinished(UionPayDialogFragment.UionPayStaus status, boolean issuccess, PosTransLog log, NewLDResponse ldResponse) {
                                if (issuccess) {// 扣款成功
                                    // 调用付款方法
                                    mPosTransLog = log;
                                    mPaymentInfo.getOtherPay().clear();
                                    mPaymentInfo.getOtherPay().addPayModelItem(getPayModelItem());
                                    if (mDoPayApi != null)
                                        mDoPayApi.doPay(getActivity(), mPaymentInfo, mPayOverCallback);

                                } else {// 扣款失败
                                    callback.onFinished(false, 0);
                                }
                            }
                        });
        uionPayDialogFragment.show(context.getSupportFragmentManager(), "union");
    }
}
