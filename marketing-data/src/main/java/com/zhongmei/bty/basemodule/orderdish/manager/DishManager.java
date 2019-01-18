package com.zhongmei.bty.basemodule.orderdish.manager;

import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.data.R;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryInfo;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryCacheUtil;
import com.zhongmei.bty.basemodule.log.RLog;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache.DataFilter;
import com.zhongmei.bty.basemodule.orderdish.bean.DishBrandTypes;
import com.zhongmei.bty.basemodule.orderdish.bean.DishInfo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.bty.basemodule.orderdish.entity.DishBrandProperty;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.orderdish.entity.DishUnitDictionary;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.bty.basemodule.orderdish.event.EventDishChangedNotice;
import com.zhongmei.yunfu.util.Checks;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.context.util.helper.SpHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;

/**
 * 点菜管理
 *
 * @version: 1.0
 * @date 2015年7月7日
 */
public class DishManager {

    private static final String TAG = DishManager.class.getSimpleName();

    //搜索类型 0=所有   1=首字母  2=价格 3=编码 4=菜名
    public static final int ALL = 0;

    public static final int FIRST_LETTER = 1;

    public static final int PRICE = 2;

    public static final int CODE = 3;

    public static final int DISHNAME = 4;

    public static final int BARCODE = 5;

    public DishManager() {
        initShowValue(loadData().dishTypeList);  //初始化设备选择的中类
    }

    /**
     * 加载数据。将使用通知所有菜品分类列表
     */
    public DishBrandTypes loadData() {
        /*
        Collection<DishBrandType> dishTypes = DishCache.getDishTypeHolder().getAll();
        Log.i(TAG, "dishTypes.size()=" + dishTypes.size());
        List<DishBrandType> dishTypeList = new ArrayList<DishBrandType>(dishTypes);
        */
        final DishCache.DishHolder dishHolder = DishCache.getDishHolder();
        Collection<DishBrandType> dishTypes = DishCache.getDishTypeHolder().filter(new DataFilter<DishBrandType>() {
            @Override
            public boolean accept(DishBrandType entity) {
                return dishHolder.containsTypeId(entity.getId());
            }
        });
        RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存中获取的[商品中类]数量为: " + dishTypes.size() + " position: DishManager -> loadData()");
        if (Utils.isEmpty(dishTypes)) {
            RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存中发现[商品DishHolder]商品" + dishHolder.getAll() + " position: DishManager -> loadData()");
            RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存中发现[商品中类]数量为0,下一步开启线程进入实际数据库中查询" + " position: DishManager -> loadData()");
            DishLogManager.queryDishBrandTypeFromDb();
        }
        RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存中获取的[商品中类]名称为: " + dishTypes + " position: DishManager -> loadData()");
        List<DishBrandType> dishTypeList = new ArrayList<DishBrandType>(dishTypes);
        return new DishBrandTypes(dishTypeList);
    }

    private void initShowValue(List<DishBrandType> dishTypeList) {
        Set<String> unselectUUids = SharedPreferenceUtil.getSpUtil().getStringSet("unselect_dish_types", null);
        if (unselectUUids == null)
            return;
        Map<String, DishBrandType> map = new HashMap<>();
        for (DishBrandType type : dishTypeList) {
            type.setShow(Bool.YES.value());
            map.put(type.getUuid(), type);
        }

        for (String uuid : unselectUUids) {
            if (map.get(uuid) != null)
                map.get(uuid).setShow(Bool.NO.value());
        }
    }

    /**
     * 获取菜品大类
     *
     * @return
     */
    public DishBrandTypes getSupperDishTypes() {
        Collection<DishBrandType> dishTypes = DishCache.getDishTypeHolder().filter(new DataFilter<DishBrandType>() {
            @Override
            public boolean accept(DishBrandType entity) {
                return entity.getParentId() == null && !getSecondBrandTypes(entity).isEmpty();
            }
        });
        RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存中获取的[商品大类]数量为: " + dishTypes.size() + " position: DishManager -> getSupperDishTypes()");
        List<DishBrandType> dishTypeList = new ArrayList<DishBrandType>(dishTypes);
        DishBrandTypes dishBrandTypes = new DishBrandTypes(dishTypeList);
        return dishBrandTypes;
    }

    private Collection<DishBrandType> getSecondBrandTypes(final DishBrandType type) {
        final DishCache.DishHolder dishHolder = DishCache.getDishHolder();
        Collection<DishBrandType> dishTypes = DishCache.getDishTypeHolder().filter(new DataFilter<DishBrandType>() {
            @Override
            public boolean accept(DishBrandType entity) {
                return entity.getParentId() != null
                        && entity.getParentId().longValue() == type.getId().longValue()
                        && dishHolder.containsTypeId(entity.getId())
                        && entity.isShow() == Bool.YES.value();
            }
        });
        RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存中获取的[商品大类]下中类数量为: " + dishTypes.size() + " position: DishManager -> getSecondBrandTypes()");
        return dishTypes;
    }

