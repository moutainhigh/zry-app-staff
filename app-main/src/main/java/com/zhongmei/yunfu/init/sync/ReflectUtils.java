package com.zhongmei.yunfu.init.sync;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;


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
        if (type instanceof ParameterizedType) {             return getGenericClass((ParameterizedType) type, i);
        } else if (type instanceof TypeVariable) {
            return getClass(((TypeVariable) type).getBounds()[0], 0);         } else {            return (Class) type;
        }
    }

    private static Class getGenericClass(ParameterizedType parameterizedType, int i) {
        Object genericClass = parameterizedType.getActualTypeArguments()[i];
        if (genericClass instanceof ParameterizedType) {             return (Class) ((ParameterizedType) genericClass).getRawType();
        } else if (genericClass instanceof GenericArrayType) {             return (Class) ((GenericArrayType) genericClass).getGenericComponentType();
        } else if (genericClass instanceof TypeVariable) {             return getClass(((TypeVariable) genericClass).getBounds()[0], 0);
        } else {
            return (Class) genericClass;
        }
    }
}
