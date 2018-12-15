package com.zhongmei.analysis.store;

import android.content.Context;

import com.zhongmei.analysis.DataHandleListener;

/**
 * Created by demo on 2018/12/15
 */
public interface IDataStore<T1, T2> {
    /**
     * 存储数据
     *
     * @param context
     * @param data
     */
    void realStoreData(Context context, T1 data);

    /**
     * 删除数据
     *
     * @param context
     * @param dataFormat
     */
    void deleteStoreData(Context context, T2 dataFormat);

    /**
     * 强制将所有缓存数据写入磁盘或上传
     *
     * @param context
     * @param listener
     */
    void writeAllData(Context context, DataHandleListener listener);
}
