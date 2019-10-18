package com.zhongmei.bty.basemodule.orderdish.manager;

import android.support.v4.util.LongSparseArray;
import android.util.Log;

import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache.DataFilter;
import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealGroupVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealVo;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderSetmeal;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishSetmeal;
import com.zhongmei.yunfu.db.entity.dish.DishSetmealGroup;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.orderdish.entity.DishUnitDictionary;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.bty.basemodule.orderdish.event.EventSetmealGroupsNotice;
import com.zhongmei.bty.basemodule.orderdish.event.EventSetmealModifyNotice;
import com.zhongmei.bty.basemodule.orderdish.event.EventSetmealsNotice;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;

;


public class DishSetmealManager {

    private static final String TAG = DishSetmealManager.class.getSimpleName();

    private final ShopcartItem shopcartItem;
    private final Map<Long, SetmealGroupWrapper> groupWrapperMap;
    private final List<DishSetmealVo> selectedList;
    private final List<DishSetmealVo> requisiteClearList;    private boolean requisiteDeleted;    private boolean loaded;


    public DishSetmealManager(ShopcartItem shopcartItem) {
        this.shopcartItem = shopcartItem;
        groupWrapperMap = new LinkedHashMap<Long, SetmealGroupWrapper>();
        selectedList = new ArrayList<DishSetmealVo>();
        requisiteClearList = new ArrayList<DishSetmealVo>();
        requisiteDeleted = false;
        loaded = false;
    }


    public boolean isValid() {
        checkLoad();
        for (SetmealGroupWrapper groupWrapper : groupWrapperMap.values()) {
            if (!groupWrapper.isValid()) {
                return false;
            }
        }

                if (requisiteDeleted) {
            return false;
        }

        return true;
    }


    public boolean isRequisiteDeleted() {
        return requisiteDeleted;
    }

    public boolean isRequisiteClearListEmpty() {
        return requisiteClearList.isEmpty();
    }

    public void removeRequisiteClearListItem(DishSetmealVo dishSetmealVo) {
        if (requisiteClearList.contains(dishSetmealVo)) {
            requisiteClearList.remove(dishSetmealVo);
        }
    }

    public void addRequisiteClearListItem(DishSetmealVo dishSetmealVo) {
        boolean isAdd = false;

                DishSetmeal setmeal = dishSetmealVo.getSetmeal();
        SetmealGroupWrapper setmealGroupWrapper = groupWrapperMap.get(setmeal.getComboDishTypeId());
        SetmealWrapper setmealWrapper = setmealGroupWrapper.getSetmealWrapper(setmeal.getId());
        if (!setmealWrapper.isValid()) {
            isAdd = true;
        }

        if (isAdd && !requisiteClearList.contains(dishSetmealVo)) {
            requisiteClearList.add(dishSetmealVo);
        }
    }


    public void loadData() {
        List<DishSetmealVo> selecteds = null;
        boolean loadState = true;
        if (!loaded) {
                        loadState = doLoadData();
            selecteds = selectedList;
        }
        if (loadState) {
            List<DishSetmealGroupVo> groupVoList = new ArrayList<DishSetmealGroupVo>();
            for (SetmealGroupWrapper groupWrapper : groupWrapperMap.values()) {
                DishSetmealGroupVo groupVo = new DishSetmealGroupVo(groupWrapper.setmealGroup);
                groupVo.setSelectedQty(groupWrapper.groupCount);
                groupVoList.add(groupVo);
            }
            EventBus.getDefault().post(new EventSetmealGroupsNotice(shopcartItem.getUuid(),
                    groupVoList, selecteds));
        } else {
            EventBus.getDefault().post(new EventSetmealGroupsNotice(shopcartItem.getUuid()));
        }
    }


