package com.zhongmei.bty.entity.enums;

import com.zhongmei.yunfu.util.ValueEnum;


public enum InventoryShowType implements ValueEnum<Integer> {


    ONLY_SHOW_INVENTORY(1),


    SHOW_OTHER_DATA(2),



    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;

    private InventoryShowType(Integer value) {
        helper = Helper.valueHelper(value);
    }

    private InventoryShowType() {
        helper = Helper.unknownHelper();
    }

    @Override
    public Integer value() {
        return helper.value();
    }

    @Override
    public boolean equalsValue(Integer value) {
        return helper.equalsValue(this, value);
    }

    @Override
    public boolean isUnknownEnum() {
        return helper.isUnknownEnum();
    }

    @Override
    public void setUnknownValue(Integer value) {
        helper.setUnknownValue(value);
    }

    @Override
    public String toString() {
        return "" + value();
    }

    public static InventoryShowType newShowType(int type) {
        for (InventoryShowType inventoryShowType : InventoryShowType.values()) {
            if (inventoryShowType.value() == type) {
                return inventoryShowType;
            }
        }
        return __UNKNOWN__;
    }
}
