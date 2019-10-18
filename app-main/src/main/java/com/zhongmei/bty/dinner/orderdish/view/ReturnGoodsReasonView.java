package com.zhongmei.bty.dinner.orderdish.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.entity.ReasonSetting;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonSource;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.bty.basemodule.commonbusiness.operates.ReasonDal;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryItem;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryCacheUtil;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryUtils;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.bty.cashier.inventory.view.ReturnInventoryDialogFragment;
import com.zhongmei.bty.cashier.inventory.view.ReturnInventoryLayout;
import com.zhongmei.bty.cashier.ordercenter.view.ObservableScrollView;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.bty.dinner.manager.DinnerTradeItemManager;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@EFragment(R.layout.view_return_goods_reason)
public class ReturnGoodsReasonView extends BasicDialogFragment {

    private final static long DEFAULT_RETURN_GOODS_REASON_ID = -3;

    @ViewById(R.id.ib_close)
    ImageButton ibClose;

    @ViewById(R.id.sv_container)
    ObservableScrollView svContainer;

    @ViewById(R.id.tv_count)
    TextView tvCount;

    @ViewById(R.id.item_title)
    TextView mItemTitle;

    @ViewById(R.id.ll_count)
    LinearLayout llCount;

    @ViewById(R.id.et_count)
    EditText etCount;

    @ViewById(R.id.et_other_reason)
    EditText etOtherReason;

    @ViewById(R.id.ll_select_reason)
    LinearLayout llSelectReason;

    @ViewById(R.id.reason_layout)
    ViewGroup reasonLayout;

    @ViewById(R.id.return_inventory_layout)
    ReturnInventoryLayout inventoryLayout;

    private IShopcartItemBase shopcartItemBase;

    private DishDataItem dishDataItem;

    private List<DishDataItem> dishDataItemList;

    private List<InventoryItem> inventoryItemList;

    private Drawable unselectDrawable;

    private Drawable selectDrawable;

    private BigDecimal returnCount;
    private BigDecimal increaseUnit = BigDecimal.ONE;    private BigDecimal stepNum = BigDecimal.ONE;
    InputMethodManager mInputMethodManager;

    private ConfirmListener confirmListener;
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    public boolean isBatchMode = false;

    public boolean isShowReturnInventoryLayout = false;

    public boolean isShowReason = false;

