package com.zhongmei.bty.basemodule.beauty;

import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.basemodule.discount.bean.CardServicePrivilegeVo;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.bty.commonmodule.database.enums.ServerPrivilegeType;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.yunfu.util.ToastUtil;

import java.util.HashMap;
import java.util.List;


public class BeautyCardManager {

    public interface IBeautyCardListener {
        void onAddCartCallBack(int position);

        void onRemoveCartCallBack();
    }

    private IBeautyCardListener mListener;

    public void setIBeautyCardListener(IBeautyCardListener listener) {
        this.mListener = listener;
    }

    private static BeautyCardManager instance;


    public HashMap<ServerPrivilegeType, HashMap<Long, Integer>> mCacheDishShopVo = new HashMap<>();


    public static BeautyCardManager getInstance() {
        if (instance == null) {
            instance = new BeautyCardManager();
        }
        return instance;
    }


    public void init(List<IShopcartItem> items) {
        if (items != null && items.size() > 0) {
            HashMap<Long, Integer> numMap;
            for (IShopcartItem item : items) {
                if (item.getCardServicePrivilgeVo() != null) {
                    CardServicePrivilegeVo vo = item.getCardServicePrivilgeVo();
                    if (mCacheDishShopVo.containsKey(vo.getType())) {
                        numMap = mCacheDishShopVo.get(vo.getType());
                        if (numMap.containsKey(vo.getServerRecordId())) {
                            numMap.put(vo.getServerRecordId(), numMap.get(vo.getServerRecordId()) + item.getTotalQty().intValue());
                        } else {
                            numMap.put(vo.getServerRecordId(), item.getTotalQty().intValue());
                        }
                        mCacheDishShopVo.put(vo.getType(), numMap);
                    } else {
                        numMap = new HashMap<>();
                        numMap.put(vo.getServerRecordId(), item.getTotalQty().intValue());
                        mCacheDishShopVo.put(vo.getType(), numMap);
                    }
                }
            }
        }
    }


    public void addDishshopToShopCart(ServerPrivilegeType privilegeType, BeautyCardServiceInfo vo,DishShop dishShop, int position) {

        if (privilegeType != null && dishShop != null) {
            HashMap<Long, Integer> numMap;
            if (mCacheDishShopVo.containsKey(privilegeType)) {
                numMap = mCacheDishShopVo.get(privilegeType);
                int count;
                if (numMap.containsKey(vo.cardInstanceId)) {
                    count = numMap.get(vo.cardInstanceId);
                    Log.i("BeautyCardManager", " brandDishId = [ " + dishShop.getBrandDishId() + " ] , oldCount = " + count);
                    numMap.put(vo.cardInstanceId, count + dishShop.getDishIncreaseUnit().intValue());                     Log.i("BeautyCardManager", " brandDishId = [ " + dishShop.getBrandDishId() + " ] , newOldCount = " + numMap.get(dishShop.getBrandDishId()));
                } else {
                    Log.i("BeautyCardManager", " brandDishId = [ " + dishShop.getBrandDishId() + " ] ,  one = 1  !containsKey");
                    numMap.put(vo.cardInstanceId, dishShop.getDishIncreaseUnit().intValue());
                }
                Log.i("BeautyCardManager", "mCacheDishShopVo.add");
                mCacheDishShopVo.put(privilegeType, numMap);
            } else {
                numMap = new HashMap<>();
                Log.i("BeautyCardManager", " brandDishId = [ " + dishShop.getBrandDishId() + " ] ,  one = 1  mCacheDishShopVo null");
                numMap.put(vo.cardInstanceId, dishShop.getDishIncreaseUnit().intValue());
                mCacheDishShopVo.put(privilegeType, numMap);
            }
            if (mListener != null) {
                mListener.onAddCartCallBack(position);
            }
            DinnerShoppingCart.getInstance().addCardService(dishShop, vo.cardInstanceId);
        }
    }


    public void removeDishshopFromShopCart(ServerPrivilegeType type, IShopcartItemBase mShopcartItem) {
        CardServicePrivilegeVo cardServicePrivilegeVo = mShopcartItem.getCardServicePrivilgeVo();
        removeDishshopFromShopCart(type, cardServicePrivilegeVo.getServerRecordId(), mShopcartItem.getTotalQty().intValue());
    }


    public void removeDishshopFromShopCart(ServerPrivilegeType type, Long serverRecordId, Integer num) {
        if (serverRecordId != null) {
            if (mCacheDishShopVo.containsKey(type)) {
                HashMap<Long, Integer> numMap = mCacheDishShopVo.get(type);
                if (numMap != null && numMap.size() > 0 && numMap.containsKey(serverRecordId)) {
                    int count = numMap.get(serverRecordId) - num;
                    if (count < 0) {
                        count = 0;
                    }
                    numMap.put(serverRecordId, count);
                    mCacheDishShopVo.put(type, numMap);
                    if (mListener != null) {
                        mListener.onRemoveCartCallBack();
                    }
                }
            }
        }
    }


    public int getCacheCountById(ServerPrivilegeType type, Long serverRecordId) {
        int count = 0;
        if (mCacheDishShopVo != null && mCacheDishShopVo.size() > 0) {
            if (mCacheDishShopVo.containsKey(type)) {
                HashMap<Long, Integer> numMap = mCacheDishShopVo.get(type);
                if (numMap != null && numMap.size() > 0 && numMap.containsKey(serverRecordId)) {
                    count = numMap.get(serverRecordId);
                    return count;
                }
            }
        }
        return count;
    }


    public void exitCardManager() {
        mListener = null;
        mCacheDishShopVo.clear();
    }

}
