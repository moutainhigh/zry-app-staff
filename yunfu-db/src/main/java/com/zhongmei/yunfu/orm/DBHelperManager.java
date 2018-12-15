package com.zhongmei.yunfu.orm;

import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.Option;
import com.zhongmei.yunfu.db.IEntity;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.util.EmptyUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**

 *
 */
public class DBHelperManager {
    public static String LOCAL_DATABASE_HELPER = "local_databse_helper";
    public static String CALM_DATABSE_HELPER = "calm_database_helper";

    private static final String Tag = DBHelperManager.class.getSimpleName();

    private static Map<String, IDBHelperFunc> sDatabaseHelperMap = new HashMap<>();

    public static void initDatabaseHelper(Map<String, IDBHelperFunc> databaseHelperMap) {
        sDatabaseHelperMap = databaseHelperMap;
    }

    /**
     * 默认的情况下访问calmDatabaseHelper
     *
     * @return
     */
    public static DatabaseHelper getHelper() {
        return getHelper(DBHelperManager.CALM_DATABSE_HELPER);
    }

    /**
     * 创建一个{@link DatabaseHelper}对象。 使用完后应该调用
     * {releaseHelper()方法释放资源，否则会引起资源泄漏。
     *
     * @return
     */
    public static DatabaseHelper getHelper(String helperName) {
        DatabaseHelper databaseHelper = getDatabaseHelper(helperName);
        if (databaseHelper == null) {
            throw new NullPointerException("no db class match the helperName");
        }
        return databaseHelper;
    }

    private static DatabaseHelper getDatabaseHelper(String helperName) {
        if (sDatabaseHelperMap.containsKey(helperName)) {
            IDBHelperFunc databaseHelper = sDatabaseHelperMap.get(helperName);
            return databaseHelper.getHelper();
        }
        return null;
    }

    /***
     * 通过实体类getHelper
     * @param entityClass
     * @param <T>
     * @return
     */
    public static <T extends IEntity<?>> DatabaseHelper getHelper(Class<T> entityClass) {
        if (EmptyUtils.isEmpty(sDatabaseHelperMap)) {
            throw new RuntimeException("databaseHelperMap is empty");
        }

        for (IDBHelperFunc databaseHelper : sDatabaseHelperMap.values()) {
            if (databaseHelper.contains(entityClass)) {
                return databaseHelper.getHelper();
            }
        }

        throw new NullPointerException("table is not in databasehelper");
    }

    public static <T extends IEntity<?>> void releaseHelper(Class<T> entityClass, DatabaseHelper helper) {
        if (EmptyUtils.isEmpty(sDatabaseHelperMap)) {
            throw new RuntimeException("databaseHelperList is empty");
        }

        for (IDBHelperFunc databaseHelper : sDatabaseHelperMap.values()) {
            if (databaseHelper.contains(entityClass)) {
                databaseHelper.releaseHelper(helper);
            }
        }
    }

    /**
     * 释放DatabaseHelper资源
     */
    public static void releaseHelper(DatabaseHelper helper) {
        IDBHelperFunc idbHelperFunc = sDatabaseHelperMap.get(DBHelperManager.CALM_DATABSE_HELPER);
        if (idbHelperFunc != null) {
            idbHelperFunc.releaseHelper(helper);
        }
    }

