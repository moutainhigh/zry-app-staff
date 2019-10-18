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


public class DBHelperManager {
    public static String LOCAL_DATABASE_HELPER = "local_databse_helper";
    public static String CALM_DATABSE_HELPER = "calm_database_helper";

    private static final String Tag = DBHelperManager.class.getSimpleName();

    private static Map<String, IDBHelperFunc> sDatabaseHelperMap = new HashMap<>();

    public static void initDatabaseHelper(Map<String, IDBHelperFunc> databaseHelperMap) {
        sDatabaseHelperMap = databaseHelperMap;
    }


    public static DatabaseHelper getHelper() {
        return getHelper(DBHelperManager.CALM_DATABSE_HELPER);
    }


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


    public static void releaseHelper(DatabaseHelper helper) {
        IDBHelperFunc idbHelperFunc = sDatabaseHelperMap.get(DBHelperManager.CALM_DATABSE_HELPER);
        if (idbHelperFunc != null) {
            idbHelperFunc.releaseHelper(helper);
        }
    }


    public static <ID, T extends IEntity<ID>> boolean deleteById(final Class<T> entityClass, final ID id) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper(entityClass);
        try {
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
                    } finally {
            DBHelperManager.releaseHelper(entityClass, helper);
        }
    }


    public static String getTableName(Class<?> entityClass) {
        DatabaseTable table = entityClass.getAnnotation(DatabaseTable.class);
        if (table != null) {
            return table.tableName();
        }
        return null;
    }


    public static Uri getUri(Class<?> entityClass) {
        return Uri.parse(DataBaseUtils.getUriHeader() + getTableName(entityClass));
    }


    public static <T extends IEntity<?>> T convertToEntity(Class<T> entityClass, Cursor cursor) throws Exception {
        DatabaseHelper helper = getHelper(entityClass);
        try {
            Dao<T, ?> dao = helper.getDao(entityClass);
            return dao.mapSelectStarRow(new AndroidDatabaseResults(cursor, dao.getObjectCache()));
        } finally {
            releaseHelper(entityClass, helper);
        }
    }


    public static <ID, T extends IEntity<ID>> T queryById(Class<T> entityClass, ID id) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper(entityClass);
        try {
            Dao<T, ID> dao = helper.getDao(entityClass);
            return dao.queryForId(id);
        } finally {
            DBHelperManager.releaseHelper(entityClass, helper);
        }
    }


    public static <ID, T extends IEntity<ID>> List<T> queryByValue(Class<T> entityClass, String field, Object value) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper(entityClass);
        try {
            Dao<T, ID> dao = helper.getDao(entityClass);
            return dao.queryForEq(field, value);
        } finally {
            DBHelperManager.releaseHelper(entityClass, helper);
        }
    }


    public static <ID, T extends IEntity<ID>> List<T> queryByValues(Class<T> entityClass, String field, Iterable<?> values) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper(entityClass);
        try {
            Dao<T, ID> dao = helper.getDao(entityClass);
            return dao.queryBuilder().where().in(field, values).query();
        } finally {
            DBHelperManager.releaseHelper(entityClass, helper);
        }
    }



    public static <ID, T extends IEntity<ID>> long countOf(Class<T> entityClass) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper(entityClass);
        try {
            Dao<T, ID> dao = helper.getDao(entityClass);
            return dao.countOf();
        } finally {
            DBHelperManager.releaseHelper(entityClass, helper);
        }
    }


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

                }
            }
            if (changed) {
                DataDistributionCenter.deal(helper, entityClass, entityList);
                helper.getChangeSupportable().addChange(entityClass, entityList);             }
        }
    }


    private static <T extends IEntity<?>> boolean saveEntity(Dao<T, Object> dao, Class<T> entityClass, T entity, boolean recordEnable) throws Exception {
        if (entity == null) {
            return false;
        }
        if (accept(entityClass, entity)) {
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

                        entity.checkNullValue();
                        dao.update(entity);
                                            }


                }
                return true;
            } else if (!needDelete(entity)) {
                entity.checkNullValue();
                dao.create(entity);

                return true;
            }
            return false;
        }
        return true;
    }

    private static <T extends IEntity<?>> boolean accept(Class<T> entityClass, T entity) {
        if (Trade.class == entityClass) {
                        Trade trade = (Trade) entity;
            switch (trade.getBusinessType()) {
                case SNACK:
                case DINNER:
                case TAKEAWAY:
                case BUFFET:
                case GROUP:
                case POS_PAY:                case BEAUTY:
                case ONLINE_RECHARGE:
                case CARD_TIME:
                    break;
                default:
                    return false;
            }
            return true;
        }





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


    private static <T extends IEntity<?>> boolean needDelete(T entity) {
        if (entity.isValid()) {
            return false;
        }
                if (entity.getClass() == Tables.class
                || entity.getClass() == TradeItem.class
                || entity.getClass() == TradeItemProperty.class
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
