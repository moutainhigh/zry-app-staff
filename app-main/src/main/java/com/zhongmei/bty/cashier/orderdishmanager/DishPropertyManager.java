package com.zhongmei.bty.cashier.orderdishmanager;

import android.annotation.SuppressLint;
import android.support.v4.util.LongSparseArray;
import android.util.Log;

import com.zhongmei.bty.basemodule.orderdish.entity.DishBrandProperty;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.db.entity.dish.DishPropertyType;
import com.zhongmei.yunfu.db.entity.dish.DishSetmeal;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.orderdish.entity.DishUnitDictionary;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.bty.entity.event.orderdishevent.EventDishPropertiesNotice;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache.DataFilter;
import com.zhongmei.bty.basemodule.orderdish.bean.DishAndStandards;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishPropertiesVo;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishPropertyVo;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishStandardVo;
import com.zhongmei.bty.basemodule.orderdish.bean.ExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderDish;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderExtra;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderSetmeal;
import com.zhongmei.bty.cashier.shoppingcart.vo.PropertyGroupVo;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItemBase;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import de.greenrobot.event.EventBus;

/**
 * 商品属性管理
 *
 * @version: 1.0
 * @date 2015年7月7日
 */
@SuppressLint("UseSparseArrays")
public class DishPropertyManager {

    private static final String TAG = DishPropertyManager.class.getSimpleName();

    /**
     * 每个商品的属性数据。key为Dish.brandDishId
     */
    private final LongSparseArray<DishPropertiesWrapper> propertiesWrapperArray;
    /**
     * 同系列所有商品的规格分组数据。key为DishPropertyType.id
     */
    private final Map<Long, PropertyGroupWrapper> standardGroupMap;

    /**
     * 选中的商品
     */
    private final Set<String> selectedProperties;
    private final LongSparseArray<OrderExtra> selectedExtras;
    private String uuid;
    private boolean loaded;
    private OrderDish selectedOrderDish;
    private boolean isSetmeal;

    public DishPropertyManager() {
        propertiesWrapperArray = new LongSparseArray<DishPropertiesWrapper>();
        standardGroupMap = new HashMap<Long, PropertyGroupWrapper>();
        loaded = false;
        selectedProperties = new HashSet<String>();
        selectedExtras = new LongSparseArray<OrderExtra>();
    }

    public boolean isSetmeal() {
        checkLoad();
        return isSetmeal;
    }

