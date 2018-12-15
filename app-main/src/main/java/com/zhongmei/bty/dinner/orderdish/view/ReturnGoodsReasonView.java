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

/**
 * 正餐退菜原因窗口
 *
 * @Date：2015-12-11 上午9:57:42
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
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

    private BigDecimal returnCount;// 退菜数

    private BigDecimal increaseUnit = BigDecimal.ONE;//菜品起卖份数
    private BigDecimal stepNum = BigDecimal.ONE;//菜品增量

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

    /**
     * 初始化编辑框的数量
     */
    private void initCount() {
        if (!DinnerTradeItemManager.isMustReturnAll(shopcartItemBase)
                && !TextUtils.isEmpty(shopcartItemBase.getBatchNo())) {// 退菜数默认为增量值
            // 菜品数量小于等于起卖份数时，只能全退
            if (shopcartItemBase.getSingleQty().compareTo(increaseUnit) <= 0) {
                returnCount = shopcartItemBase.getSingleQty();
                // 正常情况下为其增量值
            } else {
                BigDecimal leaveCount = shopcartItemBase.getSingleQty().subtract(stepNum);
                if (leaveCount.compareTo(increaseUnit) < 0) {//加数量时，如果导致剩余数量小于起卖份数，自动跳为全退数目
                    returnCount = shopcartItemBase.getSingleQty();
                } else {
                    returnCount = stepNum;
                }
            }
        } else {// 称重商品不显示数量修改，数量为全退
            returnCount = shopcartItemBase.getSingleQty();

        }

        if (returnCount.compareTo(shopcartItemBase.getSingleQty()) > 0) {
            returnCount = shopcartItemBase.getSingleQty();
        }
    }

    private void initReturnDishCountView() {
        if (!DinnerTradeItemManager.isMustReturnAll(shopcartItemBase)
                && !TextUtils.isEmpty(shopcartItemBase.getBatchNo())
                && shopcartItemBase.getShopcartItemType() != ShopcartItemType.MAINBATCH) {// 退菜数默认为增量值
            tvCount.setVisibility(View.VISIBLE);
            llCount.setVisibility(View.VISIBLE);
            // 小于等于0的还好意思展示？
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
        //初始化输入法
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
//			initInventoryItemList();
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

            // 不能退0
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
//					InputMethodManager imm =
//						(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//					imm.hideSoftInputFromWindow(getActivity()., InputMethodManager.RESULT_UNCHANGED_SHOWN);
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

                if (shopcartItemBase.getSingleQty().compareTo(increaseUnit) <= 0) {//菜品数量小于等于起卖份数，只能全退
                    newCount = shopcartItemBase.getSingleQty();
                    ToastUtil.showShortToast(R.string.must_return_all);
                } else {
                    BigDecimal leaveCount = shopcartItemBase.getSingleQty().subtract(newCount);
                    if (leaveCount.compareTo(BigDecimal.ZERO) > 0 && leaveCount.compareTo(increaseUnit) < 0) {//加数量时，如果导致剩余数量小于起卖份数，自动跳为全量－剩余数量
                        newCount = shopcartItemBase.getSingleQty().subtract(increaseUnit);
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
                    newCount = shopcartItemBase.getSingleQty();//全退
                    ToastUtil.showShortToast(R.string.return_cannot_more_than_count);
                } else {
                    BigDecimal leaveCount = shopcartItemBase.getSingleQty().subtract(newCount);
                    if (leaveCount.compareTo(increaseUnit) < 0) {//加数量时，如果导致剩余数量小于起卖份数，自动跳为全退数目
                        newCount = shopcartItemBase.getSingleQty();
                    }
                }

                etCount.setText(MathDecimal.toTrimZeroString(newCount));
                etCount.setSelection(etCount.getText().length());
                break;
            case R.id.btn_confirm_return:
                //非批量模式需要验证退菜数量
                if (!isBatchMode//不是批量模式
                        && !TextUtils.isEmpty(dishDataItem.getBase().getBatchNo()) //已出单
                        && !dishDataItem.getBase().isGroupDish() //不是团餐菜品
                        && dishDataItem.getBase().getSaleType() != SaleType.WEIGHING  //不是称重
                        && dishDataItem.getBase().getShopcartItemType() != ShopcartItemType.MAINBATCH) {//不是正餐联台批量菜
                    if (!verifyReturnCount()) {
                        return;
                    }
                }

                // 退菜监听
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

        // 不能退0
        if (returnCount.compareTo(BigDecimal.ZERO) <= 0) {
            ToastUtil.showShortToast(R.string.return_must_more_than_0);
            etCount.setSelection(0, etCount.getText().length());

            return false;
            // 没退完，但是剩余部分少于起卖份数
        } else if (leaveCount.compareTo(increaseUnit) < 0 && leaveCount.compareTo(BigDecimal.ZERO) > 0) {
            ToastUtil.showShortToast(R.string.leave_less_than_min);
            etCount.setSelection(0, etCount.getText().length());

            return false;
            // 退菜份数比原本的份数还多了
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
        // 处理选中的原因
        if (textView != null) {
            id = textView.id;
            text = textView.getText().toString();
            // 未选中，处理输入的原因
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
                etOtherReason.setFocusable(true);//设置输入框可聚集
                etOtherReason.setFocusableInTouchMode(true);//设置触摸聚焦
                etOtherReason.requestFocus();//请求焦点
                etOtherReason.findFocus();//获取焦点
                if (event.getAction() == MotionEvent.ACTION_UP) {
//					svContainer.scrollTo(0, (int)v.getY());
                }
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
//		if(isBatchMode || shopcartItemBase != null && shopcartItemBase.)
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

    /**
     * 显示时，添加所有可选原因
     *
     * @Title: initReasonLayout
     * @Return void 返回类型
     */
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
                    // 最后一个不加分割线
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

    /**
     * 隐藏时，清除所有可选原因
     *
     * @Title: hide
     * @Return void 返回类型
     */
    public void hide() {
        etOtherReason.setText("");
        llSelectReason.removeAllViews();
        // 滚回顶部
//		svContainer.scrollTo(0, 0);
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

    /**
     * 获取选中的原因
     *
     * @Title: getSelectReason
     * @Return ReturnGoodsReasonTextView 返回类型
     */
    private ReturnGoodsReasonTextView getSelectReason() {
        for (int i = 0; i < llSelectReason.getChildCount(); i++) {
            View child = llSelectReason.getChildAt(i);
            if ((child instanceof ReturnGoodsReasonTextView) && child.isSelected()) {
                return (ReturnGoodsReasonTextView) child;
            }
        }

        return null;
    }

    /**
     * 清空所有选中的原因
     *
     * @Title: clearReasonSelect
     * @Return void 返回类型
     */
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
                        mInputMethodManager.hideSoftInputFromWindow(etOtherReason.getWindowToken(), 0);// 隐藏输入法
                    }
                    if (isSelected()) {
                        setSelected(false);
                    } else {
                        // 先取消勾选其他的
                        clearReasonSelect();
                        // 选中自己
                        setSelected(true);
                        // 清空自定义输入框
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

    /**
     * 获取起卖份数
     *
     * @param shopcartItem
     * @return
     */
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

    /**
     * @Title: getStepNum
     * @Description: 获取菜品增量
     * @Param @param shopcartItem
     * @Return BigDecimal 返回类型
     */
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
