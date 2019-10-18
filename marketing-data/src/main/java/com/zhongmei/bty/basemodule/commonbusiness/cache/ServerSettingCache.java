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



public class ServerSettingCache {
    private static final String TAG = ServerSettingCache.class.getSimpleName();

        private static final Uri URI_COMMERCIAL_CUSTOMER_SETTINGS = DBHelperManager.getUri(CommercialCustomSettings.class);

        private static final Uri URI_CASH_HANDOVER_CONFIG = DBHelperManager.getUri(CashHandoverConfig.class);

        private static final Uri URI_SYNC_DICT = DBHelperManager.getUri(SyncDict.class);

        private static final Uri URI_TRADE_DEAL_SETTING = DBHelperManager.getUri(TradeDealSetting.class);

        private static final Uri URI_TRADE_DEAL_SETTING_ITEM = DBHelperManager.getUri(TradeDealSettingItem.class);

        private static final Uri URI_EXTRA_CHARGE_OUTTIME = DBHelperManager.getUri(ExtraCharge.class);

    private static final Uri URI_EXTRA_CHARGE_AREA_REF_OUTTIME = DBHelperManager.getUri(ExtraChargeCommercialAreaRef.class);

        private static final Uri URI_EXTRA_PARTNER_SHOP_BIZ = DBHelperManager.getUri(PartnerShopBiz.class);

        private static final Uri URI_PARTNER_DELIVERY_PLATFORM_CONFIG = DBHelperManager.getUri(PartnerDeliveryPlatformConfig.class);

        private static final Uri URI_TAX_RATE_INFO = DBHelperManager.getUri(TaxRateInfo.class);

        private static final Uri URI_APP_PRIVILEGE = DBHelperManager.getUri(AppPrivilege.class);

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


    public Map<String, String> getCommercialCustomSettingsMap() {
        return mCommercialCustomSettingsMap;
    }


    public String getLimitServiceTime() {
        return mCommercialCustomSettingsMap.get("shop.buffet.time");
    }


    public boolean getBuffetTableStatus() {
        String value = mCommercialCustomSettingsMap.get("buffet.setting.buffetMode");
        return !"2".equals(value);
    }


    public boolean getDepositEnable() {
        String value = mCommercialCustomSettingsMap.get("trade.deposit.switch");
        return "1".equals(value);
    }


    public boolean getBuffetDepositEnable() {
        String value = mCommercialCustomSettingsMap.get("buffet.setting.deposit.switch");
        return "1".equals(value);
    }


    public boolean getBuffetOutTimeFeeEnable() {
        String value = mCommercialCustomSettingsMap.get("buffet.setting.otFee.switch");
        return "1".equals(value) && mOutTimeRule != null;
    }


    public OutTimeInfo getBuffetOutTimeFeeRule() {

        if (!getBuffetOutTimeFeeEnable()) {            return null;
        }

        OutTimeInfo outTimeInfo = new OutTimeInfo();
        outTimeInfo.setOutTimeUnit(mOutTimeRule.getContentByTime().longValue());
        outTimeInfo.setOutTimeFee(new BigDecimal(mOutTimeRule.getContent()));
        outTimeInfo.setLimitTimeLine(getBuffetTimeLimitLine());

        return outTimeInfo;
    }



    public long getBuffetTimeLimitLine() {
        String value = mCommercialCustomSettingsMap.get("buffet.setting.dinnerTime");
        if (TextUtils.isEmpty(value)) {
            return 0;
        }
        return Integer.parseInt(value);
    }



    public DepositInfo getBuffetTradeDeposit() {
        DepositInfo depositInfo = new DepositInfo();

                String depositType = mCommercialCustomSettingsMap.get("buffet.setting.deposit.type");
        if (TextUtils.isEmpty(depositType)) {
            depositInfo.setType(DepositInfo.TYPE_BY_PEOPLE);
        } else {
            depositInfo.setType(Integer.valueOf(depositType));
        }

                String depositValue = mCommercialCustomSettingsMap.get("buffet.setting.deposit");
        if (!TextUtils.isEmpty(depositValue)) {
            depositInfo.setValue(new BigDecimal(depositValue));
        }

        return depositInfo;
    }


