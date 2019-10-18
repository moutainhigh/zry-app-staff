package com.zhongmei.bty.dinner.util;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zhongmei.bty.base.MainBaseActivity;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.bty.basemodule.commonbusiness.operates.ReasonDal;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryItem;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryUtils;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlySetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterOperateDialogFragment;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.dinner.Listener.DishOptListener;
import com.zhongmei.bty.dinner.manager.DinnerTradeItemManager;
import com.zhongmei.bty.dinner.orderdish.DinnerDishMiddleFragment;
import com.zhongmei.bty.dinner.orderdish.OrderDishUserdefineGiveDialogFragment;
import com.zhongmei.bty.dinner.orderdish.OrderDishUserdefineGiveDialogFragment_;
import com.zhongmei.bty.dinner.orderdish.manager.DinnerDishManager;
import com.zhongmei.bty.dinner.orderdish.view.ReturnGoodsReasonView;
import com.zhongmei.bty.dinner.orderdish.view.ReturnGoodsReasonView.ConfirmListener;
import com.zhongmei.bty.dinner.orderdish.view.ReturnGoodsReasonView_;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.GuestPrinted;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.IssueStatus;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.UserActionCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhongmei.bty.dinner.orderdish.DinnerDishMiddleFragment.DINNER_ORDER_MODE;
import static com.zhongmei.bty.dinner.orderdish.DinnerDishMiddleFragment.WESTERN_DISH_MODE;

public class TradeItemOperateUtil implements OnClickListener, DishOptListener {
    private static final String TAG = TradeItemOperateUtil.class.getSimpleName();

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

    protected MainBaseActivity mActivity;

    private ReturnGoodsReasonView rgrvReturnReason;

    protected IShopcartItemBase shopcartItemBase;

    protected DishDataItem dishDataItem;

    protected ChangePageListener mChangePageListener;

    private Drawable pauseDrawable;

    private Drawable pausePressedDrawable;

    private Drawable deleteDrawable;

    private Drawable confirmDeleteDrawble;

    private Drawable returnGoodsDrawable;
    private Drawable giveDrawable, cancelGiveDrawable;

    private Map<String, List<TradeItemOperation>> mUnOpItemOperation = new HashMap<>();

    private int deleteMode = -1;
    private int giveMode = -1;

    private final static int DELETE_MODE = 1;    private final static int CONFIRM_DELETE_MODE = 2;    private final static int REFUND_MODE = 3;
    private final static int GIVE_MODE = 1;    private final static int CANCEL_GIVE_MODE = 2;
    boolean isGive = false;    boolean isSingleOrCombo = false;    boolean isUnsaveDish = false;    View parentView;
    DinnerDishMiddleFragment.IChangePageListener mListener;

        private int mCurrentMode = WESTERN_DISH_MODE;

    public void setCurrentMode(int currentMode) {
        mCurrentMode = currentMode;
    }


    public void setOperateButton(FragmentActivity activity, DishDataItem item, View parentView, ChangePageListener changePageListener, DinnerDishMiddleFragment.IChangePageListener listener, boolean isComboEditMode) {
        mActivity = (MainBaseActivity) activity;
        this.parentView = parentView;
        mChangePageListener = changePageListener;
        mListener = listener;
        initView();
        setDishDataItem(item, isComboEditMode);
    }


    public void contorlSlideDishShow() {
        btnGive.setVisibility(View.GONE);
        btnPausePrint.setVisibility(View.GONE);
    }

    public DishDataItem getDishDataItem() {
        return dishDataItem;
    }

    private void setDishDataItem(DishDataItem dishDataItem, boolean isComboEditMode) {
        this.dishDataItem = dishDataItem;
        if (dishDataItem != null) {
            shopcartItemBase = dishDataItem.getBase();
        }

        isGive = false;
        isSingleOrCombo = false;
        isUnsaveDish = false;
        ItemType itemType = dishDataItem.getType();
        switch (itemType) {
            case SINGLE_DISCOUNT:
            case COMBO_DISCOUNT:
                isGive = true;
                break;
            case SINGLE:
            case COMBO:
                isSingleOrCombo = true;
                if (shopcartItemBase.getId() == null) {
                    isUnsaveDish = true;
                }
                break;
            case CHILD:
            case WEST_CHILD:
                if (shopcartItemBase.getId() == null) {
                    isUnsaveDish = true;
                }
                break;
            default:
                break;
        }
                boolean isChild = false;
        if (dishDataItem.getType() == ItemType.WEST_CHILD || dishDataItem.getType() == ItemType.CHILD) {
            isChild = true;
        }
        if (isUnsaveDish) {
            showUnSave();
        } else if (TextUtils.isEmpty(shopcartItemBase.getBatchNo())) {
            showUnPrint(isChild);
        } else if (!TextUtils.isEmpty(shopcartItemBase.getBatchNo())) {
            if ((dishDataItem.getBase().getStatusFlag() == StatusFlag.INVALID) &&
                    (dishDataItem.getBase().getInvalidType() != InvalidType.SPLIT)) {
                if (dishDataItem.getType() != ItemType.CHILD) {
                                        showReturned();
                }
            } else {
                if (!TextUtils.isEmpty(dishDataItem.getBase().getRelateTradeItemUuid())
                        && dishDataItem.getBase().getSingleQty().compareTo(BigDecimal.ZERO) == 0) {
                    showReturned();
                } else {
                    showPrinted(isChild);
                }
            }
        }
        arrangeBtns();
        if (dishDataItem.getType() != ItemType.COMBO && (mCurrentMode == WESTERN_DISH_MODE || mCurrentMode == DINNER_ORDER_MODE) && !DinnerShoppingCart.getInstance().isReturnCash() && !isComboEditMode) {
            initWakeupAndMakeStatus(shopcartItemBase);
        } else {
            btnWakeup.setVisibility(View.GONE);
            btnMake.setVisibility(View.GONE);
            btnWakeupCancel.setVisibility(View.GONE);
            btnMakeCancel.setVisibility(View.GONE);
        }
    }

