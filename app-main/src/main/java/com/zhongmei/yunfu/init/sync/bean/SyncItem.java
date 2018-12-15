package com.zhongmei.yunfu.init.sync.bean;

import com.zhongmei.yunfu.context.util.NoProGuard;

import java.io.Serializable;
import java.util.List;

/**
 * 与同步Request和Response中content中的某一项对应
 *
 * @param <T>
 * @version: 1.0
 * @date 2015年4月15日
 */
public class SyncItem<T> implements NoProGuard, Serializable {

    private String lastSyncMarker;

    private List<T> datas;

    public String getLastSyncMarker() {
        return lastSyncMarker;
    }

    public void setLastSyncMarker(String lastSyncMarker) {
        this.lastSyncMarker = lastSyncMarker;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public static <T> SyncItem<T> create(String lastSyncMarker) {
        SyncItem<T> syncItem = new SyncItem<>();
        syncItem.setLastSyncMarker(lastSyncMarker);
        return syncItem;
    }
}
