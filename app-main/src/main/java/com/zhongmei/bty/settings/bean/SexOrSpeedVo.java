package com.zhongmei.bty.settings.bean;

/**
 *

 *
 */
public class SexOrSpeedVo {

    private int value;

    private String name;

    // 0:未选中 1：选中
    private boolean selectedState;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelectedState() {
        return selectedState;
    }

    public void setSelectedState(boolean selectedState) {
        this.selectedState = selectedState;
    }
}
