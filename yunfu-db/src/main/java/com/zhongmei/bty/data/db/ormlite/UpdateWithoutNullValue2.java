package com.zhongmei.bty.data.db.ormlite;


import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 实体所有的字段都是Object类型，如果实体的字段值为null，那么在update的时候将不更新改字段。它不同于BaseDaoImpl的update函数更新所有字段
 * id相等作为更新条件
 * 此为方案2
 */
public class UpdateWithoutNullValue2 {

    public static <T> int update(Dao dao, T data) throws Exception {
        if (dao == null || data == null) {
            return 0;
        }
        UpdateBuilder builder = dao.updateBuilder();
        FieldType[] fieldTypes = extractFieldTypes(dao.getConnectionSource(), dao.getDataClass());
        FieldType idField = null;
        boolean hasFieldToSet = false;
        for (FieldType fieldType : fieldTypes) {
            if (idField == null && fieldType.isId()) {
                idField = fieldType;
            }
            Object fieldValue = fieldType.extractRawJavaFieldValue(data);
            if (isFieldUpdatable(fieldType, fieldValue)) {
                builder.updateColumnValue(fieldType.getColumnName(), fieldValue);
                hasFieldToSet = true;
            }
        }
        if (idField == null) {
            Log.w("sql=", "no id cloum");
            return 0;
        }
        if (!hasFieldToSet) {
            Log.w("sql=", "no cloum set");
            return 0;
        }
        Object idfieldValue = idField.extractRawJavaFieldValue(data);
        if (idfieldValue == null) {
            Log.w("sql=", "no id value");
            return 0;
        }
        builder.where().eq(idField.getColumnName(), idfieldValue);
        Log.i("sql=", builder.prepare().getStatement());
        return builder.update();
    }

    private static <T> FieldType[] extractFieldTypes(ConnectionSource connectionSource, Class<T> clazz) throws SQLException {
        String tableName = DatabaseTableConfig.extractTableName(clazz);
        if (connectionSource.getDatabaseType().isEntityNamesMustBeUpCase()) {
            tableName = tableName.toUpperCase();
        }

        ArrayList fieldTypes = new ArrayList();

        for (Class classWalk = clazz; classWalk != null; classWalk = classWalk.getSuperclass()) {
            Field[] arr$ = classWalk.getDeclaredFields();
            int len$ = arr$.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                Field field = arr$[i$];
                FieldType fieldType = FieldType.createFieldType(connectionSource, tableName, field, clazz);
                if (fieldType != null) {
                    fieldTypes.add(fieldType);
                }
            }
        }

        if (fieldTypes.isEmpty()) {
            throw new IllegalArgumentException("No fields have a " + DatabaseField.class.getSimpleName() + " annotation in " + clazz);
        } else {
            return (FieldType[]) fieldTypes.toArray(new FieldType[fieldTypes.size()]);
        }
    }

    private static <T> boolean isFieldUpdatable(FieldType fieldType, T fieldValue) throws SQLException {
        if (fieldType.isId() || fieldType.isForeignCollection() || fieldType.isReadOnly() || fieldValue == null) {
            return false;
        } else {
            return true;
        }
    }

    /*
    public static void test() {
        DatabaseHelper helper = DBHelper.getHelper();
        try {

            Log.i("sql","方案2测试－－－－－－－－－－－－－－");

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