    /**
     * 根据ID删除Entity对象
     *
     * @param entityClass
     * @param id
     * @return
     * @throws Exception
     */
    public static <ID, T extends IEntity<ID>> boolean deleteById(final Class<T> entityClass, final ID id) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper(entityClass);
        try {
            //更新后的数据插入数据库
            //modify 20170216 begin 统一调用事务实现线程同步
            final Dao<T, ID> dao = helper.getDao(entityClass);
            return helper.callInTransaction(new Callable<Boolean>() {
                @Override
                public Boolean call()
                        throws Exception {
                    boolean changed = dao.deleteById(id) == 1;
                    helper.getChangeSupportable().addChange(entityClass);
                    return changed;
                }
            });
            //modify 20170216 end
        } finally {
            DBHelperManager.releaseHelper(entityClass, helper);
        }
    }

    /**
     * 获取指定类对应的数据库表名称。 从{@link DatabaseTable}注解中获取tableName
     *
     * @param entityClass
     * @return
     */
    public static String getTableName(Class<?> entityClass) {
        DatabaseTable table = entityClass.getAnnotation(DatabaseTable.class);
        if (table != null) {
            return table.tableName();
        }
        return null;
    }

    /**
     * 获取指定类对应的数据库表的URI
     *
     * @param entityClass
     * @return
     */
    public static Uri getUri(Class<?> entityClass) {
        return Uri.parse(DataBaseUtils.getUriHeader() + getTableName(entityClass));
    }

    /**
     * 将Cursor的当前记录转为指定类型的对象
     *
     * @param entityClass
     * @param cursor
     * @return
     * @throws Exception
     */
    public static <T extends IEntity<?>> T convertToEntity(Class<T> entityClass, Cursor cursor) throws Exception {
        DatabaseHelper helper = getHelper(entityClass);
        try {
            Dao<T, ?> dao = helper.getDao(entityClass);
            return dao.mapSelectStarRow(new AndroidDatabaseResults(cursor, dao.getObjectCache()));
        } finally {
            releaseHelper(entityClass, helper);
        }
    }

    /**
     * 根据ID获取Entity对象
     *
     * @param entityClass
     * @param id
     * @return
     * @throws Exception
     */
    public static <ID, T extends IEntity<ID>> T queryById(Class<T> entityClass, ID id) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper(entityClass);
        try {
            Dao<T, ID> dao = helper.getDao(entityClass);
            return dao.queryForId(id);
        } finally {
            DBHelperManager.releaseHelper(entityClass, helper);
        }
    }

    /**
     * 根据字段名和value查询对象
     *
     * @param entityClass
     * @param field
     * @param value
     * @param <ID>
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <ID, T extends IEntity<ID>> List<T> queryByValue(Class<T> entityClass, String field, Object value) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper(entityClass);
        try {
            Dao<T, ID> dao = helper.getDao(entityClass);
            return dao.queryForEq(field, value);
        } finally {
//			releaseHelper(helper);
            DBHelperManager.releaseHelper(entityClass, helper);
        }
    }

    /**
     * 根据字段名和values查询对象
     *
     * @param entityClass
     * @param field
     * @param values
     * @param <ID>
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <ID, T extends IEntity<ID>> List<T> queryByValues(Class<T> entityClass, String field, Iterable<?> values) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper(entityClass);
        try {
            Dao<T, ID> dao = helper.getDao(entityClass);
            return dao.queryBuilder().where().in(field, values).query();
        } finally {
            DBHelperManager.releaseHelper(entityClass, helper);
        }
    }


    /**
     * 查询表中的记录数
     *
     * @Title: coutOf
     * @Param @param entityClass
     * @Param @return
     * @Return long 返回类型
     */
    public static <ID, T extends IEntity<ID>> long countOf(Class<T> entityClass) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper(entityClass);
        try {
            Dao<T, ID> dao = helper.getDao(entityClass);
            return dao.countOf();
        } finally {
            DBHelperManager.releaseHelper(entityClass, helper);
        }
    }

    /**
     * 将entityList中的数据保存到数据库中,不传helper,自动释放
     *
     * @param entityClass
     * @param entityList
     * @throws Exception
     */
    public static synchronized <T extends IEntity<?>> void saveEntities(Class<T> entityClass,
                                                                        List<T> entityList) throws Exception {
        DatabaseHelper helper = getHelper(entityClass);
        try {
            saveEntities(helper, entityClass, entityList);
        } catch (Exception e) {
            Log.i(Tag, e.getMessage());
        } finally {
            releaseHelper(entityClass, helper);
        }

    }

    public static <T extends IEntity<?>> void saveEntities(DatabaseHelper helper, Class<T> entityClass, T... entities) throws Exception {
        saveEntities(helper, entityClass, false, entities);
    }

    public static <T extends IEntity<?>> void saveEntities(DatabaseHelper helper, Class<T> entityClass, boolean recordEnable, T... entities) throws Exception {
        saveEntities(helper, entityClass, Arrays.asList(entities), recordEnable);
    }

    public static <T extends IEntity<?>> void saveEntities(DatabaseHelper helper, Class<T> entityClass, List<T> entityList) throws Exception {
        saveEntities(helper, entityClass, entityList, false);
    }

    public static <T extends IEntity<?>> void saveEntities(DatabaseHelper helper, Class<T> entityClass, List<T> entityList, boolean recordEnable) throws Exception {
        if (entityList != null && !entityList.isEmpty()) {
            boolean changed = false;
            Dao<T, Object> dao = helper.getDao(entityClass);
            for (T entity : entityList) {
                if (saveEntity(dao, entityClass, entity, recordEnable)) {
                    changed = true;
					/*if (entityClass.equals(NewDocumentTemplate.class)) {
						EventBus.getDefault().post(new DocumentTemplateEventBus());
					}*/
                }
            }
            if (changed) {
                DataDistributionCenter.deal(helper, entityClass, entityList);
                helper.getChangeSupportable().addChange(entityClass, entityList); // 记录数据已经修改
            }
        }
    }

    /**
     * 保存单个Entity对象
     *
     * @param dao
     * @param entityClass
     * @param entity
     * @return 返回true表示本地数据库有变更
     * @throws Exception
     */
    private static <T extends IEntity<?>> boolean saveEntity(Dao<T, Object> dao, Class<T> entityClass, T entity, boolean recordEnable) throws Exception {
        if (entity == null) {
            return false;
        }
        if (accept(entityClass, entity)) {
            // 要保存记录比现有记录新才执行保存
            entity.checkNullIDValue();
            T dbEntity = dao.queryForId(entity.pkValue());
            if (dbEntity != null) {
                if (gt(dbEntity.verValue(), entity.verValue())) {
                    return false;
                }
                if (needDelete(entity)) {
                    dao.deleteById(entity.pkValue());
                } else {
                    if (recordEnable) {
                        entity.checkNullValue();
                        dao.update(entity);
                    } else {
                        /*if (entity instanceof UuidEntityBase) {
                            if (!(AsyncUuidRecord.exist((UuidEntityBase) entity))) {
                                entity.checkNullValue();
                                dao.update(entity);
                            }
                        } else {*/
                        entity.checkNullValue();
                        dao.update(entity);
                        //}
                    }

                    //发送打印服务更新的广播
					/*if (entity instanceof PrinterDevice) {
						PrinterDevice printerServer = (PrinterDevice) entity;
						Log.d("save_print_server", "saveEntity printer: " + printerServer.getAddress() + "; type = " + printerServer.getPrinterDeviceType());
						if (printerServer.getPrinterDeviceType() == PrinterDeviceType.SERVER) {
							ActionPrinterServerChanged action = new ActionPrinterServerChanged();
							action.setIp(printerServer.getAddress());
							action.setDeviceIdentity(printerServer.getDeviceIdenty());
							SharedPreferenceUtil.getSpUtil().putString(CommonConstant.SP_PRINT_IP_ADDRESS, printerServer.getAddress());
							EventBus.getDefault().post(action);
						} else {
							ActionLocalPrinterChanged action = new ActionLocalPrinterChanged();
							EventBus.getDefault().post(action);
						}
					}*/
                }
                return true;
            } else if (!needDelete(entity)) {
                entity.checkNullValue();
                dao.create(entity);
                /*if (recordEnable) {
                    if (entity instanceof UuidEntityBase) {
                        UuidEntityBase uuidEntityBase = (UuidEntityBase) entity;
                        AsyncUuidRecord.record(uuidEntityBase);
                    }
                }*/
                return true;
            }
            return false;
        }
        return true;
    }

    private static <T extends IEntity<?>> boolean accept(Class<T> entityClass, T entity) {
        if (Trade.class == entityClass) {
            // 只保留快餐、正餐、外卖
            Trade trade = (Trade) entity;
            switch (trade.getBusinessType()) {
                case SNACK:
                case DINNER:
                case TAKEAWAY:
                case BUFFET:
                case GROUP:
                case POS_PAY://v8.5
                case BEAUTY:
                case ONLINE_RECHARGE:
                case CARD_TIME:
                    break;
                default:
                    return false;
            }
            return true;
        }

        // 只保留InitSystem中deviceType为1的数据
        /*if (InitSystem.class == entityClass) {
            InitSystem initSystem = (InitSystem) entity;
            switch (initSystem.getDeviceType()) {
                case PAD:
                    return true;
                default:
                    return false;
            }
        }*/

        // payment只保留已经支付的
		/*if (Payment.class == entityClass) {
			Payment payment = (Payment) entity;
			//return payment.getIsPaid() == Bool.YES;
			return true;
		}*/

        // TradeReasonRel 表由于后台没有uuid，所以用id补上
        if (TradeReasonRel.class == entityClass) {
            TradeReasonRel tradeReasonRel = (TradeReasonRel) entity;
            if (tradeReasonRel.getUuid() == null) {
                tradeReasonRel.setUuid(String.valueOf(tradeReasonRel.getId()));
            }
            return true;
        }
        if (entity instanceof Option) {
            Option optionEntityBase = (Option) entity;
            optionEntityBase.onOption();
        }

        return true;
    }

    /**
     * 是否需要物理删除
     *
     * @param entity
     * @return
     */
    private static <T extends IEntity<?>> boolean needDelete(T entity) {
        if (entity.isValid()) {
            return false;
        }
        // 以下表不物理删除
        if (entity.getClass() == Tables.class
                || entity.getClass() == TradeItem.class
                || entity.getClass() == TradeItemProperty.class
            //|| entity.getClass() == TradeItemLog.class
            //|| entity.getClass() == CommercialQueueConfigFile.class
            //|| entity.getClass() == TableNumberSetting.class
            //|| entity.getClass() == TradeTax.class
                ) {
            return false;
        }
        return true;
    }

    private static boolean gt(Long ver1, Long ver2) {
        if (ver1 == null) {
            return false;
        }
        if (ver2 == null) {
            return true;
        }
        return ver1 > ver2;
    }
}
