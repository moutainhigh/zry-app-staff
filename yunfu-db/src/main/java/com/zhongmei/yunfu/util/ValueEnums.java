package com.zhongmei.yunfu.util;


/**
 * @version: 1.0
 * @date 2016年3月23日
 */
public class ValueEnums {

    /**
     * 如果指定的值与枚举值相同就返回true
     *
     * @param valueEnum
     * @param value
     * @return
     */
    public static <T> boolean equalsValue(ValueEnum<T> valueEnum, T value) {
        Checks.verifyNotNull(valueEnum, "valueEnum");
        if (valueEnum.value() == null && value == null) {
            return true;
        }
        if ((valueEnum.value() == null && value != null)
                || (valueEnum.value() != null && value == null)) {
            return false;
        }
        return valueEnum.value().equals(value);
    }

    /**
     * 获取枚举的值
     *
     * @param valueEnum
     * @return
     */
    public static <T, E extends Enum<E> & ValueEnum<T>> T toValue(E valueEnum) {
        return valueEnum == null ? null : valueEnum.value();
    }

    /**
     * 将值转为指定类型的枚举
     *
     * @param classType
     * @param value
     * @return
     */
    public static <T, E extends Enum<E> & ValueEnum<T>> E toEnum(Class<E> classType, T value) {
        ValueEnum<T> unknownEnum = null;
        E unknown = null;
        for (E e : classType.getEnumConstants()) {
            if (e.equalsValue(value)) {
                return e;
            } else if (unknown == null) {
                ValueEnum<T> ve = (ValueEnum<T>) e;
                if (ve.isUnknownEnum()) {
                    unknownEnum = ve;
                    unknown = e;
                    continue;
                }
            }
        }
        if (unknown != null) {
            unknownEnum.setUnknownValue(value);
            return unknown;
        }
        return null;
    }

}