    /**
     * 获取大类下中类
     *
     * @param type
     * @return
     */
    public DishBrandTypes getSecondDishTypes(final DishBrandType type) {
        Collection<DishBrandType> dishTypes = getSecondBrandTypes(type);
        List<DishBrandType> dishTypeList = new ArrayList<DishBrandType>(dishTypes);
        if (dishTypeList != null && !dishTypeList.isEmpty()) {
            DishBrandType allType = new DishBrandType();
            allType.setId(-1L);
            allType.setName(BaseApplication.sInstance.getString(R.string.dinner_dish_type_all));
            allType.setParentId(type.getId());
            dishTypeList.add(0, allType);
        }
        RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存中获取的[商品大类]下中类数量为: " + dishTypes.size() + " position: DishManager -> getSecondDishTypes()");
        DishBrandTypes dishBrandTypes = new DishBrandTypes(dishTypeList);
        return dishBrandTypes;
    }


    /**
     * 切换菜品分类。将使用{@link }通知该分类下的菜品列表
     *
     * @param dishType
     */
    public DishInfo switchType(final DishBrandType dishType) {
        /*DataFilter<DishShop> filter = new DataFilter<DishShop>() {

			@Override
			public boolean accept(DishShop entity) {
				// 排除不能单点的
				if (entity.getIsSingle() != Bool.YES) {
					return false;
				}
				return dishType.getId().equals(entity.getDishTypeId());
			}
			
		};
		List<DishShop> dishList = DishCache.getDishHolder().filter(filter);*/
        List<DishShop> dishList = null;
        //大类下全部菜品
        if (dishType.getId() == -2L) {//以前是-1，-1现在被服务所占用了
            Collection<DishBrandType> twoDishTypes = DishCache.getDishTypeHolder().filter(new DataFilter<DishBrandType>() {
                @Override
                public boolean accept(DishBrandType entity) {
                    return entity.getParentId() != null && entity.getParentId().longValue() == dishType.getParentId().longValue();
                }
            });
            List<DishShop> tmpDishList = null;
            dishList = new ArrayList<>();
            for (DishBrandType type : twoDishTypes) {
                tmpDishList = DishCache.getDishHolder().getDishShopByType(type.getId());
                if (tmpDishList != null && !tmpDishList.isEmpty())
                    dishList.addAll(tmpDishList);
            }
        } else {
            dishList = DishCache.getDishHolder().getDishShopByType(dishType.getId());
        }
        RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存查询结果,[中类名称]为: " + dishType.getName() + " position: DishManager -> switchType()");
        RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存查询结果,该中类下查询的商品个数为: " + dishList.size() + " position: DishManager -> switchType()");
        if (Utils.isEmpty(dishList)) {
            RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存查询结果,中类下商品(dishSop)数量为0,即将开启线程进入实际数据库中查询" + " position: DishManager -> switchType()");
            DishLogManager.queryDishFromDb();
        }
        Log.i(TAG, "dishList.size()=" + dishList.size() + ", typeName=" + dishType.getName());
        List<DishVo> dishsVo = getDishsVo(dishList);
        DishInfo dishInfo = new DishInfo(dishType, dishsVo, false);
        selectedList = dishList;
        return dishInfo;
    }

    /**
     * 切换菜品分类。
     *
     * @param dishType
     */
    public DishInfo switchType2(final DishBrandType dishType) {
        List<DishShop> dishList = null;
        //大类下全部菜品
        if (dishType.getId() == -1L) {
            Collection<DishBrandType> twoDishTypes = DishCache.getDishTypeHolder().filter(new DataFilter<DishBrandType>() {
                @Override
                public boolean accept(DishBrandType entity) {
                    return entity.getParentId() != null && entity.getParentId().longValue() == dishType.getParentId().longValue();
                }
            });
            List<DishShop> tmpDishList = null;
            dishList = new ArrayList<>();
            for (DishBrandType type : twoDishTypes) {
                tmpDishList = DishCache.getDishHolder().getDishShopByType(type.getId());
                if (tmpDishList != null && !tmpDishList.isEmpty())
                    dishList.addAll(tmpDishList);
            }
        } else {
            dishList = DishCache.getDishHolder().getDishShopByType(dishType.getId());
        }
        RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存查询结果,中类名称为: " + dishType.getName() + " position: DishManager -> switchType2()");
        RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存查询结果,该中类下查询的商品个数为: " + dishList.size() + " position: DishManager -> switchType2()");
        if (Utils.isEmpty(dishList)) {
            RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存查询结果,中类下商品(dishSop)数量为0,即将开启线程进入实际数据库中查询" + " position: DishManager -> switchType2()");
            DishLogManager.queryDishFromDb();
        }
        Log.i(TAG, "dishList.size()=" + dishList.size() + ", typeName=" + dishType.getName());
        List<DishVo> dishsVo = getDishsVo(dishList);
        DishInfo dishInfo = new DishInfo(dishType, dishsVo, false);
        selectedList = dishList;
        return dishInfo;
    }

