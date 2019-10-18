package com.zhongmei.bty.basemodule.shopmanager.handover.manager;

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;

import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerDeliveryPlatformConfig;
import com.zhongmei.bty.basemodule.commonbusiness.operates.SystemSettingDal;
import com.zhongmei.bty.basemodule.database.db.CashHandoverConfig;
import com.zhongmei.bty.basemodule.shopmanager.handover.enums.CashHandoverConfigKey;
import com.zhongmei.bty.basemodule.trade.bean.TradeDealSettingVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeDealSetting;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.util.CarryBitRule;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.InitSystem;
import com.zhongmei.yunfu.db.enums.DeliveryPlatform;
import com.zhongmei.bty.commonmodule.database.enums.InitSystemDeviceType;
import com.zhongmei.bty.commonmodule.database.enums.TradeDealSettingBusinessType;
import com.zhongmei.bty.commonmodule.database.enums.TradeDealSettingOperateType;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.List;


public class ServerSettingManager {
    private static final String TAG = ServerSettingManager.class.getSimpleName();


    public static final int BILL_ORIGINAL_STYLE_ONLY_ONE = 0;

    public static final int BILL_ORIGINAL_STYLE_MULTI = 1;

        private static final int DEFAULT_CARRY_LIMIT = 2;
        private static final int DEFAULT_BILL_ORIGINAL_STYLE = 1;


    public static int getPrintTime() {
        Integer configValue = 0;
        SparseIntArray cashHandoverConfigArray = ServerSettingCache.getInstance().getCashHandoverConfigArray();
        if (cashHandoverConfigArray == null || cashHandoverConfigArray.size() == 0) {
            configValue = getCashHandoverConfig(CashHandoverConfigKey.PRINTTIME.value());
        } else {
            configValue = cashHandoverConfigArray.get(CashHandoverConfigKey.PRINTTIME.value(), 0);
        }

                int printTime;
        switch (configValue) {
            case 1:
                printTime = 30;
                break;
            case 2:
                printTime = 60;
                break;
            case 3:
                printTime = 90;
                break;
            case 4:
                printTime = 120;
                break;
            default:
                printTime = 0;
                break;
        }

        return printTime;
    }

    private static Integer getCashHandoverConfig(Integer configKey) {
        CashHandoverConfig cashHandoverConfig = null;

        try {
            SystemSettingDal systemSettingDal = OperatesFactory.create(SystemSettingDal.class);
            cashHandoverConfig = systemSettingDal.findCashHandoverConfig(3);
        } catch (Exception e) {
            Log.e(TAG, "getCashHandoverConfig error");
        }

        return cashHandoverConfig == null ? 0 : cashHandoverConfig.getConfigValue();
    }


    public static int getCarryLimit() {
        return ServerSettingCache.getInstance().getCashHandoverConfigArray()
                .get(CashHandoverConfigKey.CARRYLIMIT.value(), DEFAULT_CARRY_LIMIT);
    }


    public static CarryBitRule getCarryRule() {
        Integer configValue = ServerSettingCache.getInstance().getCashHandoverConfigArray()
                .get(CashHandoverConfigKey.CARRYRULE.value(), 1);
        return ValueEnums.toEnum(CarryBitRule.class, configValue);
    }


    public static boolean allowMultiTradesOnTable() {
        Integer configValue = ServerSettingCache.getInstance().getCashHandoverConfigArray()
                .get(CashHandoverConfigKey.DINNERBILLORISTYLE.value(), DEFAULT_BILL_ORIGINAL_STYLE);
        return configValue == DEFAULT_BILL_ORIGINAL_STYLE;
    }


    public static boolean isCommercialNeedVerifPassword() {
        String value = ServerSettingCache.getInstance().getCommercialCustomSettingsMap().get("shop.memberPassInput.switch");
        return "1".equals(value);
    }

    public static int getHandoverType() {
        String value = ServerSettingCache.getInstance().getCommercialCustomSettingsMap().get("shop.fast.handover.switch");
        return value != null ? Integer.parseInt(value) : 1;
    }





    public static String getOrderSourceName(int source) {
        return ServerSettingCache.getInstance().getOrderSourceArray().get(source);
    }


    public static String getOrderDisplayDevice() {
        String orderDisplayDevice = ServerSettingCache.getInstance().getCommercialCustomSettingsMap().get("shop.fast.support.deviceNo");
                if (TextUtils.isEmpty(orderDisplayDevice)) {
            try {
                List<InitSystem> initSystems = OperatesFactory.create(SystemSettingDal.class).queryInitSystem(InitSystemDeviceType.PAD);
                if (Utils.isNotEmpty(initSystems)) {
                    for (InitSystem initSystem : initSystems) {
                        if (initSystem.getIsMainPos() == 0) {
                            return initSystem.getDeviceID();
                        }
                    }

                    return initSystems.get(0).getDeviceID();
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        return orderDisplayDevice;
    }


    public static TradeDealSettingVo getTradeDealSettingVo(TradeDealSettingBusinessType businessType, TradeDealSettingOperateType operateType) {
        List<TradeDealSettingVo> tradeDealSettingVos = ServerSettingCache.getInstance().getTradeDealSettingVos();
        for (TradeDealSettingVo tradeDealSettingVo : tradeDealSettingVos) {
            TradeDealSetting tradeDealSetting = tradeDealSettingVo.getTradeDealSetting();
            if (tradeDealSetting.getBusinessType() == businessType && tradeDealSetting.getOperateType() == operateType) {
                return tradeDealSettingVo;
            }
        }

        return null;
    }


    public static void updateHandoverSet(CashHandoverConfig config, ResponseListener<CashHandoverConfig> listener) {
        SystemSettingDal dal = OperatesFactory.create(SystemSettingDal.class);
        dal.handoverClean(config, listener);
    }


    public static PartnerDeliveryPlatformConfig getPartnerDeliveryPlatformConfig(DeliveryPlatform deliveryPlatform) {
        List<PartnerDeliveryPlatformConfig> partnerDeliveryPlatformConfigs = ServerSettingCache.getInstance().getPartnerDeliveryPlatformConfigs();
        if (Utils.isNotEmpty(partnerDeliveryPlatformConfigs)) {
            for (PartnerDeliveryPlatformConfig partnerDeliveryPlatformConfig : partnerDeliveryPlatformConfigs) {
                if (partnerDeliveryPlatformConfig.getType().equals(1)
                        && deliveryPlatform != null && deliveryPlatform == partnerDeliveryPlatformConfig.getDeliveryPlatform()) {
                    return partnerDeliveryPlatformConfig;
                }
            }
        }

        return null;
    }
}
