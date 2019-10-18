package com.zhongmei.bty.dinner.util;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.dinner.Listener.DishOptListener;
import com.zhongmei.bty.dinner.manager.DinnerTradeItemManager;
import com.zhongmei.bty.dinner.orderdish.DinnerDishMiddleFragment;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.bty.snack.orderdish.buinessview.CustomEmptyView;
import com.zhongmei.bty.snack.orderdish.buinessview.CustomEmptyView_;
import com.zhongmei.bty.snack.orderdish.view.CustomDishQuantityView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CategoryDishOperateUtil implements View.OnClickListener, DishOptListener {

    private static final String TAG = CategoryDishOperateUtil.class.getSimpleName();

    private FragmentActivity mActivity;

    private View mParentView;

        private FrameLayout vContent;
        private CustomDishQuantityView vDishQuantity;
        private Button btnStandard;
        protected Button btnProperty;
        private Button btnRemark;
        protected Button btnExtra;
        private Button btnChangePrice;
        private View vDivideLine;

    private Button btnDeleteItem;

    private Button btnPausePrint;

        private Button btnReprintKitchenAll;

        private Button btnReprintKitchenCell;
        private Button btnGive;
        private Button btn_replace;

    private Button btnWakeup;
    private Button btnWakeupCancel;
    private Button btnMake;
    private Button btnMakeCancel;
    private Button btnRemind;

    private Map<String, List<TradeItemOperation>> mUnOpItemOperation = new HashMap<>();


    private CustomEmptyView mCustomEmptyView;


    private ChangePageListener mChangePageListener;

    private DinnerDishMiddleFragment.IChangePageListener mListener;
    private List<DishDataItem> items;
    private DinnerShoppingCart mShoppingCart;

    private boolean canWakeup = false;
    private boolean canWakeupCancel = false;
    private boolean canMake = false;
    private boolean canMakeCancel = false;
    private boolean canRemind = false;

    public void setOperateButton(FragmentActivity activity, List<DishDataItem> items, View parentView, ChangePageListener changePageListener, DinnerDishMiddleFragment.IChangePageListener listener) {
        mActivity = activity;
        mParentView = parentView;
        mChangePageListener = changePageListener;
        mListener = listener;
        mShoppingCart = DinnerShoppingCart.getInstance();
        initView();
        setDishDataItems(items);
    }

    private void initView() {
        vContent = (FrameLayout) mParentView.findViewById(R.id.v_content);
        vDishQuantity = (CustomDishQuantityView) mParentView.findViewById(R.id.v_dish_quantity);
        btnStandard = (Button) mParentView.findViewById(R.id.btn_standard);
        btnProperty = (Button) mParentView.findViewById(R.id.btn_property);
        btnRemark = (Button) mParentView.findViewById(R.id.btn_remark);
        btnExtra = (Button) mParentView.findViewById(R.id.btn_extra);
        btnChangePrice = (Button) mParentView.findViewById(R.id.btn_change_price);
        vDivideLine = mParentView.findViewById(R.id.v_divide_line);

        btnDeleteItem = (Button) mParentView.findViewById(R.id.btn_delete);
        btnPausePrint = (Button) mParentView.findViewById(R.id.btn_pause);
        btnGive = (Button) mParentView.findViewById(R.id.btn_give);
                btnReprintKitchenCell = (Button) mParentView.findViewById(R.id.btn_reprint_kitchen_cell);
                btnReprintKitchenAll = (Button) mParentView.findViewById(R.id.btn_reprint_kitchen_all);
        btn_replace = (Button) mParentView.findViewById(R.id.btn_replace);

        btnWakeup = (Button) mParentView.findViewById(R.id.btn_prepare);
        btnMake = (Button) mParentView.findViewById(R.id.btn_make);
        btnWakeupCancel = (Button) mParentView.findViewById(R.id.btn_cancel_prepare);
        btnMakeCancel = (Button) mParentView.findViewById(R.id.btn_cancel_make);
        btnRemind = (Button) mParentView.findViewById(R.id.btn_remind);


        vDishQuantity.setVisibility(View.INVISIBLE);
        btnStandard.setVisibility(View.GONE);
        btnChangePrice.setVisibility(View.GONE);
        btnProperty.setVisibility(View.GONE);
        btnRemark.setVisibility(View.GONE);
        btnExtra.setVisibility(View.GONE);
        btnChangePrice.setVisibility(View.GONE);
        vDivideLine.setVisibility(View.GONE);

        btnDeleteItem.setVisibility(View.GONE);
        btnPausePrint.setVisibility(View.GONE);
        btnGive.setVisibility(View.GONE);
        btnReprintKitchenCell.setVisibility(View.GONE);
        btnReprintKitchenAll.setVisibility(View.GONE);
        btn_replace.setVisibility(View.GONE);
        mParentView.findViewById(R.id.btn_seat).setVisibility(View.GONE);


        btnMake.setOnClickListener(this);
        btnMakeCancel.setOnClickListener(this);
        btnWakeup.setOnClickListener(this);
        btnWakeupCancel.setOnClickListener(this);
        btnRemind.setOnClickListener(this);

        if (mCustomEmptyView == null) {
            mCustomEmptyView = CustomEmptyView_.build(mActivity);
        }
        vContent.addView(mCustomEmptyView);

    }

    private void setDishDataItems(List<DishDataItem> items) {
                canMake = false;
        canMakeCancel = false;
        canWakeup = false;
        canWakeupCancel = false;
        canRemind = false;
        this.items = items;
        if (Utils.isNotEmpty(items)) {
            Iterator<DishDataItem> iterator = items.iterator();
            while (iterator.hasNext()) {
                DishDataItem item = iterator.next();
                IShopcartItemBase shopcartItemBase = item.getBase();
                                if (shopcartItemBase == null || shopcartItemBase.getStatusFlag() == StatusFlag.INVALID) {
                    iterator.remove();
                    continue;
                }
                if (item.getType() != ItemType.SINGLE && item.getType() != ItemType.COMBO && item.getType() != ItemType.WEST_CHILD) {
                    continue;
                }


                if (DinnerTradeItemManager.getInstance().getDishCheckStatus(item, PrintOperationOpType.WAKE_UP) == DishDataItem.DishCheckStatus.NOT_CHECK)
                    canWakeup = true;
                if (DinnerTradeItemManager.getInstance().getDishCheckStatus(item, PrintOperationOpType.WAKE_UP_CANCEL) == DishDataItem.DishCheckStatus.NOT_CHECK)
                    canWakeupCancel = true;
                if (DinnerTradeItemManager.getInstance().getDishCheckStatus(item, PrintOperationOpType.RISE_DISH) == DishDataItem.DishCheckStatus.NOT_CHECK)
                    canMake = true;
                if (DinnerTradeItemManager.getInstance().getDishCheckStatus(item, PrintOperationOpType.RISE_DISH_CANCEL) == DishDataItem.DishCheckStatus.NOT_CHECK)
                    canMakeCancel = true;
                if (DinnerTradeItemManager.getInstance().getDishCheckStatus(item, PrintOperationOpType.REMIND_DISH) == DishDataItem.DishCheckStatus.NOT_CHECK)
                    canRemind = true;
            }
        }
        initWakeupAndMakeStatus();
    }

    private void initWakeupAndMakeStatus() {

        if (canWakeup)
            btnWakeup.setEnabled(true);
        else
            btnWakeup.setEnabled(false);
        if (canWakeupCancel)
            btnWakeupCancel.setVisibility(View.VISIBLE);
        else
            btnWakeupCancel.setVisibility(View.GONE);
        if (canMake)
            btnMake.setEnabled(true);
        else
            btnMake.setEnabled(false);
        if (canMakeCancel)
            btnMakeCancel.setVisibility(View.VISIBLE);
        else
            btnMakeCancel.setVisibility(View.GONE);
        if (canRemind)
            btnRemind.setVisibility(View.VISIBLE);
        else
            btnRemind.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_prepare:                  {
                backuDishpDataItems(items);
                DinnerTradeItemManager.getInstance().dishOperation(PrintOperationOpType.WAKE_UP, getOptDish(PrintOperationOpType.WAKE_UP), mActivity.getSupportFragmentManager(), this);
                break;
            }
            case R.id.btn_make:                     {
                backuDishpDataItems(items);
                DinnerTradeItemManager.getInstance().dishOperation(PrintOperationOpType.RISE_DISH, getOptDish(PrintOperationOpType.RISE_DISH), mActivity.getSupportFragmentManager(), this);
                break;
            }
            case R.id.btn_cancel_prepare:               {
                backuDishpDataItems(items);
                DinnerTradeItemManager.getInstance().dishOperation(PrintOperationOpType.WAKE_UP_CANCEL, getOptDish(PrintOperationOpType.WAKE_UP_CANCEL), mActivity.getSupportFragmentManager(), this);
                break;
            }
            case R.id.btn_cancel_make:                  {
                backuDishpDataItems(items);
                DinnerTradeItemManager.getInstance().dishOperation(PrintOperationOpType.RISE_DISH_CANCEL, getOptDish(PrintOperationOpType.RISE_DISH_CANCEL), mActivity.getSupportFragmentManager(), this);
                break;
            }
            case R.id.btn_remind: {
                backuDishpDataItems(items);
                DinnerTradeItemManager.getInstance().dishOperation(PrintOperationOpType.REMIND_DISH, getOptDish(PrintOperationOpType.REMIND_DISH), mActivity.getSupportFragmentManager(), this);
                break;
            }
        }
    }

    private List<DishDataItem> getOptDish(PrintOperationOpType opType) {
        List<DishDataItem> optItems = new ArrayList<>();
        for (DishDataItem item : items) {
            if (DinnerTradeItemManager.getInstance().getDishCheckStatus(item, opType) == DishDataItem.DishCheckStatus.NOT_CHECK)
                optItems.add(item);
        }
        return optItems;
    }

    private void backuDishpDataItems(List<DishDataItem> dataItems) {
        mUnOpItemOperation.clear();
        for (DishDataItem item : dataItems) {
            if (item.getBase() == null)
                continue;
            if (Utils.isNotEmpty(item.getBase().getTradeItemOperations()))
                mUnOpItemOperation.put(item.getBase().getUuid(), new ArrayList<>(item.getBase().getTradeItemOperations()));
            else
                mUnOpItemOperation.put(item.getBase().getUuid(), new ArrayList<TradeItemOperation>());
        }
    }

    private void restoreDishDataItems(List<DishDataItem> dataItems) {
        for (DishDataItem item : dataItems) {
            IShopcartItemBase shopcartItem = item.getBase();
            if (shopcartItem == null)
                continue;
            List<TradeItemOperation> tmp = mUnOpItemOperation.get(shopcartItem.getUuid());
            if (tmp != null)
                shopcartItem.setTradeItemOperations(tmp);
        }
        DinnerShoppingCart.getInstance().updateDinnerDish(dataItems);
    }


    @Override
    public void onSuccess(PrintOperationOpType type, List<DishDataItem> dataItems) {
        if (mListener != null) {
            mListener.closePage(null);
        }
            }

    @Override
    public void onFail(PrintOperationOpType type, List<DishDataItem> dataItems) {
        restoreDishDataItems(dataItems);
    }
}