    /**
     * 切换菜品分类。
     *
     * @param dishType
     */
    public DishInfo switchType3(final DishBrandType dishType) {
        List<DishShop> dishList = null;
        //大类下全部菜品
        if (dishType == null || dishType.getId() == -1L) {
            Collection<DishBrandType> twoDishTypes = DishCache.getDishTypeHolder().filter(new DataFilter<DishBrandType>() {
                @Override
                public boolean accept(DishBrandType entity) {
                    return true;
                }
            });
            List<DishShop> tmpDishList = null;
            dishList = new ArrayList<>();
            for (DishBrandType type : twoDishTypes) {
                tmpDishList = DishCache.getDishHolder().getPrintDishShopByType(type.getId());
                if (tmpDishList != null && !tmpDishList.isEmpty())
                    dishList.addAll(tmpDishList);
            }
        } else {
            dishList = DishCache.getDishHolder().getPrintDishShopByType(dishType.getId());
        }
        RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存查询结果,中类名称为: " + dishType.getName() + " position: DishManager -> switchType3()");
        RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存查询结果,该中类下查询的商品个数为: " + dishList.size() + " position: DishManager -> switchType3()");
        if (Utils.isEmpty(dishList)) {
            RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存查询结果,中类下商品(dishSop)数量为0,即将开启线程进入实际数据库中查询" + " position: DishManager -> switchType3()");
            DishLogManager.queryDishFromDb();
        }
        List<DishVo> dishsVo = getDishsVo1(dishList);
        DishInfo dishInfo = new DishInfo(dishType, dishsVo, false);
        selectedList = dishList;
        return dishInfo;
    }

    /**
     * 切换菜品分类。
     * 不同的规格当成不同的菜，并且过滤称重商品和套餐
     *
     * @param dishType
     */
    public DishInfo switchTypeFilter(final DishBrandType dishType) {
        List<DishShop> dishList = null;
        //大类下全部菜品
        if (dishType == null || dishType.getId() == -1L) {
            Collection<DishBrandType> twoDishTypes = DishCache.getDishTypeHolder().filter(new DataFilter<DishBrandType>() {
                @Override
                public boolean accept(DishBrandType entity) {
                    return true;
                }
            });
            List<DishShop> tmpDishList = null;
            dishList = new ArrayList<>();
            for (DishBrandType type : twoDishTypes) {
                tmpDishList = DishCache.getDishHolder().getPrintDishShopByType(type.getId());
                if (tmpDishList != null && !tmpDishList.isEmpty())
                    dishList.addAll(tmpDishList);
            }
        } else {
            dishList = DishCache.getDishHolder().getBindDishShopByType(dishType.getId());
        }
        RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存查询结果,中类名称为: " + dishType.getName() + " position: DishManager -> switchType3()");
        RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存查询结果,该中类下查询的商品个数为: " + dishList.size() + " position: DishManager -> switchType3()");
        if (Utils.isEmpty(dishList)) {
            RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存查询结果,中类下商品(dishSop)数量为0,即将开启线程进入实际数据库中查询" + " position: DishManager -> switchType3()");
            DishLogManager.queryDishFromDb();
        }
        List<DishVo> dishsVo = getDishsVo1(dishList);
        DishInfo dishInfo = new DishInfo(dishType, dishsVo, false);
        selectedList = dishList;
        return dishInfo;
    }

    /**
     * 自助获取菜品大类
     *
     * @return
     */
    public DishBrandTypes getSupperDishTypes(final boolean single, final String carteUuid) {
        //final DishCache.DishCarteDetailHolder dishCarteDetailHolder  = DishCache.getDishCarteDetailHolder();
        Collection<DishBrandType> dishTypes = DishCache.getDishTypeHolder().filter(new DataFilter<DishBrandType>() {
            @Override
            public boolean accept(DishBrandType entity) {
                return entity.getParentId() == null && Utils.isNotEmpty(getSecondBrandTypes(single, carteUuid, entity));
            }
        });
        RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存查询结果,[自助获取菜品大类]个数为: " + dishTypes.size() + " position: DishManager -> getSupperDishTypes()");
        List<DishBrandType> dishTypeList = new ArrayList<DishBrandType>(dishTypes);
        DishBrandTypes dishBrandTypes = new DishBrandTypes(dishTypeList);
        return dishBrandTypes;
    }

    /**
     * 自助获取大类下中类
     *
     * @param type
     * @return
     */
    public DishBrandTypes getSecondDishTypes(final boolean single, String carteUuid, final DishBrandType type) {

        Collection<DishBrandType> dishTypes = getSecondBrandTypes(single, carteUuid, type);
        List<DishBrandType> dishTypeList = new ArrayList<DishBrandType>(dishTypes);
        if (dishTypeList != null && !dishTypeList.isEmpty()) {
            DishBrandType allType = new DishBrandType();
            allType.setId(-1L);
            allType.setName(BaseApplication.sInstance.getString(R.string.dinner_dish_type_all));
            allType.setParentId(type.getId());
            dishTypeList.add(0, allType);
        }
        RLog.i(RLog.DISH_KEY_TAG, "[DishCache]缓存查询结果,[自助获取大类下中类]个数为: " + dishTypes.size() + " position: DishManager -> getSecondDishTypes()");
        DishBrandTypes dishBrandTypes = new DishBrandTypes(dishTypeList);
        return dishBrandTypes;
    }


