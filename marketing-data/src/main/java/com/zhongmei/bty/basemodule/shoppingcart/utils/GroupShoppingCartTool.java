package com.zhongmei.bty.basemodule.shoppingcart.utils;

import com.zhongmei.bty.basemodule.orderdish.bean.DishMenuVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.bty.basemodule.orderdish.bean.IExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IOrderProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.bty.basemodule.orderdish.entity.DishCarteDetail;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishSetmeal;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.orderdish.entity.DishUnitDictionary;
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.bean.ShoppingCartVo;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 团餐购物车工具类
 * Created by demo on 2018/12/15
 */
public class GroupShoppingCartTool {

    private static Map<Long, List<IShopcartItem>> typeMap = null;

    private static Map<Long, List<IShopcartItem>> singleTypeMap = null;

    protected static final Long otherTypeId = -10000L;

    /**
     * 将菜单和开台数据带入购物车
     *
     * @param dishMenuVo 模板可能为null
     */
    public static void addMenuToShopcart(DishMenuVo dishMenuVo, DinnerShoppingCart shoppingCart) {
        if (dishMenuVo == null || Utils.isEmpty(dishMenuVo.getCarteDetailList())) {
            return;
        }
        ShoppingCartVo dinnerShoppingCartVo = shoppingCart.getShoppingCartVo();
        //存储数量
        Map<Long, BigDecimal> disCartNumMap = new HashMap<>();
        List<DishCarteDetail> dishCarteDetailList = dishMenuVo.getCarteDetailList();
        //子菜map
        Map<Long, List<DishCarteDetail>> detailMap = new HashMap<Long, List<DishCarteDetail>>();
        //单菜list
        List<DishShop> singleList = new ArrayList<>();
        //套餐list
        List<DishShop> comboList = new ArrayList<>();
        //存储菜品的备注
        Map<Long, String> memoMap = new HashMap<>();
        if (Utils.isNotEmpty(dishCarteDetailList)) {
            for (DishCarteDetail detail : dishCarteDetailList) {
                DishShop dishShop = DishCache.getDishHolder().get(detail.getDishUuid());
                if (dishShop == null) {
                    continue;
                }
                if (detail.getMemo() != null)
                    memoMap.put(detail.getDishId(), detail.getMemo());
                if (dishShop.isCombo()) {
                    comboList.add(dishShop);
                } else if (detail.getMealId() != null && detail.getMealId() != 0) {
                    List childDetailList = detailMap.get(detail.getMealId());
                    if (childDetailList == null) {
                        childDetailList = new ArrayList();
                        detailMap.put(detail.getMealId(), childDetailList);
                    }
                    childDetailList.add(detail);
                } else {
                    singleList.add(dishShop);
                }
                disCartNumMap.put(detail.getDishId(), detail.getNum());
            }
        }

        for (DishShop dishShop : singleList) {
            DishUnitDictionary unit = DishCache.getUnitHolder().get(dishShop.getUnitId());
            DishVo dishVo = new DishVo(dishShop, unit);
            ShopcartItem shopcartItem = CreateItemTool.createShopcartItem(dishVo);
            String memo = memoMap.get(dishShop.getId());
            shopcartItem.setMemo(memo);
            shopcartItem.setIsGroupDish(true);
            BigDecimal num = disCartNumMap.get(dishShop.getId());
            if (num == null) {
                num = BigDecimal.ONE;
            }
            shopcartItem.changeQty(num);
            shoppingCart.addShippingToCart(dinnerShoppingCartVo, shopcartItem, false);
        }

        for (DishShop dishShop : comboList) {
            DishUnitDictionary unit = DishCache.getUnitHolder().get(dishShop.getUnitId());
            DishVo dishVo = new DishVo(dishShop, unit);
            ShopcartItem shopcartItem = CreateItemTool.createShopcartItem(dishVo);
            String memo = memoMap.get(dishShop.getId());
            shopcartItem.setMemo(memo);
            shopcartItem.setIsGroupDish(true);
            BigDecimal num = disCartNumMap.get(dishShop.getId());
            shopcartItem.changeQty(num);
            shopcartItem.setSetmealItems(new ArrayList<SetmealShopcartItem>());
            List<DishCarteDetail> setmealList = detailMap.get(dishShop.getId());
            if (setmealList != null) {
                for (DishCarteDetail carteDetail : setmealList) {
                    DishShop setmealDishShop = DishCache.getDishHolder().get(carteDetail.getDishUuid());
                    if (setmealDishShop == null) {
                        continue;
                    }
                    DishUnitDictionary setmealUnit = DishCache.getUnitHolder().get(setmealDishShop.getUnitId());
                    Set<DishProperty> standards = DishManager.filterStandards(setmealDishShop);
                    Long comboDishId = dishShop.getBrandDishId();
                    Long childDishId = setmealDishShop.getBrandDishId();
                    Long setmealGroupId = carteDetail.getSetmealGroupId();
                    DishSetmeal dishSetmeal = DishCache.getSetmealHolder().get(comboDishId, childDishId, setmealGroupId);
                    DishSetmealVo dishSetmealVo = new DishSetmealVo(setmealDishShop, dishSetmeal, standards, setmealUnit);
                    String uuid = SystemUtils.genOnlyIdentifier();
                    SetmealShopcartItem setmealShopcartItem = new SetmealShopcartItem(uuid, dishSetmealVo.toOrderSetmeal(), shopcartItem);
                    setmealShopcartItem.setMemo(carteDetail.getMemo());
                    setmealShopcartItem.setIsGroupDish(true);
                    setmealShopcartItem.changeQty(carteDetail.getNum());
                    shopcartItem.getSetmealItems().add(setmealShopcartItem);
                }
            }
            shoppingCart.addShippingToCart(dinnerShoppingCartVo, shopcartItem, false);
        }

    }

