package com.zhongmei.bty.basemodule.commonbusiness.cache;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.basemodule.PosConfig;
import com.zhongmei.bty.basemodule.commonbusiness.constants.SnackConstant;
import com.zhongmei.bty.basemodule.commonbusiness.entity.AppPrivilege;
import com.zhongmei.bty.basemodule.commonbusiness.entity.CommercialCustomSettings;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerDeliveryPlatformConfig;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;
import com.zhongmei.bty.basemodule.commonbusiness.entity.SyncDict;
import com.zhongmei.bty.basemodule.commonbusiness.entity.TaxRateInfo;
import com.zhongmei.bty.basemodule.customer.entity.CrmMemberDay;
import com.zhongmei.bty.basemodule.database.db.CashHandoverConfig;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.discount.entity.ExtraChargeCommercialAreaRef;
import com.zhongmei.bty.basemodule.discount.manager.ExtraManager;
import com.zhongmei.bty.basemodule.trade.bean.DepositInfo;
import com.zhongmei.bty.basemodule.trade.bean.OutTimeInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeDealSettingVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeDealSetting;
import com.zhongmei.bty.basemodule.trade.entity.TradeDealSettingItem;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.util.EmptyUtils;
import com.zhongmei.yunfu.context.util.ThreadUtils;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务端设置数据缓存
 */

public class ServerSettingCache {
    private static final String TAG = ServerSettingCache.class.getSimpleName();

    //commercial_custom_settings表
    private static final Uri URI_COMMERCIAL_CUSTOMER_SETTINGS = DBHelperManager.getUri(CommercialCustomSettings.class);

    //cash_handover_config表
    private static final Uri URI_CASH_HANDOVER_CONFIG = DBHelperManager.getUri(CashHandoverConfig.class);

    //sync_dict表
    private static final Uri URI_SYNC_DICT = DBHelperManager.getUri(SyncDict.class);

    //trade_deal_setting表
    private static final Uri URI_TRADE_DEAL_SETTING = DBHelperManager.getUri(TradeDealSetting.class);

    //trade_deal_setting_item表
    private static final Uri URI_TRADE_DEAL_SETTING_ITEM = DBHelperManager.getUri(TradeDealSettingItem.class);

    //附加费－超时费
    private static final Uri URI_EXTRA_CHARGE_OUTTIME = DBHelperManager.getUri(ExtraCharge.class);

    private static final Uri URI_EXTRA_CHARGE_AREA_REF_OUTTIME = DBHelperManager.getUri(ExtraChargeCommercialAreaRef.class);

    // 商户类型 v7.16 判断是否为金城商户
    private static final Uri URI_EXTRA_PARTNER_SHOP_BIZ = DBHelperManager.getUri(PartnerShopBiz.class);

    //partner_delivery_platform_config表
    private static final Uri URI_PARTNER_DELIVERY_PLATFORM_CONFIG = DBHelperManager.getUri(PartnerDeliveryPlatformConfig.class);

    //税率设置表
    private static final Uri URI_TAX_RATE_INFO = DBHelperManager.getUri(TaxRateInfo.class);

    //购买服务表
    private static final Uri URI_APP_PRIVILEGE = DBHelperManager.getUri(AppPrivilege.class);

    // v8.12.0 会员日
    private static final Uri URI_MEMBER_DAY = DBHelperManager.getUri(CrmMemberDay.class);


    private ServerSettingCache() {
        mCommercialCustomSettingsMap = new HashMap<String, String>();
        mCashHandoverConfigArray = new SparseIntArray();
        mOrderSourceArray = new SparseArray<String>();
        mTradeDealSettingVos = new ArrayList<TradeDealSettingVo>();
        mPartnerDeliveryPlatformConfigs = new ArrayList<PartnerDeliveryPlatformConfig>();
        mAppPrivilegeMap = new HashMap<String, Boolean>();
        mPartnerShopBizMap = new ConcurrentHashMap<Integer, PartnerShopBiz>();
    }

    public boolean isOfflineEnable() {
        return SnackConstant.OfflineEnableSwitch.isOfflineEnable();
    }

    private static class LazySingletonHolder {
        private static final ServerSettingCache INSTANCE = new ServerSettingCache();
    }

