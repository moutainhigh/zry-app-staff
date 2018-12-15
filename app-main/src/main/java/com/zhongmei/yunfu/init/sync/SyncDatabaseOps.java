package com.zhongmei.yunfu.init.sync;

import com.j256.ormlite.dao.Dao;
import com.zhongmei.yunfu.db.entity.SyncMark;
import com.zhongmei.yunfu.init.sync.bean.SyncModule;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.IEntity;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.init.sync.bean.SyncContent;
import com.zhongmei.yunfu.init.sync.bean.SyncItem;
import com.zhongmei.yunfu.init.sync.bean.SyncResponse;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**

 */
class SyncDatabaseOps {

    private static final SyncDatabaseOps INSTANCE = new SyncDatabaseOps();

    static SyncDatabaseOps getInstance() {
        return INSTANCE;
    }

    private SyncDatabaseOps() {
    }

    public static String getMarkerKey(Class genericClazz) {
        return DBHelperManager.getTableName(genericClazz);
    }

    /**
     * 获取同步请求的content
     *
     * @param modules     同步请求上行中包含的表名称
     * @param syncMarkMap
     * @return
     */
    public SyncContent querySyncContent(final List<String> modules, final Map<String, String> syncMarkMap) throws Exception {
        final SyncContent content = new SyncContent();
        SyncContent.callSyncContentFields(content.getClass(), new SyncContent.SyncContentFieldCall() {
            @Override
            public void onCall(Field field, Class<?> genericClazz) {
                String moduleName = SyncContent.getModuleName(field, genericClazz);
                if (modules.contains(moduleName)) {
                    SyncItem<?> syncItem = createSyncItem(genericClazz, syncMarkMap);
                    if (syncItem != null) {
                        try {
                            field.setAccessible(true);
                            field.set(content, syncItem);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

        return content;
    }

    public static Map<String, String> getSyncMarkMap() throws SQLException {
        final Map<String, String> map = new HashMap<>();
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<SyncMark, String> dao = helper.getDao(SyncMark.class);
            List<SyncMark> list = dao.queryForAll();
            for (SyncMark syncMark : list) {
                map.put(syncMark.getUuid(), syncMark.getLastSyncMarker());
            }
        } catch (Exception e) {
            SyncServiceUtil.error(e, "query SyncMark error! ");
            throw new SQLException(e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return map;
    }

    /**
     * 将response中的数据存入数据库
     *
     * @param syncMarkMap
     * @param response
     * @param isInit      为true时表示是初始化
     * @throws Exception
     */
    public void save(SyncModule syncModule, List<String> modules, Map<String, String> syncMarkMap, SyncResponse response, boolean isInit) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            syncData(helper, syncModule, modules, syncMarkMap, response, isInit);
        } catch (Exception e) {
            SyncServiceUtil.error(e, "Save to db error!");
            throw e;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    private synchronized void syncData(final DatabaseHelper helper,
                                       final SyncModule syncModule,
                                       final List<String> modules,
                                       final Map<String, String> syncMarkMap,
                                       final SyncResponse syncContent,
                                       final boolean isInit) throws Exception {
        /*callSyncContentFieldsAndValClear(syncModule, modules, syncMarkMap, syncContent, new SyncContentFieldCall() {
            @Override
            public void onCall(Field field, SyncItem<IEntity<?>> syncItem, Class<IEntity<?>> clazz) throws Exception {
                doSyncCallable(helper, clazz, syncItem);
            }
        });*/

        helper.callInTransaction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                callSyncContentFieldsAndValClear(syncModule, modules, syncMarkMap, syncContent, new SyncContentFieldCall() {
                    @Override
                    public void onCall(Field field, SyncItem<IEntity<?>> syncItem, Class<IEntity<?>> clazz) throws Exception {
                        doSync(helper, clazz, syncItem, isInit);
                    }
                });
                return null;
            }
        });

        // 初始化时不通知数据变更
        if (isInit) {
            helper.getChangeSupportable().clearChange();
        }
    }

    interface SyncContentFieldCall {

        void onCall(Field field, SyncItem<IEntity<?>> syncItem, Class<IEntity<?>> clazz) throws Exception;
    }

    private static void callSyncContentFieldsAndValClear(SyncModule syncModule, final List<String> modules, final Map<String, String> syncMarkMap, final SyncResponse syncResponse, final SyncContentFieldCall call) throws Exception {
        final List<String> removeModule = new ArrayList<>();
        final SyncContent syncContent = syncResponse.getContent();
        SyncContent.callSyncContentFields(syncContent.getClass(), new SyncContent.SyncContentFieldCall() {
            @Override
            public void onCall(Field field, Class<?> genericClazz) {
                String moduleName = SyncContent.getModuleName(field, genericClazz);
                try {
                    field.setAccessible(true);
                    SyncItem<?> syncItem = (SyncItem<?>) field.get(syncContent);
                    if (syncItem != null) {
                        String markKey = getMarkerKey(genericClazz);
                        String lastSyncMarker = syncMarkMap.get(markKey);
                        boolean lastSyncMarkerEq = lastSyncMarker == null ? syncItem.getLastSyncMarker() == null : lastSyncMarker.equals(syncItem.getLastSyncMarker());
                        if (!lastSyncMarkerEq && IEntity.class.isAssignableFrom(genericClazz)) {
                            List<?> itemData = syncItem.getDatas();
                            if (itemData != null && itemData.size() > 0) {
                                if (call != null) {
                                    SyncItem<IEntity<?>> itemEntity = (SyncItem<IEntity<?>>) syncItem;
                                    Class<IEntity<?>> entityClass = (Class<IEntity<?>>) genericClazz;
                                    call.onCall(field, itemEntity, entityClass);
                                }
                                return;
                            }
                        }
                    }

                    modules.remove(moduleName);
                    removeModule.add(moduleName);
                } catch (Exception e) {
                    SyncServiceUtil.error(e, "Module Name:" + moduleName);
                    throw new RuntimeException(e);
                }
            }
        });
        if (removeModule.size() > 0) {
            SyncServiceUtil.info("remove module[%d]: %s", removeModule.size(), removeModule);
            if (!syncResponse.isSyncSuccess()) {
                syncModule.putSyncModules(removeModule);
            }
        }
    }

    private <T extends IEntity<?>> void doSync(DatabaseHelper helper, Class<T> entityClass, SyncItem<T> syncItem, boolean isInit) throws Exception {
        if (isNotEmpty(syncItem)) {
            SyncServiceUtil.info((isInit ? "init" : "sync") + "..." + entityClass.getSimpleName());
            DBHelperManager.saveEntities(helper, entityClass, syncItem.getDatas());
            String tableName = DBHelperManager.getTableName(entityClass);
            saveSyncMark(helper, tableName, syncItem.getLastSyncMarker());
        }
    }

    private <T extends IEntity<?>> void doSyncCallable(final DatabaseHelper helper, final Class<T> entityClass, final SyncItem<T> syncItem) throws SQLException {
        if (isNotEmpty(syncItem)) {
            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    SyncServiceUtil.info("sync..." + entityClass.getSimpleName());
                    DBHelperManager.saveEntities(helper, entityClass, syncItem.getDatas());
                    String tableName = DBHelperManager.getTableName(entityClass);
                    saveSyncMark(helper, tableName, syncItem.getLastSyncMarker());
                    return null;
                }
            });
        }
    }

    public static void saveSyncMark(DatabaseHelper helper, String tableName, String lastSyncMarker) throws Exception {
        Dao<SyncMark, String> syncMarkDao = helper.getDao(SyncMark.class);
        SyncMark syncMark = syncMarkDao.queryForId(tableName);
        if (syncMark == null) {
            syncMark = createSyncMark(tableName);
            syncMark.setLastSyncMarker(lastSyncMarker);
            syncMarkDao.create(syncMark);
        } else {
            syncMark.setLastSyncMarker(lastSyncMarker);
            syncMark.setClientUpdateTime(System.currentTimeMillis());
            syncMarkDao.update(syncMark);
        }
    }

    private <T> SyncItem<T> createSyncItem(Class<T> entityClass, Map<String, String> markerFinder) {
        SyncItem<T> syncItem = new SyncItem<>();
        String lastSyncMarker = markerFinder.get(DBHelperManager.getTableName(entityClass));
        if (lastSyncMarker == null) {
            lastSyncMarker = "0:0";
        }
        syncItem.setLastSyncMarker(lastSyncMarker);
        return syncItem;
    }

    private static SyncMark createSyncMark(String tableName) {
        SyncMark syncMark = new SyncMark();
        syncMark.setUuid(tableName);
        syncMark.setClientCreateTime(System.currentTimeMillis());
        syncMark.setStatusFlag(StatusFlag.VALID);
        return syncMark;
    }

    private boolean isNotEmpty(SyncItem<?> syncItem) {
        return syncItem != null
                && syncItem.getDatas() != null
                && !syncItem.getDatas().isEmpty();
    }

}
