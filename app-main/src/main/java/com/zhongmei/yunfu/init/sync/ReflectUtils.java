package com.zhongmei.yunfu.init.sync;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

/**
 * Desc
 *
 * @created 2017/9/25
 */
public class ReflectUtils {

    public interface FieldCall {

        void onCall(Field field);
    }

    static final Map<Class, Field[]> CLASS_MAP = new HashMap<>();

    public static void callFields(Class<?> clazz, FieldCall call) {
        callFields(clazz, call, false);
    }

    public static void callFields(Class<?> clazz, FieldCall call, boolean cache) {
        Field[] fields = CLASS_MAP.get(clazz);
        if (fields == null) {
            fields = clazz.getDeclaredFields();
            if (cache) {
                CLASS_MAP.put(clazz, fields);
            }
        }
        if (fields != null) {
            for (Field field : fields) {
                if (call != null) {
                    call.onCall(field);
                }
            }
        }
    }

    public static Class getClass(Type type) {
        return getClass(type, 0);
    }

    public static Class getClass(Type type, int i) {
        if (type instanceof ParameterizedType) { // 处理泛型类型
            return getGenericClass((ParameterizedType) type, i);
        } else if (type instanceof TypeVariable) {
            return getClass(((TypeVariable) type).getBounds()[0], 0); // 处理泛型擦拭对象
        } else {// class本身也是type，强制转型
            return (Class) type;
        }
    }

    private static Class getGenericClass(ParameterizedType parameterizedType, int i) {
        Object genericClass = parameterizedType.getActualTypeArguments()[i];
        if (genericClass instanceof ParameterizedType) { // 处理多级泛型
            return (Class) ((ParameterizedType) genericClass).getRawType();
        } else if (genericClass instanceof GenericArrayType) { // 处理数组泛型
            return (Class) ((GenericArrayType) genericClass).getGenericComponentType();
        } else if (genericClass instanceof TypeVariable) { // 处理泛型擦拭对象
            return getClass(((TypeVariable) genericClass).getBounds()[0], 0);
        } else {
            return (Class) genericClass;
        }
    }
}
