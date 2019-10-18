package com.zhongmei.bty.dinner.util;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.commonbusiness.operates.ReasonDal;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryItem;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterOperateDialogFragment;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlySetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.dinner.manager.DinnerTradeItemManager;
import com.zhongmei.bty.dinner.orderdish.DinnerDishMiddleFragment;
import com.zhongmei.bty.dinner.orderdish.view.ReturnGoodsReasonView;
import com.zhongmei.bty.dinner.orderdish.view.ReturnGoodsReasonView_;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



public class BatchTradeItemOperateUtil implements View.OnClickListener {

    private final static int DELETE_OR_RETURN_MODE = 1;
    private final static int GIVE_MODE = 1;    private final static int CANCEL_GIVE_MODE = 2;
    private FragmentActivity mActivity;

    private View mParentView;

        private Button btnDeleteItem;
        private Button btnPausePrint;
        private Button btnReprintKitchenAll;
        private Button btnReprintKitchenCell;
        private Button btnGive;

    private Button btnWakeup;
    private Button btnMake;

    private Drawable deleteDrawable, confirmDeleteDrawble, returnGoodsDrawable, giveDrawable, cancelGiveDrawable;

        private ReturnGoodsReasonView rgrvReturnReason;

    private ChangePageListener mChangePageListener;

    private DinnerDishMiddleFragment.IChangePageListener mListener;

    private List<DishDataItem> items;

    private List<InventoryItem> inventoryItemList;

    private List<DishDataItem> takeEffectItems;    private List<DishDataItem> notTakeEffectItems;    private List<DishDataItem> unPrintAddTakeEffectItems;    private List<DishDataItem> printedItems;
    boolean hasSavedItem = false;    boolean hasPrintedItem = false;    boolean hasUngiveItem = false;
    public void setOperateButton(FragmentActivity activity, List<DishDataItem> items, View parentView, ChangePageListener changePageListener, DinnerDishMiddleFragment.IChangePageListener listener) {
        mActivity = activity;
        mParentView = parentView;
        mChangePageListener = changePageListener;
        mListener = listener;
        initView();
        setDishDataItems(items);
        initData(items);
    }

    private void initView() {
                btnDeleteItem = (Button) mParentView.findViewById(R.id.btn_delete);
        btnDeleteItem.setVisibility(View.VISIBLE);
        btnPausePrint = (Button) mParentView.findViewById(R.id.btn_pause);
        btnPausePrint.setVisibility(View.GONE);
        btnGive = (Button) mParentView.findViewById(R.id.btn_give);
        btnGive.setVisibility(View.VISIBLE);
        btnReprintKitchenCell = (Button) mParentView.findViewById(R.id.btn_reprint_kitchen_cell);
        btnReprintKitchenCell.setVisibility(View.GONE);
        btnReprintKitchenAll = (Button) mParentView.findViewById(R.id.btn_reprint_kitchen_all);
        btnReprintKitchenAll.setVisibility(View.GONE);

        btnWakeup = (Button) mParentView.findViewById(R.id.btn_prepare);
        btnMake = (Button) mParentView.findViewById(R.id.btn_make);
        btnWakeup.setVisibility(View.GONE);
        btnMake.setVisibility(View.GONE);

        btnDeleteItem.setOnClickListener(this);
        btnGive.setOnClickListener(this);

        initDrawable();
    }

    private void initDrawable() {
                deleteDrawable = mActivity.getResources().getDrawable(R.drawable.dinner_dish_delete);
        deleteDrawable.setBounds(0, 0, deleteDrawable.getMinimumWidth(), deleteDrawable.getMinimumHeight());
        confirmDeleteDrawble = mActivity.getResources().getDrawable(R.drawable.dinner_dish_del_confirm);
        confirmDeleteDrawble.setBounds(0, 0, confirmDeleteDrawble.getMinimumWidth(), confirmDeleteDrawble.getMinimumHeight());
        giveDrawable = mActivity.getResources().getDrawable(R.drawable.dinner_dish_give);
        giveDrawable.setBounds(0, 0, giveDrawable.getMinimumWidth(), giveDrawable.getMinimumHeight());
        cancelGiveDrawable = mActivity.getResources().getDrawable(R.drawable.dinner_dish_give_cancel);
        cancelGiveDrawable.setBounds(0, 0, cancelGiveDrawable.getMinimumWidth(), cancelGiveDrawable.getMinimumHeight());
        returnGoodsDrawable = mActivity.getResources().getDrawable(R.drawable.dinner_dish_return);
        returnGoodsDrawable.setBounds(0, 0, returnGoodsDrawable.getMinimumWidth(), returnGoodsDrawable.getMinimumHeight());
    }

