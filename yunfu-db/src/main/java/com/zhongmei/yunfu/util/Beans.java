package com.zhongmei.yunfu.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;

/**
 *

 *
 */
@SuppressLint("DefaultLocale")
public final class Beans {

    private Beans() {
    }

    private static final String GET_PREFIX = "get";
    private static final String SET_PREFIX = "set";
    private static final String IS_PREFIX = "is";

    /**
     * 首字母大写
     *
     * @param name
     * @return
     */
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

    /**
     * 首字母小写
     *
     * @param name
     * @return
     */
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

    /**
     * 设置bean对象的可写属性值，相当于调用属性对应的setter方法
     *
     * @param bean
     * @param propertyName
     * @param value
     * @throws Exception
     */
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

    /**
     * 获取bean对象的可读属性值，相当于调用属性对应的getter方法
     *
     * @param bean
     * @param propertyName 属性名称
     * @return
     * @throws NoSuchMethodException
     */
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

    /**
     * 返回包含指定对象的所有可读属性及值的Map，属性名称为key，属性值为value。
     * 返回的Map中只包含有getter方法的属性。
     *
     * @param bean
     * @return
     */
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

    /***
     * 将一个对象的属性值赋给另一个对象的相同的属性，这两个对象必须都符合JavaBean的标准
     *
     * @param source
     *            要赋值的源对象
     * @param target
     *            被赋值的目标对象
     * @param ignoreProperties
     *            被忽略赋值的属性数组
     * @throws Exception
     *
     */
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

    /**
     * @param source
     * @param target
     * @return
     * @throws Exception
     */
    public static <T> T copyEntity(T source, T target) throws Exception {
        Beans.copyProperties(source, target);
        return target;
    }

    /**
     * 返回指定属性的getter方法
     *
     * @param classType
     * @param propertyName
     * @return
     */
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

    /**
     * 返回指定属性的setter方法
     *
     * @param classType
     * @param propertyName
     * @param paramType
     * @return
     */
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

    /**
     * 创建一个用于比较两对象的getter方法返回值是否相等的比较器
     *
     * @param value 被比较的对象
     * @return
     */
    public static PropertiesComparator comparator(Object value) {
        return new PropertiesComparator(value);
    }

    /**
     * 用于对比两个对象中的getter方法名称与返回值是否都是相等的
     *
     * @version: 1.0
     * @date 2015年10月16日
     */
    public static class PropertiesComparator {

        private final Object value;

        public PropertiesComparator(Object value) {
            Checks.verifyNotNull(value, "value");
            this.value = value;
        }

        /**
         * 如果value与object中的getter方法返回值完全相等就返回true，否则返回false
         *
         * @param object           用来与value对比的对象
         * @param ignoreProperties 忽略的属性名称
         * @return
         */
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