    private Collection<DishBrandType> getSecondBrandTypes(boolean single, final String carteUuid, final DishBrandType type) {

        Collection<DishBrandType> dishTypes = null;
        if (single) {
            dishTypes = DishCache.getDishTypeHolder().filter(new DataFilter<DishBrandType>() {
                @Override
                public boolean accept(DishBrandType entity) {
                    return entity.getParentId() != null
                            && entity.getParentId().longValue() == type.getId().longValue()
                            && Utils.isNotEmpty(buffetGetSingleDish(carteUuid, entity));
                }
            });
        } else {
            final DishCache.DishCarteDetailHolder dishCarteDetailHolder = DishCache.getDishCarteDetailHolder();
            dishTypes = DishCache.getDishTypeHolder().filter(new DataFilter<DishBrandType>() {
                @Override
                public boolean accept(DishBrandType entity) {
                    return entity.getParentId() != null
                            && entity.getParentId().longValue() == type.getId().longValue()
                            && Utils.isNotEmpty(dishCarteDetailHolder.getDishShopsByType(carteUuid, entity.getId()));
                }
            });
        }
        return dishTypes;
    }


    private List<DishShop> buffetGetSingleDish(String carteUuid, DishBrandType dishType) {
        Map<String, DishShop> carteMap = DishCache.getDishCarteDetailHolder().getDishShopMapByType(carteUuid, dishType.getId());
        List<DishShop> dishShopList = DishCache.getDishHolder().getDishShopByType(dishType.getId());
        if (carteMap == null || carteMap.isEmpty())
            return dishShopList;
        List<DishShop> dishList = new ArrayList<>();
        for (DishShop dishShop : dishShopList) {
            if (carteMap.get(dishShop.getUuid()) == null)
                dishList.add(dishShop);
        }
        return dishList;
    }


    /**
     * 自助餐菜品数据获取
     */
    public DishInfo buffetSwitchTypes(boolean single, String carteUuid, final DishBrandType dishType) {
        List<DishShop> dishList = null;
        DishCache.DishCarteDetailHolder dishCarteDetailHolder = DishCache.getDishCarteDetailHolder();
        if (dishType.getId() == -1L) {
            Collection<DishBrandType> twoDishTypes = DishCache.getDishTypeHolder().filter(new DataFilter<DishBrandType>() {
                @Override
                public boolean accept(DishBrandType entity) {
                    return entity.getParentId() != null && entity.getParentId().longValue() == dishType.getParentId().longValue();
                }
            });
            List<DishShop> tmpDishList = null;
            dishList = new ArrayList<>();
            for (DishBrandType type : twoDishTypes) {
                if (single)
                    tmpDishList = buffetGetSingleDish(carteUuid, type);
                else
                    tmpDishList = dishCarteDetailHolder.getDishShopsByType(carteUuid, type.getId());
                if (tmpDishList != null && !tmpDishList.isEmpty())
                    dishList.addAll(tmpDishList);
            }
        } else {
            if (single)
                dishList = buffetGetSingleDish(carteUuid, dishType);
            else
                dishList = dishCarteDetailHolder.getDishShopsByType(carteUuid, dishType.getId());
        }
        List<DishVo> dishsVo = getDishsVo(dishList);
        DishInfo dishInfo = new DishInfo(dishType, dishsVo, false);
        selectedList = dishList;
        return dishInfo;
    }

    //##########################################################################
    // 监听数据库中DishShop的变化

    private List<DishShop> selectedList;
    private DishCache.OnDataChangeListener onDataChangelistener;

    /**
     * 注册监听数据库中DishShop的变化(如剩余可售数量)，使用{@link EventDishChangedNotice}通知变化。
     * 注意！在不用时要调用{@link #unregisterObserver()}取消注册以便回收资源。
     */
    public void registerObserver() {
        if (onDataChangelistener != null) {
            throw new IllegalStateException("Already registered! ");
        }
        onDataChangelistener = new DishCache.OnDataChangeListener() {

            @Override
            public void onDataChanged() {
                List<DishShop> dishShopList = selectedList;
                if (dishShopList != null) {
                    Map<String, DishShop> changedMap = new HashMap<String, DishShop>();
                    for (DishShop dishShop : dishShopList) {
                        DishShop cache = DishCache.getDishHolder().get(dishShop.getUuid());
                        boolean changed = false;
                        if (Utils.isNotEquals(dishShop.getResidueTotal(), cache.getResidueTotal())) {
                            changed = true;
                        } else if (Utils.isNotEquals(dishShop.getSaleTotal(), cache.getSaleTotal())) {
                            changed = true;
                        }
                        if (!changed && cache.getClearStatus() != dishShop.getClearStatus()) {
                            changed = true;
                        }
                        if (changed) {
                            changedMap.put(cache.getUuid(), cache);
                        }
                    }
                    if (!changedMap.isEmpty()) {
                        EventBus.getDefault().post(new EventDishChangedNotice(changedMap));
                    }
                }
            }

        };
        DishCache.getDishHolder().addDataChangeListener(onDataChangelistener);
    }