    public static Map<Long, List<IShopcartItem>> getTypeMap() {
        return typeMap;
    }

    public static Map<Long, List<IShopcartItem>> getSingleTypeMap() {
        return singleTypeMap;
    }

    public static BigDecimal getDisplyExtraQty(IShopcartItemBase shopcartItem, BigDecimal deskCount) {
        if (shopcartItem.isGroupDish() && shopcartItem instanceof ReadonlyExtraShopcartItem) {
            return MathDecimal.div(shopcartItem.getSingleQty(), deskCount);
        }
        return shopcartItem.getSingleQty();
    }

    /**
     * 获取分组菜品的市场价
     *
     * @param shopcartItemList
     * @return
     */
    public static BigDecimal getGroupDiffAmount(BigDecimal dishMenuPrice, List<IShopcartItem> shopcartItemList, BigDecimal deskCount) {
        BigDecimal totalMarketPrice = BigDecimal.ZERO;
        for (IShopcartItem shopcartItem : shopcartItemList) {
            if (!shopcartItem.isGroupDish() || shopcartItem.getStatusFlag() == StatusFlag.INVALID) {
                continue;
            }
            //怎么拿到实际价格,保存过的菜没有实际价格
            if (shopcartItem instanceof ReadonlyShopcartItem) {
                DishShop dishShop = DishCache.getDishHolder().get(shopcartItem.getSkuUuid());
                if (dishShop == null) {
                    continue;
                }
                BigDecimal qty = ShopcartItemUtils.getDisplyQty(shopcartItem, deskCount);
                totalMarketPrice = totalMarketPrice.add(dishShop.getMarketPrice().multiply(qty));
                if (!Utils.isEmpty(shopcartItem.getSetmealItems())) {
                    for (ISetmealShopcartItem setmealShopcartItem : shopcartItem.getSetmealItems()) {
                        totalMarketPrice = totalMarketPrice.add(mathSaveGroupDishPrice(setmealShopcartItem, deskCount));
                    }
                } else {
                    totalMarketPrice = totalMarketPrice.add(mathSaveGroupDishPrice(shopcartItem, deskCount));
                }
            } else {
                totalMarketPrice = totalMarketPrice.add(shopcartItem.getActualAmount());
            }
        }
        BigDecimal diff = dishMenuPrice.subtract(totalMarketPrice);
        return diff.negate();
    }

