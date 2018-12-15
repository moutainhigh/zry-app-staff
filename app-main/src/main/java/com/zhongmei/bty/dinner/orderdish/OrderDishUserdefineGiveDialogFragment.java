package com.zhongmei.bty.dinner.orderdish;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonSource;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryItem;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryCacheUtil;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.bty.cashier.inventory.view.ReturnInventoryDialogFragment;
import com.zhongmei.bty.cashier.inventory.view.ReturnInventoryLayout;
import com.zhongmei.bty.cashier.ordercenter.view.ReasonLayout;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
@EFragment(R.layout.order_dish_userdefine_give)
public class OrderDishUserdefineGiveDialogFragment extends BasicDialogFragment {

    @ViewById(R.id.et_count)
    EditText etCount;

    @ViewById(R.id.return_inventory_layout)
    ReturnInventoryLayout inventoryLayout;

    @ViewById(R.id.reasonLayout)
    ReasonLayout reasonLayout;

    public OnItemGiveListener itemGiveListener;

    public BigDecimal maxCount = BigDecimal.ZERO;

    public boolean mReasonSwitch;//理由开关

    public boolean isNeedReturnInventory = true;//是否需要退回库存

    public List<InventoryItem> mInventoryItemList;//库存数据

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (maxCount.compareTo(BigDecimal.ZERO) <= 0) {
            ToastUtil.showShortToast(R.string.give_limit_must_more_than_0);
            dismiss();
            return;
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

    @AfterViews
    void initView() {
        setCount();
//        setReturnInventoryLayout();
        setReasonLayout();
    }

    private void setCount() {
        etCount.setText(MathDecimal.toTrimZeroString(maxCount));
    }

//    private void setReturnInventoryLayout(){
//        if(isNeedReturnInventory && Utils.isNotEmpty(mInventoryItemList)){
//            inventoryLayout.setVisibility(View.VISIBLE);
//            inventoryLayout.refreshView(mInventoryItemList);
//        } else {
//            inventoryLayout.setVisibility(View.GONE);
//        }
//    }

    private void setReasonLayout() {
        if (mReasonSwitch) {
            reasonLayout.setTypeAndStart(ReasonSource.ZHONGMEI.value(), ReasonType.ITEM_GIVE.value());
        } else {
            reasonLayout.setVisibility(View.GONE);
        }
    }

    @Click({R.id.btn_close, R.id.iv_minus, R.id.iv_plus, R.id.btn_ok, R.id.return_inventory_item})
    void click(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                if (itemGiveListener != null) {
                    itemGiveListener.onClose();
                }
                dismiss();
                break;
            case R.id.iv_minus:
                BigDecimal count = new BigDecimal(etCount.getText().toString());
                count = count.subtract(BigDecimal.ONE);
                if (count.compareTo(BigDecimal.ZERO) <= 0) {
                    ToastUtil.showShortToast(R.string.give_must_more_than_0);
                    return;
                }

                etCount.setText(MathDecimal.toTrimZeroString(count));
                break;
            case R.id.iv_plus:
                count = new BigDecimal(etCount.getText().toString());
                count = count.add(BigDecimal.ONE);
                if (count.compareTo(maxCount) > 0) {
                    count = maxCount;
                    ToastUtil.showShortToast(R.string.give_must_less_than_dish_count);
                }

                etCount.setText(MathDecimal.toTrimZeroString(count));
                break;
            case R.id.btn_ok:
                String text = etCount.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    ToastUtil.showShortToast(R.string.give_cannot_be_empty);
                    return;
                }

                count = new BigDecimal(text);
                if (count.compareTo(BigDecimal.ZERO) <= 0) {
                    ToastUtil.showShortToast(R.string.give_must_more_than_0);
                    return;
                }

                if (count.compareTo(maxCount) > 0) {
                    ToastUtil.showShortToast(R.string.give_must_less_than_dish_count);
                    return;
                }

                Reason reason = null;
                if (mReasonSwitch && (reason = reasonLayout.getReason()) == null) {
                    ToastUtil.showShortToast(R.string.give_reason_cannot_be_empty);
                    return;
                }

                if (itemGiveListener != null) {
                    List<InventoryItem> inventoryItemList = null;
                    if (isNeedReturnInventory && count.compareTo(maxCount) < 0) {//非全部赠送时，需要退回库存
                        inventoryItemList = mInventoryItemList;
                    }
                    itemGiveListener.onConfirm(reason, count, inventoryItemList);
                }
                dismiss();
                break;
            case R.id.return_inventory_item:
                ReturnInventoryDialogFragment dialogFragment = new ReturnInventoryDialogFragment();
                dialogFragment.setInventoryItemList(mInventoryItemList);
                dialogFragment.setReturnDishDataListener(new ReturnInventoryDialogFragment.ReturnDishDataListener() {
                    @Override
                    public void setDishData(List<InventoryItem> inventoryItemList) {
                        mInventoryItemList = inventoryItemList;
                        inventoryLayout.refreshView(mInventoryItemList);
                    }
                });
                dialogFragment.show(getFragmentManager(), "returnInventory");
                break;
        }
    }

    @AfterTextChange(R.id.et_count)
    void textChanged(Editable s) {
        if (isNeedReturnInventory) {
            BigDecimal count = BigDecimal.ZERO;

            if (!TextUtils.isEmpty(s)) {
                count = new BigDecimal(s.toString());
            }

            if (Utils.isNotEmpty(mInventoryItemList)) {
                mInventoryItemList.get(0).setMaxInventoryNum(count);
                inventoryLayout.refreshView(mInventoryItemList);
                if (count.compareTo(maxCount) != 0 && InventoryCacheUtil.getInstance().getSaleSwitch()) {
                    inventoryLayout.setVisibility(View.VISIBLE);
                } else {
                    inventoryLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    public interface OnItemGiveListener {
        void onConfirm(Reason reason, BigDecimal count, List<InventoryItem> inventoryItemList);

        void onClose();
    }

}