    /**
     * 取消监听数据库中DishShop的变化
     */
    public void unregisterObserver() {
        if (onDataChangelistener == null) {
            throw new IllegalStateException("Already unregistered! ");
        }
        DishCache.getDishHolder().removeDataChangeListener(onDataChangelistener);
        onDataChangelistener = null;
    }

    //##########################################################################

    /**
     * 按关键词搜索菜品。将使用{@link }通知符合条件的菜品列表
     *
     * @param keyword 搜索的关键词
     */
    public DishInfo search(final String keyword, final int searchType) {
        Checks.verifyNotNull(keyword, "keyword");
        DataFilter<DishShop> filter = new DataFilter<DishShop>() {

            @Override
            public boolean accept(DishShop entity) {
                // 排除不能单点的
                if (entity.getIsSingle() != Bool.YES) {
                    return false;
                }
                if (entity.getDishCode().equals(DishCache.TEMPDISHCODE)) {
                    return false;
                }
                switch (searchType) {
                    case CODE:
                        if (entity.getDishCode() == null || !entity.getDishCode().matches("[a-zA-Z0-9]+")) {
                            return false;
                        }
                        if (contains(entity.getDishCode().toUpperCase())) {
                            return true;
                        }
                        break;
                    case FIRST_LETTER:
                        if (entity.getDishNameIndex() == null || !entity.getDishNameIndex().matches("[a-zA-Z0-9\\(\\)\\.\\*]+")) {
                            return false;
                        }
                        if (contains(entity.getDishNameIndex().toUpperCase())) {
                            return true;
                        }
                        break;
                    case PRICE:
//                        if (keyword.toUpperCase().equals(MathDecimal.toTrimZeroString(entity.getMarketPrice()))) {
//                            return true;
//                        }

                        //update by dzb 为了能够搜索处带小数价格的菜品
                        Log.e(TAG, entity.getMarketPrice() + "," + toTrimDecimal(entity.getMarketPrice()));
                        if (keyword.toUpperCase().equals(toTrimDecimal(entity.getMarketPrice()))) {
                            return true;
                        }
                        break;
                    case DISHNAME://根据名字查询
                        if (contains(entity.getName().toUpperCase())) {
                            return true;
                        }
                        break;

                    case BARCODE://根据条形码模糊查询
                        if (containStart(entity.getBarcode())) {
                            return true;
                        }
                        break;
                    case ALL:
                        if (contains(entity.getName().toUpperCase()) || containStart(entity.getBarcode()) || (!TextUtils.isEmpty(entity.getDishNameIndex()) && contains(entity.getDishNameIndex().toUpperCase()))) {
                            return true;
                        }
                        break;
                }

                return false;
            }

            private boolean contains(String... values) {
                for (String value : values) {
                    if (value != null && value.contains(keyword.toUpperCase())) {
                        return true;
                    }
                }
                return false;
            }

            private boolean containStart(String... values) {
                for (String value : values) {
                    if (value != null && value.startsWith(keyword.toUpperCase())) {
                        return true;
                    }
                }
                return false;
            }
        };
        List<DishShop> dishList = DishCache.getDishHolder().filter(filter);
        Log.i(TAG, "dishList.size()=" + dishList.size() + ", keyword=" + keyword);
        List<DishVo> dishsVo = getDishsVo(dishList);
        DishInfo dishInfo = new DishInfo(null, dishsVo, false);
        return dishInfo;
    }

