package com.zhongmei.bty.mobilepay.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.fragment.com.BasePayFragment;
import com.zhongmei.bty.mobilepay.fragment.com.CashEditTextWatcher;
import com.zhongmei.bty.mobilepay.fragment.com.PayView;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.util.UserActionCode;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.commonmodule.view.NumberInputdialog;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;
import com.zhongmei.bty.mobilepay.event.ExemptEventUpdate;
import com.zhongmei.bty.mobilepay.intelligent.IntelligentCash;
import com.zhongmei.bty.mobilepay.intelligent.Money;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.basemodule.commonbusiness.adapter.BaseListAdapter;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.PayKeyPanel;

import java.math.BigDecimal;
import java.util.ArrayList;




public class CashPayFragment extends BasePayFragment implements View.OnClickListener, PayView {
    private final String TAG = CashPayFragment.class.getSimpleName();
    private PayKeyPanel mNumberKeyBorad;
    private ArrayList<Money> IntelligentCashList;
    private int MoneyListSize;
    private double userChanges;
    private TextView mCashPayUnreceived;
    private LinearLayout mInputLayout;
    private TextView mCashPayAlerttext;
    private TextView tvOverIncoume;
    private RelativeLayout rlDiffChangesHint;
    private RelativeLayout mInput;
    private EditText mCashPayEditValue;
    private ImageView mCashPayDeleteCash;
    private GridView mGridview;
    private Button btnModifyPayment;
    private Button mPay;

    public static CashPayFragment newInstance(IPaymentInfo info, DoPayApi doPayApi) {
        CashPayFragment f = new CashPayFragment();
        f.setPaymentInfo(info);
        f.setDoPayApi(doPayApi);
                        f.setArguments(new Bundle());
        return f;
    }

