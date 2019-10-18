package com.zhongmei.bty.commonmodule.database.entity.local;

import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.commonmodule.database.db.local.LocalDBManager;
import com.zhongmei.yunfu.db.IEntity;
import com.zhongmei.yunfu.db.UuidEntityBase;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


@DatabaseTable(tableName = AsyncUuidRecord.$.name)
public class AsyncUuidRecord implements IEntity<String> {

    public interface $ {
        String name = "async_uuid_record";
        String uuid = "uuid";
        String type = "type";
        String modifiedTime = "modify_time";
    }

    @DatabaseField(columnName = $.uuid, id = true)
    private String uuid;

    @DatabaseField(columnName = $.type)
    private String type;

    @DatabaseField(columnName = $.modifiedTime)
    private long modifiedTime;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(long modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String pkValue() {
        return uuid;
    }

    @Override
    public Long verValue() {
        return modifiedTime;
    }

    @Override
    public void checkNullValue() {

    }

    @Override
    public void checkNullIDValue() {

    }

    public static <T extends UuidEntityBase> AsyncUuidRecord inflate(T entity) {
        if (entity == null) {
            throw new NullPointerException("UuidEntityBase is null");
        }
        String uuid = entity.getUuid();
        String type = entity.getClass().getName();
        long modifiedTime = System.currentTimeMillis();

        AsyncUuidRecord record = new AsyncUuidRecord();
        record.setUuid(uuid);
        record.setType(type);
        record.setModifiedTime(modifiedTime);

        return record;
    }

    public static <T extends UuidEntityBase> boolean exist(T entity) throws SQLException {
        return entity != null && exist(entity.getUuid());
    }

    public static boolean exist(String uuid) throws SQLException {
        if (TextUtils.isEmpty(uuid)) {
            return false;
        }

        Map<String, Object> vars = new HashMap<>();
        vars.put($.uuid, uuid);

        Dao<AsyncUuidRecord, String> recordDao;
        DatabaseHelper helper;
        try {
            helper = LocalDBManager.getHelper();
            recordDao = helper.getDao(AsyncUuidRecord.class);
            return recordDao.idExists(uuid);
        } catch (Exception e) {
            return false;
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    public static <T extends UuidEntityBase> void record(T entity) throws SQLException {
        AsyncUuidRecord record = inflate(entity);
        if (record != null) {
            DatabaseHelper helper;
            try {
                helper = LocalDBManager.getHelper();
                Dao<AsyncUuidRecord, String> recordDao = helper.getDao(AsyncUuidRecord.class);
                recordDao.createIfNotExists(record);
            } catch (Exception e) {
            } finally {
                LocalDBManager.releaseHelper();
            }
        }
    }

}