    private void initWakeupAndMakeStatus(IShopcartItemBase item) {
        if (dishDataItem.getType() != ItemType.SINGLE && dishDataItem.getType() != ItemType.COMBO && dishDataItem.getType() != ItemType.WEST_CHILD && dishDataItem.getType() != ItemType.CHILD) {
            return;
        }

        if (DinnerTradeItemManager.getInstance().getDishCheckStatus(dishDataItem, PrintOperationOpType.WAKE_UP) == DishDataItem.DishCheckStatus.NOT_CHECK)
            btnWakeup.setEnabled(true);
        else
            btnWakeup.setEnabled(false);
        if (DinnerTradeItemManager.getInstance().getDishCheckStatus(dishDataItem, PrintOperationOpType.WAKE_UP_CANCEL) == DishDataItem.DishCheckStatus.NOT_CHECK)
            btnWakeupCancel.setVisibility(View.VISIBLE);
        else
            btnWakeupCancel.setVisibility(View.GONE);
        if (DinnerTradeItemManager.getInstance().getDishCheckStatus(dishDataItem, PrintOperationOpType.RISE_DISH) == DishDataItem.DishCheckStatus.NOT_CHECK)
            btnMake.setEnabled(true);
        else
            btnMake.setEnabled(false);
        if (DinnerTradeItemManager.getInstance().getDishCheckStatus(dishDataItem, PrintOperationOpType.RISE_DISH_CANCEL) == DishDataItem.DishCheckStatus.NOT_CHECK)
            btnMakeCancel.setVisibility(View.VISIBLE);
        else
            btnMakeCancel.setVisibility(View.GONE);

        if (DinnerTradeItemManager.getInstance().getDishCheckStatus(dishDataItem, PrintOperationOpType.REMIND_DISH) == DishDataItem.DishCheckStatus.NOT_CHECK)
            btnRemind.setVisibility(View.VISIBLE);
        else
            btnRemind.setVisibility(View.GONE);
    }


    private void setPauseButton() {
                if (isSingleOrCombo && TextUtils.isEmpty(shopcartItemBase.getBatchNo())) {
            if (shopcartItemBase.getIssueStatus() == IssueStatus.PAUSE) {
                setPausePrintBtn(true);
            } else {
                setPausePrintBtn(false);
            }
        } else {
            btnPausePrint.setVisibility(View.GONE);
        }
    }


    private boolean arrangeBtns() {
        if (shopcartItemBase == null) {
            return false;
        }

                if (DinnerShoppingCart.getInstance().isReturnCash()) {
            btnPausePrint.setVisibility(View.GONE);
            btnReprintKitchenAll.setVisibility(View.GONE);
            btnReprintKitchenCell.setVisibility(View.GONE);
        }

                if (isSingleOrCombo && shopcartItemBase.getStatusFlag() == StatusFlag.VALID) {
            isShowGiveView();
            boolean hasGiven = DinnerTradeItemManager.getInstance().hasGiven(shopcartItemBase);
            setGiveBtn(hasGiven ? CANCEL_GIVE_MODE : GIVE_MODE);
        } else {
            btn_replace.setVisibility(View.GONE);
            btnGive.setVisibility(View.GONE);
        }

                if (shopcartItemBase.getShopcartItemType() == ShopcartItemType.SUBBATCH
                || shopcartItemBase.getShopcartItemType() == ShopcartItemType.SUBBATCHMODIFY) {
            btnReprintKitchenAll.setVisibility(View.GONE);
            btnReprintKitchenCell.setVisibility(View.GONE);
        }

        return true;
    }

