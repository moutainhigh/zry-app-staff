package com.zhongmei.bty.cashier.orderdishmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v4.util.LongSparseArray;
import android.util.Log;

import com.zhongmei.bty.basemodule.orderdish.entity.DishBrandProperty;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishPropertyType;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.orderdish.entity.DishUnitDictionary;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.bty.cashier.orderdishmanager.DishPropertyManager.PropertyGroupWrapper;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache.DataFilter;
import com.zhongmei.bty.basemodule.orderdish.bean.DishAndStandards;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishStandardVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.bty.cashier.shoppingcart.vo.PropertyGroupVo;


@SuppressLint("UseSparseArrays")
public class DishClearStatusManager {

    private static final String TAG = DishClearStatusManager.class.getSimpleName();

    private final IViewer viewer;


    private final LongSparseArray<DishAndStandards> standardsArray;

    private boolean loaded;

    public DishClearStatusManager(IViewer viewer) {
        this.viewer = viewer;
        standardsArray = new LongSparseArray<DishAndStandards>();
    }


    public void loadData(DishVo dishVo) {
        if (loaded) {
            throw new RuntimeException("This manager is already loaded!");
        }
        loaded = true;

        DishShop dishShop = dishVo.getDishShop();
                LoadDataTask task = new LoadDataTask(dishShop);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }



    public void filter(ClearStatus clearStatus, Set<DishProperty> standards) {
        if (!loaded) {
            throw new RuntimeException("The manager is not load!");
        }

        List<DishAndStandards> dishList = new ArrayList<DishAndStandards>();
        for (int i = standardsArray.size() - 1; i >= 0; i--) {
            DishAndStandards dish = standardsArray.valueAt(i);
            if (standards != null && !standards.isEmpty() && dish.compareStandards(standards) == -1) {
                continue;
            }
            if (dish.getDishShop().getClearStatus() == clearStatus) {
                dishList.add(dish);
            }
        }
        viewer.onFilter(dishList);
    }


    private static class StandardFilter implements DataFilter<DishBrandProperty> {

        private final DishShop dishShop;

        StandardFilter(DishShop dishShop) {
            this.dishShop = dishShop;
        }

        @Override
        public boolean accept(DishBrandProperty entity) {
            if (entity.getPropertyKind() == PropertyKind.STANDARD) {
                if (dishShop.getName().equals(entity.getDishName())) {
                    DishShop tmp = DishCache.getDishHolder().get(entity.getDishId());
                    if (tmp != null) {
                        return dishShop.getDishTypeId().equals(tmp.getDishTypeId());
                    }
                }
            }
            return false;
        }

    }


    public static interface IViewer {

        void onLoadData(List<PropertyGroupVo<DishStandardVo>> standardGroupList);

        void onFilter(List<DishAndStandards> dishList);

    }

        class LoadDataTask extends AsyncTask<Void, Void, List<PropertyGroupVo<DishStandardVo>>> {
        DishShop dishShop;

        LoadDataTask(DishShop dishShop) {
            this.dishShop = dishShop;
        }

        @Override
        protected List<PropertyGroupVo<DishStandardVo>> doInBackground(Void... params) {
            StandardFilter filter = new StandardFilter(this.dishShop);
            List<DishBrandProperty> dishStandardList = DishCache.getDishPropertyHolder().filter(filter);
            Log.i(TAG, "dishStandardList.size()=" + dishStandardList.size());

            Map<Long, PropertyGroupWrapper> standardGroupMap = new HashMap<Long, PropertyGroupWrapper>();
            for (DishBrandProperty dishProperty : dishStandardList) {
                                DishShop tmpDish = DishCache.getDishHolder().get(dishProperty.getDishId());
                if (tmpDish == null) {
                    Log.w(TAG, "无此商品！ dishId=" + dishProperty.getDishId());
                    continue;
                }
                DishUnitDictionary unit = DishCache.getUnitHolder().get(tmpDish.getUnitId());
                DishAndStandards dish = standardsArray.get(dishProperty.getDishId());
                if (dish == null) {
                    dish = new DishAndStandards(tmpDish, unit);
                    standardsArray.put(tmpDish.getBrandDishId(), dish);
                }

                                DishProperty property = DishCache.getPropertyHolder().get(dishProperty.getPropertyId());
                if (property != null) {
                    Log.i(TAG, "STANDARD: " + property.getName());
                    Long typeId = property.getPropertyTypeId();
                    PropertyGroupWrapper groupVo = standardGroupMap.get(typeId);
                    if (groupVo == null) {
                        DishPropertyType propertyType = DishCache.getPropertyTypeHolder().get(typeId);
                        if (propertyType == null) {
                            Log.w(TAG, "没有属性分类！property.id=" + property.getId());
                            continue;
                        } else {
                            groupVo = new PropertyGroupWrapper(propertyType);
                            standardGroupMap.put(typeId, groupVo);
                        }
                    }
                    groupVo.addProperty(property);
                    if (dishProperty.getDishId().equals(dish.getBrandDishId())) {
                        dish.addStandard(property);
                    }
                } else {
                    Log.w(TAG, "无此属性！ propertyId=" + dishProperty.getPropertyId());
                }
            }

            List<PropertyGroupVo<DishStandardVo>> standardGroupList = new ArrayList<PropertyGroupVo<DishStandardVo>>();
            for (PropertyGroupWrapper wrapper : standardGroupMap.values()) {
                PropertyGroupVo<DishStandardVo> groupVo = new PropertyGroupVo<DishStandardVo>(
                        wrapper.propertyType, new ArrayList<DishStandardVo>());
                for (DishProperty property : wrapper.properties) {
                    groupVo.getPropertyList().add(new DishStandardVo(property, false, DishStandardVo.ENABLE));
                }
                standardGroupList.add(groupVo);
            }
            Collections.sort(standardGroupList, DishPropertyManager.newGroupVoComparator());
            return standardGroupList;
        }

        @Override
        protected void onPostExecute(List<PropertyGroupVo<DishStandardVo>> standardGroupList) {
            if (standardGroupList != null && viewer != null) {
                viewer.onLoadData(standardGroupList);
            }
        }
    }
}
