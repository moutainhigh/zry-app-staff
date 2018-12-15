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

/**
 * 菜品批量操作工具类，支持删除／作废和赠送操作
 * Created by demo on 2018/12/15
 */

public class BatchTradeItemOperateUtil implements View.OnClickListener {

    private final static int DELETE_OR_RETURN_MODE = 1;//删除／作废
//    private final static int CONFIRM_DELETE_MODE = 2;//确认删除
//    private final static int REFUND_MODE = 3;//作废商品

    private final static int GIVE_MODE = 1;//赠送
    private final static int CANCEL_GIVE_MODE = 2;//取消赠送

    private FragmentActivity mActivity;

    private View mParentView;

    //删除／作废商品
    private Button btnDeleteItem;
    //暂停／恢复打印
    private Button btnPausePrint;
    // 重打厨总
    private Button btnReprintKitchenAll;
    // 重打堂口
    private Button btnReprintKitchenCell;
    // 赠送
    private Button btnGive;

    private Button btnWakeup;
    private Button btnMake;

    private Drawable deleteDrawable, confirmDeleteDrawble, returnGoodsDrawable, giveDrawable, cancelGiveDrawable;

    //退菜理由框
    private ReturnGoodsReasonView rgrvReturnReason;

    private ChangePageListener mChangePageListener;

    private DinnerDishMiddleFragment.IChangePageListener mListener;

    private List<DishDataItem> items;

    private List<InventoryItem> inventoryItemList;

    private List<DishDataItem> takeEffectItems;//已生效（已出单&&已生效未出单）
    private List<DishDataItem> notTakeEffectItems;//未生效和已生效数量为0
    private List<DishDataItem> unPrintAddTakeEffectItems;//已生效未出单
    private List<DishDataItem> printedItems;//已出单

    boolean hasSavedItem = false;//有没有未出单（已经生效）的菜品
    boolean hasPrintedItem = false;//有没有已打印的菜品
    boolean hasUngiveItem = false;//有没有未赠送的菜品

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
        //批量模式下，只展示删除／作废和赠送／取消赠送
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
        // 初始化按钮图标
//        pauseDrawable = mActivity.getResources().getDrawable(R.drawable.dinner_pause_print);
//        pauseDrawable.setBounds(0, 0, pauseDrawable.getMinimumWidth(), pauseDrawable.getMinimumHeight());
//        pausePressedDrawable = mActivity.getResources().getDrawable(R.drawable.dinner_resume_print);
//        pausePressedDrawable.setBounds(0, 0, pausePressedDrawable.getMinimumWidth(), pausePressedDrawable.getMinimumHeight());
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
                //无效菜品不计入判断,并移出
                if (shopcartItemBase.getStatusFlag() == StatusFlag.INVALID) {
                    items.remove(i);
                    continue;
                }

