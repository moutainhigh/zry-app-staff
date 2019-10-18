package com.zhongmei.yunfu.init.sync;

import com.zhongmei.yunfu.init.sync.bean.SyncContent;
import com.zhongmei.yunfu.init.sync.bean.SyncItem;
import com.zhongmei.yunfu.init.sync.bean.SyncModule;
import com.zhongmei.yunfu.init.sync.bean.SyncResponse;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;


public class SyncSession {

    private boolean mSyncSuccess;

    public SyncSession() {
    }

    public boolean isSyncSuccess() {
        return mSyncSuccess;
    }

    public void commit(SyncModule syncModule, List<String> modules, Map<String, String> syncMarkMap, SyncResponse syncResponse) throws Exception {
        boolean isInit = SyncServiceUtil.isNeedInit();
                SyncDatabaseOps.getInstance().save(syncModule, modules, syncMarkMap, syncResponse, isInit);
        if (syncResponse.isSyncSuccess()) {
            mSyncSuccess = true;
        }
    }



    private void cleanModuleContent(final SyncContent syncContent, final SyncContentFieldCall call, final Class<?>... itemClazz) {
        SyncContent.callSyncContentFields(SyncContent.class, new SyncContent.SyncContentFieldCall() {
            @Override
            public void onCall(Field field, Class<?> genericClazz) {
                if (isFilterClazz(genericClazz, itemClazz)) {
                    try {
                        field.setAccessible(true);
                        SyncItem<?> syncItem = (SyncItem<?>) field.get(syncContent);
                        if (call != null) {
                            call.onCall(field, syncItem, genericClazz);
                        }

                        field.set(syncContent, null);
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    private boolean isFilterClazz(Class<?> genericClazz, Class<?>... itemClazz) {
        for (Class<?> clazz : itemClazz) {
            if (clazz == genericClazz) {
                return true;
            }
        }
        return false;
    }

    interface SyncContentFieldCall {

        void onCall(Field field, SyncItem<?> syncItem, Class<?> contentClazz) throws Exception;
    }
}