    private void setDishDataItems(List<DishDataItem> items) {
        this.items = items;

        if (Utils.isNotEmpty(items)) {
            for (int i = items.size() - 1; i >= 0; i--) {
                DishDataItem item = items.get(i);
                IShopcartItemBase shopcartItemBase = item.getBase();
                                if (shopcartItemBase.getStatusFlag() == StatusFlag.INVALID) {
                    items.remove(i);
                    continue;
                }

                                if (shopcartItemBase.getSingleQty().compareTo(BigDecimal.ZERO) == 0
                        && !TextUtils.isEmpty(shopcartItemBase.getBatchNo())) {
                    items.remove(i);
                    continue;
                }

                if (!TextUtils.isEmpty(shopcartItemBase.getBatchNo())) {
                    hasPrintedItem = true;
                } else {
                    if (shopcartItemBase.getId() != null && shopcartItemBase.getId() > 0) {
                        hasSavedItem = true;
                    }
                }

                if (!DinnerTradeItemManager.getInstance().hasGiven(shopcartItemBase)) {
                    hasUngiveItem = true;
                }
            }
        }

        setDeleteBtn(DELETE_OR_RETURN_MODE);
        setGiveBtn(hasUngiveItem ? GIVE_MODE : CANCEL_GIVE_MODE);
    }

    private void initData(List<DishDataItem> items) {
        notTakeEffectItems = new ArrayList<>();        takeEffectItems = new ArrayList<>();        unPrintAddTakeEffectItems = new ArrayList<>();        printedItems = new ArrayList<>();        if (Utils.isNotEmpty(items)) {
            for (DishDataItem item : items) {
                IShopcartItemBase shopcartItemBase = item.getBase();
                if (TextUtils.isEmpty(shopcartItemBase.getBatchNo())) {
                    if (shopcartItemBase.getId() == null ||
                            (shopcartItemBase.getId() != null &&
                                    ((ReadonlyShopcartItem) shopcartItemBase).tradeItem.getQuantity().compareTo(BigDecimal.ZERO) == 0)) {
                        notTakeEffectItems.add(item);
                    } else {
                        takeEffectItems.add(item);
                        unPrintAddTakeEffectItems.add(item);
                    }
                } else {
                                        takeEffectItems.add(item);
                    printedItems.add(item);
                }
            }
        }
    }


    private void setDeleteBtn(int mode) {
        switch (mode) {
            case DELETE_OR_RETURN_MODE:
                btnDeleteItem.setCompoundDrawables(null, deleteDrawable, null, null);
                btnDeleteItem.setText(R.string.delete_or_return_item);
                btnDeleteItem.setTextColor(mActivity.getResources().getColor(R.color.text_pay_other_black));
                break;
            default:
                btnDeleteItem.setCompoundDrawables(null, deleteDrawable, null, null);
                btnDeleteItem.setText(R.string.delete_or_return_item);
                btnDeleteItem.setTextColor(mActivity.getResources().getColor(R.color.text_pay_other_black));
                break;
        }
    }