    /**
     * 加载数据。将使用{@link EventDishPropertiesNotice}通知属性和加料数据
     *
     * @param shopcartItem
     * @param refundMode   为true表示退货模式
     */
    public EventDishPropertiesNotice loadData(ShopcartItemBase<?> shopcartItem, boolean refundMode, boolean isDinner) {
        if (loaded) {
            throw new RuntimeException("This manager is already loaded!");
        }
        loaded = true;
        /*
         * 根据shopcartItem统计哪些属性和加料是选中了的
         */
        uuid = shopcartItem.getUuid();
        selectedOrderDish = shopcartItem.getOrderDish();

        DishShop dishShop = DishCache.getDishHolder().get(selectedOrderDish.getSkuId());

        if (dishShop == null) {
            Log.w(TAG, "菜品已经被停用。brandDishId=" + selectedOrderDish.getBrandDishId());
            EventDishPropertiesNotice eventDishPropertiesNotice = new EventDishPropertiesNotice(uuid, null);
            EventBus.getDefault().post(new EventDishPropertiesNotice(uuid, null));
            return eventDishPropertiesNotice;
        }

        isSetmeal = (selectedOrderDish instanceof OrderSetmeal);
        Log.i(TAG, "selectedOrderDish=" + selectedOrderDish.getBrandDishId() + ", isSetmeal=" + isSetmeal);

        if (shopcartItem.getProperties() != null) {
            for (OrderProperty orderProperty : shopcartItem.getProperties()) {
                String key = toPropertyKey(dishShop, orderProperty.getProperty());
                selectedProperties.add(key);
            }
        }
        if (shopcartItem.getExtraItems() != null) {
            for (ExtraShopcartItem extraItem : shopcartItem.getExtraItems()) {
                OrderExtra orderExtra = extraItem.getOrderDish();
//				selectedExtras.put(orderExtra.getSetmealId(), orderExtra);
                selectedExtras.put(orderExtra.getSkuId(), orderExtra);
            }
        }


        //如果dishShop不为null
        propertiesWrapperArray.clear();
        List<OrderExtra> listExtra = new ArrayList<>();
        if (dishShop != null) {
            DishPropertiesWrapper dishWrapper = new DishPropertiesWrapper(dishShop);
            propertiesWrapperArray.put(dishShop.getBrandDishId(), dishWrapper);

            listExtra = getShopExtra(dishShop, dishWrapper);
        }

        //设置返回值
        DishPropertiesVo dishProperTiesVo = new DishPropertiesVo();
        dishProperTiesVo.setExtraList(listExtra);
        EventDishPropertiesNotice eventDishPropertiesNotice = new EventDishPropertiesNotice(uuid, dishProperTiesVo);
        EventBus.getDefault().post(new EventDishPropertiesNotice(uuid, null));
        return eventDishPropertiesNotice;




        /*
         * 找出所有属性。
         * 套餐明细由于不合并规格，所以可以按ID找出此商品的属性。
         * 单品按名称找出所有属性（排除不属于同一分类的属性）
         */
//        List<DishBrandProperty> dishPropertyList;
//        if (isSetmeal) {
//            SetmealPropertyFilter filter = new SetmealPropertyFilter(dishShop);
//            dishPropertyList = DishCache.getDishPropertyHolder().filter(filter);
//        } else {
//            DishPropertyFilter filter = new DishPropertyFilter(dishShop);
//            dishPropertyList = DishCache.getDishPropertyHolder().filter(filter);
//        }
//        Log.i(TAG, "dishPropertyList.size()=" + dishPropertyList.size());
//
//        if (dishPropertyList.isEmpty()) {
//            DishPropertiesWrapper dishWrapper = new DishPropertiesWrapper(dishShop);
//            propertiesWrapperArray.put(dishShop.getBrandDishId(), dishWrapper);
//            addExtra(dishWrapper, dishShop, isDinner);
//        } else {
//            /*
//             * 将上一步找出的属性分配到每个商品中，每个商品一个DishPropertiesWrapper对象，并找出每个商品的加料信息。
//             * 全部商品都放在propertiesWrapperArray中，key为dishShop.brandDishId
//             */
//            Set<Long> standardIds = new HashSet<Long>();
//            for (DishBrandProperty dishProperty : dishPropertyList) {
//                DishShop tmpDish = DishCache.getDishHolder().get(dishProperty.getDishId());
//                if (tmpDish == null) {
//                    Log.w(TAG, "无此商品！ dishId=" + dishProperty.getDishId());
//                    continue;
//                }
//
//                DishPropertiesWrapper dishWrapper = propertiesWrapperArray.get(dishProperty.getDishId());
//                if (dishWrapper == null) {
//                    dishWrapper = new DishPropertiesWrapper(tmpDish);
//                    propertiesWrapperArray.put(tmpDish.getBrandDishId(), dishWrapper);
//                    // 获取到商品的加料信息
//                    addExtra(dishWrapper, tmpDish, isDinner);
//                }
//
//                // 获取商品的属性，区分规格与其他属性
//                DishProperty property = DishCache.getPropertyHolder().get(dishProperty.getPropertyId());
//                if (property != null) {
//                    //如果没有设置规格，不添加所有的规格
//                    if (property.getPropertyKind() == PropertyKind.STANDARD) {
//                        Log.i(TAG, "STANDARD: " + property.getName());
//                        // 如果没有找到规则类属性分类，就丢弃此规则类属性
//                        boolean b = true;
//                        if (!standardIds.contains(property.getId())) {
//                            b = addPropertyToMap(standardGroupMap, property);
//                            standardIds.add(property.getId());
//                        }
//                        if (b && dishProperty.getDishId().equals(dishWrapper.dish.getBrandDishId())) {
//                            dishWrapper.addStandard(property);
//                        }
//                    } else {
//                        dishWrapper.addProperty(property);
//                    }
//                } else {
//                    Log.w(TAG, "无此属性！ propertyId=" + dishProperty.getPropertyId());
//                }
//            }
//        }
//
//        // 发送包含此规格菜品信息的通知
//        Long dishId = dishShop.getBrandDishId();
//        DishPropertiesWrapper wrapper = propertiesWrapperArray.get(dishId);
//        if (wrapper == null && isDinner) {
//            wrapper = initWrapper(dishShop, isDinner);
//        }
//        if (wrapper != null) {
//            //如果没有加料，参加所有的
//            if (isDinner) {
//                if (wrapper.extraMap == null || wrapper.extraMap.isEmpty()) {
//                    addExtra(wrapper, dishShop, isDinner);
//                }
//                //做法为null时,设置为公共库
//                if (wrapper.propertyGroupMap == null || wrapper.propertyGroupMap.isEmpty() || !isHasRecipe(wrapper)) {
//                    setPublicProperty(wrapper);
//                }
//            }
//            if (isSetmeal) {
//                return postEventBySetmeal(wrapper);
//            } else {
//                return postEventByBasic(wrapper, null, refundMode);
//            }
//        } else {
//            Log.w(TAG, "无此商品的属性！ dishId=" + dishId);
//        }
//        return null;
    }

