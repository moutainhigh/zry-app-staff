package com.zhongmei.bty.offline.backup.backup4qs;

import com.zhongmei.bty.offline.backup.core.common.Entity;

import java.util.List;

public interface ITransporter {

    interface Callback {
        void onUploading(List<Entity> entities);

        void onUploadSuccess(List<Entity> entities);

        void onUploadError(List<Entity> entities, String message);
    }
}
