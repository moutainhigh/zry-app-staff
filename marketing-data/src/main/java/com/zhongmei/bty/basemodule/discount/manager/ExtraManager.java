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


public class ExtraManager {

    private static final String TAG = "ExtraManager";

    private Context mContext;
        public static final String mealFee = "CHF";

        public static final String DELIVERY_FEE = "PSF";

        public static final String BUFFET_OOUTTIME_CODE = "ZZCSF";

        public static final String BUFFET_MIN_CONSUM = "ZDXFCE";
        public static final String SERVICE_CONSUM = "SERVICE_CHARGE";

    public ExtraManager() {
            }


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

                                        if (!needDeliveryFee && extraCharge.getCode() != null && DELIVERY_FEE.equals(extraCharge.getCode())) {
                        continue;
                    }
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
                                if (!cloneMap.containsKey(ex.getId()) && !mealFee.equalsIgnoreCase(ex.getCode())) {
                    tempList.add(ex);
                }
            }
        }
                if (cloneMap != null) {
            tempList.addAll(cloneMap.values());
        }
        return tempList;
    }


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


    public List<DinnerExtraChargeVo> getExtraListVo(TradeVo tradeVo, boolean isDinner) {
        if (tradeVo.getExtraChargeMap() == null) {
            return getExtraListVo(isDinner, false, true);
        }
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


    public void resetSelectById(List<DinnerExtraChargeVo> extraVoList, Long extraChargeId) {
        for (DinnerExtraChargeVo exVo : extraVoList) {
            ExtraCharge ex = exVo.getExtrageCharge();
            if (ex.getId().longValue() == extraChargeId.longValue()) {
                exVo.setSelected(false);
            }
        }
    }


    public static ExtraCharge getExtraChargeById(TradeVo tradeVo, Long extraChargeId) {
        Map<Long, ExtraCharge> extraChargeMap = tradeVo.getExtraChargeMap();
        if (extraChargeMap == null || extraChargeId == null) {
            return null;
        }
        return extraChargeMap.get(extraChargeId);
    }


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
                                e.printStackTrace();
            }
        }
        return tempMap;
    }



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

        public static boolean isMinConsum(ExtraCharge extraCharge) {
        if (extraCharge == null) {
            return false;
        }
        return BUFFET_MIN_CONSUM.equalsIgnoreCase(extraCharge.getCode());
    }


}
