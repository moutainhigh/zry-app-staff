package com.zhongmei.bty.mobilepay.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.fragment.com.BasePayFragment;
import com.zhongmei.bty.mobilepay.fragment.com.CashEditTextWatcher;
import com.zhongmei.bty.mobilepay.fragment.com.PayView;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.commonbusiness.utils.BusinessTypeUtils;
import com.zhongmei.bty.basemodule.devices.handset.HandsetDialogFragment;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.bty.mobilepay.event.ExemptEventUpdate;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.trade.message.FHQueryBalanceReq;
import com.zhongmei.bty.basemodule.trade.message.FHQueryBalanceResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.data.operate.OperatesRetailFactory;
import com.zhongmei.yunfu.resp.data.GatewayTransferResp;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.bty.mobilepay.IPayConstParame;
import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.bty.mobilepay.PayKeyPanel;
import com.zhongmei.bty.mobilepay.dialog.IPQRCodeDialog;

import java.math.BigDecimal;

import de.greenrobot.event.EventBus;

/**
 * Created by demo on 2018/12/15
 */

public class FengHuoPayFragment extends BasePayFragment implements View.OnClickListener, PayView, IPayConstParame {

    private final String TAG = FengHuoPayFragment.class.getSimpleName();

    private PayKeyPanel mNumberKeyBorad;
    private PayModeId mCurrentPayModeId = PayModeId.FENGHUO_WRISTBAND;// 当前支付方式Id
    private TextView mCashPayAlerttext;
    private EditText mCashPayEditValue;
    private ImageView mCashPayDeleteCash;
    private RelativeLayout mIPRL;
    private String mWristBandNo;//add 20180319
    private Button mPay;