    public DepositInfo getTradeDeposit() {
        DepositInfo depositInfo = new DepositInfo();

                String depositType = mCommercialCustomSettingsMap.get("trade.deposit.type");
        if (TextUtils.isEmpty(depositType)) {
            depositInfo.setType(DepositInfo.TYPE_BY_PEOPLE);
        } else {
            depositInfo.setType(Integer.valueOf(depositType));
        }

                String depositValue = mCommercialCustomSettingsMap.get("trade.deposit");
        if (!TextUtils.isEmpty(depositValue)) {
            depositInfo.setValue(new BigDecimal(depositValue));
        }

        return depositInfo;
    }


    public boolean checkAppService(String appCode) {
        if (mAppPrivilegeMap != null) {
            return mAppPrivilegeMap.get(appCode) != null ? mAppPrivilegeMap.get(appCode) : false;
        }
        return false;
    }


    public boolean getDadaSwitchEnable() {
        String value = mCommercialCustomSettingsMap.get("shop.thirdDeliveryPlat.dadaSwitch");
        return "1".equals(value);
    }


    public SparseIntArray getCashHandoverConfigArray() {
        return mCashHandoverConfigArray;
    }


    public SparseArray<String> getOrderSourceArray() {
        return mOrderSourceArray;
    }


    public List<TradeDealSettingVo> getTradeDealSettingVos() {
        return mTradeDealSettingVos;
    }

    public List<PartnerDeliveryPlatformConfig> getPartnerDeliveryPlatformConfigs() {
        return mPartnerDeliveryPlatformConfigs;
    }


    private Map<String, String> mCommercialCustomSettingsMap;


    private SparseIntArray mCashHandoverConfigArray;


    private SparseArray<String> mOrderSourceArray;


    private ExtraCharge mOutTimeRule;

        private ExtraCharge mServiceExtraCharge;

        private List<ExtraCharge> extraChargeList;

        private List<ExtraChargeCommercialAreaRef> extraChargeCommercialAreaRefList;


    private List<TradeDealSettingVo> mTradeDealSettingVos;


    private ConcurrentHashMap<Integer, PartnerShopBiz> mPartnerShopBizMap;        private TaxRateInfo mTaxRateInfo;

        private Map<String, Boolean> mAppPrivilegeMap;


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
                                                if (uris.contains(URI_EXTRA_PARTNER_SHOP_BIZ)) {
                                                        refreshPartnerShopBiz(helper);                        }

                        if (uris.contains(URI_PARTNER_DELIVERY_PLATFORM_CONFIG)) {
                            refreshPartnerDeliveryPlatformConfig(helper);
                        }

                        if (uris.contains(URI_TAX_RATE_INFO)) {
                            refreshTaxRateInfo(helper);
                        }

                        if (uris.contains(URI_APP_PRIVILEGE)) {

                        }
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


    private void registerContentObserver() {
        if (mDataChangeObserver == null) {
            mDataChangeObserver = new ServerSettingObserver();
        }
        DatabaseHelper.Registry.register(mDataChangeObserver);
    }


    private void unregisterContentObserver() {
        if (mDataChangeObserver != null) {
            DatabaseHelper.Registry.unregister(mDataChangeObserver);
            mDataChangeObserver = null;
        }
    }


