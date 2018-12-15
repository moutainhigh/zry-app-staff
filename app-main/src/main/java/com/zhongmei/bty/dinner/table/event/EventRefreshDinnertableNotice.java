package com.zhongmei.bty.dinner.table.event;

/**
 * @version: 1.0
 * @date 2015年9月21日
 */
public class EventRefreshDinnertableNotice {
    private int mEmptyTableCount = 0;

    public EventRefreshDinnertableNotice(int emptyTableCount) {
        this.mEmptyTableCount = emptyTableCount;
    }

    public int getmEmptyTableCount() {
        return mEmptyTableCount;
    }

}
