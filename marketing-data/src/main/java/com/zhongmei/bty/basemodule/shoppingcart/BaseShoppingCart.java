package com.zhongmei.bty.basemodule.shoppingcart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.customer.bean.DishMemberPrice;
import com.zhongmei.bty.basemodule.customer.entity.CrmMemberDay;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.operates.interfaces.CustomerDal;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardLevelSetting;
import com.zhongmei.bty.basemodule.discount.bean.BanquetVo;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.MarketRuleVo;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsInfo;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsVo;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionRuleVo;
import com.zhongmei.bty.basemodule.discount.cache.MarketRuleCache;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.discount.enums.UserType;
import com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConvertHelper;
import com.zhongmei.bty.basemodule.discount.utils.BuildPrivilegeTool;
import com.zhongmei.bty.basemodule.orderdish.bean.IExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.bean.GiftShopcartItemSingleton;
import com.zhongmei.bty.basemodule.shoppingcart.bean.ShoppingCartVo;
import com.zhongmei.bty.basemodule.shoppingcart.listerner.ModifyShoppingCartListener;
import com.zhongmei.bty.basemodule.shoppingcart.utils.CardServiceTool;
import com.zhongmei.bty.basemodule.shoppingcart.utils.CreateTradeTool;
import com.zhongmei.bty.basemodule.shoppingcart.utils.DiscountTool;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathManualMarketTool;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathSalesPromotionTool;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShoppingCartTool;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.bty.commonmodule.database.enums.CardRechagingStatus;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.data.R;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.ActivityRuleEffective;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.CouponType;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.DomainType;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.OperateType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment.CommonDialogFragmentBuilder;
import com.zhongmei.yunfu.util.EmptyUtils;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.util.ToastUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class BaseShoppingCart {

    protected Map<Integer, ModifyShoppingCartListener> arrayListener =
            new HashMap<Integer, ModifyShoppingCartListener>();

    public Map<Integer, ModifyShoppingCartListener> getArrayListener() {
        return arrayListener;
    }


    public void checkNeedBuildMainOrder(TradeVo mTradeVo) {
        if (mTradeVo == null || mTradeVo.getTrade() == null) {
            CreateTradeTool.buildMainTradeVo(mTradeVo);
            if (mTradeVo.getTrade() == null) {
                for (ModifyShoppingCartListener listener : arrayListener.values()) {
                    listener.exception("");
                }
            }
        }
    }


    public void setOrderBusinessType(ShoppingCartVo mShoppingCartVo, BusinessType mBusinessType) {
        checkNeedBuildMainOrder(mShoppingCartVo.getmTradeVo());
        if (mShoppingCartVo.getmTradeVo() != null && mShoppingCartVo.getmTradeVo().getTrade() != null
                && mBusinessType != null) {
            mShoppingCartVo.getmTradeVo().getTrade().setBusinessType(mBusinessType);
        }
    }


    public void setOrderType(ShoppingCartVo mShoppingCartVo, DeliveryType orderType) {
        checkNeedBuildMainOrder(mShoppingCartVo.getmTradeVo());
        if (mShoppingCartVo.getmTradeVo() != null && mShoppingCartVo.getmTradeVo().getTrade() != null
                && orderType != null) {
            mShoppingCartVo.getmTradeVo().getTrade().setDeliveryType(orderType);
        }
    }


    public DeliveryType getOrderType(ShoppingCartVo mShoppingCartVo) {
        return mShoppingCartVo.getmTradeVo().getTrade().getDeliveryType();
    }


    public void setDominType(ShoppingCartVo mShoppingCartVo, DomainType dominType) {
        if (mShoppingCartVo.getmTradeVo() == null || mShoppingCartVo.getmTradeVo().getTrade() == null
                || dominType == null) {
            return;
        }
        mShoppingCartVo.getmTradeVo().getTrade().setDomainType(dominType);
    }


    public Boolean isHaveTempDish() {
        return false;
    }


    public void addShippingToCart(ShoppingCartVo mShoppingCartVo, ShopcartItem mShopcartItem, Boolean isTempDish) {
        checkNeedBuildMainOrder(mShoppingCartVo.getmTradeVo());
        IShopcartItem value = getShopcartItemFromList(mShoppingCartVo, mShopcartItem.getUuid());
        if (value != null) {
            mShopcartItem.setTradeTable(value.getTradeTableUuid(), value.getTradeTableId());
            OperateShoppingCart.updateDish(mShoppingCartVo.getmTradeVo(),
                    mShoppingCartVo.getListOrderDishshopVo(),
                    mShopcartItem);
            resetSelectDishQTY(mShoppingCartVo);
        } else {
            if (mShoppingCartVo.getmTradeVo().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
                mShopcartItem.setShopcartItemType(ShopcartItemType.MAINBATCH);
                mShopcartItem.changeQty(mShopcartItem.getSingleQty().multiply(mShoppingCartVo.getmTradeVo().getSubTradeCount()));
            } else if (mShoppingCartVo.getmTradeVo().getTrade().getTradeType() == TradeType.UNOIN_TABLE_SUB) {
                mShopcartItem.setShopcartItemType(ShopcartItemType.SUB);
            } else {
                mShopcartItem.setShopcartItemType(ShopcartItemType.COMMON);
            }
            if (mShoppingCartVo.getDinnertableTradeInfo() != null) {
                DinnertableTradeInfo info = mShoppingCartVo.getDinnertableTradeInfo();
                mShopcartItem.setTradeTable(info.getTradeTableUuid(), info.getTradeTableId());
            }
            OperateShoppingCart.addToShoppingCart(mShoppingCartVo.getmTradeVo(),
                    mShoppingCartVo.getListOrderDishshopVo(),
                    mShopcartItem);
            addDishQTY(mShoppingCartVo, mShopcartItem);
        }

    }

    public void addReadOnlyShippingToCart(ShoppingCartVo mShoppingCartVo, IShopcartItem mShopcartItem, Boolean isTempDish) {
        checkNeedBuildMainOrder(mShoppingCartVo.getmTradeVo());
        IShopcartItem value = getShopcartItemFromList(mShoppingCartVo, mShopcartItem.getUuid());

        if (value != null) {
            OperateShoppingCart.updateReadOnlyShopcarItem(mShoppingCartVo.getmTradeVo(),
                    mShoppingCartVo.getListIShopcatItem(),
                    mShopcartItem);
            resetSelectDishQTY(mShoppingCartVo);
        } else {
            if (mShoppingCartVo.getDinnertableTradeInfo() != null) {
                List<? extends ISetmealShopcartItem> listSetmeal = mShopcartItem.getSetmealItems();
                if (listSetmeal != null) {
                    for (ISetmealShopcartItem setmeal : listSetmeal) {
                    }
                }

            }
            OperateShoppingCart.addToReadOnlyShoppingCart(mShoppingCartVo.getmTradeVo(),
                    mShoppingCartVo.getListIShopcatItem(),
                    mShopcartItem);
            addDishQTY(mShoppingCartVo, mShopcartItem);
        }

    }


    public void addTradeItemExtraDinner(ShoppingCartVo mShoppingCartVo, TradeItemExtraDinner tradeItemExtraDinner) {
        if (tradeItemExtraDinner == null) {
            return;
        }

        checkNeedBuildMainOrder(mShoppingCartVo.getmTradeVo());

        if (mShoppingCartVo.getmTradeVo().getTradeItemExtraDinners() == null) {
            mShoppingCartVo.getmTradeVo().setTradeItemExtraDinners(new ArrayList<TradeItemExtraDinner>());
        }

        mShoppingCartVo.getmTradeVo().getTradeItemExtraDinners().add(tradeItemExtraDinner);

    }


    public void isCheckAdd(ShoppingCartVo mShoppingCartVo, ShopcartItem mShopcartItem, Boolean isAdd) {
        if (!isAdd) {
            removeDish(mShoppingCartVo, mShopcartItem);
        }
        mShoppingCartVo.setTempShopItem(null);
    }


    public ShopcartItem getShopcartItemByUUID(ShoppingCartVo mShoppingCartVo, String uuid) {
        for (ShopcartItem mShopcartItem : mShoppingCartVo.getListOrderDishshopVo()) {
            if (mShopcartItem.getUuid().equals(uuid)) {
                return mShopcartItem;
            }
        }
        return null;
    }


    public IShopcartItem getIShopcartItemByUUID(ShoppingCartVo mShoppingCartVo, String uuid) {
        for (IShopcartItem iShopcartItem : mShoppingCartVo.getListIShopcatItem()) {
            if (iShopcartItem.getUuid().equals(uuid)) {
                return iShopcartItem;
            }
        }
        return null;
    }


    public ShopcartItem getShopcartItemBySetmeal(ShoppingCartVo mShoppingCartVo, String setmealUUID) {
        for (ShopcartItem shopcart : mShoppingCartVo.getListOrderDishshopVo()) {
            List<SetmealShopcartItem> listSetmeal = shopcart.getSetmealItems();
            for (SetmealShopcartItem setmeal : listSetmeal) {
                if (setmeal.getUuid().equals(setmealUUID)) {
                    return shopcart;
                }
            }
        }
        return null;
    }


    public ShopcartItem removeTradeItem(ShoppingCartVo mShoppingCartVo, TradeItemVo mTradeItemVo) {

        ShopcartItem mShopcartItem = OperateShoppingCart.removTradeItem(mShoppingCartVo.getmTradeVo(),
                mShoppingCartVo.getListOrderDishshopVo(),
                mTradeItemVo);

        if (mShopcartItem != null) {
            subtractDishQTY(mShoppingCartVo, mShopcartItem);
        }
        return mShopcartItem;

    }


    public void removeDish(ShoppingCartVo mShoppingCartVo, IShopcartItemBase mShopcartItemBase) {
        if (mShopcartItemBase instanceof ShopcartItemBase) {
            removeShopcartItem(mShoppingCartVo, (ShopcartItemBase) mShopcartItemBase);
            CardServiceTool.removeService(mShopcartItemBase);
        } else if (mShopcartItemBase instanceof ReadonlyShopcartItem) {
            removeReadOnlyShopcartItem(mShoppingCartVo, (ReadonlyShopcartItem) mShopcartItemBase);
        }
    }


    private void removeReadOnlyShopcartItem(ShoppingCartVo mShoppingCartVo, ReadonlyShopcartItem mShopcartItemBase) {
        IShopcartItem mShopcartItem = OperateShoppingCart.removeReadOnlyDish(mShoppingCartVo.getmTradeVo(),
                mShoppingCartVo.getListIShopcatItem(),
                mShopcartItemBase);
        if (mShopcartItem != null) {
            subtractDishQTY(mShoppingCartVo, mShopcartItem);
        }
    }

    private void removeShopcartItem(ShoppingCartVo mShoppingCartVo, ShopcartItemBase mShopcartItemBase) {
        ShopcartItem mShopcartItem = OperateShoppingCart.removeDish(mShoppingCartVo.getmTradeVo(),
                mShoppingCartVo.getListOrderDishshopVo(),
                mShopcartItemBase);
        if (mShopcartItem != null) {
            subtractDishQTY(mShoppingCartVo, mShopcartItem);
        }
    }

    protected void removeReadonlyShopcartItem(ShoppingCartVo mShoppingCartVo,
                                              ReadonlyShopcartItem mReadonlyShopcartItem) {
        for (IShopcartItem mIShopcartItem : mShoppingCartVo.getListIShopcatItem()) {
            if (mIShopcartItem.getUuid().equals(mReadonlyShopcartItem.getUuid())) {
                mIShopcartItem.delete();
                break;
            }
        }

    }

    public void removeShoppingcartItem(ShoppingCartVo mShoppingCartVo, IShopcartItem mShopcartItem,
                                       SetmealShopcartItem mSetmealShopcartItem, ChangePageListener mChangePageListener,
                                       FragmentManager mFragmentManager) {
        if (mShopcartItem.getId() != null) {
            removeReadonlyShopcartItem(mShoppingCartVo, (ReadonlyShopcartItem) mShopcartItem);
            return;
        }

        if (mSetmealShopcartItem == null) {
            removeDish(mShoppingCartVo, mShopcartItem);
            if (mShoppingCartVo.getTempShopItem() != null
                    && mShoppingCartVo.getTempShopItem().getUuid().equals(mShopcartItem.getUuid())) {
                mShoppingCartVo.setTempShopItem(null);
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.NONEEDCHECK, true);
                mChangePageListener.changePage(ChangePageListener.ORDERDISHLIST, null);
            }
        } else {
            switch (mShopcartItem.getSetmealManager().testModify(mSetmealShopcartItem, BigDecimal.ZERO)) {
                case SUCCESSFUL:
                    if (mSetmealShopcartItem != null) {
                        removeDish(mShoppingCartVo, mSetmealShopcartItem);
                        mShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);
                        if (mShoppingCartVo.getIndexPage() == ChangePageListener.DISHPROPERTY
                                && mShoppingCartVo.getShowPropertyPageDishUUID().equals(mSetmealShopcartItem.getUuid())) {
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(Constant.NONEEDCHECK, true);
                            bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, mShopcartItem.getUuid());
                            bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
                            mChangePageListener.changePage(ChangePageListener.DISHCOMBO, bundle);
                        }
                    }

                    break;
                case FAILED_REMOVE_REQUISITE:
                    ToastUtil.showLongToast(
                            BaseApplication.sInstance.getResources().getString(R.string.canont_delete_dish));

                    break;
                default:
                    if (mShoppingCartVo.getTempShopItem() != null
                            && mShopcartItem.getUuid().equals(mShoppingCartVo.getTempShopItem().getUuid())
                            && mShoppingCartVo.getIndexPage() == ChangePageListener.DISHCOMBO) {
                        removeDish(mShoppingCartVo, mSetmealShopcartItem);
                        mShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);

                    } else if (mShoppingCartVo.getTempShopItem() != null && mShopcartItem != null
                            && mShopcartItem.getUuid().equals(mShoppingCartVo.getTempShopItem().getUuid())
                            && mShoppingCartVo.getIndexPage() == ChangePageListener.DISHPROPERTY
                            && mShoppingCartVo.getShowPropertyPageDishUUID().equals(mSetmealShopcartItem.getUuid())) {
                        removeDish(mShoppingCartVo, mSetmealShopcartItem);
                        mShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);

                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constant.NONEEDCHECK, true);
                        bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, mShopcartItem.getUuid());
                        bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
                        mChangePageListener.changePage(ChangePageListener.DISHCOMBO, bundle);

                    } else if (mShoppingCartVo.getTempShopItem() != null
                            && mShopcartItem.getUuid().equals(mShoppingCartVo.getTempShopItem().getUuid())
                            && mShoppingCartVo.getIndexPage() == ChangePageListener.DISHPROPERTY
                            && !mShoppingCartVo.getShowPropertyPageDishUUID().equals(mSetmealShopcartItem.getUuid())) {
                        removeDish(mShoppingCartVo, mSetmealShopcartItem);
                        mShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);

                    } else if (mShoppingCartVo.getTempShopItem() != null
                            && !mShopcartItem.getUuid().equals(mShoppingCartVo.getTempShopItem().getUuid())) {
                        isCheckVaild(mShoppingCartVo,
                                ChangePageListener.DISHCOMBO,
                                mChangePageListener,
                                mFragmentManager,
                                mShopcartItem,
                                mSetmealShopcartItem);
                    } else if (mShoppingCartVo.getTempShopItem() == null) {
                        removeDish(mShoppingCartVo, mSetmealShopcartItem);
                        mShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);

                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constant.NONEEDCHECK, true);
                        bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, mShopcartItem.getUuid());
                        bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
                        mChangePageListener.changePage(ChangePageListener.DISHCOMBO, bundle);
                    }
                    break;
            }
        }
    }

    public void isCheckVaild(ShoppingCartVo mShoppingCartVo, int mPageNo, ChangePageListener mChangePageListener,
                             FragmentManager mFragmentManager, IShopcartItem mShopcartItem, SetmealShopcartItem mSetmealShopcartItem) {
        if (mShoppingCartVo.getTempShopItem() != null && mShoppingCartVo.getTempShopItem().getSetmealManager() != null
                && !mShoppingCartVo.getTempShopItem().getSetmealManager().isValid()) {
            showCheckDialog(mShoppingCartVo,
                    mPageNo,
                    mChangePageListener,
                    mFragmentManager,
                    mShopcartItem,
                    mSetmealShopcartItem);

        } else {
            removeDish(mShoppingCartVo, mSetmealShopcartItem);
            mShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);

            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.NONEEDCHECK, true);
            bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, mShopcartItem.getUuid());
            bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
            mChangePageListener.changePage(ChangePageListener.DISHCOMBO, bundle);
        }
    }

    public void showCheckDialog(final ShoppingCartVo mShoppingCartVo, final int mPageNo,
                                final ChangePageListener mChangePageListener, FragmentManager mFragmentManager,
                                final IShopcartItem mShopcartItem, final SetmealShopcartItem mSetmealShopcartItem) {
        CommonDialogFragmentBuilder cb = new CommonDialogFragmentBuilder(BaseApplication.sInstance);
        cb.iconType(CommonDialogFragment.ICON_WARNING)
                .title(BaseApplication.sInstance.getResources().getString(R.string.order_dish_setmeal_close_tip))
                .negativeText(R.string.calm_logout_no)
                .positiveText(R.string.close)
                .positiveLinstner(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        if (mSetmealShopcartItem != null) {
                            ShopcartItem deleteShopcartItem =
                                    getShopcartItemByUUID(mShoppingCartVo, mSetmealShopcartItem.getParentUuid());
                            deleteShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);
                            removeDish(mShoppingCartVo, mSetmealShopcartItem);
                        }

                        if (mShoppingCartVo.getTempShopItem() != null) {
                            isCheckAdd(mShoppingCartVo, mShoppingCartVo.getTempShopItem(), false);
                            mShoppingCartVo.setTempShopItem(null);
                        }

                        if (mChangePageListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID,
                                    mShopcartItem != null ? mShopcartItem.getUuid() : "");
                            bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
                            bundle.putBoolean(Constant.NONEEDCHECK, true);
                            mChangePageListener.changePage(mPageNo, bundle);
                        }

                        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(mShoppingCartVo),
                                mShoppingCartVo.getmTradeVo());
                        for (int key : arrayListener.keySet()) {
                            arrayListener.get(key).removeShoppingCart(mergeShopcartItem(mShoppingCartVo),
                                    mShoppingCartVo.getmTradeVo(),
                                    mShopcartItem != null ? mShopcartItem : mSetmealShopcartItem);
                        }
                    }

                })
                .build()
                .show(mFragmentManager, "closeSetmealFragment");
    }


    public void updateDish(ShoppingCartVo mShoppingCartVo, IShopcartItemBase mShopcartItemBase, Boolean isTempDish) {

        if (mShopcartItemBase instanceof ShopcartItemBase) {
            if (isTempDish && mShoppingCartVo.getUpdateTempShopItem() == null) {
                ShopcartItem mShopcartItem =
                        (ShopcartItem) getShopcartItemFromList(mShoppingCartVo, mShopcartItemBase.getUuid());
                mShoppingCartVo.setUpdateTempShopItem(mShopcartItem);
            }

            OperateShoppingCart.updateDish(mShoppingCartVo.getmTradeVo(),
                    mShoppingCartVo.getListOrderDishshopVo(),
                    (ShopcartItemBase) mShopcartItemBase);

        } else if (mShopcartItemBase instanceof ReadonlyShopcartItemBase) {
            OperateShoppingCart.updateReadOnlyShopcarItem(mShoppingCartVo.getmTradeVo(),
                    mShoppingCartVo.getListIShopcatItem(),
                    mShopcartItemBase);
        }
    }


    public void isCheckUpdate(ShoppingCartVo mShoppingCartVo, ShopcartItem mShopcartItem, Boolean isUpdate) {
        if (!isUpdate) {
            updateDish(mShoppingCartVo, mShoppingCartVo.getUpdateTempShopItem(), false);
        }
        mShoppingCartVo.setUpdateTempShopItem(null);
        mShoppingCartVo.setTempShopItem(null);
    }


    public Boolean isEditDishNew(ShoppingCartVo mShoppingCartVo) {
        if (mShoppingCartVo.getUpdateTempShopItem() == null && mShoppingCartVo.getTempShopItem() == null) {
            return false;
        }
        return true;
    }


    public void resetSelectDishQTY(ShoppingCartVo mShoppingCartVo) {
        mShoppingCartVo.getSelectDishQTYMap().clear();
        for (IShopcartItemBase mShopcartItemBase : mergeShopcartItem(mShoppingCartVo)) {
            if (mShopcartItemBase.getStatusFlag() == StatusFlag.VALID) {
                addDishQTY(mShoppingCartVo, mShopcartItemBase);
            }
        }
    }


    public void addDishQTY(ShoppingCartVo mShoppingCartVo, IShopcartItemBase mShopcartItem) {
        BigDecimal selectQTY = mShoppingCartVo.getSelectDishQTYMap().get(mShopcartItem.getSkuUuid());
        BigDecimal itemQTY = mShopcartItem.getSingleQty();
        if (mShopcartItem.isGroupDish()) {
            itemQTY = ShopcartItemUtils.getDisplyQty(mShopcartItem, mShoppingCartVo.getmTradeVo().getDeskCount());
        }
        if (selectQTY != null) {
            mShoppingCartVo.getSelectDishQTYMap().put(mShopcartItem.getSkuUuid(), itemQTY.add(selectQTY));
        } else {
            mShoppingCartVo.getSelectDishQTYMap().put(mShopcartItem.getSkuUuid(), itemQTY);
        }
    }


    public void subtractDishQTY(ShoppingCartVo mShoppingCartVo, IShopcartItemBase mShopcartItem) {
        BigDecimal selectQTY = mShoppingCartVo.getSelectDishQTYMap().get(mShopcartItem.getSkuUuid());
        BigDecimal itemQTY = mShopcartItem.getSingleQty();
        if (mShopcartItem.isGroupDish()) {
            itemQTY = ShopcartItemUtils.getDisplyQty(mShopcartItem, mShoppingCartVo.getmTradeVo().getDeskCount());
        }
        if (selectQTY != null) {
            if (selectQTY.subtract(itemQTY).compareTo(BigDecimal.ZERO) <= 0) {
                mShoppingCartVo.getSelectDishQTYMap().remove(mShopcartItem.getSkuUuid());
            } else {
                mShoppingCartVo.getSelectDishQTYMap().put(mShopcartItem.getSkuUuid(), selectQTY.subtract(itemQTY));
            }

        }

    }


    public Map<String, BigDecimal> getDishSelectQTY(ShoppingCartVo mShoppingCartVo) {
        return mShoppingCartVo.getSelectDishQTYMap();
    }

    public int getSelectDishCount(ShoppingCartVo mShoppingCartVo) {
        return mergeShopcartItem(mShoppingCartVo).size();
    }


    public ShopcartItem getShopcartItem(ShoppingCartVo mShoppingCartVo, String uuid) {
        for (ShopcartItem shopItem : mShoppingCartVo.getListOrderDishshopVo()) {
            if (shopItem.getUuid().equals(uuid)) {
                return shopItem;
            }
        }

        for (ShopcartItem shopcartItem : mShoppingCartVo.getListOrderDishshopVo()) {
            List<SetmealShopcartItem> listSetmeal = shopcartItem.getSetmealItems();
            if (listSetmeal != null) {
                for (SetmealShopcartItem setmeal : listSetmeal) {
                    if (setmeal.getUuid().equals(uuid)) {
                        return shopcartItem;
                    }
                }
            }
        }

        return null;
    }


    public IShopcartItemBase getIShopcartItem(ShoppingCartVo mShoppingCartVo, String uuid) {
        for (IShopcartItem shopItem : mergeShopcartItem(mShoppingCartVo)) {
            if (shopItem.getUuid().equals(uuid)) {
                return shopItem;
            }
            IShopcartItemBase extraItem = findExtraItem(shopItem, uuid);
            if (extraItem != null) {
                return extraItem;
            }
            List<? extends ISetmealShopcartItem> listSetmeal = shopItem.getSetmealItems();
            if (listSetmeal != null) {
                for (ISetmealShopcartItem setmeal : listSetmeal) {
                    if (setmeal.getUuid().equals(uuid)) {
                        return setmeal;
                    }
                    extraItem = findExtraItem(shopItem, uuid);
                    if (extraItem != null) {
                        return extraItem;
                    }
                }
            }
        }
        return null;
    }


    public IShopcartItemBase getIShopcartItem(ShoppingCartVo mShoppingCartVo, Long id) {
        for (IShopcartItem shopItem : mergeShopcartItem(mShoppingCartVo)) {
            if (Utils.equals(shopItem.getId(), id)) {
                return shopItem;
            }
            IShopcartItemBase extraItem = findExtraItem(shopItem, id);
            if (extraItem != null) {
                return extraItem;
            }
            List<? extends ISetmealShopcartItem> listSetmeal = shopItem.getSetmealItems();
            if (listSetmeal != null) {
                for (ISetmealShopcartItem setmeal : listSetmeal) {
                    if (Utils.equals(setmeal.getId(), id)) {
                        return setmeal;
                    }
                    extraItem = findExtraItem(shopItem, id);
                    if (extraItem != null) {
                        return extraItem;
                    }
                }
            }
        }
        return null;
    }

    private IShopcartItemBase findExtraItem(IShopcartItemBase parent, String uuid) {
        Collection<? extends IExtraShopcartItem> extraItems = parent.getExtraItems();
        if (Utils.isNotEmpty(extraItems)) {
            for (IExtraShopcartItem extraItem : extraItems) {
                if (extraItem.getUuid().equals(uuid)) {
                    return extraItem;
                }
            }
        }
        return null;
    }

    private IShopcartItemBase findExtraItem(IShopcartItemBase parent, Long id) {
        Collection<? extends IExtraShopcartItem> extraItems = parent.getExtraItems();
        if (Utils.isNotEmpty(extraItems)) {
            for (IExtraShopcartItem extraItem : extraItems) {
                if (Utils.equals(extraItem.getId(), id)) {
                    return extraItem;
                }
            }
        }
        return null;
    }


    public Boolean checkDishIsVaild(ShoppingCartVo mShoppingCartVo) {
        if (mShoppingCartVo.getTempShopItem() != null && mShoppingCartVo.getTempShopItem().getSetmealManager() != null
                && mShoppingCartVo.getTempShopItem().getSetmealManager().isValid()) {
            return true;
        } else if (mShoppingCartVo.getTempShopItem() != null
                && mShoppingCartVo.getTempShopItem().getSetmealManager() == null) {
            return true;
        } else if (mShoppingCartVo.getTempShopItem() == null) {
            return true;
        } else {
            return false;
        }
    }


    public Boolean checkDishIsVaild(ShoppingCartVo mShoppingCartVo, ShopcartItem mShopcartItem) {
        if (mShoppingCartVo.getTempShopItem() != null
                && mShoppingCartVo.getTempShopItem().getSetmealManager() != null
                && mShoppingCartVo.getTempShopItem().getSetmealManager().isValid()) {
            return true;
        } else if (mShoppingCartVo.getTempShopItem() != null
                && mShoppingCartVo.getTempShopItem().getSetmealManager() == null) {
            return true;
        } else if (mShoppingCartVo.getTempShopItem() == null) {
            return true;
        } else {
            return false;
        }
    }

    public List<IShopcartItem> mergeShopcartItem(ShoppingCartVo mShoppingCartVo) {
        List<IShopcartItem> tempList = new ArrayList<IShopcartItem>();
        tempList.addAll(mShoppingCartVo.getListOrderDishshopVo());
        tempList.addAll(mShoppingCartVo.getListIShopcatItem());
        return tempList;
    }


    public IShopcartItem getShopcartItemFromList(ShoppingCartVo mShoppingCartVo, String uuid) {
        for (IShopcartItem item : mergeShopcartItem(mShoppingCartVo)) {
            if (item.getUuid().equals(uuid)) {
                return item;
            }
        }
        return null;
    }


    public BigDecimal getMinPrice(ShoppingCartVo mShoppingCartVo) {
        BigDecimal minPrice = null;
        for (IShopcartItem orderDish : mergeShopcartItem(mShoppingCartVo)) {
            if (orderDish.isSelected()) {
                BigDecimal amount = orderDish.getActualAmount();
                if (minPrice == null) {
                    minPrice = amount;
                } else if (minPrice.subtract(amount).floatValue() > 0) {
                    minPrice = amount;
                }
            }

        }
        return minPrice;
    }


    public BigDecimal getTradeAmoutCanDiscount(ShoppingCartVo mShoppingCartVo) {
        List<IShopcartItem> iShopcartItem = mergeShopcartItem(mShoppingCartVo);

        BigDecimal noDiscountAllAmout = BigDecimal.ZERO;
        BigDecimal noDiscPrivilegeAmout = BigDecimal.ZERO;
        for (IShopcartItem item : iShopcartItem) {
            if (item.getStatusFlag() == StatusFlag.VALID) {
                boolean isAppletService = item.getAppletPrivilegeVo() != null && item.getAppletPrivilegeVo().isPrivilegeValid();
                boolean isCardService = item.getCardServicePrivilgeVo() != null && item.getCardServicePrivilgeVo().isPrivilegeValid();
                if (item.getEnableWholePrivilege() == Bool.YES && !isAppletService && !isCardService) {
                    noDiscountAllAmout = noDiscountAllAmout.add(item.getActualAmount());
                    if (item.getPrivilege() != null && item.getPrivilege().getPrivilegeAmount() != null && item.getPrivilege().isValid()) {
                        noDiscPrivilegeAmout = noDiscPrivilegeAmout.add(item.getPrivilege().getPrivilegeAmount());
                    }
                }
            }
        }
        BigDecimal shellAmount = MathShoppingCartTool.mathMealShellAmount(mShoppingCartVo.getmTradeVo());
        noDiscPrivilegeAmout = noDiscPrivilegeAmout.add(shellAmount);
        noDiscPrivilegeAmout = noDiscPrivilegeAmout.add(mShoppingCartVo.getmTradeVo().getDiscountExtracharge());
        return noDiscountAllAmout.add(noDiscPrivilegeAmout);
    }


    public BigDecimal getTotalRebatedDiscount(ShoppingCartVo mShoppingCartVo, BigDecimal discountValue) {
        BigDecimal totalDiscount = BigDecimal.ZERO;
        for (IShopcartItem orderDish : mergeShopcartItem(mShoppingCartVo)) {
            if (orderDish.isSelected()) {
                totalDiscount = totalDiscount.add(discountValue);
            }

        }
        return totalDiscount;
    }


    public void setCustomer(ShoppingCartVo mShoppingCartVo, TradeCustomer mTradeCustomer) {
        if (mShoppingCartVo.getArrayTradeCustomer() == null) {
            mShoppingCartVo.setArrayTradeCustomer(new HashMap<Integer, TradeCustomer>());

        }
        removeCustomer(mShoppingCartVo);
        if (mTradeCustomer != null) {
            mShoppingCartVo.getArrayTradeCustomer().put(mTradeCustomer.getCustomerType().value(), mTradeCustomer);
        }
    }

    private void removeCustomer(ShoppingCartVo mShoppingCartVo) {
        Map<Integer, TradeCustomer> arrayTradeCustomer = mShoppingCartVo.getArrayTradeCustomer();
        if (arrayTradeCustomer != null) {
            arrayTradeCustomer.remove(CustomerType.MEMBER.value());
            arrayTradeCustomer.remove(CustomerType.CARD.value());
            arrayTradeCustomer.remove(CustomerType.BOOKING.value());
            arrayTradeCustomer.remove(CustomerType.CUSTOMER.value());
        }
    }


    public void setMemberCustomer(ShoppingCartVo mShoppingCartVo, Map<Integer, TradeCustomer> mTradeCustomers) {

        checkNeedBuildMainOrder(mShoppingCartVo.getmTradeVo());

        List<TradeCustomer> listCustomer = mShoppingCartVo.getmTradeVo().getTradeCustomerList();
        if (listCustomer != null) {
            for (int i = listCustomer.size() - 1; i >= 0; i--) {
                TradeCustomer mCustomer = listCustomer.get(i);
                if (mCustomer.getCustomerType() == CustomerType.MEMBER
                        || mCustomer.getCustomerType() == CustomerType.CARD
                        || mCustomer.getCustomerType() == CustomerType.BOOKING
                        || mCustomer.getCustomerType() == CustomerType.CUSTOMER) {
                    listCustomer.remove(i);
                }
            }
        } else {
            listCustomer = new ArrayList<TradeCustomer>();
        }
        if (mTradeCustomers != null) {

            TradeCustomer mTradeCustomer = mTradeCustomers.get(CustomerType.MEMBER.value());
            if (mTradeCustomer != null) {
                createTradeCustomer(mTradeCustomer, mShoppingCartVo.getmTradeVo().getTrade().getUuid());
                listCustomer.add(mTradeCustomer);
            }

            TradeCustomer customerTradeCustomer = mTradeCustomers.get(CustomerType.CUSTOMER.value());
            if (customerTradeCustomer != null) {
                createTradeCustomer(customerTradeCustomer, mShoppingCartVo.getmTradeVo().getTrade().getUuid());
                listCustomer.add(customerTradeCustomer);
            }

            TradeCustomer cTradeCustomer = mTradeCustomers.get(CustomerType.CARD.value());
            if (cTradeCustomer != null) {
                createTradeCustomer(cTradeCustomer, mShoppingCartVo.getmTradeVo().getTrade().getUuid());
                listCustomer.add(cTradeCustomer);
            }

            mShoppingCartVo.getmTradeVo().setTradeCustomerList(listCustomer);
        }
    }

    public void createTradeCustomer(TradeCustomer mTradeCustomer, String tradeUuid) {
        mTradeCustomer.setEntitycardNum(mTradeCustomer.getEntitycardNum());
        mTradeCustomer.setCustomerType(mTradeCustomer.getCustomerType());
        mTradeCustomer.validateCreate();
        AuthUser authUser = Session.getAuthUser();
        if (authUser != null) {
            mTradeCustomer.setCreatorId(authUser.getId());
            mTradeCustomer.setCreatorName(authUser.getName());
        }
        mTradeCustomer.setTradeUuid(tradeUuid);
        if (TextUtils.isEmpty(mTradeCustomer.getUuid())) {
            mTradeCustomer.setUuid(SystemUtils.genOnlyIdentifier());
        }
    }


    protected void buildCallCustomer(ShoppingCartVo mShoppingCartVo, TradeCustomer mTradeCustomer) {
        checkNeedBuildMainOrder(mShoppingCartVo.getmTradeVo());
        if (mTradeCustomer != null) {
            List<TradeCustomer> listCustomer = mShoppingCartVo.getmTradeVo().getTradeCustomerList();
            if (listCustomer != null) {
                for (TradeCustomer mCustomer : listCustomer) {
                    if (mCustomer.getCustomerType() == CustomerType.BOOKING) {
                        listCustomer.remove(mCustomer);
                        break;
                    }
                }
            } else {
                listCustomer = new ArrayList<TradeCustomer>();
            }

            String haveNoName = BaseApplication.sInstance.getResources().getString(R.string.have_no_name);
            if (TextUtils.isEmpty(mTradeCustomer.getCustomerName())
                    || haveNoName.equals(mTradeCustomer.getCustomerName())) {
                if (mShoppingCartVo.getmTradeVo().getTradeExtra() != null && mTradeCustomer.getCustomerPhone() != null
                        && mTradeCustomer.getCustomerPhone()
                        .equals(mShoppingCartVo.getmTradeVo().getTradeExtra().getReceiverPhone())
                        && !TextUtils.isEmpty(mShoppingCartVo.getmTradeVo().getTradeExtra().getReceiverName())) {
                    mTradeCustomer.setCustomerName(mShoppingCartVo.getmTradeVo().getTradeExtra().getReceiverName());
                }
            }

            mTradeCustomer.setCustomerType(CustomerType.BOOKING);
            mTradeCustomer.setTradeUuid(mShoppingCartVo.getmTradeVo().getTrade().getUuid());
            if (TextUtils.isEmpty(mTradeCustomer.getUuid())) {
                mTradeCustomer.setUuid(SystemUtils.genOnlyIdentifier());
            }
            mTradeCustomer.validateCreate();
            listCustomer.add(mTradeCustomer);
            mShoppingCartVo.getmTradeVo().setTradeCustomerList(listCustomer);
        }
    }


    public void setRemarks(ShoppingCartVo mShoppingCartVo, String remarks) {
        checkNeedBuildMainOrder(mShoppingCartVo.getmTradeVo());
        mShoppingCartVo.getmTradeVo().getTrade().setTradeMemo(remarks);
        mShoppingCartVo.getmTradeVo().getTrade().setChanged(true);
    }


    public void removeSetmealRemark(ShoppingCartVo mShoppingCartVo, SetmealShopcartItem mSetmeal) {
        List<SetmealShopcartItem> listSetmeal = new ArrayList<SetmealShopcartItem>();
        for (ShopcartItem mShopcartItem : mShoppingCartVo.getListOrderDishshopVo()) {
            if (mShopcartItem.getUuid().equals(mSetmeal.getParentUuid())) {
                listSetmeal.addAll(mShopcartItem.getSetmealItems());
                break;
            }
        }
        for (SetmealShopcartItem setmeal : listSetmeal) {
            if (setmeal.getUuid().equals(mSetmeal.getUuid())) {
                setmeal.setMemo(null);
                break;
            }
        }
    }


    public TradeVo createOrder(ShoppingCartVo mShoppingCartVo, Boolean isGuaDan) {
        checkNeedBuildMainOrder(mShoppingCartVo.getmTradeVo());
        if (mShoppingCartVo.getArrayTradeCustomer() != null) {
            setMemberCustomer(mShoppingCartVo, mShoppingCartVo.getArrayTradeCustomer());

            TradeCustomer bTradeCustomer = mShoppingCartVo.getArrayTradeCustomer().get(CustomerType.BOOKING.value());
            if (bTradeCustomer != null) {
                buildCallCustomer(mShoppingCartVo, bTradeCustomer);
            }
            TradeCustomer invalidTradeCustomer = mShoppingCartVo.getArrayTradeCustomer().get(null);
            if (invalidTradeCustomer != null) {

                List<TradeCustomer> listCustomer = mShoppingCartVo.getmTradeVo().getTradeCustomerList();
                invalidTradeCustomer.setChanged(true);

                listCustomer.add(invalidTradeCustomer);
            }

        }
        if (isGuaDan) {
            List<CouponPrivilegeVo> couponPrivilegeVoList = mShoppingCartVo.getmTradeVo().getCouponPrivilegeVoList();
            if (couponPrivilegeVoList != null) {
                for (CouponPrivilegeVo couponPrivilegeVo : couponPrivilegeVoList) {
                    if (!couponPrivilegeVo.isActived()) {
                        couponPrivilegeVo.getTradePrivilege().setPrivilegeAmount(BigDecimal.ZERO);
                    }
                }
            }
        } else {
        }

        CreateTradeTool.createTradeVo(mShoppingCartVo.getListOrderDishshopVo(),
                mShoppingCartVo.getListIShopcatItem(),
                mShoppingCartVo.getmTradeVo(),
                mShoppingCartVo.getmTakeOutInfo());

        TradeVo tradeVo = mShoppingCartVo.getmTradeVo();
        tradeVo.setTradeDeposit(mShoppingCartVo.getmTradeVo().getTradeDeposit());
        tradeVo.getTrade().setDishKindCount(mShoppingCartVo.getSelectDishQTYMap().size());
        tradeVo.setTradeUser(mShoppingCartVo.getTradeUser());
        if (tradeVo.getTradeTableList() != null && tradeVo.getTradeTableList().size() > 0) {
            int peopleCount = 0;
            for (TradeTable tradeTable : tradeVo.getTradeTableList()) {
                if (!tradeTable.isValid()) {
                    continue;
                }
                Integer i = tradeTable.getTablePeopleCount();
                if (i != null) {
                    peopleCount += i;
                }
            }
            tradeVo.getTrade().setTradePeopleCount(peopleCount);
        }
        return tradeVo;
    }


    public TradeVo createSalesReturn(TradeVo mSalesTradeVo) {
        Trade mTrade = mSalesTradeVo.getTrade();
        if (mTrade != null) {
            mTrade.setTradeAmountBefore(mTrade.getTradeAmountBefore().negate());
            mTrade.setTradeAmount(mTrade.getTradeAmount().negate());
            mTrade.setSaleAmount(mTrade.getSaleAmount().negate());
            mTrade.setDishKindCount(new BigDecimal(mTrade.getDishKindCount()).negate().intValue());
            mTrade.setPrivilegeAmount(mTrade.getPrivilegeAmount().negate());
        }

        List<TradePrivilege> tradePrivileges = mSalesTradeVo.getTradePrivileges();
        if (Utils.isNotEmpty(tradePrivileges)) {
            for (TradePrivilege tradePrivilege : tradePrivileges) {
                if (tradePrivilege != null) {
                    tradePrivilege.setPrivilegeAmount(tradePrivilege.getPrivilegeAmount().negate());
                }
            }
        }

        List<TradeItemVo> listTradeItem = mSalesTradeVo.getTradeItemList();
        if (listTradeItem != null) {
            for (TradeItemVo item : listTradeItem) {
                TradeItem mTradeItem = item.getTradeItem();
                mTradeItem.setQuantity(mTradeItem.getQuantity().negate());
                mTradeItem.setAmount(mTradeItem.getAmount().negate());
                mTradeItem.setActualAmount(mTradeItem.getActualAmount().negate());
                mTradeItem.setPropertyAmount(mTradeItem.getPropertyAmount().negate());

                TradePrivilege itemTradePrivilege = item.getTradeItemPrivilege();
                if (itemTradePrivilege != null) {
                    itemTradePrivilege.setPrivilegeAmount(itemTradePrivilege.getPrivilegeAmount().negate());
                }

                List<TradeItemProperty> itemProperty = item.getTradeItemPropertyList();
                if (itemProperty != null) {
                    for (TradeItemProperty property : itemProperty) {
                        property.setAmount(property.getAmount().negate());
                        property.setQuantity(property.getQuantity().negate());
                    }
                }
            }
        }

        List<TradePlanActivity> tradePlanActivities = mSalesTradeVo.getTradePlanActivityList();
        if (tradePlanActivities != null) {
            for (TradePlanActivity tradePlanActivity : tradePlanActivities) {
                tradePlanActivity.setOfferValue(tradePlanActivity.getOfferValue().negate());
            }
        }
        List<TradeTax> tradeTaxs = mSalesTradeVo.getTradeTaxs();
        if (tradeTaxs != null) {
            for (TradeTax tradeTax : tradeTaxs) {
                tradeTax.setTaxAmount(tradeTax.getTaxAmount().negate());
                tradeTax.setTaxableIncome(tradeTax.getTaxableIncome().negate());
            }
        }
        return mSalesTradeVo;
    }


    public void batchMemberPrivilege(ShoppingCartVo mShoppingCartVo, CustomerResp mCustomer, Boolean isDinner) {
        if (mCustomer == null) {
            return;
        }

        for (IShopcartItemBase item : mergeShopcartItem(mShoppingCartVo)) {

            if ((item.getPrivilege() != null &&
                    (item.getPrivilege().getPrivilegeType() == PrivilegeType.DISCOUNT
                            || item.getPrivilege().getPrivilegeType() == PrivilegeType.REBATE
                            || item.getPrivilege().getPrivilegeType() == PrivilegeType.FREE
                            || item.getPrivilege().getPrivilegeType() == PrivilegeType.GIVE)
                    && item.getPrivilege().isValid()) || item.isGroupDish()) {
                continue;
            }
            if (item.getCardServicePrivilgeVo() != null && item.getCardServicePrivilgeVo().isPrivilegeValid()) {
                continue;
            }

            if (item.getPrivilege() != null && (item.getPrivilege().getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT || item.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_PRICE || item.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_REBATE)) {
                item.setPrivilege(null);
            }

            setDishMemberPrivilege(mShoppingCartVo, item, isDinner);
        }
    }


    public void batchMemberPrivilege(ShoppingCartVo mShoppingCartVo) {
        CustomerResp mCustomer = CustomerManager.getInstance().getLoginCustomer();
        if (mCustomer == null) {
            return;
        }

        Map<String, TradeItemPlanActivity> tradeItemPlanActivityMap = covertItemPlanListToMap(mShoppingCartVo.getmTradeVo().getTradeItemPlanActivityList());

        for (IShopcartItemBase item : mergeShopcartItem(mShoppingCartVo)) {
            if ((item.getPrivilege() != null &&
                    (item.getPrivilege().getPrivilegeType() == PrivilegeType.DISCOUNT
                            || item.getPrivilege().getPrivilegeType() == PrivilegeType.REBATE
                            || item.getPrivilege().getPrivilegeType() == PrivilegeType.FREE
                            || item.getPrivilege().getPrivilegeType() == PrivilegeType.GIVE)
                    && item.getPrivilege().isValid()) || item.isGroupDish()
                    && hasSalesPromotion(item, tradeItemPlanActivityMap)) {
                continue;
            }

            if (isSalesPromotion(mShoppingCartVo.getmTradeVo().getTradeItemPlanActivityList(), item)) {
                continue;
            }

            if (item.getCardServicePrivilgeVo() != null && item.getCardServicePrivilgeVo().isPrivilegeValid()) {
                continue;
            }

            if (item.getPrivilege() != null && (item.getPrivilege().getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT || item.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_PRICE || item.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_REBATE)) {
                item.setPrivilege(null);
            }

            setDishMemberPrivilege(mShoppingCartVo, item, false);
        }
    }


    public static boolean hasSalesPromotion(IShopcartItemBase item, Map<String, TradeItemPlanActivity> tradeItemPlanActivityMap) {
        return tradeItemPlanActivityMap.containsKey(item.getUuid());
    }

    private boolean isSalesPromotion(List<TradeItemPlanActivity> planActivityList, IShopcartItemBase shopcartItem) {
        if (Utils.isEmpty(planActivityList)) {
            return false;
        }

        for (TradeItemPlanActivity activity : planActivityList) {
            if (shopcartItem.getUuid().equals(activity.getTradeItemUuid())) {
                return true;
            }
        }

        return false;
    }


    public static boolean isHasMemberPlanActivity(IShopcartItemBase item, Map<String, TradeItemPlanActivity> tradeItemPlanActivityMap) {
        TradeItemPlanActivity tradeItemPlanActivity = tradeItemPlanActivityMap.get(item.getUuid());
        MarketRuleVo marketRuleVo = null;
        if (tradeItemPlanActivity != null) {
            marketRuleVo = MarketRuleCache.getMarketDishVoByRule(tradeItemPlanActivity.getRuleId());
        }
        if ((marketRuleVo != null && marketRuleVo.getUserTypes().contains(UserType.MEMBER)) && tradeItemPlanActivityMap.containsKey(item.getUuid())) {
            return true;
        }
        return false;
    }


    public static Map<String, TradeItemPlanActivity> covertItemPlanListToMap(List<TradeItemPlanActivity> tradeItemPlanList) {
        Map<String, TradeItemPlanActivity> itemMap = new HashMap<String, TradeItemPlanActivity>();
        if (tradeItemPlanList != null && tradeItemPlanList.size() > 0) {

            for (TradeItemPlanActivity tradeItemPlan : tradeItemPlanList) {
                if (tradeItemPlan.getStatusFlag() == StatusFlag.INVALID) {
                    continue;
                }
                itemMap.put(tradeItemPlan.getTradeItemUuid(), tradeItemPlan);
            }
        }
        return itemMap;
    }


    public void setDishMemberPrivilege(ShoppingCartVo mShoppingCartVo, IShopcartItemBase mIShopcartItemBase, Boolean isDinner) {

        CustomerResp mCustomer = null;
        if (isDinner) {
            mCustomer = DinnerShopManager.getInstance().getLoginCustomer();
        } else {
            mCustomer = CustomerManager.getInstance().getLoginCustomer();
        }
        setDishMemberPrivilege(mShoppingCartVo, mIShopcartItemBase, mCustomer, isDinner);
    }

    public void setDishMemberPrivilege(ShoppingCartVo mShoppingCartVo, IShopcartItemBase mIShopcartItemBase, CustomerResp mCustomer, Boolean isDinner) {
        setDishMemberPrivilege(mShoppingCartVo.getmTradeVo(), mIShopcartItemBase, mCustomer, isDinner);
    }


    public static void setDishMemberPrivilege(TradeVo tradeVo, IShopcartItemBase mIShopcartItemBase, CustomerResp mCustomer, Boolean isDinner) {

        if (mIShopcartItemBase.getDishShop() == null) {
            return;
        }


        if (mIShopcartItemBase.getIsChangedPrice() == Bool.YES) {
            mIShopcartItemBase.setPrivilege(null);
            return;
        }
        Map<String, TradeItemPlanActivity> tradeItemPlanActivityMap = covertItemPlanListToMap(tradeVo.getTradeItemPlanActivityList());

        if ((mIShopcartItemBase.getPrivilege() != null && mIShopcartItemBase.getPrivilege().isValid()
                && (mIShopcartItemBase.getPrivilege().getPrivilegeType() == PrivilegeType.DISCOUNT
                || mIShopcartItemBase.getPrivilege().getPrivilegeType() == PrivilegeType.REBATE
                || mIShopcartItemBase.getPrivilege().getPrivilegeType() == PrivilegeType.FREE
                || mIShopcartItemBase.getPrivilege().getPrivilegeType() == PrivilegeType.GIVE))
                || (mIShopcartItemBase.getCouponPrivilegeVo() != null && mIShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege() != null
                && mIShopcartItemBase.getCouponPrivilegeVo().getTradePrivilege().isValid())
                || isHasMemberPlanActivity(mIShopcartItemBase, tradeItemPlanActivityMap) || mIShopcartItemBase.isGroupDish()) {
            return;
        }

        if (mIShopcartItemBase.getCardServicePrivilgeVo() != null && mIShopcartItemBase.getCardServicePrivilgeVo().isPrivilegeValid()) {
            return;
        }

        if (mIShopcartItemBase.getAppletPrivilegeVo() != null && mIShopcartItemBase.getAppletPrivilegeVo().isPrivilegeValid()) {
            return;
        }

        if (mCustomer == null || mCustomer.levelId==null) {
            return;
        }

        if (mCustomer.card != null && mCustomer.card.getRightStatus() != CardRechagingStatus.EFFECTIVE) {
            return;
        }

        CrmMemberDay crmMemberDay = ServerSettingCache.getInstance().getCrmMemberDay();
        boolean isInMemberDay = isInMemberDay(crmMemberDay);


        EcCardLevelSetting mCardLevelSetting = null;

        if (mCustomer.card != null) {
            mCardLevelSetting = mCustomer.card.getCardLevelSetting();
        }

        CustomerDal operates = OperatesFactory.create(CustomerDal.class);
        Long dishID = mIShopcartItemBase.getDishShop().getBrandDishId();
        DishMemberPrice mDishMemberPrice = null;

        mDishMemberPrice = operates.findDishMemberPriceByDishId(dishID, mCustomer.levelId);
        Long privilegeTime = tradeVo.getTrade().getServerCreateTime();
        if (privilegeTime == null) {
            privilegeTime = System.currentTimeMillis();
        }
        if (mDishMemberPrice != null && isInDate(privilegeTime, mDishMemberPrice.getPeriodStart(), mDishMemberPrice.getPeriodEnd())) {
            TradePrivilege mTradePrivilege = BuildPrivilegeTool.mathMemberPrice(mIShopcartItemBase, mDishMemberPrice, tradeVo.getTrade().getUuid());
            mIShopcartItemBase.setPrivilege(mTradePrivilege);
        } else {
            TradePrivilege temp = mIShopcartItemBase.getPrivilege();
            if (temp != null && (temp.getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT || temp.getPrivilegeType() == PrivilegeType.MEMBER_PRICE || temp.getPrivilegeType() == PrivilegeType.MEMBER_REBATE)) {
                List<TradePrivilege> tradePrivileges = tradeVo.getTradePrivileges();
                if (temp.getId() != null && tradePrivileges != null) {
                    for (TradePrivilege tradePrivilege : tradePrivileges) {
                        if (tradePrivilege.getId() != null && temp.getId().equals(tradePrivilege.getId())) {
                            tradePrivilege.setChanged(true);
                            tradePrivilege.setStatusFlag(StatusFlag.INVALID);
                        }
                    }
                }
                mIShopcartItemBase.setPrivilege(null);
            }

        }
    }


    public static boolean isInDate(Long time, String strDateBegin, String strDateEnd) {
        if (strDateBegin.equals("00:00") && strDateEnd.equals("00:00"))
            return true;

        Calendar calendar = Calendar.getInstance();
        String[] startTime = strDateBegin.split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startTime[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(startTime[1]));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTimeL = calendar.getTimeInMillis();
        String[] endTime = strDateEnd.split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endTime[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(endTime[1]));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long endTimeL = calendar.getTimeInMillis();
        return time >= startTimeL && time <= endTimeL;
    }


    private static boolean isInMemberDay(CrmMemberDay crmMemberDay) {
        if (crmMemberDay == null) {
            return false;
        }
        if (!crmMemberDay.isValid()) {
            return false;
        }
        if (!crmMemberDay.getIsSwitch()) {
            return false;
        }
        String value = crmMemberDay.getMemberDayValue();
        if (EmptyUtils.isEmpty(value)) {
            return false;
        }
        if (EmptyUtils.isEmpty(crmMemberDay.getMemberPriceTempletId())) {
            return false;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        if (crmMemberDay.getMemberDayType() == 1) {
            String week = String.valueOf(cal.get(Calendar.DAY_OF_WEEK) - 1);
            return value.contains(week);

        } else if (crmMemberDay.getMemberDayType() == 2) {
            String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
            return value.contains(day);
        }
        return false;
    }


    public List<IShopcartItem> filterDishList(List<IShopcartItem> list) {
        return filterDishList(list, true);
    }


    public List<IShopcartItem> filterDishList(List<IShopcartItem> list, boolean isHasSplit) {
        if (list == null) {
            return null;
        }
        List<IShopcartItem> tempList = new ArrayList<IShopcartItem>();
        for (IShopcartItem shopcartItem : list) {
            if (shopcartItem.getTotalQty() != null && (shopcartItem.getTotalQty().doubleValue() - 0 == 0)) {
                continue;
            }
            if ((shopcartItem.getStatusFlag() == StatusFlag.VALID)
                    || (shopcartItem.getStatusFlag() == StatusFlag.INVALID
                    && shopcartItem.getInvalidType() == InvalidType.SPLIT && isHasSplit)) {
                tempList.add(shopcartItem);
            }
        }
        return tempList;
    }


    public void setShowPropertyPageDishUUID(ShoppingCartVo mShoppingCartVo, String showPropertyPageDishUUID) {
        mShoppingCartVo.setShowPropertyPageDishUUID(showPropertyPageDishUUID);
    }

    public Boolean getIsSalesReturn() {
        return false;
    }


    public TradeCustomer getMemberCustomer(TradeVo mTradeVo) {
        List<TradeCustomer> listOrder = mTradeVo.getTradeCustomerList();
        if (listOrder != null) {
            for (TradeCustomer customer : listOrder) {
                if (customer.getCustomerType() == CustomerType.MEMBER) {
                    return customer;
                }

            }
        }
        return null;

    }

    public void setTradeFreeReasonRel(TradeVo mTradeVo, Reason mReason, OperateType mOperateType) {
        TradeReasonRel tradeReasonRel = new TradeReasonRel();
        tradeReasonRel.validateCreate();
        tradeReasonRel.setReasonContent(mReason.getContent());
        tradeReasonRel.setReasonId(mReason.getId());
        tradeReasonRel.setOperateType(mOperateType);
        tradeReasonRel.setRelateId(mTradeVo.getTrade().getId());
        tradeReasonRel.setRelateUuid(mTradeVo.getTrade().getUuid());
        tradeReasonRel.setUuid(SystemUtils.genOnlyIdentifier());

        List<TradeReasonRel> listReason = mTradeVo.getTradeReasonRelList();

        if (listReason == null) {
            List<TradeReasonRel> listTradeReason = new ArrayList<TradeReasonRel>();
            listTradeReason.add(tradeReasonRel);
            mTradeVo.setTradeReasonRelList(listTradeReason);
        } else {
            for (int i = 0; i < listReason.size(); i++) {
                TradeReasonRel tradeReasonRel1 = listReason.get(i);
                OperateType operateType = tradeReasonRel1.getOperateType();
                if (operateType == OperateType.TRADE_DINNER_FREE
                        || operateType == OperateType.TRADE_FASTFOOD_FREE
                        || operateType == OperateType.TRADE_DISCOUNT
                        || operateType == OperateType.TRADE_REBATE
                        || operateType == OperateType.TRADE_BANQUET) {
                    if (tradeReasonRel1.getId() == null) {
                        listReason.remove(i);
                    } else {
                        tradeReasonRel1.setStatusFlag(StatusFlag.INVALID);
                        tradeReasonRel1.validateUpdate();
                    }
                    break;
                }
            }
            listReason.add(tradeReasonRel);
        }
    }


    public TradeReasonRel setTradeItemReasonRel(IShopcartItemBase shopcartItem, Reason mReason, OperateType mOperateType) {
        TradeReasonRel tradeReasonRel = new TradeReasonRel();
        tradeReasonRel.validateCreate();
        tradeReasonRel.setReasonContent(mReason.getContent());
        tradeReasonRel.setReasonId(mReason.getId());
        tradeReasonRel.setOperateType(mOperateType);
        tradeReasonRel.setRelateId(shopcartItem.getId());
        tradeReasonRel.setRelateUuid(shopcartItem.getUuid());
        tradeReasonRel.setUuid(SystemUtils.genOnlyIdentifier());
        return tradeReasonRel;
    }


    public void removeOrderTreadePrivilege(TradeVo mTradeVo) {
        List<TradePrivilege> listPrivilege = mTradeVo.getTradePrivileges();
        if (listPrivilege != null) {
            for (int i = listPrivilege.size() - 1; i >= 0; i--) {
                TradePrivilege mTradePrivilege = listPrivilege.get(i);
                if (mTradePrivilege != null && (mTradePrivilege.getPrivilegeType() == PrivilegeType.DISCOUNT
                        || mTradePrivilege.getPrivilegeType() == PrivilegeType.REBATE
                        || mTradePrivilege.getPrivilegeType() == PrivilegeType.FREE)) {
                    if (mTradePrivilege.getId() == null) {
                        listPrivilege.remove(i);
                    } else {
                        mTradePrivilege.setStatusFlag(StatusFlag.INVALID);
                        mTradePrivilege.setChanged(true);
                    }
                }
            }
        }

        removeReason(mTradeVo, OperateType.TRADE_DISCOUNT);
        removeFreeReason(mTradeVo);
    }


    public void removeFreeReason(TradeVo mTradeVo) {


        List<TradeReasonRel> listReason = mTradeVo.getTradeReasonRelList();

        if (listReason != null) {

            for (int i = 0; i < listReason.size(); i++) {

                if (listReason.get(i).getOperateType() == OperateType.TRADE_DINNER_FREE
                        || listReason.get(i).getOperateType() == OperateType.TRADE_FASTFOOD_FREE) {

                    listReason.remove(i);

                }

            }

        }

    }


    public void removeReason(TradeVo mTradeVo, OperateType operateType) {


        List<TradeReasonRel> listReason = mTradeVo.getTradeReasonRelList();

        if (listReason != null) {

            for (int i = 0; i < listReason.size(); i++) {
                TradeReasonRel tradeReasonRel = listReason.get(i);
                if (tradeReasonRel.getOperateType() == operateType) {
                    if (tradeReasonRel.getId() == null) {
                        listReason.remove(i);
                    } else {
                        tradeReasonRel.setStatusFlag(StatusFlag.INVALID);
                        tradeReasonRel.validateUpdate();
                    }
                }

            }

        }

    }

    @Deprecated
    public TradePrivilege buildMarketingPrivilege(TradePrivilege mTradePrivilege, TradeVo mTradeVo, BigDecimal privilegeAoument) {
        if (mTradePrivilege == null) {
            mTradePrivilege = new TradePrivilege();
            mTradePrivilege.validateCreate();
            mTradePrivilege.setUuid(SystemUtils.genOnlyIdentifier());
        } else {
            mTradePrivilege.validateUpdate();
        }

        mTradePrivilege.setTradeUuid(mTradeVo.getTrade().getUuid());
        mTradePrivilege.setPrivilegeAmount(privilegeAoument);
        mTradePrivilege.setPrivilegeValue(privilegeAoument);
        mTradePrivilege.setPrivilegeType(PrivilegeType.MARKTING);
        mTradePrivilege.setStatusFlag(StatusFlag.VALID);
        mTradePrivilege.setPrivilegeName(BaseApplication.sInstance.getString(R.string.marketing_campaign));
        return mTradePrivilege;
    }


    public void setMarktingTradePrivilege(TradeVo tradeVo) {
        List<TradePlanActivity> tradePlans = tradeVo.getTradePlanActivityList();
        if (EmptyUtils.isEmpty(tradePlans)) {
            List<TradePrivilege> tradePrivileges = tradeVo.getTradePrivileges();
            if (EmptyUtils.isEmpty(tradePrivileges)) {
                return;
            }
            for (TradePrivilege tradePrivilege : tradePrivileges) {
                if (tradePrivilege.getPrivilegeType() == PrivilegeType.MARKTING) {
                    tradePrivilege.setStatusFlag(StatusFlag.INVALID);
                    tradePrivilege.setChanged(true);
                }
            }
        } else {
            List<TradePrivilege> tradePrivileges = tradeVo.getTradePrivileges();
            if (EmptyUtils.isEmpty(tradePrivileges)) {
                tradePrivileges = new ArrayList<>();
                tradeVo.setTradePrivileges(tradePrivileges);
            }
            for (TradePlanActivity tradePlan : tradePlans) {
                Long planId = tradePlan.getPlanId();
                boolean hasPlanId = false;
                for (TradePrivilege tradePrivilege : tradePrivileges) {
                    if (tradePrivilege.getPrivilegeType() == PrivilegeType.MARKTING && planId != null && tradePrivilege.getPromoId() != null && planId.compareTo(tradePrivilege.getPromoId()) == 0) {
                        hasPlanId = true;
                        tradePlanToPrivilege(tradePlan, tradePrivilege);
                        break;
                    }
                }
                if (hasPlanId) {
                    continue;
                }
                TradePrivilege tradePrivilege = new TradePrivilege();
                tradePlanToPrivilege(tradePlan, tradePrivilege);
                tradePrivileges.add(tradePrivilege);
            }
        }
    }


    private void tradePlanToPrivilege(TradePlanActivity tradePlan, TradePrivilege privilege) {
        if (privilege.getClientCreateTime() == null) {
            privilege.setPromoId(tradePlan.getPlanId());
            privilege.validateCreate();
            privilege.setUuid(SystemUtils.genOnlyIdentifier());
        } else {
            privilege.validateUpdate();
        }
        privilege.setTradeUuid(tradePlan.getTradeUuid());
        BigDecimal offerValue = tradePlan.getOfferValue();
        privilege.setPrivilegeAmount(offerValue);
        privilege.setPrivilegeValue(offerValue);
        privilege.setPrivilegeType(PrivilegeType.MARKTING);
        privilege.setStatusFlag(StatusFlag.VALID);
        privilege.setPrivilegeName(tradePlan.getRuleName());
    }


    @Deprecated
    private void setMarktingTradePrivilegeOldVersion(TradeVo mTradeVo) {
        List<TradePlanActivity> listTP = mTradeVo.getTradePlanActivityList();
        BigDecimal privilegeAoument = BigDecimal.ZERO;
        if (listTP != null && listTP.size() > 0) {

            Boolean haveValid = false;
            for (TradePlanActivity mTradePlanActivity : listTP) {
                if (mTradePlanActivity.getStatusFlag() == StatusFlag.VALID && mTradePlanActivity.getRuleEffective() == ActivityRuleEffective.VALID) {
                    privilegeAoument = privilegeAoument.add(mTradePlanActivity.getOfferValue());
                    haveValid = true;
                }

            }
            if (haveValid) {


                List<TradePrivilege> listTradePrivilege = mTradeVo.getTradePrivileges();
                if (listTradePrivilege != null) {
                    Boolean isHave = false;
                    for (TradePrivilege mTP : listTradePrivilege) {
                        if (mTP.getPrivilegeType() == PrivilegeType.MARKTING) {
                            isHave = true;
                            buildMarketingPrivilege(mTP, mTradeVo, privilegeAoument);
                        }
                    }
                    if (!isHave) {
                        listTradePrivilege.add(buildMarketingPrivilege(null, mTradeVo, privilegeAoument));
                    }
                } else {
                    listTradePrivilege = new ArrayList<TradePrivilege>();
                    listTradePrivilege.add(buildMarketingPrivilege(null, mTradeVo, privilegeAoument));
                    mTradeVo.setTradePrivileges(listTradePrivilege);
                }
            } else {
                List<TradePrivilege> listTradePrivilege = mTradeVo.getTradePrivileges();
                if (listTradePrivilege != null) {
                    for (TradePrivilege mTP : listTradePrivilege) {
                        if (mTP.getPrivilegeType() == PrivilegeType.MARKTING) {
                            mTP.setStatusFlag(StatusFlag.INVALID);
                            mTP.setChanged(true);
                        }
                    }

                }
            }

        } else {
            List<TradePrivilege> listTradePrivilege = mTradeVo.getTradePrivileges();
            if (listTradePrivilege != null) {
                for (TradePrivilege mTP : listTradePrivilege) {
                    if (mTP.getPrivilegeType() == PrivilegeType.MARKTING) {
                        mTP.setStatusFlag(StatusFlag.INVALID);
                        mTP.setChanged(true);
                    }
                }

            }
        }

    }

    public String getPrivilegeKey(PrivilegeType type, Boolean isAllOrder) {
        if (isAllOrder) {
            if (type == PrivilegeType.DISCOUNT
                    || type == PrivilegeType.REBATE
                    || type == PrivilegeType.FREE
                    || type == PrivilegeType.GIVE
                    || type == PrivilegeType.AUTO_DISCOUNT
                    || type == PrivilegeType.MEMBER_PRICE
                    || type == PrivilegeType.MEMBER_REBATE
                    || type == PrivilegeType.WECHAT_CARD_COUPONS
                    || type == PrivilegeType.MARKTING
            ) {
                return type.value().toString() + "forOrder";
            } else {
                return type.value().toString();
            }
        } else {
            if (type == PrivilegeType.DISCOUNT
                    || type == PrivilegeType.REBATE
                    || type == PrivilegeType.FREE
                    || type == PrivilegeType.GIVE
                    || type == PrivilegeType.WECHAT_CARD_COUPONS
            ) {
                return "forDish";
            } else {
                return type.value().toString();
            }
        }
    }

    public static void copyOnlyData(TradePrivilege theRef, TradePrivilege theNew) {

        theNew.setPrivilegeType(theRef.getPrivilegeType());

        theNew.setPrivilegeValue(theRef.getPrivilegeValue());

        theNew.setPrivilegeName(theRef.getPrivilegeName());

        theNew.setPrivilegeAmount(theRef.getPrivilegeAmount());

        theNew.setPromoId(theRef.getPromoId());

        theNew.setStatusFlag(theRef.getStatusFlag());

    }


    public void addWeiXinCouponsVo(TradeVo mTradeVo, WeiXinCouponsInfo mWeiXinCouponsInfo) {
        List<WeiXinCouponsVo> listWX = mTradeVo.getmWeiXinCouponsVo();
        if (listWX == null) {
            listWX = new ArrayList<WeiXinCouponsVo>();
            mTradeVo.setmWeiXinCouponsVo(listWX);
        }
        WeiXinCouponsVo mWeiXinCouponsVo = new WeiXinCouponsVo();
        TradePrivilege mTradePrivilege = BuildPrivilegeTool.buildWeiXinCouponsPrivilege(mTradeVo, mWeiXinCouponsInfo);
        mWeiXinCouponsVo.setActived(false);
        mWeiXinCouponsVo.setmTradePrivilege(mTradePrivilege);
        mWeiXinCouponsVo.setmWeiXinCouponsInfo(mWeiXinCouponsInfo);

        listWX.add(mWeiXinCouponsVo);
    }


    public void removeWeiXinCouponsVo(TradeVo mTradeVo, TradePrivilege mTradePrivilege) {
        List<WeiXinCouponsVo> listWX = mTradeVo.getmWeiXinCouponsVo();
        if (listWX != null) {
            for (int i = listWX.size() - 1; i >= 0; i--) {
                WeiXinCouponsVo mw = listWX.get(i);
                if (mw.getmTradePrivilege().getUuid().equals(mTradePrivilege.getUuid())) {
                    if (mw.getmTradePrivilege().isSaveServer() && mw.getmTradePrivilege().isValid()) {
                        mw.getmTradePrivilege().setStatusFlag(StatusFlag.INVALID);
                        mw.getmTradePrivilege().validateUpdate();
                    } else {
                        listWX.remove(i);
                    }
                    break;
                }
            }
        }
    }


    public static void removeWeiXinCouponsVo(TradeVo tradeVo, WeiXinCouponsVo couponsVo) {
        TradePrivilege mTradePrivilege = couponsVo.getmTradePrivilege();
        if (mTradePrivilege == null) {
            return;
        }
        List<WeiXinCouponsVo> listWX = tradeVo.getmWeiXinCouponsVo();
        if (listWX != null) {
            for (int i = listWX.size() - 1; i >= 0; i--) {
                WeiXinCouponsVo mw = listWX.get(i);
                if (mw.getmTradePrivilege().getUuid().equals(mTradePrivilege.getUuid())) {
                    if (mTradePrivilege.getId() != null) {
                        mTradePrivilege.setChanged(true);
                        mTradePrivilege.setStatusFlag(StatusFlag.INVALID);
                    } else {
                        listWX.remove(i);
                    }
                    break;
                }
            }
        }
    }


    public List<IShopcartItem> getAllValidShopcartItem(List<IShopcartItem> list) {
        List<IShopcartItem> tempList = new ArrayList<>();
        if (list != null) {
            Map<String, IShopcartItem> validShopcartItemMap = new HashMap<String, IShopcartItem>();
            for (IShopcartItem item : list) {
                if (item.getStatusFlag() == StatusFlag.VALID && !TextUtils.isEmpty(item.getRelateTradeItemUuid())) {
                    validShopcartItemMap.put(item.getRelateTradeItemUuid(), item);
                }
            }
            for (IShopcartItem item : list) {
                if (item.getStatusFlag() == StatusFlag.VALID || (item.getStatusFlag() == StatusFlag.INVALID
                        && (item.getInvalidType() == InvalidType.SPLIT || item.getInvalidType() == InvalidType.RETURN_QTY
                        || item.getInvalidType() == InvalidType.MODIFY_DISH))) {
                    if (item.getStatusFlag() == StatusFlag.INVALID
                            && (item.getInvalidType() == InvalidType.RETURN_QTY
                            || (item.getInvalidType() == InvalidType.MODIFY_DISH))) {
                        if (validShopcartItemMap.get(item.getUuid()) == null) {
                            continue;
                        }
                    }
                    tempList.add(item);
                }
            }
        }
        return tempList;
    }


    public boolean isOnlySingleAddDish(List<IShopcartItem> list, List<String> uuids) {
        for (IShopcartItem iShopcartItem : list) {
            String uuid = iShopcartItem.getUuid();
            if (TextUtils.isEmpty(uuid) || !uuids.contains(uuid)) {
                continue;
            }

            if (TextUtils.isEmpty(iShopcartItem.getBatchNo())
                    && iShopcartItem.getStatusFlag() == StatusFlag.INVALID) {
                continue;
            }

            if (iShopcartItem.isGroupDish()) {
                return false;
            }
        }
        return true;
    }


    public void removeAllWeixinCoupon(TradeVo mTradeVo) {
        List<WeiXinCouponsVo> weiXinCouponsVoList = mTradeVo.getmWeiXinCouponsVo();
        if (weiXinCouponsVoList == null) {
            return;
        }
        Iterator iterator = weiXinCouponsVoList.iterator();
        while (iterator.hasNext()) {
            WeiXinCouponsVo couponsVo = (WeiXinCouponsVo) iterator.next();
            if (couponsVo.isActived() && couponsVo.getmTradePrivilege() != null && couponsVo.getmTradePrivilege().isValid()) {
                if (couponsVo.getmTradePrivilege().getId() == null) {
                    iterator.remove();
                } else {
                    couponsVo.getmTradePrivilege().setStatusFlag(StatusFlag.INVALID);
                    couponsVo.getmTradePrivilege().setChanged(true);
                }
            }
        }
    }


    protected void removeBanquetOnly(TradeVo tradeVo) {
        BanquetVo banquetVo = tradeVo.getBanquetVo();
        if (banquetVo != null && banquetVo.getTradePrivilege() != null) {
            TradePrivilege tradePrivilege = banquetVo.getTradePrivilege();
            if (tradePrivilege != null && tradePrivilege.isValid()) {
                if (tradePrivilege.getId() != null) {
                    tradePrivilege.setStatusFlag(StatusFlag.INVALID);
                    tradePrivilege.setChanged(true);
                } else {
                    tradeVo.setBanquetVo(null);
                }
            }
        }
        removeReason(tradeVo, OperateType.TRADE_BANQUET);
    }


    public static boolean isHasValidBanquet(TradeVo tradeVo) {
        if (tradeVo.getBanquetVo() != null && tradeVo.getBanquetVo().getTradePrivilege().isValid()) {
            return true;
        }
        return false;
    }


    public static void removeAllPrivilige(TradeVo tradeVo, boolean isRmBanquet, boolean isRemoveExtrage, boolean isRemoveMemeberPrice) {
        List<TradePrivilege> tradePrivilegeList = tradeVo.getTradePrivileges();
        if (tradePrivilegeList != null) {
            for (int i = tradePrivilegeList.size() - 1; i >= 0; i--) {
                TradePrivilege tradePrivilege = tradePrivilegeList.get(i);
                if (tradePrivilege.getPrivilegeName().startsWith(BaseApplication.sInstance.getString(R.string.shoppingcart_minimum_charge)))
                    continue;
                if (tradePrivilege.getId() == null) {
                    tradePrivilegeList.remove(i);
                } else {
                    tradePrivilege.setStatusFlag(StatusFlag.INVALID);
                    tradePrivilege.validateUpdate();
                }
            }
        }
        if (isRemoveExtrage) {
            tradeVo.setExtraChargeMap(null);
            if (tradeVo.getBanquetVo() != null && isRmBanquet) {
                TradePrivilege tradePrivilege = tradeVo.getBanquetVo().getTradePrivilege();
                if (tradePrivilege.getId() == null) {
                    tradePrivilege = null;
                    tradeVo.setBanquetVo(null);
                } else {
                    tradePrivilege.setStatusFlag(StatusFlag.INVALID);
                    tradePrivilege.validateUpdate();
                }
            }
        }
        if (Utils.isNotEmpty(tradeVo.getCouponPrivilegeVoList())) {
            for (CouponPrivilegeVo couponPrivilegeVo : tradeVo.getCouponPrivilegeVoList()) {
                TradePrivilege tradePrivilege = couponPrivilegeVo.getTradePrivilege();
                if (tradePrivilege.getId() == null) {
                    tradePrivilege = null;
                    tradeVo.setCouponPrivilege(null);
                } else {
                    tradePrivilege.setStatusFlag(StatusFlag.INVALID);
                    tradePrivilege.validateUpdate();
                }
            }
        }

        if (tradeVo.getIntegralCashPrivilegeVo() != null) {
            TradePrivilege tradePrivilege = tradeVo.getIntegralCashPrivilegeVo().getTradePrivilege();
            if (tradePrivilege.getId() == null) {
                tradePrivilege = null;
                tradeVo.setIntegralCashPrivilegeVo(null);
            } else {
                tradePrivilege.setStatusFlag(StatusFlag.INVALID);
                tradePrivilege.validateUpdate();
            }
        }

        if (tradeVo.getmWeiXinCouponsVo() != null) {
            List<WeiXinCouponsVo> weixinCouponList = tradeVo.getmWeiXinCouponsVo();
            if (weixinCouponList != null) {
                for (int i = weixinCouponList.size() - 1; i >= 0; i--) {
                    WeiXinCouponsVo weiXinCouponsVo = weixinCouponList.get(i);
                    TradePrivilege tradePrivilege = weiXinCouponsVo.getmTradePrivilege();
                    if (tradePrivilege.getId() == null) {
                        tradePrivilege = null;
                        weixinCouponList.remove(i);
                    } else {
                        tradePrivilege.setStatusFlag(StatusFlag.INVALID);
                        tradePrivilege.validateUpdate();
                    }
                }
            }
        }
        MathManualMarketTool.removeAllActivity(tradeVo);
        List<TradeItemVo> tradeItemVoList = tradeVo.getTradeItemList();
        if (tradeItemVoList != null) {
            for (TradeItemVo tradeItemVo : tradeItemVoList) {
                if (tradeItemVo.getTradeItemPrivilege() != null) {
                    PrivilegeType privilegeType = tradeItemVo.getTradeItemPrivilege().getPrivilegeType();
                    if (privilegeType != PrivilegeType.MEMBER_REBATE && privilegeType != PrivilegeType.MEMBER_PRICE && privilegeType != PrivilegeType.AUTO_DISCOUNT || isRemoveMemeberPrice) {
                        if (tradeItemVo.getTradeItemPrivilege().getId() == null) {
                            tradeItemVo.setTradeItemPrivilege(null);
                        } else {
                            tradeItemVo.getTradeItemPrivilege().setStatusFlag(StatusFlag.INVALID);
                            tradeItemVo.getTradeItemPrivilege().validateUpdate();
                        }
                    }
                }
                if (tradeItemVo.getCouponPrivilegeVo() != null && tradeItemVo.getCouponPrivilegeVo().getTradePrivilege() != null) {
                    TradePrivilege tradePrivilege = tradeItemVo.getCouponPrivilegeVo().getTradePrivilege();
                    if (tradePrivilege.getId() == null) {
                        tradeItemVo.getCouponPrivilegeVo().setTradePrivilege(null);
                    } else {
                        tradePrivilege.setStatusFlag(StatusFlag.INVALID);
                        tradePrivilege.validateUpdate();
                    }
                }
            }
        }
    }


    protected void CheckGiftCouponIsActived(ShoppingCartVo shoppingCartVo) {
        CheckGiftCouponIsActived(mergeShopcartItem(shoppingCartVo), shoppingCartVo.getmTradeVo());
    }

    public static void CheckGiftCouponIsActived(List<IShopcartItem> shopcartItemList, TradeVo tradeVo) {
        if (Utils.isEmpty(shopcartItemList)) {
            return;
        }
        for (IShopcartItem item : shopcartItemList) {
            if (item.getCouponPrivilegeVo() != null && item.getCouponPrivilegeVo().getTradePrivilege() != null) {
                BigDecimal count = BigDecimal.ONE;

                BigDecimal privilegeAmount = item.getPrice().multiply(count);
                item.getCouponPrivilegeVo().getTradePrivilege().setPrivilegeAmount(privilegeAmount.negate());
                item.getCouponPrivilegeVo().getTradePrivilege().setPrivilegeValue(privilegeAmount);

                BigDecimal depositValue = BigDecimal.ZERO;
                if (tradeVo.getTradeDeposit() != null) {
                    depositValue = tradeVo.getTradeDeposit().getDepositPay();
                }
                BigDecimal saleAmount = tradeVo.getTrade().getSaleAmount().subtract(depositValue);
                if ((saleAmount.compareTo(BigDecimal.ZERO) == 0
                        && item.getCouponPrivilegeVo().getCoupon().getFullValue().doubleValue() != 0)
                        || item.getCouponPrivilegeVo().getCoupon() == null) {
                    item.getCouponPrivilegeVo().setActived(false);
                } else {
                    if (saleAmount.compareTo(item.getCouponPrivilegeVo().getCoupon().getFullValue()) < 0
                            || item.getSingleQty().compareTo(count) < 0) {

                        item.getCouponPrivilegeVo().setActived(false);
                    } else {

                        item.getCouponPrivilegeVo().setActived(true);
                    }
                }

            }
        }
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, tradeVo);
    }


    public void removeGiftCouponePrivilege(Long couponId, ShoppingCartVo shoppingCartVo, boolean isDinner) {
        List<IShopcartItem> iShopcartItems = mergeShopcartItem(shoppingCartVo);
        for (IShopcartItem item : iShopcartItems) {
            if (item.getCouponPrivilegeVo() != null && item.getCouponPrivilegeVo().getTradePrivilege() != null) {
                if (item.getCouponPrivilegeVo().getTradePrivilege().getPromoId().compareTo(couponId) == 0) {
                    if (isDinner) {
                        if (item.getCouponPrivilegeVo().getTradePrivilege() != null) {
                            item.getCouponPrivilegeVo().getTradePrivilege().setStatusFlag(StatusFlag.INVALID);
                            item.getCouponPrivilegeVo().getTradePrivilege().setChanged(true);
                        } else {
                            item.setCouponPrivilegeVo(null);
                        }
                    } else {
                        item.setCouponPrivilegeVo(null);
                    }
                    setDishMemberPrivilege(shoppingCartVo, item, isDinner);
                }
            }
        }
        MathShoppingCartTool.mathTotalPrice(iShopcartItems, shoppingCartVo.getmTradeVo());

        for (ModifyShoppingCartListener listener : arrayListener.values()) {
            listener.updateDish(iShopcartItems, shoppingCartVo.getmTradeVo());
        }
    }


    public void removeGiftCouponePrivilege(List<Long> couponIds, ShoppingCartVo shoppingCartVo, boolean isDinner) {
        List<IShopcartItem> iShopcartItems = mergeShopcartItem(shoppingCartVo);
        for (IShopcartItem item : iShopcartItems) {
            if (item.getCouponPrivilegeVo() != null && item.getCouponPrivilegeVo().getTradePrivilege() != null) {
                if (couponIds != null) {
                    for (Long couponId : couponIds) {
                        if (item.getCouponPrivilegeVo() != null
                                && item.getCouponPrivilegeVo().getTradePrivilege().getPromoId().compareTo(couponId) == 0) {
                            if (isDinner) {
                                if (item.getCouponPrivilegeVo().getTradePrivilege() != null) {
                                    item.getCouponPrivilegeVo().getTradePrivilege().setStatusFlag(StatusFlag.INVALID);
                                    item.getCouponPrivilegeVo().getTradePrivilege().setChanged(true);
                                } else {
                                    item.setCouponPrivilegeVo(null);
                                }
                            } else {
                                item.setCouponPrivilegeVo(null);
                            }
                            setDishMemberPrivilege(shoppingCartVo, item, isDinner);
                        }
                    }
                }

            }
        }
        MathShoppingCartTool.mathTotalPrice(iShopcartItems,
                shoppingCartVo.getmTradeVo());
        for (ModifyShoppingCartListener listener : arrayListener.values()) {
            listener.updateDish(iShopcartItems, shoppingCartVo.getmTradeVo());
        }
    }


    public void removeDefineOrGiftCoupon(ShoppingCartVo shoppingCartVo, CouponPrivilegeVo couponPrivilegeVo) {
        if (couponPrivilegeVo.getCoupon().getCouponType() == CouponType.GIFT
                && couponPrivilegeVo.getShopcartItem() != null && couponPrivilegeVo.getTradePrivilege() != null) {
            removeGiftCouponePrivilege(couponPrivilegeVo.getTradePrivilege().getPromoId(), shoppingCartVo, true);
        } else {
            removeCouponPrivilege(shoppingCartVo, couponPrivilegeVo, true);
        }
    }


    public void removeCouponPrivilege(ShoppingCartVo shoppingCartVo, CouponPrivilegeVo couponPrivilegeVo, boolean isNeedListener) {
        if (couponPrivilegeVo.getTradePrivilege() == null || couponPrivilegeVo.getTradePrivilege().getPromoId() == null) {
            return;
        }
        List<Long> couponIdList = new ArrayList<Long>();
        couponIdList.add(couponPrivilegeVo.getTradePrivilege().getPromoId());
        removeCouponPrivilege(shoppingCartVo, couponIdList, isNeedListener);
    }


    public void removeCouponPrivilege(ShoppingCartVo shoppingCartVo, List<Long> couponIdList, boolean isNeedListener) {
        List<CouponPrivilegeVo> couponPrivilegeList = shoppingCartVo.getmTradeVo().getCouponPrivilegeVoList();
        if (Utils.isEmpty(couponIdList) || Utils.isEmpty(couponPrivilegeList)) {
            return;
        }
        for (int i = couponPrivilegeList.size() - 1; i >= 0; i--) {
            CouponPrivilegeVo mCouponPrivilegeVo = couponPrivilegeList.get(i);
            for (Long couponId : couponIdList) {
                if (couponId.equals(mCouponPrivilegeVo.getTradePrivilege().getPromoId())) {
                    if (mCouponPrivilegeVo.getTradePrivilege().getId() == null) {
                        couponPrivilegeList.remove(i);
                    } else {
                        mCouponPrivilegeVo.getTradePrivilege().setInValid();
                    }
                }
            }
        }

        List<IShopcartItem> shopcartItemList = mergeShopcartItem(shoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                shoppingCartVo.getmTradeVo());
        if (isNeedListener) {
            for (ModifyShoppingCartListener listener : arrayListener.values()) {
                listener.removeCouponPrivilege(shopcartItemList, shoppingCartVo.getmTradeVo());
            }
        }
    }


    public boolean isAllowAddCoupon(ShoppingCartVo shoppingCartVo, CouponPrivilegeVo couponPrivilegeVo) {
        List<CouponPrivilegeVo> couponPrivilegeVoList = shoppingCartVo.getmTradeVo().getCouponPrivilegeVoList();
        if (Utils.isEmpty(couponPrivilegeVoList)) {
            return true;
        }
        CouponType couponType = couponPrivilegeVo.getCoupon().getCouponType();
        for (CouponPrivilegeVo mCouponPrivilegeVo : couponPrivilegeVoList) {
            if (mCouponPrivilegeVo.getCoupon() != null && mCouponPrivilegeVo.getTradePrivilege() != null
                    && mCouponPrivilegeVo.getTradePrivilege().isValid()
            ) {
                if (mCouponPrivilegeVo.getCoupon().getCouponType() != couponType || couponType != CouponType.CASH) {
                    return false;
                }
            }
        }
        return true;
    }


    public boolean isAllowAddCoupon(ShoppingCartVo shoppingCartVo, Long id) {
        List<CouponPrivilegeVo> couponPrivilegeVoList = shoppingCartVo.getmTradeVo().getCouponPrivilegeVoList();
        if (Utils.isEmpty(couponPrivilegeVoList)) {
            return true;
        }
        for (CouponPrivilegeVo mCouponPrivilegeVo : couponPrivilegeVoList) {
            if (mCouponPrivilegeVo.getCouponInfoId() != null
                    && mCouponPrivilegeVo.getCouponInfoId().equals(id)
                    && mCouponPrivilegeVo.getTradePrivilege() != null
                    && mCouponPrivilegeVo.getTradePrivilege().isValid()) {
                return false;
            }
        }

        return true;
    }


    public static String getNewWxCode(String code) {
        int zero = 0;
        int p = 0;
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if (c == '0') {
                zero++;
            } else {
                p = i;
                break;
            }
        }

        return code.substring(p) + (String.valueOf(zero).length() == 1 ? ("0" + String.valueOf(zero)) : String.valueOf(zero));
    }


    public void removeTradePrivilege(PrivilegeType privilegeType, TradeVo tradeVo) {
        List<TradePrivilege> listPrivilege = tradeVo.getTradePrivileges();
        if (listPrivilege != null) {
            ExtraCharge outTimeExtra = ServerSettingCache.getInstance().getmOutTimeRule();
            ExtraCharge minCharge = tradeVo.getMinconExtraCharge();
            for (int i = listPrivilege.size() - 1; i >= 0; i--) {
                TradePrivilege mTradePrivilege = listPrivilege.get(i);

                if (mTradePrivilege != null && (mTradePrivilege.getPrivilegeType() == privilegeType)) {
                    if (outTimeExtra != null && outTimeExtra.getId().longValue() == mTradePrivilege.getPromoId().longValue()) {
                        continue;
                    }

                    if (minCharge != null && minCharge.getId().longValue() == mTradePrivilege.getPromoId().longValue())
                        continue;

                    if (mTradePrivilege.getId() == null) {
                        listPrivilege.remove(i);
                    } else {
                        mTradePrivilege.setStatusFlag(StatusFlag.INVALID);
                        mTradePrivilege.setChanged(true);
                    }
                }
            }
        }
    }


    protected void removeOuttimeCharge(String uuid, TradeVo tradeVo) {
        List<TradePrivilege> listPrivilege = tradeVo.getTradePrivileges();
        if (listPrivilege != null) {
            for (int i = listPrivilege.size() - 1; i >= 0; i--) {
                TradePrivilege mTradePrivilege = listPrivilege.get(i);
                if (mTradePrivilege != null && (mTradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL) && mTradePrivilege.getUuid().equals(uuid)) {
                    if (mTradePrivilege.getId() == null) {
                        listPrivilege.remove(i);
                    } else {
                        mTradePrivilege.setStatusFlag(StatusFlag.INVALID);
                        mTradePrivilege.setChanged(true);
                    }
                }
            }
        }

    }


    public List<Long> removeAllInvalidCoupon(ShoppingCartVo shoppingCartVo, boolean isNeedListener) {
        List<CouponPrivilegeVo> couponPrivilegeVoList = shoppingCartVo.getmTradeVo().getCouponPrivilegeVoList();
        List<Long> deleteCouponIds = new ArrayList<Long>();
        if (Utils.isNotEmpty(couponPrivilegeVoList)) {
            for (int i = couponPrivilegeVoList.size() - 1; i >= 0; i--) {
                CouponPrivilegeVo couponPrivilegeVo = couponPrivilegeVoList.get(i);
                if (!couponPrivilegeVo.isActived()
                        && couponPrivilegeVo.isValid()) {
                    if (couponPrivilegeVo.getTradePrivilege() != null && couponPrivilegeVo.getTradePrivilege().isSaveServer()) {
                        couponPrivilegeVo.getTradePrivilege().setInValid();
                    } else {
                        couponPrivilegeVoList.remove(i);
                    }
                    deleteCouponIds.add(couponPrivilegeVo.getCoupon().getId());
                }
            }
        }
        List<IShopcartItem> iShopcartItems = mergeShopcartItem(shoppingCartVo);
        if (isNeedListener) {
            MathShoppingCartTool.mathTotalPrice(iShopcartItems, shoppingCartVo.getmTradeVo());
            if (isNeedListener) {

                for (ModifyShoppingCartListener listener : arrayListener.values()) {
                    listener.removeCouponPrivilege(iShopcartItems, shoppingCartVo.getmTradeVo());
                }
            }
        }
        return deleteCouponIds;
    }


    public void removeAllCouponPrivilege(ShoppingCartVo shoppingCartVo, boolean isNeedListener) {
        List<CouponPrivilegeVo> couponPrivilegeVoList = shoppingCartVo.getmTradeVo().getCouponPrivilegeVoList();
        if (Utils.isNotEmpty(couponPrivilegeVoList)) {
            int size = couponPrivilegeVoList.size();
            for (int i = size - 1; i >= 0; i--) {
                CouponPrivilegeVo couponPrivilegeVo = couponPrivilegeVoList.get(i);
                if (couponPrivilegeVo.getTradePrivilege().getId() == null) {
                    couponPrivilegeVoList.remove(i);
                } else {
                    couponPrivilegeVo.getTradePrivilege().setInValid();
                }
            }
        }

        List<IShopcartItem> shopcartItemList = mergeShopcartItem(shoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, shoppingCartVo.getmTradeVo());
        if (isNeedListener) {

            for (ModifyShoppingCartListener listener : arrayListener.values()) {
                listener.removeCouponPrivilege(shopcartItemList, shoppingCartVo.getmTradeVo());
            }
        }
    }


    public static List<TradePrivilege> getTradePrivilegeByPromeIds(List<Long> promoIdList, TradeVo tradeVo) {
        if (promoIdList == null) {
            return null;
        }
        List<TradePrivilege> tradePrivilegeList = new ArrayList<TradePrivilege>();
        if (Utils.isNotEmpty(tradeVo.getCouponPrivilegeVoList())) {
            for (Long promoId : promoIdList) {
                for (CouponPrivilegeVo couponPrivilegeVo : tradeVo.getCouponPrivilegeVoList()) {
                    if (couponPrivilegeVo.getTradePrivilege() != null && couponPrivilegeVo.getTradePrivilege().getPromoId().equals(promoId)) {
                        tradePrivilegeList.add(couponPrivilegeVo.getTradePrivilege());
                        break;
                    }
                }
            }
        }
        if (Utils.isNotEmpty(tradeVo.getTradeItemList())) {
            for (Long promoId : promoIdList) {
                for (TradeItemVo itemVo : tradeVo.getTradeItemList()) {
                    if (itemVo.getCouponPrivilegeVo() != null && itemVo.getCouponPrivilegeVo().getTradePrivilege() != null
                            && itemVo.getCouponPrivilegeVo().getTradePrivilege().getPromoId().equals(promoId)) {
                        tradePrivilegeList.add(itemVo.getCouponPrivilegeVo().getTradePrivilege());
                    }
                }
            }
        }
        return tradePrivilegeList;
    }


    public void setOpenIdenty(ShoppingCartVo shoppingCartVo, String openId) {
        checkNeedBuildMainOrder(shoppingCartVo.getmTradeVo());
        if (!TextUtils.isEmpty(openId)) {
            if (shoppingCartVo.getmTradeVo().getTradeExtra() == null) {
                shoppingCartVo.getmTradeVo().setTradeExtra(new TradeExtra());
            }
            shoppingCartVo.getmTradeVo().getTradeExtra().setOpenIdenty(openId);
        } else {
            if (shoppingCartVo.getmTradeVo() != null && shoppingCartVo.getmTradeVo().getTradeExtra() != null) {
                shoppingCartVo.getmTradeVo().getTradeExtra().setOpenIdenty("");
            }
        }
    }


    protected void addSalesPromotion(@NonNull ShoppingCartVo shoppingCartVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> selectedShopcartItems, @Nullable CustomerResp customerNew) {
        if (MathSalesPromotionTool.canAddSalesPromotion(salesPromotionRuleVo, selectedShopcartItems, customerNew)) {
            MathSalesPromotionTool.mathManualAddSalesPromotion(salesPromotionRuleVo, selectedShopcartItems, shoppingCartVo.getmTradeVo(), customerNew);
            removeBanquetOnly(shoppingCartVo.getmTradeVo());
            removeGiftCouponPrivilege(selectedShopcartItems, shoppingCartVo);
            MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(shoppingCartVo),
                    shoppingCartVo.getmTradeVo());
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).addSalesPromotion(true, shoppingCartVo.getmTradeVo());
            }
        } else {
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).addSalesPromotion(false, null);
            }
        }
    }


    protected void removeGiftCouponPrivilege(@NonNull List<IShopcartItem> selectedItemList, @NonNull ShoppingCartVo shoppingCartVo) {
        for (IShopcartItem item : mergeShopcartItem(shoppingCartVo)) {
            for (IShopcartItem selectItem : selectedItemList) {
                if (item.getCouponPrivilegeVo() != null && item.getCouponPrivilegeVo().getTradePrivilege() != null
                        && item.getUuid().equals(selectItem.getUuid())) {
                    item.setCouponPrivilegeVo(null);
                }
            }
        }
    }


    protected void removeSalesPromotion(@NonNull ShoppingCartVo shoppingCartVo, Long planId, CustomerResp mCustomer, boolean isDinner) {
        TradeVo mTradeVo = shoppingCartVo.getmTradeVo();
        MathSalesPromotionTool.removeTradePlanActivity(mTradeVo.getTradePlanActivityList(), mTradeVo.getTradeItemPlanActivityList(), planId);
        batchMemberPrivilege(shoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(shoppingCartVo), mTradeVo);

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeSalesPromotion(shoppingCartVo.getmTradeVo());
        }
    }


    public void removeSalesPromotion(@NonNull ShoppingCartVo shoppingCartVo, String tradePlanUuid, CustomerResp mCustomer, boolean isDinner) {
        TradeVo mTradeVo = shoppingCartVo.getmTradeVo();
        MathSalesPromotionTool.removeTradePlanActivity(mTradeVo.getTradePlanActivityList(), mTradeVo.getTradeItemPlanActivityList(), tradePlanUuid);
        batchMemberPrivilege(shoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(shoppingCartVo), mTradeVo);

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeSalesPromotion(mTradeVo);
        }
    }


    public void removeSalesPromotion(TradeVo tradeVo) {
        List<TradePlanActivity> tradePlanActivityList = tradeVo.getTradePlanActivityList();
        if (Utils.isNotEmpty(tradePlanActivityList)) {
            Iterator<TradePlanActivity> iterator = tradePlanActivityList.iterator();
            while (iterator.hasNext()) {
                TradePlanActivity tradePlanActivity = iterator.next();
                if (tradePlanActivity.getId() == null) {
                    iterator.remove();
                } else {
                    tradePlanActivity.setStatusFlag(StatusFlag.INVALID);
                    tradePlanActivity.setChanged(true);
                }
            }
        }

        List<TradeItemPlanActivity> tradeItemPlanActivityList = tradeVo.getTradeItemPlanActivityList();
        if (Utils.isNotEmpty(tradeItemPlanActivityList)) {
            Iterator<TradeItemPlanActivity> iterator = tradeItemPlanActivityList.iterator();
            while (iterator.hasNext()) {
                TradeItemPlanActivity tradeItemPlanActivity = iterator.next();
                if (tradeItemPlanActivity.getId() == null) {
                    iterator.remove();
                } else {
                    tradeItemPlanActivity.setStatusFlag(StatusFlag.INVALID);
                    tradeItemPlanActivity.setChanged(true);
                }
            }
        }
    }


    public void autoAddSalesPromotion(@NonNull ShoppingCartVo shoppingCartVo) {
        removeSalesPromotion(shoppingCartVo.getmTradeVo());
        GiftShopcartItemSingleton.getInstance().getListPolicyDishshopVo().clear();
        List<SalesPromotionRuleVo> salesPromotionRuleVoList = SalesPromotionConvertHelper.getInstance().getSalesPromotionRuleVos();
        if (Utils.isEmpty(salesPromotionRuleVoList)) {
            return;
        }

        Collections.sort(salesPromotionRuleVoList, new Comparator<SalesPromotionRuleVo>() {
            @Override
            public int compare(SalesPromotionRuleVo o1, SalesPromotionRuleVo o2) {
                return o2.getRule().getServerCreateTime().compareTo(o1.getRule().getServerCreateTime());
            }
        });

        List<IShopcartItem> shopcartItemList = new ArrayList<>(mergeShopcartItem(shoppingCartVo));
        Map<SalesPromotionRuleVo, List<IShopcartItem>> map = new HashMap<>();
        for (SalesPromotionRuleVo salesPromotionRuleVo : salesPromotionRuleVoList) {
            List<IShopcartItem> temp = new ArrayList<>();
            for (IShopcartItem shopcartItem : shopcartItemList) {
                if (shopcartItem.getSaleType() != SaleType.WEIGHING && shopcartItem.getCouponPrivilegeVo() == null && !DiscountTool.isSinglePrivilege(shopcartItem) && salesPromotionRuleVo.isContainDish(shopcartItem.getDishShop())) {
                    temp.add(shopcartItem);
                }
            }

            if (MathSalesPromotionTool.canAddSalesPromotion(salesPromotionRuleVo, temp, CustomerManager.getInstance().getLoginCustomer())) {
                map.put(salesPromotionRuleVo, temp);
                shopcartItemList.removeAll(temp);
            }

            if (Utils.isEmpty(shopcartItemList)) {
                break;
            }
        }

        if (Utils.isEmpty(map.values())) {
            return;
        }

        for (SalesPromotionRuleVo salesPromotionRuleVo : map.keySet()) {
            MathSalesPromotionTool.mathManualAddSalesPromotion(salesPromotionRuleVo, map.get(salesPromotionRuleVo), shoppingCartVo.getmTradeVo(), CustomerManager.getInstance().getLoginCustomer());
        }

        removeBanquetOnly(shoppingCartVo.getmTradeVo());
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).addSalesPromotion(true, shoppingCartVo.getmTradeVo());
        }
    }
}
