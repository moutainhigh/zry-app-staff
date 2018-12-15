/*
package com.zhongmei.bty.baseservice.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ReflectHelper {

    static HashMap<CacheKey, LinkedHashMap<String, Field>> sfiledCacheHashMap = new HashMap<CacheKey, LinkedHashMap<String, Field>>();

    static class CacheKey {
        Class<?> cls;
        String suffix;

        public CacheKey(Class<?> cls, String suffix) {
            this.cls = cls;
            this.suffix = suffix;
        }

        @Override
        public int hashCode() {
            return (cls.getClass().getName() + suffix).hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof CacheKey) {
                CacheKey that = (CacheKey) o;
                if (this == o) {
                    return true;
                }
                if (that.cls == null || this.cls == null || this.suffix == null
                        || that.suffix == null) {
                    return false;
                }
                if (this.cls.getName().equals(that.cls.getName())
                        && this.suffix.equals(that.suffix)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static LinkedHashMap<String, Field> getStaticFields(Class<?> cls, String suffix) {
        CacheKey cacheKey = new CacheKey(cls, suffix);
        if (sfiledCacheHashMap.containsKey(cacheKey)) {
            return sfiledCacheHashMap.get(cacheKey);
        }

        LinkedHashMap<String, Field> retFields = new LinkedHashMap<String, Field>();
        Field[] fields = cls.getFields();
        for (Field field : fields) {
            try {
                if (field.get(cls) != null && field.getName().endsWith(suffix)) {
                    retFields.put((String) field.get(cls), field);
                }
            } catch (Exception e) {
            }
        }
        sfiledCacheHashMap.put(cacheKey, retFields);
        return retFields;
    }

    public static Field getStaticFirstField(Class<?> cls, String suffix) {

        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (field.get(cls) != null && field.getName().endsWith(suffix)) {
                    return field;
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static Object newInstance(String className) {
        try {
            Class<?> cls = Class.forName(className);
            return cls.newInstance();
        } catch (Exception e) {
        }
        return null;
    }

    public static Object invoke(Object instance, String methodName) {
        try {
            Method method = instance.getClass().getMethod(methodName);
            return method.invoke(instance);
        } catch (Exception e) {
        }
        return null;
    }
}
*/
