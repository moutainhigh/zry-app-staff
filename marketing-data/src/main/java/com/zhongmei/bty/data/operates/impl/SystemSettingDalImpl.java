package com.zhongmei.bty.data.operates.impl;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.bty.basemodule.commonbusiness.entity.CommercialCustomSettings;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;
import com.zhongmei.bty.basemodule.commonbusiness.entity.SyncDict;
import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.yunfu.http.OpsRequest.SaveDatabaseResponseProcessor;
import com.zhongmei.bty.basemodule.commonbusiness.message.CommercialCustomSettingsReq;
import com.zhongmei.bty.basemodule.commonbusiness.operates.SystemSettingDal;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.basemodule.database.db.CashHandoverConfig;
import com.zhongmei.bty.basemodule.pay.bean.ElectronicInvoiceVo;
import com.zhongmei.bty.basemodule.pay.entity.ElectronicInvoice;
import com.zhongmei.bty.basemodule.pay.entity.InvoiceTaxRate;
import com.zhongmei.bty.basemodule.shopmanager.closing.entity.ClosingAccountRecord;
import com.zhongmei.yunfu.db.entity.OpenTime;
import com.zhongmei.yunfu.db.enums.IsNextDay;
import com.zhongmei.bty.basemodule.shopmanager.message.OpenAndCloseReq;
import com.zhongmei.bty.basemodule.shopmanager.paymentmanager.entitys.AccountSubjectVo;
import com.zhongmei.bty.basemodule.shopmanager.paymentmanager.entitys.SubjectInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeDealSettingVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeDealSetting;
import com.zhongmei.bty.basemodule.trade.entity.TradeDealSettingItem;
import com.zhongmei.bty.basemodule.trade.message.TableNumberSettingReq;
import com.zhongmei.bty.basemodule.trade.message.TableNumberSettingResp;
import com.zhongmei.yunfu.util.Checks;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.bty.commonmodule.data.db.AccountSubject;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.resp.data.MindTransferResp;
import com.zhongmei.yunfu.resp.data.TransferReq;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.commonmodule.database.entity.InitSystem;
import com.zhongmei.bty.commonmodule.database.entity.LydMarketSetting;
import com.zhongmei.bty.commonmodule.database.entity.TableNumberSetting;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.commonmodule.database.enums.InitSystemDeviceType;
import com.zhongmei.yunfu.db.enums.IsDelete;
import com.zhongmei.bty.commonmodule.database.enums.Status;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.commonmodule.database.enums.SubjectType;
import com.zhongmei.bty.commonmodule.database.enums.TradeDealSettingBusinessType;
import com.zhongmei.bty.commonmodule.database.enums.TradeDealSettingOperateType;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.data.operates.message.content.CommercialSettingsReq;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

/**

 */
public class SystemSettingDalImpl extends AbstractOpeartesImpl implements SystemSettingDal {

    private final static String TAG = SystemSettingDalImpl.class.getSimpleName();

    public SystemSettingDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void openAndCloseBusiness(OpenAndCloseReq req, ResponseListener<Object> listener) {
        String url = ServerAddressUtil.getInstance().openAndCloseBusiness();
        OpsRequest.Executor<OpenAndCloseReq, Object> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(Object.class).execute(listener, "openAndCloseBusiness");
    }

    @Override
    public void commercialSettings(List<CommercialCustomSettings> commercialCustomSettings, ResponseListener<List<CommercialCustomSettings>> listener) {
        CommercialSettingsReq req = new CommercialSettingsReq();
        req.setSettingItems(commercialCustomSettings);

        String url = ServerAddressUtil.getInstance().commercialSettings();
        OpsRequest.Executor<CommercialSettingsReq, List<CommercialCustomSettings>> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseType(OpsRequest.getListContentResponseType(CommercialCustomSettings.class))
                .responseProcessor(new CommercialCustomSettingsProcessor())
                .execute(listener, "commercialSettings");
    }

