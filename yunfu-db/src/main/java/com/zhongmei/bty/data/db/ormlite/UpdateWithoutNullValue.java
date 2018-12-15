package com.zhongmei.bty.data.db.ormlite;

import android.util.Log;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableInfo;
import com.j256.ormlite.stmt.mapped.*;

/**
 * 实体所有的字段都是Object类型，如果实体的字段值为null，那么在update的时候将不更新改字段。它不同于BaseDaoImpl的update函数更新所有字段
 * id相等作为更新条件
 * 此为方案1
 */
public class UpdateWithoutNullValue<T, ID> extends BaseMappedStatement<T, ID> {

    public static <T> int update(Dao dao, T data) throws SQLException {
        if (dao == null || data == null) {
            return 0;
        }
        if (!(dao instanceof BaseDaoImpl)) {
            throw new SQLException("dao is not instanceof  BaseDaoImpl");
        }

        BaseDaoImpl bdao = (BaseDaoImpl) dao;
        DatabaseConnection connection = bdao.getConnectionSource().getReadWriteConnection();

        int res;
        try {
            UpdateWithoutNullValue mappedUpdate = UpdateWithoutNullValue.build(bdao.getConnectionSource().getDatabaseType(), bdao.getTableInfo(), data);
            res = mappedUpdate.update(connection, data, null);
        } finally {
            bdao.getConnectionSource().releaseConnection(connection);
        }
        return res;
    }

    private final FieldType versionFieldType;
    private final int versionFieldTypeIndex;

    private UpdateWithoutNullValue(TableInfo<T, ID> tableInfo, String statement, FieldType[] argFieldTypes,
                                   FieldType versionFieldType, int versionFieldTypeIndex) {
        super(tableInfo, statement, argFieldTypes);
        this.versionFieldType = versionFieldType;
        this.versionFieldTypeIndex = versionFieldTypeIndex;
    }

    public static <T, ID> UpdateWithoutNullValue<T, ID> build(DatabaseType databaseType, TableInfo<T, ID> tableInfo, T data)
            throws SQLException {
        FieldType idField = tableInfo.getIdField();
        if (idField == null) {
            throw new SQLException("Cannot update " + tableInfo.getDataClass() + " because it doesn't have an id field");
        }
        StringBuilder sb = new StringBuilder(64);
        appendTableName(databaseType, sb, "UPDATE ", tableInfo.getTableName());
        boolean first = true;
        int argFieldC = 0;
        FieldType versionFieldType = null;
        int versionFieldTypeIndex = -1;
        // first we count up how many arguments we are going to have
        for (FieldType fieldType : tableInfo.getFieldTypes()) {
            if (isFieldUpdatable(fieldType, idField, data)) {
                if (fieldType.isVersion()) {
                    versionFieldType = fieldType;
                    versionFieldTypeIndex = argFieldC;
                }
                argFieldC++;
            }
        }
        // one more for where id = ?
        argFieldC++;
        if (versionFieldType != null) {
            // one more for the AND version = ?
            argFieldC++;
        }
        FieldType[] argFieldTypes = new FieldType[argFieldC];
        argFieldC = 0;
        for (FieldType fieldType : tableInfo.getFieldTypes()) {
            if (!isFieldUpdatable(fieldType, idField, data)) {
                continue;
            }
            if (first) {
                sb.append("SET ");
                first = false;
            } else {
                sb.append(", ");
            }
            appendFieldColumnName(databaseType, sb, fieldType, null);
            argFieldTypes[argFieldC++] = fieldType;
            sb.append("= ?");
        }
        sb.append(' ');
        appendWhereFieldEq(databaseType, idField, sb, null);
        argFieldTypes[argFieldC++] = idField;
        if (versionFieldType != null) {
            sb.append(" AND ");
            appendFieldColumnName(databaseType, sb, versionFieldType, null);
            sb.append("= ?");
            argFieldTypes[argFieldC++] = versionFieldType;
        }
        return new UpdateWithoutNullValue<T, ID>(tableInfo, sb.toString(), argFieldTypes, versionFieldType, versionFieldTypeIndex);
    }