    private void setGiveBtn(int mode) {
        switch (mode) {
            case GIVE_MODE:
                btnGive.setCompoundDrawables(null, giveDrawable, null, null);
                btnGive.setTextColor(mActivity.getResources().getColor(R.color.text_black));
                btnGive.setText(R.string.give);
                break;
            case CANCEL_GIVE_MODE:
                btnGive.setText(R.string.cancel_give);
                btnGive.setCompoundDrawables(null, cancelGiveDrawable, null, null);
                btnGive.setTextColor(mActivity.getResources().getColor(R.color.item_operate_text));
                break;
            default:
                btnGive.setCompoundDrawables(null, giveDrawable, null, null);
                btnGive.setTextColor(mActivity.getResources().getColor(R.color.text_black));
                btnGive.setText(R.string.give);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (Utils.isEmpty(items)) {
            return;
        }

        switch (v.getId()) {
            case R.id.btn_delete:
                if (hasSavedItem || hasPrintedItem) {                    VerifyHelper.verifyAlert(mActivity, DinnerApplication.PERMISSION_DINNER_BATCH_DELETE_OR_RETURN,
                            new VerifyHelper.Callback() {
                                @Override
                                public void onPositive(User user, String code, Auth.Filter filter) {
                                    super.onPositive(user, code, filter);
                                    showDeleteOrReturnDialog();
                                }
                            });
                } else {
                    showDeleteOrReturnDialog();
                }
                break;
            case R.id.btn_give:
                if (hasUngiveItem) {
                    showGiveReasonDialog();
                } else {
                    doCancelGiveItems();
                    doClose();
                }
                break;
        }
    }

    private void showDeleteOrReturnDialog() {
        DialogUtil.showWarnConfirmDialog(mActivity.getSupportFragmentManager(),
                R.string.delete_or_return_item_tip, R.string.dinner_continume, R.string.cancel,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteOrReturnItems();
                        doClose();
                    }
                }, null, "delete_or_return_item_tip");
    }


    private void deleteOrReturnItems() {
        deleteItems(notTakeEffectItems);
        returnTakeEffectItems(takeEffectItems);
    }


    private void deleteItems(List<DishDataItem> notTakeEffectItems) {
        if (Utils.isEmpty(notTakeEffectItems)) {
            return;
        }

        for (DishDataItem item : notTakeEffectItems) {
            IShopcartItemBase shopcartItemBase = item.getBase();
            IShopcartItem shopcartItem = item.getItem();
                        DinnerTradeItemManager.getInstance().deleteItem(shopcartItemBase, shopcartItem.getUuid(),
                    mChangePageListener, mActivity);
            if (!DinnerTradeItemManager.getInstance().isSaved(item) &&
                    !TextUtils.isEmpty(shopcartItemBase.getRelateTradeItemUuid())) {
                DinnerShoppingCart.getInstance().getInventoryVo().getReturnInventoryItemMap().remove(shopcartItemBase.getRelateTradeItemUuid());
            }
            if (DinnerTradeItemManager.getInstance().isSaved(item) &&
                    !TextUtils.isEmpty(shopcartItemBase.getRelateTradeItemUuid())) {
                deductionInventory((ReadonlyShopcartItem) shopcartItemBase);
            }
        }
    }


    private void deductionInventory(ReadonlyShopcartItem shopcartItem) {
        if (shopcartItem == null) return;
        List<TradeItem> tradeItemList = new ArrayList<>();
        TradeItem tradeItem = null;
        ReadonlyShopcartItem readonlyShopcartItem = (ReadonlyShopcartItem) DinnerShoppingCart.getInstance().getIShopcartItemByUUID(
                DinnerShoppingCart.getInstance().getShoppingCartVo(), shopcartItem.getRelateTradeItemUuid());
        if (readonlyShopcartItem != null) {
            tradeItem = readonlyShopcartItem.tradeItem;
            tradeItemList.add(tradeItem);
            if (Utils.isNotEmpty(readonlyShopcartItem.getSetmealItems())) {
                List<ReadonlySetmealShopcartItem> setmealShopcartItemList = readonlyShopcartItem.getSetmealItems();
                for (ReadonlySetmealShopcartItem item : setmealShopcartItemList) {
                    tradeItem = item.tradeItem;
                    tradeItemList.add(tradeItem);
                }
            }
        }
        DinnerShoppingCart.getInstance().getInventoryVo().addNewAddDishList(tradeItemList);
    }


    private void returnTakeEffectItems(List<DishDataItem> takeEffectItems) {
        if (Utils.isEmpty(takeEffectItems)) {
            return;
        }
        ReasonDal reasonDal = OperatesFactory.create(ReasonDal.class);
        boolean needReturnReason = reasonDal.isReasonSwitchOpen(ReasonType.ITEM_RETURN_QTY);
        boolean needReturnInventory = true;        if (needReturnInventory) {
            if (needReturnReason) {
                showReturnReasonDialog(takeEffectItems, true, Utils.isEmpty(printedItems) ? false : true);
            } else {
                showReturnReasonDialog(takeEffectItems, true, false);
            }
        } else {
            deleteItems(unPrintAddTakeEffectItems);
            if (needReturnReason) {
                if (!Utils.isEmpty(printedItems))
                    showReturnReasonDialog(printedItems, false, true);
            } else {
                doReturnItems(printedItems, null);
            }
        }
    }


    private void doReturnItems(List<DishDataItem> printedItems, Reason reason) {
        if (Utils.isNotEmpty(printedItems)) {
            for (DishDataItem item : printedItems) {
                IShopcartItemBase shopcartItemBase = item.getBase();
                doReturnItem(shopcartItemBase, shopcartItemBase.getTotalQty(), reason);
            }
        }
    }


    private void showReturnReasonDialog(final List<DishDataItem> dishDataItemList, final boolean isShowReturnInventoryLayout, final boolean isShowReason) {
        rgrvReturnReason = new ReturnGoodsReasonView_();
        rgrvReturnReason.isBatchMode = true;
        rgrvReturnReason.isShowReturnInventoryLayout = isShowReturnInventoryLayout;
        rgrvReturnReason.isShowReason = isShowReason;
        rgrvReturnReason.setDishDataItemList(dishDataItemList);
        rgrvReturnReason.setConfirmListener(new ReturnGoodsReasonView.ConfirmListener() {

            @Override
            public void onConfirm(BigDecimal returnCount, Reason reason, List<InventoryItem> inventoryItemList) {
                if (mActivity != null && mActivity.isDestroyed()) {
                    return;
                }
                DinnerShoppingCart.getInstance().addReturnInventoryList(inventoryItemList);
                if (!isShowReturnInventoryLayout && isShowReason) {
                    doReturnItems(dishDataItemList, reason);
                    rgrvReturnReason.dismiss();
                    return;
                }
                deleteItems(unPrintAddTakeEffectItems);
                doReturnItems(printedItems, reason);
            }
        });
        rgrvReturnReason.show(mActivity.getSupportFragmentManager(), "returnReason");
    }


    private void doReturnItem(final IShopcartItemBase shopcartItemBase, BigDecimal returnCount, Reason returnReason) {
        if (!(shopcartItemBase instanceof ReadonlyShopcartItem)) {
            return;
        }

                TradeReasonRel tradeReasonRel = null;
        if (returnReason != null) {
            tradeReasonRel = new TradeReasonRel();
            tradeReasonRel.setReasonId(returnReason.getId());
            tradeReasonRel.setReasonContent(returnReason.getContent());
        }
                IShopcartItem shopcartItem = ((IShopcartItem) shopcartItemBase).returnQty(returnCount.negate(), tradeReasonRel);
                if (DinnerTradeItemManager.getInstance().hasGiven(shopcartItemBase)) {
            DinnerTradeItemManager.getInstance().give(shopcartItem, null);
        }
                DinnerShoppingCart.getInstance().returnQTY(shopcartItem);
    }


    private void showGiveReasonDialog() {
        if (Utils.isEmpty(items)) {
            return;
        }

        final List<DishDataItem> unsendItems = new ArrayList<>();
        for (DishDataItem item : items) {
            IShopcartItemBase shopcartItemBase = item.getBase();
            if (!DinnerTradeItemManager.getInstance().hasGiven(shopcartItemBase)) {
                unsendItems.add(item);
            }
        }

        if (Utils.isEmpty(unsendItems)) {
            ToastUtil.showShortToast(R.string.no_dish_can_send);
            return;
        }

        OrderCenterOperateDialogFragment dialog = new OrderCenterOperateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", ReasonType.ITEM_GIVE.value());
        dialog.setArguments(bundle);
        dialog.registerListener(new OrderCenterOperateDialogFragment.OperateListener() {
            @Override
            public boolean onSuccess(OrderCenterOperateDialogFragment.OperateResult result) {
                Reason mReason = result.reason;
                doGiveItems(unsendItems, mReason);
                doClose();
                return true;
            }
        });
        dialog.registerCloseListener(new OrderCenterOperateDialogFragment.OperateCloseListener() {

            @Override
            public void onClose(OrderCenterOperateDialogFragment.OperateResult result) {
                doClose();
            }
        });
        dialog.show(mActivity.getSupportFragmentManager(), "discount");
    }


    private void doGiveItems(List<DishDataItem> unsendItems, Reason reason) {
        for (DishDataItem item : unsendItems) {
            IShopcartItemBase shopcartItemBase = item.getBase();
            DinnerTradeItemManager.getInstance().give(shopcartItemBase, reason);
        }
    }


    private void doCancelGiveItems() {
        if (Utils.isEmpty(items)) {
            return;
        }

        final List<DishDataItem> sendItems = new ArrayList<>();
        for (DishDataItem item : items) {
            IShopcartItemBase shopcartItemBase = item.getBase();
            if (DinnerTradeItemManager.getInstance().hasGiven(shopcartItemBase)) {
                sendItems.add(item);
            }
        }

        if (Utils.isEmpty(sendItems)) {
            return;
        }

        for (DishDataItem item : sendItems) {
            IShopcartItemBase shopcartItemBase = item.getBase();
            DinnerShoppingCart.getInstance().removeShopcarItemPrivilege(shopcartItemBase);
        }
    }

    private void doClose() {
        if (mListener != null) {
            mListener.closePage(null);
        }
    }

}