    /**
     * @param keyword 搜索的关键词
     */
    public DishInfo search1(final String keyword, final int searchType) {
        Checks.verifyNotNull(keyword, "keyword");
        DataFilter<DishShop> filter = new DataFilter<DishShop>() {

            @Override
            public boolean accept(DishShop entity) {
                // 排除不能单点的
                if (entity.getIsSingle() != Bool.YES) {
                    return false;
                }
                if (entity.getDishCode().equals(DishCache.TEMPDISHCODE)) {
                    return false;
                }
                switch (searchType) {
                    case CODE:
                        if (entity.getDishCode() == null || !entity.getDishCode().matches("[a-zA-Z0-9]+")) {
                            return false;
                        }
                        if (contains(entity.getDishCode().toUpperCase())) {
                            return true;
                        }
                        break;
                    case FIRST_LETTER:
                        if (entity.getDishNameIndex() == null || !entity.getDishNameIndex().matches("[a-zA-Z0-9\\(\\)\\.\\*]+")) {
                            return false;
                        }
                        if (contains(entity.getDishNameIndex().toUpperCase())) {
                            return true;
                        }
                        break;
                    case PRICE:
                        Log.e(TAG, entity.getMarketPrice() + "," + toTrimDecimal(entity.getMarketPrice()));
                        if (keyword.toUpperCase().equals(toTrimDecimal(entity.getMarketPrice()))) {
                            return true;
                        }
                        break;
                    case DISHNAME://根据名字查询
                        if (contains(entity.getName().toUpperCase())) {
                            return true;
                        }
                        break;

                    case BARCODE://根据条形码模糊查询
                        if (containStart(entity.getBarcode())) {
                            return true;
                        }
                        break;
                    case ALL:
                        if ((!entity.isCombo() && entity.getSaleType() != SaleType.WEIGHING && entity.getIsChangePrice() != Bool.YES)
                                && (contains(entity.getName().toUpperCase()) || containStart(entity.getBarcode()) || (!TextUtils.isEmpty(entity.getDishNameIndex()) && contains(entity.getDishNameIndex().toUpperCase())))) {
                            return true;
                        }
                        break;
                }

                return false;
            }

            private boolean contains(String... values) {
                for (String value : values) {
                    if (value != null && value.contains(keyword.toUpperCase())) {
                        return true;
                    }
                }
                return false;
            }

            private boolean containStart(String... values) {
                for (String value : values) {
                    if (value != null && value.startsWith(keyword.toUpperCase())) {
                        return true;
                    }
                }
                return false;
            }
        };
        List<DishShop> dishList = DishCache.getDishHolder().filterSet(filter);
        List<DishVo> dishsVo = getDishsVo1(dishList);
        DishInfo dishInfo = new DishInfo(null, dishsVo, false);
        return dishInfo;
    }


    /**
     * @param keyword 搜索的关键词
     */
    public DishInfo search2(final String keyword) {
        Checks.verifyNotNull(keyword, "keyword");
        List<DishShop> temp1 = new ArrayList<>();
        List<DishShop> temp2 = new ArrayList<>();
        List<DishShop> temp3 = new ArrayList<>();
        Collection<DishShop> dishShops = DishCache.getDishHolder().getAll();
        if (!Utils.isEmpty(dishShops)) {
            for (DishShop item : dishShops) {
                if (equalsSearch(keyword, item)) {
                    temp1.add(item);
                } else if (startWithSearch(keyword, item)) {
                    temp2.add(item);
                } else if (containsSearch(keyword, item)) {
                    temp3.add(item);
                }
            }
        }

        temp1.addAll(temp2);
        temp1.addAll(temp3);

        List<DishVo> dishsVo = getDishsVo(temp1);
        DishInfo dishInfo = new DishInfo(null, dishsVo, false);
        return dishInfo;
    }

    private boolean equalsSearch(String keyWorkds, DishShop dishShop) {
        return (!TextUtils.isEmpty(dishShop.getBarcode()) && dishShop.getBarcode().toUpperCase().equals(keyWorkds))
                || (!TextUtils.isEmpty(dishShop.getDishNameIndex()) && dishShop.getDishNameIndex().toUpperCase().equals(keyWorkds))
                || (!TextUtils.isEmpty(dishShop.getName()) && dishShop.getName().toUpperCase().equals(keyWorkds));
    }

    private boolean containsSearch(String keyWorkds, DishShop dishShop) {
        return (!TextUtils.isEmpty(dishShop.getBarcode()) && dishShop.getBarcode().toUpperCase().contains(keyWorkds))
                || (!TextUtils.isEmpty(dishShop.getDishNameIndex()) && dishShop.getDishNameIndex().toUpperCase().contains(keyWorkds))
                || (!TextUtils.isEmpty(dishShop.getName()) && dishShop.getName().toUpperCase().contains(keyWorkds));
    }

    private boolean startWithSearch(String keyWorkds, DishShop dishShop) {
        return (!TextUtils.isEmpty(dishShop.getBarcode()) && dishShop.getBarcode().toUpperCase().startsWith(keyWorkds))
                || (!TextUtils.isEmpty(dishShop.getDishNameIndex()) && dishShop.getDishNameIndex().toUpperCase().startsWith(keyWorkds))
                || (!TextUtils.isEmpty(dishShop.getName()) && dishShop.getName().toUpperCase().startsWith(keyWorkds));
    }

    /**
     * 去掉小数为，解决不能搜索小数的bug
     *
     * @param value
     * @return
     */
    private String toTrimDecimal(Number value) {
        if (value == null) {
            return "";
        }
        String str = value.toString();
        if (str.indexOf('.') < 0) {
            return str;
        }

        return str.substring(0, str.indexOf("."));
    }