    public void setConfirmListener(ConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    private ReturnInventoryListener inventoryListener;

    public void setInventoryListener(ReturnInventoryListener inventoryListener) {
        this.inventoryListener = inventoryListener;
    }

    public ReturnGoodsReasonView() {
    }

    public void setDishDataItem(DishDataItem dishDataItem) {
        this.dishDataItem = dishDataItem;
        if (dishDataItem != null) {
            shopcartItemBase = dishDataItem.getBase();

            increaseUnit = getIncreaseUnit(shopcartItemBase);
            stepNum = genStepNum(shopcartItemBase);
            initCount();
            initInventoryItemList();
        }
    }

    public void setDishDataItemList(List<DishDataItem> dishDataItemList) {
        this.dishDataItemList = dishDataItemList;
        initInventoryItemList();
    }


    private void initCount() {
        if (!DinnerTradeItemManager.isMustReturnAll(shopcartItemBase)
                && !TextUtils.isEmpty(shopcartItemBase.getBatchNo())) {                        if (shopcartItemBase.getSingleQty().compareTo(increaseUnit) <= 0) {
                returnCount = shopcartItemBase.getSingleQty();
                            } else {
                BigDecimal leaveCount = shopcartItemBase.getSingleQty().subtract(stepNum);
                if (leaveCount.compareTo(increaseUnit) < 0) {                    returnCount = shopcartItemBase.getSingleQty();
                } else {
                    returnCount = stepNum;
                }
            }
        } else {            returnCount = shopcartItemBase.getSingleQty();

        }

        if (returnCount.compareTo(shopcartItemBase.getSingleQty()) > 0) {
            returnCount = shopcartItemBase.getSingleQty();
        }
    }

    private void initReturnDishCountView() {
        if (!DinnerTradeItemManager.isMustReturnAll(shopcartItemBase)
                && !TextUtils.isEmpty(shopcartItemBase.getBatchNo())
                && shopcartItemBase.getShopcartItemType() != ShopcartItemType.MAINBATCH) {            tvCount.setVisibility(View.VISIBLE);
            llCount.setVisibility(View.VISIBLE);
                    } else {
            hideCountView();
        }
        if (returnCount.compareTo(BigDecimal.ZERO) > 0) {
            Resources res = getActivity().getResources();
            tvCount.setText(res.getString(R.string.return_count) + MathDecimal.toTrimZeroString(returnCount) + "  "
                    + res.getString(R.string.leave_count) + 0);
            etCount.setText(MathDecimal.toTrimZeroString(returnCount) + "");
            etCount.setSelection(etCount.length());
        }
    }

    private void hideCountView() {
        mItemTitle.setVisibility(View.GONE);
        tvCount.setVisibility(View.GONE);
        llCount.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_LAYOUT_FLAGS | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        Window window = getDialog().getWindow();
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setDialogWidthAndHeight(View view) {
        Window window = getDialog().getWindow();
        view.measure(0, 0);
        Resources resources = getActivity().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int desiredWidth = metrics.widthPixels;
        int desiredHeight = metrics.heightPixels;
        window.setLayout(desiredWidth, desiredHeight);
        window.getAttributes().y = 0;
    }

    @AfterViews
    void initViews() {
        setDialogWidthAndHeight(getView());
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mItemTitle.requestFocus();
        unselectDrawable = getActivity().getResources().getDrawable(R.drawable.print_checkbox_default);
        unselectDrawable.setBounds(0, 0, unselectDrawable.getMinimumWidth(), unselectDrawable.getMinimumHeight());
        selectDrawable = getActivity().getResources().getDrawable(R.drawable.print_checkbox_selected_new);
        selectDrawable.setBounds(0, 0, selectDrawable.getMinimumWidth(), selectDrawable.getMinimumHeight());
        initUtil();
        initUI();


    }

    private void initUtil() {
                mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void initUI() {
        if (!isBatchMode) {
            initReturnDishCountView();
        } else {
            hideCountView();
        }
        if (isShowReason) {
            reasonLayout.setVisibility(View.VISIBLE);
            initReasonLayout();
        } else {
            reasonLayout.setVisibility(View.GONE);
        }
        if (isShowReturnInventoryLayout) {
            inventoryLayout.setVisibility(View.VISIBLE);
            inventoryLayout.refreshView(inventoryItemList);
        } else {
            inventoryLayout.setVisibility(View.GONE);
        }
    }

    private void initInventoryItemList() {
        inventoryItemList = new ArrayList<>();
        if (dishDataItem != null) {
            InventoryItem inventoryItem = InventoryUtils.transformInventoryItem(dishDataItem, returnCount);
            inventoryItemList.add(inventoryItem);
        }
        if (dishDataItemList != null) {
            inventoryItemList = InventoryUtils.transformInventoryItemList(dishDataItemList);
        }
    }

    @AfterTextChange({R.id.et_count})
    void afterTextChange() {
        if (isBatchMode) {
            return;
        }

        if (shopcartItemBase != null && !TextUtils.isEmpty(etCount.getText())) {
            BigDecimal inputCount = new BigDecimal(etCount.getText().toString());

            BigDecimal leaveCount = shopcartItemBase.getSingleQty().subtract(inputCount);

                        if (inputCount.compareTo(BigDecimal.ZERO) <= 0) {
                ToastUtil.showShortToast(R.string.return_must_more_than_0);
                etCount.setText(MathDecimal.toTrimZeroString(returnCount));
                etCount.setSelection(etCount.getText().length());
            } else if (inputCount.compareTo(shopcartItemBase.getSingleQty()) > 0) {
                ToastUtil.showShortToast(R.string.return_cannot_more_than_count);
                etCount.setText(MathDecimal.toTrimZeroString(returnCount));
                etCount.setSelection(etCount.getText().length());
            } else {
                Resources res = getActivity().getResources();
                tvCount.setText(res.getString(R.string.return_count) + MathDecimal.toTrimZeroString(inputCount) + "  "
                        + res.getString(R.string.leave_count) + MathDecimal.toTrimZeroString(leaveCount));
                returnCount = inputCount;
                if (Utils.isNotEmpty(inventoryItemList)) {
                    inventoryItemList.get(0).setMaxInventoryNum(inputCount);
                    inventoryLayout.refreshView(inventoryItemList);
                }
            }
        }
    }

    @FocusChange({R.id.et_other_reason})
    void focusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_other_reason:
                if (hasFocus) {
                    clearReasonSelect();
                } else {
                }
                break;
            default:
                break;
        }
    }

    @Click({R.id.ib_close, R.id.iv_minus, R.id.iv_plus, R.id.btn_confirm_return, R.id.return_inventory_item})
    void click(View v) {
        switch (v.getId()) {
            case R.id.ib_close:
                hide();
                break;
            case R.id.iv_minus:
                if (isBatchMode) {
                    return;
                }

                if (TextUtils.isEmpty(etCount.getText())) {
                    return;
                }

                if (shopcartItemBase == null || !(shopcartItemBase instanceof ReadonlyShopcartItem)) {
                    return;
                }
                BigDecimal oldCount = new BigDecimal(etCount.getText().toString());
                BigDecimal newCount = oldCount.subtract(stepNum);
                if (newCount.compareTo(BigDecimal.ZERO) <= 0) {
                    ToastUtil.showShortToast(R.string.return_must_more_than_0);

                    return;
                }

                if (shopcartItemBase.getSingleQty().compareTo(increaseUnit) <= 0) {                    newCount = shopcartItemBase.getSingleQty();
                    ToastUtil.showShortToast(R.string.must_return_all);
                } else {
                    BigDecimal leaveCount = shopcartItemBase.getSingleQty().subtract(newCount);
                    if (leaveCount.compareTo(BigDecimal.ZERO) > 0 && leaveCount.compareTo(increaseUnit) < 0) {                        newCount = shopcartItemBase.getSingleQty().subtract(increaseUnit);
                    }
                }

                etCount.setText(MathDecimal.toTrimZeroString(newCount));
                etCount.setSelection(etCount.getText().length());
                break;
            case R.id.iv_plus:
                if (isBatchMode) {
                    return;
                }

                if (TextUtils.isEmpty(etCount.getText())) {
                    return;
                }

                if (shopcartItemBase == null || !(shopcartItemBase instanceof ReadonlyShopcartItem)) {
                    return;
                }

                oldCount = new BigDecimal(etCount.getText().toString());
                newCount = oldCount.add(stepNum);

                if (newCount.compareTo(shopcartItemBase.getSingleQty()) > 0) {
                    newCount = shopcartItemBase.getSingleQty();                    ToastUtil.showShortToast(R.string.return_cannot_more_than_count);
                } else {
                    BigDecimal leaveCount = shopcartItemBase.getSingleQty().subtract(newCount);
                    if (leaveCount.compareTo(increaseUnit) < 0) {                        newCount = shopcartItemBase.getSingleQty();
                    }
                }

                etCount.setText(MathDecimal.toTrimZeroString(newCount));
                etCount.setSelection(etCount.getText().length());
                break;
            case R.id.btn_confirm_return:
                                if (!isBatchMode                        && !TextUtils.isEmpty(dishDataItem.getBase().getBatchNo())                         && !dishDataItem.getBase().isGroupDish()                         && dishDataItem.getBase().getSaleType() != SaleType.WEIGHING                          && dishDataItem.getBase().getShopcartItemType() != ShopcartItemType.MAINBATCH) {                    if (!verifyReturnCount()) {
                        return;
                    }
                }

                                if (confirmListener != null) {
                    Reason reason = null;
                    if (reasonLayout.isShown()) {
                        reason = getReason();
                        if (reason == null) {
                            return;
                        }
                    }
                    confirmListener.onConfirm(returnCount, reason, inventoryItemList);
                }
                if (inventoryListener != null) {
                    inventoryListener.onConfirm(inventoryItemList);
                }
                dismiss();
                break;
            case R.id.return_inventory_item:
                ReturnInventoryDialogFragment dialogFragment = new ReturnInventoryDialogFragment();
                dialogFragment.setInventoryItemList(inventoryItemList);
                dialogFragment.setReturnDishDataListener(new ReturnInventoryDialogFragment.ReturnDishDataListener() {
                    @Override
                    public void setDishData(List<InventoryItem> inventoryItemList) {
                        ReturnGoodsReasonView.this.inventoryItemList = inventoryItemList;
                        inventoryLayout.refreshView(ReturnGoodsReasonView.this.inventoryItemList);
                    }
                });
                dialogFragment.show(getFragmentManager(), "returnInventory");
                break;
            default:
                break;
        }
    }

    private boolean verifyReturnCount() {
        if (TextUtils.isEmpty(etCount.getText()) || returnCount.compareTo(BigDecimal.ZERO) <= 0) {
            ToastUtil.showShortToast(R.string.input_valid_return_count);

            return false;
        }

        BigDecimal leaveCount = shopcartItemBase.getSingleQty().subtract(returnCount);

                if (returnCount.compareTo(BigDecimal.ZERO) <= 0) {
            ToastUtil.showShortToast(R.string.return_must_more_than_0);
            etCount.setSelection(0, etCount.getText().length());

            return false;
                    } else if (leaveCount.compareTo(increaseUnit) < 0 && leaveCount.compareTo(BigDecimal.ZERO) > 0) {
            ToastUtil.showShortToast(R.string.leave_less_than_min);
            etCount.setSelection(0, etCount.getText().length());

            return false;
                    } else if (returnCount.compareTo(shopcartItemBase.getSingleQty()) > 0) {
            ToastUtil.showShortToast(R.string.return_cannot_more_than_count);
            etCount.setSelection(0, etCount.getText().length());

            return false;
        }

        return true;
    }

    private Reason getReason() {
        ReturnGoodsReasonTextView textView = getSelectReason();
        Long id;
        String text;
                if (textView != null) {
            id = textView.id;
            text = textView.getText().toString();
                    } else if (!TextUtils.isEmpty(etOtherReason.getText())) {
            id = DEFAULT_RETURN_GOODS_REASON_ID;
            text = etOtherReason.getText().toString();
        } else {
            ToastUtil.showShortToast(R.string.select_or_input_return_reason);
            return null;
        }

        Reason reason = new Reason();
        reason.setId(id);
        reason.setContent(text);

        return reason;
    }

    @Touch({R.id.et_other_reason})
    boolean touch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.et_other_reason:
                etOtherReason.setFocusable(true);                etOtherReason.setFocusableInTouchMode(true);                etOtherReason.requestFocus();                etOtherReason.findFocus();                if (event.getAction() == MotionEvent.ACTION_UP) {
                }
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        isShowReturnInventoryLayout = InventoryCacheUtil.getInstance().getSaleSwitch();
        if (isShowReturnInventoryLayout || isShowReason) {
            super.show(manager, tag);
        } else {
            if (confirmListener != null) {
                confirmListener.onConfirm(returnCount, null, inventoryItemList);
            }
            if (inventoryListener != null) {
                inventoryListener.onConfirm(inventoryItemList);
            }
        }
    }