    private void initView() {
        btnDeleteItem = (Button) parentView.findViewById(R.id.btn_delete);
        btnPausePrint = (Button) parentView.findViewById(R.id.btn_pause);
        btnGive = (Button) parentView.findViewById(R.id.btn_give);
                btnReprintKitchenCell = (Button) parentView.findViewById(R.id.btn_reprint_kitchen_cell);
                btnReprintKitchenAll = (Button) parentView.findViewById(R.id.btn_reprint_kitchen_all);
        btn_replace = (Button) parentView.findViewById(R.id.btn_replace);

        btnWakeup = (Button) parentView.findViewById(R.id.btn_prepare);
        btnMake = (Button) parentView.findViewById(R.id.btn_make);
        btnWakeupCancel = (Button) parentView.findViewById(R.id.btn_cancel_prepare);
        btnMakeCancel = (Button) parentView.findViewById(R.id.btn_cancel_make);
        btnRemind = (Button) parentView.findViewById(R.id.btn_remind);

        btnDeleteItem.setOnClickListener(this);
        btnPausePrint.setOnClickListener(this);
        btnGive.setOnClickListener(this);
        btnReprintKitchenCell.setOnClickListener(this);
        btnReprintKitchenAll.setOnClickListener(this);
        btn_replace.setOnClickListener(this);

        btnMake.setOnClickListener(this);
        btnMakeCancel.setOnClickListener(this);
        btnWakeup.setOnClickListener(this);
        btnWakeupCancel.setOnClickListener(this);
        btnRemind.setOnClickListener(this);

        initDrawable();
    }

