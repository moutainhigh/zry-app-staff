package com.zhongmei.yunfu.util;

import java.io.Serializable;

/**
 * @param <T>
 */
public interface ValueEnum<T> {

    T value();

    boolean equalsValue(T value);

    boolean isUnknownEnum();

    void setUnknownValue(T value);

    /**
     * @param <T>
     * @param <E>
     */
    final class Helper<T> implements Serializable {

        public static <T> Helper<T> valueHelper(T value) {
            return new Helper<T>(value);
        }

        public static <T> Helper<T> unknownHelper() {
            return new Helper<T>();
        }

        private final T value;
        private final boolean unknown;
        private T unknownValue;

        private Helper(T value) {
            this.value = value;
            this.unknown = false;
        }

        private Helper() {
            this.value = null;
            this.unknown = true;
        }

        public T value() {
            return unknown ? unknownValue : value;
        }

        public <E extends Enum<E> & ValueEnum<T>> boolean equalsValue(E valueEnum, T value) {
            return ValueEnums.equalsValue(valueEnum, value);
        }

        public boolean isUnknownEnum() {
            return unknown;
        }

        public void setUnknownValue(T value) {
            if (unknown) {
                this.unknownValue = value;
            } else {
                throw new UnsupportedOperationException("Must be unknown enum.");
            }
        }

    }

}