    public static ServerSettingCache getInstance() {
        return LazySingletonHolder.INSTANCE;
    }

    public ExtraCharge getmOutTimeRule() {
        return mOutTimeRule;
    }

    public ExtraCharge getmServiceExtraCharge() {
        return mServiceExtraCharge;
    }

    public List<ExtraCharge> getExtraChargeList() {
        return extraChargeList;
    }

    public List<ExtraChargeCommercialAreaRef> getExtraChargeCommercialAreaRefList() {
        return extraChargeCommercialAreaRefList;
    }

    public List<ExtraChargeCommercialAreaRef> getExtraChargeCommercialAreaRef(Long extrageChargeId) {
        List<ExtraChargeCommercialAreaRef> results = new ArrayList<>();
        for (ExtraChargeCommercialAreaRef commercialAreaRef : extraChargeCommercialAreaRefList) {
            if (extrageChargeId != null && extrageChargeId.equals(commercialAreaRef.getExtraChargeId())) {
                results.add(commercialAreaRef);
            }
        }
        return results;
    }

    public TaxRateInfo getmTaxRateInfo() {
        return mTaxRateInfo;
    }

    /**
     * 获取commercial_custom_settings表中数据
     *
     * @return Map
     */
    public Map<String, String> getCommercialCustomSettingsMap() {
        return mCommercialCustomSettingsMap;
    }

    /**
     * 获取就餐时长限制
     */
    public String getLimitServiceTime() {
        return mCommercialCustomSettingsMap.get("shop.buffet.time");
    }

    /**
     * 获取自助餐桌台配置，是否使用有桌自助餐
     */
    public boolean getBuffetTableStatus() {
        String value = mCommercialCustomSettingsMap.get("buffet.setting.buffetMode");
        return !"2".equals(value);
    }

    /**
     * 获取后台设置的押金开关
     *
     * @return 返回true表示押金开关开启，返回false表示押金开关关闭
     */
    public boolean getDepositEnable() {
        String value = mCommercialCustomSettingsMap.get("trade.deposit.switch");
        return "1".equals(value);
    }

    /**
     * 自助餐押金开关
     */
    public boolean getBuffetDepositEnable() {
        String value = mCommercialCustomSettingsMap.get("buffet.setting.deposit.switch");
        return "1".equals(value);
    }

    /**
     * 自助餐超时开关
     */
    public boolean getBuffetOutTimeFeeEnable() {
        String value = mCommercialCustomSettingsMap.get("buffet.setting.otFee.switch");
        return "1".equals(value) && mOutTimeRule != null;
    }

    /**
     * 将超时费的的规则存入附加费
     *
     * @return
     */
    public OutTimeInfo getBuffetOutTimeFeeRule() {

        if (!getBuffetOutTimeFeeEnable()) {//如果
            return null;
        }

        OutTimeInfo outTimeInfo = new OutTimeInfo();
        outTimeInfo.setOutTimeUnit(mOutTimeRule.getContentByTime().longValue());
        outTimeInfo.setOutTimeFee(new BigDecimal(mOutTimeRule.getContent()));
        outTimeInfo.setLimitTimeLine(getBuffetTimeLimitLine());

        return outTimeInfo;
    }


    /**
     * 获取自助餐用餐显示限制(单位：分钟)
     *
     * @return
     */
    public long getBuffetTimeLimitLine() {
        String value = mCommercialCustomSettingsMap.get("buffet.setting.dinnerTime");
        if (TextUtils.isEmpty(value)) {
            return 0;
        }
        return Integer.parseInt(value);
    }


    /**
     * 获取自助餐押金规则
     *
     * @return
     */
    public DepositInfo getBuffetTradeDeposit() {
        DepositInfo depositInfo = new DepositInfo();

        //押金类型
        String depositType = mCommercialCustomSettingsMap.get("buffet.setting.deposit.type");
        if (TextUtils.isEmpty(depositType)) {
            depositInfo.setType(DepositInfo.TYPE_BY_PEOPLE);
        } else {
            depositInfo.setType(Integer.valueOf(depositType));
        }

        //押金值
        String depositValue = mCommercialCustomSettingsMap.get("buffet.setting.deposit");
        if (!TextUtils.isEmpty(depositValue)) {
            depositInfo.setValue(new BigDecimal(depositValue));
        }

        return depositInfo;
    }