    private void initDrawable() {
                pauseDrawable = mActivity.getResources().getDrawable(R.drawable.dinner_pause_print);
        pauseDrawable.setBounds(0, 0, pauseDrawable.getMinimumWidth(), pauseDrawable.getMinimumHeight());
        pausePressedDrawable = mActivity.getResources().getDrawable(R.drawable.dinner_resume_print);
        pausePressedDrawable.setBounds(0, 0, pausePressedDrawable.getMinimumWidth(), pausePressedDrawable.getMinimumHeight());
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


    private void showUnSave() {
        btnDeleteItem.setVisibility(View.VISIBLE);
        setDeleteBtn(DELETE_MODE);
        isShowGiveView();
        btnReprintKitchenCell.setVisibility(View.GONE);
        btnReprintKitchenAll.setVisibility(View.GONE);
        btnPausePrint.setVisibility(View.VISIBLE);
        setPauseButton();
    }


    private void showUnPrint(boolean isWestChild) {
        setDeleteBtn(DELETE_MODE);
        isShowGiveView();
        btnReprintKitchenCell.setVisibility(View.GONE);
        btnReprintKitchenAll.setVisibility(View.GONE);
        if (isWestChild) {
            btnDeleteItem.setVisibility(View.GONE);
            btnPausePrint.setVisibility(View.GONE);
        } else {
            btnDeleteItem.setVisibility(View.VISIBLE);
            btnPausePrint.setVisibility(View.VISIBLE);
        }
        setPauseButton();
    }



    private void showPrinted(boolean isWestChild) {
        setDeleteBtn(REFUND_MODE);
        if (isWestChild) {
            btnDeleteItem.setVisibility(View.GONE);
        } else {
            btnDeleteItem.setVisibility(View.VISIBLE);
        }
        isShowGiveView();
        if (isSingleOrCombo) {
            btnReprintKitchenCell.setVisibility(View.VISIBLE);
            btnReprintKitchenAll.setVisibility(View.VISIBLE);
        } else {
            btnReprintKitchenCell.setVisibility(View.GONE);
            btnReprintKitchenAll.setVisibility(View.GONE);
        }
        btnPausePrint.setVisibility(View.GONE);
    }


    private void showReturned() {
        btnDeleteItem.setVisibility(View.GONE);
        btnGive.setVisibility(View.GONE);
        btnReprintKitchenCell.setVisibility(View.VISIBLE);
        btnReprintKitchenAll.setVisibility(View.VISIBLE);
        btnPausePrint.setVisibility(View.GONE);
    }

    private void isShowGiveView() {
        if (dishDataItem != null && dishDataItem.getBase().getShopcartItemType() == ShopcartItemType.MAINBATCH) {
            btnGive.setVisibility(View.GONE);
        } else {
            btnGive.setVisibility(View.VISIBLE);
        }
    }


    private void setDeleteBtn(int mode) {
        deleteMode = mode;
        btnDeleteItem.setVisibility(View.VISIBLE);
        switch (mode) {
            case DELETE_MODE:
                btnDeleteItem.setCompoundDrawables(null, deleteDrawable, null, null);
                btnDeleteItem.setText(R.string.delete_item);
                btnDeleteItem.setTextColor(mActivity.getResources().getColor(R.color.text_pay_other_black));
                break;
            case CONFIRM_DELETE_MODE:
                btnDeleteItem.setCompoundDrawables(null, confirmDeleteDrawble, null, null);
                btnDeleteItem.setText(R.string.confirm_delete);
                btnDeleteItem.setTextColor(mActivity.getResources().getColor(R.color.text_red_2));
                break;
            case REFUND_MODE:
                btnDeleteItem.setCompoundDrawables(null, returnGoodsDrawable, null, null);
                btnDeleteItem.setText(R.string.return_goods);
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


    private void showReturnInventoryDialog() {
        rgrvReturnReason = new ReturnGoodsReasonView_();
        rgrvReturnReason.isShowReturnInventoryLayout = true;
        rgrvReturnReason.setDishDataItem(dishDataItem);
        rgrvReturnReason.setInventoryListener(new ReturnGoodsReasonView.ReturnInventoryListener() {
            @Override
            public void onConfirm(List<InventoryItem> inventoryItemList) {
                if (mActivity != null && mActivity.isDestroyed()) {
                    return;
                }
                doDelete(DinnerTradeItemManager.DELETE_TAKE_EFFECT, shopcartItemBase, inventoryItemList);
            }
        });
        rgrvReturnReason.show(mActivity.getSupportFragmentManager(), "return_reason");
    }


    private void showReturnInVentoryOrReasonDialog(final boolean isShowReturnInventoryLayout, final boolean isShowReason) {
        rgrvReturnReason = new ReturnGoodsReasonView_();
        rgrvReturnReason.isShowReturnInventoryLayout = isShowReturnInventoryLayout;
        rgrvReturnReason.isShowReason = isShowReason;
        rgrvReturnReason.setDishDataItem(dishDataItem);
        rgrvReturnReason.setConfirmListener(new ConfirmListener() {

            @Override
            public void onConfirm(BigDecimal returnCount, Reason reason, List<InventoryItem> inventoryItemList) {
                if (mActivity != null && mActivity.isDestroyed()) {
                    return;
                }

                doReturnItem(returnCount, reason, inventoryItemList);
            }
        });
        rgrvReturnReason.show(mActivity.getSupportFragmentManager(), "return_reason");
    }



    private void doReturnItem(BigDecimal returnCount, Reason returnReason, final List<InventoryItem> inventoryItemList) {
        TradeReasonRel tradeReasonRel = null;
        if (returnReason != null) {
            tradeReasonRel = new TradeReasonRel();
            tradeReasonRel.setReasonId(returnReason.getId());
            tradeReasonRel.setReasonContent(returnReason.getContent());
        }
        ShopcartItemUtils.splitBatchItem(shopcartItemBase);
        final IShopcartItem shopcartItem = ((IShopcartItem) shopcartItemBase).returnQty(returnCount.negate(), tradeReasonRel);

                if (DinnerTradeItemManager.getInstance().hasGiven(shopcartItemBase)) {
            Reason giveReason = null;
            if (shopcartItem.getDiscountReasonRel() != null) {
                giveReason = new Reason();
                giveReason.setContent(shopcartItem.getDiscountReasonRel().getReasonContent());
                giveReason.setId(shopcartItem.getDiscountReasonRel().getReasonId());
            }
            doGive(shopcartItem, giveReason);
        }
        TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();

        DinnerShoppingCart.getInstance().returnQTY(shopcartItem);

        if (tradeVo != null && tradeVo.getTrade() != null && tradeVo.getTrade().getBusinessType() == BusinessType.BUFFET) {
            DinnerTradeItemManager.getInstance().showBuffetTradeAmountNegativeDialog(mActivity, new Runnable() {
                @Override
                public void run() {
                    DinnerShoppingCart.getInstance().removeDinnerShoppingcartItem(shopcartItem,
                            null, mChangePageListener, mActivity.getSupportFragmentManager());
                }
            }, new Runnable() {
                @Override
                public void run() {
                    DinnerTradeItemManager.getInstance().doChangeInventory(DinnerTradeItemManager.CANCELLED, shopcartItem, inventoryItemList);
                }
            });
        } else {
            DinnerTradeItemManager.getInstance().showTradeAmountNegativeDialog(mActivity, new Runnable() {
                @Override
                public void run() {
                    DinnerShoppingCart.getInstance().removeDinnerShoppingcartItem(shopcartItem,
                            null, mChangePageListener, mActivity.getSupportFragmentManager());
                }
            }, new Runnable() {
                @Override
                public void run() {
                    DinnerTradeItemManager.getInstance().doChangeInventory(DinnerTradeItemManager.CANCELLED, shopcartItem, inventoryItemList);
                }
            });
        }
    }

    private void doClose() {
        if (mListener != null) {
            mListener.closePage(null);
        }
    }


    private void setPausePrintBtn(boolean isPause) {
        btnPausePrint.setVisibility(View.VISIBLE);
        if (isPause) {
            btnPausePrint.setCompoundDrawables(null, pausePressedDrawable, null, null);
            btnPausePrint.setText(R.string.recover_print);
            btnPausePrint.setTextColor(mActivity.getResources().getColor(R.color.item_operate_text));
        } else {
            btnPausePrint.setCompoundDrawables(null, pauseDrawable, null, null);
            btnPausePrint.setText(R.string.pause_print);
            btnPausePrint.setTextColor(mActivity.getResources().getColor(R.color.text_pay_other_black));
        }
    }


    @Override
    public void onClick(View v) {
        if (shopcartItemBase == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_delete:
                if (isUnsaveDish) {                    doDeleteDish();
                    doClose();
                    MobclickAgentEvent.onEvent(UserActionCode.ZC020018);
                    return;
                }

                if (isCanDelete()) {                    if (deleteMode == DELETE_MODE) {
                        setDeleteBtn(CONFIRM_DELETE_MODE);

                        return;
                    }

                    if (deleteMode == CONFIRM_DELETE_MODE) {
                        doDeleteDish();
                        doClose();

                        return;
                    }
                }

                if (isCanReturn()) {                    MobclickAgentEvent.onEvent(UserActionCode.ZC020022);
                    TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
                    if (tradeVo != null && tradeVo.getTrade() != null && tradeVo.getTrade().getBusinessType() == BusinessType.BUFFET) {
                        if (shopcartItemBase.getCouponPrivilegeVo() != null && shopcartItemBase.getCouponPrivilegeVo().isUsed()) {
                            ToastUtil.showShortToast(mActivity.getResources().getString(R.string.toast_coupon_is_cancel_hint));

                            return;
                        }
                    }
                    VerifyHelper.verifyAlert(mActivity, DinnerApplication.PERMISSION_DINNER_RETURN_GOODS,
                            new VerifyHelper.Callback() {
                                @Override
                                public void onPositive(User user, String code, Auth.Filter filter) {
                                    super.onPositive(user, code, filter);
                                    doCancelledDish();
                                    doClose();
                                }
                            });
                }
                break;
            case R.id.btn_pause:
                MobclickAgentEvent.onEvent(UserActionCode.ZC020019);
                ShopcartItemUtils.splitBatchItem(shopcartItemBase);
                IssueStatus issueStatus = shopcartItemBase.getIssueStatus();
                if (issueStatus != IssueStatus.PAUSE) {
                                        DinnerShoppingCart.getInstance().updatePrintStatus(shopcartItemBase, IssueStatus.PAUSE);
                                        setPausePrintBtn(true);
                } else {
                                        DinnerShoppingCart.getInstance().updatePrintStatus(shopcartItemBase, IssueStatus.DIRECTLY);
                                        setPausePrintBtn(false);
                }
                doClose();
                break;
            case R.id.btn_reprint_kitchen_all:
            case R.id.btn_reprint_kitchen_cell:
                MobclickAgentEvent.onEvent(UserActionCode.ZC020023);
                if (shopcartItemBase.getShopcartItemType() == ShopcartItemType.COMMON || shopcartItemBase.getShopcartItemType() == ShopcartItemType.SUB) {
                                        reprintKitchen(v.getId() == R.id.btn_reprint_kitchen_all);
                } else if (shopcartItemBase.getShopcartItemType() == ShopcartItemType.MAINBATCH) {
                                        reprintUnionMainKitchen(v.getId() == R.id.btn_reprint_kitchen_all);
                }
                doClose();
                break;
            case R.id.btn_give:
                MobclickAgentEvent.onEvent(UserActionCode.ZC020021);
                if (DinnerTradeItemManager.getInstance().hasGiven(shopcartItemBase)) {
                    DinnerShoppingCart.getInstance().removeShopcarItemPrivilege(shopcartItemBase);
                    doClose();
                } else {
                                        if (DinnerTradeItemManager.getInstance().hasMarketActivity(shopcartItemBase)) {
                        ToastUtil.showShortToast(R.string.cannot_give);
                        return;
                    }
                    VerifyHelper.verifyAlert(mActivity, DinnerApplication.PERMISSION_DINNER_PRIVILEDGE_PRESENT,
                            new VerifyHelper.Callback() {
                                @Override
                                public void onPositive(User user, String code, Auth.Filter filter) {
                                    super.onPositive(user, code, filter);
                                    ReasonDal reasonDal = OperatesFactory.create(ReasonDal.class);
                                    boolean reasonSwitch = reasonDal.isReasonSwitchOpen(ReasonType.ITEM_GIVE);

                                    if (!DinnerTradeItemManager.isCommonUnweightDish(shopcartItemBase)) {                                        showGiveReasonDialog();
                                    } else {
                                        showUserdefineGiveDialog(shopcartItemBase, reasonSwitch);
                                    }
                                }
                            });
                }
                break;
            case R.id.btn_replace:
                doClose();
                break;
            case R.id.btn_prepare:                  {
                MobclickAgentEvent.onEvent(UserActionCode.ZC020016);
                DinnerShopManager.getInstance().verifyDishInventory(mActivity, DinnerShoppingCart.getInstance().getShoppingCartVo().getListOrderDishshopVo(), new Runnable() {
                    @Override
                    public void run() {
                        List<DishDataItem> dishDataItems = new ArrayList<>();
                        dishDataItems.add(dishDataItem);
                        backuDishpDataItems(dishDataItems);
                        DinnerTradeItemManager.getInstance().dishOperation(PrintOperationOpType.WAKE_UP, dishDataItems, mActivity.getSupportFragmentManager(), TradeItemOperateUtil.this);
                    }
                });
                break;
            }
            case R.id.btn_make:                     {
                MobclickAgentEvent.onEvent(UserActionCode.ZC020017);
                DinnerShopManager.getInstance().verifyDishInventory(mActivity, DinnerShoppingCart.getInstance().getShoppingCartVo().getListOrderDishshopVo(), new Runnable() {
                    @Override
                    public void run() {
                        List<DishDataItem> dishDataItems = new ArrayList<>();
                        dishDataItems.add(dishDataItem);
                        backuDishpDataItems(dishDataItems);
                        DinnerTradeItemManager.getInstance().dishOperation(PrintOperationOpType.RISE_DISH, dishDataItems, mActivity.getSupportFragmentManager(), TradeItemOperateUtil.this);
                    }
                });
                break;
            }
            case R.id.btn_cancel_prepare:               {
                MobclickAgentEvent.onEvent(UserActionCode.ZC020029);
                DinnerShopManager.getInstance().verifyDishInventory(mActivity, DinnerShoppingCart.getInstance().getShoppingCartVo().getListOrderDishshopVo(), new Runnable() {
                    @Override
                    public void run() {
                        List<DishDataItem> dishDataItems = new ArrayList<>();
                        dishDataItems.add(dishDataItem);
                        backuDishpDataItems(dishDataItems);
                        DinnerTradeItemManager.getInstance().dishOperation(PrintOperationOpType.WAKE_UP_CANCEL, dishDataItems, mActivity.getSupportFragmentManager(), TradeItemOperateUtil.this);
                    }
                });
                break;
            }
            case R.id.btn_cancel_make:                  {
                MobclickAgentEvent.onEvent(UserActionCode.ZC020030);
                DinnerShopManager.getInstance().verifyDishInventory(mActivity, DinnerShoppingCart.getInstance().getShoppingCartVo().getListOrderDishshopVo(), new Runnable() {
                    @Override
                    public void run() {
                        List<DishDataItem> dishDataItems = new ArrayList<>();
                        dishDataItems.add(dishDataItem);
                        backuDishpDataItems(dishDataItems);
                        DinnerTradeItemManager.getInstance().dishOperation(PrintOperationOpType.RISE_DISH_CANCEL, dishDataItems, mActivity.getSupportFragmentManager(), TradeItemOperateUtil.this);
                    }
                });
                break;
            }
            case R.id.btn_remind: {
                DinnerShopManager.getInstance().verifyDishInventory(mActivity, DinnerShoppingCart.getInstance().getShoppingCartVo().getListOrderDishshopVo(), new Runnable() {
                    @Override
                    public void run() {
                        List<DishDataItem> dishDataItems = new ArrayList<>();
                        dishDataItems.add(dishDataItem);
                        backuDishpDataItems(dishDataItems);
                        DinnerTradeItemManager.getInstance().dishOperation(PrintOperationOpType.REMIND_DISH, dishDataItems, mActivity.getSupportFragmentManager(), TradeItemOperateUtil.this);
                    }
                });
            }
        }
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
        doClose();
    }

    @Override
    public void onFail(PrintOperationOpType type, List<DishDataItem> dataItems) {
        restoreDishDataItems(dataItems);
    }


    private void needInventoryDeleteOrCancelled() {
                boolean isSave = DinnerTradeItemManager.getInstance().isSaved(dishDataItem);
                boolean isRelate = !TextUtils.isEmpty(dishDataItem.getBase().getRelateTradeItemUuid());
                boolean isPrint = !TextUtils.isEmpty(dishDataItem.getBase().getBatchNo());
        if (isPrint) {
            doCancelledAndReturnInventory();
        }
    }


    private void doDeleteDish() {
        boolean needInventory = true;
                boolean isSave = DinnerTradeItemManager.getInstance().isSaved(dishDataItem);
                boolean isRelate = !TextUtils.isEmpty(dishDataItem.getBase().getRelateTradeItemUuid());
        if (needInventory && mCurrentMode != DinnerDishMiddleFragment.BOOKING_DISH_MODE && mCurrentMode != DinnerDishMiddleFragment.GROUP_SLIDE_MODE) {
            if (!isSave) {
                doDeleteNoTakeEffect(isRelate);
            } else {
                doDeleteTakeEffect(isRelate);
            }
        } else {
            doDelete();
        }
    }


    private void doCancelledDish() {
        boolean needInventory = true;
        if (needInventory && mCurrentMode != DinnerDishMiddleFragment.BOOKING_DISH_MODE && mCurrentMode != DinnerDishMiddleFragment.GROUP_SLIDE_MODE) {
            doCancelledAndReturnInventory();
        } else {
            doCancelled();
        }
    }


    private void doDeleteNoTakeEffect(boolean hasRelateDish) {
        if (!hasRelateDish) {
            doDelete();
        } else {
            doDelete(DinnerTradeItemManager.DELETE_NOT_TAKE_EFFECT, shopcartItemBase, null);
        }
    }


    private void doDeleteTakeEffect(boolean hasRelateDish) {
        if (!hasRelateDish) {
            showReturnInventoryDialog();
        } else {
            doDeleteAddChangeInventory();
        }
    }


    private void doCancelled() {
        ReasonDal reasonDal = OperatesFactory.create(ReasonDal.class);
        boolean needReturnReason = reasonDal.isReasonSwitchOpen(ReasonType.ITEM_RETURN_QTY);
                if (DinnerTradeItemManager.isMustReturnAll(shopcartItemBase) && !needReturnReason) {
            doReturnItem(shopcartItemBase.getTotalQty(), null, null);
        } else {
            showReturnInVentoryOrReasonDialog(false, true);
        }
    }


    private void doCancelledAndReturnInventory() {
        ReasonDal reasonDal = OperatesFactory.create(ReasonDal.class);
        boolean needReturnReason = reasonDal.isReasonSwitchOpen(ReasonType.ITEM_RETURN_QTY);
                if (DinnerTradeItemManager.isMustReturnAll(shopcartItemBase) && !needReturnReason) {
            showReturnInVentoryOrReasonDialog(true, false);
        } else {
            showReturnInVentoryOrReasonDialog(true, true);
        }
    }


    public void doDelete() {
        DinnerTradeItemManager.getInstance().deleteItem(shopcartItemBase,
                dishDataItem.getItem().getUuid(), mChangePageListener, mActivity);

        DinnerTradeItemManager.getInstance().showTradeAmountNegativeDialog(mActivity, new Runnable() {
            @Override
            public void run() {
                DinnerTradeItemManager.getInstance().cancelDeleteItem(shopcartItemBase, dishDataItem.getItem().getUuid());
            }
        });
    }



    public void doDelete(final int type, final IShopcartItemBase shopcartItemBase, final List<InventoryItem> inventoryItemList) {
        ShopcartItemUtils.splitBatchItem(shopcartItemBase);
        DinnerTradeItemManager.getInstance().deleteItem(shopcartItemBase,
                dishDataItem.getItem().getUuid(), mChangePageListener, mActivity);
        DinnerTradeItemManager.getInstance().showTradeAmountNegativeDialog(mActivity, new Runnable() {
            @Override
            public void run() {
                DinnerTradeItemManager.getInstance().cancelDeleteItem(shopcartItemBase, dishDataItem.getItem().getUuid());
            }
        }, new Runnable() {
            @Override
            public void run() {
                DinnerTradeItemManager.getInstance().doChangeInventory(type, shopcartItemBase, inventoryItemList);
            }
        });
    }


    private void doDeleteAddChangeInventory() {
        if (((ReadonlyShopcartItem) shopcartItemBase).tradeItem.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
            showReturnInventoryDialog();
        } else {
            doDelete(DinnerTradeItemManager.DELETE_TAKE_EFFECT, shopcartItemBase, null);
        }
    }


    private void reprintKitchen(boolean isKitchenAll) {
        try {
            if (shopcartItemBase instanceof ReadonlyShopcartItemBase) {
                TradeItem tradeItem = DBHelperManager.queryById(TradeItem.class, shopcartItemBase.getUuid());
                if (tradeItem != null) {
                    ArrayList<String> tradeItemUuids = new ArrayList<String>();
                    tradeItemUuids.add(tradeItem.getUuid());

                    List<TradeItemOperation> tradeItemOperations = new ArrayList<>();
                    TradeItemOperation tradeItemOperation = DinnerDishManager.getInstance().
                            getLastOperation(shopcartItemBase.getTradeItemOperations());
                    if (tradeItemOperation != null) {
                        tradeItemOperations.add(tradeItemOperation);
                    }

                                        IShopcartItem shopcartItem = (IShopcartItem) shopcartItemBase;
                    if (shopcartItem.getSetmealItems() != null) {                        List<? extends ISetmealShopcartItem> chirldList = shopcartItem.getSetmealItems();
                        if (chirldList.size() > 0) {
                            for (ISetmealShopcartItem child : chirldList) {
                                tradeItemUuids.add(child.getUuid());

                                                                TradeItemOperation childTradeItemOperation = DinnerDishManager.getInstance().
                                        getLastOperation(child.getTradeItemOperations());
                                if (childTradeItemOperation != null) {
                                    tradeItemOperations.add(childTradeItemOperation);
                                }
                            }
                        }
                    }
                    if (shopcartItem.isGroupDish()) {
                        TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
                        if (tradeVo != null && tradeVo.getMealShellVo() != null) {
                            tradeItemUuids.add(tradeVo.getMealShellVo().getTradeItem().getUuid());
                        }
                    }
                    Log.i(TAG, "tradeItemUuids size" + tradeItemUuids.size());
                    String tradeUuid = tradeItem.getTradeUuid();


                }
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }




    private void reprintUnionMainKitchen(boolean isKitchenAll) {
        try {
            if (shopcartItemBase instanceof ReadonlyShopcartItemBase) {
                ReadonlyShopcartItemBase readonlyShopcartItemBase = (ReadonlyShopcartItemBase) shopcartItemBase;

                List<TradeItemOperation> tradeItemOperations = new ArrayList<>();
                TradeItemOperation tradeItemOperation = DinnerDishManager.getInstance().
                        getLastOperation(shopcartItemBase.getTradeItemOperations());
                if (tradeItemOperation != null) {
                    tradeItemOperations.add(tradeItemOperation);
                }

                                List<String> itemUuids = new ArrayList<>();
                itemUuids.add(readonlyShopcartItemBase.getUuid());
                if (Utils.isNotEmpty(((ReadonlyShopcartItem) readonlyShopcartItemBase).getSetmealItems())) {
                    for (ReadonlySetmealShopcartItem setmealItem : ((ReadonlyShopcartItem) readonlyShopcartItemBase).getSetmealItems()) {
                        itemUuids.add(setmealItem.getUuid());

                                                TradeItemOperation childTradeItemOperation = DinnerDishManager.getInstance().
                                getLastOperation(setmealItem.getTradeItemOperations());
                        if (childTradeItemOperation != null) {
                            tradeItemOperations.add(childTradeItemOperation);
                        }
                    }
                }

            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }


    private boolean isCanDelete() {
                return isUnsaveDish || isGive ||
                                (TextUtils.isEmpty(shopcartItemBase.getBatchNo())
                        && shopcartItemBase.getStatusFlag() == StatusFlag.VALID && isSingleOrCombo);
    }


    private boolean isCanReturn() {
        return !TextUtils.isEmpty(shopcartItemBase.getBatchNo()) && shopcartItemBase.getStatusFlag() == StatusFlag.VALID
                && shopcartItemBase.getSingleQty().compareTo(BigDecimal.ZERO) > 0 && isSingleOrCombo;
    }


    private void showGiveReasonDialog() {
        OrderCenterOperateDialogFragment dialog = new OrderCenterOperateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", ReasonType.ITEM_GIVE.value());
        dialog.setArguments(bundle);
        dialog.registerListener(new OrderCenterOperateDialogFragment.OperateListener() {
            @Override
            public boolean onSuccess(OrderCenterOperateDialogFragment.OperateResult result) {
                Reason mReason = result.reason;
                doGive(shopcartItemBase, mReason);
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

    private void showUserdefineGiveDialog(final IShopcartItemBase shopcartItemBase, boolean reasonSwitch) {
        OrderDishUserdefineGiveDialogFragment dialog = new OrderDishUserdefineGiveDialogFragment_();
        dialog.maxCount = shopcartItemBase.getSingleQty();
        dialog.mReasonSwitch = reasonSwitch;
        dialog.isNeedReturnInventory = shopcartItemBase.getId() != null;        if (dialog.isNeedReturnInventory) {
            dialog.mInventoryItemList = Utils.asList(InventoryUtils.transformInventoryItem(dishDataItem, shopcartItemBase.getTotalQty()));
        }
        dialog.itemGiveListener = new OrderDishUserdefineGiveDialogFragment.OnItemGiveListener() {
            @Override
            public void onConfirm(Reason reason, BigDecimal count, List<InventoryItem> inventoryItems) {
                if (count.compareTo(shopcartItemBase.getSingleQty()) == 0) {
                    doGive(shopcartItemBase, reason);
                } else {
                    doUserdefineGive(shopcartItemBase, reason, count, inventoryItems);
                }
                doClose();
            }

            @Override
            public void onClose() {
                doClose();
            }
        };
        dialog.show(mActivity.getSupportFragmentManager(), "userdefine_give");
    }


    protected void doGive(final IShopcartItemBase shopcartItemBase, Reason reason) {
        ShopcartItemUtils.splitBatchItem(shopcartItemBase);
        DinnerTradeItemManager.getInstance().give(shopcartItemBase, reason);

        DinnerTradeItemManager.getInstance().showTradeAmountNegativeDialog(mActivity, new Runnable() {
            @Override
            public void run() {
                DinnerShoppingCart.getInstance().removeShopcarItemPrivilege(shopcartItemBase);
            }
        });
    }


    private void doUserdefineGive(final IShopcartItemBase shopcartItemBase, Reason reason, BigDecimal giveCount,
                                  List<InventoryItem> inventoryItems) {
        if (!(shopcartItemBase instanceof IShopcartItem)) {
            return;
        }

        ShopcartItemUtils.splitBatchItem(shopcartItemBase);

        BigDecimal leftCount = shopcartItemBase.getSingleQty().subtract(giveCount);
        if (shopcartItemBase instanceof ReadonlyShopcartItem) {
                        ReadonlyShopcartItem newShopcartItem = ShopcartItemUtils.shopcartItemCopy((ReadonlyShopcartItem) shopcartItemBase);
                        newShopcartItem.setGuestPrinted(GuestPrinted.PRINTED);
            ShopcartItemUtils.modifyReadonlyItemByQty(newShopcartItem, giveCount);
                        ShopcartItemUtils.modifyReadonlyItemByQty((ReadonlyShopcartItem) shopcartItemBase, leftCount);
            if (newShopcartItem != null) {
                                DinnerShoppingCart.getInstance().returnQTY(newShopcartItem);
                DinnerTradeItemManager.getInstance().give(newShopcartItem, reason);
            }

                        if (Utils.isNotEmpty(inventoryItems)) {
                DinnerShoppingCart.getInstance().addReturnInventoryList(inventoryItems);
            }
        } else {
                        ShopcartItem newShopcartItem = ShopcartItemUtils.shopcartItemCopy((ShopcartItem) shopcartItemBase);
            newShopcartItem.changeQty(giveCount);
                        ((ShopcartItem) shopcartItemBase).changeQty(leftCount);
            if (newShopcartItem != null) {
                                DinnerShoppingCart.getInstance().addDishToShoppingCart(newShopcartItem, false);
                DinnerTradeItemManager.getInstance().give(newShopcartItem, reason);
            }
        }


    }




}