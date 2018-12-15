package com.zhongmei.yunfu.context.data;

public enum Biz {

    DINNER(1),
    SNACK(2);

    int value;

    Biz(int value) {
        this.value = value;
    }

    public static Biz valueOf(int value) {
        for (Biz biz : Biz.values()) {
            if (value == biz.value) {
                return biz;
            }
        }
        return SNACK;
    }
}