    /**
     * 获取后台设置的押金信息
     *
     * @return DepositInfo
     */
    public DepositInfo getTradeDeposit() {
        DepositInfo depositInfo = new DepositInfo();

        //押金类型
        String depositType = mCommercialCustomSettingsMap.get("trade.deposit.type");
        if (TextUtils.isEmpty(depositType)) {
            depositInfo.setType(DepositInfo.TYPE_BY_PEOPLE);
        } else {
            depositInfo.setType(Integer.valueOf(depositType));
        }

        //押金值
        String depositValue = mCommercialCustomSettingsMap.get("trade.deposit");
        if (!TextUtils.isEmpty(depositValue)) {
            depositInfo.setValue(new BigDecimal(depositValue));
        }

        return depositInfo;
    }

    /**
     * 检查是否购买了某个服务
     *
     * @param appCode
     */
    public boolean checkAppService(String appCode) {
        if (mAppPrivilegeMap != null) {
            return mAppPrivilegeMap.get(appCode) != null ? mAppPrivilegeMap.get(appCode) : false;
        }
        return false;
    }

    /**
     * 判断达达配送是否开启
     *
     * @return 为true表示开启，为false表示关闭
     */
    public boolean getDadaSwitchEnable() {
        String value = mCommercialCustomSettingsMap.get("shop.thirdDeliveryPlat.dadaSwitch");
        return "1".equals(value);
    }

    /**
     * 获取cash_handover_config表中数据
     *
     * @return SparseIntArray
     */
    public SparseIntArray getCashHandoverConfigArray() {
        return mCashHandoverConfigArray;
    }

    /**
     * 获取sync_dict表中订单来源数据
     *
     * @return SparseArray
     */
    public SparseArray<String> getOrderSourceArray() {
        return mOrderSourceArray;
    }

    /**
     * 获取trade_deal_setting、trade_deal_setting_item表数据
     *
     * @return
     */
    public List<TradeDealSettingVo> getTradeDealSettingVos() {
        return mTradeDealSettingVos;
    }

    public List<PartnerDeliveryPlatformConfig> getPartnerDeliveryPlatformConfigs() {
        return mPartnerDeliveryPlatformConfigs;
    }

    /**
     * commercial_custom_settings表中数据缓存到这个Map中
     */
    private Map<String, String> mCommercialCustomSettingsMap;

    /**
     * cash_handover_config表中数据缓存到这个SparseIntArray中
     */
    private SparseIntArray mCashHandoverConfigArray;

    /**
     * sync_dict表中订单来源数据缓存到这个SparseArray中
     */
    private SparseArray<String> mOrderSourceArray;

    /**
     * 超时费
     */
    private ExtraCharge mOutTimeRule;

    //服务费
    private ExtraCharge mServiceExtraCharge;

    //附加费不包含服务费
    private List<ExtraCharge> extraChargeList;

    //区域附加费关联关系
    private List<ExtraChargeCommercialAreaRef> extraChargeCommercialAreaRefList;

    /**
     * trade_deal_setting、trade_deal_setting_item表数据会缓存到这个List中
     */
    private List<TradeDealSettingVo> mTradeDealSettingVos;

    /**
     * PartnerShopBiz表数据会缓存到这个map中, getway开通第3方业务表
     */
    private ConcurrentHashMap<Integer, PartnerShopBiz> mPartnerShopBizMap;// add v8.15
    //税率设置
    private TaxRateInfo mTaxRateInfo;

    //购买服务
    private Map<String, Boolean> mAppPrivilegeMap;

    /**
     * partner_delivery_platform_config表数据会缓存到这个List中
     */
    private List<PartnerDeliveryPlatformConfig> mPartnerDeliveryPlatformConfigs;

    private DatabaseHelper.DataChangeObserver mDataChangeObserver;