    public boolean doLoadData() {
        if (loaded) {
            throw new RuntimeException("This manager is already loaded!");
        }
        loaded = true;
        groupWrapperMap.clear();

                SetmealGroupFilter groupFilter = new SetmealGroupFilter(shopcartItem.getOrderDish().getDishShop());
        List<DishSetmealGroup> groupList = DishCache.getSetmealGroupHolder().filter(groupFilter);
        Log.i(TAG, "groupList.size()=" + groupList.size());
        for (DishSetmealGroup group : groupList) {
            SetmealGroupWrapper groupWrapper = new SetmealGroupWrapper(group);
            groupWrapperMap.put(group.getId(), groupWrapper);
        }

        boolean hasItems = Utils.isNotEmpty(shopcartItem.getSetmealItems());

                SetmealFilter setmealFilter = new SetmealFilter(groupFilter.dishShop);
        List<DishSetmeal> setmealList = DishCache.getSetmealHolder().filter(setmealFilter);
        Log.i(TAG, "setmealList.size()=" + setmealList.size());
        for (DishSetmeal setmeal : setmealList) {
            DishShop dishShop = DishCache.getDishHolder().get(setmeal.getChildDishId());
            if (dishShop != null) {
                Log.i(TAG, "SETMEAL: DishSetmeal.id=" + setmeal.getId() + ", DishShop.name=" + dishShop.getName());
                Long groupId = setmeal.getComboDishTypeId();
                SetmealGroupWrapper groupWrapper = groupWrapperMap.get(groupId);
                if (groupWrapper == null) {
                    Log.w(TAG, "无此套餐明细分组！ comboDishTypeId=" + groupId);
                    return false;
                }
                SetmealWrapper setmealWrapper = groupWrapper.getSetmealWrapper(setmeal.getId());
                if (setmealWrapper == null) {
                    DishUnitDictionary unit = DishCache.getUnitHolder().get(dishShop.getUnitId());
                    setmealWrapper = new SetmealWrapper(dishShop, setmeal, unit);

                                        Set<DishProperty> standards = DishManager.filterStandards(dishShop);
                    Log.i(TAG, "standards.size()=" + standards.size());
                    setmealWrapper.setStandards(standards);

                    groupWrapper.addSetmeal(setmealWrapper);
                }

                if (!hasItems) {
                                        if (setmealWrapper.isRequisite() || setmealWrapper.isDefault()) {
                        if (setmealWrapper.isClear()) {
                            if (setmealWrapper.isRequisite()) {
                                requisiteClearList.add(setmealWrapper.toDishSetmealVo());
                            }
                        } else {
                            BigDecimal qty = setmealWrapper.setmeal.getLeastCellNum();
                            if (qty == null) {
                                qty = BigDecimal.ONE;
                            }
                            DishSetmealVo setmealVo = setmealWrapper.toDishSetmealVo();
                            setmealVo.setSelectedQty(qty);
                            selectedList.add(setmealVo);
                        }
                    }
                }
            } else {
                Log.w(TAG, "无此套餐明细！ childDishId=" + setmeal.getChildDishId());
                if (setmeal.getIsReplace() == Bool.YES) {
                    requisiteDeleted = true;
                }
            }
        }
        if (hasItems) {
            for (SetmealShopcartItem item : shopcartItem.getSetmealItems()) {
                OrderSetmeal orderSetmeal = item.getOrderDish();
                Long groupId = orderSetmeal.getSetmeal().getComboDishTypeId();
                SetmealGroupWrapper groupWrapper = groupWrapperMap.get(groupId);
                ModifyResult result = groupWrapper.modifyQty(item, item.getSingleQty());
                if (result != ModifyResult.SUCCESSFUL) {
                    return false;
                }
            }
        }
        return true;
    }


    public void switchGroup(DishSetmealGroupVo groupVo) {
        checkLoad();
        Long groupId = groupVo.getSetmealGroup().getId();
        SetmealGroupWrapper groupWrapper = groupWrapperMap.get(groupId);
        List<DishSetmealVo> voList = new ArrayList<DishSetmealVo>();
        int size = groupWrapper.setmealWrapperArray.size();
        for (int i = 0; i < size; i++) {
            SetmealWrapper setmealWrapper = groupWrapper.setmealWrapperArray.valueAt(i);
            DishSetmealVo setmealVo = setmealWrapper.toDishSetmealVo();
            voList.add(setmealVo);
        }
        Log.i(TAG, "voList.size()=" + voList.size() + ", groupName=" + groupVo.getSetmealGroup().getName());
        EventBus.getDefault().post(new EventSetmealsNotice(shopcartItem.getUuid(), groupVo, voList));
    }


