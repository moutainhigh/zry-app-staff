package com.zhongmei.bty.offline.backup.backup4qs;

import com.zhongmei.bty.offline.backup.core.common.Entity;

import java.util.List;

public interface IQSBackup {

    boolean isUploadFinish();

    void backup(Object object);

    void registerCallback(ITransporter.Callback callback);

    void unRegisterCallback(ITransporter.Callback callback);

}