    public DishInfo scan(final String barcode) {
        DataFilter<DishShop> filter = new DataFilter<DishShop>() {

            @Override
            public boolean accept(DishShop entity) {
                // 排除不能单点的
                if (entity.getIsSingle() != Bool.YES) {
                    return false;
                }

                if (entity.getBarcode() != null && barcode.equals(entity.getBarcode())) {
                    return true;
                }

                return false;
            }
        };
        List<DishShop> dishList = DishCache.getDishHolder().filter(filter);
        Log.i(TAG, "dishList.size()=" + dishList.size() + ", barcode=" + barcode);
        List<DishVo> dishsVo = getDishsVo(dishList);
        DishInfo dishInfo = new DishInfo(null, dishsVo, true);
        return dishInfo;
    }

    public DishInfo getDishInfoById(final long dishId) {
        DataFilter<DishShop> filter = new DataFilter<DishShop>() {

            @Override
            public boolean accept(DishShop entity) {
                // 排除不能单点的
                if (entity.getIsSingle() != Bool.YES) {
                    return false;
                }

                if (entity.getId() != null && entity.getId() == dishId) {
                    return true;
                }

                return false;
            }
        };
        List<DishShop> dishList = DishCache.getDishHolder().filter(filter);
        Log.i(TAG, "dishList.size()=" + dishList.size() + ", dishId=" + dishId);
        List<DishVo> dishsVo = getDishsVo(dishList);
        DishInfo dishInfo = new DishInfo(null, dishsVo, true);
        return dishInfo;
    }

    public DishVo getDishShopById(final long dishId) {
        DataFilter<DishShop> filter = new DataFilter<DishShop>() {

            @Override
            public boolean accept(DishShop entity) {
                // 排除不能单点的
                if (entity.getIsSingle() != Bool.YES) {
                    return false;
                }

                if (entity.getId() != null && entity.getId() == dishId) {
                    return true;
                }

                return false;
            }
        };
        List<DishShop> dishList = DishCache.getDishHolder().filter(filter);
        List<DishVo> dishsVoList = getDishsVo(dishList);
        if (!Utils.isEmpty(dishsVoList)) {
            return dishsVoList.get(0);
        }
        return null;
    }

    public DishVo getDishVoByBrandDishId(final long dishId) {
        DataFilter<DishShop> filter = new DataFilter<DishShop>() {

            @Override
            public boolean accept(DishShop entity) {
                // 排除不能单点的
                if (entity.getIsSingle() != Bool.YES) {
                    return false;
                }

                if (entity.getId() != null && entity.getId() == dishId) {
                    return true;
                }

                return false;
            }
        };
        List<DishShop> dishList = DishCache.getDishHolder().filter(filter);
        List<DishVo> dishsVoList = getDishsVo(dishList);
        if (!Utils.isEmpty(dishsVoList)) {
            return dishsVoList.get(0);
        }
        return null;
    }

    /**
     * 生成DishVo对象
     *
     * @param dishList
     */
    public List<DishVo> getDishsVo(List<DishShop> dishList) {
        InventoryCacheUtil inventoryCacheUtil = InventoryCacheUtil.getInstance();
        boolean inventoryIsOpen = inventoryCacheUtil.getSaleSwitch();//true库存开关打开
        Map<String, DishVo> voMap = new LinkedHashMap<String, DishVo>();
        for (DishShop dishShop : dishList) {
            DishUnitDictionary unit = DishCache.getUnitHolder().get(dishShop.getUnitId());
            // 没有规格的与有规格的不合成一个
            if (dishShop.getHasStandard() == Bool.YES) {
                DishVo vo = voMap.get(dishShop.getName());
                if (vo != null) {
                    vo.setContainProperties(true);
                    vo.addSameSeriesDish(dishShop);
                    if (dishShop.getMarketPrice().compareTo(vo.getMinPrice()) < 0) {
                        vo.setMinPrice(dishShop.getMarketPrice());
                    } else if (dishShop.getMarketPrice().compareTo(vo.getMaxPrice()) > 0) {
                        vo.setMaxPrice(dishShop.getMarketPrice());
                    }
                } else {
                    Set<DishProperty> standards = filterStandards(dishShop);
                    vo = new DishVo(dishShop, standards, unit);
                    voMap.put(vo.getName(), vo);
                }
                if (inventoryIsOpen) {//实时库存
                    InventoryInfo inventoryInfo = inventoryCacheUtil.getInventoryNumByDishUuid(dishShop.getUuid());
                    if (inventoryInfo != null) {
                        vo.setInventoryNum(inventoryInfo.getInventoryQty());
                    }
                }
            } else {
                String key = dishShop.getUuid() + "_" + dishShop.getName();
                DishVo dishVo = new DishVo(dishShop, unit);
                if (inventoryIsOpen) {//实时库存
                    InventoryInfo inventoryInfo = inventoryCacheUtil.getInventoryNumByDishUuid(dishShop.getUuid());
                    if (inventoryInfo != null) {
                        dishVo.setInventoryNum(inventoryInfo.getInventoryQty());
                    }
                }
                voMap.put(key, dishVo);
            }
        }
        List<DishVo> dishVoList = new ArrayList<DishVo>(voMap.values());
        Log.i(TAG, "voList.size()=" + dishVoList.size());
        return dishVoList;
//		EventBus.getDefault().post(new EventDishsNotice(dishType, dishVoList, scanResult));
    }