    private void initReasonLayout() {
        ReasonDal reasonDal = OperatesFactory.create(ReasonDal.class);
        if (reasonDal.isReasonSwitchOpen(ReasonType.ITEM_RETURN_QTY)) {
            reasonLayout.setVisibility(View.VISIBLE);
            List<ReasonSetting> reasons = queryReason();
            if (reasons != null && reasons.size() > 0) {
                llSelectReason.setVisibility(View.VISIBLE);
                for (int i = 0; i < reasons.size(); i++) {
                    ReasonSetting reason = reasons.get(i);
                    llSelectReason.addView(new ReturnGoodsReasonTextView(getActivity(), reason.getId(), reason.getContent()));
                                        if (i < reasons.size() - 1) {
                        LinearLayout.LayoutParams separatorLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
                        View separatorView = new View(getActivity());
                        separatorView.setBackgroundResource(R.color.separator_reason);
                        llSelectReason.addView(separatorView, separatorLp);
                    }
                }
            } else {
                llSelectReason.setVisibility(View.GONE);
            }
        } else {
            reasonLayout.setVisibility(View.GONE);
        }
    }


    public void hide() {
        etOtherReason.setText("");
        llSelectReason.removeAllViews();
                dismiss();
    }

    private List<ReasonSetting> queryReason() {
        ReasonDal reasonDal = OperatesFactory.create(ReasonDal.class);
        try {
            return reasonDal.findReasonSetting(ReasonSource.ZHONGMEI, ReasonType.ITEM_RETURN_QTY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    private ReturnGoodsReasonTextView getSelectReason() {
        for (int i = 0; i < llSelectReason.getChildCount(); i++) {
            View child = llSelectReason.getChildAt(i);
            if ((child instanceof ReturnGoodsReasonTextView) && child.isSelected()) {
                return (ReturnGoodsReasonTextView) child;
            }
        }

        return null;
    }


    private void clearReasonSelect() {
        for (int i = 0; i < llSelectReason.getChildCount(); i++) {
            View child = llSelectReason.getChildAt(i);
            if (child instanceof ReturnGoodsReasonTextView) {
                ReturnGoodsReasonTextView textView = (ReturnGoodsReasonTextView) child;
                if (textView.isSelected()) {
                    textView.setSelected(false);
                }
            }
        }
    }

    private final class ReturnGoodsReasonTextView extends TextView {

        private long id;

        private ReturnGoodsReasonTextView(Context context, long id, String text) {
            super(context);
            this.id = id;
            setPadding(DensityUtil.dip2px(MainApplication.getInstance(), 9), 0, 0, 0);
            setGravity(Gravity.CENTER_VERTICAL);
            setText(text);
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            setTextColor(getContext().getResources().getColor(R.color.dinner_dishname_color));

            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(MainApplication.getInstance(), 53));
            setLayoutParams(lp);

            setCompoundDrawables(unselectDrawable, null, null, null);
            setCompoundDrawablePadding(10);
            setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    etOtherReason.setFocusable(false);
                    if (mInputMethodManager.isActive()) {
                        mInputMethodManager.hideSoftInputFromWindow(etOtherReason.getWindowToken(), 0);                    }
                    if (isSelected()) {
                        setSelected(false);
                    } else {
                                                clearReasonSelect();
                                                setSelected(true);
                                                etOtherReason.setText("");
                        tvCount.requestFocus();
                    }
                }
            });
        }

