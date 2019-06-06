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

/**
 * @Date：2015年9月16日 下午4:58:59
 * @Description: g购物车基类
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class BaseShoppingCart {

    protected Map<Integer, ModifyShoppingCartListener> arrayListener =
            new HashMap<Integer, ModifyShoppingCartListener>();

    public Map<Integer, ModifyShoppingCartListener> getArrayListener() {
        return arrayListener;
    }

    /**
     * @Title: checkNeedBuildMainOrder
     * @Description: 验证是否需要创建订单主体
     * @Param @param mTradeVo TODO
     * @Return void 返回类型
     */
    public void checkNeedBuildMainOrder(TradeVo mTradeVo) {
        if (mTradeVo == null || mTradeVo.getTrade() == null) {
//			buildMainOrder(mTradeVo);
            CreateTradeTool.buildMainTradeVo(mTradeVo);
            if (mTradeVo.getTrade() == null) {
                for (ModifyShoppingCartListener listener : arrayListener.values()) {
                    listener.exception("");
                }
            }
        }
    }

    /**
     * 构建交易主单
     *

     * @Title: buildOrderBase
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
//	public void buildMainOrder(TradeVo mTradeVo) {
//		if (AuthUserCache.getAuthUser() != null) {
//			CreateTradeTool.buildMainTradeVo(mTradeVo);
//		}
//	}

    /**
     * @Title: setOrderBusinessType
     * @Description: 设置订单业务形态
     * @Param @param mBusinessType TODO
     * @Return void 返回类型
     */
    public void setOrderBusinessType(ShoppingCartVo mShoppingCartVo, BusinessType mBusinessType) {
        checkNeedBuildMainOrder(mShoppingCartVo.getmTradeVo());
        if (mShoppingCartVo.getmTradeVo() != null && mShoppingCartVo.getmTradeVo().getTrade() != null
                && mBusinessType != null) {
            mShoppingCartVo.getmTradeVo().getTrade().setBusinessType(mBusinessType);
        }
    }

    /**
     * 设置交付方式：1:HERE:内用，在店内点餐，可以绑定桌号或者叫餐号
     * 2:SEND:外送，需要填写收货人信息和期望送达时间
     *
     * @Title: setOrderType
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
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

    /**
     * 设置所属领域
     *
     * @param mShoppingCartVo
     * @param dominType
     */
    public void setDominType(ShoppingCartVo mShoppingCartVo, DomainType dominType) {
        if (mShoppingCartVo.getmTradeVo() == null || mShoppingCartVo.getmTradeVo().getTrade() == null
                || dominType == null) {
            return;
        }
        mShoppingCartVo.getmTradeVo().getTrade().setDomainType(dominType);
    }

    /**
     * @Title: getTempShopItem
     * @Description: 获取零时套餐保存数据
     * @Param @return TODO
     * @Return ShopcartItem 返回类型
     */
    public Boolean isHaveTempDish() {
        return false;
    }

    /**
     * @Title: addShippingToCart
     * @Description: 添加菜品到购物车
     * @Param @param mShopcartItem
     * @Param @param isTempDish ture:是临时保存菜品 false:不是临时保存菜品
     * 是时时加入购物车
     * @Return void 返回类型
     */
    public void addShippingToCart(ShoppingCartVo mShoppingCartVo, ShopcartItem mShopcartItem, Boolean isTempDish) {
        checkNeedBuildMainOrder(mShoppingCartVo.getmTradeVo());
        // 该处代码是用于在设置菜品子菜、属性时避免重复添加的问题
        IShopcartItem value = getShopcartItemFromList(mShoppingCartVo, mShopcartItem.getUuid());
        if (value != null) {
            //针对规格切换后更新tradeTable
            mShopcartItem.setTradeTable(value.getTradeTableUuid(), value.getTradeTableId());
            OperateShoppingCart.updateDish(mShoppingCartVo.getmTradeVo(),
                    mShoppingCartVo.getListOrderDishshopVo(),
                    mShopcartItem);
            resetSelectDishQTY(mShoppingCartVo);
        } else {
            //根据tradeType 区分联台主单菜、子单单点菜、非联台单菜
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
        // 该处代码是用于在设置菜品子菜、属性时避免重复添加的问题
        IShopcartItem value = getShopcartItemFromList(mShoppingCartVo, mShopcartItem.getUuid());

        if (value != null) {
            OperateShoppingCart.updateReadOnlyShopcarItem(mShoppingCartVo.getmTradeVo(),
                    mShoppingCartVo.getListIShopcatItem(),
                    mShopcartItem);
            resetSelectDishQTY(mShoppingCartVo);
        } else {
            if (mShoppingCartVo.getDinnertableTradeInfo() != null) {
//				DinnertableTradeInfo info = mShoppingCartVo.getDinnertableTradeInfo();
//				mShopcartItem.setTradeTable(info.getTradeTableUuid(), info.getTradeTableId());
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

    /**
     * 添加菜品座位号关联
     *
     * @param mShoppingCartVo
     * @param tradeItemExtraDinner
     */
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

    /**
     * @Title: isAddTempDish
     * @Description: 变更临时菜品状态
     * @Param @param mShopcartItem
     * @Param @param isAdd isAdd true:确认临时套餐，false:移除临时套餐
     * @Return void 返回类型
     */
    public void isCheckAdd(ShoppingCartVo mShoppingCartVo, ShopcartItem mShopcartItem, Boolean isAdd) {
        if (!isAdd) {
            removeDish(mShoppingCartVo, mShopcartItem);
        }
        mShoppingCartVo.setTempShopItem(null);
    }

    /**
     * @Title: getShopcartItemByUUID
     * @Description: 根据菜品UUID获取已添加购物车中菜品数据信息
     * @Param @param uuid TODO
     * @Return void 返回类型
     */
    public ShopcartItem getShopcartItemByUUID(ShoppingCartVo mShoppingCartVo, String uuid) {
        for (ShopcartItem mShopcartItem : mShoppingCartVo.getListOrderDishshopVo()) {
            if (mShopcartItem.getUuid().equals(uuid)) {
                return mShopcartItem;
            }
        }
        return null;
    }

    /**
     * @Title: getIShopcartItemBaseByUUID
     * @Description: 根据菜品UUID获取已添加购物车中菜品数据信息
     * @Param @param uuid TODO
     * @Return void 返回类型
     */
    public IShopcartItem getIShopcartItemByUUID(ShoppingCartVo mShoppingCartVo, String uuid) {
        for (IShopcartItem iShopcartItem : mShoppingCartVo.getListIShopcatItem()) {
            if (iShopcartItem.getUuid().equals(uuid)) {
                return iShopcartItem;
            }
        }
        return null;
    }

    /**
     * @Title: getShopcartItemBySetmeal
     * @Description: 根据子菜查询套餐外壳
     * @Param @param setmealUUID
     * @Param @return TODO
     * @Return ShopcartItem 返回类型
     */
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

    /**
     * @Title: removeDish
     * @Description: 根据订单将整个菜品（单菜、套餐、子菜）移除购车
     * @Param @param mTradeItemVo TODO
     * @Return void 返回类型
     */
    public ShopcartItem removeTradeItem(ShoppingCartVo mShoppingCartVo, TradeItemVo mTradeItemVo) {

        ShopcartItem mShopcartItem = OperateShoppingCart.removTradeItem(mShoppingCartVo.getmTradeVo(),
                mShoppingCartVo.getListOrderDishshopVo(),
                mTradeItemVo);

        if (mShopcartItem != null) {
            subtractDishQTY(mShoppingCartVo, mShopcartItem);
        }
        return mShopcartItem;

    }

    /**
     * @Title: removeDish
     * @Description: 根据菜品将整个菜品（单菜、套餐、子菜）移除购车
     * @Param @param mShopcartItemBase TODO
     * @Return void 返回类型
     */
    public void removeDish(ShoppingCartVo mShoppingCartVo, IShopcartItemBase mShopcartItemBase) {
        if (mShopcartItemBase instanceof ShopcartItemBase) {
            removeShopcartItem(mShoppingCartVo, (ShopcartItemBase) mShopcartItemBase);
            CardServiceTool.removeService(mShopcartItemBase);
        } else if (mShopcartItemBase instanceof ReadonlyShopcartItem) {
            // 删除退回菜品生成的新只读临时商品
            removeReadOnlyShopcartItem(mShoppingCartVo, (ReadonlyShopcartItem) mShopcartItemBase);
        }
    }

    /**
     * @Title: removeReadOnlyShopcartItem
     * @Description: 删除只读的临时商品
     * @Param @param mShoppingCartVo
     * @Param @param mShopcartItemBase TODO
     * @Return void 返回类型
     */
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
//				subtractDishQTY(mShoppingCartVo, mIShopcartItem);
                break;
            }
        }

    }

    public void removeShoppingcartItem(ShoppingCartVo mShoppingCartVo, IShopcartItem mShopcartItem,
                                       SetmealShopcartItem mSetmealShopcartItem, ChangePageListener mChangePageListener,
                                       FragmentManager mFragmentManager) {
        // 如果是已下单菜品删除则只需修改菜品状态为无效
        if (mShopcartItem.getId() != null) {
            removeReadonlyShopcartItem(mShoppingCartVo, (ReadonlyShopcartItem) mShopcartItem);
            return;
        }

        // 表示删除的是整个套餐或单菜
        if (mSetmealShopcartItem == null) {
            removeDish(mShoppingCartVo, mShopcartItem);
            // 判断删除的是当前正在操作的菜品
            if (mShoppingCartVo.getTempShopItem() != null
                    && mShoppingCartVo.getTempShopItem().getUuid().equals(mShopcartItem.getUuid())) {
                mShoppingCartVo.setTempShopItem(null);
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.NONEEDCHECK, true);
                mChangePageListener.changePage(ChangePageListener.ORDERDISHLIST, null);
            }
        } else {
            // 删除的是套餐子菜
            switch (mShopcartItem.getSetmealManager().testModify(mSetmealShopcartItem, BigDecimal.ZERO)) {
                case SUCCESSFUL:
                    if (mSetmealShopcartItem != null) {
                        removeDish(mShoppingCartVo, mSetmealShopcartItem);
                        mShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);
                        // 如果当前打开的删除子菜的属性界面则需要跳转
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
                    // 删除的子菜正处于当前操作套餐 并且不处在删除子菜列表界面
                    if (mShoppingCartVo.getTempShopItem() != null
                            && mShopcartItem.getUuid().equals(mShoppingCartVo.getTempShopItem().getUuid())
                            && mShoppingCartVo.getIndexPage() == ChangePageListener.DISHCOMBO) {
                        removeDish(mShoppingCartVo, mSetmealShopcartItem);
                        mShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);

                    } else if (mShoppingCartVo.getTempShopItem() != null && mShopcartItem != null
                            && mShopcartItem.getUuid().equals(mShoppingCartVo.getTempShopItem().getUuid())
                            && mShoppingCartVo.getIndexPage() == ChangePageListener.DISHPROPERTY
                            && mShoppingCartVo.getShowPropertyPageDishUUID().equals(mSetmealShopcartItem.getUuid())) {
                        // 删除的子菜正处于当前操作套餐 并且不处在删除子菜属性界面
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
                        // 删除的子菜正处于当前操作套餐，并且当前所处属性界面不是删除子菜属性界面
                        removeDish(mShoppingCartVo, mSetmealShopcartItem);
                        mShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);

                    } else if (mShoppingCartVo.getTempShopItem() != null
                            && !mShopcartItem.getUuid().equals(mShoppingCartVo.getTempShopItem().getUuid())) {
                        // 删除的子菜不是当前正在操作的菜品
                        isCheckVaild(mShoppingCartVo,
                                ChangePageListener.DISHCOMBO,
                                mChangePageListener,
                                mFragmentManager,
                                mShopcartItem,
                                mSetmealShopcartItem);
                    } else if (mShoppingCartVo.getTempShopItem() == null) {
                        // 当前没有操作菜品
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
        // 操作菜品跟当前菜品不是同一个菜，并且当前操作菜品不满足条件
        if (mShoppingCartVo.getTempShopItem() != null && mShoppingCartVo.getTempShopItem().getSetmealManager() != null
                && !mShoppingCartVo.getTempShopItem().getSetmealManager().isValid()) {
            // 当前选择套餐满未满足套餐选择规则则填出对话框 确认是否离开此界面
            showCheckDialog(mShoppingCartVo,
                    mPageNo,
                    mChangePageListener,
                    mFragmentManager,
                    mShopcartItem,
                    mSetmealShopcartItem);

        } else {
            // 当前操作菜品满足规则。则跳转到因删除导致不满足套餐规则的菜品中
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

                        // 删除菜品
                        if (mSetmealShopcartItem != null) {
                            ShopcartItem deleteShopcartItem =
                                    getShopcartItemByUUID(mShoppingCartVo, mSetmealShopcartItem.getParentUuid());
                            deleteShopcartItem.getSetmealManager().modifySetmeal(mSetmealShopcartItem, BigDecimal.ZERO);
                            removeDish(mShoppingCartVo, mSetmealShopcartItem);
                        }

                        // 移除临时未完成套餐
                        if (mShoppingCartVo.getTempShopItem() != null) {
                            isCheckAdd(mShoppingCartVo, mShoppingCartVo.getTempShopItem(), false);
                            mShoppingCartVo.setTempShopItem(null);
                        }

                        // 调整界面
                        if (mChangePageListener != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID,
                                    mShopcartItem != null ? mShopcartItem.getUuid() : "");
                            bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
                            bundle.putBoolean(Constant.NONEEDCHECK, true);
                            mChangePageListener.changePage(mPageNo, bundle);
                        }

                        // 计算订单总价格
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

    /**
     * @Title: updateDish
     * @Description: 菜品信息变更
     * @Param @param mShopcartItem TODO
     * @Return void 返回类型
     */
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

    /**
     * @Title: isCheckUpdate
     * @Description: 确认更新菜品数据信息
     * @Param @param mShoppingCartVo
     * @Param @param mShopcartItem
     * @Param @param isUpdate TODO
     * @Return void 返回类型
     */
    public void isCheckUpdate(ShoppingCartVo mShoppingCartVo, ShopcartItem mShopcartItem, Boolean isUpdate) {
        if (!isUpdate) {
            updateDish(mShoppingCartVo, mShoppingCartVo.getUpdateTempShopItem(), false);
        }
        mShoppingCartVo.setUpdateTempShopItem(null);
        mShoppingCartVo.setTempShopItem(null);
    }

    /**
     * @Title: isEditDishNew
     * @Description: 验证当前菜品是否操作完成
     * @Param @return TODO
     * @Return Boolean 返回类型
     */
    public Boolean isEditDishNew(ShoppingCartVo mShoppingCartVo) {
        if (mShoppingCartVo.getUpdateTempShopItem() == null && mShoppingCartVo.getTempShopItem() == null) {
            return false;
        }
        return true;
    }

    /**
     * @Title: resetSelectDishQTY
     * @Description: 构建菜品所点数量
     * @Param TODO
     * @Return void 返回类型
     */
    public void resetSelectDishQTY(ShoppingCartVo mShoppingCartVo) {
        mShoppingCartVo.getSelectDishQTYMap().clear();
        for (IShopcartItemBase mShopcartItemBase : mergeShopcartItem(mShoppingCartVo)) {
            if (mShopcartItemBase.getStatusFlag() == StatusFlag.VALID) {
                addDishQTY(mShoppingCartVo, mShopcartItemBase);
            }
        }
    }

    /**
     * @Title: mathDishQTY
     * @Description: 添加菜品所点数量
     * @Param @param mShopcartItem TODO
     * @Return void 返回类型
     */
    public void addDishQTY(ShoppingCartVo mShoppingCartVo, IShopcartItemBase mShopcartItem) {
        BigDecimal selectQTY = mShoppingCartVo.getSelectDishQTYMap().get(mShopcartItem.getSkuUuid());
        BigDecimal itemQTY = mShopcartItem.getSingleQty();
        //团餐只展示一桌的数量
        if (mShopcartItem.isGroupDish()) {
            itemQTY = ShopcartItemUtils.getDisplyQty(mShopcartItem, mShoppingCartVo.getmTradeVo().getDeskCount());
        }
        if (selectQTY != null) {
            mShoppingCartVo.getSelectDishQTYMap().put(mShopcartItem.getSkuUuid(), itemQTY.add(selectQTY));
        } else {
            mShoppingCartVo.getSelectDishQTYMap().put(mShopcartItem.getSkuUuid(), itemQTY);
        }
    }

    /**
     *

     * @Title: subtractDishQty
     * @Description: 退回商品数据计算
     * @Param @param mShoppingCartVo
     * @Param @param mShopcartItem TODO
     * @Return void 返回类型
     */
//	public void subtractReturnQty(ShoppingCartVo mShoppingCartVo, IShopcartItemBase mShopcartItem) {
//		BigDecimal selectQTY = mShoppingCartVo.getSelectDishQTYMap().get(mShopcartItem.getSkuUuid());
//		BigDecimal returnQTY = mShopcartItem.getReturnQty();
//		mShoppingCartVo.getSelectDishQTYMap().put(mShopcartItem.getSkuUuid(), selectQTY.add(returnQTY));
//	}

    /**
     * @Title: multiplyDishQTY
     * @Description: 减少菜品所点数量
     * @Param TODO
     * @Return void 返回类型
     */
    public void subtractDishQTY(ShoppingCartVo mShoppingCartVo, IShopcartItemBase mShopcartItem) {
        BigDecimal selectQTY = mShoppingCartVo.getSelectDishQTYMap().get(mShopcartItem.getSkuUuid());
        BigDecimal itemQTY = mShopcartItem.getSingleQty();
        //团餐只展示一桌的数量
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

    /**
     * @Title: getDishSelectQTY
     * @Description: 获取菜品选中数量
     * @Param @return TODO
     * @Return Map<Long                               ,                               BigDecimal> 返回类型
     */
    public Map<String, BigDecimal> getDishSelectQTY(ShoppingCartVo mShoppingCartVo) {
        return mShoppingCartVo.getSelectDishQTYMap();
    }

    public int getSelectDishCount(ShoppingCartVo mShoppingCartVo) {
        return mergeShopcartItem(mShoppingCartVo).size();
    }

    /**
     * @Title: getShopcartItem
     * @Description: TODO
     * @Param @param uuid
     * @Param @return TODO
     * @Return ShopcartItem 返回类型
     */
    public ShopcartItem getShopcartItem(ShoppingCartVo mShoppingCartVo, String uuid) {
        for (ShopcartItem shopItem : mShoppingCartVo.getListOrderDishshopVo()) {
            if (shopItem.getUuid().equals(uuid)) {
                return shopItem;
            }
        }

        // 查询的是套餐中得子菜
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

    /**
     * @Title: getIShopcartItem
     * @Description: 根据套餐、子菜 或套餐子菜uuid获取菜品信息
     * @Param @param mShoppingCartVo
     * @Param @param uuid
     * @Param @return TODO
     * @Return IShopcartItem 返回类型
     */
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

    /**
     * @Title: getIShopcartItem
     * @Description: 根据套餐、子菜 或套餐子菜uuid获取菜品信息
     * @Param @param mShoppingCartVo
     * @Param @param id
     * @Param @return TODO
     * @Return IShopcartItem 返回类型
     */
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

    /**
     * @Title: checkDishIsVaild
     * @Description: 挂单时验证当前操作菜品是否完成 是否可以执行挂单操作
     * @Param @param mShopcartItem
     * @Param @return TODO
     * @Return Boolean 返回类型
     */
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

    /**
     * @Title: checkDishIsVaild
     * @Description: 切换界面时验证当前菜品是否满足条件
     * @Param @param mShopcartItem
     * @Param @return TODO
     * @Return Boolean 返回类型
     */
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

    /**
     * @Title: getShopcartItemFromList
     * @Description: 从购物车中获取已加入菜品
     * @Param @param listOrderDishshopVo
     * @Param @param mShopcartItem
     * @Param @return TODO
     * @Return ShopcartItem 返回类型
     */
    public IShopcartItem getShopcartItemFromList(ShoppingCartVo mShoppingCartVo, String uuid) {
        for (IShopcartItem item : mergeShopcartItem(mShoppingCartVo)) {
            if (item.getUuid().equals(uuid)) {
                return item;
            }
        }
        return null;
    }

    /**
     * @Title: getMinPrice
     * @Description: 获取选中菜品中最低价格
     * @Param @return TODO
     * @Return BigDecimal 返回类型
     */
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

    /**
     * @Title: getTradeAmoutCanDiscount
     * @Description: 获取可以参与折扣的整单价格
     * @Param @param mShoppingCartVo
     * @Param @return TODO
     * @Return BigDecimal 返回类型
     */
    public BigDecimal getTradeAmoutCanDiscount(ShoppingCartVo mShoppingCartVo) {
        List<IShopcartItem> iShopcartItem = mergeShopcartItem(mShoppingCartVo);

        //用于保存不能参整单打折菜品总价
        BigDecimal noDiscountAllAmout = BigDecimal.ZERO;
        BigDecimal noDiscPrivilegeAmout = BigDecimal.ZERO;
        for (IShopcartItem item : iShopcartItem) {
            if (item.getStatusFlag() == StatusFlag.VALID) {
                //判断菜品是否能参与整单打折
                if (item.getEnableWholePrivilege() == Bool.YES) {
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

    /**
     * @Title: getTotalRebatedDiscount
     * @Description: 获取批量这样菜品总优惠金额
     * @Param @param mShoppingCartVo
     * @Param @return TODO
     * @Return BigDecimal 返回类型
     */
    public BigDecimal getTotalRebatedDiscount(ShoppingCartVo mShoppingCartVo, BigDecimal discountValue) {
        BigDecimal totalDiscount = BigDecimal.ZERO;
        for (IShopcartItem orderDish : mergeShopcartItem(mShoppingCartVo)) {
            if (orderDish.isSelected()) {
                totalDiscount = totalDiscount.add(discountValue);
            }

        }
        return totalDiscount;
    }

    /**
     * 设置会员
     *
     * @Title: setCustomerV5
     * @Description: TODOR
     * @Param TODO
     * @Return void 返回类型
     */
    public void setCustomer(ShoppingCartVo mShoppingCartVo, TradeCustomer mTradeCustomer) {
        if (mShoppingCartVo.getArrayTradeCustomer() == null) {
            mShoppingCartVo.setArrayTradeCustomer(new HashMap<Integer, TradeCustomer>());

        }
        //移除登录会员（实体卡登录、会员账号登录）
        removeCustomer(mShoppingCartVo);
        //登录会员信息不为null是表示有登录会员，为null时表示注销登录
        if (mTradeCustomer != null) {
            //添加新登录会员信息
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

    /**
     * @Title: setMemberCustomer
     * @Description: 保存登录会员信息到TradeVo对象中
     * @Param @param mTradeCustomer TODO
     * @Return void 返回类型
     */
    public void setMemberCustomer(ShoppingCartVo mShoppingCartVo, Map<Integer, TradeCustomer> mTradeCustomers) {

        checkNeedBuildMainOrder(mShoppingCartVo.getmTradeVo());

        // 遍历移除原有设置的会员信息，根据TradeCustomerList中会员类型来移除
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
        // 添加会员，如果注销会员这不加入到该信息列表中
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
        //设置会员卡卡号
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

    /**
     * @Title: buildCallCustomer
     * @Description: 保存呼入电话信息到tradeVo
     * @Param @param mTradeCustomer TODO
     * @Return void 返回类型
     */
    protected void buildCallCustomer(ShoppingCartVo mShoppingCartVo, TradeCustomer mTradeCustomer) {
        checkNeedBuildMainOrder(mShoppingCartVo.getmTradeVo());
        if (mTradeCustomer != null) {
            // 遍历移除原有设置的呼入电话用户信息，根据TradeCustomerList中会员类型来移除
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

            // 如果呼入电话用户名为空
            String haveNoName = BaseApplication.sInstance.getResources().getString(R.string.have_no_name);
            if (TextUtils.isEmpty(mTradeCustomer.getCustomerName())
                    || haveNoName.equals(mTradeCustomer.getCustomerName())) {
                // 如果呼入电话用户名为空，同时在自提或外卖信息中用设置的客户电话号码相同时，则将该用户名绑定到呼入电话中
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

    /**
     * 设置整单备注
     *
     * @Title: setComment
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    public void setRemarks(ShoppingCartVo mShoppingCartVo, String remarks) {
        checkNeedBuildMainOrder(mShoppingCartVo.getmTradeVo());
        mShoppingCartVo.getmTradeVo().getTrade().setTradeMemo(remarks);
        mShoppingCartVo.getmTradeVo().getTrade().setChanged(true);
    }

    /**
     * @Title: removeSetmealRemark
     * @Description: 移除套餐子菜的备注信息
     * @Param @param mShoppingCartVo
     * @Param @param mSetmeal TODO
     * @Return void 返回类型
     */
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

    /**
     * @Title: createOrder
     * @Description: 构建订单对象
     * @Param @return TODO
     * @Return TradeVo 返回类型
     */
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
        // 挂单不考虑优惠卷是否满足使用规则
        if (isGuaDan) {
            // 如果是挂单并且使用的优惠券未满足使用规则时，对优化金额设置为0
            List<CouponPrivilegeVo> couponPrivilegeVoList = mShoppingCartVo.getmTradeVo().getCouponPrivilegeVoList();
            if (couponPrivilegeVoList != null) {
                for (CouponPrivilegeVo couponPrivilegeVo : couponPrivilegeVoList) {
                    if (!couponPrivilegeVo.isActived()) {
                        couponPrivilegeVo.getTradePrivilege().setPrivilegeAmount(BigDecimal.ZERO);
                    }
                }
            }
        } else {
            // 判断优惠卷是否满足使用规则,如不面足规则则下单时移除优惠劵信息
            // if
            // (mShoppingCartVo.getmTradeVo().getCouponPrivilege()
            // != null
            // &&
            // !mShoppingCartVo.getmTradeVo().getCouponPrivilege().isValid())
            // {
            // mShoppingCartVo.getmTradeVo().setCouponPrivilege(null);
            // }
            // // 判断积分抵现是否满足规则，如果不满足规则则下单时移除会员积分抵现
            // if
            // (mShoppingCartVo.getmTradeVo().getIntegralCashPrivilegeVo()
            // != null
            // &&
            // !mShoppingCartVo.getmTradeVo().getIntegralCashPrivilegeVo().isValid())
            // {
            // mShoppingCartVo.getmTradeVo().setIntegralCashPrivilegeVo(null);
            //
            // }
        }

        CreateTradeTool.createTradeVo(mShoppingCartVo.getListOrderDishshopVo(),
                mShoppingCartVo.getListIShopcatItem(),
                mShoppingCartVo.getmTradeVo(),
                mShoppingCartVo.getmTakeOutInfo());

        TradeVo tradeVo = mShoppingCartVo.getmTradeVo();
        tradeVo.setTradeDeposit(mShoppingCartVo.getmTradeVo().getTradeDeposit());
        tradeVo.getTrade().setDishKindCount(mShoppingCartVo.getSelectDishQTYMap().size());
        tradeVo.setTradeUser(mShoppingCartVo.getTradeUser());// 添加销售员add v8.1
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

    /**
     * @Title: createSalesReturn
     * @Description: 无单退货时所有金额数量都取反
     * @Param @param mTradeVo TODO
     * @Return void 返回类型
     */
    public TradeVo createSalesReturn(TradeVo mSalesTradeVo) {
        Trade mTrade = mSalesTradeVo.getTrade();
        if (mTrade != null) {
            mTrade.setTradeAmountBefore(mTrade.getTradeAmountBefore().negate());
            mTrade.setTradeAmount(mTrade.getTradeAmount().negate());
            mTrade.setSaleAmount(mTrade.getSaleAmount().negate());
            mTrade.setDishKindCount(new BigDecimal(mTrade.getDishKindCount()).negate().intValue());
            mTrade.setPrivilegeAmount(mTrade.getPrivilegeAmount().negate());
        }

        //整单优惠或者附加费取反
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

                // 折扣信息
                TradePrivilege itemTradePrivilege = item.getTradeItemPrivilege();
                if (itemTradePrivilege != null) {
                    itemTradePrivilege.setPrivilegeAmount(itemTradePrivilege.getPrivilegeAmount().negate());
                }

                // 属性
                List<TradeItemProperty> itemProperty = item.getTradeItemPropertyList();
                if (itemProperty != null) {
                    for (TradeItemProperty property : itemProperty) {
                        property.setAmount(property.getAmount().negate());
                        property.setQuantity(property.getQuantity().negate());
                    }
                }
            }
        }

        //营销活动
        List<TradePlanActivity> tradePlanActivities = mSalesTradeVo.getTradePlanActivityList();
        if (tradePlanActivities != null) {
            for (TradePlanActivity tradePlanActivity : tradePlanActivities) {
                tradePlanActivity.setOfferValue(tradePlanActivity.getOfferValue().negate());
            }
        }
        //消费税
        List<TradeTax> tradeTaxs = mSalesTradeVo.getTradeTaxs();
        if (tradeTaxs != null) {
            for (TradeTax tradeTax : tradeTaxs) {
                tradeTax.setTaxAmount(tradeTax.getTaxAmount().negate());
                tradeTax.setTaxableIncome(tradeTax.getTaxableIncome().negate());
            }
        }
        return mSalesTradeVo;
    }

    /**
     * @Title: batchMemberPrivilege
     * @Description: 批量对已加入购物车中的菜品设置会员价
     * @Param mShoppingCartVo
     * @Param mCustomer TODO
     * @Return void 返回类型
     */
    public void batchMemberPrivilege(ShoppingCartVo mShoppingCartVo, CustomerResp mCustomer, Boolean isDinner) {
        if (mCustomer == null) {
            return;
        }

        //获取购物车中当前所有的菜品进行遍历
        for (IShopcartItemBase item : mergeShopcartItem(mShoppingCartVo)) {
            //如果该商品已有手动折扣或营销活动时时，就不能将其覆盖

            //餐标下的菜品不能会员优惠
            if ((item.getPrivilege() != null &&
                    (item.getPrivilege().getPrivilegeType() == PrivilegeType.DISCOUNT
                            || item.getPrivilege().getPrivilegeType() == PrivilegeType.REBATE
                            || item.getPrivilege().getPrivilegeType() == PrivilegeType.FREE
                            || item.getPrivilege().getPrivilegeType() == PrivilegeType.GIVE)
                    && item.getPrivilege().isValid()) || item.isGroupDish()) {
                continue;
            }
            //次卡服务不参与会员价
            if (item.getCardServicePrivilgeVo() != null && item.getCardServicePrivilgeVo().isPrivilegeValid()) {
                continue;
            }

            if (item.getPrivilege() != null && (item.getPrivilege().getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT || item.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_PRICE || item.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_REBATE)) {
                item.setPrivilege(null);
            }

            setDishMemberPrivilege(mShoppingCartVo, item, isDinner);
        }
    }

    /**
     * @Title: batchMemberPrivilege
     * @Description: 批量对已加入购物车中的菜品设置会员价
     * @Param mShoppingCartVo
     * @Param mCustomer TODO
     * @Return void 返回类型
     */
    public void batchMemberPrivilege(ShoppingCartVo mShoppingCartVo) {
        CustomerResp mCustomer = CustomerManager.getInstance().getLoginCustomer();
        if (mCustomer == null) {
            return;
        }

        Map<String, TradeItemPlanActivity> tradeItemPlanActivityMap = covertItemPlanListToMap(mShoppingCartVo.getmTradeVo().getTradeItemPlanActivityList());

        for (IShopcartItemBase item : mergeShopcartItem(mShoppingCartVo)) {
            //如果该商品已有手动折扣或营销活动时时，就不能将其覆盖
            //餐标下的菜品不能会员优惠
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

            //次卡服务不参与会员价
            if (item.getCardServicePrivilgeVo() != null && item.getCardServicePrivilgeVo().isPrivilegeValid()) {
                continue;
            }

            if (item.getPrivilege() != null && (item.getPrivilege().getPrivilegeType() == PrivilegeType.AUTO_DISCOUNT || item.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_PRICE || item.getPrivilege().getPrivilegeType() == PrivilegeType.MEMBER_REBATE)) {
                item.setPrivilege(null);
            }

            setDishMemberPrivilege(mShoppingCartVo, item, false);
        }
    }

    /**
     * 是否有促销活动
     *
     * @param item
     * @return
     */
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

    /**
     * 是否有会员的营销活动
     *
     * @param item
     * @return
     */
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

    /**
     * @Title: covertItemPlanListToMap
     * @Description: 将list转换成map
     * @Param @param tradeItemPlanList
     * @Param @return TODO
     * @Return Map<String                               ,                               TradeItemPlanActivity> 返回类型
     */
    public static Map<String, TradeItemPlanActivity> covertItemPlanListToMap(List<TradeItemPlanActivity> tradeItemPlanList) {
        Map<String, TradeItemPlanActivity> itemMap = new HashMap<String, TradeItemPlanActivity>();
        // 将已保存的活动list转为map方便获取
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

    /**
     * @Title: setDishMemberPrivilege
     * @Description: 给单菜设置会员价优惠信息;这个方法用于快餐和拆单界面，正餐点菜界面customer用传人的
     * @Param mShoppingCartVo
     * @Param mIShopcartItemBase
     * @Param isSeparat 是否是拆单
     * @Return void 返回类型
     */
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

    /**
     * 针对非购物车计算时调用，计算单品会员价
     *
     * @param tradeVo
     * @param mIShopcartItemBase
     * @param mCustomer
     * @param isDinner
     */
    public static void setDishMemberPrivilege(TradeVo tradeVo, IShopcartItemBase mIShopcartItemBase, CustomerResp mCustomer, Boolean isDinner) {

        //针对已添加到购物车中的商品在后台删除后找不到对应的disShop
        if (mIShopcartItemBase.getDishShop() == null) {
            return;
        }

        // v8.15.0 雅座合并 雅座非正餐不展示会员优惠价格
        /*if (KeyAt.getKeyAtType() == KeyAtType.YAZUO && tradeVo.getTrade().getBusinessType() != BusinessType.DINNER) {
            return;
        }*/

        if (mIShopcartItemBase.getIsChangedPrice() == Bool.YES) {//如果商品已经变价就不参与会员价、会员折扣
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

        //次卡服务不参与会员价
        if (mIShopcartItemBase.getCardServicePrivilgeVo() != null && mIShopcartItemBase.getCardServicePrivilgeVo().isPrivilegeValid()) {
            return;
        }

        if (mCustomer == null) {
            return;
        }

        //卡过期不享受会员权益
        if (mCustomer.card != null && mCustomer.card.getRightStatus() != CardRechagingStatus.EFFECTIVE) {
            return;
        }

        // v8.12.0 会员日
        CrmMemberDay crmMemberDay = ServerSettingCache.getInstance().getCrmMemberDay();
        boolean isInMemberDay = isInMemberDay(crmMemberDay);


        EcCardLevelSetting mCardLevelSetting = null;

        if (mCustomer.card != null) {
            mCardLevelSetting = mCustomer.card.getCardLevelSetting();
        }

        //根据会员信息和商品id获取
        CustomerDal operates = OperatesFactory.create(CustomerDal.class);
        Long dishID = mIShopcartItemBase.getDishShop().getBrandDishId();
        DishMemberPrice mDishMemberPrice = null;

        //判断cardTempLetId会员卡对应的销售策略是否为null,如为null表示是虚拟会员登录，不为null表示实体卡登录
        mDishMemberPrice = operates.findDishMemberPriceByDishId(dishID, mCustomer.levelId);
//        if (!isInMemberDay && mCardLevelSetting == null && mCustomer.levelId != null) {
//            mDishMemberPrice = operates.findDishMemberPriceByDishId(dishID, mCustomer.levelId);
//        } else if (isInMemberDay && crmMemberDay.getMemberPriceTempletId() != null && mCustomer.levelId != null) {
//            mDishMemberPrice = operates.queryMemberPrice(crmMemberDay.getMemberPriceTempletId(), dishID);
//        } else if (mCardLevelSetting != null && mCardLevelSetting.getMemberPriceTempletId() != null) {//如mCardLevelSetting.getMemberPriceTempletId()为空，这表示该卡没有会员价
//            mDishMemberPrice = operates.queryMemberPrice(mCardLevelSetting.getMemberPriceTempletId(), dishID);
//        }
        Long privilegeTime = tradeVo.getTrade().getServerCreateTime();
        if (privilegeTime == null) {
            privilegeTime = System.currentTimeMillis();
        }
        if (mDishMemberPrice != null && isInDate(privilegeTime, mDishMemberPrice.getPeriodStart(), mDishMemberPrice.getPeriodEnd())) {
            TradePrivilege mTradePrivilege = BuildPrivilegeTool.mathMemberPrice(mIShopcartItemBase, mDishMemberPrice, tradeVo.getTrade().getUuid());
            mIShopcartItemBase.setPrivilege(mTradePrivilege);
        } else {
            //没有查到会员优惠，将之前的会员优惠移除v7.7添加
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


    /**
     * 判断时间是否在时间段内
     *
     * @param time         System.currentTimeMillis()
     * @param strDateBegin 开始时间
     * @param strDateEnd   结束时间
     * @return
     */
    public static boolean isInDate(Long time, String strDateBegin, String strDateEnd) {
        if (strDateBegin.equals("00:00") && strDateEnd.equals("00:00"))
            return true;

        Calendar calendar = Calendar.getInstance();
        // 处理开始时间
        String[] startTime = strDateBegin.split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startTime[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(startTime[1]));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTimeL = calendar.getTimeInMillis();
        // 处理结束时间
        String[] endTime = strDateEnd.split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endTime[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(endTime[1]));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long endTimeL = calendar.getTimeInMillis();
        return time >= startTimeL && time <= endTimeL;
    }

    /**
     * v8.12.0
     * 计算会员日
     */
    private static boolean isInMemberDay(CrmMemberDay crmMemberDay) {
        if (crmMemberDay == null) {
            return false;
        }
        // 无效
        if (!crmMemberDay.isValid()) {
            return false;
        }
        // 开关关闭
        if (!crmMemberDay.getIsSwitch()) {
            return false;
        }
        // 会员日值
        String value = crmMemberDay.getMemberDayValue();
        // 无选择
        if (EmptyUtils.isEmpty(value)) {
            return false;
        }
        if (EmptyUtils.isEmpty(crmMemberDay.getMemberPriceTempletId())) {
            return false;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // 判断类型
        if (crmMemberDay.getMemberDayType() == 1) {
            // 有星期选择
            String week = String.valueOf(cal.get(Calendar.DAY_OF_WEEK) - 1);
            return value.contains(week);

        } else if (crmMemberDay.getMemberDayType() == 2) {
            // 有日期选择
            String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
            return value.contains(day);
        }
        return false;
    }

    /**
     * 过滤已删除菜品
     *
     * @Title: filterDishList
     * @Description: TODO
     * @Param @param list
     * @Param @return TODO
     * @Return List<IShopcartItem> 返回类型
     */
    public List<IShopcartItem> filterDishList(List<IShopcartItem> list) {
        return filterDishList(list, true);
    }

    /**
     * @param list
     * @param isHasSplit 是否包含拆单的数据 true：包含、false：不包含
     * @return
     */
    public List<IShopcartItem> filterDishList(List<IShopcartItem> list, boolean isHasSplit) {
        if (list == null) {
            return null;
        }
        List<IShopcartItem> tempList = new ArrayList<IShopcartItem>();
        for (IShopcartItem shopcartItem : list) {
            // 过滤掉数量为0的菜品
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

    /**
     * @Title: getCustomerV5
     * @Description: 获取登录会员信息
     * @Param @param mTradeVo
     * @Param @return TODO
     * @Return TradeCustomer 返回类型
     */
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

    /**
     * 设置理由和菜品的关联
     *
     * @param shopcartItem
     * @param mReason
     * @param mOperateType
     */
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

    /**
     * 移除手动整单折扣
     */
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

        // 移除理由
        removeReason(mTradeVo, OperateType.TRADE_DISCOUNT);
        removeFreeReason(mTradeVo);
    }

    /**
     * 移除免单理由
     *
     * @param mTradeVo
     */
    public void removeFreeReason(TradeVo mTradeVo) {

        // 移除免单理由

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


    /**
     * 移除理由
     *
     * @param mTradeVo
     */
    public void removeReason(TradeVo mTradeVo, OperateType operateType) {

        // 移除免单理由

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
    public TradePrivilege buildMarketingPrivilege(TradePrivilege mTradePrivilege, TradeVo mTradeVo, BigDecimal privilageAoument) {
        if (mTradePrivilege == null) {
            mTradePrivilege = new TradePrivilege();
            mTradePrivilege.validateCreate();
            mTradePrivilege.setUuid(SystemUtils.genOnlyIdentifier());
        } else {
            mTradePrivilege.validateUpdate();
        }

        mTradePrivilege.setTradeUuid(mTradeVo.getTrade().getUuid());
        mTradePrivilege.setPrivilegeAmount(privilageAoument);
        mTradePrivilege.setPrivilegeValue(privilageAoument);
        mTradePrivilege.setPrivilegeType(PrivilegeType.MARKTING);
        mTradePrivilege.setStatusFlag(StatusFlag.VALID);
        mTradePrivilege.setPrivilegeName(BaseApplication.sInstance.getString(R.string.marketing_campaign));
        return mTradePrivilege;
    }

    /**
     * v8.7.0 营销活动保存到TradePrivilege
     */
    public void setMarktingTradePrivilege(TradeVo tradeVo) {
        // setMarktingTradePrivilegeOldVersion(tradeVo);
        // v8.7.0 重新组装TradePrivilege Mind需求
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
                // 判断是否已经存在营销活动
                for (TradePrivilege tradePrivilege : tradePrivileges) {
                    if (tradePrivilege.getPrivilegeType() == PrivilegeType.MARKTING && planId != null && tradePrivilege.getPromoId() != null && planId.compareTo(tradePrivilege.getPromoId()) == 0) {
                        // 存在已有的营销活动
                        hasPlanId = true;
                        tradePlanToPrivilege(tradePlan, tradePrivilege);
                        break;
                    }
                }
                if (hasPlanId) {
                    continue;
                }
                // 没有TradePrivilege存在，则新建
                TradePrivilege tradePrivilege = new TradePrivilege();
                tradePlanToPrivilege(tradePlan, tradePrivilege);
                tradePrivileges.add(tradePrivilege);
            }
        }
    }

    /**
     * v8.7.0 TradePlanActivity转TradePrivileg
     */
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

    /**
     * 将营销活动添加到TradePrivilege
     */
    @Deprecated
    private void setMarktingTradePrivilegeOldVersion(TradeVo mTradeVo) {
        List<TradePlanActivity> listTP = mTradeVo.getTradePlanActivityList();
        BigDecimal privilageAoument = BigDecimal.ZERO;
        if (listTP != null && listTP.size() > 0) {

            Boolean haveValid = false;
            for (TradePlanActivity mTradePlanActivity : listTP) {
                if (mTradePlanActivity.getStatusFlag() == StatusFlag.VALID && mTradePlanActivity.getRuleEffective() == ActivityRuleEffective.VALID) {
                    privilageAoument = privilageAoument.add(mTradePlanActivity.getOfferValue());
                    haveValid = true;
                }

            }
            if (haveValid) {

//				TradePrivilege mTradePrivilege = new TradePrivilege();
//				mTradePrivilege.validateCreate();
//				mTradePrivilege.setUuid(SystemUtils.genOnlyIdentifier());
//				mTradePrivilege.setTradeUuid(mTradeVo.getTrade().getUuid());
//				mTradePrivilege.setCreatorId(AuthUserCache.getAuthUser().getId());
//				mTradePrivilege.setCreatorName(AuthUserCache.getAuthUser().getName());
//				mTradePrivilege.setPrivilegeAmount(privilageAoument);
//				mTradePrivilege.setPrivilegeValue(privilageAoument);
//				mTradePrivilege.setPrivilegeType(PrivilegeType.MARKTING);
//				mTradePrivilege.setStatusFlag(StatusFlag.VALID);
//				mTradePrivilege.setPrivilegeName("商品营销");

                List<TradePrivilege> listTradePrivilege = mTradeVo.getTradePrivileges();
                if (listTradePrivilege != null) {
                    Boolean isHave = false;
                    for (TradePrivilege mTP : listTradePrivilege) {
                        if (mTP.getPrivilegeType() == PrivilegeType.MARKTING) {
                            isHave = true;
                            buildMarketingPrivilege(mTP, mTradeVo, privilageAoument);
                        }
                    }
                    if (!isHave) {
                        listTradePrivilege.add(buildMarketingPrivilege(null, mTradeVo, privilageAoument));
                    }
                } else {
                    listTradePrivilege = new ArrayList<TradePrivilege>();
                    listTradePrivilege.add(buildMarketingPrivilege(null, mTradeVo, privilageAoument));
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

    /**
     * @Title: addWeiXinCouponsVo
     * @Description: 添加微信卡卷
     * @Param mTradeVo
     * @Param mWeiXinCouponsInfo TODO
     * @Return void 返回类型
     */
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

    /**
     * @Title: removeWeiXinCouponsVo
     * @Description: 移除微信卡卷
     * @Param @param mTradeVo
     * @Param @param mWeiXinCouponsVo TODO
     * @Return void 返回类型
     */
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

    /**
     * 移除tradevo中没有规则的微信卡劵
     */
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

    /**
     * 获取列表中所以有效的菜品数据列表
     *
     * @param list
     * @return
     */
    public List<IShopcartItem> getAllValidShopcartItem(List<IShopcartItem> list) {
        List<IShopcartItem> tempList = new ArrayList<>();
        if (list != null) {
            //缓存退菜、或者改菜的有效菜品
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
                    //退菜或者改菜被移走后不显示
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

    /**
     * 是否有单点的加菜
     *
     * @param list
     * @return
     */
    public boolean isOnlySingleAddDish(List<IShopcartItem> list, List<String> uuids) {
        for (IShopcartItem iShopcartItem : list) {
            String uuid = iShopcartItem.getUuid();
            if (TextUtils.isEmpty(uuid) || !uuids.contains(uuid)) {//不是uuids列表里的菜
                continue;
            }

            if (TextUtils.isEmpty(iShopcartItem.getBatchNo())
                    && iShopcartItem.getStatusFlag() == StatusFlag.INVALID) {
                continue;
            }

            //判断是否有未打印的团餐／自助餐餐标下的菜品
            if (iShopcartItem.isGroupDish()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 移除所有的微信卡劵
     */
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

    /**
     * 单纯的移除宴请，不包括回调
     */
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

    /**
     * 宴请有有效的宴请
     *
     * @param tradeVo
     * @return
     */
    public static boolean isHasValidBanquet(TradeVo tradeVo) {
        if (tradeVo.getBanquetVo() != null && tradeVo.getBanquetVo().getTradePrivilege().isValid()) {
            return true;
        }
        return false;
    }

    /**
     * 移除tradevo中的所有优惠,不含单品的会员价
     *
     * @param isRemoveExtrage 是否移除附加费  true:移除、false：不移除
     * @isRmBanquet 是否移除宴请 true:移除宴请，false不移除
     * @isRemoveMemeberPrice 是否移除会员价
     */
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
        //移除附加费
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
        //移除所有单品优惠,不包括会员折扣
        List<TradeItemVo> tradeItemVoList = tradeVo.getTradeItemList();
        if (tradeItemVoList != null) {
            for (TradeItemVo tradeItemVo : tradeItemVoList) {
                if (tradeItemVo.getTradeItemPrivilege() != null) {
                    PrivilegeType privilegeType = tradeItemVo.getTradeItemPrivilege().getPrivilegeType();
                    //移菜或者复制菜品时会员价会带入，所以不移除
                    if (privilegeType != PrivilegeType.MEMBER_REBATE && privilegeType != PrivilegeType.MEMBER_PRICE && privilegeType != PrivilegeType.AUTO_DISCOUNT || isRemoveMemeberPrice) {
                        if (tradeItemVo.getTradeItemPrivilege().getId() == null) {
                            tradeItemVo.setTradeItemPrivilege(null);
                        } else {
                            tradeItemVo.getTradeItemPrivilege().setStatusFlag(StatusFlag.INVALID);
                            tradeItemVo.getTradeItemPrivilege().validateUpdate();
                        }
                    }
                }
                //移除礼品劵
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

    /**
     * @Title: CheckGiftCouponIsActived
     * @Description: 校验礼品优惠劵是否有效
     * @Param TODO
     * @Return void 返回类型
     */
    protected void CheckGiftCouponIsActived(ShoppingCartVo shoppingCartVo) {
        CheckGiftCouponIsActived(mergeShopcartItem(shoppingCartVo), shoppingCartVo.getmTradeVo());
    }

    public static void CheckGiftCouponIsActived(List<IShopcartItem> shopcartItemList, TradeVo tradeVo) {
        if (Utils.isEmpty(shopcartItemList)) {
            return;
        }
        for (IShopcartItem item : shopcartItemList) {
            if (item.getCouponPrivilegeVo() != null && item.getCouponPrivilegeVo().getTradePrivilege() != null) {
                //获取原始赠送数量
                BigDecimal count = BigDecimal.ONE;

                //重新设置优惠值
                BigDecimal privilageAmount = item.getPrice().multiply(count);
                item.getCouponPrivilegeVo().getTradePrivilege().setPrivilegeAmount(privilageAmount.negate());
                item.getCouponPrivilegeVo().getTradePrivilege().setPrivilegeValue(privilageAmount);

                BigDecimal depositValue = BigDecimal.ZERO;
                //押金
                if (tradeVo.getTradeDeposit() != null) {
                    depositValue = tradeVo.getTradeDeposit().getDepositPay();
                }
                //判断礼品券是否满足规则的金额需要减掉押金金额
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
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, tradeVo);
    }

    /**
     * @Title: removeGiftCouponePrivilege
     * @Description: 移除礼品优惠劵
     * @Param TODO
     * @Return void 返回类型
     */
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
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(iShopcartItems, shoppingCartVo.getmTradeVo());

        for (ModifyShoppingCartListener listener : arrayListener.values()) {
            listener.updateDish(iShopcartItems, shoppingCartVo.getmTradeVo());
        }
    }

    /**
     * @Title: removeGiftCouponePrivilege
     * @Description: 批量移除礼品优惠劵
     * @Param TODO
     * @Return void 返回类型
     */
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
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(iShopcartItems,
                shoppingCartVo.getmTradeVo());
        for (ModifyShoppingCartListener listener : arrayListener.values()) {
            listener.updateDish(iShopcartItems, shoppingCartVo.getmTradeVo());
        }
    }

    /**
     * 移除优惠劵包括礼品劵
     */
    public void removeDefineOrGiftCoupon(ShoppingCartVo shoppingCartVo, CouponPrivilegeVo couponPrivilegeVo) {
        //单品礼品劵
        if (couponPrivilegeVo.getCoupon().getCouponType() == CouponType.GIFT
                && couponPrivilegeVo.getShopcartItem() != null && couponPrivilegeVo.getTradePrivilege() != null) {
            removeGiftCouponePrivilege(couponPrivilegeVo.getTradePrivilege().getPromoId(), shoppingCartVo, true);
        } else {
            removeCouponPrivilege(shoppingCartVo, couponPrivilegeVo, true);
        }
    }


    /**
     * 移除整单优惠劵
     */
    public void removeCouponPrivilege(ShoppingCartVo shoppingCartVo, CouponPrivilegeVo couponPrivilegeVo, boolean isNeedListener) {
        if (couponPrivilegeVo.getTradePrivilege() == null || couponPrivilegeVo.getTradePrivilege().getPromoId() == null) {
            return;
        }
        List<Long> couponIdList = new ArrayList<Long>();
        couponIdList.add(couponPrivilegeVo.getTradePrivilege().getPromoId());
        removeCouponPrivilege(shoppingCartVo, couponIdList, isNeedListener);
    }

    /**
     * 移除整单优惠劵
     * couponIdList优惠劵id list
     */
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

        // 计算订单总价格
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(shoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList,

                shoppingCartVo.getmTradeVo());
        if (isNeedListener) {
            for (ModifyShoppingCartListener listener : arrayListener.values()) {
                listener.removeCouponPrivilege(shopcartItemList, shoppingCartVo.getmTradeVo());
            }
        }
    }

    /**
     * 是否允许加入整单的优惠劵
     *
     * @param shoppingCartVo
     * @param couponPrivilegeVo
     * @return true  允许，false 不允许
     */
    public boolean isAllowAddCoupon(ShoppingCartVo shoppingCartVo, CouponPrivilegeVo couponPrivilegeVo) {
        List<CouponPrivilegeVo> couponPrivilegeVoList = shoppingCartVo.getmTradeVo().getCouponPrivilegeVoList();
        if (Utils.isEmpty(couponPrivilegeVoList)) {
            return true;
        }
        CouponType couponType = couponPrivilegeVo.getCoupon().getCouponType();
        for (CouponPrivilegeVo mCouponPrivilegeVo : couponPrivilegeVoList) {
            //只有代金券允许加入多张
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

    /**
     * 是否允许加入整单的优惠劵
     *
     * @param shoppingCartVo
     * @param id             会员券Id
     * @return true  允许，false 不允许
     */
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

    /**
     * @Title: getNewWxCode
     * @Description: 将微信卡券code进行转换
     * @Param code    TODO
     * @Return String 返回类型
     */
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

    /**
     * 通过优惠类型移除tradeVo中的优惠
     */
    public void removeTradePrivilege(PrivilegeType privilegeType, TradeVo tradeVo) {
        List<TradePrivilege> listPrivilege = tradeVo.getTradePrivileges();
        if (listPrivilege != null) {
            ExtraCharge outTimeExtra = ServerSettingCache.getInstance().getmOutTimeRule();
            ExtraCharge minCharge = tradeVo.getMinconExtraCharge();
            for (int i = listPrivilege.size() - 1; i >= 0; i--) {
                TradePrivilege mTradePrivilege = listPrivilege.get(i);

                if (mTradePrivilege != null && (mTradePrivilege.getPrivilegeType() == privilegeType)) {
                    //超时费不做移除操作
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

    /**
     * 移除所有未激活且有效的数据
     */
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
               /* for (int key : arrayListener.keySet()) {
                    arrayListener.get(key).removeCouponPrivilege(mergeShopcartItem(shoppingCartVo), shoppingCartVo.getmTradeVo());
                }*/
                for (ModifyShoppingCartListener listener : arrayListener.values()) {
                    listener.removeCouponPrivilege(iShopcartItems, shoppingCartVo.getmTradeVo());
                }
            }
        }
        return deleteCouponIds;
    }

    /**
     * @Title: removeCouponPrivilege
     * @Description: 移除所有整单优惠劵价
     * @Param TODO
     * @Return void 返回类型
     */

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

        // 计算订单总价格
        List<IShopcartItem> shopcartItemList = mergeShopcartItem(shoppingCartVo);
        MathShoppingCartTool.mathTotalPrice(shopcartItemList, shoppingCartVo.getmTradeVo());
        if (isNeedListener) {
           /* for (int key : arrayListener.keySet()) {
                arrayListener.get(key).removeCouponPrivilege(shopcartItemList,
                        shoppingCartVo.getmTradeVo());
            }*/
            for (ModifyShoppingCartListener listener : arrayListener.values()) {
                listener.removeCouponPrivilege(shopcartItemList, shoppingCartVo.getmTradeVo());
            }
        }
    }

    /**
     * 通过promoIds获得tradeVo的tradePrivilege List
     */
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

    /**
     * @param openId
     * @Title: setOpenIdenty
     * @Description: 设置openId
     */

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

    /**
     * 添加促销活动到购物车
     *
     * @param shoppingCartVo        购物车数据对象
     * @param salesPromotionRuleVo  指定促销活动
     * @param selectedShopcartItems 选择的购物车条目
     * @param customerNew           当前登录的顾客
     */
    protected void addSalesPromotion(@NonNull ShoppingCartVo shoppingCartVo, @NonNull SalesPromotionRuleVo salesPromotionRuleVo, @NonNull List<IShopcartItem> selectedShopcartItems, @Nullable CustomerResp customerNew) {
        //判断是否可以带入促销活动
        if (MathSalesPromotionTool.canAddSalesPromotion(salesPromotionRuleVo, selectedShopcartItems, customerNew)) {
            //计算促销活动
            MathSalesPromotionTool.mathManualAddSalesPromotion(salesPromotionRuleVo, selectedShopcartItems, shoppingCartVo.getmTradeVo(), customerNew);
            //移除宴请
            removeBanquetOnly(shoppingCartVo.getmTradeVo());
            // 如果选择的菜品有礼品券优惠移除
            removeGiftCouponPrivilege(selectedShopcartItems, shoppingCartVo);
            // 计算订单总价格
            MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(shoppingCartVo),
                    shoppingCartVo.getmTradeVo());
            //发送促销活动加入成功消息
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).addSalesPromotion(true, shoppingCartVo.getmTradeVo());
            }
        } else {
            //发送促销活动加入失败消息
            for (int key : arrayListener.keySet()) {
                arrayListener.get(key).addSalesPromotion(false, null);
            }
        }
    }

    /**
     * 删除购物车中指定商品的礼品券优惠
     *
     * @param selectedItemList 已选择的商品
     * @param shoppingCartVo   shoppingCartVo
     */
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

    /**
     * 从购物车删除促销活动
     *
     * @param shoppingCartVo 购物车数据对象
     * @param planId         促销活动方案Id
     * @param mCustomer      当前登录的顾客
     * @param isDinner       是否正餐
     */
    protected void removeSalesPromotion(@NonNull ShoppingCartVo shoppingCartVo, Long planId, CustomerResp mCustomer, boolean isDinner) {
        TradeVo mTradeVo = shoppingCartVo.getmTradeVo();
        //移除相关促销活动
        MathSalesPromotionTool.removeTradePlanActivity(mTradeVo.getTradePlanActivityList(), mTradeVo.getTradeItemPlanActivityList(), planId);
        //重新计算会员价
        batchMemberPrivilege(shoppingCartVo);
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(shoppingCartVo), mTradeVo);

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeSalesPromotion(shoppingCartVo.getmTradeVo());
        }
    }

    /**
     * 从购物车删除促销活动
     *
     * @param shoppingCartVo 购物车数据对象
     * @param tradePlanUuid  TradePlanActivity的UUID
     * @param mCustomer      当前登录的顾客
     * @param isDinner       是否正餐
     */
    public void removeSalesPromotion(@NonNull ShoppingCartVo shoppingCartVo, String tradePlanUuid, CustomerResp mCustomer, boolean isDinner) {
        TradeVo mTradeVo = shoppingCartVo.getmTradeVo();
        MathSalesPromotionTool.removeTradePlanActivity(mTradeVo.getTradePlanActivityList(), mTradeVo.getTradeItemPlanActivityList(), tradePlanUuid);
        //重新计算会员价
        batchMemberPrivilege(shoppingCartVo);
        // 计算订单总价格
        MathShoppingCartTool.mathTotalPrice(mergeShopcartItem(shoppingCartVo), mTradeVo);

        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).removeSalesPromotion(mTradeVo);
        }
    }


    /**
     * 移除TradeVo所有促销
     *
     * @param tradeVo
     */
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

    /**
     * 自动添加促销活动
     *
     * @param shoppingCartVo 购物车数据对象
     */
    public void autoAddSalesPromotion(@NonNull ShoppingCartVo shoppingCartVo) {
        removeSalesPromotion(shoppingCartVo.getmTradeVo());
//
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
            //计算促销活动
            MathSalesPromotionTool.mathManualAddSalesPromotion(salesPromotionRuleVo, map.get(salesPromotionRuleVo), shoppingCartVo.getmTradeVo(), CustomerManager.getInstance().getLoginCustomer());
        }

        //移除宴请
        removeBanquetOnly(shoppingCartVo.getmTradeVo());
        //发送促销活动加入成功消息
        for (int key : arrayListener.keySet()) {
            arrayListener.get(key).addSalesPromotion(true, shoppingCartVo.getmTradeVo());
        }
    }
}
