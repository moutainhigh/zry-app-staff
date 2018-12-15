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

/**
 * 美业卡manager
 *
 * @date 2018/6/15
 */
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

    /**
     * 通过优惠类型来处理次数
     */
    public HashMap<ServerPrivilegeType, HashMap<Long, Integer>> mCacheDishShopVo = new HashMap<>();


    public static BeautyCardManager getInstance() {
        if (instance == null) {
            instance = new BeautyCardManager();
        }
        return instance;
    }

    /**
     * 初始化数据
     * <p>
     * 改单的时候调用
     * 初始化购物车中的次卡服务
     */
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

    /**
     * 添加商品到购物车
     */
    public void addDishshopToShopCart(ServerPrivilegeType privilegeType, BeautyCardServiceAccount vo, int position) {
        DishShop dishShop = DishCache.getDishHolder().get(vo.serviceId);
        if (dishShop == null) {
            ToastUtil.showShortToast("当前菜品不存在");
            return;
        }

        if (privilegeType != null && dishShop != null) {
            HashMap<Long, Integer> numMap;
            if (mCacheDishShopVo.containsKey(privilegeType)) {
                numMap = mCacheDishShopVo.get(privilegeType);
                int count;
                if (numMap.containsKey(vo.cardInstanceId)) {
                    count = numMap.get(vo.cardInstanceId);
                    Log.i("BeautyCardManager", " brandDishId = [ " + dishShop.getBrandDishId() + " ] , oldCount = " + count);
                    numMap.put(vo.cardInstanceId, count + dishShop.getDishIncreaseUnit().intValue()); // 添加起卖份数
                    Log.i("BeautyCardManager", " brandDishId = [ " + dishShop.getBrandDishId() + " ] , newOldCount = " + numMap.get(dishShop.getBrandDishId()));
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

    /**
     * 购物车移除商品
     * 重新就算剩余数量，更新View
     */
    public void removeDishshopFromShopCart(ServerPrivilegeType type, IShopcartItemBase mShopcartItem) {
        CardServicePrivilegeVo cardServicePrivilegeVo = mShopcartItem.getCardServicePrivilgeVo();
        removeDishshopFromShopCart(type, cardServicePrivilegeVo.getServerRecordId(), mShopcartItem.getTotalQty().intValue());
    }

    /**
     * 购物车移除商品
     */
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

    /**
     * 获取已点的商品数量
     *
     * @param type           优惠类型
     * @param serverRecordId 服务购买记录
     * @return
     */
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

    /**
     * 退出manager
     */
    public void exitCardManager() {
        mListener = null;
        mCacheDishShopVo.clear();
    }

}