        @Override
        public void setSelected(boolean selected) {
            super.setSelected(selected);
            if (selected) {
                setCompoundDrawables(selectDrawable, null, null, null);
            } else {
                setCompoundDrawables(unselectDrawable, null, null, null);
            }
        }
    }


    private BigDecimal getIncreaseUnit(IShopcartItemBase shopcartItem) {
        DishShop dishShop = shopcartItem.getDishShop();
        if (dishShop == null) {
            return BigDecimal.ONE;
        }

        BigDecimal increaseUnit = dishShop.getDishIncreaseUnit();
        if (increaseUnit != null && increaseUnit.compareTo(BigDecimal.ZERO) > 0) {
            return increaseUnit;
        } else {
            return BigDecimal.ONE;
        }
    }


    private BigDecimal genStepNum(IShopcartItemBase shopcartItem) {
        DishShop dishShop = shopcartItem.getDishShop();
        if (dishShop == null) {
            return BigDecimal.ONE;
        }

        BigDecimal stepNum = dishShop.getStepNum();
        if (stepNum != null && stepNum.compareTo(BigDecimal.ZERO) > 0) {
            return stepNum;
        } else {
            return BigDecimal.ONE;
        }
    }

    public interface ConfirmListener {
        void onConfirm(BigDecimal returnCount, Reason reason, List<InventoryItem> inventoryItemList);
    }

    public interface ReturnInventoryListener {
        void onConfirm(List<InventoryItem> inventoryItemList);
    }
}
