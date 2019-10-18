package com.zhongmei.bty.cashier.ordercenter.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.os.AsyncTaskCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.constants.OCConstant;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonSource;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.bty.basemodule.commonbusiness.operates.ReasonDal;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryItem;
import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryCacheUtil;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryUtils;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.bty.cashier.inventory.view.ReturnInventoryDialogFragment;
import com.zhongmei.bty.cashier.inventory.view.ReturnInventoryLayout;
import com.zhongmei.bty.common.util.ExtraAsyncTask;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.bty.entity.enums.InventoryShowType;

import java.util.ArrayList;
import java.util.List;


public class OrderCenterOperateDialogFragment extends BasicDialogFragment
        implements OnClickListener {
    private static final String TAG = OrderCenterOperateDialogFragment.class.getName();

    public static final String EXTRA_SOURCE = "extra_source";

        public static final String EXTRA_TIP = "extra_tip";

    private static final int DIALOG_WIDTH = 460;
    private static final int DIALOG_HEIGHT = 590;
    private Context mContext;
    private ObservableScrollView main_content;
    private LinearLayout mContainer;
    private ReasonLayout reasonLayout;
    private PayedLayout payedLayout;
    private PrintLayout printLayout;
    private InventoryLayout inventoryLayout;     private TextView dialog_title;
    private TextView tvTip;
    private ImageView btn_close;
    private Button btn_ok;
    private int mSource = 1;
    private int mType = -1;
    private int mInventoryStyle = -1;    private int mTypeForNoDocument = -1;
    private String mTip;
    private boolean isEmptyShow;
    private String uuid;
    private boolean mReasonSwitch;
    private TradeDeposit tradeDeposit;
    private PaymentVo sellPaymentVo;
    private List<PaymentVo> adjustPaymentVos = new ArrayList<PaymentVo>();
    private ArrayList<OperateListener> listeners = new ArrayList<OperateListener>();
    private ArrayList<OperateCloseListener> closelisteners = new ArrayList<OperateCloseListener>();
        private boolean isShowPrint = true;
    private ReturnInventoryLayout returnInventoryView;     private TradeVo tradeVo;     private List<InventoryItemReq> returnInventroryItemReqs;    private List<InventoryItem> inventoryItems;

    private int mFromType = -1000;

    public OrderCenterOperateDialogFragment() {
    }

    protected int getLayoutResId() {
        return R.layout.order_center_operate_dialog_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Bundle arguments = getArguments();
        if (arguments != null) {
            mSource = arguments.getInt(EXTRA_SOURCE, ReasonSource.ZHONGMEI.value());
            mType = arguments.getInt("type");
            mFromType = arguments.getInt("from_type");
            isEmptyShow = arguments.getBoolean("emptyshow");
            uuid = arguments.getString("uuid");
            mTypeForNoDocument = arguments.getInt("typefornodocument");
            mTip = arguments.getString(EXTRA_TIP);
        }
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        main_content = (ObservableScrollView) view.findViewById(R.id.main_content);
        mContainer = (LinearLayout) view.findViewById(R.id.container);
        reasonLayout = (ReasonLayout) view.findViewById(R.id.reasonLayout);
        payedLayout = (PayedLayout) view.findViewById(R.id.payedLayout);
        printLayout = (PrintLayout) view.findViewById(R.id.printLayout);
        inventoryLayout = (InventoryLayout) view.findViewById(R.id.inventoryLayout);
        dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        tvTip = (TextView) view.findViewById(R.id.tv_tip);
        btn_close = (ImageView) view.findViewById(R.id.btn_close);
        btn_ok = (Button) view.findViewById(R.id.btn_ok);
        returnInventoryView = (ReturnInventoryLayout) view.findViewById(R.id.return_inventory_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDialogWidthAndHeight(view);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                setReasonLayout();
                setPaymentVo();
        setTitleByType();
                setInventoryLayout();
                setInventoryLayoutView();
        setClickListener();
        DisplayPrintLayoutOrNot();

                if (mFromType == OCConstant.FromType.FROM_TYPE_RETAIL) {
            printLayout.setVisibility(View.GONE);
        }

    }

    private void setReasonLayout() {
        if (mType == ReasonType.AGREE_RETURN.value().intValue()
                || mInventoryStyle == InventoryShowType.ONLY_SHOW_INVENTORY.value().intValue()) {
            reasonLayout.setVisibility(View.GONE);
        } else {
            reasonLayout.setTypeAndStart(mSource, mType);
        }
    }

    public void setmInventoryStyle(int value) {
        mInventoryStyle = value;
    }

    private void setInventoryLayout() {
        if (mInventoryStyle == InventoryShowType.ONLY_SHOW_INVENTORY.value().intValue()
                || mInventoryStyle == InventoryShowType.SHOW_OTHER_DATA.value().intValue()) {
            inventoryLayout.setVisibility(View.VISIBLE);
        } else {
            inventoryLayout.setVisibility(View.GONE);
        }
    }

    private void setInventoryLayoutView() {
        inventoryItems = buildInventoryItems(tradeVo);
        if (InventoryCacheUtil.getInstance().getSaleSwitch() && !Utils.isEmpty(inventoryItems)) {
            returnInventoryView.setVisibility(View.VISIBLE);
                        returnInventoryView.setFocusable(true);
            returnInventoryView.setFocusableInTouchMode(true);
            returnInventoryView.requestFocus();
            returnInventoryView.refreshView(inventoryItems);
            returnInventoryView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tradeVo != null) {
                        showReturnInventoryDialog();
                    }
                }
            });
        } else {
            returnInventoryView.setVisibility(View.GONE);
            returnInventroryItemReqs = InventoryUtils.buildInventoryItemReqs(inventoryItems);
        }
    }

    private void showReturnInventoryDialog() {
        ReturnInventoryDialogFragment returnInventoryDialogFragment = new ReturnInventoryDialogFragment();
        returnInventoryDialogFragment.setInventoryItemList(inventoryItems);
        returnInventoryDialogFragment.setReturnDishDataListener(new ReturnInventoryDialogFragment.ReturnDishDataListener() {
            @Override
            public void setDishData(List<InventoryItem> inventoryItemList) {
                returnInventoryView.refreshView(inventoryItemList);
                returnInventroryItemReqs = InventoryUtils.buildInventoryItemReqs(inventoryItemList);
            }
        });
        returnInventoryDialogFragment.show(getActivity().getSupportFragmentManager(), "returnInventoryDialogFragment");
    }

    private List<TradeItem> findReturnInventoryChildTradeItems(TradeVo tradeVo, TradeItem parentTradeItem) {
        List<TradeItem> childTradeItems = new ArrayList<>();
        for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
            TradeItem tradeItem = tradeItemVo.getTradeItem();
            if (parentTradeItem.getUuid().equals(tradeItem.getParentUuid())) {
                childTradeItems.add(tradeItem);
            }
        }
        return childTradeItems;
    }

        private List<InventoryItem> buildInventoryItems(TradeVo tradeVo) {
        if (tradeVo == null || Utils.isEmpty(tradeVo.getTradeItemList())) {
            return null;
        }

        String buffetOrGroupParentUUID = null;
        if (tradeVo.getMealShellVo() != null) {            buffetOrGroupParentUUID = tradeVo.getMealShellVo().getUuid();
        }

        List<InventoryItem> inventoryItems = new ArrayList<>();
        for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
            TradeItem tradeItem = tradeItemVo.getTradeItem();
            if (tradeItem.getStatusFlag() == StatusFlag.INVALID) {
                continue;
            }
            if (tradeItem.getType() == DishType.COMBO) {
                List<TradeItem> childTradeItem = findReturnInventoryChildTradeItems(tradeVo, tradeItem);
                if (!Utils.isEmpty(childTradeItem)) {
                    InventoryItem inventoryItem = new InventoryItem(tradeItem);
                    inventoryItem.setChildTradeItem(childTradeItem);
                    inventoryItems.add(inventoryItem);
                }
            } else if (tradeItem.getType() == DishType.SINGLE) {
                InventoryItem inventoryItem = new InventoryItem(tradeItem);
                inventoryItems.add(inventoryItem);
            }
        }
        return inventoryItems;
    }

    private void setDialogWidthAndHeight(View view) {
        Window window = getDialog().getWindow();
        view.measure(0, 0);
        int measuredHeight = view.getMeasuredHeight();
        int measuredWidth = view.getMeasuredHeight();
        Log.d(TAG, "measuredWidth:" + measuredWidth + ",measuredHeight:" + measuredHeight);

        Resources resources = getActivity().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int desiredWidth = metrics.widthPixels;
        int desiredHeight = metrics.heightPixels;
        window.setLayout(desiredWidth, desiredHeight);
        window.getAttributes().y = 0;

            }

    public void registerListener(OperateListener listener) {
        if (listener == null) return;
        listeners.add(listener);
    }

    public void registerCloseListener(OperateCloseListener listener) {
        if (listener == null) return;
        closelisteners.add(listener);
    }

    public void isShowPrint(boolean isShowPrint) {
        this.isShowPrint = isShowPrint;
    }

    private OperateResult getResult() {
        return new OperateResult(reasonLayout.getReason(), printLayout.getPrintCheckedStatus(), inventoryLayout.getInventoryCheckStatus());
    }

    private void DisplayPrintLayoutOrNot() {
        if (mType == ReasonType.TRADE_REFUSED.value().intValue()
                || mType == ReasonType.TRADE_FREE.value().intValue()
                || !isShowPrint
                || mType == ReasonType.REFUSE_RETURN.value().intValue()
                || mType == ReasonType.TRADE_REPEATED.value().intValue()
                || mType == ReasonType.BOOKING_CANCEL.value().intValue()
                || mType == ReasonType.BOOKING_REFUSED.value().intValue()
                || mType == ReasonType.LAG_REASON.value().intValue()
                || mType == ReasonType.TRADE_DISCOUNT.value().intValue()
                || mType == ReasonType.ITEM_GIVE.value().intValue()
                || mType == ReasonType.TRADE_BANQUET.value().intValue()
                || mType == ReasonType.TRADE_SINGLE_DISCOUNT.value().intValue()
                || mType == ReasonType.INTEGRAL_MODIFY.value().intValue()
                || mInventoryStyle == InventoryShowType.ONLY_SHOW_INVENTORY.value().intValue()
                ) {
            printLayout.setVisibility(View.GONE);
        }
    }

    private void setPaymentVo() {
        if (payedLayout != null) {
            if (mType == ReasonType.TRADE_INVALID.value().intValue()
                    || mType == ReasonType.REFUSE_RETURN.value().intValue()
                    || mType == ReasonType.TRADE_REPEATED.value().intValue()
                    || mType == ReasonType.BOOKING_CANCEL.value().intValue()
                    || mType == ReasonType.BOOKING_REFUSED.value().intValue()
                    || mType == ReasonType.LAG_REASON.value().intValue()
                    || mType == ReasonType.TRADE_DISCOUNT.value().intValue()
                    || mType == ReasonType.ITEM_GIVE.value().intValue()
                    || mType == ReasonType.TRADE_BANQUET.value().intValue()
                                        || mType == ReasonType.TRADE_FREE.value().intValue()
                    || mType == ReasonType.INTEGRAL_MODIFY.value().intValue()
                    || mInventoryStyle == InventoryShowType.ONLY_SHOW_INVENTORY.value().intValue()) {
                payedLayout.setVisibility(View.GONE);
                return;
            }

            if (tradeDeposit != null) {
                payedLayout.setTradeDeposit(tradeDeposit);
            }

            payedLayout.setCurrentType(mTypeForNoDocument);
            if (sellPaymentVo != null) {
                payedLayout.setPayment(sellPaymentVo);
                return;
            }
            if (adjustPaymentVos != null && adjustPaymentVos.size() > 0) {
                payedLayout.setPayment(adjustPaymentVos);
                return;
            }
            payedLayout.setVisibility(View.GONE);
        }
    }

    private void setTitleByType() {
        payedLayout.setTitle(R.string.order_center_fragment_dialog_title_payed);
        printLayout.setTitle(R.string.order_center_fragment_dialog_title_print);
        inventoryLayout.setTitle(R.string.select_return_inventory);
        if (mType == ReasonType.TRADE_RETURNED.value().intValue()) {
            dialog_title.setText(R.string.reason_type_delete_dish_title);
        } else if (mType == ReasonType.TRADE_INVALID.value().intValue()) {
            dialog_title.setText(R.string.reason_type_destroy_title);
        } else if (mType == ReasonType.TRADE_REFUSED.value().intValue()) {
            dialog_title.setText(R.string.reason_type_refuse_title);
        } else if (ReasonType.TRADE_REPEATED.equalsValue(mType)) {
            dialog_title.setText(R.string.reason_type_repay_btn);
        } else if (mType == ReasonType.TRADE_FREE.value().intValue()) {
            dialog_title.setText(R.string.reason_type_free_btn);
        } else if (mType == ReasonType.BOOKING_REFUSED.value().intValue()) {
            dialog_title.setText(R.string.booking_refused_title);
        } else if (mType == ReasonType.BOOKING_CANCEL.value().intValue()) {
            dialog_title.setText(R.string.booking_recision_title);
        } else if (mType == ReasonType.AGREE_RETURN.value().intValue()) {
            dialog_title.setText(R.string.order_detail_agree_st);
        } else if (mType == ReasonType.REFUSE_RETURN.value().intValue()) {
            dialog_title.setText(R.string.order_detail_refuse_st);
        } else if (mType == ReasonType.LAG_REASON.value().intValue()) {
            dialog_title.setText(R.string.reason_lag_title);
        } else if (mType == ReasonType.TRADE_DISCOUNT.value().intValue()) {
            dialog_title.setText(R.string.reason_trade_discount_title);
        } else if (mType == ReasonType.ITEM_GIVE.value().intValue()) {
            dialog_title.setText(R.string.reason_item_give_title);
        } else if (mType == ReasonType.TRADE_BANQUET.value().intValue()) {
            dialog_title.setText(R.string.reason_banquet_title);
        } else if (mType == ReasonType.TRADE_SINGLE_DISCOUNT.value()) {
            dialog_title.setText(R.string.reason_trade_discount_title);
        } else if (mType == ReasonType.INTEGRAL_MODIFY.value()) {
            dialog_title.setText(getString(R.string.reason_member_integral_modify));
        }
        if (!TextUtils.isEmpty(mTip)) {
            tvTip.setText(mTip);
            tvTip.setVisibility(View.VISIBLE);
        } else {
            tvTip.setVisibility(View.GONE);
        }
                if (mInventoryStyle == InventoryShowType.ONLY_SHOW_INVENTORY.value().intValue()) {
            dialog_title.setText(getString(R.string.goods_return_inventory));
        }
    }

    private void setClickListener() {
        btn_close.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        if (mType == ReasonType.TRADE_RETURNED.value().intValue()) {
            btn_ok.setBackgroundResource(R.drawable.bg_order_center_dialog_refuse);
            btn_ok.setText(R.string.reason_type_delete_dish_title);        } else if (mType == ReasonType.TRADE_INVALID.value().intValue()) {
            btn_ok.setBackgroundResource(R.drawable.bg_order_center_dialog_refuse);
            btn_ok.setText(R.string.reason_type_destroy_title);        } else if (mType == ReasonType.TRADE_REFUSED.value().intValue()) {
            btn_ok.setBackgroundResource(R.drawable.bg_order_center_dialog_refuse);
            btn_ok.setText(R.string.reason_type_refuse_title);        } else if (mType == ReasonType.TRADE_REPEATED.value().intValue()) {
            btn_ok.setBackgroundResource(R.drawable.bg_order_center_dialog_refund);
            btn_ok.setText(R.string.reason_type_repay_btn);        } else if (mType == ReasonType.TRADE_FREE.value().intValue()) {
            btn_ok.setBackgroundResource(R.drawable.bg_order_center_dialog_refund);
            btn_ok.setText(R.string.reason_type_free_btn);        } else if (mType == ReasonType.BOOKING_CANCEL.value().intValue()                || mType == ReasonType.BOOKING_REFUSED.value().intValue()                ) {
            btn_ok.setBackgroundResource(R.drawable.bg_order_center_dialog_refuse);
            btn_ok.setText(R.string.common_submit);
        } else if (mType == ReasonType.LAG_REASON.value().intValue()                || mType == ReasonType.TRADE_DISCOUNT.value().intValue()                || mType == ReasonType.TRADE_BANQUET.value().intValue()                || mType == ReasonType.ITEM_GIVE.value().intValue()                || mType == ReasonType.TRADE_SINGLE_DISCOUNT.value().intValue()) {            btn_ok.setBackgroundResource(R.drawable.bg_order_center_dialog_refund);
            btn_ok.setText(R.string.common_submit);
        } else if (mType == ReasonType.AGREE_RETURN.value().intValue()) {            btn_ok.setBackgroundResource(R.drawable.bg_order_center_dialog_refund);
            btn_ok.setText(R.string.order_detail_agree_st);
        } else if (mType == ReasonType.REFUSE_RETURN.value().intValue()) {            btn_ok.setBackgroundResource(R.drawable.bg_order_center_dialog_refuse);
            btn_ok.setText(R.string.order_detail_refuse_st);
        } else if (mType == ReasonType.INTEGRAL_MODIFY.value().intValue()) {
            btn_ok.setBackgroundResource(R.drawable.bg_order_center_dialog_refund);
            btn_ok.setText(R.string.common_submit);
        } else {
            btn_ok.setBackgroundResource(R.drawable.bg_order_center_dialog_refund);
        }
        if (mInventoryStyle == InventoryShowType.ONLY_SHOW_INVENTORY.value().intValue()) {
            btn_ok.setBackgroundResource(R.drawable.bg_order_center_dialog_refund);
            btn_ok.setText(R.string.common_submit);
        }
    }

    public void setPayment(PaymentVo paymentVo) {
        sellPaymentVo = paymentVo;
        setPaymentVo();
    }

    public void setPayment(List<PaymentVo> paymentVos) {
        adjustPaymentVos.clear();
        boolean sucess = adjustPaymentVos.addAll(paymentVos);
        setPaymentVo();
    }


    public void setTradeDepoist(TradeDeposit vTradeDeposit) {
        tradeDeposit = vTradeDeposit;
    }


    public void setTradeVo(TradeVo tradeVo) {
        this.tradeVo = tradeVo;
    }

    private void clearStatus() {
        if (listeners != null) {
            listeners.clear();
            listeners = null;
        }
        if (closelisteners != null) {
            closelisteners.clear();
            closelisteners = null;
        }
        if (adjustPaymentVos != null) {
            adjustPaymentVos.clear();
            adjustPaymentVos = null;
        }
    }

    private boolean setResult() {
        if (listeners == null || listeners.size() == 0) {
            Log.d(TAG, "setResult listeners empty");
            return true;
        }
        OperateResult result = getResult();
        if (returnInventroryItemReqs != null) {
            result.returnInventoryItemReqs = returnInventroryItemReqs;
        } else {
                        result.returnInventoryItemReqs = InventoryUtils.buildInventoryItemReqs(buildInventoryItems(tradeVo));
        }
        if (mInventoryStyle != InventoryShowType.ONLY_SHOW_INVENTORY.value().intValue()) {
            if (mReasonSwitch) {
                if ((result == null || result.reason == null) && (mType != ReasonType.AGREE_RETURN.value().intValue())) {
                    if (mType == ReasonType.BOOKING_CANCEL.value().intValue()) {
                        ToastUtil.showShortToast(R.string.not_cancel_reason);
                    } else if (mType == ReasonType.BOOKING_REFUSED.value().intValue()) {
                        ToastUtil.showShortToast(R.string.not_refused_reason);
                    } else {
                        ToastUtil.showShortToast(R.string.reason_not_illedgal);
                    }
                    return false;
                }
            }

        }
        setResultListener(result);

        return true;
    }

    private void setResultListener(OperateResult result) {
        for (OperateListener listener : listeners) {
            listener.onSuccess(result);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                if (closelisteners != null && closelisteners.size() > 0) {
                    for (OperateCloseListener listener : closelisteners) {
                        listener.onClose(null);
                    }

                }
                clearStatus();
                if (v != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                dismiss();
                break;
            case R.id.btn_ok:
                boolean success = setResult();
                if (!success) return;
                clearStatus();
                dismiss();
                break;

            default:
                break;
        }

    }

    public interface OperateListener {
        public boolean onSuccess(OperateResult result);
    }

    public interface OperateCloseListener {
        public void onClose(OperateResult result);
    }

    static public class OperateResult {
        public List<InventoryItemReq> returnInventoryItemReqs;
        public Reason reason;
        public boolean isPrintChecked;
        public boolean isReturnInvetory;

        public OperateResult(Reason reason, boolean isPrintChecked, boolean isReturnInvetory) {
            this.reason = reason;
            this.isPrintChecked = isPrintChecked;
            this.isReturnInvetory = isReturnInvetory;
        }

    }


    private boolean isReasonSwitchOpen() {
        ReasonDal reasonDal = OperatesFactory.create(ReasonDal.class);
        int type = getArguments().getInt("type");
        if (ReasonType.LAG_REASON.value() == type) {            return true;
        }
        return reasonDal.isReasonSwitchOpen(ReasonType.newReason(type));
    }

    @Override
    public void show(FragmentManager manager, String tag) {
                asyncBack(manager, null, tag);
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
                asyncBack(null, transaction, tag);
        return -1;
    }

    private void asyncBack(FragmentManager manager, FragmentTransaction transaction, String tag) {
        AsyncTaskCompat.executeParallel(new ExtraAsyncTask<Void, Void, Boolean>(manager, transaction, tag) {
            @Override
            protected Boolean doInBackground(Void... params) {
                mReasonSwitch = isReasonSwitchOpen();
                return mReasonSwitch || mInventoryStyle != -1;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                FragmentManager manager = get(0);
                FragmentTransaction transaction = get(1);
                String tag = get(2);

                if (!aBoolean) {
                    setResultListener(new OperateResult(null, false, true));
                } else {
                    try {                        if (manager != null) {
                            OrderCenterOperateDialogFragment.super.show(manager, tag);
                        } else if (transaction != null) {
                            OrderCenterOperateDialogFragment.super.show(transaction, tag);
                        } else {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}