    private class ServerSettingObserver implements DatabaseHelper.DataChangeObserver {
        @Override
        public void onChange(final Collection<Uri> uris) {
            ThreadUtils.runOnWorkThread(new Runnable() {
                @Override
                public void run() {
                    DatabaseHelper helper = DBHelperManager.getHelper();
                    try {
                        if (uris.contains(URI_COMMERCIAL_CUSTOMER_SETTINGS)) {
                            refreshCommercialCustomSettings(helper);
                        }

                        if (uris.contains(URI_CASH_HANDOVER_CONFIG)) {
                            refreshCashHandoverConfig(helper);
                        }

                        if (uris.contains(URI_SYNC_DICT)) {
                            refreshSyncDict(helper);
                        }

                        if (uris.contains(URI_TRADE_DEAL_SETTING)
                                || uris.contains(URI_TRADE_DEAL_SETTING_ITEM)) {
                            refreshTradeDealSetting(helper);
                        }

                        if (uris.contains(URI_EXTRA_CHARGE_OUTTIME) || uris.contains(URI_EXTRA_CHARGE_AREA_REF_OUTTIME)) {
                            refreshOutTimeResult(helper);
                        }
                        // v7.16 判断是否为金城商户
                        if (uris.contains(URI_EXTRA_PARTNER_SHOP_BIZ)) {
                            // refreshPartnerShopResult(helper);
                            refreshPartnerShopBiz(helper);//add v8.15
                        }

                        if (uris.contains(URI_PARTNER_DELIVERY_PLATFORM_CONFIG)) {
                            refreshPartnerDeliveryPlatformConfig(helper);
                        }

                        if (uris.contains(URI_TAX_RATE_INFO)) {
                            refreshTaxRateInfo(helper);
                        }

                        if (uris.contains(URI_APP_PRIVILEGE)) {

                        }
                        // v8.12.0 会员日
                        if (uris.contains(URI_MEMBER_DAY)) {
                            refreshMemberDay();
                        }
                    } finally {
                        DBHelperManager.releaseHelper(helper);
                    }
                }
            });
        }
    }

    /**
     * 注册数据变更监听器
     */
    private void registerContentObserver() {
        if (mDataChangeObserver == null) {
            mDataChangeObserver = new ServerSettingObserver();
        }
        DatabaseHelper.Registry.register(mDataChangeObserver);
    }

    /**
     * 反注册掉数据变更监听器
     */
    private void unregisterContentObserver() {
        if (mDataChangeObserver != null) {
            DatabaseHelper.Registry.unregister(mDataChangeObserver);
            mDataChangeObserver = null;
        }
    }