                //已出单数目为0的菜品不计入判断，并移出
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
        notTakeEffectItems = new ArrayList<>();//未生效
        takeEffectItems = new ArrayList<>();//已生效
        unPrintAddTakeEffectItems = new ArrayList<>();//未打印已生效
        printedItems = new ArrayList<>();//已出单
        if (Utils.isNotEmpty(items)) {
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
                    //记录已打印的菜品，供批量退菜使用
                    takeEffectItems.add(item);
                    printedItems.add(item);
                }
            }
        }
    }

    /**
     * 设置删除按钮样式
     *
     * @param mode 1表示删除品项，2表示确认删除，3表示作废商品
     */
    private void setDeleteBtn(int mode) {
        switch (mode) {
            case DELETE_OR_RETURN_MODE:
                btnDeleteItem.setCompoundDrawables(null, deleteDrawable, null, null);
                btnDeleteItem.setText(R.string.delete_or_return_item);
                btnDeleteItem.setTextColor(mActivity.getResources().getColor(R.color.text_pay_other_black));
                break;
//            case CONFIRM_DELETE_MODE:
//                btnDeleteItem.setCompoundDrawables(null, confirmDeleteDrawble, null, null);
//                btnDeleteItem.setText(R.string.confirm_delete);
//                btnDeleteItem.setTextColor(mActivity.getResources().getColor(R.color.text_red_2));
//                break;
//            case REFUND_MODE:
//                btnDeleteItem.setCompoundDrawables(null, returnGoodsDrawable, null, null);
//                btnDeleteItem.setText(R.string.return_goods);
//                btnDeleteItem.setTextColor(mActivity.getResources().getColor(R.color.text_pay_other_black));
//                break;
            default:
                btnDeleteItem.setCompoundDrawables(null, deleteDrawable, null, null);
                btnDeleteItem.setText(R.string.delete_or_return_item);
                btnDeleteItem.setTextColor(mActivity.getResources().getColor(R.color.text_pay_other_black));
                break;
        }
    }

    /**
     * 设置赠送按钮样式
     *
     * @param mode 1表示赠送，2表示取消赠送
     */
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
                if (hasSavedItem || hasPrintedItem) {//有未出单／已出单的菜品，进行权限验证
                    VerifyHelper.verifyAlert(mActivity, DinnerApplication.PERMISSION_DINNER_BATCH_DELETE_OR_RETURN,
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

    /**
     * 删除／作废商品
     */
    private void deleteOrReturnItems() {
        deleteItems(notTakeEffectItems);
        returnTakeEffectItems(takeEffectItems);
    }

    /**
     * 对未生效的菜品进行删除操作
     *
     * @param notTakeEffectItems
     */
    private void deleteItems(List<DishDataItem> notTakeEffectItems) {
        if (Utils.isEmpty(notTakeEffectItems)) {
            return;
        }

        for (DishDataItem item : notTakeEffectItems) {
            IShopcartItemBase shopcartItemBase = item.getBase();
            IShopcartItem shopcartItem = item.getItem();
            //删除品项
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

    /**
     * 关于有关联菜品的未出单的菜品进行删菜
     * 改单是要扣减库存
     */
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

    /**
     * 对已生效的菜品进行批量退菜
     *
     * @param takeEffectItems
     */
    private void returnTakeEffectItems(List<DishDataItem> takeEffectItems) {
        if (Utils.isEmpty(takeEffectItems)) {
            return;
        }
        ReasonDal reasonDal = OperatesFactory.create(ReasonDal.class);
        boolean needReturnReason = reasonDal.isReasonSwitchOpen(ReasonType.ITEM_RETURN_QTY);
//        boolean needReturnInventory = InventoryCacheUtil.getInstance().getSaleSwitch();
        boolean needReturnInventory = true;//临时方案
        if (needReturnInventory) {
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

    /**
     * 执行批量退菜
     *
     * @param printedItems
     * @param reason
     */
    private void doReturnItems(List<DishDataItem> printedItems, Reason reason) {
        if (Utils.isNotEmpty(printedItems)) {
            for (DishDataItem item : printedItems) {
                IShopcartItemBase shopcartItemBase = item.getBase();
                doReturnItem(shopcartItemBase, shopcartItemBase.getTotalQty(), reason);
            }
        }
    }

    /**
     * 展示退菜理由弹框
     *
     * @param dishDataItemList
     */
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

    /**
     * 退菜结果处理
     *
     * @param shopcartItemBase 原菜
     * @param returnCount      退菜份数
     * @param returnReason     退菜原因
     */
    private void doReturnItem(final IShopcartItemBase shopcartItemBase, BigDecimal returnCount, Reason returnReason) {
        if (!(shopcartItemBase instanceof ReadonlyShopcartItem)) {
            return;
        }

        //构造原因对象
        TradeReasonRel tradeReasonRel = null;
        if (returnReason != null) {
            tradeReasonRel = new TradeReasonRel();
            tradeReasonRel.setReasonId(returnReason.getId());
            tradeReasonRel.setReasonContent(returnReason.getContent());
        }
        //构造退菜新菜
        IShopcartItem shopcartItem = ((IShopcartItem) shopcartItemBase).returnQty(returnCount.negate(), tradeReasonRel);
        //拷贝原菜的赠送
        if (DinnerTradeItemManager.getInstance().hasGiven(shopcartItemBase)) {
            DinnerTradeItemManager.getInstance().give(shopcartItem, null);
        }
        //退菜新菜加入购物车
        DinnerShoppingCart.getInstance().returnQTY(shopcartItem);
    }

    /**
     * 赠送操作
     */
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

    /**
     * 执行批量赠送
     *
     * @param unsendItems
     * @param reason
     */
    private void doGiveItems(List<DishDataItem> unsendItems, Reason reason) {
        for (DishDataItem item : unsendItems) {
            IShopcartItemBase shopcartItemBase = item.getBase();
            DinnerTradeItemManager.getInstance().give(shopcartItemBase, reason);
        }
    }

    /**
     * 执行批量取消赠送
     */
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
