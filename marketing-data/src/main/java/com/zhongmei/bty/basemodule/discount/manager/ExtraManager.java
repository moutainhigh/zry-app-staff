package com.zhongmei.bty.basemodule.discount.manager;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.bty.basemodule.discount.bean.DinnerExtraChargeVo;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.discount.entity.ExtraChargeCommercialAreaRef;
import com.zhongmei.bty.basemodule.discount.operates.ExtraChargeDal;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.operates.TablesDal;
import com.zhongmei.yunfu.util.Beans;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 附加费管理的类
 *
 * @date:2016年3月11日上午11:03:50
 */
public class ExtraManager {

    private static final String TAG = "ExtraManager";

    private Context mContext;
    //餐盒费的code
    public static final String mealFee = "CHF";

    //配送费的code
    public static final String DELIVERY_FEE = "PSF";

    //自助超时费
    public static final String BUFFET_OOUTTIME_CODE = "ZZCSF";

    //最低消费差额
    public static final String BUFFET_MIN_CONSUM = "ZDXFCE";
    //服务费
    public static final String SERVICE_CONSUM = "SERVICE_CHARGE";

    public ExtraManager() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param isNeedMealFee 是否需要餐盒费
     * @Title: getExtraListVo
     * @Description: 获取附加费列表包装的list
     * @Param @return TODO
     * @Return List<DinnerExtraChrge> 返回类型
     */
    public List<DinnerExtraChargeVo> getExtraListVo(boolean isDinner, boolean isNeedMealFee, boolean needDeliveryFee) {
        ExtraChargeDal extraChargeDal = OperatesFactory.create(ExtraChargeDal.class);
        try {
            List<ExtraCharge> extraList = extraChargeDal.queryExtraChargeRules(false);
            if (Utils.isNotEmpty(extraList)) {
                List<DinnerExtraChargeVo> extraVoList = new ArrayList<DinnerExtraChargeVo>();
                for (ExtraCharge extraCharge : extraList) {
                    if (extraCharge.getCode() != null && mealFee.equals(extraCharge.getCode()) && !isNeedMealFee) {
                        continue;
                    }

                    //若不需要配送费，则移除配送费
                    if (!needDeliveryFee && extraCharge.getCode() != null && DELIVERY_FEE.equals(extraCharge.getCode())) {
                        continue;
                    }
                    //快餐和外卖不需要服务费
                    if (!isDinner && extraCharge.getCode() != null && SERVICE_CONSUM.equals(extraCharge.getCode())) {
                        continue;
                    }

                    DinnerExtraChargeVo chargeVo = new DinnerExtraChargeVo();
                    chargeVo.setExtrageCharge(extraCharge);
                    extraVoList.add(chargeVo);
                }
                return extraVoList;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    public static List<ExtraCharge> getAutoOrderExtraMap(Map<Long, ExtraCharge> extraChargeMap, boolean isHasAuto) {
        return getAutoOrderExtraMap(null, extraChargeMap, isHasAuto);
    }

    /**
     * @Title: getAutoOrderExtraList
     * @Description: 获得自动加入订单的附加费列表
     * @Param extraChargeMap 原单的附加费
     * isHasAuto:是否包含后台设置的知道带入订单的附加费 true：包含
     * @Return Map<Long       ,       ExtraCharge> 返回类型
     */
    public static List<ExtraCharge> getAutoOrderExtraMap(TradeVo tradeVo, Map<Long, ExtraCharge> extraChargeMap, boolean isHasAuto) {
        Map<Long, ExtraCharge> cloneMap = cloneExtraMap(extraChargeMap);
        List<ExtraCharge> extraList = null;
        if (isHasAuto) {
            List<Long> commercialAreasIds = null;
            if (tradeVo != null) {
                commercialAreasIds = getCommercialAreasIds(tradeVo.getTableIds());
            }
            extraList = getAutoOrderExtraList(commercialAreasIds);
        }
        List<ExtraCharge> tempList = new ArrayList<ExtraCharge>();
        if (extraList != null && !extraList.isEmpty()) {
            if (cloneMap == null) {
                cloneMap = new HashMap<Long, ExtraCharge>();
            }
            for (ExtraCharge ex : extraList) {
                // 正餐屏蔽餐盒费
                if (!cloneMap.containsKey(ex.getId()) && !mealFee.equalsIgnoreCase(ex.getCode())) {
                    tempList.add(ex);
                }
            }
        }
        // 将原单的附加费加入到列表
        if (cloneMap != null) {
            tempList.addAll(cloneMap.values());
        }
        return tempList;
    }

    /**
     * @Title: getAutoOrderList
     * @Description: 返回从数据库中查询的自动加入订单的附加费
     * @Param @return TODO
     * @Return List<ExtraCharge> 返回类型
     */
    public static List<ExtraCharge> getAutoOrderExtraList() {
        List<ExtraCharge> extraList = new ArrayList<ExtraCharge>();
        ExtraChargeDal extraChargeDal = OperatesFactory.create(ExtraChargeDal.class);
        try {
            extraList = extraChargeDal.queryExtraChargeRules(true);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return extraList;
    }

    public static List<ExtraCharge> getAutoOrderExtraList(List<Long> commercialAreasIds) {
        List<ExtraCharge> result = new ArrayList<>();
        List<ExtraCharge> extraList = getAutoOrderExtraList();
        if (extraList != null) {
            for (ExtraCharge charge : extraList) {
                if (commercialAreasIds != null) {
                    List<ExtraChargeCommercialAreaRef> commercialAreaRefs = ServerSettingCache.getInstance().getExtraChargeCommercialAreaRef(charge.getId());
                    if (commercialAreaRefs != null && commercialAreaRefs.size() > 0) {
                        for (ExtraChargeCommercialAreaRef areaRef : commercialAreaRefs) {
                            if (commercialAreasIds.contains(areaRef.getCommercialAreaId())) {
                                result.add(charge);
                            }
                        }
                    } else {
                        result.add(charge);
                    }
                } else {
                    result.add(charge);
                }
            }
        }
        return result;
    }

    /**
     * 根据区域获取附近加费
     *
     * @param tradeVo
     * @return
     */
    public List<DinnerExtraChargeVo> getExtraListVoByCommercialArea(TradeVo tradeVo) {
        List<DinnerExtraChargeVo> result = new ArrayList<>();
        try {
            List<Long> tableIds = tradeVo.getTableIds();
            List<Long> commercialAreasIds = getCommercialAreasIds(tableIds);
            List<DinnerExtraChargeVo> extraListVo = getExtraListVo(tradeVo, false);
            if (extraListVo != null) {
                for (DinnerExtraChargeVo extraChargeVo : extraListVo) {
                    if (extraChargeVo.getExtrageCharge().isTableAreaEnable()) {
                        boolean isContainsArea = false;
                        List<ExtraChargeCommercialAreaRef> commercialAreaRefs = ServerSettingCache.getInstance().getExtraChargeCommercialAreaRef(extraChargeVo.getExtrageCharge().getId());
                        for (ExtraChargeCommercialAreaRef areaRef : commercialAreaRefs) {
                            if (commercialAreasIds.contains(areaRef.getCommercialAreaId())) {
                                isContainsArea = true;
                                break;
                            }
                        }
                        if (isContainsArea) {
                            result.add(extraChargeVo);
                        }
                    } else {
                        result.add(extraChargeVo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static List<Long> getCommercialAreasIds(List<Long> tableIds) {
        List<Long> commercialAreasIds = new ArrayList<>();
        try {
            TablesDal tablesDal = OperatesFactory.create(TablesDal.class);
            Map<CommercialArea, List<Tables>> commercialAreaListMap = tablesDal.listAreaByTableId(tableIds.toArray(new Long[0]));
            Set<CommercialArea> commercialAreas = commercialAreaListMap.keySet();
            if (commercialAreas != null) {
                for (CommercialArea area : commercialAreas) {
                    commercialAreasIds.add(area.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commercialAreasIds;
    }

    /**
     * @Title: getExtraListVo
     * @Description: TODO
     * @Param @param tradeVo
     * @Param @return TODO
     * @Return List<DinnerExtraChargeVo> 返回类型
     */
    public List<DinnerExtraChargeVo> getExtraListVo(TradeVo tradeVo, boolean isDinner) {
        if (tradeVo.getExtraChargeMap() == null) {
            return getExtraListVo(isDinner, false, true);
        }
        //
        Collection<ExtraCharge> sourceExtraList = tradeVo.getExtraChargeMap().values();
        List<DinnerExtraChargeVo> extraList = getExtraListVo(isDinner, false, true);
        if (extraList != null) {
            for (DinnerExtraChargeVo extraChargeVo : extraList) {
                for (ExtraCharge extra : sourceExtraList) {
                    if (extraChargeVo.getExtrageCharge().getId().longValue() == extra.getId().longValue()) {
                        extraChargeVo.setSelected(true);
                    }
                }
            }
        }
        return extraList;
    }

    /**
     * @Title: resetSelectById
     * @Description: 将指定id附加费 状态设为不选择
     * @Param @param extraVoList
     * @Param @param extraChargeId TODO
     * @Return void 返回类型
     */
    public void resetSelectById(List<DinnerExtraChargeVo> extraVoList, Long extraChargeId) {
        for (DinnerExtraChargeVo exVo : extraVoList) {
            ExtraCharge ex = exVo.getExtrageCharge();
            if (ex.getId().longValue() == extraChargeId.longValue()) {
                exVo.setSelected(false);
            }
        }
    }

    /**
     * @Title: getExtraChargeById
     * @Description: 根据id获得附加费对象
     * @Param @param tradeVo
     * @Param @param extraChargeId
     * @Param @return TODO
     * @Return ExtraCharge 返回类型
     */
    public static ExtraCharge getExtraChargeById(TradeVo tradeVo, Long extraChargeId) {
        Map<Long, ExtraCharge> extraChargeMap = tradeVo.getExtraChargeMap();
        if (extraChargeMap == null || extraChargeId == null) {
            return null;
        }
        return extraChargeMap.get(extraChargeId);
    }

    /**
     * @Title: cloneExtraMap
     * @Description: 克隆map
     * @Param @param extraChargeMap
     * @Param @return TODO
     * @Return Map<Long       ,       ExtraCharge> 返回类型
     */
    public static Map<Long, ExtraCharge> cloneExtraMap(Map<Long, ExtraCharge> extraChargeMap) {
        if (extraChargeMap == null || extraChargeMap.isEmpty() || extraChargeMap.values() == null) {
            return null;
        }
        Map<Long, ExtraCharge> tempMap = new HashMap<Long, ExtraCharge>();
        for (ExtraCharge extraCharge : extraChargeMap.values()) {
            ExtraCharge targetCharge = new ExtraCharge();
            try {
                if (extraCharge.getStatusFlag() == StatusFlag.VALID) {
                    Beans.copyProperties(extraCharge, targetCharge);
                    tempMap.put(extraCharge.getId(), targetCharge);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return tempMap;
    }

    /**
     * @Title: getExtraChargeChf
     * @Description: 获取餐盒费规则
     * @Param @return TODO
     * @Return ExtraCharge 返回类型
     */

    public static ExtraCharge getExtraChargeChf() {
        ExtraCharge extraCharge = null;
        ExtraChargeDal extraChargeDal = OperatesFactory.create(ExtraChargeDal.class);
        try {
            extraCharge = extraChargeDal.queryExtraChargeChf();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return extraCharge;
    }


    /**
     * 获取最低消费附加费选项
     *
     * @return
     */
    public static ExtraCharge getMinconsumExtra() {

        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<ExtraCharge, Long> extraChargeDao = helper.getDao(ExtraCharge.class);
            return extraChargeDao.queryBuilder().where().
                    eq(ExtraCharge.$.statusFlag, StatusFlag.VALID)
                    .and().eq(ExtraCharge.$.enabledFlag, Bool.YES)
                    .and().eq(ExtraCharge.$.code, ExtraManager.BUFFET_MIN_CONSUM).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }

    //是否是最低消费
    public static boolean isMinConsum(ExtraCharge extraCharge) {
        if (extraCharge == null) {
            return false;
        }
        return BUFFET_MIN_CONSUM.equalsIgnoreCase(extraCharge.getCode());
    }


}