    private void assignViews(View view) {
        mCashPayAlerttext = (TextView) view.findViewById(R.id.cash_pay_alerttext);
        mCashPayEditValue = (EditText) view.findViewById(R.id.cash_pay_edit_value);
        mCashPayDeleteCash = (ImageView) view.findViewById(R.id.delete_all_text);
        mPay = (Button) view.findViewById(R.id.pay);
        mIPRL = (RelativeLayout) view.findViewById(R.id.pay_fenghuo_ip_rlt);
        mPay.setOnClickListener(this);
        mCashPayEditValue.setOnClickListener(this);
        mCashPayDeleteCash.setOnClickListener(this);
        mIPRL.setOnClickListener(this);
        mCashPayEditValue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocus();
                return true;
            }
        });
    }

    public static FengHuoPayFragment newInstance(IPaymentInfo paymentInfo, DoPayApi doPayApi) {
        FengHuoPayFragment f = new FengHuoPayFragment();
        f.setPaymentInfo(paymentInfo);
        f.setDoPayApi(doPayApi);
        Bundle bundle = new Bundle();
      /*  bundle.putSerializable("paymentInfo", paymentInfo);
        bundle.putSerializable("dopayapi", doPayApi);*/
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  mPaymentInfo = (IPaymentInfo) getArguments().getSerializable("paymentInfo");
        mDoPayApi = (DoPayApi) getArguments().getSerializable("dopayapi");*/
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay_fenghuo_fragment_layout, container, false);
        assignViews(view);
        if (mPaymentInfo != null) {
            mNumberKeyBorad = new PayKeyPanel(view);
            mCashPayEditValue.setText(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()));
            mNumberKeyBorad.setDefaultValue(true);
            mNumberKeyBorad.setEnabled(true);
            mNumberKeyBorad.setCurrentInput(mCashPayEditValue);
            mCashPayDeleteCash.setVisibility(View.VISIBLE);
            updateNotPayMent();
            EventBus.getDefault().register(this);
            setVewEvents();
        }
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            mCashPayEditValue.setText(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()));
            updateNotPayMent();
        } else {
            //隐藏时清空已输入金额
            clearInputData();
        }
        super.onHiddenChanged(hidden);
    }

    //add begin 20170424 for 组合支付开关
    /*private boolean enablePay() {
        if (isSuportGroupPay()) {//如果支持组合支付
            return getInputValue() > 0;
        } else {
            return getInputValue() >= mPaymentInfo.getActualAmount();//不分步支付，输入金额必须大于等于应付金额
        }
    }*/

    //add end 20170424 for 组合支付开关
    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.pay) {
            if (!ClickManager.getInstance().isClicked()) {
                if (doPayChecked(false)) {
                    verify();
                }
            }
        } else if (vId == R.id.cash_pay_edit_value) {
            mNumberKeyBorad.setCurrentInput(mCashPayEditValue);
        } else if (vId == R.id.delete_all_text) {
            mCashPayEditValue.setText("");
        } else if (vId == R.id.pay_fenghuo_ip_rlt) {
            if (!ClickManager.getInstance().isClicked()) {
                IPQRCodeDialog.start(this.getActivity());
            }
        }
    }

    private void verify() {
        if (mPaymentInfo.isDinner()) {
            VerifyHelper.verifyAlert(getActivity(), DinnerApplication.PERMISSION_DINNER_CASH,
                    new VerifyHelper.Callback() {
                        @Override
                        public void onPositive(User user, String code, Auth.Filter filter) {
                            super.onPositive(user, code, filter);
                            showHandSetInputView(false);
                        }
                    });
        } else {
            showHandSetInputView(false);
        }
    }


    private void doPay() {
        if (!TextUtils.isEmpty(mWristBandNo)) {
            PayModelItem payModelItem = new PayModelItem(mCurrentPayModeId);
            payModelItem.setDeviceId(mWristBandNo);
            payModelItem.setUsedValue(BigDecimal.valueOf(getInputValue()));
            mNumberKeyBorad.setEnabled(false);
            mPay.setEnabled(false);
            clearInputData();
            this.mPaymentInfo.getOtherPay().addPayModelItem(payModelItem);
            this.mDoPayApi.doPay(this.getActivity(), this.mPaymentInfo, mPayOverCallback);
        }
    }

    IPayOverCallback mPayOverCallback = new IPayOverCallback() {

        @Override
        public void onFinished(boolean isOK, int statusCode) {
            try {
                if (isOK) {
                    mNumberKeyBorad.setEnabled(true);
                    mCashPayEditValue.setText("");

                } else {
                    mNumberKeyBorad.setEnabled(true);
                    DoPayUtils.updatePayEnable(getActivity(), mPay, enablePay());

                    switch (statusCode) {

                        case GATWAY_CODE_BALANCE_NOT_ENOUGH://余额不足
                            queryBalance(mWristBandNo);
                            break;

                        case GATWAY_CODE_PASSWORD_EMPTY://密码为空
                            alterPasswordDialog(statusCode);
                            break;

                        case GATWAY_CODE_PASSWORD_ERROR:
                            alterPasswordDialog(statusCode);
                            break;

                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    };

    /**
     * 设置编辑框改变事件
     */
    private void setVewEvents() {
        mCashPayEditValue.addTextChangedListener(new CashEditTextWatcher(this, this.mPaymentInfo, mCashPayEditValue, ShopInfoCfg.getInstance().getCurrencySymbol()));
    }

    /**
     * @Title: updateNotPayMent
     * @Description: 刷新未支付或找零
     * @Return void 返回类型
     */
    public void updateNotPayMent() {
        double notpayvalue = mPaymentInfo.getActualAmount();
        String input = mCashPayEditValue.getText().toString();
        if (!TextUtils.isEmpty(input)) {
            if (input.startsWith(".")) {
                input = "0" + input;
            }
            String valueStr = input.replace(ShopInfoCfg.getInstance().getCurrencySymbol(), "").trim();
            double inputvalue = Double.valueOf(valueStr);
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
        } else {
            mCashPayAlerttext.setVisibility(View.VISIBLE);
            mCashPayAlerttext.setText(getString(R.string.pay_rest_payment_text) + CashInfoManager.formatCash(notpayvalue));
            DoPayUtils.updatePayEnable(getActivity(), mPay, false);
        }
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

    /***
     * 监听抹零事件
     *
     * @param event
     */
    public void onEventMainThread(ExemptEventUpdate event) {
        mCashPayEditValue.setText(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()));
        mNumberKeyBorad.setDefaultValue(true);
        updateNotPayMent();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    //获取输入金额
    public double getInputValue() {
        String inputValueStr = mCashPayEditValue.getText().toString();
        return DoPayUtils.formatInputCash(inputValueStr);
    }

    //查询余额
    private void queryBalance(String wristBandNo) {
        FHQueryBalanceReq req = new FHQueryBalanceReq();
        req.braceletDeviceIdenty = wristBandNo;
        req.deviceIdenty = ShopInfoCfg.getInstance().deviceID;
        req.shopId = TextUtils.isEmpty(ShopInfoCfg.getInstance().shopId) ? 0 : Long.parseLong(ShopInfoCfg.getInstance().shopId);

        TradeOperates tradeOperates = BusinessTypeUtils.isRetail() ? OperatesRetailFactory.create(TradeOperates.class) : OperatesFactory.create(TradeOperates.class);
        tradeOperates.queryBalanceFH(req, LoadingResponseListener.ensure(new ResponseListener<GatewayTransferResp<FHQueryBalanceResp>>() {
            @Override
            public void onResponse(ResponseObject<GatewayTransferResp<FHQueryBalanceResp>> response) {
                if (ResponseObject.isOk(response)) {
                    showBalanceNotEnoughDialog(response.getContent().getResult().balance);
                } else {
                    showBalanceNotEnoughDialog(null);
                }
            }

            @Override
            public void onError(VolleyError error) {
                showBalanceNotEnoughDialog(null);
            }
        }, getChildFragmentManager()));
    }


    private void showBalanceNotEnoughDialog(BigDecimal balance) {
        String title = getString(R.string.pay_dict_not_enough);
        if (balance != null) {
            title += "\n(" + getString(R.string.pay_current_blance_text) + ":" + CashInfoManager.formatCash(balance.doubleValue()) + ")";
        }
        new CommonDialogFragment.CommonDialogFragmentBuilder(this.getActivity())
                .title(title)
                .iconType(CommonDialogFragment.ICON_ERROR)
                .positiveText(R.string.invoice_btn_ok)
                .positiveLinstner(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                    }
                })
                .build()
                .show(getChildFragmentManager(), "BalanceNotEnough");
    }

    private void showHandSetInputView(boolean isInputPassword) {
        HandsetDialogFragment dialogFragment = new HandsetDialogFragment();
        if (isInputPassword) {
            dialogFragment.setArguments(dialogFragment.passwordLaunch(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()), mWristBandNo));
        } else {
            dialogFragment.setArguments(dialogFragment.readIdLaunch(CashInfoManager.formatCash(mPaymentInfo.getActualAmount())));
        }

        dialogFragment.setHandsetCallBack(new HandsetDialogFragment.HandsetCallBack() {
            @Override
            public void onBraceletInfoCallBack(String braceletId) {
                if (!TextUtils.isEmpty(braceletId)) {
                    mWristBandNo = braceletId;
                    doPay();
                }
            }

            @Override
            public void onPasswordCallBack(String braceletId, String password) {
                if (!TextUtils.isEmpty(password)) {
                    mPaymentInfo.setMemberPassword(password);
                    doPay();
                }
            }
        });
        dialogFragment.show(getChildFragmentManager(), "HandsetDialogFragment");

    }

    private void alterPasswordDialog(int errorCode) {
        String title = null;
        if (errorCode == GATWAY_CODE_PASSWORD_EMPTY) {
            title = getString(R.string.pay_password_empty_input_please);
        } else {
            title = getString(R.string.pay_password_error_input_again);
        }
        new CommonDialogFragment.CommonDialogFragmentBuilder(this.getActivity())
                .title(title)
                .iconType(CommonDialogFragment.ICON_ERROR)
                .positiveText(R.string.invoice_btn_ok)
                .negativeText(R.string.invoice_btn_cancel)
                .positiveLinstner(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        //弹出密码框
                        showHandSetInputView(true);
                    }
                })
                .build()
                .show(getChildFragmentManager(), "BalanceNotEnough");

    }
}