    /***
     * 门店设置数据存储
     */
    private static class CommercialCustomSettingsProcessor extends SaveDatabaseResponseProcessor<List<CommercialCustomSettings>> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final List<CommercialCustomSettings> resp) {
            return new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    if (Utils.isNotEmpty(resp)) {
                        for (CommercialCustomSettings commercialCustomSettings : resp) {
                            DBHelperManager.saveEntities(helper, CommercialCustomSettings.class, commercialCustomSettings);
                        }
                    }
                    return null;
                }
            };
        }

    }

    @Override
    public Date getBizClosingTime() throws Exception {
        Date now = new Date();

        Date onlyDate = DateTimeUtils.onlyDate(now);

        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<OpenTime, Long> openTimeDao = helper.getDao(OpenTime.class);
            QueryBuilder<OpenTime, Long> qb = openTimeDao.queryBuilder();
            qb.where().eq(OpenTime.$.status, Status.VALID);
            qb.orderBy(OpenTime.$.modifyTime, false);
            List<OpenTime> openTimes = qb.query();
            if (openTimes != null && openTimes.size() > 0) {
                OpenTime openTime = openTimes.get(0);
                String closingTime = openTime.getClosingTime();
                IsNextDay isNextDay = openTime.getIsNextDay();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault());
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date close = dateTimeFormat.parse(dateFormat.format(now) + closingTime);
                return close;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return onlyDate;
    }

    @Override
    public Date getCurrentBizDate() {
        Date now = new Date();

        Date onlyDate = DateTimeUtils.onlyDate(now);

        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<OpenTime, Long> openTimeDao = helper.getDao(OpenTime.class);
            QueryBuilder<OpenTime, Long> qb = openTimeDao.queryBuilder();
            qb.where().eq(OpenTime.$.status, Status.VALID);
            qb.orderBy(OpenTime.$.modifyTime, false);
            List<OpenTime> openTimes = qb.query();
            if (openTimes != null && openTimes.size() > 0) {
                OpenTime openTime = openTimes.get(0);
                String closingTime = openTime.getClosingTime();
                IsNextDay isNextDay = openTime.getIsNextDay();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault());
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date close = dateTimeFormat.parse(dateFormat.format(now) + closingTime);

                // 跨天
                if (isNextDay == IsNextDay.YES) {
                    // 比歇业时间大，取今天日期
                    if (now.after(close)) {
                        return onlyDate;
                        // 比歇业时间小，取昨天日期
                    } else {
                        // 减一天
                        Calendar c = Calendar.getInstance();
                        c.setTime(onlyDate);
                        c.add(Calendar.DAY_OF_MONTH, -1);
                        return c.getTime();
                    }
                    // 不跨天
                } else {
                    // 比歇业时间大，取明天日期
                    if (now.after(close)) {
                        // 加一天
                        Calendar c = Calendar.getInstance();
                        c.setTime(onlyDate);
                        c.add(Calendar.DAY_OF_MONTH, +1);
                        return c.getTime();
                        // 比歇业时间小，取今天日期
                    } else {
                        return onlyDate;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return onlyDate;
    }

    @Override
    public TradeDealSettingVo findTradeDealSetting(TradeDealSettingOperateType tradeDealSettingOperateType,
                                                   TradeDealSettingBusinessType tradeDealSettingBusinessType) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            TradeDealSettingVo tradeDealSettingVo = new TradeDealSettingVo();
            Dao<TradeDealSetting, String> tradeDealSettingDao = helper.getDao(TradeDealSetting.class);
            QueryBuilder<TradeDealSetting, String> qb = tradeDealSettingDao.queryBuilder();
            qb.where()
                    .eq(TradeDealSetting.$.operateType, tradeDealSettingOperateType)
                    .and()
                    .eq(TradeDealSetting.$.businessType, tradeDealSettingBusinessType)
                    .and()
                    .eq(TradeDealSetting.$.statusFlag, StatusFlag.VALID);
            qb.orderBy(TradeDealSetting.$.serverUpdateTime, false);
            TradeDealSetting tradeDealSetting = qb.queryForFirst();
            tradeDealSettingVo.setTradeDealSetting(tradeDealSetting);

            if (tradeDealSetting != null) {
                // 交易自动处理时段表
                List<TradeDealSettingItem> tradeDealSettingItems =
                        helper.getDao(TradeDealSettingItem.class).queryForEq(TradeDealSettingItem.$.settingId,
                                tradeDealSetting.getId());
                tradeDealSettingVo.setTradeDealSettingItems(tradeDealSettingItems);
            }
            return tradeDealSettingVo;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<TradeDealSetting> listDinnerSetting() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeDealSetting, String> dao = helper.getDao(TradeDealSetting.class);
            QueryBuilder<TradeDealSetting, String> qb = dao.queryBuilder();
            qb.where()
                    .eq(TradeDealSetting.$.businessType, TradeDealSettingBusinessType.DINNER)
                    .and()
                    .eq(TradeDealSetting.$.statusFlag, StatusFlag.VALID);
            qb.orderBy(TradeDealSetting.$.serverUpdateTime, true);
            return qb.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public void handoverClean(CashHandoverConfig config, ResponseListener<CashHandoverConfig> listener) {
        String url = ServerAddressUtil.getInstance().handoverCleanSet();
        OpsRequest.Executor<CashHandoverConfig, CashHandoverConfig> executor = OpsRequest.Executor.create(url);
        executor.requestValue(config)
                .responseClass(CashHandoverConfig.class)
                .responseProcessor(new HandoverSetProcessor())
                .execute(listener, "openAndCloseBusiness");

    }

    @Override
    public CashHandoverConfig findCashHandoverConfig(Integer configKey) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<CashHandoverConfig, Long> configDao = helper.getDao(CashHandoverConfig.class);
            List<CashHandoverConfig> configs = configDao.queryForEq(CashHandoverConfig.$.configKey, configKey);
            if (configs != null && configs.size() > 0) {
                return configs.get(0);
            } else {
                return null;
            }
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    /***
     *

     *
     */
    private static class HandoverSetProcessor extends SaveDatabaseResponseProcessor<CashHandoverConfig> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final CashHandoverConfig resp) {
            return new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    DBHelperManager.saveEntities(helper, CashHandoverConfig.class, resp);
                    return null;
                }
            };
        }

    }

    @Override
    public void setTableNumberSetting(List<TableNumberSetting> tableNumberSettings, ResponseListener<TableNumberSettingResp> listener) {
        String url = ServerAddressUtil.getInstance().tableNumberSetting();

        TableNumberSettingReq req = new TableNumberSettingReq();
        req.setTableNumberSettings(tableNumberSettings);

        OpsRequest.Executor<TableNumberSettingReq, TableNumberSettingResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(TableNumberSettingResp.class)
                .responseProcessor(new TableNumberSettingProcessor())
                .execute(listener, "setTableNumberSetting");
    }

    private static class TableNumberSettingProcessor extends SaveDatabaseResponseProcessor<TableNumberSettingResp> {

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final TableNumberSettingResp resp) {
            return new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    DBHelperManager.saveEntities(helper, TableNumberSetting.class, resp.getTableNumberSettings());
                    return null;
                }
            };
        }

    }

    @Override
    public List<TableNumberSetting> listTableNumberSetting(boolean onlyValid) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TableNumberSetting, Long> dao = helper.getDao(TableNumberSetting.class);
            List<TableNumberSetting> tableNumberSettings = Collections.emptyList();
            if (onlyValid) {
                tableNumberSettings = dao.queryForEq(TableNumberSetting.$.statusFlag, StatusFlag.VALID);
            } else {
                tableNumberSettings = dao.queryForAll();
            }

            return tableNumberSettings;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<CashHandoverConfig> listCashConfigs() throws Exception {
        // TODO Auto-generated method stub
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<CashHandoverConfig, Long> configDao = helper.getDao(CashHandoverConfig.class);
            List<CashHandoverConfig> configs = configDao.queryForAll();
            return configs;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public AccountSubjectVo findAccoutSubject() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        AccountSubjectVo accountSubjectVo = new AccountSubjectVo();
        accountSubjectVo.setmExpensesSubject(new ArrayList<SubjectInfo>());
        accountSubjectVo.setmInComeSubject(new ArrayList<SubjectInfo>());
        try {
            Dao<AccountSubject, Long> configDao = helper.getDao(AccountSubject.class);
            QueryBuilder<AccountSubject, Long> subjects = configDao.queryBuilder();
            subjects.where().isNull(AccountSubject.$.parentId)
                    .and()
                    .eq(AccountSubject.$.statusFlag, StatusFlag.VALID);
            List<AccountSubject> parentSub = subjects.query();

            for (AccountSubject subject : parentSub) {
                //查询子科目
                subjects.where().eq(AccountSubject.$.parentId, subject.getId())
                        .and()
                        .eq(AccountSubject.$.statusFlag, StatusFlag.VALID);
                List<AccountSubject> childsubjects = subjects.query();

                List<SubjectInfo> childSubInfos = new ArrayList<SubjectInfo>();
                for (AccountSubject childSub : childsubjects) {
                    SubjectInfo childSubInfo = new SubjectInfo();
                    childSub.setParentId(subject.getId());
                    childSubInfo.setmSubject(childSub);
                    childSubInfo.setParent(false);
                    childSubInfos.add(childSubInfo);
                }

                //父科目信息
                SubjectInfo info = new SubjectInfo();
                info.setmSubject(subject);
                info.setParent(true);
                info.setChildSubInfo(childSubInfos);
                if (subject.getType() == SubjectType.INCOME) {
                    accountSubjectVo.getmInComeSubject().add(info);
                } else {
                    accountSubjectVo.getmExpensesSubject().add(info);
                }
            }

        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return accountSubjectVo;
    }

    @Override
    public CommercialCustomSettings findCommercialCustomSettings(String key) throws Exception {
        Checks.verifyNotNull(key, key);
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            return findCommercialCustomSettings(helper, key);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    private CommercialCustomSettings findCommercialCustomSettings(DatabaseHelper helper, String key) throws Exception {
        return helper.getDao(CommercialCustomSettings.class)
                .queryBuilder()
                .selectColumns(CommercialCustomSettings.$.value)
                .where()
                .eq(CommercialCustomSettings.$.key, key)
                .queryForFirst();
    }

    @Override
    public ClosingAccountRecord findClosingAccountRecord() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            ClosingAccountRecord record = helper.getDao(ClosingAccountRecord.class).queryBuilder().orderBy(ClosingAccountRecord.$.serverUpdateTime, false).queryForFirst();
            return record;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public ElectronicInvoiceVo findElectronicInvoiceVo() throws Exception {
        DatabaseHelper dbHelper = DBHelperManager.getHelper();
        try {
            ElectronicInvoiceVo electronicInvoiceVo = new ElectronicInvoiceVo();
            //获取开关
            CommercialCustomSettings commercialCustomSettings = findCommercialCustomSettings(dbHelper, "invoice.shop.switch");
            if (commercialCustomSettings != null && commercialCustomSettings.getValue() != null) {
                electronicInvoiceVo.setSwitchOn("1".equals(commercialCustomSettings.getValue()));
            } else {
                electronicInvoiceVo.setSwitchOn(false);
            }
            //获取电子发票设置信息
            Dao<ElectronicInvoice, String> electronicInvoiceDao = dbHelper.getDao(ElectronicInvoice.class);
            QueryBuilder<ElectronicInvoice, String> electronicInvoiceStringQueryBuilder = electronicInvoiceDao.queryBuilder();
            electronicInvoiceStringQueryBuilder.where().eq(ElectronicInvoice.$.statusFlag, StatusFlag.VALID);
            electronicInvoiceStringQueryBuilder.orderBy(ElectronicInvoice.$.serverUpdateTime, false);
            ElectronicInvoice electronicInvoice = electronicInvoiceStringQueryBuilder.queryForFirst();
            electronicInvoiceVo.setElectronicInvoice(electronicInvoice);
            //电子发票税率
            if (electronicInvoice != null) {
                Dao<InvoiceTaxRate, String> invoiceTaxRateDao = dbHelper.getDao(InvoiceTaxRate.class);
                List<InvoiceTaxRate> invoiceTaxRates = invoiceTaxRateDao.queryBuilder().where().eq(InvoiceTaxRate.$.invoiceId, electronicInvoice.getId()).query();
                electronicInvoiceVo.setInvoiceTaxRates(invoiceTaxRates);
            }

            return electronicInvoiceVo;
        } finally {
            DBHelperManager.releaseHelper(dbHelper);
        }
    }

    @Override
    public LydMarketSetting findMarketSetting(String configName) throws Exception {
        Checks.verifyNotNull(configName, configName);
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            return helper.getDao(LydMarketSetting.class)
                    .queryBuilder()
                    .where()
                    .eq(LydMarketSetting.$.configName, configName)
                    .and()
                    .eq(LydMarketSetting.$.statusFlag, StatusFlag.VALID)
                    .queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<PartnerShopBiz> queryPartnerShopBiz(int bizType, boolean onlyEnable) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<PartnerShopBiz, Long> partnerShopBizDao = helper.getDao(PartnerShopBiz.class);
            QueryBuilder<PartnerShopBiz, Long> partnerShopBizQueryBuilder = partnerShopBizDao.queryBuilder();
            partnerShopBizQueryBuilder.selectColumns(PartnerShopBiz.$.source, PartnerShopBiz.$.sourceName, PartnerShopBiz.$.enableFlag, PartnerShopBiz.$.bizType);
            Where<PartnerShopBiz, Long> partnerShopBizLongWhere = partnerShopBizQueryBuilder.where()
                    .eq(PartnerShopBiz.$.bizType, bizType)
                    .and()
                    .eq(PartnerShopBiz.$.statusFlag, StatusFlag.VALID);
            if (onlyEnable) {
                partnerShopBizLongWhere.and().eq(PartnerShopBiz.$.enableFlag, Bool.YES);
            }

            return partnerShopBizQueryBuilder.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<SyncDict> querySyncDict(int type) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<SyncDict, Long> syncDictDao = helper.getDao(SyncDict.class);
            QueryBuilder<SyncDict, Long> syncDictQueryBuilder = syncDictDao.queryBuilder();
            syncDictQueryBuilder.selectColumns(SyncDict.$.code, SyncDict.$.name);
            Where<SyncDict, Long> syncDictWhere = syncDictQueryBuilder.where();
            syncDictWhere.eq(SyncDict.$.statusFlag, StatusFlag.VALID);

            return syncDictQueryBuilder.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    /**
     * 设置交接方式
     *
     * @param type     交接类型
     * @param listener
     */
    @Override
    public void setHandoverType(int type, ResponseListener<MindTransferResp<CommercialCustomSettings>> listener) {
        String url = ServerAddressUtil.getInstance().mindTransfer();
        String mindUrl = ServerAddressUtil.getInstance().getSaveSettingURL();
        TransferReq<CommercialCustomSettingsReq> transferReq = new TransferReq<>(mindUrl, toHandoverTypeReq("shop.fast.handover.switch", type));
        OpsRequest.Executor<TransferReq, MindTransferResp<CommercialCustomSettings>> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq)
                .responseType(OpsRequest.getContentResponseType(MindTransferResp.class, CommercialCustomSettings.class))
                .responseProcessor(new HandoverTypeRespProcessor())
                .execute(listener, mindUrl);//用mind的url作为请求的tag
    }

    private CommercialCustomSettingsReq toHandoverTypeReq(String key, int value) {
        CommercialCustomSettingsReq req = new CommercialCustomSettingsReq();
        req.setBrandIdenty(BaseApplication.getInstance().getBrandIdenty());
        req.setShopIdenty(BaseApplication.getInstance().getShopIdenty());
        req.setKey(key);
        req.setValue(String.valueOf(value));
        return req;
    }

    private static class HandoverTypeRespProcessor extends SaveDatabaseResponseProcessor<MindTransferResp<CommercialCustomSettings>> {

        @Override
        protected Callable<Void> getCallable(DatabaseHelper helper, MindTransferResp<CommercialCustomSettings> resp) throws Exception {
            if (resp.isOk() && resp.isSuccess() && resp.getData() != null) {
                DBHelperManager.saveEntities(helper, CommercialCustomSettings.class, resp.getData());
            }
            return null;
        }
    }

    @Override
    public void updatePrintSort(int sort, ResponseListener<MindTransferResp<CommercialCustomSettings>> listener) {
        String url = ServerAddressUtil.getInstance().mindTransfer();
        String mindUrl = ServerAddressUtil.getInstance().getSaveSettingURL();
        TransferReq<CommercialCustomSettingsReq> transferReq = new TransferReq<>(mindUrl, toHandoverTypeReq("ticket.order.type", sort));
        OpsRequest.Executor<TransferReq, MindTransferResp<CommercialCustomSettings>> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq)
                .responseType(OpsRequest.getContentResponseType(MindTransferResp.class, CommercialCustomSettings.class))
                .responseProcessor(new HandoverTypeRespProcessor())
                .execute(listener, mindUrl);//用mind的url作为请求的tag
    }

    @Override
    public List<InitSystem> queryInitSystem(InitSystemDeviceType initSystemDeviceType) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<InitSystem, String> dao = helper.getDao(InitSystem.class);
            return dao.queryBuilder()
                    .selectColumns(InitSystem.$.padNo, InitSystem.$.deviceID, InitSystem.$.isMainPos)
                    .where().eq(InitSystem.$.status, IsDelete.VALID)
                    .and().eq(InitSystem.$.deviceType, InitSystemDeviceType.PAD)
                    .query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public void updateIsOpenShortName(int isOpen, ResponseListener<MindTransferResp<CommercialCustomSettings>> listener) {
        String url = ServerAddressUtil.getInstance().mindTransfer();
        String mindUrl = ServerAddressUtil.getInstance().getSaveSettingURL();
        TransferReq<CommercialCustomSettingsReq> transferReq = new TransferReq<>(mindUrl, toHandoverTypeReq("ticket_kitchen_is_open_short_name", isOpen));
        OpsRequest.Executor<TransferReq, MindTransferResp<CommercialCustomSettings>> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq)
                .responseType(OpsRequest.getContentResponseType(MindTransferResp.class, CommercialCustomSettings.class))
                .responseProcessor(new HandoverTypeRespProcessor())
                .execute(listener, mindUrl);//用mind的url作为请求的tag
    }

    @Override
    public void updateSaveSetting(CommercialCustomSettingsReq req, ResponseListener<MindTransferResp<CommercialCustomSettings>> listener) {
        String url = ServerAddressUtil.getInstance().mindTransfer();
        TransferReq<CommercialCustomSettingsReq> TransferReq = new TransferReq<CommercialCustomSettingsReq>();
        TransferReq.setPostData(req);
        TransferReq.setUrl(ServerAddressUtil.getInstance().mindUpdateSaveSetting());
        OpsRequest.Executor<TransferReq<CommercialCustomSettingsReq>, MindTransferResp<CommercialCustomSettings>> executor = OpsRequest.Executor.create(url);
        executor.requestValue(TransferReq)
                .responseType(OpsRequest.getContentResponseType(MindTransferResp.class, CommercialCustomSettings.class))
                .responseProcessor(new UpdateSaveSettingProcessor())
                .execute(listener, "updateSaveSetting");
    }

    /***
     * 门店设置数据存储
     */
    private static class UpdateSaveSettingProcessor extends SaveDatabaseResponseProcessor<MindTransferResp<CommercialCustomSettings>> {

        @Override
        protected Callable<Void> getCallable(DatabaseHelper helper, MindTransferResp<CommercialCustomSettings> resp) throws Exception {
            if (resp != null && resp.getData() != null) {
                DBHelperManager.saveEntities(helper, CommercialCustomSettings.class, resp.getData());
            }
            return null;
        }
    }
}