    /**
     * 刷新税费
     *
     * @param helper
     */
    private void refreshTaxRateInfo(DatabaseHelper helper) {
        try {
            mTaxRateInfo = helper.getDao(TaxRateInfo.class)
                    .queryBuilder()
                    .where().eq(TaxRateInfo.$.statusFlag, StatusFlag.VALID).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 刷新税费
     *
     * @param helper
     */
    private void refreshAppPrivilege(DatabaseHelper helper) {
        //查询app_privilege数据库中的数据
        List<AppPrivilege> appPrivileges = new ArrayList<AppPrivilege>();
        try {
            appPrivileges = helper.getDao(AppPrivilege.class)
                    .queryBuilder()
                    .selectColumns(AppPrivilege.$.appCode,
                            AppPrivilege.$.available)
                    .where().eq(CommercialCustomSettings.$.statusFlag, StatusFlag.VALID).query();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        //遍历返回结果，将其存入map
        mAppPrivilegeMap.clear();
        if (Utils.isNotEmpty(appPrivileges)) {
            for (AppPrivilege appPrivilege : appPrivileges) {
                mAppPrivilegeMap.put(appPrivilege.getAppCode(), appPrivilege.getAvailable());
            }
        }
    }

    /**
     * 刷新所有设置数据
     */
    public synchronized void refreshAll() {
        unregisterContentObserver();
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            refreshCommercialCustomSettings(helper);
            refreshCashHandoverConfig(helper);
            refreshSyncDict(helper);
            refreshTradeDealSetting(helper);
            refreshOutTimeResult(helper);
            refreshPartnerShopBiz(helper);//add v8.15
            refreshPartnerDeliveryPlatformConfig(helper);
            refreshTaxRateInfo(helper);
            refreshAppPrivilege(helper);
            refreshMemberDay();
            //refreshPosConfig();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        registerContentObserver();
        checkData();
    }

    private void checkData() {
        Log.i(TAG, "mCommercialCustomSettingsMap.size = " + mCommercialCustomSettingsMap.size());
        Log.i(TAG, "mCashHandoverConfigArray.size = " + mCashHandoverConfigArray.size());
        Log.i(TAG, "mOrderSourceArray.size() = " + mOrderSourceArray.size());
        Log.i(TAG, "mTradeDealSettingVos.size() = " + mTradeDealSettingVos.size());
        Log.i(TAG, "mPartnerDeliveryPlatformConfigs = " + mPartnerDeliveryPlatformConfigs.size());
    }

    /**
     * 刷新commercial_custom_settings表数据
     *
     * @param helper helper
     */
    private void refreshCommercialCustomSettings(DatabaseHelper helper) {
        //查询commercial_custom_settings数据库中的数据
        List<CommercialCustomSettings> commercialCustomSettingsList = new ArrayList<CommercialCustomSettings>();
        try {
            commercialCustomSettingsList = helper.getDao(CommercialCustomSettings.class)
                    .queryBuilder()
                    .selectColumns(CommercialCustomSettings.$.key,
                            CommercialCustomSettings.$.value)
                    .where().eq(CommercialCustomSettings.$.statusFlag, StatusFlag.VALID).query();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        //遍历返回结果，将其存入map
        mCommercialCustomSettingsMap.clear();
        if (Utils.isNotEmpty(commercialCustomSettingsList)) {
            for (CommercialCustomSettings commercialCustomSettings : commercialCustomSettingsList) {
                mCommercialCustomSettingsMap.put(commercialCustomSettings.getSettingKey(), commercialCustomSettings.getSettingValue());
            }
        }
    }

    /**
     * 刷新cash_handover_config表数据
     *
     * @param helper helper
     */
    private void refreshCashHandoverConfig(DatabaseHelper helper) {
        //查询cash_handover_config数据库中的数据
        List<CashHandoverConfig> cashHandoverConfigList = new ArrayList<CashHandoverConfig>();

        try {
            cashHandoverConfigList = helper.getDao(CashHandoverConfig.class)
                    .queryBuilder()
                    .selectColumns(CashHandoverConfig.$.configKey,
                            CashHandoverConfig.$.configValue)
                    .where().eq(CashHandoverConfig.$.statusFlag, StatusFlag.VALID).query();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        //遍历返回结果，将其存入SparseIntArray
        mCashHandoverConfigArray.clear();
        if (Utils.isNotEmpty(cashHandoverConfigList)) {
            for (CashHandoverConfig cashHandoverConfig : cashHandoverConfigList) {
                mCashHandoverConfigArray.put(cashHandoverConfig.getConfigKey(), cashHandoverConfig.getConfigValue());
            }
        }
    }

    /**
     * 刷新sync_dict表数据
     *
     * @param helper helper
     */
    private void refreshSyncDict(DatabaseHelper helper) {
        List<SyncDict> syncDictList = new ArrayList<SyncDict>();
        try {
            syncDictList = helper.getDao(SyncDict.class).queryBuilder()
                    .selectColumns(SyncDict.$.code, SyncDict.$.name, SyncDict.$.type)
                    .where().eq(SyncDict.$.statusFlag, StatusFlag.VALID)
                    .query();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        //遍历返回结果，将其存入SparseArray
        mOrderSourceArray.clear();
        if (Utils.isNotEmpty(syncDictList)) {
            for (SyncDict syncDict : syncDictList) {
                //根据sync_dict_type表，1表示订单来源,2表示订单子来源
                if (syncDict.getType().intValue() == 1 || syncDict.getType().intValue() == 2) {
                    mOrderSourceArray.put(syncDict.getCode(), syncDict.getName());
                }
            }
        }
    }

    /**
     * 刷新trade_deal_setting、trade_deal_setting_item表数据
     *
     * @param helper
     */
    private void refreshTradeDealSetting(DatabaseHelper helper) {
        try {
            Dao<TradeDealSetting, String> tradeDealSettingDao = helper.getDao(TradeDealSetting.class);
            QueryBuilder<TradeDealSetting, String> qb = tradeDealSettingDao.queryBuilder();
            qb.where().eq(TradeDealSetting.$.statusFlag, StatusFlag.VALID);
            qb.orderBy(TradeDealSetting.$.serverUpdateTime, false);
            List<TradeDealSetting> tradeDealSettings = qb.query();

            if (Utils.isNotEmpty(tradeDealSettings)) {
                List<TradeDealSettingVo> list = new ArrayList<TradeDealSettingVo>(tradeDealSettings.size());
                for (TradeDealSetting tradeDealSetting : tradeDealSettings) {
                    // 交易自动处理时段表
                    List<TradeDealSettingItem> tradeDealSettingItems =
                            helper.getDao(TradeDealSettingItem.class).queryForEq(TradeDealSettingItem.$.settingId,
                                    tradeDealSetting.getId());

                    TradeDealSettingVo tradeDealSettingVo = new TradeDealSettingVo();
                    tradeDealSettingVo.setTradeDealSetting(tradeDealSetting);
                    tradeDealSettingVo.setTradeDealSettingItems(tradeDealSettingItems);
                    list.add(tradeDealSettingVo);
                }
                mTradeDealSettingVos = list;//modify 20170307 防止多线程操作同一个对象
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    private void refreshOutTimeResult(DatabaseHelper helper) {
        try {
            Dao<ExtraCharge, Long> extraChargeDao = helper.getDao(ExtraCharge.class);
            mOutTimeRule = extraChargeDao.queryBuilder().where().eq(ExtraCharge.$.code, "ZZCSF").queryForFirst();
            mServiceExtraCharge = extraChargeDao.queryBuilder().where().eq(ExtraCharge.$.code, ExtraManager.SERVICE_CONSUM).queryForFirst();
            extraChargeList = extraChargeDao.queryBuilder().where().eq(ExtraCharge.$.statusFlag, StatusFlag.VALID).and().ne(ExtraCharge.$.code, ExtraManager.SERVICE_CONSUM).query();
            extraChargeCommercialAreaRefList = helper.getDao(ExtraChargeCommercialAreaRef.class).queryForEq(ExtraChargeCommercialAreaRef.$.statusFlag, StatusFlag.VALID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isJinCh = false;

    /**
     * v7.16 金城 判断是否为金城商户
     */
    /*private void refreshPartnerShopResult(DatabaseHelper helper) {
        try {
            Dao<PartnerShopBiz, Long> partnerShopBizDao = helper.getDao(PartnerShopBiz.class);
            QueryBuilder<PartnerShopBiz, Long> partnerShopBizQueryBuilder = partnerShopBizDao.queryBuilder();
            List<PartnerShopBiz> jcBizs = partnerShopBizQueryBuilder.selectColumns(PartnerShopBiz.$.source, PartnerShopBiz.$.sourceName)
                    .where()
                    .eq(PartnerShopBiz.$.bizType, 6)
                    .and()
                    .eq(PartnerShopBiz.$.source, -61)
                    .and()
                    .eq(PartnerShopBiz.$.statusFlag, StatusFlag.VALID).query();
            isJinCh = !Utils.isEmpty(jcBizs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * v7.16 金城 判断是否为金城商户
     */
    public boolean isJinChBusiness() {
        return isJinCh;
    }

    /**
     * v8.15 判断是否开通第3方业务source
     */
    public boolean isOpenPartnerShop(Integer source, Integer bizType) {
        PartnerShopBiz partnerShopBiz = mPartnerShopBizMap.get(source);
        if (partnerShopBiz != null && partnerShopBiz.getBizType() == bizType) {
            return true;
        }
        return false;
    }

    public PartnerShopBiz getPartnerShop(Integer source) {
        PartnerShopBiz partnerShopBiz = null;
        partnerShopBiz = mPartnerShopBizMap.get(source);
        return partnerShopBiz;
    }

    /**
     * 刷新第3方平台开通情况 add v8.15
     */
    private void refreshPartnerShopBiz(DatabaseHelper helper) {
        try {
            Dao<PartnerShopBiz, Long> partnerShopBizDao = helper.getDao(PartnerShopBiz.class);
            QueryBuilder<PartnerShopBiz, Long> partnerShopBizQueryBuilder = partnerShopBizDao.queryBuilder();
            List<PartnerShopBiz> list = partnerShopBizQueryBuilder.selectColumns(PartnerShopBiz.$.bizType, PartnerShopBiz.$.source, PartnerShopBiz.$.sourceName)
                    .where().eq(PartnerShopBiz.$.statusFlag, StatusFlag.VALID).query();
            if (!Utils.isEmpty(list)) {
                mPartnerShopBizMap.clear();
                for (PartnerShopBiz partnerShopBiz : list) {
                    //判断是否是金城
                    if (partnerShopBiz.getSource() == -61 && partnerShopBiz.getBizType() == 6) {
                        isJinCh = true;
                    }
                    mPartnerShopBizMap.put(partnerShopBiz.getSource(), partnerShopBiz);
                }
            } else {
                mPartnerShopBizMap.clear();
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void refreshPartnerDeliveryPlatformConfig(DatabaseHelper helper) {
        //查询cash_handover_config数据库中的数据
        List<PartnerDeliveryPlatformConfig> partnerDeliveryPlatformConfigList = new ArrayList<PartnerDeliveryPlatformConfig>();

        try {
            partnerDeliveryPlatformConfigList = helper.getDao(PartnerDeliveryPlatformConfig.class)
                    .queryBuilder()
                    .selectColumns(PartnerDeliveryPlatformConfig.$.deliveryPlatform,
                            PartnerDeliveryPlatformConfig.$.type,
                            PartnerDeliveryPlatformConfig.$.isAutoAddTip,
                            PartnerDeliveryPlatformConfig.$.autoAddMoney,
                            PartnerDeliveryPlatformConfig.$.intervalTime)
                    .where().eq(PartnerDeliveryPlatformConfig.$.statusFlag, StatusFlag.VALID).query();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        //遍历返回结果，将其存入List
        mPartnerDeliveryPlatformConfigs.clear();
        if (Utils.isNotEmpty(partnerDeliveryPlatformConfigList)) {
            mPartnerDeliveryPlatformConfigs.addAll(partnerDeliveryPlatformConfigList);
        }
    }

    //会员登录密码开关 add v8.8
    public boolean isCommercialNeedVerifPassword() {
        String value = mCommercialCustomSettingsMap.get("shop.memberPassInput.switch");
        return "1".equals(value);
    }


    /**
     * 是否在储值时使用储值折扣
     * @return
     */
    public boolean isChargePrivilegeWhenPay() {
        String value = mCommercialCustomSettingsMap.get("IS_NEED_SAVE_PAYMENT");
        return "1".equals(value);
    }

    /**
     * 会员支付是否需要密码
     * @return
     */
    public boolean isNeedMemberPayPWDVerify(){
        String value = mCommercialCustomSettingsMap.get("IS_CKECK_PASSWORD_DOPAY");
        return "1".equals(value);
    }

    // v8.12.0 会员日计算
    private CrmMemberDay crmMemberDay;

    public CrmMemberDay getCrmMemberDay() {
        return crmMemberDay;
    }

    /**
     * 刷新数据
     */
    private void refreshMemberDay() {
        try {
            DatabaseHelper helper = DBHelperManager.getHelper();
            Dao<CrmMemberDay, Long> crmMemberDayLongDao = helper.getDao(CrmMemberDay.class);
            List<CrmMemberDay> crmMemberDayList = crmMemberDayLongDao.queryForAll();
            if (EmptyUtils.isNotEmpty(crmMemberDayList)) {
                crmMemberDay = crmMemberDayList.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshPosConfig() {
        try {
            DatabaseHelper helper = DBHelperManager.getHelper();
            PosConfig posConfig = helper.getDao(PosConfig.class)
                    .queryBuilder()
                    .selectColumns(PosConfig.$.value)
                    .where().eq(PosConfig.$.key, SnackConstant.OfflineEnableSwitch.KEY_OFFLINE_ENABLE).queryForFirst();
            if (posConfig != null) {
                SnackConstant.OfflineEnableSwitch.recordSwitchValue(posConfig.getValue());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
