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


@SuppressLint("UseSparseArrays")
public class DishPropertyManager {

    private static final String TAG = DishPropertyManager.class.getSimpleName();


    private final LongSparseArray<DishPropertiesWrapper> propertiesWrapperArray;

    private final Map<Long, PropertyGroupWrapper> standardGroupMap;


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


    public EventDishPropertiesNotice loadData(ShopcartItemBase<?> shopcartItem, boolean refundMode, boolean isDinner) {
        if (loaded) {
            throw new RuntimeException("This manager is already loaded!");
        }
        loaded = true;

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
                selectedExtras.put(orderExtra.getSkuId(), orderExtra);
            }
        }


                propertiesWrapperArray.clear();
        List<OrderExtra> listExtra = new ArrayList<>();
        if (dishShop != null) {
            DishPropertiesWrapper dishWrapper = new DishPropertiesWrapper(dishShop);
            propertiesWrapperArray.put(dishShop.getBrandDishId(), dishWrapper);

            listExtra = getShopExtra(dishShop, dishWrapper);
        }

                DishPropertiesVo dishProperTiesVo = new DishPropertiesVo();
        dishProperTiesVo.setExtraList(listExtra);
        EventDishPropertiesNotice eventDishPropertiesNotice = new EventDishPropertiesNotice(uuid, dishProperTiesVo);
        EventBus.getDefault().post(new EventDishPropertiesNotice(uuid, null));
        return eventDishPropertiesNotice;





    }

    private DishPropertiesWrapper initWrapper(DishShop dishShop, boolean isDinner) {
        DishPropertiesWrapper wrapper = new DishPropertiesWrapper(dishShop);
        propertiesWrapperArray.put(dishShop.getBrandDishId(), wrapper);
                addExtra(wrapper, dishShop, isDinner);
        return wrapper;
    }


    private void setPublicProperty(DishPropertiesWrapper dishWrapper) {
        List<DishProperty> propertyList = DishCache.getPropertyHolder().filter(null);
        if (!Utils.isEmpty(propertyList)) {
            for (DishProperty property : propertyList) {
                dishWrapper.addProperty(property);
            }
        }
    }


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


    private void addExtra(DishPropertiesWrapper dishWrapper, DishShop dishShop, boolean isDinner) {

        List<DishProperty> listExtra = DishCache.getPropertyHolder().getDishPropertyByDishShopId(dishShop.getId());

        if (Utils.isNotEmpty(listExtra)) {
            for (DishProperty property : listExtra) {
                dishWrapper.addExtra(new OrderExtra(convertDishShop(property), null));
            }
        }


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
        dishShop.setUnitName("份");        return dishShop;
    }


    public EventDishPropertiesNotice selectStandard(Set<DishProperty> standards, boolean refundMode) {
        checkLoad();
        selectedProperties.clear();
        selectedExtras.clear();
        return postEventByBasic(null, standards, refundMode);
    }


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

        Set<DishProperty> usableStandards = new HashSet<DishProperty>(standards);        Map<DishProperty, ClearStatus> clearStatusMap = new HashMap<DishProperty, ClearStatus>();
        int size = propertiesWrapperArray.size();
        if (standards.isEmpty()) {
            for (int i = 0; i < size; i++) {
                DishPropertiesWrapper wrapper = propertiesWrapperArray.valueAt(i);
                                usableStandards.addAll(wrapper.dish.getStandards());
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
                if (v == -1) {                      if (wrapper.dish.isClear()) {
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
                                        if (v == 0) {
                        postWrapper = wrapper;
                    }

                                        usableStandards.addAll(wrapper.dish.getStandards());

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


    private EventDishPropertiesNotice postEvent(List<PropertyGroupVo<DishStandardVo>> standardGroupList, DishPropertiesWrapper postWrapper) {
                Collections.sort(standardGroupList, newGroupVoComparator());
        DishPropertiesVo vo = new DishPropertiesVo();
        vo.setStandardGroupList(standardGroupList);
        if (postWrapper != null) {
                        if (selectedOrderDish.getDishShop().equals(postWrapper.dish.getDishShop())) {
                vo.setOrderDish(selectedOrderDish);
            } else {
                                OrderDish orderShop = postWrapper.dish.toOrderDish();
                orderShop.setQty(selectedOrderDish.getSingleQty(), selectedOrderDish.getTotalQty());
                vo.setOrderDish(orderShop);
            }
            Log.i(TAG, "vo.dishShop=" + vo.getOrderDish().getBrandDishId());
                        vo.setPropertyGroupList(new ArrayList<PropertyGroupVo<DishPropertyVo>>());
            if (postWrapper.propertyGroupMap != null) {
                for (PropertyGroupWrapper wrapper : postWrapper.propertyGroupMap.values()) {
                    PropertyGroupVo<DishPropertyVo> groupVo = new PropertyGroupVo<DishPropertyVo>(
                            wrapper.propertyType, new ArrayList<DishPropertyVo>());
                    for (DishProperty property : wrapper.properties) {
                                                String key = toPropertyKey(postWrapper.dish.getDishShop(), property);
                        boolean selected = selectedProperties.contains(key);
                        groupVo.getPropertyList().add(new DishPropertyVo(property, selected));
                    }
                    vo.getPropertyGroupList().add(groupVo);
                }
            }
            Collections.sort(vo.getPropertyGroupList(), newGroupVoComparator());
                        vo.setExtraList(new ArrayList<OrderExtra>());
            if (postWrapper.extraMap != null) {
                for (OrderSetmeal extra : postWrapper.extraMap.values()) {
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


    public static class DishPropertyFilter implements DataFilter<DishBrandProperty> {

        private final DishShop dishShop;

        public DishPropertyFilter(DishShop dishShop) {
            this.dishShop = dishShop;
        }

        @Override
        public boolean accept(DishBrandProperty entity) {
            if (dishShop.getHasStandard() == Bool.YES) {
                                if (dishShop.getName().equals(entity.getDishName())) {
                    DishShop tmp = DishCache.getDishHolder().get(entity.getDishId());
                    return tmp != null
                            && tmp.getHasStandard() == Bool.YES
                            && tmp.getDishTypeId().equals(dishShop.getDishTypeId());
                }
                return false;
            } else {
                                return dishShop.getBrandDishId().equals(entity.getDishId());
            }
        }

    }


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


    static class PropertyGroupWrapper {

        final DishPropertyType propertyType;

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


    private static class DishPropertiesWrapper {

        private final DishAndStandards dish;

        private final Map<Long, PropertyGroupWrapper> propertyGroupMap;

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
            extraMap.put(extra.getBrandDishId(), extra);
        }

        boolean hasExtra(DishSetmeal dishExtra) {
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