    public DishSetmealGroupVo getGroupBySubDishUuid(String subDishUuid) {
        for (SetmealGroupWrapper setmealGroupWrapper : groupWrapperMap.values()) {
            for (int i = 0; i < setmealGroupWrapper.setmealWrapperArray.size(); i++) {
                if (setmealGroupWrapper.setmealWrapperArray.valueAt(i).dishShop.getUuid().equals(subDishUuid)) {

                    return new DishSetmealGroupVo(setmealGroupWrapper.setmealGroup);
                }
            }
        }

        return null;
    }


    public void addDefaultSelected(SetmealShopcartItem setmealItem) {
        modifySetmeal(setmealItem, setmealItem.getSingleQty());
    }


    public ModifyResult modifySetmeal(SetmealShopcartItem setmealItem, BigDecimal qty) {
        checkLoad();
        Log.i(TAG, ">>modifySetmeal(" + setmealItem.getSkuName() + ", " + qty + ")");
        ModifyResult result = ModifyResult.FAILED_NOT_FOUND;
        DishSetmeal setmeal = setmealItem.getOrderDish().getSetmeal();
        Long groupId = setmeal.getComboDishTypeId();
        SetmealGroupWrapper groupWrapper = groupWrapperMap.get(groupId);
        if (groupWrapper != null) {
            result = groupWrapper.modifyQty(setmealItem, qty);
        }
        Log.i(TAG, "##modifySetmeal(" + setmealItem.getSkuName() + ", " + qty + "): " + result);
        if (result != ModifyResult.SUCCESSFUL) {
            EventBus.getDefault().post(new EventSetmealModifyNotice(shopcartItem.getUuid(), result));
        } else {
            DishSetmealGroupVo groupVo = new DishSetmealGroupVo(groupWrapper.setmealGroup);
            groupVo.setSelectedQty(groupWrapper.groupCount);
            DishSetmealVo setmealVo = groupWrapper.getSetmealWrapper(setmeal.getId()).toDishSetmealVo();
            EventBus.getDefault().post(new EventSetmealModifyNotice(shopcartItem.getUuid(), groupVo, setmealVo));
        }
        return result;
    }


    public ModifyResult testModify(SetmealShopcartItem setmealItem, BigDecimal newQty) {
        checkLoad();
        Log.i(TAG, ">>testModify(" + setmealItem.getSkuName() + ", " + newQty + ")");
        Long groupId = setmealItem.getSetmealGroupId();
        SetmealGroupWrapper groupWrapper = groupWrapperMap.get(groupId);
        ModifyResult result = ModifyResult.FAILED_NOT_FOUND;
        if (groupWrapper != null) {
            TestModifyResult testResult = groupWrapper.testModifyQty(setmealItem, newQty);
            result = testResult.modifyResult;
        }
        Log.i(TAG, "##testModify(" + setmealItem.getSkuName() + ", " + newQty + "): " + result);
        return result;
    }

    private void checkLoad() {
        if (!loaded) {
            throw new RuntimeException("The manager is not load!");
        }
    }


    private static class SetmealGroupFilter implements DataFilter<DishSetmealGroup> {

        private DishShop dishShop;

        SetmealGroupFilter(DishShop dishShop) {
            this.dishShop = dishShop;
        }

        @Override
        public boolean accept(DishSetmealGroup entity) {
            return entity.getSetmealDishId().equals(dishShop.getBrandDishId());
        }
    }


    private static class SetmealFilter implements DataFilter<DishSetmeal> {

        private DishShop dishShop;

        SetmealFilter(DishShop dishShop) {
            this.dishShop = dishShop;
        }

        @Override
        public boolean accept(DishSetmeal entity) {
            return entity.getDishId().equals(dishShop.getBrandDishId());
        }

    }


    private static class SetmealWrapper {

        private final DishShop dishShop;
        private final DishSetmeal setmeal;
        private final DishUnitDictionary unit;
        private final Map<String, BigDecimal> qtyMap;
        private Set<DishProperty> standards;
        private BigDecimal totalQty;

        SetmealWrapper(DishShop dishShop, DishSetmeal setmeal, DishUnitDictionary unit) {
            this.dishShop = dishShop;
            this.setmeal = setmeal;
            this.unit = unit;
            standards = new LinkedHashSet<DishProperty>();
            qtyMap = new HashMap<String, BigDecimal>();
            totalQty = BigDecimal.ZERO;
        }


