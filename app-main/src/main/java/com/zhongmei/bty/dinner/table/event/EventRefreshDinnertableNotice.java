package com.zhongmei.bty.dinner.table.event;


public class EventRefreshDinnertableNotice {
    private int mEmptyTableCount = 0;

    public EventRefreshDinnertableNotice(int emptyTableCount) {
        this.mEmptyTableCount = emptyTableCount;
    }

    public int getmEmptyTableCount() {
        return mEmptyTableCount;
    }

}
