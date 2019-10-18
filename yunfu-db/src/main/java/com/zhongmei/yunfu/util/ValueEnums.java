package com.zhongmei.yunfu.util;



public class ValueEnums {


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


    public static <T, E extends Enum<E> & ValueEnum<T>> T toValue(E valueEnum) {
        return valueEnum == null ? null : valueEnum.value();
    }


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