    private void assignViews(View view) {
        mCashPayUnreceived = (TextView) view.findViewById(R.id.cash_pay_unreceived);
        mInputLayout = (LinearLayout) view.findViewById(R.id.input_layout);
        mCashPayAlerttext = (TextView) view.findViewById(R.id.cash_pay_alerttext);
        btnModifyPayment = (Button) view.findViewById(R.id.modify_payment_btn);
        tvOverIncoume = (TextView) view.findViewById(R.id.over_income_tv);
        rlDiffChangesHint = (RelativeLayout) view.findViewById(R.id.rl_diff_changes_hint);
        mInput = (RelativeLayout) view.findViewById(R.id.input);
        mCashPayEditValue = (EditText) view.findViewById(R.id.cash_pay_edit_value);
        mCashPayDeleteCash = (ImageView) view.findViewById(R.id.cash_pay_delete_cash);
        mGridview = (GridView) view.findViewById(R.id.gridview);
        mPay = (Button) view.findViewById(R.id.pay);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay_cash_fragment_new, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignViews(view);
        mPay.setOnClickListener(this);
        mCashPayEditValue.setOnClickListener(this);
        mCashPayDeleteCash.setOnClickListener(this);
        btnModifyPayment.setOnClickListener(this);
        mCashPayEditValue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocus();
                return true;
            }
        });
        if (mPaymentInfo != null) {
            mNumberKeyBorad = new PayKeyPanel(view);
            initIntelligentCash();            mCashPayEditValue.setText(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()));
            mNumberKeyBorad.setDefaultValue(true);
            mNumberKeyBorad.setEnabled(true);
            if (!mPaymentInfo.isDinner() || mPaymentInfo.isOrderCenter())
                DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
            mNumberKeyBorad.setCurrentInput(mCashPayEditValue);
            mCashPayDeleteCash.setVisibility(View.VISIBLE);
            updateNotPayMent();
            this.registerEventBus();
            setViewEvents();
                        if (mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT || mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) {
                btnModifyPayment.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            mCashPayEditValue.setText(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()));
            mNumberKeyBorad.setDefaultValue(true);
            mNumberKeyBorad.setEnabled(true);
            DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
            updateNotPayMent();
            initIntelligentCash();        } else {
                        clearInputData();

        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.pay) {
            if (!ClickManager.getInstance().isClicked()) {
                try {
                    MobclickAgentEvent.onEvent(UserActionCode.ZC030025);
                    if (doPayChecked(true)) {
                        UserActionEvent.start(UserActionEvent.DINNER_PAY_SETTLE_CASH);
                        mNumberKeyBorad.setEnabled(false);
                        mPay.setEnabled(false);
                        this.mPaymentInfo.getOtherPay().clear();
                        this.mPaymentInfo.getOtherPay().addPayModelItem(getPayModelItem());
                        if (mDoPayApi != null)
                            mDoPayApi.doPay(this.getActivity(), this.mPaymentInfo, mPayOverCallback);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }

        } else if (vId == R.id.cash_pay_edit_value) {
            mNumberKeyBorad.setCurrentInput(mCashPayEditValue);
        } else if (vId == R.id.cash_pay_delete_cash) {
            mCashPayEditValue.setText("");
        } else if (vId == R.id.modify_payment_btn) {
                        if (!ClickManager.getInstance().isClicked()) {
                showNumberInputDialog();
            }
        }
    }


    private void showNumberInputDialog() {
        double inputvalue = getInputValue();
        double notpayvalue = mPaymentInfo.getActualAmount();
        double maxValue = CashInfoManager.floatSubtract(inputvalue, notpayvalue);
        String defaultInput = null;
        NumberInputdialog numberDialog =
                new NumberInputdialog(this.getActivity(), getString(R.string.title_modify_change_amount), getString(R.string.hint_input_modify_change_amount), defaultInput, maxValue, mInputOverListener);
        numberDialog.setNumberType(NumberInputdialog.NUMBER_TYPE_FLOAT).setDotType(NumberInputdialog.DotType.DOT).setRemark(String.format(getString(R.string.pay_should_changes), CashInfoManager.formatCash(maxValue))).show();
    }


    NumberInputdialog.InputOverListener mInputOverListener = new NumberInputdialog.InputOverListener() {
        @Override
        public void afterInputOver(String inputContent) {            if (inputContent != null) {
                double inputvalue = getInputValue();                double notpayvalue = mPaymentInfo.getActualAmount();                userChanges = DoPayUtils.formatInputCash(inputContent);
                double overIncoume = CashInfoManager.floatSubtract(CashInfoManager.floatSubtract(inputvalue, notpayvalue), userChanges);                mCashPayAlerttext.setText(String.format(getString(R.string.cash_change_text), CashInfoManager.formatCash(userChanges)));
                if (overIncoume > 0) {
                    tvOverIncoume.setVisibility(View.VISIBLE);
                    tvOverIncoume.setText(String.format(getString(R.string.more_pay_cash_text), CashInfoManager.formatCash(overIncoume)));
                } else {
                    tvOverIncoume.setVisibility(View.GONE);
                }
            }
        }
    };

    public boolean enablePay() {
        if (mPaymentInfo.getActualAmount() <= 0) {
                        if (mPaymentInfo.getTradeBusinessType() == BusinessType.BUFFET && mPaymentInfo.getTradeVo().getTrade().getTradePayStatus() == TradePayStatus.PREPAID) {
                return false;
            }
            return true;
        } else {

            return super.enablePay();
        }
    }

        public double getInputValue() {
        String inputValueStr = mCashPayEditValue.getText().toString();
        return DoPayUtils.formatInputCash(inputValueStr);
    }

        private PayModelItem getPayModelItem() {
        PayModelItem item = new PayModelItem(PayModeId.CASH);        double faceValue = getInputValue();
                if (userChanges > 0)        {
            item.setChangeAmount(BigDecimal.valueOf(userChanges));
        } else {
            item.setChangeAmount(BigDecimal.ZERO);
        }
        item.setUsedValue(BigDecimal.valueOf(faceValue));
        return item;
    }

    IPayOverCallback mPayOverCallback = new IPayOverCallback() {

        @Override
        public void onFinished(boolean isOK, int statusCode) {
            try {
                if (isOK) {
                    mNumberKeyBorad.setEnabled(true);
                    mCashPayEditValue.setText("");
                    initIntelligentCash();                                    } else {
                    mNumberKeyBorad.setEnabled(true);
                    mPay.setEnabled(true);
                    DoPayUtils.updatePayEnable(getActivity(), mPay, enablePay());
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    };


    private void initIntelligentCash() {

        double cashNotPayMent = mPaymentInfo.getActualAmount();

        if (cashNotPayMent >= 0) {
            IntelligentCashList = IntelligentCash.getPossibleMoney(BigDecimal.valueOf(cashNotPayMent));
        } else {
            IntelligentCashList = IntelligentCash.getPossibleMoney(BigDecimal.ZERO);
        }

        if (mPaymentInfo.getActualAmount() <= 0 && IntelligentCashList != null) {
            IntelligentCashList.clear();
        }
        if (IntelligentCashList != null)
            MoneyListSize = IntelligentCashList.size();

        final BaseListAdapter<Holder, Money> adapter =
                new BaseListAdapter<Holder, Money>(R.layout.pay_intelligent_cash_gride_item, IntelligentCashList) {

                    @Override
                    public Holder initViewHodler(View convertView) {
                        Holder holder = new Holder();
                        holder.tv = (TextView) convertView.findViewById(R.id.cash_intelligent);
                        return holder;
                    }

                    @Override
                    public void setViewHodler(Holder viewHolder, int position) {
                        if (getItem(position).getType() == 1) {
                            viewHolder.tv.setText(getString(R.string.other_cash));
                        } else {
                            viewHolder.tv.setText(ShopInfoCfg.getInstance().getCurrencySymbol() + getItem(position).getValue());
                        }
                    }
                };
        if (MoneyListSize <= 4) {
            mGridview.setNumColumns(MoneyListSize);
        } else {
            mGridview.setNumColumns(4);
        }
        mGridview.setAdapter(adapter);

        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCashPayEditValue.getText().clear();
                mCashPayEditValue.setText(ShopInfoCfg.formatCurrencySymbol(
                        IntelligentCashList.get(position).getValue() + ""));
                mNumberKeyBorad.setDefaultValue(true);
            }
        });
    }

    private static class Holder {
        TextView tv;

    }


    private void setViewEvents() {
                mCashPayEditValue.addTextChangedListener(new CashEditTextWatcher(this, this.mPaymentInfo, mCashPayEditValue, ShopInfoCfg.getInstance().getCurrencySymbol()).setSuportPayMore(true));

    }



    public void updateNotPayMent() {
                double inputvalue = getInputValue();
        double notpayvalue = mPaymentInfo.getActualAmount();
        double value = CashInfoManager.floatSubtract(inputvalue, notpayvalue);
        userChanges = value;
        if (value > 0) {
            rlDiffChangesHint.setVisibility(View.VISIBLE);
            if (mPaymentInfo.getPayScene() != PayScene.SCENE_CODE_BUFFET_DEPOSIT && mPaymentInfo.getPayScene() != PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) {
                btnModifyPayment.setVisibility(View.VISIBLE);
            }
            mCashPayAlerttext.setText(String.format(getString(R.string.cash_change_text), CashInfoManager.formatCash(value)));
            mCashPayAlerttext.getPaint().setFakeBoldText(true);
            mCashPayAlerttext.setTextColor(getResources().getColor(R.color.print_font_color));
        } else if (value < 0) {
            rlDiffChangesHint.setVisibility(View.VISIBLE);
            btnModifyPayment.setVisibility(View.GONE);
            mCashPayAlerttext.setText(getString(R.string.pay_rest_payment_text) + CashInfoManager.formatCash(-value));
            mCashPayAlerttext.getPaint().setFakeBoldText(false);
            mCashPayAlerttext.setTextColor(getResources().getColor(R.color.print_text_black));
        } else {
            rlDiffChangesHint.setVisibility(View.INVISIBLE);
        }
        tvOverIncoume.setVisibility(View.GONE);
        DoPayUtils.updatePayEnable(getActivity(), mPay, enablePay());
    }


    public void inputMoreAlter() {
        double inputVale = getInputValue();
        if (inputVale > 99999999) {
            ToastUtil.showLongToastCenter(getActivity(),
                    getActivity().getString(R.string.pay_more_limit));
        } else if (inputVale > 0) {
            ToastUtil.showLongToastCenter(getActivity(),
                    getActivity().getString(R.string.inputTwoADecimal));
        }
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



    public void onEventMainThread(ExemptEventUpdate event) {
        if (this.isAdded() && !this.isHidden()) {
            mCashPayEditValue.setText(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()));
            mNumberKeyBorad.setDefaultValue(true);
            updateNotPayMent();
            initIntelligentCash();        }
    }

    @Override
    public void onDestroy() {
        this.unregisterEventBus();
        super.onDestroy();
    }
}