    private DishPropertiesWrapper initWrapper(DishShop dishShop, boolean isDinner) {
        DishPropertiesWrapper wrapper = new DishPropertiesWrapper(dishShop);
        propertiesWrapperArray.put(dishShop.getBrandDishId(), wrapper);
        // 获取到商品的加料信息
        addExtra(wrapper, dishShop, isDinner);
        return wrapper;
    }

    /**
     * 设置公共库的做法
     */
    private void setPublicProperty(DishPropertiesWrapper dishWrapper) {
        List<DishProperty> propertyList = DishCache.getPropertyHolder().filter(null);
        if (!Utils.isEmpty(propertyList)) {
            for (DishProperty property : propertyList) {
                dishWrapper.addProperty(property);
            }
        }
    }

    /**
     * 是否有口味做法
     *
     * @param dishWrapper
     * @return
     */
    private boolean isHasRecipe(DishPropertiesWrapper dishWrapper) {
        if (dishWrapper.propertyGroupMap == null || dishWrapper.propertyGroupMap.isEmpty()) {
            return false;
        }
        for (PropertyGroupWrapper wrapper : dishWrapper.propertyGroupMap.values()) {
            if (wrapper.properties != null) {
                for (DishProperty property : wrapper.properties) {
                    if (property.getPropertyKind() == PropertyKind.PROPERTY) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private List<OrderExtra> getShopExtra(DishShop dishShop, DishPropertiesWrapper dishWrapper) {
        List<OrderExtra> listExtra = new ArrayList<>();
        List<DishProperty> listProperty = DishCache.getPropertyHolder().getDishPropertyByDishShopId(dishShop.getId());
        if (Utils.isNotEmpty(listProperty)) {
            for (DishProperty property : listProperty) {
                OrderExtra extrao = new OrderExtra(convertDishShop(property), null);
                listExtra.add(extrao);
                dishWrapper.addExtra(extrao);
            }

        }


        return listExtra;
    }

    /**
     * 构建指定商品的加料数据
     *
     * @param dishWrapper
     * @param dishShop
     */
    private void addExtra(DishPropertiesWrapper dishWrapper, DishShop dishShop, boolean isDinner) {
        // 获取到商品的加料信息

        List<DishProperty> listExtra = DishCache.getPropertyHolder().getDishPropertyByDishShopId(dishShop.getId());

        if (Utils.isNotEmpty(listExtra)) {
            for (DishProperty property : listExtra) {
                dishWrapper.addExtra(new OrderExtra(convertDishShop(property), null));
            }
        }


//        DishExtraFilter dishExtraFilter = new DishExtraFilter(dishShop);
//        List<DishSetmeal> dishExtraList = DishCache.getDishExtraHolder().filter(dishExtraFilter);
//        if (Utils.isEmpty(dishExtraList) && isDinner) {
////			dishExtraList= DishCache.getDishExtraHolder().filter(null);
//            List<DishShop> extraList = DishCache.getExtraHolder().filter(null);
//            if (!Utils.isEmpty(extraList)) {
//                for (DishShop shop : extraList) {
//                    dishWrapper.addExtra(new OrderExtra(shop, null));
//                }
//                return;
//            }
//        }
//        for (DishSetmeal dishExtra : dishExtraList) {
//            if (!dishWrapper.hasExtra(dishExtra)) {
//                DishShop extra = DishCache.getExtraHolder().get(dishExtra.getChildDishId());
//                if (extra != null) {
//                    Log.i(TAG, "EXTRA: " + extra.getName());
//                    dishWrapper.addExtra(new OrderExtra(extra, dishExtra));
//                } else {
//                    Log.w(TAG, "无此加料！ childDishId=" + dishExtra.getChildDishId());
//                }
//            }
//        }
    }

    private DishShop convertDishShop(DishProperty property) {
        DishShop dishShop = new DishShop();
        dishShop.setId(property.getId());
        dishShop.setName(property.getName());
        dishShop.setDishIncreaseUnit(BigDecimal.ONE);
        dishShop.setMarketPrice(property.getReprice());
        dishShop.setSaleType(SaleType.UNWEIGHING);
        dishShop.setType(DishType.EXTRA);
        dishShop.setUuid(property.getUuid());
        dishShop.setIsDiscountAll(Bool.YES);
        dishShop.setUnitName("份");//默认设置一个单位名字 容错，服务器必须传入单位名称
        return dishShop;
    }

    /**
     * 选中多个规格类属性。将使用{@link EventDishPropertiesNotice}通知属性和加料数据
     *
     * @param standards  UI上选择的规格
     * @param refundMode 为true表示退货模式
     */
    public EventDishPropertiesNotice selectStandard(Set<DishProperty> standards, boolean refundMode) {
        checkLoad();
        selectedProperties.clear();
        selectedExtras.clear();
        return postEventByBasic(null, standards, refundMode);
    }

    /**
     * 打开非套餐明细的商品的属性界面时使用此方法发送Event
     *
     * @param postWrapper
     * @param standards   UI中选中的规格类属性
     * @param refundMode  为true表示退货模式
     */
    private EventDishPropertiesNotice postEventByBasic(DishPropertiesWrapper postWrapper, Set<DishProperty> standards,
                                                       boolean refundMode) {
        if (postWrapper != null) {
            if (!refundMode && postWrapper.dish.isClear()) {
                postWrapper = null;
            } else {
                standards = postWrapper.dish.getStandards();
            }
        }
        if (standards == null) {
            standards = Collections.emptySet();
        }
        /*
         * 判断哪些规格可以被UI选择，用传入的standards依次与所有商品的规格集对比：
         * 1、如果传入与某个商品规格集完全相同就定位到了该商品
         * 2、如果传入的是某个商品规格集的子集，那么该商品的规格都是可选的
         *
         * 判断哪些规格要显示估清状态：
         * 1、没有传入规格集，使用某个规格的所有商品都被估清时该规格显示为估清状态
         * 2、传入了规格集，分两种情况：
         *    a、定位到的商品(一个或多个)所使用的规格：使用某个规格的该批商品都被估清时该规格显示为估清状态
         *    b、未被定位到的商品所使用的规格按1的方式处理
         */
        Set<DishProperty> usableStandards = new HashSet<DishProperty>(standards);//可用的规格集
        Map<DishProperty, ClearStatus> clearStatusMap = new HashMap<DishProperty, ClearStatus>();
        int size = propertiesWrapperArray.size();
        if (standards.isEmpty()) {
            for (int i = 0; i < size; i++) {
                DishPropertiesWrapper wrapper = propertiesWrapperArray.valueAt(i);
                // 未传入规格集，所有商品的规格都是可用状态
                usableStandards.addAll(wrapper.dish.getStandards());
                // 判断估清状态
                if (wrapper.dish.isClear()) {
                    for (DishProperty standard : wrapper.dish.getStandards()) {
                        if (clearStatusMap.get(standard) != ClearStatus.SALE) {
                            clearStatusMap.put(standard, ClearStatus.CLEAR);
                        }
                    }
                } else {
                    for (DishProperty standard : wrapper.dish.getStandards()) {
                        clearStatusMap.put(standard, ClearStatus.SALE);
                    }
                }
            }
        } else {
            Map<DishProperty, ClearStatus> clearStatusByUnusable = new HashMap<DishProperty, ClearStatus>();
            for (int i = 0; i < size; i++) {
                DishPropertiesWrapper wrapper = propertiesWrapperArray.valueAt(i);
                int v;
                if (postWrapper == wrapper) {
                    v = 0;
                } else {
                    v = wrapper.dish.compareStandards(standards);
                }
                if (v == -1) {  // 传入规格集有些在遍历的商品规格之外
                    if (wrapper.dish.isClear()) {
                        for (DishProperty standard : wrapper.dish.getStandards()) {
                            if (clearStatusByUnusable.get(standard) != ClearStatus.SALE) {
                                clearStatusByUnusable.put(standard, ClearStatus.CLEAR);
                            }
                        }
                    } else {
                        for (DishProperty standard : wrapper.dish.getStandards()) {
                            clearStatusByUnusable.put(standard, ClearStatus.SALE);
                        }
                    }
                } else {
                    // 传入规格集与遍历的商品规格完全一样，选中该商品
                    if (v == 0) {
                        postWrapper = wrapper;
                    }

                    // 传入规格集是遍历的商品规格的子集，商品的所有规格都可用状态
                    usableStandards.addAll(wrapper.dish.getStandards());

                    // 如果遍历的商品被估清了，不在传入规格之列且未被其他在售商品使用的规格都要置为估清
                    if (wrapper.dish.isClear()) {
                        for (DishProperty standard : wrapper.dish.getStandards()) {
                            if (clearStatusMap.get(standard) != ClearStatus.SALE) {
                                clearStatusMap.put(standard, ClearStatus.CLEAR);
                            }
                        }
                    } else {
                        for (DishProperty standard : wrapper.dish.getStandards()) {
                            clearStatusMap.put(standard, ClearStatus.SALE);
                        }
                    }
                }
            }
            // 加入未定位到的商品中被估清的规格
            for (Map.Entry<DishProperty, ClearStatus> entry : clearStatusByUnusable.entrySet()) {
                DishProperty standard = entry.getKey();
                ClearStatus clearStatus = entry.getValue();
                switch (clearStatus) {
                    case SALE:
                        clearStatusMap.put(standard, clearStatus);
                        break;
                    case CLEAR:
                        if (clearStatusMap.get(standard) != ClearStatus.SALE) {
                            clearStatusMap.put(standard, clearStatus);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        Log.w(TAG, "usableStandards.size()=" + usableStandards.size());

        // 得到要返回给UI展示的规格列表（相当于全系列商品的所有规格的并集）
        List<PropertyGroupVo<DishStandardVo>> standardGroupList = new ArrayList<PropertyGroupVo<DishStandardVo>>();
        if (standardGroupMap != null) {
            for (PropertyGroupWrapper wrapper : standardGroupMap.values()) {
                PropertyGroupVo<DishStandardVo> groupVo = new PropertyGroupVo<DishStandardVo>(
                        wrapper.propertyType, new ArrayList<DishStandardVo>());
                for (DishProperty property : wrapper.properties) {
                    int state;
                    if (usableStandards.contains(property)) {
                        state = DishStandardVo.ENABLE;
                    } else {
                        state = DishStandardVo.DISABLE;
                    }
                    ClearStatus clearStatus = clearStatusMap.get(property);
                    if (clearStatus == null) {
                        clearStatus = ClearStatus.SALE;
                    }
                    boolean selected = standards.contains(property);
                    Log.i(TAG, property.getName() + ": selected=" + selected + ", state=" + state + ", clearStatus=" + clearStatus);
                    groupVo.getPropertyList().add(new DishStandardVo(property, selected, state, clearStatus));
                }
                standardGroupList.add(groupVo);
            }
        }
        return postEvent(standardGroupList, postWrapper);
    }

    /**
     * 打开套餐明细的属性界面时使用此方法发送Event
     *
     * @param postWrapper
     */
    private EventDishPropertiesNotice postEventBySetmeal(DishPropertiesWrapper postWrapper) {
        List<PropertyGroupVo<DishStandardVo>> standardGroupList = new ArrayList<PropertyGroupVo<DishStandardVo>>();
        if (standardGroupMap != null) {
            for (PropertyGroupWrapper wrapper : standardGroupMap.values()) {
                PropertyGroupVo<DishStandardVo> groupVo = new PropertyGroupVo<DishStandardVo>(
                        wrapper.propertyType, new ArrayList<DishStandardVo>());
                for (DishProperty property : wrapper.properties) {
                    if (postWrapper.dish.hasStandard(property)) {
                        groupVo.getPropertyList().add(new DishStandardVo(property, true, DishStandardVo.ENABLE));
                        break;
                    }
                }
                standardGroupList.add(groupVo);
            }
        }
        return postEvent(standardGroupList, postWrapper);
    }

    /**
     * @param standardGroupList 要返回给UI展示的规格
     * @param postWrapper       定位到的商品
     */
    private EventDishPropertiesNotice postEvent(List<PropertyGroupVo<DishStandardVo>> standardGroupList, DishPropertiesWrapper postWrapper) {
        // Event中总是会包含规格类属性列表。其他属性与加料只有定位到一个商品时才会有
        Collections.sort(standardGroupList, newGroupVoComparator());
        DishPropertiesVo vo = new DishPropertiesVo();
        vo.setStandardGroupList(standardGroupList);
        if (postWrapper != null) {
            // 如果定位到的商品已经选中了，就直接返回选中的orderDish。
            if (selectedOrderDish.getDishShop().equals(postWrapper.dish.getDishShop())) {
                vo.setOrderDish(selectedOrderDish);
            } else {
                // 套餐明细不可能选规格，所以定位到的商品一定会是已经选中的，所以不会用OrderSetmeal创建vo
                OrderDish orderShop = postWrapper.dish.toOrderDish();
                orderShop.setQty(selectedOrderDish.getSingleQty(), selectedOrderDish.getTotalQty());
                vo.setOrderDish(orderShop);
            }
            Log.i(TAG, "vo.dishShop=" + vo.getOrderDish().getBrandDishId());
            // 返回非规格类的其他属性
            vo.setPropertyGroupList(new ArrayList<PropertyGroupVo<DishPropertyVo>>());
            if (postWrapper.propertyGroupMap != null) {
                for (PropertyGroupWrapper wrapper : postWrapper.propertyGroupMap.values()) {
                    PropertyGroupVo<DishPropertyVo> groupVo = new PropertyGroupVo<DishPropertyVo>(
                            wrapper.propertyType, new ArrayList<DishPropertyVo>());
                    for (DishProperty property : wrapper.properties) {
                        // 选中之前选中的属性
                        String key = toPropertyKey(postWrapper.dish.getDishShop(), property);
                        boolean selected = selectedProperties.contains(key);
                        groupVo.getPropertyList().add(new DishPropertyVo(property, selected));
                    }
                    vo.getPropertyGroupList().add(groupVo);
                }
            }
            Collections.sort(vo.getPropertyGroupList(), newGroupVoComparator());
            // 返回加料列表
            vo.setExtraList(new ArrayList<OrderExtra>());
            if (postWrapper.extraMap != null) {
                for (OrderSetmeal extra : postWrapper.extraMap.values()) {
//				OrderExtra orderExtra = selectedExtras.get(extra.getSetmealId());
                    OrderExtra orderExtra = selectedExtras.get(extra.getSkuId());
                    if (orderExtra == null) {
                        orderExtra = new OrderExtra(extra.getDishShop(), extra.getSetmeal());
                    }
                    vo.getExtraList().add(orderExtra);
                }
            }
            Collections.sort(vo.getExtraList(), newExtraComparator());
        }
        EventDishPropertiesNotice eventDishPropertiesNotice = new EventDishPropertiesNotice(uuid, vo);
        EventBus.getDefault().post(eventDishPropertiesNotice);
        return eventDishPropertiesNotice;
    }

    private String toPropertyKey(DishShop dishShop, DishProperty property) {
        return dishShop.getId() + "_" + property.getId();
    }

    private void checkLoad() {
        if (!loaded) {
            throw new RuntimeException("The manager is not load!");
        }
    }

    /**
     * 套餐明细的属性数据过滤器
     *
     * @version: 1.0
     * @date 2015年7月20日
     */
    private static class SetmealPropertyFilter implements DataFilter<DishBrandProperty> {

        private final DishShop dishShop;

        SetmealPropertyFilter(DishShop dishShop) {
            this.dishShop = dishShop;
        }

        @Override
        public boolean accept(DishBrandProperty entity) {
            return dishShop.getBrandDishId().equals(entity.getDishId());
        }

    }

    /**
     * 单品或套餐的属性数据过滤器
     *
     * @version: 1.0
     * @date 2015年7月15日
     */
    public static class DishPropertyFilter implements DataFilter<DishBrandProperty> {

        private final DishShop dishShop;

        public DishPropertyFilter(DishShop dishShop) {
            this.dishShop = dishShop;
        }

        @Override
        public boolean accept(DishBrandProperty entity) {
            if (dishShop.getHasStandard() == Bool.YES) {
                // 有规格时名称和所属类别都相同的归为同系列商品，所以同系列所有商品的属性都要保留
                if (dishShop.getName().equals(entity.getDishName())) {
                    DishShop tmp = DishCache.getDishHolder().get(entity.getDishId());
                    return tmp != null
                            && tmp.getHasStandard() == Bool.YES
                            && tmp.getDishTypeId().equals(dishShop.getDishTypeId());
                }
                return false;
            } else {
                // 无规格时比较ID
                return dishShop.getBrandDishId().equals(entity.getDishId());
            }
        }

    }

    /**
     * 菜品加料数据过滤器
     *
     * @version: 1.0
     * @date 2015年7月15日
     */
    public static class DishExtraFilter implements DataFilter<DishSetmeal> {

        private final DishShop dishShop;

        public DishExtraFilter(DishShop dishShop) {
            this.dishShop = dishShop;
        }

        @Override
        public boolean accept(DishSetmeal entity) {
            return dishShop.getBrandDishId().equals(entity.getDishId());
        }

    }

    /**
     * 对属性分类及其下所有属性的封装
     *
     * @version: 1.0
     * @date 2015年7月15日
     */
    static class PropertyGroupWrapper {

        final DishPropertyType propertyType;
        /**
         * 属性列表
         */
        final Set<DishProperty> properties;

        PropertyGroupWrapper(DishPropertyType propertyType) {
            this.propertyType = propertyType;
            properties = new TreeSet<DishProperty>(newPropertyComparator());
        }

        void addProperty(DishProperty property) {
            if (!properties.contains(property)) {
                properties.add(property);
            }
        }
    }

    /**
     * 封装一个商品及其属性和加料
     *
     * @version: 1.0
     * @date 2015年7月15日
     */
    private static class DishPropertiesWrapper {

        private final DishAndStandards dish;
        /**
         * 属性分组数据。key为DishPropertyType.id
         */
        private final Map<Long, PropertyGroupWrapper> propertyGroupMap;
        /**
         * 加料数据。key为DishSetmeal.id
         */
        private final Map<Long, OrderExtra> extraMap;

        DishPropertiesWrapper(DishShop dishShop) {
            DishUnitDictionary unit = DishCache.getUnitHolder().get(dishShop.getUnitId());
            dish = new DishAndStandards(dishShop, unit);
            propertyGroupMap = new HashMap<Long, PropertyGroupWrapper>();
            extraMap = new HashMap<Long, OrderExtra>();
        }

        void addStandard(DishProperty property) {
            dish.addStandard(property);
        }

        void addProperty(DishProperty property) {
            addPropertyToMap(propertyGroupMap, property);
        }

        void addExtra(OrderExtra extra) {
//			extraMap.put(extra.getSetmeal().getChildDishId(), extra);
            extraMap.put(extra.getBrandDishId(), extra);
        }

        boolean hasExtra(DishSetmeal dishExtra) {
//			return extraMap.containsKey(dishExtra.getChildDishId());
            return extraMap.containsKey(dishExtra.getChildDishId());
        }

    }

    private static boolean addPropertyToMap(Map<Long, PropertyGroupWrapper> map, DishProperty property) {
        Long typeId = property.getPropertyTypeId();
        PropertyGroupWrapper groupVo = map.get(typeId);
        if (groupVo == null) {
            DishPropertyType propertyType = DishCache.getPropertyTypeHolder().get(typeId);
            PropertyKind kind = property.getPropertyKind();
            if (propertyType == null && (kind == PropertyKind.PROPERTY || kind == PropertyKind.STANDARD)) {
                Log.w(TAG, "没有属性分类！property.id=" + property.getId());
                return false;
            }
            groupVo = new PropertyGroupWrapper(propertyType);
            groupVo.addProperty(property);
            map.put(typeId, groupVo);
        } else {
            groupVo.addProperty(property);
        }
        return true;
    }

    private static Comparator<DishProperty> newPropertyComparator() {
        return new Comparator<DishProperty>() {

            @Override
            public int compare(DishProperty lhs, DishProperty rhs) {
                if (lhs.getSort() == null) {
                    if (rhs.getSort() != null) {
                        return 1;
                    }
                } else if (rhs.getSort() == null) {
                    return -1;
                }
                int v = lhs.getSort().compareTo(rhs.getSort());
                if (v == 0) {
                    v = lhs.getId().compareTo(rhs.getId());
                }
                return v;
            }

        };
    }

    static Comparator<PropertyGroupVo<?>> newGroupVoComparator() {
        return new Comparator<PropertyGroupVo<?>>() {

            @Override
            public int compare(PropertyGroupVo<?> lhs, PropertyGroupVo<?> rhs) {
                if (lhs.getPropertyType() == null) {
                    if (rhs.getPropertyType() != null) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else if (rhs.getPropertyType() == null) {
                    return -1;
                }
                Integer sort1 = lhs.getPropertyType().getSort();
                Integer sort2 = rhs.getPropertyType().getSort();
                if (sort1 == null) {
                    if (sort2 != null) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else if (sort2 == null) {
                    return -1;
                }
                int v = sort1.compareTo(sort2);
                if (v == 0) {
                    v = lhs.getPropertyType().getId().compareTo(rhs.getPropertyType().getId());
                }
                return v;
            }

        };
    }

    private static Comparator<OrderExtra> newExtraComparator() {
        return new Comparator<OrderExtra>() {

            @Override
            public int compare(OrderExtra lhs, OrderExtra rhs) {
                if (lhs.getDishShop().getSort() == null) {
                    if (rhs.getDishShop().getSort() != null) {
                        return 1;
                    }
                } else if (rhs.getDishShop().getSort() == null) {
                    return -1;
                }
                int v = lhs.getDishShop().getSort().compareTo(rhs.getDishShop().getSort());
                if (v == 0) {
                    v = lhs.getSetmealId().compareTo(rhs.getSetmealId());
                }
                return v;
            }

        };
    }
}