        BigDecimal getCount() {
            if (dishShop.getSaleType() == SaleType.WEIGHING) {
                return BigDecimal.valueOf(qtyMap.size());
            }
            return getTotalQty();
        }

        BigDecimal getQty(String uuid) {
            return qtyMap.get(uuid);
        }

        BigDecimal getTotalQty() {
            return totalQty;
        }

        void setQty(String uuid, BigDecimal qty) {
            BigDecimal oldQty = BigDecimal.ZERO;
            if (qty.compareTo(BigDecimal.ZERO) == 0) {
                oldQty = qtyMap.remove(uuid);
                if (oldQty != null) {
                    totalQty = totalQty.subtract(oldQty);
                }
            } else {
                oldQty = qtyMap.put(uuid, qty);
                totalQty = totalQty.add(qty);
                if (oldQty != null) {
                    totalQty = totalQty.subtract(oldQty);
                }
            }
        }

        Long getId() {
            return setmeal.getId();
        }

        void setStandards(Set<DishProperty> standards) {
            this.standards = standards;
        }


        boolean isRequisite() {
            return setmeal.getIsReplace() == Bool.YES;
        }


        boolean isDefault() {
            return setmeal.getIsDefault() == Bool.YES;
        }


        boolean isMulti() {
            return setmeal.getIsMulti() != Bool.YES;
        }


        boolean isClear() {
            return dishShop.getClearStatus() == ClearStatus.CLEAR;
        }

        boolean isUnweighing() {
            return dishShop.getSaleType() != SaleType.WEIGHING;
        }