    /**
     * 生成DishVo对象
     *
     * @param dishList
     */
    public List<DishVo> getDishsVo1(List<DishShop> dishList) {
        List<DishVo> dishVoList = new ArrayList<DishVo>();
        for (DishShop dishShop : dishList) {
            DishUnitDictionary unit = DishCache.getUnitHolder().get(dishShop.getUnitId());
            Set<DishProperty> standards = filterStandards(dishShop);
            DishVo vo = new DishVo(dishShop, standards, unit);
            dishVoList.add(vo);
        }
        return dishVoList;
    }

    /**
     * 获取指定商品的规格集
     *
     * @param dishShop
     * @return
     */
    public static Set<DishProperty> filterStandards(DishShop dishShop) {
        Set<DishProperty> standards = new LinkedHashSet<DishProperty>();
//		DishStandardFilter filter = new DishStandardFilter(dishShop);
//		List<DishBrandProperty> dishPropertyList = DishCache.getDishPropertyHolder().filter(filter);
        List<DishBrandProperty> dishPropertyList = DishCache.getDishPropertyHolder().getPropertysByDishId(dishShop.getBrandDishId());
        if (dishPropertyList == null) {
            return standards;
        }
        for (DishBrandProperty dishProperty : dishPropertyList) {
            DishProperty standard = DishCache.getPropertyHolder().get(dishProperty.getPropertyId());
            if (standard != null) {
                standards.add(standard);
            }
        }
        return standards;
    }

    /**
     * @version: 1.0
     * @date 2015年7月20日
     */
    private static class DishStandardFilter implements DataFilter<DishBrandProperty> {

        private final DishShop dishShop;

        DishStandardFilter(DishShop dishShop) {
            this.dishShop = dishShop;
        }

        @Override
        public boolean accept(DishBrandProperty entity) {
            return entity.getPropertyKind() == PropertyKind.STANDARD
                    && dishShop.getBrandDishId().equals(entity.getDishId());
        }
    }

    /**
     * 获取菜品中有估清的菜品
     *
     * @param
     */
    public DishInfo loadDishInfoHaveClear() {

        List<DishBrandType> typeList = new ArrayList<>(DishCache.getDishTypeHolder().getAll());
        List<DishVo> dishsVos = new ArrayList<DishVo>();
        for (DishBrandType type : typeList) {
            List<DishVo> typeDishVos = getClearDishByType(type.getId());
            dishsVos.addAll(typeDishVos);
        }

        DishInfo dishInfo = new DishInfo(null, dishsVos, false);
        selectedList = new ArrayList<>(DishCache.getDishHolder().getAll());
        return dishInfo;
    }

    private List<DishVo> getClearDishByType(Long typeId) {
        List<DishShop> dishList = new ArrayList<>(DishCache.getDishHolder().getDishShopByType(typeId));
        Log.i(TAG, "dishList.size()=" + dishList.size());
        List<DishVo> dishsVos = getDishsVo(dishList);
        Iterator iterator = dishsVos.iterator();
        while (iterator.hasNext()) {
            DishVo dishVo = (DishVo) iterator.next();
            if (!dishVo.isHaveClear()) {
                iterator.remove();
            }

        }
        return dishsVos;
    }

    /**
     * 是否有临时菜设置
     *
     * @return
     */
    public boolean isHasTempDish() {
        if (DishCache.getTempDishShop() != null) {
            return true;
        }
        return false;
    }

    /**
     * 获取临时菜封装的dishVo对象
     *
     * @return
     */
    public DishVo getTempDishVo() {
        DishShop dishShop = DishCache.getTempDishShop();
        if (dishShop == null) {
            return null;
        }
        List<DishShop> warpperList = new ArrayList<DishShop>();
        warpperList.add(dishShop);
        List<DishVo> dishVoList = getDishsVo(warpperList);
        if (dishVoList.size() > 0)
            return dishVoList.get(0);
        return null;
    }


    /**
     * 估清菜品排后面
     */
    public static void clearDishToEnd(List<DishVo> dataSet) {
        if (dataSet == null || dataSet.isEmpty())
            return;
        if (SpHelper.getDefault().getBoolean(SpHelper.DINNER_CLEAR_SORT_SETTING) == false)
            return;
        List<DishVo> clearDishList = new ArrayList<>();
        Iterator<DishVo> iterator = dataSet.iterator();
        while (iterator.hasNext()) {
            DishVo dishVo = iterator.next();
            if (dishVo.isClear()) {
                clearDishList.add(dishVo);
                iterator.remove();
            }
        }
        dataSet.addAll(clearDishList);
    }

    /**
     * 获取所有的菜品
     *
     * @return
     */
    public List<DishVo> getAllDishVo() {
        List<DishShop> dishList = new ArrayList<>(DishCache.getDishHolder().getAll());
        return getDishsVo(dishList);
    }
}
