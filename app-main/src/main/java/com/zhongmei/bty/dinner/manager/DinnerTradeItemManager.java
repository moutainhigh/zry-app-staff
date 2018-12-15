package com.zhongmei.bty.dinner.manager;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;

import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryItem;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlySetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShoppingCartTool;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.dinner.Listener.DishOptListener;
import com.zhongmei.bty.dinner.orderdish.manager.DinnerDishManager;
import com.zhongmei.bty.dinner.shopcart.adapter.DinnerShopCartAdapter;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.enums.GuestPrinted;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.IssueStatus;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.DialogUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class DinnerTradeItemManager {

    private static DinnerTradeItemManager instance = new DinnerTradeItemManager();

    private DinnerTradeItemManager() {
    }

    public static DinnerTradeItemManager getInstance() {
        return instance;
    }

    //删除未生效操作
    public static final int DELETE_NOT_TAKE_EFFECT = 1;
    //删除为生效操作
    public static final int DELETE_TAKE_EFFECT = 2;
    //赠送操作
    public static final int GIVE = 3;
    //作废操作
    public static final int CANCELLED = 4;

    /**
     * 该菜是否已赠送
     *
     * @return
     */
    public boolean hasGiven(IShopcartItemBase shopcartItemBase) {
        TradePrivilege privilege = shopcartItemBase.getPrivilege();
        return privilege != null
                && (privilege.getPrivilegeType() == PrivilegeType.FREE || privilege.getPrivilegeType() == PrivilegeType.GIVE)
                && privilege.getStatusFlag() == StatusFlag.VALID;
    }

    /**
     * 删除品项
     *
     * @param shopcartItemBase
     * @param parentUuid
     * @param mChangePageListener
     * @param mActivity
     */
    public void deleteItem(IShopcartItemBase shopcartItemBase, String parentUuid,
                           ChangePageListener mChangePageListener, FragmentActivity mActivity) {
        // 删除未下单的子菜（已下单的子菜暂不允许操作）
        if (shopcartItemBase instanceof SetmealShopcartItem) {
            // 先获取外壳
            ShopcartItem shopcartItem = DinnerShoppingCart.getInstance().
                    getShopcartItemByUUID(DinnerShoppingCart.getInstance().getShoppingCartVo(), parentUuid);
            DinnerShoppingCart.getInstance().removeDinnerShoppingcartItem(shopcartItem,
                    (SetmealShopcartItem) shopcartItemBase,
                    mChangePageListener,
                    mActivity.getSupportFragmentManager());

            return;
        }

        // 删除单菜或套餐外壳
        if (shopcartItemBase instanceof IShopcartItem) {
            DinnerShoppingCart.getInstance().removeDinnerShoppingcartItem((IShopcartItem) shopcartItemBase,
                    null, mChangePageListener, mActivity.getSupportFragmentManager());
            // 没批次号的菜就不用更新打印状态，反正都不会打印了
            if (!TextUtils.isEmpty(shopcartItemBase.getBatchNo())) {
                DinnerShoppingCart.getInstance().updatePrintStatus(shopcartItemBase,
                        IssueStatus.DIRECTLY);
            }
            //已下单的菜品，更新客看单状态为未打印
            if (shopcartItemBase instanceof ReadonlyShopcartItem) {
                ((ReadonlyShopcartItem) shopcartItemBase).setGuestPrinted(GuestPrinted.UNPRINT);
            }
        }
    }

    public void give(final IShopcartItemBase shopcartItemBase, Reason reason) {
        TradePrivilege privilege = new TradePrivilege();
        privilege.setPrivilegeName(BaseApplication.sInstance.getString(R.string.give));
        privilege.setPrivilegeType(PrivilegeType.GIVE);
        shopcartItemBase.setPrivilege(privilege);
        DinnerShoppingCart.getInstance().setShopcartItemPrivilege(shopcartItemBase, reason);
    }

    /**
     * 弹出导致金额未负的弹框
     *
     * @param mActivity
     * @param runnable
     */
    public void showTradeAmountNegativeDialog(FragmentActivity mActivity, final Runnable runnable) {
        //退菜导致订单金额为负时，弹框进行提示
        TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
        if (tradeVo != null && tradeVo.getTrade() != null && tradeVo.getTrade().getTradeAmount().compareTo(BigDecimal.ZERO) < 0) {
            DialogUtil.showErrorConfirmDialog(mActivity.getSupportFragmentManager(), R.string.operation_make_amount_negative, R.string.go_on_opearation,
                    R.string.cancel_operation, null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (runnable != null) {
                                runnable.run();
                            }
                        }
                    }, "operation_make_amount_negative");
        }
    }

    /**
     * 弹出导致金额未负的弹框
     * 关于库存的操作
     *
     * @param mActivity
     * @param runnable
     */
    public void showTradeAmountNegativeDialog(FragmentActivity mActivity, final int type, final IShopcartItemBase shopcartItemBase, final List<InventoryItem> inventoryItemList, final Runnable runnable) {
        //退菜导致订单金额为负时，弹框进行提示
        TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
        if (tradeVo != null && tradeVo.getTrade() != null && tradeVo.getTrade().getTradeAmount().compareTo(BigDecimal.ZERO) < 0) {
            DialogUtil.showErrorConfirmDialog(mActivity.getSupportFragmentManager(), R.string.operation_make_amount_negative, R.string.go_on_opearation,
                    R.string.cancel_operation, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            doChangeInventory(type, shopcartItemBase, inventoryItemList);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (runnable != null) {
                                runnable.run();
                            }
                        }
                    }, "operation_make_amount_negative");
        } else {
            doChangeInventory(type, shopcartItemBase, inventoryItemList);
        }
    }

    /**
     * 弹出导致金额未负的弹框
     * 关于库存的操作
     *
     * @param mActivity
     * @param runnable
     */
    public void showTradeAmountNegativeDialog(FragmentActivity mActivity, final Runnable runnable, final Runnable inventoryRunnable) {
        //退菜导致订单金额为负时，弹框进行提示
        TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
        if (tradeVo != null && tradeVo.getTrade() != null && tradeVo.getTrade().getTradeAmount().compareTo(BigDecimal.ZERO) < 0) {
            DialogUtil.showErrorConfirmDialog(mActivity.getSupportFragmentManager(), R.string.operation_make_amount_negative, R.string.go_on_opearation,
                    R.string.cancel_operation, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (inventoryRunnable != null) {
                                inventoryRunnable.run();
                            }
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (runnable != null) {
                                runnable.run();
                            }
                        }
                    }, "operation_make_amount_negative");
        } else {
            if (inventoryRunnable != null) {
                inventoryRunnable.run();
            }
        }
    }

    public void doChangeInventory(int type, IShopcartItemBase shopcartItemBase, List<InventoryItem> inventoryItemList) {
        switch (type) {
            case DELETE_NOT_TAKE_EFFECT:
                DinnerShoppingCart.getInstance().getInventoryVo().getReturnInventoryItemMap().remove(shopcartItemBase.getRelateTradeItemUuid());
                break;
            case DELETE_TAKE_EFFECT:
                if (!TextUtils.isEmpty(shopcartItemBase.getRelateTradeItemUuid())) {
                    deductionInventory(shopcartItemBase);
                }
                DinnerShoppingCart.getInstance().addReturnInventoryList(inventoryItemList);
                break;
            case CANCELLED:
                DinnerShoppingCart.getInstance().addReturnInventoryList(inventoryItemList);
                break;
        }
    }

    /**
     * 关于有关联菜品的未出单的菜品进行删菜
     * 改单是要扣减库存
     */
    private void deductionInventory(IShopcartItemBase shopcartItemBase) {
        List<TradeItem> tradeItemList = new ArrayList<>();
        TradeItem tradeItem = null;
        ReadonlyShopcartItem shopcartItem = (ReadonlyShopcartItem) shopcartItemBase;
        ReadonlyShopcartItem readonlyShopcartItem = (ReadonlyShopcartItem) DinnerShoppingCart.getInstance().getIShopcartItemByUUID(
                DinnerShoppingCart.getInstance().getShoppingCartVo(), shopcartItem.getRelateTradeItemUuid());
        if (readonlyShopcartItem == null) {
            return;
        }
        if (shopcartItemBase.getInvalidType() != null &&
                (shopcartItemBase.getInvalidType() != InvalidType.DELETE_RETURN_QTY && shopcartItemBase.getInvalidType() != InvalidType.DELETE_MODIY_DISH)) {
            return;
        }
        tradeItem = readonlyShopcartItem.tradeItem;
        tradeItemList.add(tradeItem);
        if (Utils.isNotEmpty(readonlyShopcartItem.getSetmealItems())) {
            List<ReadonlySetmealShopcartItem> setmealShopcartItemList = readonlyShopcartItem.getSetmealItems();
            for (ReadonlySetmealShopcartItem item : setmealShopcartItemList) {
                tradeItem = item.tradeItem;
                tradeItemList.add(tradeItem);
            }
        }
        DinnerShoppingCart.getInstance().getInventoryVo().addNewAddDishList(tradeItemList);
    }

    /**
     * 自助弹出导致金额未负的弹框
     *
     * @param mActivity
     * @param runnable
     */
    public void showBuffetTradeAmountNegativeDialog(FragmentActivity mActivity, final Runnable runnable) {
        //退菜导致订单金额为负时，弹框进行提示
        TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
        if (tradeVo == null || tradeVo.getTrade() == null || tradeVo.getTrade().getTradeAmount() == null || tradeVo.getPaidAmount() == null)
            return;
        if (Utils.isNotEmpty(tradeVo.getCouponPrivilegeVoList())) {
            for (CouponPrivilegeVo couponPrivilegeVo : tradeVo.getCouponPrivilegeVoList()) {
                if (couponPrivilegeVo.getCoupon() == null || couponPrivilegeVo.getCoupon().getFullValue() == null || !couponPrivilegeVo.isUsed()) {
                    continue;
                }
                if (couponPrivilegeVo.getCoupon().getFullValue().compareTo(MathShoppingCartTool.couponBeforeAmount) > 0) {
                    DialogUtil.showErrorConfirmDialog(mActivity.getSupportFragmentManager(), R.string.buffet_operation_make_privilege_invalid, R.string.cancel_operation,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (runnable != null) {
                                        runnable.run();
                                    }
                                }
                            }, true, "operation_make_amount_negative");
                    return;
                }
            }
        }
        if (tradeVo.getTrade().getTradeAmount().subtract(tradeVo.getPaidAmount()).compareTo(BigDecimal.ZERO) < 0) {
            DialogUtil.showErrorConfirmDialog(mActivity.getSupportFragmentManager(), R.string.buffet_operation_make_amount_negative, R.string.cancel_operation,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (runnable != null) {
                                runnable.run();
                            }
                        }
                    }, true, "operation_make_amount_negative");
        }
    }

    /**
     * 自助弹出导致金额未负的弹框
     *
     * @param mActivity
     * @param runnable
     */
    public void showBuffetTradeAmountNegativeDialog(FragmentActivity mActivity, final Runnable runnable, final Runnable inventoryRunnable) {
        //退菜导致订单金额为负时，弹框进行提示
        TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
        if (tradeVo == null || tradeVo.getTrade() == null || tradeVo.getTrade().getTradeAmount() == null || tradeVo.getPaidAmount() == null)
            return;
        if (Utils.isNotEmpty(tradeVo.getCouponPrivilegeVoList())) {
            for (CouponPrivilegeVo couponPrivilegeVo : tradeVo.getCouponPrivilegeVoList()) {
                if (couponPrivilegeVo.getCoupon() == null || couponPrivilegeVo.getCoupon().getFullValue() == null || !couponPrivilegeVo.isUsed()) {
                    continue;
                }
                if (couponPrivilegeVo.getCoupon().getFullValue().compareTo(MathShoppingCartTool.couponBeforeAmount) > 0) {
                    DialogUtil.showErrorConfirmDialog(mActivity.getSupportFragmentManager(), R.string.buffet_operation_make_privilege_invalid, R.string.cancel_operation,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (runnable != null) {
                                        runnable.run();
                                    }
                                }
                            }, true, "operation_make_amount_negative");
                    return;
                } else {
                    if (inventoryRunnable != null) {
                        inventoryRunnable.run();
                    }
                }
            }
        }
        if (tradeVo.getTrade().getTradeAmount().subtract(tradeVo.getPaidAmount()).compareTo(BigDecimal.ZERO) < 0) {
            DialogUtil.showErrorConfirmDialog(mActivity.getSupportFragmentManager(), R.string.buffet_operation_make_amount_negative, R.string.cancel_operation,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (runnable != null) {
                                runnable.run();
                            }
                        }
                    }, true, "operation_make_amount_negative");
        } else {
            if (inventoryRunnable != null) {
                inventoryRunnable.run();
            }
        }
    }


    /**
     * 取消对菜品的删除操作
     *
     * @param shopcartItemBase
     * @param parentUuid
     */
    public void cancelDeleteItem(IShopcartItemBase shopcartItemBase, String parentUuid) {
        if (shopcartItemBase instanceof ReadonlyShopcartItem) {
            DinnerShoppingCart.getInstance().recoverInvalidDish((ReadonlyShopcartItem) shopcartItemBase);
        } else if (shopcartItemBase instanceof ShopcartItem) {
            DinnerShoppingCart.getInstance().addDishToShoppingCart((ShopcartItem) shopcartItemBase, false);
        } else if (shopcartItemBase instanceof SetmealShopcartItem) {
            // 先获取外壳
            ShopcartItem shopcartItem = DinnerShoppingCart.getInstance().
                    getShopcartItemByUUID(DinnerShoppingCart.getInstance().getShoppingCartVo(), parentUuid);
            shopcartItem.addSetmeal((SetmealShopcartItem) shopcartItemBase);
            DinnerShoppingCart.getInstance().updateDinnerDish(shopcartItem, false);
        }
    }

    /**
     * 菜品是否参与了营销活动
     *
     * @param shopcartItemBase
     * @return
     */
    public boolean hasMarketActivity(IShopcartItemBase shopcartItemBase) {
        TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
        if (tradeVo != null && Utils.isNotEmpty(tradeVo.getTradeItemPlanActivityList())) {
            for (TradeItemPlanActivity tradeItemPlanActivity : tradeVo.getTradeItemPlanActivityList()) {
                if (tradeItemPlanActivity.getStatusFlag() == StatusFlag.VALID
                        && shopcartItemBase.getUuid().equals(tradeItemPlanActivity.getTradeItemUuid())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 是否为未生效的readonlyshopcartitem
     *
     * @param base
     * @return
     */
    public boolean isUnsavedReadonly(IShopcartItemBase base) {
        if (base != null && (base instanceof ReadonlyShopcartItem) && base.getId() == null) {
            return true;
        }
        return false;
    }

    /**
     * 部分退菜产生的item
     *
     * @param base
     * @return
     */
    public boolean isReturnDish(IShopcartItemBase base) {
        if (base != null && (base instanceof ReadonlyShopcartItem) && !TextUtils.isEmpty(base.getRelateTradeItemUuid())) {
            return true;
        }
        return false;
    }

    /**
     * 是否全退
     *
     * @return
     */
    public boolean isDishReturnAll(DishDataItem dishDataItem) {
        IShopcartItemBase base = dishDataItem.getBase();
        if (base != null && (base instanceof ReadonlyShopcartItem) && !TextUtils.isEmpty(base.getRelateTradeItemUuid())
                && (base.getSingleQty().compareTo(BigDecimal.ZERO) == 0)) {
            return true;
        }
        return false;
    }


    /**
     * 判断当前菜品是否为套餐
     *
     * @return
     */
    public boolean isCombo(ShopcartItemBase<?> realItemBase) {
        if (realItemBase != null) {
            return realItemBase.getOrderDish().isCombo();
        }

        return false;
    }

    /**
     * 是否保存过服务器(未出单或者已出单),并且有效
     *
     * @return
     */
    public boolean isSaved(DishDataItem dishDataItem) {
        if (dishDataItem != null && dishDataItem.getBase() != null) {
            return dishDataItem.getBase().getId() != null;
        }
        return false;
    }

    /**
     * 判断菜品的选中状态
     *
     * @param dataItem
     * @param opType
     * @return
     */
    public DishDataItem.DishCheckStatus getDishCheckStatus(DishDataItem dataItem, PrintOperationOpType opType) {
        if (opType == null) {
            return DishDataItem.DishCheckStatus.INVALIATE_CHECK;
        }
        if (dataItem.getType() == ItemType.WEST_CHILD || dataItem.getType() == ItemType.CHILD) {
            IShopcartItemBase shopcartItem = dataItem.getBase();
            return getShopCartItemCheckStatus(shopcartItem, opType);
        } else if (dataItem.getType() == ItemType.COMBO) {
            IShopcartItem shopcartItem = dataItem.getItem();
            List<? extends ISetmealShopcartItem> iSetmealShopcartItems = shopcartItem.getSetmealItems();
            DishDataItem.DishCheckStatus status = DishDataItem.DishCheckStatus.INVALIATE_CHECK;
            for (ISetmealShopcartItem iSetmealShopcartItem : iSetmealShopcartItems) {
                if (iSetmealShopcartItem != null && getShopCartItemCheckStatus(iSetmealShopcartItem, opType) != DishDataItem.DishCheckStatus.INVALIATE_CHECK)
                    status = getShopCartItemCheckStatus(iSetmealShopcartItem, opType);
                if (status == DishDataItem.DishCheckStatus.CHECKED)
                    return status;
                /*
                if(iSetmealShopcartItem != null && (getShopCartItemCheckStatus(iSetmealShopcartItem, opType) == DishDataItem.DishCheckStatus.NOT_CHECK
                        || getShopCartItemCheckStatus(iSetmealShopcartItem, opType) == DishDataItem.DishCheckStatus.CHECKED))
                    return getShopCartItemCheckStatus(iSetmealShopcartItem, opType);
                */
            }
            return status;
        } else {
            IShopcartItemBase shopcartItem = dataItem.getItem();
            return getShopCartItemCheckStatus(shopcartItem, opType);
        }
    }

    public DishDataItem.DishCheckStatus getShopCartItemCheckStatus(IShopcartItemBase shopcartItem, PrintOperationOpType opType) {
        switch (opType) {
            case WAKE_UP:
                return getWakeUpCheckStatus(shopcartItem);
            case WAKE_UP_CANCEL:
                return getWakeUpCancelCheckStatus(shopcartItem);
            case RISE_DISH:
                return getRiseUpCheckStatus(shopcartItem);
            case RISE_DISH_CANCEL:
                return getRiseUpCancelCheckStatus(shopcartItem);
            case REMIND_DISH:
                return getRemindDishCheckStatus(shopcartItem);
            case BATCH_OPERATION:
                return getBatchOperationCheckStatus(shopcartItem);
            default:
                return DishDataItem.DishCheckStatus.INVALIATE_CHECK;
        }
    }

    /**
     * 获取等叫模式下菜品的选中状态
     *
     * @param shopcartItem
     * @return
     */
    public DishDataItem.DishCheckStatus getWakeUpCheckStatus(IShopcartItemBase shopcartItem) {
        int canWakUp = shopcartItem.canWakeUp();
        switch (canWakUp) {
            case IShopcartItem.NOT_WAKE_UP:// 未等叫
                return DishDataItem.DishCheckStatus.NOT_CHECK;
            case IShopcartItem.HAS_WAKE_UP:// 已等叫
                return DishDataItem.DishCheckStatus.CHECKED;
            case IShopcartItem.CANNOT_WAKE_UP:// 禁用
                return DishDataItem.DishCheckStatus.INVALIATE_CHECK;
            default:
                return DishDataItem.DishCheckStatus.INVALIATE_CHECK;
        }
    }

    /**
     * 获取起菜模式下菜品的选中状态
     *
     * @param shopcartItem
     * @return
     */
    private DishDataItem.DishCheckStatus getRiseUpCheckStatus(IShopcartItemBase shopcartItem) {
        if (shopcartItem.canRiseDish()) {// 未起菜
            return DishDataItem.DishCheckStatus.NOT_CHECK;
        } else {// 禁用
            return DishDataItem.DishCheckStatus.INVALIATE_CHECK;
        }
    }


    /**
     * 获取取消等叫模式下菜品的选中状态
     *
     * @param shopcartItem
     * @return
     */
    public DishDataItem.DishCheckStatus getWakeUpCancelCheckStatus(IShopcartItemBase shopcartItem) {
        if (shopcartItem == null || shopcartItem.getId() == null)    //未保存的菜品
            return DishDataItem.DishCheckStatus.INVALIATE_CHECK;
        boolean canCancelWakUp = shopcartItem.canWakeUpCancel();
        if (canCancelWakUp)
            return DishDataItem.DishCheckStatus.NOT_CHECK;
        else
            return DishDataItem.DishCheckStatus.INVALIATE_CHECK;
    }

    /**
     * 获取取消起菜模式下菜品的选中状态
     *
     * @param shopcartItem
     * @return
     */
    public DishDataItem.DishCheckStatus getRiseUpCancelCheckStatus(IShopcartItemBase shopcartItem) {
        if (shopcartItem == null || shopcartItem.getId() == null)    //未保存的菜品
            return DishDataItem.DishCheckStatus.INVALIATE_CHECK;
        if (shopcartItem.canRiseDishCancel()) {
            return DishDataItem.DishCheckStatus.NOT_CHECK;
        } else {// 禁用
            return DishDataItem.DishCheckStatus.INVALIATE_CHECK;
        }
    }


    /**
     * 获取催菜模式下菜品的选中状态
     *
     * @param shopcartItem
     * @return
     */
    private DishDataItem.DishCheckStatus getRemindDishCheckStatus(IShopcartItemBase shopcartItem) {
        if (shopcartItem.canRemindDish()) {// 可以催菜
            return DishDataItem.DishCheckStatus.NOT_CHECK;
        } else {
            return DishDataItem.DishCheckStatus.INVALIATE_CHECK;
        }
    }

    /**
     * 获取批量操作模式下菜品的选中状态
     *
     * @param shopcartItem
     * @return
     */
    private DishDataItem.DishCheckStatus getBatchOperationCheckStatus(IShopcartItemBase shopcartItem) {
        if (shopcartItem.getStatusFlag() == StatusFlag.VALID) {
            //已出单数目为0的菜品，不能进行批量操作
            if (!TextUtils.isEmpty(shopcartItem.getBatchNo())
                    && shopcartItem.getSingleQty().compareTo(BigDecimal.ZERO) == 0) {
                return DishDataItem.DishCheckStatus.INVALIATE_CHECK;
            } else {
                return DishDataItem.DishCheckStatus.NOT_CHECK;
            }
        } else {
            return DishDataItem.DishCheckStatus.INVALIATE_CHECK;
        }
    }

    /**
     * 给trade内的各项数据绑定tradeid
     *
     * @param tradeVo
     */
    public void bindTradeId(TradeVo tradeVo) {
        if (tradeVo == null) {
            return;
        }

        // 获取tradeid
        Long tradeId = null;
        if (tradeVo.getTrade() != null) {
            tradeId = tradeVo.getTrade().getId();
        }

        // 会员绑定tradeid
        List<TradeCustomer> customerList = tradeVo.getTradeCustomerList();
        if (customerList != null) {
            for (TradeCustomer tradeCustomer : customerList) {
                tradeCustomer.setTradeId(tradeId);
            }
        }

        // 菜品折扣绑定tradeid
        for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
            if (tradeItemVo.getTradeItemPrivilege() != null
                    && tradeItemVo.getTradeItemPrivilege().getTradeId() != tradeId) {
                tradeItemVo.getTradeItemPrivilege().setTradeId(tradeId);
                tradeItemVo.getTradeItemPrivilege().setChanged(true);
            }
        }
    }


    private DishOptListener mOptListener;

    public void dishOperation(PrintOperationOpType opType, List<DishDataItem> dataItems, FragmentManager fragmentManager, DishOptListener optlistener) {
        mOptListener = optlistener;
        TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
        if (tradeVo == null || tradeVo.getTrade() == null || dataItems.isEmpty()) {
            return;
        }

        switch (opType) {
            case WAKE_UP:
            case RISE_DISH:
            case WAKE_UP_CANCEL:
            case RISE_DISH_CANCEL:
            case REMIND_DISH:
                dishOperationfun(tradeVo, opType, dataItems, fragmentManager, null, mOptListener);
                break;
        }
    }


    public static void dishOperationfun(TradeVo tradeVo,
                                        PrintOperationOpType opType,
                                        List<DishDataItem> dataItems,
                                        FragmentManager fragmentManager,
                                        DinnerShopCartAdapter adapter,
                                        DishOptListener optlistener) {

        TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
        //List<String> kitchenUUIDs = DinnerPrintUtil.getNoBatchNo(dataItems);
        if (tradeVo.isUnionMainTrade() || tradeVo.isUnionSubTrade()) {
            for (DishDataItem item : dataItems) {
                if (TextUtils.isEmpty(item.getBase().getBatchNo()))
                    item.getBase().setIssueStatus(IssueStatus.ISSUING);
                if (TextUtils.isEmpty(item.getItem().getBatchNo()))
                    item.getItem().setIssueStatusWithoutSetmeal(IssueStatus.ISSUING);//只改套餐本身的，避免其他子菜受到影响

                // 子单修改主单菜时，需要先调用拆菜
                ShopcartItemUtils.splitBatchItem(item.getItem());
            }
            tradeVo = DinnerShoppingCart.getInstance().createOrder();

            //OperationDishListener listener = new OperationDishListener(dataItems, opType, tradeVo, kitchenUUIDs, optlistener);
            DinnerDishManager.getInstance().addSelectedTradeItemOperations(dataItems, opType);
            //listener.preparePrintData();
            /*DinnerUnionManager.unionTradeOperationDish(tradeVo, DinnerShoppingCart.getInstance().getShoppingCartDish(), DinnerShoppingCart.getInstance().getMainTradeInfo(), dataItems,
                    LoadingResponseListener.ensure(listener, fragmentManager), tradeOperates);*/
        } else {
            List<Long> selectedItemIds = DinnerDishManager.getInstance().getSingleAndComboIds(dataItems);

            if (selectedItemIds.isEmpty() || selectedItemIds.size() < dataItems.size()
                    /*|| !kitchenUUIDs.isEmpty()*/ || (adapter != null && DinnerDishManager.getInstance().isChanged(adapter.getAllData()))) {

                DinnerDishManager.getInstance().removeInvoidTradeItemOperations(dataItems);

                for (DishDataItem item : dataItems) {
                    if (TextUtils.isEmpty(item.getBase().getBatchNo()))
                        item.getBase().setIssueStatus(IssueStatus.ISSUING);
                    if (TextUtils.isEmpty(item.getItem().getBatchNo()))
                        item.getItem().setIssueStatusWithoutSetmeal(IssueStatus.ISSUING);//只改套餐本身的，避免其他子菜受到影响
                }
                tradeVo = DinnerShoppingCart.getInstance().createOrder();

                //团餐餐标
                /*if (tradeVo.getMealShellVo() != null && tradeVo.getMealShellVo().getTradeItem() != null)
                    kitchenUUIDs.add(tradeVo.getMealShellVo().getTradeItem().getUuid());*/

                //OperationModifyDishListener modifyDishListener = new OperationModifyDishListener(opType, tradeVo, dataItems, kitchenUUIDs, fragmentManager, optlistener);
                // modifyDishListener.preparePrintData();
                //tradeOperates.modifyDinner(tradeVo, LoadingResponseListener.ensure(modifyDishListener, fragmentManager));
            } else {
                //OperationDishListener listener = new OperationDishListener(dataItems, opType, tradeVo, kitchenUUIDs, optlistener);
                DinnerDishManager.getInstance().addSelectedTradeItemOperations(dataItems, opType);
                //tradeOperates.operationDish(tradeVo.getTrade().getId(), opType, dataItems, LoadingResponseListener.ensure(listener, fragmentManager));
            }
        }

    }


    /**
     * 菜品是否必须全退
     *
     * @param shopcartItemBase
     * @return
     */
    public static boolean isMustReturnAll(IShopcartItemBase shopcartItemBase) {
        return shopcartItemBase.getSaleType() != SaleType.UNWEIGHING || shopcartItemBase.isGroupDish()
                || shopcartItemBase.getShopcartItemType() == ShopcartItemType.MAINBATCH;
    }

    /**
     * 是否是普通（非联台相关菜品）非称重商品
     *
     * @param shopcartItemBase
     * @return
     */
    public static boolean isCommonUnweightDish(IShopcartItemBase shopcartItemBase) {
        return shopcartItemBase.getShopcartItemType() == ShopcartItemType.COMMON &&
                shopcartItemBase.getSaleType() == SaleType.UNWEIGHING;
    }

}