    /**
     * Update the object in the database.
     */
    public int update(DatabaseConnection databaseConnection, T data, ObjectCache objectCache) throws SQLException {
        try {
            // there is always and id field as an argument so just return 0 lines updated
            if (argFieldTypes.length <= 1) {
                return 0;
            }
            Object[] args = getFieldObjects(data);
            Object newVersion = null;
            if (versionFieldType != null) {
                newVersion = versionFieldType.extractJavaFieldValue(data);
                newVersion = versionFieldType.moveToNextValue(newVersion);
                args[versionFieldTypeIndex] = versionFieldType.convertJavaFieldToSqlArgValue(newVersion);
            }
            Log.i("sql", statement);
            int rowC = databaseConnection.update(statement, args, argFieldTypes);
            if (rowC > 0) {
                if (newVersion != null) {
                    // if we have updated a row then update the version field in our object to the new value
                    versionFieldType.assignField(data, newVersion, false, null);
                }
                if (objectCache != null) {
                    // if we've changed something then see if we need to update our cache
                    Object id = idField.extractJavaFieldValue(data);
                    T cachedData = objectCache.get(clazz, id);
                    if (cachedData != null && cachedData != data) {
                        // copy each field from the updated data into the cached object
                        for (FieldType fieldType : tableInfo.getFieldTypes()) {
                            if (fieldType != idField) {
                                fieldType.assignField(cachedData, fieldType.extractJavaFieldValue(data), false,
                                        objectCache);
                            }
                        }
                    }
                }
            }
            logger.debug("update data with statement '{}' and {} args, changed {} rows", statement, args.length, rowC);
            if (args.length > 0) {
                // need to do the (Object) cast to force args to be a single object
                logger.trace("update arguments: {}", (Object) args);
            }
            return rowC;
        } catch (SQLException e) {
            throw SqlExceptionUtil.create("Unable to run update stmt on object " + data + ": " + statement, e);
        }
    }

    private static <T> boolean isFieldUpdatable(FieldType fieldType, FieldType idField, T data) throws SQLException {

        if (fieldType == idField || fieldType.isForeignCollection() || fieldType.isReadOnly() || fieldType.extractRawJavaFieldValue(data) == null) {
            return false;
        } else {
            return true;
        }
    }

    static void appendWhereFieldEq(DatabaseType databaseType, FieldType fieldType, StringBuilder sb, List<FieldType> fieldTypeList) {
        sb.append("WHERE ");
        appendFieldColumnName(databaseType, sb, fieldType, fieldTypeList);
        sb.append("= ?");
    }

    static void appendTableName(DatabaseType databaseType, StringBuilder sb, String prefix, String tableName) {
        if (prefix != null) {
            sb.append(prefix);
        }

        databaseType.appendEscapedEntityName(sb, tableName);
        sb.append(' ');
    }

    static void appendFieldColumnName(DatabaseType databaseType, StringBuilder sb, FieldType fieldType, List<FieldType> fieldTypeList) {
        databaseType.appendEscapedEntityName(sb, fieldType.getColumnName());
        if (fieldTypeList != null) {
            fieldTypeList.add(fieldType);
        }

        sb.append(' ');
    }

    /*
    public static void test() {
        DatabaseHelper helper = DBHelper.getHelper();
        try {

            Log.i("sql","方案1测试－－－－－－－－－－－－－－");

            Dao<DishBrandProperty,Long> dao=helper.getDao(DishBrandProperty.class);
            DishBrandProperty a= dao.queryBuilder().queryForFirst();

            //测试1:所有字段为null
            DishBrandProperty b= new DishBrandProperty();
            int res=update(dao,b);
            Log.i("sql","测试1:所有字段为null，res="+res);

            //测试2:id错误，其它所有字段为null
            b= new DishBrandProperty();
            b.setId(Long.MIN_VALUE);
            res=update(dao,b);
            Log.i("sql","测试2:id错误，其它所有字段为null res="+res);

            //测试3:id正确，其它所有字段为null
            b= new DishBrandProperty();
            b.setId(a.getId());
            res=update(dao,b);
            Log.i("sql","测试3:id正确，其它所有字段为null res＝"+res);


            //测试4:id错误，DishName字段错误，其它所有字段为null
            b.setDishName("苹果1");
            b.setId(Long.MIN_VALUE);
            res=update(dao,b);
            Log.i("sql","测试4:id错误，DishName字段错误，其它所有字段为null res="+res);


            //测试5:id错误，DishName字段正确，其它所有字段为null
            b.setDishName("苹果1");
            b.setId(a.getId());
            res=update(dao,b);
            Log.i("sql","测试5:id错误，DishName字段正确，其它所有字段为null res="+res);


            //测试6:id错误，DishName和CreatorName字段正确，其它所有字段为null
            b.setDishName("苹果1");
            b.setCreatorName("Admin");
            b.setId(a.getId());
            res=update(dao,b);
            Log.i("sql","测试6:id错误，DishName和CreatorName字段正确，其它所有字段为null res="+res);

        }catch (Exception e){
            Log.e("sql","e",e);
        }
    }
    */
}