    private void refreshTaxRateInfo(DatabaseHelper helper) {
        try {
            mTaxRateInfo = helper.getDao(TaxRateInfo.class)
                    .queryBuilder()
                    .where().eq(TaxRateInfo.$.statusFlag, StatusFlag.VALID).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void refreshAppPrivilege(DatabaseHelper helper) {
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

                mAppPrivilegeMap.clear();
        if (Utils.isNotEmpty(appPrivileges)) {
            for (AppPrivilege appPrivilege : appPrivileges) {
                mAppPrivilegeMap.put(appPrivilege.getAppCode(), appPrivilege.getAvailable());
            }
        }
    }


    public synchronized void refreshAll() {
        unregisterContentObserver();
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            refreshCommercialCustomSettings(helper);
            refreshCashHandoverConfig(helper);
            refreshSyncDict(helper);
            refreshTradeDealSetting(helper);
            refreshOutTimeResult(helper);
            refreshPartnerShopBiz(helper);            refreshPartnerDeliveryPlatformConfig(helper);
            refreshTaxRateInfo(helper);
            refreshAppPrivilege(helper);
            refreshMemberDay();
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


    private void refreshCommercialCustomSettings(DatabaseHelper helper) {
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

                mCommercialCustomSettingsMap.clear();
        if (Utils.isNotEmpty(commercialCustomSettingsList)) {
            for (CommercialCustomSettings commercialCustomSettings : commercialCustomSettingsList) {
                mCommercialCustomSettingsMap.put(commercialCustomSettings.getSettingKey(), commercialCustomSettings.getSettingValue());
            }
        }
    }


    private void refreshCashHandoverConfig(DatabaseHelper helper) {
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

                mCashHandoverConfigArray.clear();
        if (Utils.isNotEmpty(cashHandoverConfigList)) {
            for (CashHandoverConfig cashHandoverConfig : cashHandoverConfigList) {
                mCashHandoverConfigArray.put(cashHandoverConfig.getConfigKey(), cashHandoverConfig.getConfigValue());
            }
        }
    }


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

                mOrderSourceArray.clear();
        if (Utils.isNotEmpty(syncDictList)) {
            for (SyncDict syncDict : syncDictList) {
                                if (syncDict.getType().intValue() == 1 || syncDict.getType().intValue() == 2) {
                    mOrderSourceArray.put(syncDict.getCode(), syncDict.getName());
                }
            }
        }
    }


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
                                        List<TradeDealSettingItem> tradeDealSettingItems =
                            helper.getDao(TradeDealSettingItem.class).queryForEq(TradeDealSettingItem.$.settingId,
                                    tradeDealSetting.getId());

                    TradeDealSettingVo tradeDealSettingVo = new TradeDealSettingVo();
                    tradeDealSettingVo.setTradeDealSetting(tradeDealSetting);
                    tradeDealSettingVo.setTradeDealSettingItems(tradeDealSettingItems);
                    list.add(tradeDealSettingVo);
                }
                mTradeDealSettingVos = list;            }
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





    public boolean isJinChBusiness() {
        return isJinCh;
    }


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


    private void refreshPartnerShopBiz(DatabaseHelper helper) {
        try {
            Dao<PartnerShopBiz, Long> partnerShopBizDao = helper.getDao(PartnerShopBiz.class);
            QueryBuilder<PartnerShopBiz, Long> partnerShopBizQueryBuilder = partnerShopBizDao.queryBuilder();
            List<PartnerShopBiz> list = partnerShopBizQueryBuilder.selectColumns(PartnerShopBiz.$.bizType, PartnerShopBiz.$.source, PartnerShopBiz.$.sourceName)
                    .where().eq(PartnerShopBiz.$.statusFlag, StatusFlag.VALID).query();
            if (!Utils.isEmpty(list)) {
                mPartnerShopBizMap.clear();
                for (PartnerShopBiz partnerShopBiz : list) {
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

                mPartnerDeliveryPlatformConfigs.clear();
        if (Utils.isNotEmpty(partnerDeliveryPlatformConfigList)) {
            mPartnerDeliveryPlatformConfigs.addAll(partnerDeliveryPlatformConfigList);
        }
    }

        public boolean isCommercialNeedVerifPassword() {
        String value = mCommercialCustomSettingsMap.get("shop.memberPassInput.switch");
        return "1".equals(value);
    }



    public boolean isChargePrivilegeWhenPay() {
        String value = mCommercialCustomSettingsMap.get("IS_NEED_SAVE_PAYMENT");
        return "1".equals(value);
    }


    public boolean isNeedMemberPayPWDVerify(){
        String value = mCommercialCustomSettingsMap.get("IS_CKECK_PASSWORD_DOPAY");
        return "1".equals(value);
    }

        private CrmMemberDay crmMemberDay;

    public CrmMemberDay getCrmMemberDay() {
        return crmMemberDay;
    }


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