    /**
     * 计算保存过的菜品价格
     *
     * @return
     */
    private static BigDecimal mathSaveGroupDishPrice(IShopcartItemBase shopcartItem, BigDecimal deskCount) {
        BigDecimal totalMarketPrice = BigDecimal.ZERO;
        List<? extends IOrderProperty> properties = shopcartItem.getProperties();
        if (properties != null) {
            for (IOrderProperty property : properties) {
                if (property.getPropertyKind() == PropertyKind.PROPERTY) {
                    DishProperty dishProperty = DishCache.getPropertyHolder().get(property.getPropertyUuid());
                    if (dishProperty != null) {
                        totalMarketPrice = totalMarketPrice.add(dishProperty.getReprice());
                    }
                }
            }
        }
        Collection<? extends IExtraShopcartItem> extraList = shopcartItem.getExtraItems();
        if (extraList != null) {
            for (IExtraShopcartItem extraShopcartItem : extraList) {
                BigDecimal singleQty = ShopcartItemUtils.getDisplyQty(extraShopcartItem, deskCount);
                DishShop dishShopExtra = DishCache.getExtraHolder().get(extraShopcartItem.getSkuUuid());
                if (dishShopExtra != null) {
                    totalMarketPrice = totalMarketPrice.add(dishShopExtra.getMarketPrice().multiply(singleQty));
                }
            }
        }
        return totalMarketPrice;
    }

    /**
     * 创建分类
     *
     * @param dataList
     */
    public static List<IShopcartItem> buildTypeMap(List<IShopcartItem> dataList) {
        List<IShopcartItem> filterList = new ArrayList<>();

        typeMap = new HashMap<Long, List<IShopcartItem>>();
        singleTypeMap = new HashMap<Long, List<IShopcartItem>>();
        if (dataList != null && dataList.size() > 0) {
            // 遍历购物车中的菜品，对菜品进行分组
            for (int i = 0; i < dataList.size(); i++) {

                IShopcartItem shopCartItem = dataList.get(i);

                if (shopCartItem.getStatusFlag() == StatusFlag.INVALID
                        && (shopCartItem.getInvalidType() != InvalidType.SPLIT)
                        && (shopCartItem.getInvalidType() != InvalidType.RETURN_QTY)
                        && (shopCartItem.getInvalidType() != InvalidType.MODIFY_DISH)) {
                    continue;
                }

                //添加商品，用来做累计商品数量
                filterList.add(shopCartItem);

                Long typeId = otherTypeId;
                DishShop dishShop = shopCartItem.getDishShop();
                if (dishShop == null) {
                    dishShop = DishCache.getDishHolder().get(shopCartItem.getSkuUuid());
                }
                if (dishShop != null) {
                    typeId = dishShop.getDishTypeId();
                }
                if (shopCartItem.isGroupDish()) {
                    List<IShopcartItem> shopcartItemList = typeMap.get(typeId);
                    if (shopcartItemList == null) {
                        shopcartItemList = new ArrayList<>();
                        typeMap.put(typeId, shopcartItemList);
                    }
                    shopcartItemList.add(shopCartItem);
                } else {
                    List<IShopcartItem> shopcartItemList = singleTypeMap.get(typeId);
                    if (shopcartItemList == null) {
                        shopcartItemList = new ArrayList<>();
                        singleTypeMap.put(typeId, shopcartItemList);
                    }
                    shopcartItemList.add(shopCartItem);
                }
            }
        }

        return filterList;
    }

}
