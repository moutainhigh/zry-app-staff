package com.zhongmei.bty.snack.event;

/**
 * 等叫起菜、选中菜品时事件
 * Created by demo on 2018/12/15
 */

public class EventChoice {
    private String count;
    private boolean isCheckAll;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public boolean isCheckAll() {
        return isCheckAll;
    }

    public void setCheckAll(boolean checkAll) {
        isCheckAll = checkAll;
    }
}
