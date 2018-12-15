package com.zhongmei.bty.entity.enums;

import com.zhongmei.yunfu.util.ValueEnum;

/**
 * @Date： 2017/3/2
 * @Description:库存退回选择样式
 * @Version: 1.0
 */
public enum InventoryShowType implements ValueEnum<Integer> {

    /**
     * 只展示库存退回选择
     */
    ONLY_SHOW_INVENTORY(1),

    /**
     * 库存退回与其它内容一起展示
     */
    SHOW_OTHER_DATA(2),


    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
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
