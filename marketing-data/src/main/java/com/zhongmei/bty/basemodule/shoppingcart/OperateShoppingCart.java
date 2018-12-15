package com.zhongmei.bty.basemodule.shoppingcart;

import android.text.TextUtils;

import com.zhongmei.bty.basemodule.discount.utils.BuildPrivilegeTool;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date：2015年7月16日 下午3:53:11
 * @Description: 操作购物车中的数据
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class OperateShoppingCart {

    /**
     * @Title: addToShoppingCart
     * @Description: 添加菜品到购物车
     * @Param @param mTradeVo
     * @Param @param listOrderDishshopVo
     * @Param @param mShopcartItem TODO
     * @Return void 返回类型
     */
    public static void addToShoppingCart(TradeVo mTradeVo, List<ShopcartItem> listOrderDishshopVo,
                                         ShopcartItem mShopcartItem) {
        // 折扣信息
        TradePrivilege mTradePrivilege = BuildPrivilegeTool.buildPrivilege(mShopcartItem, mTradeVo.getTrade().getUuid());
        mShopcartItem.setPrivilege(mTradePrivilege);
//		if(mTradePrivilege != null && mShopcartItem.getmPrivilegeModle() != null){
//			mShopcartItem.setmPrivilegeModle(PrivilegeModle.DEFERT);
//		}
        //为购物车中的商品添加序列号
        mShopcartItem.setIndex(listOrderDishshopVo.size());
        listOrderDishshopVo.add(mShopcartItem);
    }

    /**
     * @param mTradeVo
     * @param listOrderDishshopVo
     * @param mShopcartItem       TODO
     * @Title: addToReadOnlyShoppingCart
     * @Description: 添加只读菜品到购物车
     * @Return void 返回类型
     */
    public static void addToReadOnlyShoppingCart(TradeVo mTradeVo, List<IShopcartItem> listOrderDishshopVo,
                                                 IShopcartItem mShopcartItem) {
        // 折扣信息
        if (mTradeVo.getTrade() != null) {
            TradePrivilege mTradePrivilege = BuildPrivilegeTool.buildPrivilege(mShopcartItem, mTradeVo.getTrade().getUuid());
            mShopcartItem.setPrivilege(mTradePrivilege);
        }

        listOrderDishshopVo.add(mShopcartItem);
    }

    /**
     * @Title: updateDish
     * @Description: 更新购物车中数据
     * @Param @param mTradeVo
     * @Param @param listOrderDishshopVo
     * @Param @param mShopcartItemBase TODO
     * @Return void 返回类型
     */
    public static void updateDish(TradeVo mTradeVo, List<ShopcartItem> listOrderDishshopVo, ShopcartItemBase mShopcartItemBase) {
        // parentUUID不为空，表示删除的是子菜，反之表示删除的是整个套餐或单菜
        String parentUUID = mShopcartItemBase.getParentUuid();
        String shopcatUUID = mShopcartItemBase.getUuid();

        // 更新listOrderDishshopVo中的菜品对象
        if (TextUtils.isEmpty(parentUUID)) {
            for (int i = 0; i < listOrderDishshopVo.size(); i++) {
                IShopcartItem item = listOrderDishshopVo.get(i);
                if (shopcatUUID.equals(item.getUuid())) {
                    if (mShopcartItemBase instanceof ShopcartItem) {
                        // 更新优惠信息
                        TradePrivilege mTradePrivilege =
                                BuildPrivilegeTool.buildPrivilege(mShopcartItemBase, mTradeVo.getTrade().getUuid());
                        mShopcartItemBase.setPrivilege(mTradePrivilege);
                        listOrderDishshopVo.set(i, (ShopcartItem) mShopcartItemBase);
                    }
                    break;
                }
            }
        } else {
            List<SetmealShopcartItem> listSetmeal = null;
            for (int i = 0; i < listOrderDishshopVo.size(); i++) {
                ShopcartItem dish = listOrderDishshopVo.get(i);
                if (dish.getUuid().equals(parentUUID)) {
                    listSetmeal = dish.getSetmealItems();
                    break;
                }
            }
            if (listSetmeal != null) {
                for (int j = 0; j < listSetmeal.size(); j++) {
                    SetmealShopcartItem setmeal = listSetmeal.get(j);
                    if (setmeal.getUuid().equals(shopcatUUID)) {
                        if (mShopcartItemBase instanceof SetmealShopcartItem) {
                            listSetmeal.set(j, (SetmealShopcartItem) mShopcartItemBase);
                        }
                        break;
                    }
                }
            }
        }

    }

    public static void updateReadOnlyShopcarItem(TradeVo mTradeVo, List<IShopcartItem> listOrderDishshopVo, IShopcartItemBase mShopcartItemBase) {

        // parentUUID不为空，表示删除的是子菜，反之表示删除的是整个套餐或单菜
        String parentUUID = mShopcartItemBase.getParentUuid();
        String shopcatUUID = mShopcartItemBase.getUuid();

        // 更新listOrderDishshopVo中的菜品对象
        if (TextUtils.isEmpty(parentUUID)) {
            for (int i = 0; i < listOrderDishshopVo.size(); i++) {
                IShopcartItem item = listOrderDishshopVo.get(i);
                if (shopcatUUID.equals(item.getUuid())) {
                    if (mShopcartItemBase instanceof ShopcartItem) {
                        // 更新优惠信息
                        TradePrivilege mTradePrivilege =
                                BuildPrivilegeTool.buildPrivilege(mShopcartItemBase, mTradeVo.getTrade().getUuid());
                        mShopcartItemBase.setPrivilege(mTradePrivilege);
                        listOrderDishshopVo.set(i, (ShopcartItem) mShopcartItemBase);
                    }
                    break;
                }
            }
        } else {
            List<ISetmealShopcartItem> listSetmeal = null;
            for (int i = 0; i < listOrderDishshopVo.size(); i++) {
                IShopcartItem dish = listOrderDishshopVo.get(i);
                if (dish.getUuid().equals(parentUUID)) {
                    listSetmeal = (List<ISetmealShopcartItem>) dish.getSetmealItems();
                    break;
                }
            }
            if (listSetmeal != null) {
                for (int j = 0; j < listSetmeal.size(); j++) {
                    ISetmealShopcartItem setmeal = listSetmeal.get(j);
                    if (setmeal.getUuid().equals(shopcatUUID)) {
                        if (mShopcartItemBase instanceof SetmealShopcartItem) {
                            listSetmeal.set(j, (SetmealShopcartItem) mShopcartItemBase);
                        }
                        break;
                    }
                }
            }
        }


    }

//	public static void updateReadOnlyDish(TradeVo mTradeVo, List<IShopcartItem> listOrderDishshopVo, IShopcartItemBase mShopcartItemBase){
//
//		// parentUUID不为空，表示删除的是子菜，反之表示删除的是整个套餐或单菜
//		String parentUUID = mShopcartItemBase.getParentUuid();
//		String shopcatUUID = mShopcartItemBase.getUuid();
//		
//		// 更新listOrderDishshopVo中的菜品对象
//		if (TextUtils.isEmpty(parentUUID)) {
//			for (int i = 0; i < listOrderDishshopVo.size(); i++) {
//				IShopcartItem item = listOrderDishshopVo.get(i);
//				if (shopcatUUID.equals(item.getUuid())) {
//					if (mShopcartItemBase instanceof ShopcartItem) {
//						// 更新优惠信息
//						TradePrivilege mTradePrivilege =
//							BuildPrivilegeTool.buildPrivilege(mShopcartItemBase, mTradeVo.getTrade().getUuid());
//						mShopcartItemBase.setPrivilege(mTradePrivilege);
//						listOrderDishshopVo.set(i, (ShopcartItem)mShopcartItemBase);
//					}
//					break;
//				}
//			}
//		} else {
//			List<ISetmealShopcartItem> listSetmeal = null;
//			for (int i = 0; i < listOrderDishshopVo.size(); i++) {
//				IShopcartItem dish = listOrderDishshopVo.get(i);
//				if (dish.getUuid().equals(parentUUID)) {
//					listSetmeal = (List<ISetmealShopcartItem>)dish.getSetmealItems();
//					break;
//				}
//			}
//			
//			for (int j = 0; j < listSetmeal.size(); j++) {
//				ISetmealShopcartItem setmeal = listSetmeal.get(j);
//				if (setmeal.getUuid().equals(shopcatUUID)) {
//					if (mShopcartItemBase instanceof SetmealShopcartItem) {
//						listSetmeal.set(j, (SetmealShopcartItem)mShopcartItemBase);
//					}
//					break;
//				}
//			}
//		}
//		
//	
//	}

    /**
     * @Title: removeReadOnlyDish
     * @Description: 移除退回商品产生的只读临时商品
     * @Param @param mTradeVo
     * @Param @param listOrderDishshopVo
     * @Param @param mShopcartItemBase
     * @Param @return TODO
     * @Return IShopcartItem 返回类型
     */
    public static IShopcartItem removeReadOnlyDish(TradeVo mTradeVo, List<IShopcartItem> listOrderDishshopVo,
                                                   ReadonlyShopcartItem mShopcartItemBase) {
        IShopcartItem mDish = null;
        String parentUUID = mShopcartItemBase.getParentUuid();
        String shopcatUUID = mShopcartItemBase.getUuid();

        // 删除子菜
        if (!TextUtils.isEmpty(parentUUID)) {
            // 删除套餐下的子菜
            List<? extends ISetmealShopcartItem> listSetmeal = null;
            IShopcartItem dish = null;
            for (int i = 0; i < listOrderDishshopVo.size(); i++) {
                dish = listOrderDishshopVo.get(i);
                if (dish.getUuid().equals(parentUUID)) {
                    listSetmeal = dish.getSetmealItems();
                    break;
                }
            }
            if (listSetmeal != null) {
                for (int j = listSetmeal.size() - 1; j >= 0; j--) {
                    ISetmealShopcartItem setmeal = listSetmeal.get(j);
                    if (setmeal.getUuid().equals(shopcatUUID)) {
                        listSetmeal.remove(j);
                        break;
                    }
                }
            }

            if (dish != null) {
                dish.setPrivilege(BuildPrivilegeTool.buildPrivilege(dish, mTradeVo.getTrade().getUuid()));
            }


        } else {
            for (int i = 0; i < listOrderDishshopVo.size(); i++) {
                IShopcartItem dish = listOrderDishshopVo.get(i);
                if (dish.getUuid().equals(shopcatUUID)) {
                    listOrderDishshopVo.remove(i);
                    mDish = dish;
                    break;
                }
            }
        }

        return mDish;
    }

    /**
     * @Title: removeDish
     * @Description: 移除购物车中的菜品
     * @Param @param mTradeVo
     * @Param @param listOrderDishshopVo
     * @Param @param mShopcartItemBase
     * @Param @return TODO
     * @Return ShopcartItem 返回类型
     */
    public static ShopcartItem removeDish(TradeVo mTradeVo, List<ShopcartItem> listOrderDishshopVo,
                                          IShopcartItemBase mShopcartItemBase) {
        ShopcartItem mDish = null;
        String parentUUID = mShopcartItemBase.getParentUuid();
        String shopcatUUID = mShopcartItemBase.getUuid();

        // 删除子菜
        if (!TextUtils.isEmpty(parentUUID)) {
            // 删除套餐下的子菜
            List<SetmealShopcartItem> listSetmeal = null;
            ShopcartItem dish = null;
            for (int i = 0; i < listOrderDishshopVo.size(); i++) {
                dish = listOrderDishshopVo.get(i);
                if (dish.getUuid().equals(parentUUID)) {
                    listSetmeal = dish.getSetmealItems();
                    break;
                }
            }
            if (listSetmeal != null) {
                for (int j = listSetmeal.size() - 1; j >= 0; j--) {
                    SetmealShopcartItem setmeal = listSetmeal.get(j);
                    if (setmeal.getUuid().equals(shopcatUUID)) {
                        listSetmeal.remove(j);
                        break;
                    }
                }
            }

            if (dish != null) {
                dish.setPrivilege(BuildPrivilegeTool.buildPrivilege(dish, mTradeVo.getTrade().getUuid()));
            }


        } else {
            for (int i = 0; i < listOrderDishshopVo.size(); i++) {
                ShopcartItem dish = listOrderDishshopVo.get(i);
                if (dish.getUuid().equals(shopcatUUID)) {
                    listOrderDishshopVo.remove(i);
                    mDish = dish;
                    break;
                }
            }
        }

        return mDish;
    }

    /**
     * @Title: removTradeItem
     * @Description: 移除购物车中的TradeItem
     * @Param @param mTradeVo
     * @Param @param listOrderDishshopVo
     * @Param @param mTradeItemVo TODO
     * @Return void 返回类型
     */
    public static ShopcartItem removTradeItem(TradeVo mTradeVo, List<ShopcartItem> listOrderDishshopVo,
                                              TradeItemVo mTradeItemVo) {
        ShopcartItem mDish = null;
        // parentUUID不为空，表示删除的是子菜，反之表示删除的是整个套餐或单菜
        String parentUUID = mTradeItemVo.getTradeItem().getParentUuid();
        String shopcatUUID = mTradeItemVo.getTradeItem().getUuid();
        if (TextUtils.isEmpty(parentUUID)) {
            for (int i = 0; i < listOrderDishshopVo.size(); i++) {
                ShopcartItem dish = listOrderDishshopVo.get(i);
                if (dish.getUuid().equals(shopcatUUID)) {
                    listOrderDishshopVo.remove(i);
                    mDish = dish;
                    break;
                }
            }
        } else {

            List<SetmealShopcartItem> listSetmeal = new ArrayList<SetmealShopcartItem>();
            for (int i = 0; i < listOrderDishshopVo.size(); i++) {
                ShopcartItem dish = listOrderDishshopVo.get(i);
                if (dish.getUuid().equals(parentUUID)) {
                    listSetmeal = dish.getSetmealItems();
                    break;
                }
            }
            // 删除套餐下的子菜
            for (int j = 0; j < listSetmeal.size(); j++) {
                SetmealShopcartItem setmeal = listSetmeal.get(j);
                if (setmeal.getUuid().equals(shopcatUUID)) {
                    listSetmeal.remove(j);
                    break;
                }
            }

        }

        // 从TradeItem中删除
        List<TradeItemVo> listTradeItem = mTradeVo.getTradeItemList();

        if (listTradeItem != null) {
            for (int i = listTradeItem.size() - 1; i >= 0; i--) {
                if (listTradeItem.get(i).getTradeItem().getUuid().equals(shopcatUUID)) {
                    listTradeItem.remove(i);
                    break;
                }
            }
        }

        return mDish;
    }

    /**
     * @Title: removeDishPrivilege
     * @Description: 删除菜品折扣
     * @Param @param mTradeVo
     * @Param @param listOrderDishshopVo
     * @Param @param mShopcartItem TODO
     * @Return void 返回类型
     */
    public static void removeDishPrivilege(TradeVo mTradeVo, List<ShopcartItem> listOrderDishshopVo,
                                           ShopcartItem mShopcartItem) {
        // 移除listOrderDishshopVo中对于菜品的折扣信息
        for (ShopcartItem item : listOrderDishshopVo) {
            if (item.getUuid().equals(mShopcartItem.getUuid())) {
                item.setPrivilege(null);
                break;
            }
        }
    }

    public static void removeDishRemark(TradeVo mTradeVo, List<ShopcartItem> listOrderDishshopVo,
                                        ShopcartItem mShopcartItem) {
        // 移除listOrderDishshopVo中对于菜品的折扣信息
        for (ShopcartItem item : listOrderDishshopVo) {
            if (item.getUuid().equals(mShopcartItem.getUuid())) {
                item.setMemo(null);
                break;
            }
        }
    }

}