        boolean isValid() {
            if (isRequisite()) {
                                BigDecimal num = setmeal.getLeastCellNum();
                if (num == null) {
                    num = BigDecimal.ONE;
                }
                for (BigDecimal value : qtyMap.values()) {
                    if (value.compareTo(num) >= 0) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }

        DishSetmealVo toDishSetmealVo() {
            DishSetmealVo setmealVo = new DishSetmealVo(dishShop, setmeal, standards, unit);
            setmealVo.setSelectedQty(totalQty);
            return setmealVo;
        }
    }


    private static class SetmealGroupWrapper {

        private final DishSetmealGroup setmealGroup;
        private final LongSparseArray<SetmealWrapper> setmealWrapperArray;
        private BigDecimal groupCount;

        SetmealGroupWrapper(DishSetmealGroup setmealGroup) {
            this.setmealGroup = setmealGroup;
            setmealWrapperArray = new LongSparseArray<SetmealWrapper>();
            groupCount = BigDecimal.ZERO;
        }

        void addSetmeal(SetmealWrapper setmealWrapper) {
            setmealWrapperArray.put(setmealWrapper.getId(), setmealWrapper);
            groupCount = groupCount.add(setmealWrapper.getCount());
        }

        SetmealWrapper getSetmealWrapper(Long setmealId) {
            return setmealWrapperArray.get(setmealId);
        }

        void modifyQty(SetmealWrapper setmealWrapper, String itemUuid, BigDecimal newQty) {
            BigDecimal oldCount = setmealWrapper.getCount();
            setmealWrapper.setQty(itemUuid, newQty);
            BigDecimal newCount = setmealWrapper.getCount();
            groupCount = groupCount.add(newCount).subtract(oldCount);
            Log.i(TAG, "groupCount=" + groupCount + ", qty=" + setmealWrapper.getTotalQty());
        }


        TestModifyResult testModifyQty(SetmealShopcartItem setmealItem, BigDecimal newQty) {
            SetmealWrapper wrapper = setmealWrapperArray.get(setmealItem.getSetmealId());

            if (wrapper == null) {
                return new TestModifyResult(ModifyResult.FAILED_NOT_FOUND, wrapper);
            }
            if (newQty.compareTo(BigDecimal.ZERO) < 0) {
                return new TestModifyResult(ModifyResult.FAILED_QTY_WRONG, wrapper);
            }

            BigDecimal leastCellNum = wrapper.setmeal.getLeastCellNum();
            if (leastCellNum == null) {
                leastCellNum = BigDecimal.ONE;
            }
                        if (newQty.compareTo(BigDecimal.ZERO) > 0 && newQty.compareTo(leastCellNum) < 0) {
                return new TestModifyResult(ModifyResult.FAILED_LESS_THAN_STEP, wrapper);
            }

            BigDecimal oldQty = MathDecimal.nullToZero(wrapper.getQty(setmealItem.getUuid()));
            BigDecimal oldTotalQty = wrapper.getTotalQty();
            BigDecimal oldCount = wrapper.getCount();
            BigDecimal newTotalQty = oldTotalQty.add(newQty).subtract(oldQty);
            BigDecimal newCount = oldCount;
            if (wrapper.isUnweighing()) {
                newCount = newCount.subtract(oldQty).add(newQty);
            } else if (oldQty.compareTo(BigDecimal.ZERO) == 0) {
                newCount = newCount.add(BigDecimal.ONE);
            }

                        if (wrapper.isRequisite() && newTotalQty.compareTo(BigDecimal.ZERO) <= 0) {
                return new TestModifyResult(ModifyResult.FAILED_REMOVE_REQUISITE, wrapper);
            }
            if (wrapper.isMulti()) {
                                                if (wrapper.isUnweighing()) {
                    if (newTotalQty.compareTo(leastCellNum) > 0) {
                        return new TestModifyResult(ModifyResult.FAILED_MULTI, wrapper);
                    }
                } else if (newCount.compareTo(BigDecimal.ONE) > 0) {
                    return new TestModifyResult(ModifyResult.FAILED_MULTI, wrapper);
                }
            }
                        BigDecimal newGroupCount = groupCount.add(newCount).subtract(oldCount);
            BigDecimal maxCount = setmealGroup.getOrderMax();
            if (maxCount != null && newGroupCount.compareTo(maxCount) > 0) {
                return new TestModifyResult(ModifyResult.FAILED_GREATER_THAN_MAX, wrapper);
            }
                        BigDecimal minCount = setmealGroup.getOrderMin();
            if (minCount != null && newGroupCount.compareTo(minCount) < 0) {
                return new TestModifyResult(ModifyResult.FAILED_LESS_THAN_MIN, wrapper);
            }

            return new TestModifyResult(ModifyResult.SUCCESSFUL, wrapper);
        }


        ModifyResult modifyQty(SetmealShopcartItem setmealItem, BigDecimal newQty) {
            TestModifyResult result = testModifyQty(setmealItem, newQty);
            switch (result.modifyResult) {
                case FAILED_QTY_WRONG:
                    throw new RuntimeException("The qty is wrong.(qty<0)");

                case SUCCESSFUL:
                case FAILED_LESS_THAN_MIN:
                    SetmealWrapper setmealWrapper = result.setmealWrapper;
                    modifyQty(setmealWrapper, setmealItem.getUuid(), newQty);
                    return ModifyResult.SUCCESSFUL;

                default:
                    return result.modifyResult;
            }
        }


        boolean isValid() {
            BigDecimal minQty = setmealGroup.getOrderMin();
            if (minQty != null && groupCount.compareTo(minQty) < 0) {
                return false;
            }
            BigDecimal maxQty = setmealGroup.getOrderMax();
            if (maxQty != null && groupCount.compareTo(maxQty) > 0) {
                return false;
            }
            for (int i = setmealWrapperArray.size() - 1; i >= 0; i--) {
                SetmealWrapper wrapper = setmealWrapperArray.valueAt(i);
                if (!wrapper.isValid()) {
                    return false;
                }
            }
            return true;
        }

    }


    private static class TestModifyResult {

        final ModifyResult modifyResult;
        final SetmealWrapper setmealWrapper;

        TestModifyResult(ModifyResult modifyResult, SetmealWrapper setmealWrapper) {
            this.modifyResult = modifyResult;
            this.setmealWrapper = setmealWrapper;
        }
    }


    public static enum ModifyResult {


        SUCCESSFUL,

        FAILED_NOT_FOUND,

        FAILED_QTY_WRONG,

        FAILED_REMOVE_REQUISITE,

        FAILED_LESS_THAN_STEP,

        FAILED_MULTI,

        FAILED_LESS_THAN_MIN,

        FAILED_GREATER_THAN_MAX;

    }

}
