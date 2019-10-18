package com.zhongmei.yunfu.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;


@SuppressLint("DefaultLocale")
public final class Beans {

    private Beans() {
    }

    private static final String GET_PREFIX = "get";
    private static final String SET_PREFIX = "set";
    private static final String IS_PREFIX = "is";


    public static String capitalize(String name) {
        if (name == null) {
            return name;
        } else if (name.length() > 1 && Character.isUpperCase(name.charAt(1))) {
            return name;
        } else if (name.length() > 1) {
            return Character.toUpperCase(name.charAt(0)) + name.substring(1);
        } else {
            return name.toUpperCase();
        }
    }


    public static String uncapitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) &&
                Character.isUpperCase(name.charAt(0))) {
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    public static boolean isAccessorPresent(String prefix, String property, Class<?> bean) {
        try {
            bean.getMethod(prefix + capitalize(property));
            return true;
        } catch (NoSuchMethodException ex) {
            return false;
        }
    }

    public static Method getAccessor(String prefix, String property, Class<?> bean) {
        try {
            return bean.getMethod(prefix + capitalize(property));
        } catch (NoSuchMethodException e) {
            return null;
        }
    }


    public static void setPropertyValue(Object bean, String propertyName, Object value) throws Exception {
        bean = Checks.verifyNotNull(bean, "bean");
        propertyName = Checks.verifyNotNull(propertyName, "propertyName");
        Class<?> classType = bean.getClass();
        Class<?> paramType = null;
        if (value != null) {
            paramType = value.getClass();
        } else {
            Method getter = findGetter(classType, propertyName);
            if (getter == null) {
                throw new NoSuchMethodException("set" + capitalize(propertyName) + "(Void)");
            }
            paramType = getter.getReturnType();
        }
        Method setter = findSetter(classType, propertyName, paramType);
        if (setter == null) {
            throw new NoSuchMethodException("set" + capitalize(propertyName) + "(" + paramType + ")");
        }
        setter.invoke(bean, value);
    }


    public static Object getPropertyValue(Object bean, String propertyName) throws Exception {
        bean = Checks.verifyNotNull(bean, "bean");
        propertyName = Checks.verifyNotNull(propertyName, "propertyName");
        Class<?> classType = bean.getClass();
        Method method = findGetter(classType, propertyName);
        if (method == null) {
            throw new NoSuchMethodException();
        }
        return method.invoke(bean, (Object[]) null);
    }


    public static Map<String, Object> getPropertiesValues(Object bean) {
        bean = Checks.verifyNotNull(bean, "bean");
        HashMap<String, Object> result = new HashMap<String, Object>();
        Class<?> clazz = bean.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            String fieldName = null;
            String methodName = method.getName();
            if (methodName.startsWith(GET_PREFIX)) {
                fieldName = methodName.substring(3);
            } else if (methodName.startsWith(IS_PREFIX) && boolean.class != method.getReturnType()) {
                fieldName = methodName.substring(2);
            }
            if (fieldName != null) {
                fieldName = uncapitalize(fieldName);
                Field field = findField(clazz, fieldName);
                if (field != null) {
                    try {
                        Object value = method.invoke(bean, (Object[]) null);
                        result.put(fieldName, value);
                    } catch (Exception e) {
                    }
                }
            }
        }
        return result;
    }


    public static void copyProperties(Object source, Object target, String... ignoreProperties)
            throws Exception {
        source = Checks.verifyNotNull(source, "source");
        target = Checks.verifyNotNull(target, "target");
        Set<String> ignores = new HashSet<String>();
        if (ignoreProperties != null) {
            for (String ignore : ignoreProperties) {
                ignores.add(ignore);
            }
        }
        HashMap<String, Method> targetMethodMap = new HashMap<String, Method>();
        Class<?> targetClass = target.getClass();
        for (Method method : targetClass.getMethods()) {
            if (method.getParameterTypes().length != 1 || Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            String methodName = method.getName();
            if (methodName.startsWith(SET_PREFIX)) {
                String fieldName = methodName.substring(3);
                if (!ignores.contains(uncapitalize(fieldName))) {
                    targetMethodMap.put(fieldName, method);
                }
            }
        }
        Class<?> sourceClass = source.getClass();
        for (Method method : sourceClass.getMethods()) {
            if (method.getParameterTypes().length != 0 || Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            String fieldName = null;
            String methodName = method.getName();
            if (methodName.startsWith(GET_PREFIX)) {
                fieldName = methodName.substring(3);
            } else if (methodName.startsWith(IS_PREFIX)) {
                fieldName = methodName.substring(2);
            }
            if (fieldName != null) {
                Method setter = targetMethodMap.get(fieldName);
                if (setter != null) {
                    try {
                        Object value = method.invoke(source);
                        setter.invoke(target, value);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        throw new IllegalArgumentException("Could not copy property from source to target:"
                                + uncapitalize(fieldName) + " of " + setter, ex);
                    }
                }
            }
        }
    }


    public static <T> T copyEntity(T source, T target) throws Exception {
        Beans.copyProperties(source, target);
        return target;
    }


    private static Method findGetter(Class<?> classType, String propertyName) {
        propertyName = capitalize(propertyName);
        Method method = null;
        try {
            method = classType.getMethod(GET_PREFIX + propertyName, (Class[]) null);
        } catch (NoSuchMethodException ex) {
        }
        if (method == null) {
            try {
                method = classType.getMethod(IS_PREFIX + propertyName, (Class[]) null);
                Class<?> returnType = method.getReturnType();
                if (boolean.class != returnType) {
                    method = null;
                }
            } catch (NoSuchMethodException ex) {
            }
        }
        return method;
    }


    private static Method findSetter(Class<?> classType, String propertyName, Class<?> paramType) {
        propertyName = capitalize(propertyName);
        Method method = null;
        try {
            method = classType.getMethod(SET_PREFIX + propertyName, paramType);
        } catch (NoSuchMethodException ex) {
        }
        return method;
    }

    private static Field findField(Class<?> clazz, String fieldName) {
        if (clazz == Object.class) {
            return null;
        }
        try {
            Field field = clazz.getDeclaredField(fieldName);
            if (field != null) {
                return field;
            }
        } catch (Exception ex) {
        }
        return findField(clazz.getSuperclass(), fieldName);
    }


    public static PropertiesComparator comparator(Object value) {
        return new PropertiesComparator(value);
    }


    public static class PropertiesComparator {

        private final Object value;

        public PropertiesComparator(Object value) {
            Checks.verifyNotNull(value, "value");
            this.value = value;
        }


        public boolean eq(Object object, String... ignoreProperties) {
            if (object == null) {
                return false;
            }
            Set<String> ignores = new HashSet<String>();
            if (ignoreProperties != null) {
                for (String ignore : ignoreProperties) {
                    ignores.add(ignore);
                }
            }
            Map<String, Object> thisMap = getPropertiesValues(value);
            Map<String, Object> objectMap = getPropertiesValues(object);
            for (Map.Entry<String, Object> entry : thisMap.entrySet()) {
                String propertyName = entry.getKey();
                if (ignores.contains(propertyName)) {
                    continue;
                }
                Object v1 = entry.getValue();
                if (objectMap.containsKey(propertyName)) {
                    Object v2 = objectMap.get(propertyName);
                    if (v1 == null) {
                        if (v2 == null) {
                            continue;
                        }
                    } else if (v1.equals(v2)) {
                        continue;
                    }
                }
                return false;
            }
            return true;
        }

    }

}
