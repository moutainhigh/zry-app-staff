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


public class OperateShoppingCart {


    public static void addToShoppingCart(TradeVo mTradeVo, List<ShopcartItem> listOrderDishshopVo,
                                         ShopcartItem mShopcartItem) {
                TradePrivilege mTradePrivilege = BuildPrivilegeTool.buildPrivilege(mShopcartItem, mTradeVo.getTrade().getUuid());
        mShopcartItem.setPrivilege(mTradePrivilege);
                mShopcartItem.setIndex(listOrderDishshopVo.size());
        listOrderDishshopVo.add(mShopcartItem);
    }


    public static void addToReadOnlyShoppingCart(TradeVo mTradeVo, List<IShopcartItem> listOrderDishshopVo,
                                                 IShopcartItem mShopcartItem) {
                if (mTradeVo.getTrade() != null) {
            TradePrivilege mTradePrivilege = BuildPrivilegeTool.buildPrivilege(mShopcartItem, mTradeVo.getTrade().getUuid());
            mShopcartItem.setPrivilege(mTradePrivilege);
        }

        listOrderDishshopVo.add(mShopcartItem);
    }


    public static void updateDish(TradeVo mTradeVo, List<ShopcartItem> listOrderDishshopVo, ShopcartItemBase mShopcartItemBase) {
                String parentUUID = mShopcartItemBase.getParentUuid();
        String shopcatUUID = mShopcartItemBase.getUuid();

                if (TextUtils.isEmpty(parentUUID)) {
            for (int i = 0; i < listOrderDishshopVo.size(); i++) {
                IShopcartItem item = listOrderDishshopVo.get(i);
                if (shopcatUUID.equals(item.getUuid())) {
                    if (mShopcartItemBase instanceof ShopcartItem) {
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

                String parentUUID = mShopcartItemBase.getParentUuid();
        String shopcatUUID = mShopcartItemBase.getUuid();

                if (TextUtils.isEmpty(parentUUID)) {
            for (int i = 0; i < listOrderDishshopVo.size(); i++) {
                IShopcartItem item = listOrderDishshopVo.get(i);
                if (shopcatUUID.equals(item.getUuid())) {
                    if (mShopcartItemBase instanceof ShopcartItem) {
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



    public static IShopcartItem removeReadOnlyDish(TradeVo mTradeVo, List<IShopcartItem> listOrderDishshopVo,
                                                   ReadonlyShopcartItem mShopcartItemBase) {
        IShopcartItem mDish = null;
        String parentUUID = mShopcartItemBase.getParentUuid();
        String shopcatUUID = mShopcartItemBase.getUuid();

                if (!TextUtils.isEmpty(parentUUID)) {
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


    public static ShopcartItem removeDish(TradeVo mTradeVo, List<ShopcartItem> listOrderDishshopVo,
                                          IShopcartItemBase mShopcartItemBase) {
        ShopcartItem mDish = null;
        String parentUUID = mShopcartItemBase.getParentUuid();
        String shopcatUUID = mShopcartItemBase.getUuid();

                if (!TextUtils.isEmpty(parentUUID)) {
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


    public static ShopcartItem removTradeItem(TradeVo mTradeVo, List<ShopcartItem> listOrderDishshopVo,
                                              TradeItemVo mTradeItemVo) {
        ShopcartItem mDish = null;
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
                        for (int j = 0; j < listSetmeal.size(); j++) {
                SetmealShopcartItem setmeal = listSetmeal.get(j);
                if (setmeal.getUuid().equals(shopcatUUID)) {
                    listSetmeal.remove(j);
                    break;
                }
            }

        }

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


    public static void removeDishPrivilege(TradeVo mTradeVo, List<ShopcartItem> listOrderDishshopVo,
                                           ShopcartItem mShopcartItem) {
                for (ShopcartItem item : listOrderDishshopVo) {
            if (item.getUuid().equals(mShopcartItem.getUuid())) {
                item.setPrivilege(null);
                break;
            }
        }
    }

    public static void removeDishRemark(TradeVo mTradeVo, List<ShopcartItem> listOrderDishshopVo,
                                        ShopcartItem mShopcartItem) {
                for (ShopcartItem item : listOrderDishshopVo) {
            if (item.getUuid().equals(mShopcartItem.getUuid())) {
                item.setMemo(null);
                break;
            }
        }
    }

}
