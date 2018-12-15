package com.zhongmei.bty.mobilepay.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.event.ExemptEventUpdate;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.bty.commonmodule.database.entity.TradeEarnestMoney;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;

import java.math.BigDecimal;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by demo on 2018/12/15
 * 预定金抵扣退款
 */

public class BookingDeductionRefundDialog extends BasicDialogFragment implements View.OnClickListener {

    public static final String TAG = BookingDeductionRefundDialog.class.getSimpleName();

    public static final int OP_TYPE_DEDUCTION_AND_PAY = 1;

    public static final int OP_TYPE_DEDUCTION_AND_REFUND = 2;

    protected ImageButton mIbClose;

    protected TextView mTvTitle;

    protected TextView mTvTitleContent;

    protected LinearLayout mLLPrepaymentInfo;

    protected TextView mTvDeductionInfoTitle;

    protected TextView mTvTradeInfo;

    protected TextView mTvDeductionInfo;

    protected TextView mTvResidueTitle;

    protected TextView mTvResidueInfo;

    protected Button mBtnSubmit;

    private IPaymentInfo mPaymentInfo;

    private DoPayApi mDoPayApi;

    private int opType = OP_TYPE_DEDUCTION_AND_PAY;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_LAYOUT_FLAGS | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        Window window = dialog.getWindow();
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        return dialog;
    }

    public static BookingDeductionRefundDialog start(FragmentManager manager, IPaymentInfo info, DoPayApi doPayApi, int opType) {
        BookingDeductionRefundDialog deductionDialog = new BookingDeductionRefundDialog();
        deductionDialog.mPaymentInfo = info;
        deductionDialog.mDoPayApi = doPayApi;
        deductionDialog.opType = opType;
        deductionDialog.show(manager, TAG);
        return deductionDialog;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.booking_prepayment_deduction_dialog_layout, container, false);
        bindView(view);
        switchView();
        initView();
        return view;
    }

    /**
     * 绑定控件
     */
    private void bindView(View view) {
        /*if (opType == OP_TYPE_DEDUCTION_AND_REFUND) {
            PrePayMessage payMessage = createPrePayMessage();
            DisplayServiceManager.updateDisplay(getContext(), payMessage);
        }*/
        mIbClose = (ImageButton) view.findViewById(R.id.back);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvTitleContent = (TextView) view.findViewById(R.id.tv_title_content);
        mLLPrepaymentInfo = (LinearLayout) view.findViewById(R.id.ll_prepayment_info);
        mTvDeductionInfoTitle = (TextView) view.findViewById(R.id.tv_deduction_info_title);
        mTvTradeInfo = (TextView) view.findViewById(R.id.tv_trade_info);
        mTvDeductionInfo = (TextView) view.findViewById(R.id.tv_deduction_info);
        mTvResidueTitle = (TextView) view.findViewById(R.id.tv_residue_title);
        mTvResidueInfo = (TextView) view.findViewById(R.id.tv_residue_info);
        mBtnSubmit = (Button) view.findViewById(R.id.btn_ok);
        mIbClose.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
    }

    /*private PrePayMessage createPrePayMessage() {
        PrePayMessage payMessage = new PrePayMessage();
        payMessage.setPayType(PrePayMessage.PAY_TYPE_NONE);
        TradeVo tradeVo = mPaymentInfo.getTradeVo();
        double restAmount = CashInfoManager.floatSubtract(tradeVo.getTradeEarnestMoney(), mPaymentInfo.getActualAmount());
        payMessage.setLabel(getString(R.string.handover_cash));
        payMessage.setMessage(CashInfoManager.formatCash(restAmount));
        payMessage.setPrePayPrice(CashInfoManager.formatCash(tradeVo.getTradeEarnestMoney()));
        payMessage.setDiscountPrice(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()));
        payMessage.setRefundPrice(CashInfoManager.formatCash(restAmount));
        return payMessage;
    }*/

    /**
     * 选择UI
     */
    private void switchView() {
        if (opType == OP_TYPE_DEDUCTION_AND_REFUND) {
            mTvTitle.setText(R.string.prepayment_deduction_and_refunt);
            mTvTitleContent.setText(R.string.prepayment_refunt_type_cash);
            mTvDeductionInfoTitle.setText(R.string.refunt_prepayment_title);
            mTvResidueTitle.setText(R.string.refunt_prepayment_hint);
            mBtnSubmit.setText(R.string.prepayment_deduction_and_refunt);
        } else if (opType == OP_TYPE_DEDUCTION_AND_PAY) {
            mTvTitle.setText(R.string.prepayment_deduction_and_pay);
            mTvTitleContent.setText(R.string.prepayment_deduction_and_pay_hint);
            mTvDeductionInfoTitle.setText(R.string.unpaid_retainage);
            mTvResidueTitle.setText(R.string.unpaid_retainage_hint);
            mBtnSubmit.setText(R.string.prepayment_deduction_and_pay);
        }
    }

    /**
     * 初始化数据
     */
    private void initView() {
        TradeVo tradeVo = mPaymentInfo.getTradeVo();
        mTvTradeInfo.setText(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()));//订单未收金额
        if (opType == OP_TYPE_DEDUCTION_AND_PAY) {
            mTvDeductionInfo.setText(CashInfoManager.formatCash(tradeVo.getTradeEarnestMoney()));//抵扣金额
            double restAmount = CashInfoManager.floatSubtract(mPaymentInfo.getActualAmount(), tradeVo.getTradeEarnestMoney());
            mTvResidueInfo.setText(CashInfoManager.formatCash(restAmount));//尾款金额或者退款金额
        } else {
            mTvDeductionInfo.setText(CashInfoManager.formatCash(mPaymentInfo.getActualAmount()));//抵扣金额
            double restAmount = CashInfoManager.floatSubtract(tradeVo.getTradeEarnestMoney(), mPaymentInfo.getActualAmount());
            mTvResidueInfo.setText(CashInfoManager.formatCash(restAmount));//尾款金额或者退款金额
        }
        initPrepaymentView(tradeVo.getTradeEarnestMoneys());
    }

    private void initPrepaymentView(List<TradeEarnestMoney> earnestMoneyList) {
        LinearLayout linearLayout;
        TextView prepaymentPayModeTV;
        TextView prepaymentPayMoneyTV;
        if (Utils.isNotEmpty(earnestMoneyList)) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(0, DensityUtil.dip2px(getContext(), 13), 0, 0);
            for (TradeEarnestMoney earnestMoney : earnestMoneyList) {
                linearLayout = new LinearLayout(getContext());
                linearLayout.setLayoutParams(params);
                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                prepaymentPayModeTV = new TextView(getContext());
                prepaymentPayModeTV.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
                prepaymentPayModeTV.setTextColor(Color.parseColor("#999999"));
                prepaymentPayModeTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                prepaymentPayMoneyTV = new TextView(getContext());
                prepaymentPayMoneyTV.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                prepaymentPayMoneyTV.setTextColor(Color.parseColor("#999999"));
                prepaymentPayMoneyTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                linearLayout.addView(prepaymentPayModeTV);
                linearLayout.addView(prepaymentPayMoneyTV);
                mLLPrepaymentInfo.addView(linearLayout);
                prepaymentPayModeTV.setText(PaySettingCache.getPayModeNameByModeId(earnestMoney.getPayModeId()));
                prepaymentPayMoneyTV.setText(CashInfoManager.formatCash(earnestMoney.getEarnestMoney().doubleValue()));
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (ClickManager.getInstance().isClicked()) return;
        int i = v.getId();
        if (i == R.id.back) {
            dismiss();
            DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
        } else if (i == R.id.btn_ok) {
            if (opType == OP_TYPE_DEDUCTION_AND_REFUND) {
                showDialog();
            } else {
                this.mPaymentInfo.getOtherPay().clear();
                this.mPaymentInfo.getOtherPay().addPayModelItem(getPayModelItemTypePay());
                this.mDoPayApi.doPay(this.getActivity(), this.mPaymentInfo, mPayOverCallback);
            }
        }
    }

    IPayOverCallback mPayOverCallback = new IPayOverCallback() {

        @Override
        public void onFinished(boolean isOK, int statusCode) {
            try {
                if (isOK) {
                    dismiss();//如果抵扣成功，关闭界面
                    EventBus.getDefault().post(new ExemptEventUpdate(mPaymentInfo.getEraseType())); //刷新输入框
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    };

    private PayModelItem getPayModelItemTypePay() {
        PayModelItem item = new PayModelItem(PayModeId.EARNEST_DEDUCT);//订金抵扣
        double faceValue = mPaymentInfo.getTradeVo().getTradeEarnestMoney();
        item.setChangeAmount(BigDecimal.ZERO);
        item.setUsedValue(BigDecimal.valueOf(faceValue));
        return item;
    }

    private PayModelItem getPayModelItemTypeRefund() {
        PayModelItem item = new PayModelItem(PayModeId.EARNEST_DEDUCT);//订金抵扣
        double faceValue = mPaymentInfo.getActualAmount();
        item.setChangeAmount(BigDecimal.ZERO);
        item.setUsedValue(BigDecimal.valueOf(faceValue));
        return item;
    }

    void showDialog() {
        DialogUtil.showConfirmDialog(getFragmentManager(), CommonDialogFragment.ICON_WARNING, getString(R.string.booking_prepayment_deduction_refund_hint), R.string.ok_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaymentInfo.getOtherPay().clear();
                mPaymentInfo.getOtherPay().addPayModelItem(getPayModelItemTypeRefund());
                mDoPayApi.doPay(getActivity(), mPaymentInfo, mPayOverCallback);
            }
        }, true, "");
    }
}
