package com.zhongmei.analysis.store;

import android.content.Context;

import com.zhongmei.analysis.DataHandleListener;


public interface IDataStore<T1, T2> {

    void realStoreData(Context context, T1 data);


    void deleteStoreData(Context context, T2 dataFormat);


    void writeAllData(Context context, DataHandleListener listener);
}
