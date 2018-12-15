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

/**
 * 系统设置项管理(交接设置,收银进位规则)
 *
 * @date:2016年2月17日下午3:07:03
 */
public class ServerSettingManager {
    private static final String TAG = ServerSettingManager.class.getSimpleName();

    /**
     * 一桌一单
     */
    public static final int BILL_ORIGINAL_STYLE_ONLY_ONE = 0;
    /**
     * 一桌多单
     */
    public static final int BILL_ORIGINAL_STYLE_MULTI = 1;

    //默认小数进位数
    private static final int DEFAULT_CARRY_LIMIT = 2;
    //默认正餐开台方式
    private static final int DEFAULT_BILL_ORIGINAL_STYLE = 1;

    /**
     * 获取定时打印时间(按分钟计算)
     *
     * @return 定时打印间隔时间，按分钟计算
     */
    public static int getPrintTime() {
        Integer configValue = 0;
        SparseIntArray cashHandoverConfigArray = ServerSettingCache.getInstance().getCashHandoverConfigArray();
        if (cashHandoverConfigArray == null || cashHandoverConfigArray.size() == 0) {
            configValue = getCashHandoverConfig(CashHandoverConfigKey.PRINTTIME.value());
        } else {
            configValue = cashHandoverConfigArray.get(CashHandoverConfigKey.PRINTTIME.value(), 0);
        }

        //0. 0min 1. 30min 2. 60min  3. 90min  4. 120min
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

    /**
     * 获取收银保留的小数位数 0|1|2|3表示小数点保留0|1|2|3位
     *
     * @Title: getCarryLimit
     * @Description: TODO
     * @Param @return TODO
     * @Return int 返回类型
     */
    public static int getCarryLimit() {
        return ServerSettingCache.getInstance().getCashHandoverConfigArray()
                .get(CashHandoverConfigKey.CARRYLIMIT.value(), DEFAULT_CARRY_LIMIT);
    }

    /**
     * @Title: getCarryRule
     * @Description: 获取收银进位规则 1.四舍五入,2.无条件进位,3无条件摸零
     * @Param @return TODO
     * @Return int 返回类型
     */
    public static CarryBitRule getCarryRule() {
        Integer configValue = ServerSettingCache.getInstance().getCashHandoverConfigArray()
                .get(CashHandoverConfigKey.CARRYRULE.value(), 1);
        return ValueEnums.toEnum(CarryBitRule.class, configValue);
    }

    /**
     * 是否允许一桌多单
     *
     * @return 1.允许, 2.不允许
     */
    public static boolean allowMultiTradesOnTable() {
        Integer configValue = ServerSettingCache.getInstance().getCashHandoverConfigArray()
                .get(CashHandoverConfigKey.DINNERBILLORISTYLE.value(), DEFAULT_BILL_ORIGINAL_STYLE);
        return configValue == DEFAULT_BILL_ORIGINAL_STYLE;
    }

    /**
     * 门店是否需要验证会员密码
     *
     * @return 为true表示需要校验密码，为false表示不需要校验密码
     */
    public static boolean isCommercialNeedVerifPassword() {
        String value = ServerSettingCache.getInstance().getCommercialCustomSettingsMap().get("shop.memberPassInput.switch");
        return "1".equals(value);
    }

    public static int getHandoverType() {
        String value = ServerSettingCache.getInstance().getCommercialCustomSettingsMap().get("shop.fast.handover.switch");
        return value != null ? Integer.parseInt(value) : 1;
    }

    /**
     * 根据key值查询是否自动接单
     *
     * @param key
     * @return
     */
   /* public static boolean isThridAutoAccept(String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        DatabaseHelper helper = DBHelper.getHelper();
        try {
            CommercialCustomSettings typeSettings = helper.getDao(CommercialCustomSettings.class).queryBuilder().where()
                    .eq(CommercialCustomSettings.$.key, key)
                    .and().eq(CommercialCustomSettings.$.statusFlag, StatusFlag.VALID).queryForFirst();
            if (typeSettings != null) {
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            DBHelper.releaseHelper(helper);
        }
        return false;
    }
*/

    /**
     * 根据订单来源获取订单来源名称
     *
     * @param source 订单来源(可以为来源、子来源)
     * @return 订单来源名称
     */
    public static String getOrderSourceName(int source) {
        return ServerSettingCache.getInstance().getOrderSourceArray().get(source);
    }

    /**
     * 获取获取指定设备接收网络订单设置项,此方法可能会阻塞调用线程
     */
    public static String getOrderDisplayDevice() {
        String orderDisplayDevice = ServerSettingCache.getInstance().getCommercialCustomSettingsMap().get("shop.fast.support.deviceNo");
        //如果设置项没有值，那么优先去当前没门店下的主收银设备，如果没有主收银设备，取返回列表的第一个设备
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

    /**
     * 根据业务类型和操作类型获取TradeDealSettingVo
     *
     * @param businessType 业务类型
     * @param operateType  操作类型
     * @return
     */
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

    /**
     * 更新设置
     */
    public static void updateHandoverSet(CashHandoverConfig config, ResponseListener<CashHandoverConfig> listener) {
        SystemSettingDal dal = OperatesFactory.create(SystemSettingDal.class);
        dal.handoverClean(config, listener);
    }

    /**
     * 根据配送平台返回小费配置
     *
     * @param deliveryPlatform 指定配送平台
     */
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
