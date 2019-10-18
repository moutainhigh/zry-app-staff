package com.zhongmei.yunfu.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class EntityBase<ID> implements IEntity<ID> {


    private static final long serialVersionUID = 1L;


    private boolean changed = false;


    public boolean isChanged() {
        return changed;
    }


    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public void validateCreate() {
        setChanged(true);
        if (this instanceof ICreator) {
            AuthUser user = Session.getAuthUser();
            if (user != null) {
                ICreator creator = (ICreator) this;
                creator.setCreatorId(user.getId());
                creator.setCreatorName(user.getName());
            }
        }
        validateUpdate();
    }

    public void validateUpdate() {
        setChanged(true);
        if (this instanceof IUpdator) {
            AuthUser user = Session.getAuthUser();
            if (user != null) {
                IUpdator updator = (IUpdator) this;
                updator.setUpdatorId(user.getId());
                updator.setUpdatorName(user.getName());
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pkValue() == null) ? 0 : pkValue().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IEntity<?> other = (IEntity<?>) obj;
        if (pkValue() == null) {
            if (other.pkValue() != null)
                return false;
        } else if (!pkValue().equals(other.pkValue()))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pkValue=" + pkValue() + "]";
    }

    public static boolean isChanged(EntityBase<?> entity) {
        return entity != null && entity.isChanged();
    }

    @Override
    public void checkNullValue() {
        final String tableName = getTableName();
        final String idName = getIdName();
        eachDbField(this, new FieldCallback() {
            @Override
            public void onCall(Field field, Object value, DatabaseField dbField) {
                if (!dbField.canBeNull()) {
                    if (value == null) {
                        throw new DbCheckNullException(String.format("%s[%s][%s=%s]: %s is null", EntityBase.this.getClass().getSimpleName(), tableName, idName, pkValue(), field.getName()));
                    }
                }
            }
        });
    }

    @Override
    public void checkNullIDValue() {
        final String tableName = getTableName();
        eachDbField(this, new FieldCallback() {
            @Override
            public void onCall(Field field, Object value, DatabaseField dbField) {
                if (dbField.id() && !dbField.canBeNull()) {
                    String idName = dbField.columnName();
                    if (value == null) {
                        throw new DbCheckNullException(String.format("%s[%s][%s=%s]: %s is null", EntityBase.this.getClass().getSimpleName(), tableName, idName, pkValue(), field.getName()));
                    }
                }
            }
        });
    }

    private String getTableName() {
        String tableName = getClass().getSimpleName();
        DatabaseTable databaseTable = getClass().getAnnotation(DatabaseTable.class);
        if (databaseTable != null) {
            tableName = databaseTable.tableName();
        }
        return tableName;
    }

    private String getIdName() {
        Class<?> clazz = getClass();
        List<Field> fields = getAllDeclaredFields(clazz);
        for (Field field : fields) {
            DatabaseField databaseField = field.getAnnotation(DatabaseField.class);
            if (databaseField != null && databaseField.id()) {
                return databaseField.columnName();
            }
        }
        return null;
    }

    @Deprecated
    public boolean checkNonNull() {
        return true;
    }

    public static void eachDbField(Object target, FieldCallback callback) {
        Class<?> clazz = target.getClass();
        List<Field> fields = getAllDeclaredFields(clazz);
        for (Field field : fields) {
            DatabaseField databaseField = field.getAnnotation(DatabaseField.class);
            if (databaseField != null) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(target);
                    if (callback != null) {
                        callback.onCall(field, value, databaseField);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<Field> getAllDeclaredFields(Class<?> clazz) {
        List<Field> result = new ArrayList<>();
        if (clazz != null && clazz != Object.class) {
            result.addAll(getAllDeclaredFields(clazz.getSuperclass()));
            result.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }
        return result;
    }

    public static boolean checkNonNull(Object... array) {
        if (array != null) {
            for (Object object : array) {
                if (object == null) {
                    return false;
                }
            }
        }
        return true;
    }

    interface FieldCallback {
        void onCall(Field field, Object value, DatabaseField dbField);
    }
}
